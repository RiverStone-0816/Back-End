<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form commandName="form" cssClass="ui modal tiny -json-submit" data-method="post"
           data-action="${pageContext.request.contextPath}/api/callback-distribution/service/"
           data-before="prepareHuntDistributionForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">대표번호 큐 설정</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">서비스</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select name="svcNumber">
                            <option value="">선택안함</option>
                            <c:forEach var="e" items="${services}">
                                <option value="${e.key}">${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">큐 리스트</label></div>
                <div class="eight wide column"><label class="control-label">등록된 큐</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select id="addHunts" class="form-control -right-selector" size="8" multiple="multiple">
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select id="addedHunts" name="hunts" class="form-control -left-selector" size="8" multiple="multiple"></select>
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
    window.prepareHuntDistributionForm = function (data) {
        data.hunts = [];
        modal.find('[name="hunts"] option').each(function () {
            data.hunts.push($(this).val());
        });

        modal.attr('action', modal.attr('data-action') + data.svcNumber + '/hunts');
        delete data.svcNumber;
    };

   modal.find('[name=svcNumber]').change(function () {
       const e = event.target;
       const value = e.value;
       const addableHuntList = JSON.parse('${addableHunts}')[value];
       const addHuntSelect = modal.find('#addHunts');

       addHuntSelect.empty();

        for (let key in addableHuntList) {
            if (addableHuntList.hasOwnProperty(key)) {
                const option = $('<option/>', {value: key, text: key + '[' + addableHuntList[key] + ']'});
                if (key === value)
                    addHuntSelect.prepend(option);
                else
                    addHuntSelect.append(option);
            }
        }

       const addedHuntList = JSON.parse('${addedHunts}')[value];

       const addedHuntSelect = modal.find('#addedHunts');

       addedHuntSelect.empty();

       for (let key in addedHuntList) {
           if (addedHuntList.hasOwnProperty(key)) {
               const option = $('<option/>', {value: key, text: key + '[' + addedHuntList[key] + ']'});
               if (key === value)
                   addedHuntSelect.prepend(option);
               else
                   addedHuntSelect.append(option);
           }
       }
    });
</script>
