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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/talk-service/${entity == null ? null : entity.seq}" data-done="reload">

    <i class="close icon"></i>
    <div class="header">플러스친구정보[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">상담톡서비스명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="kakaoServiceName"/></div>
                </div>
                <div class="three wide column"><label class="control-label">상담톡아이디</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="kakaoServiceId"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">상담톡키</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="senderKey"/></div>
                </div>
                <div class="three wide column"><label class="control-label">상담톡활성화</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox checked">
                                    <form:radiobutton path="isChattEnable" class="hidden" value="Y"/>
                                    <label>활성화(Y)</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isChattEnable" class="hidden" value="N"/>
                                    <label>비활성화(N)</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">챗봇명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="botName"/></div>
                </div>
                <div class="three wide column"><label class="control-label">챗봇아이디</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="botId"/></div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>
