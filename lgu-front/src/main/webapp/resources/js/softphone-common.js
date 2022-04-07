//
// JavaScript Common Functions for EICN Softphone
//

var WEBRTC_INFO = {};
WEBRTC_INFO.server = {};
WEBRTC_INFO.phone = {};
WEBRTC_INFO.env = { "ringtone": true, "busytone": true };

function playTone(tone) {
    if (!WEBRTC_INFO.env.ringtone) { return; }

    stopTones();

    if (tone === "ring") {
        if (!WEBRTC_INFO.env.ringtone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
            console.log("-------- RINGTONE.play()");
            RINGTONE.currentTime = 0;
            RINGTONE.play();
        
            // stop microphone stream acquired by getUserMedia
            stream.getTracks().forEach(function (track) { track.stop(); });
        });
    }
    else if (tone === "busy") {
        if (!WEBRTC_INFO.env.busytone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
            console.log("-------- BUSYTONE.play()");   
            BUSYTONE.currentTime = 0;
            BUSYTONE.play();
        
            // stop microphone stream acquired by getUserMedia
            stream.getTracks().forEach(function (track) { track.stop(); });
        });
    }
}

function stopTones() {
    // Ringtone 중지
    console.log("-------- RINGTONE.pause()");
    RINGTONE.pause();
    RINGTONE.currentTime = 0;

    // Busytone 중지
    console.log("-------- BUSYTONE.pause()");
    BUSYTONE.pause();
    BUSYTONE.currentTime = 0;
}



// RINGTONE 플레이 기능을 ON
function set_ringtone_on() {
    WEBRTC_INFO.env.ringtone = true;
}
// RINGTONE 플레이 기능을 OFF
function set_ringtone_off() {
    WEBRTC_INFO.env.ringtone = false;
}
// RINGTONE 볼륨 크기 조정
function set_ringtone_volume(vol) {
    if (vol >= 0.0 && vol <= 1.0) {
        RINGTONE.volume = vol;
    }
    else {
        console.error("Invalid volume level!!!")
    }
}
// BUSYTONE 플레이 기능을 ON
function set_busytone_on() {
    WEBRTC_INFO.env.busytone = true;
}
// BUSYTONE 플레이 기능을 OFF
function set_busytone_off() {
    WEBRTC_INFO.env.busytone = false;
}
// BUSYTONE 볼륨 크기 조정
function set_busytone_volume(vol) {
    if (vol >= 0.0 && vol <= 1.0) {
        BUSYTONE.volume = vol;
    }
    else {
        console.error("Invalid volume level!!!")
    }
}

function is_support_softphone(phoneKind) {
    // 소프트폰인지 검사
    if (!phoneKind || phoneKind != "S") {
        console.log("No Softphone... ");
        return false;
    }

    // https인지 확인
    if (location.protocol !== "https:") {
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

function is_support_vchat(chatKind) {
    // 보이스챗인지 검사
    // TODO:
    if (!chatKind || chatKind != "Y") {
        console.log("No Chat... ");
        return false;
    }

    // https인지 확인
    if (location.protocol !== "https:") {
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

function set_webrtc_server_info(server_info) {
    if (server_info) {
        WEBRTC_INFO.server = server_info;
        return true;
    }else {
        return false;
    }

}