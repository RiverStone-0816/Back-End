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
        <tags:page-menu-tab url="/admin/stat/inbound/category/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">대표번호인입경로통계</div>
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
                                        <form:checkbox path="busy"/>
                                        <label>콜백</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="workHour"/>
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
                                <th>IVR선택</th>
                                <td colspan="11">
                                    <div class="ui form">
                                        <form:select path="ivrMulti" multiple="multiple" class="ui fluid dropdown">
                                            <form:option value="" label="서비스선택"/>
                                            <form:options items="${ivrNodes}"/>
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
                                인입경로별통계 <span class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</span>
                            </div>
                            <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        </div>
                        <div class="table-scroll-wrap">
                            <table class="ui celled table compact unstackable structured border-top">
                                <thead>
                                <tr>
                                    <th rowspan="2">날짜/시간</th>
                                    <th rowspan="2">서비스</th>
                                    <th rowspan="2">IVR</th>
                                    <th colspan="${maxLevel+1}">인입경로</th>
                                    <th colspan="5">I/B 콜 현황</th>
                                    <th colspan="7">응답호 분석</th>
                                    <th colspan="3">통화시간 분석</th>
                                </tr>
                                <tr>
                                    <c:forEach var="i" begin="1" end="${maxLevel+1}">
                                        <th>${i}th</th>
                                    </c:forEach>
                                    <th>인입콜수</th>
                                    <th>단순조회</th>
                                    <th>연결요청</th>
                                    <th>응대호</th>
                                    <th>포기호</th>

                                    <th>호응답률</th>
                                    <th>서비스레벨 호응답률</th>

                                    <th>~10(초)</th>
                                    <th>~20(초)</th>
                                    <th>~30(초)</th>
                                    <th>~40(초)</th>
                                    <th>40~(초)</th>

                                    <th>I/B 총통화시간</th>
                                    <th>평균 통화시간</th>
                                    <th>평균 대기시간</th>
                                </tr>
                                </thead>

                                <c:choose>
                                    <c:when test="${stat.size() > 0}">
                                        <tbody>
                                        <c:forEach var="stat" items="${stat}">
                                            <tr class="-lead-tr-service">
                                            <td rowspan="${stat.recordList.stream().map(e -> e.recordNameList.size()+1).sum()+1}">${g.htmlQuote(stat.timeInformation)}</td>
                                            <td rowspan="${stat.recordList.stream().map(e -> e.recordNameList.size()+1).sum()+1}">${g.htmlQuote(stat.svcName)}</td>
                                            <c:choose>
                                                <c:when test="${stat.recordList.size() > 0}">
                                                    <c:forEach var="record" items="${stat.recordList}">
                                                        <tr class="-lead-tr-ivr">
                                                        <td rowspan="${record.recordNameList.size()+1}">${g.htmlQuote(record.ivrName)}</td>
                                                        <c:choose>
                                                            <c:when test="${record.recordNameList.size() > 0}">
                                                                <c:forEach var="recordName" items="${record.recordNameList}">
                                                                    <tr class="-tr-record-name"
                                                                        data-service="${g.escapeQuote(stat.timeInformation)}|${g.escapeQuote(stat.svcName)}"
                                                                        data-ivr="${g.escapeQuote(record.ivrName)}"
                                                                        data-record-level="${recordName.level}"
                                                                        data-record="${g.escapeQuote(recordName.name)}">

                                                                        <c:forEach var="i" begin="0" end="${maxLevel}">
                                                                            <td>
                                                                                <c:if test="${recordName.level == i}">
                                                                                    ${g.htmlQuote(recordName.name)}&ensp;
                                                                                </c:if>
                                                                            </td>
                                                                        </c:forEach>

                                                                        <td>${recordName.record.total}</td>
                                                                        <td>${recordName.record.onlyRead}</td>
                                                                        <td>${recordName.record.connReq}</td>
                                                                        <td>${recordName.record.success}</td>
                                                                        <td>${recordName.record.cancel}</td>

                                                                        <td>${recordName.record.responseRate}%</td>
                                                                        <td>${recordName.record.svcLevelAvg}%</td>

                                                                        <td>${recordName.record.waitSucc_0_10}</td>
                                                                        <td>${recordName.record.waitSucc_10_20}</td>
                                                                        <td>${recordName.record.waitSucc_20_30}</td>
                                                                        <td>${recordName.record.waitSucc_30_40}</td>
                                                                        <td>${recordName.record.waitSucc_40}</td>

                                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(recordName.record.billSecSum)}</td>
                                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(recordName.record.billSecAvg)}</td>
                                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(recordName.record.waitAvg)}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach var="i" begin="0" end="${maxLevel+15}">
                                                                    <td></td>
                                                                </c:forEach>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        </tr>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="i" begin="0" end="${maxLevel+15}">
                                                        <td></td>
                                                    </c:forEach>
                                                    <tr></tr>
                                                </c:otherwise>
                                            </c:choose>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </c:when>
                                    <c:otherwise>
                                        <tbody>
                                        <tr>
                                            <td colspan="${15 + maxLevel + 1}" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                        </tbody>
                                    </c:otherwise>
                                </c:choose>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            $('.-button-time-unit').click(function () {
                $('.-button-time-unit').removeClass('active');
                $(this).addClass('active');
                $('#search-form [name=timeUnit]').val($(this).attr('data-value'));
            });

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/inbound/category/_excel?${g.escapeQuote(search.query)}', '_blank');
            }

            (function () {
                const rootPathStats = {
                    <c:forEach var="stat" items="${stat}">
                    '${g.escapeQuote(stat.timeInformation)}|${g.escapeQuote(stat.svcName)}': {
                        <c:forEach var="record" items="${stat.recordList}">
                        '${g.escapeQuote(record.ivrName)}': {

                            <c:forEach var="recordName" items="${record.recordNameList}" varStatus="recordNameStatus">

                            <c:if test="${recordName.level == 0}">

                            ${!recordNameStatus.first ? '],' : ''}

                            '${g.escapeQuote(recordName.name)}': [
                                </c:if>
                                {
                                    total: ${recordName.record.total},
                                    onlyRead: ${recordName.record.onlyRead},
                                    connReq: ${recordName.record.connReq},
                                    success: ${recordName.record.success},
                                    cancel: ${recordName.record.cancel},
                                    responseRate:${recordName.record.responseRate},
                                    svcLevelAvg: ${recordName.record.svcLevelAvg},
                                    waitSucc_0_10: ${recordName.record.waitSucc_0_10},
                                    waitSucc_10_20: ${recordName.record.waitSucc_10_20},
                                    waitSucc_20_30: ${recordName.record.waitSucc_20_30},
                                    waitSucc_30_40: ${recordName.record.waitSucc_30_40},
                                    waitSucc_40: ${recordName.record.waitSucc_40},
                                    billSecSum: ${recordName.record.billSecSum},
                                    billSecAvg: ${recordName.record.billSecAvg},
                                    waitAvg: ${recordName.record.waitAvg},
                                },

                                <c:if test="${recordNameStatus.last}">
                            ],
                            </c:if>
                            </c:forEach>
                        },
                        </c:forEach>
                    },
                    </c:forEach>
                };

                console.log(rootPathStats);

                keys(rootPathStats).map(function (service) {
                    const serviceStats = rootPathStats[service];
                    keys(serviceStats).map(function (ivr) {
                        const ivrStats = serviceStats[ivr];
                        keys(ivrStats).map(function (rootRecord) {
                            const rootRecordStats = ivrStats[rootRecord];
                            const summary = {};
                            rootRecordStats.map(function (recordValue) {
                                keys(recordValue).map(function (key) {
                                    summary[key] = (summary[key] === undefined ? 0 : summary[key]) + recordValue[key];
                                });
                            });

                            const recordsLength = rootRecordStats.length;
                            ['responseRate', 'svcLevelAvg', 'billSecAvg', 'waitAvg'].map(function (key) {
                                summary[key] /= recordsLength;
                            });

                            function timeFormatFromSecondsWithoutSimpleDateFormat(sec) {
                                return zeroPad((sec / (60 * 60)).toFixed(0), 2) + ":" + zeroPad((sec / 60 % 60).toFixed(0), 2) + ":" + zeroPad((sec % 60).toFixed(0), 2);
                            }

                            const element = $('<tr/>')
                                .append($('<td/>', {colspan: ${maxLevel + 1}, text: rootRecord}))
                                .append($('<td/>', {text: summary.total}))
                                .append($('<td/>', {text: summary.onlyRead}))
                                .append($('<td/>', {text: summary.connReq}))
                                .append($('<td/>', {text: summary.success}))
                                .append($('<td/>', {text: summary.cancel}))
                                .append($('<td/>', {text: (summary.responseRate / recordsLength) + '%'}))
                                .append($('<td/>', {text: (summary.svcLevelAvg / recordsLength) + '%'}))
                                .append($('<td/>', {text: summary.waitSucc_0_10}))
                                .append($('<td/>', {text: summary.waitSucc_10_20}))
                                .append($('<td/>', {text: summary.waitSucc_20_30}))
                                .append($('<td/>', {text: summary.waitSucc_30_40}))
                                .append($('<td/>', {text: summary.waitSucc_40}))
                                .append($('<td/>', {text: timeFormatFromSecondsWithoutSimpleDateFormat(summary.billSecSum)}))
                                .append($('<td/>', {text: timeFormatFromSecondsWithoutSimpleDateFormat(summary.billSecAvg / recordsLength)}))
                                .append($('<td/>', {text: timeFormatFromSecondsWithoutSimpleDateFormat(summary.waitAvg / recordsLength)}));

                            const rootElement = $('.-tr-record-name[data-record-level="0"]').filter(function () {
                                return $(this).attr('data-service') === service;
                            }).filter(function () {
                                return $(this).attr('data-ivr') === ivr;
                            }).filter(function () {
                                return $(this).attr('data-record') === rootRecord;
                            });
                            let beforeElement = rootElement;
                            for (let i = 1; i < recordsLength; i++)
                                beforeElement = beforeElement.next();
                            beforeElement.after(element);

                            rootElement.prevAll('.-lead-tr-service').first().children('[rowspan]').each(function () {
                                $(this).attr('rowspan', parseInt($(this).attr('rowspan')) + 1);
                            });
                            rootElement.prevAll('.-lead-tr-ivr').first().children('[rowspan]').each(function () {
                                $(this).attr('rowspan', parseInt($(this).attr('rowspan')) + 1);
                            });
                        });
                    });
                });
            })();
        </script>
    </tags:scripts>
</tags:tabContentLayout>
