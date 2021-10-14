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

<div class="ui column grid" id="talk-panel">
    <div class="nine wide column">
        <div class="talk-list-container">
            <div class="ui top attached tabular menu">
                <a class="item active" data-tab="talk-list-type-MY" onclick="$(this).removeClass('newImg_c')">
                    <text>상담중 (<span></span>)</text><div></div>
                </a>
                <a class="item" data-tab="talk-list-type-TOT" onclick="$(this).removeClass('newImg_c')">
                    <text>비접수 (<span></span>)</text><div></div>
                </a>
                <a class="item" data-tab="talk-list-type-OTH" onclick="$(this).removeClass('newImg_c')">
                    <text>타상담 (<span></span>)</text><div></div>
                </a>
                <a class="item" data-tab="talk-list-type-END" onclick="$(this).removeClass('newImg_c')">
                    <text>종료 (<span></span>)</text><div></div>
                </a>
            </div>
            <div class="ui bottom attached tab segment active" data-tab="talk-list-type-MY">
                <div id="talk-list-container-MY"></div>
            </div>
            <div class="ui bottom attached tab segment" data-tab="talk-list-type-TOT">
                <div id="talk-list-container-TOT"></div>
            </div>
            <div class="ui bottom attached tab segment" data-tab="talk-list-type-OTH">
                <div id="talk-list-container-OTH"></div>
            </div>
            <div class="ui bottom attached tab segment" data-tab="talk-list-type-END">
                <div id="talk-list-container-END"></div>
            </div>
        </div>
    </div>

    <div class="seven wide column">
        <div class="chat-container overflow-hidden">
            <div class="room" id="talk-room">
                <div class="chat-header"></div>
                <div class="chat-body"></div>
            </div>
            <div class="write-chat">
                <%--<div class="template-container">
                    <div class="template-container-inner">
                        <ul class="template-ul">
                            <li class="template-list">
                                <div class="template-title">/템플릿</div>
                                <div class="template-content">템플릿 1호</div>
                            </li>
                            <li class="template-list">
                                <div class="template-title">/ㅁㅁㅁ</div>
                                <div class="template-content">[object Object]</div>
                            </li>
                            <li class="template-list">
                                <div class="template-title">/ㄹㄹㄹ</div>
                                <div class="template-content">12222</div>
                            </li>
                        </ul>
                    </div>
                </div>--%>
                <%--todo: 메신저에 들어간 슬래쉬키로 템플릿 선택 기능 적용 요청--%>
                <div class="view-to-reply">
                    <%--사진답변일 경우만 출력--%>
                    <div class="target-image">
                        <img src="https://i.pinimg.com/736x/6a/82/f2/6a82f2127d3fb32e5734a87543002e5b.jpg" class="target-image-content">
                    </div>
                    <%--사진답변일 경우만 출력--%>
                    <div class="target-text">
                        <p class="target-user">홍길동에게 답장</p>
                        <p class="target-content">내용</p>
                        <%--<p class="target-content">이미지</p>--%>
                        <%--<p class="target-content">파일명.pdf</p>--%>
                    </div>
                    <div class="target-close">
                        <img src="<c:url value="/resources/images/icon-close.svg"/>">
                    </div>
                </div>
                <div class="write-menu">
                    <div>
                        <button type="button" class="mini ui button compact" onclick="templateSelectPopup()">템플릿</button>
                        <button type="button" class="mini ui button compact">봇템플릿</button>
                        <button type="button" class="mini ui button compact" onclick="uploadTalkFile()">파일전송</button>
                        <button class="ui icon compact mini button" data-inverted="" data-tooltip="음성대화" data-variation="tiny" data-position="top center"> <i class="microphone icon"></i> </button>
                        <button class="ui icon compact mini button" data-inverted="" data-tooltip="화상대화" data-variation="tiny" data-position="top center"> <i class="user icon"></i> </button>
                        <div class="ui fitted toggle checkbox auto-ment vertical-align-middle ml5">
                            <input type="checkbox">
                            <label></label>
                        </div>

                    </div>
                    <div>
                        <button type="button" class="mini ui button compact -assignUnassignedRoomToMe" style="display: none;"
                                onclick="talkCommunicator.assignUnassignedRoomToMe($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key')); $(this).hide()">
                            찜하기
                        </button>
                        <button type="button" class="mini ui button compact -assignAssignedRoomToMe" style="display: none;"
                                onclick="talkCommunicator.assignAssignedRoomToMe($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key')); $(this).hide()">
                            가져오기
                        </button>
                        <button type="button" class="mini ui button compact -deleteRoom" style="display: none;"
                                onclick="deleteRoom($('#talk-room').attr('data-id'))">
                            대화방내리기
                        </button>
                        <button type="button" class="mini ui button compact -downRoom" style="display: none;"
                                onclick="talkCommunicator.deleteRoom($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key'))">
                            대화방종료
                        </button>
                    </div>
                </div>
                <div class="wrap-inp">
                    <div class="inp-box">
                        <textarea id="talk-message" placeholder="전송하실 메시지를 입력하세요."></textarea>
                    </div>
                    <button type="button" class="send-btn" onclick="sendTalkMessage()">전송</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="ui modal side-view-room" id="side-view-modal">
    <div class="chat-container">
        <div class="room">
            <div class="chat-header">
                <span><img src="/resources/images/kakao-icon.png" class="channel-icon"> [S]-대화방ZDgm6rQ043dz</span>
                <i class="x icon modal-close"></i>
            </div>
            <div class="chat-body">
                <p class="info-msg">시스템메시지</p>
                <div class="chat-item">
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
            </div>
        </div>
        <div class="write-chat">
            <%--<div class="template-container">
                    <div class="template-container-inner">
                        <ul class="template-ul">
                            <li class="template-list">
                                <div class="template-title">/템플릿</div>
                                <div class="template-content">템플릿 1호</div>
                            </li>
                            <li class="template-list">
                                <div class="template-title">/ㅁㅁㅁ</div>
                                <div class="template-content">[object Object]</div>
                            </li>
                            <li class="template-list">
                                <div class="template-title">/ㄹㄹㄹ</div>
                                <div class="template-content">12222</div>
                            </li>
                        </ul>
                    </div>
                </div>--%>
            <%--todo: 메신저에 들어간 슬래쉬키로 템플릿 선택 기능 적용 요청--%>
            <div class="view-to-reply">
                <%--사진답변일 경우만 출력--%>
                <div class="target-image">
                    <img src="https://i.pinimg.com/736x/6a/82/f2/6a82f2127d3fb32e5734a87543002e5b.jpg" class="target-image-content">
                </div>
                <%--사진답변일 경우만 출력--%>
                <div class="target-text">
                    <p class="target-user">홍길동에게 답장</p>
                    <p class="target-content">내용</p>
                    <%--<p class="target-content">이미지</p>--%>
                    <%--<p class="target-content">파일명.pdf</p>--%>
                </div>
                <div class="target-close">
                    <img src="<c:url value="/resources/images/icon-close.svg"/>">
                </div>
            </div>
            <div class="write-menu">
                <div>
                    <button type="button" class="mini ui button compact" onclick="templateSelectPopup()">템플릿</button>
                    <button type="button" class="mini ui button compact" onclick="uploadTalkFile()">파일전송</button>
                    <button class="ui icon compact mini button" data-inverted="" data-tooltip="음성대화" data-variation="tiny" data-position="top center"> <i class="microphone icon"></i> </button>
                    <button class="ui icon compact mini button" data-inverted="" data-tooltip="화상대화" data-variation="tiny" data-position="top center"> <i class="user icon"></i> </button>
                    <div class="ui fitted toggle checkbox auto-ment vertical-align-middle ml5 checked">
                        <input type="checkbox" tabindex="0" class="hidden">
                        <label></label>
                    </div>

                </div>
                <div>
                    <button type="button" class="mini ui button compact -assignUnassignedRoomToMe" style="display: none;" onclick="talkCommunicator.assignUnassignedRoomToMe($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key')); $(this).hide()">
                        찜하기
                    </button>
                    <button type="button" class="mini ui button compact -assignAssignedRoomToMe" style="display: none;" onclick="talkCommunicator.assignAssignedRoomToMe($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key')); $(this).hide()">
                        가져오기
                    </button>
                    <button type="button" class="mini ui button compact -deleteRoom" style="display: none;" onclick="deleteRoom($('#talk-room').attr('data-id'))">
                        대화방내리기
                    </button>
                    <button type="button" class="mini ui button compact -downRoom" style="" onclick="talkCommunicator.deleteRoom($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key'))">
                        대화방종료
                    </button>
                </div>
            </div>
            <div class="wrap-inp">
                <div class="inp-box">
                    <textarea id="talk-message" placeholder="전송하실 메시지를 입력하세요."></textarea>
                </div>
                <button type="button" class="send-btn" onclick="sendTalkMessage()">전송</button>
            </div>
        </div>
    </div>
