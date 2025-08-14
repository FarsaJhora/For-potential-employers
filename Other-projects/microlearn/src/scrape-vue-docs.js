import fs from 'fs'
import path from 'path'
import axios from 'axios'
import matter from 'gray-matter'
import { marked } from 'marked'
import glob from 'glob'

// URL repozitáře s Vue dokumentací (markdown soubory)
const VUE_DOCS_REPO = 'https://raw.githubusercontent.com/vuejs/docs/main/src/guide'

// Lokální složka pro stažené soubory
const TEMP_DIR = './vue_docs_temp'

// Funkce pro stažení všech .md souborů z guide
async function fetchMarkdownFiles() {
    const guideFiles = [
        'introduction.md',
        'essentials/reactivity-fundamentals.md',
        'essentials/computed.md',
        'essentials/class-and-style.md'
    ]

    if (!fs.existsSync(TEMP_DIR)) fs.mkdirSync(TEMP_DIR, { recursive: true })

    for (const file of guideFiles) {
        const url = `${VUE_DOCS_REPO}/${file}`
        const res = await axios.get(url)
        const filePath = path.join(TEMP_DIR, file.replace(/\//g, '_'))
        fs.writeFileSync(filePath, res.data, 'utf-8')
        console.log(`Staženo: ${file}`)
    }
}

// Extrakce flashcards z Markdownu
function extractCardsFromMarkdown(mdText) {
    const { content } = matter(mdText) // ignorujeme frontmatter

    const tokens = marked.lexer(content)
    const cards = []

    let lastHeading = null
    let buffer = []

    for (const token of tokens) {
        if (token.type === 'heading' && token.depth <= 3) {
            if (lastHeading && buffer.length > 0) {
                cards.push({
                    id: lastHeading.toLowerCase().replace(/\s+/g, '-'),
                    front: lastHeading,
                    back: buffer.join(' ').trim()
                })
                buffer = []
            }
            lastHeading = token.text
        } else if (token.type === 'paragraph') {
            buffer.push(token.text)
        }
    }

    // poslední sekce
    if (lastHeading && buffer.length > 0) {
        cards.push({
            id: lastHeading.toLowerCase().replace(/\s+/g, '-'),
            front: lastHeading,
            back: buffer.join(' ').trim()
        })
    }

    return cards
}

// Generování kvízových otázek (zatím jednoduché – otázky z nadpisů)
function generateQuizFromCards(cards) {
    return cards.slice(0, 3).map((c, idx) => ({
        q: `O čem je sekce „${c.front}“?`,
        options: [
            `O ${c.front}`,
            'O úplně jiné věci',
            'O základech JavaScriptu'
        ],
        answer: 0
    }))
}

// Hlavní funkce
async function main() {
    await fetchMarkdownFiles()

    const lessonFiles = glob.sync(`${TEMP_DIR}/*.md`)
    const lessons = []

    for (const filePath of lessonFiles) {
        const mdText = fs.readFileSync(filePath, 'utf-8')
        const title = path.basename(filePath, '.md').replace(/_/g, ' ')
        const cards = extractCardsFromMarkdown(mdText)
        const quiz = generateQuizFromCards(cards)

        lessons.push({
            id: title.toLowerCase().replace(/\s+/g, '-'),
            title,
            estMins: Math.max(3, Math.round(cards.length * 1.5)), // odhad
            cards,
            quiz
        })
    }

    fs.writeFileSync('./vue-lessons.json', JSON.stringify(lessons, null, 2))
    console.log(`Hotovo ✅ — uložen JSON s ${lessons.length} lekcemi do vue-lessons.json`)
}

main().catch(err => console.error(err))