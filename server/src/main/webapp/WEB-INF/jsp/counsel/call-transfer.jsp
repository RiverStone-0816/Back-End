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

<table class="ui table celled definition very compact small">
    <tbody>
    <tr>
        <td class="four wide">대표번호</td>
        <td>
            <select id="service-numbers">
                <c:forEach var="e" items="${services}">
                    <option value="${g.htmlQuote(e.svcNumber)}">${g.htmlQuote(e.svcName)}</option>
                </c:forEach>
            </select>
            <button type="button" class="ui icon button mini -redirect-to-service" data-input="#service-numbers">
                <i class="share square icon"></i>
            </button>
        </td>
    </tr>
    <tr>
        <td>큐번호</td>
        <td>
            <select id="hunt-numbers">
                <c:forEach var="e" items="${queues}">
                    <option value="${g.htmlQuote(e.number)}">${g.htmlQuote(e.hanName)}</option>
                </c:forEach>
            </select>
            <button type="button" class="ui icon button mini -redirect-to-hunt" data-input="#hunt-numbers">
                <i class="share square icon"></i>
            </button>
        </td>
    </tr>
    </tbody>
</table>

<tags:scripts>
    <script>
        $('.-redirect-to-hunt').click(function () {
            ipccCommunicator.redirectHunt($($(this).attr('data-input')).val());
        });
        $('.-redirect-to-service').click(function () {
            ipccCommunicator.redirectHunt($($(this).attr('data-input')).val());
        });
    </script>
</tags:scripts>

