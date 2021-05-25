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

<form class="ui modal -json-submit" data-method="get"
      action="${pageContext.request.contextPath}/api/outbound-type/${entity.seq}/move"
      data-before="prepareUpdateSequenceFieldForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">유형순서변경[프리뷰]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="twelve wide column">
                    <div class="ui form">
                        <select multiple="multiple" class="one-multiselect" name="sequences">
                            <c:forEach var="e" items="${entity.fields}">
                                <option value="${e.seq}">${g.htmlQuote(e.fieldInfo)}[${g.htmlQuote(e.fieldName)}]</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-first">맨위로</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-prev">위로</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-next">아래로</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-last">맨아래로</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form>

<script>
    const select = modal.find('[name="sequences"]');

    window.prepareUpdateSequenceFieldForm = function (data) {
        data.sequences = [];
        select.find('option').each(function () {
            data.sequences.push($(this).val());
        });
    };

    modal.find('.-to-first').click(function () {
        const options = select.find('option:selected').detach();
        select.prepend(options);
    });
    modal.find('.-to-prev').click(function () {
        const prev = select.find('option:selected:first').prev();
        if (!prev.length) return;

        const options = select.find('option:selected').detach();
        options.insertBefore(prev);
    });
    modal.find('.-to-next').click(function () {
        const next = select.find('option:selected:last').next();
        if (!next.length) return;

        const options = select.find('option:selected').detach();
        options.insertAfter(next);
    });
    modal.find('.-to-last').click(function () {
        const options = select.find('option:selected').detach();
        select.append(options);
    });
    modal.find('.-delete').click(function () {
        const options = select.find('option:selected').detach();
        options.remove();
    });
</script>
