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
<%--@elvariable id="data" type="kr.co.eicn.ippbx.front.model.ScreenDataForByService"--%>
<%--@elvariable id="statusCodes" type="java.util.Map"--%>

<link href="<c:url value="/resources/vendors/flexslider/2.7.2/flexslider.css?version=${version}"/>" rel="stylesheet"/>
<tags:layout-screen>
    <style>
        .flex-direction-nav {display: none !important;}

        .marquee {
            height: 50px;
            overflow: hidden;
            position: relative;
        }
        .marquee h2 {
            font-size: 1.1em;
            color: white;
            position: absolute;
            width:  100%;
            height: 100%;
            margin: 0;
            line-height: 50px;
            text-align: left;
            /* Starting position */
            -moz-transform:translateX(100%);
            -webkit-transform:translateX(100%);
            transform:translateX(100%);
            /* Apply animation to this element */
            -moz-animation: marquee 30s linear infinite;
            -webkit-animation: marquee 30s linear infinite;
            animation: marquee 30s linear infinite;
        }
        /* Move it (define the animation) */
        @-moz-keyframes marquee {
            0%   { -moz-transform: translateX(100%); }
            100% { -moz-transform: translateX(-100%); }
        }
        @-webkit-keyframes marquee {
            0%   { -webkit-transform: translateX(100%); }
            100% { -webkit-transform: translateX(-100%); }
        }
        @keyframes marquee {
            0%   {
                -moz-transform: translateX(100%); /* Firefox bug fix */
                -webkit-transform: translateX(100%); /* Firefox bug fix */
                transform: translateX(100%);
            }
            100% {
                -moz-transform: translateX(-100%); /* Firefox bug fix */
                -webkit-transform: translateX(-100%); /* Firefox bug fix */
                transform: translateX(-100%);
            }
        }
    </style>

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
            <span class="pull-left title"><i class="material-icons"> how_to_reg </i><text id="service-number">${data.services.get(0).serviceNumber}</text> <text
                    id="service-name">${data.services.get(0).serviceName}</text></span>
            <span class="pull-right -time"></span>
        </div>
        <div class="body">
            <div class="flexslider" style="width:100%; min-width:1500px;">
                <ul class="slides">
                    <c:forEach var="datum" items="${data.services}">
                        <li data-service="${g.htmlQuote(datum.serviceNumber)}" data-service-number="${g.htmlQuote(datum.serviceNumber)}">
                            <c:choose>
                                <c:when test="${config.lookAndFeel == 1}">
                                    <div class="flex-container">
                                        <div class="table-container">
                                            <div class="ui grid full-height">
                                                <div class="ten wide column">
                                                    <table class="padded blue">
                                                        <thead>
                                                        <tr>
                                                            <th colspan="3">통계현황</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <tr>
                                                            <td>
                                                                <h3 class="data-label">응답률</h3>
                                                                <span class="big-size-font warning data-response-rate"><fmt:formatNumber value="${datum.responseRate}" pattern="#.#"/></span>%
                                                            </td>
                                                            <td>
                                                                <h3 class="data-label">응대호</h3>
                                                                <span class="big-size-font data-success-call">${datum.successCall}</span>
                                                            </td>
                                                            <td>
                                                                <h3 class="data-label">포기호</h3>
                                                                <span class="big-size-font data-cancel-call">${datum.cancelCall}</span>
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <div class="six wide column">
                                                    <table class="padded green">
                                                        <thead>
                                                        <tr>
                                                            <th colspan="3">대기현황</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <tr>
                                                            <td>
                                                                <h3 class="data-label">연결요청</h3>
                                                                <span class="big-size-font data-connection-request">${datum.connectionRequest}</span>
                                                            </td>
                                                            <td>
                                                                <h3 class="data-label">대기호</h3>
                                                                <span class="big-size-font -custom-wait-count">${datum.customerWaiting}</span>
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
                                                        <c:if test="${e.key != 9}">
                                                            <th>${e.value}</th>
                                                            <c:if test="${e.key == 0}">
                                                                <th>비로그인 대기</th>
                                                            </c:if>
                                                        </c:if>
                                                    </c:forEach>
                                                    <th>로그인</th>
                                                    <th>로그아웃</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <c:forEach var="e" items="${statusCodes}">
                                                        <c:if test="${e.key != 9}">
                                                            <td class="-consultant-status-count" data-value="${e.key}" data-service="${g.htmlQuote(datum.serviceNumber)}" ${e.key == 0 ? 'data-login="true"' : ''}></td>
                                                            <c:if test="${e.key == 0}">
                                                                <td class="-consultant-status-count" data-value="${e.key}" data-service="${g.htmlQuote(datum.serviceNumber)}" data-login="false"></td>
                                                            </c:if>
                                                        </c:if>
                                                    </c:forEach>
                                                    <td class="-login-user-count" data-service="${g.htmlQuote(datum.serviceNumber)}" data-login="true"></td>
                                                    <td class="-logout-user-count" data-service="${g.htmlQuote(datum.serviceNumber)}" data-login="false"></td>
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
                                                    <th>연결요청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td rowspan="5" class="inverted -custom-wait-count"><span class="very big-size-font">${datum.customerWaiting}</span></td>
                                                    <td rowspan="5" class="inverted data-response-rate"><span class="very big-size-font"><fmt:formatNumber value="${datum.responseRate}" pattern="#.#"/></span>%</td>
                                                    <td class="data-inbound-call">${datum.inboundCall}</td>
                                                    <td class="data-connection-request">${datum.connectionRequest}</td>
                                                </tr>
                                                <tr>
                                                    <th colspan="2">응답호</th>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" class="data-success-call">${datum.successCall}</td>
                                                </tr>
                                                <tr>
                                                    <th colspan="2">포기호</th>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" class="data-cancel-call">${datum.cancelCall}</td>
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
                                                    <th>로그인</th>
                                                    <th>로그아웃</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <c:forEach var="e" items="${statusCodes}">
                                                        <td class="-consultant-status-count" data-value="${e.key}" data-service="${g.htmlQuote(datum.serviceNumber)}" ${e.key == 0 ? 'data-login="true"' : ''}></td>
                                                        <c:if test="${e.key == 0}">
                                                            <td class="-consultant-status-count" data-value="${e.key}" data-service="${g.htmlQuote(datum.serviceNumber)}" data-login="false"></td>
                                                        </c:if>
                                                    </c:forEach>
                                                    <td class="-login-user-count" data-service="${g.htmlQuote(datum.serviceNumber)}" data-login="true"></td>
                                                    <td class="-logout-user-count" data-service="${g.htmlQuote(datum.serviceNumber)}" data-login="false"></td>
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
                                            <th style="width: 30%">고객대기</th>
                                            <td class="-custom-wait-count" style="width: 20%">${datum.customerWaiting}</td>
                                            <th style="width: 30%">응대율</th>
                                            <td class="data-response-rate">${datum.responseRate}%</td>
                                        </tr>
                                        <tr>
                                            <th>상담대기</th>
                                            <td class="-consultant-status-count" data-value="0" data-login="true" data-service="${g.htmlQuote(datum.serviceNumber)}"></td>
                                            <th rowspan="2">연결요청</th>
                                            <td rowspan="2" class="data-connection-request">${datum.connectionRequest}</td>
                                        </tr>
                                        <tr>
                                            <th>비로그인대기</th>
                                            <td class="-consultant-status-count" data-value="0" data-login="false" data-service="${g.htmlQuote(datum.serviceNumber)}"></td>
                                        </tr>
                                        <tr>
                                            <th>상담중</th>
                                            <td class="-consultant-status-count" data-value="1" data-service="${g.htmlQuote(datum.serviceNumber)}"></td>
                                            <th rowspan="2">응대호</th>
                                            <td rowspan="2" class="data-success-call">${datum.successCall}</td>
                                        </tr>
                                        <tr>
                                            <th>후처리</th>
                                            <td class="-consultant-status-count" data-value="2" data-service="${g.htmlQuote(datum.serviceNumber)}"></td>
                                        </tr>
                                        <tr>
                                            <th>휴식</th>
                                            <td class="-consultant-status-count" data-value="3" data-service="${g.htmlQuote(datum.serviceNumber)}"></td>
                                            <th rowspan="2">포기호</th>
                                            <td rowspan="2" class="data-cancel-call">${datum.cancelCall}</td>
                                        </tr>
                                        <tr>
                                            <th>기타</th>
                                            <td class="-consultant-status-count" data-value="4,5,6,7,8,9" data-service="${g.htmlQuote(datum.serviceNumber)}"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <c:if test="${config.showSlidingText}">
            <div class="marquee notice" style="padding: 2px 20px;">
                <h2>${g.htmlQuote(config.slidingText)}</h2>
            </div>
        </c:if>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            <c:if test="${config.showSlidingText}">
            //$('.notice .text').transition('fly left');
            </c:if>

            const services = {
                <c:forEach var="e" items="${data.services}">
                '${g.escapeQuote(e.serviceNumber)}': '${g.escapeQuote(e.serviceName)}',
                </c:forEach>
            };

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

                setTimeout(function () {
                    const number = $('.flex-active-slide').attr('data-service');
                    const name = services[number];
                    $('#service-number').text(number);
                    $('#service-name').text(name);
                }, 100);

            }, 10000);

            setInterval(function () {
                restSelf.get("/api/screen-data/by-service").done(function (data) {
                    data.data.services.forEach(function (service) {
                        const serviceStat = $('[data-service-number="'+ service.serviceNumber +'"]').not('.clone');
                            serviceStat.find('.data-response-rate').text(service.responseRate.toFixed(1) + "%");
                            serviceStat.find('.data-success-call').text(service.successCall);
                            serviceStat.find('.data-cancel-call').text(service.cancelCall);
                            serviceStat.find('.data-connection-request').text(service.connectionRequest);
                            serviceStat.find('.data-inbound-call').text(service.inboundCall);
                    });
                });
            }, 1000 * 30);

            updatePersonStatus();
            updateQueues();
        </script>
    </tags:scripts>
</tags:layout-screen>
