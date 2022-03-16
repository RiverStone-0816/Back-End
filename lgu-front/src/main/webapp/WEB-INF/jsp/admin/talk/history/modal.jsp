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

<div class="ui modal tiny" style="height: 750px; max-height: none;">
    <i class="close icon"></i>
    <div class="header">
        상담톡대화방메시지
    </div>
    <div class="scrolling content rows"  style="height: 690px; max-height: none;" >
        <div class="chat-container">
            <div class="room" style="background-color:#dce6f2; height:640px; max-height: none;">
                <div class="chat-header">
                    ${g.htmlQuote(entity.roomName)}
                </div>
                <div class="chat-body" style="height: 605px; max-height: none;">
                    <c:forEach var="e" items="${messageHistory}">
                        <c:choose>

                            <c:when test="${e.sendReceive == 'RI' || e.sendReceive == 'SI' || e.sendReceive == 'RB' || e.sendReceive == 'SB' || e.sendReceive == 'RT' || e.sendReceive == 'RR'}">
                            </c:when>

                            <c:when test="${e.sendReceive == 'SE' || e.sendReceive == 'RE' || e.sendReceive == 'AE' || e.sendReceive == 'E' }">
                                <p class="info-msg">[${g.htmlQuote(e.insertTime)}] ${g.htmlQuote(e.content)}</p>
                            </c:when>

                            <c:when test="${e.sendReceive == 'RM'}">
                                <p class="info-msg">[${g.htmlQuote(e.insertTime)}] 상담사연결을 요청하였습니다.</p>
                            </c:when>

                            <c:when test="${e.sendReceive == 'SZ'}">
                                <p class="info-msg">[${g.htmlQuote(e.insertTime)}] [${g.htmlQuote(e.idName)}] 상담사가 상담을
                                    찜했습니다.</p>
                            </c:when>

                            <c:when test="${e.sendReceive == 'SG'}">
                                <p class="info-msg">[${g.htmlQuote(e.insertTime)}] [${g.htmlQuote(e.idName)}] 상담사가 상담을
                                    가져왔습니다.</p>
                            </c:when>

                            <c:when test="${e.sendReceive == 'SE' || e.sendReceive == 'RE'}">
                                <p class="info-msg">[${g.htmlQuote(e.insertTime)}] ${g.htmlQuote(e.content)}</p>
                            </c:when>

                            <c:when test="${e.sendReceive == 'AF' || e.sendReceive == 'S' || e.sendReceive == 'R'}">
                                <div class="chat-item ${e.sendReceive == 'AF' || e.sendReceive == 'S' ? 'chat-me' : ''}">
                                    <div class="wrap-content">
                                        <c:set var="name"
                                               value="${e.sendReceive == 'AF' || e.sendReceive == 'S' ? (e.idName == '' || e.idName == null ? '자동발신' : e.idName) : (entity.maindbCustomName == '' || entity.maindbCustomName == null ? '' : entity.maindbCustomName)}"/>
                                        <div class="txt-time">[${g.htmlQuote(name)}] ${e.insertTime}</div>
                                        <c:choose>

                                            <c:when test="${ e.type == 'block' && e.blockInfo.displayList.get(0).type=='INPUT'}">
                                                <div class="chat bot">
                                                    <div class="bubble">
                                                        <div class="card">
                                                            <div class="card-list"
                                                                 style="border-radius: .5rem; border-color: black">
                                                                <ul class="card-list-ul">
                                                                    <div class="label" align="left"
                                                                         style="padding: 0.7em 1em; border-bottom: 1px solid #dcdcdc;">${e.blockInfo.displayList.get(0).elementList.get(0).title}</div>
                                                                    <c:forEach var="k"
                                                                               items="${e.blockInfo.displayList.get(0).elementList}">

                                                                        <c:choose>
                                                                            <c:when test="${k.inputType.code=='time'}">
                                                                                <li class="item form">
                                                                                    <div align="left" class="label">
                                                                                            ${k.displayName}
                                                                                    </div>
                                                                                    <div class="ui multi form">
                                                                                        <select class="slt"
                                                                                                style="border-color: #0c0c0c">
                                                                                            <option>오전</option>
                                                                                            <option>오후</option>
                                                                                        </select>
                                                                                        <select class="slt"
                                                                                                style="border-color: #0c0c0c">
                                                                                            <option>0
                                                                                            </option>
                                                                                        </select>
                                                                                        <span class="unit"
                                                                                              style="font-weight: 900; color: black">시</span>
                                                                                        <select class="slt"
                                                                                                style="border-color: #0c0c0c">
                                                                                            <option>0
                                                                                            </option>
                                                                                        </select>
                                                                                        <span class="unit"
                                                                                              style="font-weight: 900; color: black">분</span>
                                                                                    </div>
                                                                                </li>
                                                                            </c:when>
                                                                            <c:when test="${k.inputType.code=='text' || k.inputType.code=='number' || k.inputType.code=='calendar' || k.inputType.code=='date' ||
                                                                            k.inputType.code=='secret' && k.inputType != null}">
                                                                                <li class="item form">
                                                                                    <div align="left" class="label">
                                                                                            ${k.displayName}
                                                                                    </div>
                                                                                    <div class="ui fluid input">
                                                                                        <input style="border-color: #0c0c0c; border-radius: .5rem;">
                                                                                    </div>
                                                                                </li>
                                                                            </c:when>
                                                                        </c:choose>

                                                                    </c:forEach>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>

                                            <c:when test="${e.type == 'block' && e.blockInfo.displayList.get(0).elementList.get(0).content !=null}">
                                                <c:choose>
                                                    <c:when test="${ e.blockInfo.displayList.get(0).elementList.get(0).image!=null}">
                                                        <div class="chat bot">
                                                            <div class="bubble">
                                                                <div>
                                                                    <div class="card-img">
                                                                        <img src="'${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=' + encodeURIComponent(e.blockInfo.displayList.get(0).elementList.get(0).image">
                                                                    </div>
                                                                    <div class="card-content" style="padding: 10px;">
                                                                        <div class="card-title"
                                                                             style="font-weight: 900; margin: 0 0 10px 0;">${e.blockInfo.displayList.size()}입니다. ${e.blockInfo.name}
                                                                        </div>
                                                                        <div class="card-text"
                                                                             style="white-space: pre-wrap;"> ${g.htmlQuote(e.blockInfo.displayList.get(0).elementList.get(0).content)}
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="chat">
                                                            <div class="bubble">
                                                                <p class="txt_chat">${g.htmlQuote(e.blockInfo.displayList.get(0).elementList.get(0).content)}</p>
                                                            </div>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:when test="${e.type == 'photo'}">
                                                <div class="chat">
                                                    <div class="bubble">
                                                        <p class="txt_chat">
                                                            <img src="${g.htmlQuote(e.content)}">
                                                        </p>
                                                    </div>
                                                </div>
                                                <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                            </c:when>
                                            <c:when test="${e.type == 'audio'}">
                                                <div class="chat">
                                                    <div class="bubble">
                                                        <p class="txt_chat">
                                                            <audio controls src="${g.htmlQuote(e.content)}"></audio>
                                                        </p>
                                                    </div>
                                                </div>
                                                <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                            </c:when>
                                            <c:when test="${e.type == 'file'}">
                                                <div class="chat">
                                                    <div class="bubble">
                                                        <p class="txt_chat">
                                                            <a href="${g.htmlQuote(e.content)}"
                                                               target="_blank">${g.htmlQuote(e.content)}</a>
                                                        </p>
                                                    </div>
                                                </div>
                                                <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="chat">
                                                    <div class="bubble">
                                                        <p class="txt_chat">${g.htmlQuote(e.content)}</p>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:when>

                            <c:when test="${e.sendReceive == 'SA' || e.sendReceive == 'AM' || e.sendReceive == 'AW'  || e.sendReceive == 'SAV'}">
                                <div class="chat-item ${e.sendReceive == 'SA' || e.sendReceive == 'AM' || e.sendReceive == 'AW' || e.sendReceive == 'SAV' ? 'chat-me' : ''}">
                                    <div class="wrap-content">
                                        <c:set var="name" value='오토멘트'/>
                                        <div class="txt-time">[${g.htmlQuote(name)}] ${e.insertTime}</div>
                                        <c:choose>
                                            <c:when test="${e.type == 'photo'}">
                                                <div class="chat">
                                                    <div class="bubble"
                                                         style="background-color: rgba(224, 57, 151, 0.28)">
                                                        <p class="txt_chat">
                                                            <img src="${g.htmlQuote(e.content)}">
                                                        </p>
                                                    </div>
                                                </div>
                                                <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                            </c:when>
                                            <c:when test="${e.type == 'audio'}">
                                                <div class="chat">
                                                    <div class="bubble">
                                                        <p class="txt_chat">
                                                            <audio controls src="${g.htmlQuote(e.content)}"></audio>
                                                        </p>
                                                    </div>
                                                </div>
                                                <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                            </c:when>
                                            <c:when test="${e.type == 'file'}">
                                                <div class="chat">
                                                    <div class="bubble">
                                                        <p class="txt_chat">
                                                            <a href="${g.htmlQuote(e.content)}"
                                                               target="_blank">${g.htmlQuote(e.content)}</a>
                                                        </p>
                                                    </div>
                                                </div>
                                                <a href="${g.htmlQuote(e.content)}" target="_blank">저장하기</a>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="chat">
                                                    <div class="bubble"
                                                         style="background-color: rgba(224, 57, 151, 0.28)">
                                                        <p class="txt_chat">${g.htmlQuote(e.content)}</p>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
