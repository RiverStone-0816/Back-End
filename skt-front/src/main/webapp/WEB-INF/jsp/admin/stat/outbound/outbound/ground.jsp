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
        <tags:page-menu-tab url="/admin/stat/outbound/outbound/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">발신통계</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
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
                                    </div>
                                </td>
                                <th>기간설정</th>
                                <td colspan="3">
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>추가조건선택</th>
                                <td colspan="7">
                                    <div class="ui checkbox">
                                        <form:checkbox path="inner" value="Y"/>
                                        <label>내선통화</label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button class="ui button sharp brand large">검색</button>
                                <button class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <div class="panel-section">
                        <div class="panel-section-title">
                            <div class="title-txt">
                                아웃바운드통계 <span class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</span>
                            </div>
                            <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        </div>
                        <table class="ui celled table compact unstackable structured border-top">
                            <thead>
                            <tr>
                                <th rowspan="2">날짜/시간</th>
                                <th colspan="3">O/B</th>
                                <th colspan="3">성과지표</th>
                            </tr>
                            <tr>
                                <th>총 시도콜</th>
                                <th>O/B건수(성공호)</th>
                                <th>비수신</th>

                                <th>통화성공률</th>
                                <th>O/B 총 통화시간</th>
                                <th>O/B 평균통화시간</th>
                            </tr>
                            </thead>

                            <c:choose>
                                <c:when test="${list.size() > 0}">
                                    <tbody>
                                    <c:forEach var="e" items="${list}">
                                        <tr>
                                            <td>${g.htmlQuote(e.timeInformation)}</td>
                                            <td>${e.statOutboundResponse.total}</td>
                                            <td>${e.statOutboundResponse.success}</td>
                                            <td>${e.statOutboundResponse.cancel}</td>
                                            <td>${e.statOutboundResponse.successAvg}%</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.statOutboundResponse.billSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.statOutboundResponse.billSecAvg)}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>

                                    <tfoot>
                                    <tr>
                                        <td>합계</td>
                                        <td>${total.total}</td>
                                        <td>${total.success}</td>
                                        <td>${total.cancel}</td>
                                        <td>${String.format("%.1f", total.successAvg)}%</td>
                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.billSecSum)}</td>
                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.billSecAvg)}</td>
                                    </tr>
                                    </tfoot>
                                </c:when>
                                <c:otherwise>
                                    <tbody>
                                    <tr>
                                        <td colspan="8" class="null-data">조회된 데이터가 없습니다.</td>
                                    </tr>
                                    </tbody>
                                </c:otherwise>
                            </c:choose>
                        </table>
                    </div>
                    <div class="panel-section">
                        <c:if test="${list.size() > 0}">
                            <div class="panel">
                                <div class="panel-heading">
                                    <text class="content">아웃바운드 통계 그래프</text>
                                </div>
                                <div class="panel-body pd-1em">
                                    <div class="ui grid">
                                        <div class="four wide column">
                                            <div class="label-container"><label class="control-label">평균통계</label><span class="ui label small">일별</span></div>
                                            <div class="pie-chart-container">
                                                <div id="outer-pie-chart" class="full-width" style="padding: 0 50px;"></div>

                                                <div class="inner-label">
                                                    <span class="ui label">TOTAL</span> ${total.success + total.cancel}
                                                </div>
                                            </div>
                                            <div class="chart-label-container">
                                                <div class="ui segment secondary">
                                                    <text class="label-list">응대호 <span class="color-bar1" style="color: #F37402 !important;">${total.success}</span></text>
                                                    <text class="label-list">포기호 <span class="color-bar2" style="color: #00802F !important;">${total.cancel}</span></text>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="twelve wide column">
                                            <div class="label-container">
                                                <label class="control-label">O/B비교통계</label>
                                            </div>
                                            <div class="-chart basic-chart-container" id="chart"></div>
                                            <div class="chart-label-container">
                                                <div class="ui segment secondary">
                                                    <text class="label-list"><span class="point color-red" style="background-color: #F37402 !important;"></span>응대호</text>
                                                    <text class="label-list"><span class="point color-blue" style="background-color: #00802F !important;"></span>포기호</text>
                                                </div>
                                            </div>
                                        </div>
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
                    <c:forEach var="e" items="${list}">{
                    time: '${g.escapeQuote(e.timeInformation)}',
                    success: ${e.statOutboundResponse.success},
                    cancel: ${e.statOutboundResponse.cancel},
                }, </c:forEach>
            ];

            chartjs.drawBarChart($('#chart')[0], data, 'time', ['success', 'cancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue'],
                colors: ['#F37402', '#00802F', ],
                labels: ['응대호', '포기호']
            });

            chartjs.drawDonutChart(
                '#outer-pie-chart',
                <c:choose>
                <c:when test="${total.success + total.cancel == 0}">
                [0, 0],
                </c:when>
                <c:otherwise>
                [${total.cancel}, ${total.success},],
                </c:otherwise>
                </c:choose>
                {
                    startAngle: 0,
                    endAngle: 360,
                    innerRadius: 100,
                    outerRadius: 120,
                    innerLabel: ' ',
                    colorClasses: ['bcolor-bar2', 'bcolor-bar1'],
                    colors: ['#00802F', '#F37402'],
                    labels: ['포기호', '응대호']
                }
            );
            </c:if>

            $('.-button-time-unit').click(function () {
                $('.-button-time-unit').removeClass('active');
                $(this).addClass('active');
                $('#search-form [name=timeUnit]').val($(this).attr('data-value'));
            });

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/outbound/outbound/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