</div>

<div class="ui modal small cover-modal-index" id="messenger-template-add-popup">
    <i class="close icon"></i>
    <div class="header">상담톡 템플릿 추가</div>
    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">사용권한</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select>
                            <option>개인</option>
                            <option>그룹</option>
                            <option>회사전체</option>
                        </select>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">유형</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select>
                            <option>텍스트</option>
                            <option>이미지</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">템플릿명</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <input type="text" value="">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">템플릿멘트</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="align-right"><label>500자 이하</label></div>
                        <textarea id="ment" name="ment" row="10" maxlength="500"></textarea>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">이미지</label>
                </div>
                <div class="twelve wide column">
                    <div class="file-upload-header">
                        <label for="file" class="ui button blue mini compact">파일찾기</label>
                        <input type="file" id="file">
                        <span class="file-name">No file selected</span>
                    </div>
                    <div>
                        <progress value="0" max="100" style="width:100%"></progress>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</div>

<div class="ui modal inverted large" id="knowledge-management-modal">
    <i class="close icon"></i>
    <div class="header">지식관리검색</div>
    <div class="content rows scrolling">
        지식관리 iclude 요청
    </div>
</div>

<div class="ui xsmall modal cover-modal-index" id="image-view-modal">
    <div class="header">이미지 뷰어</div>
    <div class="content img">
        <img src="https://t1.daumcdn.net/cfile/blog/1568434B50EF2C8C0D">
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">닫기</button>
        <a href="#" target="_blank" class="ui blue floated button">다운로드</a>
    </div>
