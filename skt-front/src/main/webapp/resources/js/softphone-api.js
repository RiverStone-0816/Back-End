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
        "peer_num": "43779188",
        "peer_secret": "3c82a78b878b7f2fa4126611530c626a"
    }

 --- get_webrtc_server_info.jsp 의 응답값 형식
    {
        "result": "OK",
        "message": "",
        "pbx_server_ip": "122.49.74.121",
        "pbx_server_port": "5060",
        "webrtc_server_ip": "122.49.74.231",
        "webrtc_server_port": "8200",
        "turn_server_ip": "122.49.74.231",
        "turn_server_port": "3478",
        "turn_user": "turn",
        "turn_secret": "turnrw"
    }
 */

const SIP_PROTO_PREFIX = "sip:";
const SIP_DOMAIN = "ippbx.eicn.co.kr";
const SIP_REALM = "eicn";

var janusSip = null;
var sipcall = null;
var sipOpaqueId = "softphone-" + Janus.randomString(12);

var sipRegistered = false;
var masterId = null, helpers = {}, helpersCount = 0;

var sipRegisterTimerId = null;
const SIP_REGISTER_INTERVAL = 5000;

// 연결이 끊어졌을 때 재연결
janusSip = null;
var sipReconnectTimerId = null;
const SIP_RECONNECT_INTERVAL = 5000;


function register_sip_softphone() {
    if (WEBRTC_INFO.phone.peer_num && WEBRTC_INFO.phone.peer_num != "" && WEBRTC_INFO.phone.peer_secret && WEBRTC_INFO.phone.peer_secret != "") {
        // Registration
        Janus.log("sip-server: " + WEBRTC_INFO.server.pbx_server_ip + ":" + WEBRTC_INFO.server.pbx_server_port);
        var pbx_server = SIP_PROTO_PREFIX + WEBRTC_INFO.server.pbx_server_ip + ":" + WEBRTC_INFO.server.pbx_server_port;
        var username = SIP_PROTO_PREFIX + WEBRTC_INFO.phone.peer_num + "@" + SIP_DOMAIN;
        
        var register = {
            request: "register",
            username: username,
            //ha1_secret: md5(WEBRTC_INFO.phone.peer_num + ":" + SIP_REALM + ":" + WEBRTC_INFO.phone.peer_secret),
            ha1_secret: WEBRTC_INFO.phone.peer_secret,
            sips: false,
            force_udp: true,
            proxy: pbx_server
        };
        //Janus.debug("h1_secret:", register.ha1_secret);

        sipcall.send({ message: register });
    }
    else {
        Janus.log("WEBRTC_INFO.phone.peer_num: " + WEBRTC_INFO.phone.peer_num + ", WEBRTC_INFO.phone.peer_secret:" + WEBRTC_INFO.phone.peer_secret);
    }
}


