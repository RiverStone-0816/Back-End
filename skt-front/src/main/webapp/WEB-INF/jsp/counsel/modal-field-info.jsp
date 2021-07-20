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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal">
    <i class="close icon"></i>
    <div class="header">${g.htmlQuote(field.fieldInfo)}</div>

    <div class="content scrolling">
        <table class="ui table celled compact unstackable">
            <tbody>
            <c:forEach var="e" items="${field.commonCodes}">
                <tr>
                    <th>${g.htmlQuote(e.codeName)}</th>
                    <td>${g.htmlQuote(e.script)}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="actions">
        <button type="submit" class="ui brand button modal-close">확인</button>
    </div>
</div>
