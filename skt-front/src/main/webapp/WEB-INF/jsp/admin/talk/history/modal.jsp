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

<div class="ui modal tiny">
    <i class="close icon"></i>
    <div class="header">
        채팅상담대화방메시지
    </div>
    <div class="scrolling content rows">
        <div class="chat-container">
            <div class="room">
                <div class="chat-header">
                    ${g.htmlQuote(entity.roomName)}
                </div>
                <div class="chat-body -overlay-scroll">
                    <c:forEach var="e" items="${messageHistory}">
                        <c:choose>
                            <c:when test="${e.sendReceive == 'SZ' || e.sendReceive == 'SG'}"></c:when>
                            <c:when test="${e.sendReceive == 'SE' || e.sendReceive == 'RE'}">
                                <p class="info-msg">[${g.htmlQuote(e.insertTime)}] ${g.htmlQuote(e.content)}</p>
                            </c:when>
                            <c:when test="${e.sendReceive == 'AF' || e.sendReceive == 'S' || e.sendReceive == 'R'}">
                                <div class="chat-item ${e.sendReceive == 'AF' || e.sendReceive == 'S' ? 'chat-me' : ''}">
                                    <div class="wrap-content">
                                        <div class="profile-image"></div>
                                        <div class="txt-segment">
                                            <c:set var="name" value="${e.sendReceive == 'AF' || e.sendReceive == 'S' ? (e.idName == '' || e.idName == null ? '자동발신' : e.idName) : (entity.maindbCustomName == '' || entity.maindbCustomName == null ? '' : entity.maindbCustomName)}"/>
                                            <div class="txt-time">[${g.htmlQuote(name)}] ${e.insertTime}</div>
                                            <c:choose>
                                                <c:when test="${e.type == 'photo'}">
                                                    <div class="chat">
                                                        <div class="bubble">
                                                            <p class="txt-chat">
                                                                <img src="${g.htmlQuote(e.content)}">
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                                </c:when>
                                                <c:when test="${e.type == 'audio'}">
                                                    <div class="chat">
                                                        <div class="bubble">
                                                            <p class="txt-chat">
                                                                <audio controls src="${g.htmlQuote(e.content)}"></audio>
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                                </c:when>
                                                <c:when test="${e.type == 'file'}">
                                                    <div class="chat">
                                                        <div class="bubble">
                                                            <p class="txt-chat">
                                                                <a href="${g.htmlQuote(e.content)}" target="_blank">${g.htmlQuote(e.content)}</a>
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="chat">
                                                        <div class="bubble">
                                                            <p class="txt-chat">${g.htmlQuote(e.content)}</p>
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
            </div>
        </div>
    </div>
</div>
