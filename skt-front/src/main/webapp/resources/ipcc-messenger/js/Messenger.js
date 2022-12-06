function Messenger(userId, isEmail, accessToken) {
    const messenger = this;

    messenger.me = userId;
    messenger.isPosition = isEmail;
    messenger.accessToken = accessToken;

    messenger.ui = {
        filterInput: $('#messenger-filter-text').keyup(function () {
            messenger.filterItem();
        }),
        organizationPanel: $('#messenger-organization-panel'),
        bookmarkPanel: $('#messenger-bookmark-panel'),
        chatContainer: $('#messenger-chat-container'),
        messageIndicator: $('.-message-indicator'),
    };

    function receiveMessage(data) {
        if (messenger.ui.roomWindow && messenger.ui.roomWindow.closed)
            messenger.closeRoom();

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
            if (!userStatuses[userid])
                userStatuses[userid] = {status: 0, userId: userid};

            userStatuses[userid].login = true;

            updateUserStatus(userid, true);
        })
        .on('svc_logout', function (data) {
            const userid = data.userid;
            if (!userStatuses[userid])
                userStatuses[userid] = {status: 0, userId: userid};

            userStatuses[userid].login = false;

            updateUserStatus(userid, true);
        })
        .on('svc_msg', receiveMessage)
        .on('svc_join_msg', function (data) {
            receiveMessage(data);
            messenger.communicator.join(data.room_id);
        })
        .on('svc_invite_room', function (data) {
            const roomId = data.room_id;

            if (messenger.me === data.invited_userid)
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

                $('.-messenger-chat-item').filter(function () {
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

            const chattingRoomElement = $('.-messenger-chat-item').filter(function () {
                return $(this).attr('data-id') === roomId;
            });
            chattingRoomElement.find('.-room-name').text(messenger.rooms[roomId].roomName);

            if (messenger.ui.roomName && messenger.currentRoom.id === roomId)
                messenger.ui.roomName.text(data.change_room_name);
        })
        .on('svc_memo_send', function (data) {
            const messageId = data.message_id;
            const sender = data.send_userid;

            const indicator = $('#unread-memo-indicator');
            const currentCount = parseInt(indicator.text());
            indicator.text((currentCount || 0) + 1);
            indicator.show();

            $('#tab4 button[type=submit]').click();
        })
        .on('svc_memo_read', function (data) {
            const messageId = data.message_id;
            const reader = data.read_userid;

            const tr = $('.-sent-memo[data-id="' + messageId + '"]');
            tr.find('.-sent-memo-unread-user[data-user="' + reader + '"]')
                .removeClass('-sent-memo-unread-user')
                .addClass('-sent-memo-read-user')
                .addClass('import');
            tr.find('.-sent-memo-read-count').text(tr.find('.-sent-memo-read-user').length + '/' + (tr.find('.-sent-memo-read-user').length + tr.find('.-sent-memo-unread-user').length));
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

    if (messenger.ui.roomWindow && messenger.currentRoom && messenger.currentRoom.id === entity.roomId)
        messenger.ui.roomName.text(entity.roomName);

    messenger._appendChatListItem(roomId);
};

Messenger.prototype._loadRoomName = function (roomId) {
    const messenger = this;

    restSelf.get('/api/chatt/' + roomId, null, null, true).done(function (response) {
        messenger._inputRoomInfo(response.data);

        const chattingRoomElement = $('.-messenger-chat-item').filter(function () {
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

        const ciphertext = CryptoJS.AES.encrypt(message, cipherKey).toString();

        messenger.communicator.sendMessage(roomId, ciphertext);
        messenger.ui.messageInput.val('');
    }
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
            return $('<li/>', {class: '-chat-message -ignored-message', 'data-id': messageId, 'data-time': time});

        if (['SE', 'RE'].indexOf(sendReceive) >= 0)
            return $('<li/>', {class: 'event-message -chat-message -system-message ', text: '[' + timeString + '] ' + contents, 'data-id': messageId, 'data-time': time});

        if (['AF', 'S', 'R'].indexOf(sendReceive) >= 0) {
            if (messageType === 'info') {
                return $('<li/>', {class: 'event-message -chat-message -system-message ', text: '[' + timeString + '] ' + contents, 'data-id': messageId, 'data-time': time});
            }

            const item = $('<li/>', {class: 'message-balloon-wrap -chat-message ' + (myMessage ? 'me' : ''), 'data-id': messageId, 'data-time': time});
            const contentInfo = messageType !== 'info' ? $('<div/>', {class: 'user-info', text: username + ' [' + timeString + ']'}) : '';
            const content = $('<div/>', {class: 'message-balloon-inner'}).appendTo(messageType === 'info' ? item.css('display', 'inline') : item)
                .append(contentInfo);

            const bubble = $('<div class="new-message-balloon">')
                .append($('<div/>', {class: 'view-whether -unread-count', text: unreadCount || ''}))
                .appendTo(content);

            if (messageType === 'file') {
                const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(contents);
                const url = $.addQueryString(split && split[4] || '', {token: messenger.accessToken});

                if (!split || split.length < 2)
                    bubble.append($('<p/>', {class: 'chat', text: contents}));
                else {
                    if (split[1].endsWith('g') || split[1].endsWith('G'))
                        bubble.append($('<p/>', {class: "chat"}).append($('<img/>', {src: url})));
                    else if (split[1].contains('wav') || split[1].contains('mp'))
                        bubble.append($('<p/>', {class: "chat"}).append($('<div/>', {class: 'maudio'}).append($('<audio/>', {controls: 'controls', src: url}))));
                    else
                        bubble.append($('<p/>', {class: "chat"}).append($('<span/>', {
                            class: 'material-icons',
                            text: 'folder_open'
                        })).append($('<text/>', {text: split && split[2] || ''})));

                    bubble.append($('<p/>', {class: "sm-text", text: '용량: ' + (split && split[3] || '')}));
                    content.append($('<div/>', {class: 'save-link'}).append($('<a/>', {href: url, text: '저장하기', target: '_blank'})));
                }
            } else {
                let decryptedText;
                try {
                    decryptedText = CryptoJS.AES.decrypt(contents, cipherKey).toString(CryptoJS.enc.Utf8);
                } catch (e) {
                    console.info(e);
                    decryptedText = 'decryption error: ' + contents;
                }
                bubble.append($('<p/>', {class: 'chat', text: decryptedText}));
            }

            return item;
        }

        return $('<text/>', {text: 'unknown sendReceive: ' + sendReceive, class: '-chat-message -unknown-message ', 'data-id': messageId, 'data-time': time})
            .append('<br/>')
            .append($('<text/>', {text: contents}));
    })();

    const chatBody = messenger.ui.room.find('.room-content');
    const scrollBody = chatBody.find('.message-balloon');

    if (messenger.currentRoom.startMessageTime >= time) {
        messenger.currentRoom.startMessageTime = time;
        messenger.currentRoom.startMessageId = messageId;
        scrollBody.prepend(item);
    } else if (messenger.currentRoom.endMessageTime <= time) {
        messenger.currentRoom.endMessageTime = time;
        messenger.currentRoom.endMessageId = messageId;
        scrollBody.append(item);

        chatBody.scrollTop(chatBody.prop('scrollHeight'));
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
    const lastMsg = (function (lastMsg) {
        const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(lastMsg);

        if (split)
            return split[2];

        try {
            return CryptoJS.AES.decrypt(lastMsg, cipherKey).toString(CryptoJS.enc.Utf8) || lastMsg;
        } catch (e) {
            console.info(e);
        }

        return lastMsg;
    })(messenger.rooms[roomId].lastMsg);
    const sendReceive = messenger.rooms[roomId].sendReceive;
    const messageType = messenger.rooms[roomId].messageType;
    const lastTime = messenger.rooms[roomId].lastTime;
    const unreadMessageTotalCount = messenger.rooms[roomId].unreadMessageTotalCount;

    const timeString = '[' + moment(lastTime).format('MM-DD HH:mm') + ']';

    const existChatItem = messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
        return $(this).attr('data-id') === roomId;
    });

    if (existChatItem.length > 0) {
        existChatItem.find('.-room-name').text(roomName);
        existChatItem.find('.-last-message-time').text(timeString).attr('data-value', lastTime);

        if (['SZ', 'SG', 'SE', 'RE'].indexOf(sendReceive) >= 0 || 'info' === messageType) {
            existChatItem.find('.-preview').text(lastMsg);
        } else {
            const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(messenger.rooms[roomId].lastMsg);

            if (split) {
                existChatItem.find('.-preview').text('[파일전송]' + split[2]);
            } else {
                existChatItem.find('.-preview').text(lastMsg);
            }
        }

        existChatItem.find('.-unread-message-number').text(unreadMessageTotalCount).attr('style', 'display: ' + (unreadMessageTotalCount || 'none'));

        if (!messenger.rooms[roomId].lastMsg)
            existChatItem.hide();
        else
            existChatItem.show();

        return;
    }

    const chatItem = $('<li/>', {class: 'room-item -messenger-chat-item', 'data-id': roomId})
        .append(
            $('<div/>', {
                class: 'title',
                onclick: 'return false;',
                click: function () {
                    const $this = $(this);
                    prompt('채팅방 이름 변경').done(function (text) {
                        text = text.trim();
                        if (!text)
                            return;
                        restSelf.put('/api/chatt/' + roomId + '/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
                            $this.text(text);
                            messenger.communicator.changeRoomName(roomId, text);

                            if (messenger.currentRoom && messenger.currentRoom.id === roomId) {
                                messenger.currentRoom.roomName = text;
                                messenger.showRoomTitle();
                            }
                        });
                    });
                    return false;
                }
            })
                .append($('<text/>', {class: '-room-name', text: roomName}))
                .append($('<text/>', {class: '-last-message-time', style: 'padding-left: 1em;', text: timeString, 'data-value': lastTime}))
        )
        .append($('<div/>', {class: 'content -preview', text: lastMsg}))
        // .append($('<span/>', {class: 'view-whether -unread-message-number', text: '읽음'}))
        .append($('<span/>', {class: 'number -unread-message-number', text: unreadMessageTotalCount, style: 'display: ' + (unreadMessageTotalCount || 'none')}))
        .appendTo(messenger.ui.chatContainer)
        .click(function () {
            messenger.loadRoom(roomId);
        });

    if (!messenger.rooms[roomId].lastMsg)
        chatItem.hide();
    else
        chatItem.show();
};

Messenger.prototype.filterItem = function () {
    const messenger = this;

    const text = (messenger.ui.filterInput.val() || '').trim();
    messenger.ui.chatContainer.find('.-messenger-chat-item').each(function () {
        if ($(this).text().indexOf(text) >= 0) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });

    messenger.ui.bookmarkPanel.find('.-messenger-bookmark').each(function () {
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

// 플로팅 버튼 옆에 안 읽은 메세지 총 갯수를 적는다.
Messenger.prototype._showMessageIndicator = function () {
    const messenger = this;

    let unreadCount = 0;
    values(messenger.rooms).map(function (e) {
        unreadCount += e.unreadMessageTotalCount;
    });
    messenger.ui.messageIndicator.show().text(unreadCount);
    if (!unreadCount)
        messenger.ui.messageIndicator.hide();
};

Messenger.prototype._sortChatListItem = function () {
    const messenger = this;

    let items = [];
    $('.-messenger-chat-item').detach().each(function () {
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

// 채팅방 모달의 타이틀 변경
Messenger.prototype.showRoomTitle = function () {
    const messenger = this;

    if (!messenger.currentRoom)
        return;

    messenger.ui.roomMembers.empty();
    values(messenger.currentRoom.members).map(function (e) {
        messenger.ui.roomMembers.append($('<li/>', {text: e.userName}));
    });

    messenger.ui.roomName.text(messenger.currentRoom.roomName);
};

// 열려있는 채팅방을 닫는다. (나가기 아님)
Messenger.prototype.closeRoom = function () {
    const messenger = this;

    messenger.clearSearching();
    if (messenger.ui.roomWindow)
        messenger.ui.roomWindow.close();
    delete messenger.currentRoom;
    delete messenger.ui.roomWindow;
    delete messenger.ui.room;
    delete messenger.ui.roomName;
    delete messenger.ui.invitationPanel;
    delete messenger.ui.searchingTextCountExpression;
    delete messenger.ui.roomMembers;
    delete messenger.ui.messageInput;
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

            const li = $('<li/>', {class: 'item user -messenger-bookmark', 'data-id': e.id})
                .append($('<img/>', {
                    class: 'picture ' + (e.isLoginChatt === 'L' ? 'active' : ''),
                    src: e.profilePhoto
                        ? apiServerUrl + '/api/memo/profile-resource?path=' + encodeURIComponent(e.profilePhoto) + '&token=' + encodeURIComponent(accessToken)
                        : defaultProfilePicture
                }))
                .append($('<span/>', {
                    'data-id': e.id,
                    class: 'material-icons -status-icon',
                    text: userStatuses[e.id] && statusCodes[userStatuses[e.id].status] ? statusCodes[userStatuses[e.id].status].icon || 'person' : 'person'
                }))
                .append($('<span/>', {class: 'name', text: e.idName + (e.userDivs ? ' [' + e.userDivs + ']' : '')}))
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

            if (e.extension)
                li.append($('<span/>', {class: 'detail word hover-focus -extension', text: '내선:' + e.extension, 'data-value': e.extension}));
            if (e.hpNumber && messenger.isPosition === 'N')
                li.append($('<span/>', {class: 'detail word', text: e.extension ? ' / ' : ''})).append($('<span/>', {class: 'detail word hover-focus -hp-number', text: '휴대폰:' + e.hpNumber, 'data-value': e.hpNumber}));
            if (e.emailInfo)
                li.append($('<span/>', {class: 'detail word', text: e.extension || (e.hpNumber && messenger.isPosition === 'N') ? ' / ' : ''})).append($('<span/>', {class: 'detail word', text: '이메일:' + e.emailInfo}));

        });
        messenger.filterItem();
    });
};

Messenger.prototype._loadOrganization = function (response) {
    const messenger = this;

    function attachFolder(container, e, level) {
        level = level || 1;

        const item = $('<div/>', {class: 'item -messenger-folder'})
            .append($('<span/>', {
                class: 'material-icons', id: 'setting-tab-indicator', text: 'filter_' + (level > 9 ? '9_plus' : level)
            }))
            .appendTo(container);

        const content = $('<div/>', {class: 'content'})
            .append(
                $('<div/>', {class: 'header level -messenger-folder-header', text: e.groupName})
                    .click(function (event) {
                        const item = $(this).closest('.item');
                        if (event.ctrlKey) {
                            item.toggleClass('active');
                        } else {
                            messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                            messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                            messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                            item.addClass('active');
                        }
                    })
                    .dblclick(function () {
                        const item = $(this).closest('.item');
                        messenger.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                        messenger.ui.bookmarkPanel.find('.-messenger-bookmark').removeClass('active');
                        messenger.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                        item.addClass('active');
                        messenger.openRoom();
                    })
            )
            .appendTo(item);

        const childrenContainer = $('<div/>', {class: 'list'}).css({'display': groupTree.contains(e.groupTreeName) ? '' : 'none'})
            .appendTo(content);

        if (e.personList)
            e.personList.map(function (p) {
                attachPerson(childrenContainer, p);
            });

        if (e.organizationMetaChatt)
            e.organizationMetaChatt.map(function (e) {
                attachFolder(childrenContainer, e, level + 1);
            });
    }

    function attachPerson(container, e) {
        // if (e.id === userId)
        //     return;

        const header = $('<div/>', {class: 'header'})
            .append($('<span/>', {class: 'name', text: e.idName + (e.userDivs ? ' [' + e.userDivs + ']' : '')}));

        if (e.extension)
            header.append($('<span/>', {class: 'detail word hover-focus -extension', text: '내선:' + e.extension, 'data-value': e.extension}));
        if (e.hpNumber && messenger.isPosition === 'N')
            header.append($('<span/>', {class: 'detail word', text: e.extension ? ' / ' : ''})).append($('<span/>', {class: 'detail word hover-focus -hp-number', text: '휴대폰:' + e.hpNumber, 'data-value': e.hpNumber}));
        if (e.emailInfo)
            header.append($('<span/>', {class: 'detail word', text: e.extension || (e.hpNumber && messenger.isPosition === 'N') ? ' / ' : ''})).append($('<span/>', {class: 'detail word', text: '이메일:' + e.emailInfo}));

        $('<div/>', {class: 'item user -messenger-user', 'data-id': e.id, 'data-name': e.idName})
            .append(
                $('<div/>', {class: 'picture-container'})
                    .append($('<img/>', {
                        class: 'picture -picture ' + (e.isLoginChatt === 'L' ? 'active' : ''),
                        'data-id': e.id,
                        src: e.profilePhoto
                            ? apiServerUrl + '/api/memo/profile-resource?path=' + encodeURIComponent(e.profilePhoto) + '&token=' + encodeURIComponent(accessToken)
                            : defaultProfilePicture
                    }))
                    .append($('<span/>', {
                        'data-id': e.id,
                        class: 'material-icons -status-icon',
                        text: userStatuses[e.id] && statusCodes[userStatuses[e.id].status] ? statusCodes[userStatuses[e.id].status].icon || 'person' : 'person'
                    }))
            )
            .append($('<div/>', {class: 'content'}).append(header))
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

    messenger.ui.organizationPanel.empty();
    response.data.map(function (e) {
        attachFolder(messenger.ui.organizationPanel, e);
    });

    messenger.filterItem();

    $('.material-icons').on('click', function () {
        const list = $(this).next(".content").find('.list');
        const folderHeader = $(this).text().contains('1') ? $(this).find('.list') : $(this).next(".content").find('.-messenger-folder-header');
        list.slideToggle(0, '', function () {
            if (list.css('display') === 'none')
                folderHeader.css({'color': '#dbdbdb'})
            else
                folderHeader.css({'color': ''})
        });
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

    restSelf.get('/api/chatt/' + roomId + '/chatting', {limit: messenger.READ_LIMIT}).done(function (response) {
        messenger.closeRoom();

        const popup = window.open("modal-room/" + roomId, "_blank", "width=600, height=750");
        popup.messenger = messenger;

        $(popup).on('load', function () {

            messenger.ui.roomWindow = popup;
            messenger.ui.room = $(popup.document);
            messenger.ui.roomName = messenger.ui.room.find('.-chatroom-name');
            messenger.ui.searchingTextCountExpression = messenger.ui.room.find('.-text-count');
            messenger.ui.roomMembers = messenger.ui.room.find('#messenger-room-members');
            messenger.ui.messageInput = messenger.ui.room.find('#messenger-message');

            messenger.initRoom();

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
                messenger.ui.roomMembers.append($('<li/>', {text: e.userName}));
            });

            messenger.showRoomTitle();

            entity.chattingMessages.sort(function (a, b) {
                return a.insertTime - b.insertTime;
            });

            entity.chattingMessages.map(function (e) {
                messenger._appendMessage(e.roomId, e.messageId, e.insertTime, e.type, e.sendReceive, e.content, e.userid, e.userName, e.unreadMessageCount);
            });

            if (messenger.currentRoom.endMessageId)
                messenger.communicator.confirmMessage(messenger.currentRoom.id, messenger.currentRoom.endMessageId);
        });
    }).done(function () {
        messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
            return $(this).attr('data-id') === roomId;
        }).find('.-unread-message').text(0);
        messenger._showMessageIndicator();
    });
};

Messenger.prototype.openInvitationModal = function () {
    const messenger = this;

    if (messenger.ui.invitationWindow)
        messenger.ui.invitationWindow.close();

    const popup = window.open(contextPath + "/ipcc-messenger/modal-invitation", "_blank", "width=600, height=485");
    popup.messenger = messenger;
    messenger.ui.invitationWindow = popup;

    $(popup).on('load', function () {
        const document = $(popup.document);
        document.find('#organization').append(messenger.ui.organizationPanel.clone(false).children());
        document.find('.detail').remove();
        /*document.find('.-messenger-folder-header').click(function (event) {
            const item = $(this).closest('.item');
            if (event.ctrlKey) {
                item.toggleClass('active');
            } else {
                document.find('.-messenger-user').removeClass('active');
                document.find('.-messenger-bookmark').removeClass('active');
                document.find('.-messenger-folder').removeClass('active');
                item.addClass('active');
            }
        });*/
        document.find('.-messenger-user').click(function (event) {
            event.stopPropagation();

            if (event.ctrlKey) {
                $(this).toggleClass('active');

                if ($(this).hasClass('active')) {
                    popup.lastActiveElement = this;
                }

                return false;
            }
            if (event.shiftKey && popup.lastActiveElement) {
                document.find('.-messenger-user').removeClass('active');
                document.find('.-messenger-bookmark').removeClass('active');
                document.find('.-messenger-folder').removeClass('active');
                $(popup.lastActiveElement).addClass('active');

                const _this = this;

                if (_this === popup.lastActiveElement)
                    return false;

                let meetActiveElement = false;
                let meetMe = false;
                document.find('.-messenger-user').each(function () {
                    if (this === popup.lastActiveElement) {
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
                document.find('.-messenger-user').removeClass('active');
                document.find('.-messenger-bookmark').removeClass('active');
                document.find('.-messenger-folder').removeClass('active');
                $(this).addClass('active');
                popup.lastActiveElement = this;
            }

            return false;
        });
        document.find('.material-icons').click(function () {
            const list = $(this).next(".content").find('.list');
            const folderHeader = $(this).text().contains('1') ? $(this).find('.list') : $(this).next(".content").find('.-messenger-folder-header');
            list.slideToggle(100, '', function () {
                if (list.css('display') === 'none')
                    folderHeader.css({'color': '#dbdbdb'})
                else
                    folderHeader.css({'color': ''})
            });
        });
    });
};

Messenger.prototype.clearSearching = function () {
    const messenger = this;

    if (messenger.currentRoom) {
        messenger.currentRoom.searchingMessage = null;
        messenger.currentRoom.searchingMessages = [];
        messenger.ui.searchingTextCountExpression.attr('data-total', 0).text('0/0');
        messenger.ui.room.find('.-chat-message').removeClass('active');
    }
};

Messenger.prototype.loadMessages = function (option) {
    const messenger = this;

    return restSelf.get('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id) + '/chatting', option, undefined, true).done(function (response) {
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

    const messageElement = messenger.ui.room.find('.-chat-message').removeClass('active').filter(function () {
        return messageId === $(this).attr('data-id');
    }).addClass('active');

    const chatBody = messenger.ui.room.find('.room-content');
    const containerScrollTop = messenger.ui.room.find('.message-balloon').position().top;
    const elementScrollTop = messageElement.position().top;
    chatBody.scrollTop(elementScrollTop - containerScrollTop);
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

Messenger.prototype.popupMessageSendModal = function () {
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

    if (users.length > 0) {
        window.open($.addQueryString(contextPath + '/ipcc-messenger/modal-send-memo', {members: users}), "_blank", "width=600, height=400")
            .messenger = this;
    } else {
        window.open(contextPath + "/ipcc-messenger/user-modal-send-memo", "_blank", "width=600, height=700")
            .messenger = this;
    }
}

Messenger.prototype.popupBookmarkModal = function () {
    const users = [];

    messenger.ui.organizationPanel.find('.-messenger-user').filter('.active').removeClass('active').each(function () {
        const id = $(this).attr('data-id');
        if (users.indexOf(id) >= 0)
            return;
        users.push(id);
    });

    if (users.length > 0) {
        confirm('선택한 사용자를 즐겨찾기에 추가하시겠습니까?').done(function () {
            restSelf.put('/api/chatt/bookmark/', {memberList: users, type: "Y"}).done(function () {
                messenger.loadBookmarks();
            });
        });
    }
    else
        messenger.openPopupBookmarkModal();
}

Messenger.prototype.openPopupBookmarkModal = function () {
    window.open(contextPath + "/ipcc-messenger/modal-messenger-bookmark", "_blank", "width=600, height=400")
        .messenger = this;
}

Messenger.prototype.popupMemoModal = function (seq, withReply) {
    window.open(contextPath + "/ipcc-messenger/modal-memo/" + seq, "_blank", "width=600, height=" + (withReply ? 500 : 325))
        .messenger = this;
}

Messenger.prototype.init = function () {
    const messenger = this;

    messenger.loadBookmarks();
    messenger.loadRooms();

    restSelf.get('/api/chatt/', null, null, true).done(function (response) {
        messenger._loadOrganization(response);
    });

    restSelf.get('/api/auth/socket-info').done(function (response) {
        messenger.communicator.connect(response.data.messengerSocketUrl, response.data.companyId, response.data.userId, response.data.userName, response.data.password);
    });
};

Messenger.prototype.initRoom = function () {
    const messenger = this;

    messenger.ui.room.find('.-close-room').click(function () {
        messenger.closeRoom();
    });

    messenger.ui.room.find('.-leave-room').click(function () {
        if (!messenger.currentRoom)
            return;

        messenger.ui.roomWindow.confirm('채팅방을 나가시겠습니까?').done(function () {
            restSelf.delete('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id), null, null, true).done(function () {
                messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
                    return $(this).attr('data-id') === messenger.currentRoom.id;
                }).remove();
                messenger.communicator.leave(messenger.currentRoom.id);
                messenger.closeRoom();
            });
        });
    });

    function setSearchingMessages() {
        if (!messenger.currentRoom.searchingMessage)
            return;

        messenger.currentRoom.searchingMessages = [];

        messenger.ui.room.find('.-chat-message:not(.-system-message):contains("' + messenger.currentRoom.searchingMessage.replace(/"/gi, '\\\\"') + '")').each(function () {
            messenger.currentRoom.searchingMessages.push({messageId: $(this).attr('data-id'), messageTime: $(this).attr('data-time')});
        });

        messenger.ui.searchingTextCountExpression.attr('data-total', messenger.currentRoom.searchingMessages.length);
    }

    const searchButton = messenger.ui.room.find('.-search-button');
    const searchText = messenger.ui.room.find('.-search-text');

    searchButton.click(function () {
        const keyword = searchText.val().trim();
        if (!keyword) {
            messenger.clearSearching();
            return;
        }
        // if (keyword === messenger.currentRoom.searchingMessage)
        //     return messenger.ui.room.find('.-move-to-next-text').click();

        messenger.currentRoom.searchingMessage = keyword;
        setSearchingMessages();
        messenger._moveToText(0);
    });

    searchText.keyup(function (event) {
        if (event.keyCode === 13) {
            searchButton.click();
        } else if (event.keyCode === 27) {
            $(this).val('');
            messenger.clearSearching();
        }
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

    (function scrollToBottom() {
        const chatBody = messenger.ui.room.find('.room-content');
        const scrollHeight = chatBody.prop('scrollHeight');
        chatBody.scrollTop(scrollHeight);

        if (!scrollHeight)
            setTimeout(function () {
                scrollToBottom()
            }, 100);
    })();

    // overflow scroll이 생성된 상태까지 기다려서. 생성된 scroll에 scroll event에 대한 액션을 설정한다.
    (function setScrollEvent() {
        setTimeout(function () {
            const scrollContainer = messenger.ui.room.find('.room-content');
            if (!scrollContainer.length)
                return setScrollEvent();

            scrollContainer.scroll(function () {
                if ($(this).scrollTop())
                    return;

                if (messenger.currentRoom)
                    messenger.loadMessages({startMessageId: messenger.currentRoom.startMessageId, limit: messenger.READ_LIMIT + 1}).done(function () {
                        setSearchingMessages();
                    });
            });
        }, 100);
    })();
};
