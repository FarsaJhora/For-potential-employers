import { useLessons } from '../hooks/useLessons'
import { useProgress } from '../context/ProgressContext'
import { useState } from 'react'

export default function Quiz() {
    const { lessons, loading, error } = useLessons()
    const { addScore } = useProgress()
    const [qIndex, setQIndex] = useState(0)
    const [score, setScore] = useState(0)
    const [showResult, setShowResult] = useState(false)

    if (loading) return <p>Načítám kvíz...</p>
    if (error) return <p style={{ color: 'red' }}>{error}</p>

    // všechny kvízové otázky z lekcí do jednoho pole
    const questions = lessons.flatMap(l => l.quiz)
    const q = questions[qIndex]

    function answer(optionIndex) {
        if (optionIndex === q.answer) {
            setScore(prev => prev + 1)
            addScore(10) // +10 bodů do globálního skóre
        }

        if (qIndex < questions.length - 1) {
            setQIndex(qIndex + 1)
        } else {
            setShowResult(true)
        }
    }

    if (showResult) {
        return (
            <div>
                <h2>Výsledek</h2>
                <p>Správně: {score} z {questions.length}</p>
                <p>Získal jsi {score * 10} bodů 🎉</p>
            </div>
        )
    }

    return (
        <div>
            <h2>Kvíz</h2>
            <p>{q.q}</p>
            <ul>
                {q.options.map((opt, i) => (
                    <li key={i}>
                        <button className="btn" onClick={() => answer(i)}>{opt}</button>
                    </li>
                ))}
            </ul>
        </div>
    )
}