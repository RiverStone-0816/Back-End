import {io} from "socket.io-client";

function IpccAdminCommunicator() {
    this.socket = null;
    this.init();
}

IpccAdminCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
IpccAdminCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
IpccAdminCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
IpccAdminCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
IpccAdminCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};
IpccAdminCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
IpccAdminCommunicator.prototype.connect = function (url, companyId, userId, pbxName) {
    this.url = url;
    this.request = {companyId: companyId, userId: userId, pbxName: pbxName};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'secure': url.includes('https')}, {'reconnect': true, 'resource': 'socket.io'});
        this.socket.emit('climsg_join', {
            company_id: _this.request.companyId,
            userid: _this.request.userId,
            pbxname: _this.request.pbxName
        }).on('connect', function () {
            _this.parse("NODEJS|KIND:CONNECT");
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

    return this;
};
IpccAdminCommunicator.prototype.disconnect = function () {
    try {
        this.socket.emit('climsg_bye', "Bye.");
    } catch (ignored) {
        console.log('error: climsg_bye')
    }
    try {
        this.socket.disconnect();
    } catch (ignored) {
        console.log('error: disconnect()')
    }

    this.socket = null;
    this.init();
};
IpccAdminCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.serverIp, this.request.companyId, this.request.userId]);
};
IpccAdminCommunicator.prototype.send = function (message) {
    if (!this.socket)
        return this.processor.NODESVC_STATUS("RELOADED");

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};
IpccAdminCommunicator.prototype.processor = {
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
    NODESVC_STATUS: function () { // message, kind, peer, data1, data2, data3, data4, data5, data6, data7, data8
    },
    BYE: null
};
IpccAdminCommunicator.prototype.parse = function (message) {
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
    const args = [message, o.KIND, o.PEER, o.DATA1, o.DATA2, o.DATA3, o.DATA4, o.DATA5, o.DATA6, o.DATA7];

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

export default IpccAdminCommunicator
