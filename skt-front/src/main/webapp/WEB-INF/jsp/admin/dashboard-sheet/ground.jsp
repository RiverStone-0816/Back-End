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
    <div class="dashboard-wrapper">
        <div class="content-inner" id="main-dashboard">
            <div class="top">
                <div class="panel panel-dashboard">
                    <div class="panel-heading">
                        <div class="panel-label">호 통계 및 관련</div>
                    </div>
                    <div class="panel-body">
                        <div class="ui grid remove-mb">
                            <div class="five wide column remove-pr">
                                <div class="ui grid">
                                    <div class="sixteen wide column remove-pb">
                                        <div class="visual-panel">
                                            <div class="panel-heading">실시간 응답율</div>
                                            <div class="panel-body">
                                                <span class="num -data-response-rate">%</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="eight wide column remove-pr">
                                        <div class="visual-panel">
                                            <div class="panel-heading">수신건</div>
                                            <div class="panel-body">
                                                <span class="num -data-success-call"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="eight wide column">
                                        <div class="visual-panel">
                                            <div class="panel-heading">비수신건</div>
                                            <div class="panel-body">
                                                <span class="num -data-cancel-call"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="eleven wide column">
                                <div class="visual-panel">
                                    <div class="panel-heading">인바운드 현황</div>
                                    <div class="panel-body">
                                        <div class="chart-wrap" id="inbound-chart"></div>
                                        <div class="chart-label-wrap-bar">
                                            <ul>
                                                <li><span class="symbol bcolor-bar1" style="background-color: #D81159 !important;"></span><span class="text">I/B전체</span></li>
                                                <li><span class="symbol bcolor-bar2" style="background-color: #218380 !important;"></span><span class="text">무효콜</span></li>
                                                <li><span class="symbol bcolor-bar3" style="background-color: #FFBC42 !important;"></span><span class="text">연결요청</span></li>
                                                <li><span class="symbol bcolor-bar4" style="background-color: #F7AA97 !important;"></span><span class="text">응대호</span></li>
                                                <li><span class="symbol bcolor-bar5" style="background-color: #808080 !important;"></span><span class="text">포기호</span></li>
                                                <li><span class="symbol bcolor-bar6" style="background-color: #4F86C6 !important;"></span><span class="text">콜백</span></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <table class="ui celled table compact unstackable structured">
                            <thead>
                            <tr>
                                <th rowspan="2">날짜/시간</th>
                                <th colspan="6">I/B 콜 현황</th>
                                <th colspan="7">응답호 분석</th>
                                <th colspan="6">포기호 분석</th>
                                <th colspan="4">기타 분석</th>
                            </tr>
                            <tr>
                                <th>I/B 전체콜</th>
                                <th>무효콜</th>
                                <th>연결요청</th>
                                <th>응대호</th>
                                <th>포기호</th>
                                <th>콜백</th>
                                <th>호응답률</th>
                                <th>서비스레벨 호응답률</th>
                                <th>~10(초)</th>
                                <th>~20(초)</th>
                                <th>~30(초)</th>
                                <th>~40(초)</th>
                                <th>40~(초)</th>
                                <th>호 포기율</th>
                                <th>~10(초)</th>
                                <th>~20(초)</th>
                                <th>~30(초)</th>
                                <th>~40(초)</th>
                                <th>40~(초)</th>
                                <th>무효콜비율</th>
                                <th>I/B 총통화시간</th>
                                <th>평균통화시간</th>
                                <th>평균대기시간</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>합계</td>

                                <td>${inboundData.total}</td>
                                <td>${inboundData.onlyRead}</td>
                                <td>${inboundData.connReq}</td>
                                <td>${inboundData.success}</td>
                                <td>${inboundData.cancel}</td>
                                <td>${inboundData.callbackSuccess}</td>

                                <td>${String.format("%.1f", inboundData.responseRate)}%</td>
                                <td>${String.format("%.1f", inboundData.svcLevelAvg)}%</td>
                                <td>${inboundData.waitSucc_0_10}</td>
                                <td>${inboundData.waitSucc_10_20}</td>
                                <td>${inboundData.waitSucc_20_30}</td>
                                <td>${inboundData.waitSucc_30_40}</td>
                                <td>${inboundData.waitSucc_40}</td>

                                <td>${inboundData.cancelAvg}%</td>
                                <td>${inboundData.waitCancel_0_10}</td>
                                <td>${inboundData.waitCancel_10_20}</td>
                                <td>${inboundData.waitCancel_20_30}</td>
                                <td>${inboundData.waitCancel_30_40}</td>
                                <td>${inboundData.waitCancel_40}</td>

                                <td>${String.format("%.1f", inboundData.ivrAvg)}%</td>
                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(inboundData.billSecSum)}</td>
                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(inboundData.billSecAvg)}</td>
                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(inboundData.waitAvg)}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="bottom">
                <div class="state-area">
                    <div class="panel panel-dashboard">
                        <div class="panel-heading">
                            <div class="panel-label">상담원 상태</div>
                        </div>
                        <div class="panel-body">
                            <div class="ui grid">
                                <div class="six wide column">
                                    <div class="chart-wrap" style="padding: 0 30px;">
                                        <div id="pie-consultant-status"></div>
                                    </div>
                                    <div class="chart-label-wrap-circle">
                                        <ul>
                                            <c:set var="picColors" value="${['#218380', '#d81159', '#ffbc42', '#4F86C6', '#f7aa97', '#808080', '#4f86c6', '#9055A2', '#D499B9', '#2E294F']}"/>
                                            <c:forEach var="status" items="${statuses}" varStatus="vStatus">
                                                <c:if test="${status.key != 9}">
                                                    <li>
                                                        <span class="symbol bcolor-bar${status.key + 1}" style="background-color: ${picColors[vStatus.index]} !important;"></span>
                                                        <span class="text">${g.htmlQuote(status.value)}</span>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>
                                <div class="ten wide column">
                                    <table class="ui celled table compact">
                                        <thead>
                                        <tr>
                                            <th>콜그룹명</th>
                                            <th>고객대기</th>

                                            <c:forEach var="status" items="${statuses}">
                                                <c:if test="${status.key != 9}">
                                                    <th>${g.htmlQuote(status.value)}</th>
                                                </c:if>
                                            </c:forEach>

                                            <th>로그인</th>
                                            <th>로그아웃</th>
                                            <th>전체</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                            <c:when test="${huntConsultantsStat.size() > 0}">
                                                <c:forEach var="e" items="${huntConsultantsStat}">
                                                    <tr>
                                                        <td>${g.htmlQuote(e.queueHanName)}</td>
                                                        <td class="-custom-wait-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.customWait}</td>

                                                        <c:forEach var="status" items="${statuses}">
                                                            <c:if test="${status.key != 9}">
                                                                <td class="-consultant-status-count" data-value="${status.key}"
                                                                    data-hunt="${g.htmlQuote(e.queueName)}">${e.statusCountMap.getOrDefault(status.key, 0)}</td>
                                                            </c:if>
                                                        </c:forEach>

                                                        <td class="-login-user-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.loginCount}</td>
                                                        <td class="-logout-user-count" data-hunt="${g.htmlQuote(e.queueName)}">${e.logoutCount}</td>
                                                        <td>${e.total}</td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="11" class="null-data">조회된 데이터가 없습니다.</td>
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
                <div class="rank-area">
                    <div class="panel panel-dashboard">
                        <div class="panel-heading">
                            <div class="pull-left">
                                <div class="panel-label" id="ranking-title1" data-index="0">최다수신 TOP 10</div>
                            </div>
                            <div class="pull-right">
                                <button type="button" class="arrow-btn" onclick="moveRankingContents(1, -1)">◀</button>
                                <button type="button" class="arrow-btn" onclick="moveRankingContents(1, 1)">▶</button>
                            </div>
                        </div>
                        <div class="panel-body">
                            <table class="ui celled table compact">
                                <thead>
                                <tr>
                                    <th>상담사명</th>
                                    <th>상담사ID</th>
                                    <th>내선번호</th>
                                    <th>개인번호</th>
                                    <th>상태</th>
                                </tr>
                                </thead>
                                <tbody id="ranking-sheet1"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="rank-area">
                    <div class="panel panel-dashboard">
                        <div class="panel-heading">
                            <div class="pull-left">
                                <div class="panel-label" id="ranking-title2" data-index="1">최다수신 TOP 10</div>
                            </div>
                            <div class="pull-right">
                                <button type="button" class="arrow-btn" onclick="moveRankingContents(2, -1)">◀</button>
                                <button type="button" class="arrow-btn" onclick="moveRankingContents(2, 1)">▶</button>
                            </div>
                        </div>
                        <div class="panel-body">
                            <table class="ui celled table compact">
                                <thead>
                                <tr>
                                    <th>상담사명</th>
                                    <th>상담사ID</th>
                                    <th>내선번호</th>
                                    <th>개인번호</th>
                                    <th>상태</th>
                                </tr>
                                </thead>
                                <tbody id="ranking-sheet2"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            function loadInboundData() {
                restSelf.get("/api/screen-data/integration", null, null, true).done(function (data) {
                    $('.-data-response-rate').text(data.data.responseRate.toFixed(1) + '%');
                    $('.-data-success-call').text(data.data.successCall);
                    $('.-data-cancel-call').text(data.data.cancelCall);
                    $('.-data-connection-request').text(data.data.connectionRequest);
                    $('.-data-inbound-call').text(data.data.inboundCall);
                });
            }

            setInterval(loadInboundData, 1000 * 3);

            $(window).on('load', loadInboundData);

            chartjs.drawDonutChart('#pie-consultant-status', [
                <c:choose>
                <c:when test="${statusCountMapSum > 0}">

                <c:forEach var="status" items="${statuses}">
                <c:if test="${status.key != 9}">
                ${statusCountMap.getOrDefault(status.key, 0)},
                </c:if>
                </c:forEach>

                </c:when>
                <c:otherwise>
                1
                </c:otherwise>
                </c:choose>
            ], {
                colorClasses: [
                    <c:choose>
                    <c:when test="${statusCountMapSum > 0}">

                    <c:forEach var="status" items="${statuses}">
                    <c:if test="${status.key != 9}">
                    'bcolor-bar${status.key + 1}',
                    </c:if>
                    </c:forEach>

                    </c:when>
                    <c:otherwise>
                    'bcolor-bar0'
                    </c:otherwise>
                    </c:choose>
                ],
                colors: [
                    <c:choose>
                    <c:when test="${statusCountMapSum > 0}">

                    <c:forEach var="status" items="${statuses}" varStatus="vStatus">
                    '${g.escapeQuote(picColors[vStatus.index])}',
                    </c:forEach>

                    </c:when>
                    <c:otherwise>
                    '#AAAAAA'
                    </c:otherwise>
                    </c:choose>
                ],
                labels: [
                    <c:choose>
                    <c:when test="${statusCountMapSum > 0}">

                    <c:forEach var="status" items="${statuses}">
                    '${g.escapeQuote(status.value)}',
                    </c:forEach>

                    </c:when>
                    <c:otherwise>
                    ''
                    </c:otherwise>
                    </c:choose>
                ],
            });

            const consultantRecords = [
                <c:forEach var="e" items="${consultantRecords}">
                {
                    customNumber: '${g.escapeQuote(e.customNumber)}',
                    id: '${g.escapeQuote(e.person.id)}',
                    idName: '${g.escapeQuote(e.person.idName)}',
                    extension: '${g.escapeQuote(e.person.extension)}',
                    peer: '${g.escapeQuote(e.person.peer)}',
                    paused: '${g.escapeQuote(e.person.paused)}',
                    isLogin: '${g.escapeQuote(e.person.isLogin)}',
                    values: {
                        inSuccess: ${e.inSuccess},
                        outSuccess: ${e.outSuccess},
                        inBillsecSum: ${e.inBillsecSum},
                        outBillsecSum: ${e.outBillsecSum},
                        callbackSuccess: ${e.callbackSuccess},
                    },
                },
                </c:forEach>
            ];

            const rankingKeywords = [
                {property: 'inSuccess', title: '최다수신'},
                {property: 'outSuccess', title: '최다발신'},
                {property: 'inBillsecSum', title: '최장수신'},
                {property: 'outBillsecSum', title: '최장발신'},
                {property: 'billsecSum', title: '최장통화'},
                {property: 'callbackSuccess', title: '최다콜백'},
            ];

            const statuses = {
                <c:forEach var="e" items="${statuses}">
                '${e.key}': '${g.escapeQuote(e.value)}',
                </c:forEach>
            };

            const rankingTitles = ['ranking-title1', 'ranking-title2'];

            function moveRankingContents(index, move) {
                const title = $('#ranking-title' + index);
                const sheet = $('#ranking-sheet' + index).empty();

                if (!title.length)
                    return;

                const usingContentIndices = [];
                rankingTitles.map(function (e) {
                    if (e === 'ranking-title' + index)
                        return;
                    usingContentIndices.push(parseInt($('#' + e).attr('data-index')));
                });

                const currentContentIndex = (function () {
                    let currentContentIndex = (parseInt(title.attr('data-index')) + move) % rankingKeywords.length;

                    if (usingContentIndices.indexOf(currentContentIndex) >= 0)
                        currentContentIndex = currentContentIndex + (move >= 0 ? 1 : -1);

                    currentContentIndex = currentContentIndex >= 0 ? currentContentIndex : currentContentIndex + (Math.abs(parseInt(currentContentIndex / rankingKeywords.length)) + 1) * rankingKeywords.length;

                    title.attr('data-index', currentContentIndex);
                    return currentContentIndex;
                })();

                title.text(rankingKeywords[currentContentIndex].title + ' TOP 10');

                const propertyName = rankingKeywords[currentContentIndex].property;
                consultantRecords.sort(function (a, b) {
                    return a.values[propertyName] > b.values[propertyName] ? -1 : a.values[propertyName] < b.values[propertyName] ? 1 : 0;
                });

                for (let i = 0; i < 10 && i < consultantRecords.length; i++) {
                    const e = consultantRecords[i];
                    sheet.append(
                        $('<tr/>')
                            .append($('<td/>', {text: e.idName}))
                            .append($('<td/>', {text: e.id}))
                            .append($('<td/>', {text: e.extension}))
                            .append($('<td/>', {text: e.peer}))
                            .append($('<td/>', {class: '-consultant-status', 'data-peer': e.peer,text: statuses[e.paused]}))
                    );
                    updatePersonStatus();
                }
            }
            $(window).on('load', function () {
                moveRankingContents(1, 0);
                moveRankingContents(2, 0);
            });

            const inboundChartData = [
                <c:forEach var="e" items="${inboundChart}">
                {
                    hour: ${e.key} +'시',
                    totalCnt: ${e.value != null ? e.value.totalCnt : 0},
                    onlyReadCnt: ${e.value != null ? e.value.onlyReadCnt : 0},
                    connReqCnt: ${e.value != null ? e.value.connReqCnt : 0},
                    successCnt: ${e.value != null ? e.value.successCnt : 0},
                    cancelCnt: ${e.value != null ? e.value.cancelCnt : 0},
                    callbackCnt: ${e.value != null ? e.value.callbackCnt : 0},
                },
                </c:forEach>
            ];

            chartjs.drawLineChart(
                document.getElementById('inbound-chart'),
                inboundChartData,
                'hour',
                ['totalCnt', 'onlyReadCnt', 'connReqCnt', 'successCnt', 'cancelCnt', 'callbackCnt',],
                {
                    ticks: 4,
                    yLabel: '',
                    unitWidth: 30,
                    colorClasses: ['bcolor-bar1', 'bcolor-bar2', 'bcolor-bar3', 'bcolor-bar4', 'bcolor-bar5', 'bcolor-bar6'],
                    colors: ['#D81159', '#218380', '#FFBC42', '#F7AA97', '#808080', '#4F86C6'],
                    labels: ['I/B전체', '무효콜', '연결요청', '응대호', '포기호', '콜백']
                }
            );
        </script>
    </tags:scripts>
</tags:tabContentLayout>
