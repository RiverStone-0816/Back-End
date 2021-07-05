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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<form:form id="call-counseling-input" modelAttribute="form" cssClass="-json-submit"
           data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/maindb-result/${entity != null ? entity.seq : null}"
           data-before="prepareCounselingInfoFormData" data-done="donePostCounselingInfo">

    <div class="panel-heading fixed">
        <div class="pull-left">
            <label class="panel-label">상담결과 입력</label>
            <div class="ui checkbox ml10">
                <input type="checkbox" name="saveCustomInfo" class="options" checked>
                <label>고객정보저장</label>
            </div>
            <div class="ui checkbox ml10">
                <input type="checkbox" name="saveWaitStatusAfterPostCounselingInfo" class="options" checked>
                <label>저장 후 대기</label>
            </div>
        </div>
        <div class="pull-right">
            <button type="button" class="ui basic button" onclick="popupSearchCounselingHistoryModal()">상담이력</button>
            <button type="button" class="ui basic button" onclick="popupReservationModal()">상담예약</button>
            <button type="button" class="ui basic button" onclick="popupTransferModal()">상담이관</button>
            <button class="ui button sharp light -submit-form" data-method="post"><img src="<c:url value="/resources/images/save.svg"/>" alt="save">상담저장</button>
            <c:if test="${entity != null}">
                <button type="button" class="ui button sharp light -submit-form" data-method="put"><img src="<c:url value="/resources/images/save.svg"/>" alt="save">상담수정</button>
            </c:if>
        </div>
    </div>

    <div class="panel-body overflow-auto">
        <form:hidden path="groupKind"/>
        <form:hidden path="customId"/>
        <form:hidden path="maindbType"/>
        <form:hidden path="resultType"/>
        <form:hidden path="groupId"/>
        <form:hidden path="clickKey"/>

        <table class="ui celled table compact unstackable border-top-default">
            <tbody>
            <tr>
                <c:set var="chargedColCount" value="${0}"/>
                <c:forEach var="field" items="${resultType.fields}" varStatus="status">
                <c:set var="name" value="${field.fieldId.substring(resultType.kind.length() + '_'.length()).toLowerCase()}"/>
                <c:set var="value" value="${fieldNameToValueMap.get(field.fieldId)}"/>

                <c:choose>
                    <c:when test="${field.fieldType == 'MULTICODE'}">
                        <c:if test="${chargedColCount > 0}">
                            <td colspan="${8 - chargedColCount}" class="border-left-none"></td>
                            ${'</tr><tr>'}
                        </c:if>
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="7">
                            <div class="ui form flex">
                                <select name="${name}" id="${name}" data-type="select"
                                        data-text="${g.htmlQuote(field.fieldInfo)}"
                                        data-value="${field.isneed}" multiple="multiple"
                                        class="ui fluid dropdown">
                                    <option value=""></option>
                                    <c:forEach var="e" items="${field.codes}">
                                        <c:set var="contains" value="${false}"/>
                                        <c:if test="${value != null}">
                                            <c:forEach var="e2" items="${value.split(',')}">
                                                <c:if test="${!contains}">
                                                    <c:set var="contains"
                                                           value="${e2 == e.codeId}"/>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                        <option value="${g.htmlQuote(e.codeId)}" ${contains ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                    </c:forEach>
                                </select>
                                <button type="button" class="ui button sharp navy ml5"
                                        onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#call-counseling-input').find('#${name}').val()) ">TIP
                                </button>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${8}"/>
                    </c:when>
                    <c:when test="${field.fieldType == 'CODE'}">
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="3">
                            <div class="ui form flex">
                                <select name="${name}" id="${name}" data-type="select" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}">
                                    <option value=""></option>
                                    <c:forEach var="e" items="${field.codes}">
                                        <option value="${g.htmlQuote(e.codeId)}" ${value == e.codeId ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                    </c:forEach>
                                </select>
                                <button type="button" class="ui button sharp navy ml5"
                                        onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', [$('#call-counseling-input').find('#${name}').val()])">TIP
                                </button>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${chargedColCount + 4}"/>
                    </c:when>
                    <c:when test="${field.fieldType == 'INT' || field.fieldType == 'NUMBER'}">
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="3">
                            <div class="ui form">
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" class="-input-numerical"
                                       value="${g.htmlQuote(value)}"/>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${chargedColCount + 4}"/>
                    </c:when>
                    <c:when test="${field.fieldType == 'DATETIME'}">
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="3">
                            <div class="ui form flex">
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" multiple="multiple"
                                       value="${value != null ? g.dateFormat(value) : null}"
                                       class="-datepicker" style="width: 130px"/>&ensp;
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" multiple="multiple"
                                       value="${value != null ? value.hours : null}"
                                       class="-input-numeric" style="width: 50px"/>
                                <text style="line-height: 30px">시</text>
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" multiple="multiple"
                                       value="${value != null ? value.minutes : null}"
                                       class="-input-numeric" style="width: 50px"/>
                                <text style="line-height: 30px">분</text>
                                <input type="hidden" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" multiple="multiple"
                                       value="00" class="-input-numeric"/>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${chargedColCount + 4}"/>
                    </c:when>
                    <c:when test="${field.fieldType == 'DATE' || field.fieldType == 'DAY'}">
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="3">
                            <div class="ui form">
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" value="${value}"
                                       class="-datepicker"/>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${chargedColCount + 4}"/>
                    </c:when>
                    <c:when test="${field.fieldType == 'STRING' && field.fieldSize > 50}">
                        <c:if test="${chargedColCount > 0}">
                            <td colspan="${8 - chargedColCount}" class="border-left-none"></td>
                            ${'</tr><tr>'}
                        </c:if>
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="7">
                            <div class="ui form">
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}"
                                       maxlength="${field.fieldSize}" value="${g.escapeQuote(value)}"/>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${8}"/>
                    </c:when>
                    <c:when test="${field.fieldType == 'IMG'}">
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="3">
                            <div class="ui form flex">
                                <input name="${name}" type="hidden" value="">
                                <div class="file-upload-header">
                                    <label for="file" class="ui button blue mini compact">파일찾기</label>
                                    <input type="file" id="file" data-value="${name}">
                                    <span class="file-name">No file selected</span>
                                </div>
                                    <%--<div>
                                        <progress value="0" max="100" style="width:100%"></progress>
                                    </div>--%>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${chargedColCount + 4}"/>
                    </c:when>
                    <c:otherwise>
                        <th><label for="${name}">${g.htmlQuote(field.fieldInfo)}</label></th>
                        <td colspan="3">
                            <div class="ui input fluid">
                                <input type="text" name="${name}" id="${name}" data-type="text"
                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                       data-value="${field.isneed}" maxlength="${field.fieldSize}"
                                       value="${g.htmlQuote(value)}" placeholder="${field.fieldSize > 0 ? '최대길이:'.concat(field.fieldSize).concat(' Bytes') : ''}"/>
                            </div>
                        </td>
                        <c:set var="chargedColCount" value="${chargedColCount + 4}"/>
                    </c:otherwise>
                </c:choose>

                <c:if test="${chargedColCount >= 8 || status.last}">
            </tr>
                ${!status.last ? '<tr>' : ''}
            <c:set var="chargedColCount" value="${0}"/>
            </c:if>
            </c:forEach>

            <c:if test="${todoList != null && todoList.size() > 0}">
                <tr>
                    <td>TODO 처리</td>
                    <td colspan="7">
                        <c:forEach var="e" items="${todoList}">
                            <input type="hidden" name="todoSequences" data-multiple="true" value="${e.seq}"/>
                        </c:forEach>
                        <div class="ui form flex">
                            <select name="todoStatus">
                                <c:forEach var="todoStatus" items="${todoStatuses}">
                                    <option value="${g.htmlQuote(todoStatus.key)}">${g.htmlQuote(todoStatus.value)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </td>
                </tr>
            </c:if>
            <c:if test="${serviceKind.equals('SC')}">
                <c:if test="${smsVocGroups != null && smsVocGroups.size() > 0}">
                    <tr>
                        <td>VOC/해피콜</td>
                        <td colspan="7">
                            <div class="ui grid" style="padding: 0.5rem 0;">
                                <div class="row">
                                    <div class="eight wide column">
                                        <div class="ui form flex">
                                            <select name="smsVocGroup">
                                                <option>SMS선택</option>
                                                <c:forEach var="smsVocGroup" items="${smsVocGroups}">
                                                    <option value="${smsVocGroup.seq}"
                                                            data-type="${smsVocGroup.isArsSms}">${smsVocGroup.vocGroupName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="eight wide column -voc-sms">
                                        <div class="ui form">
                                            <div class="inline fields">
                                                <div class="field">
                                                    SMS 전송됨
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${vocGroups != null && vocGroups.size() > 0}">
                    <tr>
                        <td>VOC/해피콜</td>
                        <td colspan="7">
                            <div class="ui grid" style="padding: 0.5rem 0;">
                                <div class="row">
                                    <div class="eight wide column">
                                        <div class="ui form flex">
                                            <form:select path="vocGroup">
                                                <form:option value="" label="ARS선택"/>
                                                <c:forEach var="vocGroup" items="${vocGroups}">
                                                    <form:option value="${vocGroup.seq}"
                                                                 label="${vocGroup.vocGroupName}"
                                                                 data-research-id="${vocGroup.arsResearchId}"
                                                                 data-type="${vocGroup.isArsSms}"/>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                    <div class="eight wide column -voc-ars">
                                        <div class="ui form">
                                            <div class="inline fields">
                                                <div class="field ui radio checkbox">
                                                    <input type="radio" name="vocSchedule" value="instance" checked/>
                                                    <label>돌려주기</label>
                                                </div>
                                                <div class="field ui radio checkbox">
                                                    <input type="radio" name="vocSchedule" value="after"/>
                                                    <label>콜종료 후 진행</label>
                                                </div>
                                                <div class="field">
                                                    <button type="button" class="ui button mini -voc-submit">실행</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:if>
            </c:if>
            </tbody>
        </table>
    </div>
</form:form>

<script>
    ui.find('[type="file"]').change(function () {
        uploadFile(this.files[0], ui.find('progress')).done(function (response) {
            const name = ui.find('[type="file"]').attr('data-value');
            ui.find('.file-name').text(response.data.originalName);
            ui.find('[name=' + name + ']').val(response.data.fileName);
        });
    });

    const DBTYPE_FIELD_PREFIX = 'RS_';

    const fields = {
        <c:forEach var="field" items="${resultType.fields}">
        <c:if test="${field.codes != null && field.codes.size() > 0}">
        '${g.escapeQuote(field.fieldId)}': [<c:forEach var="code" items="${field.codes}">{
            value: '${g.escapeQuote(code.codeId)}',
            text: '${g.escapeQuote(code.codeName)}'
        }, </c:forEach>],
        </c:if>
        </c:forEach>
    };
    const fieldToRelatedField = {<c:forEach var="e" items="${fieldToRelatedField}">'${g.escapeQuote(e.key)}': '${g.escapeQuote(e.value)}', </c:forEach>};

    function convertToDbTypeFieldId(fieldId) {
        return DBTYPE_FIELD_PREFIX + fieldId.toUpperCase();
    }

    function convertToFormFieldId(fieldId) {
        return fieldId.substr(DBTYPE_FIELD_PREFIX.length).toLowerCase();
    }

    ui.find('select').change(function () {
        const selectName = $(this).attr('name');
        if (!selectName)
            return;

        const dbName = convertToDbTypeFieldId(selectName);
        const parentValue = $(this).val();

        if (!fieldToRelatedField[dbName])
            return;

        const relatedField = ui.find('[name="' + convertToFormFieldId(fieldToRelatedField[dbName]) + '"]');
        const preValue = relatedField.val();

        relatedField.empty()
            .append($('<option/>', {value: '', text: ''}));
        fields[fieldToRelatedField[dbName]].map(function (o) {
            if (o.value.indexOf(parentValue) !== 0)
                return;

            relatedField.append($('<option/>', {value: o.value, text: o.text}).prop('selected', o.value === preValue));
        });
        relatedField.change();
    });


    const STORAGE_KEY = "counselingInput";

    ui.find('.-voc-submit').click(function () {
        const vocGroupSchedule = ui.find('[name=vocSchedule]').val();
        const vocGroup = ui.find('[name=vocGroup]').val();
        const vocGroupArs = ui.find('[name=vocGroup] option:selected').attr('data-research-id');

        if (vocGroupSchedule === 'instance' && vocGroup && vocGroupArs)
            ipccCommunicator.voc(vocGroup, vocGroupArs);
    });

    window.prepareCounselingInfoFormData = function (data) {
        delete data.saveCustomInfo;
        delete data.saveWaitStatusAfterPostCounselingInfo;

        data.uniqueId = audioId;
        data.customNumber = phoneNumber !== '' ? phoneNumber : $('#call-custom-input').find('[name=channels] option:first').val();

        const transferredUser = getTransferredUser();
        if (transferredUser) data.userIdTr = transferredUser;

        for (let name in data) {
            if (data.hasOwnProperty(name)) {
                if (name.indexOf('datetime_') === 0) {
                    if (!(function () {
                        for (let i = 0; i < data[name].length; i++) {
                            if (!data[name][i]) return false;
                        }
                        return true;
                    })()) {
                        delete data[name];
                    } else {
                        data[name] = moment(data[name][0]).hour(data[name][1]).minute(data[name][2]).second(data[name][3]).valueOf();
                    }
                } else if (data[name] instanceof Array && name.indexOf('multicode_') === 0) {
                    data[name] = data[name].join(',');
                }
            }
        }

        if (!data.todoStatus)
            delete data.todoStatus;

        delete data.vocSchedule;
        delete data.smsVocGroup;
    };

    window.donePostCounselingInfo = function () {
        alert('상담정보가 저장되었습니다.');
        clearCustomerAndCounselingInput();
        loadTodoList();
        loadCounselingList("");
    };

    $(document).ready(function () {
        const storedValues = localStorage.getItem(STORAGE_KEY);

        if (storedValues) {
            const values = JSON.parse(storedValues)

            const options = $('.options');
            for (let key in values) {
                if (values.hasOwnProperty(key)) {
                    options.filter(function () {
                        return $(this).attr('name') === key;
                    }).prop('checked', values[key]);
                }
            }
        }
    });

    ui.find('[name=vocGroup]').change(function () {
        const type = $(this).find(':selected').attr('data-type');

        if (type === 'ARS') {
            ui.find('.-voc-ars').show();
            ui.find('.-voc-sms').hide();
        } else if (type === 'SMS') {
            ui.find('.-voc-ars').hide();
            ui.find('.-voc-sms').show();
            ui.find('[name=vocSchedule] [value=instance]').click();
        } else {
            ui.find('.-voc-ars').hide();
            ui.find('.-voc-sms').hide();
        }
    }).change();

    ui.find('.-submit-form').click(function () {
        ui.find('#clickKey').val(ipccCommunicator.status.clickKey);
        ipccCommunicator.status.clickKey = null;

        function submit(customId) {
            const customIdInput = ui.find('[name=customId]');
            customIdInput.val(customIdInput.val() || customId || $('#call-custom-input .-custom-id').text());

            submitJsonData(ui[0]).done(function () {
                if (ui.find('[name="saveWaitStatusAfterPostCounselingInfo"]').is(':checked')) {
                    ipccCommunicator.setMemberStatus(0);
                }
            });
        }

        if (this.getAttribute('data-method') === 'put') {
            $('#call-counseling-input').attr("action", "${pageContext.request.contextPath}/api/maindb-result/${entity != null ? entity.seq : null}");
            $('#call-counseling-input').data('method', 'put');
        } else {
            $('#call-counseling-input').attr("action", "${pageContext.request.contextPath}/api/maindb-result/");
            $('#call-counseling-input').data('method', 'post');
        }

        let objText = ui.find('[data-value="Y"]');
        for (let i = 0; i < objText.length; i++) {
            if (objText[i].getAttribute('data-type') === 'text') {
                if (objText[i].value.trim() === "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                    return;
                }
            } else if (objText[i].getAttribute('data-type') === 'select') {
                if (objText[i].options[objText[i].selectedIndex].value === "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 선택 해 주세요.");
                    return;
                }
            } else {
                if (objText[i].value.trim() === "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                    return;
                }
            }
        }

        if (ui.find('[name="saveCustomInfo"]').is(':checked')) {
            let customInfo = $("#call-custom-input");
            let customObjText = customInfo.find('[data-value="Y"]');

            for (let i = 0; i < customObjText.length; i++) {
                if (customObjText[i].getAttribute('data-type') === 'text') {
                    if (customObjText[i].value.trim() === "") {
                        alert("고객정보에서 [" + customObjText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                        return;
                    }
                } else if (customObjText[i].getAttribute('data-type') === 'select') {
                    if (customObjText[i].options[customObjText[i].selectedIndex].value === "") {
                        alert("고객정보에서 [" + customObjText[i].getAttribute('data-text') + "] 을(를) 선택 해 주세요.");
                        return;
                    }
                } else {
                    if (customObjText[i].value.trim() === "") {
                        alert("고객정보에서 [" + customObjText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                        return;
                    }
                }
            }

            submitCallCustomInput().done(function (response) {
                submit(response.data);
            });

        } else {
            submit();
        }
    });

    $('.options').change(function () {
        localStorage.setItem(STORAGE_KEY, JSON.stringify({
            saveCustomInfo: $('input[name="saveCustomInfo"]').prop('checked'),
            saveWaitStatusAfterPostCounselingInfo: $('input[name="saveWaitStatusAfterPostCounselingInfo"]').prop('checked')
        }));
    });

    function prepareResultCodeSelect() {
        <c:forEach var="e" items="${fieldNameToValueMap}">
        <c:if test="${e.key.contains('RS_CODE')}">
        ui.find('[name=${e.key.substring(resultType.kind.length() + '_'.length()).toLowerCase()}]').change();
        </c:if>
        </c:forEach>
    }

    prepareResultCodeSelect();
</script>
