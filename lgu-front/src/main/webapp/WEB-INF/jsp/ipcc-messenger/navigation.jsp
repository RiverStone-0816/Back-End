<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>

<div class="inner-box bb-unset">
    <ul class="sidebar-menu">
        <%--<li><a href="<c:url value="/admin/dashboard/"/>" class="tab-indicator" title="대시보드"><i class="material-icons menu-icon"> computer </i><span>대시보드</span></a></li>--%>
        <c:forEach var="e" items="${menu.menus}">
            <c:if test="${e.viewYn == 'Y'}">
                <c:choose>
                    <c:when test="${e.actionType == 'MENU'}">
                        <li>
                            <a href="#" title="${g.htmlQuote(e.menuName)}">
                                <i class="material-icons"> ${g.htmlQuote(e.icon)} </i><span>${g.htmlQuote(e.menuName)}</span>
                                <i class="material-icons arrow"> keyboard_arrow_down </i>
                            </a>
                            <ul class="treeview-menu">
                                <c:forEach var="e2" items="${e.children}">
                                    <c:if test="${e2.viewYn == 'Y'}">
                                        <c:choose>
                                            <c:when test="${e2.actionType == 'MENU'}">
                                                <c:set var="presentLink" value="${false}"/>
                                                <c:forEach var="e3" items="${e2.children}">
                                                    <c:if test="${e3.viewYn == 'Y' && !presentLink}">
                                                        <c:set var="presentLink" value="${true}"/>
                                                        <li>
                                                            <a href="<c:url value="${e3.menuActionExeId}"/>" class="tab-indicator" title="${g.htmlQuote(e3.menuName)}">
                                                                    ${g.htmlQuote(e2.menuName)}
                                                            </a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${e2.actionType == 'PAGE'}">
                                                <li>
                                                    <a href="<c:url value="${e2.menuActionExeId}"/>" class="tab-indicator"
                                                       title="${g.htmlQuote(e2.menuName)}">${g.htmlQuote(e2.menuName)}</a>
                                                </li>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:when>
                    <c:when test="${e.actionType == 'PAGE'}">
                        <li>
                            <a href="<c:url value="${e.menuActionExeId}"/>" title="${g.htmlQuote(e.menuName)}" class="tab-indicator">
                                <i class="material-icons"> ${g.htmlQuote(e.icon)} </i><span>${g.htmlQuote(e.menuName)}</span>
                            </a>
                        </li>
                    </c:when>
                </c:choose>
            </c:if>
        </c:forEach>
    </ul>
</div>

<c:if test="${hasExtension && user.isStat == 'Y'}">
    <div class="inner-box bb-unset">
        <ul class="sidebar-menu">
            <li><a href="<c:url value="/"/>" class="tab-indicator" title="상담화면"><i class="material-icons menu-icon"> computer </i><span>상담화면</span></a></li>
        </ul>
    </div>
</c:if>

<tags:scripts>
    <script>
        $('.tab-indicator').click(function (event) {
            event.preventDefault();
            event.stopPropagation();

            const href = $(this).attr('href');

            const mode = (function () {
                try {
                    const search = href.indexOf('?') >= 0 ? href.substring(href.indexOf('?') + 1) : ' ';
                    const params = search ? JSON.parse('{"' + search.replace(/&/g, '","').replace(/=/g, '":"') + '"}', function (key, value) {
                        return key === "" ? value : decodeURIComponent(value)
                    }) : {};
                    return params.mode;
                } catch (e) {
                    return undefined;
                }
            })();

            if (window.isElectron)
                window.open(href, '_ipcc');
            else {
                if (mode === 'popup')
                    window.open(href, '', '_blank');
                else
                    window.open(href);
            }
            return false;
        });
    </script>
</tags:scripts>
