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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<form:form id="modal-sms-send-form" modelAttribute="form" cssClass="ui modal inverted tiny -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/send-sms"
           data-before="prepareSMSSendForm" data-done="doneSMSReload">
    <i class="close icon"></i>
    <div class="header">SMS발송</div>
    <div class="content rows scrolling panel-rows">
        <div class="ui grid">
            <div class="row">
                <div class="sixteen wide column">
                    <div class="ui form fluid">
                        <div class="field">
                            <form:textarea path="content" class="template-message" rows="10"/>
                        </div>
                        <div class="field">
                            <b><text class="-send-type">SMS</text></b> : <text class="-send-byte">0</text> Byte
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">카테고리</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select id="sms-category">
                            <option value ="" label="선택안함"></option>
                            <c:forEach var="e" items="${smsCategories}">
                                <option value="${g.htmlQuote(e.categoryCode)}">${g.htmlQuote(e.categoryName)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">상용문구</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="template" class="sms-template" multiple="multiple"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">대표번호</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="serviceNumber">
                            <c:forEach var="e" items="${serviceLists}">
                                <form:option value="${g.htmlQuote(e.svcCid)}" label="${g.htmlQuote(e.svcName)}(${g.htmlQuote(e.svcCid)})"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">전화번호</label></div>
                <div class="twelve wide column ui form">
                    <div class="fields">
                        <div class="sixteen wide field">
                            <input name="targetNumbers" type="text" class="-input-numerical">
                        </div>
                    </div>
                </div>
            </div>
            <%--<div class="row">
                <div class="three wide column"><label class="control-label">파일선택</label></div>
                <div class="thirteen wide column">
                    <form:hidden path="fileName"/>
                    <form:hidden path="filePath"/>
                    <form:hidden path="originalName"/>
                    <div class="file-upload-header">
                        <label for="smsFile" class="ui button brand mini compact">파일찾기</label>
                        <input type="file" id="smsFile" onchange="uploadFile(this.files[0])">
                        <span class="smsFile-name">No file selected</span>
                    </div>
                    <div>
                        <progress value="0" max="100" style="width:100%"></progress>
                    </div>
                </div>
            </div>--%>
        </div>
    </div>
    <div class="actions form-actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button -submit">확인</button>
    </div>
</form:form>

<script>
    const fileList = modal.find('.file-upload-body .list');

    modal.find('#smsFile').change(function () {
        uploadSmsFile(this.files[0], modal.find('progress')).done(function (response) {
            if (!(response.data.originalName.endsWith('.jpg') || response.data.originalName.endsWith('.mmf') || response.data.originalName.endsWith('.k3g'))) {
                alert('jpg/mmf/k3g 형식만 가능합니다.');
                modal.find('progress').val(0);
                return;
            }
            modal.find('.smsFile-name').text(response.data.originalName);
            modal.find('[name=originalName]').val(response.data.originalName);
            modal.find('[name=fileName]').val(response.data.fileName);
            modal.find('[name=filePath]').val(response.data.filePath);
        });

        if (modal.find('[name=filePath]').val() === '' && modal.find('progress').val() === 100) {
            alert('최대 파일 사이즈는 300KB 까지입니다.');
            modal.find('progress').val(0);
        }
    });

    modal.find("#content").keyup(() => {
        chkContents(modal.find("#content").val());
    })

    function chkContents(t) {
        const b = getByteLengthOfString(t);
        if (b < 90) {
            modal.find('.-send-type').text('SMS');
            modal.find('.-send-type').removeClass('text-red');
        } else {
            modal.find('.-send-type').text('MMS');
            modal.find('.-send-type').addClass('text-red');
        }
        modal.find('.-send-byte').text(b);
    }

    const getByteLengthOfString = function (s, b, i, c) {
        for (b = i = 0; (c = s.charCodeAt(i++)); b += c >> 11 ? 3 : c >> 7 ? 2 : 1);
        return b;
    };

    const smsCategoryToTemplates = {
        <c:forEach var="e" items="${smsTemplates}">
        '${e.key}': [
            <c:forEach var="m" items="${e.value}">
            {
                id: '${g.htmlQuote(m.id)}',
                content: '${g.htmlQuote(m.content)}',
            },
            </c:forEach>
        ],
        </c:forEach>
        '': ''
    };

    window.prepareSMSSendForm = function (data) {
        $('[name=template]').each(function () {
            data.template = $('.sms-template option:selected').val();
        });

        data.targetNumbers = [];
        $('[name=targetNumbers]').each(function () {
            if ($(this).val())
                data.targetNumbers.push($(this).val());
        });
    };

    window.doneSMSReload = function () {
        alert('발송 되었습니다.', () => {
            modal.hide();
        });
    };

    $('#sms-category').change(function () {
        const value = $(this).val();

        $('.sms-template').empty();
        if (value === '') {
            $('.template-message').val('');
            defaultSmsTemplates();
            return;
        }

        if (values(smsCategoryToTemplates[value]).length > 0)
            smsCategoryToTemplates[value].forEach(e => {
                $('.sms-template').append($('<option/>', {value: e.id, 'data-value': value, text: e.content}));
            })
    });

    $('.sms-template').on('click', function () {
        $('.template-message').val($('.sms-template option:selected').text());
        chkContents($('.sms-template option:selected').text());
    });

    function defaultSmsTemplates() {
        <c:forEach var="e" items="${smsTemplates}">
        <c:forEach var="m" items="${e.value}">
        $('.sms-template').append($('<option/>', {value: '${g.htmlQuote(m.id)}', 'data-value': '${g.htmlQuote(e.key)}', text: '${g.htmlQuote(m.content)}'}));
        </c:forEach>
        </c:forEach>
    }
    defaultSmsTemplates();

    let cid = $('#cid option:selected').val();
    if (cid != null) {
        $('#serviceNumber').val(cid);
    }
</script>