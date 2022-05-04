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

var janusVChat = null;
var vchat = null;
var vchatOpaqueId = "vchat-" + Janus.randomString(12);

var vchatBitrateTimer = null;
var vchatSpinner = null;

var audioEnabled = false;
var videoEnabled = false;

var myUsername = null;
var remoteUsername = null;

var doSimulcast = (getQueryStringValue("simulcast") === "yes" || getQueryStringValue("simulcast") === "true");
var doSimulcast2 = (getQueryStringValue("simulcast2") === "yes" || getQueryStringValue("simulcast2") === "true");
var simulcastStarted = false;

var vchatRegistered = false;

var vchatRegisterTimerId = null;
const VCHAT_REGISTER_INTERVAL = 5000;

// 연결이 끊어졌을 때 재연결
var vchatReconnectTimerId = null;
const VCHAT_RECONNECT_INTERVAL = 5000;

var accept_vchat = null;

var callback_vchat_outgoing_call = function(){};
var callback_vchat_incoming_call = function(){};
var callback_vchat_accept = function(){};
var callback_vchat_hangup = function(){

    WEBRTC_INFO.server.webrtc_server_ip
    WEBRTC_INFO.server.my_username
    WEBRTC_INFO.server.remote_username
};

function register_vchat(username) {
    if (username === "") {
        Janus.error("  -- Error register_vchat : username required...");
        return;
    }

    if(/[^a-zA-Z0-9\-]/.test(username)) {
        Janus.error('Input is not alphanumeric');
        //$('#username').removeAttr('disabled').val("");
        //$('#register').removeAttr('disabled').click(registerUsername);
        return;
    }

    var register = { request: "register", username: username };
    vchat.send({ message: register });
}

