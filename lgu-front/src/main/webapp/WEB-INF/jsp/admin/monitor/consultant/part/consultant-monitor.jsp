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

<div id="consultant-monitor" class="panel">
    <div class="panel-heading">
        <label class="control-label">상담원모니터링</label>
        <div class="pull-right">
            <%--<div class="ui action input">
                <input type="text" placeholder="큐(그룹)명">
                <button class="ui icon button">
                    <i class="search icon"></i>
                </button>
            </div>--%>
        </div>
    </div>
    <div class="panel-body">
        <table id="consultant-monitor-table" class="ui table celled fixed structured">
            <thead>
            <tr>
                <th rowspan="2">상담사명<i class="sort-icon material-icons"></i></th>
                <th rowspan="2">상담사ID<i class="sort-icon material-icons"></i></th>
                <th rowspan="2">내선번호<i class="sort-icon material-icons"></i></th>
                <th colspan="9" class="no-sort">상담사 개인별 상태</th>
                <th colspan="5" class="no-sort">상담사 개인별 통계</th>
            </tr>
            <tr>
                <th class="no-sort">전화기</th>
                <th class="no-sort">로그인</th>
                <th class="no-sort">인입큐</th>
                <th data-sort-index="6">상태<i class="sort-icon material-icons"></i></th>
                <th class="no-sort">유지시간</th>
                <th class="no-sort">수/발신</th>
                <th class="no-sort">고객번호</th>
                <th class="no-sort">등급</th>
                <th class="no-sort">STT</th>
                <th class="no-sort">수신</th>
                <th class="no-sort">발신</th>
                <th class="no-sort">합계</th>
                <th class="no-sort">총통화시간</th>
                <th class="no-sort">평균통화시간</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${list.size() > 0}">
                    <c:forEach var="e" items="${list}">
                        <tr>
                            <td data-sort-value="${g.htmlQuote(e.person.idName)}">${g.htmlQuote(e.person.idName)}</td>
                            <td data-sort-value="${g.htmlQuote(e.person.id)}">${g.htmlQuote(e.person.id)}</td>
                            <td data-sort-value="${g.htmlQuote(e.person.extension)}">${g.htmlQuote(e.person.extension)}</td>
                            <td>
                                <i class="phone icon ${e.isPhone != 'Y' ? 'translucent' : 'blue'} -consultant-phone-status" data-peer="${g.htmlQuote(e.person.peer)}"></i>
                            </td>
                            <td>
                                <i class="key icon ${e.person.isLogin != 'Y' ? 'translucent' : 'blue'} -consultant-login" data-value="${e.person.isLogin}" data-peer="${g.htmlQuote(e.person.peer)}" data-logon-class="blue" data-logout-class="translucent"></i>
                            </td>
                            <td class="-consultant-queue-name" data-peer="${g.htmlQuote(e.person.peer)}" data-default="${e.queueHanName}">${e.queueHanName}</td>
                            <td class="-consultant-status" data-sort-value="${g.htmlQuote(e.person.paused)}" data-peer="${g.htmlQuote(e.person.peer)}">${statuses.get(e.person.paused)}</td>
                            <td class="-consultant-status-time" data-peer="${g.htmlQuote(e.person.peer)}" data-time="${e.statusTime}">00:00</td>
                            <td class="-consultant-send-receive-status" data-peer="${g.htmlQuote(e.person.peer)}" data-default="${e.inOut == 'I' ? '수신' : e.inOut == 'O' ? '발신' : ''}"></td>
                            <td class="-consultant-calling-custom-number" data-peer="${g.htmlQuote(e.person.peer)}" data-default="${e.customNumber}">${e.customNumber}</td>
                            <td class="-consultant-calling-custom-number-grade" data-peer="${g.htmlQuote(e.person.peer)}" data-default="${e.gradeName}"></td>
                            <td><button type="button" class="ui button mini compact blue -consultant-calling-custom-number-stt" data-peer="${g.htmlQuote(e.person.peer)}" onclick="popupSttMonit('${e.person.id}')">STT</button></td>
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
                        <td colspan="15" class="null-data">조회된 데이터가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <table class="ui table celled structured">
            <thead>
            <tr>
                <th colspan="4">우수상담원</th>
            </tr>
            <tr>
                <th>구분</th>
                <th>상담사명</th>
                <th>콜수</th>
                <th>시간</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${excellentConsultants.size() > 0}">
                    <c:forEach var="e" items="${excellentConsultants}">
                        <tr>
                            <td>${excellentConsultantTypes.get(e.type.name())}</td>
                            <td>${g.htmlQuote(e.userName)}</td>
                            <td>${e.callCount}</td>
                            <td>${g.timeFormatFromSeconds(e.time)}</td>
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
<script>
    updatePersonStatus();

    function updatePhoneStatus(peer, status) {
        $('.-consultant-phone-status').each(function () {
            const peerStatus = peerStatuses[peer];
            if (!peerStatus) return;

            if (status != null) {
                if (status.contains('UNREGISTERED') || status.contains('UNREACHABLE'))
                    $('.-consultant-phone-status[data-peer="' + peerStatus.peer + '"]').addClass('translucent');
                else
                    $('.-consultant-phone-status[data-peer="' + peerStatus.peer + '"]').removeClass('translucent');
            } else
                $('.-consultant-phone-status[data-peer="' + peerStatus.peer + '"]').addClass('translucent');
        })
    }

    $(document).ready(function () {
        $('#consultant-monitor-table').tablesort();
    });

    $('#consultant-monitor-table').on('tablesort:complete', function (event, tablesort) {
        $('.sort-icon').text('');

        $(tablesort.$sortCells.get(tablesort.index)).find('.sort-icon').text(tablesort.direction === 'asc' ? "arrow_drop_down" : "arrow_drop_up");
    });
</script>
