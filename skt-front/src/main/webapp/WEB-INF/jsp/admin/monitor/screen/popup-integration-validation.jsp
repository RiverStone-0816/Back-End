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
                <div class="twelve wide column remove-pb">
                    <div class="ui equal width grid full-height flex-flow-column">
                        <div class="equal width row flex-130">
                            <div class="column">
                                <div class="board-box incoming-call full-height">
                                    <div class="board-title flex-100">인입콜</div>
                                    <div class="board-number large ${config.lookAndFeel == 1 ? 'compact' : ''} flex-160">
                                        <text class="-data-inbound-call">${data.inboundCall}</text>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-box connection-request full-height">
                                    <div class="board-title flex-100">연결요청</div>
                                    <div class="board-number large ${config.lookAndFeel == 1 ? 'compact' : ''} flex-160">
                                        <text class="-data-connection-request">${data.connectionRequest}</text>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row flex-100 remove-pb">
                            <div class="column">
                                <div class="board-box reception full-height">
                                    <div class="board-title flex-100">수신</div>
                                    <div class="board-number ${config.lookAndFeel == 1 ? 'compact' : ''} flex-140">
                                        <text class="-data-success-call">${data.successCall}</text>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-box non-reception full-height">
                                    <div class="board-title flex-100">비수신</div>
                                    <div class="board-number ${config.lookAndFeel == 1 ? 'compact' : ''} flex-140">
                                        <text class="-data-cancel-call">${data.cancelCall}</text>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-box response-rate full-height">
                                    <div class="board-title flex-100">응대율</div>
                                    <div class="board-number ${config.lookAndFeel == 1 ? 'compact' : ''} flex-140">
                                        <text class="-data-response-rate"><fmt:formatNumber value="${data.responseRate}" pattern="#.#"/></text>
                                        %
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="four wide column remove-padding">
                    <div class="ui one column grid full-height remove-margin">
                        <div class="column">
                            <div class="board-label">
                                <div class="left">대기</div>
                                <div class="right">
                                    <text class="-consultant-status-count" data-value="0" data-login="true"></text>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">통화중</div>
                                <div class="right">
                                    <text class="-consultant-status-count" data-value="1" data-login="true"></text>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">후처리</div>
                                <div class="right">
                                    <text class="-consultant-status-count" data-value="2" data-login="true"></text>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">로그아웃</div>
                                <div class="right">
                                    <text class="-logout-user-count"></text>
                                </div>
                            </div>
                        </div>

                        <c:forEach var="status" items="${statusCodes}">
                            <c:if test="${status.key != 0 && status.key != 1 && status.key != 2 && status.key != 9}">
                                <div class="column">
                                    <div class="board-label">
                                        <div class="left">${g.htmlQuote(status.value)}</div>
                                        <div class="right">
                                            <text class="-consultant-status-count" data-value="3,4,5,6,7,8" data-login="true"></text>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
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
        </script>
    </tags:scripts>
</tags:layout-screen>
