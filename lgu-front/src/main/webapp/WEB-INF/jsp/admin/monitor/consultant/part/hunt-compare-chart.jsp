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

<div id="hunt-compare-chart" class="panel">
    <div class="panel-heading">
        <label class="control-label">통계 비교 모니터링</label>
        <div class="pull-right">
            <div class="ui action input" style="width: 300px">
                <select name="queues" multiple="multiple" class="ui fluid dropdown" data-dropdown-max-selections="2">
                    <option value="">비교 큐그룹 선택</option>
                    <c:forEach var="e" items="${queues}">
                        <option value="${e.key}" ${queueAName == e.key || queueBName == e.key ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="ui grid">
            <div class="eight wide column">
                <div style="text-align: center;">일별</div>
                <div class="-day-chart" style="width: 100%; height: 300px; overflow-y: hidden; overflow-x: auto;"></div>
            </div>
            <div class="eight wide column">
                <div style="text-align: center;">주별</div>
                <div class="-week-chart" style="width: 100%; height: 300px; overflow-y: hidden; overflow-x: auto;"></div>
            </div>
        </div>
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
    const colorClasses = ['bcolor-bar1', 'bcolor-bar2', 'bcolor-bar3', 'bcolor-bar4', 'bcolor-bar5', 'bcolor-bar6', 'bcolor-bar7', 'bcolor-bar8', 'bcolor-bar9', 'bcolor-bar10'];
    const huntToColorClass = {<c:forEach var="e" items="${queues}" varStatus="status"> '${g.escapeQuote(e.key)}': colorClasses[parseInt(${status.index}) % colorClasses.length], </c:forEach>};

    const dataOfDay = [
        <c:forEach var="hourEntry" items="${hourToQueueNameToCountOfDayMap}"> {
            hour: ${hourEntry.key} +'시',
            <c:forEach var="e" items="${queues}">
            '${g.escapeQuote(e.key)}': ${hourEntry.value.getOrDefault(e.key, 0)},
            </c:forEach>
        }, </c:forEach>
    ];

    const dataOfWeek = [
        <c:forEach var="hourEntry" items="${hourToQueueNameToCountOfWeekMap}"> {
            hour: ${hourEntry.key} +'시',
            <c:forEach var="e" items="${queues}">
            '${g.escapeQuote(e.key)}': ${hourEntry.value.getOrDefault(e.key, 0)},
            </c:forEach>
        }, </c:forEach>
    ];

    ui.find('[name=queues]').change(function () {
        const selected = $(this).find(':selected');
        if (selected.length !== 2) return;

        const fields = [];
        selected.each(function () {
            fields.push($(this).val());
        });

        replaceReceivedHtmlInSilence($.addQueryString('/admin/monitor/consultant/part/hunt-compare-chart', {queueAName: fields[0], queueBName: fields[1]}), '#hunt-compare-chart');
    });

    <c:if test="${queueAName != null && queueAName.length() > 0 && queueBName != null && queueBName.length() > 0}">
    drawBarChart(ui.find('.-day-chart')[0], dataOfDay, 'hour', ['${g.escapeQuote(queueAName)}', '${g.escapeQuote(queueBName)}'], {
        unitWidth: 30,
        colorClasses: [huntToColorClass['${g.escapeQuote(queueAName)}'], huntToColorClass['${g.escapeQuote(queueBName)}']]
    });
    drawBarChart(ui.find('.-week-chart')[0], dataOfWeek, 'hour', ['${g.escapeQuote(queueAName)}', '${g.escapeQuote(queueBName)}'], {
        unitWidth: 30,
        colorClasses: [huntToColorClass['${g.escapeQuote(queueAName)}'], huntToColorClass['${g.escapeQuote(queueBName)}']]
    });
    </c:if>
</script>
