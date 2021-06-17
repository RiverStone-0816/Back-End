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
           action="${pageContext.request.contextPath}/api/conference-room/${entity == null ? null : entity.seq}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">회의실[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">회의실명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="roomName"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">회의실번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="roomNumber">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${confRoomNumbers}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의실RID</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="roomCid"/>
                    </div>
                </div>
                <div class="eight wide column">
                    (회의 초대시 상대방 전화기에 보일 발신번호)
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">내부회의실단축번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:input path="roomShortNum" placeholder="4자리 숫자 입력"/>
                    </div>
                </div>
                <div class="eight wide column">
                    (초대시 상대방 전화기에 보일 발신번호 or 참가시 단축번호 99XXXX)
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini orange compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e)}</span>
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
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>
