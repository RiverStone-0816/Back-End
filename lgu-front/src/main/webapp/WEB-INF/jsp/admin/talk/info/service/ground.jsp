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
        <tags:page-menu-tab url="/admin/talk/info/service/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic button" onclick="popupModal()">추가</button>
                        <button type="button" class="ui basic button -control-entity" data-entity="TalkService" style="display: none;" onclick="popupModal(getEntityId('TalkService'))">수정</button>
                        <button type="button" class="ui basic button -control-entity" data-entity="TalkService" style="display: none;" onclick="deleteEntity(getEntityId('TalkService'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="TalkService">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>상담톡서비스명</th>
                            <th>상담톡아이디</th>
                            <th>상담톡키</th>
                            <th>상담창활성화</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.kakaoServiceName)}</td>
                                        <td>${g.htmlQuote(e.kakaoServiceId)}</td>
                                        <td>${g.htmlQuote(e.senderKey)}</td>
                                        <td>${e.isChattEnable == 'Y' ? '활성화' : '비활성화'}</td>
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
            function popupModal(seq) {
                popupReceivedHtml('/admin/talk/info/service/' + (seq || 'new') + '/modal', 'modal-talk-service');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/talk-service/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
