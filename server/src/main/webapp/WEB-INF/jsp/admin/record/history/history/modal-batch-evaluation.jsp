<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div class="ui modal xlarge">
    <i class="close icon"></i>
    <div class="header">상담원 평가</div>

    <div class="content">
        <div class="ui grid">
            <div class="eleven wide column">
                <div class="scrolling content pr15">
                    <div id="cdr-evaluation-form"></div>
                </div>
            </div>
            <div class="five wide column scrolling content rows">
                <h3 class="ui header">평가리스트</h3>
                <div>
                    <table class="ui table celled compact unstackable selectable-only">
                        <thead>
                        <tr>
                            <th>통화날짜</th>
                            <th>고객번호</th>
                            <th>평가여부</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="e" items="${list}">
                            <tr class="-cdr-item" data-id="${e.seq}" style="cursor: pointer;">
                                <td class="-item" data-id="${e.seq}">
                                    <fmt:formatDate value="${e.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td class="-item" data-id="${e.seq}">
                                        ${g.htmlQuote(e.personList.extension.equals(e.dst) || e.service.svcNumber.equals(e.dst) ? e.src : e.dst)}
                                </td>
                                <td class="-radio-item">
                                    <div class="ui radio checkbox">
                                        <input type="radio" name="radio${e.seq}" checked="checked" value="Y">
                                        <label>평가함</label>
                                    </div>
                                    <div class="ui radio checkbox">
                                        <input type="radio" name="radio${e.seq}" value="N">
                                        <label>안함</label>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
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

    modal.on('change', '[name=evaluationId]', function () {
        if ($(this).val().length > 0)
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

    window.doneWriteFormData = function (form) {
        alert('제출되었습니다.');
        modal.find('.-cdr-item.active').remove();
        if (modal.find('.-cdr-item').length > 0)
            loadFirstCdr();
        else
            modal.modalHide();
    };

    function replaceCdr(seq) {
        replaceReceivedHtml("/admin/record/history/history/" + seq + "/cdr-evaluation-form", '#cdr-evaluation-form').done(function () {
            modal.find('[name=evaluationId]').change();
        });
    }

    function loadFirstCdr() {
        replaceCdr(modal.find('.-cdr-item:first').addClass('active').attr('data-id'));
    }

    modal.find('.-item').click(function () {
        const seq = $(this).attr('data-id');
        confirm('작성 중이던 평가가 교체됩니다. 변경하시겠습니까?').done(function () {
            replaceCdr(seq);
        });
    });

    modal.find('.-submit').click(function () {
        modal.find('form:first').submit();
    });

    loadFirstCdr();
</script>