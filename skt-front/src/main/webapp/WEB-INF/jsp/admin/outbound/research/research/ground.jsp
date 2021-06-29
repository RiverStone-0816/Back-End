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
        <tags:page-menu-tab url="/admin/outbound/research/research/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">설문지생성</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <input type="checkbox" name="newsletter" id="_newsletter" checked="" tabindex="0" class="hidden"><label for="_newsletter">검색옵션 전체보기</label>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>
                                    설문명
                                </th>
                                <td colspan="7">
                                    <div class="ui form">
                                        <form:input path="researchName"/>
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
                        <h3 class="panel-total-count">전체 <span class="text-primary">${pagination.totalCount}</span>건</h3>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal()">추가</button>
                            <button class="ui button -control-entity" data-entity="Research" style="display: none;" onclick="popupModal(getEntityId('Research'))">수정</button>
                            <button class="ui button -control-entity" data-entity="Research" style="display: none;" onclick="deleteEntity(getEntityId('Research'))">삭제</button>
                            <button class="ui button -control-entity" data-entity="Research" style="display: none;" onclick="popupScenarioModal(getEntityId('Research'))">설문시나리오설정</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/research/research/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured border-top compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="Research">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>설문명</th>
                            <th>시나리오</th>
                            <th>등록일</th>
                            <th>부서</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.researchId}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.researchName)}</td>
                                        <td>${e.haveTree == 'Y' ? '있음' : '없음'}</td>
                                        <td><fmt:formatDate value="${e.regdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>
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
                                    <td colspan="5" class="null-data">조회된 데이터가 없습니다.</td>
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
            function popupScenarioModal(researchId) {
                popupReceivedHtml('/admin/outbound/research/research/' + (researchId) + '/modal-scenario', 'modal-research-scenario');
            }

            function popupModal(researchId) {
                popupReceivedHtml('/admin/outbound/research/research/' + (researchId || 'new') + '/modal', 'modal-research');
            }

            function deleteEntity(researchId) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/research/' + researchId).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
