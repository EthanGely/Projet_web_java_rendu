<%@ page import="fr.iut2.project1.utility.GestionFactory" %>
<%@ page import="fr.iut2.project1.Etudiant.Etudiant" %>
<%@ page import="fr.iut2.project1.Groupe.Groupe" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Liste des étudiants, déjà filtrée (il faut afficher tous les étudiants de cette liste) -->
<jsp:useBean id="etudiants" type="java.util.Collection<fr.iut2.project1.Etudiant.Etudiant>" scope="request"/>

<jsp:useBean id="filtre" class="java.lang.String" scope="request"/>
<jsp:useBean id="isGroupeFiltre" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="groupesFiltre" type="java.util.Collection<fr.iut2.project1.Groupe.Groupe>" scope="request"/>
<jsp:useBean id="etuLogin" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<jsp:useBean id="profLogin" class="fr.iut2.project1.Prof.Prof" scope="request"/>
<jsp:useBean id="hasConnected" type="java.lang.Boolean" scope="request"/>

<% boolean isEtu = true;
    try {
        if (etuLogin.getMail().equals("")) {
            isEtu = false;
        }
    } catch (Exception e) {
        isEtu = false;
    }%>

<!DOCTYPE html>
<html>
<head>
    <title>GEDI'NOTE - Liste étudiants</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<%if (!isEtu || !profLogin.isHasConnected()) {%>
<!--<button class="btn btn-success" onclick="resetUser()">RESET</button> -->
<button style="display: none;" value="<%=hasConnected%>" id="btnModal" type="button" class="btn btn-primary"
        data-bs-toggle="modal" data-bs-target="#welcomeModal"></button>
<!-- Modal -->
<input type="hidden" value="<%if (isEtu){%><%=etuLogin.getId()%><%}else{%><%=profLogin.getId()%><%}%>" name="idUser"
       id="idUser">
<div class="modal fade" id="welcomeModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Bienvenue dans <img class="gediText"
                                                                                        src="${pageContext.request.contextPath}/Ressources/img/gedinote.png"
                                                                                        alt="GEDI'NOTE"></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div style="display: flex; flex-wrap: nowrap; width: 100%; justify-content: center;">
                    <img style="max-width: 200px; width: 100%; height: auto;"
                         src="${pageContext.request.contextPath}/Ressources/img/PastaMat.png" alt="">
                    <img style="max-width: 200px; width: 100%; height: auto;"
                         src="${pageContext.request.contextPath}/Ressources/img/GediBox.png" alt="">
                </div>
                <div>
                    <p style="margin-bottom: 20px">Bienvenue <%=profLogin.getPrenom() + " " + profLogin.getNom()%>.
                        <br>
                        Si vous ne connaissez pas encore <img class="gediText"
                                                              src="${pageContext.request.contextPath}/Ressources/img/gedinote.png"
                                                              alt="GEDI'NOTE">, nous vous conseillons vivement d'aller
                        voir
                        <a>
                            <button class="btn btn-primary btn-sm">notre guide</button>
                        </a>.<br>
                        <span class="citation">PastaMat et GediBox seront ravis de vous aider !</span><br>
                    </p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                <button type="button" class="btn btn-primary">Voir la documentation</button>
            </div>
        </div>
    </div>
</div>
<%}%>
<body onload="changerGroupe()">
<div id="wrapper">
    <jsp:include page="commun/headerRecherche.jsp"></jsp:include>
    <div id="content">

        <div style="display: flex; justify-content: space-around; margin: 30px auto; width: 35%">
            <form action="<%= application.getContextPath()%>/do/CreerEtu" style="display: flex;">
                <button type="submit" class="btn btn-outline-success" style="margin: 0 auto;">Ajouter un étudiant
                </button>
            </form>
            <% if (profLogin.isAdmin()) {%>
            <form action="<%= application.getContextPath()%>/do/creerProf" style="display: flex;">
                <button type="submit" class="btn btn-outline-success" style="margin: 0 auto;">Ajouter un prof</button>
            </form>
            <%}%>
        </div>

        <!-- Fiches étudiant -->
        <div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly; padding: 0rem 5rem;">
            <!-- Itération sur la liste des étudiants -->
            <% for (Etudiant etu : etudiants) { %>
            <div class="card" style="width: 20%; margin: 1.25rem 2.5rem;">
                <div class="card-header" style="display: flex;">
                    <div>Élève</div>
                    <div style="position: absolute;right: 15px;top: 4px;">
                        <form action="<%= application.getContextPath()%>/do/SupprimerEtu">
                            <input type="hidden" name="id" value="<%=etu.getId()%>">
                            <button type="submit" class="btn btn-outline-danger" style="padding: 5px;font-size: 15px;">❌
                            </button>
                        </form>

                    </div>
                </div>
                <div class="card-body">
                    <h5 class="card-title"><%= etu.getPrenom() + " " + etu.getNom() %>
                    </h5>
                    <% if (!filtre.isEmpty()) {
                        if (filtre.charAt(filtre.length() - 1) == '1') {
                            float moyenne = etu.getMoyenne();%>
                    <div class="alert alert-<% if (moyenne >= 14) {%>success<% } else if (moyenne < 14 && moyenne >= 7) { %>warning<% } else if (moyenne < 7 && moyenne != -1){ %>danger<% } else { %>dark<%}%>"
                         role="alert">
                        <% if (moyenne == -1) {%>Non noté<%} else {%>Moyenne : <%= moyenne %><%}%>
                    </div>
                    <% }

                        if (filtre.charAt(filtre.length() - 2) == '1') { %>
                    <div id="notifAbs-<%=etu.getId()%>" style="display: flex;"
                         class="alert alert-<% if (etu.getNbAbsences() <= 0) {%>success<% } else { %>danger<% } %>"
                         role="alert">
                        <div id="ABS-<%=etu.getId()%>">Nombre d'absence<% if (etu.getNbAbsences() > 0) {%>s<% }%>
                            : <%= etu.getNbAbsences() %>
                        </div>
                        <div style="margin: auto 0 auto auto;">
                            <button type="button" onclick="addAbs(<%= etu.getId() %>)" class="btn btn-warning"
                                    style="padding: 0 5px;font-size: 15px;vertical-align: initial;">+
                            </button>
                        </div>
                    </div>
                    <% }

                        if (filtre.charAt(filtre.length() - 3) == '1') { %>
                    <div>
                        <p>Groupe : <%= etu.getGroupe().getNom() %>
                        </p>

                    </div>
                    <% }
                    }
                    %>
                    <div style="display: flex; justify-content: space-evenly;">
                        <form action="<%= application.getContextPath()%>/do/FicheEtudiant" method="post"
                              style="display: flex;">
                            <input type="hidden" name="id" value="<%= etu.getId() %>">
                            <input type="submit" class="btn btn-success" value="Détails" style="margin: 0 auto">
                        </form>
                        <form action="<%= application.getContextPath()%>/do/editionetudiant" method="post"
                              style="display: flex;">
                            <input type="hidden" name="id" value="<%= etu.getId() %>">
                            <input type="submit" class="btn btn-primary" value="Éditer" style="margin: 0 auto">
                        </form>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>
    <jsp:include page="commun/footer.jsp"></jsp:include>
</div>
<%if (!isEtu) {%>
<script type='text/javascript' src="<%=application.getContextPath()%>/Ressources/javascript/introWebsite.js"
        charset="UTF-8"></script>
<%}%>
</body>
</html>