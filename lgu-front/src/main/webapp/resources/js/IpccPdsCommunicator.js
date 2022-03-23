function IpccPdsCommunicator() {
    this.socket = null;
    this.init();
}

IpccPdsCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
IpccPdsCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
IpccPdsCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
IpccPdsCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
IpccPdsCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};
IpccPdsCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
IpccPdsCommunicator.prototype.connect = function (url, companyId, userId, password) {
    this.url = url;
    this.request = {companyId: companyId, userId: userId, password: password};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnection': true, 'resource': 'socket.io'});
        this.socket.emit('climsg_login', {
            company_id: _this.request.companyId,
            userid: _this.request.userId,
            passwd: _this.request.password,
            serverip: 'PBX_VIP',
            option: 'pds'
        }).on('connect', function () {
            _this.parse("NODEJS|KIND:CONNECT_OK");
        }).on('svcmsg', function (data) {
            _this.parse(data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
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
IpccPdsCommunicator.prototype.disconnect = function () {
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
IpccPdsCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.companyId, this.request.userId, this.request.password]);
};
IpccPdsCommunicator.prototype.send = function (message) {
    if (!this.socket)
        return this.processor.NODESVC_STATUS("RELOADED");

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};
IpccPdsCommunicator.prototype.processor = {
    LOGIN: null,
    LOGOUT: null,
    SERVER_STATUS: null,
    ADMPEER: null,
    ADMLOGIN: null,
    ADMCALLEVENT: null,
    ADMHANGUPEVENT: null,
    MEMBERSTATUS: null,
    PDSMEMBERSTATUS: null,
    SPY_RES: null,
    ADMQUEUE: null,
    LOGIN_CNT: null,
    STATUS_CNT: null,
    PEERSTATUS: null,
    NODESVC_STATUS: function (message, kind, peer, data0, data1, data2, data3, data4, data5, data6, data7, data8) {
    },
    BYE: null
};
IpccPdsCommunicator.prototype.parse = function (message) {
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
IpccPdsCommunicator.prototype.start = function (pdsGroupId) {
    this.send("CMD|PDS_START|" + pdsGroupId);
};
IpccPdsCommunicator.prototype.stop = function (pdsGroupId) {
    this.send("CMD|PDS_STOP|" + pdsGroupId);
};
IpccPdsCommunicator.prototype.delete = function (pdsGroupId) {
    this.send("CMD|PDS_DELETE|" + pdsGroupId);
};
IpccPdsCommunicator.prototype.setRid = function (executeId, pdsGroupId, value) {
    this.send("CMD|PDS_SETRID|" + executeId + "," + pdsGroupId + "," + value);
};
IpccPdsCommunicator.prototype.setSpeed = function (executeId, pdsGroupId, value) {
    this.send("CMD|PDS_SETRID|" + executeId + "," + pdsGroupId + "," + value);
};
IpccPdsCommunicator.prototype.setTimeout = function (executeId, pdsGroupId, value) {
    this.send("CMD|PDS_SETRID|" + executeId + "," + pdsGroupId + "," + value);
};