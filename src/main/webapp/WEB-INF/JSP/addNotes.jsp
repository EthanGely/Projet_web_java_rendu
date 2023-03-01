<%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 12/01/2023
  Time: 16:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="fr.iut2.project1.Etudiant.Etudiant" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Liste des étudiants, si besoin filtrée (il faut afficher tous les étudiants de cette liste) -->
<jsp:useBean id="etudiants" type="java.util.Collection<fr.iut2.project1.Etudiant.Etudiant>" scope="request"/>
<jsp:useBean id="filtre" class="java.lang.String" scope="request"/>
<jsp:useBean id="recherche" type="java.lang.String" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>GEDI'NOTE - Ajouter des notes</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<body onload="changerGroupe()">
<jsp:include page="commun/headerRecherche.jsp"></jsp:include>
<form action="<%= application.getContextPath()%>/do/validerNotes">
    <input type="hidden" name="filtre" value="<%= filtre %>">
    <input type="hidden" name="nom" value="<%= recherche %>">
    <div style="display: flex;margin: 30px auto 0 auto;width: 75%">
        <div class="mb-3" style="margin-left: auto;margin-right: auto;">
            <label for="coefficient" class="form-label">Coefficient de la note</label>
            <input type="number" class="form-control" id="coefficient" value="1" name="coef">
        </div>
        <div class="mb-3" style="margin-left: auto;margin-right: auto;">
            <label for="libelle" class="form-label">Libellé de la note</label>
            <input type="text" class="form-control" id="libelle" placeholder="Libellé" name="libelle">
        </div>
    </div>

    <!-- Tableau étudiants -->
    <div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly; padding: 0rem 5rem;">
        <table class="table table-striped">
            <thead>
            <tr>
                <th scope="col">Etudiant</th>
                <th scope="col">Groupe</th>
                <th scope="col">Moyenne actuelle</th>
                <th scope="col">Absent</th>
                <th scope="col">Nouvelle note</th>
            </tr>
            </thead>
            <tbody>

            <!-- Itération sur la liste des étudiants -->
            <% for (Etudiant etu : etudiants) { %>

            <tr>
                <td><%= etu.getPrenom() + " " + etu.getNom()%>
                </td>
                <td><%=etu.getGroupe().getNom()%>
                </td>
                <td><%if (etu.getMoyenne() == -1) {%>Non noté<%} else {%><%= etu.getMoyenne()%><%}%>
                </td>
                <td>
                    <div class="form-check form-switch">
                        <input onclick="isAbsent(<%=etu.getId()%>)" class="form-check-input absCheckBox" type="checkbox"
                               role="switch" id="isAbsent-<%= etu.getId()%>" name="isAbsent-<%= etu.getId()%>">
                        <label class="form-check-label" for="isAbsent-<%= etu.getId()%>">Absent</label>
                    </div>
                </td>
                <td><input class="form-control" type="number" id="note-<%= etu.getId()%>" name="note-<%= etu.getId()%>"
                           min="0" max="20" step="0.01"
                           required placeholder="Nouvelle note"></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <div style="display: flex;margin-top: 30px;">
        <div class="mb-3" style="margin-left: auto;margin-right: auto;">
            <button class="btn btn-success" type="submit" onclick="checkAbs()">Valider</button>
        </div>
    </div>
</form>
<jsp:include page="commun/footer.jsp"></jsp:include>
<script>function checkAbs() {
    let listeAbs = document.getElementsByClassName("absCheckBox");
    let count = 0;
    for (let i = 0; i < listeAbs.length; i++) {
        if (listeAbs[i].checked) {
            count += 1;
        }
    }
    if (count === <%=etudiants.size()%>) {
        alert("Mettre tous les élèves absents à un contrôle est assez inutile.\nNous avons donc pris le soin de vous déconnecter.\nDe cette manière, vous avez une nouvelle raison pour perdre plus de temps.");
        document.getElementById("btnDisconnect").click();
    }
}</script>
</body>
</html>
