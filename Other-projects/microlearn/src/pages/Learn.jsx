import { useSearchParams, Link } from 'react-router-dom'
import { useLessons } from '../hooks/useLessons'
import { useProgress } from '../context/ProgressContext'
import { useState } from 'react'

export default function Learn() {
    const { lessons, loading, error } = useLessons()
    const { markLessonCompleted } = useProgress()
    const [searchParams] = useSearchParams()
    const [step, setStep] = useState(0)

    const lessonId = searchParams.get('id') || lessons[0]?.id
    const lesson = lessons.find(l => l.id === lessonId)

    if (loading) return <p>Načítám lekci...</p>
    if (error) return <p style={{ color: 'red' }}>{error}</p>
    if (!lesson) return <p>Lekce nebyla nalezena.</p>

    const card = lesson.cards[step]

    function nextCard() {
        if (step < lesson.cards.length - 1) {
            setStep(step + 1)
        } else {
            markLessonCompleted()
        }
    }

    return (
        <div>
            <h2>{lesson.title}</h2>
            <div className="card">
                <p><strong>{card.front}</strong></p>
                <p>{card.back}</p>
            </div>

            <div style={{ marginTop: '1rem' }}>
                {step < lesson.cards.length - 1 ? (
                    <button className="btn" onClick={nextCard}>Další karta</button>
                ) : (
                    <Link to="/" className="btn secondary">Zpět na Dashboard</Link>
                )}
            </div>
        </div>
    )
}