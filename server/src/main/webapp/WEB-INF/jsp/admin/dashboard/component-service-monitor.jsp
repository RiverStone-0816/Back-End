<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div id="component-service-monitor">
    <div class="ui segment">
        <h5 class="ui header">고객대기자수 모니터링</h5>
        <table class="ui celled table striped small compact fixed">
            <tbody>
            <tr>
                <td>고객대기</td>
                <td>
                    <text class="-custom-wait-count">${customWaitMonitor.customWaitCnt}</text>
                    명
                </td>
            </tr>
            <tr>
                <td>상담대기</td>
                <td>
                    <text class="-consultant-status-count" data-value="0" data-login="true">${customWaitMonitor.counselWaitCnt}</text>
                    명
                </td>
            </tr>
            <tr>
                <td>비로그인 대기</td>
                <td>
                    <text class="-consultant-status-count" data-value="0" data-login="false">${customWaitMonitor.counselWaitNoLoginCnt}</text>
                    명
                </td>
            </tr>
            <tr>
                <td>통화중</td>
                <td>
                    <text class="-consultant-status-count" data-value="1">${customWaitMonitor.callingCnt}</text>
                    명
                </td>
            </tr>
            <tr>
                <td>후처리 등 기타</td>
                <td>
                    <text class="-consultant-status-count" data-value="2,3,4,5,6,7,8,9">${customWaitMonitor.etcCnt}</text>
                    명
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="ui segment">
        <h5 class="ui header">대표서비스별 응답률</h5>
        <table class="ui celled table striped small compact fixed">
            <tbody>
            <c:forEach var="e" items="${aveByService.serviceListStat}">
                <tr>
                    <td title="${g.htmlQuote(e.svcName)}">${g.htmlQuote(e.svcName)}</td>
                    <td>${e.rateValue}%</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="ui segment">
        <h5 class="ui header">큐(그룹)별 고객대기/상담대기</h5>
        <table class="ui celled table striped small compact fixed">
            <tbody>
            <c:forEach var="e" items="${monitorByHunt.huntList}">
                <tr>
                    <td title="${g.htmlQuote(e.queueHanName)}">${g.htmlQuote(e.queueHanName)}</td>
                    <td>
                        <text class="-custom-wait-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.customWaitCnt}</text>
                        /
                        <text class="-consultant-status-count" data-value="0" data-hunt="${g.htmlQuote(e.queueName)}">${e.counselWaitCnt + e.counselWaitNoLoginCnt}</text>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="ui segment">
        <h5 class="ui header">우수실적상담원</h5>
        <table class="ui celled table striped small compact fixed">
            <thead>
            <tr>
                <th>실적</th>
                <th>상담원</th>
                <th>콜수</th>
                <th>통화시간</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="e" items="${excellentCS}">
                <tr>
                    <td>${g.htmlQuote(message.getEnumText(e.type))}</td>
                    <td>${g.htmlQuote(e.userName)}</td>
                    <td>${e.callCount}</td>
                    <td>${g.timeFormatFromSeconds(e.time)}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    (function () {
        if (window.queues) {
            <c:forEach var="e" items="${monitorByHunt.huntList}">
            if (!queues['${g.escapeQuote(e.queueName)}']) queues['${g.escapeQuote(e.queueName)}'] = {name: '${g.escapeQuote(e.queueName)}', hanName: '${g.escapeQuote(e.queueHanName)}'};
            queues['${g.escapeQuote(e.queueName)}'].waitingCustomerCount = ${e.customWaitCnt};
            queues['${g.escapeQuote(e.queueName)}'].peers = [<c:forEach var="member" items="${e.queueMemberList}">'${g.escapeQuote(member.peer)}', </c:forEach>];
            </c:forEach>

            if (updateQueues)
                updateQueues();
        }

        if (window.peerStatuses) {
            <c:forEach var="e" items="${monitorByHunt.huntList}">
            <c:forEach var="member" items="${e.queueMemberList}">
            if (!peerStatuses['${g.escapeQuote(member.peer)}']) peerStatuses['${g.escapeQuote(member.peer)}'] = {peer: '${g.escapeQuote(member.peer)}'};
            peerStatuses['${g.escapeQuote(member.peer)}'].status = ${member.status};
            peerStatuses['${g.escapeQuote(member.peer)}'].login = ${member.login};
            </c:forEach>
            </c:forEach>

            if (updatePersonStatus)
                updatePersonStatus();
        }
    })();
</script>