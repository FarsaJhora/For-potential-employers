import express from 'express'
import cors from 'cors'
import nodemailer from 'nodemailer'
import dotenv from 'dotenv'
import fetch from 'node-fetch'

dotenv.config()

const app = express()
const PORT = 3001

app.use(cors())
app.use(express.json())

const WEATHER_API_KEY = process.env.WEATHER_API_KEY

app.post('/check-weather', async (req, res) => {
    const { lat, lon, email } = req.body

    if (!lat || !lon || !email) {
        return res.status(400).json({ error: 'Missing parameters' })
    }

    try {
        const weatherRes = await fetch(
            `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${WEATHER_API_KEY}&units=metric`
        )
        const weatherData = await weatherRes.json()
        const weather = weatherData.weather[0].main.toLowerCase()

        if (weather.includes('rain')) {
            await sendEmail(email)
            return res.json({ message: 'Déšť detekován. E-mail odeslán.' })
        } else {
            return res.json({ message: 'Dnes by pršet nemělo.' })
        }
    } catch (err) {
        console.error(err)
        res.status(500).json({ error: 'Server error' })
    }
})

async function sendEmail(toEmail) {
    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: process.env.MAIL_USER,
            pass: process.env.MAIL_PASS,
        },
    })

    await transporter.sendMail({
        from: `"Deštník Alert" <${process.env.MAIL_USER}>`,
        to: toEmail,
        subject: 'Pozor na déšť!',
        text: 'Nezapomeň si deštník! ☔',
    })
}

app.listen(PORT, () => {
    console.log(`Server běží na http://localhost:${PORT}`)
})