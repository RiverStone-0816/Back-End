<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="dial-pad">
    <div class="header">
        다이얼 패드
        <button class="dial-close"><img src="<c:url value="/resources/images/close.svg"/>"></button>
    </div>
    <div class="content">
        <div class="number-result">
            <input type="text" class="" id="dial-pad-input"/>
        </div>
        <div class="ui three column grid number">
            <c:forEach var="e" items="${'123456789*0#'.toCharArray()}">
                <div class="column">
                    <button class="ui button fluid" onclick="appendDialPadInput('${e}')">${e}</button>
                </div>
            </c:forEach>
        </div>
        <div class="call-btn-wrap">
            <button class="ui grey button" onclick="removeLastDialNumber()">지우기</button>
            <button class="ui brand button" onclick="tryDialByDialPadInput();">전화걸기</button>
        </div>
    </div>
</div>
<div id="videos" style="display:none;">
    <div id="videoleft">
        <video id="myVideo" autoplay playsinline muted="muted"/>
    </div>
    <div id="videoright">
        <video id="remoteVideo" autoplay playsinline/>
    </div>
</div>

<tags:scripts>
    <script>
        const softPhoneApi = new SoftPhoneApi();
        const dialPadInput = $('#dial-pad-input');

        $(document).ready(() => {
            // WebRTC 소프트폰 사용 가능 여부 확인
            if (softPhoneApi.is_support_webrtc('${g.user.phoneKind}') === false) {
                return false;
            }

            softPhoneApi.set_ringtone_volume(1.0);
            softPhoneApi.set_busytone_volume(1.0);

            softPhoneApi.set_local_stream_object($('#myVideo'));
            softPhoneApi.set_remote_stream_object($('#remoteVideo'));

            softPhoneApi.set_accept_call_btn_object($(".-call-receive"));

            softPhoneApi.set_callback_registered_status(() => {
                $('div.dial-pad .header').css('background-color','lime');
            });
            softPhoneApi.set_callback_unregistered_status(() => {
                $('div.dial-pad .header').css('background-color','red');
            });
            softPhoneApi.set_callback_disconnected_status(() => {
                $('div.dial-pad .header').css('background-color','grey');
            });


            restSelf.get('/api/auth/softPhone-info').done(function (response) {
                console.log(response);
                softPhoneApi.start_webrtc(response.data.peerNum, response.data.peerSecret
                    , response.data.serverInformation.pbxServerIp
                    , response.data.serverInformation.pbxServerPort
                    , response.data.serverInformation.webrtcServerIp
                    , response.data.serverInformation.webrtcServerPort
                    , response.data.serverInformation.turnServerIp
                    , response.data.serverInformation.turnServerPort
                    , response.data.serverInformation.turnUser
                    , response.data.serverInformation.turnSecret);
            });

            console.log("=========== WEBRTC_INFO ===========");
            console.dir(softPhoneApi.WEBRTC_INFO);
        })

        function removeLastDialNumber() {
            if (dialPadInput.val().length <= 0)
                return;

            dialPadInput.val(dialPadInput.val().substr(0, dialPadInput.val().length - 1));
            dialPadInput.keyup();
        }

        function appendDialPadInput(letter) {
            dialPadInput.val(dialPadInput.val() + letter);
            softPhoneApi.sipcall.dtmf({dtmf: {tones: letter}});
            dialPadInput.keyup();
        }

        function tryDialByDialPadInput() {
            ipccCommunicator.clickDial('', $.onlyNumber(dialPadInput.val()));
        }
    </script>
</tags:scripts>
