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

<form id="modal-update-my-password" class="ui modal -json-submit tiny" data-method="patch" data-done="doneUpdateMyPassword" action="<c:url value="/api/user/me/password"/>">
    <i class="close icon"></i>
    <div class="header">패스워드변경</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">현재 비밀번호</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><input id="oldPassword" name="oldPassword" placeholder="문자/숫자/특수문자 8~20자" type="password" value=""></div>
                </div>
            </div>
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
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form>

<tags:scripts>
    <script>
        function doneUpdateMyPassword() {
            alert('패스워드가 적용되어 로그아웃 됩니다. 다시 로그인해주세요.', logout());
            $('#oldPassword').val('');
            $('#password').val('');
            $('#passwordConfirm').val('');
            $('#modal-update-my-password').modalHide();
        }

        function popupMyPasswordModal() {
            $('#modal-update-my-password').modalShow();
        }
    </script>
</tags:scripts>
