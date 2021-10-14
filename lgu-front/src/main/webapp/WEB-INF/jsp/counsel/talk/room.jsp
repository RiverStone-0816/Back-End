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


<div class="room" id="talk-room" data-status="${g.htmlQuote(talk.roomStatus)}" data-id="${g.htmlQuote(talk.roomId)}" data-sender-key="${g.htmlQuote(talk.senderKey)}"
     data-user-key="${g.htmlQuote(talk.userKey)}" data-user="${g.htmlQuote(talk.userId)}">
    <div class="chat-header dp-flex justify-content-space-between align-items-center">
        <span><img src="<c:url value="/resources/images/kakao-icon.png"/>" class="channel-icon"> [${talk.roomStatus}]-${g.htmlQuote(talk.roomName)}</span>
        <button type="button" class="ui button tiny compact button-sideview" onclick="sideViewRoomModal()"></button>
    </div>
    <div class="chat-body" data-last="${talk.lastMsgSeq}">
        <c:forEach var="e" items="${talk.talkMsgSummaryList}">
            <c:choose>
                <c:when test="${e.sendReceive == 'SZ' || e.sendReceive == 'SG'}"></c:when>
                <c:when test="${e.sendReceive == 'SE' || e.sendReceive == 'RE'}">
                    <p class="info-msg">[<fmt:formatDate value="${e.insertTime}" pattern="yyyy-MM-dd HH:mm:ss"/>] ${g.htmlQuote(e.content)}</p>
                </c:when>
                <c:when test="${e.sendReceive == 'AF' || e.sendReceive == 'AS' || e.sendReceive == 'S' || e.sendReceive == 'R'}">
                    <div class="chat-item ${e.sendReceive == 'AF' || e.sendReceive == 'AS' || e.sendReceive == 'S' ? 'chat-me' : ''}">
                        <div class="wrap-content">
                            <c:set var="name" value="${e.sendReceive == 'AF' || e.sendReceive == 'AS' ||  e.sendReceive == 'S' ? e.userName : talk.customName}"/>
                            <div class="txt-time">
                                <c:if test="${name != null && name != ''}">[${g.htmlQuote(name)}]</c:if> <fmt:formatDate value="${e.insertTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </div>
                            <c:set var="url" value="${g.htmlQuote(g.addQueryString(e.content, 'token='.concat(accessToken)))}"/>
                            <c:choose>
                                <c:when test="${e.type == 'photo'}">
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">
                                                <img src="${url}" class="cursor-pointer" onclick="viewImg()">
                                            </div>
                                            <a href="${url}" target="_blank" class="save-txt">저장하기</a>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${e.type == 'audio'}">
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">
                                                <audio controls src="${url}"></audio>
                                            </div>
                                            <a href="${url}" target="_blank" class="save-txt">저장하기</a>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${e.type == 'file'}">
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">
                                                <a href="${url}" target="_blank">${url}</a>
                                            </div>
                                            <a href="${url}" target="_blank" class="save-txt">저장하기</a>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">
                                                <p>${g.htmlQuote(e.content)}</p>
                                            </div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="chat-item ">
                        <div class="wrap-content">
                            <div class="txt-time">
                                [미등록고객] 2021-10-10 11:01:29
                            </div>
                            <div class="chat">
                                <div class="bubble">
                                    <div class="txt_chat"><p>남이보낸메시지</p></div>
                                </div>
                                <div class="chat-layer">
                                    <div class="buttons">
                                        <button class="button-reply" data-inverted="" data-tooltip="답장 달기" data-position="top center" onclick="viewToReply()"></button>
                                        <button class="button-template" data-inverted="" data-tooltip="템플릿 만들기" data-position="top center" onclick="templateModal()"></button>
                                        <button class="button-knowledge" data-inverted="" data-tooltip="지식관리 호출" data-position="top center" onclick="knowledgeManagementModal()"></button>
                                        <button class="button-sideview" data-inverted="" data-tooltip="사이드뷰" data-position="top center"></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="chat-item chat-me">
                        <div class="wrap-content">
                            <div class="txt-time">
                                [상담사1] 2021-10-10 11:13:51
                            </div>
                            <div class="chat">
                                <div class="chat-layer">
                                    <div class="buttons">'
                                        <button class="button-reply" data-inverted="" data-tooltip="답장 달기" data-position="top center" onclick="viewToReply()"></button>
                                        <button class="button-template" data-inverted="" data-tooltip="템플릿 만들기" data-position="top center" onclick="templateModal()"></button>
                                        <button class="button-knowledge" data-inverted="" data-tooltip="지식관리 호출" data-position="top center" onclick="knowledgeManagementModal()"></button>
                                        <button class="button-sideview" data-inverted="" data-tooltip="사이드뷰" data-position="top center"></button>
                                    </div>
                                </div>
                                <div class="bubble">
                                    <div class="txt_chat"><p>내가보낸메시지</p></div>
                                </div>
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
</div>

<script>
    ui.find('.chat-body').overlayScrollbars().scroll({y: "100%"}, 100);
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
