<%--
  Created by IntelliJ IDEA.
  User: ethan.gely
  Date: 14/01/2023
  Time: 12:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="erreur" type="java.lang.Integer" scope="request"/>
<html>
<head>
    <title>GEDI'NOTE - Login</title>
    <jsp:include page="commun/head.jsp"></jsp:include>
</head>
<body>
<header style="display: flex;justify-content: center;padding: 30px 10px;">
    <img style="width: 40%;" src="${pageContext.request.contextPath}/Ressources/img/gedinote.png"/>
</header>
<% if (erreur != 0) {%>
<div class="alert alert-<%if(erreur == 1){%>warning<%}else{%>danger<%}%>" role="alert"
     style="margin: 0 auto;width: fit-content;">
    <%if (erreur == 1) {%>Aucun utilisateur ne correspond à cette adresse mail.<%} else {%>Mot de passe éronné !<%}%>
</div>
<%}%>

<div class="container">
    <div class="row">
        <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
            <div class="card card-signin my-5">
                <div class="card-body">
                    <h5 class="card-title text-center">Se connecter</h5>
                    <form class="form-signin" action="<%= application.getContextPath()%>/do/verifLogin" method="post">
                        <div class="form-floating mb-3">
                            <input type="email" class="form-control" id="champ_1" name="mail"
                                   placeholder="nom@example.com" onkeyup="checkFields(2)">
                            <label for="champ_1">Adresse e-mail</label>
                        </div>

                        <div class="form-floating">
                            <input type="password" class="form-control" id="champ_2" name="pass"
                                   placeholder="Mot de passe" onkeyup="checkFields(2)">
                            <button type="button"
                                    style="position: absolute;right: 15px;top: 35%; background: transparent; border: none"
                                    onclick="togglePasswordVisibility()">
                                <i class="fa fa-eye-slash" id="eye-img"></i>
                            </button>
                            <label for="champ_2">Mot de passe</label>
                        </div>
                        <div class="col-12" style="display: flex; justify-content: center; margin-top: 10px;">
                            <button style="position: relative;transition: all 0.3s;" onmouseover="moveBtn(2)"
                                    class="btn btn-primary" id="btnValider" type="submit">Se connecter
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
