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
           action="${pageContext.request.contextPath}/api/pds-ivr/${entity.code}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">매핑</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="five wide column"><label class="control-label">IVR명</label></div>
                <div class="eleven wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="five wide column"><label class="control-label">음원선택</label></div>
                <div class="eleven wide column">
                    <div class="ui form fluid">
                        <form:select path="soundCode" items="${sounds}"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <table class="ui table celled compact unstackable">
                    <thead>
                    <tr>
                        <th class="">버튼</th>
                        <th class="five wide column">이름</th>
                        <th class="five wide column">연결설정</th>
                        <th class="five wide column">연결데이터</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="key" items="${['0','1','2','3','4','5','6','7','8','9','*','#']}">
                        <input type="hidden" name="buttonMappingFormRequests.${key}.seq" value="${buttonMap.get(key).seq}"/>
                        <tr>
                            <td>
                                <input type="hidden" name="buttonMappingFormRequests.${key}.button" value="${key}"/>
                                    ${key}
                            </td>
                            <td>
                                <div class="ui form">
                                    <input type="text" name="buttonMappingFormRequests.${key}.name" value="${buttonMap.get(key).name}"/>
                                </div>
                            </td>
                            <td>
                                <div class="ui form">
                                    <select name="buttonMappingFormRequests.${key}.type" data-key="${key}" class="-select-type">
                                        <c:forEach var="e" items="${types}">
                                            <option value="${e.key}" ${buttonMap.get(key).type == e.key ? 'selected' : null}>${g.htmlQuote(e.value)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </td>
                            <td>
                                <div class="ui form">
                                    <select name="buttonMappingFormRequests.${key}.typeData.2" class="-type-data" data-key="${key}" data-type="2">
                                        <c:forEach var="e" items="${queues}">
                                            <option value="${e.key}" ${buttonMap.get(key).typeData == "".concat(e.key) ? 'selected' : null}>${g.htmlQuote(e.value)}</option>
                                        </c:forEach>
                                    </select>
                                    <select name="buttonMappingFormRequests.${key}.typeData.8" class="-type-data" data-key="${key}" data-type="8">
                                        <c:forEach var="e" items="${sounds}">
                                            <option value="${e.key}" ${buttonMap.get(key).typeData == "".concat(e.key) ? 'selected' : null}>${g.htmlQuote(e.value)}</option>
                                        </c:forEach>
                                    </select>
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
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        const buttonMappingFormRequests = data.buttonMappingFormRequests;
        data.buttonMappingFormRequests = [];

        for (let key in buttonMappingFormRequests) {
            if (buttonMappingFormRequests.hasOwnProperty(key)) {
                const o = buttonMappingFormRequests[key];
                if (!o.name) continue;

                data.buttonMappingFormRequests.push({seq: o.seq, name: o.name, button: key, type: o.type, typeData: o.typeData[o.type]});
            }
        }
    };

    modal.find('.-select-type').change(function () {
        const key = $(this).attr('data-key');
        const type = $(this).val();

        modal.find('.-type-data').filter(function () {
            return $(this).attr('data-key') === key;
        }).hide().filter(function () {
            return $(this).attr('data-type') === type;
        }).show();
    }).change();

</script>
