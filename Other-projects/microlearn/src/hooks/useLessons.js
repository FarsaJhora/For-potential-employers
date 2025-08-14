import { useEffect, useState } from 'react'

/**
 * Hook pro načítání lekcí (flashcards + kvíz) z JSON souboru
 * generovaného Node.js skriptem z Vue.js dokumentace.
 */
export function useLessons() {
    const [lessons, setLessons] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        async function load() {
            try {
                setLoading(true)
                setError(null)

                // URL může být buď public path (React public/) nebo API endpoint
                const res = await fetch('/vue-lessons.json')
                if (!res.ok) {
                    throw new Error(`Chyba při načítání dat: ${res.status} ${res.statusText}`)
                }
                const data = await res.json()
                setLessons(data)
            } catch (err) {
                console.error('Nepodařilo se načíst lekce', err)
                setError(err.message)
            } finally {
                setLoading(false)
            }
        }
        load()
    }, [])

    return { lessons, loading, error }
}