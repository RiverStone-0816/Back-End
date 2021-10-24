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
        <tags:page-menu-tab url="/admin/service/help/manual/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div>
                        검색
                    </div>
                    <div class="dp-flex align-items-center">
                        <div class="ui slider checkbox mr15">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
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
                                <div class="two wide column"><label class="control-label">제목</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="searchText"/>
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
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span>건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button -control-entity" data-entity="Manual" style="display: none;" onclick="popupShowModal(getEntityId('Manual'))">보기</button>
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="Manual" style="display: none;" onclick="popupModal(getEntityId('Manual'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="Manual" style="display: none;" onclick="deleteEntity(getEntityId('Manual'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable fixed ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="Manual">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th class="nine wide">제목</th>
                            <th class="two wide">등록일</th>
                            <th class="two wide">작성자</th>
                            <th class="one wide">조회수</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.title)}</td>
                                        <td><fmt:formatDate value="${e.createdAt}" pattern="yyyy-MM-dd"/></td>
                                        <td>${g.htmlQuote(e.writer)}</td>
                                        <td>${e.viewCnt}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/service/help/manual/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupShowModal(id) {
                popupReceivedHtml('/admin/service/help/manual/' + (id || 'new') + '/modal-show', 'modal-show-manual');
            }

            function popupModal(id) {
                popupReceivedHtml('/admin/service/help/manual/' + (id || 'new') + '/modal', 'modal-manual');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/manual/' + id).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
