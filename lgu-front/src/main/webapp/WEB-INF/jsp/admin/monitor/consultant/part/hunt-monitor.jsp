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

<form:form id="hunt-monitor" modelAttribute="search" class="panel -ajax-loader">
    <div class="panel-heading">
        <label class="control-label">모니터링</label>
            <%--<div class="pull-right">
                <div class="ui action input">
                    <form:input path="huntName" placeholder="큐(그룹)명"/>
                    <button type="submit" class="ui icon button">
                        <i class="search icon"></i>
                    </button>
                </div>
            </div>--%>
    </div>
    <div class="panel-body">
        <table class="ui table celled fixed structured">
            <thead>
            <tr>
                <th>큐그룹명</th>
                <th>고객대기</th>

                <c:forEach var="status" items="${statuses}">
                    <c:if test="${status.key != 9}">
                        <th>${g.htmlQuote(status.value)}</th>
                    </c:if>
                </c:forEach>

                <th>로그인</th>
                <th>로그아웃</th>
                <th>전체</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${list.size() > 0}">
                    <c:forEach var="e" items="${list}">
                        <tr>
                            <td>${g.htmlQuote(e.queueHanName)}</td>
                            <td class="-custom-wait-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.customWait}</td>

                            <c:forEach var="status" items="${statuses}">
                                <c:if test="${status.key != 9}">
                                    <td class="-consultant-status-count" data-value="${status.key}"
                                        data-hunt="${g.htmlQuote(e.queueName)}">${e.statusCountMap.getOrDefault(status.key, 0)}</td>
                                </c:if>
                            </c:forEach>

                            <td class="-login-user-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.loginCount}</td>
                            <td class="-logout-user-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.logoutCount}</td>
                            <td class="-total-user-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.total}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <c:set var="statuseSize" value="${statuses.size()}"/>
                        <td colspan="${5 + statuseSize - 1}" class="null-data">조회된 데이터가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</form:form>

<script>
    (function () {
        if (window.queues) {
            <c:forEach var="e" items="${list}">
            if (!queues['${g.escapeQuote(e.queueName)}']) queues['${g.escapeQuote(e.queueName)}'] = {name: '${g.escapeQuote(e.queueName)}', hanName: '${g.escapeQuote(e.queueHanName)}', peer: []};
            queues['${g.escapeQuote(e.queueName)}'].waitingCustomerCount = ${e.customWait};
            </c:forEach>

            if (updateQueues)
                updateQueues();
        }
    })();
</script>
