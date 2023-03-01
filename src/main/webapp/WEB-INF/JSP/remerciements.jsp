<%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 18/01/2023
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="etuLogin" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<jsp:useBean id="profLogin" class="fr.iut2.project1.Prof.Prof" scope="request"/>
<% boolean isEtu = true;
    try {
        if (etuLogin.getMail() == null || etuLogin.getMail().equals("")) {
            isEtu = false;
        }
    } catch (Exception e) {
        isEtu = false;
    }%>
<html>
<head>
    <title>GEDI'NOTE - Remerciements</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<body>
<% if (isEtu){ %>
<jsp:include page="commun/headerSimpleEtu.jsp"></jsp:include>
<% }else{ %>
<jsp:include page="commun/headerSimple.jsp"></jsp:include>
<% } %>
<div style="width: 90%; margin: 0 auto;">
    <p>Je tiens à remercier chaleureusement Edwige et Tiffany pour la réalisation respective de GediBox et de
        PastaMat.</p>
    <div style="display: flex; flex-wrap: nowrap; width: 100%; justify-content: center;">
        <img style="max-width: 200px; width: 100%; height: auto;"
             src="${pageContext.request.contextPath}/Ressources/img/PastaMat.png" alt="">
        <img style="max-width: 200px; width: 100%; height: auto;"
             src="${pageContext.request.contextPath}/Ressources/img/GediBox.png" alt="">
    </div>
    <p>Sans elles, ce projet aurait été bien plus sérieux, sans compter le temps gagné à ne pas intégrer ces magnifiques
        mascottes.</p>
    <br>
    <p>Je me doute bien que personne ne va cliquer sur un lien "remerciements" (en même temps, il n'y a que vous et moi
        qui avont accès à ce site), mais bon...<br>Je suis quand même sûr à 100% que vous avez cliqué et lu au moins une
        partie de ce message.</p>
    <br>
    <p>J'ai demandé à une IA (oui, c'est ChatGPT...) de générer du code pour avoir des meilleures notes, dont voici un
        extrait :</p>
    <pre>
        <code class="language-java">
            List&lt;Student&gt; students = StudentDAO.getAllStudents();

            for (Student student : students) {
                Project project = student.getProject("Projet_Web_Java");
                float grade;
                if (student.getFirstName().equals("Ethan")) {
                    grade = 20.0f;
                } else {
                    grade = ProfDAO.getCurrentProf().getNoteFromProject(project);
                }
                student.setGrade(project, grade);
            }
        </code>
    </pre>
    <p>En revanche, étant donné que ce code ne me semble pas très équitable, je ne l'ai pas intégré dans cette
        application.</p>
</div>
<jsp:include page="commun/footer.jsp"></jsp:include>
<script type='text/javascript' src="<%=application.getContextPath()%>/Ressources/javascript/prism.js"></script>
</body>
</html>