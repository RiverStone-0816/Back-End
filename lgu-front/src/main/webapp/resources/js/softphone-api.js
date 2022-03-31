const SIP_PROTO_PREFIX = "sip:";
const SIP_DOMAIN = "ippbx.eicn.co.kr";
const SIP_REALM = "eicn";
const REGISTER_INTERVAL = 5000;
const RECONNECT_INTERVAL = 5000;
const RINGTONE = new Audio('../resource/sounds/SimpleTone.mp3');
const BUSYTONE = new Audio('../resource/sounds/BusySignal.mp3');
let callback_registered_status = function(){};
let callback_unregistered_status = function(){};
let callback_disconnected_status = function(){};

function SoftPhoneApi(){
    this.init();
}

SoftPhoneApi.prototype.init = function() {
    this.WEBRTC_INFO = {};
    this.WEBRTC_INFO.server = {};
    this.WEBRTC_INFO.phone = {};
    this.WEBRTC_INFO.env = { "ringtone": true, "busytone": true };

    this.janus = null;
    this.sipcall = null;
    this.opaqueId = "webrtc-" + Janus.randomString(12);

    this.bitrateTimer = null;
    this.spinner = null;

    this.audioDeviceId = null;
    this.videoDeviceId = null;

    this.audioenabled = false;
    this.videoenabled = false;

    this.registered = false;
    this.masterId = null
    this.helpers = {}
    this.helpersCount = 0;

    this.registerId = null;
    this.januSessionId = null;

    RINGTONE.loop = true;

    return this;
}

SoftPhoneApi.prototype.playTone = function(tone) {
    if (!this.WEBRTC_INFO.env.ringtone) { return; }

    this.stopTones();

    if (tone === "ring") {
        if (!this.WEBRTC_INFO.env.ringtone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
            console.log("-------- RINGTONE.play()");
            RINGTONE.currentTime = 0;
            RINGTONE.play();

            // stop microphone stream acquired by getUserMedia
            stream.getTracks().forEach(function (track) { track.stop(); });
        });
    }
    else if (tone === "busy") {
        if (!this.WEBRTC_INFO.env.busytone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
            console.log("-------- BUSYTONE.play()");
            BUSYTONE.currentTime = 0;
            BUSYTONE.play();

            // stop microphone stream acquired by getUserMedia
            stream.getTracks().forEach(function (track) { track.stop(); });
        });
    }
}

SoftPhoneApi.prototype.stopTones = function() {
    // Ringtone 중지
    console.log("-------- RINGTONE.pause()");
    RINGTONE.pause();
    RINGTONE.currentTime = 0;

    // Busytone 중지
    console.log("-------- BUSYTONE.pause()");
    BUSYTONE.pause();
    BUSYTONE.currentTime = 0;
}

SoftPhoneApi.prototype.registerSoftphone = function() {
    if (this.WEBRTC_INFO.phone.peer_num && this.WEBRTC_INFO.phone.peer_num !== "" && this.WEBRTC_INFO.phone.peer_secret && this.WEBRTC_INFO.phone.peer_secret !== "") {
        // Registration
        Janus.log("sip-server: " +this. WEBRTC_INFO.server.pbx_server_ip + ":" + this.WEBRTC_INFO.server.pbx_server_port);
        const pbx_server = SIP_PROTO_PREFIX + this.WEBRTC_INFO.server.pbx_server_ip + ":" + this.WEBRTC_INFO.server.pbx_server_port;
        const username = SIP_PROTO_PREFIX + this.WEBRTC_INFO.phone.peer_num + "@" + SIP_DOMAIN;

        const register = {
            request: "register",
            username: username,
            ha1_secret: this.WEBRTC_INFO.phone.peer_secret,
            sips: false,
            force_udp: true,
            proxy: pbx_server
        };
        this.sipcall.send({ message: register });
    }
    else {
        Janus.log("WEBRTC_INFO.phone.peer_num: " + this.WEBRTC_INFO.phone.peer_num + ", WEBRTC_INFO.phone.peer_secret:" + this.WEBRTC_INFO.phone.peer_secret);
    }
}

