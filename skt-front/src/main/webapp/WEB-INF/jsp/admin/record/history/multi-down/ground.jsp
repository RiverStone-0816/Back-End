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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/record/history/multi-down/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">녹취파일다운로드</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>업로드날짜</th>
                                <td colspan="7" class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic button -control-entity" style="display: none;" data-entity="Record" onclick="deleteEntity(getEntityId('Record'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl ${list.size() > 0 ? "selectable-only" : null}" data-entity="Record">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>다운로드명</th>
                            <th>실행일</th>
                            <th>실행자</th>
                            <th>사이즈(KBytes)</th>
                            <th>상태</th>
                            <th>다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${e.downName}</td>
                                        <td><fmt:formatDate value="${e.requestdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.userName)}</td>
                                        <td><fmt:formatNumber value="${e.size}" pattern="#,###"/></td>
                                        <td>${e.status == 'A' ? '요청완료' : (e.status == 'B' ? '준비중' : '준비완료')}</td>
                                        <td>
                                            <c:if test="${g.user.downloadRecordingAuthority.equals('ALL')}">
                                                <c:if test="${e.status == 'C'}">
                                                    <a class="ui icon button mini compact" href="${apiServerUrl}/api/v1/admin/record/history/record-multi-down/${e.seq}/resource?token=${accessToken}">
                                                        <i class="arrow down icon"></i>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
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
            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/record-multi-down/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
