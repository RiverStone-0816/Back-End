<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div id="service-stat">
    <table class="ui celled table compact unstackable">
        <thead>
        <tr>
            <th>서비스명</th>
            <th>수신콜</th>
            <th>무효콜</th>
            <th>연결요청</th>
            <th>응답호</th>
            <th>포기호</th>
            <th>응답율</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${data.services}">
            <tr >
                <td>${g.htmlQuote(e.serviceName)}</td>
                <td>${e.totalCall}</td>
                <td>${e.onlyReadCall}</td>
                <td>${e.connectionRequest}</td>
                <td>${e.successCall}</td>
                <td>${e.cancelCall}</td>
                <td>${String.format("%.1f", e.responseRate)}%</td>
            </tr>
        </c:forEach>
        <tr class="total-tr">
            <td>합계</td>
            <td>${data.services.stream().map(e -> e.totalCall).sum()}</td>
            <td>${data.services.stream().map(e -> e.onlyReadCall).sum()}</td>
            <td>${data.services.stream().map(e -> e.connectionRequest).sum()}</td>
            <td>${data.services.stream().map(e -> e.successCall).sum()}</td>
            <td>${data.services.stream().map(e -> e.cancelCall).sum()}</td>
            <td>${String.format("%.1f", data.services.stream().map(e -> e.responseRate).average().orElse(0.0))}%</td>
        </tr>
        </tbody>
    </table>
</div>
