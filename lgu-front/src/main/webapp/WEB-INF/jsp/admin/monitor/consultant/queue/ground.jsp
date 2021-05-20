<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="person" type="kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/consultant/queue/"/>
        <div class="sub-content ui container fluid">
            <div class="ui grid">
                <div class="sixteen wide column"></div>
                <div class="sixteen wide column">
                    <div class="panel">
                        <div class="panel-heading">
                            <div class="pull-left">
                                <h3 class="panel-title">전체 큐(그룹): <span class="text-primary">${queueSummaries.size()}</span></h3>
                            </div>
                            <div class="pull-right">
                                <div class="ui basic buttons">
                                    <button class="ui button" onclick="monitoringHuntSelect()">큐선택</button>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <c:forEach var="e" items="${queueSummaries}">
                                <div class="ui segments -queue-info-container" data-id="${g.htmlQuote(e.queue.name)}">
                                    <div class="ui segment secondary">
                                        [${g.htmlQuote(e.queue.name)}] ${g.htmlQuote(e.queue.hanName)}
                                    </div>
                                    <div class="ui segment -queue-summary" data-id="${g.htmlQuote(e.queue.name)}">
                                        <table class="ui table celled fixed">
                                            <thead>
                                            <tr>
                                                <th>고객대기</th>
                                                <c:forEach var="status" items="${memberStatuses}">
                                                    <c:if test="${status.key != 9}">
                                                        <th>${status.value}</th>
                                                    </c:if>
                                                </c:forEach>
                                                <th>로그인수</th>
                                                <th>로그아웃</th>
                                                <th>전체</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td class="-custom-wait-count"
                                                    data-hunt="${g.htmlQuote(e.queue.name)}">${e.customWaitCnt}</td>
                                                <c:forEach var="status" items="${memberStatuses}">
                                                    <c:if test="${status.key != 9}">
                                                        <td class="-consultant-status-count" data-value="${status.key}"
                                                            data-hunt="${g.htmlQuote(e.queue.name)}">${e.statusToUserCount.getOrDefault(status.key, 0)}</td>
                                                    </c:if>
                                                </c:forEach>
                                                <td class="-login-user-count"
                                                    data-hunt="${g.htmlQuote(e.queue.name)}">${e.loginUser}</td>
                                                <td class="-logout-user-count"
                                                    data-hunt="${g.htmlQuote(e.queue.name)}">${e.logoutUser}</td>
                                                <td>${e.totalUser}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="ui segment -queue-person" data-id="${g.htmlQuote(e.queue.name)}">
                                        <div class="items-wrap">
                                            <c:forEach var="personStatus" items="${queuePersonStatus}">
                                                <c:if test="${personStatus.queueName.equals(e.queue.name)}">
                                                    <c:forEach var="personList" items="${personStatus.personList}">
                                                    <div class="ui image label" data-peer="${g.htmlQuote(personList.peer)}">
                                                        <i class="phone icon"></i>
                                                            ${g.htmlQuote(personList.idName)}(${g.htmlQuote(personList.extension)})
                                                        <div class="detail -consultant-status-time" data-peer="${g.htmlQuote(personList.peer)}">${g.timeFormatFromSeconds(personStatus.billSec)}</div>
                                                        <div class="detail -consultant-status"
                                                             data-peer="${g.htmlQuote(personList.peer)}">${g.htmlQuote(memberStatuses.get(personList.paused))}</div>
                                                    </div>
                                                    </c:forEach>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">큐그룹별 모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div class="panel">
                        <div class="panel-heading">
                            <label class="control-label">통계</label>
                        </div>
                        <div class="panel-body">
                            <table class="ui table celled fixed structured">
                                <thead>
                                <tr>
                                    <th rowspan="2">큐그룹명</th>
                                    <th colspan="4">일 통계 요약</th>
                                    <th colspan="7">상담원 상태 요약</th>
                                </tr>
                                <tr>
                                    <th>연결요청</th>
                                    <th>응대호</th>
                                    <th>콜백</th>
                                    <th>응대율</th>
                                    <th>근무상담사</th>
                                    <th>고객대기</th>
                                    <th>대기</th>
                                    <th>I/B통화중</th>
                                    <th>O/B통화중</th>
                                    <th>후처리</th>
                                    <th>기타</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="e" items="${queueStats.statList}">
                                    <tr class="-queue-summary" data-id="${g.htmlQuote(e.queueName)}">
                                        <td>${g.htmlQuote(e.queueHanName)}</td>
                                        <td>${e.connReq}</td>
                                        <td>${e.success}</td>
                                        <td>${e.callback}</td>
                                        <td class="negative">${e.responseRate}%</td>
                                        <td class="-consultant-status-count" data-value="0,1,2,3,4,5,6,7,8"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.serviceCounselorCnt}</td>
                                        <td class="-custom-wait-count"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.customWaitCnt}</td>
                                        <td class="-consultant-status-count" data-value="0"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.counselorWaitCnt}</td>
                                        <td class="-consultant-status-count" data-value="1" data-call="IR,ID"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.inboundCallCnt}</td>
                                        <td class="-consultant-status-count" data-value="1" data-call="OR,OD"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.outboundCallCnt}</td>
                                        <td class="-consultant-status-count" data-value="2"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.postprocessStatusCnt}</td>
                                        <td class="-consultant-status-count" data-value="3,4,5,6,7,8,9"
                                            data-hunt="${g.htmlQuote(e.queueName)}">${e.etcCnt}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="sixteen wide column">
                    <div class="panel">
                        <div class="panel-heading">
                            <label class="control-label">주요현황</label>
                        </div>
                        <div class="panel-body">
                            <div class="ui four column grid">
                                <div class="column">
                                    <div style="text-align: center;">응답률</div>
                                    <svg id="pie-response-rate" style="width: 100%; height: 200px;"></svg>
                                </div>
                                <div class="column">
                                    <div style="text-align: center;">콜백 처리율</div>
                                    <svg id="pie-process-callback" style="width: 100%; height: 200px;"></svg>
                                </div>
                                <div class="column">
                                    <div style="text-align: center;">상담 가용률</div>
                                    <svg id="pie-consultation-availability" style="width: 100%; height: 200px;"></svg>
                                </div>
                                <div class="column">
                                    <div style="text-align: center;">상담원 상태</div>
                                    <svg id="pie-consultant-status" style="width: 100%; height: 200px;"></svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="sixteen wide column">
                    <div class="panel">
                        <div class="panel-heading">
                            <label class="control-label">상담사별통계</label>
                        </div>
                        <div class="panel-body">
                            <table class="ui table celled fixed structured">
                                <thead>
                                <tr>
                                    <th rowspan="2">상담사명</th>
                                    <th rowspan="2">상담사ID</th>
                                    <th rowspan="2">내선번호</th>
                                    <th colspan="8">상담사 개인별 상태</th>
                                    <th colspan="5">상담사 개인별 통계</th>
                                </tr>
                                <tr>
                                    <th>전화기</th>
                                    <th>로그인</th>
                                    <th>인입큐</th>
                                    <th>상태</th>
                                    <th>유지시간</th>
                                    <th>수/발신</th>
                                    <th colspan="2">고객번호</th>
                                    <th>수신</th>
                                    <th>발신</th>
                                    <th>합계</th>
                                    <th>총통화시간</th>
                                    <th>평균통화시간</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <c:forEach var="e" items="${list}">
                                            <tr>
                                                <td>${g.htmlQuote(e.person.idName)}</td>
                                                <td>${g.htmlQuote(e.person.id)}</td>
                                                <td>${g.htmlQuote(e.person.extension)}</td>
                                                <td>
                                                    <i class="phone icon ${e.isPhone != 'Y' ? 'translucent' : ''} -consultant-phone-status" data-peer="${g.htmlQuote(e.person.peer)}"></i>
                                                </td>
                                                <td>
                                                    <i class="key icon ${e.person.isLogin != 'Y' ? 'translucent' : ''} -consultant-login" data-peer="${g.htmlQuote(e.person.peer)}"></i>
                                                </td>
                                                <td class="-consultant-queue-name" data-peer="${g.htmlQuote(e.person.peer)}"></td>
                                                <td class="-consultant-status" data-peer="${g.htmlQuote(e.person.peer)}">${memberStatuses.get(e.person.paused)}</td>
                                                <td class="-consultant-status-time" data-peer="${g.htmlQuote(e.person.peer)}">00:00</td>
                                                <td class="-consultant-send-receive-status" data-peer="${g.htmlQuote(e.person.peer)}">${e.inOut == 'I' ? '수신' : e.inOut == 'O' ? '발신' : ''}</td>
                                                <td class="-consultant-calling-custom-number" data-peer="${g.htmlQuote(e.person.peer)}" colspan="2">${g.htmlQuote(e.customNumber)}</td>
                                                <td>${e.inboundSuccess}</td>
                                                <td>${e.outboundSuccess}</td>
                                                <td>${e.statTotal}</td>
                                                <td>${g.timeFormatFromSeconds(e.billSecSum)}</td>
                                                <td>${g.timeFormatFromSeconds(e.billSecAvg)}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="16" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal" id="modal-monitoring-hunt-select">
        <i class="close icon"></i>
        <div class="header">큐(그룹)선택</div>

        <div class="content rows scrolling">
            <table class="ui table celled unstackable fixed">
                <thead>
                <tr>
                    <th>큐그룹명</th>
                    <th>
                        <div class="ui checkbox">
                            <input type="checkbox" class="-lead-selector" data-target=".-checkbox-queue">
                            <label>전체선택</label>
                        </div>
                    </th>
                    <th>
                        <div class="ui checkbox">
                            <input type="checkbox" class="-lead-selector" data-target=".-checkbox-person">
                            <label>전체선택</label>
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="e" items="${queues}">
                    <tr>
                        <td>${g.htmlQuote(e.value)}</td>
                        <td>
                            <div class="ui checkbox">
                                <input type="checkbox" class="-checkbox-queue" multiple name="queues"
                                       value="${g.htmlQuote(e.key)}" checked>
                                <label>요약보기</label>
                            </div>
                        </td>
                        <td>
                            <div class="ui checkbox">
                                <input type="checkbox" class="-checkbox-person" multiple name="persons"
                                       value="${g.htmlQuote(e.key)}" checked>
                                <label>상담원상태보기</label>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="button" class="ui blue button modal-close" onclick="filterQueue()">확인</button>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            function monitoringHuntSelect() {
                $('#modal-monitoring-hunt-select').modalShow();
            }

            function filterQueue() {
                $('#modal-monitoring-hunt-select').asJsonData().done(function (data) {
                    const containers = $('.-queue-info-container').hide();
                    const summaries = $('.-queue-summary').hide();
                    const persons = $('.-queue-person').hide();

                    if (data.queues) data.queues.map(function (queueName) {
                        containers.filter(function () {
                            return $(this).attr('data-id') === queueName;
                        }).show();
                        summaries.filter(function () {
                            return $(this).attr('data-id') === queueName;
                        }).show();
                    });
                    if (data.persons) data.persons.map(function (queueName) {
                        containers.filter(function () {
                            return $(this).attr('data-id') === queueName;
                        }).show();
                        persons.filter(function () {
                            return $(this).attr('data-id') === queueName;
                        }).show();
                    });
                });
            }

            drawPieChart('#pie-response-rate', ${queueStats.responseRate / 100}, {
                startAngle: -90,
                endAngle: 90,
                colorClasses: ['bcolor-bar1', 'bcolor-bar2']
            });
            drawPieChart('#pie-process-callback', ${queueStats.callbackProcessingRate}, {
                startAngle: -90,
                endAngle: 90,
                colorClasses: ['bcolor-bar1', 'bcolor-bar2']
            });
            drawPieChart('#pie-consultation-availability', ${queueStats.callCounselRate}, {
                startAngle: -90,
                endAngle: 90,
                colorClasses: ['bcolor-bar1', 'bcolor-bar2']
            });
            drawPieChart('#pie-consultant-status', ${queueStats.counselorStatus}, {
                startAngle: -90,
                endAngle: 90,
                colorClasses: ['bcolor-bar1', 'bcolor-bar2']
            });

            updateQueues();
            updatePersonStatus();
        </script>
    </tags:scripts>
</tags:tabContentLayout>
