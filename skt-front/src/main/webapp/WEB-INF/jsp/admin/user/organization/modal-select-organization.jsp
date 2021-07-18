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

<%--@elvariable id="root" type="kr.co.eicn.ippbx.front.model.OrganizationTree"--%>

<div class="ui modal">

    <i class="close icon"></i>
    <div class="header">부서조회</div>

    <div class="content rows scrolling">
        <div class="ui action input fluid">
            <input type="text" placeholder="검색" class="-input-keyword">
            <button type="button" class="ui icon button -button-search">
                <i class="search icon"></i>
            </button>
        </div>
        <div class="ui list">
            <c:forEach var="e" items="${root.children}">
                <tags:organization-tree e="${e}"/>
            </c:forEach>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui orange button -button-submit">확인</button>
    </div>
</div>
