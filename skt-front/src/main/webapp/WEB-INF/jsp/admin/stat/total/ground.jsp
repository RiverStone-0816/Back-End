<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stat/total/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/stat/total/"))}</div>
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
                                <td colspan="7">
                                    <div class="ui basic buttons">
                                        <form:hidden path="timeUnit"/>
                                        <c:forEach var="e" items="${searchCycles}">
                                            <button type="button" class="ui button -button-time-unit ${search.timeUnit.name() == e.key ? 'active' : ''}"
                                                    data-value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</button>
                                        </c:forEach>
                                    </div>
                                </td>
                                <th>기간설정</th>
                                <td colspan="7">
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
                                <th>서비스선택</th>
                                <td colspan="7">
                                    <div class="ui form">
                                        <form:select path="serviceNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="서비스선택"/>
                                            <form:options items="${services}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>추가조건선택</th>
                                <td colspan="7">
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
                    </div>
                </div>
            </form:form>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <div class="panel-section">
                        <div class="panel-section-title">
                            <div class="title-txt">
                                총 통화통계 <span class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</span>
                            </div>
                            <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        </div>
                        <div class="table-scroll-wrap">
                            <table class="ui celled table compact unstackable structured border-top">
                                <thead>
                                <tr>
                                    <th rowspan="2">날짜/시간</th>
                                    <th colspan="4">O/B 건수 통계</th>
                                    <th colspan="8">I/B 건수 통계</th>
                                    <th rowspan="2">총 통화<br>성공 건수</th>
                                    <th colspan="3">O/B 시간통계</th>
                                    <th colspan="3">I/B 시간통계</th>
                                    <th rowspan="2">총시간</th>
                                </tr>
                                <tr>
                                    <th>총시도콜</th>
                                    <th>O/B건수</th>
                                    <th>비수신</th>
                                    <th>통화성공률</th>
                                    <th>I/B 전체콜</th>
                                    <th>무효콜</th>
                                    <th>연결요청</th>
                                    <th>응대호</th>
                                    <th>포기호</th>
                                    <th>콜백</th>
                                    <th>호응답률</th>
                                    <th>무효콜비율</th>
                                    <th>O/B 총통화시간</th>
                                    <th>O/B 평균통화시간</th>
                                    <th>평균 대기시간</th>
                                    <th>I/B 총통화시간</th>
                                    <th>평균 통화 시간</th>
                                    <th>평균 대기시간</th>
                                </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <tbody>
                                        <c:forEach var="e" items="${list}">
                                            <tr>
                                                <td>${g.htmlQuote(e.timeInformation)}</td>

                                                <td>${e.outboundStat.total}</td>
                                                <td>${e.outboundStat.success}</td>
                                                <td>${e.outboundStat.cancel}</td>
                                                <td>${e.outboundStat.successAvg}%</td>

                                                <td>${e.inboundStat.total}</td>
                                                <td>${e.inboundStat.onlyRead}</td>
                                                <td>${e.inboundStat.connReq}</td>
                                                <td>${e.inboundStat.success}</td>
                                                <td>${e.inboundStat.cancel}</td>
                                                <td>${e.inboundStat.callbackSuccess}</td>
                                                <td>${e.inboundStat.responseRate}%</td>
                                                <td>${e.inboundStat.ivrAvg}%</td>

                                                <td>${e.totalCount}</td>

                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.outboundStat.billSecSum)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.outboundStat.billSecAvg)}</td>
                                                <td></td>

                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inboundStat.billSecSum)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inboundStat.billSecAvg)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inboundStat.waitAvg)}</td>

                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.totalBillSec)}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                        <tfoot>
                                        <tr>
                                            <td>합계</td>

                                            <td>${total.outboundStat.total}</td>
                                            <td>${total.outboundStat.success}</td>
                                            <td>${total.outboundStat.cancel}</td>
                                            <td>${String.format("%.1f", total.outboundStat.successAvg)}%</td>

                                            <td>${total.inboundStat.total}</td>
                                            <td>${total.inboundStat.onlyRead}</td>
                                            <td>${total.inboundStat.connReq}</td>
                                            <td>${total.inboundStat.success}</td>
                                            <td>${total.inboundStat.cancel}</td>
                                            <td>${total.inboundStat.callbackSuccess}</td>
                                            <td>${String.format("%.1f", total.inboundStat.responseRate)}%</td>
                                            <td>${String.format("%.1f", total.inboundStat.ivrAvg)}%</td>

                                            <td>${total.totalCount}</td>

                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.outboundStat.billSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.outboundStat.billSecAvg)}</td>
                                            <td></td>

                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inboundStat.billSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inboundStat.billSecAvg)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inboundStat.waitAvg)}</td>

                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.totalBillSec)}</td>
                                        </tr>
                                        </tfoot>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody>
                                        <tr>
                                            <td colspan="21" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                        </tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>
                    </div>
                    <c:if test="${list.size() > 0}">
                        <div class="panel-section">
                            <div class="panel">
                                <div class="panel-heading">아웃바운드 통계 그래프</div>
                                <div class="panel-body pd-1em">
                                    <div class="ui middle aligned grid">
                                        <div class="four wide column">
                                            <div class="ui segments pie-chart">
                                                <div class="ui segment pie-chart-bg">
                                                    <div class="label-container">O/B 통계</div>
                                                    <div class="pie-chart-container">
                                                        <div id="outbound-outer-pie-chart" class="full-width full-height" style="padding: 0 50px"></div>
                                                    </div>
                                                </div>
                                                <div class="ui secondary segment">
                                                    <div class="chart-label-container">
                                                        <div>
                                                            <text class="label-list"><span class="symbol-square symbol-blue"></span>O/B 건수</text>
                                                            <text class="label-list"><span class="symbol-square symbol-orange"></span>비수신</text>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="twelve wide column">
                                            <div class="label-container">
                                                <label class="control-label">O/B 통계</label>
                                            </div>
                                            <div class="-chart basic-chart-container" id="outbound-chart"></div>
                                            <div class="chart-label-container">
                                                <div>
                                                    <text class="label-list"><span class="symbol-square symbol-blue"></span>O/B 건수</text>
                                                    <text class="label-list"><span class="symbol-square symbol-orange"></span>비수신</text>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-section">
                            <div class="panel">
                                <div class="panel-heading">인바운드 통계 그래프</div>
                                <div class="panel-body pd-1em">
                                    <div class="ui middle aligned grid">
                                        <div class="four wide column">
                                            <div class="ui segments pie-chart">
                                                <div class="ui segment pie-chart-bg">
                                                    <div class="label-container">I/B 통계</div>
                                                    <div class="pie-chart-container">
                                                        <div id="inbound-outer-pie-chart" class="full-width full-height" style="padding: 0 50px"></div>
                                                    </div>
                                                </div>
                                                <div class="ui secondary segment">
                                                    <div class="chart-label-container">
                                                        <div>
                                                            <text class="label-list"><span class="symbol-square symbol-blue"></span>응대호</text>
                                                            <text class="label-list"><span class="symbol-square symbol-orange"></span>포기호</text>
                                                            <text class="label-list"><span class="symbol-square symbol-grey"></span>콜백</text>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="twelve wide column">
                                            <div class="label-container">
                                                <label class="control-label">I/B 통계</label>
                                            </div>
                                            <div class="-chart basic-chart-container" id="inbound-chart"></div>
                                            <div class="chart-label-container">
                                                <div>
                                                    <text class="label-list"><span class="symbol-square symbol-blue"></span>응대호</text>
                                                    <text class="label-list"><span class="symbol-square symbol-orange"></span>포기호</text>
                                                    <text class="label-list"><span class="symbol-square symbol-grey"></span>콜백</text>
                                                </div>
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

    <tags:scripts>
        <script>
            <c:if test="${list.size() > 0}">
            const data = [
                    <c:forEach var="e" items="${list}">{
                    time: '${g.escapeQuote(e.timeInformation)}',
                    inboundTotal: ${e.inboundStat.total},
                    outboundTotal: ${e.outboundStat.total},
                    inboundSuccess: ${e.inboundStat.success},
                    outboundSuccess: ${e.outboundStat.success},
                    inboundCancel: ${e.inboundStat.cancel},
                    outboundCancel: ${e.outboundStat.cancel},
                }, </c:forEach>
            ];
            chartjs.drawLineChart($('#total-call-chart')[0], data, 'time', ['inboundTotal', 'outboundTotal'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue'],
                colors: ['#F37402', '#4472C4'],
                labels: ['I/B', 'O/B']
            });

            chartjs.drawBarChart($('#outbound-chart')[0], data, 'time', ['outboundSuccess', 'outboundCancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue'],
                colors: ['#F37402', '#4472C4'],
                labels: ['응대호', '포기호'],
                stacked: true
            });

            chartjs.drawBarChart($('#inbound-chart')[0], data, 'time', ['inboundSuccess', 'inboundCancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue'],
                colors: ['#F37402', '#4472C4'],
                labels: ['성공호', '비수신'],
                stacked: true
            });

            chartjs.drawDonutChart(
                '#outbound-outer-pie-chart',
                [${total.outboundStat.success}, ${total.outboundStat.cancel}],
                {
                    startAngle: 0,
                    endAngle: 360,
                    innerRadius: 100,
                    outerRadius: 120,
                    innerLabel: ' ',
                    colorClasses: ['bcolor-bar1', 'bcolor-bar2'],
                    colors: ['#F37402', '#4472C4'],
                    labels: ['응대호', '포기호']
                }
            );

            chartjs.drawDonutChart(
                '#inbound-outer-pie-chart',
                [${total.inboundStat.success}, ${total.inboundStat.cancel}],
                {
                    startAngle: 0,
                    endAngle: 360,
                    innerRadius: 100,
                    outerRadius: 120,
                    innerLabel: ' ',
                    colorClasses: ['bcolor-bar1', 'bcolor-bar2'],
                    colors: ['#F37402', '#4472C4'],
                    labels: ['성공호', '비수신']
                }
            );
            </c:if>

            $('.-button-time-unit').click(function () {
                $('.-button-time-unit').removeClass('active');
                $(this).addClass('active');
                $('#search-form [name=timeUnit]').val($(this).attr('data-value'));
            });

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/total/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
