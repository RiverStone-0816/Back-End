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
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<c:set var="CHATTABLE" value="${serviceKind.equals('SC') && usingServices.contains('CHATT')}"/>

<div class="ui modal tiny" id="user-info-popup">
    <i class="close icon"></i>
    <div class="header">유저정보</div>
    <div class="content">
        <table class="ui table celled unstackable border-top-default">
            <tr>
                <th>이름</th>
                <td class="-field" data-name="idName"></td>
            </tr>
            <tr>
                <th>부서명</th>
                <td class="-field" data-name="groupName"></td>
            </tr>
            <tr>
                <th>메일주소</th>
                <td class="-field" data-name="emailInfo"></td>
            </tr>
            <tr>
                <th>전화번호</th>
                <td class="row-btn-wrap">
                    <text class="-field" data-name="hpNumber"></text>
                    <%--TODO: 전화돌려주기 기능 붙여야할지도. --%>
                    <button></button>
                </td>
            </tr>
            <tr>
                <th>내선번호</th>
                <td class="row-btn-wrap">
                    <text class="-field" data-name="extension"></text>
                    <%--TODO: 전화돌려주기 기능 붙여야할지도. --%>
                    <button></button>
                </td>
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
                    <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13</div>
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
                    <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13</div>
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
                    <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13</div>
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
                    <div class="dot-label-wrap"><span class="dot-label"></span>13 <span class="dot-label active"></span>13</div>
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
                        <div class="ui brand button">변경</div>
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
                                        <div class="txt_chat"><img src="">
                                        </div>
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
                                        <div class="txt_chat"><img src="">
                                        </div>
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

<div class="consult-left-panel">
    <div class="panel consult-organization-panel full-height">
        <div class="panel-heading">조직도
            <div class="btn-wrap">
                <button type="button" class="ui basic button" id="organi-state">현황</button>
                <button type="button" class="ui basic button" id="organi-room">
                    <text class="message-indicator">0</text>
                    대화방
                </button>
            </div>
            <div class="state-header">현황</div>
            <button class="state-header-close"></button>
        </div>
        <div class="panel-body remove-pl remove-pr">

            <div class="panel-segment favor">
                <div class="panel-segment-header">
                    즐겨찾기
                    <button type="button" class="ui basic mini button" onclick="messenger.popupBookmarkModal()">편집</button>
                </div>
                <div class="panel-segment-body overflow-auto">
                    <div class="area">
                        <ul class="organization-ul border-bottom-none remove-padding" id="messenger-bookmark-panel"></ul>
                    </div>
                </div>
            </div>

            <div class="panel-segment list">
                <div class="panel-segment-header">
                    조직도
                    <button type="button" class="ui basic mini button" onclick="organiChatCreate()">선택대화</button>
                </div>
                <div class="panel-segment-body">
                    <div class="area" id="messenger-organization-panel"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="organi-pop-wrap">
        <div class="organi-pop-second">
            <div class="panel full-height">
                <div class="panel-heading">조직 대화방
                    <button type="button" class="panel-close"></button>
                </div>
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
                                조직 대화방 목록
                                <button type="button" class="ui basic right floated button" onclick="organiChatCreate()">추가</button>
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
</div>



<div id="messenger-modal" class="ui modal large" style="width: 500px;">
    <i class="close icon"></i>
    <div class="header">메신저</div>

    <div class="content rows">
        <div class="ui fluid icon input mb15">
            <input id="messenger-filter-text" placeholder="검색" onkeyup="messenger.filterItem(); return false;"/>
            <i class="search icon"></i>
        </div>
        <div class="messenger-panel-wrap">
            <div id="messenger-left-panel">
                <div class="mb10 align-center">
                    <button class="ui icon button -messenger-container-selector active" onclick="messenger.showPersonPanel()" title="조직도">
                        <i class="list icon large"></i>
                    </button>
                    <span>조직도</span>
                </div>
                <div class="align-center">
                    <button class="ui icon button -messenger-container-selector" onclick="messenger.showChatPanel()" title="채팅방" style="position: relative;">
                        <i class="comments icon large"></i>
                        <text class="message-indicator" style="font-size: 10px; line-height: 20px; width: 20px; height: 20px; border-radius: 20px;">0</text>
                    </button>
                    <span>채팅방</span>
                </div>
            </div>
            <div id="messenger-content-panel">
                <div class="inner">
                    <div class="ui list" id="messenger-chat-container"></div>
                </div>
            </div>
        </div>
    </div>
    <div id="messenger-control-panel" class="actions">
        <button type="button" class="ui button blue" onclick="messenger.openRoom()">대화방 열기</button>
    </div>

    <div class="chat-container" id="messenger-room">
        <div class="room">
            <div class="chat-header" title="">
                <button type="button" class="ui mini compact icon button -toggle-invitation-panel">
                    <i class="user plus icon"></i>
                </button>
                <text class="-chatroom-name"></text>
                <i class="x icon -close-room" style="position: absolute; right: 10px; top: 10px;"></i>
            </div>
            <div class="chat-body -overlay-scroll" style="height: calc(100% - 170px);"></div>
            <div class="write-chat">
                <div class="write-menu">
                    <button type="button" class="mini ui button icon compact -upload-file" title="파일전송"><i class="paperclip icon"></i></button>

                    <div class="ui small action input">
                        <input type="text" placeholder="Search...">
                        <button class="ui icon button -search-text">
                            <i class="search icon"></i>
                        </button>
                    </div>

                    <text data-total="0" data-index="0" class="-text-count" style="margin-right: 0.5em;">0/0</text>
                    <button type="button" class="mini ui button icon compact -move-to-prev-text"><i class="angle up icon"></i></button>
                    <button type="button" class="mini ui button icon compact -move-to-next-text"><i class="angle down icon"></i></button>
                    <button type="button" class="mini ui button compact pull-right -leave-room" style="margin-top: 3px;">나가기</button>

                </div>
                <div class="wrap-inp">
                    <div class="inp-box">
                        <textarea id="messenger-message" placeholder="전송하실 메시지를 입력하세요."></textarea>
                    </div>
                    <button type="button" class="send-btn" onclick="messenger.sendMessage()">전송</button>
                </div>
            </div>
        </div>
        <div class="invitation-panel"
             style="display: none; position: absolute; top: 44px; bottom: 180px; left: 1px; width: 400px; background: rgba(255,255,255,0.9); padding: 15px; overflow: hidden; z-index: 1;">
            <div class="" style="position: absolute; top: 15px; left: 15px; right: 0; bottom: 30px; overflow: auto;">
                <div id="messenger-room-members"></div>
                <div class="organization-panel ui list"></div>
            </div>
            <button type="button" class="ui button mini compact -invite-to-room" style="position: absolute; bottom: 1px; right: 55px;">초대</button>
            <button type="button" class="ui button mini compact -hide-panel" style="position: absolute; bottom: 1px; right: 1px;">닫기</button>
        </div>
    </div>