</div>


<jsp:include page="/counsel/talk/modal-template"/>

<tags:scripts>
    <script>
        function deleteRoom(roomId) {
            restSelf.delete('/api/counsel/talk-remove-room/' + encodeURIComponent(roomId), null, function () {
            }, true).done(function () {
                loadTalkList('END');
                replaceReceivedHtmlInSilence('/counsel/talk/room/empty', '#talk-room');
            });
        }

        function loadTalkList(mode, disableNoti) {
            replaceReceivedHtmlInSilence('/counsel/talk/list-container?mode=' + mode + '&showNoti=' + (!disableNoti), '#talk-list-container-' + mode);
        }

        function loadTalkRoom(roomId, senderKey, userKey) {
            replaceReceivedHtmlInSilence('/counsel/talk/room/' + encodeURIComponent(roomId), '#talk-room');
            loadTalkCustomInput(null, null, roomId, senderKey, userKey);
        }

        function loadTalkCustomInput(maindbGroupSeq, customId, roomId, senderKey, userKey) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/talk/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), '#talk-custom-input');
        }

        function loadTalkCounselingInput(maindbGroupSeq, customId, roomId, senderKey, userKey) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/talk/counseling-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), '#talk-counseling-input');
        }

        function uploadTalkFile() {
            const room = $('#talk-room');
            const roomStatus = room.attr('data-status');
            const roomId = room.attr('data-id');
            const senderKey = room.attr('data-sender-key');
            const userKey = room.attr('data-user-key');

            if (!roomId || !senderKey || !userKey || roomStatus === 'E')
                return;

            window.open($.addQueryString(contextPath + '/counsel/talk/upload-file', {roomId: roomId, senderKey: senderKey, userKey: userKey}), '_blank');
        }

        function sendTalkMessage() {
            const room = $('#talk-room');
            const roomStatus = room.attr('data-status');
            const roomId = room.attr('data-id');
            const senderKey = room.attr('data-sender-key');
            const userKey = room.attr('data-user-key');
            const messageInput = $('#talk-message');

            const message = messageInput.val();

            if (!roomId || !senderKey || !userKey || !message || roomStatus === 'E')
                return;

            talkCommunicator.sendMessage(roomId, senderKey, userKey, message);
            messageInput.val('');
        }

        function processTalkMessage(data, event) {
            const room = $('#talk-room');
            const chatBody = room.find('.chat-body .os-content');
            const roomId = room.attr('data-id');

            if (roomId === data.room_id) {
                if (['SZ', 'SG'].indexOf(data.send_receive) >= 0) {
                } else if (['SE', 'RE'].indexOf(data.send_receive) >= 0) {
                    chatBody.append($('<p/>', {class: 'info-msg', text: '[' + data.cur_timestr + '] ' + data.content}));
                } else if (['AF', 'S', 'R'].indexOf(data.send_receive) >= 0) {
                    const myMessage = ['AF', 'S'].indexOf(data.send_receive) >= 0;

                    const item = $('<div/>', {class: 'chat-item ' + (myMessage ? 'chat-me' : '')}).appendTo(chatBody);
                    const content = $('<div/>', {class: 'wrap-content'}).appendTo(item)
                        .append($('<div/>', {class: 'txt-time', text: '[' + (myMessage ? data.username : data.customname) + '] ' + data.cur_timestr}));

                    const url = $.addQueryString(data.content, {token: '${g.escapeQuote(accessToken)}'});

                    if (data.type === 'photo') {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt_chat">' +
                            '            <img src="' + url + '">' +
                            '        </p>' +
                            '    </div>' +
                            '</div>' +
                            '<a href="' + url + '" target="_blank">저장하기</a>');
                    } else if (data.type === 'audio') {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt_chat">' +
                            '            <audio controls src="' + url + '"></audio>' +
                            '        </p>' +
                            '    </div>' +
                            '</div>' +
                            '<a href="' + url + '" target="_blank">저장하기</a>');
                    } else if (data.type === 'file') {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt_chat">' +
                            '            <a href="' + url + '" target="_blank">' + url + '</a>' +
                            '        </p>' +
                            '    </div>' +
                            '</div>' +
                            '<a href="' + url + '" target="_blank">저장하기</a>');
                    } else {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt_chat">' + data.content + '</p>' +
                            '    </div>' +
                            '</div>');
                    }
                } else {
                    chatBody.append($('<text/>', {text: 'unknown sendReceive: ' + data.send_receive}))
                        .append('<br/>')
                        .append($('<text/>', {text: data.content}));
                }

                room.find('.chat-body').overlayScrollbars().scroll({y: "100%"}, 100);
            }

            const talk = talkList[data.room_id];
            if (talk) {
                if (talk.user === '${g.escapeQuote(user.id)}') {
                    loadTalkList('MY');
                } else if (talk.user) {
                    loadTalkList('OTH');
                } else {
                    loadTalkList('TOT');
                }
            }

            if (data.userid === '${g.escapeQuote(user.id)}') {
                if (!talk || talk.user !== '${g.escapeQuote(user.id)}')
                    loadTalkList('MY');
            } else if (data.userid) {
                if (!talk || !talk.user)
                    loadTalkList('OTH');
            } else {
                if (!talk || talk.user)
                    loadTalkList('TOT');
            }

            if (event === 'svc_end')
                loadTalkList('END');
        }

        window.talkCommunicator = new TalkCommunicator()
            .on('svc_msg', processTalkMessage)
            .on('svc_control', processTalkMessage)
            .on('svc_end', processTalkMessage);

        $(window).on('load', function () {
            loadTalkList('MY', true);
            loadTalkList('TOT', true);
            loadTalkList('END', true);
            loadTalkList('OTH', true);

            loadTalkCustomInput();
        });

        $('#talk-message').keypress(e => {
            if (!e.shiftKey && e.keyCode === 13) {
                e.preventDefault();
                return sendTalkMessage();
            }
        });

        function viewToReply() {
            $('.view-to-reply').toggle();
        }

        function templateModal() {
            $('#messenger-template-add-popup').dragModalShow();
        }

        function knowledgeManagementModal() {
            $('#knowledge-management-modal').dragModalShow();
        }

        function viewImg() {
            $('#image-view-modal').dragModalShow();
        }

        function sideViewRoomModal() {
            $('#side-view-modal').dragModalShow();
        }



    </script>
</tags:scripts>
