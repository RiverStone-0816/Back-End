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

<div class="panel full-height" id="primary-stat">
    <div class="panel-heading">
        <label class="control-label">주요현황</label>
    </div>
    <div class="panel-body overflow-hidden">
        <div class="ui four column grid halfdonut-container">
            <div class="column">
                <div id="pie-response-rate" class="full-width"></div>
            </div>
            <div class="column">
                <div id="pie-process-callback" class="full-width"></div>
            </div>
            <div class="column">
                <div id="pie-consultation-availability" class="full-width"></div>
            </div>
            <div class="column">
                <div id="pie-consultant-status" class="full-width"></div>
            </div>
        </div>
    </div>
</div>

<script>
    chartjs.drawHalfDonutChart('#pie-response-rate', ${(stat.rateValue == null ? 0 : stat.rateValue).doubleValue() / 100}, {
        colors: ['#C60452', '#0E6EB8'],
        title: '응답률'
    });
    chartjs.drawHalfDonutChart('#pie-process-callback', ${not empty stat.totalCallback && stat.totalCallback > 0 ? (empty stat.processedCallback ? 0 : stat.processedCallback).doubleValue() / stat.totalCallback.doubleValue() : 0}, {
        colors: ['#C60452', '#0E6EB8'],
        title: '콜백 처리율'
    });
    chartjs.drawHalfDonutChart('#pie-consultation-availability', ${not empty stat.workingPerson && stat.workingPerson > 0 ? (empty stat.loginCount ? 0 : stat.loginCount).doubleValue() / stat.workingPerson.doubleValue() : 0}, {
        colors: ['#C60452', '#0E6EB8'],
        title: '상담 가용율'
    });
    chartjs.drawHalfDonutChart('#pie-consultant-status', ${not empty stat.workingPerson && stat.workingPerson > 0 ? (empty stat.waitPersonCount ? 0 : stat.waitPersonCount).doubleValue() / stat.workingPerson.doubleValue() : 0}, {
        colors: ['#C60452', '#0E6EB8'],
        title: '상담원 상태'
    });
</script>
