<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
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
        <div class="chat-container">
            <div class="room" id="talk-room">
                <div class="chat-header"></div>
                <div class="chat-body"></div>
            </div>
            <div class="write-chat">
                <div class="write-menu">
                    <button type="button" class="mini ui button compact" onclick="templateSelectPopup()">템플릿</button>
                    <button type="button" class="mini ui button compact" onclick="uploadTalkFile()">파일전송</button>
                    <button type="button" class="mini ui button compact pull-right -assignUnassignedRoomToMe" style="display: none;"
                            onclick="talkCommunicator.assignUnassignedRoomToMe($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key')); $(this).hide()">
                        찜하기
                    </button>
                    <button type="button" class="mini ui button compact pull-right -assignAssignedRoomToMe" style="display: none;"
                            onclick="talkCommunicator.assignAssignedRoomToMe($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key')); $(this).hide()">
                        가져오기
                    </button>
                    <button type="button" class="mini ui button compact pull-right -deleteRoom" style="display: none;"
                            onclick="deleteRoom($('#talk-room').attr('data-id'))">
                        대화방내리기
                    </button>
                    <button type="button" class="mini ui button compact pull-right -downRoom" style="display: none;"
                            onclick="talkCommunicator.deleteRoom($('#talk-room').attr('data-id'), $('#talk-room').attr('data-sender-key'), $('#talk-room').attr('data-user-key'))">
                        대화방종료
                    </button>
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
    </script>
</tags:scripts>
