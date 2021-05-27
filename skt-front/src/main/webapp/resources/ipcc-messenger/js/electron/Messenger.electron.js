const ipcRenderer = window.ipcRenderer;

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
        const roomId = data.room_id;
        const roomName = data.room_name;
        const messageId = data.message_id;
        const time = parseFloat(data.cur_timestr) * 1000;
        const messageType = data.type;
        const sendReceive = data.send_receive;
        const contents = data.contents;
        const formUserId = data.userid;
        const formUserName = data.username;

        ipcRenderer.send('receiveMessage', {"roomId": data.room_id, "data": data});

        messenger._inputRoomInfo({roomId: roomId, roomName: roomName, lastMsg: contents, lastTime: time, lastUserid: formUserId,},
            messenger.currentRoom && messenger.currentRoom.id === roomId ? 0 : (messenger.rooms[roomId] && messenger.rooms[roomId].unreadMessageTotalCount || 0) + 1);

        messenger._sortChatListItem();
        messenger.filterItem();
        messenger._showMessageIndicator();
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
                ipcRenderer.send('readConfirm', {'roomId': roomId, 'userId': userid, 'lastReadMessageId': lastReadMessageId});

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

    ipcRenderer.on("changeRoomName", function (event, arg) {
        messenger.communicator.changeRoomName(arg.roomId, arg.roomName)
    });

    ipcRenderer.on("requestLoadMessages", function (event, arg) {
        messenger.loadMessages(arg.roomId, arg.option);
    });

    ipcRenderer.on("sendMessage", function (event, arg) {
        messenger.sendMessage(arg.roomId, arg.ciphertext);
    });

    ipcRenderer.on('confirmMessage', function (event, arg) {
        messenger.communicator.confirmMessage(arg.roomId, arg.messageId);
    });

    ipcRenderer.on('leaveRoom', function (event, arg) {
        restSelf.delete('/api/chatt/' + encodeURIComponent(arg.roomId), null, null, true).done(function () {
            messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
                return $(this).attr('data-id') === arg.roomId;
            }).remove();
            messenger.communicator.leave(arg.roomId);
        });
    });

    ipcRenderer.on('loadOrganizationPanel', function (event, arg) {
        console.log('try load')
        restSelf.get('/api/chatt/', null, null, true).done(function (response) {
            console.log('calling api success')
            ipcRenderer.send('responseOrganizationPanel', {
                'roomId': arg.roomId,
                'members': arg.members,
                "organizationData": response,
                'statusCodes': statusCodes,
                'userStatuses': userStatuses
            });

            console.log('load success')
        });
    });

    ipcRenderer.on('inviteUsers', function (event, arg) {
        messenger.communicator.invite(arg.roomId, arg.users, arg.userNames);
    });

    ipcRenderer.on('inviteSuccess', function (event, arg) {
        messenger._loadRoomName(arg.roomId)
    })

    ipcRenderer.on('roomFocusOn', function (event, arg) {
        messenger.currentRoom = arg.roomInfo;
        messenger.rooms[arg.roomInfo.id].unreadMessageTotalCount = 0;
        if (arg.roomInfo && arg.roomInfo.id && arg.roomInfo.endMessageId) {
            messenger.communicator.confirmMessage(arg.roomInfo.id, arg.roomInfo.endMessageId);
        }

        messenger._showMessageIndicator();
    });

    ipcRenderer.on('roomFocusOut', function (event, arg) {
        messenger.currentRoom = null;
    });

    ipcRenderer.on('sendMemo', function (event, arg) {
        messenger.communicator.sendMemo(arg.receiveUserId, arg.data);

        $('#tab5 button[type=submit]').click();
    });

    ipcRenderer.on('loadBookmarks', function (event, arg) {
        messenger.loadBookmarks();
    });

    ipcRenderer.on('logout', function (event, arg) {
        restSelf.get("/api/auth/logout").done(function () {
            location.href = contextPath + '/ipcc-messenger';
        });
    });

    ipcRenderer.on('clickDial', function (event, arg) {
        ipccCommunicator.clickDial('', arg.value);
        $(document).click();
    });

    ipcRenderer.on('profileChange', function (event, args) {
        $(window.document).find(`.-picture[data-id="${args.userId}"]`).attr('src', args.src);
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

    if (entity.roomId && entity.roomName){
        ipcRenderer.send('changedRoomName', {'roomId': entity.roomId, 'roomName': entity.roomName});
    }

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

Messenger.prototype.sendMessage = function (roomId, ciphertext) {
    const messenger = this;

    messenger.communicator.sendMessage(roomId, ciphertext);
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

    console.log({sendReceive: sendReceive, messageType: messageType})

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
                    if (split[1].endsWith('g'))
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
        return;
    }

    $('<li/>', {class: 'room-item -messenger-chat-item', 'data-id': roomId})
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

// 떠다니는 버튼 옆에 안 읽은 메세지 총 갯수를 적는다.
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

// 채팅방 모달의 타이틀을 변경
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
            .append($('<span/>', {class: 'material-icons', text: 'filter_' + (level > 9 ? '9_plus' : level)}))
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
            e.personList.map(function (e) {
                attachPerson(childrenContainer, e);
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
            .append($('<span/>', {class: 'name', text: e.idName + (e.userDivs ? ' [' + e.userDivs + ']' : '')}))

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

    $('.material-icons').on('click',function(){
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
        ipcRenderer.send('roomOpen', {"roomId": roomId, "responseData": response.data, "messengerSocketUrl": messenger.communicator.request.url});

        const entity = response.data;
        messenger._inputRoomInfo(entity, 0);
    }).done(function () {
        messenger.ui.chatContainer.find('.-messenger-chat-item').filter(function () {
            return $(this).attr('data-id') === roomId;
        }).find('.-unread-message').text(0);
        messenger._showMessageIndicator();
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

Messenger.prototype.loadMessages = function (roomId, option) {
    const messenger = this;

    return restSelf.get('/api/chatt/' + encodeURIComponent(roomId) + '/chatting', option, undefined, true).done(function (response) {
        // messenger._inputRoomInfo(response.data);

        if (response.data.chattingMessages.length <= 0)
            return;

        response.data.chattingMessages.sort(function (a, b) {
            return parseFloat(a.cur_timestr || a.insertTime) - parseFloat(b.cur_timestr || b.insertTime);
        });

        ipcRenderer.send('responseLoadMessages', {"roomId": roomId, "response": response});
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

Messenger.prototype.popupMessageSendModal = function () {
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
            ipcRenderer.send('openMessageSendModal', {'users' : $.addQueryString('', {'members' : users})});
        } else {
            ipcRenderer.send('openMessageSendModal', {'users' : ""});
        }
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
    ipcRenderer.send('openBookmarkModal');
}

Messenger.prototype.popupMemoModal = function (seq, withReply) {
    ipcRenderer.send('openMemoModal', {"seq": seq, "withReply": withReply});
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
