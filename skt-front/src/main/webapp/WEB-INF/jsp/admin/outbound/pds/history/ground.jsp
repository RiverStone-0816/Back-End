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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/history/"/>
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
                                <div class="two wide column"><label class="control-label">실행날짜</label></div>
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
                                <div class="two wide column"><label class="control-label">상담그룹</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="seq">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${pdsGroups}"/>
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
                    <div class="pull-right">
                        <c:if test="${serviceKind.equals('SC')}">
                            <a href="<c:url value="/admin/outbound/pds/result/"/>" class="ui button basic tab-indicator">상담결과이력</a>
                        </c:if>
                        <a href="<c:url value="/admin/record/history/history/"/>" class="ui button basic tab-indicator">통화이력</a>
                        <button class="ui basic button -control-entity" data-entity="PdsHistory" style="display: none;" onclick="deleteEntity(getEntityId('PdsHistory'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? 'selectable-only' : null}" data-entity="PdsHistory">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>실행명</th>
                            <th>그룹명</th>
                            <th>실행시작</th>
                            <th>실행종료</th>
                            <th>수행시간</th>
                            <th>고객수/전화번호수</th>
                            <th>진행된건/남은건</th>
                            <th>수신/비수신/통화중/비연결/기타</th>
                            <th>상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.executeId)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.executeName)}</td>
                                        <td>${g.htmlQuote(e.pdsName)}</td>
                                        <td>${g.htmlQuote(e.startDate)}</td>
                                        <td>${g.htmlQuote(e.stopDate)}</td>
                                        <td>${e.totalRunTime}</td>
                                        <td>${e.totalCnt}/${e.numberCnt}</td>
                                        <td>${e.callTryCnt}/${e.noCallCnt}</td>
                                        <td>${g.htmlQuote(e.lastCallStat)}</td>
                                        <td>${g.htmlQuote(g.messageOf('PDSGroupExecuteStatus', e.pdsStatus))}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="10" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/pds/history/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function deleteEntity(executeId) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/pds-history/execution/' + encodeURIComponent(executeId)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
