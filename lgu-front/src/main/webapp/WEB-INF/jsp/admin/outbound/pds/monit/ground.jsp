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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/monit/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div>
                        검색
                    </div>
                    <div class="dp-flex align-items-center">
                        <div class="ui slider checkbox mr15">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">
                                초기화
                            </button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label
                                        class="control-label">${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'}실행일</label>
                                </div>
                                <div class="nine wide column -buttons-set-range-container"
                                     data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                                        <button type="button" data-interval="day" data-number="1"
                                                class="ui button -button-set-range">당일
                                        </button>
                                        <button type="button" data-interval="day" data-number="3"
                                                class="ui button -button-set-range">3일
                                        </button>
                                        <button type="button" data-interval="day" data-number="7"
                                                class="ui button -button-set-range">1주일
                                        </button>
                                        <button type="button" data-interval="month" data-number="1"
                                                class="ui button -button-set-range">1개월
                                        </button>
                                        <button type="button" data-interval="month" data-number="3"
                                                class="ui button -button-set-range">3개월
                                        </button>
                                        <button type="button" data-interval="month" data-number="6"
                                                class="ui button -button-set-range">6개월
                                        </button>
                                    </div>
                                </div>
                                <div class="two wide column"><label
                                        class="control-label">실행중인${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'}</label>
                                </div>
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
                            <button type="button" class="ui button" onclick="deletePdsExecuteGroup()">정지후내리기</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui teal message">
                        <p>완료된 건들은 시스템 성능을 위해서 [정지후내리기]를 해주시기 바랍니다. 모든 데이터와 수행결과는 삭제되지 않습니다.</p>
                    </div>

                    <c:choose>
                        <c:when test="${pagination.rows.size() > 0}">
                            <c:forEach var="e" items="${pagination.rows}">
                                <div class="ui segments">
                                    <div class="ui segment secondary">
                                        <div class="ui checkbox">
                                            <input type="checkbox" class="-execute-indicator"
                                                   value="${g.htmlQuote(e.executeGroup.pdsGroupId)}">
                                            <label>
                                                <div class="ui label" title="PDS 상태">
                                                    [
                                                    <text data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}"
                                                          class="-pds-status">
                                                            ${g.htmlQuote(g.messageOf('PDSGroupExecuteStatus', e.executeGroup.pdsStatus))}
                                                    </text>
                                                    ]
                                                </div>
                                                <div class="ui label" title="PDS그룹명">
                                                        ${g.htmlQuote(e.executeGroup.pdsName)}
                                                </div>
                                                <div class="ui label" title="실행명"
                                                     data-group-id="${g.htmlQuote(e.executeGroup.pdsGroupId)}">
                                                        ${g.htmlQuote(e.executeGroup.executeName)}
                                                </div>

                                                <div class="ui label" title="실행자"
                                                     data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}"
                                                     data-admin-id="${g.htmlQuote(e.executeGroup.pdsAdminId)}"
                                                     class="-pds-admin">
                                                        ${g.htmlQuote(e.executeGroup.pdsAdminName)}
                                                </div>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="ui segment">
                                        <table class="ui celled table compact structured unstackable fixed">
                                            <tbody>
                                            <tr>
                                                <th>시작일시</th>
                                                <th>유형</th>
                                                <th>연결구분</th>
                                                <th>연결대상</th>
                                                <th>RID</th>
                                                <th>속도(${g.htmlQuote(g.messageOf('PDSGroupSpeedKind', e.executeGroup.speedKind))})</th>
                                                <th>다이얼시간(초)</th>
                                            </tr>
                                            <tr>
                                                <td><fmt:formatDate value="${e.executeGroup.startDate}"
                                                                    pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>${g.htmlQuote(e.executeGroup.pdsTypeName)}</td>
                                                <td>${g.htmlQuote(g.messageOf('PDSGroupConnectKind', e.pdsGroup.connectKind))}</td>
                                                <td>${g.htmlQuote(e.pdsGroup.connectDataValue)}</td>
                                                <td>
                                                    <div class="ui action input form fluid">
                                                        <select class="-pds-rid-data"
                                                                style="border-top-right-radius: 0 !important;border-bottom-right-radius: 0 !important;border-right-color: transparent !important;"
                                                                data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}">
                                                            <c:forEach var="f" items="${rids}">
                                                                <option value="${f.number}" ${e.executeGroup.ridData eq f.number ? 'selected' : ''}>${f.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                        <button type="button" class="ui button "
                                                                onclick="setRid('${g.htmlQuote(e.executeGroup.executeId)}', '${g.htmlQuote(e.executeGroup.pdsGroupId)}', $(this).prev().val())">
                                                            수정
                                                        </button>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui action input form fluid">
                                                        <c:choose>
                                                            <c:when test="${e.executeGroup.speedKind eq 'MEMBER'}">
                                                                <select class="-pds-speed-data"
                                                                        style="border-top-right-radius: 0 !important;border-bottom-right-radius: 0 !important;border-right-color: transparent !important;"
                                                                        data-group-id="${g.htmlQuote(e.executeGroup.pdsGroupId)}">
                                                                    <c:forEach var="f" items="${pdsGroupSpeedOptions}">
                                                                        <option value="${f.key / 10}" ${e.executeGroup.speedData * 10 eq f.key ? 'selected' : ''}>${f.value}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </c:when>
                                                            <c:when test="${e.executeGroup.speedKind eq 'CHANNEL'}">
                                                                <input type="number" class="-pds-speed-data -input-numerical"
                                                                       min="1" max="500" placeholder="500이하" title="1~500"
                                                                       data-group-id="${g.htmlQuote(e.executeGroup.pdsGroupId)}"
                                                                       value="${e.executeGroup.speedData.intValue()}">
                                                            </c:when>
                                                        </c:choose>
                                                        <button type="button" class="ui button"
                                                                onclick="setSpeed('${g.htmlQuote(e.executeGroup.pdsGroupId)}', $(this).prev().val())">
                                                            수정
                                                        </button>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui action input fluid">
                                                        <input type="text" class="-input-numerical -pds-timeout-data"
                                                               data-group-id="${g.htmlQuote(e.executeGroup.pdsGroupId)}"
                                                               value="${g.htmlQuote(e.executeGroup.dialTimeout)}">
                                                        <button type="button" class="ui button"
                                                                onclick="setTimeoutValue('${g.htmlQuote(e.executeGroup.pdsGroupId)}', $(this).prev().val())">
                                                            수정
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>고객수 / 전화번호</th>
                                                <th>진행된건 / 남은건</th>
                                                <th colspan="2">시도 / 통화 / 종료</th>
                                                <th colspan="3">수신 / 비수신 / 통화중 / 비연결 / 기타</th>
                                            </tr>
                                            <tr>
                                                <td>${e.executeGroup.totalCnt} / ${e.executeGroup.numbersCnt}</td>
                                                <td data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}"
                                                    class="-processing-rest">
                                                        ${e.count.totalTry} / ${e.count.totalMod}
                                                </td>
                                                <td data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}"
                                                    class="-try-call-end" colspan="2">
                                                        ${e.count.rings} / ${e.count.dialed} / ${e.count.hanged}
                                                </td>
                                                <td data-execute-id="${g.htmlQuote(e.executeGroup.executeId)}"
                                                    class="-receive-fail-calling-noconnected-etc" colspan="3">
                                                        ${e.count.rst1} / ${e.count.rst2} / ${e.count.rst3}
                                                    / ${e.count.rst4} / ${e.count.rst5}
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="ui segment">
                                        <div class="item">
                                            <div class="items-wrap">
                                                <c:forEach var="person" items="${e.persons}">
                                                    <div class="ui image label -pds-member"
                                                         data-peer="${g.htmlQuote(person.peer)}"
                                                         data-person="${g.htmlQuote(person.id)}">
                                                        <i class="phone icon"></i>
                                                            ${g.htmlQuote(person.idName)}(${g.htmlQuote(person.extension)})
                                                        <div class="detail -pds-consultant-status-time"
                                                             data-person="${g.htmlQuote(person.id)}">
                                                            00:00
                                                        </div>
                                                        <div class="detail -pds-consultant-status">PDS이외</div>
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
                    <tags:pagination navigation="${pagination.navigation}" pageForm="${search}"
                                     url="${pageContext.request.contextPath}/admin/outbound/pds/monit/"/>
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

            const pdsGroupIdNames = {
                <c:forEach var="e" items="${pagination.rows}">
                '${g.htmlQuote(e.executeGroup.pdsGroupId)}': '${g.htmlQuote(e.executeGroup.pdsName)}',
                </c:forEach>
            }

            const pdsExecuteIds = new Set([
                <c:forEach var="e" items="${pagination.rows}">
                '${g.htmlQuote(e.executeGroup.executeId)}',
                </c:forEach>
            ]);

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
                const checked = $('.-execute-indicator:checked')
                if (!checked)
                    return;

                $('.-execute-indicator:checked').each(function () {
                    ipccPdsCommunicator.delete($(this).val());
                });

                setTimeout(() => $('#search-form').submit(), 1000);
            }

            function setRid(executeId, pdsGroupId, value) {
                ipccPdsCommunicator.setRid(executeId, pdsGroupId, value);
            }

            function setSpeed(pdsGroupId, value) {
                ipccPdsCommunicator.setSpeed(pdsGroupId, value);
            }

            function setTimeoutValue(executeId, pdsGroupId, value) {
                ipccPdsCommunicator.setTimeout(executeId, pdsGroupId, value);
            }

            function updatePDSPersonStatus() {
                $('.-pds-member').each(function () {
                    const userId = $(this).attr('data-person');
                    const pdsStatus = pdsStatuses[userId];

                    updatePDSPersonStatusByUserId(userId, pdsStatus);
                })
            }

            function updatePDSPersonStatusByUserId(userId, pdsStatus) {
                if (!userId || !pdsStatus)
                    return;

                const statusFiled = $('.-pds-member[data-person="' + userId + '"] > .-pds-consultant-status');
                statusFiled.css({backgroundColor: pdsStatus.paused === '0' ? 'lightgreen' : (pdsStatus.paused === '1' ? 'yellow' : (pdsStatus.paused === '2' ? 'orange' : '#e8e8e8'))});
                statusFiled.text(pdsStatus.paused === '0' ? '대기' : (pdsStatus.paused === '1' ? '상담중' : (pdsStatus.paused === '2' ? '후처리' : 'PDS이외')));
            }

            setInterval(function () {
                $('.-pds-consultant-status-time').each(function () {
                    const pdsStatus = pdsStatuses[$(this).attr('data-person')];
                    const statusTime = $(this).attr('data-time');
                    if (!pdsStatus) return;

                    if (statusTime) {
                        pdsStatus.callStatusUpdatedTime = statusTime;
                        $(this).removeAttr('data-time');
                    } else if (!pdsStatus.callStatusUpdatedTime)
                        pdsStatus.callStatusUpdatedTime = new Date().getTime();

                    const callingTime = parseInt(((pdsStatus.callStatusUpdatedTime) - new Date().getTime()) / 1000) * -1;
                    $(this).text(pad(parseInt(callingTime / 60), 2) + ':' + pad(callingTime % 60, 2));
                });
            }, 1000);

            const ipccPdsCommunicator = new IpccPdsCommunicator();
            restSelf.get('/api/auth/socket-info').done(function (response) {
                ipccPdsCommunicator.connect(response.data.pdsSocketUrl, response.data.companyId, response.data.userId, response.data.password);
            });

            ipccPdsCommunicator
                .on('PDS_START', function (message, kind, result, pdsGroupId, executeId) {
                    const currentPdsName = pdsGroupIdNames[pdsGroupId];
                    if (!currentPdsName)
                        return;

                    if (kind === 'OK') {
                        alert(currentPdsName + ' PDS를 실행합니다.');
                        return
                    }

                    let cause = '미확인';
                    if (result === 'NO_STATUS_S')
                        cause = '준비/정지 상태가 아님';
                    else if (result === 'EXIST_BEFORE_SEQUENCE')
                        cause = '순차 실행할 PDS가 없음';

                    alert(currentPdsName + ' PDS를 실행할 수 없습니다. (' + cause + ')');
                })
                .on('PDS_STOP', function (message, kind, result, pdsGroupId, executeId) {
                    const currentPdsName = pdsGroupIdNames[pdsGroupId];
                    if (!currentPdsName)
                        return;

                    if (kind === 'OK') {
                        alert(currentPdsName + ' PDS를 정지합니다.');
                        return;
                    }

                    let cause = '미확인';
                    if (result === 'NO_STATUS_P')
                        cause = '실행 상태가 아님';

                    alert(currentPdsName + ' PDS를 정지할 수 없습니다. (' + cause + ')');
                })
                .on('PDS_READY', function (message, kind, result, pdsGroupId, executeId) {
                    const currentPdsName = pdsGroupIdNames[pdsGroupId];
                    if (!currentPdsName)
                        return;

                    if (kind === 'OK') {
                        alert(currentPdsName + ' PDS를 준비합니다.');
                        return;
                    }

                    let cause = '미확인';
                    if (result === 'DB_ERROR')
                        cause = 'DB에러';
                    else if (result === 'NO_GROUP_ID')
                        cause = '해당 PDS그룹이 없음';
                    else if (result === 'NO_NUMBER_FIELD')
                        cause = '해당 PDS그룹에 전화번호필드가 없음';
                    else if (result === 'NO_PDS_CUSTOM_INFO')
                        cause = '해당 PDS그룹에 고객정보가 없음';
                    else if (result === 'NO_CUSTOM_NUMBERS')
                        cause = '해당 PDS그룹에 전화번호가 없음';

                    alert(currentPdsName + ' PDS를 준비할 수 없습니다. (' + cause + ')');
                })
                .on('PDS_SETRID', function (message, kind, result, pdsGroupId, executeId) {
                    const currentPdsName = pdsGroupIdNames[pdsGroupId];
                    if (!currentPdsName)
                        return;

                    if (kind === 'OK') {
                        alert(currentPdsName + ' PDS의 RID가 변경되었습니다.');
                        $('.-pds-rid-data[data-execute-id="' + executeId + '"]').val(result);
                        return;
                    }

                    let cause = '미확인';
                    if (result === 'NO_PERMISSION_CID')
                        cause = '미허용 번호';

                    alert(currentPdsName + ' PDS의 RID를 변경할 수 없습니다. (' + cause + ')');
                })
                .on('PDS_SETSPEED', function (message, kind, result, pdsGroupId, executeId) {
                    const currentPdsName = pdsGroupIdNames[pdsGroupId];
                    if (!currentPdsName)
                        return;

                    if (kind === 'OK') {
                        alert(currentPdsName + ' PDS의 속도가 변경되었습니다.');
                        $('.-pds-speed-data[data-group-id="' + pdsGroupId + '"]').val(result);
                        return;
                    }

                    let cause = '미확인';
                    if (result === 'ARG_ERROR')
                        cause = '속도값 미설정';
                    else if (result === 'INVALID_VALUE')
                        cause = '올바르지 않은 속도값';

                    alert(currentPdsName + ' PDS의 속도를 변경할 수 없습니다. (' + cause + ')');
                })
                .on('PDS_SETTIMEOUT', function (message, kind, result, pdsGroupId, executeId) {
                    const currentPdsName = pdsGroupIdNames[pdsGroupId];
                    if (!currentPdsName)
                        return;

                    if (kind === 'OK') {
                        alert(currentPdsName + ' PDS의 다이얼시간이 변경되었습니다.');
                        $('.-pds-timeout-data[data-group-id="' + pdsGroupId + '"]').val(result);
                        return;
                    }

                    let cause = '미확인';
                    if (result === 'ARG_ERROR')
                        cause = '다이얼시간 미설정';
                    else if (result === 'INVALID_VALUE')
                        cause = '올바르지 않은 다이얼시간';

                    alert(currentPdsName + ' PDS의 다이얼시간을 변경할 수 없습니다. (' + cause + ')');
                })
                .on('PDS_STATUS', function (message, kind, userId, executeId, newStatus) {
                    if (!pdsExecuteIds.has(executeId))
                        return;

                    let pdsStatusName = '';

                    switch (newStatus) {
                        case 'START_PDS' :
                            pdsStatusName = '진행중'
                            break;
                        case 'REQUESTED_STOP' :
                            pdsStatusName = '정지'
                            break;
                        case 'REQUESTED_DEL' :
                            pdsStatusName = '정지후삭제됨'
                            break;
                        case 'WAIT_NORMAL_STOP' :
                            pdsStatusName = '시도완료'
                            break;
                        case 'NORMAL_STOP' :
                            pdsStatusName = '완료됨'
                            break;
                        case 'TIMEOVER_STOP' :
                            pdsStatusName = '종료시간 설정에 의한 종료'
                            break;
                        case 'COUNT_STOP' :
                            pdsStatusName = '일정건수 설정에 의한 종료'
                            break;
                        case 'RSCHCOUNT_STOP' :
                            pdsStatusName = '설문 답변건수 설정에 의한 종료'
                            break;
                        case 'EXCEPT_STOP' :
                            pdsStatusName = '비정상종료'
                            break;
                        default:
                            pdsStatusName = newStatus;
                    }

                    $('.-pds-status[data-execute-id="' + executeId + '"]').text(pdsStatusName);
                })
                .on('PDS_STAT', function (message, kind, userId, executeId, processingAndRest, tryAndCallAndEnd, receiveAndFailAndCallingAndNoconnectedAndEtc) {
                    if (!pdsExecuteIds.has(executeId))
                        return;

                    $('.-processing-rest[data-execute-id="' + executeId + '"]').text(processingAndRest.replace(/\//g, ' / '));
                    $('.-try-call-end[data-execute-id="' + executeId + '"]').text(tryAndCallAndEnd.replace(/\//g, ' / '));
                    $('.-receive-fail-calling-noconnected-etc[data-execute-id="' + executeId + '"]').text(receiveAndFailAndCallingAndNoconnectedAndEtc.replace(/\//g, ' / '));
                })
                .on('Bye', function () {
                    this.recovery();
                })
                .on('SERVER_STATUS', function (kind) {
                    if (kind === 'ERROR')
                        this.recovery();
                })
                .on('PDSMEMBERSTATUS', function (message, status, userId) {
                    const pdsStatus = pdsStatuses[userId];
                    if (!pdsStatus)
                        return;

                    pdsStatus.paused = status;
                    pdsStatus.callStatusUpdatedTime = new Date().getTime();

                    updatePDSPersonStatusByUserId(userId, pdsStatus);
                });

            updatePDSPersonStatus();
        </script>
    </tags:scripts>
</tags:tabContentLayout>