</div>
<jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
<tags:scripts>
    <script>
        const userToGroupNames = {};

        $('#organi-state').click(function () {
            $('.consult-left-panel').toggleClass('wide');
            $('.consult-wrapper .consult-center-panel').toggleClass('control');
            if ($('.consult-left-panel').hasClass('wide') === true) {
                $('.content-inner.-counsel-content-panel').addClass('control');
            } else {
                $('.content-inner.-counsel-content-panel').removeClass('control');
            }
        });

        $('.consult-organization-panel .panel-heading .ui.basic.button').click(function () {
            $(this).toggleClass('active');
        });

        let organiChatTitleDefault = $('.organi-chat-room-header .default-inner');
        let organiChatTitleModify = $('.organi-chat-room-header .modify-inner');

        function chatTitleModifyBtn() {
            if (organiChatTitleModify.css('display') === 'none') {
                $(organiChatTitleModify).css('display', 'flex');
                $(organiChatTitleDefault).css('display', 'none');
            } else {
                $(organiChatTitleModify).css('display', 'none');
                $(organiChatTitleDefault).css('display', 'flex');
            }
        }

        function organiChatCreate() {
            $('#organi-chat-create-popup').css({'z-index': 1003}).dragModalShow();
        }

        function organiChatRoom() {
            $('#organi-chat-room-popup').css({'z-index': 1003}).dragModalShow();
        }

        $('.consult-organization-panel .state-header-close').click(function(){
            $('.consult-organization-panel #organi-state').removeClass('active');
            $('.consult-left-panel').removeClass('wide');
            $('.consult-wrapper .consult-center-panel').removeClass('control');
        });

        $('.organi-pop-second .panel-close').click(function(){
            $('.consult-organization-panel #organi-room').removeClass('active');
            $(this).parents('.organi-pop-second').hide();
        });

        $('#organi-room').click(function () {
            <c:choose>
            <c:when test="${CHATTABLE}">
            if ($('.consult-left-panel').hasClass('wide') === true) {

            } else {

            }
            $('.organi-pop-second').toggle();
            </c:when>
            <c:otherwise>
            alert('메신저 라이센스가 없습니다.');
            </c:otherwise>
            </c:choose>
        });

        function Messenger() {
            const messenger = this;

            messenger.me = '${g.escapeQuote(user.id)}';
            messenger.accessToken = '${g.escapeQuote(accessToken)}';

            messenger.ui = {
                modal: $('#messenger-modal'),
                filterInput: $('#messenger-filter-text'),
                organizationPanel: $('#messenger-organization-panel'),
                bookmarkPanel: $('#messenger-bookmark-panel'),
                chatContainer: $('#messenger-chat-container'),
                messageInput: $('#messenger-message'),
            };
            messenger.ui.room = $('#messenger-room');
            messenger.ui.roomName = messenger.ui.room.find('.-chatroom-name');
            messenger.ui.invitationPanel = messenger.ui.room.find('.organization-panel');
            messenger.ui.searchingTextCountExpression = messenger.ui.room.find('.-text-count');
            messenger.ui.roomMembers = $('#messenger-room-members');

            function receiveMessage(data) {
                const roomId = data.room_id;
                const roomName = data.room_name;
                const messageId = data.message_id;
                const time = parseFloat(data.cur_timestr) * 1000;
                const messageType = data.type;
                const sendReceive = data.send_receive;
                const contents = data.contents;
                const formUserId = data.userid;
                const formUserName = data.username;

                messenger._appendMessage(roomId, messageId, time, messageType, sendReceive, contents, formUserId, formUserName, parseInt(data.member_cnt) - 1);

                messenger._inputRoomInfo({roomId: roomId, roomName: roomName, lastMsg: contents, lastTime: time, lastUserid: formUserId,},
                    messenger.currentRoom && messenger.currentRoom.id === roomId ? 0 : (messenger.rooms[roomId] && messenger.rooms[roomId].unreadMessageTotalCount || 0) + 1);

                messenger._sortChatListItem();
                messenger.filterItem();
                messenger._showMessageIndicator();

                if (messenger.currentRoom && messenger.currentRoom.id === roomId) {
                    messenger.communicator.confirmMessage(roomId, messageId);
                    messenger.currentRoom.members[formUserId].lastReadMessageId = messageId;
                    messenger.currentRoom.members[messenger.me].lastReadMessageId = messageId;
                    messenger.updateMessageReadCount();
                }
            }

            messenger.communicator = new MessengerCommunicator()
                .on('svc_login', function (data) {
                    const userid = data.userid;

                    $('.-messenger-user,.-messenger-bookmark').each(function () {
                        const $this = $(this);

                        if ($this.attr('data-id') !== userid)
                            return;

                        $this.find('.icon').css('color', '#c60452');
                    });
                })
                .on('svc_logout', function (data) {
                    const userid = data.userid;

                    $('.-messenger-user,.-messenger-bookmark').each(function () {
                        const $this = $(this);

                        if ($this.attr('data-id') !== userid)
                            return;

                        $this.find('.icon').css('color', 'black');
                    });
                })
                .on('svc_msg', receiveMessage)
                .on('svc_join_msg', function (data) {
                    receiveMessage(data);
                    messenger.communicator.join(data.room_id);
                })
                .on('svc_invite_room', function (data) {
                    const roomId = data.room_id;

                    if (me === data.invited_userid)
                        messenger.communicator.join(roomId);

                    restSelf.get('/api/chatt/' + roomId, null, null, true).done(function (response) {
                        messenger._inputRoomInfo(response.data);
                        messenger._sortChatListItem();
                        messenger.filterItem();
                        messenger._showMessageIndicator();
                    });

                })
                .on('svc_leave_room', function (data) {
                    const roomId = data.room_id;
                    const userid = data.leave_userid;

                    if (messenger.currentRoom && messenger.currentRoom.id === roomId)
                        delete messenger.currentRoom.members[userid];

                    if (userid === messenger.me) {
                        if (messenger.currentRoom && messenger.currentRoom.id === roomId)
                            messenger.closeRoom();

                        messenger.ui.modal.find('.-messenger-chat-item').filter(function () {
                            return $(this).attr('data-id') === roomId;
                        }).remove();
                    } else {
                        messenger._loadRoomName(roomId);
                    }
                })
                .on('svc_read_confirm', function (data) {
                    const roomId = data.room_id;
                    const userid = data.userid;
                    const lastReadMessageId = data.last_read_message_id;

                    if (messenger.currentRoom && messenger.currentRoom.id === roomId) {
                        messenger.currentRoom.members[userid].lastReadMessageId = lastReadMessageId;
                        messenger.updateMessageReadCount();

                        messenger.rooms[roomId].unreadMessageTotalCount = 0;
                        messenger._appendChatListItem(roomId);
                    }
                })
                .on('svc_roomname_change', function (data) {
                    const roomId = data.room_id;
                    const roomName = data.change_room_name;

                    messenger.rooms[roomId].roomName = roomName;

                    const chattingRoomElement = messenger.ui.modal.find('.-messenger-chat-item').filter(function () {
                        return $(this).attr('data-id') === roomId;
                    });
                    chattingRoomElement.find('.-room-name').text(messenger.rooms[roomId].roomName);
                });
        }

        Messenger.prototype.READ_LIMIT = 10;
        // 채팅방의 읽지 않은 메세지 갯수를 기록한다.
        Messenger.prototype._inputRoomInfo = function (entity, unreadMessageTotalCount) {
            const messenger = this;

            const roomId = entity.roomId;

            if (!messenger.rooms[roomId]) {
                messenger.rooms[roomId] = entity;
                messenger.rooms[roomId].unreadMessageTotalCount = $.isNumeric(unreadMessageTotalCount) ? unreadMessageTotalCount : 1;
            } else {
                keys(entity).map(function (key) {
                    messenger.rooms[roomId][key] = entity[key];
                });
                messenger.rooms[roomId].unreadMessageTotalCount = $.isNumeric(unreadMessageTotalCount) ? unreadMessageTotalCount
                    : $.isNumeric(messenger.rooms[roomId].unreadMessageTotalCount) ? messenger.rooms[roomId].unreadMessageTotalCount
                        : 0;
            }

            messenger._appendChatListItem(roomId);
        };
        Messenger.prototype._loadRoomName = function (roomId) {
            const messenger = this;

            restSelf.get('/api/chatt/' + roomId, null, null, true).done(function (response) {
                messenger._inputRoomInfo(response.data);

                const chattingRoomElement = messenger.ui.modal.find('.-messenger-chat-item').filter(function () {
                    return $(this).attr('data-id') === roomId;
                });
                chattingRoomElement.find('.-room-name').text(messenger.rooms[roomId].roomName);
            });
        };
        Messenger.prototype.showPersonPanel = function () {
            this.ui.modal.removeClass('show-rooms');
        };
        Messenger.prototype.showChatPanel = function () {
            this.ui.modal.addClass('show-rooms');
            this.ui.chatContainer.show();
        };
        Messenger.prototype.sendMessage = function () {
            const messenger = this;

            if (messenger.currentRoom) {
                const roomId = messenger.currentRoom.id;
                const message = messenger.ui.messageInput.val();

                if (!roomId || !message)
                    return;

                messenger.communicator.sendMessage(roomId, message);
                messenger.ui.messageInput.val('');
            }
        };
        Messenger.prototype.popupBookmarkModal = function () {
            popupReceivedHtml('/modal-messenger-bookmark', 'modal-messenger-bookmark');
        };
        // 조직도에서 대화방 열기를 시도한다.. 메서드 이름 변경좀....
        Messenger.prototype.openRoom = function () {
            const messenger = this;

            const users = [];

            messenger.ui.organizationPanel.find('.-messenger-folder').filter('.active').removeClass('active').each(function () {
                $(this).find('.-messenger-user').each(function () {
                    const id = $(this).attr('data-id');
                    if (users.indexOf(id) >= 0)
                        return;
                    users.push(id);
                });
            });

            messenger.ui.organizationPanel.find('.-messenger-user').filter('.active').removeClass('active').each(function () {
                const id = $(this).attr('data-id');
                if (users.indexOf(id) >= 0)
                    return;
                users.push(id);
            });

            messenger.ui.bookmarkPanel.find('.-messenger-bookmark').filter('.active').removeClass('active').each(function () {
                const id = $(this).attr('data-id');
                if (users.indexOf(id) >= 0)
                    return;
                users.push(id);
            });

            if (!users.length)
                return;

            users.push(messenger.me);

            restSelf.post('/api/chatt/', {memberList: users}).done(function (response) {
                messenger.loadRoom(response.data);
            });
        };
        Messenger.prototype._appendMessage = function (roomId, messageId, time, messageType, sendReceive, contents, userId, username, unreadCount) {
            const messenger = this;

            const timeString = moment(time).format('MM-DD HH:mm');
            const myMessage = ['AF', 'S'].indexOf(sendReceive) >= 0 && messenger.me === userId;

            if (!messenger.currentRoom || messenger.currentRoom.id !== roomId)
                return;

            if (messenger.currentRoom.messages[messageId])
                return;

            const item = (function () {
                if (['SZ', 'SG'].indexOf(sendReceive) >= 0)
                    return $('<div/>', {class: '-chat-message -ignored-message', 'data-id': messageId, 'data-time': time});

                if (['SE', 'RE'].indexOf(sendReceive) >= 0)
                    return $('<p/>', {class: 'info-msg -chat-message -system-message ', text: '[' + timeString + '] ' + contents, 'data-id': messageId, 'data-time': time});

                if (['AF', 'S', 'R'].indexOf(sendReceive) >= 0) {
                    if (messageType === 'info') {
                        return $('<p/>', {class: 'info-msg -chat-message -system-message ', text: '[' + timeString + '] ' + contents, 'data-id': messageId, 'data-time': time});
                    }

                    const item = $('<div/>', {class: 'chat-item -chat-message ' + (myMessage ? 'chat-me' : ''), 'data-id': messageId, 'data-time': time});
                    const contentInfo = messageType !== 'info' ? $('<div/>', {class: 'txt-time', text: '[' + username + '] ' + timeString}) : '';
                    const content = $('<div/>', {class: 'wrap-content'}).appendTo(messageType === 'info' ? item.css('display', 'inline') : item)
                        .append(contentInfo);

                    const url = $.addQueryString(contents, {token: messenger.accessToken});

                    const chat = $('<div class="chat">').appendTo(content);
                    const bubble = $('<div class="bubble">')
                        .append($('<div/>', {class: 'outer-unread-count', text: unreadCount || ''}))
                        .appendTo(chat);

                    if (messageType === 'file') {
                        const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(contents);
                        const url2 = $.addQueryString(split && split[4] || '', {token: messenger.accessToken});

                        if (split[1].endsWith('g'))
                            bubble.append('<p class="txt_chat"><img src="' + url2 + '"></p>');
                        else if (split[1].contains('wav') || split[1].contains('mp'))
                            bubble.append('<p class="txt_chat"><audio controls src="' + url2 + '" style="height: 35px;"></audio></p>');
                        else
                            bubble.append('<p class="txt_chat"><a href="' + url2 + '" target="_blank"><i class="paperclip icon"></i> ' + (split && split[2] || '') + '<p style="opacity: 50%; font-size: smaller; padding: 0 0.5em 1em;"> 용량: ' + (split && split[3] || '') + '</p></a></p>');
                        content.append('<a href="' + url2 + '" target="_blank">저장하기</a>');
                    } else {
                        bubble.append('<p class="txt_chat">' + htmlQuote(contents) + '</p>');
                    }

                    return item;
                }

                return $('<text/>', {text: 'unknown sendReceive: ' + sendReceive, class: '-chat-message -unknown-message ', 'data-id': messageId, 'data-time': time})
                    .append('<br/>')
                    .append($('<text/>', {text: contents}));
            })();

            const chatBody = messenger.ui.room.find('.chat-body .os-content');

            if (messenger.currentRoom.startMessageTime >= time) {
                messenger.currentRoom.startMessageTime = time;
                messenger.currentRoom.startMessageId = messageId;
                chatBody.prepend(item);
            } else if (messenger.currentRoom.endMessageTime <= time) {
                messenger.currentRoom.endMessageTime = time;
                messenger.currentRoom.endMessageId = messageId;
                chatBody.append(item);

                const scrollBody = messenger.ui.room.find('.chat-body .os-viewport');
                scrollBody.scrollTop(scrollBody[0].scrollHeight);
            } else {
                (function () {
                    const messages = messenger.ui.room.find('.-chat-message');
                    for (let i = 0; i < messages.length; i++) {
                        const message = $(messages[i]);
                        const messageTime = parseInt(message.attr('data-time'));
                        if (time < messageTime) {
                            message.before(item);
                            return;
                        }
                    }
                })();
            }

            messenger.currentRoom.messages[messageId] = {
                messageId: messageId,
                insertTime: time,
                messageType: messageType,
                sendReceive: sendReceive,
                contents: contents,
                userId: userId,
                username: username,
            };
        };
        // 채팅방 리스트의 아이템 추가
        Messenger.prototype._appendChatListItem = function (roomId) {
            const messenger = this;

            const roomName = messenger.rooms[roomId].roomName;
            const lastMsg = messenger.rooms[roomId].lastMsg;
            const lastTime = messenger.rooms[roomId].lastTime;
            const unreadMessageTotalCount = messenger.rooms[roomId].unreadMessageTotalCount;

            const timeString = moment(lastTime).format('MM-DD HH:mm');

            const existChatItem = messenger.ui.modal.find('.-messenger-chat-item').filter(function () {
                return $(this).attr('data-id') === roomId;
            });
            messenger.ui.roomName.text(roomName);

            if (existChatItem.length > 0) {
                existChatItem.find('.-room-name').text(roomName);
                existChatItem.find('.-last-message-time').text(timeString).attr('data-value', lastTime);
                const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(lastMsg);
                existChatItem.find('.preview').text(split ? '[파일전송]' + split[2] : lastMsg);
                existChatItem.find('.number').text(unreadMessageTotalCount).css('display', !unreadMessageTotalCount ? 'none' : '');
                return;
            }

            $('<div/>', {class: 'item -messenger-chat-item', 'data-id': roomId})
                .append(
                    $('<div/>', {class: 'header'})
                        .append($('<i/>', {class: 'comments icon'}))
                        .append($('<text/>', {
                            class: '-room-name', text: roomName, onclick: 'return false;', click: function () {
                                const $this = $(this);
                                prompt('채팅방 이름 변경').done(function (text) {
                                    text = text.trim();
                                    if (!text)
                                        return;
                                    restSelf.put('/api/chatt/' + roomId + '/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
                                        $this.text(text);
                                        messenger.communicator.changeRoomName(roomId, text);
                                    });
                                });
                                return false;
                            }
                        }))
                        .append($('<text/>', {class: '-last-message-time', text: timeString, 'data-value': lastTime}))
                )
                .append(
                    $('<div/>', {class: 'content'})
                        .append($('<div/>', {class: 'preview', text: lastMsg}))
                        .append($('<div/>', {class: 'number', text: unreadMessageTotalCount, style: 'display: ' + (!unreadMessageTotalCount ? 'none' : '')}))
                )
                .appendTo(messenger.ui.chatContainer)
                .click(function () {
                    messenger.loadRoom(roomId);
                });
        };
        Messenger.prototype.filterItem = function () {
            const messenger = this;

            const text = messenger.ui.filterInput.val().trim();
            messenger.ui.modal.find('.-messenger-chat-item,.-messenger-bookmark').each(function () {
                if ($(this).text().indexOf(text) >= 0) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });

            messenger.ui.organizationPanel.find('.-messenger-folder').show().filter(function () {
                return $(this).parent()[0] === messenger.ui.organizationPanel[0];
            }).each(function () {
                function renderAndReturnContainsText(element) {
                    let containsText = false;

                    if (element.hasClass('-messenger-folder')) {
                        containsText = element.children('.content').children('.header').text().indexOf(text) >= 0;

                        element.children('.content').children('.list').children().map(function () {
                            containsText |= renderAndReturnContainsText($(this));
                        });
                    } else {
                        containsText = element.text().indexOf(text) >= 0;
                    }

                    if (containsText) {
                        element.show();
                        return true;
                    } else {
                        element.hide();
                        return false;
                    }
                }

                renderAndReturnContainsText($(this));
            });
        };
        // 떠다니는 버튼 옆에 안 읽은 메세지 총 갯수를 적는다.
        Messenger.prototype._showMessageIndicator = function () {
            const messenger = this;

            let unreadCount = 0;
            values(messenger.rooms).map(function (e) {
                unreadCount += e.unreadMessageTotalCount;
            });
            $('.message-indicator').show().text(unreadCount);
            if (!unreadCount)
                $('.message-indicator').hide();
        };
        Messenger.prototype._sortChatListItem = function () {
            const messenger = this;

            let items = [];
            messenger.ui.modal.find('.-messenger-chat-item').detach().each(function () {
                items.push($(this));
            });
            items.sort(function (a, b) {
                return parseInt(b.find('.-last-message-time').attr('data-value'))
                    - parseInt(a.find('.-last-message-time').attr('data-value'));
            });

            items.map(function (e) {
                messenger.ui.chatContainer.append(e);
            });
        };
        Messenger.prototype.showRoomTitle = function () {
            const messenger = this;

            let roomTitle = '';
            const members = values(messenger.currentRoom.members);

            for (let i = 0; i < members.length; i++) {
                roomTitle += members[i].userName;
                if (i < members.length - 1)
                    roomTitle += ', ';
            }
            messenger.ui.roomName.text(messenger.currentRoom.roomName);
            $('.chat-header').attr("title", roomTitle);
        };
        // 열려있는 채팅방을 닫는다. (나가기 아님)
        Messenger.prototype.closeRoom = function () {
            const messenger = this;

            messenger.ui.modal.removeClass('show-room');

            (function reduceWidth() {
                setTimeout(function () {
                    if (!messenger.ui.room.is(':hidden'))
                        return reduceWidth();
                    messenger.ui.modal.css('width', messenger.ui.modal.width() / 2);
                }, 10);
            })();

            messenger.clearSearching();
            delete messenger.currentRoom;
            messenger.ui.room.find('.-chat-message').remove();
            messenger.ui.room.find('.-chatroom-name').text('');
        };
        Messenger.prototype.loadRooms = function () {
            const messenger = this;

            return restSelf.get('/api/chatt/chatt-room', null, null, true).done(function (response) {
                messenger.ui.chatContainer.empty();
                messenger.rooms = {};

                response.data.map(function (e) {
                    messenger._inputRoomInfo(e.chattRoom, e.unreadMessageTotalCount);
                });

                messenger._sortChatListItem();
                messenger.filterItem();
                messenger._showMessageIndicator();
            });
        };
        Messenger.prototype.loadBookmarks = function () {
            const messenger = this;

            return restSelf.get('/api/chatt/bookmark-list', null, null, true).done(function (response) {
                messenger.ui.bookmarkPanel.empty();
                response.data.map(function (e) {
                    if (e.id === messenger.me)
                        return;

                    $('<li/>', {class: '-messenger-bookmark', 'data-id': e.id})
                        .append(
                            $('<div/>', {class: 'user-wrap'})
                                .append($('<span/>', {class: 'user-icon ' + (e.isLoginChatt === 'L' ? 'active' : '')})) /*todo: 로그인변화 대응 확인*/
                                .append($('<text/>', {text: e.idName}))
                        )
                        .append(
                            $('<div/>', {class: 'btn-wrap'})
                                .append($('<span/>', {class: 'ui mini label -consultant-status-with-color', 'data-peer': e.peer, css: {visibility: e.peer ? 'visible' : 'hidden'}})) /*todo: 초기값 세팅*/
                                .append(
                                    $('<div/>', {class: 'buttons'})
                                        .append($('<button/>', {type: 'button', class: 'arrow button', 'data-inverted': '', 'data-tooltip': '호전환', 'data-position': 'bottom center'}))
                                        .append($('<button/>', {type: 'button', class: 'talk ${CHATTABLE ? 'on' : 'off'} button'}))
                                        .append($('<button/>', {
                                            type: 'button', class: 'info button', 'data-inverted': '', 'data-tooltip': '정보', 'data-position': 'bottom center', click: function () {
                                                const modal = $('#user-info-popup');
                                                e.groupName = userToGroupNames[e.id];
                                                modal.find('.-field').each(function () { /*todo: groupTreeName 값있는지 확인*/
                                                    $(this).text(e[$(this).attr('data-name')]);
                                                });
                                                modal.dragModalShow();
                                            }
                                        }))
                                )
                        )
                        .append(
                            $('<div/>', {class: 'state-wrap'})
                                .append($('<text/>', {text: '전화'}))
                                .append($('<span/>', {class: 'num', text: '0'})) /*todo: 기능 확인*/
                                .append($('<text/>', {text: '채팅'}))
                                .append($('<span/>', {class: 'num', text: '0'})) /*todo: 기능 확인*/
                        )
                        .appendTo(messenger.ui.bookmarkPanel)
                        .click(function (event) {
                            if (event.ctrlKey) {
                                $(this).toggleClass('active');
                            } else {
                                messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                                messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                                messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                                $(this).addClass('active');
                            }
                        })
                        .dblclick(function () {
                            messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                            messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                            messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                            $(this).addClass('active');
                            messenger.openRoom();
                        });
                });
                messenger.filterItem();
            });
        };

        Messenger.prototype._loadOrganization = function (response) {
            const messenger = this;

            function hierarchicalOrganizationString(upperOrganizationNames, groupName) {
                let string = '';

                upperOrganizationNames.map(function (name) {
                    if (string) string += '>';
                    string += name;
                });

                if (string) string += '>';
                return string + groupName;
            }

            function attachFolder(e, upperOrganizationNames) {
                if (e.personList && e.personList.length) {
                    const item = $('<ul/>', {class: 'organization-ul -messenger-folder'})
                        .append(
                            $('<div/>', {class: 'title'})
                                .append($('<span/>', {class: 'team-name', text: e.groupName}))
                                .append(
                                    $('<div/>', {class: 'dot-label-wrap'})
                                        .append($('<span/>', {class: 'dot-label'}))
                                        .append($('<text/>', {text: 0})) /*todo: 비로그인 사용자 카운트*/
                                        .append($('<span/>', {class: 'dot-label active'}))
                                        .append($('<text/>', {text: 0})) /*todo: 로그인 사용자 카운트*/
                                )
                        )
                        .append(
                            $('<li/>', {class: 'belong'})
                                .append(
                                    $('<div/>', {class: 'user-wrap'})
                                        .append(
                                            $('<div/>', {class: 'ui checkbox'})
                                                .append($('<input/>', {type: 'checkbox'})) /*todo: active 상태 기능 변경*/
                                                .append($('<label/>', {text: hierarchicalOrganizationString(upperOrganizationNames, e.groupName)}))
                                        )
                                )
                        )
                        .appendTo(messenger.ui.organizationPanel);

                    const groupName = e.groupName;
                    e.personList.map(function (e) {
                        e.groupName = groupName;
                        userToGroupNames[e.id] = groupName;
                        attachPerson(item, e);
                    });
                }

                const newUpperOrganizationNames = upperOrganizationNames.slice();
                newUpperOrganizationNames.push(e.groupName);

                if (e.organizationMetaChatt)
                    e.organizationMetaChatt.map(function (e) {
                        attachFolder(e, newUpperOrganizationNames);
                    });
            }

            function attachPerson(container, e) {
                $('<li/>', {class: '-messenger-user', 'data-id': e.id})
                    .append(
                        $('<div/>', {class: 'user-wrap'})
                            .append($('<span/>', {class: 'user-icon ' + (e.isLoginChatt === 'L' ? 'active' : '')})) /*todo: 로그인변화 대응 확인*/
                            .append($('<text/>', {text: e.idName}))
                    )
                    .append(
                        $('<div/>', {class: 'btn-wrap'})
                            .append($('<span/>', {class: 'ui mini label -consultant-status-with-color', 'data-peer': e.peer, css: {visibility: e.peer ? 'visible' : 'hidden'}})) /*todo: 초기값 세팅*/
                            .append(
                                $('<div/>', {class: 'buttons'})
                                    .append($('<button/>', {type: 'button', class: 'arrow button', 'data-inverted': '', 'data-tooltip': '호전환', 'data-position': 'bottom center'}))
                                    .append($('<button/>', {type: 'button', class: 'talk ${CHATTABLE ? 'on' : 'off'} button'}))
                                    .append($('<button/>', {
                                        type: 'button', class: 'info button', 'data-inverted': '', 'data-tooltip': '정보', 'data-position': 'bottom center', click: function () {
                                            const modal = $('#user-info-popup');
                                            modal.find('.-field').each(function () { /*todo: groupTreeName 값있는지 확인*/
                                                $(this).text(e[$(this).attr('data-name')]);
                                            });
                                            modal.dragModalShow();
                                        }
                                    }))
                            )
                    )
                    .append(
                        $('<div/>', {class: 'state-wrap'})
                            .append($('<text/>', {text: '전화'}))
                            .append($('<span/>', {class: 'num', text: '0'})) /*todo: 기능 확인*/
                            .append($('<text/>', {text: '채팅'}))
                            .append($('<span/>', {class: 'num', text: '0'})) /*todo: 기능 확인*/
                    )
                    .appendTo(container)
                    .click(function (event) {
                        if (event.ctrlKey) {
                            $(this).toggleClass('active');

                            if ($(this).hasClass('active')) {
                                messenger.lastActiveElement = this;
                            }

                            return;
                        }
                        if (event.shiftKey && messenger.lastActiveElement) {
                            messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                            messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                            messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                            $(messenger.lastActiveElement).addClass('active');

                            const _this = this;

                            if (_this === messenger.lastActiveElement)
                                return;

                            let meetActiveElement = false;
                            let meetMe = false;
                            messenger.ui.organizationPanel.find('.-messenger-user').each(function () {
                                if (this === messenger.lastActiveElement) {
                                    $(this).addClass('active');
                                    meetActiveElement = true;
                                } else if (this === _this) {
                                    $(this).addClass('active');
                                    meetMe = true;
                                } else {
                                    if (meetActiveElement ^ meetMe)
                                        $(this).addClass('active');
                                }
                            });
                        } else {
                            messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                            messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                            messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                            $(this).addClass('active');
                            messenger.lastActiveElement = this;
                        }
                    })
                    .dblclick(function () {
                        messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                        messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                        messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                        $(this).addClass('active');
                        messenger.openRoom();
                    });
            }

            response.data.map(function (e) {
                attachFolder(e, []);
            });

            messenger.filterItem();
        };

        Messenger.prototype.loadOrganization = function () {
            const messenger = this;

            return restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                messenger._loadOrganization(response);
            });
        };
        // 대화방을 연다. (최초로 특정 대화방의 메세지를 화면에 뿌린다.)
        Messenger.prototype.loadRoom = function (roomId) {
            const messenger = this;

            const hasClass = messenger.ui.modal.hasClass('show-room');
            messenger.ui.modal.addClass('show-room');

            (function extendWidth() {
                if (hasClass)
                    return;

                setTimeout(function () {
                    if (messenger.ui.room.is(':hidden'))
                        return extendWidth();
                    messenger.ui.modal.css('width', Math.min($(window).width(), messenger.ui.modal.width() * 2));
                }, 10);
            })();

            restSelf.get('/api/chatt/' + roomId + '/chatting', {limit: messenger.READ_LIMIT}).done(function (response) {

                messenger.ui.room.find('.-chat-message').remove();

                const entity = response.data;
                messenger._inputRoomInfo(entity, 0);
                messenger._showMessageIndicator();

                messenger.currentRoom = {
                    id: roomId,
                    roomName: entity.roomName,
                    members: {},
                    messages: {},
                    searchingMessage: null,
                    searchingMessages: [],
                    startMessageTime: entity.chattingMessages.length > 0 ? entity.chattingMessages[0].insertTime : null,
                    startMessageId: entity.chattingMessages.length > 0 ? entity.chattingMessages[0].messageId : null,
                    endMessageTime: entity.chattingMessages.length > 0 ? entity.chattingMessages[entity.chattingMessages.length - 1].insertTime : null,
                    endMessageId: entity.chattingMessages.length > 0 ? entity.chattingMessages[entity.chattingMessages.length - 1].messageId : null,
                };

                entity.chattingMembers.map(function (e) {
                    messenger.currentRoom.members[e.userid] = e;
                });

                messenger.showRoomTitle();

                entity.chattingMessages.sort(function (a, b) {
                    return a.insertTime - b.insertTime;
                });

                entity.chattingMessages.map(function (e) {
                    messenger._appendMessage(e.roomId, e.messageId, e.insertTime, e.type, e.sendReceive, e.content, e.userid, e.userName, e.unreadMessageCount);
                });

                const scrollBody = messenger.ui.room.find('.chat-body .os-viewport');
                scrollBody.scrollTop(scrollBody[0].scrollHeight);

                if (messenger.currentRoom.endMessageId)
                    messenger.communicator.confirmMessage(messenger.currentRoom.id, messenger.currentRoom.endMessageId);


            }).done(function () {
                messenger.ui.modal.find('.-messenger-chat-item').filter(function () {
                    return $(this).attr('data-id') === roomId;
                }).find('.-unread-message').text(0);
                messenger._showMessageIndicator();
            });
        };
        Messenger.prototype._loadInvitablePersons = function (response) {
            const messenger = this;

            function attachFolder(container, e) {
                const item = $('<div/>', {class: 'item -messenger-folder'})
                    .append($('<i/>', {class: 'folder icon'}))
                    .appendTo(container);

                const content = $('<div/>', {class: 'content'})
                    .append($('<div/>', {class: 'header', text: e.groupName}))
                    .appendTo(item);

                const childrenContainer = $('<div/>', {class: 'list'})
                    .appendTo(content);

                if (e.personList)
                    e.personList.map(function (e) {
                        attachPerson(childrenContainer, e);
                    });

                if (e.organizationMetaChatt)
                    e.organizationMetaChatt.map(function (e) {
                        attachFolder(childrenContainer, e);
                    });
            }

            function attachPerson(container, e) {
                /*if (memberIds.indexOf(e.id) >= 0)
                    return;*/

                let text = '';
                if (e.extension) text += ' / 내선:' + e.extension;
                if (e.hpNumber) text += ' / 휴대폰:' + e.hpNumber;
                if (e.emailInfo) text += ' / 이메일:' + e.emailInfo;
                if (text.indexOf(' / ') === 0) text = text.substr(3);
                const info = text !== '' ? $('<span/>', {style: 'margin-left: 1em; font-size: 90%; color: #aaa;', text: '[' + text + ']'}) : '';

                $('<div/>', {class: 'item -messenger-user', 'data-id': e.id})
                    .append(
                        $('<div/>', {class: 'header'})
                            .append($('<i/>', {class: 'user outline icon', style: 'color: ' + (e.isLoginChatt === 'L' ? '#c60452' : 'black') + ';'}))
                            .append($('<text/>', {text: e.idName, 'data-name': e.idName}).append(info))
                    )
                    .appendTo(container)
                    .click(function (event) {
                        if (event.shiftKey) {
                            $(this).toggleClass('active');
                        } else {
                            messenger.ui.room.find('.-messenger-user').removeClass('active');
                            $(this).addClass('active');
                        }
                    });
            }

            response.data.map(function (e) {
                attachFolder(messenger.ui.invitationPanel, e);
            });
        };
        Messenger.prototype.loadInvitablePersons = function () {
            const messenger = this;

            restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                messenger._loadInvitablePersons(response);
            });
        };
        Messenger.prototype.clearSearching = function () {
            const messenger = this;

            if (messenger.currentRoom) {
                messenger.currentRoom.searchingMessage = null;
                messenger.currentRoom.searchingMessages = [];
                messenger.ui.searchingTextCountExpression.attr('data-total', 0).text('0/0');
                $('.-chat-message').removeClass('active');
            }
        };
        Messenger.prototype.loadMessages = function (option) {
            const messenger = this;

            return restSelf.get('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id) + '/chatting', option).done(function (response) {
                messenger._inputRoomInfo(response.data);

                if (response.data.chattingMessages.length <= 0)
                    return;

                response.data.chattingMessages.sort(function (a, b) {
                    return parseFloat(a.cur_timestr || a.insertTime) - parseFloat(b.cur_timestr || b.insertTime);
                });

                for (let i = response.data.chattingMessages.length - 1; i >= 0; i--) {
                    if (response.data.chattingMessages[i].messageId !== messenger.currentRoom.startMessageId)
                        messenger._appendMessage(response.data.chattingMessages[i].roomId,
                            response.data.chattingMessages[i].messageId,
                            response.data.chattingMessages[i].insertTime,
                            response.data.chattingMessages[i].type,
                            response.data.chattingMessages[i].sendReceive,
                            response.data.chattingMessages[i].content,
                            response.data.chattingMessages[i].userid,
                            response.data.chattingMessages[i].userName,
                            response.data.chattingMessages[i].unreadMessageCount);
                }
            });
        };
        Messenger.prototype._moveToText = function (index) {
            const messenger = this;

            const total = parseInt(messenger.ui.searchingTextCountExpression.attr('data-total'));

            index = (index + total) % total;
            if (total === 0 || isNaN(index)) {
                parseInt(messenger.ui.searchingTextCountExpression.attr('data-index', 0));
                messenger.ui.searchingTextCountExpression.text('0/0');
                return;
            }

            messenger.ui.searchingTextCountExpression.attr('data-index', index + 1);
            messenger.ui.searchingTextCountExpression.text((index + 1) + '/' + total);
            const messageId = messenger.currentRoom.searchingMessages[index].messageId;
            // const messageTime = messenger.currentRoom.searchingMessages[index].messageTime;

            const messageElement = $('.-chat-message').removeClass('active').filter(function () {
                return messageId === $(this).attr('data-id');
            });
            if (messageElement.length > 0)
                return messageElement.addClass('active').closest('.os-viewport').animate({scrollTop: messageElement.position().top}, 400);

            messenger.loadMessages({
                endMessageId: messageId,
                startMessageId: messenger.currentRoom.startMessageId
            }).done(function () {
                const messageElement = $('.-chat-message').filter(function () {
                    return messageId === $(this).attr('data-id');
                });
                if (messageElement.length > 0)
                    messageElement.addClass('active').closest('.os-viewport').animate({scrollTop: messageElement.position().top}, 400);
            });
        };
        // 대화방 안에서의 안읽은 메세지 카운트 갱신
        Messenger.prototype.updateMessageReadCount = function () {
            const messenger = this;

            const memberIds = keys(messenger.currentRoom.members);
            const lastReadMessageTimesOfEachMember = {};

            memberIds.map(function (userid) {
                const lastReadMessageId = messenger.currentRoom.members[userid].lastReadMessageId;
                lastReadMessageTimesOfEachMember[userid] = lastReadMessageId && messenger.currentRoom.messages[lastReadMessageId]
                    ? messenger.currentRoom.messages[lastReadMessageId].insertTime
                    : (messenger.currentRoom.startMessageTime || 0) - 1;
            });

            messenger.ui.room.find('.chat-item').each(function () {
                const messageId = $(this).attr('data-id');
                const message = messenger.currentRoom.messages[messageId];
                if (!message)
                    throw 'ui에 표시된 message인데, 정보가 없다는 건 messages 관리하는 로직에 문제가 있다는 뜻.';

                $(this).find('.outer-unread-count').text(memberIds.filter(function (userid) {
                    return lastReadMessageTimesOfEachMember[userid] < message.insertTime;
                }).length || '');
            });
        };

        Messenger.prototype.init = function () {
            const messenger = this;

            messenger.ui.modal
                .dragModalShow('#header:first')
                .resizable({
                    helper: "ui-resizable-helper",
                    minWidth: 500,
                    minHeight: 500,
                    start: function (event, ui) {
                        $('iframe').css('pointer-events', 'none');
                    },
                    stop: function (event, ui) {
                        $('iframe').css('pointer-events', 'auto');
                    }
                })
                .hide();
            setTimeout(function () {
                messenger.ui.modal.css('max-height', 'none');
            }, 100);

            messenger.ui.modal.find('.-messenger-container-selector').click(function () {
                messenger.ui.modal.find('.-messenger-container-selector').removeClass('active');
                $(this).addClass('active');
            });

            messenger.ui.room.find('.-hide-panel').click(hideInvitationPanel);
            messenger.ui.modal.find('.-toggle-invitation-panel').click(hideInvitationPanel);

            function hideInvitationPanel() {
                const panel = messenger.ui.room.find('.invitation-panel');
                if (panel.is(':visible')) {
                    panel.hide();
                } else {
                    if (messenger.currentRoom) {
                        restSelf.get('/api/chatt/' + messenger.currentRoom.id + '/chatting', {limit: 0}, null, null, true).done(function (response) {
                            messenger.ui.roomMembers.empty();
                            response.data.chattingMembers.map(function (e) {
                                messenger.ui.invitationPanel.find('.-messenger-user').filter(function () {
                                    return e.userid === $(this).attr('data-id');
                                }).clone().css('margin-top', '0.5em').appendTo(messenger.ui.roomMembers);
                            });

                            panel.show();
                        });
                    } else {
                        panel.show();
                    }

                }
            }

            messenger.ui.room.find('.-invite-to-room').click(function () {
                const users = [];
                const userNames = [];
                const userMap = {};

                messenger.ui.room.find('.-messenger-user').filter('.active').removeClass('active').each(function () {
                    const id = $(this).attr('data-id');
                    const name = $(this).find('[data-name]').attr('data-name');

                    if (users.indexOf(id) >= 0)
                        return;

                    if (keys(messenger.currentRoom.members).filter(function (userid) {
                        return userid === id;
                    }).length > 0)
                        return;

                    users.push(id);
                    userNames.push(name);
                    userMap[id] = name;
                });

                if (!users.length)
                    return;

                messenger.communicator.invite(messenger.currentRoom.id, users, userNames);
                restSelf.put('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id) + '/chatt-member', {memberList: users}).done(function () {
                    users.map(function (e) {
                        if (!messenger.currentRoom.members[e])
                            messenger.currentRoom.members[e] = {}

                        messenger.currentRoom.members[e].userid = e;
                        messenger.currentRoom.members[e].userName = userMap[e];
                    });
                    messenger.showRoomTitle();

                    messenger._loadRoomName(messenger.currentRoom.id);
                });
            });

            messenger.ui.room.find('.-close-room').click(function () {
                messenger.closeRoom();
            });

            messenger.ui.room.find('.-leave-room').click(function () {
                if (!messenger.currentRoom)
                    return;

                confirm('채팅방을 나가시겠습니까?').done(function () {
                    restSelf.delete('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id), null, null, true).done(function () {
                        $('.-messenger-chat-item').filter(function () {
                            return $(this).attr('data-id') === messenger.currentRoom.id;
                        }).remove();
                        messenger.communicator.leave(messenger.currentRoom.id);
                        messenger.closeRoom();
                    });
                });
            });

            messenger.ui.room.find('.-search-text').click(function () {
                const keyword = $(this).prev().val().trim();
                if (!keyword) {
                    messenger.clearSearching();
                    return;
                }
                // if (keyword === messenger.currentRoom.searchingMessage)
                //     return messenger.ui.room.find('.-move-to-next-text').click();

                messenger.currentRoom.searchingMessage = keyword;
                restSelf.get('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id) + '/chatting', {
                    message: keyword,
                    limit: messenger.READ_LIMIT
                }).done(function (response) {
                    messenger._inputRoomInfo(response.data);

                    messenger.currentRoom.searchingMessages = [];

                    response.data.chattingMessages.sort(function (a, b) {
                        return b.insertTime - a.insertTime;
                    });

                    response.data.chattingMessages.map(function (e) {
                        messenger.currentRoom.searchingMessages.push({messageId: e.messageId, messageTime: e.insertTime});
                    });

                    messenger.ui.searchingTextCountExpression.attr('data-total', messenger.currentRoom.searchingMessages.length);
                    messenger._moveToText(0);
                });
            });

            messenger.ui.room.find('.-move-to-prev-text').click(function () {
                messenger._moveToText(parseInt(messenger.ui.searchingTextCountExpression.attr('data-index')));
            });

            messenger.ui.room.find('.-move-to-next-text').click(function () {
                messenger._moveToText(parseInt(messenger.ui.searchingTextCountExpression.attr('data-index')) - 2);
            });

            messenger.ui.room.find('.-upload-file').click(function () {
                if (messenger.currentRoom) {
                    window.open($.addQueryString(contextPath + '/messenger-upload-file', {
                        roomId: messenger.currentRoom.id,
                        messengerSocketUrl: messenger.communicator.request.url
                    }), '_blank', 'toolbar=0,location=0,menubar=0,height=100,width=200,left=100,top=100');
                }
            });

            messenger.ui.messageInput.keydown(function (key) {
                if (key.keyCode === 13)
                    if (!key.shiftKey) {
                        messenger.sendMessage();
                        return false;
                    }
            });

            // overflow scroll이 생성된 상태까지 기다려서. 생성된 scroll에 scroll event에 대한 액션을 설정한다.
            (function setScrollEvent() {
                setTimeout(function () {
                    const scrollContainer = messenger.ui.room.find('.os-viewport');
                    if (!scrollContainer.length)
                        return setScrollEvent();

                    scrollContainer.scroll(function () {
                        if ($(this).scrollTop())
                            return;

                        if (messenger.currentRoom)
                            messenger.loadMessages({startMessageId: messenger.currentRoom.startMessageId, limit: messenger.READ_LIMIT + 1})
                    });
                }, 100);
            })();

            messenger.loadBookmarks();
            messenger.loadRooms();

            restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                messenger._loadOrganization(response);
                messenger._loadInvitablePersons(response);
            });

            restSelf.get('/api/auth/socket-info').done(function (response) {
                messenger.communicator.connect(response.data.messengerSocketUrl, response.data.companyId, response.data.userId, response.data.userName, response.data.password);
            });
        };

        const messenger = new Messenger();

        $(window).on('load', function () {
            messenger.init();
        });
    </script>
</tags:scripts>
