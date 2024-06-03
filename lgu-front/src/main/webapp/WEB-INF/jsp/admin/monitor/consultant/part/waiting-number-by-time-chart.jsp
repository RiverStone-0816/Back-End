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

<div class="panel" id="waiting-number-chart">
    <div class="panel-heading">
        <label class="control-label">시간별 연결요청</label>
    </div>
    <div class="panel-body">
        <div class="-chart"></div>
    </div>
</div>

<script>
    const data = [
        <c:forEach var="e" items="${stat}">
        {hour: ${e.key} + '시', waitCount: ${e.value}},
        </c:forEach>
    ];

    chartjs.drawBarChart(ui.find('.-chart')[0], data, 'hour', ['waitCount'], {
        colors: ['#C60452'],
        labels: ['연결요청'],
    });
</script>
