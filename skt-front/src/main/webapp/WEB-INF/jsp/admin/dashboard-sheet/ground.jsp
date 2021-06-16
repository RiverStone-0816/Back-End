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
                                                <li><span class="symbol bcolor-bar1"></span><span class="text">I/B전체</span></li>
                                                <li><span class="symbol bcolor-bar2"></span><span class="text">단순조회</span></li>
                                                <li><span class="symbol bcolor-bar3"></span><span class="text">연결요청</span></li>
                                                <li><span class="symbol bcolor-bar4"></span><span class="text">응대호</span></li>
                                                <li><span class="symbol bcolor-bar5"></span><span class="text">포기호</span></li>
                                                <li><span class="symbol bcolor-bar6"></span><span class="text">콜백</span></li>
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
                                <th colspan="6">성과지표</th>
                                <th colspan="5">응대호 대기시간 분석</th>
                                <th colspan="5">포기호 대기시간 분석</th>
                            </tr>
                            <tr>
                                <th>I/B<br>전체콜</th>
                                <th>단순조회</th>
                                <th>연결요청</th>
                                <th>응대호</th>
                                <th>포기호</th>
                                <th>콜백</th>

                                <th>I/B<br>총통화시간</th>
                                <th>평균통화시간</th>
                                <th>평균대기시간</th>
                                <th>호응답률</th>
                                <th>서비스레벨<br>호응답률</th>
                                <th>단순조회율</th>
                                <th>~10(초)</th>
                                <th>~20(초)</th>
                                <th>~30(초)</th>
                                <th>~40(초)</th>
                                <th>40~(초)</th>
                                <th>~10(초)</th>
                                <th>~20(초)</th>
                                <th>~30(초)</th>
                                <th>~40(초)</th>
                                <th>40~(초)</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th>합계</th>

                                <td><span>${inboundData.total}</span></td>
                                <td><span>${inboundData.onlyRead}</span></td>
                                <td><span>${inboundData.connReq}</span></td>
                                <td><span>${inboundData.success}</span></td>
                                <td><span>${inboundData.cancel}</span></td>
                                <td><span>${inboundData.callbackSuccess}</span></td>

                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(inboundData.billSecSum)}</td>
                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(inboundData.billSecAvg)}</td>
                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(inboundData.waitAvg)}</td>
                                <td>${inboundData.responseRate}%</td>
                                <td>${inboundData.svcLevelAvg}%</td>
                                <td>${inboundData.ivrAvg}%</td>

                                <td>${inboundData.waitSucc_0_10}</td>
                                <td>${inboundData.waitSucc_10_20}</td>
                                <td>${inboundData.waitSucc_20_30}</td>
                                <td>${inboundData.waitSucc_30_40}</td>
                                <td>${inboundData.waitSucc_40}</td>

                                <td>${inboundData.waitCancel_0_10}</td>
                                <td>${inboundData.waitCancel_10_20}</td>
                                <td>${inboundData.waitCancel_20_30}</td>
                                <td>${inboundData.waitCancel_30_40}</td>
                                <td>${inboundData.waitCancel_40}</td>
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
                                    <div class="chart-wrap">
                                        <svg id="pie-consultant-status" style="width: 100%; height: 100%;"></svg>
                                    </div>
                                    <div class="chart-label-wrap-circle">
                                        <ul>
                                            <c:forEach var="status" items="${statuses}">
                                                <c:if test="${status.key != 9}">
                                                    <li>
                                                        <span class="symbol bcolor-bar${status.key + 1}"></span>
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

            setInterval(loadInboundData, 1000 * 30);

            $(window).on('load', loadInboundData);

            drawDonutChart('#pie-consultant-status', [
                <c:choose>
                <c:when test="${statusCountMapSum > 0}">

                <c:forEach var="status" items="${statuses}">
                <c:if test="${status.key != 9}">
                ${statusCountMap.getOrDefault(status.key, 0) / statusCountMapSum},
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
                    'bcolor-bar${status.key+ 1}',
                    </c:if>
                    </c:forEach>

                    </c:when>
                    <c:otherwise>
                    'bcolor-bar0'
                    </c:otherwise>
                    </c:choose>
                ]
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

                const currentContentIndex = (function extract() {
                    const currentContentIndex = (parseInt(title.attr('data-index')) + move) % rankingKeywords.length;
                    title.attr('data-index', currentContentIndex);

                    if (usingContentIndices.indexOf(currentContentIndex) >= 0)
                        return extract();

                    return currentContentIndex;
                })();

                title.text(rankingKeywords[currentContentIndex].title + ' TOP 10');

                const propertyName = rankingKeywords[currentContentIndex].property;
                consultantRecords.sort(function (a, b) {
                    return a.values[propertyName] > b.values[propertyName];
                });

                for (let i = 0; i < 10 && i < consultantRecords.length; i++) {
                    const e = consultantRecords[i];
                    sheet.append(
                        $('<tr/>')
                            .append($('<td/>', {text: e.idName}))
                            .append($('<td/>', {text: e.id}))
                            .append($('<td/>', {text: e.extension}))
                            .append($('<td/>', {text: e.peer}))
                            .append($('<td/>', {class: 'bcolor-bar' + e.paused, text: statuses[e.paused]}))
                    );
                }
            }

            $(window).on('load', function () {
                moveRankingContents(1, 0);
                moveRankingContents(2, 0);
            });

            const inboundChartData = [
                <c:forEach var="e" items="${inboundChart}">
                {
                    hour: ${e.key},
                    totalCnt: ${e.value != null ? e.value.totalCnt : 0},
                    onlyReadCnt: ${e.value != null ? e.value.onlyReadCnt : 0},
                    connReqCnt: ${e.value != null ? e.value.connReqCnt : 0},
                    successCnt: ${e.value != null ? e.value.successCnt : 0},
                    cancelCnt: ${e.value != null ? e.value.cancelCnt : 0},
                    callbackCnt: ${e.value != null ? e.value.callbackCnt : 0},
                },
                </c:forEach>
            ];

            drawLineChart(
                document.getElementById('inbound-chart'),
                inboundChartData,
                'hour',
                ['totalCnt', 'onlyReadCnt', 'connReqCnt', 'successCnt', 'cancelCnt', 'callbackCnt',],
                {ticks: 4, yLabel: '', unitWidth: 30, colorClasses: ['bcolor-bar1', 'bcolor-bar2', 'bcolor-bar3', 'bcolor-bar4', 'bcolor-bar5', 'bcolor-bar6']}
            );
        </script>
    </tags:scripts>
</tags:tabContentLayout>
