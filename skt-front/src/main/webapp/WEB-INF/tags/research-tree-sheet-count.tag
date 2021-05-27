<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<%@ attribute name="item" required="true" type="kr.co.eicn.ippbx.front.model.TreeItem" %>

path: '${g.escapeQuote(item.path)}',
code: '${g.escapeQuote(item.code)}',
level: ${item.level},
itemId: ${item.itemId},
mappedNumber: ${item.mappedNumber},
itemName: '${g.escapeQuote(item.itemName)}',
mappedNumberDescription: '${g.escapeQuote(item.mappedNumberDescription)}',
count: ${item.count},
leafDescendantCount: ${item.leafDescendantCount},
leaf: ${item.leaf},
children: [
<c:forEach var="e" items="${item.children}">
    {<tags:research-tree-sheet-count item="${e}"/>},
</c:forEach>
],
