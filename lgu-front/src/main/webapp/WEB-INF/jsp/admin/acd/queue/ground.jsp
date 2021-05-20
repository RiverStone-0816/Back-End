<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/acd/queue/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" commandName="search" method="get" class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic button" onclick="popupModal()">추가</button>
                        <button type="button" class="ui basic button -control-entity" style="display: none;" data-entity="Queue" onclick="popupModal(getEntityId('Queue'))">수정</button>
                        <button type="button" class="ui basic button -control-entity" style="display: none;" data-entity="Queue" onclick="deleteEntity(getEntityId('Queue'))">삭제</button>
                            <%--<button type="button" class="ui basic grey button -control-entity" style="display: none;" data-entity="Queue" onclick="popupBlendingModeModal(getEntityId('Queue'))">블랜딩</button>--%>
                    </div>
                </div>

                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="Queue">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>큐(그룹)명</th>
                            <th>예비큐</th>
                            <th>큐번호</th>
                            <th>분배정책</th>
                                <%--<th>블랜딩기능</th>--%>
                            <th>사용자수</th>
                            <th>소속</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.name)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.hanName)}(${g.htmlQuote(e.name)})</td>
                                        <td>${g.htmlQuote(e.subGroupName)}</td>
                                        <td>${g.htmlQuote(e.number)}</td>
                                        <td>${g.htmlQuote(g.messageOf('CallDistributionStrategy', e.strategy))}</td>
                                            <%--<td>${g.htmlQuote(g.messageOf('BlendingMode', e.blendingMode))}</td>--%>
                                        <td>${e.personCount}</td>
                                        <td class="five wide">
                                            <div class="ui breadcrumb">
                                                <c:forEach var="o" items="${e.companyTrees}" varStatus="oStatus">
                                                    <span class="section">${o.groupName}</span>
                                                    <c:if test="${!oStatus.last}">
                                                        <i class="right angle icon divider"></i>
                                                    </c:if>
                                                </c:forEach>
                                            </div>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/acd/queue/" pageForm="${search}"/>
                </div>
            </form:form>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupBlendingModeModal(name) {
                popupReceivedHtml('/admin/acd/queue/' + encodeURIComponent(name) + '/modal-blending', 'modal-blending');
            }

            function popupModal(name) {
                popupReceivedHtml('/admin/acd/queue/' + encodeURIComponent(name || 'new') + '/modal', 'modal-queue');
            }

            function deleteEntity(name) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/queue/' + encodeURIComponent(name)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
