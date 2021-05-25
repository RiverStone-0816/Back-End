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


<div class="ui modal inverted tiny" id="modal-customer-grade-manage-popup">
    <i class="close icon"></i>
    <div class="header">등급관리</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">등급</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <input type="hidden" name="number"/>
                        <select name="type">
                            <option value="VIP">VIP</option>
                            <option value="BLACK">블랙리스트</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column"><label class="control-label">등록사유</label></div>
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
        <button type="button" class="ui blue button" onclick="submitRouteApplication()">확인</button>
    </div>
</div>

<tags:scripts>
    <script>
        function popupGradeAppModal() {
            const phoneNumber = $('#counseling-target').text();
            if (!phoneNumber)
                return alert('상담중에만 가능합니다.');

            $('#modal-customer-grade-manage-popup').dragModalShow();
            $('#modal-customer-grade-manage-popup [name]').val('');
            $('#modal-customer-grade-manage-popup [name=number]').val(phoneNumber);
        }

        function submitRouteApplication() {
            $('#modal-customer-grade-manage-popup').asJsonData().done(function (data) {
                restSelf.post('/api/route-app/', data).done(function () {
                    alert('신청되었습니다.');
                    $('#modal-customer-grade-manage-popup').modalHide();
                });
            });
        }
    </script>
</tags:scripts>
