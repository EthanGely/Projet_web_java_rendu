<%@ page import="fr.iut2.project1.utility.GestionFactory" %>
<%@ page import="fr.iut2.project1.Etudiant.Etudiant" %>
<%@ page import="java.io.ObjectInputStream" %>
<%@ page import="fr.iut2.project1.Note.Note" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="fr.iut2.project1.Prof.Prof" %><%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 02/12/2022
  Time: 09:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- Récupération des données --%>
<jsp:useBean id="etudiant" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<jsp:useBean id="etuLogin" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<jsp:useBean id="profLogin" class="fr.iut2.project1.Prof.Prof" scope="request"/>

<% boolean isEtu = true;
    try {
        if (etuLogin == null || etuLogin.getMail().equals("")) {
            isEtu = false;
        }
    } catch (Exception e) {
        isEtu = false;
    }
    float moyenne = etudiant.getMoyenne();%>
<html>
<head>
    <title>GEDI'NOTE - Fiche étudiant</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<%if (isEtu && !etuLogin.getHasConnected()) {%>
<button style="display: none;" id="btnModal" type="button" class="btn btn-primary" data-bs-toggle="modal"
        data-bs-target="#welcomeModal"></button>

<!-- Modal -->
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
                <div style="display: flex; flex-wrap: nowrap; width: 100%; justify-content: center; margin-bottom: 20px;">
                    <img style="max-width: 200px; width: 100%; height: auto;"
                         src="${pageContext.request.contextPath}/Ressources/img/PastaMat.png" alt="">
                    <img style="max-width: 200px; width: 100%; height: auto;"
                         src="${pageContext.request.contextPath}/Ressources/img/GediBox.png" alt="">
                </div>
                <div>
                    <p>Bienvenue <%=etuLogin.getPrenom() + " " + etuLogin.getNom()%>.</p>
                    <p>Si vous ne connaissez pas encore
                        <img class="gediText" src="${pageContext.request.contextPath}/Ressources/img/gedinote.png"
                             alt="GEDI'NOTE">
                        , nous vous conseillons vivement d'aller voir
                        <a>
                            <button class="btn btn-primary btn-sm"> notre guide</button>
                        </a>.
                    </p>
                    <p>
                    <span class="citation"><%if (moyenne != -1) {%>GediBox et PastaMat vous <% if (moyenne >= 13) {%>félicitent pour votre moyenne !<br>Continuez ainsi !<%} else if (moyenne < 13 && moyenne >= 8) {%>recommandent de travailler un peu plus !<br>Attention à vos notes !<%} else {%>incitent fortement à aller prendre des cours particuliers.<br>L'abandon est aussi une option possible vu votre médiocrité.<%
                            }
                        }
                    %></span><br>
                    </p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><%
                    if (moyenne != -1) {
                        if (moyenne >= 13) {
                %>Fermer et admirez votre moyenne<%} else if (moyenne < 13 && moyenne >= 8) {%>Fermer et commencer à
                    réviser<%} else {%>Fermer et Road to McDo<%
                        }
                    } else {
                    %>Fermer<%}%></button>
                <button type="button" class="btn btn-primary">Voir la documentation</button>
            </div>
        </div>
    </div>
</div>
<%}%>

<body style="display:flex; flex-direction:column;">
<div id="wrapper">
    <% if (isEtu){ %>
    <jsp:include page="commun/headerSimpleEtu.jsp"></jsp:include>
    <% }else{ %>
    <jsp:include page="commun/headerSimple.jsp"></jsp:include>
    <% } %>
    <div id="content">
        <% if (etudiant.getId() != null) {%>
        <div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly;">
            <div class="card" style="width: 30%;">
                <div class="card-header"><%= etudiant.getPrenom() + " " + etudiant.getNom()%>
                </div>
                <div class="card-body">
                    <h5 class="card-title"><%= etudiant.getGroupe().getNom() %>
                    </h5>
                    <div class="alert alert-<% if (etudiant.getNbAbsences() <= 0) {%>success<% } else { %>danger<% } %>"
                         role="alert">Nombre d'absence<% if (etudiant.getNbAbsences() > 0) {%>s<% }%>
                        : <%= etudiant.getNbAbsences() %>
                    </div>
                    <div class="alert alert-<% if (moyenne >= 14) {%>success<% } else if (moyenne < 14 && moyenne >= 7) { %>warning<% } else if (moyenne < 7 && moyenne != -1){ %>danger<% }else{ %>dark<%}%>"
                         role="alert"><% if (moyenne == -1) { %>Non noté<%} else {%> Moyenne
                        : <%= etudiant.getMoyenne() %><%}%></div>
                    <div>
                        <% for (Note note : etudiant.getNotes()) {

                            Prof prof = note.getControle().getProf();

                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM", Locale.FRANCE);
                            String date = sdf.format(note.getDate());
                            String dateModif = null;
                            if (note.getDateModif() != null) {
                                dateModif = sdf.format(note.getDateModif());
                            }
                        %>
                        <form class="form-floating">
                            <span style="margin-top: 15px;"
                                  class="position-absolute top-0 start-50 translate-middle badge rounded-pill bg-info">
                                    <%= date %><%if (dateModif != null && !dateModif.equals(date)) {%> - Modifié :
                                    <%=dateModif%><%}%>
                                </span>
                            <input style="padding-top: 3.625rem;padding-bottom: 1.625rem;" disabled type="text"
                                   class="form-control" id="floatingInputInvalid"
                                   placeholder="<%= note.getNote() + "/20 - Responsable : " +  prof.getPrenom() + " " + prof.getNom() %> "
                                   value="<%= note.getNote() + "/20 - Responsable : " +  prof.getPrenom() + " " + prof.getNom() %>">
                            <label style="transform: scale(.85) translateY(1rem) translateX(.15rem);"
                                   for="floatingInputInvalid"><%= note.getControle().getIntitule() + " - Coefficient " + note.getCoef() %>
                            </label>
                        </form>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>
        <% } else { %>

        <h2>L'étudiant n'a pas été trouvé</h2>

        <% }
            if (!isEtu) {%>
        <div style="justify-content: center;width: 100%;display: flex;margin-top: 50px;">
            <form action="<%= application.getContextPath()%>/do/editionetudiant" method="post">
                <input type="hidden" name="id" value="<%= etudiant.getId() %>">
                <input type="submit" class="btn btn-warning" value="Editer">
            </form>
        </div>
        <div style="justify-content: center;width: 100%;display: flex;margin-top: 50px;">
            <form action="<%= application.getContextPath()%>/do/listeEtudiants" method="post">
                <input type="submit" class="btn btn-primary" value="Retour à la liste">
            </form>
        </div>
        <%} else {%>
        <div style="justify-content: center;width: 100%;display: flex;margin-top: 50px;">
            <form action="<%= application.getContextPath()%>/do/login" method="post">
                <input type="submit" class="btn btn-primary" value="Se déconnecter">
            </form>
        </div>
        <%}%>
    </div>
    <jsp:include page="commun/footer.jsp"></jsp:include>
</div>
<%if (isEtu && !etuLogin.getHasConnected()) {%>
<script type='text/javascript' src="<%=application.getContextPath()%>/Ressources/javascript/introWebsite.js"
        charset="UTF-8"></script>
<%}%>
</body>
</html>
