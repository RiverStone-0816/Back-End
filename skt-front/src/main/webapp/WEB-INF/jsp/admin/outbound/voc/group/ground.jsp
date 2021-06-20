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
        <tags:page-menu-tab url="/admin/outbound/voc/group/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">VOC설정</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="VocGroup" style="display: none;" onclick="popupModal(getEntityId('VocGroup'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="VocGroup" style="display: none;" onclick="deleteEntity(getEntityId('VocGroup'))">삭제</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/voc/group/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable ${pagination.rows.size() > 0 ? 'selectable-only' : null}" data-entity="VocGroup">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>VOC/해피콜명</th>
                            <th>진행여부/기한</th>
                            <th>진행종류(설문명)</th>
                            <th>진행자</th>
                            <th>만든날짜</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.vocGroupName)}</td>
                                        <td>
                                                ${g.htmlQuote(g.messageOf('ProcessKind', e.processKind))}
                                            <c:if test="${e.processKind == 'T'}">
                                                (${g.dateFormat(e.startTerm)} ~ ${g.dateFormat(e.endTerm)})
                                            </c:if>
                                        </td>
                                        <td>
                                                ${g.htmlQuote(e.isArsSms)}
                                            <c:if test="${e.isArsSms == 'ARS'}">
                                                (${g.htmlQuote(e.research.researchName)})
                                            </c:if>
                                        </td>
                                        <td>${g.htmlQuote(g.messageOf('VocGroupSender', e.sender))}</td>
                                        <td><fmt:formatDate value="${e.insertDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
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
            function popupModal(seq) {
                popupReceivedHtml('/admin/outbound/voc/group/' + (seq || 'new') + '/modal', 'modal-voc-group');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/voc-group/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
