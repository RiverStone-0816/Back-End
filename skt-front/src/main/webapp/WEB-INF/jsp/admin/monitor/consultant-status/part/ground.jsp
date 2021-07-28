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
                        <div class="panel-label">센터현황관리[부서별]</div>
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
                                <th>부서별</th>
                                <td colspan="11">
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini brand compact -select-group">
                                            <i class="search icon"></i>
                                        </button>
                                        <input id="groupCode" name="groupCode" type="hidden" value="" onchange="console.log(1); filterGroup($(this).val())">
                                        <div class="ui breadcrumb -group-name">
                                            <span class="section">부서를 선택해 주세요.</span>
                                        </div>
                                        <button type="button" class="ui icon button mini compact -clear-group">
                                            <i class="undo icon"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <c:forEach var="group" items="${groupToPersons}">

                        <div class="panel-section -group-section" data-name="${g.escapeQuote(group.key)}">
                            <div class="panel-heading transparent">
                                <div class="panel-sub-title">${g.escapeQuote(groups.get(group.key))}</div>
                                <ul class="state-list">
                                    <li>
                                        대기:
                                        <text class="-group-consultant-status-count" data-group="${g.escapeQuote(group.key)}" data-status="0">
                                                ${groupToStatusToCount.get(group.key).getOrDefault((0).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                    <li>
                                        통화중:
                                        <text class="-group-consultant-status-count" data-group="${g.escapeQuote(group.key)}" data-status="1">
                                                ${groupToStatusToCount.get(group.key).getOrDefault((1).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                    <li>
                                        후처리:
                                        <text class="-group-consultant-status-count" data-group="${g.escapeQuote(group.key)}" data-status="2">
                                                ${groupToStatusToCount.get(group.key).getOrDefault((2).intValue(), 0)}
                                        </text>
                                        명
                                    </li>
                                    <li>
                                        기타:
                                        <text class="-group-consultant-status-count" data-group="${g.escapeQuote(group.key)}" data-status="3,4,5,6,7,8,9">
                                                ${groupToStatusToCount.get(group.key).getOrDefault((3).intValue(), 0)
                                                        + groupToStatusToCount.get(group.key).getOrDefault((4).intValue(), 0)
                                                        + groupToStatusToCount.get(group.key).getOrDefault((5).intValue(), 0)
                                                        + groupToStatusToCount.get(group.key).getOrDefault((6).intValue(), 0)
                                                        + groupToStatusToCount.get(group.key).getOrDefault((7).intValue(), 0)
                                                        + groupToStatusToCount.get(group.key).getOrDefault((8).intValue(), 0)
                                                        + groupToStatusToCount.get(group.key).getOrDefault((9).intValue(), 0)}
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
                                                <c:forEach var="i" begin="0" end="${(group.value.size() - 1) / 2}">
                                                    <c:set var="e" value="${group.value.get(i)}"/>
                                                    <tr>
                                                        <td>${g.htmlQuote(e.idName)}</td>
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
                                                <c:forEach var="i" begin="${(group.value.size() + 1) / 2}" end="${group.value.size() - 1}">
                                                    <c:set var="e" value="${group.value.get(i)}"/>
                                                    <tr>
                                                        <td>${g.htmlQuote(e.idName)}</td>
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
            function filterGroup(groupCode) {
                if (groupCode) {
                    $('.-group-section').hide().filter('[data-name="' + groupCode + '"]').show();
                } else {
                    $('.-group-section').show();
                }
            }

            const groupToPeers = {
                <c:forEach var="group" items="${groupToPersons}">
                '${g.escapeQuote(group.key)}': [<c:forEach var="person" items="${group.value}">'${g.escapeQuote(person.peer)}', </c:forEach>],
                </c:forEach>
            };

            const _updatePersonStatus = updatePersonStatus;

            window.updatePersonStatus = function () {
                _updatePersonStatus();

                $('.-group-consultant-status-count').each(function () {
                    const validStatuses = $(this).attr('data-status').split(',');
                    const validPeers = groupToPeers[$(this).attr('data-group')];

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
