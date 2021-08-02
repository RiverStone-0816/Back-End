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
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<c:set var="CHATTABLE" value="${serviceKind.equals('SC') && usingServices.contains('CHATT')}"/>

<div class="ui modal tiny" id="user-info-modal">
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
                    <button type="button" class="-hp-number"></button>
                </td>
            </tr>
            <tr>
                <th>내선번호</th>
                <td class="row-btn-wrap">
                    <text class="-field" data-name="extension"></text>
                    <button type="button" class="-extension"></button>
                </td>
            </tr>
        </table>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">닫기</button>
    </div>
</div>

<div class="ui modal" id="room-creation-organization-modal">
    <i class="close icon"></i>
    <div class="header">
        <text class="-modal-title"></text>
    </div>
    <div class="content">
        <div class="organization-ul-wrap" id="room-creation-organization-panel">
            <ul class="organization-ul modal"></ul>
        </div>
    </div>
    <div class="actions">
        <span class="left-txt">선택된 조직원 <text class="-selected-user-count">0</text>명</span>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui orange button modal-close -submit">확인</button>
    </div>
</div>

<div class="consult-left-panel">
    <div class="panel consult-organization-panel full-height">
        <div class="panel-heading">
            <text>조직도</text>
            <div class="btn-wrap">
                <button type="button" class="ui basic button" id="organi-state">현황</button>
                <button type="button" class="ui basic button" id="organi-room">
                    <text class="message-indicator">0</text>
                    <text>대화방</text>
                </button>
            </div>
            <div class="state-header">현황</div>
            <button class="state-header-close"></button>
        </div>
        <div class="panel-body remove-pl remove-pr">
            <div class="panel-segment favor">
                <div class="panel-segment-header">
                    <text>즐겨찾기</text>
                    <button type="button" class="ui basic mini button" onclick="messenger.popupBookmarkModal()">편집</button>
                </div>
                <div class="panel-segment-body overflow-overlay">
                    <div class="area">
                        <ul class="organization-ul border-bottom-none remove-padding" id="messenger-bookmark-panel"></ul>
                        <div class="empty">즐겨찾기가 없습니다.</div>
                    </div>
                </div>
            </div>

            <div class="panel-segment list">
                <div class="panel-segment-header">
                    <text>조직도</text>
                    <button type="button" class="ui basic mini button" onclick="messenger.openRoomFromOrganization()">선택대화</button>
                </div>
                <div class="panel-segment-body overflow-overlay">
                    <div class="area" id="messenger-organization-panel"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="organi-pop-wrap">
        <div class="organi-pop-second">
            <div class="panel full-height">
                <div class="panel-heading">
                    <text>조직 대화방</text>
                    <button type="button" class="panel-close"></button>
                </div>
                <div class="panel-body full-height remove-padding">
                    <div class="pd10 panel-border-bottom">
                        <div class="ui fluid icon input">
                            <input id="messenger-filter-text" type="text" placeholder="검색어 입력" onkeyup="messenger.filterItem(); return false;"/>
                            <i class="search link icon"></i>
                        </div>
                    </div>
                    <div class="pd10 full-height">
                        <div class="organization-chat-list-wrap">
                            <div class="chat-list-header">
                                <text>조직 대화방 목록</text>
                                <button type="button" class="ui basic right floated button" onclick="messenger.popupRoomCreationOrganizationModal(true, false)">추가</button>
                            </div>
                            <div class="chat-list-body">
                                <div class="chat-list-container">
                                    <ul id="messenger-chat-container"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
