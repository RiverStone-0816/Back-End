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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/sounds/sounds/ring-back-tone/"/>
        <div class="sub-content ui container fluid">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">11</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="Moh" style="display: none;" onclick="deleteEntity(getEntityId('Moh'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="Moh">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>음원명</th>
                            <th>음원파일</th>
                            <th>듣기/다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.category)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.mohName)}</td>
                                        <td>${g.htmlQuote(e.fileName)}</td>
                                        <td>
                                            <div class="popup-element-wrap">
                                                <button class="ui icon button mini compact -play-trigger" type="button">
                                                    <i class="volume up icon" data-value="${e.category}"></i>
                                                </button>
                                                <div class="ui popup top right">
                                                    <div class="maudio">
                                                        <audio controls src="${apiServerUrl}/api/v1/admin/sounds/ring-back-tone/${g.htmlQuote(e.category)}/resource?type=PLAY&token=${accessToken}"></audio>
                                                    </div>
                                                </div>
                                                <a class="ui icon button mini compact"
                                                   href="${apiServerUrl}/api/v1/admin/sounds/ring-back-tone/${g.htmlQuote(e.category)}/resource?type=DOWN&token=${accessToken}">
                                                    <i class="arrow down icon" data-value="${e.category}"></i>
                                                </a>
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
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/sounds/sounds/ring-back-tone/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal() {
                popupReceivedHtml('/admin/sounds/sounds/ring-back-tone/new/modal', 'modal-ring-back-tone');
            }

            $('.volume').click(function () {
                restSelf.put('/api/ars/' + $(this).attr('data-value') + '/web-log?type=' + encodeURIComponent('PLAY'));
            });

            $('.down').click(function () {
                restSelf.put('/api/ars/' + $(this).attr('data-value') + '/web-log?type=' + encodeURIComponent('DOWN'));
            })

            function deleteEntity(category) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/ring-back-tone/' + encodeURIComponent(category)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
