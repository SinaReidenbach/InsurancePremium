<?php
header('Content-Type: application/json');
require 'database.php'; // Datei mit der Datenbankverbindung

$type = isset($_GET['type']) ? $_GET['type'] : '';

$conn = getDatabaseConnection();

if ($conn) {
    if ($type === 'regions') {
        $query = "SELECT region_name FROM regions";
    } elseif ($type === 'vehicles') {
        $query = "SELECT vehicle_name FROM vehicles";
    } else {
        echo json_encode(["error" => "Ungültiger Typ"]);
        exit;
    }

    $result = $conn->query($query);
    $data = [];

    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }

    echo json_encode($data);
} else {
    echo json_encode(["error" => "Datenbankverbindung fehlgeschlagen"]);
}

$conn->close();
?>