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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/email/reception-group/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic button" onclick="popupModal()">추가</button>
                        <button type="button" class="ui basic button -control-entity" data-entity="EmailReceiveGroup" style="display: none;" onclick="popupModal(getEntityId('EmailReceiveGroup'))">수정
                        </button>
                        <button type="button" class="ui basic button -control-entity" data-entity="EmailReceiveGroup" style="display: none;" onclick="deleteEntity(getEntityId('EmailReceiveGroup'))">
                            삭제
                        </button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact structured unstackable num-tbl ${list.size() > 0 ? "selectable-only" : null}" data-entity="EmailReceiveGroup">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>이메일그룹명</th>
                            <th>관련이메일서비스</th>
                            <th>멤버수</th>
                            <th>그룹멤버</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${e.groupId}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.groupName)}</td>
                                        <td>${g.htmlQuote(e.serviceName)}</td>
                                        <td>${e.memberCnt}</td>
                                        <td>
                                            <c:forEach var="member" items="${e.emailMemberLists}">[${g.htmlQuote(member.id)}(${g.htmlQuote(member.idName)})] </c:forEach>
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
            function popupModal(groupId) {
                popupReceivedHtml('/admin/email/reception-group/' + (groupId || 'new') + '/modal', 'modal-email-reception-group');
            }

            function deleteEntity(groupId) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/email-reception-group/' + groupId).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
