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

<form:form modelAttribute="form" class="ui modal small -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/pds-ivr/"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">IVR[추가]</div>

    <div class="content rows">
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
                <div class="five wide column">
                    <label class="control-label">음원선택</label>
                </div>
                <div class="eleven wide column">
                    <div class="ui form">
                        <form:select path="soundCode" items="${sounds}"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">저장</button>
    </div>
</form:form>
