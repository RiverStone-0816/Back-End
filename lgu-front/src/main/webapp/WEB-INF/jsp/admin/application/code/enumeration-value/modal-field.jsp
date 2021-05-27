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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/enumeration-value/type/${type}/field/${entity.fieldId}/code"
           data-before="prepareUpdateSequenceFieldForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">코드수정</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">연동된하위코드</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="relatedFieldId">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${relatedFields}"/>
                        </form:select>
                    </div>
                </div>
                <div class="eight wide column">해당 결과 코드가 바뀌면 같이 연동되어 바뀌어야할 필드</div>
            </div>
            <div class="row">
                <div class="twelve wide column">
                    <div class="ui form">
                        <select multiple="multiple" class="one-multiselect" name="codes">
                            <c:forEach var="e" items="${entity.commonCodes}">
                                <option value="${g.htmlQuote(e.codeId)}" data-name="${g.htmlQuote(e.codeName)}" data-script="${g.htmlQuote(e.script)}" class="${e.hide == 'Y' ? 'hide' : null}">
                                    [${g.htmlQuote(e.codeId)}] ${g.htmlQuote(e.codeName)}
                                </option>
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
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -delete">삭제</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -hide">코드숨기기</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="two wide column"><label class="control-label">코드</label></div>
                <div class="three wide column">
                    <div class="ui input fluid">
                        <input type="text" name="codeId"/>
                    </div>
                </div>
                <div class="two wide column"><label class="control-label">코드명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid">
                        <input type="text" name="codeName"/>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form">
                        <button type="button" class="ui button mini -add-code">코드추가</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">스크립트</label></div>
                <div class="thirteen wide column">
                    <div class="ui form">
                        <div class="field">
                            <textarea name="script" row="7"></textarea>
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
</form:form>

<script>
    window.prepareUpdateSequenceFieldForm = function (data) {
        data.codes = [];
        const options = select.find('option');

        for (let i = 0; i < options.length; i++) {
            const option = options[i];
            data.codes.push({
                codeId: $(option).val(),
                codeName: $(option).attr('data-name'),
                sequence: i,
                hide: $(option).hasClass('hide') ? 'Y' : 'N',
                script: $(option).attr('data-script'),
            });
        }

        delete data.codeId;
        delete data.codeName;
        delete data.script;
    };

    const select = modal.find('[name=codes]');

    select.on('click','option',function () {
        modal.find('[name=codeId]').val($(this).val());
        modal.find('[name=codeName]').val($(this).attr('data-name'));
        modal.find('[name=script]').val($(this).attr('data-script') || '');
    });

    modal.find('.-add-code').click(function () {
        const codeId = modal.find('[name=codeId]').val();
        const codeName = modal.find('[name=codeName]').val();
        const script = modal.find('[name=script]').val();

        const existOption = select.find('option').filter(function () {
            return $(this).val() === codeId;
        });

        if (existOption.length > 0) {
            existOption.text('[' + codeId + '] ' + codeName)
            existOption.attr('data-name', codeName);
            existOption.attr('data-script', script);
        } else {
            select.append($('<option/>', {value: codeId, text: '[' + codeId + '] ' + codeName, 'data-name': codeName, 'data-script': script}));
        }
    });

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
    modal.find('.-hide').click(function () {
        select.find('option:selected').each(function () {
            $(this).toggleClass('hide');
        });
    });

</script>
