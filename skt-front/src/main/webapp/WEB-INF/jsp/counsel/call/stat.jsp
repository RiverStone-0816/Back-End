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
        <c:set var="totalSum" value="0" />
        <c:set var="outTotalSum" value="0" />
        <c:set var="outSuccessSum" value="0" />
        <c:set var="inTotalSum" value="0" />
        <c:set var="inSuccessSum" value="0" />
        <c:set var="memberStatSum" value="0" />
        <c:forEach var="i" begin="0" end="23">
            <c:set var="e" value="${data.myStatData.get(i.byteValue())}"/>
            <c:set var="totalSum_for" value="${e != null ? e.totalCnt : 0}" />
            <c:set var="outTotalSum_for" value="${e != null ? e.outTotal : 0}" />
            <c:set var="outSuccessSum_for" value="${e != null ? e.outSuccess : 0}" />
            <c:set var="inTotalSum_for" value="${e != null ? e.inTotal : 0}" />
            <c:set var="inSuccessSum_for" value="${e != null ? e.inSuccess : 0}" />
            <c:set var="memberStatSum_for" value="${e != null ? e.memberTotal : 0}" />
            <tr>
                <th>${i}시</th>
                <td>${totalSum_for}</td>
                <td>${outTotalSum_for}</td>
                <td>${outSuccessSum_for}</td>
                <td>${inTotalSum_for}</td>
                <td>${inSuccessSum_for}</td>
                <td>${memberStatSum_for}</td>
            </tr>
            <c:set var="totalSum" value="${totalSum + totalSum_for}" />
            <c:set var="outTotalSum" value="${outTotalSum + outTotalSum_for}" />
            <c:set var="outSuccessSum" value="${outSuccessSum + outSuccessSum_for}" />
            <c:set var="inTotalSum" value="${inTotalSum + inTotalSum_for}" />
            <c:set var="inSuccessSum" value="${inSuccessSum + inSuccessSum_for}" />
            <c:set var="memberStatSum" value="${memberStatSum + memberStatSum_for}" />
        </c:forEach>
            <tr>
                <th>합계</th>
                <td>${totalSum}</td>
                <td>${outTotalSum}</td>
                <td>${outSuccessSum}</td>
                <td>${inTotalSum}</td>
                <td>${inSuccessSum}</td>
                <td>${memberStatSum}</td>
            </tr>
        </tbody>
    </table>
</div>
