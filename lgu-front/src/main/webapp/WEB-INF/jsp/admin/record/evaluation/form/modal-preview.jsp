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
    <div class="header">상세보기</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="sixteen wide column">
                <div class="scrolling content pr15">
                    <div>
                        <jsp:include page="/admin/record/evaluation/form/${id}/form"/>
                    </div>
                    <div style="margin-top:10px">
                        <table class="ui celled table compact unstackable two">
                            <tbody>
                            <tr>
                                <th>메모</th>
                                <td>
                                    <div class="ui form">
                                        <div class="field">
                                            <form:textarea path="memo" rows="3"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button modal-close">확인</button>
    </div>
</div>

<script>
    modal.find('[data-name=score][data-max]').on('focusout, blur', function () {
        const score = parseInt($(this).val());
        const max = parseInt($(this).attr('data-max'));
        $(this).val(isNaN(score) || !isFinite(score) ? 0 : score > max ? max : score);
    });
</script>
