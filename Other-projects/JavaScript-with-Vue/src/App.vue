<!-- App.vue -->
<template>
  <div class="app">
    <h1>Deštník Alert ☔</h1>
    <input v-model="recipientEmail" type="email" placeholder="Zadejte e-mail pro upozornění" />
    <button @click="checkWeather">Zkontroluj počasí</button>
    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import emailjs from 'emailjs-com'

const message = ref('')
// OpenWeatherMap API key je konfigurovatelný uživatelem
const EMAILJS_SERVICE_ID = 'mailtrap_service_123' // Zde vložíme svůj EmailJS service ID
const EMAILJS_TEMPLATE_ID = 'template_wnmnmvr' // Zde vložíme svůj EmailJS template ID
// EmailJS public/private key je konfigurovatelný uživatelem
const recipientEmail = ref('') // E-mail je nyní konfigurovatelný uživatelem

const checkWeather = () => {
  if (!navigator.geolocation) {
    message.value = 'Geolokace není podporována.'
    return
  }

  navigator.geolocation.getCurrentPosition(async (position) => {
    const { latitude, longitude } = position.coords

    try {
      const res = await fetch('http://localhost:3001/check-weather', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          lat: latitude,
          lon: longitude,
          email: recipientEmail.value, // Použijeme hodnotu z inputu
        }),
      })

      const data = await res.json()
      message.value = data.message
    } catch (err) {
      message.value = 'Chyba při komunikaci se serverem.'
    }
  })
}

const sendEmail = () => {
  const templateParams = {
    to_email: recipientEmail.value,
    message: 'Dnes má pršet. Nezapomeň si vzít deštník! ☔'
  }

  emailjs.send(EMAILJS_SERVICE_ID, EMAILJS_TEMPLATE_ID, templateParams, EMAILJS_PUBLIC_KEY)
      .then(() => {
        message.value = 'E-mail odeslán!'
      }, (error) => {
        console.error(error)
        message.value = 'Nepodařilo se odeslat e-mail.'
      })
}
</script>

<style>
.app {
  text-align: center;
  font-family: sans-serif;
  padding: 2rem;
}
button {
  padding: 0.5rem 1rem;
  margin-top: 1rem;
}
</style>