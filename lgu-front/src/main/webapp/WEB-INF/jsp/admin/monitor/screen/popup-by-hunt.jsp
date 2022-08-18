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
<%--@elvariable id="data" type="kr.co.eicn.ippbx.front.model.ScreenDataForByHunt"--%>
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
            color: #000000;
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
                                    <div class="table-container mb20">
                                        <table class="blue">
                                            <thead>
                                            <tr style="line-height: 80px">
                                                <th>큐(그룹)명</th>
                                                <th>고객대기</th>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <th>${e.value}</th>
                                                    <c:if test="${e.key == 0}">
                                                        <th>비로그인 대기</th>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="e" items="${data.hunts}">
                                                <tr style="line-height: 50px">
                                                    <td>${g.htmlQuote(e.queueKoreanName)}</td>
                                                    <td class="-custom-wait-count">${e.customerWaiting}</td>
                                                    <c:forEach var="status" items="${statusCodes}">
                                                        <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queueName)}" ${status.key == 0 ? 'data-login="true"' : ''}></td>
                                                        <c:if test="${status.key == 0}">
                                                            <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queueName)}" data-login="false"></td>
                                                        </c:if>
                                                    </c:forEach>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="table-container">
                                        <table class="green">
                                            <thead>
                                            <tr style="line-height: 80px">
                                                <th>서비스명</th>
                                                <th>인입호</th>
                                                <th>단순조회</th>
                                                <th>연결요청</th>
                                                <th>응답호</th>
                                                <th>포기호</th>
                                                <th>응답률</th>
                                            </tr>
                                            </thead>
                                            <tbody class="service-data">
                                            <c:forEach var="e" items="${data.services}">
                                                <tr style="line-height: 50px">
                                                    <td>${g.htmlQuote(e.serviceName)}</td>
                                                    <td>${e.totalCall}</td>
                                                    <td>${e.onlyReadCall}</td>
                                                    <td>${e.connectionRequest}</td>
                                                    <td>${e.successCall}</td>
                                                    <td>${e.cancelCall}</td>
                                                    <td>${e.responseRate}%</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${config.lookAndFeel == 2}">
                                <div class="flex-container">
                                    <div class="table-container mb20">
                                        <table>
                                            <thead>
                                            <tr style="line-height: 80px">
                                                <th>큐(그룹)명</th>
                                                <th>고객대기</th>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <th>${e.value}</th>
                                                    <c:if test="${e.key == 0}">
                                                        <th>비로그인 대기</th>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="e" items="${data.hunts}">
                                                <tr style="line-height: 50px">
                                                    <td>${g.htmlQuote(e.queueKoreanName)}</td>
                                                    <td class="-custom-wait-count">${e.customerWaiting}</td>
                                                    <c:forEach var="status" items="${statusCodes}">
                                                        <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queueName)}" ${status.key == 0 ? 'data-login="true"' : ''}></td>
                                                        <c:if test="${status.key == 0}">
                                                            <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queueName)}" data-login="false"></td>
                                                        </c:if>
                                                    </c:forEach>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="table-container">
                                        <table>
                                            <thead>
                                            <tr style="line-height: 80px">
                                                <th>서비스명</th>
                                                <th>인입호</th>
                                                <th>단순조회</th>
                                                <th>연결요청</th>
                                                <th>응답호</th>
                                                <th>포기호</th>
                                                <th>응답률</th>
                                            </tr>
                                            </thead>
                                            <tbody class="service-data">
                                            <c:forEach var="e" items="${data.services}">
                                                <tr style="line-height: 50px">
                                                    <td>${g.htmlQuote(e.serviceName)}</td>
                                                    <td>${e.totalCall}</td>
                                                    <td>${e.onlyReadCall}</td>
                                                    <td>${e.connectionRequest}</td>
                                                    <td>${e.successCall}</td>
                                                    <td>${e.cancelCall}</td>
                                                    <td>${e.responseRate}%</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="flex-container">
                                    <div class="table-container mb20">
                                        <table class="blue">
                                            <thead>
                                            <tr style="line-height: 80px">
                                                <th>큐(그룹)명</th>
                                                <th>고객대기</th>
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <th>${e.value}</th>
                                                    <c:if test="${e.key == 0}">
                                                        <th>비로그인 대기</th>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="e" items="${data.hunts}">
                                                <tr>
                                                    <td>${g.htmlQuote(e.queueKoreanName)}</td>
                                                    <td class="-custom-wait-count">${e.customerWaiting}</td>
                                                    <c:forEach var="status" items="${statusCodes}">
                                                        <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queueName)}" ${status.key == 0 ? 'data-login="true"' : ''}></td>
                                                        <c:if test="${status.key == 0}">
                                                            <td class="-consultant-status-count" data-value="${status.key}" data-hunt="${g.htmlQuote(e.queueName)}" data-login="false"></td>
                                                        </c:if>
                                                    </c:forEach>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="table-container">
                                        <table class="green">
                                            <thead>
                                            <tr style="line-height: 80px">
                                                <th>구분</th>
                                                <th>인입호</th>
                                                <th>단순조회</th>
                                                <th>연결요청</th>
                                                <th>응답호</th>
                                                <th>포기호</th>
                                                <th>응답률</th>
                                            </tr>
                                            </thead>
                                            <tbody class="service-data">
                                            <c:forEach var="e" items="${data.services}">
                                                <tr style="line-height: 50px">
                                                    <td>${g.htmlQuote(e.serviceName)}</td>
                                                    <td>${e.totalCall}</td>
                                                    <td>${e.onlyReadCall}</td>
                                                    <td>${e.connectionRequest}</td>
                                                    <td>${e.successCall}</td>
                                                    <td>${e.cancelCall}</td>
                                                    <td>${e.responseRate}%</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </li>
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
                restSelf.get("/api/screen-data/by-hunt").done(function (data) {
                    $(".service-data").empty();
                    data.data.services.forEach(function (service) {
                        console.log(service.responseRate);
                        $(".service-data").append("<tr style=\"line-height: 50px\">\n" +
                            "<td>" + service.serviceName + "</td>\n" +
                            "<td>" + service.totalCall + "</td>\n" +
                            "<td>" + service.onlyReadCall + "</td>\n" +
                            "<td>" + service.connectionRequest + "</td>\n" +
                            "<td>" + service.successCall + "</td>\n" +
                            "<td>" + service.cancelCall + "</td>\n" +
                            "<td>" + service.responseRate.toFixed(1) + "%</td>\n" +
                            "</tr>");
                    });
                });
            }, 1000 * 30);

            updatePersonStatus();
            updateQueues();
        </script>
    </tags:scripts>
</tags:layout-screen>
