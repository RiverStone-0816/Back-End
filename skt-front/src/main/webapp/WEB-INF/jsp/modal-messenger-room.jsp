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
    <i class="close icon -close-room"></i>
    <div class="header"><span style="cursor: pointer;" onclick="chatTitleModifyBtn()" class="-chatroom-name">대화방이름</span></div>
    <div class="content">
        <div class="organi-chat-room-container">
            <div class="organi-chat-room-header">
                <div class="default-inner">
                    <div class="search-wrap">
                        <div class="ui icon input">
                            <input type="text" class="-search-text">
                            <i class="search link icon"></i>
                        </div>
                        <div class="search-count">
                            <text class="-text-count">0 / 0</text>
                        </div>
                    </div>
                    <div class="btn-wrap">
                        <button type="button" class="-move-to-prev-text"><img src="<c:url value="/resources/images/chat-search-up.svg"/>" alt="이전 검색단어"></button>
                        <button type="button" class="-move-to-next-text"><img src="<c:url value="/resources/images/chat-search-down.svg"/>" alt="다음 검색단어"></button>
                        <button type="button" class="-upload-file"><img src="<c:url value="/resources/images/chat-file.svg"/>" alt="파일 전송"></button>
                        <button type="button" class="-invite-to-room"><img src="<c:url value="/resources/images/chat-user-add.svg"/>" alt="초대"></button>
                        <button type="button" class="-leave-room"><img src="<c:url value="/resources/images/chat-exit.svg"/>" alt="나가기"></button>
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

    function hideInvitationPanel() {
        const panel = modal.find('.invitation-panel');
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

    modal.find('.-invite-to-room').click(function () {
        const users = [];
        const userNames = [];
        const userMap = {};

        modal.find('.-messenger-user').filter('.active').removeClass('active').each(function () {
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

    modal.find('.-close-room').click(function () {
        messenger.closeRoom();
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

    modal.find('.-search-text').keyup(function (event) {
        if (event.keyCode !== 13)
            return;

        const keyword = $(this).val().trim();
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

    (function () {
        const scrollContainer = modal.find('.chat-body');

        scrollContainer.scroll(function () {
            if ($(this).scrollTop())
                return;

            if (messenger.currentRoom)
                messenger.loadMessages({startMessageId: messenger.currentRoom.startMessageId, limit: messenger.READ_LIMIT + 1})
        });
    })();

</script>
