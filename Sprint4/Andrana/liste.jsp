<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <p>Nom:<%= request.getAttribute("nom")%></p>
    <p>Age:<%= request.getAttribute("age")%></p>
    <p>
        <form action="objetType" method="get">
            <input type="text" name="objet.nom">
            <input type="number" name="objet.age">
            <input type="submit" value="valider">
        </form>
    </p>
</body>
</html>