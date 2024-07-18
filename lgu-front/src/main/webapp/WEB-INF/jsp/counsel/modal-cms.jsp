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

<div class="ui modal inverted tiny" id="modal-cms-popup">
    <i class="close icon"></i>
    <div class="header">
        CMS
    </div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">은행명</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <input type="text" name="bank">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">계좌번호</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <input type="text" name="account">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">예금주</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <input type="text" name="holder">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">이체일</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <input type="text" name="transferringDate" class="-datepicker">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">이체금액</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <input type="text" name="transferringAmount" class="-input-numerical">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button class="ui button modal-close">취소</button>
        <button class="ui blue button" onclick="applyCms()">확인</button>
    </div>
</div>

<tags:scripts>
    <script>
        function popupCmsModal() {
            const phoneNumber = $('#counseling-target${(g.usingServices.contains('AST') && g.user.isAstIn eq 'Y') || (g.usingServices.contains('BSTT') && g.user.isAstStt eq 'Y') ? "-stt" : ""}').text();
            if (!phoneNumber)
                return alert('상담중에만 가능합니다.');

            $('#modal-cms-popup').dragModalShow();
            $('#modal-cms-popup [name]').val('');
        }

        function applyCms() {
            $('#modal-cms-popup').asJsonData().done(function (data) {
                ipccCommunicator.applyCms(data.bank, data.account, data.holder, data.transferringDate, data.transferringAmount);
            });
        }
    </script>
</tags:scripts>
