<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/sounds/sounds/ars/"/>
        <div class="sub-content ui container fluid">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/sounds/sounds/ars/"))}</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal()">추가</button>
                            <button class="ui button -control-entity" data-entity="SoundList" style="display: none;" onclick="deleteEntity(getEntityId('SoundList'))">삭제</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/sounds/sounds/ars/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="SoundList">
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>번호</th>
                            <th class="two wide">음원명</th>
                            <th class="two wide">업로드 파일명</th>
                            <th>음원문구</th>
                            <th class="one wide">듣기/다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.soundName)}</td>
                                        <td>${g.htmlQuote(e.soundFile)}</td>
                                        <td>${g.htmlQuote(e.comment)}</td>
                                        <td>
                                            <div class="popup-element-wrap">
                                                <button class="ui icon button mini compact -play-trigger" type="button">
                                                    <i class="volume up icon" data-value="${e.seq}"></i>
                                                </button>
                                                <div class="ui popup top right">
                                                    <div class="maudio">
                                                        <audio controls src="${apiServerUrl}/api/v1/admin/sounds/ars/${e.seq}/resource?token=${accessToken}"></audio>
                                                    </div>
                                                </div>
                                                <c:if test="${!(g.serviceKind.equals('CC') && usingServices.contains('TYPE2'))}">
                                                    <a class="ui icon button mini compact" href="${apiServerUrl}/api/v1/admin/sounds/ars/${e.seq}/resource?token=${accessToken}">
                                                        <i class="arrow down icon" data-value="${e.seq}"></i>
                                                    </a>
                                                </c:if>
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
            function popupModal() {
                popupReceivedHtml('/admin/sounds/sounds/ars/new/modal', 'modal-ars');
            }

            $('.volume').click(function () {
                restSelf.put('/api/ars/' + $(this).attr('data-value') + '/web-log?type=' + encodeURIComponent('PLAY'));
            });

            $('.down').click(function () {
                restSelf.put('/api/ars/' + $(this).attr('data-value') + '/web-log?type=' + encodeURIComponent('DOWN'));
            });

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/ars/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
