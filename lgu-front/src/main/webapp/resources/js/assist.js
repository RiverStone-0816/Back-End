/*
    * assist, kms 관련 javascript 코드 파일 입니다.
*/

// assist를 위한 socket 연결 (실패시 disconnection)
function initAssistSocketReady(sttSocketUrl){
    // console.log('initAssistSocketReady 실행@')
    // console.log('sttSocketUrl = ', sttSocketUrl)
    if(!window) return console.error('window 가 없습니다')
    if(window && window.ipccAssistCommunicator) {
        // return console.error('이미 ipccAssistCommunicator가 존재 합니다. window.ipccAssistCommunicator = ', window.ipccAssistCommunicator)
        return
    }

    window.ipccAssistCommunicator = new IpccAssistCommunicator();
    ipccAssistCommunicator
        .on('STT', function (message, kind, data1) {
            const data = JSON.parse(data1);
            if (data.kind === "CALL_END" && data.extension === phoneNumber) {
                //sttClear();
            } else {
                data.my_extension === phoneNumber ? console.log("고객 : " + data.data?.text) : console.log("상담사 : " + data.data?.text);
                processSttMessage(data);
            }
        })
        .on('STT_KEYWORD', function (message, kind, data1) {
            const data = JSON.parse(data1);
            processSttKeyword(data);
        })
        .on('ADMINMESSAGE', function (message, kind, data1) {
            const data = JSON.parse(data1);
            processAdminMessage(data);
        });

    restSelf.get('/api/auth/socket-info').done(function (response) {
        ipccAssistCommunicator.connect(sttSocketUrl, response.data.pbxHost, response.data.companyId, response.data.userId, response.data.extension, response.data.password);
    });
}

// KMS에 로그인 할 수 없는 상태를 확인 및 알림
function loginStatus(){
    const successTemplate = $('#assist-custom-sidebar #assist-custom-sidebar-wrap')
    const failureTemplate = $('#assist-custom-sidebar #kms-login-status.unavailable')
    if(successTemplate.length){
        console.log('KMS 로그인에 성공 하였습니다.')
    }
    else if(failureTemplate.length) {
        console.log('KMS 로그인에 실패 하였습니다.')
        const errorMessage = '상담지원 시스템 설정을 확인해주세요'
        alert(errorMessage)
    }else{
        console.log('KMS 기능을 사용하지 않습니다.')
    }
}