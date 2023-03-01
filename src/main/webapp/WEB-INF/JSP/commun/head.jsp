<!-- Favicon -->
<link rel="icon" href="<%=application.getContextPath()%>/Ressources/img/GediLogo.ico">
<!-- Jquery -->
<script type='text/javascript' src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<!-- feuille de style -->
<link href="<%=application.getContextPath()%>/Ressources/style/style.css" rel="stylesheet">
<!-- Coloration syntaxique code -->
<link href="<%=application.getContextPath()%>/Ressources/style/prism.css" rel="stylesheet">
<!-- Bootstrap (CSS + JS) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
<!-- FontAwsome (petits logos) -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.14.0/css/all.css"
      integrity="sha384-HzLeBuhoNPvSl5KYnjx0BT+WB0QEEqLprO+NBkkk5gbc67FTaL7XIGa2w1L0Xbgc" crossorigin="anonymous">
<!-- Variables pour les appels Ajax -->
<script type='text/javascript'>
    // Description des urls de demande ajax
    var urlAddAbs = "<%=application.getContextPath()%>/ajax/addAbs";
    var urlSetConnected = "<%=application.getContextPath()%>/ajax/setConnected";
    var urlSetDisconnected = "<%=application.getContextPath()%>/ajax/setDisconnected";
    var urlDeleteGrade = "<%=application.getContextPath()%>/ajax/deleteGrade";
    var urlDeleteUser = "<%=application.getContextPath()%>/ajax/deleteUser";
</script>
<!-- Script JS -->
<script type='text/javascript' src="<%=application.getContextPath()%>/Ressources/javascript/monscript.js"
        charset="UTF-8"></script>
