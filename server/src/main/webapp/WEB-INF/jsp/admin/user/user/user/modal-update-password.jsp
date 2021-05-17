<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.server.model.form.PersonPasswordUpdateRequest"--%>

<form:form commandName="form" cssClass="ui modal -json-submit" data-method="patch" action="${pageContext.request.contextPath}/api/user/${g.htmlQuote(id)}/password" data-done="doneUpdatePassword">
    <i class="close icon"></i>
    <div class="header">패스워드변경</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">새로운 비밀번호</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><form:password path="password" placeholder="문자/숫자/특수문자 8~20자"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">비밀번호 확인</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><form:password path="passwordConfirm"/></div>
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
    window.doneUpdatePassword = function () {
        alert('적용되었습니다.');
        modal.find('.modal-close:first').click();
    };
</script>