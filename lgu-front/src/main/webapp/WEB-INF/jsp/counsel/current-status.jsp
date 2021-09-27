<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<table class="ui x-small table celled unstackable" id="current-status-sheet">
    <tbody>
        <tr>
            <td>채널</td>
            <td>접수</td>
            <td>처리</td>
            <td>처리율</td>
        </tr>
    <c:forEach var="e" items="${list}">
        <c:if test="${todoKinds.contains(e.todoKind)}">
            <tr data-name="${g.htmlQuote(e.todoKind)}" data-value="${e.successRate}">
                <td>${g.htmlQuote(message.getEnumText(e.todoKind))}</td>
                <td>${e.total}</td>
                <td>${e.success}</td>
                <td>${e.successRate}%</td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>

<tags:scripts>
    <script>
        $(window).on('load', function () {
            setAlertCurrentStatus();
        });
    </script>
</tags:scripts>

<script>
    if (window.setAlertCurrentStatus)
        setAlertCurrentStatus();
</script>
