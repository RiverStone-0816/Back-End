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
        <tags:page-menu-tab url="/admin/service/help/task-script/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <form:hidden path="categoryId"/>
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">지식관리</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>등록일</th>
                                <td colspan="3" class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                                <th>제목</th>
                                <td>
                                    <div class="ui form">
                                        <form:input path="title"/>
                                    </div>
                                </td>
                                <th>태그</th>
                                <td>
                                    <div class="ui form">
                                        <form:input path="tag"/>
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
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span>건</h3>
                        <div class="ui buttons">
                            <button class="ui basic button" onclick="popupCategoryModal();">분류관리</button>
                            <button class="ui basic button -control-entity" data-entity="TaskScript" style="display: none;" onclick="popupShowModal(getEntityId('TaskScript'))">보기</button>
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="TaskScript" style="display: none;" onclick="popupModal(getEntityId('TaskScript'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="TaskScript" style="display: none;" onclick="deleteEntity(getEntityId('TaskScript'))">삭제</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/service/help/task-script/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui secondary  menu">
                        <a href="javascript: chnageCategory()" class="item ${search.categoryId == null ? 'active' : null}">전체</a>
                        <c:forEach var="e" items="${categories}">
                            <a href="javascript: chnageCategory(${e.key})" class="item ${search.categoryId == e.key ? 'active' : null}">${g.htmlQuote(e.value)}</a>
                        </c:forEach>
                    </div>
                    <table class="ui celled table num-tbl unstackable ${pagination.rows.size() > 0 ? 'selectable-only' : null}" data-entity="TaskScript">
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>번호</th>
                            <th>제목</th>
                            <th class="five wide">태그</th>
                            <th class="two wide">등록일</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.title)}</td>
                                        <td>
                                            <c:forEach var="tag" items="${e.tag.split(',')}">
                                                <div class="ui tiny label">${g.htmlQuote(tag)}</div>
                                            </c:forEach>
                                        </td>
                                        <td><fmt:formatDate value="${e.createdAt}" pattern="yyyy-MM-dd"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="null-data">조회된 데이터가 없습니다.</td>
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
            function chnageCategory(categoryId) {
                const searchForm = $('#search-form');
                searchForm.find('[name=categoryId]').val(categoryId != null ? categoryId : '');
                searchForm.submit();
            }

            function popupShowModal(id) {
                popupReceivedHtml('/admin/service/help/task-script/' + (id || 'new') + '/modal-show', 'modal-show-task-script');
            }

            function popupModal(id) {
                popupReceivedHtml('/admin/service/help/task-script/' + (id || 'new') + '/modal', 'modal-task-script');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/task-script/' + id).done(function () {
                        reload();
                    });
                });
            }

            function popupCategoryModal() {
                popupReceivedHtml('/admin/service/help/task-script/modal-category', 'modal-category');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
