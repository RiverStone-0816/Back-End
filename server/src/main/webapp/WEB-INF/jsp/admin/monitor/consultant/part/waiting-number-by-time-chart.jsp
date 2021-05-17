<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div class="panel" id="waiting-number-chart">
    <div class="panel-heading">
        <label class="control-label">시간별대기자수</label>
    </div>
    <div class="panel-body">
        <div class="-chart"></div>
    </div>
</div>

<script>
    const data = [
        <c:forEach var="e" items="${stat}">
        {hour: ${e.key}, waitCount: ${e.value}},
        </c:forEach>
    ];

    drawBarChart(ui.find('.-chart')[0], data, 'hour', ['waitCount'], {
        unitWidth: 20,
        colorClasses: ['bcolor-bar1', 'bcolor-bar2', 'bcolor-bar3', 'bcolor-bar4', 'bcolor-bar5', 'bcolor-bar6', 'bcolor-bar7', 'bcolor-bar8', 'bcolor-bar9', 'bcolor-bar10']
    });
</script>