<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/learn-group/${entity != null ? entity.seq : null}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">학습그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">학습그룹명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="groupName"/></div>
                </div>
            </div>

            <div class="row">
                <div class="eight wide column"><label class="control-label">전사그룹 리스트</label></div>
                <div class="eight wide column"><label class="control-label">학습할 전사그룹리스트</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${groupList}">
                                    <option value="${e.seq}">${g.htmlQuote(e.groupName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="groupList" class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addedGroupList}">
                                    <option value="${e.seq}">${g.htmlQuote(e.groupName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button -button-submit">확인</button>
    </div>
</form:form>

<script>
    modal.find('.-button-submit').click(() => {
        modal.find('.-button-submit').attr("disabled", "true");
        modal.submit();
    })

    window.prepareWriteFormData = function (data) {
        data.groupList = [];
        modal.find('[name="groupList"] option').each(function () {
            data.groupList.push($(this).val());
        });
    };

    modal.find('.-to-left, .-to-right').click(function () {
        strategySelector.change();
    });

    let timer;
    modal.find('.-right-selector option, .-left-selector option').dblclick(function () {
        timer = setTimeout(function(){
            strategySelector.change();
        },150);
    });
</script>
