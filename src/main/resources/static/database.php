<?php
function getDatabaseConnection() {
    $servername = "localhost"; // Datenbank-Host
    $username = "root";        // Dein Datenbank-Benutzername
    $password = "";            // Dein Datenbank-Passwort
    $dbname = "versicherungen"; // Deine Datenbankname (ändere das nach Bedarf)

    // Verbindung zur Datenbank herstellen
    $conn = new mysqli($servername, $username, $password, $dbname);

    // Überprüfen, ob die Verbindung erfolgreich ist
    if ($conn->connect_error) {
        die("Verbindung fehlgeschlagen: " . $conn->connect_error);
    }

    return $conn;
}
?>