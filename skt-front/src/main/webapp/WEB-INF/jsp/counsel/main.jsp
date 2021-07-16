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
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<div class="consult-wrapper -counsel-panel">

    <div class="ui modal tiny" id="user-info-popup">
        <i class="close icon"></i>
        <div class="header">유저정보</div>
        <div class="content">
            <table class="ui table celled unstackable border-top-default">
                <tr>
                    <th>이름</th>
                    <td>홍길동</td>
                </tr>
                <tr>
                    <th>부서명</th>
                    <td>개발팀</td>
                </tr>
                <tr>
                    <th>메일주소</th>
                    <td>hong@eicn.co.kr</td>
                </tr>
                <tr>
                    <th>휴대전화</th>
                    <td>010-0000-0000 <img style="float: right; width: 32px; border-radius: 5px;" src="<c:url value="/resources/images/call-img-temp.JPG"/>"></td>
                </tr>
                <tr>
                    <th>전화번호</th>
                    <td>070-0000-0000 <img style="float: right; width: 32px; border-radius: 5px;" src="<c:url value="/resources/images/call-img-temp.JPG"/>"></td>
                </tr>
                <tr>
                    <th>내선번호</th>
                    <td>0000 <img style="float: right; width: 32px; border-radius: 5px;" src="<c:url value="/resources/images/call-img-temp.JPG"/>"></td>
                </tr>
            </table>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">닫기</button>
        </div>
    </div>

    <div class="ui modal" id="organi-chat-create-popup">
        <i class="close icon"></i>
        <div class="header">새로운 채팅방 만들기</div>
        <div class="content">
            <div class="organization-ul-wrap">
                <ul class="organization-ul modal">
                    <div class="title">조직명
                        <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13 </div>
                    </div>
                    <li class="belong">
                        <div class="user-wrap">
                            <div class="ui checkbox">
                                <input type="checkbox" name="example">
                                <label>본사>본부>서울지사</label>
                            </div>
                        </div>
                    </li>
                    <li class="active">
                        <div class="user-wrap">
                            <span class="user-icon active"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label after-state">후처리</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label wait-state">대기</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label bell-state">벨울림</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                </ul>
                <ul class="organization-ul modal">
                    <div class="title">조직명
                        <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13 </div>
                    </div>
                    <li class="belong">
                        <div class="user-wrap">
                            <div class="ui checkbox">
                                <input type="checkbox" name="example">
                                <label>본사>본부>서울지사</label>
                            </div>
                        </div>
                    </li>
                    <li class="active">
                        <div class="user-wrap">
                            <span class="user-icon active"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label after-state">후처리</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label wait-state">대기</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label bell-state">벨울림</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                </ul>
                <ul class="organization-ul modal">
                    <div class="title">조직명
                        <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13 </div>
                    </div>
                    <li class="belong">
                        <div class="user-wrap">
                            <div class="ui checkbox">
                                <input type="checkbox" name="example">
                                <label>본사>본부>서울지사</label>
                            </div>
                        </div>
                    </li>
                    <li class="active">
                        <div class="user-wrap">
                            <span class="user-icon active"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label after-state">후처리</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label wait-state">대기</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label bell-state">벨울림</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                </ul>
                <ul class="organization-ul modal">
                    <div class="title">조직명
                        <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13 </div>
                    </div>
                    <li class="belong">
                        <div class="user-wrap">
                            <div class="ui checkbox">
                                <input type="checkbox" name="example">
                                <label>본사>본부>서울지사</label>
                            </div>
                        </div>
                    </li>
                    <li class="active">
                        <div class="user-wrap">
                            <span class="user-icon active"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label after-state">후처리</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label wait-state">대기</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                    <li>
                        <div class="user-wrap">
                            <span class="user-icon"></span>홍길동
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label bell-state">벨울림</span>
                            <span class="user-num">내선:000 / 휴대폰:010-0000-0000</span>
                        </div>
                        <div class="state-wrap">
                            전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="actions">
            <span class="left-txt">선택된 조직원 00명</span>
            <button type="button" class="ui button modal-close">취소</button>
            <button type="button" class="ui orange button modal-close">생성</button>
        </div>
    </div>

    <div class="ui modal tiny" id="organi-chat-room-popup">
        <i class="close icon"></i>
        <div class="header"><span style="cursor:pointer" onclick="chatTitleModifyBtn()">대화방이름</span></div>
        <div class="content">
            <div class="organi-chat-room-container">
                <div class="organi-chat-room-header">
                    <div class="default-inner">
                        <div class="search-wrap">
                            <div class="ui icon input">
                                <input type="text">
                                <i class="search link icon"></i>
                            </div>
                            <div class="search-count">
                                0 / 0
                            </div>
                        </div>
                        <div class="btn-wrap">
                            <button type="button"><img src="<c:url value="/resources/images/chat-search-up.svg"/>"></button>
                            <button type="button"><img src="<c:url value="/resources/images/chat-search-down.svg"/>"></button>
                            <button type="button"><img src="<c:url value="/resources/images/chat-file.svg"/>"></button>
                            <button type="button"><img src="<c:url value="/resources/images/chat-user-add.svg"/>"></button>
                            <button type="button"><img src="<c:url value="/resources/images/chat-exit.svg"/>"></button>
                        </div>
                    </div>
                    <div class="modify-inner">
                        <div class="input-wrap">
                            <div class="ui fluid input">
                                <input type="text">
                            </div>
                        </div>
                        <div class="btn-wrap">
                            <div class="ui button">취소</div>
                            <div class="ui orange button">변경</div>
                        </div>
                    </div>
                </div>
                <div class="organi-chat-room-content">
                    <div class="chat-body">
                        <p class="event-txt">[00-00 00:00] 이벤트 문구</p>
                        <div class="chat-item">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        참여자 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">대화내용대화내용대화내용</div>
                                            <span class="count">3</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-item">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        참여자 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">
                                                <div class="file-wrap"><img src="<c:url value="/resources/images/chat-folder.svg"/>">첨부파일이름첨부파일이름.zip</div>
                                                <div class="file-size-wrap">용량 : 1563.KB</div>
                                            </div>
                                            <span class="count">3</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-item">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        참여자 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">
                                                <div class="file-wrap"><img src="<c:url value="/resources/images/chat-audio.svg"/>">첨부파일이름첨부파일이름.mp3</div>
                                                <div class="file-size-wrap">용량 : 1563.KB</div>
                                            </div>
                                            <span class="count">3</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-item">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        참여자 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat"><img src="http://dn-m.talk.kakao.com/talkm/oYsrn2lMaM/AbUPsuGnGZVKP1xHwZXYs0/i_fd0196c4dd8e.jpeg?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImNvbXBhbnlJZCI6InNrZGV2IiwiZXh0ZW5zaW9uIjoiMTAwMCIsImlkVHlwZSI6IkEiLCJleHAiOjE2MjYzOTE2ODMsImlhdCI6MTYyNjMwNTI4M30.Czop2V7xeToXiOR3ScQ3vPs6nI0eAR2MbtwDAYo3_lA0Ro2FpT6CwnsV4qFmB1d-NXZWKk2wzBXZYRWPcePBKw"></div>
                                            <span class="count">3</span>
                                        </div>
                                    </div>
                                    <div class="save">
                                        <a href="#">저장하기</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-item chat-me">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        참여자 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat"><img src="http://dn-m.talk.kakao.com/talkm/oYsrn2lMaM/AbUPsuGnGZVKP1xHwZXYs0/i_fd0196c4dd8e.jpeg?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImNvbXBhbnlJZCI6InNrZGV2IiwiZXh0ZW5zaW9uIjoiMTAwMCIsImlkVHlwZSI6IkEiLCJleHAiOjE2MjYzOTE2ODMsImlhdCI6MTYyNjMwNTI4M30.Czop2V7xeToXiOR3ScQ3vPs6nI0eAR2MbtwDAYo3_lA0Ro2FpT6CwnsV4qFmB1d-NXZWKk2wzBXZYRWPcePBKw"></div>
                                            <span class="count">3</span>
                                        </div>
                                    </div>
                                    <div class="save">
                                        <a href="#">저장하기</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-item chat-me">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        참여자 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">대화내용대화내용대화내용</div>
                                            <span class="count">99</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-item system">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        시스템 [00-00 00:00]
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="txt_chat">대화내용대화내용대화내용대화내용대화내용대화내용대화내용대화내용대화내용대화내용</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="write-chat">
                        <textarea></textarea>
                        <button class="send-btn">전송</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal tiny" id="organi-favor-popup">
        <i class="close icon"></i>
        <div class="header">즐겨찾기 편집</div>
        <div class="content">
            <div style="text-align:center">
                <img style="width:496px;" src="<c:url value="/resources/images/organi-favor-temp.jpg"/>">
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="button" class="ui orange button modal-close">생성</button>
        </div>
    </div>

    <div class="consult-left-panel">
        <div class="panel consult-organization-panel full-height">
            <div class="panel-heading">조직도
                <div class="btn-wrap">
                    <button type="button" class="ui basic button organi-state">현황</button>
                    <button type="button" class="ui basic button organi-room">대화방</button>
                </div>
                <div class="state-header">현황</div>
                <button class="state-header-close"></button>
            </div>
            <div class="panel-body remove-pl remove-pr">
                <div class="panel-segment overflow-auto -vertical-resizable">
                    <div class="panel-segment-header">
                        즐겨찾기
                        <button type="button" class="ui basic mini button" onclick="organiFavor()">편집</button>
                    </div>
                    <div class="panel-segment-body">
                        <div class="area mt15">
                            <ul class="organization-ul border-bottom-none remove-padding">
                                <li class="active">
                                    <div class="user-wrap">
                                        <span class="user-icon active"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label after-state">후처리</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk off button"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="user-wrap">
                                        <span class="user-icon"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label wait-state">대기</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk button" data-inverted="" data-tooltip="상담톡" data-position="bottom center"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="user-wrap">
                                        <span class="user-icon"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label bell-state">벨울림</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk button" data-inverted="" data-tooltip="상담톡" data-position="bottom center"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel-segment overflow-auto flex-100">
                    <div class="panel-segment-header">
                        조직도
                        <button type="button" class="ui basic mini button" onclick="organiChatCreate()">선택대화</button>
                    </div>
                    <div class="panel-segment-body">
                        <div class="area">
                            <ul class="organization-ul">
                                <div class="title">
                                    <span class="team-name">조직명조직명조직명조직명명</span>
                                    <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13 </div>
                                </div>
                                <li class="belong">
                                    <div class="user-wrap">
                                        <div class="ui checkbox">
                                            <input type="checkbox" name="example">
                                            <label>본사>본부>서울지사</label>
                                        </div>
                                    </div>
                                </li>
                                <li class="active">
                                    <div class="user-wrap">
                                        <span class="user-icon active"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label after-state">후처리</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk off button"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="user-wrap">
                                        <span class="user-icon"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label wait-state">대기</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk button" data-inverted="" data-tooltip="상담톡" data-position="bottom center"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="user-wrap">
                                        <span class="user-icon"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label bell-state">벨울림</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk button" data-inverted="" data-tooltip="상담톡" data-position="bottom center"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="user-wrap">
                                        <span class="user-icon"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label logout-state">로그아웃</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk button" data-inverted="" data-tooltip="상담톡" data-position="bottom center"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="user-wrap">
                                        <span class="user-icon"></span>홍길동
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label call-state">통화중</span>
                                        <div class="buttons">
                                            <button type="button" class="arrow button" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                                            <button type="button" class="talk button" data-inverted="" data-tooltip="상담톡" data-position="bottom center"></button>
                                            <button type="button" class="info button" data-inverted="" data-tooltip="정보" data-position="bottom center" onclick="userinfoModal()"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        전화 <span class="num">15</span> 채팅 <span class="num">15</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="organi-pop-wrap">
        <div class="organi-pop-second">
            <div class="panel full-height">
                <div class="panel-heading">조직 대화방 <button type="button" class="panel-close"></button></div>
                <div class="panel-body full-height remove-padding">
                    <div class="pd10 panel-border-bottom">
                        <div class="ui fluid icon input">
                            <input type="text" placeholder="검색어 입력">
                            <i class="search link icon"></i>
                        </div>
                    </div>
                    <div class="pd10 full-height">
                        <div class="organization-chat-list-wrap">
                            <div class="chat-list-header">
                                조직 대화방 목록 <button type="button" class="ui basic right floated button" onclick="organiChatCreate()">추가</button>
                            </div>
                            <div class="chat-list-body">
                                <div class="chat-list-container">
                                    <ul>
                                        <li class="item" onclick="organiChatRoom()">
                                            <div class="item-header">
                                                <div class="chat-item-title">대화방 이름</div>
                                                <div class="chat-unread">23</div>
                                            </div>
                                            <div class="item-content">
                                                <div class="last-chat"><img src="<c:url value="/resources/images/chat-img.svg"/>"> 마지막메시지가 나오는 곳 입니다.</div>
                                                <div class="last-time">2021-05-21 09:00:00</div>
                                            </div>
                                        </li>
                                        <li class="item" onclick="organiChatRoom()">
                                            <div class="item-header">
                                                <div class="chat-item-title">대화방 이름</div>
                                                <div class="chat-unread">23</div>
                                            </div>
                                            <div class="item-content">
                                                <div class="last-chat"><img src="<c:url value="/resources/images/chat-audio.svg"/>"> 마지막메시지가 나오는 곳 입니다.</div>
                                                <div class="last-time">2021-05-21 09:00:00</div>
                                            </div>
                                        </li>
                                        <li class="item active" onclick="organiChatRoom()">
                                            <div class="item-header">
                                                <div class="chat-item-title">대화방 이름</div>
                                                <div class="chat-unread">23</div>
                                            </div>
                                            <div class="item-content">
                                                <div class="last-chat"><img src="<c:url value="/resources/images/chat-system.svg"/>"> 마지막메시지가 나오는 곳 입니다.</div>
                                                <div class="last-time">2021-05-21 09:00:00</div>
                                            </div>
                                        </li>
                                        <li class="item" onclick="organiChatRoom()">
                                            <div class="item-header">
                                                <div class="chat-item-title">대화방 이름</div>
                                                <div class="chat-unread">23</div>
                                            </div>
                                            <div class="item-content">
                                                <div class="last-chat"><img src="<c:url value="/resources/images/chat-folder.svg"/>"> 마지막메시지가 나오는 곳 입니다.</div>
                                                <div class="last-time">2021-05-21 09:00:00</div>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="-counsel-content-panel consult-center-panel" data-type="COUNSEL" style="display: none;">
        <div class="ui top attached tabular menu">
            <button class="item active" data-tab="call-panel" onclick="viewCallPanel()">전화</button>
            <c:if test="${user.isTalk.equals('Y')}">
                <button class="item" data-tab="talk-panel" onclick="viewTalkPanel(); $(this).removeClass('highlight'); $(this).removeClass('newImg');">
                    <text>상담톡</text>
                    <div></div>
                </button>
            </c:if>
        </div>

        <jsp:include page="/counsel/call/"/>

        <c:if test="${user.isTalk.equals('Y')}">
            <jsp:include page="/counsel/talk/"/>
        </c:if>
    </div>

    <div class="-counsel-content-panel consult-right-panel" data-type="COUNSEL" style="display: none;">
        <div id="custom-input-panel" class="top-area">
            <div class="panel top remove-mb panel-resizable -vertical-resizable">
                <div id="call-custom-input-container" style="overflow-y: auto;">
                    <div id="call-custom-input"></div>
                </div>
                <c:if test="${user.isTalk.equals('Y')}">
                    <div id="talk-custom-input-container" style="overflow-y: auto; display: none;">
                        <div id="talk-custom-input"></div>
                    </div>
                </c:if>
            </div>
        </div>

        <div id="counseling-input-panel" class="middle-area">
            <div class="panel remove-mb panel-resizable -vertical-resizable">
                <div id="call-counseling-input-container" style="overflow-y: auto;">
                    <div id="call-counseling-input"></div>
                </div>
                <c:if test="${user.isTalk.equals('Y')}">
                    <div id="talk-counseling-input-container" style="overflow-y: auto; display: none;">
                        <div id="talk-counseling-input"></div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="flex-100 bottom-area">
            <div class="ui top attached tabular menu line light flex remove-margin">
                <button class="item active" data-tab="todo">To-Do</button>
                <button class="item" data-tab="consult-history">상담이력</button>
                <%--FIXME:필요시 다음과 같이 추가--%>
                <%--<button class="item" data-tab="etc-lookup">기타조회</button>--%>
            </div>
            <div class="ui bottom attached tab segment remove-margin active" data-tab="todo">
                <jsp:include page="/counsel/todo-list"/>
            </div>
            <div class="ui bottom attached tab segment remove-margin" data-tab="consult-history">
                <table class="ui celled table unstackable">
                    <thead>
                    <tr>
                        <c:if test="${serviceKind.equals('SC')}">
                            <th>채널</th>
                        </c:if>
                        <th>수/발신</th>
                        <th>상담등록시간</th>
                        <th>전화번호</th>
                        <th>상담톡아이디</th>
                        <th>상담원</th>
                        <th>자세히</th>
                    </tr>
                    </thead>
                    <tbody id="counsel-list"></tbody>
                </table>
            </div>
            <div class="ui bottom attached tab segment remove-margin" data-tab="etc-lookup">기타조회</div>
        </div>
    </div>

    <iframe class="content-inner -counsel-content-panel" data-type="NOTICE" src="<c:url value="/admin/service/help/notice/"/>" style="display: none;"></iframe>
    <iframe class="content-inner -counsel-content-panel" data-type="KNOWLEDGE" src="<c:url value="/admin/service/help/task-script/"/>" style="display: none;"></iframe>
    <iframe class="content-inner -counsel-content-panel" data-type="CALENDAR" src="<c:url value="/user-schedule/"/>" style="display: none;"></iframe>
    <div class="content-inner -counsel-content-panel" data-type="PREVIEW">
        <jsp:include page="/counsel/preview/"/>
    </div>

    <jsp:include page="/counsel/modal-calling"/>
    <jsp:include page="/counsel/modal-route-application"/>
    <jsp:include page="/counsel/modal-ars"/>
    <jsp:include page="/counsel/modal-cms"/>
    <jsp:include page="/counsel/modal-send-message"/>