SoftPhoneApi.prototype.start_webrtc = function (peer_num, peer_secret, pbx_server_ip, pbx_server_port, webrtc_server_ip, webrtc_server_port, turn_server_ip, turn_server_port, turn_user, turn_secret) {

    this.WEBRTC_INFO.server = {
        pbx_server_ip: pbx_server_ip
        ,pbx_server_port: pbx_server_port
        ,webrtc_server_ip: webrtc_server_ip
        ,webrtc_server_port: webrtc_server_port
        ,turn_server_ip: turn_server_ip
        ,turn_server_port: turn_server_port
        ,turn_user: turn_user
        ,turn_secret: turn_secret
    };
    this.WEBRTC_INFO.phone = {peer_num: peer_num, peer_secret: peer_secret};

    if (this.januSessionId) {
        clearInterval(this.januSessionId);
        this.januSessionId = null;
    }
    if (this.janus) {
        delete this.janus;
        this.janus = null;
    }
    const _this = this;
    Janus.init({debug: "all", callback: _this.create_webrtc_session(_this)});
}

SoftPhoneApi.prototype.create_webrtc_session = function(_this) {
    console.log('WEBRTC_INFO',_this.WEBRTC_INFO)
    const webrtc_server = "wss://" + _this.WEBRTC_INFO.server.webrtc_server_ip + ":" + _this.WEBRTC_INFO.server.webrtc_server_port;
    const turn_server = "turn:" + _this.WEBRTC_INFO.server.turn_server_ip + ":" + _this.WEBRTC_INFO.server.turn_server_port;

    // 세션 생성
    _this.janus = new Janus(
        {
            server: webrtc_server,
            iceServers: [ {urls: turn_server, username: _this.WEBRTC_INFO.server.turn_user, credential: _this.WEBRTC_INFO.server.turn_secret} ],
            success: function() {
                // SIP 플러그인 Attach
                _this.janus.attach(
                    {
                        plugin: "janus.plugin.sip",
                        opaqueId: _this.opaqueId,
                        success: function(pluginHandle) {
                            _this.sipcall = pluginHandle;
                            Janus.log("Plugin attached! (" + _this.sipcall.getPlugin() + ", id=" + _this.sipcall.getId() + ")");

                            if (_this.WEBRTC_INFO.phone.peer_num !== "" && _this.WEBRTC_INFO.phone.peer_secret !== "") {
                                // 소프트폰을 등록한다.
                                this.registerSoftphone();
                                // 등록실패시 REGISTER_INTERVAL 시간마다 등록을 재시도한다.
                                _this.registerId = setInterval(_this.registerSoftphone, REGISTER_INTERVAL);
                            }
                        },
                        error: function(error) {
                            Janus.error("  -- Error attaching plugin...", error);
                        },
                        consentDialog: function(on) {
                            // getUserMedia 호출 전에 trigger된다.
                            Janus.log("Consent dialog should be " + (on ? "on" : "off") + " now");
                            if (on) {
                            }
                            else {
                            }
                        },
                        iceState: function(state) {
                            Janus.log("ICE state changed to " + state);
                        },
                        mediaState: function(medium, on) {
                            Janus.log("Janus " + (on ? "started" : "stopped") + " receiving our " + medium);
                        },
                        webrtcState: function(on) {
                            Janus.log("Janus says our WebRTC PeerConnection is " + (on ? "up" : "down") + " now");
                        },
                        onmessage: function(msg, jsep) {
                            Janus.debug(" ::: Got a message :::", msg);

                            let error = msg["error"];
                            if (error) {
                                if (!_this.registered) {
                                }
                                else {
                                    // Reset status
                                    _this.sipcall.hangup();
                                }
                                Janus.error(error);
                                return;
                            }

                            let callId = msg["call_id"];
                            let result = msg["result"];

                            if (result && result["event"]) {
                                let event = result["event"];
                                if (event === 'registration_failed') {
                                    Janus.warn("Registration failed: " + result["code"] + " " + result["reason"]);
                                    callback_unregistered_status();

                                    if (_this.registerId) {
                                        clearInterval(_this.registerId);
                                        _this.registerId = null;
                                    }
                                    _this.registerId = setInterval(_this.registerSoftphone, REGISTER_INTERVAL);
                                    return;
                                }
                                else if (event === 'registered') {   // 등록 성공
                                    Janus.log("Successfully registered as " + result["username"] + "!");
                                    callback_registered_status();

                                    if (!_this.registered) {
                                        _this.registered = true;
                                        _this.masterId = result["master_id"];
                                        // FIXME : addhelper
                                    }
                                    // 등록 성공했으므로, 재등록 스케쥴링을 제거한다.
                                    if (_this.registerId) {
                                        clearInterval(_this.registerId);
                                        _this.registerId = null;
                                    }
                                }
                                else if (event === 'calling') { // 전화 발신중
                                    Janus.log("Waiting for the peer to answer...");
                                    // TODO: Any ringtone?
                                    // FIXME : 전화끊기 버튼 활성화
                                }
                                else if (event === 'incomingcall') {    // 전화 수신중 (호인입)
                                    //Janus.log("Incoming call from " + result["username"] + "!");

                                    _this.sipcall.callId = callId;

                                    let doAudio = true, doVideo = true;
                                    let offerLessInvite = false;

                                    if (jsep) {
                                        // What has been negotiated?
                                        doAudio = (jsep.sdp.indexOf("m=audio ") > -1);
                                        doVideo = (jsep.sdp.indexOf("m=video ") > -1);
                                        Janus.debug("Audio " + (doAudio ? "has" : "has NOT") + " been negotiated");
                                        Janus.debug("Video " + (doVideo ? "has" : "has NOT") + " been negotiated");
                                    }
                                    else {
                                        Janus.log("This call doesn't contain an offer... we'll need to provide on ourselves");
                                        offerLessInvite = true;
                                        // In case you want to offer video when reacting to an offerless call, set this to true
                                        doVideo = false;
                                    }

                                    // Is this the result of a transfer?
                                    let transfer = "";
                                    let referredBy = result["referred_by"];
                                    if (referredBy) {
                                        transfer = " (referred by " + referredBy + ")";
                                        transfer = transfer.replace(new RegExp('<', 'g'), '&lt');
                                        transfer = transfer.replace(new RegExp('>', 'g'), '&gt');
                                    }

                                    // Any security offered? A missing "srtp" attribute means plain RTP
                                    let rtpType = "";
                                    let srtp = result["srtp"];
                                    if (srtp === "sdes_optional") {
                                        rtpType = " (SDES-SRTP offered)";
                                    }
                                    else if (srtp === "sdes_mandatory") {
                                        rtpType = " (SDES-SRTP mandatory)";
                                    }

                                    // Notify user
                                    let extra = "";
                                    if (offerLessInvite) {
                                        extra = " (no SDP offer provided)";
                                    }

                                    // CallerID Number 가 0000으로 표시되는 경우, 발신콜로 인식한다.
                                    // 이 때, 자동받기 처리해야 정상적으로 콜이 발신된다.
                                    if (result["username"] && result["username"].indexOf("sip:0000@") > -1) {
                                        Janus.log("Outbound call to " + result["displayname"] + "!" + transfer + rtpType + extra);

                                        // Notice that we can only answer if we got an offer:
                                        // if this was an offerless call, we'll need to create an offer ourselves
                                        let sipCallAction = (offerLessInvite ? _this.sipcall.createOffer : _this.sipcall.createAnswer);
                                        sipCallAction(
                                            {
                                                jsep: jsep,
                                                media: { audio: doAudio, video: doVideo },
                                                success: function(jsep) {
                                                    Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                    let body = { request: "accept" };
                                                    _this.sipcall.send({ message: body, jsep: jsep });
                                                },
                                                error: function() {
                                                    Janus.error("WebRTC error:", error);
                                                    // Don't keep the caller waiting any longer, but use a 480 instead of the default 486 to clarify the cause
                                                    let body = { request: "decline", code: 480 };
                                                    _this.sipcall.send({ message: body });
                                                }
                                            }
                                        );
                                    }
                                    else {
                                        // CallerID Number 가 0000이 아닌 경우는 수신콜이므로
                                        // 받기버튼의 클릭 이벤트를 등록해야 한다.
                                        Janus.log("Incoming call from " + result["username"] + "!" + transfer + rtpType + extra);

                                        let SoftPhoneAcceptCall = function() {
                                            let sipCallAction = (offerLessInvite ? _this.sipcall.createOffer : _this.sipcall.createAnswer);
                                            sipCallAction(
                                                {
                                                    jsep: jsep,
                                                    media: { audio: doAudio, video: doVideo },
                                                    success: function(jsep) {
                                                        Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                        let body = { request: "accept" };

                                                        _this.sipcall.send({ message: body, jsep: jsep });
                                                        // Ringtone 중지
                                                        _this.stopTones();
                                                    },
                                                    error: function(error) {
                                                        Janus.error("WebRTC error:", error);
                                                        // Don't keep the caller waiting any longer, but use a 480 instead of the default 486 to clarify the cause
                                                        let body = { request: "decline", code: 480 };
                                                        _this.sipcall.send({ message: body });
                                                    }
                                                }
                                            );
                                        }

                                        $ACCEPT_CALL_BTN.one('click', function() {
                                            SoftPhoneAcceptCall();
                                        });

                                        // Ringtone 플레이
                                        _this.playTone("ring");
                                    }
                                }
                                else if (event === 'accepting') {
                                    Janus.debug("Response to an offerless INVITE, let's wait for an 'accepted'");
                                }
                                else if (event === 'progress') {
                                    Janus.log("There's early media from " + result["username"] + ", waiting for the call!", jsep);
                                    // Call can start already: handle the remote answer
                                    if (jsep) {
                                        _this.sipcall.handleRemoteJsep({ jsep: jsep, error: doHangup });
                                    }
                                }
                                else if (event === 'accepted') {
                                    Janus.log(result["username"] + " accepted tha call!", jsep);
                                    // Call can start, now: handle the remote answer
                                    if (jsep) {
                                        _this.sipcall.handleRemoteJsep({ jsep: jsep, error: doHangup });
                                    }
                                    _this.sipcall.callId = callId;
                                }
                                else if (event === 'updatingcall') {
                                    // We got a re-INVITE: while we may prompt the user (e.g.,
                                    // to notify about media changes), to keep things simple
                                    // we just accept the update and send an answer right way
                                    Janus.log("Got re-INVITE");
                                    let doAudio = (jsep.sdp.indexOf("m=audio ") > -1),
                                        doVideo = (jsep.sdp.indexOf("m=video ") > -1);

                                    _this.sipcall.createAnswer(
                                        {
                                            jsep: jsep,
                                            media: { audio: doAudio, video: doVideo },
                                            success: function(jsep) {
                                                Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                let body = { request: "update" };
                                                _this.sipcall.send({ message: body, jsep: jsep });
                                            },
                                            error: function(error) {
                                                Janus.error("WebRTC error:", error);
                                            }
                                        }
                                    );
                                }
                                else if (event === 'message') {
                                    // We got a MESSAGE
                                    let sender = result["displayname"] ? result["displayname"] : result["sender"];
                                    let content = result["content"];
                                    content = content.replace(new RegExp('<', 'g'), '&lt');
                                    content = content.replace(new RegExp('>', 'g'), '&gt');
                                    Janus.log("Message from " + sender);
                                }
                                else if (event === 'info') {
                                    // We got an INFO
                                    let sender = result["displayname"] ? result["displayname"] : result["sender"];
                                    let content = result["content"];
                                    content = content.replace(new RegExp('<', 'g'), '&lt');
                                    content = content.replace(new RegExp('>', 'g'), '&gt');
                                    Janus.log("Info from " + sender);
                                }
                                else if (event === 'notify') {
                                    // We got an NOTIFY
                                    let notify = result["notify"];
                                    let content = result["content"];
                                    Janus.log(content, "Notify (" + notify + ")");
                                }
                                else if (event === 'transfer') {
                                    // We're being asked to transfer the call, ask the user what to do
                                    let referTo = result["refer_to"];
                                    let referredBy = result["referred_by"] ? result["referred_by"] : "an unknown party";
                                    let referId = result["refer_id"];
                                    let replaces = result["replaces"];
                                    let extra = ("referred by " + referredBy);
                                    if (replaces) {
                                        extra += (", replaces call-ID " + referredBy);
                                    }
                                    extra = extra.replace(new RegExp('<', 'g'), '&lt');
                                    extra = extra.replace(new RegExp('>', 'g'), '&gt');

                                    let transfer_confirm = confirm("Transfer the call to " + referTo + "? (" + extra + ")");
                                    if (transfer_confirm) {
                                        // Call the person we're being transferred to
                                        if (!_this.sipcall.webrtcStuff.pc) {
                                            // Do it here
                                            // FIXME : Transfer 기능 테스트하면 수정할 것!!!
                                        }
                                        else {
                                        }
                                    }
                                    else {

                                    }
                                }
                                else if (event === 'hangup') {
                                    Janus.log("Call hung up (" + result["code"] + " " + result["reason"] + ")!");
                                    // Reset status
                                    _this.sipcall.hangup();

                                    if (result["code"] && result["code"] === 200) {
                                        // Busytone 플레이
                                        _this.playTone("busy");
                                    }
                                    else {
                                        _this.stopTones();
                                    }

                                    $ACCEPT_CALL_BTN.off('click');
                                }
                            }
                        },
                        onlocalstream: function(stream) {
                            Janus.debug(" ::: Got a local stream :::", stream);
                            // 내 음성/영상스트림을 VIDEO 객체에 연결
                            Janus.attachMediaStream($LOCAL_STREAM_OBJECT.get(0), stream);
                            $LOCAL_STREAM_OBJECT.get(0).muted = "muted";
                            // FIXME : 영상통화용 코드 작성
                        },
                        onremotestream: function(stream) {
                            Janus.debug(" ::: Got a Remote stream :::", stream);
                            // 상대방의 음성/영상스트림을 VIDEO 객체에 연결
                            Janus.attachMediaStream($REMOTE_STREAM_OBJECT.get(0), stream);
                            // FIXME : 영상통화용 코드 작성
                        },
                        oncleanup: function() {
                            // FIXME : Element 초기화
                            if (_this.sipcall) {
                                _this.sipcall.callId = null;
                            }
                        }
                    }
                );
            },
            error: function(error) {
                callback_disconnected_status();
                console.log(error);
                Janus.error(error);
                Janus.log("error:" + error);

                if (_this.januSessionId) {
                    clearInterval(_this.januSessionId);
                    _this.januSessionId = null;
                }
                _this.januSessionId = setInterval(_this.start_webrtc, RECONNECT_INTERVAL);
            },
            destroyed: function() {
                callback_disconnected_status();

                Janus.log("destroyed");

                if (_this.januSessionId) {
                    clearInterval(_this.januSessionId);
                    _this.januSessionId = null;
                }
                _this.januSessionId = setInterval(_this.start_webrtc, RECONNECT_INTERVAL);
            }
        }
    );
}

