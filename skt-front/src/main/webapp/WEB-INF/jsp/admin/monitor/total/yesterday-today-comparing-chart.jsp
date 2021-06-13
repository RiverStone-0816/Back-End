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

<div id="yesterday-today-comparing-chart">
    <div class="ui grid">
        <div class="eight wide column compact-pr">
            <div class="inner-box">
                <h2 class="chart-title">수신 통계</h2>
                <div class="pd10 align-center height-250">
                    <div id="today-and-yesterday-inbound-comparing-chart" style="width: 100%; height: 100%; overflow-y: hidden; overflow-x: auto;"></div>
                </div>
                <div class="chart-label-wrap-bar">
                    <ul>
                        <li><span class="symbol bcolor-bar2"></span><span class="text">어제</span></li>
                        <li><span class="symbol bcolor-bar1"></span><span class="text">오늘</span></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="eight wide column compact-pl">
            <div class="inner-box">
                <h2 class="chart-title">응답율 통계</h2>
                <div class="pd10 align-center height-250">
                    <div id="today-and-yesterday-response-rate-comparing-chart" style="width: 100%; height: 100%; overflow-y: hidden; overflow-x: auto;"></div>
                </div>
                <div class="chart-label-wrap-bar">
                    <ul>
                        <li><span class="symbol bcolor-bar2"></span><span class="text">어제</span></li>
                        <li><span class="symbol bcolor-bar1"></span><span class="text">오늘</span></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    drawLineChart($('#today-and-yesterday-inbound-comparing-chart')[0], [
        <c:forEach var="hour" begin="${0}" end="${23}" >
        {
            yesterday: ${(data.get(hour) != null && data.get(hour).left != null ? data.get(hour).left.total : 0)},
            today: ${(data.get(hour) != null && data.get(hour).right != null ? data.get(hour).right.total : 0)},
            time: '${hour}시'
        },
        </c:forEach>
    ], 'time', ['yesterday', 'today'], {
        ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['bcolor-bar2', 'bcolor-bar1']
    });

    drawLineChart($('#today-and-yesterday-response-rate-comparing-chart')[0], [
        <c:forEach var="hour" begin="${0}" end="${23}" >
        {
            yesterday: ${(data.get(hour) != null && data.get(hour).left != null ? data.get(hour).left.responseRate : 0)},
            today: ${(data.get(hour) != null && data.get(hour).right != null ? data.get(hour).right.responseRate : 0)},
            time: '${hour}시'
        },
        </c:forEach>
    ], 'time', ['yesterday', 'today'], {
        ticks: 5, yLabel: '', unitWidth: 30, colorClasses: ['bcolor-bar2', 'bcolor-bar1']
    });
</script>
