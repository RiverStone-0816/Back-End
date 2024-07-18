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
            <input type="number" style="width: 145px" id="dial-pad-input"/>
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
        <div class="call-btn-wrap">
            <button class="ui brand button" id="btn-mute" onclick="sipCallMute()">음소거</button>
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
        const dialPadInput = $('#dial-pad-input');

        function removeLastDialNumber() {
            if (dialPadInput.val().length <= 0)
                return;

            dialPadInput.val(dialPadInput.val().substr(0, dialPadInput.val().length - 1));
            dialPadInput.keyup();
        }

        function appendDialPadInput(letter) {
            dialPadInput.val(dialPadInput.val() + letter);
            sipcall.dtmf({dtmf: {tones: letter}});
            dialPadInput.keyup();
        }

        function tryDialByDialPadInput() {
            ipccCommunicator.clickByCampaign($('#cid${(g.usingServices.contains("AST") && g.user.isAstIn eq "Y") || (g.usingServices.contains('BSTT') && g.user.isAstStt eq "Y") ? "-stt" : ""}').val(), dialPadInput.val(), "SoftPhone", $('#call-custom-input [name=groupSeq]').val(), $('#call-custom-input .-custom-id').text());
        }

        let muteType = 1;
        function sipCallMute(){
            muteType *= -1;
            if(muteType === 1) {
                $('#btn-mute').removeClass('green');
                sipcall.unmuteAudio();
            } else {
                $('#btn-mute').addClass('green');
                sipcall.muteAudio();
            }
        }
    </script>
</tags:scripts>
