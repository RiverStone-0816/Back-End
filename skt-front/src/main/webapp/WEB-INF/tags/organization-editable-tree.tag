<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>

<%@ attribute name="e" required="true" type="kr.co.eicn.ippbx.front.model.OrganizationTree" %>
<%@ attribute name="maxLevel" required="true" type="java.lang.Integer" %>
<%@ attribute name="keyword" required="true" type="java.lang.String" %>

<li class="-item">
    <div class="header ${keyword != null && keyword.length() > 0 && e.groupName.contains(keyword) ? 'highlight' : null}" onclick="showOrganizationSummary(this, ${e.seq})">
        <span class="ui circular mini label">${e.groupLevel}</span><text>${g.htmlQuote(e.groupName)}</text>
        <c:if test="${e.groupLevel < maxLevel}">
            <button type="button" class="ui basic button mini" title="추가"
                    onclick="event.stopPropagation(); addChild($(this).closest('.-item').find('ul:first'), '${g.htmlQuote(e.groupCode)}', ${e.groupLevel + 1}); return false">추가
            </button>
        </c:if>
        <button type="button" class="ui basic button mini" title="수정"
                onclick="event.stopPropagation(); showInput($(this).closest('.header'), ${e.seq}); return false">
            수정
        </button>
        <button type="button" class="ui basic button mini" title="삭제" onclick="event.stopPropagation(); deleteEntity(${e.seq});">
            삭제
        </button>
    </div>
    <ul>
        <c:forEach var="child" items="${e.children}">
            <tags:organization-editable-tree e="${child}" maxLevel="${maxLevel}" keyword="${keyword}"/>
        </c:forEach>
        <c:forEach var="person" items="${e.persons}">
            <li class="-item">
                <div class="header"><img src="<c:url value="/resources/images/user.svg"/>" class="user-icon" alt="user">
                    <text>${g.htmlQuote(person.idName)} (${g.htmlQuote(person.id)})</text>
                </div>
            </li>
        </c:forEach>
    </ul>
</li>
