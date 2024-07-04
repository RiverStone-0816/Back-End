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

<form:form id="call-custom-input" modelAttribute="form" cssClass="panel -json-submit" data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/maindb-data/${entity != null ? g.htmlQuote(entity.maindbSysCustomId) : null}"
           data-before="prepareCustomInfoFormData" data-done="donePostCustomInfo">
    <div class="panel-heading">
        <label class="control-label">고객정보</label>
        <c:if test="${vip}">
            <span class="ui blue basic label mini compact sparkle-blue">VIP</span>
        </c:if>
        <c:if test="${blacklist}">
            <span class="ui red basic label mini compact sparkle-red">블랙리스트</span>
        </c:if>
        <c:if test="${serviceKind.equals('SC')}">
            <button type="button" class="ui button mini compact" onclick="popupArsModal()">ARS</button>
            <button type="button" class="ui button mini compact" onclick="popupGradeAppModal()">등급관리</button>
                <%--CMS를 사용하는 고객인 경우에만 주석 해제--%>
<%--            <button type="button" class="ui button mini compact" onclick="popupCmsModal()">CMS</button>--%>
        </c:if>
        <button type="button" class="ui button mini right floated compact blue"
                id="call-submitButton">${entity != null ? '고객수정' : '신규등록'}</button>
        <button type="button" class="ui button mini right floated compact" onclick="popupSearchMaindbCustomModal()">
            고객검색
        </button>
    </div>
    <div class="panel-body">
        <div style="height: 100%;">
            <table class="ui table celled definition">
                <tbody>
                <tr>
                    <td class="three wide">고객DB</td>
                    <td>
                        <div class="ui form">
                            <div class="two fields">
                                <div class="field">
                                    <select onchange="loadCustomInput(${form.groupSeq}, $(this).val() === 'insert' ? null : '${entity != null ? g.htmlQuote(entity.maindbSysCustomId) : ''}', '${g.htmlQuote(phoneNumber)}')">
                                        <option value="insert" ${entity == null ? 'selected' : ''}>새로운 고객으로 등록</option>
                                        <option value="update" ${entity != null ? 'selected' : ''}>고객정보 갱신</option>
                                    </select>
                                </div>
                                <div class="field">
                                    <select onchange="loadCustomInput($(this).val(), null, '${g.htmlQuote(phoneNumber)}')">
                                        <c:forEach var="e" items="${maindbGroups}">
                                            <option value="${e.seq}" ${e.seq == form.groupSeq ? 'selected' : ''}>${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <c:if test="${entity != null}">
                    <div style="display: none;" class="-custom-id">${g.htmlQuote(entity.maindbSysCustomId)}</div>
                </c:if>
                <form:hidden path="groupSeq"/>
                <c:forEach var="field" items="${customDbType.fields}">
                    <c:set var="name"
                           value="${field.fieldId.substring(customDbType.kind.length() + '_'.length()).toLowerCase()}"/>
                    <c:set var="value" value="${fieldNameToValueMap.get(field.fieldId)}"/>
                    <tr>
                        <td class="three wide">${g.htmlQuote(field.fieldInfo)}</td>
                        <td>
                            <div class="ui grid">
                                <c:choose>
                                    <c:when test="${field.fieldType == 'MULTICODE'}">
                                        <div class="sixteen wide column">
                                            <div class="ui form flex">
                                                <div style="flex: 1; margin-right: 5px;">
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
                                                </div>
                                                <button type="button" class="ui button mini"
                                                        onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#call-custom-input').find('#${name}').val())">상세
                                                </button>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${field.fieldType == 'CODE'}">
                                        <div class="eight wide column">
                                            <div class="ui form flex">
                                                <div style="flex: 1; margin-right: 5px;">
                                                    <select name="${name}" id="${name}" data-type="select" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}">
                                                        <option value=""></option>
                                                        <c:forEach var="e" items="${field.codes}">
                                                            <c:if test="${e.hide == 'N'}">
                                                                <option value="${g.htmlQuote(e.codeId)}" ${value == e.codeId ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                                            </c:if>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <button type="button" class="ui button mini" onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#call-custom-input').find('#${name}').val())">상세</button>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${field.fieldType == 'INT' || field.fieldType == 'NUMBER'}">
                                        <div class="sixteen wide column">
                                            <div class="ui form fluid">
                                                <div class="field">
                                                    <input type="text" name="${name}" id="${name}" data-type="text"
                                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                                           data-value="${field.isneed}" class="-input-numerical"
                                                           value="${g.htmlQuote(value)}"/>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${field.fieldType == 'DATETIME'}">
                                        <div class="sixteen wide column">
                                            <div class="ui form fluid" style="text-align: left">
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
                                        </div>
                                    </c:when>
                                    <c:when test="${field.fieldType == 'DATE' || field.fieldType == 'DAY'}">
                                        <div class="sixteen wide column">
                                            <div class="ui form fluid">
                                                <div class="field">
                                                    <input type="text" name="${name}" id="${name}" data-type="text"
                                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                                           data-value="${field.isneed}" value="${value}"
                                                           class="-datepicker"/>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${field.fieldType == 'STRING' && field.fieldSize > 50}">
                                        <div class="sixteen wide column">
                                            <div class="ui form fluid">
                                                <div class="field">
                                                    <textarea name="${name}" id="${name}" rows="3" data-type="text"
                                                              data-text="${g.htmlQuote(field.fieldInfo)}"
                                                              data-value="${field.isneed}"
                                                              maxlength="${field.fieldSize}">${g.htmlQuote(value)}</textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${field.fieldType == 'IMG'}">
                                        <div class="sixteen wide column" style="text-align: left">
                                            <div class="thirteen wide column">
                                                <input name="${name}" type="hidden" value="">
                                                <div class="file-upload-header">
                                                    <label for="file" class="ui button blue mini compact">파일찾기</label>
                                                    <input type="file" id="file" data-value="${name}">
                                                    <span class="file-name">No file selected</span>
                                                </div>
                                                <div>
                                                    <progress value="0" max="100" style="width:100%"></progress>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="eight wide column">
                                            <div class="ui input fluid">
                                                <input type="text" name="${name}" id="${name}" data-type="text"
                                                       data-text="${g.htmlQuote(field.fieldInfo)}"
                                                       data-value="${field.isneed}" maxlength="${field.fieldSize}"
                                                       value="${g.htmlQuote(value)}"/>
                                            </div>
                                        </div>
                                        <c:if test="${field.fieldSize > 0}">
                                            <div class="eight wide column">
                                                <div class="ui input fluid" style="line-height: 30px">
                                                    (최대길이:${field.fieldSize} Bytes)
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="ui grid" style="margin-top: 2em;">
                <div class="row">
                    <div class="twelve wide column"><label class="control-label">멀티채널(전화번호,상담톡) 추가</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select name="channels" multiple="multiple" class="one-multiselect channels">
                                <c:forEach var="channel" items="${entity.multichannelList}">
                                    <option value="${channel.channelData}" data-type="${g.htmlQuote(channel.channelType)}">
                                        [${channel.channelType == 'TALK' ? g.htmlQuote(talkServices.get(channel.channelData.split('[-]')[0])) : g.htmlQuote(channelTypes.get(channel.channelType))}]
                                            ${g.htmlQuote(channel.channelData)}
                                    </option>
                                </c:forEach>
                                <c:if test="${phoneNumber != null && phoneNumber != '' && (entity == null || entity.multichannelList.stream().map(e -> e.channelData == phoneNumber).count() == 0)}">
                                    <option value="${g.htmlQuote(phoneNumber)}" data-type="PHONE">
                                        [${g.htmlQuote(channelTypes.get('PHONE'))}] ${g.htmlQuote(phoneNumber)}</option>
                                </c:if>
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
                                <c:forEach var="channel" items="${channelTypes}">
                                    <option value="${g.htmlQuote(channel.key)}">${g.htmlQuote(channel.value)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="five wide column">
                        <div class="ui form flex">
                            <div class="field" style="flex: 1; display: none;">
                                <select name="channelDataTalkService">
                                    <c:forEach var="talk" items="${talkServices}">
                                        <option value="${g.htmlQuote(talk.key)}">${g.htmlQuote(talk.value)}</option>
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

    <c:if test="${phoneNumberData != ''}">
        phoneNumber = '${phoneNumberData}';
        $('#calling-number').val('${phoneNumberData}');
    </c:if>

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


    $('#call-submitButton').click(function () {
        let objText = $('#call-custom-input [data-value="Y"]');
        for (let i = 0; i < objText.length; i++) {
            if (objText[i].getAttribute('data-type') == 'text') {
                if (objText[i].value.trim() == "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                    return;
                }
            } else if (objText[i].getAttribute('data-type') == 'select') {
                if (objText[i].options[objText[i].selectedIndex].value == "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 선택 해 주세요.");
                    return;
                }
            } else {
                if (objText[i].value.trim() == "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                    return;
                }
            }
        }
        $('#call-custom-input').submit();
    });

    window.prepareCustomInfoFormData = function (data) {
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
        ui.find('[name=channels] option').each(function () {
            data.channels.push({
                type: $(this).attr('data-type'),
                value: $(this).val()
            });
        });

        delete data.channelType;
        delete data.channelData;
        delete data.channelDataTalkService;
    };

    window.donePostCustomInfo = function (form, response) {
        alert('고객정보가 저장되었습니다.');
        loadCustomInput('${form.groupSeq}', response.data || '${entity != null ? g.htmlQuote(entity.maindbSysCustomId) : ''}', '${g.htmlQuote(phoneNumber)}');
    };

    ui.find('[name="channelType"]').change(function () {
        const channelType = $(this).val();
        if (channelType === 'TALK') {
            ui.find('[name=channelDataTalkService]').parent().show();
        } else {
            ui.find('[name=channelDataTalkService]').parent().hide();
        }
    }).change();

    ui.find('.-remove-channel').click(function () {
        ui.find('[name=channels] :selected').remove();
    });

    ui.find('.-add-channel').click(function () {
        const channelType = ui.find('[name=channelType] :selected').val();
        const channelTypeName = ui.find('[name=channelType] :selected').text();
        const channelData = ui.find('[name=channelData]').val();
        if (!channelType || !channelData) return;

        const talkServiceSenderKey = ui.find('[name=channelDataTalkService] :selected').val();
        const talkServiceName = ui.find('[name=channelDataTalkService] :selected').text();

        let transChannelData = channelData;
        if (channelType === 'PHONE') {
            transChannelData = channelData.replaceAll(/[^0-9]/g, '');
            if (!transChannelData || !transChannelData) return;
        }
        else if (channelType === 'EMAIL' && !transChannelData.includes("@"))
            return;
        else if (channelType === 'TALK')
            transChannelData = talkServiceSenderKey + '_' + channelData;


        ui.find('[name=channels]').append($('<option/>', {
            'data-type': channelType,
            value: transChannelData,
            text: '[' + (channelType === 'TALK' ? talkServiceName : channelTypeName) + '] ' + transChannelData
        }));
    });

    <c:if test="${entity != null}">
        loadCounselingList('${g.escapeQuote(entity.maindbSysCustomId)}');
    </c:if>
    loadCounselingInput('${form.groupSeq}', '${entity != null ? g.htmlQuote(entity.maindbSysCustomId) : ''}', '${g.htmlQuote(phoneNumber)}', '${not empty maindbResultSeq ? maindbResultSeq : ''}');

    function prepareMaindbCodeSelect() {
        <c:forEach var="e" items="${fieldNameToValueMap}">
        <c:if test="${e.key.contains('MAINDB_CODE')}">
        ui.find('[name=${e.key.substring(customDbType.kind.length() + '_'.length()).toLowerCase()}]').change();
        </c:if>
        </c:forEach>
    }

    prepareMaindbCodeSelect();
</script>
