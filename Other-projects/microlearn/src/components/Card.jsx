import { useState } from 'react'

export default function Card({ front, back, onDone }) {
    const [flipped, setFlipped] = useState(false)
    return (
        <article className="card" aria-live="polite">
            <h3 style={{margin:'0 0 8px'}}>Flashcard</h3>
            <p style={{minHeight:56}}>{flipped ? back : front}</p>
            <div className="row">
                <button className="btn secondary" onClick={() => setFlipped(f => !f)} aria-pressed={flipped}>
                    {flipped ? 'Zpět' : 'Zobrazit odpověď'}
                </button>
                <button className="btn" onClick={onDone} title="Označit jako zvládnuté">
                    Hotovo
                </button>
            </div>
        </article>
    )
}