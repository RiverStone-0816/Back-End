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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/sounds/sounds/ars/"/>
        <div class="sub-content ui container fluid">
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
                                <div class="two wide column"><label class="control-label">음원명</label></div>
                                <div class="two wide column -search-type-sub-input" data-type="TEXT">
                                    <div class="ui input fluid">
                                        <form:input path="soundName"/>
                                    </div>
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
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="SoundList" style="display: none;" onclick="deleteEntity(getEntityId('SoundList'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="SoundList">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>음원명</th>
                            <th>음원파일</th>
                            <th>음원문구</th>
                            <th>듣기/다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
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
                                                        <audio controls src="/api/ars/id/${g.htmlQuote(e.seq)}/resource?mode=PLAY"></audio>
                                                    </div>
                                                </div>
                                                <a class="ui icon button mini compact" href="/api/ars/id/${g.htmlQuote(e.seq)}/resource?mode=DOWN">
                                                    <i class="arrow down icon" data-value="${e.seq}"></i>
                                                </a>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/sounds/sounds/ars/" pageForm="${search}"/>
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
