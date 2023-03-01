<%@ page import="fr.iut2.project1.Groupe.Groupe" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!-- Liste des groupes -->
<jsp:useBean id="allGroupes" type="java.util.Collection<fr.iut2.project1.Groupe.Groupe>" scope="request"/>
<jsp:useBean id="profLogin" class="fr.iut2.project1.Prof.Prof" scope="request"/>

<html>
<head>
    <title>GEDI'NOTE - Ajouter un étudiant</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="commun/headerSimple.jsp"></jsp:include>
<form action="<%= application.getContextPath()%>/do/CreationEtudiant" method="post">
    <div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly;">
        <div class="card" style="width: 30%;">
            <div class="card-header">
                <div class="input-group mb-3">
                    <input type="text" name="prenom" class="form-control" placeholder="Prénom" required>
                    <span class="input-group-text">&nbsp;</span>
                    <input type="text" name="nom" class="form-control" placeholder="Nom" required>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-title">
                    <div id="selectGroupes" style="position: relative;left: 20px;">
                        <% if (profLogin.isAdmin()) { %>
                        <select class="form-select" name="groupe" required aria-label="select"
                                style="width: 90%;">
                            <% for (Groupe gr : allGroupes) { %>
                            <option value="<%= gr.getNom() %>"><%= gr.getNom() %>
                            </option>
                            <%}%>
                        </select>
                        <% } else {
                            Groupe gr = profLogin.getGroupe();
                        %>
                        <select class="form-select" name="groupe" required disabled aria-label="select"
                                style="width: 90%;">
                            <option value="<%= gr.getNom() %>"><%= gr.getNom() %>
                            </option>
                        </select>
                        <% } %>
                    </div>
                </h5>
                <div style="width: 100%;display: flex;justify-content: center;">
                    <input type="submit" class="btn btn-success" value="Créer l'étudiant">
                </div>
            </div>
        </div>
    </div>
</form>
<div style="justify-content: center;width: 100%;display: flex;margin-top: 50px;">
    <form action="<%= application.getContextPath()%>/do/listeEtudiants" method="post">
        <input type="submit" class="btn btn-primary" value="Retour à la liste">
    </form>
</div>
<jsp:include page="commun/footer.jsp"></jsp:include>
</body>
</html>

