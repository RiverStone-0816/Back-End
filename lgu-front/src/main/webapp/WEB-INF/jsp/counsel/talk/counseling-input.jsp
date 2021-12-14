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

<form:form id="talk-counseling-input" modelAttribute="form" cssClass="panel -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/maindb-result/"
           data-before="prepareTalkCounselingInfoFormData" data-done="donePostTalkCounselingInfo">

    <div class="panel-heading">
        <label class="control-label">상담정보</label>
        <div class="ui checkbox">
            <div class="ui checkbox">
                <input type="checkbox" name="saveCustomInfo" checked>
                <label>고객정보저장</label>
            </div>
        </div>
        <c:if test="${roomId != null && roomId != ''}">
            <button type="button" class="ui button mini right floated compact blue -submit-form">상담저장</button>
        </c:if>
    </div>
    <div class="panel-body consulting-info-panel">
        <table class="ui table celled definition">
            <tbody>
            <c:if test="${talk != null}">
                <tr>
                    <td class="three wide">진행정보</td>
                    <td>
                        <div class="ui form flex">
                            [${g.htmlQuote(g.messageOf('RoomStatus', talk.roomStatus))}]
                                ${g.timestampFormat(talk.roomStartTime)} ~ ${talk.roomStatus == 'E' ? g.timestampFormat(talk.roomLastTime) : ''}
                        </div>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td class="three wide">대화방명</td>
                <td>
                    <div class="ui form flex">
                        <form:input path="roomName"/>
                    </div>
                </td>
            </tr>
            <form:hidden path="hangupMsg"/>
            <form:hidden path="clickKey"/>
            <form:hidden path="customNumber"/>
            <form:hidden path="groupKind"/>
            <form:hidden path="customId"/>
            <form:hidden path="maindbType"/>
            <form:hidden path="resultType"/>
            <form:hidden path="groupId"/>
            <c:forEach var="field" items="${resultType.fields}">
                <c:set var="name" value="${field.fieldId.substring(resultType.kind.length() + '_'.length()).toLowerCase()}"/>
                <c:set var="value" value=""/>
                <tr>
                    <td class="three wide">${g.htmlQuote(field.fieldInfo)}</td>
                    <td>
                        <div class="ui grid">
                            <c:choose>
                                <c:when test="${field.fieldType == 'MULTICODE'}">
                                    <div class="sixteen wide column">
                                        <div class="ui form flex">
                                            <div style="flex: 1; margin-right: 5px;">
                                                <select name="${name}" id="${name}" data-type="select" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" multiple="multiple" class="ui fluid dropdown">
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
                                            <button type="button" class="ui button mini" onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#talk-counseling-input').find('#${name}').val())">상세</button>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${field.fieldType == 'CODE'}">
                                    <div class="sixteen wide column">
                                        <div class="ui form flex">
                                            <select name="${name}" id="${name}" data-type="select" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}">
                                                <option value=""></option>
                                                <c:forEach var="e" items="${field.codes}">
                                                    <option value="${g.htmlQuote(e.codeId)}" ${value == e.codeId ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                                </c:forEach>
                                            </select>
                                            <button type="button" class="ui button mini" onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#talk-counseling-input').find('#${name}').val())">상세</button>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${field.fieldType == 'INT' || field.fieldType == 'NUMBER'}">
                                    <div class="sixteen wide column">
                                        <div class="ui form fluid">
                                            <div class="field">
                                                <input type="text" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" class="-input-numerical" value="${g.htmlQuote(value)}"/>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${field.fieldType == 'DATETIME'}">
                                    <div class="sixteen wide column">
                                        <div class="ui form fluid" style="text-align: left">
                                            <input type="text" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" multiple="multiple" value="${value != null ? g.dateFormat(value) : null}" class="-datepicker" style="width: 130px"/>&ensp;
                                            <input type="text" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" multiple="multiple" value="${value != '' ? value.hours : null}" class="-input-numeric" style="width: 50px"/><text style="line-height: 30px">시</text>
                                            <input type="text" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" multiple="multiple" value="${value != '' ? value.minutes : null}" class="-input-numeric" style="width: 50px"/><text style="line-height: 30px">분</text>
                                            <input type="hidden" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" multiple="multiple" value="00" class="-input-numeric"/>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${field.fieldType == 'DATE' || field.fieldType == 'DAY'}">
                                    <div class="sixteen wide column">
                                        <div class="ui form fluid">
                                            <div class="field">
                                                <input type="text" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" value="${value}" class="-datepicker"/>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${field.fieldType == 'STRING' && field.fieldSize > 50}">
                                    <div class="sixteen wide column">
                                        <div class="ui form fluid">
                                            <div class="field">
                                                <textarea name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" rows="3" maxlength="${field.fieldSize}">${g.htmlQuote(value)}</textarea>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="eight wide column">
                                        <div class="ui input fluid">
                                            <input type="text" name="${name}" id="${name}" data-type="text" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}" maxlength="${field.fieldSize}" value="${g.htmlQuote(value)}"/>
                                        </div>
                                    </div>
                                    <c:if test="${field.fieldSize > 0}">
                                        <div class="eight wide column" style="line-height: 30px">
                                            (최대길이:${field.fieldSize} Bytes)
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
    </div>
