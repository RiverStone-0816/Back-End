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

<form:form modelAttribute="form" cssClass="ui modal small -json-submit" data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/maindb-data/${entity != null ? g.htmlQuote(entity.maindbSysCustomId) : null}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">고객DB${entity != null ? '수정' : '추가'}[그룹명:${g.htmlQuote(customGroup.name)}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <form:hidden path="groupSeq"/>
            <c:forEach var="field" items="${customDbType.fields}">
                <c:set var="name" value="${field.fieldId.substring(customDbType.kind.length() + '_'.length()).toLowerCase()}"/>
                <c:set var="value" value="${fieldNameToValueMap.get(field.fieldId)}"/>
                <div class="row">
                    <div class="four wide column"><label class="control-label">${g.htmlQuote(field.fieldInfo)}</label></div>
                    <c:choose>
                        <c:when test="${field.fieldType == 'MULTICODE'}">
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <div class="field">
                                        <select name="${name}" multiple="multiple" class="ui fluid dropdown">
                                            <c:forEach var="e" items="${field.codes}">
                                                <c:set var="contains" value="${false}"/>
                                                <c:if test="${value != null}">
                                                    <c:forEach var="e2" items="${value.split(',')}">
                                                        <c:if test="${!contains}">
                                                            <c:set var="contains" value="${e2 == e.codeId}"/>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:if>
                                                <option value="${g.htmlQuote(e.codeId)}" ${contains ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${field.fieldType == 'CODE'}">
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <div class="field">
                                        <select name="${name}">
                                            <option value="">선택안함</option>
                                            <c:forEach var="e" items="${field.codes}">
                                                <option value="${g.htmlQuote(e.codeId)}" ${value == e.codeId ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${field.fieldType == 'INT' || field.fieldType == 'NUMBER'}">
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <div class="field">
                                        <input type="text" name="${name}" class="-input-numerical" value="${g.htmlQuote(value)}"/>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${field.fieldType == 'DATETIME'}">
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <input type="text" name="${name}" multiple="multiple" value="${value != null ? g.dateFormat(value) : null}" class="-datepicker" style="width: 130px"/>&ensp;
                                    <input type="text" name="${name}" multiple="multiple" value="${value != null ? value.hours : null}" class="-input-numeric" style="width: 50px"/>시
                                    <input type="text" name="${name}" multiple="multiple" value="${value != null ? value.minutes : null}" class="-input-numeric" style="width: 50px"/>분
                                    <input type="text" name="${name}" multiple="multiple" value="${value != null ? value.seconds : null}" class="-input-numeric" style="width: 50px"/>초
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${field.fieldType == 'DATE' || field.fieldType == 'DAY'}">
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <div class="field">
                                        <input type="text" name="${name}" class="-datepicker" value="${value}"/>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${field.fieldType == 'STRING' && field.fieldSize > 50}">
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <div class="field">
                                        <textarea name="${name}" rows="3" maxlength="${field.fieldSize}">${g.htmlQuote(value)}</textarea>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${field.fieldType == 'IMG'}">
                            <div class="twelve wide column">
                                <div class="thirteen wide column">
                                    <input name="${name}" type="hidden" value="">
                                    <div class="file-upload-header">
                                        <label for="file_${name}" class="ui button blue mini compact">파일찾기</label>
                                        <input type="file" id="file_${name}" data-value="${name}">
                                        <span class="file-name">No file selected</span>
                                    </div>
                                    <div>
                                        <progress value="0" max="100" style="width:100%"></progress>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="four wide column">
                                <div class="ui input fluid">
                                    <input type="text" name="${name}" maxlength="${field.fieldSize}" value="${g.htmlQuote(value)}"/>
                                </div>
                            </div>
                            <c:if test="${field.fieldSize > 0}">
                                <div class="four wide column">
                                    (최대길이:${field.fieldSize} Bytes)
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>

            <div class="row">
                <div class="twelve wide column"><label class="control-label">멀티채널(전화번호,상담톡) 추가</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select name="channels" multiple="multiple" class="one-multiselect">
                            <c:forEach var="c" items="${entity.multichannelList}">
                                <option value="${c.channelData}" data-type="${g.htmlQuote(c.channelType)}">
                                    [${c.channelType == 'TALK' ? g.htmlQuote(talkServices.get(c.channelData.split('[_]')[0])) : g.htmlQuote(channelTypes.get(c.channelType))}]
                                        ${g.htmlQuote(c.channelType == 'TALK' ? c.channelData.split('[_]')[1] : c.channelData)}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -remove-channel">삭제</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <div class="ui form">
                        <select name="channelType">
                            <c:forEach var="e" items="${channelTypes}">
                                <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="five wide column">
                    <div class="ui form flex">
                        <div class="field" style="flex: 1; display: none;">
                            <select name="channelDataTalkService">
                                <c:forEach var="e" items="${talkServices}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="field" style="flex: 1">
                            <input type="text" name="channelData">
                        </div>
                    </div>
                </div>
                <div class="two wide column">
                    <div class="ui form">
                        <button type="button" class="ui button compact -add-channel">추가</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    modal.find('[type="file"]').change(function () {
        const obj = $(this);
        uploadFile(this.files[0], obj.parent().next().find('progress')).done(function (response) {
            const name = obj.attr('data-value');
            obj.next().text(response.data.originalName);
            modal.find('[name=' + name + ']').val(response.data.fileName);
        });
    });

    window.prepareWriteFormData = function (data) {
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
                } else if (data[name] instanceof Array) {
                    data[name] = data[name].join(',');
                }
            }
        }

        data.channels = [];
        modal.find('[name=channels] option').each(function () {
            data.channels.push({
                type: $(this).attr('data-type'),
                value: $(this).val()
            });
        });

        delete data.channelType;
        delete data.channelData;
        delete data.channelDataTalkService;
    };

    modal.find('[name="channelType"]').change(function () {
        const channelType = $(this).val();
        if (channelType === 'TALK') {
            modal.find('[name=channelDataTalkService]').parent().show();
        } else {
            modal.find('[name=channelDataTalkService]').parent().hide();
        }
    }).change();

    modal.find('.-remove-channel').click(function () {
        modal.find('[name=channels] :selected').remove();
    });

    modal.find('.-add-channel').click(function () {
        const channelType = modal.find('[name=channelType] :selected').val();
        const channelTypeName = modal.find('[name=channelType] :selected').text();
        const channelData = modal.find('[name=channelData]').val();
        if (!channelType || !channelData) return;

        const talkServiceSenderKey = modal.find('[name=channelDataTalkService] :selected').val();
        const talkServiceName = modal.find('[name=channelDataTalkService] :selected').text();

        modal.find('[name=channels]').append($('<option/>', {
            value: (channelType === 'PHONE' ? channelData : talkServiceSenderKey + '_' + channelData),
            'data-type': channelType,
            text: '[' + (channelType === 'PHONE' ? channelTypeName : talkServiceName) + '] ' + channelData
        }));
    });

    const CustomInfo = $('#modal-data');

    const DBTYPE_FIELD_PREFIX = 'MAINDB_';

    const fields = {
        <c:forEach var="field" items="${customDbType.fields}">
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

    CustomInfo.find('select').change(function () {
        const selectName = $(this).attr('name');
        if (!selectName)
            return;

        const dbName = convertToDbTypeFieldId(selectName);
        const parentValue = $(this).val();

        if (!fieldToRelatedField[dbName])
            return;

        const relatedField = CustomInfo.find('[name="' + convertToFormFieldId(fieldToRelatedField[dbName]) + '"]');
        const preValue = relatedField.val();

        relatedField.empty()
            .append($('<option/>', {value: '', text: '선택안함'}));
        fields[fieldToRelatedField[dbName]].map(function (o) {
            if (o.value.indexOf(parentValue) !== 0)
                return;

            relatedField.append($('<option/>', {value: o.value, text: o.text}).prop('selected', o.value === preValue));
        });
        relatedField.change();
    });
</script>
