<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="usingservices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<div class="content" id="counsel-nav">
    <c:if test="${serviceKind.equals('SC')}">
        <div class="counsel-trans-wrap">
            <button type="button" class="ui orange fluid button" onclick="popupArsModal()">>ARS로 돌려주기</button>
        </div>
    </c:if>

    <jsp:include page="/counsel/call-transfer"/>

    <c:forEach var="e" items="${list}" varStatus="status">
        <c:if test="${e.person != null && e.person.size() > 0}">
            <div class="accordion">
                <button class="team-title">
                    <div class="team">
                        <img src="<c:url value="/resources/images/layer-group.svg"/>" alt="group">
                        <span>${g.htmlQuote(e.groupName)}</span>
                    </div>
                        <%--<div class="arrow">
                            <i class="material-icons arrow"> keyboard_arrow_down </i>
                        </div>--%>
                </button>
                <ul class="team-list">
                    <c:forEach var="person" items="${e.person}">
                        <li class="item">
                            <div class="user">${g.htmlQuote(person.idName)}[${g.htmlQuote(person.extension)}]
                                <button class="forwarded-btn -redirect-to" data-extension="${person.extension}" title="전화돌려주기"></button>
                            </div>
                            <div class="title">
                                <span class="ui mini label -consultant-status-with-color" data-peer="${g.htmlQuote(person.peer)}" style="color: white;"></span>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>

        </c:if>
    </c:forEach>
</div>

<script>
    if (window.peerStatuses) {
        <c:forEach var="e" items="${list}" varStatus="status">
        <c:forEach var="person" items="${e.person}">
        if (!peerStatuses['${g.escapeQuote(person.peer)}']) peerStatuses['${g.escapeQuote(person.peer)}'] = {peer: '${g.escapeQuote(person.peer)}'};
        peerStatuses['${g.escapeQuote(person.peer)}'].status = ${person.paused};
        peerStatuses['${g.escapeQuote(person.peer)}'].login = ${person.isLogin == 'L'};
        </c:forEach>
        </c:forEach>
    }

    if (updatePersonStatus)
        updatePersonStatus();

    ui.find('.-redirect-to').click(function () {
        ipccCommunicator.redirect($(this).attr('data-extension'));
    });
</script>
