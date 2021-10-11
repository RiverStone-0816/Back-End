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

<div class="sub-content ui container fluid unstackable" id="modal-search-task-script-body">
    <form:form modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
               action="${pageContext.request.contextPath}/admin/service/help/task-script/modal-search-body"
               data-target="#modal-search-task-script-body">
        <form:hidden path="categoryId"/>
        <div class="panel-heading">
            <div class="pull-left">
                검색
            </div>
            <div class="pull-right">
                <div class="ui slider checkbox">
                    <label>접기/펴기</label>
                    <input type="checkbox" name="newsletter" id="_newsletter">
                </div>
                <div class="btn-wrap">
                    <button type="submit" class="ui brand basic button">검색</button>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <div class="search-area">
                <div class="ui grid">
                    <div class="row">
                        <div class="two wide column"><label class="control-label">등록일</label></div>
                        <div class="fourteen wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                        <div class="two wide column"><label class="control-label">제목</label></div>
                        <div class="six wide column">
                            <div class="ui input fluid">
                                <form:input path="title"/>
                            </div>
                        </div>
                        <div class="two wide column"><label class="control-label">태그</label></div>
                        <div class="six wide column">
                            <div class="ui input fluid">
                                <form:input path="tag"/>
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
        </div>
        <div class="panel-body">
            <div class="ui secondary  menu">
                <a href="javascript: changeCategory()" class="item ${search.categoryId == null ? 'active' : null}">전체</a>
                <c:forEach var="e" items="${categories}">
                    <a href="javascript: changeCategory(${e.key})" class="item ${search.categoryId == e.key ? 'active' : null}">${g.htmlQuote(e.value)}</a>
                </c:forEach>
            </div>
            <table class="ui celled table compact unstackable fixed ${pagination.rows.size() > 0 ? 'selectable-only' : null}" data-entity="TaskScript">
                <thead>
                <tr>
                    <th class="one wide">번호</th>
                    <th class="eight wide">제목</th>
                    <th class="four wide">태그</th>
                    <th class="two wide">등록일</th>
                    <th class="one wide">내용보기</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${pagination.rows.size() > 0}">
                        <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                            <tr data-id="${e.id}">
                                <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                <td>${g.htmlQuote(e.title)}</td>
                                <td>
                                    <c:forEach var="tag" items="${e.tag.split(',')}">
                                        <div class="ui tiny label">${g.htmlQuote(tag)}</div>
                                    </c:forEach>
                                </td>
                                <td><fmt:formatDate value="${e.createdAt}" pattern="yyyy-MM-dd"/></td>
                                <td>
                                    <button type="button" class="ui icon compact mini button view-content-btn"
                                            onclick="$(this).closest('tr').next().toggle(); $(this).children('.icon').toggleClass('trans-rotate-180')">
                                        <i class="angle down icon"></i>
                                    </button>
                                </td>
                            </tr>
                            <tr class="list-view">
                                <td colspan="5">
                                    <div class="ui grid mg-1em align-left">
                                        <div class="row">
                                            <div class="two wide column">
                                                <label class="control-label">내용</label>
                                            </div>
                                            <div class="fourteen wide column">
                                                <div class="ws-prewrap">${g.htmlQuote(e.content)}</div>
                                            </div>
                                        </div>
                                        <c:if test="${e.imageFileInfos.size() > 0}">
                                            <div class="row">
                                                <div class="two wide column">
                                                    <label class="control-label">이미지</label>
                                                </div>
                                                <div class="fourteen wide column">
                                                    <ul class="board-img-ul">
                                                        <c:forEach var="file" items="${e.imageFileInfos}">
                                                            <li class="thumbnail-item" onclick="imgViewPopup()">
                                                                <img src="${apiServerUrl}/api/v1/admin/help/notice/${e.id}/specific-file-resource?token=${accessToken}">
                                                            </li>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </c:if>
                                        <c:if test="${e.nonImageFileInfos.size() > 0}">
                                            <div class="row">
                                                <div class="two wide column">
                                                    <label class="control-label">첨부파일</label>
                                                </div>
                                                <div class="fourteen wide column">
                                                    <div class="board-con-inner">
                                                        <div class="ui list filelist">
                                                            <c:forEach var="file" items="${e.nonImageFileInfos}">
                                                                <div class="item">
                                                                    <i class="file alternate outline icon"></i>
                                                                    <div class="content">
                                                                        <a href="${apiServerUrl}/api/v1/admin/help/notice/${e.id}/specific-file-resource?token=${accessToken}"
                                                                           target="_blank">${g.htmlQuote(e.originalName)}</a>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </td>
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
        <div class="panel-footer">
            <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/service/help/task-script/" pageForm="${search}"
                             ajaxLoaderEnable="${true}" ajaxLoaderTarget="#modal-search-task-script-body"/>
        </div>
    </div>
</div>

<script>
    function changeCategory(categoryId) {
        const searchForm = $('#modal-search-task-script-body form');
        searchForm.find('[name=categoryId]').val(categoryId != null ? categoryId : '');
        searchForm.submit();
    }

    function popupShowModal(id) {
        popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/' + (id || 'new') + '/modal-show', 'modal-show-task-script');
    }

    function popupModal(id) {
        popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/' + (id || 'new') + '/modal', 'modal-task-script');
    }

    function deleteEntity(id) {
        confirm('정말 삭제하시겠습니까?').done(function () {
            restSelf.delete('/api/task-script/' + id).done(function () {
                reload();
            });
        });
    }

    function popupCategoryModal() {
        popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/modal-category', 'modal-task-script-category');
    }
</script>
