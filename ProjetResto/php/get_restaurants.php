<?php
include 'connexion.php';

try {
    $pdo = connectToDatabase();

    if (!$pdo) {
        echo "Erreur de connexion à la base de données";
        exit;
    }

    $sql = "SELECT id, name, image,latitude,longitude FROM resto";
    $stmt = $pdo->query($sql);
    $restaurants = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($restaurants);
} catch (PDOException $e) {
    echo $e->getMessage();
}
?>
