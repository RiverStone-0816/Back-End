//
// JavaScript API for EICN Softphone
//

/*
 *  참고 사항 
 *  
 --- get_softphone_info.jsp 의 응답값 형식
    {
        "result": "OK",
        "message": "",
        "peerNum": "43779188",
        "peerSecret": "3c82a78b878b7f2fa4126611530c626a"
    }

 --- get_webrtc_server_info.jsp 의 응답값 형식
    {
        "result": "OK",
        "message": "",
        "pbxServerIp": "122.49.74.121",
        "pbxServerPort": "5060",
        "webrtcServerIp": "122.49.74.231",
        "webrtcServerPort": "8200",
        "turnServerIp": "122.49.74.231",
        "turnServerPort": "3478",
        "turnUser": "turn",
        "turnSecret": "turnrw"
    }
 */

const SIP_PROTO_PREFIX = "sip:";
const SIP_DOMAIN = "ippbx.eicn.co.kr";
const SIP_REALM = "eicn";

let janusSip = null;
let sipcall = null;
let sipOpaqueId = "softphone-" + Janus.randomString(12);

let sipRegistered = false;
let masterId = null, helpers = {}, helpersCount = 0;

let sipRegisterTimerId = null;
const SIP_REGISTER_INTERVAL = 5000;

// 연결이 끊어졌을 때 재연결
janusSip = null;
let sipReconnectTimerId = null;
const SIP_RECONNECT_INTERVAL = 5000;


function register_sip_softphone() {
    if (WEBRTC_INFO.phone.peerNum && WEBRTC_INFO.phone.peerNum !== "" && WEBRTC_INFO.phone.peerSecret && WEBRTC_INFO.phone.peerSecret !== "") {
        // Registration
        Janus.log("sip-server: " + WEBRTC_INFO.server.pbxServerIp + ":" + WEBRTC_INFO.server.pbxServerPort);
        let pbx_server = SIP_PROTO_PREFIX + WEBRTC_INFO.server.pbxServerIp + ":" + WEBRTC_INFO.server.pbxServerPort;
        let username = SIP_PROTO_PREFIX + WEBRTC_INFO.phone.peerNum + "@" + SIP_DOMAIN;

        let register = {
            request: "register",
            username: username,
            //ha1_secret: md5(WEBRTC_INFO.phone.peerNum + ":" + SIP_REALM + ":" + WEBRTC_INFO.phone.peerSecret),
            ha1_secret: WEBRTC_INFO.phone.peerSecret,
            sips: false,
            force_udp: true,
            proxy: pbx_server
        };
        //Janus.debug("h1_secret:", register.ha1_secret);

        sipcall.send({ message: register });
    }
    else {
        Janus.log("WEBRTC_INFO.phone.peerNum: " + WEBRTC_INFO.phone.peerNum + ", WEBRTC_INFO.phone.peerSecret:" + WEBRTC_INFO.phone.peerSecret);
    }
}