</div>

<tags:scripts>
    <script id="toast-reservation-script">
        window.toastr.options = {
            "closeButton": true,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-bottom-right",
            "preventDuplicates": false,
            "onclick": function (event) {
                $('.toast .toast-close-button').focus();
            },
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "0",
            "extendedTimeOut": "0",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };

        const toastedCalling = {};
        const toastingCalling = [];

        function setReservation() {
            return restSelf.get('/api/counsel/recent-consultation-reservation', null, function () {
            }, true).done(function (response) {
                for (let i = 0; i < response.data.length; i++) {
                    toastingCalling.push(response.data[i]);
                }
            });
        }

        function toastReservation() {
            if (toastingCalling.length <= 0)
                return;

            toastingCalling.sort(function (a, b) {
                return a.time - b.time;
            });

            const now = new Date().getTime();

            for (let i = 0; i < toastingCalling.length; i++) {
                const e = toastingCalling.shift();
                if (e.time > now) {
                    toastingCalling.unshift(e);
                    return;
                }

                noticeReservation(e.todoInfo, parseInt(e.detailConnectInfo));
            }
        }

        function noticeReservation(phone, time) {
            const preTime = toastedCalling[phone];
            if (preTime) {
                if (preTime + 1000 * 60 * 5 > time)
                    return;
            }

            toastedCalling[phone] = time;

            const div = $('<div/>').append(
                $('<div/>', {onclick: "$(this).closest('.toast').find('.toast-close-button').click(); $('#calling-number').val('" + phone + "');", style: 'cursor: pointer;'})
                    .append($('<text/>', {text: '[' + moment(time).format('HH:mm') + '] '}))
                    .append($('<b/>', {text: phone}))
            );
            toastr.warning(div.html(), '통화약속');
        }

        $(document).ready(function () {
            setReservation();
            if ($(parent.document).find('#main').is('.change-mode')) {
                setInterval(setReservation, 60000);
            }
            setInterval(toastReservation, 3000);
        });
    </script>
    <script>
        const ITEM_KEY_CONFIG = "counselDisplayConfiguration";
        let counselDisplayConfiguration = {useCallNoticePopup: true};

        function viewCallPanel() {
            $('#talk-panel').removeClass('active');
            $('#call-panel').addClass('active');
            $('#call-custom-input-container,#call-counseling-input-container').show();
            $('#talk-custom-input-container,#talk-counseling-input-container').hide();

            $('#etc-panel-resizer').hide();
            callExpandEtcPanel();
        }

        function viewTalkPanel() {
            $('#call-panel').removeClass('active');
            $('#talk-panel').addClass('active');
            $('#call-custom-input-container,#call-counseling-input-container').hide();
            $('#talk-custom-input-container,#talk-counseling-input-container').show();

            $('#etc-panel-resizer').show();
            reduceEtcPanel();
        }

        function expandEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_down');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $('#talk-panel').addClass('reduce-panel');
            $('#etc-panel').addClass('expand-panel');
        }

        function callExpandEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_up');
            $('#talk-panel').removeClass('expand-panel');
            $('#etc-panel').removeClass('reduce-panel');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
        }

        function reduceEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_up');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $('#talk-panel').addClass('expand-panel');
            $('#etc-panel').addClass('reduce-panel');
        }

        $('#etc-panel-resizer').click(function () {
            if ($(this).find('i').text() === 'keyboard_arrow_down')
                reduceEtcPanel();
            else
                expandEtcPanel();
        });

        $('.-counsel-panel-indicator').click(function () {
            $('.-counsel-panel-indicator').removeClass('active');
            $(this).addClass('active');
        });

        function popupFieldInfo(type, fieldId, selectValue) {
            var isEmpty = function (value) {
                if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
                    return true
                } else {
                    return false
                }
            };
            if (isEmpty(selectValue)) {
                alert('선택하세요.');
                return;
            }
            popupDraggableModalFromReceivedHtml($.addQueryString('/counsel/modal-field-info', {type: type, fieldId: fieldId, selectItem: selectValue}), 'modal-field-info');
        }


        function popupSearchMaindbCustomModal(type, roomId, senderKey, userKey) {
            popupDraggableModalFromReceivedHtml($.addQueryString('/counsel/modal-search-maindb-custom', {
                type: type || 'CALL',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), 'modal-search-maindb-custom');
        }

        function popupSearchCounselingHistoryModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-counseling-history', 'modal-search-counseling-history');
        }

        function popupSearchCallHistoryModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-call-history', 'modal-search-call-history');
        }

        function popupSearchCallbackModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-callback-history', 'modal-search-callback-history');
        }

        function popupCounselDisplayConfiguration() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-config', 'modal-config');
        }

        function popupCounselingInfo(seq) {
            popupDraggableModalFromReceivedHtml('/counsel/modal-counseling-info?seq=' + seq, 'modal-counseling-info');
        }

        function setAlertCurrentStatus() {
            const ui = $('#current-status-sheet');

            const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
            if (!stringify)
                return;

            const data = JSON.parse(stringify);

            keys(data).map(function (key) {
                const tr = ui.find('[data-name="' + key + '"]');
                const value = parseInt(tr.attr('data-value'));
                if (value !== 0 && (!value || isNaN(value))) return;

                const threshold = parseInt(data[key]);
                if (!threshold || isNaN(threshold)) return;

                if (threshold >= value)
                    tr.addClass('negative');
                else
                    tr.removeClass('negative');
            });
        }

        function setConfiguredTab() {
            $('.-configured-indicator,.-configured-tab').hide();

            const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
            if (!stringify)
                return;

            const data = JSON.parse(stringify);

            keys(data).map(function (key) {
                $('.-configured-indicator').filter(function () {
                    return $(this).attr('data-tab') === key;
                }).show();

                $('.-configured-tab').filter(function () {
                    return $(this).attr('data-tab') === key;
                }).css('display', '');
            });

            if ($('.-configured-indicator.active').css('display') === 'none') {
                $('.-counsel-panel-indicator[data-tab="call-panel"]').click();
            } else {
                $('.-configured-indicator.active').click();
            }
        }

        function loadCoworkerNavigation() {
            replaceReceivedHtmlInSilence('/counsel/coworker-navigation', '#counsel-nav');
        }

        function loadOuterLink() {
            replaceReceivedHtmlInSilence('/counsel/outer-link', '#outer-link-list');
        }

        function loadCurrentStatus() {
            replaceReceivedHtmlInSilence('/counsel/current-status', '#current-status-sheet');
        }

        function loadTodoList() {
            replaceReceivedHtmlInSilence('/counsel/todo-list', '#todo-list');
        }

        function loadCounselingList(customId) {
            if (customId && customId !== '') {
                replaceReceivedHtmlInSilence($.addQueryString('/counsel/counsel-list', {customId: customId}), '#counsel-list');
            } else {
                $('#counsel-list').empty();
            }
        }

        $(window).on('load', function () {
            loadCoworkerNavigation();
            loadOuterLink();
            loadCurrentStatus();
            setInterval(function () {
                if ($(parent.document).find('#main').is('.change-mode')) {
                    loadCoworkerNavigation();
                    loadOuterLink();
                    loadCurrentStatus();
                    loadTodoList();
                }
            }, 30 * 1000);

            $('#call-control-panel').show();

            (function () {
                const configString = localStorage.getItem(ITEM_KEY_CONFIG);
                if (!configString) {
                    localStorage.setItem(ITEM_KEY_CONFIG, JSON.stringify(counselDisplayConfiguration));
                } else {
                    counselDisplayConfiguration = JSON.parse(configString);
                }
            })();

            setAlertCurrentStatus();
            setConfiguredTab();
        });
    </script>
    <script>

        $('.organization-ul .title').click(function(){
            $(this).siblings().toggle();
        });

        $(".organization-ul li:not('.belong')").click(function(){
            $(this).toggleClass('active');
        });

        $('.consult-organization-panel .panel-heading .ui.basic.button').click(function () {
            $(this).toggleClass('active');
        });

        function userinfoModal() {
            $('#user-info-popup').dragModalShow();
        }

        $('.organi-state').click(function () {
            $('.consult-left-panel').toggleClass('wide');
            $('.consult-wrapper .consult-center-panel').toggleClass('control');
            if($('.consult-left-panel').hasClass('wide') === true) {
                $('.organi-pop-wrap').css('left','460px');
            } else {
                $('.organi-pop-wrap').css('left','326px');
            }
        });

        $('.organi-room').click(function () {
            if($('.consult-left-panel').hasClass('wide') === true) {
                $('.organi-pop-wrap').css('left','460px');
            } else {
                $('.organi-pop-wrap').css('left','326px');
            };
            $('.organi-pop-second').toggle();
        });

        let organiChatTitleDefault = $('.organi-chat-room-header .default-inner');
        let organiChatTitleModify = $('.organi-chat-room-header .modify-inner');

        function chatTitleModifyBtn() {
            if(organiChatTitleModify.css('display') === 'none') {
                $(organiChatTitleModify).css('display','flex');
                $(organiChatTitleDefault).css('display','none');
            } else {
                $(organiChatTitleModify).css('display','none');
                $(organiChatTitleDefault).css('display','flex');
            }
        }

        function organiChatCreate() {
            $('#organi-chat-create-popup').css({'z-index': 1003}).dragModalShow();
        }

        function organiFavor() {
            $('#organi-favor-popup').dragModalShow();
        }

        function organiChatRoom() {
            $('#organi-chat-room-popup').css({'z-index': 1003}).dragModalShow();
        }

        const services = {<c:forEach var="e" items="${services}">'${g.escapeQuote(e.key)}': '${g.escapeQuote(e.value)}', </c:forEach>};

        let currentUserStatus = null;
        let currentUserStatusChangedAt = null;
        setInterval(function () {
            $('.-call-waiting-time').each(function () {
                $(this).text((parseInt($(this).text()) + 1) + '초');
            });

            if (currentUserStatusChangedAt !== null) {
                const time = parseInt((new Date().getTime() - currentUserStatusChangedAt) / 1000);
                $('#status-keeping-time').text(pad(parseInt(time / 60), 2) + ":" + pad(time % 60, 2));
            }
        }, 1000);

        $(window).on('load', function () {
            updateQueues();
            updatePersonStatus();
        });
    </script>
    <script>
        window.ipccCommunicator = new IpccCommunicator();

        let audioId, phoneNumber;
        ipccCommunicator
            .on('LOGIN', function (message, kind /*[ LOGIN_OK | LOGIN_ALREADY | LOGOUT | ... ]*/) {
                if (kind === "LOGOUT")
                    return alert("로그아웃");
                if (kind === 'LOGIN_OK' || kind === 'LOGIN_ALREADY')
                    return;
                alert("IPCC 웹소켓 로긴 실패");
            })
            .on('PEER', function (message, kind, data1, data2 /*[ OK | REGISTERED | REACHABLE | NOK | UNREACHABLE | UNREGISTERED | ... ]*/) {
            })
            .on('MEMBERSTATUS', function (message, kind) {
                const status = parseInt(kind);
                $('.-member-status')
                    .removeClass("active")
                    .filter(function () {
                        return parseInt($(this).attr('data-status')) === status;
                    }).addClass("active");

                currentUserStatus = status;
                currentUserStatusChangedAt = new Date().getTime();
            })
            .on('PDSMEMBERSTATUS', function (message, kind) {
                if (!$.isNumeric(pdsStatus))
                    return;

                const status = parseInt(kind);

                $('.-member-status-pds')
                    .removeClass("active")
                    .filter(function () {
                        return parseInt($(this).attr('data-status')) === status;
                    }).addClass("active");

                currentUserStatusChangedAt = new Date().getTime();
            })
            .on('CALLEVENT', function (message, kind /*[ IR | ID | OR | OD | ... ]*/, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12, data13) {
                if (kind === "PICKUP" || kind === "ID" || kind === "OD")
                    $('#partial-recoding').show().find('text').text('부분녹취');

                if (kind === 'IR') { // 인바운드 링울림
                    audioId = data8;
                    phoneNumber = data1;
                    const callingPath = data3;
                    const extension = data2;
                    const secondNum = data4;

                    const candidateQueues = values(queues).filter(function (queue) {
                        return queue.number === secondNum;
                    });

                    const queueName = candidateQueues && candidateQueues.length > 0 ? candidateQueues[0].hanName : null;

                    $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('.-call-waiting-time').text('0초');
                    $('#call-status').text('[수신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
                    loadUserCustomInfo(phoneNumber);
                    $('#user-call-history').empty().append('<option>통화진행중</option>');

                    if (counselDisplayConfiguration.useCallNoticePopup)
                        viewCallReception();

                    loadCustomInput(null, null, phoneNumber).done(function () {
                        $('.item[data-tab="counsel-list"]').click();
                    });
                } else if (kind === 'PICKUP') { //픽업
                    audioId = data8;
                    phoneNumber = data1;
                    const callingPath = data3;
                    const extension = data2;
                    const secondNum = data4;

                    const candidateQueues = values(queues).filter(function (queue) {
                        return queue.number === secondNum;
                    });

                    const queueName = candidateQueues && candidateQueues.length > 0 ? candidateQueues[0].hanName : null;

                    $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('.-call-waiting-time').text('0초');
                    $('#call-status').text('[수신] 당겨받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');

                    /*if (counselDisplayConfiguration.useCallNoticePopup)
                        viewCallReception();
*/
                    loadCustomInput(null, null, phoneNumber).done(function () {
                        $('.item[data-tab="counsel-list"]').click();
                    });
                } else if (kind === 'ID') { // 인바운드 통화시작
                    audioId = data8;
                    phoneNumber = data1;

                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('#modal-calling').modalHide();
                    $('#call-status').text('[수신] 전화받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');
                } else if (kind === 'OR') { // 아웃바운드 링울림
                    audioId = data8;
                    phoneNumber = data2;

                    if (data9 === 'PRV') {
                        const previewGroupId = data11;
                        const previewCustomId = data12;

                        loadPreviewCustomInput(previewGroupId, previewCustomId);
                        loadPreviewCounselingInput(previewGroupId, previewCustomId);
                    } else {
                        $('.-calling-number').val(phoneNumber).text(phoneNumber);
                        $('#call-status').text('[발신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
                        $('#user-call-history').empty().append('<option>통화진행중</option>');

                        loadCustomInput(null, null, phoneNumber).done(function () {
                            $('.item[data-tab="counsel-list"]').click();
                        });
                    }
                } else if (kind === 'OD') { // 아웃바운드 통화시작
                    audioId = data8;
                    phoneNumber = data2;

                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('#call-status').text('[발신] 전화받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');
                }
            })
            .on('HANGUPEVENT', function () {
                $('#partial-recoding').hide();
                $('#call-status').text('통화종료');

                // IR 이후, 곧장 HANGUP이 일어나면 modal의 transition때문에 옳바르게 dimmer가 상태변화되지 못한다.
                setTimeout(function () {
                    $('#modal-calling').modalHide();
                }, 1000);

                //HANGUP이 일어나고 DB에 데이터가 쌓이는 시간 감안
                setTimeout(function () {
                    loadUserCallHistory();
                }, 5000);
            })
            .on('FORWARDING', function (message, kind/*[ OK | ... ]*/, data1, data2/*[ (공백) | A | B | C | T | ... ]*/) {
            })
            .on('CALLSTATUS', function (message, kind/*[ REDIRECT | ... ]*/, data1, data2/*[ NOCHAN | BUSY ]*/) {
            })
            .on('DTMFREADEVENT', function (message, kind) {
            })
            .on('PDSMEMBERSTATUS', function (message, kind) {
            })
            .on('MSG', function (message, kind, data1, data2) {
                if (!data1 || !data2)
                    return;

                const decodeMessage = decodeURI(data2).replace(/[ψ\n]/gi, "<br/>");
                toastr.info(data1, decodeMessage);
            })
            .on('CMS', function (message, kind, data1) {
                if (kind === 'OK') {
                    $('#modal-cms-popup').modalHide();
                    return alert('적용되었습니다.');
                } else {
                    return alert('실패하였습니다: ' + data1);
                }
            })
            .on('REC_START'/*부분녹취 시작*/, function (message, kind/*[ NOK | STARTOK ]*/, data1, partialRecordCnt) {
                const partialRecord = $('#partial-recoding');
                if (kind === 'NOK') {
                    console.error("부분녹취실패 : {}", message);
                    return;
                }
                if (kind === 'STARTOK')
                    $(partialRecord).find('text').text('부분녹취(' + partialRecordCnt + ")");
            })
            .on('REC_STOP'/*부분녹취 중지*/, function (message, kind/*[ NOK | STOPOK ]*/, data1, partialRecordCnt) {
                const partialRecord = $('#partial-recoding');
                if (kind === 'NOK') {
                    console.error("부분녹취실패 : {}", message);
                    return;
                }
                if (kind === 'STOPOK')
                    $(partialRecord).find('text').text('부분녹취(' + partialRecordCnt + ")");
            })
            .on('BYE', function (message, kind/*[ SAME_UID | SAME_PID ]*/, data1, data2, data3) {
                switch (kind) {
                    case"SAME_UID":
                        return alert("다른 컴퓨터에서 같은 아이디로 로긴되어서 서버와 끊김");
                    case "SAME_PID":
                        return alert("다른 컴퓨터에서 같은 내선으로로 로긴되어서 서버와 끊김");
                    default:
                        return alert("[" + kind + "]" + data3 + "(" + data1 + ")");
                }
            });


        let pdsStatus;

        $(window).on('load', function () {
            restSelf.get('/api/auth/socket-info').done(function (response) {
                const fromUi = "EICN_IPCC";
                if (response.data.extension != null)
                    ipccCommunicator.connect(response.data.callControlSocketUrl, response.data.pbxHost, response.data.companyId, response.data.userId, response.data.extension, hex_sha512(response.data.password), response.data.idType, fromUi, response.data.isMulti);

                <c:if test="${user.isTalk.equals('Y')}">
                talkCommunicator.connect(response.data.talkSocketUrl, response.data.companyId, response.data.groupCode, response.data.groupTreeName, response.data.groupLevel, response.data.userId, response.data.userName, "USER", response.data.idType);
                </c:if>
            });

            $(".-call-reject").click(function () {
                $('#modal-calling').modalHide();
                ipccCommunicator.reject();
            });

            $(".-call-receive").click(function () {
                $('#modal-calling').modalHide();
                ipccCommunicator.receiveCall();
            });

            $(".-call-hangup").click(function () {
                ipccCommunicator.hangUp();
                ipccCommunicator.stopMultiCall();
            });

            $('.-call-pickup').click(function () {
                ipccCommunicator.pickUp();
            });

            $(".-call-hold").click(function () {
                if (ipccCommunicator.status.cMemberStatus !== MEMBER_STATUS_CALLING)
                    return;

                if ($(this).hasClass("active")) {
                    ipccCommunicator.stopHolding();
                    $(this).removeClass("active");
                } else {
                    ipccCommunicator.startHolding();
                    $(this).addClass("active");
                }
            });

            $('#partial-recoding').click(function () {
                if ($(this).find('i').hasClass('fa-play')) {
                    ipccCommunicator.startRecording();
                    const recordIcon = $(this).find('i');
                    recordIcon.removeClass('fa-play').addClass('fa-stop').addClass("partial-record-stop").show();
                } else {
                    ipccCommunicator.stopRecording();
                    $(this).find('i').removeClass('fa-stop').removeClass("partial-record-stop").addClass('fa-play').show();
                }
            });
        });
    </script>
</tags:scripts>
