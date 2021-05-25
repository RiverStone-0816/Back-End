<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>


<div class="ui modal inverted tiny" id="note-send-popup">
    <i class="close icon"></i>
    <div class="header">
        쪽지 발송 - 수신:테스트[616]
    </div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="sixteen wide column">
                    <div class="ui form fluid">
                        <div class="field">
                            <input type="hidden" name="peer"/>
                            <textarea rows="10" name="message"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button" onclick="sendMessageToConsultant()">확인</button>
    </div>
</div>

<tags:scripts>
    <script>
        function noteSendPopup(extension, idName) {
            const modal = $('#note-send-popup');
            modal.dragModalShow();
            modal.find('header:first').text('쪽지 발송 - 수신: ' + idName + '(' + extension + ')');
            modal.find('[name=peer]').val(extension);
        }

        function sendMessageToConsultant() {
            const modal = $('#note-send-popup');
            modal.asJsonData().done(function (data) {
                ipccCommunicator.sendMessage(data.peer, data.message);
            });
            modal.modalHide();
        }
    </script>
</tags:scripts>
