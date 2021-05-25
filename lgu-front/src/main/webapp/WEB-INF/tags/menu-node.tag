<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<%@ attribute name="userId" required="true" type="java.lang.String" %>
<%@ attribute name="entity" required="true" type="kr.co.eicn.ippbx.model.dto.configdb.UserMenuCompanyResponse" %>

<li class="-menu ${entity.actionType == 'MENU' ? 'menu' : ''} ${entity.viewYn == 'Y' ? '' : 'hidden'}" data-id="${g.htmlQuote(entity.menuCode)}">
    <p>
        <text class="-menu-name">${g.htmlQuote(entity.menuName)}</text>
        <text class="ui left pointing basic label mini -menu-group-level-auth"
              style="${entity.actionType == 'PAGE' && entity.groupLevelAuthYn != null && entity.groupLevelAuthYn != '' ? '' : 'display: none;'}">
            ${g.htmlQuote(g.messageOf('GroupLevelAuth', entity.groupLevelAuthYn))}
            <c:if test="${entity.groupLevelAuthYn == 'G'}">
                (${g.htmlQuote(entity.groupName)})
            </c:if>
        </text>
        <text class="ui buttons">
            <button type="button" class="ui button mini compact" onclick="popupMenuAttributesModal('${g.htmlQuote(userId)}', '${g.htmlQuote(entity.menuCode)}')">변경</button>
            <c:if test="${entity.actionType == 'MENU' && entity.children.size() >= 2}">
                <button type="button" class="ui button mini compact" onclick="popupUpdateSequenceModal('${g.htmlQuote(userId)}', '${g.htmlQuote(entity.menuCode)}')">순서변경</button>
            </c:if>
        </text>
    </p>
    <c:if test="${entity.actionType == 'MENU'}">
        <ul class="-menu-container">
            <c:forEach var="child" items="${entity.children}">
                <tags:menu-node userId="${userId}" entity="${child}"/>
            </c:forEach>
        </ul>
    </c:if>
</li>
