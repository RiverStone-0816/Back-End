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

<%--<h5 class="ui header">${g.htmlQuote(stat.title)}</h5>--%>
<table class="ui celled table striped small compact fixed">
    <tbody>
    <tr>
        <td>현재대기호수</td>
        <td class="-custom-wait-count">${stat.currentWaitCnt}</td>
    </tr>
    <tr class="color-bar1">
        <td>평균대기시간</td>
        <td>${stat.waitSecAve}초</td>
    </tr>
    <tr>
        <td>최대대기시간</td>
        <td>${stat.waitSecMax}초</td>
    </tr>
    <tr>
        <td>고객포기율</td>
        <td>${stat.customCancelRateValue}%</td>
    </tr>
    </tbody>
</table>
<div class="-chart -dashboard-chart"></div>

<script>
    const data = [<c:forEach var="e" items="${stat.hourToAvgWaitingTime}">{hour: '${e.key}시', value: parseInt(${e.value})}, </c:forEach>];
    chartjs.drawLineTimeChart(component.find('.-chart')[0], data, 'hour', ['value'], {
        colors: ['#C60452'],
        labels: ['평균대기시간'],
        top: 30,
        bottom: -5,
        right: 25,
    });
</script>
