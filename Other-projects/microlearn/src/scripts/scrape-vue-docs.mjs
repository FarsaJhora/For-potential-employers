// scripts/scrape-vue-docs.mjs
// ------------------------------------------------------
// Vue.js docs -> microlearning JSON (flashcards + quiz)
// ------------------------------------------------------

import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url'
import simpleGit from 'simple-git'
import glob from 'glob'
import matter from 'gray-matter'
import { marked } from 'marked'
import slugify from 'slugify'

// -----------------------
// Konfigurace
// -----------------------
const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const REPO_URL = process.env.VUE_DOCS_REPO || 'https://github.com/vuejs/docs.git'
const CACHE_DIR = path.resolve(__dirname, '../.cache/vue-docs')
const DOCS_SUBDIR = process.env.DOCS_SUBDIR || 'src/guide'   // kde jsou .md
const OUTPUT_FILE = path.resolve(__dirname, '../public/vue-lessons.json')

// Filtry souborů – kdyby bylo potřeba něco vynechat, upravili by jsme zde:
const EXCLUDE_PATTERNS = [
    '**/README.md',
    '**/index.md',
    '**/translations.md',
    '**/migration/**',     // volitelně vynecháme migrační průvodce
    '**/about/**'
]

// Limitace generování
const MAX_PARAGRAPHS_PER_CARD = 2
const MAX_BACK_CHARS = 420
const MAX_QUIZ_PER_LESSON = 5

// -----------------------
// Utility
// -----------------------
function ensureDirSync(dir) {
    if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true })
}

function toSlug(str) {
    return slugify(str, { lower: true, strict: true })
}

function trimAndSingleLine(str) {
    return str.replace(/\s+/g, ' ').trim()
}

function firstSentence(str) {
    const m = str.match(/(.+?[.!?])(\s|$)/)
    return m ? m[1] : str
}

// -----------------------
// 1) Klon nebo pull repo
// -----------------------
async function prepareRepo() {
    ensureDirSync(CACHE_DIR)
    const git = simpleGit()

    if (!fs.existsSync(path.join(CACHE_DIR, '.git'))) {
        console.log('⏬ Klonuji Vue docs repo (shallow)...')
        await git.clone(REPO_URL, CACHE_DIR, ['--depth', '1'])
    } else {
        console.log('🔄 Aktualizuji Vue docs repo...')
        const repo = simpleGit(CACHE_DIR)
        await repo.fetch()
        await repo.reset(['--hard', 'origin/main'])
        await repo.pull('origin', 'main')
    }
}

// -----------------------
// 2) Najdi Markdown soubory
// -----------------------
function findGuideMarkdown() {
    const base = path.join(CACHE_DIR, DOCS_SUBDIR)
    const files = glob.sync('**/*.md', { cwd: base, nodir: true, absolute: true })
    const excluded = EXCLUDE_PATTERNS.flatMap(pattern =>
        glob.sync(pattern, { cwd: base, nodir: true, absolute: true })
    )
    const excludeSet = new Set(excluded)
    const filtered = files.filter(f => !excludeSet.has(f))
    console.log(`📄 Nalezeno MD souborů: ${filtered.length}`)
    return filtered
}

// -----------------------
// 3) Markdown -> karty
// -----------------------
function extractCardsFromMarkdown(md) {
    const { content } = matter(md)
    const tokens = marked.lexer(content)

    const cards = []
    let currentHeading = null
    let buffer = []

    const pushCard = () => {
        if (!currentHeading) return
        const backText = trimAndSingleLine(buffer.join(' '))
            .slice(0, MAX_BACK_CHARS)
        if (backText.length < 30) { // příliš krátké -> vynech
            buffer = []
            return
        }
        cards.push({
            id: toSlug(currentHeading),
            front: currentHeading,
            back: backText
        })
        buffer = []
    }

    for (const t of tokens) {
        if (t.type === 'heading' && (t.depth === 2 || t.depth === 3)) {
            // ukonči předchozí kartu
            pushCard()
            currentHeading = t.text
        } else if (t.type === 'paragraph' && currentHeading) {
            if (buffer.length < MAX_PARAGRAPHS_PER_CARD) {
                buffer.push(t.text)
            }
        } else if (t.type === 'code' && currentHeading) {
            // z kódu uděláme jednovětné shrnutí – decentně
            if (buffer.length < MAX_PARAGRAPHS_PER_CARD) {
                buffer.push(`Ukázka kódu (${t.lang || 'code'}).`)
            }
        } else if (t.type === 'list' && currentHeading) {
            const items = t.items?.map(i => trimAndSingleLine(i.text)).filter(Boolean) || []
            if (items.length) buffer.push(`Klíčové body: ${items.slice(0, 3).join('; ')}.`)
        }
    }
    // poslední karta
    pushCard()

    return cards
}

