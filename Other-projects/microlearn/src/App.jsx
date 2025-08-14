import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Dashboard from './pages/Dashboard.jsx'
import Learn from './pages/Learn.jsx'
import Quiz from './pages/Quiz.jsx'
import Footer from './components/Footer.jsx'
import { ProgressProvider } from './context/ProgressContext.jsx'

export default function App() {
    return (
        <ProgressProvider>
            <Router>
                <div
                    style={{
                        minHeight: '100vh',
                        display: 'flex',
                        flexDirection: 'column'
                    }}
                >
                    {/* Obsah aplikace */}
                    <div style={{ flex: '1' }}>
                        <Routes>
                            <Route path="/" element={<Dashboard />} />
                            <Route path="/learn" element={<Learn />} />
                            <Route path="/quiz" element={<Quiz />} />
                        </Routes>
                    </div>

                    {/* Patička */}
                    <Footer />
                </div>
            </Router>
        </ProgressProvider>
    )
}
