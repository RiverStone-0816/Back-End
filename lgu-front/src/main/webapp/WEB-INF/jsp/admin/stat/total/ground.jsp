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
        <tags:page-menu-tab url="/admin/stat/total/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div>
                        검색
                    </div>
                    <div class="dp-flex align-items-center">
                        <div class="ui slider checkbox mr15">
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
                                    총통화통계
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </text>
                            </h3>
                        </div>
                        <div class="sixteen wide column">
                            <table class="ui celled table compact unstackable definition structured">
                                <thead>
                                <tr>
                                    <th rowspan="2">날짜/시간</th>
                                    <th colspan="2">총 통화</th>
                                    <th colspan="6" class="color blue">O/B</th>
                                    <th colspan="11" class="color red">I/B</th>
                                </tr>
                                <tr>
                                    <th>총 건수</th>
                                    <th>총 시간</th>
                                    <th class="color blue">총 시도콜</th>
                                    <th class="color blue">O/B건수<br>(성공호)</th>
                                    <th class="color blue">비수신</th>
                                    <th class="color blue">통화성공률</th>
                                    <th class="color blue">O/B<br>총 통화시간</th>
                                    <th class="color blue">O/B<br>평균통화시간</th>
                                    <th class="color red">I/B<br>전체콜</th>
                                    <th class="color red">단순조회</th>
                                    <th class="color red">연결요청</th>
                                    <th class="color red">응대호</th>
                                    <th class="color red">포기호</th>
                                    <th class="color red">콜백</th>
                                    <th class="color red">I/B<br>총 통화시간</th>
                                    <th class="color red">I/B<br>평균통화시간</th>
                                    <th class="color red">I/B<br>평균대기시간</th>
                                    <th class="color red">호응답률</th>
                                    <th class="color red">단순조회율</th>
                                </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <tbody>
                                        <c:forEach var="e" items="${list}">
                                            <tr>
                                                <td>${g.htmlQuote(e.timeInformation)}</td>
                                                <td>${e.totalCount}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.totalBillSec)}</td>
                                                <td><span class="data-detail-trigger">${e.outboundStat.total}</span></td>
                                                <td>${e.outboundStat.success}</td>
                                                <td><span class="data-detail-trigger">${e.outboundStat.cancel}</span></td>
                                                <td>${e.outboundStat.successAvg}%</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.outboundStat.billSecSum)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.outboundStat.billSecAvg)}</td>
                                                <td><span class="data-detail-trigger">${e.inboundStat.total}</span></td>
                                                <td><span class="data-detail-trigger">${e.inboundStat.onlyRead}</span></td>
                                                <td><span class="data-detail-trigger">${e.inboundStat.connReq}</span></td>
                                                <td><span class="data-detail-trigger">${e.inboundStat.success}</span></td>
                                                <td><span class="data-detail-trigger">${e.inboundStat.cancel}</span></td>
                                                <td>${e.inboundStat.callbackSuccess}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inboundStat.billSecSum)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inboundStat.billSecAvg)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inboundStat.waitAvg)}</td>
                                                <td>${e.inboundStat.responseRate}%</td>
                                                <td>${e.inboundStat.ivrAvg}%</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                        <tfoot>
                                        <tr>
                                            <td>합계</td>
                                            <td>${total.totalCount}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.totalBillSec)}</td>
                                            <td>${total.outboundStat.total}</td>
                                            <td>${total.outboundStat.success}</td>
                                            <td>${total.outboundStat.cancel}</td>
                                            <td>${String.format("%.1f", total.outboundStat.successAvg)}%</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.outboundStat.billSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.outboundStat.billSecAvg)}</td>
                                            <td>${total.inboundStat.total}</td>
                                            <td>${total.inboundStat.onlyRead}</td>
                                            <td>${total.inboundStat.connReq}</td>
                                            <td>${total.inboundStat.success}</td>
                                            <td>${total.inboundStat.cancel}</td>
                                            <td>${total.inboundStat.callbackSuccess}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inboundStat.billSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inboundStat.billSecAvg)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inboundStat.waitAvg)}</td>
                                            <td>${String.format("%.1f", total.inboundStat.responseRate)}%</td>
                                            <td>${String.format("%.1f", total.inboundStat.ivrAvg)}%</td>
                                        </tr>
                                        </tfoot>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody>
                                        <tr>
                                            <td colspan="20" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                        </tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>

                        <c:if test="${list.size() > 0}">
                            <div class="sixteen wide column">
                                <h3 class="ui header center aligned">
                                    <text class="content">총통화그래프</text>
                                </h3>
                            </div>
                            <div class="sixteen wide column">
                                <div class="-chart basic-chart-container" id="total-call-chart"></div>
                                <div class="chart-label-container">
                                    <div class="ui segment secondary">
                                        <text class="label-list"><i class="point color-red"></i>인바운드</text>
                                        <text class="label-list"><i class="point color-blue"></i>아웃바운드</text>
                                    </div>
                                </div>
                            </div>
                            <div class="sixteen wide column">
                                <h3 class="ui header center aligned">
                                    <text class="content">아웃바운드 통계 그래프</text>
                                </h3>
                            </div>
                            <div class="four wide column" >
                                <div class="label-container"><label class="control-label">평균통계</label></div>
                                <div class="pie-chart-container">
                                    <svg id="outbound-outer-pie-chart" class="full-width full-height"></svg>
                                    <div class="inner-label">
                                        <span class="ui label">TOTAL</span> ${total.outboundStat.success + total.outboundStat.cancel}
                                    </div>
                                </div>

                                    <div class="chart-label-container">
                                        <div class="ui segment secondary">
                                            <text class="label-list">성공호 <span class="color-bar1">${total.outboundStat.success}</span></text>
                                            <text class="label-list">비수신 <span class="color-bar2">${total.outboundStat.cancel}</span></text>
                                        </div>
                                    </div>

                            </div>
                            <div class="twelve wide column">
                                <div class="label-container">
                                    <label class="control-label">O/B비교통계</label>
                                </div>
                                <div class="-chart basic-chart-container" id="outbound-chart"></div>
                                <div class="chart-label-container">
                                    <div class="ui segment secondary">
                                        <text class="label-list"><span class="point color-red"></span>성공호</text>
                                        <text class="label-list"><span class="point color-blue"></span>비수신</text>
                                    </div>
                                </div>
                            </div>
                            <div class="sixteen wide column">
                                <h3 class="ui header center aligned">
                                    <text class="content">인바운드 통계 그래프</text>
                                </h3>
                            </div>
                            <div class="four wide column">
                                <div class="label-container"><label class="control-label">평균통계</label></div>
                                <div class="pie-chart-container">
                                    <svg id="inbound-outer-pie-chart" class="full-width full-height"></svg>

                                    <div class="inner-label">
                                        <span class="ui label">TOTAL</span> ${total.inboundStat.success + total.inboundStat.cancel}
                                    </div>
                                </div>
                                <div class="chart-label-container">
                                    <div class="ui segment secondary">
                                        <text class="label-list">성공호 <span class="color-bar1">${total.inboundStat.success}</span></text>
                                        <text class="label-list">비수신 <span class="color-bar2">${total.inboundStat.cancel}</span></text>
                                    </div>
                                </div>

                            </div>
                            <div class="twelve wide column">
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

    <div class="ui modal" id="modal-data-detain-view">
        <i class="close icon"></i>
        <div class="header">
            자세히
        </div>
        <div class="scrolling content rows">
            <table class="ui table celled compact unstackable">
                <thead>
                <tr>
                    <th>날짜</th>
                    <th>시간</th>
                    <th>번호</th>
                    <th>수신번호</th>
                    <th>대표번호</th>
                    <th>IVR번호</th>
                    <th>부서명</th>
                    <th>호상태</th>
                    <th>부가상태</th>
                </thead>
                <tbody>
                    <%--<tr>
                        <td colspan="9" class="null-data">조회된 데이터가 없습니다.</td>
                    </tr>--%>
                <tr>
                    <td>2020-03-24</td>
                    <td>11:14:49</td>
                    <td>01011111111</td>
                    <td>01022222222</td>
                    <td>01033333333</td>
                    <td>기타문의</td>
                    <td>영업부</td>
                    <td>비수신</td>
                    <td>연결전끊음</td>
                </tr>
                <tr>
                    <td>2020-03-24</td>
                    <td>11:14:49</td>
                    <td>01011111111</td>
                    <td>01022222222</td>
                    <td>01033333333</td>
                    <td>기타문의</td>
                    <td>영업부</td>
                    <td>비수신</td>
                    <td>연결전끊음</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <tags:scripts>
        <script>
            /* TODO
            $('.data-detail-trigger').click(function () {
                $('#modal-data-detain-view').modalShow();
            });*/

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
            drawLineChart($('#total-call-chart')[0], data, 'time', ['inboundTotal', 'outboundTotal'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue']
            });

            drawBarChart($('#outbound-chart')[0], data, 'time', ['outboundSuccess', 'outboundCancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue']
            });

            drawBarChart($('#inbound-chart')[0], data, 'time', ['inboundSuccess', 'inboundCancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue']
            });

            drawPieChart(
                '#outbound-outer-pie-chart',
                ${total.outboundStat.cancel.doubleValue() / (total.outboundStat.success + total.outboundStat.cancel)},
                {
                    startAngle: 0,
                    endAngle: 360,
                    innerRadius: 100,
                    outerRadius: 120,
                    innerLabel: ' ',
                    colorClasses: ['bcolor-bar2', 'bcolor-bar1']
                }
            );

            drawPieChart(
                '#inbound-outer-pie-chart',
                ${total.inboundStat.success.doubleValue() / (total.inboundStat.success + total.inboundStat.cancel)},
                {
                    startAngle: 0,
                    endAngle: 360,
                    innerRadius: 100,
                    outerRadius: 120,
                    innerLabel: ' ',
                    colorClasses: ['bcolor-bar1', 'bcolor-bar2']
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
