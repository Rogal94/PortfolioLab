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
        <div class="slogan--item">
            <h1>
                Oddaj rzeczy, których już nie chcesz<br />
                <span class="uppercase">potrzebującym</span>
            </h1>

            <div class="slogan--steps">
                <div class="slogan--steps-title">Wystarczą 4 proste kroki:</div>
                <ul class="slogan--steps-boxes">
                    <li>
                        <div><em>1</em><span>Wybierz rzeczy</span></div>
                    </li>
                    <li>
                        <div><em>2</em><span>Spakuj je w worki</span></div>
                    </li>
                    <li>
                        <div><em>3</em><span>Wybierz fundację</span></div>
                    </li>
                    <li>
                        <div><em>4</em><span>Zamów kuriera</span></div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</header>
<section class="steps">
    <div class="contact">
        <h2>Lista Zbiórek</h2>
        <a href="<c:url value="/form"/>" class="btn">Dodaj</a>
            <div class="form-group form-group--50" style="margin:2em; font-size:2em; border: 0.1em solid black">
                <span class="description">
                    <div class="title">Fundacja “<span>${donation.institution.name}</span></div>
                    <div class="title">Dary:
                        <span>
                            <c:forEach items="${donation.categories}" var="category">
                                <span> ${category.name}</span>
                            </c:forEach>
                    </div>
                    <div class="title">Liczba worków: <span>${donation.quantity}</span></div>
                    <div class="title">Data odbioru: <span>${donation.pickUpDate} ${donation.pickUpTime}</span></div>
                    <div class="subtitle">
                        Status: <span>${donation.status}</span>
                    </div>
                    <c:if test="${not empty donation.received}">
                        <div class="subtitle">
                            Odebrano: <span>${donation.received}</span>
                        </div>
                    </c:if>
                    <div class="subtitle">
                        Dodano: <span>${donation.created}</span>
                    </div>
                </span>
                <span>
                    <a href="<c:url value="/user/donations/details/${donation.id}"/>" class="btn">Wróć</a>
                    <c:if test="${donation.status.equals('nieodebrane')}">
                        <a href="<c:url value="/user/donations/received/${donation.id}"/>" class="btn">Odebrane</a>
                    </c:if>
                </span>
            </div>
    </div>
</section>
<%@include file="/WEB-INF/views/jspf/footer.jspf"%>
<script src="<c:url value="resources/js/app.js"/>"></script>
</body>
</html>


