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

<tags:tabContentLayout>

    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/total/"/>
        <div class="sub-content ui container fluid unstackable">
            <form class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">모니터링[큐그룹별]</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <input type="checkbox" name="newsletter" id="_newsletter" tabindex="0" class="hidden"><label for="_newsletter">검색 옵션 전체보기</label>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>큐그룹</th>
                                <td colspan="11">
                                    <div class="ui form">
                                        <select onchange="filterQueue($(this).val())">
                                            <option value="">전체</option>
                                            <c:forEach var="e" items="${queues}">
                                                <option value="${g.escapeQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <c:forEach var="queue" items="${queueToPersons}">
                        <div class="panel-section -queue-section" data-name="${g.escapeQuote(queue.key)}">
                            <div class="panel-heading transparent">
                                <div class="panel-sub-title">${g.htmlQuote(queues.get(queue.key))}</div>
                                <ul class="state-list">
                                    <li>
                                        대기:
                                        <text class="-queue-consultant-status-count" data-queue="${g.escapeQuote(queue.key)}" data-status="0">
                                                ${queueToStatusToCount.get(queue.key).getOrDefault((0).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                    <li>
                                        통화중:
                                        <text class="-queue-consultant-status-count" data-queue="${g.escapeQuote(queue.key)}" data-status="1">
                                                ${queueToStatusToCount.get(queue.key).getOrDefault((1).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                    <li>
                                        후처리:
                                        <text class="-queue-consultant-status-count" data-queue="${g.escapeQuote(queue.key)}" data-status="2">
                                                ${queueToStatusToCount.get(queue.key).getOrDefault((2).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                    <li>
                                        기타:
                                        <text class="-queue-consultant-status-count" data-queue="${g.escapeQuote(queue.key)}" data-status="3,4,5,6,7,8,9">
                                                ${queueToStatusToCount.get(queue.key).getOrDefault((3).intValue(), 0)
                                                        + queueToStatusToCount.get(queue.key).getOrDefault((4).intValue(), 0)
                                                        + queueToStatusToCount.get(queue.key).getOrDefault((5).intValue(), 0)
                                                        + queueToStatusToCount.get(queue.key).getOrDefault((6).intValue(), 0)
                                                        + queueToStatusToCount.get(queue.key).getOrDefault((7).intValue(), 0)
                                                        + queueToStatusToCount.get(queue.key).getOrDefault((8).intValue(), 0)
                                                        + queueToStatusToCount.get(queue.key).getOrDefault((9).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                </ul>
                            </div>
                            <div class="panel-body">
                                <div class="ui grid stackable">
                                    <div class="eight wide column">
                                        <div class="table-scroll-wrap">
                                            <table class="ui celled table compact unstackable">
                                                <thead>
                                                <tr>
                                                    <th>이름</th>
                                                    <th>내선</th>
                                                    <th>상태</th>
                                                    <th>인입경로</th>
                                                    <th colspan="2">통화량</th>
                                                    <th>고객번호</th>
                                                    <th>시간</th>
                                                    <th>감청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="i" begin="0" end="${(queue.value.size() - 1) / 2}">
                                                    <c:set var="e" value="${queue.value.get(i)}"/>
                                                    <tr>
                                                        <td>${e.idName}</td>
                                                        <td>${g.htmlQuote(e.extension)}</td>
                                                        <td class="-consultant-status -consultant-status-with-color bcolor-bar${e.paused + 1}" data-peer="${g.escapeQuote(e.peer)}">
                                                                ${memberStatuses.get(e.paused)}
                                                        </td>
                                                        <td class="-consultant-queue-name" data-peer="${g.escapeQuote(e.peer)}"></td>
                                                        <td>
                                                            수신:
                                                            <text class="-inbound-success" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.inboundSuccess}</text>
                                                            /
                                                            <text class="-inbound-total" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.inboundTotal}</text>
                                                        </td>
                                                        <td>
                                                            발신:
                                                            <text class="-outbound-success" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.outboundSuccess}</text>
                                                            /
                                                            <text class="-outbound-total" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.outboundTotal}</text>
                                                        </td>
                                                        <td class="-consultant-calling-custom-number" data-peer="${g.escapeQuote(e.peer)}">${g.htmlQuote(e.customNumber)}</td>
                                                        <td class="-consultant-status-time" data-peer="${g.escapeQuote(e.peer)}">00:00</td>
                                                        <td>
                                                            <button type="button" class="play-btn" onclick="eavesdrop('${g.escapeQuote(e.extension)}')">
                                                                <img src="<c:url value="/resources/images/play.svg"/>" alt="play">
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="eight wide column">
                                        <div class="table-scroll-wrap">
                                            <table class="ui celled table compact unstackable">
                                                <thead>
                                                <tr>
                                                    <th>이름</th>
                                                    <th>내선</th>
                                                    <th>상태</th>
                                                    <th>인입경로</th>
                                                    <th colspan="2">통화량</th>
                                                    <th>고객번호</th>
                                                    <th>시간</th>
                                                    <th>감청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="i" begin="${queue.value.size() / 2}" end="${queue.value.size() - 1}">
                                                    <c:set var="e" value="${queue.value.get(i)}"/>
                                                    <tr>
                                                        <td>${e.idName}</td>
                                                        <td>${g.htmlQuote(e.extension)}</td>
                                                        <td class="-consultant-status -consultant-status-with-color bcolor-bar${e.paused + 1}" data-peer="${g.escapeQuote(e.peer)}">
                                                                ${memberStatuses.get(e.paused)}
                                                        </td>
                                                        <td class="-consultant-queue-name" data-peer="${g.escapeQuote(e.peer)}"></td>
                                                        <td>
                                                            수신:
                                                            <text class="-inbound-success" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.inboundSuccess}</text>
                                                            /
                                                            <text class="-inbound-total" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.inboundTotal}</text>
                                                        </td>
                                                        <td>
                                                            발신:
                                                            <text class="-outbound-success" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.outboundSuccess}</text>
                                                            /
                                                            <text class="-outbound-total" data-id="${g.escapeQuote(e.id)}" data-peer="${g.escapeQuote(e.peer)}">${e.outboundTotal}</text>
                                                        </td>
                                                        <td class="-consultant-calling-custom-number" data-peer="${g.escapeQuote(e.peer)}">${g.htmlQuote(e.customNumber)}</td>
                                                        <td class="-consultant-status-time" data-peer="${g.escapeQuote(e.peer)}">00:00</td>
                                                        <td>
                                                            <button type="button" class="play-btn" onclick="eavesdrop('${g.escapeQuote(e.extension)}')">
                                                                <img src="<c:url value="/resources/images/play.svg"/>" alt="play">
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            function filterQueue(queueName) {
                if (queueName) {
                    $('.-queue-section').hide().filter('[data-name="' + queueName + '"]').show();
                } else {
                    $('.-queue-section').show();
                }
            }

            const _updatePersonStatus = updatePersonStatus;

            window.updatePersonStatus = function () {
                _updatePersonStatus();

                $('.-queue-consultant-status-count').each(function () {
                    const validStatuses = $(this).attr('data-status').split(',');
                    const validPeers = queues[$(this).attr('data-queue')].peers;

                    let count = 0;
                    values(peerStatuses).map(function (peer) {
                        if (validPeers.indexOf(peer.peer) >= 0 && validStatuses.indexOf(peer.status + '') >= 0)
                            count++;
                    });

                    $(this).text(count);
                });
            };

            setInterval(function () {
                restSelf.get('/api/stat/user/', {startDate: moment().format('YYYY-MM-DD'), endDate: moment().format('YYYY-MM-DD')}, null, true).done(function (response) {
                    response.data[0].userStatList.map(function (userStat) {
                        $('.-inbound-success[data-id="' + userStat.userId + '"]').text(userStat.inboundSuccess);
                        $('.-inbound-total[data-id="' + userStat.userId + '"]').text(userStat.inboundTotal);
                        $('.-outbound-success[data-id="' + userStat.userId + '"]').text(userStat.outboundSuccess);
                        $('.-outbound-total[data-id="' + userStat.userId + '"]').text(userStat.outboundTotal);
                    });
                });
            }, 30 * 1000);

            function eavesdrop(extension) {
                if (!window.parent.ipccCommunicator)
                    return alert('전화 소켓이 생성되어 있지 않습니다. 전화 권한이 있는 계정으로 로그인하세요.');
                window.parent.ipccCommunicator.eavesdrop(extension);
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>