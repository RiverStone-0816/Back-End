<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<table class="table celled ui" id="todo-list">
    <thead>
    <tr>
        <th>채널</th>
        <th>요청시간</th>
        <th>고객정보</th>
        <th>처리상태</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="e" items="${list}">
        <tr>
            <td>${g.htmlQuote(message.getEnumText(e.todoKind))}</td>
            <td><fmt:formatDate value="${e.regdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>
                <div class="ui action input fluid" style="text-align: center;">
                    <c:if test="${e.todoInfo != null && e.todoInfo != ''}">
                        <input type="text" readonly value="${g.htmlQuote(e.todoInfo)}"/>
                        <button type="button" class="ui icon button" onclick="$('#calling-number').val('${g.htmlQuote(e.todoInfo)}')"><i class="phone icon"></i></button>
                    </c:if>
                </div>
            </td>
            <td>${g.htmlQuote(message.getEnumText(e.todoStatus))}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
