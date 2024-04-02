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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/research/item/"/>
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
                                <div class="two wide column"><label class="control-label">문항제목</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="itemName"/>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">질문</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="word"/>
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
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span>건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>문항제목</th>
                            <th>음원유형</th>
                            <th>소속</th>
                            <th class="two wide">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.itemName)}</td>

                                        <td>
                                            <div class="popup-element-wrap">
                                                    ${g.htmlQuote(g.messageOf('ResearchItemSoundKind', e.soundKind))}
                                                <c:if test="${e.soundKind == 'S'}">
                                                    <button type="button" class="ui button mini compact -play-trigger">음원듣기</button>
                                                    <div class="ui popup top right">
                                                        <div class="maudio">
                                                            <audio controls src="${pageContext.request.contextPath}/api/ars/id/${e.seq}/resource"></audio>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </td>

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

                                        <td rowspan="2">
                                            <div class="ui form">
                                                <button type="button" class="ui button mini compact" onclick="popupModal(${e.seq})">수정</button>
                                                <button type="button" class="ui button mini compact" onclick="deleteEntity(${e.seq})">삭제</button>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="4">
                                            <div class="ui comments">
                                                <div class="comment">
                                                    <div class="content">
                                                        <a class="author">질문</a>
                                                        <div class="text">
                                                            <p>${g.htmlQuote(e.word)}</p>
                                                        </div>
                                                    </div>
                                                    <c:if test="${e.answers != null && e.answers.size() > 0}">
                                                        <div class="comments">
                                                            <div class="comment">
                                                                <div class="content">
                                                                    <a class="author">답변</a>
                                                                    <div class="text">
                                                                        <c:forEach var="answer" items="${e.answers}" varStatus="aStatus">
                                                                            <span class="ui label blue small">답변${aStatus.index + 1}</span> ${g.htmlQuote(answer)}
                                                                        </c:forEach>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
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
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/research/item/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(seq) {
                <c:choose>
                <c:when test="${serviceKind.equals('CC') && pagination.rows.size() > 1}">
                    alert("더이상 추가할 수 없습니다.");
                </c:when>
                <c:when test="${serviceKind.equals('CC') && pagination.rows.size() < 2}">
                    popupReceivedHtml('/admin/outbound/research/item/' + (seq || 'new') + '/modal', 'modal-research-item');
                </c:when>
                <c:otherwise>
                    popupReceivedHtml('/admin/outbound/research/item/' + (seq || 'new') + '/modal', 'modal-research-item');
                </c:otherwise>
                </c:choose>
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/research-item/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
