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

                    <div class="panel-section">
                        <div class="panel-sub-title">스케쥴명 : 대표</div>
                        <div class="panel-sub-container">
                            <table class="ui celled table fixed compact unstackable">
                                <thead>
                                <tr>
                                    <th>스케쥴 종류</th>
                                    <th>시간</th>
                                    <th>스케쥴 종류</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>주간 목요일</td>
                                    <td>00:01:00 ~ 00:01:00</td>
                                    <td>IVR연결(1)</td>
                                </tr>
                                </tbody>
                            </table>
                            <div id="pds-ivr-container">

                                <div class="ui segments">
                                    <div class="ui segment">
                                        <span class="ui header">테스트IVR</span>
                                        <div class="ui popup top right" id="ivr-sound-625">
                                            <div class="maudio">
                                                <div class="maudio">      <audio src="http://122.49.74.233/api-server/api/v1/admin/sounds/ars/49/resource?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNjIwIiwiY29tcGFueUlkIjoicHJlbWl1bSIsImV4dGVuc2lvbiI6IjYyMCIsImlkVHlwZSI6IkEiLCJleHAiOjE2MjM3NjE1NDksImlhdCI6MTYyMzY3NTE0OX0.ysm8BLFrVAR9XimIQjK5qXEmXZ6ZsEfkcdcw2K9DyxXNfgZyWaPp6Rk98dqNUo6vLDXhdjAWiBx6o9EBt8Wo-A" initaudio="false"></audio>      <div class="audio-control">          <a href="javascript:" class="fast-reverse"></a>          <a href="javascript:" class="play"></a>          <a href="javascript:" class="fast-forward"></a>          <div class="progress-bar">              <div class="progress-pass"></div>          </div>          <div class="time-keep">              <span class="current-time">00:00</span> / <span class="duration">00:00</span>          </div>          <a class="mute"></a>          <div class="volume-bar">              <div class="volume-pass"></div>          </div>      </div>    </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="ui secondary segment">
                                        <div class="ui list">
                                            <div class="item">
                                                <div class="content">
                                                    <div class="header"><span class="ui circular label tiny">0</span>다시듣기</div>
                                                </div>
                                            </div>
                                            <div class="item">
                                                <div class="content">
                                                    <div class="header"><span class="ui circular label tiny">1</span>test연결</div>
                                                </div>
                                            </div>
                                            <div class="item">
                                                <div class="content">
                                                    <div class="header"><span class="ui circular label tiny">2</span>PDS헌트연결</div>
                                                </div>
                                            </div>
                                            <div class="item">
                                                <div class="content">
                                                    <div class="header"><span class="ui circular label tiny">3</span>하위 듣기</div>
                                                    <div class="list">
                                                        <div class="item">
                                                            <i class="folder icon"></i>
                                                            <div class="content">
                                                                <div class="header">새로운IVR
                                                                    <div class="ivr-control">
                                                                        <button type="button" class="ui basic button compact -play-trigger" data-target="#ivr-sound-629">음원듣기</button>
                                                                        <button type="button" class="ui basic button compact" onclick="popupKeyMapModal(630)">버튼맵핑</button>
                                                                        <button type="button" class="ui basic button compact" onclick="deleteEntity(45)">삭제</button>
                                                                    </div>
                                                                    <div class="ui popup top right" id="ivr-sound-629">
                                                                        <div class="maudio">
                                                                            <div class="maudio">      <audio src="http://122.49.74.233/api-server/api/v1/admin/sounds/ars/49/resource?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNjIwIiwiY29tcGFueUlkIjoicHJlbWl1bSIsImV4dGVuc2lvbiI6IjYyMCIsImlkVHlwZSI6IkEiLCJleHAiOjE2MjM3NjE1NDksImlhdCI6MTYyMzY3NTE0OX0.ysm8BLFrVAR9XimIQjK5qXEmXZ6ZsEfkcdcw2K9DyxXNfgZyWaPp6Rk98dqNUo6vLDXhdjAWiBx6o9EBt8Wo-A" initaudio="false"></audio>      <div class="audio-control">          <a href="javascript:" class="fast-reverse"></a>          <a href="javascript:" class="play"></a>          <a href="javascript:" class="fast-forward"></a>          <div class="progress-bar">              <div class="progress-pass"></div>          </div>          <div class="time-keep">              <span class="current-time">00:00</span> / <span class="duration">00:00</span>          </div>          <a class="mute"></a>          <div class="volume-bar">              <div class="volume-pass"></div>          </div>      </div>    </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="list">
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">0</span>다시듣기</div>

                                                                        </div>
                                                                    </div>
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">1</span>test연결2</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">2</span>PDS헌트연결2</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">3</span>하위 듣기</div>
                                                                            <div class="list">
                                                                                <div class="item">
                                                                                    <i class="folder open icon"></i>
                                                                                    <div class="content">
                                                                                        <div class="header">세번째IVR
                                                                                            <div class="ivr-control">
                                                                                                <button type="button" class="ui basic button compact -play-trigger" data-target="#ivr-sound-634">음원듣기</button>
                                                                                                <button type="button" class="ui basic button compact" onclick="popupKeyMapModal(635)">버튼맵핑</button>
                                                                                                <button type="button" class="ui basic button compact" onclick="deleteEntity(46)">삭제</button>
                                                                                            </div>
                                                                                            <div class="ui popup top right" id="ivr-sound-634">
                                                                                                <div class="maudio">
                                                                                                    <div class="maudio">      <audio src="http://122.49.74.233/api-server/api/v1/admin/sounds/ars/49/resource?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNjIwIiwiY29tcGFueUlkIjoicHJlbWl1bSIsImV4dGVuc2lvbiI6IjYyMCIsImlkVHlwZSI6IkEiLCJleHAiOjE2MjM3NjE1NDksImlhdCI6MTYyMzY3NTE0OX0.ysm8BLFrVAR9XimIQjK5qXEmXZ6ZsEfkcdcw2K9DyxXNfgZyWaPp6Rk98dqNUo6vLDXhdjAWiBx6o9EBt8Wo-A" initaudio="false"></audio>      <div class="audio-control">          <a href="javascript:" class="fast-reverse"></a>          <a href="javascript:" class="play"></a>          <a href="javascript:" class="fast-forward"></a>          <div class="progress-bar">              <div class="progress-pass"></div>          </div>          <div class="time-keep">              <span class="current-time">00:00</span> / <span class="duration">00:00</span>          </div>          <a class="mute"></a>          <div class="volume-bar">              <div class="volume-pass"></div>          </div>      </div>    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="list">
                                                                                            <div class="item">
                                                                                                <div class="content">
                                                                                                    <div class="header"><span class="ui circular label tiny">0</span>다시듣기</div>
                                                                                                    <div class="user-ul">
                                                                                                        <span class="title">IVR3_1_법인 대량구매</span>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state call">통화중</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state wait">대기</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state bell">벨울림</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state after">후처리</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state logout">로그아웃</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state etc">기타</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state call">통화중</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state wait">대기</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state bell">벨울림</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state after">후처리</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state logout">로그아웃</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state etc">기타</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state call">통화중</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state wait">대기</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state bell">벨울림</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state after">후처리</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state logout">로그아웃</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">홍길동[000]</div>
                                                                                                            <div class="state etc">기타</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">-</div>
                                                                                                            <div class="state">-</div>
                                                                                                        </div>
                                                                                                        <div class="box">
                                                                                                            <div class="name">-</div>
                                                                                                            <div class="state">-</div>
                                                                                                        </div>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <div class="content">
                                                                                                    <div class="header"><span class="ui circular label tiny">1</span>test연결3</div>

                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <div class="content">
                                                                                                    <div class="header"><span class="ui circular label tiny">2</span>PDS헌트연결3</div>

                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <div class="content">
                                                                                                    <div class="header"><span class="ui circular label tiny">3</span>이전단계</div>

                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <div class="content">
                                                                                                    <div class="header"><span class="ui circular label tiny">4</span>처음으로</div>

                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <div class="content">
                                                                                                    <div class="header"><span class="ui circular label tiny">5</span>안내종료</div>

                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">4</span>이전단계</div>

                                                                        </div>
                                                                    </div>
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">5</span>처음으로</div>

                                                                        </div>
                                                                    </div>
                                                                    <div class="item">
                                                                        <div class="content">
                                                                            <div class="header"><span class="ui circular label tiny">6</span>안내종료</div>

                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
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
                                                        class="ui circular label tiny">${e.monitorIvrTree.button}</span></c:if>${g.htmlQuote(e.monitorIvrTree.name)}[${e.monitorIvrTree.waitingCustomerCount}]</span>
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
