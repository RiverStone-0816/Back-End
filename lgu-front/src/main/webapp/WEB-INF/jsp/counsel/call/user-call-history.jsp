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

<select id="user-call-history" onchange="loadCustomInput('','', this.value, $(this).find('option:selected').attr('data-unique-id'), $(this).find('option:selected').attr('data-call-type'));">
    <option value="" label="">통화이력선택</option>
    <c:forEach var="e" items="${list}">
        <option value="${e.src}" data-unique-id="${e.uniqueid}" data-call-type="${e.inOut}">[<fmt:formatDate value="${e.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/>] ${g.htmlQuote(e.src)} > ${g.htmlQuote(e.dst)} (${g.htmlQuote(e.callStatusValue)})</option>
    </c:forEach>
</select>
