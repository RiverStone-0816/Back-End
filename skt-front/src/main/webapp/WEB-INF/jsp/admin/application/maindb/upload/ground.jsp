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
        <tags:page-menu-tab url="/admin/application/maindb/upload/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">업로드이력조회</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter">
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
                            <tr>
                                <th>고객DB그룹</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="maindbGroupId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${maindbTypes}"/>
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
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                        <button class="ui basic button -control-entity" data-entity="MaindbUpload"
                                style="display: none;" onclick="popupModal(getEntityId('MaindbUpload'))">업로드로그보기
                        </button>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/application/maindb/upload/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? 'selectable-only' : null}" data-entity="MaindbUpload">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>고객DB그룹명</th>
                            <th>업로드일</th>
                            <th>업로드화일명</th>
                            <th>업로드순서</th>
                            <th>업로드데이터수</th>
                            <th>업로드상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.uploadId)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.maindbGroupName)}</td>
                                        <td><fmt:formatDate value="${e.uploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.uploadName)}</td>
                                        <td>${e.tryCnt}</td>
                                        <td>${e.uploadCnt}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${e.uploadStatus.contains('E')}">
                                                    에러:${g.htmlQuote(e.uploadStatus.substring(1))}건
                                                </c:when>
                                                <c:otherwise>
                                                    ${g.htmlQuote(g.messageOf('UploadStatus', e.uploadStatus))}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="null-data">조회된 데이터가 없습니다.</td>
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
            function popupModal(uploadId) {
                popupReceivedHtml('/admin/application/maindb/upload/' + encodeURIComponent(uploadId) + '/modal', 'modal-upload');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
