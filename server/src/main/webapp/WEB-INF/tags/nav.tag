<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>

<aside class="side-bar manage-side">
    <button class="nav-bar"><i class="material-icons arrow"> keyboard_arrow_left </i></button>
    <div class="sidebar-menu-container">
        <ul class="sidebar-menu">
            <li><a href="<c:url value="/admin/dashboard/"/>" class="tab-indicator" title="대시보드"><i class="material-icons menu-icon"> computer </i><span>대시보드</span></a></li>

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
                                                        <a href="<c:url value="${e2.menuActionExeId}"/>" class="tab-indicator" title="${g.htmlQuote(e2.menuName)}">${g.htmlQuote(e2.menuName)}</a>
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
</aside>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:if test="${hasExtension}">
    <aside class="side-bar consulting-side">
        <button class="nav-bar"><i class="material-icons arrow"> keyboard_arrow_left </i></button>
        <div class="sidebar-menu-container">
            <ul class="sidebar-menu" id="counsel-nav"></ul>
        </div>
        <div class="box-container">
            <div class="box">
                <label class="control-label">외부링크</label>
                <div class="ui list" id="outer-link-list"></div>
            </div>
            <div class="box call-transform">
                <label class="control-label">대표번호/헌트번호돌려주기</label>
                <jsp:include page="/counsel/call-transfer"/>
            </div>
            <div class="box">
                <label class="control-label">처리현황(30초마다 정보 갱신)</label>
                <jsp:include page="/counsel/current-status"/>
            </div>
        </div>
    </aside>
</c:if>