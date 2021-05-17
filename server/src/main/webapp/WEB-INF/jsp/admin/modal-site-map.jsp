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
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>

<div class="ui modal custom" id="modal-site-map">
    <i class="close icon"></i>
    <div class="header">사이트맵</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="six column row">
                <c:forEach var="e" items="${menu.menus}">
                    <c:if test="${e.viewYn == 'Y'}">
                        <c:choose>
                            <c:when test="${e.actionType == 'MENU'}">
                                <div class="column">
                                    <div class="ui segments">
                                        <div class="ui segment secondary"><h4 class="ui header">${g.htmlQuote(e.menuName)}</h4></div>
                                        <c:forEach var="e2" items="${e.children}">
                                            <c:if test="${e2.viewYn == 'Y'}">
                                                <c:choose>
                                                    <c:when test="${e2.actionType == 'MENU'}">
                                                        <c:set var="presentLink" value="${false}"/>
                                                        <c:forEach var="e3" items="${e2.children}">
                                                            <c:if test="${e3.viewYn == 'Y' && !presentLink}">
                                                                <c:set var="presentLink" value="${true}"/>
                                                                <div class="ui segment">
                                                                    <a href="<c:url value="${e2.children.get(0).menuActionExeId}"/>" class="tab-indicator"
                                                                       title="${g.htmlQuote(e2.children.get(0).menuName)}">
                                                                            ${g.htmlQuote(e2.menuName)}
                                                                    </a>
                                                                </div>
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:when test="${e2.actionType == 'PAGE'}">
                                                        <div class="ui segment">
                                                            <a href="<c:url value="${e2.menuActionExeId}"/>" class="tab-indicator" title="${g.htmlQuote(e2.menuName)}">
                                                                    ${g.htmlQuote(e2.menuName)}
                                                            </a>
                                                        </div>
                                                    </c:when>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${e.actionType == 'PAGE'}">
                                <div class="column">
                                    <div class="ui segments">
                                        <div class="ui segment secondary"><h4 class="ui header">${g.htmlQuote(e.menuName)}</h4></div>
                                        <div class="ui segment">
                                            <a href="<c:url value="${e.menuActionExeId}"/>" class="tab-indicator" title="${g.htmlQuote(e.menuName)}">
                                                    ${g.htmlQuote(e.menuName)}
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        function sitemapOpen() {
            $('#modal-site-map').modalShow();
        }

        $('.tab-indicator').click(function () {
            $('#modal-site-map').modal('hide');
        });
    </script>
</tags:scripts>