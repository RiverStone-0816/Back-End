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

<%--@elvariable id="condition" type="kr.co.eicn.ippbx.front.model.search.OrganizationPanCondition"--%>
<%--@elvariable id="metaTypes" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.CompanyTreeLevelNameResponse>"--%>
<%--@elvariable id="maxLevel" type="java.lang.Integer"--%>
<%--@elvariable id="root" type="kr.co.eicn.ippbx.front.model.OrganizationTree"--%>

<div class="ui list" id="organization-pan">
    <input type="hidden" name="keyword" value="${g.htmlQuote(condition.keyword)}"/>

    <div class="ui steps mini fluid">
        <c:forEach var="e" items="${metaTypes}">
            <div class="step">
                <div class="content">
                    <div class="title">${g.htmlQuote(e.groupTreeName)}</div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:forEach var="e" items="${root.children}">
        <tags:organization-editable-tree e="${e}" maxLevel="${maxLevel}" keyword="${condition.keyword}"/>
    </c:forEach>
</div>
