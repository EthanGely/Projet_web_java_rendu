<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="etuLogin" class="fr.iut2.project1.Etudiant.Etudiant" scope="request"/>
<header style="box-shadow: 0 4px 20px -20px black; margin-bottom: 50px;">
    <nav id="formHeader" class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand"
               href="<%= application.getContextPath()%>/do/FicheEtudiant"
               style="margin-right: 50px; width: 170px;"><img style="width: inherit;" src="${pageContext.request.contextPath}/Ressources/img/gedinote.png" alt="Logo Gedi'Note"/></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link <% if (request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1].equals("ficheEtudiant.jsp")) { %>active<%}%>"
                           href="<%= application.getContextPath()%>/do/FicheEtudiant">Détail des notes</a>
                    </li>
                </ul>
            </div>
            <a href="<%= application.getContextPath()%>/do/profilUser">
                <button type="button"
                        style="width: 37.6px;height: 37.6px;border-radius: 18.8px;background-color: #0d6efd;color: white;text-align: center;">
                    <i class="fas fa-user"></i>
                </button>
            </a>
        </div>
    </nav>
</header>