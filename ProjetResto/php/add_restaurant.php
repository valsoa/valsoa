<?php
include 'connexion.php';
try {
    $pdo = connectToDatabase();

    if (!$pdo) {
        echo "Erreur de connexion à la base de données";
        exit;
    }

    $name = $_POST['name'];
    $latitude = explode(',', $_POST['coordinates'])[0];
    $longitude = explode(',', $_POST['coordinates'])[1];

    if ($_FILES['image']['name']) {
        $image = $_FILES['image']['name'];
        move_uploaded_file($_FILES['image']['tmp_name'], 'uploads/' . $image);
    } else {
        $image = null;
    }

    $sql = "INSERT INTO resto (name, image, latitude, longitude) VALUES (:name, :image, :latitude,:longitude)";
    $stmt = $pdo->prepare($sql);
    $stmt->execute(['name' => $name, 'image' => $image, 'latitude' => $latitude, 'longitude' => $longitude]);

    echo "Restaurant ajouté avec succès!";
} catch (PDOException $e) {
    echo $e->getMessage();
}
?>
