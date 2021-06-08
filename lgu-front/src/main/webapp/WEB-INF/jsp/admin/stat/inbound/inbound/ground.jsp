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
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body overflow-unset">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">주기설정</label></div>
                                <div class="five wide column">
                                    <div class="ui basic buttons">
                                        <form:hidden path="timeUnit"/>
                                        <c:forEach var="e" items="${searchCycles}">
                                            <button type="button" class="ui button -button-time-unit ${search.timeUnit.name() == e.key ? 'active' : ''}"
                                                    data-value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</button>
                                        </c:forEach>
                                    </div>
                                </div>
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
                            <div class="row">
                                <div class="two wide column"><label class="control-label">서비스선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form">
                                        <form:select path="serviceNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="서비스선택"/>
                                            <form:options items="${services}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">큐선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form">
                                        <form:select path="queueNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="큐선택"/>
                                            <form:options items="${queues}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">추가조건선택</label></div>
                                <div class="five wide column">
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
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-right">
                        <button class="ui basic green button" type="button" onclick="downloadExcel()">Excel 다운로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui grid">
                        <div class="sixteen wide column">
                            <h3 class="ui header center aligned">
                                <text class="content">
                                    인바운드 통계
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </text>
                            </h3>
                        </div>
                        <div class="sixteen wide column">
                            <table class="ui celled table compact unstackable definition structured">
                                <thead>
                                <tr>
                                    <th rowspan="2">날짜/시간</th>
                                    <th colspan="6" class="color red">I/B 콜 현황</th>
                                    <th colspan="6" class="color red">성과지표</th>
                                    <th colspan="5" class="color red">응대호 대기시간 분석</th>
                                    <th colspan="5" class="color red">포기호 대기시간 분석</th>
                                </tr>
                                <tr>
                                    <th class="color red">I/B<br>전체콜</th>
                                    <th class="color red">단순조회</th>
                                    <th class="color red">연결요청</th>
                                    <th class="color red">응대호</th>
                                    <th class="color red">포기호</th>
                                    <th class="color red">콜백</th>

                                    <th class="color red">I/B<br>총통화시간</th>
                                    <th class="color red">평균통화시간</th>
                                    <th class="color red">평균대기시간</th>
                                    <th class="color red">호응답률</th>
                                    <th class="color red">서비스레벨<br>호응답률</th>
                                    <th class="color red">단순조회율</th>

                                    <th class="color red">~10(초)</th>
                                    <th class="color red">~20(초)</th>
                                    <th class="color red">~30(초)</th>
                                    <th class="color red">~40(초)</th>
                                    <th class="color red">40~(초)</th>
                                    <th class="color red">~10(초)</th>
                                    <th class="color red">~20(초)</th>
                                    <th class="color red">~30(초)</th>
                                    <th class="color red">~40(초)</th>
                                    <th class="color red">40~(초)</th>
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