<tags:scripts>
    <script>
        const leftPanel = $('.consult-left-panel');
        $('#organi-state').click(function () {
            leftPanel.toggleClass('wide');
            $('.consult-wrapper .consult-center-panel').toggleClass('control');
            if (leftPanel.hasClass('wide') === true) {
                $('.content-inner.-counsel-content-panel').addClass('control');
            } else {
                $('.content-inner.-counsel-content-panel').removeClass('control');
            }
        });

        $('.panel-segment.favor').resizable({minHeight: 88});

        $('.consult-organization-panel .panel-heading .ui.basic.button').click(function () {
            $(this).toggleClass('active');
        });

        $('.consult-organization-panel .state-header-close').click(function () {
            leftPanel.removeClass('wide');
            $('#organi-state').removeClass('active');
            $('.consult-wrapper .consult-center-panel').removeClass('control');
            if (leftPanel.hasClass('wide') === true) {
                $('.content-inner.-counsel-content-panel').addClass('control');
            } else {
                $('.content-inner.-counsel-content-panel').removeClass('control');
            }
        });

        $('.organi-pop-second .panel-close').click(function () {
            $('.consult-organization-panel #organi-room').removeClass('active');
            $(this).parents('.organi-pop-second').hide();
        });

        $('#organi-room').click(function () {
            <c:if test="${CHATTABLE}">
            $('.organi-pop-second').toggle();
            </c:if>

            <c:if test="${!CHATTABLE}">
            alert('메신저 라이센스가 없습니다.');
            </c:if>
        });

        function Messenger() {
            const messenger = this;

            messenger.me = '${g.escapeQuote(user.id)}';
            messenger.accessToken = '${g.escapeQuote(accessToken)}';
            messenger.groupToUsers = {};
            messenger.users = {};

            messenger.ui = {
                filterInput: $('#messenger-filter-text'),
                organizationPanel: $('#messenger-organization-panel'),
                bookmarkPanel: $('#messenger-bookmark-panel'),
                chatContainer: $('#messenger-chat-container'),
                roomCreationOrganizationPanel: $('#room-creation-organization-panel'),
            };

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

            function writeGroupLogInOutUserCount(userid) {
                if (!messenger.users[userid])
                    return;

                const group = messenger.groupToUsers[messenger.users[userid].groupCode];
                if (group) {
                    let logoutCount = 0;
                    let loginCount = 0;

                    group.map(function (e) {
                        if (messenger.users[e].isLoginChatt === 'L') loginCount++;
                        else logoutCount++;
                    });

                    $('.-group-logout-user-count[data-group="' + messenger.users[userid].groupCode + '"]').text(logoutCount);
                    $('.-group-login-user-count[data-group="' + messenger.users[userid].groupCode + '"]').text(loginCount);
                }
            }

            messenger.communicator = new MessengerCommunicator()
                .on('svc_login', function (data) {
                    const userid = data.userid;

                    $('.-messenger-user,.-messenger-bookmark').each(function () {
                        const $this = $(this);

                        if ($this.attr('data-id') !== userid)
                            return;

                        $this.find('.user-icon').addClass('active');
                    });

                    if (messenger.users[userid])
                        messenger.users[userid].isLoginChatt = 'L';

                    writeGroupLogInOutUserCount(userid);
                })
                .on('svc_logout', function (data) {
                    const userid = data.userid;

                    $('.-messenger-user,.-messenger-bookmark').each(function () {
                        const $this = $(this);

                        if ($this.attr('data-id') !== userid)
                            return;

                        $this.find('.user-icon').removeClass('active');
                    });

                    if (messenger.users[userid])
                        messenger.users[userid].isLoginChatt = 'N';

                    writeGroupLogInOutUserCount(userid);
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

                        messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
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
                    messenger.rooms[roomId].roomName = data.change_room_name;

                    const chattingRoomElement = messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
                        return $(this).attr('data-id') === roomId;
                    });
                    chattingRoomElement.find('.-room-name').text(messenger.rooms[roomId].roomName);
                    if (messenger.currentRoom && messenger.currentRoom.id === roomId && messenger.ui.roomName)
                        messenger.ui.roomName.text(messenger.rooms[roomId].roomName);
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

                const chattingRoomElement = messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
                    return $(this).attr('data-id') === roomId;
                });
                chattingRoomElement.find('.-room-name').text(messenger.rooms[roomId].roomName);
            });
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
        Messenger.prototype.popupRoomCreationOrganizationModal = function (isCreatingNewRoom, withSelectedOfOrganization) {
            const modal = $('#room-creation-organization-modal').dragModalShow();

            modal.find('.-messenger-folder input[type=checkbox]:first').prop('checked', false);
            modal.find('.-messenger-user').removeClass('active');
            modal.find('.-modal-title').text(isCreatingNewRoom ? '새로운 채팅방 만들기' : '사용자 초대하기');
            modal.find('.-submit').attr('onclick', isCreatingNewRoom ? 'messenger.openRoom()' : 'messenger.inviteNewUsers()');

            if (withSelectedOfOrganization) {
                messenger.ui.organizationPanel.find('.-messenger-folder').each(function () {
                    modal.find('.-messenger-folder[data-id="' + $(this).attr('data-id') + '"] input[type=checkbox]:first').prop('checked', $(this).find('input[type=checkbox]:first').is(':checked'));
                });

                messenger.ui.organizationPanel.find('.-messenger-user').each(function () {
                    const item = modal.find('.-messenger-user[data-id="' + $(this).attr('data-id') + '"]');
                    if ($(this).hasClass('active'))
                        item.addClass('active');
                    else
                        item.removeClass('active');
                });

                messenger.ui.bookmarkPanel.find('.-messenger-bookmark').each(function () {
                    const item = modal.find('.-messenger-user[data-id="' + $(this).attr('data-id') + '"]');
                    if ($(this).hasClass('active'))
                        item.addClass('active');
                });
            }

            showRoomCreationOrganizationModalSelectedUserCount();
        };
        Messenger.prototype.inviteNewUsers = function () {
            const messenger = this;
            const users = [];
            const userNames = [];
            const userMap = {};

            messenger.ui.roomCreationOrganizationPanel.find('.-messenger-folder').filter(function () {
                return $(this).find('input[type=checkbox]:first').is(':checked');
            }).each(function () {
                const groupCode = $(this).attr('data-id');
                messenger.ui.roomCreationOrganizationPanel.find('.-messenger-user[data-group="' + groupCode + '"]').each(function () {
                    $(this).find('.-messenger-user').each(function () {
                        const id = $(this).attr('data-id');
                        if (users.indexOf(id) >= 0)
                            return;
                        users.push(id);
                    });
                });
            });

            messenger.ui.roomCreationOrganizationPanel.find('.-messenger-user.active').each(function () {
                const id = $(this).attr('data-id');
                if (users.indexOf(id) >= 0)
                    return;
                users.push(id);
                userNames.push(messenger.users[id].idName);
                userMap[id] = messenger.users[id].idName;
            });

            if (!users.length)
                return alert('선택된 사용자가 없습니다.');

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
        };
        // 조직도(left panel)에서 대화방 열기를 시도한다.
        Messenger.prototype.openRoomFromOrganization = function () {
            const messenger = this;
            const users = [];

            messenger.ui.organizationPanel.find('.-messenger-folder').filter(function () {
                return $(this).find('input[type=checkbox]:first').is(':checked');
            }).each(function () {
                $(this).find('.-messenger-user').each(function () {
                    const id = $(this).attr('data-id');
                    if (users.indexOf(id) >= 0)
                        return;
                    users.push(id);
                });
            });

            messenger.ui.organizationPanel.find('.-messenger-user.active').each(function () {
                const id = $(this).attr('data-id');
                if (users.indexOf(id) >= 0)
                    return;
                users.push(id);
            });

            if (!users.length)
                return alert('선택된 사용자가 없습니다.');

            if (users.indexOf(messenger.me) < 0)
                users.push(messenger.me);

            restSelf.post('/api/chatt/', {memberList: users}).done(function (response) {
                messenger.loadRoom(response.data);
            });
        };
        // 조직도(modal)에서 대화방 열기를 시도한다.
        Messenger.prototype.openRoom = function () {
            const messenger = this;
            const users = [];

            messenger.ui.roomCreationOrganizationPanel.find('.-messenger-folder').filter(function () {
                return $(this).find('input[type=checkbox]:first').is(':checked');
            }).each(function () {
                $(this).find('.-messenger-user').each(function () {
                    const id = $(this).attr('data-id');
                    if (users.indexOf(id) >= 0)
                        return;
                    users.push(id);
                });
            });

            messenger.ui.roomCreationOrganizationPanel.find('.-messenger-user.active').each(function () {
                const id = $(this).attr('data-id');
                if (users.indexOf(id) >= 0)
                    return;
                users.push(id);
            });

            if (!users.length)
                return alert('선택된 사용자가 없습니다.');

            if (users.indexOf(messenger.me) < 0)
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
                    return $('<div/>', {class: 'event-txt -chat-message -system-message', text: '[' + timeString + '] ' + contents, 'data-id': messageId, 'data-time': time});

                if (['AF', 'S', 'R'].indexOf(sendReceive) >= 0) {
                    const item = $('<div/>', {
                        class: 'chat-item -chat-message ' + (messageType === 'info' ? 'system -system-message ' : '') + (myMessage ? 'chat-me' : ''),
                        'data-id': messageId,
                        'data-time': time
                    });
                    const wrapContent = $('<div/>', {class: 'wrap-content'}).appendTo(item);
                    const txtSegment = $('<div/>', {class: 'txt-segment'}).appendTo(wrapContent)
                        .append($('<div/>', {class: 'txt-time', text: '[' + (messageType === 'info' ? '시스템' : username) + '] ' + timeString}));
                    const chat = $('<div class="chat">').appendTo(txtSegment);
                    const bubble = $('<div class="bubble">').appendTo(chat)
                        .append($('<span/>', {class: 'count -unread-count', text: unreadCount || ''}));
                    const txtChat = $('<div class="txt-chat">').appendTo(bubble);

                    if (messageType === 'file') {
                        const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(contents);
                        // const url = $.addQueryString(contents, {token: messenger.accessToken});
                        const url = $.addQueryString(split && split[4] || '', {token: messenger.accessToken});

                        if (split[1].endsWith('g'))
                            txtChat.append($('<img/>', {src: url}));
                        else if (split[1].contains('wav') || split[1].contains('mp'))
                            txtChat.append($('<audio/>', {controls: 'controls', src: url, css: {height: '35px'}}));
                        else
                            txtChat.append(
                                $('<a/>', {href: url, target: '_blank'})
                                    .append('<i class="paperclip icon"></i>')
                                    .append($('<text/>', {text: ' ' + (split && split[2] || '')}))
                                    .append('<div style="opacity: 50%; font-size: smaller; padding: 0 0.5em 1em;">')
                                    .append($('<text/>', {text: ' ' + (split && split[3] || '')}))
                            )
                        $('<a/>', {href: url, target: '_blank'})
                            .append($('<i/>', {class: 'file icon'}))
                            .append($('<text/>', {text: '저장하기'}))
                            .appendTo(txtSegment);
                    } else {
                        txtChat.text(contents);
                    }

                    return item;
                }

                return $('<text/>', {text: 'unknown sendReceive: ' + sendReceive, class: '-chat-message -unknown-message ', 'data-id': messageId, 'data-time': time})
                    .append('<br/>')
                    .append($('<text/>', {text: contents}));
            })();

            const chatBody = messenger.ui.room.find('.chat-body');

            if (messenger.currentRoom.startMessageTime >= time) {
                messenger.currentRoom.startMessageTime = time;
                messenger.currentRoom.startMessageId = messageId;
                chatBody.prepend(item);
            } else if (messenger.currentRoom.endMessageTime <= time) {
                messenger.currentRoom.endMessageTime = time;
                messenger.currentRoom.endMessageId = messageId;
                chatBody.append(item);

                const scrollBody = messenger.ui.room.find('.chat-body');
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

            const existChatItem = messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
                return $(this).attr('data-id') === roomId;
            });

            if (messenger.currentRoom && messenger.currentRoom.id === roomId && messenger.ui.roomName)
                messenger.ui.roomName.text(roomName);

            if (existChatItem.length > 0) {
                existChatItem.find('.-room-name').text(roomName);
                existChatItem.find('.-last-message-time').text(timeString).attr('data-value', lastTime);
                const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(lastMsg);
                existChatItem.find('.-last-message').text(split ? '[파일전송]' + split[2] : lastMsg);
                existChatItem.find('.-unread-count').text(unreadMessageTotalCount).css('display', !unreadMessageTotalCount ? 'none' : '');
                return;
            }

            $('<li/>', {class: 'item -messenger-chat-item', 'data-id': roomId})
                .append(
                    $('<div/>', {class: 'item-header'})
                        .append($('<button/>', {
                            class: 'chat-item-title -room-name', text: roomName, type: 'button', onclick: 'return false;', click: function () {
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
                        .append($('<div/>', {class: 'chat-unread -unread-count', text: unreadMessageTotalCount, style: 'display: ' + (!unreadMessageTotalCount ? 'none' : '')}))
                )
                .append(
                    $('<div/>', {class: 'item-content'})
                        .append(
                            $('<div/>', {class: 'last-chat'})
                                // .append($('<img/>', {src: '<c:url value="/resources/images/chat-system.svg"/>'}))
                                .append($('<text/>', {class: '-last-message', text: lastMsg}))
                        )
                        .append($('<div/>', {class: 'last-time -last-message-time', text: timeString, 'data-value': lastTime}))
                )
                .appendTo(messenger.ui.chatContainer)
                .click(function () {
                    messenger.loadRoom(roomId);
                });
        };
        Messenger.prototype.filterItem = function () {
            const messenger = this;

            const text = messenger.ui.filterInput.val().trim();
            messenger.ui.chatContainer.find('.-messenger-chat-item').each(function () {
                if ($(this).text().indexOf(text) >= 0) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        };
        // 안 읽은 메세지 총 갯수를 적는다.
        Messenger.prototype._showMessageIndicator = function () {
            const messenger = this;

            let unreadCount = 0;
            values(messenger.rooms).map(function (e) {
                unreadCount += e.unreadMessageTotalCount;
            });
            const indicator = $('.message-indicator');
            indicator.show().text(unreadCount);
            if (!unreadCount)
                indicator.hide();
        };
        Messenger.prototype._sortChatListItem = function () {
            const messenger = this;

            let items = [];
            messenger.ui.chatContainer.find('.-messenger-chat-item').detach().each(function () {
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

            messenger.clearSearching();
            delete messenger.currentRoom;

            delete messenger.ui.room.remove();
            delete messenger.ui.roomName;
            delete messenger.ui.searchingTextCountExpression;
            delete messenger.ui.messageInput;
            delete messenger.ui.roomMembers;
        };
        // 채팅방 목록을 불러온다.
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
                                .append($('<span/>', {class: 'user-icon ' + (e.isLoginChatt === 'L' ? 'active' : '')}))
                                .append($('<text/>', {text: e.idName}))
                        )
                        .append(
                            $('<div/>', {class: 'btn-wrap'})
                                .append($('<span/>', {class: 'ui mini label -consultant-status-with-color', 'data-peer': e.peer, css: {visibility: e.peer ? 'visible' : 'hidden'}}))
                                .append(
                                    $('<div/>', {class: 'buttons'})
                                        .append($('<button/>', {
                                            type: 'button',
                                            class: 'arrow button',
                                            'data-inverted': '',
                                            'data-tooltip': '호전환',
                                            'data-position': 'bottom center',
                                            onclick: 'ipccCommunicator.redirect(' + e.extension + ')'
                                        }))
                                        .append($('<button/>', {
                                            type: 'button', class: 'talk ${user.isTalk.equals('Y') ? 'on' : 'off'} button', click: function () {
                                                <c:if test="${user.isTalk.equals('Y')}">
                                                if ($('#organi-state').hasClass('active'))
                                                    $('#organi-state').click();
                                                if ($('#organi-room').hasClass('active'))
                                                    $('#organi-room').click();

                                                $('.-counsel-content-panel .item[data-tab="talk-panel"]').click();
                                                $('.item[data-tab="talk-list-type-OTH"]').click();

                                                const ui = $('#talk-list-panel');
                                                ui.find('.-search-type').val('userId');
                                                ui.find('.-search-value').val(e.id);
                                                ui.find('.-search-button').click();
                                                </c:if>
                                                <c:if test="${!user.isTalk.equals('Y')}">
                                                alert('상담톡 권한이 없습니다.');
                                                </c:if>
                                            }
                                        }))
                                        .append($('<button/>', {
                                            type: 'button', class: 'info button', 'data-inverted': '', 'data-tooltip': '정보', 'data-position': 'bottom center', click: function () {
                                                const modal = $('#user-info-modal');
                                                e.groupName = messenger.users[e.id].groupName;
                                                modal.find('.-field').each(function () {
                                                    $(this).text(e[$(this).attr('data-name')]);
                                                });
                                                modal.find('.-extension').attr('onclick', e.extension ? 'ipccCommunicator.clickDial("", "' + e.extension + '")' : '');
                                                modal.find('.-hp-number').attr('onclick', e.hpNumber ? 'ipccCommunicator.clickDial("", "' + e.hpNumber + '")' : '');
                                                modal.dragModalShow();
                                            }
                                        }))
                                )
                        )
                        .append(
                            $('<div/>', {class: 'state-wrap'})
                                .append($('<text/>', {text: '전화'}))
                                .append($('<span/>', {class: 'num -user-total-call-count', 'data-id': e.id, text: '0'}))
                                .append($('<text/>', {text: '채팅'}))
                                .append($('<span/>', {class: 'num -user-total-chat-count', 'data-id': e.id, text: '0'}))
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
                            const user = $(this).attr('data-id');
                            if (user === messenger.me)
                                return;

                            const users = [];
                            users.push($(this).attr('data-id'));
                            users.push(messenger.me);

                            restSelf.post('/api/chatt/', {memberList: users}).done(function (response) {
                                messenger.loadRoom(response.data);
                            });
                        });
                });
                messenger.filterItem();
            });
        };

        function hierarchicalOrganizationString(upperOrganizationNames, groupName) {
            let string = '';

            upperOrganizationNames.map(function (name) {
                if (string) string += '>';
                string += name;
            });

            if (string) string += '>';
            return string + groupName;
        }

        function showRoomCreationOrganizationModalSelectedUserCount() {
            $('#room-creation-organization-modal').find('.-selected-user-count').text(messenger.ui.roomCreationOrganizationPanel.find('.-messenger-user.active').length);
        }

        Messenger.prototype._loadRoomCreationOrganization = function (response) {
            const messenger = this;
            const panel = messenger.ui.roomCreationOrganizationPanel;

            function attachFolder(e, upperOrganizationNames) {
                if (e.personList && e.personList.length) {
                    let logoutCount = 0, loginCount = 0;
                    e.personList.map(function (e) {
                        if (e.isLoginChatt === 'L') loginCount++;
                        else logoutCount++;
                    });

                    const item = $('<ul/>', {class: 'organization-ul modal -messenger-folder', 'data-id': e.groupCode})
                        .append(
                            $('<div/>', {class: 'title'})
                                .append($('<span/>', {class: '', text: e.groupName}))
                                .append(
                                    $('<div/>', {class: 'dot-label-wrap'})
                                        .append($('<span/>', {class: 'dot-label active'}))
                                        .append($('<text/>', {class: '-group-login-user-count', 'data-group': e.groupCode, text: loginCount}))
                                        .append($('<span/>', {class: 'dot-label'}))
                                        .append($('<text/>', {class: '-group-logout-user-count', 'data-group': e.groupCode, text: logoutCount}))
                                )
                        )
                        .append(
                            $('<li/>', {class: 'belong'})
                                .append(
                                    $('<div/>', {class: 'user-wrap'})
                                        .append(
                                            $('<div/>', {class: 'ui checkbox'})
                                                .append($('<input/>', {type: 'checkbox'}))
                                                .append($('<label/>', {text: hierarchicalOrganizationString(upperOrganizationNames, e.groupName)}))
                                        )
                                )
                        )
                        .appendTo(panel);

                    const groupName = e.groupName;
                    e.personList.map(function (e) {
                        e.groupName = groupName;
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
                let text = '';
                if (e.extension) text += ' / 내선:' + e.extension;
                if (e.hpNumber) text += ' / 휴대폰:' + e.hpNumber;
                if (e.emailInfo) text += ' / 이메일:' + e.emailInfo;
                if (text.indexOf(' / ') === 0) text = text.substr(3);

                $('<li/>', {class: '-messenger-user', 'data-id': e.id, 'data-group': e.groupCode})
                    .append(
                        $('<div/>', {class: 'user-wrap'})
                            .append($('<span/>', {class: 'user-icon ' + (e.isLoginChatt === 'L' ? 'active' : '')}))
                            .append($('<text/>', {text: e.idName}))
                    )
                    .append(
                        $('<div/>', {class: 'btn-wrap'})
                            .append($('<span/>', {class: 'ui mini label -consultant-status-with-color', 'data-peer': e.peer, css: {visibility: e.peer ? 'visible' : 'hidden'}}))
                            .append($('<span/>', {class: 'user-num', text: text}))
                    )
                    .appendTo(container)
                    .click(function (event) {
                        (function () {
                            if (event.ctrlKey) {
                                $(this).toggleClass('active');

                                if ($(this).hasClass('active')) {
                                    messenger.lastActiveRoomCreationElement = this;
                                }

                                return;
                            }
                            if (event.shiftKey && messenger.lastActiveRoomCreationElement) {
                                panel.find('.-messenger-user').removeClass('active');
                                panel.find('.-messenger-folder').removeClass('active');
                                $(messenger.lastActiveRoomCreationElement).addClass('active');

                                const _this = this;

                                if (_this === messenger.lastActiveRoomCreationElement)
                                    return;

                                let meetActiveElement = false;
                                let meetMe = false;
                                panel.find('.-messenger-user').each(function () {
                                    if (this === messenger.lastActiveRoomCreationElement) {
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
                                panel.find('.-messenger-user').removeClass('active');
                                panel.find('.-messenger-folder').removeClass('active');
                                $(this).addClass('active');
                                messenger.lastActiveRoomCreationElement = this;
                            }
                        }).apply(this, []);

                        showRoomCreationOrganizationModalSelectedUserCount();
                    });
            }

            response.data.map(function (e) {
                attachFolder(e, []);
            });
        };
        Messenger.prototype._loadOrganization = function (response) {
            const messenger = this;

            function attachFolder(e, upperOrganizationNames) {
                if (e.personList && e.personList.length) {
                    let logoutCount = 0, loginCount = 0;
                    e.personList.map(function (e) {
                        if (e.isLoginChatt === 'L') loginCount++;
                        else logoutCount++;
                    });

                    const item = $('<ul/>', {class: 'organization-ul -messenger-folder', 'data-id': e.groupCode})
                        .append(
                            $('<div/>', {class: 'title'})
                                .append($('<span/>', {class: 'team-name', text: e.groupName}))
                                .append(
                                    $('<div/>', {class: 'dot-label-wrap'})
                                        .append($('<span/>', {class: 'dot-label active'}))
                                        .append($('<text/>', {class: '-group-login-user-count', 'data-group': e.groupCode, text: loginCount}))
                                        .append($('<span/>', {class: 'dot-label'}))
                                        .append($('<text/>', {class: '-group-logout-user-count', 'data-group': e.groupCode, text: logoutCount}))
                                )
                        )
                        .append(
                            $('<li/>', {class: 'belong'})
                                .append(
                                    $('<div/>', {class: 'user-wrap'})
                                        .append(
                                            $('<div/>', {class: 'ui checkbox'})
                                                .append($('<input/>', {type: 'checkbox'}))
                                                .append($('<label/>', {text: hierarchicalOrganizationString(upperOrganizationNames, e.groupName)}))
                                        )
                                )
                        )
                        .appendTo(messenger.ui.organizationPanel);

                    const groupName = e.groupName;
                    e.personList.map(function (e) {
                        e.groupName = groupName;
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
                $('<li/>', {class: '-messenger-user', 'data-id': e.id, 'data-group': e.groupCode})
                    .append(
                        $('<div/>', {class: 'user-wrap'})
                            .append($('<span/>', {class: 'user-icon ' + (e.isLoginChatt === 'L' ? 'active' : '')}))
                            .append($('<text/>', {text: e.idName}))
                    )
                    .append(
                        $('<div/>', {class: 'btn-wrap'})
                            .append($('<span/>', {class: 'ui mini label -consultant-status-with-color', 'data-peer': e.peer, css: {visibility: e.peer ? 'visible' : 'hidden'}}))
                            .append(
                                $('<div/>', {class: 'buttons'})
                                    .append($('<button/>', {
                                        type: 'button',
                                        class: 'arrow button',
                                        'data-inverted': '',
                                        'data-tooltip': '호전환',
                                        'data-position': 'bottom center',
                                        onclick: 'ipccCommunicator.redirect(' + e.extension + ')'
                                    }))
                                    .append($('<button/>', {
                                        type: 'button', class: 'talk ${user.isTalk.equals('Y') ? 'on' : 'off'} button', click: function () {
                                            <c:if test="${user.isTalk.equals('Y')}">
                                            if ($('#organi-state').hasClass('active'))
                                                $('#organi-state').click();
                                            if ($('#organi-room').hasClass('active'))
                                                $('#organi-room').click();

                                            $('.-counsel-content-panel .item[data-tab="talk-panel"]').click();
                                            $('.item[data-tab="talk-list-type-OTH"]').click();

                                            const ui = $('#talk-list-panel');
                                            ui.find('.-search-type').val('userId');
                                            ui.find('.-search-value').val(e.id);
                                            ui.find('.-search-button').click();
                                            </c:if>
                                            <c:if test="${!user.isTalk.equals('Y')}">
                                            alert('상담톡 권한이 없습니다.');
                                            </c:if>
                                        }
                                    }))
                                    .append($('<button/>', {
                                        type: 'button', class: 'info button', 'data-inverted': '', 'data-tooltip': '정보', 'data-position': 'bottom center', click: function () {
                                            const modal = $('#user-info-modal');
                                            modal.find('.-field').each(function () {
                                                $(this).text(e[$(this).attr('data-name')]);
                                            });
                                            modal.find('.-extension').attr('onclick', e.extension ? 'ipccCommunicator.clickDial("", "' + e.extension + '")' : '');
                                            modal.find('.-hp-number').attr('onclick', e.hpNumber ? 'ipccCommunicator.clickDial("", "' + e.hpNumber + '")' : '');
                                            modal.dragModalShow();
                                        }
                                    }))
                            )
                    )
                    .append(
                        $('<div/>', {class: 'state-wrap'})
                            .append($('<text/>', {text: '전화'}))
                            .append($('<span/>', {class: 'num -user-total-call-count', 'data-id': e.id, text: '0'}))
                            .append($('<text/>', {text: '채팅'}))
                            .append($('<span/>', {class: 'num -user-total-chat-count', 'data-id': e.id, text: '0'}))
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
                        const user = $(this).attr('data-id');
                        if (user === messenger.me)
                            return;

                        const users = [];
                        users.push($(this).attr('data-id'));
                        users.push(messenger.me);

                        restSelf.post('/api/chatt/', {memberList: users}).done(function (response) {
                            messenger.loadRoom(response.data);
                        });
                    });
            }

            response.data.map(function (e) {
                attachFolder(e, []);
            });
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

            popupDraggableModalFromReceivedHtml('/modal-messenger-room/' + roomId, 'modal-messenger-room').done(function () {
                messenger.ui.room = $('#modal-messenger-room');
                messenger.ui.roomName = messenger.ui.room.find('.-chatroom-name');
                messenger.ui.searchingTextCountExpression = messenger.ui.room.find('.-text-count');
                messenger.ui.messageInput = messenger.ui.room.find('.-messenger-message');
                messenger.ui.roomMembers = $('#messenger-room-members');   // fixme: 초대 모달 안의 현재 방의 사용자 리스트

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

                    const scrollBody = messenger.ui.room.find('.chat-body');
                    scrollBody.scrollTop(scrollBody[0].scrollHeight);

                    if (messenger.currentRoom.endMessageId)
                        messenger.communicator.confirmMessage(messenger.currentRoom.id, messenger.currentRoom.endMessageId);
                }).done(function () {
                    messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
                        return $(this).attr('data-id') === roomId;
                    }).find('.-unread-message').text(0);
                    messenger._showMessageIndicator();
                });
            });
        };
        Messenger.prototype.clearSearching = function () {
            const messenger = this;

            if (messenger.currentRoom) {
                messenger.currentRoom.searchingMessage = null;
                messenger.currentRoom.searchingMessages = [];
                messenger.ui.searchingTextCountExpression.attr('data-total', 0).text('0 / 0');
                messenger.ui.room.find('.-chat-message').removeClass('active');
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
                messenger.ui.searchingTextCountExpression.text('0 / 0');
                return;
            }

            messenger.ui.searchingTextCountExpression.attr('data-index', index + 1);
            messenger.ui.searchingTextCountExpression.text((index + 1) + ' / ' + total);
            const messageId = messenger.currentRoom.searchingMessages[index].messageId;
            // const messageTime = messenger.currentRoom.searchingMessages[index].messageTime;

            const messageElement = $('.-chat-message').removeClass('active').filter(function () {
                return messageId === $(this).attr('data-id');
            });
            if (messageElement.length > 0) {
                const chatBody = messageElement.addClass('active').closest('.chat-body');
                return chatBody.animate({scrollTop: messageElement.position().top - messageElement.height() + chatBody.scrollTop()}, 400);
            }
            messenger.loadMessages({
                endMessageId: messageId,
                startMessageId: messenger.currentRoom.startMessageId
            }).done(function () {
                const messageElement = $('.-chat-message').filter(function () {
                    return messageId === $(this).attr('data-id');
                });
                if (messageElement.length > 0) {
                    const chatBody = messageElement.addClass('active').closest('.chat-body');
                    return chatBody.animate({scrollTop: messageElement.position().top - messageElement.height() + chatBody.scrollTop()}, 400);
                }
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

            messenger.ui.room.find('.-chat-message').each(function () {
                const messageId = $(this).attr('data-id');
                const message = messenger.currentRoom.messages[messageId];
                if (!message)
                    throw 'ui에 표시된 message인데, 정보가 없다는 건 messages 관리하는 로직에 문제가 있다는 뜻.';

                $(this).find('.-unread-count').text(memberIds.filter(function (userid) {
                    return lastReadMessageTimesOfEachMember[userid] < message.insertTime;
                }).length || '');
            });
        };
        Messenger.prototype.init = function () {
            const messenger = this;

            messenger.loadBookmarks();
            messenger.loadRooms();

            restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                function checkGroup(e) {
                    if (e.personList && e.personList.length) {
                        const group = messenger.groupToUsers[e.groupCode] = [];
                        e.personList.map(function (e) {
                            group.push(e.id);
                            messenger.users[e.id] = e;
                        });
                    }

                    if (e.organizationMetaChatt)
                        e.organizationMetaChatt.map(function (e) {
                            checkGroup(e);
                        });
                }

                response.data.map(function (e) {
                    checkGroup(e);
                });

                messenger._loadOrganization(response);
                messenger._loadRoomCreationOrganization(response);
            });

            restSelf.get('/api/auth/socket-info').done(function (response) {
                messenger.communicator.connect(response.data.messengerSocketUrl, response.data.companyId, response.data.userId, response.data.userName, response.data.password);
            });
        };

        const messenger = new Messenger();

        $(window).on('load', function () {
            messenger.init();

            setInterval(function () {
                restSelf.get('/api/chatt/user-score-moniter', null, null, true).done(function (response) {
                    response.data.map(function (e) {
                        $('.-user-total-call-count[data-id="' + e.userId + '"]').text(e.totalCnt);
                        $('.-user-total-chat-count[data-id="' + e.userId + '"]').text(e.talkCnt);
                    });
                });
            }, 1000 * 60);
        });
    </script>
</tags:scripts>
