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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/chatbot/info/block/${entity == null ? null : entity.seq}"
           data-before="prepareWriteFormData" data-done="reload">
    <i class="close icon"></i>
    <div class="header">오픈빌더블록[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">카카오챗봇</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <form:select path="chatbotId">
                            <form:options items="${talkServiceList}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">블럭명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="blockName"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">블럭아이디</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="blockId"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">답변연동유형</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <form:select path="responseType">
                            <form:options items="${openBuilderType}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">답변연동URL</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="responseGetUrl"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">답변연동파라미터</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="responseParamNames"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">이벤트명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="eventName"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">사용여부</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <form:select path="useYn">
                            <form:option value="Y" label="사용중"/>
                            <form:option value="N" label="비사용"/>
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
    window.prepareWriteFormData = function (data) {
        data.chatbotName = $('#modal-block').find('#chatbotId').text().trim();
    }
</script>