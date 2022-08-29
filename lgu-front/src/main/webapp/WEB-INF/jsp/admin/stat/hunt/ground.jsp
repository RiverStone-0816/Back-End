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
                                <div class="four wide column">
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
                                <div class="two wide column"><label class="control-label">추가조건선택</label></div>
                                <div class="five wide column">
                                    <div class="ui checkbox">
                                        <form:checkbox path="busy" value="Y"/>
                                        <label>콜백</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="workHour" value="Y"/>
                                        <label>업무시간</label>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">서비스선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form">
                                        <form:select path="serviceNumbers" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="서비스선택"/>
                                            <form:options items="${services}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">부서선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini blue compact -select-group">
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
                                </div>
                                <div class="two wide column"><label class="control-label">상담원선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form">
                                        <form:select path="personIds" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="상담원선택"/>
                                            <form:options items="${persons}" itemValue="id" itemLabel="idName"/>
                                        </form:select>
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
                                    큐(그룹)별통계
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </text>
                            </h3>
                        </div>
                        <div class="sixteen wide column">
                            <table class="ui celled table compact unstackable definition structured">
                                <thead>
                                <tr>
                                    <th rowspan="2">날짜/시간</th>
                                    <th rowspan="2">큐그룹</th>
                                    <th colspan="8" class="color red">I/B</th>
                                </tr>
                                <tr>
                                    <th class="color red">I/B 연결요청</th>
                                    <th class="color red">응대호</th>
                                    <th class="color red">포기호</th>
                                    <th class="color red">콜백</th>
                                    <th class="color red">I/B 총 통화시간</th>
                                    <th class="color red">I/B 평균통화시간</th>
                                    <th class="color red">호응답률</th>
                                    <th class="color red">서비스레벨 호응답률</th>
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
                                                        <td>${e.getSvcLevelAvg()}%</td>
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
                                            <td>${total.getSvcLevelAvg()}%</td>
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
                        <div class="sixteen wide column">
                            <h3 class="ui header center aligned">
                                <text class="content">총통화그래프</text>
                            </h3>
                        </div>
                        <div class="sixteen wide column">
                            <div class="-chart basic-chart-container" id="total-call-chart"></div>
                            <div class="chart-label-container">
                                <div class="ui segment secondary">
                                    <text class="label-list"><i class="point color-red"></i>응대호</text>
                                    <text class="label-list"><i class="point color-blue"></i>포기호</text>
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
            drawLineChart($('#total-call-chart')[0], data, 'time', ['success', 'cancel'], {
                ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['color-red', 'color-blue']
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