function create_vchat_session() {
    var webrtc_server = "wss://" + WEBRTC_INFO.server.webrtc_server_ip + ":" + WEBRTC_INFO.server.webrtc_server_port;
    var turn_server = "turn:" + WEBRTC_INFO.server.turn_server_ip + ":" + WEBRTC_INFO.server.turn_server_port;
    var turn_user = WEBRTC_INFO.server.turn_user;
    var turn_secret = WEBRTC_INFO.server.turn_secret;

    // 세션 생성
    janusVChat = new Janus(
        {
            server: webrtc_server,
            iceServers: [ {urls: turn_server, username: turn_user, credential: turn_secret} ],
            success: function() {
                vchatRegistered = false;

                // VideoCall 플러그인 Attach
                janusVChat.attach(
                    {
                        plugin: "janus.plugin.videocall",
                        opaqueId: vchatOpaqueId,
                        success: function(pluginHandle) {
                            vchat = pluginHandle;
                            Janus.log("Plugin attached! (" + vchat.getPlugin() + ", id=" + vchat.getId() + ")");

                            callback_vchat_unregistered_status();

                            // TODO: 버튼 클릭시 register???
                            register_vchat(myUsername);
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

                            // TODO: error 체크 부분이 필요한 것인지 확인 필요
                            var error = msg["error"];
                            if (error) {
                                if (!vchatRegistered) {
                                    ;
                                }
                                else {
                                    // Reset status
                                    vchat.hangup();
                                }
                                Janus.error(error);
                                return;
                            }
                            /////////////////////////////////////////////

                            var result = msg["result"];
                            if (result) {
                                if (result["list"]) {
                                    var list = result["list"];
                                    Janus.debug("Got a list of registered peers:", list);
                                    for (var mp in list) {
                                        Janus.debug("  >> [" + list[mp] + "]");
                                    }
                                }
                                else if (result["event"]) {
                                    var event = result["event"];
                                    $('#print_text').text("event: " + event);
                                    if (event === 'registered') {
                                        myUsername = result["username"];
                                        Janus.log("Successfully registered as " + myUsername + "!");
                                        if (!vchatRegistered) {
                                            vchatRegistered = true;
                                        }

                                        callback_vchat_registered_status();

                                        // Get a list of available peers, just for fun
                                        vchat.send({ message: { request: "list" }});
                                        // 등록 성공했으므로, Call 버튼 활성화
                                        // TODO:
                                        if ($DIAL_AUDIO_VCHAT_BTN) $DIAL_AUDIO_VCHAT_BTN.unbind('click').click(doVoiceChat);
                                        if ($DIAL_VIDEO_VCHAT_BTN) $DIAL_VIDEO_VCHAT_BTN.unbind('click').click(doVideoChat);
                                    }
                                    else if (event === 'calling') { // 발신중...
                                        Janus.log("Waiting for the peer to answer...");

                                        // 전화끊기(Hangup) 버튼 이벤트 등록
                                        if ($HANGUP_VCHAT_BTN) $HANGUP_VCHAT_BTN.unbind('click').click(doHangup);

                                        // Ringtone 플레이
                                        playTone("ring");

                                        callback_vchat_outgoing_call();
                                    }
                                    else if (event === 'incomingcall') {
                                        Janus.log("Incoming call from " + result["username"] + "!");
                                        remoteUsername = result["username"];

                                        // 전화끊기(Hangup) 버튼 이벤트 등록
                                        if ($HANGUP_VCHAT_BTN) $HANGUP_VCHAT_BTN.unbind('click').click(doHangup);
                                        //$('#print_text').text("Incoming call from " + result["username"] + "!");

                                        // TODO: 전화수신을 알림
                                        // 전화받기 함수 등록
                                        accept_vchat = function() {
                                            accept_vchat = null;
                                            vchat.createAnswer(
                                                {
                                                    jsep: jsep,
                                                    // No media provided: by default, it's sendrecv for audio and video
                                                    media: { data: true },  // Let's negotiate data channels as well
                                                    // If you want to test simulcasting (Chrome and Firefox only), then
                                                    // pass a ?simulcast=true when opening this demo page: it will turn
                                                    // the following 'simulcast' property to pass to janus.js to true
                                                    simulcast: doSimulcast,
                                                    success: function(jsep) {
                                                        Janus.debug("Got SDP!", jsep);
                                                        var body = { request: "accept" };
                                                        vchat.send({ message: body, jsep: jsep });
                                                    },
                                                    error: function(error) {
                                                        Janus.error("WebRTC error:", error);
                                                    }
                                                }
                                            );
                                        };

                                        if ($ACCEPT_VCHAT_BTN) {
                                            $ACCEPT_VCHAT_BTN.one('click', function () {
                                                console.log("accept clicked............");
                                                accept_vchat();
                                            });
                                        }

                                        // Ringtone 플레이
                                        playTone("ring");

                                        callback_vchat_incoming_call();
                                    }
                                    else if (event === 'accepted') {
                                        var peer = result["username"];
                                        if (!peer) {
                                            Janus.log("Call started!");
                                        }
                                        else {
                                            Janus.log(peer + " accepted the call!");
                                            remoteUsername = peer;
                                        }
                                        // Video call can start
                                        if (jsep) {
                                            vchat.handleRemoteJsep({ jsep: jsep });
                                        }

                                        start_recording(jsep);
                                        callback_vchat_accept();

                                        // Ringtone 중지
                                        stopTones();

                                        // TODO: 전화끊기(Hangup) 버튼 이벤트 등록
                                        // doHangup() 호출
                                    }
                                    else if (event === 'update') {
                                        // An 'update' event may be used to provide renegotiation attempts
                                        if (jsep) {
                                            if (jsep.type === "answer") {
                                                vchat.handleRemoteJsep({ jsep: jsep });
                                            }
                                            else {
                                                vchat.createAnswer(
                                                    {
                                                        jsep: jsep,
                                                        media: { data: true },  // Let's negotiate data channels as well
                                                        success: function(jsep) {
                                                            Janus.debug("Got SDP!", jsep);
                                                            var body = { request: "set" };
                                                            vchat.send({ message: body, jsep: jsep });
                                                        },
                                                        error: function(error) {
                                                            Janus.error("WebRTC error:", error);
                                                        }
                                                    }
                                                );
                                            }
                                        }
                                    }
                                    else if (event === 'hangup') {
                                        Janus.log("Call hung up by " + result["username"] + " (" + result["reason"] + ")!");
                                        // Reset status
                                        vchat.hangup();
                                        if (vchatSpinner) {
                                            vchatSpinner.stop();
                                        }
                                        if ($LOCAL_VCHAT_STREAM_OBJECT) $LOCAL_VCHAT_STREAM_OBJECT.hide();
                                        if ($REMOTE_VCHAT_STREAM_OBJECT) $REMOTE_VCHAT_STREAM_OBJECT.hide();

                                        // Busytone 플레이
                                        playTone("busy");

                                        // TODO: 통화가 종료될 때 해야 할 일들 ... 버큰 재등록???

                                        callback_vchat_hangup();
                                    }
                                    else if (event === 'simulcast') {
                                        // Is simulcast in place?
                                        var substream = result["substream"];
                                        var temporal = result["temporal"];
                                        if ((substream !== null && substream !== undefined) || (temporal !== null && temporal !== undefined)) {
                                            if (!simulcastStarted) {
                                                simulcastStarted = true;
                                                // TODO: simulcast를 지원할지 고려해보자...
                                                //addSimulcastButtons(result["videocodec"] === "vp8" || result["videocodec"] === "h264");
                                            }
                                            // We just received notice that there's been a switch, update the buttons
                                            // TODO: simulcast를 지원할지 고려해보자...
                                            //updateSimulcastButtons(substream, temporal);
                                        }
                                    }
                                }
                            }   // End of "if (result)"
                            else {
                                // FIXME Error?
                                var error = msg["error"];
                                Janus.error("  -- Error attaching plugin...", error);

                                if (error.indexOf("already taken") > 0) {
                                    // FIXME Use status codes...
                                    // TODO: Register 버튼을 활성화... 이에 상응하는 동작 필요
                                }
                                // TODO Reset status
                                vchat.hangup()
                                if (vchatSpinner) {
                                    vchatSpinner.stop();
                                }
                                // TODO: 아래 동작에 상응하는 동작 추가 필요
                                /*
                                $('#waitingvideo').remove();
                                $('#videos').hide();
                                $('#peer').removeAttr('disabled').val('');
                                $('#call').removeAttr('disabled').html('Call')
                                    .removeClass("btn-danger").addClass("btn-success")
                                    .unbind('click').click(doCall);
                                $('#toggleaudio').attr('disabled', true);
                                $('#togglevideo').attr('disabled', true);
                                $('#bitrate').attr('disabled', true);
                                $('#curbitrate').hide();
                                $('#curres').hide();
                                */
                                if (vchatBitrateTimer) {
                                    clearInterval(vchatBitrateTimer);
                                }
                                vchatBitrateTimer = null;
                            }
                        },  // End of "onmessage"
                        onlocalstream: function(stream) {
                            Janus.debug(" ::: Got a local stream :::", stream);
                            // 내 음성/영상스트림을 VIDEO 객체에 연결
                            Janus.attachMediaStream($LOCAL_VCHAT_STREAM_OBJECT.get(0), stream);
                            $LOCAL_VCHAT_STREAM_OBJECT.get(0).muted = "muted";
                            // FIXME : 영상통화용 코드 작성
                        },
                        onremotestream: function(stream) {
                            Janus.debug(" ::: Got a Remote stream :::", stream);

                            // 상대방의 스트림에 영상이 없으면, local/remote video 객체를 숨김
                            if (stream.getVideoTracks().length == 0) {
                                Janus.debug(" ::: No Remote Video stream :::");
                                if ($LOCAL_VCHAT_STREAM_OBJECT) $LOCAL_VCHAT_STREAM_OBJECT.hide();
                                if ($REMOTE_VCHAT_STREAM_OBJECT) $REMOTE_VCHAT_STREAM_OBJECT.hide();
                            }
                            else {
                                Janus.debug(" ::: Found Remote Video stream :::");
                                if ($LOCAL_VCHAT_STREAM_OBJECT) $LOCAL_VCHAT_STREAM_OBJECT.show();
                                if ($REMOTE_VCHAT_STREAM_OBJECT) $REMOTE_VCHAT_STREAM_OBJECT.show();
                            }

                            // 상대방의 음성/영상스트림을 VIDEO 객체에 연결
                            Janus.attachMediaStream($REMOTE_VCHAT_STREAM_OBJECT.get(0), stream);
                            // FIXME : 영상통화용 코드 작성
                        },
                        oncleanup: function() {
                            // FIXME : Element 초기화
                            if (vchat) {
                                vchat.callId = null;
                            }
                        }
                    }
                );
            },
            error: function(error) {
                callback_vchat_disconnected_status();

                vchatRegistered = false;
                //callback_disconnected_status();

                //Janus.error(error);
                Janus.log("error:" + error);
                //window.location.reload();

                if (vchatReconnectTimerId) {
                    clearInterval(vchatReconnectTimerId);
                    vchatReconnectTimerId = null;
                }
                vchatReconnectTimerId = setInterval(start_vchat, VCHAT_RECONNECT_INTERVAL);
            },
            destroyed: function() {
                callback_vchat_disconnected_status();

                vchatRegistered = false;
                //callback_disconnected_status();

                Janus.log("destroyed");
                //window.location.reload();

                if (vchatReconnectTimerId) {
                    clearInterval(vchatReconnectTimerId);
                    vchatReconnectTimerId = null;
                }
                vchatReconnectTimerId = setInterval(start_vchat, VCHAT_RECONNECT_INTERVAL);
            }
        }
    );
}

