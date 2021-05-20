const ipcRenderer = window.ipcRenderer;

function MessengerRoom(roomId, userId, groupTreeName, accessToken) {
    const messengerRoom = this;

    messengerRoom.roomId = roomId;
    messengerRoom.me = userId;
    messengerRoom.me.groupTreeName = groupTreeName;
    messengerRoom.accessToken = accessToken;

    messengerRoom.ui = {};

    messengerRoom.ui.roomWindow = window;
    messengerRoom.ui.room = $(window.document);
    messengerRoom.ui.roomName = messengerRoom.ui.room.find('.-chatroom-name');
    messengerRoom.ui.searchingTextCountExpression = messengerRoom.ui.room.find('.-text-count');
    messengerRoom.ui.roomMembers = messengerRoom.ui.room.find('#messenger-room-members');
    messengerRoom.ui.messageInput = messengerRoom.ui.room.find('#messenger-message');

    ipcRenderer.on(messengerRoom.roomId + "_init", function (event, arg) {
        messengerRoom.initRoom();

        const entity = arg.responseData;

        messengerRoom.roomInfo = {
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

        messengerRoom.messengerSocketUrl = arg.messengerSocketUrl;

        entity.chattingMembers.map(function (e) {
            messengerRoom.roomInfo.members[e.userid] = e;
            messengerRoom.ui.roomMembers.append($('<li/>', {text: e.userName}));
        });

        messengerRoom.showRoomTitle();

        entity.chattingMessages.sort(function (a, b) {
            return a.insertTime - b.insertTime;
        });

        entity.chattingMessages.map(function (e) {
            messengerRoom._appendMessage(e.roomId, e.messageId, e.insertTime, e.type, e.sendReceive, e.content, e.userid, e.userName, e.unreadMessageCount);
        });

        if (messengerRoom.roomInfo.endMessageId)
            messengerRoom.confirmMessage(roomId, messengerRoom.roomInfo.endMessageId);

        ipcRenderer.send('roomFocusOn', {"roomId" : messenger.roomId, "roomInfo" : messenger.roomInfo});
    });

    ipcRenderer.on("receiveMessage", function (event, arg) {
        const data = arg.data;
        const roomId = data.room_id;
        const roomName = data.room_name;
        const messageId = data.message_id;
        const time = parseFloat(data.cur_timestr) * 1000;
        const messageType = data.type;
        const sendReceive = data.send_receive;
        const contents = data.contents;
        const formUserId = data.userid;
        const formUserName = data.username;

        messengerRoom._appendMessage(roomId, messageId, time, messageType, sendReceive, contents, formUserId, formUserName, parseInt(data.member_cnt) - 1);

        messengerRoom.roomInfo.members[formUserId].lastReadMessageId = messageId;
        if (document.hasFocus()) {
            messengerRoom.confirmMessage(roomId, messageId);
        }
    });

    ipcRenderer.on('readConfirm', function (event, arg) {
        messengerRoom.roomInfo.members[arg.userId].lastReadMessageId = arg.lastReadMessageId;
        messengerRoom.updateMessageReadCount();
    });

    ipcRenderer.on('inviteSuccess', function (event, arg) {
        arg.users.map(function (e) {
            if (!messengerRoom.roomInfo.members[e])
                messengerRoom.roomInfo.members[e] = {}

            messengerRoom.roomInfo.members[e].userid = e;
            messengerRoom.roomInfo.members[e].userName = arg.userMap[e];
        });
        messengerRoom.showRoomTitle();
    });

    ipcRenderer.on('changedRoomName', function (event, arg) {
        messengerRoom.ui.roomName.text(arg.roomName);
    });
}

MessengerRoom.prototype.READ_LIMIT = 10;

MessengerRoom.prototype.showRoomTitle = function () {
    const messengerRoom = this;

    if (!messengerRoom.roomInfo)
        return;

    messengerRoom.ui.roomMembers.empty();
    values(messengerRoom.roomInfo.members).map(function (e) {
        messengerRoom.ui.roomMembers.append($('<li/>', {text: e.userName}));
    });

    messengerRoom.ui.roomName.text(messengerRoom.roomInfo.roomName);
};

MessengerRoom.prototype.confirmMessage = function (roomId, messageId) {
    ipcRenderer.send('confirmMessage', {"roomId" : roomId, "messageId" : messageId});
};

MessengerRoom.prototype._moveToText = function (index) {
    const messengerRoom = this;

    const total = parseInt(messengerRoom.ui.searchingTextCountExpression.attr('data-total'));

    index = (index + total) % total;
    if (total === 0 || isNaN(index)) {
        parseInt(messengerRoom.ui.searchingTextCountExpression.attr('data-index', 0));
        messengerRoom.ui.searchingTextCountExpression.text('0/0');
        return;
    }

    messengerRoom.ui.searchingTextCountExpression.attr('data-index', index + 1);
    messengerRoom.ui.searchingTextCountExpression.text((index + 1) + '/' + total);
    const messageId = messengerRoom.roomInfo.searchingMessages[index].messageId;

    const messageElement = $('.-chat-message').removeClass('active').filter(function () {
        return messageId === $(this).attr('data-id');
    }).addClass('active');

    const chatBody = $('.room-content');
    const containerScrollTop = $('.message-balloon').position().top;
    const elementScrollTop = messageElement.position().top;
    chatBody.scrollTop(elementScrollTop - containerScrollTop);
};

MessengerRoom.prototype._appendMessage = function (roomId, messageId, time, messageType, sendReceive, contents, userId, username, unreadCount) {
    const messengerRoom = this;

    const timeString = moment(time).format('MM-DD HH:mm');
    const myMessage = ['AF', 'S'].indexOf(sendReceive) >= 0 && messengerRoom.me === userId;

    if (!messengerRoom.roomId || messengerRoom.roomId !== roomId)
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
                const url = $.addQueryString(split && split[4] || '', {token: messengerRoom.accessToken});

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
                bubble.append($('<p/>', {class: 'chat', text: decryptedText || contents}));
            }

            return item;
        }

        return $('<text/>', {text: 'unknown sendReceive: ' + sendReceive, class: '-chat-message -unknown-message ', 'data-id': messageId, 'data-time': time})
            .append('<br/>')
            .append($('<text/>', {text: contents}));
    })();

    const chatBody = $('.room-content');
    const scrollBody = chatBody.find('.message-balloon');

    if (messengerRoom.roomInfo.startMessageTime >= time) {
        messengerRoom.roomInfo.startMessageTime = time;
        messengerRoom.roomInfo.startMessageId = messageId;
        scrollBody.prepend(item);
    } else if (messengerRoom.roomInfo.endMessageTime <= time) {
        messengerRoom.roomInfo.endMessageTime = time;
        messengerRoom.roomInfo.endMessageId = messageId;
        scrollBody.append(item);

        chatBody.scrollTop(chatBody.prop('scrollHeight'));
    } else {
        (function () {
            const messages = $('.-chat-message');
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

    messengerRoom.roomInfo.messages[messageId] = {
        messageId: messageId,
        insertTime: time,
        messageType: messageType,
        sendReceive: sendReceive,
        contents: contents,
        userId: userId,
        username: username,
    };
};

MessengerRoom.prototype.changeRoomName = function (companyId, roomName) {
    const messengerRoom = this;
    ipcRenderer.send('changeRoomName', {"roomId" : messengerRoom.roomId, "companyId" : companyId, "roomName" : roomName });
};

MessengerRoom.prototype.closeRoom = function () {
    window.close();
}

MessengerRoom.prototype.loadMessages = function () {
    const messengerRoom = this;

    ipcRenderer.send('requestLoadMessages', {"roomId": messengerRoom.roomId, "option" : {"startMessageId": messengerRoom.roomInfo.startMessageId, "limit": messengerRoom.READ_LIMIT + 1}});

    ipcRenderer.on('responseLoadMessages', function (event, arg) {
        const response = arg.response;

        if (response.data.chattingMessages.length <= 0)
            return;

        response.data.chattingMessages.sort(function (a, b) {
            return parseFloat(a.cur_timestr || a.insertTime) - parseFloat(b.cur_timestr || b.insertTime);
        });

        for (let i = response.data.chattingMessages.length - 1; i >= 0; i--) {
            if (messengerRoom.roomId === response.data.chattingMessages[i].roomId) {
                if (response.data.chattingMessages[i].messageId !== messengerRoom.roomInfo.startMessageId)
                    messengerRoom._appendMessage(response.data.chattingMessages[i].roomId,
                        response.data.chattingMessages[i].messageId,
                        response.data.chattingMessages[i].insertTime,
                        response.data.chattingMessages[i].type,
                        response.data.chattingMessages[i].sendReceive,
                        response.data.chattingMessages[i].content,
                        response.data.chattingMessages[i].userid,
                        response.data.chattingMessages[i].userName,
                        response.data.chattingMessages[i].unreadMessageCount);
            } else {
                console.log("[ERROR] loadMessage : roomId 정보가 다름");
            }
        }
    });
}

MessengerRoom.prototype.sendMessage = function () {
    const messengerRoom = this;

    const message = messengerRoom.ui.messageInput.val();

    if (!messengerRoom.roomId || !message)
        return;

    const ciphertext = CryptoJS.AES.encrypt(message, cipherKey).toString();
    ipcRenderer.send('sendMessage', {"roomId": messengerRoom.roomId, "ciphertext" : ciphertext });

    messengerRoom.ui.messageInput.val('');
}

MessengerRoom.prototype.clearSearching = function () {
    const messengerRoom = this;

    if (messengerRoom.roomInfo) {
        messengerRoom.roomInfo.searchingMessage = null;
        messengerRoom.roomInfo.searchingMessages = [];
        messengerRoom.ui.searchingTextCountExpression.attr('data-total', 0).text('0/0');
        messengerRoom.ui.room.find('.-chat-message').removeClass('active');
    }
};

// 대화방 안에서의 안읽은 메세지 카운트 갱신
MessengerRoom.prototype.updateMessageReadCount = function () {
    const messengerRoom = this;

    const memberIds = keys(messengerRoom.roomInfo.members);
    const lastReadMessageTimesOfEachMember = {};

    memberIds.map(function (userid) {
        const lastReadMessageId = messengerRoom.roomInfo.members[userid].lastReadMessageId;
        lastReadMessageTimesOfEachMember[userid] = lastReadMessageId && messengerRoom.roomInfo.messages[lastReadMessageId]
            ? messengerRoom.roomInfo.messages[lastReadMessageId].insertTime
            : (messengerRoom.roomInfo.startMessageTime || 0) - 1;
    });

    messengerRoom.ui.room.find('.-chat-message').each(function () {
        const messageId = $(this).attr('data-id');
        const message = messengerRoom.roomInfo.messages[messageId];
        if (!message)
            throw 'ui에 표시된 message인데, 정보가 없다는 건 messages 관리하는 로직에 문제가 있다는 뜻.';

        $(this).find('.-unread-count').text(memberIds.filter(function (userid) {
            return lastReadMessageTimesOfEachMember[userid] < message.insertTime;
        }).length || '');
    });
};

MessengerRoom.prototype.openInvitationModal = function () {
    const messengerRoom = this;

    ipcRenderer.send('openInvitationModal', {"roomId": messengerRoom.roomId, "members": messengerRoom.roomInfo.members});
};

MessengerRoom.prototype.initRoom = function () {
    const messengerRoom = this;

    messengerRoom.ui.room.find('.-close-room').click(function () {
        messengerRoom.closeRoom();
    });

    messengerRoom.ui.room.find('.-leave-room').click(function () {
        if (!messengerRoom.roomInfo)
            return;

        messengerRoom.ui.roomWindow.confirm('채팅방을 나가시겠습니까?').done(function () {
            ipcRenderer.send('leaveRoom', {"roomId" : messengerRoom.roomId});
            messengerRoom.closeRoom();
        });
    });

    function setSearchingMessages() {
        if (!messengerRoom.roomInfo.searchingMessage)
            return;

        messengerRoom.roomInfo.searchingMessages = [];

        messengerRoom.ui.room.find('.-chat-message:not(.-system-message):contains("' + messengerRoom.roomInfo.searchingMessage.replace(/"/gi, '\\\\"') + '")').each(function () {
            messengerRoom.roomInfo.searchingMessages.push({messageId: $(this).attr('data-id'), messageTime: $(this).attr('data-time')});
        });

        messengerRoom.ui.searchingTextCountExpression.attr('data-total', messengerRoom.roomInfo.searchingMessages.length);
    }

    const searchButton = messengerRoom.ui.room.find('.-search-button');
    const searchText = messengerRoom.ui.room.find('.-search-text');

    searchButton.click(function () {
        const keyword = searchText.val().trim();
        if (!keyword) {
            messengerRoom.clearSearching();
            return;
        }

        messengerRoom.roomInfo.searchingMessage = keyword;
        setSearchingMessages();
        messengerRoom._moveToText(0);
    });

    searchText.keyup(function (event) {
        console.log(event.keyCode)
        if (event.keyCode === 13) {
            searchButton.click();
        } else if (event.keyCode === 27) {
            $(this).val('');
            messengerRoom.clearSearching();
        }
    });

    messengerRoom.ui.room.find('.-move-to-prev-text').click(function () {
        messengerRoom._moveToText(parseInt(messengerRoom.ui.searchingTextCountExpression.attr('data-index')));
    });

    messengerRoom.ui.room.find('.-move-to-next-text').click(function () {
        messengerRoom._moveToText(parseInt(messengerRoom.ui.searchingTextCountExpression.attr('data-index')) - 2);
    });

    messengerRoom.ui.room.find('.-upload-file').click(function () {
        if (messengerRoom.roomInfo) {
            window.open($.addQueryString(contextPath + '/messenger-upload-file', {
                roomId: messengerRoom.roomInfo.id,
                messengerSocketUrl: messengerRoom.messengerSocketUrl
            }), '_sendFile');
        }
    });

    messengerRoom.ui.messageInput.keydown(function (key) {
        if (key.keyCode === 13)
            if (!key.shiftKey) {
                messengerRoom.sendMessage();
                return false;
            }
    });

    (function scrollToBottom() {
        const chatBody = messengerRoom.ui.room.find('.room-content');
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
            const scrollContainer = messengerRoom.ui.room.find('.room-content');
            if (!scrollContainer.length)
                return setScrollEvent();

            scrollContainer.scroll(function () {
                if ($(this).scrollTop())
                    return;

                if (messengerRoom.roomInfo) {
                    messengerRoom.loadMessages();
                }
            });
        }, 100);
    })();
};