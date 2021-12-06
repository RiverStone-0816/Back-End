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

<form:form modelAttribute="form" class="ui modal -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/talk-schedule-day/service/type/${entity.seq}" data-done="reload">

    <i class="close icon"></i>
    <div class="header">상담톡일별스케쥴러[수정]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e)}</span>
                                        <c:if test="${!status.last}">
                                            <i class="right angle icon divider"></i>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">채널타입</label></div>
                <div class="twelve wide column">${g.htmlQuote(g.messageOf('TalkChannelType', entity.channelType))}</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">상담톡서비스</label></div>
                <div class="twelve wide column">${entity.kakaoServiceName}</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">날짜</label></div>
                <div class="four wide column">${entity.fromdate} ~ ${entity.todate}</div>
                <div class="four wide column"><label class="control-label">스케쥴유형선택</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="groupId" items="${scheduleInfos}"/>
                    </div>
                </div>
            </div>
            <div class="row" id="schedule-info">
                <table class="ui structured celled table compact unstackable">
                    <thead>
                    <tr>
                        <th colspan="2">시간</th>
                        <th>수행유형</th>
                        <th>수행데이터</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${entity.scheduleGroup.scheduleGroupLists.size() > 0}">
                            <c:forEach var="s" items="${entity.scheduleGroup.scheduleGroupLists}" varStatus="scheduleStatus">
                                <tr>
                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                    <td>
                                        <fmt:formatNumber value="${(s.fromhour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${s.fromhour % 60}" pattern="00"/>
                                        ~
                                        <fmt:formatNumber value="${(s.tohour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${s.tohour % 60}" pattern="00"/>
                                    </td>
                                    <td>
                                            ${s.kind == 'A' ? '자동멘트전송'
                                                    : s.kind == 'G' ? '서비스별그룹연결'
                                                    : '알수없음: '.concat(s.kind)}
                                    </td>
                                    <td>${g.htmlQuote(s.kindDataName)}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${entity.scheduleGroup.scheduleGroupLists.size() > 0}">
                                <tr>
                                    <td></td>
                                    <td colspan="3">
                                        <table class="time-table">
                                            <tbody>
                                            <tr>
                                                <c:forEach begin="0" end="23" var="h">
                                                    <td>${h}</td>
                                                </c:forEach>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>
                            <c:forEach var="s" items="${entity.scheduleGroup.scheduleGroupLists}" varStatus="scheduleStatus">
                                <tr>
                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                    <td colspan="3">
                                        <div class="-slider-time" data-start="${s.fromhour}" data-end="${s.tohour}"></div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="null-data">조회된 데이터가 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    modal.find('[name=groupId]').change(function () {
        modal.find('#schedule-info').empty();
        if (this.value !== '') {
            receiveHtml(contextPath + '/admin/talk/schedule/day/modal-view-schedule-group?parent=' + this.value).done(function (html) {
                const mixedNodes = $.parseHTML(html, null, true);

                const ui = (function () {
                    for (let i = 0; i < mixedNodes.length; i++) {
                        const node = $(mixedNodes[i]);
                        if (node.is('.ui'))
                            return node;
                    }
                    throw 'cannot found .ui';
                })();

                const schedule = ui.find('.row').children('table');

                modal.find('#schedule-info').append(schedule);
                schedule.bindHelpers();
            });
        }
    });
</script>
