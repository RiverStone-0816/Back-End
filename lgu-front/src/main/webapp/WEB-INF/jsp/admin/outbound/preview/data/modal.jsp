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
           action="${pageContext.request.contextPath}/api/preview-data/${entity != null ? g.htmlQuote(entity.prvSysCustomId) : null}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">프리뷰 데이터 ${entity != null ? '수정' : '추가'} [그룹명:${g.htmlQuote(previewGroup.name)}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <form:hidden path="groupSeq"/>
            <c:forEach var="field" items="${previewType.fields}">
                <c:set var="name" value="${field.fieldId.substring(previewType.kind.length() + '_'.length()).toLowerCase()}"/>
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
                                            <option value=""></option>
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
                                        <input type="text" name="${name}" class="-datepicker"/>
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
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
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
    };

    const prvCustomInfo = $('#modal-data');

    const DBTYPE_FIELD_PREFIX = 'PRV_';

    const fields = {
        <c:forEach var="field" items="${previewType.fields}">
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

    prvCustomInfo.find('select').change(function () {
        const selectName = $(this).attr('name');
        if (!selectName)
            return;

        const dbName = convertToDbTypeFieldId(selectName);
        const parentValue = $(this).val();
        if (!fieldToRelatedField[dbName])
            return;

        const relatedField = prvCustomInfo.find('[name="' + convertToFormFieldId(fieldToRelatedField[dbName]) + '"]');
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
</script>
