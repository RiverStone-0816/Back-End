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

<div id="hunt-hour-chart" class="panel">
    <div class="panel-heading">
        <label class="control-label">통계 비교 모니터링</label>
    </div>
    <div class="panel-body">
        <div class="-chart" style="width: 100%; height: 300px; overflow-y: hidden; overflow-x: auto;"></div>
        <div class="chart-label-container">
            <div class="ui segment secondary">
                <c:forEach var="e" items="${queues}" varStatus="status">
                    <text class="label-list"><span class="point bcolor-bar${status.index + 1}"></span>${g.htmlQuote(e.value)}</text>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<script>
    const data = [
        <c:forEach var="hourEntry" items="${hourToQueueNameToCountMap}"> {
            hour: ${hourEntry.key} + '시',
            <c:forEach var="e" items="${queues}">
            '${g.escapeQuote(e.key)}': ${hourEntry.value.get(e.key)},
            </c:forEach>
        }, </c:forEach>
    ];

    chartjs.drawBarChart(ui.find('.-chart')[0], data, 'hour', [<c:forEach var="e" items="${queues}">'${g.escapeQuote(e.key)}', </c:forEach>], {
        colors: ['#c60452', '#0E6EB8', 'silver', 'gold', '#0abde3', '#2e353e', '#8f94fb', '#c60452', '#AFE4B8', '#ff9f43'],
        labels: [<c:forEach var="e" items="${queues}">'${g.escapeQuote(e.value)}', </c:forEach>],
        stacked: true
    });
</script>
