<template>
  <body class="font-sans-kr text-gray-800">
    <div class="h-screen min-w-full">
      <div class="w-full" style="height: 80%">
        <div class="w-full" style="height: 70%">
          <video id="remotevideo" autoplay playsinline/>
        </div>
        <div class="w-full h-full">
          <video id="myvideo" autoplay playsinline muted="muted"/>
        </div>
      </div>
      <div class="input-box">
        <button :disabled="!DIAL_AUDIO_VCHAT_BTN" class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" id="btn-dial-audio" @click.stop="doVoiceChat">음성전화걸기</button>
        <button :disabled="!DIAL_VIDEO_VCHAT_BTN" class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" id="btn-dial-video" @click.stop="doVideoChat">영상전화걸기</button>
        <button :disabled="!ACCEPT_VCHAT_BTN" class="bg-gray-400 hover:bg-gray`-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" id="btn-accept" @click.stop="accept_vchat">전화받기</button>
        <button :disabled="!HANGUP_VCHAT_BTN" class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" id="btn-hangup" @click.stop="doHangup">전화끊기</button>
        <button class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" id="btn-mute" @click.stop="doMute">MUTE</button>
        <button class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" id="btn-unmute" @click.stop="doUnmute">UNMUTE</button>
        <select id="select_audio_input"></select>
        <select id="select_audio_output"></select>
        <!--<button id="btn-toggle-speaker">Toggle Speaker</button>-->
        <span id="print_text"></span>
      </div>
    </div>
  </body>
</template>

<script>
import modalOpener from "../utillities/mixins/modalOpener"
import store from '../store/index'
import Janus from "../utillities/janus"
import $ from 'jquery'
import SimpleTone from "@/assets/sounds/SimpleTone.mp3"
import BusySignal from "@/assets/sounds/BusySignal.mp3"

function getQueryStringValue(name) {
  name = name.replace(/[\\[]/, "\\[").replace(/[\]]/, "\\]")
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search)
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "))
}

