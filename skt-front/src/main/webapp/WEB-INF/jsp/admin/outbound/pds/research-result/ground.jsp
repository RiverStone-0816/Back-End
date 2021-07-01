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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/research-result/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">설문결과이력</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <input type="checkbox" name="newsletter" id="_newsletter" checked="" tabindex="0" class="hidden"><label for="_newsletter">검색옵션 전체보기</label>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>검색기간</th>
                                <td colspan="7" class=" -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>${serviceKind.equals('SC') ? "PDS실행그룹" : "Auto IVR그룹"}</th>
                                <td colspan="7">
                                    <div class="ui form">
                                        <form:select path="executeId">
                                            <form:option value="" label="선택안함"/>
                                            <c:forEach var="e" items="${executingPdsList}">
                                                <form:option value="${e.executeId}" label="${e.executeName}"/>
                                            </c:forEach>
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
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                        <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        <div class="ui basic buttons">
                            <button class="ui button -control-entity" data-entity="PdsResearchResult" style="display: none;" onclick="popupModal(getEntityId('PdsResearchResult'))">수정</button>
                            <button class="ui button -control-entity" data-entity="PdsResearchResult" style="display: none;" onclick="deleteEntity(getEntityId('PdsResearchResult'))">삭제</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured num-tbl unstackable border-top" data-entity="PdsResearchResult">
                        <thead>
                        <tr>
                            <th rowspan="2">번호</th>
                            <th colspan="2">기본정보</th>
                            <th colspan="${pdsType.fields.size()}">고객정보필드</th>
                            <c:if test="${maxLevel > 0}">
                                <th colspan="${maxLevel}">설문정보</th>
                            </c:if>
                        </tr>
                        <c:if test="${fn:length(pdsType.fields) > 0}">
                            <tr>
                                <th>발신시간</th>
                                <th>전화번호</th>
                                <c:forEach var="field" items="${pdsType.fields}">
                                    <th>${g.htmlQuote(field.fieldInfo)}</th>
                                </c:forEach>

                                <c:if test="${maxLevel > 0}">
                                    <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                        <th>${status.index}단계</th>
                                    </c:forEach>
                                </c:if>
                            </tr>
                        </c:if>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}">
                                        <td>${status.index + 1}</td>
                                        <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.customNumber)}</td>
                                        <c:forEach var="field" items="${pdsType.fields}">
                                            <td>${g.htmlQuote(customIdToFieldNameToValueMap.get(e.customId).get(field.fieldId))}</td>
                                        </c:forEach>

                                        <c:if test="${maxLevel > 0}">
                                            <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                                <c:set var="value" value="${seqToPathIndexToValueMap.get(e.seq).get(status.index)}"/>
                                                <td>
                                                    <c:if test="${value != null && value.split('_')[1] != null && idToNumberToDescription.get(value.split('_')[0]) != null}">
                                                        [${value.split('_')[1]}] ${idToNumberToDescription.get(value.split('_')[0]).get(value.split('_')[1])}
                                                    </c:if>
                                                </td>
                                            </c:forEach>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="${3 + (fn:length(pdsType.fields) > 0 ? pdsType.fields.size() : 1) + maxLevel}" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function downloadExcel() {
                window.open(contextPath + '/admin/outbound/pds/research-result/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
