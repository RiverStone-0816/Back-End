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

<div id="queue-stat">
    <table class="ui celled table compact unstackable">
        <thead>
        <tr>
            <th>큐그룹명</th>
            <th>고객대기</th>
            <c:forEach var="status" items="${memberStatuses}">
                <c:if test="${status.key != 9}">
                    <th>${status.value}</th>
                </c:if>
            </c:forEach>
            <th>로그인</th>
            <th>로그아웃</th>
            <th>전체</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${queueSummaries}">
            <tr>
                <td>${g.htmlQuote(e.queue.name)}</td>
                <td class="-custom-wait-count" data-hunt="${g.htmlQuote(e.queue.name)}">${e.customWaitCnt}</td>
                <c:forEach var="status" items="${memberStatuses}">
                    <c:if test="${status.key != 9}">
                        <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queue.name)}">${e.statusToUserCount.getOrDefault(status.key, 0)}</td>
                    </c:if>
                </c:forEach>
                <td class="-login-user-count" data-hunt="${g.htmlQuote(e.queue.name)}">${e.loginUser}</td>
                <td class="-logout-user-count" data-hunt="${g.htmlQuote(e.queue.name)}">${e.logoutUser}</td>
                <td>${e.totalUser}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