function create_sipcall_session() {
    let webrtc_server = "wss://" + WEBRTC_INFO.server.webrtcServerIp + ":" + WEBRTC_INFO.server.webrtcServerPort;
    let turn_server = "turn:" + WEBRTC_INFO.server.turnServerIp + ":" + WEBRTC_INFO.server.turnServerPort;
    let turnUser = WEBRTC_INFO.server.turnUser;
    let turnSecret = WEBRTC_INFO.server.turnSecret;

    // 세션 생성
    janusSip = new Janus(
        {
            server: webrtc_server,
            iceServers: [ {urls: turn_server, username: turnUser, credential: turnSecret} ],
            success: function() {
                // SIP 플러그인 Attach
                janusSip.attach(
                    {
                        plugin: "janus.plugin.sip",
                        opaqueId: sipOpaqueId,
                        success: function(pluginHandle) {
                            sipcall = pluginHandle;
                            Janus.log("Plugin attached! (" + sipcall.getPlugin() + ", id=" + sipcall.getId() + ")");

                            let result = "";
                            let message = "";
                            let peer = "";
                            restSelf.get("/api/auth/softPhone-info").done(function(response){
                                console.log(response.data);
                                WEBRTC_INFO.phone = response.data
                                if (WEBRTC_INFO.phone.result === "OK" && WEBRTC_INFO.phone.peerNum !== "" && WEBRTC_INFO.phone.peerSecret !== "") {
                                    // 소프트폰을 등록한다.
                                    register_sip_softphone();
                                    // 등록실패시 SIP_REGISTER_INTERVAL 시간마다 등록을 재시도한다.
                                    sipRegisterTimerId = setInterval(register_sip_softphone, SIP_REGISTER_INTERVAL);
                                }
                                else {
                                    alert("result:"+result+"\nmessage:"+message);
                                }
                            })
                        },
                        error: function(error) {
                            callback_sipcall_disconnected_status(error);
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
                                if (!sipRegistered) {
                                    return;
                                }
                                else {
                                    // Reset status
                                    sipcall.hangup();
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
                                    callback_sipcall_unregistered_status();

                                    if (sipRegisterTimerId) {
                                        clearInterval(sipRegisterTimerId);
                                        sipRegisterTimerId = null;
                                    }
                                    sipRegisterTimerId = setInterval(register_sip_softphone, SIP_REGISTER_INTERVAL);
                                }
                                else if (event === 'registered') {   // 등록 성공
                                    Janus.log("Successfully registered as " + result["username"] + "!");
                                    callback_sipcall_registered_status();

                                    if (!sipRegistered) {
                                        sipRegistered = true;
                                        masterId = result["master_id"];
                                        // FIXME : addhelper
                                    }
                                    // 등록 성공했으므로, 재등록 스케쥴링을 제거한다.
                                    if (sipRegisterTimerId) {
                                        clearInterval(sipRegisterTimerId);
                                        sipRegisterTimerId = null;
                                    }
                                }
                                else if (event === 'calling') { // 전화 발신중
                                    Janus.log("Waiting for the peer to answer...");
                                    // TODO: Any ringtone?
                                    // FIXME : 전화끊기 버튼 활성화
                                }
                                else if (event === 'incomingcall') {
                                    // 전화 수신중 (호인입)
                                    //Janus.log("Incoming call from " + result["username"] + "!");

                                    sipcall.callId = callId;

                                    let doAudio = true, doVideo = true;
                                    let offerlessInvite = false;

                                    if (jsep) {
                                        // What has been negotiated?
                                        doAudio = (jsep.sdp.indexOf("m=audio ") > -1);
                                        doVideo = (jsep.sdp.indexOf("m=video ") > -1);
                                        Janus.debug("Audio " + (doAudio ? "has" : "has NOT") + " been negotiated");
                                        Janus.debug("Video " + (doVideo ? "has" : "has NOT") + " been negotiated");
                                    }
                                    else {
                                        Janus.log("This call doesn't contain an offer... we'll need to provide on ourselves");
                                        offerlessInvite = true;
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
                                    if (offerlessInvite) {
                                        extra = " (no SDP offer provided)";
                                    }


                                    // CallerID Number가 0000으로 표시되는 경우, 발신콜로 인식한다.
                                    // 이 때, 자동받기 처리해야 정상적으로 콜이 발신된다.
                                    if (result["username"] && result["username"].indexOf("sip:0000@") > -1) {
                                        Janus.log("Outbound call to " + result["displayname"] + "!" + transfer + rtpType + extra);

                                        // Notice that we can only answer if we got an offer:
                                        // if this was an offerless call, we'll need to create an offer ourselves
                                        let sipcallAction = (offerlessInvite ? sipcall.createOffer : sipcall.createAnswer);
                                        sipcallAction(
                                            {
                                                jsep: jsep,
                                                media: { audio: doAudio, video: doVideo },
                                                success: function(jsep) {
                                                    Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                    let body = { request: "accept" };
                                                    sipcall.send({ message: body, jsep: jsep });
                                                    callback_sipcall_registered_status();
                                                },
                                                error: function(error) {
                                                    callback_sipcall_disconnected_status(error);
                                                    Janus.error("WebRTC error:", error);
                                                    // Don't keep the caller waiting any longer, but use a 480 instead of the default 486 to clarify the cause
                                                    let body = { request: "decline", code: 480 };
                                                    sipcall.send({ message: body });
                                                }
                                            }
                                        );
                                    }
                                    else {
                                        // CallerID Number가 0000이 아닌 경우는 수신콜이므로
                                        // 받기버튼의 클릭 이벤트를 등록해야 한다.

                                        Janus.log("Incoming call from " + result["username"] + "!" + transfer + rtpType + extra);

                                        let SoftphoneAcceptCall = function() {
                                            let sipcallAction = (offerlessInvite ? sipcall.createOffer : sipcall.createAnswer);
                                            sipcallAction(
                                                {
                                                    jsep: jsep,
                                                    media: { audio: doAudio, video: doVideo },
                                                    success: function(jsep) {
                                                        Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                        let body = { request: "accept" };

                                                        // Note: as with "call", you can add a "srtp" attribute to
                                                        // negotiate/mandate SDES support for this incoming call.
                                                        // The default behaviour is to automatically use it if
                                                        // the caller negoticated it, but you may choose to require
                                                        // SDES support by setting "srtp" to "sdes_mandatory", e.g.:
                                                        //      let body = { request: "accept", srtp: "sdes_mandatory" };
                                                        // This may you'll tell the plugin to accept the call, but ONLY
                                                        // if SDES is available, and you don't want plain RTP. If it
                                                        // is not available, you'll get an error (452) back. You can
                                                        // also specify the SRTP profile to negotiate by setting the
                                                        // "srtp_profile" property accordingly (the default if not
                                                        // set in the request is "AES_CM_128_HMAC_SHA1_80")
                                                        // Note 2: by default, the SIP plugin auto-answers incoming
                                                        // re-INVITEs, without involving the browser/client: this is
                                                        // for backwards compatibility with older Janus clients that
                                                        // may not be able to handle them. If you want to receive
                                                        // re-INVITES to handle them yourself, specify it here, e.g.:
                                                        //      body["autoaccept_reinvites"] = false;
                                                        sipcall.send({ message: body, jsep: jsep });
                                                        // Ringtone 중지
                                                        stopTones();
                                                        callback_sipcall_registered_status();
                                                    },
                                                    error: function(error) {
                                                        callback_sipcall_disconnected_status(error);
                                                        Janus.error("WebRTC error:", error);
                                                        // Don't keep the caller waiting any longer, but use a 480 instead of the default 486 to clarify the cause
                                                        let body = { request: "decline", code: 480 };
                                                        sipcall.send({ message: body });
                                                    }
                                                }
                                            );
                                        }

                                        if ($ACCEPT_SIPCALL_BTN) {
                                            $ACCEPT_SIPCALL_BTN.one('click', function () {
                                                SoftphoneAcceptCall();
                                            });
                                        }

                                        // Ringtone 플레이
                                        playTone("ring");
                                    }
                                }
                                else if (event === 'accepting') {
                                    Janus.debug("Response to an offerless INVITE, let's wait for an 'accepted'");
                                }
                                else if (event === 'progress') {
                                    Janus.log("There's early media from " + result["username"] + ", waiting for the call!", jsep);
                                    // Call can start already: handle the remote answer
                                    if (jsep) {
                                        sipcall.handleRemoteJsep({ jsep: jsep, error: doHangup });
                                    }
                                }
                                else if (event === 'accepted') {
                                    Janus.log(result["username"] + " accepted tha call!", jsep);
                                    // Call can start, now: handle the remote answer
                                    if (jsep) {
                                        sipcall.handleRemoteJsep({ jsep: jsep, error: doHangup });
                                    }
                                    sipcall.callId = callId;
                                }
                                else if (event === 'updatingcall') {
                                    // We got a re-INVITE: while we may prompt the user (e.g.,
                                    // to notify about media changes), to keep things simple
                                    // we just accept the update and send an answer right way
                                    Janus.log("Got re-INVITE");
                                    let doAudio = (jsep.sdp.indexOf("m=audio ") > -1),
                                        doVideo = (jsep.sdp.indexOf("m=video ") > -1);

                                    sipcall.createAnswer(
                                        {
                                            jsep: jsep,
                                            media: { audio: doAudio, video: doVideo },
                                            success: function(jsep) {
                                                Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                let body = { request: "update" };
                                                sipcall.send({ message: body, jsep: jsep });
                                                callback_sipcall_registered_status();
                                            },
                                            error: function(error) {
                                                callback_sipcall_disconnected_status(error);
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
                                        if (!sipcall.webrtcStuff.pc) {
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
                                    sipcall.hangup();

                                    // if (result["code"] && result["code"] === 200) {
                                    //     // Busytone 플레이
                                    //     // playTone("busy");
                                    // }
                                    // else {
                                    //     stopTones();
                                    // }
                                    stopTones();

                                    $ACCEPT_SIPCALL_BTN.off('click');
                                }
                            }
                        },
                        onlocalstream: function(stream) {
                            Janus.debug(" ::: Got a local stream :::", stream);
                            // 내 음성/영상스트림을 VIDEO 객체에 연결
                            Janus.attachMediaStream($LOCAL_SIPCALL_STREAM_OBJECT.get(0), stream);
                            $LOCAL_SIPCALL_STREAM_OBJECT.get(0).muted = "muted";
                            // FIXME : 영상통화용 코드 작성
                        },
                        onremotestream: function(stream) {
                            Janus.debug(" ::: Got a Remote stream :::", stream);
                            // 상대방의 음성/영상스트림을 VIDEO 객체에 연결
                            Janus.attachMediaStream($REMOTE_SIPCALL_STREAM_OBJECT.get(0), stream);
                            // FIXME : 영상통화용 코드 작성
                        },
                        oncleanup: function() {
                            // FIXME : Element 초기화
                            if (sipcall) {
                                sipcall.callId = null;
                            }
                        }
                    }
                );
            },
            error: function(error) {
                callback_sipcall_disconnected_status(error);

                //Janus.error(error);
                Janus.log("error:" + error);
                //window.location.reload();

                if (sipReconnectTimerId) {
                    clearInterval(sipReconnectTimerId);
                    sipReconnectTimerId = null;
                }
                sipReconnectTimerId = setInterval(start_sipcall, SIP_RECONNECT_INTERVAL);
            },
            destroyed: function() {
                callback_sipcall_disconnected_status();

                Janus.log("destroyed");
                //window.location.reload();

                if (sipReconnectTimerId) {
                    clearInterval(sipReconnectTimerId);
                    sipReconnectTimerId = null;
                }
                sipReconnectTimerId = setInterval(start_sipcall, SIP_RECONNECT_INTERVAL);
            }
        }
    );
}

function start_sipcall() {
    if (sipReconnectTimerId) {
        clearInterval(sipReconnectTimerId);
        sipReconnectTimerId = null;
    }
    if (janusSip) {
        delete janusSip;
        janusSip = null;
    }

    Janus.init({debug: "all", callback: create_sipcall_session});
}


// 전화기 등록 상태가 변경될 때 호출되는 Callback 함수
let callback_sipcall_registered_status = function(){};
let callback_sipcall_unregistered_status = function(){};
let callback_sipcall_disconnected_status = function(){};

function set_callback_sipcall_registered_status(callback) {
    callback_sipcall_registered_status = callback;
}

function set_callback_sipcall_unregistered_status(callback) {
    callback_sipcall_unregistered_status = callback;
}

function set_callback_sipcall_disconnected_status(callback) {
    callback_sipcall_disconnected_status = callback;
}

let $ACCEPT_SIPCALL_BTN = null;
function set_accept_sipcall_btn_object($btn_obj) {
    $ACCEPT_SIPCALL_BTN = $btn_obj;
}

let $LOCAL_SIPCALL_STREAM_OBJECT = null;
function set_local_sipcall_stream_object($my_obj) {
    $LOCAL_SIPCALL_STREAM_OBJECT = $my_obj;
}

let $REMOTE_SIPCALL_STREAM_OBJECT = null;
function set_remote_sipcall_stream_object($remote_obj) {
    $REMOTE_SIPCALL_STREAM_OBJECT = $remote_obj;
}
