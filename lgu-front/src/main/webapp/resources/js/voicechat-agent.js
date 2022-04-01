//
// JavaScript for EICN Softphone
//

$(document).ready(function() {
    // 보이스챗 사용 가능 여부 확인
    if (is_support_vchat() === false) {
        return;
    }

    // webrtc 서버접속에 필요한 정보를 전역객체 WEBRTC_INFO에 저장한다.
    var server_info = {
        "pbx_server_ip": "122.49.74.101",
        "pbx_server_port": "5060",
        "webrtc_server_ip": "webrtc.eicn.co.kr",
        "webrtc_server_port": "8200",
        "turn_server_ip": "webrtc.eicn.co.kr",
        "turn_server_port": "3478",
        "turn_user": "turn",
        "turn_secret": "turnrw"
    };
    if (set_webrtc_server_info(server_info) === false) {
        return;
    }

    /*
    // 보이스챗이 Registered 상태가 되었을 때, 호출되는 콜백함수 등록
    set_callback_vchat_registered_status(function() {
        $('div.userbox div.prof_pic').css('background-color', 'lime');
    });
    // 보이스챗이 Unregistered 상태가 되었을 때, 호출되는 콜백함수 등록
    set_callback_vchat_unregistered_status(function() {
        $('div.userbox div.prof_pic').css('background-color', 'red');
    });
    // 보이스챗이 Disconnected 상태가 되었을 때, 호출되는 콜백함수 등록
    set_callback_vchat_disconnected_status(function() {
        $('div.userbox div.prof_pic').css('background-color', 'grey');
    });
    */

    // 보이스챗 아웃바운드콜 발신시(연결전), 호출되는 콜백함수 등록
    set_callback_vchat_outgoing_call(function(){});

    // 보이스챗 인바운드콜 수신시(연결전), 호출되는 콜백함수 등록
    set_callback_vchat_incoming_call(function(){});

    // 보이스챗 통화연결시, 호출되는 콜백함수 등록
    set_callback_vchat_accept(function(){});

    // 보이스챗 통화종료시, 호출되는 콜백함수 등록
    set_callback_vchat_hangup(function(){});

    // 음성 전화걸기 버튼 객체를 등록
    $btn_dial_audio_vchat = $("#btn-dial-audio");
    set_dial_audio_vchat_btn_object($btn_dial_audio_vchat);
    // 사이드뷰 음성 전화걸기 버튼 객체를 등록
    ////$btn_sview_dial_audio_vchat = $(".left_btn ul li[menu_id=MENU_SVIEW_RECEIVE]");
    ////set_sview_dial_audio_vchat_btn_object($btn_sview_dial_audio_vchat);

    // 영상 전화걸기 버튼 객체를 등록
    $btn_dial_video_vchat = $("#btn-dial-video");
    set_dial_video_vchat_btn_object($btn_dial_video_vchat);
    // 사이드뷰 영상 전화걸기 버튼 객체를 등록
    ////$btn_sview_dial_video_vchat = $(".left_btn ul li[menu_id=MENU_SVIEW_HOLD]");
    ////set_sview_dial_video_vchat_btn_object($btn_sview_dial_video_vchat);

    // 전화걸기 전에 녹취파일명을 반드시 지정해야 한다. (파일명: 경로+확장자없는파일명)
    set_record_file("/data/vchat_record/eicn/tttttttttttttt");

    // 전화받기 버튼 객체를 등록
    $btn_accept_vchat = $("#btn-accept");
    set_accept_vchat_btn_object($btn_accept_vchat);
    // 사이드뷰 전화받기 버튼 객체를 등록
    $btn_sview_accept_vchat = $("#btn-sview-accept");
    set_sview_accept_vchat_btn_object($btn_sview_accept_vchat);

    // 전화끊기 버튼 객체를 등록
    $btn_hangup_vchat = $("#btn-hangup");
    set_hangup_vchat_btn_object($btn_hangup_vchat);
    // 사이드뷰 전화끊기 버튼 객체를 등록
    $btn_sview_hangup_vchat = $("#btn-sview-hangup");
    set_sview_hangup_vchat_btn_object($btn_sview_hangup_vchat);

    // MUTE 버튼 객체를 등록
    $btn_mute_vchat = $("#btn-mute");
    set_mute_vchat_btn_object($btn_mute_vchat);
    // 사이드뷰 MUTE 버튼 객체를 등록
    $btn_sview_mute_vchat = $("#btn-sview-mute");
    set_sview_mute_vchat_btn_object($btn_sview_mute_vchat);

    // UNMUTE 버튼 객체를 등록
    $btn_unmute_vchat = $("#btn-unmute");
    set_unmute_vchat_btn_object($btn_unmute_vchat);
    // 사이드뷰 UNMUTE 버튼 객체를 등록
    $btn_sview_unmute_vchat = $("#btn-sview-unmute");
    set_sview_unmute_vchat_btn_object($btn_sview_unmute_vchat);

    //set_ringtone_on();
    //set_busytone_on();

    // volume range: 0.0 ~ 1.0
    set_ringtone_volume(0.2);
    set_busytone_volume(0.2);

    // 로컬스트림 객체를 등록
    set_local_vchat_stream_object($('#myvideo'));

    // 리모트 스트림 객체를 등록
    set_remote_vchat_stream_object($('#remotevideo'));

    // 나의 username을 등록
    set_myusername_vchat("agent-AAA");
    // 상대방의 username을 등록
    set_remoteusername_vchat("chat-client-BBB");

    // 라이브러리 초기화 (모든 콘솔 디버거 활성화)
    start_vchat();

    console.log("=========== WEBRTC_INFO ===========");
    console.dir(WEBRTC_INFO);
});