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

<div class="ui modal">
    <i class="close icon"></i>
    <div class="header">권한/메뉴설정</div>

    <div class="content rows scrolling" style="max-height: 400px;">
        <label class="control-label">${g.htmlQuote(entity.id)}[${g.htmlQuote(entity.idName)}], 아이디유형:${g.htmlQuote(g.messageOf('IdType', entity.idType))}</label>
        <div class="pull-right">
            <button class="ui button mini compact" onclick="resetMenu('${g.htmlQuote(entity.id)}')">초기화</button>
            <c:if test="${menus.size() >= 2}">
                <button class="ui button mini compact" onclick="popupUpdateSequenceModal('${g.htmlQuote(entity.id)}')">순서변경</button>
            </c:if>
        </div>
        <div class="auth-tree">
            <ul>
                <c:forEach var="e" items="${menus}">
                    <tags:menu-node userId="${entity.id}" entity="${e}"/>
                </c:forEach>
            </ul>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">닫기</button>
    </div>
</div>
