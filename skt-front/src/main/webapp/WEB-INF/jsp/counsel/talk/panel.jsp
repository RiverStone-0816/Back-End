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

<div class="ui bottom attached tab segment remove-margin remove-padding border-top-none" data-tab="talk-panel">
    <div class="display-flex flex-flow-row full-height">
        <div class="top-chat-list-wrap display-flex" id="talk-list-panel">
            <div>
                <div class="ui top attached tabular menu light flex">
                    <button class="item active" data-tab="talk-list-type-MY" onclick="$(this).removeClass('newImg_c')">내대화목록(<span></span>)</button>
                    <button class="item" data-tab="talk-list-type-TOT" onclick="$(this).removeClass('newImg_c')">신규대화목록(<span></span>)</button>
                    <button class="item" data-tab="talk-list-type-OTH" onclick="$(this).removeClass('newImg_c')">타상담목록(<span></span>)</button>
                    <button class="item" data-tab="talk-list-type-END" onclick="$(this).removeClass('newImg_c')">재분배대상(<span></span>)</button>
                </div>
            </div>
            <div class="flex-100">
                <div class="ui bottom attached tab segment remove-margin remove-padding active" data-tab="talk-list-type-MY">
                    <div id="talk-list-container-MY" ></div>
                </div>
                <div class="ui bottom attached tab segment remove-margin remove-padding" data-tab="talk-list-type-TOT">
                    <div id="talk-list-container-TOT"></div>
                </div>
                <div class="ui bottom attached tab segment remove-margin remove-padding" data-tab="talk-list-type-OTH">
                    <div id="talk-list-container-OTH"></div>
                </div>
                <div class="ui bottom attached tab segment remove-margin remove-padding" data-tab="talk-list-type-END">
                    <div id="talk-list-container-END"></div>
                </div>
            </div>
        </div>
        <div class="btm-room-wrap">
            <div class="chat-container">
                <div class="room">
                    <div class="chat-header" id="talk-chat-header"></div>
                    <div class="chat-search-btn-wrap">
                        <button type="button" class="chat-search-btn">
                            <img src="<c:url value="/resources/images/chat-search-icon.svg"/>">
                        </button>
                    </div>
                    <div class="chat-search-wrap">
                        <div class="chat-search-control">
                            <div class="control-inner">
                                <input type="text">
                                <div class="keyword-count">99/99</div>
                                <button type="button"><img src="<c:url value="/resources/images/chat-arrow-up-icon.svg"/>"></button>
                                <button type="button"><img src="<c:url value="/resources/images/chat-arrow-down-icon.svg"/>"></button>
                            </div>
                        </div>
                        <button type="button" class="chat-search-close-btn">
                            <img src="<c:url value="/resources/images/chat-find-close-icon.svg"/>">
                        </button>
                    </div>
                    <div class="chat-body -overlay-scroll" id="talk-chat-body">
                        <div class="chat-item">
                            <div class="wrap-content">
                                <div class="profile-image">
                                    <img src="/resources/images/kd_2.png">
                                </div>
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        [미등록고객] 2022-01-24 13:03:35
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <div class="chat-more-nav">
                                                <button type="button">
                                                    <img src="<c:url value="/resources/images/chat-more-template-icon.svg"/>">템플릿
                                                </button>
                                                <button type="button">
                                                    <img src="<c:url value="/resources/images/chat-more-knowledge-icon.svg"/>">지식관리
                                                </button>
                                                <button type="button">
                                                    <img src="<c:url value="/resources/images/chat-more-dual-icon.svg"/>">듀얼창
                                                </button>
                                            </div>
                                            <pre class="txt-chat">ㄴㅇㄹㄴㅇ</pre>
                                            <button type="button" class="chat-reply-icon"><img src="<c:url value="/resources/images/chat-reply-icon.svg"/>"></button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="chat-item chat-me">
                            <div class="wrap-content">
                                <div class="profile-image">
                                    <img src="/resources/images/kd_2.png">
                                </div>
                                <div class="txt-segment">
                                    <div class="txt-time">
                                        [유도윤] 2022-01-20 13:02:06
                                    </div>
                                    <div class="chat">
                                        <div class="bubble">

                                            <%-- 일반메시지 답장 --%>
                                            <div class="reply-content-container">
                                                <div class="reply-content">
                                                    <div class="target-user">[홍길동]에게 답장</div>
                                                    <div class="target-msg">테스트 메시지 메시지</div>
                                                </div>
                                            </div>
                                            <pre class="txt-chat">반갑습니다 고객님 무엇을 도와드릴까요?</pre>

                                            <%-- 파일 답장 --%>
                                           <%-- <div class="reply-content-container">
                                                <div class="reply-content">
                                                    <div class="target-user">[홍길동]에게 답장</div>
                                                    <div class="target-msg">abc.hwp</div>
                                                </div>
                                            </div>
                                            <pre class="txt-chat">반갑습니다 고객님 무엇을 도와드릴까요?</pre>--%>

                                            <%-- 사진 답장 --%>
                                            <%--<div class="reply-content-container">
                                                <div class="reply-content photo">
                                                    <img src="https://t1.daumcdn.net/cfile/tistory/9933673A5E604C052F">
                                                </div>
                                                <div class="reply-content">
                                                    <div class="target-user">[홍길동]에게 답장</div>
                                                    <div class="target-msg">사진</div>
                                                </div>
                                            </div>
                                            <pre class="txt-chat">반갑습니다 고객님 무엇을 도와드릴까요?</pre>--%>


                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%--일반 메시지 답장--%>
                        <div class="view-to-reply">
                            <div class="target-text">
                                <p class="target-user">[홍길동]에게 답장</p>
                                <p class="target-content">메시지메시지메시지메시지메시지메시지메시지메시지</p>
                            </div>
                            <div class="target-close">
                                <img src="<c:url value="/resources/images/chat-reply-close-icon.svg"/>">
                            </div>
                        </div>

                        <%--파일 답장--%>
                        <%--<div class="view-to-reply">
                            <div class="target-text">
                                <p class="target-user">[홍길동]에게 답장</p>
                                <p class="target-content">abc.hwp</p>
                            </div>
                            <div class="target-close">
                                <img src="<c:url value="/resources/images/chat-reply-close-icon.svg"/>">
                            </div>
                        </div>--%>

                        <%--사진 답장--%>
                        <%--<div class="view-to-reply">
                            <div class="target-image">
                                <img src="https://t1.daumcdn.net/cfile/tistory/9933673A5E604C052F" class="target-image-content">
                            </div>
                            <div class="target-text">
                                <p class="target-user">[홍길동]에게 답장</p>
                                <p class="target-content">사진</p>
                            </div>
                            <div class="target-close">
                                <img src="<c:url value="/resources/images/chat-reply-close-icon.svg"/>">
                            </div>
                        </div>--%>
                    </div>
                    <div class="write-chat">
                        <div class="wrap-inp">
                            <div class="inp-box">
                                <textarea id="talk-message" placeholder="전송하실 메시지를 입력하세요."></textarea>
                            </div>
                            <button type="button" class="send-btn" onclick="sendTalkMessage()">전송</button>
                        </div>
                        <div class="write-menu">
                            <div>
                                <button type="button" onclick="templateSelectPopup()" data-inverted="" data-tooltip="템플릿" data-position="top center"><img src="<c:url value="/resources/images/chat-template-import-icon.svg"/>"></button>
                                <button type="button" onclick="uploadTalkFile()" data-inverted="" data-tooltip="파일첨부" data-position="top center"><img src="<c:url value="/resources/images/chat-file-icon.svg"/>"></button>
                                <button type="button" data-inverted="" data-tooltip="찜하기" data-position="top center" class="-assignUnassignedRoomToMe" style="display: none;"
                                        onclick="talkCommunicator.assignUnassignedRoomToMe($('#talk-chat-body').attr('data-id'), $('#talk-chat-body').attr('data-sender-key'), $('#talk-chat-body').attr('data-user-key')); $(this).hide()">
                                    <img src="<c:url value="/resources/images/chat-reservation-icon.svg"/>">
                                </button>
                                <button type="button" data-inverted="" data-tooltip="가져오기" data-position="top center" class="-assignAssignedRoomToMe" style="display: none;"
                                        onclick="talkCommunicator.assignAssignedRoomToMe($('#talk-chat-body').attr('data-id'), $('#talk-chat-body').attr('data-sender-key'), $('#talk-chat-body').attr('data-user-key')); $(this).hide()">
                                    <img src="<c:url value="/resources/images/chat-import-icon.svg"/>">
                                </button>
                                <button type="button" data-inverted="" data-tooltip="대화방 내리기" data-position="top center" class="-deleteRoom" style="display: none;"
                                        onclick="deleteRoom($('#talk-chat-body').attr('data-id'))">
                                    <img src="<c:url value="/resources/images/chat-down-icon.svg"/>">
                                </button>
                            </div>
                            <div>
                                <button type="button"  data-inverted="" data-tooltip="대화종료" data-position="top right" class="-downRoom" style="display: none;"
                                        onclick="talkCommunicator.deleteRoom($('#talk-chat-body').attr('data-id'), $('#talk-chat-body').attr('data-sender-key'), $('#talk-chat-body').attr('data-user-key'))">
                                    <img src="<c:url value="/resources/images/chat-exit-icon.svg"/>">
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/counsel/talk/modal-template"/>

