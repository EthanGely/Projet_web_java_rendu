<%@ page import="fr.iut2.project1.Note.Note" %>
<%@ page import="fr.iut2.project1.Controle.Controle" %><%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 01/02/2023
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="controles" type="java.util.Collection<fr.iut2.project1.Controle.Controle>" scope="request"/>
<html>
<head>
    <title>GEDI'NOTE - Liste des contrôles</title>
  <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="commun/headerSimple.jsp"></jsp:include>
<div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly; padding: 0rem 5rem;">
  <!-- Itération sur la liste des étudiants -->
  <% for (Controle ctrl : controles) {
  %>
  <div class="card" style="width: 20%; margin: 1.25rem 2.5rem;">
    <div class="card-header" style="display: flex;">
      <div><%= ctrl.getProf().getPrenom() + " " + ctrl.getProf().getNom() %></div>
    </div>
    <div class="card-body">
      <h5 class="card-title"><%= ctrl.getIntitule() %>
      </h5>
      <div>
        <p class="card-text">Moyenne : <%=ctrl.getMoyenne()%>/20</p>
      </div>
      <div style="display: flex; justify-content: space-evenly;">
        <form action="<%= application.getContextPath()%>/do/DetailControle" method="post"
              style="display: flex; padding-top: 20px;">
          <input type="hidden" name="id" value="<%= ctrl.getId() %>">
          <input type="submit" class="btn btn-success" value="Voir les détails" style="margin: 0 auto">
        </form>
      </div>
    </div>
  </div>
  <% } %>
</div>
<jsp:include page="commun/footer.jsp"></jsp:include>
</body>
</html>
