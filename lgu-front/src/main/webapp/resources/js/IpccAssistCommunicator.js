function IpccAssistCommunicator() {
    this.socket = null;
    this.init();
}

IpccAssistCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
}

IpccAssistCommunicator.prototype.init = function () {
    this.url = null;
    this.peer = null;
    this.request = null;
    this.status = {
        eventNumber: 0,
        receivedClientCommand: false,
        bMemberStatus: 0,
        cMemberStatus: 0,
        recordId: null,
        redirect: null,
        clickKey: null
    };
    this.events = {};
    this.logClear();

    return this;
}

IpccAssistCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};

IpccAssistCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};

IpccAssistCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};

IpccAssistCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};

IpccAssistCommunicator.prototype.connect = function (url, serverIp, companyId, userId, extension, password, userType) {
    this.url = url;
    this.request = {serverIp: serverIp, companyId: companyId, userId: userId, extension: extension, password: password, userType: userType};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnection': true, 'resource': 'socket.io', withCredentials: false});
        this.socket.on('connect', function () {
            _this.socket.emit('climsg_login', {
                company_id: _this.request.companyId,
                userid: _this.request.userId,
                passwd: _this.request.password,
            })
        }).on('svcmsg_stt', function (data) {
            console.log('svcmsg_stt > data = ', data)
            if(data.ipcc_userid === _this.request.userId){
                if(data && data.message_id){
                    window.recordSvcmsgSttMessageId = data.message_id
                }

                _this.parse('STT|KIND:STTMESSAGE|DATA1:'+JSON.stringify(data));
            }
        }).on('svcmsg_keyword', function (data) {
            if(data.ipcc_userid === _this.request.userId)
                _this.parse('STT_KEYWORD|KIND:KEYWORD|DATA1:'+JSON.stringify(data));
        }).on('svcmsg_message', function (data) {
            console.log('svcmsg_message', data);
        }).on('admmonit_message', function (data) {
            _this.parse('ADMINMESSAGE|KIND:MESSAGE|DATA1:'+JSON.stringify(data));
        }).on('admmonit_stt', function (data) {
            _this.parse('ADMINMONITSTT|KIND:STTMESSAGE|DATA1:'+JSON.stringify(data));
        }).on('admmonit_keyword', function (data) {
            _this.parse('ADMINMONITKEYWORD|KIND:KEYWORD|DATA1:'+JSON.stringify(data));
        }).on('svcmsg_monit_start', function(data) {
            console.log('svcmsg_monit_start',data);
        })
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }
};

IpccAssistCommunicator.prototype.disconnect = function () {
    try {
        this.send("Bye.");
    } catch (ignored) {
    }
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};

IpccAssistCommunicator.prototype.send = function (message) {
    if (!this.peer || !('phoneNumber' in this.peer))
        throw 'connect() 수행 필요';

    this.status.receivedClientCommand = message.indexOf("RECEIVE") >= 0;

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};

IpccAssistCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.serverIp, this.request.companyId, this.request.userId, this.request.extension, this.request.password, this.request.userType]);
};

IpccAssistCommunicator.prototype.processor = {
    STT: function (message, kind) {
    },
    STT_KEYWORD: function (message, kind) {
    },
    ADMINMESSAGE: function (message, kind) {
    },
}
IpccAssistCommunicator.prototype.parse = function (message) {
    this.log(false, message);

    const variables = message.split("|");
    if (variables == null || variables.length < 2)
        return;

    const event = variables[0];
    const o = {};
    for (let i = 1; i < variables.length; i++) {
        const args = variables[i].split(":");
        o[args[0]] = '';

        for (let j = 1; j < args.length; j++)
            o[args[0]] += (j === 1 ? '' : ':') + args[j];
    }

    if (event === "USERINPUT") {
        message = variables[1];
    }

    const args = [message, o.KIND, o.DATA1, o.DATA2, o.DATA3, o.DATA4, o.DATA5, o.DATA6, o.DATA7, o.DATA8, o.DATA9, o.DATA10, o.DATA11, o.DATA12, o.DATA13, o.DATA14, o.DATA15, o.DATA16];

    const processor = this.processor[event];
    if (processor)
        processor.apply(this, args);

    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }
};

IpccAssistCommunicator.prototype.mentorCall = function (callUniqueId, message, targetUserId) {
    this.log(true, "{MENTOR: '"+ targetUserId +"', callUniqueId: '"+ callUniqueId +"', message: '"+ message +"'}");
    this.socket.emit('climsg_command', {
        company_id: this.request.companyId,
        userid: this.request.userId,
        target_userid: targetUserId,
        call_uniqueid: callUniqueId,
        message: message,
        command: "SEND_MSG",
        message_id: window && window.recordSvcmsgSttMessageId ? window.recordSvcmsgSttMessageId : ''
    });
}

IpccAssistCommunicator.prototype.monit = function (targetUserId, monitCommend) {
    let message = {
        company_id: this.request.companyId,
        userid: this.request.userId,
        target_userid : targetUserId,
        command: monitCommend
    }
    this.socket.emit("climsg_command", message)
};