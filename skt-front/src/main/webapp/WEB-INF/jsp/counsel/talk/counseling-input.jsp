<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form id="talk-counseling-input" modelAttribute="form" cssClass="-json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/maindb-result/"
           data-before="prepareTalkCounselingInfoFormData" data-done="donePostTalkCounselingInfo">

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
            <div class="panel-title-info">
                <c:if test="${talk != null}">
                    <span class="label">진행정보</span>
                    <span><c:if test="${talk.roomStatus != null && talk.roomStatus != ''}">[${g.htmlQuote(g.messageOf('RoomStatus', talk.roomStatus))}] </c:if>${g.timestampFormat(talk.roomStartTime)} ~ ${talk.roomStatus == 'E' ? g.timestampFormat(talk.roomLastTime) : ''}</span>
                </c:if>
                <span class="label">대화방명</span>
                <span><form:input path="roomName"/></span>
            </div>
        </div>
        <div class="pull-right">
            <c:if test="${roomId != null && roomId != ''}">
                <button type="button" class="ui button sharp light -submit-form"><img src="<c:url value="/resources/images/save.svg"/>" alt="save">상담수정</button>
            </c:if>
        </div>
    </div>

    <div class="panel-body overflow-overlay">
        <form:hidden path="clickKey" id="talk-counseling-input-clickKey"/>
        <form:hidden path="hangupMsg" id="talk-counseling-input-hangupMsg"/>
        <form:hidden path="customNumber" id="talk-counseling-input-customNumber"/>
        <form:hidden path="groupKind" id="talk-counseling-input-groupKind"/>
        <form:hidden path="customId" id="talk-counseling-input-customId"/>
        <form:hidden path="maindbType" id="talk-counseling-input-maindbType"/>
        <form:hidden path="resultType" id="talk-counseling-input-resultType"/>
        <form:hidden path="groupId" id="talk-counseling-input-groupId"/>

        <div class="float-field-wrap">
            <c:forEach var="field" items="${resultType.fields}" varStatus="status">
                <c:set var="name" value="${field.fieldId.substring(resultType.kind.length() + '_'.length()).toLowerCase()}"/>
                <c:set var="value" value=""/>

                <c:choose>
                    <c:when test="${field.fieldType == 'MULTICODE'}">
                        <div class="float-field fluid">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form flex">
                                    <select name="${name}" id="talk-counseling-input-${name}" data-type="select"
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
                                            onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#talk-counseling-input').find('[name=${name}]').val())">TIP
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${field.fieldType == 'CODE'}">
                        <div class="float-field inline">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form flex">
                                    <select name="${name}" id="talk-counseling-input-${name}" data-type="select" data-text="${g.htmlQuote(field.fieldInfo)}" data-value="${field.isneed}">
                                        <option value=""></option>
                                        <c:forEach var="e" items="${field.codes}">
                                            <option value="${g.htmlQuote(e.codeId)}" ${value == e.codeId ? 'selected' : ''}>${g.htmlQuote(e.codeName)}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="button" class="ui button sharp navy ml5"
                                            onclick="popupFieldInfo(${field.type}, '${g.htmlQuote(field.fieldId)}', $('#talk-counseling-input').find('[name=${name}]').val())">TIP
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${field.fieldType == 'INT' || field.fieldType == 'NUMBER'}">
                        <div class="float-field inline">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form">
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" class="-input-numerical"
                                           value="${g.htmlQuote(value)}"/>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${field.fieldType == 'DATETIME'}">
                        <div class="float-field inline">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form flex">
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" multiple="multiple"
                                           value="${value != null ? g.dateFormat(value) : null}"
                                           class="-datepicker" style="width: 130px"/>&ensp;
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" multiple="multiple"
                                           value="${value != null ? value.hours : null}"
                                           class="-input-numeric" style="width: 50px"/>
                                    <text style="line-height: 30px">시</text>
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" multiple="multiple"
                                           value="${value != null ? value.minutes : null}"
                                           class="-input-numeric" style="width: 50px"/>
                                    <text style="line-height: 30px">분</text>
                                    <input type="hidden" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" multiple="multiple"
                                           value="00" class="-input-numeric"/>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${field.fieldType == 'DATE' || field.fieldType == 'DAY'}">
                        <div class="float-field inline">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form">
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" value="${value}"
                                           class="-datepicker"/>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${field.fieldType == 'STRING' && field.fieldSize > 50}">
                        <div class="float-field fluid">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form">
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}"
                                           maxlength="${field.fieldSize}" value="${g.escapeQuote(value)}"/>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${field.fieldType == 'IMG'}">
                        <div class="float-field inline">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui form flex">
                                    <input name="${name}" type="hidden" value="">
                                    <div class="file-upload-header">
                                        <label for="file" class="ui button brand mini compact">파일찾기</label>
                                        <input type="file" id="file" data-value="${name}">
                                        <span class="file-name">No file selected</span>
                                    </div>
                                        <%--<div>
                                            <progress value="0" max="100" style="width:100%"></progress>
                                        </div>--%>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="float-field inline">
                            <div class="label"><label for="talk-counseling-input-${name}">${g.htmlQuote(field.fieldInfo)}</label></div>
                            <div class="content">
                                <div class="ui input fluid">
                                    <input type="text" name="${name}" id="talk-counseling-input-${name}" data-type="text"
                                           data-text="${g.htmlQuote(field.fieldInfo)}"
                                           data-value="${field.isneed}" maxlength="${field.fieldSize}"
                                           value="${g.htmlQuote(value)}" placeholder="${field.fieldSize > 0 ? '최대길이:'.concat(field.fieldSize).concat(' Bytes') : ''}"/>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>


            </c:forEach>
        </div>
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
        alert('상담정보가 저장되었습니다.');
        loadTalkCounselingInput('${form.groupId}', null, '${g.htmlQuote(roomId)}', '${g.htmlQuote(senderKey)}', '${g.htmlQuote(userKey)}');
        loadTalkList('MY');
        replaceReceivedHtmlInSilence('/counsel/talk/room/' + encodeURIComponent('${g.escapeQuote(roomId)}'), '#talk-room');

        if (!$('#talk-room').attr('data-user')) {
            loadTalkList('TOT');
        } else if ($('#talk-room').attr('data-user') !== userId) {
            loadTalkList('OTH');
        }
    };

    ui.find('.-submit-form').click(function () {
        function submit(customId) {
            const customIdInput = ui.find('[name=customId]');
            customIdInput.val(customIdInput.val() || customId || $('#talk-custom-input .-custom-id').text());

            submitJsonData(ui[0]);
        }

        let objText = ui.find('[data-value="Y"]');
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

        if (ui.find('[name="saveCustomInfo"]').is(':checked')) {

            let customInfo = $("#talk-custom-input");
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

            submitJsonData($('#talk-custom-input')[0]).done(function (response) {
                submit(response.data);
            });
        } else {
            submit();
        }
    });
</script>