export default {
  mixins: [modalOpener],
  setup() {
    return {
      VCHAT_RECONNECT_INTERVAL: 5000,
    }
  },
  data() {
    return {

      WEBRTC_INFO: {
        server: {
          "webrtc_server_ip": window.form.webrtc_server_ip,
          "webrtc_server_port": window.form.webrtc_server_port,
          "turn_server_ip": window.form.turn_server_ip,
          "turn_server_port": window.form.turn_server_port,
          "turn_user": window.form.turn_user,
          "turn_secret": window.form.turn_secret
        },
        env: { "ringtone": true, "busytone": true },
      },
      RINGTONE: new Audio(SimpleTone),
      BUSYTONE: new Audio(BusySignal),
      opened: true,
      janusVChat: null,
      vchat: null,
      vchatOpaqueId: "vchat-"+Janus.randomString(12),
      vchatBitrateTimer: null,
      vchatSpinner: null,
      myUsername: window.form.remote_username,
      remoteUsername: window.form.my_username,
      doSimulcast: (getQueryStringValue("simulcast") === "yes" || getQueryStringValue("simulcast") === "true"),
      simulcastStarted: false,
      vchatRegistered: false,
      vchatRegisterTimerId: null,
      vchatReconnectTimerId: null,
      accept_vchat: null,
      callback_vchat_outgoing_call: () => {},
      callback_vchat_incoming_call: () => {},
      callback_vchat_accept: () => {},
      callback_vchat_hangup: () => {},
      callback_vchat_registered_status: () => {},
      callback_vchat_unregistered_status: () => {},
      callback_vchat_disconnected_status: () => {},

      DIAL_AUDIO_VCHAT_BTN: false,
      DIAL_VIDEO_VCHAT_BTN: false,
      HANGUP_VCHAT_BTN: false,
      ACCEPT_VCHAT_BTN: false,
      $ACCEPT_VCHAT_BTN: null,
      LOCAL_VCHAT_STREAM_OBJECT: $('#myvideo'),
      REMOTE_VCHAT_STREAM_OBJECT: $('#remotevideo'),
      start_recording: null,
    }
  },
  created() {
  },
  mounted() {
    console.log(window.form);
    if (!window.RTCPeerConnection) store.commit('alert/show', {body: '이 브라우저는 WEBRTC 를 지원하지 않습니다.', isClose: true})
    this.create_vchat_session()
  },
  methods: {
    start_vchat() {
      const _this = this
      if (_this.vchatReconnectTimerId) {
        clearInterval(_this.vchatReconnectTimerId);
        _this.vchatReconnectTimerId = null;
      }
      if (_this.janusVChat) {
        delete _this.janusVChat;
        _this.janusVChat = null;
      }

      Janus.init({debug: "all", callback: this.create_vchat_session});
    },
    create_vchat_session() {
      const webrtc_server = "wss://" + this.WEBRTC_INFO.server.webrtc_server_ip + ":" + this.WEBRTC_INFO.server.webrtc_server_port;
      const turn_server = "turn:" + this.WEBRTC_INFO.server.turn_server_ip + ":" + this.WEBRTC_INFO.server.turn_server_port;
      const turn_user = this.WEBRTC_INFO.server.turn_user;
      const turn_secret = this.WEBRTC_INFO.server.turn_secret;
      const _this = this

      // 세션 생성
      this.janusVChat = new Janus(
          {
            server: webrtc_server,
            iceServers: [ {urls: turn_server, username: turn_user, credential: turn_secret} ],
            success: function() {
              _this.vchatRegistered = false;

              // VideoCall 플러그인 Attach
              _this.janusVChat.attach(
                  {
                    plugin: "janus.plugin.videocall",
                    opaqueId: _this.vchatOpaqueId,
                    success: function(pluginHandle) {
                      _this.vchat = pluginHandle;
                      Janus.log("Plugin attached! (" + _this.vchat.getPlugin() + ", id=" + _this.vchat.getId() + ")");

                      _this.callback_vchat_unregistered_status();

                      // TODO: 버튼 클릭시 register???
                      _this.register_vchat(_this.myUsername);
                    },
                    error: function(error) {
                      Janus.error("  -- Error attaching plugin...", error);
                    },
                    consentDialog: function(on) {
                      // getUserMedia 호출 전에 trigger된다.
                      Janus.log("Consent dialog should be " + (on ? "on" : "off") + " now");
                      /*if (on) {
                      }
                      else {
                      }*/
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
                        if (_this.vchatRegistered) {
                          // Reset status
                          _this.vchat.hangup();
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
                            _this.myUsername = result["username"];
                            Janus.log("Successfully registered as " + _this.myUsername + "!");
                            if (!_this.vchatRegistered) {
                              _this.vchatRegistered = true;
                            }

                            _this.callback_vchat_registered_status();

                            // Get a list of available peers, just for fun
                            _this.vchat.send({ message: { request: "list" }});
                            // 등록 성공했으므로, Call 버튼 활성화
                            // TODO:
                            _this.DIAL_AUDIO_VCHAT_BTN = true
                            _this.DIAL_VIDEO_VCHAT_BTN = true
                          }
                          else if (event === 'calling') { // 발신중...
                            Janus.log("Waiting for the peer to answer...");

                            // 전화끊기(Hangup) 버튼 이벤트 등록
                            _this.HANGUP_VCHAT_BTN = true

                            // Ringtone 플레이
                            _this.playTone("ring");

                            _this.callback_vchat_outgoing_call();
                          }
                          else if (event === 'incomingcall') {
                            Janus.log("Incoming call from " + result["username"] + "!");
                            _this.remoteUsername = result["username"];

                            // 전화끊기(Hangup) 버튼 이벤트 등록
                            _this.HANGUP_VCHAT_BTN = true

                            // TODO: 전화수신을 알림
                            // 전화받기 함수 등록
                            _this.accept_vchat = function() {
                              _this.accept_vchat = null;
                              _this.vchat.createAnswer(
                                  {
                                    jsep: jsep,
                                    // No media provided: by default, it's sendrecv for audio and video
                                    media: { data: true },  // Let's negotiate data channels as well
                                    // If you want to test simulcasting (Chrome and Firefox only), then
                                    // pass a ?simulcast=true when opening this demo page: it will turn
                                    // the following 'simulcast' property to pass to janus.js to true
                                    simulcast: _this.doSimulcast,
                                    success: function(jsep) {
                                      Janus.debug("Got SDP!", jsep);
                                      var body = { request: "accept" };
                                      _this.vchat.send({ message: body, jsep: jsep });
                                    },
                                    error: function(error) {
                                      Janus.error("WebRTC error:", error);
                                    }
                                  }
                              );
                            };
                            _this.ACCEPT_VCHAT_BTN = true
                            _this.$ACCEPT_VCHAT_BTN = $('#btn-accept')
                            if (_this.$ACCEPT_VCHAT_BTN) {
                              _this.$ACCEPT_VCHAT_BTN.one('click', function () {
                                _this.accept_vchat();
                              });
                            }

                            // Ringtone 플레이
                            _this.playTone("ring");

                            _this.callback_vchat_incoming_call();
                          }
                          else if (event === 'accepted') {
                            var peer = result["username"];
                            if (!peer) {
                              Janus.log("Call started!");
                            }
                            else {
                              Janus.log(peer + " accepted the call!");
                              _this.remoteUsername = peer;
                            }
                            // Video call can start
                            if (jsep) {
                              _this.vchat.handleRemoteJsep({ jsep: jsep });
                            }

                            _this.start_recording(jsep);
                            _this.callback_vchat_accept();

                            // Ringtone 중지
                            _this.stopTones();

                            // TODO: 전화끊기(Hangup) 버튼 이벤트 등록
                            // doHangup() 호출
                          }
                          else if (event === 'update') {
                            // An 'update' event may be used to provide renegotiation attempts
                            if (jsep) {
                              if (jsep.type === "answer") {
                                _this.vchat.handleRemoteJsep({ jsep: jsep });
                              }
                              else {
                                _this.vchat.createAnswer(
                                    {
                                      jsep: jsep,
                                      media: { data: true },  // Let's negotiate data channels as well
                                      success: function(jsep) {
                                        Janus.debug("Got SDP!", jsep);
                                        var body = { request: "set" };
                                        _this.vchat.send({ message: body, jsep: jsep });
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
                            _this.vchat.hangup();
                            if (_this.vchatSpinner) {
                              _this.vchatSpinner.stop();
                            }
                            if (_this.$LOCAL_VCHAT_STREAM_OBJECT) _this.$LOCAL_VCHAT_STREAM_OBJECT.hide();
                            if (_this.$REMOTE_VCHAT_STREAM_OBJECT) _this.$REMOTE_VCHAT_STREAM_OBJECT.hide();

                            // Busytone 플레이
                            _this.playTone("busy");

                            // TODO: 통화가 종료될 때 해야 할 일들 ... 버큰 재등록???

                            _this.callback_vchat_hangup();
                          }
                          else if (event === 'simulcast') {
                            // Is simulcast in place?
                            var substream = result["substream"];
                            var temporal = result["temporal"];
                            if ((substream !== null && substream !== undefined) || (temporal !== null && temporal !== undefined)) {
                              if (!_this.simulcastStarted) {
                                _this.simulcastStarted = true;
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
                        let error = msg["error"];
                        Janus.error("  -- Error attaching plugin...", error);

                        if (error.indexOf("already taken") > 0) {
                          // FIXME Use status codes...
                          // TODO: Register 버튼을 활성화... 이에 상응하는 동작 필요
                        }
                        // TODO Reset status
                        _this.vchat.hangup()
                        if (_this.vchatSpinner) {
                          _this.vchatSpinner.stop();
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
                        if (_this.vchatBitrateTimer) {
                          clearInterval(_this.vchatBitrateTimer);
                        }
                        _this.vchatBitrateTimer = null;
                      }
                    },  // End of "onmessage"
                    onlocalstream: function(stream) {
                      Janus.debug(" ::: Got a local stream :::", stream);
                      // 내 음성/영상스트림을 VIDEO 객체에 연결
                      Janus.attachMediaStream($('#myvideo').get(0), stream);
                      $('#myvideo').get(0).muted = "muted";
                      // FIXME : 영상통화용 코드 작성
                    },
                    onremotestream: function(stream) {
                      Janus.debug(" ::: Got a Remote stream :::", stream);

                      // 상대방의 스트림에 영상이 없으면, local/remote video 객체를 숨김
                      if (stream.getVideoTracks().length == 0) {
                        Janus.debug(" ::: No Remote Video stream :::");
                        if (_this.$LOCAL_VCHAT_STREAM_OBJECT) _this.$LOCAL_VCHAT_STREAM_OBJECT.hide();
                        if (_this.$REMOTE_VCHAT_STREAM_OBJECT) _this.$REMOTE_VCHAT_STREAM_OBJECT.hide();
                      }
                      else {
                        Janus.debug(" ::: Found Remote Video stream :::");
                        if (_this.$LOCAL_VCHAT_STREAM_OBJECT) _this.$LOCAL_VCHAT_STREAM_OBJECT.show();
                        if (_this.$REMOTE_VCHAT_STREAM_OBJECT) _this.$REMOTE_VCHAT_STREAM_OBJECT.show();
                      }

                      // 상대방의 음성/영상스트림을 VIDEO 객체에 연결
                      Janus.attachMediaStream($('#remotevideo').get(0), stream);
                      // FIXME : 영상통화용 코드 작성
                    },
                    oncleanup: function() {
                      // FIXME : Element 초기화
                      if (_this.vchat) {
                        _this.vchat.callId = null;
                      }
                    }
                  }
              );
            },
            error: function(error) {
              _this.callback_vchat_disconnected_status();

              _this.vchatRegistered = false;
              //callback_disconnected_status();

              //Janus.error(error);
              console.log("error:" + error);

              if (_this.vchatReconnectTimerId) {
                clearInterval(_this.vchatReconnectTimerId);
                _this.vchatReconnectTimerId = null;
              }
              _this.vchatReconnectTimerId = setInterval(_this.start_vchat, _this.VCHAT_RECONNECT_INTERVAL);
            },
            destroyed: function() {
              _this.callback_vchat_disconnected_status();

              _this.vchatRegistered = false;
              //callback_disconnected_status();

              Janus.log("destroyed");
              //window.location.reload();

              if (_this.vchatReconnectTimerId) {
                clearInterval(_this.vchatReconnectTimerId);
                _this.vchatReconnectTimerId = null;
              }
              _this.vchatReconnectTimerId = setInterval(_this.start_vchat, _this.VCHAT_RECONNECT_INTERVAL);
            }
          }
      );
    },
    doMute() {
      var msg = {
        request: "set",
        audio: false,
      };
      this.vchat.send({message: msg});
      Janus.debug("Do Mute");
    },
    doUnmute() {
      var msg = {
        request: "set",
        audio: true,
      };
      this.vchat.send({message: msg});
      Janus.debug("Do Unmute");
    },
    register_vchat(username) {
      if (username === "") {
        Janus.error("  -- Error register_vchat : username required...");
        return;
      }

      if(/[^a-zA-Z0-9\\-]/.test(username)) {
        Janus.error('Input is not alphanumeric');
        //$('#username').removeAttr('disabled').val("");
        //$('#register').removeAttr('disabled').click(registerUsername);
        return;
      }

      var register = { request: "register", username: username };
      this.vchat.send({ message: register });
    },
    doCall(userName, doAudio, doVideo, doData) {
      const _this = this
      // Call someone
      if(userName === "") {
        Janus.error("Insert a userName to call (e.g., pluto)");
        return;
      }
      if(/[^a-zA-Z0-9\\-]/.test(userName)) {
        Janus.error('Input is not alphanumeric');
        //$('#peer').removeAttr('disabled').val("");
        //$('#call').removeAttr('disabled').click(doCall);
        return;
      }
      // Call this user
      this.vchat.createOffer(
          {
            // By default, it's sendrecv for audio and video...
            media: { data: doData, audio: doAudio, video: doVideo },
            // ... let's negotiate data channels as well
            // If you want to test simulcasting (Chrome and Firefox only), then
            // pass a ?simulcast=true when opening this demo page: it will turn
            // the following 'simulcast' property to pass to janus.js to true
            simulcast: _this.doSimulcast,
            success: function(jsep) {
              Janus.debug("Got SDP!", jsep);
              var body = { request: "call", username: userName };
              _this.vchat.send({ message: body, jsep: jsep });
            },
            error: function(error) {
              Janus.error("createOffer error...", error);
            },
            customizeSdp: function(jsep) {
              jsep.sdp = _this.customize_sdp(jsep.sdp);
            }
          });
    },
    customize_sdp(old_sdp) {
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
            for (var j = 0; j < codecs.length; j++) {
              if (codecs[j] != 0 && codecs[j] != 8) {
                exclude_codecs.push(codecs[j]);
              }
            }

            new_sdp += audio_prefix + "0 8"
            for (let j = 0; j < exclude_codecs.length; j++) {
              new_sdp += " " + exclude_codecs[j];
            }
          }
        }
        else {
          new_sdp += line[i];
        }
      }

      console.log(" ======================== NEW SDP =========================\n" + new_sdp);

      return new_sdp;
    },
    doVoiceChat() {
      this.doCall(this.remoteUsername, true, false, false);
    },
    doVideoChat() {
      this.doCall(this.remoteUsername, true, true, false);
    },
    doHangup() {
      var hangup = { request: "hangup" };
      this.vchat.send({ message: hangup });
      this.vchat.hangup();
    },
    set_record_file(record_file) {
      const _this = this
      this.start_recording = function() {
        if (record_file !== "") {
          var msg = {
            request: "set",
            audio: true,
            video: true,
            record: true,
            filename: record_file
          };
          _this.vchat.send({message: msg});
          Janus.debug("start recording: ", record_file);
        } else {
          Janus.error("  -- You must assign record_file...");
        }
      };
    },
    stopTones() {
      console.log("-------- RINGTONE.pause()");
      this.RINGTONE.pause();
      this.RINGTONE.currentTime = 0;

      // Busytone 중지
      console.log("-------- BUSYTONE.pause()");
      this.BUSYTONE.pause();
      this.BUSYTONE.currentTime = 0;
    },
    playTone(tone) {
      const _this = this
      if (!_this.WEBRTC_INFO.env.ringtone) { return; }

      this.stopTones();

      if (tone === "ring") {
        if (!_this.WEBRTC_INFO.env.ringtone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
          console.log("-------- RINGTONE.play()");
          _this.RINGTONE.currentTime = 0;
          _this.RINGTONE.play();

          // stop microphone stream acquired by getUserMedia
          stream.getTracks().forEach(function (track) { track.stop(); });
        });
      }
      else if (tone === "busy") {
        if (!_this.WEBRTC_INFO.env.busytone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
          console.log("-------- BUSYTONE.play()");
          _this.BUSYTONE.currentTime = 0;
          _this.BUSYTONE.play();

          // stop microphone stream acquired by getUserMedia
          stream.getTracks().forEach(function (track) { track.stop(); });
        });
      }
    },

  },
}
</script>