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

<%--@elvariable id="pagination" type="kr.co.eicn.ippbx.util.page.Pagination<kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupSummaryResponse>"--%>
<%--@elvariable id="searchOrganizationNames" type="java.util.List"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/user/user/pickup-group/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">픽업그룹설정</div>
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
                                <th>부서선택</th>
                                <td colspan="7">
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini blue compact -select-group">
                                            <i class="search icon"></i>
                                        </button>
                                        <form:hidden path="groupCode"/>
                                        <div class="ui breadcrumb -group-name">
                                            <c:choose>
                                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                                        <span class="section">${g.htmlQuote(e)}</span>
                                                        <c:if test="${!status.last}">
                                                            <i class="right angle icon divider"></i>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <button type="button" class="ui icon button mini compact -clear-group">
                                            <i class="undo icon"></i>
                                        </button>
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
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupRegisterModal()">추가</button>
                            <button class="ui button -control-entity" data-entity="PickupGroup" style="display: none;" onclick="popupUpdateModal(getEntityId('PickupGroup'))">수정</button>
                            <button class="ui button -control-entity" data-entity="PickupGroup" style="display: none;" onclick="deleteEntity(getEntityId('PickupGroup'))">삭제</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/user/user/pickup-group" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="PickupGroup">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>당겨받기그룹명</th>
                            <th>소속</th>
                            <th>소속교환기</th>
                            <th>구성인원수</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.groupcode}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.groupname)}</td>
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
                                        <td>${g.htmlQuote(e.hostName)}</td>
                                        <td>${e.personCount}</td>
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
            function popupRegisterModal() {
                popupReceivedHtml('/admin/user/user/pickup-group/new/modal', 'modal-pickup-group');
            }

            function popupUpdateModal(groupCode) {
                popupReceivedHtml('/admin/user/user/pickup-group/' + encodeURIComponent(groupCode) + '/modal', 'modal-pickup-group', 'small');
            }

            function deleteEntity(groupCode) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/pickup-group/' + groupCode).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
