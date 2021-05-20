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

<%--@elvariable id="entity" type="kr.co.eicn.ippbx.model.dto.eicn.ServiceListDetailResponse"--%>
<%--@elvariable id="numbers" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.NumberSummaryResponse>"--%>
<%--@elvariable id="form" type="kr.co.eicn.ippbx.model.form.ServiceListFormRequest"--%>

<form:form commandName="form" cssClass="ui modal -json-submit" data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/service/${entity != null ? entity.seq : null}" data-done="reload">

    <i class="close icon"></i>
    <div class="header">대표서비스[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">서비스명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="svcName"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">대표번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
						<c:choose>
                            <c:when test="${entity != null}">
                                <form:input path="svcNumber" readonly="true"/>
                            </c:when>
                            <c:otherwise>
                                <form:select path="svcNumber">
                                    <form:option value="" label="선택"/>
                                    <c:if test="${entity != null && !numbers.contains(entity.svcNumber)}">
                                        <form:option value="${entity.svcNumber}" label="${entity.svcNumber}"/>
                                    </c:if>
                                    <c:forEach var="e" items="${numbers}">
                                        <form:option value="${e.number}" label="${e.number}"/>
                                    </c:forEach>
                                </form:select>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">원서비스번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="svcCid" placeholder="1544,1588 등" />
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">서비스레벨</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="serviceLevel"/><span>초</span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${entity != null}">
                                    <c:forEach var="e" items="${entity.organizationSummary}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e.groupName)}</span>
                                        <c:if test="${!status.last}">
                                            <i class="right angle icon divider"></i>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
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
