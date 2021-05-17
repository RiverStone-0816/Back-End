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

<%--@elvariable id="metaTypes" type="java.util.List<kr.co.eicn.ippbx.server.model.dto.eicn.CompanyTreeLevelNameResponse>"--%>
<%--@elvariable id="summary" type="kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationPersonSummaryResponse"--%>
<%--@elvariable id="element" type="kr.co.eicn.ippbx.server.model.entity.eicn.Organization"--%>

<div class="panel-body" id="organization-element-summary">
    <table class="ui celled table structured definition unstackable">
        <tbody>
        <tr>
            <c:forEach var="type" items="${metaTypes}" varStatus="status">
                <c:choose>
                    <c:when test="${type.groupLevel <= element.groupLevel}">
                        <th class="three wide">${g.htmlQuote(type.groupTreeName)}명</th>
                    </c:when>
                    <c:otherwise>
                        <th>${g.htmlQuote(type.groupTreeName)}수</th>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <th>사용자수</th>
        </tr>
        <tr>
            <c:forEach var="type" items="${metaTypes}" varStatus="status">
                <c:choose>
                    <c:when test="${type.groupLevel <= element.groupLevel}">
                        <td>
                            <c:choose>
                                <c:when test="${status.index < summary.parents.size()}">
                                    ${g.htmlQuote(summary.parents.get(status.index).groupName)}
                                </c:when>
                                <c:otherwise>
                                    ${g.htmlQuote(element.groupName)}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <c:choose>
                                <c:when test="${summary.countsOfChildren == null || status.index - summary.parents.size() - 1 >= summary.countsOfChildren.size()}">
                                    0
                                </c:when>
                                <c:otherwise>
                                    ${summary.countsOfChildren.get(status.index - summary.parents.size() - 1)}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <td>${summary.userCountBelonging}</td>
        </tr>
        <c:forEach var="e" items="${summary.memberSummaries}">
            <tr>
                <c:forEach var="type" items="${metaTypes}" varStatus="status">
                    <c:choose>
                        <c:when test="${type.groupLevel <= element.groupLevel}">
                            <td>
                                <c:choose>
                                    <c:when test="${status.index < summary.parents.size()}">
                                        ${g.htmlQuote(summary.parents.get(status.index).groupName)}
                                    </c:when>
                                    <c:otherwise>
                                        ${g.htmlQuote(e.groupName)}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td>
                                <c:choose>
                                    <c:when test="${e.countsOfChildren == null || status.index - summary.parents.size() - 1 >= e.countsOfChildren.size()}">
                                        0
                                    </c:when>
                                    <c:otherwise>
                                        ${e.countsOfChildren.get(status.index - summary.parents.size() - 1)}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <td>${e.userCountBelonging}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>