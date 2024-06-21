<?php
include 'connexion.php';
try {
    $pdo = connectToDatabase();

    if (!$pdo) {
        echo "Erreur de connexion à la base de données";
        exit;
    }

    $restaurant_id = $_POST['restaurant_id'];
    $name = $_POST['name'];

    $sql = "INSERT INTO menu (restaurant_id, name) VALUES (:restaurant_id, :name)";
    $stmt = $pdo->prepare($sql);
    $stmt->execute(['restaurant_id' => $restaurant_id, 'name' => $name]);

    echo "Menu ajouté avec succès!";
} catch (PDOException $e) {
    echo $e->getMessage();
}
?>
