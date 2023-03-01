<%@ page import="fr.iut2.project1.Groupe.Groupe" %>
<%@ page import="fr.iut2.project1.Groupe.GroupeDAO" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="recherche" type="java.lang.String" scope="request"/>
<jsp:useBean id="allGroupes" type="java.util.Collection<fr.iut2.project1.Groupe.Groupe>" scope="request"/>
<jsp:useBean id="isGroupeFiltre" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="groupesFiltre" type="java.util.Collection<fr.iut2.project1.Groupe.Groupe>" scope="request"/>
<jsp:useBean id="filtre" class="java.lang.String" scope="request"/>
<jsp:useBean id="profLogin" class="fr.iut2.project1.Prof.Prof" scope="request"/>
<header>
    <form id="formHeader" class="" role="search" action="<%= application.getContextPath()%>/do/RechercheEtudiant"
          method="post">
        <nav class="d-flex navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <a class="navbar-brand" href="<%= application.getContextPath()%>/do/listeEtudiants"
                   style="margin-right: 50px; width: 170px;">
                    <img style="width: inherit;" src="${pageContext.request.contextPath}/Ressources/img/gedinote.png"/>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link  <% if (request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1].equals("listeEtudiants.jsp")) { %>active<%}%>"
                               href="<%= application.getContextPath()%>/do/listeEtudiants">Liste des étudiants</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link <% if (request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1].equals("addNotes.jsp")) { %>active<%}%>"
                               href="<%= application.getContextPath()%>/do/addNotes">Ajouter des notes</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link <% if (request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1].equals("listeControles.jsp")) { %>active<%}%>"
                               href="<%= application.getContextPath()%>/do/listeControles">Voir les contrôles</a>
                        </li>
                    </ul>
                    <input style="width: 25%;" class="form-control me-2" type="search"
                           placeholder="Rechercher un étudiant"
                           aria-label="rechercher" name="nom" <% if (recherche != ""){%>value="<%= recherche %>"<%}%> >
                    <input type="hidden" name="who"
                           value="<%=request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1]%>">
                    <button class="btn btn-outline-success" type="submit">Rechercher</button>
                    <a href="<%= application.getContextPath()%>/do/profilUser">
                        <button type="button"
                                style="width: 37.6px;height: 37.6px;border-radius: 18.8px;background-color: #0d6efd;color: white;text-align: center;">
                            <i class="fas fa-user"></i>
                        </button>
                    </a>
                </div>
            </div>
        </nav>
        <br/>
        <!-- Filtres -->
        <div style="box-shadow: 0px 4px 20px -20px black;padding: 20px 0;">
            <div style="display: flex; justify-content: space-around;">
                <% if (profLogin.isAdmin()) { %>
                <div style="width: 30%;">
                    <h3 style="text-align: center">Filtres</h3>
                    <div style="display: flex; flex-direction: row; justify-content: space-around">
                        <div style="width: 100%;">
                            <div class="form-check form-switch" style="margin: 0 auto;width: fit-content;">
                                <input class="form-check-input" type="checkbox" role="switch" id="allClasses"
                                       name="allClasses"
                                       <% if (isGroupeFiltre == 0) {%>checked<%}%> onclick="changerGroupe()">
                                <label class="form-check-label" for="allClasses">Tous les groupes</label>
                            </div>
                            <div id="selectGroupes"
                                 style="position: relative;left: 20px;justify-content: space-between; display: flex; visibility: hidden">
                                <% for (Groupe gr : allGroupes) { %>
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" role="switch"
                                           id="<%= gr.getNom() %>"
                                           name="<%= gr.getNom() %>"
                                           <% if (isGroupeFiltre == 1 && groupesFiltre.contains(gr)) {%>checked<%}%>>
                                    <label class="form-check-label" for="<%= gr.getNom() %>"><%= gr.getNom() %>
                                    </label>
                                </div>
                                <%}%>
                            </div>
                        </div>
                    </div>

                </div>
                <%}%>
                <%if (filtre != null) {%>
                <div style="width: 30%;">
                    <h3 style="text-align: center;">Afficher</h3>
                    <div style="display: flex; flex-direction: row; justify-content: space-around">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="fMoyenne" name="fMoyenne"
                                   <%if (filtre.charAt(filtre.length() - 1) == '1') { %>checked<%}%>>
                            <label class="form-check-label" for="fMoyenne">Moyenne</label>
                        </div>
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="fAbs" name="fAbs"
                                   <%if (filtre.charAt(filtre.length() - 2) == '1') { %>checked<%}%>>
                            <label class="form-check-label" for="fAbs">Absences</label>
                        </div>
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="fGroupeEtu"
                                   name="fGroupeEtu"
                                   <%if (filtre.charAt(filtre.length() - 3) == '1') { %>checked<%}%>>
                            <label class="form-check-label" for="fAbs">Groupe</label>
                        </div>
                    </div>
                </div>
                <%}%>
            </div>

            <div style="display: flex; justify-content: center; margin-top: 10px;">
                <input type="submit" class="btn btn-primary" value="Appliquer les filtres">
            </div>
        </div>
        <input type="hidden" name="isFiltre" value="on">
    </form>
</header>