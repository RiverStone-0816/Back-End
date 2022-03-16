function TalkCommunicator() {
    this.socket = null;
    this.init();
}

TalkCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
TalkCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
TalkCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
TalkCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
TalkCommunicator.prototype.log = function (isSend, command, body) {
    console.log((isSend ? "C->S" : "S->C") + ':: [' + (this.status.eventNumber++) + '.' + command + '] ' + (typeof body === 'object' ? JSON.stringify(body) : body));
};
TalkCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
TalkCommunicator.prototype.connect = function (url, companyId, userid, passwd, authtype, usertype) {
    this.url = url;
    this.request = {
        companyId: companyId,
        userid: userid,
        passwd: passwd,
        usertype: usertype,
        authtype: authtype,
    };

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnect': true, 'resource': 'socket.io'});
        this.socket.emit('cli_join', {
            company_id: _this.request.companyId,
            userid: _this.request.userid,
            passwd: _this.request.passwd,
            usertype: _this.request.usertype,
            authtype: _this.request.authtype,
            from_ui: 'API',
        }).on('connect', function () {
            _this.log(false, 'connect', arguments);
        }).on('svc_login', function (data) {
            _this.log(false, 'svc_login', data);
            _this.process('svc_login', data);
        }).on('svc_logout', function (data) {
            _this.log(false, 'svc_logout', data);
            _this.process('svc_logout', data);
        }).on('svc_msg', function (data) {
            _this.log(false, 'svc_msg', data);
            _this.process('svc_msg', data);
        }).on('svc_control', function (data) {
            _this.log(false, 'svc_control', data);
            _this.process('svc_control', data);
        }).on('svc_redist', function (data) {
            _this.log(false, 'svc_redist', data);
            _this.process('svc_redist', data);
        }).on('svc_dist_yn', function (data) {
            _this.log(false, 'svc_dist_yn', data);
            _this.process('svc_dist_yn', data);
        }).on('svc_end', function (data) {
            _this.log(false, 'svc_end', data);
            _this.process('svc_end', data);
        }).on('svc_delete', function (data) {
            _this.log(false, 'svc_delete', data);
            _this.process('svc_delete', data);
        }).on('svc_custom_match', function (data) {
            _this.log(false, 'svc_custom_match', data);
            _this.process('svc_custom_match', data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
        }).on('disconnect', function () {
            _this.log(false, 'disconnect', arguments);
        }).on('error', function () {
            _this.log(false, 'error', arguments);
        }).on('end', function () {
            _this.log(false, 'end', arguments);
        }).on('close', function () {
            _this.log(false, 'close', arguments);
        });
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }
};
TalkCommunicator.prototype.disconnect = function () {
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
TalkCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [
        this.url,
        this.request.companyId,
        this.request.userid,
        this.request.passwd,
        this.request.usertype,
        this.request.authtype,
    ]);
};
TalkCommunicator.prototype.process = function (event, data) {
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
TalkCommunicator.prototype.sendMessage = function (roomId, channelType, senderKey, userKey, contents) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "S",
        user_key: userKey,
        etc_data: "",
        type: 'text',
        contents: contents,
    });
};
TalkCommunicator.prototype.sendCustomMatch = function (roomId, senderKey, userKey, groupId, customId, customName) {
    this.socket.emit('cli_custom_match', {
        company_id : this.request.companyId,
        sender_key : senderKey,
        user_key : userKey,
        room_id : roomId,
        userid : this.request.userid,
        maindb_group_id : groupId,
        maindb_custom_id : customId,
        maindb_custom_name : customName,
    });
};
TalkCommunicator.prototype.sendImageTemplate = function (roomId, channelType, senderKey, userKey, filePath) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "S",
        user_key: userKey,
        etc_data: "",
        type: 'image',
        contents: filePath,
    });
};
TalkCommunicator.prototype.sendTemplateBlock = function (roomId, channelType, senderKey, userKey, blockId) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "S",
        user_key: userKey,
        etc_data: "",
        type: 'block',
        contents: '' + blockId,
    });
};
TalkCommunicator.prototype.sendFile = function (roomId, channelType, senderKey, userKey, filePath) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "S",
        user_key: userKey,
        etc_data: "",
        type: 'file',
        contents: filePath,
    });
};
TalkCommunicator.prototype.assignUnassignedRoomToMe = function (roomId, channelType, senderKey, userKey, contents) {
    this.socket.emit('cli_control', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "SZ",
        user_key: userKey,
        etc_data: "",
        contents: contents || "",
    });
};
TalkCommunicator.prototype.assignAssignedRoomToMe = function (roomId, channelType, senderKey, userKey, contents) {
    this.socket.emit('cli_control', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "SG",
        user_key: userKey,
        etc_data: "",
        contents: contents || "",
    });
};
TalkCommunicator.prototype.endRoom = function (roomId, channelType, senderKey, userKey) {
    this.socket.emit('cli_end', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "SE",
        user_key: userKey,
        etc_data: "",
        contents: ""
    });
};
TalkCommunicator.prototype.deleteRoom = function (roomId, channelType, senderKey, userKey) {
    this.socket.emit('cli_delete', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        channel_type: channelType,
        sender_key: senderKey,
        send_receive: "SE",
        user_key: userKey,
        etc_data: "",
        contents: ""
    });
};

TalkCommunicator.prototype.changeDistribution = function (distributed) {
    this.socket.emit('cli_dist_yn', {
        company_id: this.request.companyId,
        userid: this.request.userid,
        dist_yn: distributed ? 'Y' : 'N'
    });
};
/**
 * @param room_infos [{channel_type, room_id, user_key}]
 * @param target_members [{userid}]
 */
TalkCommunicator.prototype.redistribution = function (room_infos, target_members) {
    this.socket.emit('cli_redist', {
        company_id: this.request.companyId,
        room_infos: room_infos,
        target_members: target_members,
        admin_id: this.request.userid,
    });
};
