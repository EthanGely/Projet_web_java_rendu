<%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 15/01/2023
  Time: 23:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer style="box-shadow: 0px 4px 20px 0px black;padding: 20px;margin-top: 50px;">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <img style="width: 100%;" src="${pageContext.request.contextPath}/Ressources/img/gedinote.png"
                     alt="Logo">
            </div>
            <div class="col-md-2 d-flex align-items-center">
                <div class="mx-auto">
                    <a id="btnDisconnect" href="<%= application.getContextPath()%>/do/Disconnect">
                        <button class="btn btn-danger">Déconnexion</button>
                    </a>
                </div>
            </div>
            <div class="col-md-6">
                <p>Copyright ©2022 Gedi'Note</p>
                <div class="row">
                    <div class="col-md-6">
                        <ul style="list-style-type: none;">
                            <li><a href="#">Plan du site</a></li>
                            <li><a href="<%= application.getContextPath()%>/do/Remerciements">Remerciements</a></li>
                        </ul>
                    </div>
                    <div class="col-md-6">
                        <ul style="list-style-type: none;">
                            <li><a href="#">Documentation</a></li>
                            <li><a href="#">Traitement des données</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>