</form:form>

<script>
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

    window.prepareTalkCounselingInfoFormData = function (data) {
        delete data.saveCustomInfo;

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
    };

    window.donePostTalkCounselingInfo = function () {
        const roomId = '${g.escapeQuote(roomId)}'

        alert('상담정보가 저장되었습니다.');
        loadTalkCounselingInput('${form.groupId}', null, roomId, '${g.htmlQuote(channelType)}', '${g.htmlQuote(senderKey)}', '${g.htmlQuote(userKey)}');

        talkListContainer.load().done(function () {
            if (!talkListContainer.roomMap[roomId])
                return
            talkListContainer.openRoom(roomId, talkListContainer.roomMap[roomId].userName)
        })
    };

    ui.find('.-submit-form').click(function () {
        function submit(customId) {
            const customIdInput = ui.find('[name=customId]');
            customIdInput.val(customIdInput.val() || customId || $('#talk-custom-input .-custom-id').text());

            submitJsonData(ui[0]);
        }

        let objText = ui.find('[data-value="Y"]');
        for(let i = 0 ;i < objText.length;i++){
            if(objText[i].getAttribute('data-type') == 'text') {
                if (objText[i].value.trim() == "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                    return;
                }
            }else if(objText[i].getAttribute('data-type') == 'select'){
                if (objText[i].options[objText[i].selectedIndex].value == "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 선택 해 주세요.");
                    return;
                }
            }else{
                if (objText[i].value.trim() == "") {
                    alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                    return;
                }
            }
        }

        if (ui.find('[name="saveCustomInfo"]').is(':checked')) {

            let customInfo = $("#talk-custom-input");
            let customObjText = customInfo.find('[data-value="Y"]');

            for(let i = 0 ;i < customObjText.length;i++){
                if(customObjText[i].getAttribute('data-type') === 'text') {
                    if (customObjText[i].value.trim() === "") {
                        alert("고객정보에서 [" + customObjText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                        return;
                    }
                }else if(customObjText[i].getAttribute('data-type') === 'select'){
                    if (customObjText[i].options[customObjText[i].selectedIndex].value === "") {
                        alert("고객정보에서 [" + customObjText[i].getAttribute('data-text') + "] 을(를) 선택 해 주세요.");
                        return;
                    }
                }else{
                    if (customObjText[i].value.trim() === "") {
                        alert("고객정보에서 [" + customObjText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                        return;
                    }
                }
            }

            submitJsonData($('#talk-custom-input')[0]).done(function (response) {
                submit(response.data);
            });
        } else {
            submit();
        }
    });
</script>
