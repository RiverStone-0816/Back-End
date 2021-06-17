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
<%--@elvariable id="pbxServers" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.SummaryCompanyServerResponse>"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.model.form.PickUpGroupFormRequest"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post" action="${pageContext.request.contextPath}/api/pickup-group/" data-done="reload">

    <i class="close icon"></i>
    <div class="header">당겨받기그룹[추가]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini orange compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">교환기</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <c:choose>
                            <c:when test="${pbxServers.size() > 0}">
                                <form:select path="host">
                                    <form:option value="" label="선택"/>
                                    <c:forEach var="e" items="${pbxServers}">
                                        <form:option value="${e.host}" label="${e.name}"/>
                                    </c:forEach>
                                </form:select>
                            </c:when>
                            <c:otherwise>localhost</c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">당겨받기명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="groupname"/></div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>
