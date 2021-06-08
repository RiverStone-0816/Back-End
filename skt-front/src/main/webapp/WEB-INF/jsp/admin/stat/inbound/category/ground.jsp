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
                                    </div>
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
                                <button type="button" class="ui button sharp brand large">검색</button>
                                <button type="submit" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                        <%--<div class="ui grid">
                            <div class="row">
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
                                <div class="content">
                                    인입경로별통계
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </div>
                            </div>
                            <div class="panel-body pd-1em">
                                <table class="ui celled table compact unstackable structured">
                                    <thead>
                                    <tr>
                                        <th rowspan="2">날짜/시간</th>
                                        <th rowspan="2">서비스</th>
                                        <th rowspan="2">IVR</th>
                                        <th colspan="${maxLevel+1}">인입경로</th>
                                        <th colspan="5">I/B 콜 현황</th>
                                        <th colspan="5">성과지표</th>
                                        <th colspan="5">응대호 대기시간 분석</th>
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

                                        <th>평균<br>통화시간</th>
                                        <th>평균<br>대기시간</th>
                                        <th>호응답률</th>
                                        <th>서비스레벨<br>호응답률</th>
                                        <th>단순조회율</th>

                                        <th>~10(초)</th>
                                        <th>~20(초)</th>
                                        <th>~30(초)</th>
                                        <th>~40(초)</th>
                                        <th>40~(초)</th>
                                    </tr>
                                    </thead>

                                    <c:choose>
                                        <c:when test="${stat.size() > 0}">
                                            <tbody>
                                            <c:forEach var="stat" items="${stat}">
                                                <tr>
                                                <td rowspan="${stat.recordList.stream().map(e -> e.recordNameList.size()+1).sum()+1}">${g.htmlQuote(stat.timeInformation)}</td>
                                                <td rowspan="${stat.recordList.stream().map(e -> e.recordNameList.size()+1).sum()+1}">${g.htmlQuote(stat.svcName)}</td>
                                                <c:choose>
                                                    <c:when test="${stat.recordList.size() > 0}">
                                                        <c:forEach var="record" items="${stat.recordList}">
                                                            <tr>
                                                            <td rowspan="${record.recordNameList.size()+1}">${g.htmlQuote(record.ivrName)}</td>
                                                            <c:choose>
                                                                <c:when test="${record.recordNameList.size() > 0}">
                                                                    <c:forEach var="recordName" items="${record.recordNameList}">
                                                                        <tr>
                                                                            <c:forEach var="i" begin="0" end="${maxLevel}">
                                                                                <td>
                                                                                    <c:if test="${recordName.level == i}">
                                                                                        ${g.htmlQuote(recordName.name)}&ensp;
                                                                                    </c:if>
                                                                                </td>
                                                                            </c:forEach>

                                                                            <td><span class="data-detail-trigger">${recordName.record.total}</span></td>
                                                                            <td><span class="data-detail-trigger">${recordName.record.onlyRead}</span></td>
                                                                            <td><span class="data-detail-trigger">${recordName.record.connReq}</span></td>
                                                                            <td><span class="data-detail-trigger">${recordName.record.success}</span></td>
                                                                            <td><span class="data-detail-trigger">${recordName.record.cancel}</span></td>

                                                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(recordName.record.billSecAvg)}</td>
                                                                            <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(recordName.record.waitAvg)}</td>
                                                                            <td>${recordName.record.responseRate}%</td>
                                                                            <td>${recordName.record.svcLevelAvg}%</td>
                                                                            <td>${recordName.record.ivrAvg}%</td>

                                                                            <td>${recordName.record.waitSucc_0_10}</td>
                                                                            <td>${recordName.record.waitSucc_10_20}</td>
                                                                            <td>${recordName.record.waitSucc_20_30}</td>
                                                                            <td>${recordName.record.waitSucc_30_40}</td>
                                                                            <td>${recordName.record.waitSucc_40}</td>
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
                                                <td colspan="21" class="null-data">조회된 데이터가 없습니다.</td>
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
            /*$('.data-detail-trigger').click(function () {
                $('#modal-data-detain-view').modalShow();
            })*/

            $('.-button-time-unit').click(function () {
                $('.-button-time-unit').removeClass('active');
                $(this).addClass('active');
                $('#search-form [name=timeUnit]').val($(this).attr('data-value'));
            });

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/inbound/category/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