SoftPhoneApi.prototype.is_support_webrtc = function (kind) {
    // 소프트폰인지 검사
    if (!kind || kind !== "S") {
        console.log("No Softphone... ");
        return false;
    }

    // https인지 확인
    if (location.protocol !== "https:") {
        alert("You must use HTTPS protocol in order to support WebRTC!!!");
        console.log("location.protocol: " + location.protocol);
        return false;
    }

    // 브라우저가 WebRTC를 지원하는지 검사
    if (!Janus.isWebrtcSupported()) {
        console.log("No WebRTC support... ");
        return false;
    }

    return true;
}

// 전화기 등록 상태가 변경될 때 호출되는 Callback 함수
SoftPhoneApi.prototype.set_callback_registered_status = function (callback) {
    callback_registered_status = callback;
}

SoftPhoneApi.prototype.set_callback_unregistered_status = function (callback) {
    callback_unregistered_status = callback;
}

SoftPhoneApi.prototype.set_callback_disconnected_status = function (callback) {
    callback_disconnected_status = callback;
}

let $ACCEPT_CALL_BTN = null;
SoftPhoneApi.prototype.set_accept_call_btn_object = function ($btn_obj) {
    $ACCEPT_CALL_BTN = $btn_obj;
}

let $LOCAL_STREAM_OBJECT = null;
SoftPhoneApi.prototype.set_local_stream_object = function ($my_obj) {
    $LOCAL_STREAM_OBJECT = $my_obj;
}

