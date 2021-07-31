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

<form id="modal-update-my-password" class="ui modal -json-submit tiny" data-method="patch" data-done="doneUpdateMyPassword" action="<c:url value="/api/user/${g.htmlQuote(user.id)}/password"/>">
    <i class="close icon"></i>
    <div class="header">패스워드변경</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">새로운 비밀번호</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><input id="password" name="password" placeholder="문자/숫자/특수문자 8~20자" type="password" value=""></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">비밀번호 확인</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><input id="passwordConfirm" name="passwordConfirm" type="password" value=""></div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form>

<tags:scripts>
    <script>
        function doneUpdateMyPassword() {
            alert('적용되었습니다.');
            $('#modal-update-my-password').modalHide();
        }

        function popupMyPasswordModal() {
            $('#modal-update-my-password').modalShow();
        }
    </script>
</tags:scripts>
