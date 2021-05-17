<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stat/result/link/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" commandName="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
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
                                <div class="two wide column"><label class="control-label">수/발신선택</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="type">
                                            <form:option value="" label="선택안함" data-type=""/>
                                            <c:forEach var="e" items="${sendReceiveTypes}">
                                                <form:option value="${g.htmlQuote(e.key)}" label="${g.htmlQuote(e.value)}"/>
                                            </c:forEach>
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
                        <button type="button" class="ui basic green button" onclick="downloadExcel()">Excel 다운로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui grid">
                        <div class="sixteen wide column">
                            <h3 class="ui header center aligned">
                                <text class="content">
                                    상담결과통계(연계)
                                    <div class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</div>
                                </text>
                            </h3>
                        </div>
                        <div class="sixteen wide column">
                            <table class="ui celled table compact unstackable structured fixed">
                                <thead>
                                <tr>
                                    <th>고객유형</th>
                                    <th>업무구분</th>
                                    <th>세부구분</th>
                                    <c:forEach var="date" items="${dates}">
                                        <th>${g.htmlQuote(date)}</th>
                                    </c:forEach>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <c:forEach var="e" items="${list}">
                                            <c:forEach var="e2" items="${e.fieldResponses}" varStatus="status">
                                                <c:if test="${status.first}">
                                                    <td rowspan="${e.fieldResponses.stream().map(e2 -> e2.codeResponses.size()).sum() + 1}">${g.htmlQuote(e.name)}</td>
                                                </c:if>
                                                <c:forEach var="e3" items="${e2.codeResponses}" varStatus="status2">
                                                    <tr>
                                                        <c:if test="${status2.first}">
                                                            <td rowspan="${e2.codeResponses.size()}">${g.htmlQuote(e2.fieldInfo)}</td>
                                                        </c:if>
                                                        <td>${g.htmlQuote(e3.codeName)}</td>
                                                        <c:forEach var="date" items="${dates}">
                                                            <td>${codeToDateToCountMap.get(e3.codeId).getOrDefault(date, 0)}</td>
                                                        </c:forEach>
                                                    </tr>
                                                </c:forEach>
                                            </c:forEach>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="${3 + dates.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function downloadExcel() {
                window.open(contextPath + '/admin/stat/result/link/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>