let $REMOTE_STREAM_OBJECT = null;
SoftPhoneApi.prototype.set_remote_stream_object = function ($remote_obj) {
    $REMOTE_STREAM_OBJECT = $remote_obj;
}

// RINGTONE 플레이 기능을 ON
SoftPhoneApi.prototype.set_ringtone_on = function () {
    this.WEBRTC_INFO.env.ringtone = true;
}
// RINGTONE 플레이 기능을 OFF
SoftPhoneApi.prototype.set_ringtone_off = function () {
    this.WEBRTC_INFO.env.ringtone = false;
}
// RINGTONE 볼륨 크기 조정
SoftPhoneApi.prototype.set_ringtone_volume = function (vol) {
    if (vol >= 0.0 && vol <= 1.0) {
        RINGTONE.volume = vol;
    }
    else {
        console.error("Invalid volume level!!!")
    }
}
// BUSYTONE 플레이 기능을 ON
SoftPhoneApi.prototype.set_busytone_on = function () {
    this.WEBRTC_INFO.env.busytone = true;
}
// BUSYTONE 플레이 기능을 OFF
SoftPhoneApi.prototype.set_busytone_off = function () {
    this.WEBRTC_INFO.env.busytone = false;
}
// BUSYTONE 볼륨 크기 조정
SoftPhoneApi.prototype.set_busytone_volume = function (vol) {
    if (vol >= 0.0 && vol <= 1.0) {
        BUSYTONE.volume = vol;
    }
    else {
        console.error("Invalid volume level!!!")
    }
}

