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

<div class="ui list" id="outer-link-list">
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach var="e" items="${list}">
                <a class="item" href="javascript:linkCheck('${g.htmlQuote(e.reference)}');">${g.htmlQuote(e.name)}(${g.htmlQuote(e.reference)})</a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="empty">등록된 외부링크가 없습니다.</p>
        </c:otherwise>
    </c:choose>
</div>