function create_sipcall_session() {
    var webrtc_server = "wss://" + WEBRTC_INFO.server.webrtc_server_ip + ":" + WEBRTC_INFO.server.webrtc_server_port;
    var turn_server = "turn:" + WEBRTC_INFO.server.turn_server_ip + ":" + WEBRTC_INFO.server.turn_server_port;
    var turn_user = WEBRTC_INFO.server.turn_user;
    var turn_secret = WEBRTC_INFO.server.turn_secret;

    // 세션 생성
    janusSip = new Janus(
        {
            server: webrtc_server,
            iceServers: [ {urls: turn_server, username: turn_user, credential: turn_secret} ],
            success: function() {
                // SIP 플러그인 Attach
                janusSip.attach(
                    {
                        plugin: "janus.plugin.sip",
                        opaqueId: sipOpaqueId,
                        success: function(pluginHandle) {
                            sipcall = pluginHandle;
                            Janus.log("Plugin attached! (" + sipcall.getPlugin() + ", id=" + sipcall.getId() + ")");

                            var result = "";
                            var message = "";
                            var peer = "";
                            $.ajax({
                                type: "POST",
                                cache: false,
                                async: false,
                                headers: { "cache-control": "no-cache","pragma": "no-cache" },
                                url: "/ipcc/multichannel/main/get_softphone_info.jsp",
                                //data: $('#passchg_frm').serialize(),
                                dataType: "json",
                                error: function(request, status, error) {
                                    alert("code:"+request.status+"\nmessages:"+request.responseText);
                                },
                                success: function(data, status, error) {
                                    WEBRTC_INFO.phone = data;

                                    if (WEBRTC_INFO.phone.result == "OK" && WEBRTC_INFO.phone.peer_num != "" && WEBRTC_INFO.phone.peer_secret != "") {
                                        // 소프트폰을 등록한다.
                                        register_sip_softphone();
                                        // 등록실패시 SIP_REGISTER_INTERVAL 시간마다 등록을 재시도한다.
                                        sipRegisterTimerId = setInterval(register_sip_softphone, SIP_REGISTER_INTERVAL);
                                    }
                                    else {
                                        alert("result:"+result+"\nmessage:"+message);
                                    }
                                }
                            });
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

                            var error = msg["error"];
                            if (error) {
                                if (!sipRegistered) {
                                    ;
                                }
                                else {
                                    // Reset status
                                    sipcall.hangup();
                                }
                                Janus.error(error);
                                return;
                            }

                            var callId = msg["call_id"];
                            var result = msg["result"];

                            if (result && result["event"]) {
                                var event = result["event"];
                                if (event === 'registration_failed') {
                                    Janus.warn("Registration failed: " + result["code"] + " " + result["reason"]);
                                    callback_sipcall_unregistered_status();

                                    if (sipRegisterTimerId) {
                                        clearInterval(sipRegisterTimerId);
                                        sipRegisterTimerId = null;
                                    }
                                    sipRegisterTimerId = setInterval(register_sip_softphone, SIP_REGISTER_INTERVAL);
                                    return;
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
                                else if (event === 'incomingcall') {    // 전화 수신중 (호인입)
                                    //Janus.log("Incoming call from " + result["username"] + "!");
                                    
                                    sipcall.callId = callId;

                                    var doAudio = true, doVideo = true;
                                    var offerlessInvite = false;

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
                                    var transfer = "";
                                    var referredBy = result["referred_by"];
                                    if (referredBy) {
                                        transfer = " (referred by " + referredBy + ")";
                                        transfer = transfer.replace(new RegExp('<', 'g'), '&lt');
                                        transfer = transfer.replace(new RegExp('>', 'g'), '&gt');
                                    }

                                    // Any security offered? A missing "srtp" attribute means plain RTP
                                    var rtpType = "";
                                    var srtp = result["srtp"];
                                    if (srtp === "sdes_optional") {
                                        rtpType = " (SDES-SRTP offered)";
                                    }
                                    else if (srtp === "sdes_mandatory") {
                                        rtpType = " (SDES-SRTP mandatory)";
                                    }

                                    // Notify user
                                    var extra = "";
                                    if (offerlessInvite) {
                                        extra = " (no SDP offer provided)";
                                    }


                                    // CallerID Number가 0000으로 표시되는 경우, 발신콜로 인식한다.
                                    // 이 때, 자동받기 처리해야 정상적으로 콜이 발신된다.
                                    if (result["username"] && result["username"].indexOf("sip:0000@") > -1) {
                                        Janus.log("Outbound call to " + result["displayname"] + "!" + transfer + rtpType + extra);

                                        // Notice that we can only answer if we got an offer: 
                                        // if this was an offerless call, we'll need to create an offer ourselves
                                        var sipcallAction = (offerlessInvite ? sipcall.createOffer : sipcall.createAnswer);
                                        sipcallAction(
                                            {
                                                jsep: jsep,
                                                media: { audio: doAudio, video: doVideo },
                                                success: function(jsep) {
                                                    Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                    var body = { request: "accept" };
                                                    sipcall.send({ message: body, jsep: jsep });
                                                },
                                                error: function() {
                                                    Janus.error("WebRTC error:", error);
                                                    // Don't keep the caller waiting any longer, but use a 480 instead of the default 486 to clarify the cause
                                                    var body = { request: "decline", code: 480 };
                                                    sipcall.send({ message: body });
                                                }
                                            }
                                        );
                                    }
                                    else {
                                        // CallerID Number가 0000이 아닌 경우는 수신콜이므로
                                        // 받기버튼의 클릭 이벤트를 등록해야 한다.

                                        Janus.log("Incoming call from " + result["username"] + "!" + transfer + rtpType + extra);
                                        
                                        var SoftphoneAcceptCall = function() {
                                            var sipcallAction = (offerlessInvite ? sipcall.createOffer : sipcall.createAnswer);
                                            sipcallAction(
                                                {
                                                    jsep: jsep,
                                                    media: { audio: doAudio, video: doVideo },
                                                    success: function(jsep) {
                                                        Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                        var body = { request: "accept" };

                                                        // Note: as with "call", you can add a "srtp" attribute to
                                                        // negotiate/mandate SDES support for this incoming call.
                                                        // The default behaviour is to automatically use it if
                                                        // the caller negoticated it, but you may choose to require
                                                        // SDES support by setting "srtp" to "sdes_mandatory", e.g.:
                                                        //      var body = { request: "accept", srtp: "sdes_mandatory" };
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
                                                    },
                                                    error: function(error) {
                                                        Janus.error("WebRTC error:", error);
                                                        // Don't keep the caller waiting any longer, but use a 480 instead of the default 486 to clarify the cause
                                                        var body = { request: "decline", code: 480 };
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
                                    var doAudio = (jsep.sdp.indexOf("m=audio ") > -1),
                                        doVideo = (jsep.sdp.indexOf("m=video ") > -1);
                                    
                                    sipcall.createAnswer(
                                        {
                                            jsep: jsep,
                                            media: { audio: doAudio, video: doVideo },
                                            success: function(jsep) {
                                                Janus.debug("Got SDP " + jsep.type + "! audio=" + doAudio + ", video=" + doVideo + ":", jsep);
                                                var body = { request: "update" };
                                                sipcall.send({ message: body, jsep: jsep });
                                            },
                                            error: function(error) {
                                                Janus.error("WebRTC error:", error);
                                            }
                                        }
                                    );
                                }
                                else if (event === 'message') {
                                    // We got a MESSAGE
                                    var sender = result["displayname"] ? result["displayname"] : result["sender"];
                                    var content = result["content"];
                                    content = content.replace(new RegExp('<', 'g'), '&lt');
                                    content = content.replace(new RegExp('>', 'g'), '&gt');
                                    Janus.log("Message from " + sender);
                                }
                                else if (event === 'info') {
                                    // We got an INFO
                                    var sender = result["displayname"] ? result["displayname"] : result["sender"];
                                    var content = result["content"];
                                    content = content.replace(new RegExp('<', 'g'), '&lt');
                                    content = content.replace(new RegExp('>', 'g'), '&gt');
                                    Janus.log("Info from " + sender);
                                }
                                else if (event === 'notify') {
                                    // We got an NOTIFY
                                    var notify = result["notify"];
                                    var content = result["content"];
                                    Janus.log(content, "Notify (" + notify + ")");
                                }
                                else if (event === 'transfer') {
                                    // We're being asked to transfer the call, ask the user what to do
                                    var referTo = result["refer_to"];
                                    var referredBy = result["referred_by"] ? result["referred_by"] : "an unknown party";
                                    var referId = result["refer_id"];
                                    var replaces = result["replaces"];
                                    var extra = ("referred by " + referredBy);
                                    if (replaces) {
                                        extra += (", replaces call-ID " + referredBy);
                                    }
                                    extra = extra.replace(new RegExp('<', 'g'), '&lt');
                                    extra = extra.replace(new RegExp('>', 'g'), '&gt');

                                    var transfer_confirm = confirm("Transfer the call to " + referTo + "? (" + extra + ")");
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

                                    if (result["code"] && result["code"] === 200) {
                                        // Busytone 플레이
                                        playTone("busy");
                                    }
                                    else {
                                        stopTones();
                                    }

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
                callback_sipcall_disconnected_status();

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
var callback_sipcall_registered_status = function(){};
var callback_sipcall_unregistered_status = function(){};
var callback_sipcall_disconnected_status = function(){};

function set_callback_sipcall_registered_status(callback) {
    callback_sipcall_registered_status = callback;
}

function set_callback_sipcall_unregistered_status(callback) {
    callback_sipcall_unregistered_status = callback;
}

function set_callback_sipcall_disconnected_status(callback) {
    callback_sipcall_disconnected_status = callback;
}

var $ACCEPT_SIPCALL_BTN = null;
function set_accept_sipcall_btn_object($btn_obj) {
    $ACCEPT_SIPCALL_BTN = $btn_obj;
}

var $LOCAL_SIPCALL_STREAM_OBJECT = null;
function set_local_sipcall_stream_object($my_obj) {
    $LOCAL_SIPCALL_STREAM_OBJECT = $my_obj;
}

var $REMOTE_SIPCALL_STREAM_OBJECT = null;
function set_remote_sipcall_stream_object($remote_obj) {
    $REMOTE_SIPCALL_STREAM_OBJECT = $remote_obj;
}
