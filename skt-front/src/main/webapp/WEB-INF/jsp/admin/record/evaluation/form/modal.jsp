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
           action="${pageContext.request.contextPath}/api/evaluation-form/${entity == null ? (copyingEntity != null ? copyingEntity.id : null) : entity.id}${copyingEntity != null ? '/copy' : null}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">평가지[${entity != null ? '수정' : '추가'}]</div>
    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">평가지명</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">진행여부설정</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="useType" items="${useTypes}"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">진행기간</label></div>
                <div class="seven wide column">
                    <div class="date-picker from-to">
                        <div class="dp-wrap">
                            <label class="control-label" for="startDate" style="display:none">From</label>
                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일" autocomplete="off"/>
                        </div>
                        <span class="tilde">~</span>
                        <div class="dp-wrap">
                            <label class="control-label" for="endDate" style="display:none">to</label>
                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일" autocomplete="off"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">메모</label> </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="memo"/>
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
