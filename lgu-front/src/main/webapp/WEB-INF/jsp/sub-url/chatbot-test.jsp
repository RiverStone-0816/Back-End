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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:chatbotTestLayout>

    <div class="chat-preview window">
        <div class="header">
            <img src="/resources/images/chatbot-icon.svg" class="chatbot-icon"><span class="customer-title">Chat Bot</span>
        </div>
        <div class="content">
            <div class="sample-bubble">
                <img src="../resources/images/eicn-sample.png" class="customer-img">
                <p>이아이씨엔 채팅상담을 이용해 주셔서 감사합니다.
                    문의사항을 입력해주시면 상담원이 답변드리겠습니다.
                    감사합니다.</p>
            </div>
            <div class="sample-bubble">
                <p>다른 채널을 통한 상담을 원하시면 원하시는 서비스의 아이콘을 눌러주세요.</p>
                <div class="preview-channel-icon-container">
                    <a href="#"><img src="../resources/images/kakao-icon.png" class="preview-channel-icon"></a>
                    <a href="#"><img src="../resources/images/ntalk-icon.png" class="preview-channel-icon"></a>
                        <a href="#"><img src="../resources/images/nband-icon.png" class="preview-channel-icon"></a>
                </div>
            </div>
        </div>
        <div class="content editor">
            <div class="sample-bubble">
                <p class="title">
                    폴백블록제목폴백블록제목
                </p>
                <p>볼백 멘트 죄송합니다. 제가 아직 배우지 못한 단어입니다.  좀 더 노력하겠습니다.
                    볼백 멘트 죄송합니다. 제가 아직 배우지 못한 단어입니다.  좀 더 노력하겠습니다.</p>
                <button type="button" class="chatbot-button">제출</button>
                <span class="time-text">21-04-11 04:33</span>
            </div>
            <div class="sample-bubble">
                <p>
                    텍스트텍스트텍스트텍스트텍스트텍스트텍스트텍스트
                </p>
                <span class="time-text">21-04-11 04:33</span>
            </div>
            <div class="card">
                <div class="card-img">
                    <img src="/files/download?file=1636295158240_1919604306132300_team-2.jpg" class="border-radius-1em">
                </div>
                <span class="time-text">21-04-11 04:33</span>
            </div>
            <div class="card">
                <div class="card-img">
                    <img src="/files/download?file=1636295172720_1919618785913000_team-2.jpg" class="border-radius-top-1em">
                </div>
                <div class="card-content">
                    <div class="card-title">
                        카드타이틀
                    </div>
                    <div class="card-text">
                        카드제목
                    </div>
                </div>
                <span class="time-text">21-04-11 04:33</span>
            </div>
            <div class="card">
                <div class="card-list">
                    <div class="card-list-title">
                        <text>리스트타이틀</text>
                    </div>
                    <ul class="card-list-ul">
                        <li class="item"><a target="_blank" class="link-wrap">
                            <div class="item-thumb">
                                <div class="item-thumb-inner">
                                    <img src="/files/download?file=1636295220886_1919666951830200_team-2.jpg">
                                </div>
                            </div>
                            <div class="item-content">
                                <div class="subject">
                                    리스트1
                                </div>
                                <div class="ment">
                                    리스트1
                                </div>
                            </div>
                        </a></li>
                        <li class="item"><a target="_blank" class="link-wrap">
                            <!---->
                            <div class="item-content">
                                <div class="subject">
                                    리스트2
                                </div>
                                <div class="ment">
                                    리스트2
                                </div>
                            </div>
                        </a></li>
                    </ul>
                </div>
                <span class="time-text">21-04-11 04:33</span>
            </div>
            <div class="sample-bubble">
                <button type="button" class="chatbot-button">버튼제목</button><button type="button" class="chatbot-button">버튼제목</button><span class="time-text">21-04-11 04:33</span>
            </div>
            <div class="card">
                <div class="card-list">
                    <ul class="card-list-ul">
                        <li class="item form">
                            <div class="label">
                                사업자번호
                            </div>
                            <div class="ui fluid input">
                                <input type="text">
                            </div>
                        </li>
                        <li class="item form">
                            <div class="label">
                                날짜
                            </div>
                            <div class="ui fluid input">
                                <input type="text">
                            </div>
                        </li>
                        <li class="item form">
                            <div class="label">
                                시간
                            </div>
                            <div class="ui multi form">
                                <select class="slt">
                                    <option>오전</option>
                                    <option>오후</option>
                                </select>
                                <select class="slt">
                                    <option>12</option>
                                </select>
                                <span class="unit">시</span>
                                <select class="slt">
                                    <option>55</option>
                                </select>
                                <span class="unit">분</span>
                            </div>
                        </li>
                        <li class="item"><button type="button" class="chatbot-button">선택한 폴백 블록 동작</button></li>
                    </ul>
                </div>
                <span class="time-text">21-04-11 04:33</span>
            </div>
        </div>
        <div class="content send-message">
            <div class="bubble-wrap">
                <div class="bubble-inner">
                    <p class="bubble">안녕하세요.</p>
                    <span class="time-text">21-04-11 04:33</span>
                </div>
            </div>
            <div class="bubble-wrap">
                <div class="bubble-inner">
                    <p class="bubble">안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.안녕하세요.</p>
                    <span class="time-text">21-04-11 04:33</span>
                </div>
            </div>
        </div>
        <div class="action">
            <div class="ui form">
                <input type="text" placeholder="문의사항을 입력하세요.">
            </div>
            <div>
                <button type="button" class="send-btn"><img src="<c:url value="../resources/images/material-send.svg"/>"></button>
            </div>
        </div>
    </div>

    <tags:scripts>
    </tags:scripts>
</tags:chatbotTestLayout>
