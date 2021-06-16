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

<%--@elvariable id="config" type="kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScreenConfig"--%>
<%--@elvariable id="data" type="kr.co.eicn.ippbx.front.model.ScreenDataForIntegration"--%>
<%--@elvariable id="statusCodes" type="java.util.Map"--%>

<tags:layout-screen>
    <div class="billboard-wrap ${config.lookAndFeel == 2 ? 'theme2' : config.lookAndFeel == 3 ? 'theme3' : ''}">
        <div class="header">
            <div class="pull-left">${g.htmlQuote(config.name)}</div>
            <div class="pull-right">
                <div class="time-wrap -time"></div>
            </div>
        </div>
        <div class="content">
            <div class="ui grid full-height remove-margin">
                <div class="equal width row" style="height:42%">
                    <div class="column">
                        <div class="board-box incoming-call full-height">
                            <div class="board-title flex-100">인입콜</div>
                            <div class="board-number flex-160">
                                <text class="-data-inbound-call">${data.inboundCall}</text>
                            </div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box connection-request full-height">
                            <div class="board-title flex-100">연결요청</div>
                            <div class="board-number flex-160">
                                <text class="-data-connection-request">${data.connectionRequest}</text>
                            </div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box reception full-height">
                            <div class="board-title flex-100">수신</div>
                            <div class="board-number flex-160">
                                <text class="-data-success-call">${data.successCall}</text>
                            </div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box non-reception full-height">
                            <div class="board-title flex-100">비수신</div>
                            <div class="board-number flex-160">
                                <text class="-data-cancel-call">${data.cancelCall}</text>
                            </div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box response-rate full-height">
                            <div class="board-title flex-100">응대율</div>
                            <div class="board-number flex-160">
                                <text class="-data-response-rate"><fmt:formatNumber value="${data.responseRate}" pattern="#.#"/></text>
                                %
                            </div>
                        </div>
                    </div>
                </div>

                <div class="sixteen wide column" style="height:58%">
                    <div class="billboard-chart-wrap" >
                        <div class="billboard-chart-container" id="chart" >

                        </div>
                        <div class="billboard-chart-label-wrap">
                            <ul>
                                <li><span class="symbol bcolor-bar1"></span><span class="text">I/B전체</span></li>
                                <li><span class="symbol bcolor-bar2"></span><span class="text">연결요청</span></li>
                                <li><span class="symbol bcolor-bar3"></span><span class="text">응대호</span></li>
                                <li><span class="symbol bcolor-bar4"></span><span class="text">포기호</span></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${config.showSlidingText}">
            <div class="footer">
                <h2 style="width: 100%;">
                    <text id="sliding-text" class="marquee-text">${g.htmlQuote(config.slidingText)}</text>
                </h2>
            </div>
        </c:if>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            setInterval(function () {
                $('.-time').text(moment().format('YYYY-MM-DD HH:mm:ss'));
            }, 500);

            $(document).ready(function () {
                window.resizeTo(620, 690);
            });

            $(window).on('load', function () {
                window.resizeTo(1800, 1000);
            });

            setInterval(function () {
                restSelf.get("/api/screen-data/integration", null, null, true).done(function (data) {
                    $('.-data-response-rate').text(data.data.responseRate.toFixed(1));
                    $('.-data-success-call').text(data.data.successCall);
                    $('.-data-cancel-call').text(data.data.cancelCall);
                    $('.-data-connection-request').text(data.data.connectionRequest);
                    $('.-data-inbound-call').text(data.data.inboundCall);
                });
            }, 1000 * 30);

            updatePersonStatus();
            updateQueues();

            const textElement = $('#sliding-text');
            const container = textElement.parent();
            setInterval(function () {
                const textWidth = textElement.width();
                const containerWidth = container.width();

                if (textWidth >= containerWidth)
                    container.addClass('marquee');
                else
                    container.removeClass('marquee');
            }, 3 * 1000);

            function drawChart() {
                restSelf.get('/api/dashboard/dashboard-inboundchart', null, null, true).done(function (response) {
                    const data = response.data.inboundChat;
                    const inboundChat = [];

                    for (let i = 0; i < 24; i++)
                        inboundChat.push(data[i] || {statHour: i, totalCnt: 0, connReqCnt: 0, successCnt: 0, cancelCnt: 0});

                    drawLineChart(
                        document.getElementById('chart'),
                        inboundChat,
                        'statHour',
                        ['totalCnt', 'connReqCnt', 'successCnt', 'cancelCnt'],
                        {ticks: 4, yLabel: '', unitWidth: 30, colorClasses: ['bcolor-bar1', 'bcolor-bar2', 'bcolor-bar3', 'bcolor-bar4', 'bcolor-bar5', 'bcolor-bar6']}
                    );
                });
            }

            $(window).on('load', function () {
                drawChart();
                setInterval(drawChart, 5 * 60 * 1000);
            });
        </script>
    </tags:scripts>
</tags:layout-screen>
