<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/email/consultation-history/"/>
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
                                <div class="ten wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="endDate" style="display:none">to</label>
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
                                <div class="two wide column"><label class="control-label">처리상태</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="resultCode">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${resultCodeTypes}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">이메일</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="fromEmail"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">발신자명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="fromName"/></div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">상담자</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="userId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${users}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">고객명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="customName"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">고객사명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="customCompanyName"/></div>
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
                        <button type="button" class="ui basic green button" onclick="downloadExcel()">Excel 다운로드</button>
                        <button type="button" class="ui basic button">재분배</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact structured unstackable num-tbl">
                        <thead>
                        <tr>
                            <th rowspan="2">번호</th>
                            <th colspan="2">상담원</th>
                            <th colspan="3">처리결과</th>
                            <th colspan="4">이메일정보</th>
                            <th colspan="2">연관고객정보</th>
                        </tr>
                        <tr>
                            <th>분배</th>
                            <th>이관</th>
                            <th>처리여부</th>
                            <th>관련서비스</th>
                            <th>상담종류</th>
                            <th>메일수신시간</th>
                            <th>발신자명</th>
                            <th>발신이메일</th>
                            <th>제목</th>
                            <th>고객명</th>
                            <th>고객사정보</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.userName)}</td>
                                        <td>${g.htmlQuote(e.userTrName)}</td>
                                        <td>${g.htmlQuote(e.resultCodeName)}</td>
                                        <td>${g.htmlQuote(e.resultServiceName)}</td>
                                        <td>${g.htmlQuote(e.resultKindName)}</td>
                                        <td>${g.htmlQuote(e.sentDate)}</td>
                                        <td>${g.htmlQuote(e.fromEmail)}></td>
                                        <td>${g.htmlQuote(e.fromName)}</td>
                                        <td>${g.htmlQuote(e.subject)}</td>
                                        <td>${g.htmlQuote(e.customName)}</td>
                                        <td>${g.htmlQuote(e.customCompanyName)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="12" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/email/consultation-history/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>
</tags:tabContentLayout>
