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
    <tr class="color-bar1">
        <td>I/B 통화중</td>
        <td class="-consultant-status-count" data-value="1" data-call="IR,ID">${stat.inCallingCount}</td>
    </tr>
    <tr class="color-bar2">
        <td>O/B 통화중</td>
        <td class="-consultant-status-count" data-value="1" data-call="OR,OD">${stat.outCallingCount}</td>
    </tr>
    </tbody>
</table>
<div class="-chart -dashboard-chart"></div>

<script>
    const data = [<c:forEach var="e" items="${stat.hourToResultCall}">{hour: '${e.key}시', inboundCnt: ${e.value.inboundCnt}, outboundCnt: ${e.value.outboundCnt}}, </c:forEach>];
    chartjs.drawLineChart(component.find('.-chart')[0], data, 'hour', ['inboundCnt', 'outboundCnt'], {
        colors: ['#C60452', '#0E6EB8'],
        labels: ['I/B', 'O/B'],
        bottom: -5
    });
</script>
