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

<form:form commandName="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/email/${entity == null ? null : entity.seq}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">이메일[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid form">
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">공통 정보</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">이메일서비스명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="serviceName"/></div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">받는 메일 정보</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">대표메일계정</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="mailUserName"/></div>
                </div>
                <div class="four wide column"><label class="control-label">메일계정비밀번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:password path="mailUserPasswd"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">에러시알림메일계정</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="mailErrorNoticeEmail"/></div>
                </div>
                <div class="four wide column"><label class="control-label">보고자하는메일계정</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="mailViewEmail"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">메일프로토콜</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="mailProtocol">
                            <form:option value="SMTP" label="SMTP"/>
                            <form:option value="POP3" label="POP3"/>
                            <form:option value="IMAP" label="IMAP"/>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">메일SSL여부</label></div>
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
                <div class="four wide column"><label class="control-label">메일호스트/포트</label></div>
                <div class="four wide column">
                    <div class="fields remove-mb">
                        <div class="ten wide field"><form:input path="mailHost" placeholder="메일호스트"/></div>
                        <div class="six wide field"><form:input path="mailPort" placeholder="포트"/></div>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">첨부저장경로</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="mailAttachPath"/></div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">보내는 메일 정보</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">보내는메일계정</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="sendUserName"/></div>
                </div>
                <div class="four wide column"><label class="control-label">보내는메일계정비밀번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="sendUserPasswd"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">보내는메일</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="sendEmail"/></div>
                </div>
                <div class="four wide column"><label class="control-label">보내는메일명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="sendEmailName"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">보내는메일호스트/포트</label></div>
                <div class="four wide column">
                    <div class="fields remove-mb">
                        <div class="ten wide field"><form:input path="sendHost" placeholder="메일호스트"/></div>
                        <div class="six wide field"><form:input path="sendPort" placeholder="포트"/></div>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">암호화된연결방식</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="sendAuthConnType">
                            <form:option value="TLS" label="TLS"/>
                            <form:option value="SSL" label="SSL"/>
                        </form:select>
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

</script>
