import { Link } from 'react-router-dom'
import { useLessons } from '../hooks/useLessons'
import { useProgress } from '../context/ProgressContext'

export default function Dashboard() {
    const { lessons, loading, error } = useLessons()
    const { streak, totalLessons, completedLessons, score, setTotalLessons } = useProgress()

    // nastavení celkového počtu lekcí (po načtení dat)
    if (!loading && lessons.length > 0 && totalLessons !== lessons.length) {
        setTotalLessons(lessons.length)
    }

    if (loading) return <p>Načítám lekce...</p>
    if (error) return <p style={{ color: 'red' }}>{error}</p>

    return (
        <div>
            <h2>📊 Přehled učení</h2>
            <div className="card">
                <p>🔥 Streak: <strong>{streak}</strong> dní</p>
                <p>📚 Dokončené lekce: {completedLessons} / {totalLessons}</p>
                <p>🏆 Skóre: {score} bodů</p>
            </div>

            <h3>Lekce</h3>
            <ul>
                {lessons.map(lesson => (
                    <li key={lesson.id}>
                        <Link to={`/learn?id=${lesson.id}`}>{lesson.title}</Link> ({lesson.estMins} min)
                    </li>
                ))}
            </ul>

            <div style={{ marginTop: '1rem' }}>
                <Link to="/learn" className="btn">Začít učení</Link>
                <Link to="/quiz" className="btn secondary" style={{ marginLeft: '1rem' }}>Kvíz</Link>
            </div>
        </div>
    )
}