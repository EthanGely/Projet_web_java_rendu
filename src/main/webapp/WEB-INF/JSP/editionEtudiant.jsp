<%@ page import="fr.iut2.project1.Note.Note" %>
<%@ page import="fr.iut2.project1.Controle.Controle" %>
<%@ page import="fr.iut2.project1.Prof.Prof" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<jsp:useBean id="etudiant" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<jsp:useBean id="codeErreur" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="profLogin" type="fr.iut2.project1.Prof.Prof" scope="request"/>

<html>
<head>
    <title>GEDI'NOTE - Modification étudiant</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="commun/headerSimple.jsp"></jsp:include>
<% float moyenne = etudiant.getMoyenne();
    if (etudiant != null) {%>
<div style="display: flex; flex-wrap:wrap; width: 100%; justify-content: space-evenly;">
    <div class="card" style="width: 30%;">
        <div class="card-header"><%= etudiant.getPrenom() + " " + etudiant.getNom()%>
        </div>
        <div class="card-body">
            <h5 class="card-title"><%= etudiant.getGroupe().getNom() %>
            </h5>
            <form action="<%= application.getContextPath()%>/do/modifetudiant" method="post">
                <input type="hidden" name="id" value="<%= etudiant.getId() %>">
                <div class="mb-3">
                    <label for="nbAbs" class="form-label">Nombre d'absences</label>
                    <input type="number" name="nbAbs" min="0" value="<%= etudiant.getNbAbsences() %>"
                           class="form-control" id="nbAbs">
                </div>
                <hr style="margin: 2rem 0">
                <div class="mb-3">
                    <label for="moyenne" class="form-label">Moyenne générale</label>
                    <input disabled type="<%if (moyenne != -1){%>number<%} else {%>text<%}%>" step="0.01" name="moyenne"
                           min="0" max="20" value="<%if (moyenne != -1){%><%= moyenne %><%} else {%>Non noté<%}%>"
                           class="form-control" id="moyenne">
                </div>
                <div class="mb-3">
                    <% for (Note note : etudiant.getNotes()) {
                    Long idNote = note.getId();
                    Controle ctrl = note.getControle();
                    Prof prof = ctrl.getProf();
                    Long idProf = prof.getId();%>
                    <div class="mb-3" id="divNote-<%=idNote%>">
                        <label for="note-<%=idNote%>"><%= ctrl.getIntitule() %> - Coefficient <%=note.getCoef()%>
                        </label>
                        <div style="display: flex;">
                            <div class="form-floating"
                                 <% if (idProf.equals(profLogin.getId())){%>style="flex: 90% 0 1;"<%}%>>
                                <input <% if (!idProf.equals(profLogin.getId())){%>disabled<%}%>
                                       type="number" min="0" max="20" step="0.01" class="form-control"
                                       id="note-<%=idNote%>" name="note-<%=idNote%>"
                                       placeholder="<%= note.getNote() %>" value="<%= note.getNote() %>">
                                <label for="note-<%=idNote%>">Responsable
                                    : <%=prof.getPrenom() + " " + prof.getNom()%>
                                </label>
                            </div>
                            <% if (idProf.equals(profLogin.getId())) {%>
                            <div style="margin: auto">
                                <button type="button"
                                        onclick="deleteGrade(<%= idNote %>)"
                                        class="btn btn-danger"
                                        style="padding: 17px 5px;font-size: 15px;vertical-align: initial;">❌
                                </button>
                            </div>
                            <%}%>
                        </div>
                    </div>
                    <%}%>
                </div>
                <div style="width: 100%;display: flex;justify-content: center;">
                    <input type="submit" class="btn btn-success" value="Enregistrer">
                </div>
            </form>
        </div>
    </div>
</div>
<% } else { %>

<h2>L'étudiant n'a pas été trouvé</h2>

<% } %>
<div style="justify-content: center;width: 100%;display: flex;margin-top: 50px;">
    <form action="<%= application.getContextPath()%>/do/FicheEtudiant" method="post">
        <input type="hidden" name="id" value="<%= etudiant.getId() %>">
        <input type="submit" class="btn btn-warning" value="annuler">
    </form>
</div>
<div style="justify-content: center;width: 100%;display: flex;margin-top: 50px;">
    <form action="<%= application.getContextPath()%>/do/listeEtudiants" method="post">
        <input type="submit" class="btn btn-primary" value="Retour à la liste">
    </form>
</div>
<jsp:include page="commun/footer.jsp"></jsp:include>
</body>
<% if (codeErreur != 0) {%>
<script type="text/javascript">alert("<% if (codeErreur == 1 || codeErreur == 3){%>Le nombre d'abscences doit être supérieur ou égal à <%= etudiant.getNbAbsences()%>.\n<%} if(codeErreur == 2 || codeErreur == 3){%>La moyenne doit être comprise entre 0 et 20.<%}else{%><%= codeErreur%><%}%>")</script>
<%}%>
</html>

