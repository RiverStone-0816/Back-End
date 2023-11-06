<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/chatbot/info/block/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div class="pull-left">검색</div>
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
                                <div class="two wide column"><label class="control-label">챗봇</label></div>
                                <div class="three wide column">
                                    <div class="ui form">
                                        <form:select path="chatbotId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${talkServiceList}"/>
                                        </form:select>
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
                        <button class="ui basic button -control-entity" data-entity="ChatbotBlock" style="display: none;" onclick="popupModal(getEntityId('ChatbotBlock'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="ChatbotBlock" style="display: none;" onclick="deleteEntity(getEntityId('ChatbotBlock'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="ChatbotBlock">
                        <tr>
                            <th>번호</th>
                            <th>카카오챗봇</th>
                            <th>블럭명</th>
                            <th>블럭아이디</th>
                            <th>답변연동유형</th>
                            <th>답변연동URL</th>
                            <th>답변연동파라미터</th>
                            <th>이벤트명</th>
                            <th>사용여부</th>
                        </tr>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.botName)}</td>
                                        <td>${g.htmlQuote(e.blockName)}</td>
                                        <td>${g.htmlQuote(e.blockId)}</td>
                                        <td>${g.htmlQuote(e.responseType)}</td>
                                        <td>${g.htmlQuote(e.responseGetUrl)}</td>
                                        <td>${g.htmlQuote(e.responseParamNames)}</td>
                                        <td>${g.htmlQuote(e.eventName)}</td>
                                        <td>${g.htmlQuote(e.useYn == "Y" ? "사용중" : "비사용")}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9">결과가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/chatbot/info/block" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>
    <tags:scripts>
        <script>
            function popupModal(seq) {
                popupReceivedHtml('/admin/chatbot/info/block/' + encodeURIComponent(seq || 'new') + '/modal', 'modal-block');
            }

            function deleteEntity(seq) {
                if (!seq) {
                    alert("삭제할 블록을 선택하세요");
                    return
                }

                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/chatbot/info/block/' + encodeURIComponent(seq)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>