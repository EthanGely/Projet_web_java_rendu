<%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 16/01/2023
  Time: 07:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="etuLogin" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<jsp:useBean id="profLogin" class="fr.iut2.project1.Prof.Prof" scope="request"/>
<jsp:useBean id="codeErreur" type="java.lang.Integer" scope="request"/>
<% boolean isEtu = true;
    try {
        if (etuLogin.getMail() == null || etuLogin.getMail().equals("")) {
            isEtu = false;
        }
    } catch (Exception e) {
        isEtu = false;
    }%>
<!DOCTYPE html>
<html>
<head>
    <title>GEDI'NOTE - Profil</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<% if (isEtu){ %>
<jsp:include page="commun/headerSimpleEtu.jsp"></jsp:include>
<% }else{ %>
<jsp:include page="commun/headerSimple.jsp"></jsp:include>
<% } %>
<% String prenom;
    String nom;
    String mail;
    Long idUser;
    int typeUser;
    if (isEtu) {
        prenom = etuLogin.getPrenom();
        nom = etuLogin.getNom();
        mail = etuLogin.getMail();
        idUser = etuLogin.getId();
        typeUser = 1;
    } else {
        prenom = profLogin.getPrenom();
        nom = profLogin.getNom();
        mail = profLogin.getMail();
        idUser = profLogin.getId();
        typeUser = 2;
    }
%>
<body>
<div id="wrapper">
    <div class="container">
        <div class="row">
            <div class="col-md-12 d-flex" style="justify-content: center;">
                <h2>Votre profil</h2>
            </div>
        </div>
    </div>
    <%if (codeErreur != -1 && codeErreur != 0) {%>
    <div style="text-align: center;"
         class="alert alert-<%if (codeErreur == 1 || codeErreur == 3 || codeErreur == 4){%>danger<%}else{%>warning<%}%>"
         role="alert">
        <%if (codeErreur == 1) {%>L'ancien mot de passe est incorrect. Aucune modification n'a été
        sauvegardée<%} else if (codeErreur == 2) {%>Les nouveaux mots de passe ne correspondent pas<br>Les autres
        modifications ont été prises en compte.<br>Modifier le code cette page pour entrer des erreurs ne fonctionnera
        pas.<%} else if (codeErreur == 3) {%>Il semblerait que vous essayez de modifier un utilisateur différent du
        vôtre.<%} else if (codeErreur == 4) {%>Un ou plusieurs champs sont vides...<br>Modifier le code de cette page ne
        vous amènera à rien...<%}%>
    </div>
    <%}%>
    <div class="container" style="margin-bottom: 90px;">
        <div class="row">
            <div class="col-md-12">
                <form method="post" autocomplete="off" action="<%= application.getContextPath()%>/do/modifProfil">
                    <input type="hidden" name="idUser" id="idUser" value="<%=idUser%>">
                    <input type="hidden" name="typeUser" id="typeUser" value="<%=typeUser%>">
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="prenom" id="prenom" placeholder="Jean"
                               value="<%=prenom%>" required>
                        <label for="prenom">Prénom</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="nom" id="nom" placeholder="Sérien"
                               value="<%=nom%>" required>
                        <label for="nom">Nom</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="text" autocomplete="off" class="form-control" name="mail" id="mail"
                               placeholder="jean.serien@iut.fr"
                               value="<%=mail%>" required>
                        <label for="mail">Adresse mail</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="password" class="form-control" onkeyup="checkFields(1)" name="oldPass"
                               id="oldPass" placeholder="Mot de passe" required>
                        <label for="oldPass">Ancien mot de passe</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="password" autocomplete="off" class="form-control" onkeyup="checkFields(1)"
                               name="newPass" id="champ_1" placeholder="Mot de passe">
                        <label for="champ_1">Nouveau mot de passe</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="password" class="form-control" onkeyup="checkFields(1)" name="passVerif"
                               id="champ_2"
                               placeholder="Confirmation du mot de passe">
                        <label for="champ_2">Confirmation du mot de passe</label>
                    </div>
                    <div style="display: flex; justify-content: center; margin-top: 10px;">
                        <input type="submit" class="btn btn-primary" onmouseover="moveBtn(1)"
                               style="position: absolute;transition: all 0.3s;" id="btnValider"
                               value="Enregistrer les modifications">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
            integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
            crossorigin="anonymous"></script>
    <jsp:include page="commun/footer.jsp"></jsp:include>
</div>
</body>
</html>

