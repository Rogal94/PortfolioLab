<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Document</title>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
</head>
<body>
<header>
    <%@include file="/WEB-INF/views/jspf/navigation.jspf"%>
</header>

<section class="login-page">
    <h2>Zaloguj się</h2>
    <form method="post" action="/login">
        <div class="form-group">
            <input type="email" name="email" placeholder="Email" required/>
        </div>
        <div class="form-group">
            <input type="password" name="password" placeholder="Hasło" required/>
            <a href="<c:url value="/password"/>" class="btn btn--small btn--without-border reset-password">Przypomnij hasło</a>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <c:if test="${error.equals('error')}">
            <p style="color: red; margin: 5px; font-size: 15px">INVALID LOGIN OR PASSWORD! </p>
        </c:if>
        <c:if test="${error.equals('blocked')}">
            <p style="color: red; margin: 5px; font-size: 15px">ACCOUNT BLOCKED! </p>
        </c:if>
        <c:if test="${error.equals('disabled')}">
            <p style="color: red; margin: 5px; font-size: 15px">ACCOUNT NOT ACTIVE! </p>
        </c:if>
        <c:if test="${error.equals('tokenEnabled')}">
            <p style="color: cornflowerblue; margin: 5px; font-size: 15px">CONFIRMATION EMAIL WAS SEND! </p>
        </c:if>
        <c:if test="${error.equals('enabled')}">
            <p style="color: cornflowerblue; margin: 5px; font-size: 15px">YOUR ACCOUNT IS ACTIVE NOW! </p>
        </c:if>
        <c:if test="${error.equals('unValid')}">
            <p style="color: red; margin: 5px; font-size: 15px">TOKEN EXPIRED! </p>
        </c:if>
        <c:if test="${error.equals('tokenPassword')}">
            <p style="color: cornflowerblue; margin: 5px; font-size: 15px">EMAIL WITH PASSWORD RESET WAS SEND! </p>
        </c:if>
        <c:if test="${error.equals('noExist')}">
            <p style="color: red; margin: 5px; font-size: 15px">USER IS NOT EXIST! </p>
        </c:if>
        <c:if test="${error.equals('success')}">
            <p style="color: cornflowerblue; margin: 5px; font-size: 15px">PASSWORD CHANGE SUCCESSFUL! </p>
        </c:if>
        <div class="form-group form-group--buttons">
            <a href="<c:url value="/register"/>" class="btn btn--without-border">Załóż konto</a>
            <button class="btn" type="submit">Zaloguj się</button>
        </div>
    </form>
</section>

<%@include file="/WEB-INF/views/jspf/footer.jspf"%>
<script src="<c:url value="resources/js/app.js"/>"></script>
</body>
</html>

