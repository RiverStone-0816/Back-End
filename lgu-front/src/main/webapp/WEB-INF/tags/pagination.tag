<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="navigation" required="true" type="kr.co.eicn.ippbx.util.page.PageNavigation" %>
<%@ attribute name="ajaxLoaderTarget" type="java.lang.String" %>
<%@ attribute name="ajaxLoaderEnable" type="java.lang.Boolean" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="pageForm" required="true" type="kr.co.eicn.ippbx.util.page.PageForm" %>
<%@ attribute name="clickFunction" required="false" type="java.lang.String" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>

<div class="ui pagination menu pull-right small">

    <c:if test="${!navigation.items.contains(navigation.first)}">
        <a href="${url}?${pageForm.getQuery(navigation.first)}" aria-label="First"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="angle double left icon"></i>
        </a>
    </c:if>

    <c:if test="${1 < navigation.page}">
        <a href="${url}?${pageForm.getQuery(navigation.previous)}" aria-label="Previous"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="angle left icon"></i>
        </a>
    </c:if>

    <c:forEach items="${navigation.items}" var="i">
        <a href="${url}?${pageForm.getQuery(i)}"
           class="item ${navigation.page != i ? '' : 'active'} ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
                ${i}
        </a>
    </c:forEach>

    <c:if test="${navigation.page < navigation.last}">
        <a href="${url}?${pageForm.getQuery(navigation.next)}" aria-label="Next"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="angle right icon"></i>
        </a>
    </c:if>

    <c:if test="${!navigation.items.contains(navigation.last)}">
        <a href="${url}?${pageForm.getQuery(navigation.last)}" aria-label="Last"
           class="item ${(not empty ajaxLoaderTarget) && ajaxLoaderEnable != false ? '-ajax-loader' : ''}"
                <c:if test="${not empty ajaxLoaderTarget}">
                    data-ajaxify="false" data-target="<c:out value="${ajaxLoaderTarget}"/>"
                </c:if>
                <c:if test="${empty ajaxLoaderTarget}">
                    onclick="$.blockUIFixed()"
                </c:if>>
            <i class="angle double right icon"></i>
        </a>
    </c:if>
</div>
