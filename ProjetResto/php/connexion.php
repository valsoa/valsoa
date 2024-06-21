<?php
function connectToDatabase() {
    $host = 'localhost';
    $db = 'restoSig';
    $user = 'postgres';
    $pass = 'valisoa';
    $port = '5432';
    $dsn = "pgsql:host=$host;port=$port;dbname=$db;user=$user;password=$pass";

    try {
        $pdo = new PDO($dsn);

        if (!$pdo) {
            echo "Erreur de connexion à la base de données";
            exit;
        }

        return $pdo;
    } catch (PDOException $e) {
        echo $e->getMessage();
    }
}
?>