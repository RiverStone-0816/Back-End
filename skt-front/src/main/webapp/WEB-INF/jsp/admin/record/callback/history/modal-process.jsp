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
           action="${pageContext.request.contextPath}/api/callback-history/"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">콜백 처리</div>

    <div class="scrolling content">
        <div class="ui form">
            <div class="inline fields">
                <label>선택된 콜백 : [${form.serviceSequences.size()}건]</label>
                <c:forEach var="e" items="${form.serviceSequences}">
                    <input type="hidden" name="serviceSequences" value="${e}" multiple="multiple"/>
                </c:forEach>

                <c:forEach var="e" items="${callbackStatusOptions}">
                    <div class="field">
                        <div class="ui radio checkbox">
                            <form:radiobutton path="status" value="${e.key}"/>
                            <label>${g.htmlQuote(e.value)}</label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>
