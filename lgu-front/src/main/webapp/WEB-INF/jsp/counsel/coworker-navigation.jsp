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

<ul class="side-organization-ul" id="counsel-nav">
<%--        <li>--%>
<%--            <a class="item active" data-tab="talk-list-type-MY">--%>
<%--                <text>상담중 (<span></span>)</text>--%>
<%--            </a>--%>
<%--            <a class="item" data-tab="talk-list-type-TOT">--%>
<%--                <text>비접수 (<span></span>)</text>--%>
<%--            </a>--%>
<%--        </li>--%>
    <c:forEach var="e" items="${list}" varStatus="status">
        <c:if test="${e.person != null && e.person.size() > 0}">
            <li class="consulting-accordion active">
                <div class="consulting-accordion-label team">
                    <div class="left">
                        <i class="folder open icon"></i>
                        <span class="team-name">${e.groupName}</span>
                    </div>
                    <div class="right">
                        <i class="material-icons arrow">keyboard_arrow_down</i>
                    </div>
                </div>
                <ul class="treeview-menu consulting-accordion-content">
                    <c:forEach var="person" items="${e.person}">
                        <li class="team-item">
                            <div>
                                <i class="user outline icon"></i>
                                <span class="user">${g.htmlQuote(person.idName)}[${g.htmlQuote(person.extension)}]</span>
                                <button type="button" class="ui icon button mini compact -redirect-to" data-extension="${person.extension}" title="전화돌려주기">
                                    <i class="share icon"></i>
                                </button>
                            </div>
                            <div>
                                <span class="ui mini label -consultant-status-with-color" data-peer="${g.htmlQuote(person.peer)}"></span>
                            </div>
                            <c:if test="${usingservices.contains('IM')}">
                                <button type="button" class="ui icon button mini compact -note-send" title="쪽지발송" onclick="noteSendPopup('${g.escapeQuote(person.extension)}','${g.escapeQuote(person.idName)}')">
                                    <i class="comment alternate icon"></i>
                                </button>
                            </c:if>
                        </li>
                    </c:forEach>
                    <li class="team-item active">
                        <div>
                            <i class="user outline icon"></i>
                            <span class="user">상담사3[0989]</span>
                            <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0989" title="전화돌려주기">
                                <i class="share icon"></i>
                            </button>
                        </div>
                        <div>
                            <span class="ui mini label -consultant-status-with-color teal" data-peer="78390989">대기</span>
                        </div>
                    </li>
                </ul>
            </li>
        </c:if>
    </c:forEach>
</ul>

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
