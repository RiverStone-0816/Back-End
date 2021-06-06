<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
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
            <input type="text" class="-input-phone" id="dial-pad-input"/>
            <button class="ui button" onclick="removeLastDialNumber()"><img src="<c:url value="/resources/images/close.svg"/>"></button><%--todo: 스타일 확인--%>
        </div>
        <div class="ui three column grid number">
            <c:forEach var="e" items="${'123456789*0#'.toCharArray()}">
                <div class="column">
                    <button class="ui button fluid" onclick="appendDialPadInput('${e}')">${e}</button>
                </div>
            </c:forEach>
        </div>
        <div class="call-btn-wrap">
            <button class="ui button fluid" onclick="tryDialByDialPadInput();">전화걸기</button>
        </div>
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
            dialPadInput.keyup();
        }

        function tryDialByDialPadInput() {
            ipccCommunicator.clickDial('', $.onlyNumber(dialPadInput.val()));
        }
    </script>
</tags:scripts>
