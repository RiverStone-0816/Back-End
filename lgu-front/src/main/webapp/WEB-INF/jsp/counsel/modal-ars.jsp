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

<div class="ui modal inverted tiny" id="modal-consulting-ars-popup">
    <i class="close icon"></i>
    <div class="header">ARS</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">멘트선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <input type="hidden" name="number"/>
                        <input type="hidden" name="uniqueId"/>
                        <select name="sound">
                            <option value="" label="선택안함"></option>
                            <c:forEach var="e" items="${ProtectArs}">
                                <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                            <c:forEach var="e" items="${sounds}">
                                <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column"><label class="control-label">블랙리스트요청사유</label></div>
                <div class="sixteen wide column">
                    <div class="ui form fluid">
                        <div class="field">
                            <textarea name="memo" rows="3"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button" onclick="submitBlacklistApplication()">확인</button>
    </div>
</div>

<tags:scripts>
    <script>
        function popupArsModal() {
            if (!audioId)
                return alert('상담중에만 가능합니다.');

            $('#modal-consulting-ars-popup').dragModalShow();
            $('#modal-consulting-ars-popup [name]').val('');
            $('#modal-consulting-ars-popup [name=number]').val(phoneNumber);
            $('#modal-consulting-ars-popup [name=uniqueId]').val(audioId);
        }

        function submitBlacklistApplication() {
            $('#modal-consulting-ars-popup').asJsonData().done(function (data) {
                if (data.sound)
                    ipccCommunicator.protectArs(data.sound);

                restSelf.post('/api/route-app/', {
                    type: 'BLACK',
                    number: data.number,
                    memo: data.memo,
                    uniqueId: audioId,
                }).done(function () {
                    alert('처리되었습니다.');
                    $('#modal-consulting-ars-popup').modalHide();
                });
            });
        }
    </script>
</tags:scripts>
