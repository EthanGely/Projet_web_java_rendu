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
<jsp:useBean id="controle" class="fr.iut2.project1.Controle.Controle" scope="request"/>
<%
    float moyenne = controle.getMoyenne();
    String intitule = controle.getIntitule();
    Long id = controle.getId();
    Prof prof = controle.getProf();
    String responsable = prof.getPrenom() + " " + prof.getNom();
    int coef = controle.getListeNotes().get(0).getCoef();
    String date = controle.getFormatedDate();
%>
<html>
<head>
    <title>GEDI'NOTE - Fiche étudiant</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>

<body style="display:flex; flex-direction:column;">
<div id="wrapper">
    <jsp:include page="commun/headerSimple.jsp"></jsp:include>
    <div id="content">
        <% if (id != null) {
        %>
        <div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly;">
            <div class="card" style="width: 30%;">
                <div class="card-header">Responsable : <%=responsable%>
                </div>
                <div class="card-body">
                    <h5 class="card-title"><%= intitule %></h5>
                    <h6>Coefficient <%=coef%></h6>
                    <p><span style="margin-top: 15px;"
                             class="badge rounded-pill bg-info">
                                    <%= date %>
                                </span></p>
                    <div class="alert alert-<% if (moyenne >= 14) {%>success<% } else if (moyenne < 14 && moyenne >= 7) { %>warning<% } else if (moyenne < 7 && moyenne != -1){ %>danger<% }else{ %>dark<%}%>"
                         role="alert"><% if (moyenne == -1) { %>Non noté<%} else {%> Moyenne du contrôle
                        : <%= moyenne %><%}%></div>
                    <div>
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Étudiant</th>
                                <th scope="col">Note</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                for (Note note : controle.getListeNotes()) {
                                    Etudiant etu = note.getEtudiant();
                                    String prenomNom = etu.getPrenom() + " " + etu.getNom();
                                    float grade = note.getNote();
                            %>
                            <tr>
                                <td><%=prenomNom%>
                                </td>
                                <td><%=grade%>
                                </td>
                            </tr>
                            <%
                                }
                            %>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <% } else { %>

        <h2>Le contrôle n'a pas été trouvé</h2>

        <% } %>
    </div>
    <jsp:include page="commun/footer.jsp"></jsp:include>
</div>
</body>
</html>
