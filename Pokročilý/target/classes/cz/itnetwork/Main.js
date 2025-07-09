
// Spouští se po načtení DOM
// Skript pro automatické skrývání alertů po 4 sekundách
document.addEventListener("DOMContentLoaded", function () {
    const alerts = document.querySelectorAll(".alert");
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.display = "none";
        }, 4000);
    });
});