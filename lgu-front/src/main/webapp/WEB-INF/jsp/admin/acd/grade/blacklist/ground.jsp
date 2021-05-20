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
        <tags:page-menu-tab url="/admin/acd/grade/blacklist/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" commandName="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
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
                                <div class="two wide column"><label class="control-label">전화번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="gradeNumber"/></div>
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
                            <button class="ui basic button -control-entity" data-entity="BlacklistRouting" style="display: none;" onclick="popupModal(getEntityId('BlacklistRouting'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="BlacklistRouting" style="display: none;" onclick="deleteEntity(getEntityId('BlacklistRouting'))">삭제</button>
                        </div>
                        <button class="ui basic button green"onclick="popupUploadModal()">Excel 업로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="BlacklistRouting">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>전화번호</th>
                            <th>인입 방법</th>
                            <th>선택 큐</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.gradeNumber)}</td>
                                        <td>${e.type.contains('A') ? '차단' : g.htmlQuote(g.messageOf('RouteType', e.type))}</td>
                                        <c:choose>
                                            <c:when test="${e.type.contains('B')}">
                                                <td>
                                                <c:forEach var="queue" items="${queues}">
                                                        <c:if test="${queue.key.equals(e.huntNumber)}">
                                                            ${g.htmlQuote(queue.value)}(${g.htmlQuote(queue.key)})
                                                        </c:if>
                                                </c:forEach>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td></td>
                                            </c:otherwise>
                                        </c:choose>
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
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/acd/grade/blacklist/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupUploadModal() {
                popupReceivedHtml('/admin/acd/grade/blacklist/modal-upload', 'modal-upload');
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/acd/grade/blacklist/' + (seq || 'new') + '/modal', 'modal-gradelist');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/gradelist/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
