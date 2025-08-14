export default function ProgressBar({ value = 0, max = 1, label }) {
    const pct = Math.min(100, Math.round((value / Math.max(1, max)) * 100))
    return (
        <div>
            {label && <div style={{marginBottom:8, fontSize:'.9rem', opacity:.8}}>{label} — {pct}%</div>}
            <div className="progress" role="progressbar" aria-valuenow={pct} aria-valuemin="0" aria-valuemax="100">
                <span style={{ width: `${pct}%` }} />
            </div>
        </div>
    )
}