var chkInit = true;
function start_vchat() {
    if (chkInit) {
        if (vchatReconnectTimerId) {
            clearInterval(vchatReconnectTimerId);
            vchatReconnectTimerId = null;
        }
        if (janusVChat) {
            delete janusVChat;
            janusVChat = null;
        }

        Janus.init({debug: "all", callback: create_vchat_session});
        chkInit = false;
    }
}

function set_myusername_vchat(name) {
    myUsername = name;
}

function set_remoteusername_vchat(name) {
    remoteUsername = name;
}

function doVoiceChat() {
    doCall(remoteUsername, true, false, false);
}

function doVideoChat() {
    doCall(remoteUsername, true, true, false);
}

function doCall(userName, doAudio, doVideo, doData) {
    // Call someone
    if(userName === "") {
        Janus.error("Insert a userName to call (e.g., pluto)");
        return;
    }
    if(/[^a-zA-Z0-9\-]/.test(userName)) {
        Janus.error('Input is not alphanumeric');
        //$('#peer').removeAttr('disabled').val("");
        //$('#call').removeAttr('disabled').click(doCall);
        return;
    }
    // Call this user
    vchat.createOffer(
        {
            // By default, it's sendrecv for audio and video...
            media: { data: doData, audio: doAudio, video: doVideo },
            // ... let's negotiate data channels as well
            // If you want to test simulcasting (Chrome and Firefox only), then
            // pass a ?simulcast=true when opening this demo page: it will turn
            // the following 'simulcast' property to pass to janus.js to true
            simulcast: doSimulcast,
            success: function(jsep) {
                Janus.debug("Got SDP!", jsep);
                var body = { request: "call", username: userName };
                vchat.send({ message: body, jsep: jsep });
            },
            error: function(error) {
                Janus.error("createOffer error...", error);
            },
            customizeSdp: function(jsep) {
                jsep.sdp = customize_sdp(jsep.sdp);
            }
        });
}

