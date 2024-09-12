function IpccCommunicator() {
    this.socket = null;
    this.init();
}

IpccCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
IpccCommunicator.prototype.init = function () {
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
};
IpccCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
IpccCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
IpccCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};
IpccCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
IpccCommunicator.prototype.connect = function (url, serverIp, companyId, userId, extension, password, userType, fromUi, multi_yn) {
    this.url = url;
    this.request = {serverIp: serverIp, companyId: companyId, userId: userId, extension: extension, password: password, userType: userType, option: "0", fromUi: fromUi, multi_yn: multi_yn};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnection': true, 'resource': 'socket.io'});
        this.socket.on('connect', function () {
            _this.socket.emit('climsg_login', {
                company_id: _this.request.companyId,
                userid: _this.request.userId,
                exten: _this.request.extension,
                passwd: _this.request.password,
                pbxhost: _this.request.serverIp,
                usertype: _this.request.userType,
                option: _this.request.option,
                fromUi: _this.request.fromUi,
                multi_yn: _this.request.multi_yn
            });
            _this.parse("NODEJS|KIND:CONNECT_OK");
        }).on('svcmsg', function (data) {
            _this.parse(data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
        }).on('svcloginerror', function (data) {
            console.log(data);
            if (!_this.events.svcloginerror)
                return;
            for (let i = 0; i < _this.events.svcloginerror.length; i++) {
                _this.events.svcloginerror[i].apply(_this, [data]);
            }
        }).on('disconnect', function () {
            _this.processor.NODESVC_STATUS("DISCONNECT");
        }).on('error', function () {
            console.error(arguments);
            _this.processor.NODESVC_STATUS("ERROR");
        }).on('end', function () {
            _this.processor.NODESVC_STATUS("END");
        }).on('close', function () {
            _this.processor.NODESVC_STATUS("CLOSE");
        });
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }
};
IpccCommunicator.prototype.disconnect = function () {
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
IpccCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.serverIp, this.request.companyId, this.request.userId, this.request.extension, this.request.password, this.request.userType]);
};
IpccCommunicator.prototype.send = function (message) {
    if (!this.peer || !('phoneNumber' in this.peer))
        throw 'connect() 수행 필요';

    this.status.receivedClientCommand = message.indexOf("RECEIVE") >= 0;

    if (!this.socket)
        return this.processor.NODESVC_STATUS("RELOADED");

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};
IpccCommunicator.prototype.processor = {
    LOGIN: function (message, kind, data1, data2, data3, data4, data5, data6, data7, data8) {
        if (kind !== "LOGIN_OK" && kind !== "LOGIN_ALREADY")
            return;

        this.peer = {
            phoneNumber: data1,
            phonePeer: data5,
            userName: data2,
            forwardWhen: data6,
            forwardNumber: data7,
            recordType: data8
        };

        this.status.memberStatus = data3;
        this.status.phoneStatus = data4;

        this.send("CMD|LOGIN_ACK");
    },
    PEER: null,
    MEMBERSTATUS: function (message, kind) {
        if (this.status.cMemberStatus !== "1")
            this.status.bMemberStatus = this.status.cMemberStatus;
        this.status.cMemberStatus = parseInt(kind);
        this.status.memberStatus = parseInt(kind);
    },
    CALLEVENT: function (message, kind, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10) {
        this.status.clickKey = data10 ? data10.toLowerCase() : '';
    },
    HANGUPEVENT: function (message, kind, data1, data2, data3, data4, data5, data6, data7, data8) {
        this.send("CMD|HANGUP_ACK|" + data5 + ","
            + (data8 !== ""
                ? data8
                : data1.length === 3 && data2.length === 3 //내선끊은후 이전 상태콘트롤
                    ? this.status.bMemberStatus
                    : 'NORMAL'));
    },
    RECORDSTATUS: null,
    CALLBACKEVENT: null,
    MULTICHANNEL: null,
    FORWARDING: null,
    CALLSTATUS: null,
    DTMFREADEVENT: null,
    PDSMEMBERSTATUS: null,
    PDS_READY: null,
    PDS_STOP: null,
    PDS_START: null,
    PDS_DELETE: null,
    PDS_STAT: null,
    PDS_STATUS: null,
    REC_START: null,
    REC_STOP: null,
    SERVER_STATUS: null,
    NODESVC_STATUS: function (message, kind) {
    },
    MSG: null,
    USERINPUT: null,
    BYE: null
};
IpccCommunicator.prototype.parse = function (message) {
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

    if (o.PEER && this.peer.phonePeer !== o.PEER)
        return;

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
IpccCommunicator.prototype.setMemberStatus = function (status) {
    this.send("CMD|MEMBERSTATUS|" + status + "," + this.peer.phoneNumber + "," + this.status.memberStatus);
};
IpccCommunicator.prototype.setPdsStatus = function (status) {
    this.send("CMD|PDSMEMBERSTATUS|" + status + "," + this.peer.phoneNumber);
};
IpccCommunicator.prototype.clickDial = function (cid, number) {
    this.send("CMD|CLICKDIAL|" + cid + "," + number + ",oubbound");
};
IpccCommunicator.prototype.receiveCall = function () {
    this.send("CMD|RECEIVE|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.hangUp = function () {
    this.send("CMD|HANGUP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.pickUp = function () {
    this.send("CMD|PICKUP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.attended = function (extension) {
    this.send("CMD|ATTENDED|" + extension);
};
IpccCommunicator.prototype.attendedHangUp = function () {
    this.send("CMD|ATTENDEDHANGUP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.attendedToOut = function (phoneNumber) {
    this.send("CMD|ATTENDED_OUT|" + phoneNumber);
};
IpccCommunicator.prototype.redirect = function (extension) {
    this.send("CMD|REDIRECT|" + extension);
    this.status.redirect = this.status.recordId;
};
IpccCommunicator.prototype.redirectToOut = function (phoneNumber) {
    this.send("CMD|REDIRECT_OUT|" + phoneNumber.replace(/(\()|(\))|(-)/gi, ""));
    this.status.recordId = null;
};
IpccCommunicator.prototype.redirectHunt = function (phoneNumber) {
    this.send("CMD|REDIRECT_HUNT|" + phoneNumber);
    this.status.recordId = null;
};
IpccCommunicator.prototype.forward = function (forwarding) {
    this.send("CMD|FORWARDING|" + this.peer.phoneNumber + "," + forwarding + "," + this.peer.forwardWhen);
};
IpccCommunicator.prototype.getLastEvent = function () {
    this.send("CMD|GET_LASTEVENT|callevent");
};
IpccCommunicator.prototype.sendMessage = function (peer, message) {
    this.send("CMD|MSG|" + peer + "|" + encodeURI(message));
};
IpccCommunicator.prototype.eavesdrop = function (extension) {
    this.send("CMD|SPY|" + this.peer.phonePeer + "," + extension);
};
IpccCommunicator.prototype.startHolding = function () {
    this.send("CMD|HOLD_START|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.stopHolding = function () {
    this.send("CMD|HOLD_STOP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.startMultiCall = function (rid, extension, number) {
    this.send("CMD|MULTIDIAL_START|" + this.peer.phoneNumber + "," + rid + "," + extension + "," + number + ",M");
};
IpccCommunicator.prototype.stopMultiCall = function () {
    this.send("CMD|MULTIDIAL_END|" + this.peer.phoneNumber);
};
IpccCommunicator.prototype.startRecording = function () {
    this.send("CMD|REC_START|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.stopRecording = function () {
    this.send("CMD|REC_STOP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.startListeningRecordedWithCaller = function (fileId, isMonitor) {
    this.send("CMD|RECORD_PLAY_START|" + fileId + "," + isMonitor);
};
IpccCommunicator.prototype.stopListeningRecordedWithCaller = function () {
    this.send("CMD|RECORD_PLAY_END|" + this.peer.phoneNumber);
};
// C -> S 전화거절
IpccCommunicator.prototype.reject = function () {
    this.send("CMD|REJECT|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.connectArs = function (arsNumber) {
    this.send("CMD|ARS_CONNECT|" + arsNumber);
};
IpccCommunicator.prototype.startDTMF = function () {
    this.send("CMD|DTMFREAD|Y");
};
IpccCommunicator.prototype.stopDTMF = function () {
    this.send("CMD|DTMFREAD|N");
};
IpccCommunicator.prototype.arsConnect = function (dtmfType, memberNo) {
    const typeCode = (dtmfType === 'PASSWORD') ? '0000' : '1111';
    if (memberNo === undefined || memberNo === '') {
        this.send("CMD|ARS_CONNECT|" + typeCode + this.peer.phonePeer);
    } else {
        this.send("CMD|ARS_CONNECT|" + typeCode + this.peer.phonePeer + memberNo);
    }
};
IpccCommunicator.prototype.arsCert = function(userPhone,userIdx,mainId){
    this.send("CMD|ARS_CERT|"+ userPhone +","+ userIdx +","+ mainId)
}
IpccCommunicator.prototype.clickByCampaign = function (cid, number, type, typeId, customId) {
    this.send("CMD|CLICKBYCAMPAIGN|" + cid + "," + number + "," + (type || 'TAB') + "," + typeId + "," + (this.request.userId + '_' + moment().format('YYYYMMDDHHmmss') + "," + (customId || '')));
};
IpccCommunicator.prototype.protectArs = function (soundSeq) {
    this.send("CMD|PROTECT_ARS|" + soundSeq);
};
IpccCommunicator.prototype.applyCms = function (bank, account, holder, transferringDate, transferringAmount) {
    this.send("CMD|CMS|" + bank.replace(/[|]/gi, "") + "|"
        + account.replace(/[|]/gi, "") + "|"
        + holder.replace(/[|]/gi, "") + "|"
        + transferringDate.replace(/[|]/gi, "") + "|"
        + transferringAmount.replace(/[|]/gi, "") + "|"
    );
};
IpccCommunicator.prototype.voc = function (vocGroup, arsResearch) {
    this.send("CMD|VOC|" + vocGroup + arsResearch);
};
