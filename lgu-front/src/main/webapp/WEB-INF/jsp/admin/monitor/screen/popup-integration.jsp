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

<link href="<c:url value="/resources/vendors/flexslider/2.7.2/flexslider.css?version=${version}"/>" rel="stylesheet"/>
<tags:layout-screen>
    <div class="screen-container ${config.lookAndFeel == 1 ? 'basic-theme' : config.lookAndFeel == 2 ? 'black-theme' : 'blue-theme'}">
        <ul class="circles">
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
        </ul>
        <div class="header">
            <span class="pull-left title"><i class="material-icons"> how_to_reg </i> <text>${g.htmlQuote(config.name)}</text></span>
            <span class="pull-right -time"></span>
        </div>
        <div class="body">
            <div class="flexslider" style="width:100%; min-width:1500px;">
                <ul class="slides">
                    <li>
                        <c:choose>
                            <c:when test="${config.lookAndFeel == 1}">
                                <div class="flex-container">
                                    <div class="table-container">
                                        <div class="ui grid full-height">
                                            <div class="twelve wide column">
                                                <table class="padded blue">
                                                    <thead>
                                                    <tr>
                                                        <th colspan="4">통계현황</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <h3 class="data-label">응답률</h3>
                                                            <span class="big-size-font warning data-response-rate"><fmt:formatNumber value="${data.responseRate}" pattern="#.#"/></span>%
                                                        </td>
                                                        <td>
                                                            <h3 class="data-label">응대호</h3>
                                                            <span class="big-size-font data-success-call">${data.successCall}</span>
                                                        </td>
                                                        <td>
                                                            <h3 class="data-label">포기호</h3>
                                                            <span class="big-size-font data-cancel-call">${data.cancelCall}</span>
                                                        </td>
                                                        <td>
                                                            <h3 class="data-label">연결요청</h3>
                                                            <span class="big-size-font data-connection-request">${data.connectionRequest}</span>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <div class="four wide column">
                                                <table class="padded green">
                                                    <thead>
                                                    <tr>
                                                        <th colspan="2">대기현황</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <h3 class="data-label">대기호</h3>
                                                            <span class="big-size-font -custom-wait-count">${data.customerWaiting}</span>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <table class="table-fixed padded white">
                                            <caption>상담원 상태 현황</caption>
                                            <thead>
                                            <tr>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <th>${e.value}</th>
                                                    <c:if test="${e.key == 0}">
                                                        <th>비로그인 대기</th>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <td class="-consultant-status-count" data-value="${e.key}" ${e.key == 0 ? 'data-login="true"' : ''}></td>
                                                    <c:if test="${e.key == 0}">
                                                        <td class="-consultant-status-count" data-value="${e.key}" data-login="false"></td>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${config.lookAndFeel == 2}">
                                <div class="flex-container">
                                    <div class="table-container mb20">
                                        <table class="table-fixed blue regular-table">
                                            <thead>
                                            <tr>
                                                <th class="inverted">대기호</th>
                                                <th class="inverted">응답률</th>
                                                <th>인입호</th>
                                                <th>응대호</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td rowspan="5" class="inverted"><span class="very big-size-font -custom-wait-count">${data.customerWaiting}</span></td>
                                                <td rowspan="5" class="inverted"><span class="very big-size-font data-response-rate"><fmt:formatNumber value="${data.responseRate}" pattern="#.#"/></span>%</td>
                                                <td class="data-connection-request">${data.connectionRequest}</td>
                                                <td class="data-success-call">${data.successCall}</td>
                                            </tr>
                                            <tr>
                                                <th colspan="2">포기호</th>
                                            </tr>
                                            <tr>
                                                <td colspan="2" class="data-cancel-call">${data.cancelCall}</td>
                                            </tr>
                                            <tr>
                                                <th colspan="2">총인원</th>
                                            </tr>
                                            <tr>
                                                <td colspan="2" class="-consultant-status-count" data-value="0,1,2,3,4,5,6,7,8,9"></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="table-container flex-remove">
                                        <table class="table-fixed green regular-table">
                                            <thead>
                                            <tr>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <th>${e.value}</th>
                                                    <c:if test="${e.key == 0}">
                                                        <th>비로그인 대기</th>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <td class="-consultant-status-count" data-value="${e.key}" ${e.key == 0 ? 'data-login="true"' : ''}></td>
                                                    <c:if test="${e.key == 0}">
                                                        <td class="-consultant-status-count" data-value="${e.key}" data-login="false"></td>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <tbody>
                                    <tr>
                                        <th width="40%">고객대기</th>
                                        <td class="-custom-wait-count">${data.customerWaiting}</td>
                                        <th>응대율</th>
                                        <td class="data-response-rate">${data.responseRate}%</td>
                                    </tr>
                                    <tr>
                                        <th>상담대기</th>
                                        <td class="-consultant-status-count" data-value="0" data-login="true"></td>
                                        <th rowspan="2">연결요청</th>
                                        <td rowspan="2" class="data-connection-request">${data.connectionRequest}</td>
                                    </tr>
                                    <tr>
                                        <th>비로그인 대기</th>
                                        <td class="-consultant-status-count" data-value="0" data-login="false"></td>
                                    </tr>
                                    <tr>
                                        <th>상담중</th>
                                        <td class="-consultant-status-count" data-value="1"></td>
                                        <th rowspan="2">응대호</th>
                                        <td rowspan="2" class="data-success-call">${data.successCall}</td>
                                    </tr>
                                    <tr>
                                        <th>후처리</th>
                                        <td class="-consultant-status-count" data-value="2"></td>
                                    </tr>
                                    <tr>
                                        <th>휴식</th>
                                        <td class="-consultant-status-count" data-value="3"></td>
                                        <th rowspan="2">포기호</th>
                                        <td rowspan="2" class="data-cancel-call">${data.cancelCall}</td>
                                    </tr>
                                    <tr>
                                        <th>기타</th>
                                        <td class="-consultant-status-count" data-value="4,5,6,7,8,9"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>
            </div>
        </div>

        <c:if test="${config.showSlidingText}">
            <div class="marquee notice">
                <h2>${g.htmlQuote(config.slidingText)}</h2>
            </div>
        </c:if>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            <c:if test="${config.showSlidingText}">
            $('.notice .text').transition('fly left');
            </c:if>

            $('.flexslider').flexslider({
                animation: "slide",
                slideshow: true,
                slideshowSpeed: 700000,
                controlNav: false,
                start: function () {
                    $('.flexslider').resize();
                }
            });

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
                $('.flex-next').click();
            }, 3000);

            setInterval(function () {
                restSelf.get("/api/screen-data/integration").done(function (data) {
                    $('.data-response-rate').text(data.data.responseRate.toFixed(1));
                    $('.data-success-call').text(data.data.successCall);
                    $('.data-cancel-call').text(data.data.cancelCall);
                    $('.data-connection-request').text(data.data.connectionRequest);
                    $('.data-inbound-call').text(data.data.inboundCall);
                });
            }, 1000 * 30);

            updatePersonStatus();
            updateQueues();
        </script>
    </tags:scripts>
</tags:layout-screen>