<tags:scripts>
    <script>

        $('.chat-item:not(.chat-me) .txt-segment .chat').mouseenter(function(){
            $('.chat-reply-icon').show();
        });

        $('.chat-item:not(.chat-me) .txt-segment .chat').mouseleave(function(){
            $('.chat-reply-icon').hide();
        });

        function deleteRoom(roomId) {
            restSelf.delete('/api/counsel/talk-remove-room/' + encodeURIComponent(roomId), null, function () {
            }, true).done(function () {
                loadTalkList('END');
                replaceReceivedHtmlInSilence('/counsel/talk/room/empty', '#talk-chat-body').done(function () {
                    $('#talk-chat-header').text('');
                    $('.-talk-list').removeClass('active');
                });
            });
        }

        function loadTalkList(mode, disableNoti) {
            replaceReceivedHtmlInSilence('/counsel/talk/list-container?mode=' + mode + '&showNoti=' + (!disableNoti), '#talk-list-container-' + mode);
        }

        function loadTalkRoom(roomId, senderKey, userKey) {
            replaceReceivedHtmlInSilence('/counsel/talk/room/' + encodeURIComponent(roomId), '#talk-chat-body').done(function () {
                const body = $('#talk-chat-body');
                $('#talk-chat-header').text('[' + body.attr('data-status') + ']-' + body.attr('data-room-name'));
                $('.-talk-list').removeClass('active').filter('[data-id="' + roomId + '"]').addClass('active');
            });
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
            const room = $('#talk-chat-body');
            const roomStatus = room.attr('data-status');
            const roomId = room.attr('data-id');
            const senderKey = room.attr('data-sender-key');
            const userKey = room.attr('data-user-key');

            if (!roomId || !senderKey || !userKey || roomStatus === 'E')
                return;

            window.open($.addQueryString(contextPath + '/counsel/talk/upload-file', {roomId: roomId, senderKey: senderKey, userKey: userKey}), '_blank');
        }

        function sendTalkMessage() {
            const room = $('#talk-chat-body');
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
            const room = $('#talk-chat-body');
            const chatBody = room.find('.os-content');
            const roomId = room.attr('data-id');

            if (roomId === data.room_id) {
                if (['SZ', 'SG'].indexOf(data.send_receive) >= 0) {
                } else if (['SE', 'RE'].indexOf(data.send_receive) >= 0) {
                    chatBody.append($('<p/>', {class: 'info-msg', text: '[' + data.cur_timestr + '] ' + data.content}));
                } else if (['AF', 'S', 'R'].indexOf(data.send_receive) >= 0) {
                    const myMessage = ['AF', 'S'].indexOf(data.send_receive) >= 0;

                    const item = $('<div/>', {class: 'chat-item ' + (myMessage ? 'chat-me' : '')}).appendTo(chatBody);

                    const contentContainer = $('<div class="wrap-content">').appendTo(item);
                    contentContainer.append('<div class="profile-image"><img src="/resources/images/profile1.png"></div>')

                    const content = $('<div/>', {class: 'txt-segment'}).appendTo(contentContainer)
                        .append($('<div/>', {class: 'txt-time', text: '[' + (myMessage ? data.username : data.customname) + '] ' + data.cur_timestr}));

                    const url = $.addQueryString(data.content, {token: '${g.escapeQuote(accessToken)}'});

                    if (data.type === 'photo') {
                        <%--fixme: 코드 정리--%>
                        content
                            .append('<div class="chat">' +
                                '    <div class="bubble">' +
                                '        <p class="txt-chat" style="width: 118px">' +
                                '            <img src="' + url + '">' +
                                '        </p>' +
                                '    </div>' +
                                '</div>')
                            .append('<a href="' + url + '" target="_blank">저장하기</a>');
                    } else if (data.type === 'audio') {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt-chat">' +
                            '            <audio controls src="' + url + '"></audio>' +
                            '        </p>' +
                            '    </div>' +
                            '</div>' +
                            '<a href="' + url + '" target="_blank">저장하기</a>');
                    } else if (data.type === 'file') {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt-chat">' +
                            '            <a href="' + url + '" target="_blank">' + url + '</a>' +
                            '        </p>' +
                            '    </div>' +
                            '</div>' +
                            '<a href="' + url + '" target="_blank">저장하기</a>');
                    } else {
                        content.append('<div class="chat">' +
                            '    <div class="bubble">' +
                            '        <p class="txt-chat">' + data.content + '</p>' +
                            '    </div>' +
                            '</div>');
                    }
                } else {
                    chatBody.append($('<text/>', {text: 'unknown sendReceive: ' + data.send_receive}))
                        .append('<br/>')
                        .append($('<text/>', {text: data.content}));
                }

                room.overlayScrollbars().scroll({y: "100%"}, 100);
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

        $('.chat-search-btn').click(function(){
            $('.chat-search-wrap').toggleClass('active');
        });



    </script>
</tags:scripts>
