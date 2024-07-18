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

<table class="ui celled table structured compact unstackable selectable" id="user-call-stt-history">
    <thead>
    <tr>
        <th>수/발신</th>
        <th>통화시간</th>
        <th>전화번호</th>
        <th>상담원</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="e" items="${list}">
        <tr onclick="popupSttUniInfo('${e.uniqueid}', '${e.inOut == 'I' ? e.src : e.dst}', '${e.dialupDate}', '${e.hangupDate}')">
            <td>${e.inOut == 'I' ? '수신' : '발신'}</td>
            <td><fmt:formatDate value="${e.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${e.inOut == 'I' ? e.src : e.dst}</td>
            <td>${e.personList.idName}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>