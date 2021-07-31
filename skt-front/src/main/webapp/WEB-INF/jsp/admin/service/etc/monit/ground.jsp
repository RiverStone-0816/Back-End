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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/service/etc/monit/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/service/etc/monit/"))}</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <th>부서조회</th>
                            <td colspan="7">
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
                            </td>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-body">
                    <div class="ui three column grid">
                        <c:forEach var="e" items="${list}" varStatus="status">
                            <div class="column">
                                <table class="ui celled table compact unstackable fixed">
                                    <caption>${g.htmlQuote(e.groupName)}</caption>
                                    <thead>
                                    <tr>
                                        <th>상담원</th>
                                        <th>상태</th>
                                        <th>상태변경</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${e.person != null && e.person.size() > 0}">
                                            <c:forEach var="person" items="${e.person}">
                                                <tr>
                                                    <td>${g.htmlQuote(person.idName)}(${g.htmlQuote(person.extension)})</td>
                                                    <td data-peer="${g.htmlQuote(person.peer)}"
                                                          class="tbl-status-td -status-label ${person.paused != null && person.paused == 0 ? '' : null} -consultant-status"> ${g.htmlQuote(statusCodes.get(person.paused))} </td>
                                                    <td>
                                                        <div class="ui form">
                                                            <select onchange="updatePersonPausedStatus('${g.htmlQuote(person.peer)}', $(this).val())">
                                                                <c:forEach var="s" items="${statusCodes}">
                                                                    <option value="${s.key}" ${person.paused != null && person.paused == s.key ? 'selected' : null}>${g.htmlQuote(s.value)}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="3" class="null-data">소속된 상담원이 없습니다.</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            const statusCodes = {
                <c:forEach var="s" items="${statusCodes}">
                '${s.key}': '${g.escapeQuote(s.value)}',
                </c:forEach>
            };

            const ipccAdminCommunicator = createIpccAdminCommunicator()
                .on("MEMBERSTATUS", function (message, kind, nothing, nothing_, peer, currentStatus, previousStatus) {
                    $('.-status-label').filter(function () {
                        return $(this).attr('data-peer') === peer;
                    }).each(function () {
                        $(this).removeClass('blue');
                        if (currentStatus === '0') $(this).addClass('blue');
                        $(this).text(statusCodes[currentStatus]);
                    });
                });

            function updatePersonPausedStatus(peer, status) {
                ipccAdminCommunicator.send({
                    company_id: ipccAdminCommunicator.request.companyId,
                    userid: ipccAdminCommunicator.request.userId,
                    target_pbx: ipccAdminCommunicator.request.pbxName,
                    command: 'CMD|SETMEMBERSTATUS|${user.companyId},' + peer + ',' + status
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
