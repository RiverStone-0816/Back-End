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
        <tags:page-menu-tab url="/admin/service/log/login-history/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
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
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색기간</label></div>
                                <div class="ten wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">로그인명</label></div>
                                <div class="two wide column">
                                    <div class="ui form"><form:input path="userName"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">내선</label></div>
                                <div class="two wide column">
                                    <div class="ui form"><form:input path="extension"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">아이디</label></div>
                                <div class="two wide column">
                                    <div class="ui form"><form:input path="userId"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span>건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic green button" type="button" onclick="downloadExcel()">Excel 다운로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>로그인명(아이디)</th>
                            <th>로그인</th>
                            <th>로그아웃</th>
                            <th>로그인내선</th>
                            <th>내선부서</th>
                            <th>로그인 시 전화 끊은 후 상태</th>
                            <th>로그아웃상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.userName)}(${g.htmlQuote(e.userId)})</td>
                                        <td><fmt:formatDate value="${e.loginDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        <td><fmt:formatDate value="${e.logoutDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                        <td>${g.htmlQuote(e.extension)}</td>
                                        <td class="five wide">
                                            <div class="ui breadcrumb">
                                                <c:forEach var="o" items="${e.companyTrees}" varStatus="oStatus">
                                                    <span class="section">${o}</span>
                                                    <c:if test="${!oStatus.last}">
                                                        <i class="right angle icon divider"></i>
                                                    </c:if>
                                                </c:forEach>
                                            </div>
                                        </td>
                                        <td>${e.dialStatus != null ? g.htmlQuote(message.getEnumText(e.dialStatus)) : null}</td>
                                        <td>${e.logoutStatus != null ? g.htmlQuote(message.getEnumText(e.logoutStatus)) : null}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/service/log/login-history/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function downloadExcel() {
                restSelf.put('/api/web-log/down/LOGIN_LOG').done(function () {
                    window.open(contextPath + '/admin/service/log/login-history/_excel?${g.escapeQuote(search.query)}', '_blank');
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
