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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/consultant/ivr/"/>
        <div class="sub-content ui container fluid">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">센터현황관리[IVR]</div>
                    </div>
                </div>
                <form class="panel-body" method="get">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>IVR</th>
                                <td colspan="7">
                                    <div class="ui form">
                                        <select onchange="submit()" name="serviceNumber">
                                            <option value="">서비스선택</option>
                                            <c:forEach var="e" items="${services}">
                                                <option value="${e.key}" ${e.key == serviceNumber ? 'selected' : ''} >${g.htmlQuote(e.value)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <div id="pds-ivr-container">
                        <c:if test="${response != null}">
                            <c:forEach var="e" items="${response.scheduleContents}">
                                <div class="panel-section">
                                    <div class="panel-sub-title">스케쥴명 : ${g.htmlQuote(response.scheduleGroup.name)}</div>
                                    <div class="panel-sub-container">
                                        <table class="ui celled table fixed compact unstackable">
                                            <thead>
                                            <tr>
                                                <th>스케쥴 종류</th>
                                                <th>시간</th>
                                                <th>유형</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>${g.messageOf("ScheduleType", response.scheduleInfo.type)} ${g.messageOf("DayOfWeek", fn:substring(response.scheduleInfo.week, 1, fn:length(response.scheduleInfo.week)))}</td>
                                                <td>${g.timeFormatFromSeconds(e.scheduleGroupList.fromhour)} ~ ${g.timeFormatFromSeconds(e.scheduleGroupList.fromhour)}</td>
                                                <td>${g.messageOf("ScheduleKind", e.scheduleGroupList.kind)}(${e.scheduleGroupList.kind.equals("C") ? contexts.get(e.scheduleGroupList.kindData) : e.scheduleGroupList.kindData})</td>
                                            </tr>
                                            </tbody>
                                        </table>

                                        <div class="ui segments">
                                            <div class="ui segment">
                                                <span class="ui header"><c:if test="${e.monitorIvrTree.button != null && e.monitorIvrTree.button != ''}"><span
                                                        class="ui circular label tiny">${e.monitorIvrTree.button}</span></c:if>${g.htmlQuote(e.monitorIvrTree.name)}[${e.monitorIvrTree.waitingCustomerCount}]</span>
                                            </div>
                                            <div class="ui secondary segment">
                                                <p class="tree-caption"><span class="ui circular label tiny">1</span>제목[0]</p>
                                                <ul class="tree ivr">
                                                    <li>
                                                        <div class="header active"><i class="folder open icon"></i>
                                                            제목
                                                            <button type="button" class="ui basic button mini" title="추가">음원듣기</button>
                                                            <button type="button" class="ui basic button mini" title="추가">버튼맵핑</button>
                                                            <button type="button" class="ui basic button mini" title="추가">삭제</button>
                                                        </div>
                                                        <ul>
                                                            <li><div class="header"><span class="ui circular label tiny">1</span> m1</div>
                                                                <div class="user-ivr-wrap">
                                                                    <h4 class="user-ivr-title">IVR3_1_법인 대량구매</h4>
                                                                    <dl class="user-ivr-ul">
                                                                        <dd class="box">
                                                                            <div class="name">홍길동[000]</div>
                                                                            <div class="state call">통화중</div>
                                                                        </dd>
                                                                        <dd class="box">
                                                                            <div class="name">홍길동[000]</div>
                                                                            <div class="state wait">대기</div>
                                                                        </dd>
                                                                        <dd class="box">
                                                                            <div class="name">홍길동[000]</div>
                                                                            <div class="state bell">벨울림</div>
                                                                        </dd>
                                                                    </dl>
                                                                </div>
                                                                <ul>
                                                                    <li><div class="header"><span class="ui circular label tiny">1</span> m1</div>
                                                                        <div class="user-ivr-wrap">
                                                                            <h4 class="user-ivr-title">IVR3_1_법인 대량구매</h4>
                                                                            <dl class="user-ivr-ul">
                                                                                <dd class="box">
                                                                                    <div class="name">홍길동[000]</div>
                                                                                    <div class="state call">통화중</div>
                                                                                </dd>
                                                                                <dd class="box">
                                                                                    <div class="name">홍길동[000]</div>
                                                                                    <div class="state wait">대기</div>
                                                                                </dd>
                                                                                <dd class="box">
                                                                                    <div class="name">홍길동[000]</div>
                                                                                    <div class="state bell">벨울림</div>
                                                                                </dd>
                                                                            </dl>
                                                                        </div>
                                                                        <ul>
                                                                            <li><div class="header"><span class="ui circular label tiny">1</span> m1</div>
                                                                                <div class="user-ivr-wrap">
                                                                                    <h4 class="user-ivr-title">IVR3_1_법인 대량구매</h4>
                                                                                    <dl class="user-ivr-ul">
                                                                                        <dd class="box">
                                                                                            <div class="name">홍길동[000]</div>
                                                                                            <div class="state call">통화중</div>
                                                                                        </dd>
                                                                                        <dd class="box">
                                                                                            <div class="name">홍길동[000]</div>
                                                                                            <div class="state wait">대기</div>
                                                                                        </dd>
                                                                                        <dd class="box">
                                                                                            <div class="name">홍길동[000]</div>
                                                                                            <div class="state bell">벨울림</div>
                                                                                        </dd>
                                                                                    </dl>
                                                                                </div>
                                                                                <ul>
                                                                                    <li><div class="header"><span class="ui circular label tiny">1</span> m1</div>
                                                                                        <div class="user-ivr-wrap">
                                                                                            <h4 class="user-ivr-title">IVR3_1_법인 대량구매</h4>
                                                                                            <dl class="user-ivr-ul">
                                                                                                <dd class="box">
                                                                                                    <div class="name">홍길동[000]</div>
                                                                                                    <div class="state call">통화중</div>
                                                                                                </dd>
                                                                                                <dd class="box">
                                                                                                    <div class="name">홍길동[000]</div>
                                                                                                    <div class="state wait">대기</div>
                                                                                                </dd>
                                                                                                <dd class="box">
                                                                                                    <div class="name">홍길동[000]</div>
                                                                                                    <div class="state bell">벨울림</div>
                                                                                                </dd>
                                                                                            </dl>
                                                                                        </div>
                                                                                    </li>
                                                                                </ul>
                                                                            </li>
                                                                        </ul>
                                                                    </li>
                                                                </ul>
                                                            </li>
                                                            <li><div class="header"><span class="ui circular label tiny">1</span> m2</div></li>
                                                        </ul>
                                                    </li>
                                                </ul>
                                                <div class="ui list">
                                                    <c:forEach var="node" items="${e.monitorIvrTree.nodes}">
                                                        <tags:ivr-monitor-node node="${node}" statusCodes="${statusCodes}"/>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
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
