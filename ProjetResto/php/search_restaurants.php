<?php
include 'connexion.php';
try {
    $pdo = connectToDatabase();

    if (!$pdo) {
        echo "Erreur de connexion à la base de données";
        exit;
    }

    $searchName = $_GET['searchName'];
    $filterMenu = $_GET['filterMenu'];
    $filterDistance = $_GET['filterDistance'];

    $sql = "SELECT * FROM resto";
    $conditions = [];

    if (!empty($searchName)) {
        $conditions[] = "name ILIKE :searchName";
    }

    if (!empty($filterMenu)) {
        $sql .= " JOIN menus ON resto.id = menus.restaurant_id";
        $conditions[] = "menu.name ILIKE :filterMenu";
    }

    if (!empty($filterDistance)) {
        $userLat = -18.986021;
        $userLng = 47.532735;
        $distanceFormula = "6371 * acos(cos(radians(:userLat)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:userLng)) + sin(radians(:userLat)) * sin(radians(latitude)))";
        $conditions[] = "($distanceFormula) <= :filterDistance";
    }

    if (count($conditions) > 0) {
        $sql .= " WHERE " . implode(" AND ", $conditions);
    }

    $stmt = $pdo->prepare($sql);

    if (!empty($searchName)) {
        $stmt->bindValue(':searchName', '%' . $searchName . '%');
    }

    if (!empty($filterMenu)) {
        $stmt->bindValue(':filterMenu', '%' . $filterMenu . '%');
    }

    if (!empty($filterDistance)) {
        $stmt->bindValue(':userLat', $userLat);
        $stmt->bindValue(':userLng', $userLng);
        $stmt->bindValue(':filterDistance', $filterDistance);
    }

    $stmt->execute();
    $restaurants = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($restaurants);
} catch (PDOException $e) {
    echo $e->getMessage();
}
?>
