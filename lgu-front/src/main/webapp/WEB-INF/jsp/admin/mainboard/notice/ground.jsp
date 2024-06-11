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
        <tags:page-menu-tab url="/admin/main-board/notice/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span>건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button -control-entity" data-entity="Notice" style="display: none;" onclick="popupShowModal(getEntityId('Notice'))">보기</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable fixed ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="Notice">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th class="nine wide">제목</th>
                            <th class="two wide">등록일</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${topFix.size() > 0}">
                            <c:forEach var="top" items="${topFix}" varStatus="i">
                                <tr data-id="${top.id}">
                                    <td>고정</td>
                                    <td>${g.htmlQuote(top.title)}</td>
                                    <td>${g.dateFormat(top.createdAt)}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.title)}</td>
                                        <td>${g.dateFormat(e.createdAt)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/main-board/notice/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupShowModal(id) {
                popupReceivedHtml('/admin/main-board/notice/' + id, 'modal-show-notice');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
