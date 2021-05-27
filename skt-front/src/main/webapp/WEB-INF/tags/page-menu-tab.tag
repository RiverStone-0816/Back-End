<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>

<%@ attribute name="url" required="true" type="java.lang.String" %>

<c:set var="menuTree" value="${menu.findMenuByUrl(url)}"/>
<c:set var="parent" value="${menuTree.size() > 1 ? menuTree[menuTree.size() - 2] : null}"/>
<c:set var="menu" value="${menuTree.size() > 0 ? menuTree[menuTree.size() - 1] : null}"/>
<c:set var="siblings" value="${parent != null ? parent.children : []}"/>

<div class="menu-tab">
    <div class="inner">
        <ul>
            <c:forEach var="e" items="${siblings}">
                <c:if test="${e.viewYn == 'Y'}">
                    <li><a href="<c:url value="${e.menuActionExeId}"/>" class="${e.menuActionExeId == url ? 'tab-on' : ''} tab-indicator">${g.htmlQuote(e.menuName)}</a></li>
                </c:if>
            </c:forEach>
        </ul>
        <div class="ui breadcrumb">
            <c:forEach var="e" items="${menuTree}" varStatus="status">
                <span class="${status.last ? 'active' : ''} section">${g.htmlQuote(e.menuName)}</span>
                <c:if test="${!status.last}">
                    <i class="right angle icon divider"></i>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>
