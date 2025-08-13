const loginBtn = document.getElementById("loginBtn");
const searchMoodBtn = document.getElementById("searchMoodBtn");
const searchGenreBtn = document.getElementById("searchGenreBtn");
const createPlaylistBtn = document.getElementById("createPlaylistBtn");
const resultsDiv = document.getElementById("results");

let token = null;
let selectedTracks = [];

// Login
loginBtn.addEventListener("click", () => {
    window.location.href = "http://127.0.0.1:3000/login";
});

// Získání tokenu z URL
const params = new URLSearchParams(window.location.search);
token = params.get("access_token");

// Vyhledávání podle nálady (použijeme search endpoint)
searchMoodBtn.addEventListener("click", () => {
    const mood = document.getElementById("moodInput").value;
    fetch(`https://api.spotify.com/v1/search?q=${encodeURIComponent(mood)}&type=track&limit=5`, {
        headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => showResults(data.tracks.items));
});

// Vyhledávání podle žánru (doporučení podle seed_genres)
searchGenreBtn.addEventListener("click", () => {
    const genre = document.getElementById("genreInput").value;
    fetch(`https://api.spotify.com/v1/recommendations?seed_genres=${encodeURIComponent(genre)}&limit=5`, {
        headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => showResults(data.tracks));
});

// Zobrazení výsledků
function showResults(tracks) {
    resultsDiv.innerHTML = "";
    selectedTracks = [];

    tracks.forEach(track => {
        const p = document.createElement("p");
        p.textContent = `${track.name} - ${track.artists[0].name}`;
        resultsDiv.appendChild(p);
        selectedTracks.push(track.uri);
    });
}

// Vytvoření playlistu
createPlaylistBtn.addEventListener("click", () => {
    const name = document.getElementById("playlistName").value;
    fetch("http://127.0.0.1:3000/create-playlist", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            token: token,
            name: name,
            tracks: selectedTracks
        })
    })
        .then(res => res.json())
        .then(data => {
            alert("✅ Playlist vytvořen: " + data.playlist.name);
        });
});
// Mapa nálad na Spotify žánry
const moodToGenres = {
    happy: ["pop", "dance", "edm"],
    sad: ["acoustic", "piano", "singer-songwriter"],
    energetic: ["rock", "edm", "metal"],
    chill: ["chill", "ambient", "lo-fi"],
    romantic: ["romance", "jazz", "soul"],
    focus: ["classical", "ambient", "piano"]
};

searchMoodBtn.addEventListener("click", () => {
    const mood = document.getElementById("moodInput").value.toLowerCase();

    if (moodToGenres[mood]) {
        const genres = moodToGenres[mood].join(",");
        fetch(`https://api.spotify.com/v1/recommendations?seed_genres=${encodeURIComponent(genres)}&limit=5`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(data => showResults(data.tracks));
    } else {
        // pokud náladu neznáme, použijeme textové vyhledávání
        fetch(`https://api.spotify.com/v1/search?q=${encodeURIComponent(mood)}&type=track&limit=5`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(data => showResults(data.tracks.items));
    }
});