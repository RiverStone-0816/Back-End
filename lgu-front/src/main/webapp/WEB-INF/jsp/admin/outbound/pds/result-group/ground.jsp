<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/result-group/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="PdsResultGroup" style="display: none;" onclick="popupModal(getEntityId('PdsResultGroup'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="PdsResultGroup" style="display: none;" onclick="deleteEntity(getEntityId('PdsResultGroup'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="PdsResultGroup">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>상담그룹명</th>
                            <th>분배정책</th>
                            <th>교환기</th>
                            <th>사용자수</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.name)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.hanName)}</td>
                                        <td>${g.htmlQuote(g.messageOf('PDSResultGroupStrategy', e.strategy))}</td>
                                        <td>${g.htmlQuote(e.hostName)}</td>
                                        <td>${e.cnt}</td>
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
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/pds/result-group/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(name) {
                popupReceivedHtml('/admin/outbound/pds/result-group/' + (name || 'new') + '/modal', 'modal-pds-result-group');
            }

            function deleteEntity(name) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/pds-result-group/' + name).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
