import { createContext, useContext, useState, useEffect } from 'react'

const ProgressContext = createContext()

export function ProgressProvider({ children }) {
    const [streak, setStreak] = useState(0)
    const [lastStudyDate, setLastStudyDate] = useState(null)
    const [totalLessons, setTotalLessons] = useState(0)
    const [completedLessons, setCompletedLessons] = useState(0)
    const [score, setScore] = useState(0)

    // Načtení z localStorage při startu
    useEffect(() => {
        const saved = localStorage.getItem('progress')
        if (saved) {
            const parsed = JSON.parse(saved)
            setStreak(parsed.streak || 0)
            setLastStudyDate(parsed.lastStudyDate || null)
            setTotalLessons(parsed.totalLessons || 0)
            setCompletedLessons(parsed.completedLessons || 0)
            setScore(parsed.score || 0)
        }
    }, [])

    // Uložení při každé změně
    useEffect(() => {
        localStorage.setItem(
            'progress',
            JSON.stringify({
                streak,
                lastStudyDate,
                totalLessons,
                completedLessons,
                score
            })
        )
    }, [streak, lastStudyDate, totalLessons, completedLessons, score])

    // Funkce pro označení lekce jako dokončené
    function markLessonCompleted() {
        setCompletedLessons(prev => {
            const newCompleted = prev + 1
            updateStreak()
            return newCompleted
        })
    }

    // Funkce pro přidání bodů ze hry/kvízu
    function addScore(points) {
        setScore(prev => prev + points)
    }

    // Streak update (jedna lekce za den => +1 streak)
    function updateStreak() {
        const today = new Date().toDateString()
        if (lastStudyDate !== today) {
            setStreak(prev => prev + 1)
            setLastStudyDate(today)
        }
    }

    // Reset všeho
    function resetProgress() {
        setStreak(0)
        setLastStudyDate(null)
        setTotalLessons(0)
        setCompletedLessons(0)
        setScore(0)
        localStorage.removeItem('progress')
    }

    return (
        <ProgressContext.Provider
            value={{
                streak,
                totalLessons,
                completedLessons,
                score,
                setTotalLessons,
                markLessonCompleted,
                addScore,
                resetProgress
            }}
        >
            {children}
        </ProgressContext.Provider>
    )
}

export function useProgress() {
    return useContext(ProgressContext)
}