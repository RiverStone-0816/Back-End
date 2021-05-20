<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div class="ui modal large">
    <i class="close icon"></i>
    <div class="header">이의 처리</div>

    <div class="content">
        <div class="scrolling content">
            <jsp:include page="/admin/record/evaluation/result/${id}/cdr-evaluation-form"/>

            <div style="margin-bottom:10px">
                <table class="ui celled table compact unstackable two">
                    <tbody>
                    <tr>
                        <th style="width: 200px;">이의내용</th>
                        <td>
                            <div class="ui form">
                                <div class="field">
                                    <textarea readonly="readonly" rows="3">${g.htmlQuote(entity.challengeMemo)}</textarea>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button -submit">확인</button>
    </div>
</div>

<script>
    const evaluationId = ${entity.evaluationId};
    const scores = {<c:forEach var="e" items="${entity.scores}">'${e.itemId}': ${e.score}, </c:forEach>};

    $('.modal').filter(function () {
        return this !== modal[0];
    }).remove();

    modal.on('focusout, blur', '[data-name=score][data-max]', function () {
        const score = parseInt($(this).val());
        const max = parseInt($(this).attr('data-max'));
        $(this).val(isNaN(score) || !isFinite(score) ? 0 : score > max ? max : score);
    });

    modal.find('[name=evaluationId]').change(function () {
        const formId = parseInt($(this).val());
        replaceReceivedHtml('/admin/record/evaluation/form/' + formId + '/form', '#evaluation-form').done(function () {
            if (formId !== evaluationId)
                return;

            for (let itemId in  scores) {
                if (scores.hasOwnProperty(itemId)) {
                    modal.find('[name="scores.'+itemId+'"]').val(scores[itemId]);
                }
            }
        });
    }).change();

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
