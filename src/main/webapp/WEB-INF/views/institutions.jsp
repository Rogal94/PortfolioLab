<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
    <div class="contact">
        <h2>Lista Fundacji</h2>
        <a href="<c:url value="/admin/institutions/add"/>" class="btn">Dodaj</a>
        <c:forEach items="${institutionsList}" var="institution">
            <div class="form-group form-group--50" style="margin:2em; font-size:2em; border: 0.1em solid black">
                <span class="description">
                    <div class="title">Fundacja “<span>${institution.name}</span>”</div>
                    <div class="subtitle">
                        Cel i misja: ${institution.description}
                    </div>
                </span>
                <span>
                    <a href="<c:url value="/admin/institutions/edit/${institution.id}"/>" class="btn">Edytuj</a>
                    <a href="<c:url value="/admin/institutions/delete/${institution.id}"/>" class="btn">Usuń</a>
                </span>
            </div>
        </c:forEach>
    </div>
</section>
<%@include file="/WEB-INF/views/jspf/footer.jspf"%>
<script src="<c:url value="resources/js/app.js"/>"></script>
</body>
</html>

