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

<div class="pd10">
    <label class="panel-label">상담원 현황</label>
</div>
<div class="ui internally celled grid compact">
    <div class="row">
        <div class="sixteen wide column">
            <table class="ui celled table compact unstackable border-top-default">
                <thead>
                <tr>
                    <th>총원</th>
                    <c:forEach var="e" items="${statusCodes}">
                        <th>${g.htmlQuote(e.value)}</th>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${integrationData.constantStatusCounts.values().stream().sum()}</td>
                        <c:forEach var="e" items="${statusCodes}">
                            <td>${integrationData.constantStatusCounts.getOrDefault(e.key, 0)}</td>
                        </c:forEach>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="six wide column">
            <label class="panel-label">MY CALL 현황(금일)</label>
        </div>
        <div class="ten wide column">
            <label class="panel-label">상담 그룹 현황</label>
        </div>
    </div>
    <div class="row">
        <div class="six wide column">
            <table class="ui celled table compact unstackable border-top-default">
                <tr>
                    <th>수신</th>
                </tr>
                <tr>
                    <td class="-stat-my-call" data-field="inTotal"></td>
                </tr>
                <tr>
                    <th>콜백</th>
                </tr>
                <tr>
                    <td class="-stat-my-call" data-field="callback"></td>
                </tr>
                <tr>
                    <th>발신</th>
                </tr>
                <tr>
                    <td class="-stat-my-call" data-field="outSuccess"></td>
                </tr>
                <tr>
                    <th>응대율</th>
                </tr>
                <tr>
                    <td class="-stat-my-call" data-field="successPer" data-fixed="1"></td>
                </tr>
            </table>
        </div>
        <div class="ten wide column">
            <table class="ui celled table compact unstackable border-top-default">
                <thead>
                <tr>
                    <th>상담그룹</th>
                    <th>대기고객</th>
                    <th>대기상담</th>
                    <th>통화불가</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="e" items="${huntData.hunts}">
                    <tr>
                        <td>${g.htmlQuote(e.queueKoreanName)}</td>
                        <td>${e.customerWaiting}</td>
                        <td>${e.constantStatusCounts.getOrDefault(0, 0)}</td>
                        <td>${e.constantStatusCounts.getOrDefault(1, 0)
                                + e.constantStatusCounts.getOrDefault(1, 0)
                                + e.constantStatusCounts.getOrDefault(2, 0)
                                + e.constantStatusCounts.getOrDefault(3, 0)
                                + e.constantStatusCounts.getOrDefault(4, 0)
                                + e.constantStatusCounts.getOrDefault(5, 0)
                                + e.constantStatusCounts.getOrDefault(6, 0)
                                + e.constantStatusCounts.getOrDefault(7, 0)
                                + e.constantStatusCounts.getOrDefault(8, 0)
                                + e.constantStatusCounts.getOrDefault(9, 0)}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
