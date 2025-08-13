import express from "express";
import fetch from "node-fetch";
import dotenv from "dotenv";
import cors from "cors";

dotenv.config();
const app = express();
app.use(cors());
app.use(express.json());

const clientId = process.env.SPOTIFY_CLIENT_ID;
const clientSecret = process.env.SPOTIFY_CLIENT_SECRET;
const redirectUri = process.env.REDIRECT_URI;

// Přihlášení
app.get("/login", (req, res) => {
    const scope = "user-top-read playlist-modify-private playlist-modify-public";
    const authUrl = `https://accounts.spotify.com/authorize?response_type=code&client_id=${clientId}&scope=${encodeURIComponent(scope)}&redirect_uri=${encodeURIComponent(redirectUri)}`;
    res.redirect(authUrl);
});

// Callback po přihlášení
app.get("/callback", async (req, res) => {
    const code = req.query.code || null;
    const response = await fetch("https://accounts.spotify.com/api/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Basic " + Buffer.from(clientId + ":" + clientSecret).toString("base64")
        },
        body: new URLSearchParams({
            grant_type: "authorization_code",
            code: code,
            redirect_uri: redirectUri
        })
    });
    const data = await response.json();
    res.redirect(`http://localhost:5500?access_token=${data.access_token}`);
});

// Vytvoření playlistu
app.post("/create-playlist", async (req, res) => {
    const { token, name, tracks } = req.body;

    // Získání uživatelského ID
    const meRes = await fetch("https://api.spotify.com/v1/me", {
        headers: { Authorization: `Bearer ${token}` }
    });
    const meData = await meRes.json();
    const userId = meData.id;

    // Vytvoření playlistu
    const playlistRes = await fetch(`https://api.spotify.com/v1/users/${userId}/playlists`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: name,
            public: false,
            description: "Playlist vytvořen Music Suggestion App"
        })
    });
    const playlistData = await playlistRes.json();

    // Přidání skladeb
    await fetch(`https://api.spotify.com/v1/playlists/${playlistData.id}/tracks`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            uris: tracks
        })
    });

    res.json({ message: "Playlist vytvořen!", playlist: playlistData });
});

app.listen(3000, () => {
    console.log("✅ Server běží na http://127.0.0.1:3000");
});