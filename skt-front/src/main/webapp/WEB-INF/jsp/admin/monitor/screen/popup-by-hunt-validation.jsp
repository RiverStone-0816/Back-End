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
<%--@elvariable id="statusCodeKeys" type="java.util.List"--%>

<%--@elvariable id="personStatuses" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse>"--%>
<%--@elvariable id="huntData" type="kr.co.eicn.ippbx.front.model.ScreenDataForByHunt"--%>

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
                <div class="equal width row" style="height:45%">
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

                <div class="equal width row remove-padding" style="height:55%">
                    <table class="billboard-table">
                        <thead>
                        <tr>
                            <th>헌트명</th>
                            <th>고객대기</th>
                            <th>상담대기</th>
                            <th>통화중</th>
                            <th>후처리</th>
                            <th>기타</th>
                            <th>응대율</th>
                        </tr>
                        </thead>
                        <c:forEach var="e" items="${huntData}" varStatus="status">
                            <c:if test="${status.index % 5 == 0}">
                                <tbody class="-queue-div ${status.first ? 'active' : ''}">
                            </c:if>
                            <tr>
                                <td>${g.htmlQuote(e.queueKoreanName)}</td>
                                <td class="-custom-wait-count" data-hunt="${g.escapeQuote(e.queueName)}">${e.customerWaiting}</td>
                                <td class="-consultant-status-count" data-value="0" data-hunt="${g.escapeQuote(e.queueName)}">${e.constantStatusCounts.getOrDefault(0, 0)}</td>
                                <td class="-consultant-status-count" data-value="1" data-hunt="${g.escapeQuote(e.queueName)}">${e.constantStatusCounts.getOrDefault(1, 0)}</td>
                                <td class="-consultant-status-count" data-value="2" data-hunt="${g.escapeQuote(e.queueName)}">${e.constantStatusCounts.getOrDefault(2, 0)}</td>
                                <td class="-consultant-status-count" data-value="3,4,5,6,7,8,9" data-hunt="${g.escapeQuote(e.queueName)}">${e.constantStatusCounts.getOrDefault(3, 0)
                                        + e.constantStatusCounts.getOrDefault(4, 0)
                                        + e.constantStatusCounts.getOrDefault(5, 0)
                                        + e.constantStatusCounts.getOrDefault(6, 0)
                                        + e.constantStatusCounts.getOrDefault(7, 0)
                                        + e.constantStatusCounts.getOrDefault(8, 0)
                                        + e.constantStatusCounts.getOrDefault(9, 0)}</td>
                                <td class="-hunt-response-rate" data-hunt="${g.escapeQuote(e.queueName)}"></td>
                            </tr>
                            <c:if test="${status.index % 5 == 4 || status.last}">
                                </tbody>
                            </c:if>
                        </c:forEach>
                    </table>
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

            updatePersonStatus();
            updateQueues();

            setInterval(function () {
                const divList = $('.-queue-div');
                const current = divList.filter('.active').removeClass('active');

                if (current.next().length > 0)
                    current.next().addClass('active');
                else
                    divList.first().addClass('active');
            }, 10 * 1000);

            function loadByHuntSuccessPer() {
                restSelf.get('/api/screen-data/by-hunt-successper', null, null, true).done(function (response) {
                    response.data.byHuntSuccessPerList.map(function (e) {
                        $('.-hunt-response-rate').filter('[data-hunt="' + e.queueName + '"]').text(e.successPer.toFixed(1) + '%');
                    });
                });
            }

            $(window).on('load', function () {
                loadByHuntSuccessPer();
                setInterval(loadByHuntSuccessPer, 30 * 1000);
            });
        </script>
    </tags:scripts>
</tags:layout-screen>
