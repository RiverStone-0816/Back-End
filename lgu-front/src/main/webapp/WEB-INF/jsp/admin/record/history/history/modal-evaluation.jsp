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

<div class="ui modal large">
    <i class="close icon"></i>
    <div class="header">상담원 평가</div>

    <div class="content">
        <div class="scrolling content">
            <jsp:include page="/admin/record/history/history/${seq}/cdr-evaluation-form"/>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button -submit">확인</button>
    </div>
</div>

<script>
    $('.modal').filter(function () {
        return this !== modal[0];
    }).remove();

    modal.on('focusout, blur', '[data-name=score][data-max]', function () {
        const score = parseInt($(this).val());
        const max = parseInt($(this).attr('data-max'));
        $(this).val(isNaN(score) || !isFinite(score) ? 0 : score > max ? max : score);
    });

    modal.find('[name=evaluationId]').change(function () {
        if ($(this).val() === "") {
            alert("평가지를 선택하세요.");
            return;
        }
        replaceReceivedHtml('/admin/record/evaluation/form/' + $(this).val() + '/form', '#evaluation-form');
    });

    window.prepareWriteFormData = function (data) {
        const scores = [];

        for (let itemId in data.scores) {
            if (data.scores.hasOwnProperty(itemId)) {
                scores.push({itemId: itemId, score: data.scores[itemId]});
            }
        }

        data.scores = scores;
    };

    window.doneWriteFormData = function () {
        alert('제출되었습니다.');
        modal.modalHide();
    };

    modal.find('.-submit').click(function () {
        modal.find('form:first').submit();
    });
</script>
