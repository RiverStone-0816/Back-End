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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/chatbot/event/history/"
           data-done="reload">
    <form:hidden path="botId"/>
    <form:hidden path="botName"/>
    <form:hidden path="userId"/>
    <form:hidden path="userName"/>
    <form:hidden path="userType"/>
    <i class="close icon"></i>
    <div class="header">카카오이벤트보내기</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">이벤트블록선택</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <form:select path="eventName">
                            <c:forEach var="e" items="${eventList}">
                                <form:option value="${e.chatbotId}" label="${e.eventName}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">이벤트전달데이터</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="eventData"/></div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">이벤트전송</button>
    </div>
</form:form>