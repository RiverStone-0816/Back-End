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

<div class="ui modal tiny" id="modal-messenger-room">
    <i class="close icon" onclick="messenger.closeRoom()"></i>
    <div class="header"><button type="button" class="room-title -chatroom-name">대화방이름</button></div>
    <div class="content">
        <div class="organi-chat-room-container">
            <div class="organi-chat-room-header">
                <div class="default-inner">
                    <div class="search-wrap">
                        <div class="ui icon input">
                            <input type="text" class="-search-text">
                            <i class="search link icon -search-icon" style="cursor: pointer;"></i>
                        </div>
                        <div class="search-count">
                            <text class="-text-count">0 / 0</text>
                        </div>
                    </div>
                    <div class="btn-wrap">
                        <button type="button" class="-move-to-prev-text"><img src="<c:url value="/resources/images/chat-search-up.svg"/>" alt="이전 검색단어"></button>
                        <button type="button" class="-move-to-next-text"><img src="<c:url value="/resources/images/chat-search-down.svg"/>" alt="다음 검색단어"></button>
                        <button type="button" class="-upload-file"><img src="<c:url value="/resources/images/chat-file.svg"/>" alt="파일 전송"></button>
                        <button type="button" onclick="messenger.popupRoomCreationOrganizationModal(false)"><img src="<c:url value="/resources/images/chat-user-add.svg"/>" alt="초대"></button>
                        <button type="button" class="-leave-room"><img src="<c:url value="/resources/images/chat-exit.svg"/>" alt="나가기"></button>
                    </div>
                </div>
                <div class="modify-inner">
                    <div class="input-wrap">
                        <div class="ui fluid input">
                            <input type="text" class="-chatroom-name-input">
                        </div>
                    </div>
                    <div class="btn-wrap">
                        <button type="button" class="ui button -cancel-chatroom-name">취소</button>
                        <button type="button" class="ui brand button -change-chatroom-name">변경</button>
                    </div>
                </div>
            </div>
            <div class="organi-chat-room-content">
                <div class="chat-body"></div>
                <div class="write-chat">
                    <textarea class="-messenger-message"></textarea>
                    <button type="button" class="send-btn" onclick="messenger.sendMessage();">전송</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function chatTitleModifyBtn() {
        const organiChatTitleDefault = modal.find('.default-inner');
        const organiChatTitleModify = modal.find('.modify-inner');

        if (organiChatTitleModify.css('display') === 'none') {
            organiChatTitleModify.css('display', 'flex');
            organiChatTitleDefault.css('display', 'none');
        } else {
            organiChatTitleModify.css('display', 'none');
            organiChatTitleDefault.css('display', 'flex');
            modal.find('.-chatroom-name-input').val('');
        }
    }

    modal.find('.-chatroom-name,.-cancel-chatroom-name').click(chatTitleModifyBtn);

    modal.find('.-change-chatroom-name').click(function () {
        const text = modal.find('.-chatroom-name-input').val().trim();
        restSelf.put('/api/chatt/' + messenger.currentRoom.id + '/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
            modal.find('.-chatroom-name').text(text);
            messenger.communicator.changeRoomName(roomId, text);
            chatTitleModifyBtn();
        });
    });

    modal.find('.-leave-room').click(function () {
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

    function search() {
        const keyword = modal.find('.-search-text').val().trim();
        if (!keyword) {
            messenger.clearSearching();
            return;
        }
        // if (keyword === messenger.currentRoom.searchingMessage)
        //     return modal.find('.-move-to-next-text').click();

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
    }

    modal.find('.-search-text').keyup(function (event) {
        if (event.keyCode !== 13)
            return;
        search();
    });

    modal.find('.-search-icon').click(function () {
        search();
    });

    modal.find('.-move-to-prev-text').click(function () {
        messenger._moveToText(parseInt(messenger.ui.searchingTextCountExpression.attr('data-index')));
    });

    modal.find('.-move-to-next-text').click(function () {
        messenger._moveToText(parseInt(messenger.ui.searchingTextCountExpression.attr('data-index')) - 2);
    });

    modal.find('.-upload-file').click(function () {
        if (messenger.currentRoom) {
            window.open($.addQueryString(contextPath + '/messenger-upload-file', {
                roomId: messenger.currentRoom.id,
                messengerSocketUrl: messenger.communicator.request.url
            }), '_blank', 'toolbar=0,location=0,menubar=0,height=100,width=200,left=100,top=100');
        }
    });

    modal.find('.-messenger-message').keydown(function (key) {
        if (key.keyCode === 13)
            if (!key.shiftKey) {
                messenger.sendMessage();
                return false;
            }
    });

    modal.find('.chat-body').scroll(function () {
        if ($(this).scrollTop())
            return;

        if (messenger.currentRoom)
            messenger.loadMessages({startMessageId: messenger.currentRoom.startMessageId, limit: messenger.READ_LIMIT + 1})
    });
</script>