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
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/monit/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" commandName="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'}실행일</label></div>
                                <div class="nine wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="endDate" style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">실행중인${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'}</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="lastExecuteId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${pdsList}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <div class="ui basic buttons">
                            <button type="button" class="ui button" onclick="startPdsExecuteGroup()">실행</button>
                            <button type="button" class="ui button" onclick="stopPdsExecuteGroup()">정지</button>
                            <button type="button" class="ui button" onclick="reloadSubmit(deletePdsExecuteGroup())">정지후내리기</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui teal message">
                        <p>완료된건들은 시스템 성능을 위해서 [정지후내리기]를 해주시기 바랍니다. 모든데이터와 수행결과는 삭제되지 않습니다.</p>
                    </div>

                    <c:choose>
                        <c:when test="${pagination.rows.size() > 0}">
                            <c:forEach var="e" items="${pagination.rows}">
                                <div class="ui segments">
                                    <div class="ui segment secondary">
                                        <div class="ui checkbox">
                                            <input type="checkbox" class="-execute-indicator" value="${g.htmlQuote(e.executeGroup.pdsGroupId)}">
                                            <label>[
                                                <text data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}"
                                                      class="-pds-status">${g.htmlQuote(g.messageOf('PDSGroupExecuteStatus', e.executeGroup.pdsStatus))}</text>
                                                ] ${g.htmlQuote(e.executeGroup.executeName)}(상담그룹:${g.htmlQuote(e.executeGroup.pdsName)})-실행자(${g.htmlQuote(e.executeGroup.pdsAdminId)})</label>
                                        </div>
                                    </div>
                                    <div class="ui segment">
                                        <table class="ui celled table compact structured unstackable fixed">
                                            <tbody>
                                            <tr>
                                                <th>시작일시</th>
                                                <th>유형</th>
                                                <th>연결대상</th>
                                                <th>RID</th>
                                                <th>속도(대기중상담원기준)</th>
                                                <th>타임아웃(초)</th>
                                            </tr>
                                            <tr>
                                                <td><fmt:formatDate value="${e.executeGroup.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>${g.htmlQuote(e.executeGroup.pdsTypeName)}</td>
                                                <td>${g.htmlQuote(e.pdsGroup.connectKind)}</td>
                                                <td>
                                                    <div class="ui action input fluid">
                                                        <input type="text" class="-input-numerical" value="${g.htmlQuote(e.executeGroup.ridData)}">
                                                        <button type="button" class="ui button"
                                                                onclick="setRid('${g.htmlQuote(e.executeGroup.executeId)}', '${g.htmlQuote(e.executeGroup.pdsGroupId)}', $(this).prev().val())">
                                                            수정
                                                        </button>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui action input fluid">
                                                        <input type="text" class="-input-float" placeholder="1이상~5이하" value="${e.executeGroup.speedData}">
                                                        <button type="button" class="ui button"
                                                                onclick="setSpeed('${g.htmlQuote(e.executeGroup.executeId)}', '${g.htmlQuote(e.executeGroup.pdsGroupId)}', $(this).prev().val())">
                                                            수정
                                                        </button>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui action input fluid">
                                                        <input type="text" class="-input-numerical" value="${g.htmlQuote(e.executeGroup.dialTimeout)}">
                                                        <button type="button" class="ui button"
                                                                onclick="setTimeoutValue('${g.htmlQuote(e.executeGroup.executeId)}', '${g.htmlQuote(e.executeGroup.pdsGroupId)}', $(this).prev().val())">
                                                            수정
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>고객수/전화번호</th>
                                                <th>진행된건/남은건</th>
                                                <th>시도/통화/종료</th>
                                                <th colspan="3">수신/비수신/통화중/비연결/기타</th>
                                            </tr>
                                            <tr>
                                                <td>${e.executeGroup.totalCnt}/${e.executeGroup.numbersCnt}</td>
                                                <td data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}" class="-processing-rest">
                                                        ${e.count.totalTry}/${e.count.totalMod}
                                                </td>
                                                <td data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}" class="-try-call-end">
                                                        ${e.count.rings}/${e.count.dialed}/${e.count.hanged}
                                                </td>
                                                <td data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}" class="-receive-fail-calling-noconnected-etc" colspan="3">
                                                        ${e.count.rst1}/${e.count.rst2}/${e.count.rst3}/${e.count.rst4}/${e.count.rst5}
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="ui segment">
                                        <div class="item">
                                            <div class="items-wrap">
                                                <c:forEach var="person" items="${e.persons}">
                                                    <div class="ui medium label -pds-member" data-peer="${g.htmlQuote(person.peer)}" data-person="${g.htmlQuote(person.id)}">
                                                            ${g.htmlQuote(person.idName)}(${g.htmlQuote(person.extension)})
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="ui segment">
                                조회된 데이터가 없습니다.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/pds/monit/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>
    <tags:scripts>
        <script>
            window.pdsStatuses = {
                <c:forEach var="e" items="${pdsStatues}">
                '${g.escapeQuote(e.id)}': {
                    paused: '${g.htmlQuote(e.paused)}',
                    queueName: '${e.queueName}',
                },
                </c:forEach>
            };

            function startPdsExecuteGroup() {
                $('.-execute-indicator:checked').each(function () {
                    ipccPdsCommunicator.start($(this).val());
                });
            }

            function stopPdsExecuteGroup() {
                $('.-execute-indicator:checked').each(function () {
                    ipccPdsCommunicator.stop($(this).val());
                });
            }

            function deletePdsExecuteGroup() {
                $('.-execute-indicator:checked').each(function () {
                    ipccPdsCommunicator.delete($(this).val());
                });
            }

            function reloadSubmit(event) {
                $('#search-form').submit();
            }

            function updatePDSPersonStatus() {
                $('.-pds-member').each(function () {
                    const userId = $(this).attr('data-person');
                    const pdsStatus = pdsStatuses[userId];

                    $('.-pds-member[data-person="' + userId + '"]').css({backgroundColor: pdsStatus.paused === '0' ? 'lightgreen' :
                            (pdsStatus.paused === '1' ? 'yellow' : (pdsStatus.paused === '2' ? 'orange' : '#e8e8e8'))});
                })
            }

            function setRid(executeId, pdsGroupId, value) {
                ipccPdsCommunicator.setRid(executeId, pdsGroupId, value);
            }

            function setSpeed(executeId, pdsGroupId, value) {
                ipccPdsCommunicator.setSpeed(executeId, pdsGroupId, value);
            }

            function setTimeoutValue(executeId, pdsGroupId, value) {
                ipccPdsCommunicator.setTimeout(executeId, pdsGroupId, value);
            }

            const ipccPdsCommunicator = new IpccPdsCommunicator();
            restSelf.get('/api/auth/socket-info').done(function (response) {
                ipccPdsCommunicator.connect(response.data.pdsSocketUrl, response.data.companyId, response.data.userId, hex_sha512(response.data.password));
            });
            ipccPdsCommunicator
                .on('PDS_START', function (kind, result, pdsGroupId, executeId) {
                    console.log(kind, result, pdsGroupId, executeId);
                })
                .on('PDS_STOP', function (kind, result, pdsGroupId, executeId) {
                    console.log(kind, result, pdsGroupId, executeId);
                })
                .on('PDS_SETRID', function (result, data1, pdsGroupId, executeId) {
                    let cause, newRid;
                    if (result === 'OK')
                        newRid = data1;
                    else if (result === 'NOK')
                        cause = data1;

                    console.log(result, data1, pdsGroupId, executeId);
                })
                .on('PDS_SETSPEED', function (result, data1, pdsGroupId, executeId) {
                    let cause, newSpeed;
                    if (result === 'OK')
                        newSpeed = data1;
                    else if (result === 'NOK')
                        cause = data1;

                    console.log(result, data1, pdsGroupId, executeId);
                })
                .on('PDS_SETTIMEOUT', function (result, data1, pdsGroupId, executeId) {
                    let cause, newTimeoutSeconds;
                    if (result === 'OK')
                        newTimeoutSeconds = data1;
                    else if (result === 'NOK')
                        cause = data1;

                    console.log(result, data1, pdsGroupId, executeId);
                })
                .on('PDS_STATUS', function (nothing, kind, userId, executeId, newStatus) {
                    $('.-pds-status[data-execute-id="' + executeId + '"]').text(
                        newStatus === 'START_PDS' ? '진행중'
                            : newStatus === 'REQUESTED_STOP' ? '정지'
                            : newStatus === 'REQUESTED_DEL' ? '정지후삭제됨'
                                : newStatus === 'WAIT_NORMAL_STOP' ? '시도완료'
                                    : newStatus === 'NORMAL_STOP' ? '완료됨'
                                        : newStatus === 'TIMEOVER_STOP' ? '종료시간 설정에 의한 종료'
                                            : newStatus === 'COUNT_STOP' ? '일정건수 설정에 의한 종료'
                                                : newStatus === 'RSCHCOUNT_STOP' ? '설문 답변건수 설정에 의한 종료'
                                                    : newStatus === 'EXCEPT_STOP' ? '비정상종료'
                                                        : newStatus
                    );
                })
                .on('PDS_STAT', function (nothing, kind, userId, executeId, processingAndRest, tryAndCallAndEnd, receiveAndFailAndCallingAndNoconnectedAndEtc) {
                    $('.-processing-rest[data-execute-id="' + executeId + '"]').text(processingAndRest);
                    $('.-try-call-end[data-execute-id="' + executeId + '"]').text(tryAndCallAndEnd);
                    $('.-receive-fail-calling-noconnected-etc[data-execute-id="' + executeId + '"]').text(receiveAndFailAndCallingAndNoconnectedAndEtc);
                })
                .on('Bye', function () {
                    this.recovery();
                })
                .on('SERVER_STATUS', function (kind) {
                    if (kind === 'ERROR')
                        this.recovery();
                })
                .on('PDSMEMBERSTATUS', function (message, kind, userId) {
                    $('.-pds-member').each(function () {
                        const status = parseInt(kind);

                        $('.-pds-member[data-person="' + userId + '"]').css({backgroundColor: status === 0 ? 'lightgreen' :
                                (status === 1 ? 'yellow' : (status === 2 ? 'orange' : '#e8e8e8'))});
                    })
                });

            updatePDSPersonStatus();
        </script>
    </tags:scripts>
</tags:tabContentLayout>
