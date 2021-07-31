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

<%--@elvariable id="entity" type="kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupDetailResponse"--%>
<%--@elvariable id="form" type="kr.co.eicn.ippbx.model.form.PickUpGroupFormUpdateRequest"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="put" action="${pageContext.request.contextPath}/api/pickup-group/${entity.groupcode}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">당겨받기그룹 수정</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">당겨받기명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="groupname"/></div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">추가 가능한 사용자</label></div>
                <div class="eight wide column"><label class="control-label">추가된 사용자</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.addOnPersons}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.extension)}[${g.htmlQuote(e.hostName)}][${g.htmlQuote(e.pickupName)}]-${g.htmlQuote(e.idName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="peers" class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.addPersons}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.extension)}[${g.htmlQuote(e.hostName)}]-${g.htmlQuote(e.idName)}</option>
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
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.peers = [];
        modal.find('[name="peers"] option').each(function () {
            data.peers.push($(this).val());
        });
    };
</script>
