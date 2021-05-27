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

<form:form modelAttribute="form" cssClass="ui modal large -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/outbound-type/${entity.seq}"
           data-before="prepareUpdateTypeForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">유형수정[${serviceKind.equals('SC') ? 'PDS' : 'AutoIVR'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <form:hidden path="kind"/>
            <form:hidden path="purpose"/>
            <form:hidden path="type"/>
            <div class="row">
                <div class="four wide column"><label class="control-label">유형명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">유형정보</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="etc"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <table class="ui celled table compact unstackable fixed">
                    <thead>
                    <tr>
                        <th class="one wide">-</th>
                        <th>기본필드명</th>
                        <th>필드명</th>
                        <th>필수여부</th>
                        <th>상담원보여줌</th>
                        <th>리스트보여줌</th>
                        <th>검색가능여부</th>
                        <th>글자수(500자이내)<br>(영문한글공통)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="field" items="${fields}" varStatus="fieldStatus">
                        <tr>
                            <td>
                                <input type="hidden" name="field[${fieldStatus.index}].id" value="${field.id}"/>
                                <c:choose>
                                    <c:when test="${defaultFieldIds.contains(field.id)}">
                                        <input type="checkbox" name="field[${fieldStatus.index}].checked" value="${fieldStatus.index}" checked style="display: none;"/>
                                        <label>고정</label>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="ui checkbox">
                                            <input type="checkbox" name="field[${fieldStatus.index}].checked"
                                                   value="${fieldStatus.index}" ${registeredFieldIds.contains(field.id) ? 'checked' : null} />
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${g.htmlQuote(fieldIdToNames.get(field.id))}</td>
                            <td>
                                <div class="ui input fluid">
                                    <input type="text" name="field[${fieldStatus.index}].fieldName" value="${g.htmlQuote(field.fieldName)}"/>
                                </div>
                            </td>
                            <td>
                                <div class="ui form fluid">
                                    <select name="field[${fieldStatus.index}].isneed">
                                        <option value="Y" ${field.isneed == 'Y' ? 'selected' : null}>YES</option>
                                        <option value="N" ${field.isneed == 'N' ? 'selected' : null}>NO</option>
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div class="ui form fluid">
                                    <select name="field[${fieldStatus.index}].isdisplay">
                                        <option value="Y" ${field.isdisplay == 'Y' ? 'selected' : null}>YES</option>
                                        <option value="N" ${field.isdisplay == 'N' ? 'selected' : null}>NO</option>
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div class="ui form fluid">
                                    <select name="field[${fieldStatus.index}].isdisplayList">
                                        <option value="Y" ${field.isdisplayList == 'Y' ? 'selected' : null}>YES</option>
                                        <option value="N" ${field.isdisplayList == 'N' ? 'selected' : null}>NO</option>
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div class="ui form fluid">
                                    <select name="field[${fieldStatus.index}].issearch">
                                        <option value="Y" ${field.issearch == 'Y' ? 'selected' : null}>YES</option>
                                        <option value="N" ${field.issearch == 'N' ? 'selected' : null}>NO</option>
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div class="ui form fluid">
                                    <div class="ui input fluid">
                                        <c:choose>
                                            <c:when test="${fieldIdToTypes.get(field.id) == 'STRING'}">
                                                <input type="text" name="field[${fieldStatus.index}].fieldSize" value="${field.fieldSize}" class="-input-numerical"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" name="field[${fieldStatus.index}].fieldSize" value="${field.fieldSize}" style="display: none;"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    window.prepareUpdateTypeForm = function (data) {
        data.fieldFormRequests = [];

        for (let i in data.field) {
            if (data.field.hasOwnProperty(i)) {
                const field = data.field[i];
                if (!field.checked) continue;

                data.fieldFormRequests.push({
                    id: field.id,
                    fieldName: field.fieldName,
                    fieldSize: field.fieldSize,
                    isneed: field.isneed,
                    isdisplay: field.isdisplay,
                    isdisplayList: field.isdisplayList,
                    issearch: field.issearch
                })
            }
        }

        delete data.field;
    };

    modal.find('input[type="checkbox"][name$=\\.checked]').change(function () {
        const checked = $(this).prop("checked");
        $(this).closest('tr').find('td').each(function (i) {
            if (i <= 1) return;
            $(this).find('[name]').css('visibility', checked ? 'visible' : 'hidden');
        });
    }).change();
</script>
