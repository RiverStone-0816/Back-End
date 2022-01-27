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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/chatbot/${id == null ? null : (''.concat(id).concat('/fallback'))}"
           data-before="prepareFallbackData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">봇 [${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">봇이름</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">멘트입력</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="fallbackMent" rows="3"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">고객입력</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="enableCustomerInput">
                            <form:option value="${true}" label="예"/>
                            <form:option value="${false}" label="아니요"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">동작선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="fallbackAction">
                            <form:option value="FIRST" label="처음으로 가기"/>
                            <form:option value="CONNECT_BLOCK" label="다른 블록으로 연결"/>
                            <form:option value="CONNECT_MEMBER" label="상담그룹 연결"/>
                            <form:option value="CONNECT_URL" label="URL 연결"/>
                            <form:option value="CONNECT_PHONE" label="전화 연결"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row fallback-action" data-value="CONNECT_BLOCK">
                <div class="four wide column"><label class="control-label">연결 블록 설정</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="nextBlockId" items="${blocks}"/>
                    </div>
                </div>
            </div>
            <div class="row fallback-action" data-value="CONNECT_MEMBER">
                <div class="four wide column"><label class="control-label">상담그룹</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="nextGroupId">
                            <c:forEach var="e" items="${talkReceptionGroups}">
                                <form:option value="${e.groupId}" label="${e.groupName}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row fallback-action" data-value="CONNECT_URL">
                <div class="four wide column"><label class="control-label">연결 URL 설정</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="nextUrl" placeholder="URL을 입력하세요."/>
                    </div>
                </div>
            </div>
            <div class="row fallback-action" data-value="CONNECT_PHONE">
                <div class="four wide column"><label class="control-label">연결 번호 설정</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="nextPhone" placeholder="전화번호를 입력하세요."/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareFallbackData = data => {
        const fallbackActions = {FIRST: 'first', CONNECT_MEMBER: 'member', CONNECT_URL: 'url', CONNECT_BLOCK: 'block', CONNECT_PHONE: 'phone'}
        data.fallbackAction = fallbackActions[data.fallbackAction]

        data.blockInfo = {id: 0, posX: 100, posY: 100, name: '', isTemplateEnable: false,}
    }

    modal.find('[name=fallbackAction]').change(function () {
        const value = $(this).find('option:selected').val()
        modal.find('.fallback-action').hide()
            .filter(function () {
                return $(this).attr('data-value') === value
            }).show()
    }).change()
</script>