function doHangup() {
    // Hangup a call
    //$('#call').attr('disabled', true).unbind('click');
    var hangup = { request: "hangup" };
    vchat.send({ message: hangup });
    vchat.hangup();
    yourusername = null;
}

var start_recording = null;
function set_record_file(record_file)
{
    start_recording = function(jsep) {
        if (record_file != "") {
            var msg = {
                request: "set",
                audio: true,
                video: true,
                record: true,
                filename: record_file
            };
            //vchat.send({ message: msg, jsep: jsep });
            vchat.send({message: msg});
            Janus.debug("start recording: ", record_file);
        } else {
            Janus.error("  -- You must assign record_file...");
        }
    };
}

var muteFlag = true;
function doMute()
{
    if (muteFlag) {
        var msg = {
            request: "set",
            audio: false,
        };
        vchat.send({message: msg});
        Janus.debug("Do Mute");
        muteFlag = false
    } else {
        var msg = {
            request: "set",
            audio: true,
        };
        vchat.send({message: msg});
        Janus.debug("Do Unmute");
        muteFlag = true
    }
}

function doUnmute()
{
    var msg = {
        request: "set",
        audio: true,
    };
    vchat.send({message: msg});
    Janus.debug("Do Unmute");
}

function customize_sdp(old_sdp) {
    var new_sdp = "";
    var exclude_codecs = [];
    const line = old_sdp.split(/\r\n/);

    console.log(" ======================== SDP =========================\n" + old_sdp);

    for (var i = 0; i < line.length; i++) {
        if (new_sdp != "") { new_sdp += "\r\n"; }

        if (line[i].indexOf("m=audio") == 0) {
            var audio_prefix = line[i].match(/^m=audio \d+ \S+ /g);

            if (audio_prefix.length > 0) {
                var codecs_str = line[i].substr(audio_prefix[0].length, line[i].length-audio_prefix[0].length);
                var codecs = codecs_str.match(/(\d+)/g);
                //console.log(" ======================== CODECS =========================\n" + codecs);
                for (var j = 0; j < codecs.length; j++) {
                    if (codecs[j] != 0 && codecs[j] != 8) {
                        exclude_codecs.push(codecs[j]);
                        //console.log(" ======================== ADD EXCLUDE CODECS =========================\n" + codecs[j]);
                    }
                }

                //new_sdp += audio_prefix + "0 8";

                new_sdp += audio_prefix + "0 8"
                for (var j = 0; j < exclude_codecs.length; j++) {
                    new_sdp += " " + exclude_codecs[j];
                }
            }
        }
        /*
        else if (line[i].indexOf("a=rtpmap") == 0 || line[i].indexOf("a=rtcp") == 0 || line[i].indexOf("a=fmtp") == 0) {
            var match = false;

            if (exclude_codecs.length > 0) {
                //console.log(" ======================== exclude_codecs.length =========================\n" + exclude_codecs.length);
                for (var j = 0; j < exclude_codecs.length; j++) {
                    var regexp = new RegExp("a=rtpmap:" + exclude_codecs[j], "g");
                    if (regexp.test(line[i])) {
                        match = true;
                        break;
                    }
                    regexp = new RegExp("a=rtcp\S+:" + exclude_codecs[j], "g");
                    if (regexp.test(line[i])) {
                        match = true;
                        break;
                    }
                    regexp = new RegExp("a=fmtp:" + exclude_codecs[j], "g");
                    if (regexp.test(line[i])) {
                        match = true;
                        break;
                    }
                }
            }

            if (match == false) {
                new_sdp += line[i] + "\r\n";
            }
        }
        */
        else {
            new_sdp += line[i];
        }
        //console.log(" ************ NEW SDP =========================\n" + new_sdp);
    }

    console.log(" ======================== NEW SDP =========================\n" + new_sdp);

    return new_sdp;
}

