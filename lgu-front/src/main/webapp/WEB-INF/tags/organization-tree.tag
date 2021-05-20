<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>

<%@ attribute name="e" required="true" type="kr.co.eicn.ippbx.front.model.OrganizationTree" %>
<%@ attribute name="parentNames" %>

<div class="item">
    <i class="folder <%--open--%> icon"></i>
    <div class="content">
        <div class="header <%--select--%>"
             data-group-code="${g.htmlQuote(e.groupCode)}"
             data-group-name="${g.htmlQuote(e.groupName)}"
             data-group-names="${parentNames != null ? g.htmlQuote(parentNames.concat('&#0001;')) : null}${g.htmlQuote(e.groupName)}">
            ${g.htmlQuote(e.groupName)}
        </div>
        <div class="list">
            <c:forEach var="child" items="${e.children}">
                <tags:organization-tree e="${child}" parentNames="${parentNames != null ?parentNames.concat('&#0001;').concat(e.groupName) : e.groupName}"/>
            </c:forEach>
        </div>
    </div>
</div>
