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
<%--@elvariable id="accessToken" type="java.lang.String"--%>


[${talk.roomStatus}]-${g.htmlQuote(talk.roomName)}
<div class="chat-body -overlay-scroll" id="talk-chat-body" data-status="${g.htmlQuote(talk.roomStatus)}" data-id="${g.htmlQuote(talk.roomId)}" data-sender-key="${g.htmlQuote(talk.senderKey)}"
     data-user-key="${g.htmlQuote(talk.userKey)}" data-user="${g.htmlQuote(talk.userId)}" data-last="${talk.lastMsgSeq}" data-room-name="${g.escapeQuote(talk.roomName)}">
    <c:forEach var="e" items="${talk.talkMsgSummaryList}">
        <c:choose>
            <c:when test="${e.sendReceive == 'SZ' || e.sendReceive == 'SG'}"></c:when>
            <c:when test="${e.sendReceive == 'SE' || e.sendReceive == 'RE'}">
                <p class="info-msg">[<fmt:formatDate value="${e.insertTime}" pattern="yyyy-MM-dd HH:mm:ss"/>] ${g.htmlQuote(e.content)}</p>
            </c:when>
            <c:when test="${e.sendReceive == 'AF' || e.sendReceive == 'AS' || e.sendReceive == 'S' || e.sendReceive == 'R'}">
                <div class="chat-item ${e.sendReceive == 'AF' || e.sendReceive == 'AS' || e.sendReceive == 'S' ? 'chat-me' : ''}">
                    <div class="wrap-content">
                        <div class="profile-image">
                            <img src="<c:url value="/resources/images/kd_2.png"/>">
                        </div>
                        <div class="txt-segment">
                            <c:set var="name" value="${e.sendReceive == 'AF' || e.sendReceive == 'AS' ||  e.sendReceive == 'S' ? e.userName : talk.customName}"/>
                            <div class="txt-time">
                                <c:if test="${name != null && name != ''}">[${g.htmlQuote(name)}]</c:if> <fmt:formatDate value="${e.insertTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </div>
                            <c:set var="url" value="${g.htmlQuote(g.addQueryString(e.content, 'token='.concat(accessToken)))}"/>
                            <c:choose>
                                <c:when test="${e.type == 'photo'}">
                                    <div class="chat">
                                        <div class="bubble">
                                            <p class="txt-chat">
                                                <img src="${url}">
                                            </p>
                                        </div>
                                    </div>
                                    <a href="${url}" target="_blank">저장하기</a>
                                </c:when>
                                <c:when test="${e.type == 'audio'}">
                                    <div class="chat">
                                        <div class="bubble">
                                            <p class="txt-chat">
                                                <audio data-src="${url}" controls></audio>
                                            </p>
                                        </div>
                                    </div>
                                    <a href="${url}" target="_blank">저장하기</a>
                                </c:when>
                                <c:when test="${e.type == 'file'}">
                                    <div class="chat">
                                        <div class="bubble">
                                            <p class="txt-chat">
                                                <a href="${url}" target="_blank">${url}</a>
                                            </p>
                                        </div>
                                    </div>
                                    <a href="${url}" target="_blank">저장하기</a>
                                </c:when>
                                <c:otherwise>
                                    <div class="chat">
                                        <div class="bubble">
                                            <pre class="txt-chat">${g.htmlQuote(e.content)}</pre>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                unknown sendReceive: ${e.sendReceive}
                <br/>
                ${g.htmlQuote(e.content)}
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>

<script>
    ui.find('.chat-body').scroll({y: "100%"}, 100);

    $('.-assignUnassignedRoomToMe').hide();
    $('.-assignAssignedRoomToMe').hide();
    $('.-deleteRoom').hide();
    $('.-downRoom').hide();

    const talkUser = ui.attr('data-user');
    const status = ui.attr('data-status');
    if (status === 'E') {
        $('.-deleteRoom').show();
    } else if (!talkUser) {
        $('.-assignUnassignedRoomToMe').show();
    } else if (talkUser !== userId) {
        $('.-assignAssignedRoomToMe').show();
    } else {
        $('.-downRoom').show();
    }
</script>
