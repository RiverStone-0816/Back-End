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
        <tags:page-menu-tab url="/admin/stat/consultant/consultant/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">상담원(개인별)통계</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
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
                                        <form:checkbox path="person" value="Y"/>
                                        <label>직통전화</label>
                                    </div>
                                    <div class="ui checkbox">
                                        <form:checkbox path="inner" value="Y"/>
                                        <label>내선통화</label>
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
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
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
                                    상담원(개인별)실적통계
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </text>
                            </div>
                            <div class="panel-body pd-1em">
                                <table class="ui celled table compact unstackable structured">
                                    <thead>
                                    <tr>
                                        <th rowspan="2">날짜/시간</th>
                                        <th rowspan="2">부서</th>
                                        <th rowspan="2">상담원명</th>
                                        <th colspan="2">총 통화</th>
                                        <th colspan="6">O/B</th>
                                        <th colspan="7">I/B</th>
                                        <th colspan="3">후처리 시간분석</th>
                                    </tr>
                                    <tr>
                                        <th>총 건수</th>
                                        <th>총 시간</th>

                                        <th>총 시도콜</th>
                                        <th>O/B건수<br>성공호</th>
                                        <th>비수신</th>
                                        <th>O/B<br>총 통화시간</th>
                                        <th>O/B<br>평균통화시간</th>
                                        <th>통화성공률</th>

                                        <th>I/B<br>전체콜</th>
                                        <th>응대호</th>
                                        <th>I/B<br>총 통화시간</th>
                                        <th>I/B<br>평균통화시간</th>
                                        <th>평균<br>연결시간</th>
                                        <th>포기호</th>
                                        <th>응대율</th>

                                        <th>후처리건수</th>
                                        <th>총 후처리시간</th>
                                        <th>후처리<br>평균시간</th>
                                    </tr>
                                    </thead>
                                    <c:choose>
                                        <c:when test="${list.get(0).userStatList.size() > 0 && list.size() > 0}">
                                            <tbody>
                                            <c:forEach var="e" items="${list}">
                                                <tr>
                                                <td rowspan="${e.userStatList.size() + 1}">${g.htmlQuote(e.timeInformation)}</td>
                                                <c:forEach var="f" items="${e.userStatList}">
                                                    <tr>
                                                        <td>${g.htmlQuote(f.groupName)}</td>
                                                        <td>${g.htmlQuote(f.idName)}</td>

                                                        <td>${f.totalCnt}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.totalBillSec)}</td>

                                                        <td>${f.outboundStat.outTotal}</td>
                                                        <td>${f.outboundStat.outSuccess}</td>
                                                        <td>${f.outboundStat.fails}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.outboundStat.outBillSecSum)}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.outboundStat.avgBillSec)}</td>
                                                        <td>${f.outboundStat.avgRate}%</td>

                                                        <td>${f.inboundStat.total}</td>
                                                        <td>${f.inboundStat.success}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.inboundStat.billSecSum)}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.inboundStat.avgBillSec)}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.inboundStat.avgWaitSec)}</td>
                                                        <td>${f.inboundStat.cancel}</td>
                                                        <td>${f.inboundStat.avgRate}%</td>

                                                        <td>${f.memberStatusStat.postProcess}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.memberStatusStat.postProcessTime)}</td>
                                                        <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(f.memberStatusStat.postPrecessAvgTime)}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tr>
                                            </c:forEach>
                                            </tbody>

                                            <tfoot>
                                            <tr>
                                                <td colspan="3">합계(${total.totalCnt > 0 ? 1 : 0 })</td>

                                                <td>${total.totalCnt}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.totalBillSec)}</td>

                                                <td>${total.outboundStat.outTotal}</td>
                                                <td>${total.outboundStat.outSuccess}</td>
                                                <td>${total.outboundStat.fails}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.outboundStat.outBillSecSum)}</td>
                                                <td>${g.timeFormatFromSecondsWithoutSimpleDateFormat(total.outboundStat.avgBillSec)}</td>
                                                <td>${String.format("%.1f", total.outboundStat.avgRate)}%</td>

                                                <td>${total.inboundStat.total}</td>
                                                <td>${total.inboundStat.success}</td>
                                                <td>${g.timeFormatFromSeconds(total.inboundStat.billSecSum)}</td>
                                                <td>${g.timeFormatFromSeconds(total.inboundStat.avgBillSec)}</td>
                                                <td>${g.timeFormatFromSeconds(total.inboundStat.avgWaitSec)}</td>
                                                <td>${total.inboundStat.cancel}</td>
                                                <td>${String.format("%.1f", total.inboundStat.avgRate)}%</td>

                                                <td>${total.memberStatusStat.postProcess}</td>
                                                <td>${g.timeFormatFromSeconds(total.memberStatusStat.postProcessTime)}</td>
                                                <td>${g.timeFormatFromSeconds(total.memberStatusStat.postPrecessAvgTime)}</td>
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
                window.open(contextPath + '/admin/stat/consultant/consultant/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
