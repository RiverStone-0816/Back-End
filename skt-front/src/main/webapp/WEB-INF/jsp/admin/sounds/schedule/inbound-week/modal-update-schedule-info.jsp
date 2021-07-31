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

<form:form modelAttribute="form" class="ui modal -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/inbound-week-schedule/service/type/${entity.scheduleInfo.seq}" data-done="reload">

    <i class="close icon"></i>
    <div class="header">주간스케쥴러[수정]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">부서조회</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini brand compact -select-group">
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
                                    <span class="section">부서를 선택해 주세요.</span>
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
                <div class="four wide column"><label class="control-label">번호</label></div>
                <div class="four wide column">${g.htmlQuote(entity.number)}</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">요일</label></div>
                <div class="four wide column">${g.messageOf("DayOfWeek", fn:substring(entity.scheduleInfo.week, 1, fn:length(entity.scheduleInfo.week)))}</div>
                <div class="four wide column"><label class="control-label">스케쥴유형선택</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="groupId" items="${scheduleGroups}"/>
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
                        <th>음원</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${entity.scheduleInfo.scheduleGroup.scheduleGroupLists.size() > 0}">
                            <c:forEach var="s" items="${entity.scheduleInfo.scheduleGroup.scheduleGroupLists}" varStatus="scheduleStatus">
                                <tr>
                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                    <td>
                                        <fmt:formatNumber value="${(s.fromhour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${s.fromhour % 60}" pattern="00"/>
                                        ~
                                        <fmt:formatNumber value="${(s.tohour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${s.tohour % 60}" pattern="00"/>
                                    </td>
                                    <td>
                                            ${s.kind == 'S' ? '음원만플레이'
                                                    : s.kind == 'D' ? '번호직접연결(내부번호연결)'
                                                    : s.kind == 'F' ? '착신전환(외부번호연결)'
                                                    : s.kind == 'I' ? 'IVR연결'
                                                    : s.kind == 'C' ? '예외컨텍스트'
                                                    : s.kind == 'V' ? '음성사서함'
                                                    : '알수없음: ' + s.kind}
                                    </td>
                                    <td>${g.htmlQuote(s.kindDataName)}</td>
                                    <td>${g.htmlQuote(s.kindSoundName)}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${entity.scheduleInfo.scheduleGroup.scheduleGroupLists.size() > 0}">
                                <tr>
                                    <td></td>
                                    <td colspan="5">
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
                            <c:forEach var="s" items="${entity.scheduleInfo.scheduleGroup.scheduleGroupLists}" varStatus="scheduleStatus">
                                <tr>
                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                    <td colspan="5">
                                        <div class="-slider-time" data-start="${s.fromhour}" data-end="${s.tohour}"></div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="null-data">조회된 데이터가 없습니다.</td>
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
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    modal.find('[name=groupId]').change(function () {
        modal.find('#schedule-info').empty();
        if (this.value !== '') {
            receiveHtml('/admin/sounds/schedule/inbound-week/modal-view-schedule-group?parent=' + this.value).done(function (html) {
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