// Helper to parse query string
function getQueryStringValue(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

// 보이스챗 등록 상태가 변경될 때 호출되는 Callback 함수
var callback_vchat_registered_status = function(){};
var callback_vchat_unregistered_status = function(){};
var callback_vchat_disconnected_status = function(){};

function set_callback_vchat_registered_status(callback) {
    callback_vchat_registered_status = callback;
}

function set_callback_vchat_unregistered_status(callback) {
    callback_vchat_unregistered_status = callback;
}

function set_callback_vchat_disconnected_status(callback) {
    callback_vchat_disconnected_status = callback;
}


// 보이스챗 아웃바운드콜 발신시(연결전), 호출되는 콜백함수 등록
function set_callback_vchat_outgoing_call(callback) {
    callback_vchat_outgoing_call = callback;
}

// 보이스챗 인바운드콜 수신시(연결전), 호출되는 콜백함수 등록
function set_callback_vchat_incoming_call(callback) {
    callback_vchat_incoming_call = callback;
}

// 보이스챗 통화연결시, 호출되는 콜백함수 등록
function set_callback_vchat_accept(callback) {
    callback_vchat_accept = callback;
}

// 보이스챗 통화종료시, 호출되는 콜백함수 등록
function set_callback_vchat_hangup(callback) {
    callback_vchat_hangup = callback;
}


var $DIAL_AUDIO_VCHAT_BTN = null;
function set_dial_audio_vchat_btn_object($btn_obj) {
    $DIAL_AUDIO_VCHAT_BTN = $btn_obj;
}
var $SVIEW_DIAL_AUDIO_VCHAT_BTN = null;
function set_sview_dial_audio_vchat_btn_object($btn_obj) {
    $SVIEW_DIAL_AUDIO_VCHAT_BTN = $btn_obj;
}

var $DIAL_VIDEO_VCHAT_BTN = null;
function set_dial_video_vchat_btn_object($btn_obj) {
    $DIAL_VIDEO_VCHAT_BTN = $btn_obj;
}
var $SVIEW_DIAL_VIDEO_VCHAT_BTN = null;
function set_sview_dial_video_vchat_btn_object($btn_obj) {
    $SVIEW_DIAL_VIDEO_VCHAT_BTN = $btn_obj;
}

var $ACCEPT_VCHAT_BTN = null;
function set_accept_vchat_btn_object($btn_obj) {
    $ACCEPT_VCHAT_BTN = $btn_obj;
}
var $SVIEW_ACCEPT_VCHAT_BTN = null;
function set_sview_accept_vchat_btn_object($btn_obj) {
    $SVIEW_ACCEPT_VCHAT_BTN = $btn_obj;
}

var $HANGUP_VCHAT_BTN = null;
function set_hangup_vchat_btn_object($btn_obj) {
    $HANGUP_VCHAT_BTN = $btn_obj;
}
var $SVIEW_HANGUP_VCHAT_BTN = null;
function set_sview_hangup_vchat_btn_object($btn_obj) {
    $SVIEW_HANGUP_VCHAT_BTN = $btn_obj;
}

var $MUTE_VCHAT_BTN = null;
function set_mute_vchat_btn_object($btn_obj) {
    $MUTE_VCHAT_BTN = $btn_obj;
    if ($MUTE_VCHAT_BTN) {
        $MUTE_VCHAT_BTN.unbind('click').click(doMute);
    }
}
var $SVIEW_MUTE_VCHAT_BTN = null;
function set_sview_mute_vchat_btn_object($btn_obj) {
    $SVIEW_MUTE_VCHAT_BTN = $btn_obj;
    if ($SVIEW_MUTE_VCHAT_BTN) {
        $SVIEW_MUTE_VCHAT_BTN.unbind('click').click(doMute);
    }
}

var $UNMUTE_VCHAT_BTN = null;
function set_unmute_vchat_btn_object($btn_obj) {
    $UNMUTE_VCHAT_BTN = $btn_obj;
    if ($UNMUTE_VCHAT_BTN) {
        $UNMUTE_VCHAT_BTN.unbind('click').click(doUnmute);
    }
}
var $SVIEW_UNMUTE_VCHAT_BTN = null;
function set_sview_unmute_vchat_btn_object($btn_obj) {
    $SVIEW_UNMUTE_VCHAT_BTN = $btn_obj;
    if ($SVIEW_UNMUTE_VCHAT_BTN) {
        $SVIEW_UNMUTE_VCHAT_BTN.unbind('click').click(doUnmute);
    }
}

/*
var $TOGGLE_SPEAKER_VCHAT_BTN = null;
function set_toggle_speaker_vchat_btn_object($btn_obj) {
    $TOGGLE_SPEAKER_VCHAT_BTN = $btn_obj;
    if ($TOGGLE_SPEAKER_VCHAT_BTN) {
        $TOGGLE_SPEAKER_VCHAT_BTN.unbind('click').click(doToggleSpeaker);
    }
}
 */

var $LOCAL_VCHAT_STREAM_OBJECT = null;
function set_local_vchat_stream_object($my_obj) {
    $LOCAL_VCHAT_STREAM_OBJECT = $my_obj;
}

var $REMOTE_VCHAT_STREAM_OBJECT = null;
function set_remote_vchat_stream_object($remote_obj) {
    $REMOTE_VCHAT_STREAM_OBJECT = $remote_obj;
}
