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
        <tags:page-menu-tab url="/admin/user/tel/random-rid/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">발신번호설정</div>
                        <span class="ui black circular mini label help-info-btn">?</span>
                        <div class="ui flowing popup top left transition hidden help-info">
                            <dl>
                                <dd class="font-weight-bold">사용방법</dd>
                                <dd>1. 전체 발신표시번호를 사용할때 : 99+고객전화번호</dd>
                                <dd>2. 팀내부 발신표시번호를 사용할때 : 98+단축1자리+고객전화번호</dd>
                                <dd>3. 개인 발신표시번호를 사용할때 : 97 + 단축1자리+고객전화번호</dd>
                            </dl>
                        </div>
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
                                <th>부서조회</th>
                                <td>
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini brand compact -select-group">
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
                                                    <span class="section">부서를 선택해 주세요.</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <button type="button" class="ui icon button mini compact -clear-group">
                                            <i class="undo icon"></i>
                                        </button>
                                    </div>
                                </td>
                                <th>번호</th>
                                <td>
                                    <div class="ui form"><form:input path="number"/></div>
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
                        <h3 class="panel-total-count">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal()">추가</button>
                            <button class="ui button -control-entity" data-entity="RandomRid" style="display: none;" onclick="popupModal(getEntityId('RandomRid'))">수정</button>
                            <button class="ui button -control-entity" data-entity="RandomRid" style="display: none;" onclick="deleteEntity(getEntityId('RandomRid'))">삭제</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/user/tel/random-rid/" pageForm="${search}"/>
                    </div>
                </div>

                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="RandomRid">
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>No.</th>
                            <th>번호</th>
                            <th>단축번호</th>
                            <th>부서</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.number)}</td>
                                        <td>${g.htmlQuote(e.shortNum)}</td>
                                        <td class="five wide">
                                            <c:forEach var="tree" items="${e.companyTrees}" varStatus="treeStatus">
                                                <span class="section">${g.htmlQuote(tree.groupName)}</span>
                                                <c:if test="${!treeStatus.last}">
                                                    <i class="right angle icon divider"></i>
                                                </c:if>
                                            </c:forEach>
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
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>

            $('.help-info-btn').popup({
                popup: $('.help-info'),
                on: 'hover'
            });

            function popupModal(seq) {
                popupReceivedHtml('/admin/user/tel/random-rid/' + (seq || 'new') + '/modal', 'modal-random-rid');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/random-rid/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
