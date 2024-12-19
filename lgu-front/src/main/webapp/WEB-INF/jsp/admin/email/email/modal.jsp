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

<form:form modelAttribute="form" cssClass="ui modal large -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/email/${entity == null ? null : entity.seq}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">이메일[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid form">
            <div class="row">
                <div class="four wide column"><label class="control-label">이메일서비스명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="serviceName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">수신 메일 정보</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">수신 접속 계정</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="mailUserName" placeholder="aaa@bbb.ccc"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">수신 접속 계정 비밀번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:password path="mailUserPasswd"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">수신 메일 프로토콜</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="mailProtocol" items="${MailProtocolTypes}"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">메일 SSL 여부</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="mailSslYn">
                            <form:option value="Y" label="사용"/>
                            <form:option value="N" label="사용안함"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">수신 메일 호스트/포트</label></div>
                <div class="eight wide column remove-pr">
                    <div class="ui input fluid">
                        <form:input path="mailHost" placeholder="수신 메일 호스트"/>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="mailPort" cssClass="-input-numerical" placeholder="수신 포트"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">에러 알림 메일</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="mailErrorNoticeEmail" placeholder="xxx@yyy.zzz"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">발신 메일 정보</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">발신 접속 계정</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="sendUserName" placeholder="aaa@bbb.ccc"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">발신 접속 계정 비밀번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:password path="sendUserPasswd"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">발신 시 메일</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="sendEmail" placeholder="aaa@bbb.ccc"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">발신 시 메일명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="sendEmailName" placeholder="고객센터"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">발신 메일 호스트/포트</label></div>
                <div class="eight wide column remove-pr">
                    <div class="ui input fluid">
                        <form:input path="sendHost" placeholder="발신 메일 호스트"/>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="sendPort" cssClass="-input-numerical" placeholder="발신 포트"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">암호화 연결방식</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="sendAuthConnType" items="${SendAuthConnTypes}"/>
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
    modal.find('#sendUserName').on('input', function () {
        let value = $(this).val();

        if (!modal.find('#sendEmail').val() || value.includes(modal.find('#sendEmail').val()))
            modal.find('#sendEmail').val(value);
    });
</script>
