//
// JavaScript for EICN Softphone
//

$(document).ready(function() {
    // WebRTC 소프트폰 사용 가능 여부 확인
    if (is_support_softphone() === false) {
        return;
    }

    // webrtc 서버접속에 필요한 정보를 전역객체 WEBRTC_INFO에 저장한다.
    if (set_webrtc_server_info() === false) {
        return;
    }

    // 소프트폰이 Registered 상태가 되었을 때, 호출되는 콜백함수 등록
    set_callback_sipcall_registered_status(function() {
        $('div.userbox div.prof_pic').css('background-color', 'lime');
    });
    // 소프트폰이 Unregistered 상태가 되었을 때, 호출되는 콜백함수 등록
    set_callback_sipcall_unregistered_status(function() {
        $('div.userbox div.prof_pic').css('background-color', 'red');
    });
    // 소프트폰이 Disconnected 상태가 되었을 때, 호출되는 콜백함수 등록
    set_callback_sipcall_disconnected_status(function() {
        $('div.userbox div.prof_pic').css('background-color', 'grey');
    });

    // 전화받기 버튼 객체를 등록
    $btn_accept_sipcall = $(".left_btn ul li[menu_id=MENU_RECEIVE]");
    set_accept_sipcall_btn_object($btn_accept_sipcall);

    //set_ringtone_on();
    //set_busytone_on();

    // volume range: 0.0 ~ 1.0
    set_ringtone_volume(1.0);
    set_busytone_volume(1.0);

    // 로컬스트림 객체를 등록
    set_local_sipcall_stream_object($('#mysipcall'));

    // 리모트 스트림 객체를 등록
    set_remote_sipcall_stream_object($('#remotesipcall'));

    // 라이브러리 초기화 (모든 콘솔 디버거 활성화)
    start_sipcall();

    console.log("=========== WEBRTC_INFO ===========");
    console.dir(WEBRTC_INFO);
});