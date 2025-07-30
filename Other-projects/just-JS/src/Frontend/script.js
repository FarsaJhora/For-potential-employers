const checkBtn = document.getElementById('checkBtn')
const messageEl = document.getElementById('message')

checkBtn.addEventListener('click', () => {
    const email = document.getElementById('email').value.trim()
    if (!email) {
        messageEl.textContent = 'Zadej e-mail.'
        return
    }

    if (!navigator.geolocation) {
        messageEl.textContent = 'Geolokace není podporována.'
        return
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
        const { latitude, longitude } = position.coords

        try {
            const response = await fetch('http://localhost:3001/check-weather', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    lat: latitude,
                    lon: longitude,
                    email: email
                })
            })

            const data = await response.json()
            messageEl.textContent = data.message || 'Něco se pokazilo.'
        } catch (err) {
            console.error(err)
            messageEl.textContent = 'Chyba při komunikaci se serverem.'
        }
    }, () => {
        messageEl.textContent = 'Nepodařilo se zjistit polohu.'
    })
})