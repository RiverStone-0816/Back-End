<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div id="hunt-stat">
    <table class="ui table celled fixed structured">
        <thead>
        <tr>
            <th>수신그룹명</th>
            <th>수신콜</th>
            <th>연결요청</th>
            <th>응답호</th>
            <th>포기호</th>
            <th>콜백</th>
            <th>응답률</th>
        </tr>
        </thead>

        <c:choose>
            <c:when test="${list.size() > 0}">
                <tbody>
                <c:forEach var="e" items="${list}">
                    <tr>
                        <td>${g.htmlQuote(e.huntName)}</td>
                        <td>${e.total}</td>
                        <td>${e.connectionRequest}</td>
                        <td>${e.success}</td>
                        <td>${e.cancel}</td>
                        <td>${e.callback}</td>
                        <td>${String.format("%.1f", e.responseRate)}%</td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                <tr>
                    <td>합계</td>
                    <td>${list.stream().map(e -> e.total).sum()}</td>
                    <td>${list.stream().map(e -> e.connectionRequest).sum()}</td>
                    <td>${list.stream().map(e -> e.success).sum()}</td>
                    <td>${list.stream().map(e -> e.cancel).sum()}</td>
                    <td>${list.stream().map(e -> e.callback).sum()}</td>
                    <td>${String.format("%.1f", list.stream().filter(e -> e.connectionRequest > 0).map(e -> e.responseRate).average().orElse(0.0))}%</td>
                </tr>
                </tfoot>
            </c:when>
            <c:otherwise>
                <tbody>
                <tr>
                    <td colspan="8" class="null-data">조회된 데이터가 없습니다.</td>
                </tr>
                </tbody>
            </c:otherwise>
        </c:choose>
    </table>
</div>
