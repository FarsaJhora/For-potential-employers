import React from 'react'

export default function Footer() {
    return (
        <footer
            style={{
                fontSize: '0.8rem',
                color: '#777',
                padding: '1rem',
                textAlign: 'center',
                borderTop: '1px solid #ddd',
                marginTop: '2rem'
            }}
        >
            Content © {new Date().getFullYear()} Vue.js Docs (MIT License) — Adapted for educational use.{' '}
            <a
                href="https://vuejs.org/"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: '#42b883' }}
            >
                Source
            </a>
        </footer>
    )
}