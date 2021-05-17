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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/consultant/ivr/"/>
        <div class="sub-content ui container fluid">
            <div class="panel">
                <form class="panel-heading" method="get">
                    <div class="pull-left">
                        <div class="ui form">
                            <select onchange="submit()" name="serviceNumber">
                                <option value="">서비스선택</option>
                                <c:forEach var="e" items="${services}">
                                    <option value="${e.key}" ${e.key == serviceNumber ? 'selected' : ''} >${g.htmlQuote(e.value)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
                <div class="panel-body">
                    <div class="ui grid">
                        <div class="row">
                            <div class="sixteen wide column">
                                <div id="pds-ivr-container">
                                    <c:if test="${response != null}">
                                        <div class="ui segments">
                                            <c:forEach var="e" items="${response.scheduleContents}">
                                                <div class="ui segment">
                                                    스케줄명 : ${g.htmlQuote(response.scheduleGroup.name)} <br/>
                                                    스케쥴종류 : ${g.messageOf("ScheduleType", response.scheduleInfo.type)} ${g.messageOf("DayOfWeek", fn:substring(response.scheduleInfo.week, 1, fn:length(response.scheduleInfo.week)))} <br/>
                                                    시간 : ${g.timeFormatFromSeconds(e.scheduleGroupList.fromhour)} ~ ${g.timeFormatFromSeconds(e.scheduleGroupList.fromhour)} <br/>
                                                    유형 : ${g.messageOf("ScheduleKind", e.scheduleGroupList.kind)} (${e.scheduleGroupList.kind.equals("C") ? contexts.get(e.scheduleGroupList.kindData) : e.scheduleGroupList.kindData})
                                                </div>
                                                <div class="ui segment">
                                                    <i class="folder icon"></i><span class="ui header"><c:if test="${e.monitorIvrTree.button != null && e.monitorIvrTree.button != ''}"><span
                                                        class="ui grey circular label tiny">${e.monitorIvrTree.button}</span></c:if>${g.htmlQuote(e.monitorIvrTree.name)}[${e.monitorIvrTree.waitingCustomerCount}]</span>
                                                </div>
                                                <div class="ui secondary segment">
                                                    <div class="ui list">
                                                        <c:forEach var="node" items="${e.monitorIvrTree.nodes}">
                                                            <tags:ivr-monitor-node node="${node}" statusCodes="${statusCodes}"/>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
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
                .on("MEMBERSTATUS", function (message, kind, nothing, peer, currentStatus, previousStatus) {
                    $('.-status-label').filter(function () {
                        return $(this).attr('data-peer') === peer;
                    }).each(function () {
                        $(this).removeClass('blue');
                        if (currentStatus === '0') $(this).addClass('blue');
                        $(this).text(statusCodes[currentStatus]);
                    });
                });

            (function () {
                if (window.queues) {
                    if (updateQueues)
                        updateQueues();
                }
            })()
        </script>
    </tags:scripts>
</tags:tabContentLayout>