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
    <title>SUPER</title>
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
</head>
<body>
<header class="header--form-page">
    <%@include file="/WEB-INF/views/jspf/navigationLogin.jspf"%>
    <div class="slogan container container--90">
        <h2>
            Zalogowany jako admin.
        </h2>
    </div>
</header>
<section class="steps">
    <div class="contact" id="contact">
        <h2>Fundacja</h2>
        <form:form class="form--contact" modelAttribute="institution">
            <div class="form-group form-group--50" style="font-size: 2em">
                Nazwa:
                <form:input type="text" path="name" placeholder="Nazwa"/>
                <form:errors path="name" cssStyle="color: red; margin: 5px; font-size: 15px"/>
            </div>
            <div class="form-group form-group--50" style="font-size: 2em">
                Cel i misja:
                <form:input type="text" path="description" placeholder="Cel i misja"/>
                <form:errors path="name" cssStyle="color: red; margin: 5px; font-size: 15px"/>
            </div>
            <c:if test="${not empty institution.name}">
            <button class="btn" type="submit">Edytuj</button>
            </c:if>
            <c:if test="${empty institution.name}">
                <button class="btn" type="submit">Dodaj</button>
            </c:if>
        </form:form>
    </div>
</section>
<%@include file="/WEB-INF/views/jspf/footer.jspf"%>
<script src="<c:url value="resources/js/app.js"/>"></script>
</body>
</html>


