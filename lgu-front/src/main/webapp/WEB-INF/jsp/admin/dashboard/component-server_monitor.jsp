<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<%--<h5 class="ui header">${g.htmlQuote(stat.title)}</h5>--%>
<table class="ui celled table striped small compact fixed">
    <tbody>
    <tr>
        <td>cpu Idle</td>
        <td>${stat.usedCpu}</td>
    </tr>
    <tr>
        <td>메모리 사용량</td>
        <td>${stat.usedMemory}</td>
    </tr>
    <tr>
        <td>HDD 사용량</td>
        <td style="${usedHdd != null && usedHdd >= 100 * 9 / 10 ? 'background: #FFA7A7' : ''}">${stat.usedHdd}</td>
    </tr>
    <c:forEach var="e" items="${stat.daemonList}">
        <tr>
            <td>${g.htmlQuote(e.daemonName)}</td>
            <td>${e.daemonStatus}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
