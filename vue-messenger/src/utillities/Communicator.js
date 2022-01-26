import {io} from "socket.io-client";

function Communicator() {
    this.socket = null;
    this.init();
}

Communicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
Communicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.connected = false
    this.sendingMessageSequence = 0

    this.logClear();

    return this;
};
Communicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
Communicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
Communicator.prototype.log = function (isSend, command, body) {
    console.log((isSend ? "C->S" : "S->C") + ':: [' + (this.status.eventNumber++) + '.' + command + '] ' + (typeof body === 'object' ? JSON.stringify(body) : body));
};
Communicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
Communicator.prototype.connect = function (url, senderKey, userKey, ip, mode, reconnect) {
    this.url = url;
    this.request = {sender_key: senderKey, user_key: userKey, my_ip: ip, mode: mode,};

    const _this = this;
    try {
        this.socket = io(url, {'secure': url.startsWith('https')}, {'reconnect': true, 'resource': 'socket.io'})
    } catch (error) {
        console.error(error);
        return setTimeout(function () {
            _this.recovery();
        }, 2000);
    }

    const serverCommands = ['webchatsvc_start', 'webchatsvc_message', 'webchatsvc_close', 'disconnect'];
    const uncheckedServerCommands = ['connect', 'error', 'end', 'close'];

    reconnect ? this.restart() : this.start()
    this.on('webchatsvc_start', data => this.connected = data.result === 'OK')
    this.socket.on('webchatsvc_ping', data => this.socket.emit('webchatcli_pong', {pong_cnt: data.ping_cnt, receive: 'OK'}))

    serverCommands.map(function (command) {
        _this.socket.on(command, function (data) {
            _this.process(command, data);
        });
    });
    uncheckedServerCommands.map(function (command) {
        _this.socket.on(command, function () {
            _this.log(false, command, arguments);
        });
    });
};
Communicator.prototype.disconnect = function () {
    this.socket?.disconnect();
    this.socket = null;
    this.init();
};
Communicator.prototype.close = function () {
    this.socket?.close();
    this.socket = null;
    this.init();
};
Communicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';

    this.connect.apply(this, [
        this.url,
        this.request.sender_key,
        this.request.user_key,
        this.request.my_ip,
        this.request.mode,
        true
    ])
};
Communicator.prototype.process = function (event, data) {
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
}
Communicator.prototype.getMessageId = function () {
    return this.request.user_key + '_' + (++this.sendingMessageSequence)
}
Communicator.prototype.start = function () {
    this.socket.emit('webchatcli_start', this.request)
}
Communicator.prototype.restart = function () {
    this.socket.emit('webchatcli_restart', this.request)
}
Communicator.prototype.requestIntro = function () {
    this.socket.emit('webchatcli_message', Object.assign(this.request, {message_id: this.getMessageId(), message_type: 'intro'}))
}
Communicator.prototype.requestRootBlock = function (botId) {
    this.socket.emit('webchatcli_message', Object.assign(this.request, {message_id: this.getMessageId(), message_type: 'bot_root_block', message_data: botId}))
}
Communicator.prototype.sendText = function (botId, text, lastReceiveMessageType) {
    this.socket.emit('webchatcli_message', Object.assign(this.request, {
        message_id: this.getMessageId(),
        message_type: 'text',
        message_data: {last_receive_message_type: lastReceiveMessageType, text_data: text}
    }))
}
Communicator.prototype.sendAction = function (botId, data, lastReceiveMessageType) {
    this.socket.emit('webchatcli_message', Object.assign(this.request, {
        message_id: this.getMessageId(),
        message_type: 'action',
        message_data: Object.assign({last_receive_message_type: lastReceiveMessageType}, data)
    }))
}

export default Communicator
