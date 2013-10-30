<%@tag description="Generic template for Kissalista pages" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Tietokantayhteystesti</title>
  <link rel="stylesheet" href="http://advancedkittenry.github.io/css/base.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
  <script src="http://advancedkittenry.github.io/javascript/bootstrap.js"></script>
  <style>.panel h2 {margin-top: -0.5em; text-transform: capitalize; } th {text-transform: capitalize;}</style>
</head>
<body>
<div class="container" id="content">
    <jsp:doBody/>
</body>
</html>