// -----------------------
// 4) Karty -> kvíz
// -----------------------
function makeQuizFromCards(cards) {
    if (!cards.length) return []

    // Vezmeme až MAX_QUIZ_PER_LESSON karet pro kvíz, zbytek použijeme jako zdroj distraktorů
    const base = cards.slice(0, MAX_QUIZ_PER_LESSON)
    const others = cards.slice(MAX_QUIZ_PER_LESSON)

    const distractorPool = [...base, ...others].map(c => firstSentence(c.back)).filter(Boolean)

    const quiz = base.map((c) => {
        const correct = firstSentence(c.back)
        // vyberou se 2–3 distraktory, co nejsou duplicitní a nejsou stejné jako správná odpověď
        const d = []
        for (const s of distractorPool) {
            if (d.length >= 2) break
            if (!s || s === correct) continue
            if (!d.includes(s)) d.push(s)
        }
        // kdyby bylo málo, šlo by přidat generické
        while (d.length < 2) d.push('Toto tvrzení neodpovídá sekci.')
        const options = [correct, ...d].slice(0, 3)

        // náhodně promíchat
        for (let i = options.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1))
            ;[options[i], options[j]] = [options[j], options[i]]
        }
        const answer = options.indexOf(correct)

        return {
            q: `Co nejlépe vystihuje „${c.front}“?`,
            options,
            answer
        }
    })

    return quiz
}

// -----------------------
// 5) Soubor -> lekce
// -----------------------
function fileToLesson(absPath, repoBase) {
    const md = fs.readFileSync(absPath, 'utf-8')
    const rel = path.relative(path.join(repoBase, DOCS_SUBDIR), absPath) // např. essentials/reactivity-fundamentals.md
    const base = rel.replace(/\.md$/, '')
    const titleParts = base.split(/[\\/]/).map(s => s.replace(/-/g, ' '))
    const title = titleParts[titleParts.length - 1]
        .replace(/\b\w/g, c => c.toUpperCase())
    const id = toSlug(base)
    const cards = extractCardsFromMarkdown(md)
    const quiz = makeQuizFromCards(cards)
    const estMins = Math.max(3, Math.round(cards.length * 1.5))

    return { id, title, estMins, cards, quiz }
}

// -----------------------
// 6) Hlavní běh
// -----------------------
async function main() {
    console.time('⏱️ Celkem')
    await prepareRepo()

    const base = path.join(CACHE_DIR, DOCS_SUBDIR)
    const files = findGuideMarkdown()

    const lessons = files
        .map(f => fileToLesson(f, CACHE_DIR))
        // jen lekce s aspoň jednou kartou (jinak by to byly prázdné sekce)
        .filter(l => (l.cards?.length || 0) > 0)

    ensureDirSync(path.dirname(OUTPUT_FILE))
    fs.writeFileSync(OUTPUT_FILE, JSON.stringify(lessons, null, 2), 'utf-8')

    console.log(`✅ Uloženo ${lessons.length} lekcí do: ${path.relative(process.cwd(), OUTPUT_FILE)}`)
    console.timeEnd('⏱️ Celkem')
}

main().catch(err => {
    console.error('❌ Scraper selhal:', err)
    process.exit(1)
})