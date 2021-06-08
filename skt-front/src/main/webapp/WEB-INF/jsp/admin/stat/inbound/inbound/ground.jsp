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
        <tags:page-menu-tab url="/admin/stat/inbound/inbound/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">대표번호수신통계</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                    </div>
                </div>
                <div class="panel-body overflow-unset">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>주기설정</th>
                                <td colspan="3">
                                    <div class="ui basic buttons">
                                            <form:hidden path="timeUnit"/>
                                        <c:forEach var="e" items="${searchCycles}">
                                        <button type="button" class="ui button -button-time-unit ${search.timeUnit.name() == e.key ? 'active' : ''}"
                                                data-value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</button>
                                        </c:forEach>
                                </td>
                                <th>기간설정</th>
                                <td colspan="3">
                                    <div class="ui action input calendar-area">
                                        <input type="text">
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <input type="text">
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>서비스선택</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="serviceNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="서비스선택"/>
                                            <form:options items="${services}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>큐선택</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="queueNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="큐선택"/>
                                            <form:options items="${queues}"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>추가조건선택</th>
                                <td colspan="11">
                                    <div class="ui checkbox">
                                        <form:checkbox path="person" value="Y"/>
                                        <label>직통전화</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="inner" value="Y"/>
                                        <label>내선통화</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="busy" value="Y"/>
                                        <label>콜백</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="workHour" value="Y"/>
                                        <label>업무시간</label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                        <%--<div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">기간설정</label></div>
                                <div class="six wide column">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="endDate" style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>--%>
                    </div>
                </div>
            </form:form>


            <div class="panel panel-statstics">
                <div class="panel-heading">
                    <div class="pull-left">
                        <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="panel-section">
                        <div class="panel">
                            <div class="panel-heading">
                                <text class="content">
                                    인바운드 통계
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </text>
                            </div>
                            <div class="panel-body pd-1em">
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
                                    <c:choose>
                                    <c:when test="${list.size() > 0}">
                                    <tbody>
                                    <c:forEach var="element" items="${list}">
                                        <c:set var="e" value="${element.inboundStat}"/>
                                        <tr>
                                            <td>${g.htmlQuote(element.timeInformation)}</td>

                                            <td><span class="data-detail-trigger">${e.total}</span></td>
                                            <td><span class="data-detail-trigger">${e.onlyRead}</span></td>
                                            <td><span class="data-detail-trigger">${e.connReq}</span></td>
                                            <td><span class="data-detail-trigger">${e.success}</span></td>
                                            <td><span class="data-detail-trigger">${e.cancel}</span></td>
                                            <td><span class="data-detail-trigger">${e.callbackSuccess}</span></td>

                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.billSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.billSecAvg)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.waitAvg)}</td>
                                            <td>${e.responseRate}%</td>
                                            <td>${e.svcLevelAvg}%</td>
                                            <td>${e.ivrAvg}%</td>

                                            <td>${e.waitSucc_0_10}</td>
                                            <td>${e.waitSucc_10_20}</td>
                                            <td>${e.waitSucc_20_30}</td>
                                            <td>${e.waitSucc_30_40}</td>
                                            <td>${e.waitSucc_40}</td>

                                            <td>${e.waitCancel_0_10}</td>
                                            <td>${e.waitCancel_10_20}</td>
                                            <td>${e.waitCancel_20_30}</td>
                                            <td>${e.waitCancel_30_40}</td>
                                            <td>${e.waitCancel_40}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>

                                    <tfoot>
                                    <tr>
                                        <td>합계</td>

                                        <td>${total.total}</td>
                                        <td>${total.onlyRead}</td>
                                        <td>${total.connReq}</td>
                                        <td>${total.success}</td>
                                        <td>${total.cancel}</td>
                                        <td>${total.callbackSuccess}</td>

                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.billSecSum)}</td>
                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.billSecAvg)}</td>
                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.waitAvg)}</td>
                                        <td>${String.format("%.1f", total.responseRate)}%</td>
                                        <td>${String.format("%.1f", total.svcLevelAvg)}%</td>
                                        <td>${String.format("%.1f", total.ivrAvg)}%</td>

                                        <td>${total.waitSucc_0_10}</td>
                                        <td>${total.waitSucc_10_20}</td>
                                        <td>${total.waitSucc_20_30}</td>
                                        <td>${total.waitSucc_30_40}</td>
                                        <td>${total.waitSucc_40}</td>

                                        <td>${total.waitCancel_0_10}</td>
                                        <td>${total.waitCancel_10_20}</td>
                                        <td>${total.waitCancel_20_30}</td>
                                        <td>${total.waitCancel_30_40}</td>
                                        <td>${total.waitCancel_40}</td>
                                    </tr>
                                    </tfoot>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody>
                                        <tr>
                                            <td colspan="22" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                        </tbody>
                                    </c:otherwise>
                                    </c:choose>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="panel-section">
                        <div class="panel">
                            <div class="panel-heading">통계그래프</div>
                            <div class="panel-body pd-1em">
                                <div class="ui grid">
                                    <c:if test="${list.size() > 0}">
                                        <div class="four wide column" >
                                            <div class="label-container"><label class="control-label">평균통계</label></div>
                                            <div class="pie-chart-container">
                                                <svg id="inbound-outer-pie-chart" class="full-width full-height"></svg>

                                                <div class="inner-label">
                                                    <span class="ui label">TOTAL</span> ${total.success + total.cancel}
                                                </div>
                                            </div>
                                            <div class="chart-label-container">
                                                <div class="ui segment secondary">
                                                    <text class="label-list">응대호 <span class="color-bar1">${total.success}</span></text>
                                                    <text class="label-list">포기호 <span class="color-bar2">${total.cancel}</span></text>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="twelve wide column" >
                                            <div class="label-container">
                                                <label class="control-label">I/B비교통계</label>
                                            </div>
                                            <div class="-chart basic-chart-container" id="inbound-chart"></div>
                                            <div class="chart-label-container">
                                                <div class="ui segment secondary">
                                                    <text class="label-list"><span class="point color-red"></span>응대호</text>
                                                    <text class="label-list"><span class="point color-blue"></span>포기호</text>
                                                </div>
                                            </div>
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

    <tags:scripts>
        <script>

            <c:if test="${list.size() > 0}">
            const data = [
                    <c:forEach var="element" items="${list}">{
                    time: '${g.escapeQuote(element.timeInformation)}',
                    inboundSuccess: ${element.inboundStat.success},
                    inboundCancel: ${element.inboundStat.cancel},
                }, </c:forEach>
            ];

            drawBarChart($('#inbound-chart')[0], data, 'time', ['inboundSuccess', 'inboundCancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue']
            });

            drawPieChart(
                '#inbound-outer-pie-chart',
                ${total.cancel.doubleValue() / (total.success + total.cancel)},
                {
                    startAngle: 0,
                    endAngle: 360,
                    innerRadius: 100,
                    outerRadius: 120,
                    innerLabel: ' ',
                    colorClasses: ['bcolor-bar2', 'bcolor-bar1']
                }
            );
            </c:if>

            $('.-button-time-unit').click(function () {
                $('.-button-time-unit').removeClass('active');
                $(this).addClass('active');
                $('#search-form [name=timeUnit]').val($(this).attr('data-value'));
            });

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/inbound/inbound/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
