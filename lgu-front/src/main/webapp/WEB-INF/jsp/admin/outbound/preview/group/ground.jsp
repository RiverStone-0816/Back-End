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
        <tags:page-menu-tab url="/admin/outbound/preview/group/"/>
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
                                <div class="two wide column"><label class="control-label">유형</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="prvType">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${prvTypes}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">그룹명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="name"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <div class="ui basic buttons">
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="PreviewGroup" style="display: none;" onclick="popupModal(getEntityId('PreviewGroup'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="PreviewGroup" style="display: none;" onclick="deleteEntity(getEntityId('PreviewGroup'))">삭제</button>
                        </div>
                        <button class="ui basic button -control-entity" data-entity="PreviewGroup" style="display: none;" onclick="popupUploadModal(getEntityId('PreviewGroup'))">고객정보업로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="PreviewGroup">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>그룹명</th>
                            <th>유형</th>
                            <th>상담결과유형</th>
                            <th>데이터수</th>
                            <th>부서</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td>${g.htmlQuote(e.prvTypeName)}</td>
                                        <td>${g.htmlQuote(e.resultTypeName)}</td>
                                        <td>${e.totalCnt}</td>
                                        <td>
                                            <c:forEach var="e" items="${e.groupTreeNames}" varStatus="status">
                                                <span class="section">${g.htmlQuote(e)}</span>
                                                <c:if test="${!status.last}">
                                                    <i class="right angle icon divider"></i>
                                                </c:if>
                                            </c:forEach>
                                        </td>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/preview/group/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupUploadModal(seq) {
                popupReceivedHtml('/admin/outbound/preview/group/' + seq + '/modal-upload', 'modal-upload');
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/outbound/preview/group/' + (seq || 'new') + '/modal', 'modal-group');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/preview-group/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
