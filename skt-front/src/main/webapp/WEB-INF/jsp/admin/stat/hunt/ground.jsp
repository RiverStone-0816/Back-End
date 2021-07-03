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
        <tags:page-menu-tab url="/admin/stat/hunt/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">상담그룹별통계</div>
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
                                <td colspan="3">
                                    <div class="ui checkbox">
                                        <form:checkbox path="busy" value="Y"/>
                                        <label>콜백</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="workHour" value="Y"/>
                                        <label>업무시간</label>
                                    </div>
                                </td>
                                <th>서비스선택</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="serviceNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="서비스선택"/>
                                            <form:options items="${services}"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>부서선택</th>
                                <td colspan="3">
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini orange compact -select-group">
                                            <i class="search icon"></i>
                                        </button>
                                        <form:hidden path="groupCode"/>
                                        <div class="ui breadcrumb -group-name">
                                            <c:choose>
                                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                                        <span class="section">${g.htmlQuote(e)}</span>
                                                        <c:if test="${!status.last}">
                                                            <i class="right angle icon divider"></i>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <button type="button" class="ui icon button mini compact -clear-group">
                                            <i class="undo icon"></i>
                                        </button>
                                    </div>
                                </td>
                                <th>상담원선택</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="personIds" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="상담원선택"/>
                                            <form:options items="${persons}"/>
                                        </form:select>
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
                                큐(그룹)별통계  <span class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</span>
                            </div>
                            <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        </div>
                        <table class="ui celled table compact unstackable structured border-top">
                                <thead>
                                <tr>
                                    <th rowspan="2">날짜/시간</th>
                                    <th rowspan="2">큐그룹</th>
                                    <th colspan="8">I/B</th>
                                </tr>
                                <tr>
                                    <th>I/B 연결요청</th>
                                    <th>응대호</th>
                                    <th>포기호</th>
                                    <th>콜백</th>
                                    <th>I/B 총통화시간</th>
                                    <th>평균통화시간</th>
                                    <th>응답률</th>
                                    <th>서비스레벨 호응답률</th>
                                </tr>
                                </thead>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <tbody>
                                        <c:forEach var="element" items="${list}">
                                            <c:if test="${element.statQueueInboundResponses.size() > 0}">
                                                <tr>
                                                <td rowspan="${element.statQueueInboundResponses.size() + 1}">${g.htmlQuote(element.timeInformation)}</td>
                                                <c:forEach var="e" items="${element.statQueueInboundResponses}">
                                                    <tr>
                                                        <td>${g.htmlQuote(e.queueName)}</td>

                                                        <td>${e.inTotal}</td>
                                                        <td>${e.inSuccess}</td>
                                                        <td>${e.cancel}</td>
                                                        <td>${e.callbackCount}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.inBillSecSum)}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(e.avgBillSec)}</td>
                                                        <td>${e.avgRateValue}%</td>
                                                        <td>${e.serviceLevelOk}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                        </tbody>
                                        <tfoot>
                                        <tr>
                                            <td colspan="2">합계</td>

                                            <td>${total.inTotal}</td>
                                            <td>${total.inSuccess}</td>
                                            <td>${total.cancel}</td>
                                            <td>${total.callbackCount}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.inBillSecSum)}</td>
                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.avgBillSec)}</td>
                                            <td>${String.format("%.1f", total.avgRateValue)}%</td>
                                            <td>${total.serviceLevelOk}</td>
                                        </tr>
                                        </tfoot>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody>
                                        <tr>
                                            <td colspan="9" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                        </tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                    </div>
                    <div class="panel-section">
                        <div class="panel">
                            <div class="panel-heading">
                                <text class="content">총통화그래프</text>
                            </div>
                            <div class="panel-body pd-1em">
                                <div class="-chart" id="total-call-chart" style="height: 300px;"></div>
                                <div style="text-align: center; padding: 2em 0;">
                                    <text style="display: inline-block;"><i class="circle color-red" style="background-color: #F37402 !important;"></i>응대호</text>
                                    <text style="display: inline-block;"><i class="circle color-blue" style="background-color: #00802F !important;"></i>포기호</text>
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
                    success: ${element.statQueueInboundResponses.stream().map(e -> e.inSuccess).sum()},
                    cancel: ${element.statQueueInboundResponses.stream().map(e -> e.cancel).sum()},
                }, </c:forEach>
            ];
            chartjs.drawLineChart($('#total-call-chart')[0], data, 'time', ['success', 'cancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue'],
                colors: ['#F37402', '#00802F'],
                labels: ['응대호', '포기호'],
            });
            </c:if>

            $('.-button-time-unit').click(function () {
                $('.-button-time-unit').removeClass('active');
                $(this).addClass('active');
                $('#search-form [name=timeUnit]').val($(this).attr('data-value'));
            });

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/hunt/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
