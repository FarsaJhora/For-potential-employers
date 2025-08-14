const KEY = 'microlearn_v1'

export function loadProgress() {
    try {
        const raw = localStorage.getItem(KEY)
        return raw ? JSON.parse(raw) : { completedCards: {}, quizResults: {}, streak: 0, lastDay: null }
    } catch {
        return { completedCards: {}, quizResults: {}, streak: 0, lastDay: null }
    }
}

export function saveProgress(progress) {
    localStorage.setItem(KEY, JSON.stringify(progress))
}