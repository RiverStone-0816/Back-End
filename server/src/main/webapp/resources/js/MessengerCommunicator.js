function MessengerCommunicator() {
    this.socket = null;
    this.init();
}

MessengerCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
MessengerCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
MessengerCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
MessengerCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
MessengerCommunicator.prototype.log = function (isSend, command, body) {
    console.log((isSend ? "C->S" : "S->C") + ':: [' + (this.status.eventNumber++) + '.' + command + '] ' + (typeof body === 'object' ? JSON.stringify(body) : body));
};
MessengerCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
MessengerCommunicator.prototype.connect = function (url, companyId, userid, username, passwd) {
    this.url = url;
    this.request = {
        companyId: companyId,
        userid: userid,
        username: username,
        passwd: passwd
    };

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnect': true, 'resource': 'socket.io'});
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }

    const serverCommands = ['svc_login', 'svc_logout', 'svc_msg', 'svc_join_msg', 'svc_invite_room', 'svc_leave_room', 'svc_read_confirm', 'svc_memo_send'];
    const uncheckedServerCommands = ['connect', 'disconnect', 'error', 'end', 'close'];

    const o = this.socket.emit('chatt_login', {
        company_id: _this.request.companyId,
        userid: _this.request.userid,
        username: _this.request.username,
        passwd: _this.request.passwd
    }).on('svcmsg_ping', function () {
        _this.socket.emit('climsg_pong');
    });

    serverCommands.map(function (command) {
        o.on(command, function (data) {
            _this.process(command, data);
        });
    });
    uncheckedServerCommands.map(function (command) {
        o.on(command, function () {
            _this.log(false, command, arguments);
        });
    });
};
MessengerCommunicator.prototype.disconnect = function () {
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
MessengerCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [
        this.url,
        this.request.companyId,
        this.request.userid,
        this.request.username,
        this.request.passwd
    ]);
};
MessengerCommunicator.prototype.process = function (event, data) {
    this.log(false, event, data);

    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, [data, event]);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, [data, event]);
        }
    }

    return this;
};
MessengerCommunicator.prototype.sendMessage = function (roomId, contents) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        send_receive: "S",
        etc_data: "",
        contents: contents
    });
};
MessengerCommunicator.prototype.confirmMessage = function (roomId, messageId) {
    this.socket.emit('cli_read_confirm', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        last_read_message_id: messageId
    });
};
MessengerCommunicator.prototype.join = function (roomId) {
    this.socket.emit('cli_join_room', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid
    });
};
MessengerCommunicator.prototype.leave = function (roomId) {
    this.socket.emit('cli_leave_room', {
        company_id: this.request.companyId,
        room_id: roomId,
        leave_userid: this.request.userid
    });
};
MessengerCommunicator.prototype.invite = function (roomId, userId, userName) {
    this.socket.emit('cli_invite_room', {
        company_id: this.request.companyId,
        room_id: roomId,
        invite_userid: this.request.userid,
        invited_username: ((userName instanceof Array) ? userName.join(',') : userName),
        invited_userid: ((userId instanceof Array) ? userId.join(',') : userId)
    });
};
MessengerCommunicator.prototype.changeRoomName = function (roomId, roomName) {
    this.socket.emit('cli_roomname_change', {
        company_id: this.request.companyId,
        room_id: roomId,
        change_room_name: roomName,
    });
};
MessengerCommunicator.prototype.sendMemo = function (userId, messageId) {
    this.socket.emit('cli_memo_send', {
        company_id: this.request.companyId,
        message_id: messageId,
        send_userid: this.request.userid,
        receive_userid: userId
    });
};
MessengerCommunicator.prototype.readMemo = function (messageId, sendUserid) {
    this.socket.emit('cli_memo_read', {
        company_id: this.request.companyId,
        read_userid: this.request.userid,
        message_id: messageId,
        send_userid: sendUserid,
    });
};
