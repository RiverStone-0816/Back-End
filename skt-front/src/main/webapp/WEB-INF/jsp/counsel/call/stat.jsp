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

<div class="statistics-inner" id="my-call-time">
    <table class="ui celled table compact unstackable structured">
        <thead>
            <tr>
                <th rowspan="2">시간</th>
                <th rowspan="2">총 건수</th>
                <th colspan="2">O/B</th>
                <th colspan="2">I/B</th>
                <th rowspan="2">후처리건수</th>
            </tr>
            <tr>
                <th>시도</th>
                <th>성공</th>
                <th>시도</th>
                <th>성공</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="i" begin="0" end="23">
            <c:set var="e" value="${data.myStatData.get(i.byteValue())}"/>
            <tr>
                <th>${i}시</th>
                <td>${e != null ? e.totalCnt : 0}</td>
                <td>${e != null ? e.outTotal : 0}</td>
                <td>${e != null ? e.outSuccess : 0}</td>
                <td>${e != null ? e.inTotal : 0}</td>
                <td>${e != null ? e.inSuccess : 0}</td>
                <td>${e != null ? e.memberTotal : 0}</td>
            </tr>
        </c:forEach>
            <tr>
                <th>합계</th>
                <td>${data.totalSum}</td>
                <td>${data.outTotalSum}</td>
                <td>${data.outSuccessSum}</td>
                <td>${data.inTotalSum}</td>
                <td>${data.inSuccessSum}</td>
                <td>${data.memberStatSum}</td>
            </tr>
        </tbody>
    </table>
</div>
