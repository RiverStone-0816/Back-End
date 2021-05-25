<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div class="panel full-height" id="primary-stat">
    <div class="panel-heading">
        <label class="control-label">주요현황</label>
    </div>
    <div class="panel-body overflow-hidden">
        <div class="ui four column grid halfdonut-container">
            <div class="column">
                <h5 class="ui center aligned header">응답률</h5>
                <svg id="pie-response-rate" class="full-width" ></svg>
            </div>
            <div class="column">
                <h5 class="ui center aligned header">콜백 처리율</h5>
                <svg id="pie-process-callback" class="full-width"></svg>
            </div>
            <div class="column">
                <h5 class="ui center aligned header">상담 가용율</h5>
                <svg id="pie-consultation-availability" class="full-width"></svg>
            </div>
            <div class="column">
                <h5 class="ui center aligned header">상담원 상태</h5>
                <svg id="pie-consultant-status" class="full-width"></svg>
            </div>
        </div>
    </div>
</div>

<script>
    drawPieChart('#pie-response-rate', ${(stat.rateValue == null ? 0 : stat.rateValue).doubleValue() / 100}, {startAngle: -90, endAngle: 90, colorClasses: ['bcolor-bar1', 'bcolor-bar2']});
    drawPieChart('#pie-process-callback', ${(stat.processedCallback == null ? 0 : stat.processedCallback).doubleValue() / (stat.totalCallback == null ? 0 : stat.totalCallback)}, {startAngle: -90, endAngle: 90, colorClasses: ['bcolor-bar1', 'bcolor-bar2']});
    drawPieChart('#pie-consultation-availability', ${(stat.loginCount == null ? 0 : stat.loginCount).doubleValue() / (stat.workingPerson == null ? 0 : stat.workingPerson)}, {startAngle: -90, endAngle: 90, colorClasses: ['bcolor-bar1', 'bcolor-bar2']});
    drawPieChart('#pie-consultant-status', ${(stat.waitPersonCount == null ? 0 : stat.waitPersonCount).doubleValue() / (stat.workingPerson == null ? 0 : stat.workingPerson)}, {startAngle: -90, endAngle: 90, colorClasses: ['bcolor-bar1', 'bcolor-bar2']});
</script>
