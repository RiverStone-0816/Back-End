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
        <tags:page-menu-tab url="/admin/outbound/voc/result/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label>검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색기간</label></div>
                                <div class="nine wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">VOC그룹</label></div>
                                <div class="three wide column">
                                    <div class="ui form">
                                        <form:select path="vocGroupSeq">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${groups}"/>
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
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable">
                        <thead>
                        <tr>
                            <th rowspan="2">번호</th>
                            <th colspan="5">기본정보</th>
                            <th colspan="3">VOC결과</th>
                        </tr>
                        <tr>
                            <th>날짜</th>
                            <th>고객전화번호</th>
                            <th>상담사</th>
                            <th>VOC명</th>
                            <th>설문명</th>
                            <c:choose>
                                <c:when test="${maxLevel > 0}">
                                    <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                        <th>${status.index}단계</th>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <th>단계</th>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.customNumber)}</td>
                                        <td>${g.htmlQuote(e.idName)}</td>
                                        <td>${g.htmlQuote(e.group.vocGroupName)}</td>
                                        <td>${g.htmlQuote(e.group.research.researchName)}</td>

                                        <c:if test="${maxLevel > 0}">
                                            <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                                <c:set var="value" value="${e.results != null && e.results.size() > status.index ? e.results.get(status.index) : null}"/>
                                                <td>${value}</td>
                                            </c:forEach>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <c:choose>
                                        <c:when test="${maxLevel > 0}">
                                            <td colspan="${6 + maxLevel}" class="null-data">조회된 데이터가 없습니다.</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td colspan="7" class="null-data">조회된 데이터가 없습니다.</td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/voc/result/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>
</tags:tabContentLayout>
