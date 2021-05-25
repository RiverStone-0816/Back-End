<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/record-history/record-batch-download-register"
           data-before="prepareWriteFormData" data-done="doneWriteFormData">

    <i class="close icon"></i>
    <div class="header">일괄다운로드 등록</div>

    <div class="content rows">
        <c:forEach var="e" items="${form.sequences}" varStatus="status">
            <form:hidden path="sequences[${status.index}]"/>
        </c:forEach>
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">다운로드 이름</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="downName"/>
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
    window.prepareWriteFormData = function (data) {
        data.sequences = values(data.sequences);
    };

    window.doneWriteFormData = function () {
        alert('반영되었습니다.');
        modal.modalHide();
    };
</script>
