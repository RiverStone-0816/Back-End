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

<%--<h5 class="ui header">${g.htmlQuote(stat.title)}</h5>--%>
<table class="ui celled table striped small compact fixed">
    <tbody>
    <tr>
        <td>수신콜</td>
        <td>${stat.totalCnt}</td>
    </tr>
    <tr>
        <td>연결요청</td>
        <td>${stat.connReqCnt}</td>
    </tr>
    <tr>
        <td>응대호수</td>
        <td>${stat.successCnt}</td>
    </tr>
    <tr class="color-bar1">
        <td>응대율</td>
        <td>${stat.rateValue}%</td>
    </tr>
    <tr>
        <td>서비스레벨</td>
        <td>${stat.serviceLevelRateValue}%</td>
    </tr>
    </tbody>
</table>
<div class="-chart" style="height: 150px; position: absolute; bottom: 20px; left: 0; right: 0;"></div>

<script>
    const data = [<c:forEach var="e" items="${stat.hourToResponseRate}">{hour: '${e.key}시', value: ${e.value}}, </c:forEach>];
    drawLineChart(component.find('.-chart')[0], data, 'hour', ['value'], {ticks: 4, maxY: 100, yLabel: '', unitWidth: 30, colorClasses: ['bcolor-bar1']});
</script>
