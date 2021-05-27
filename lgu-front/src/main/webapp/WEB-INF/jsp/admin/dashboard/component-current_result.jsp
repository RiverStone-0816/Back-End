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
        <td>실시간통화중</td>
        <td>
            <text class="-consultant-status-count" data-value="1">${stat.callingCnt}</text>
            명
        </td>
    </tr>
    <tr class="color-bar1">
        <td>수신대기</td>
        <td>
            <text class="-consultant-status-count" data-value="0" data-login="true">${stat.counselWaitCnt}</text>
            명
        </td>
    </tr>
    <tr>
        <td>비로그인 대기</td>
        <td>
            <text class="-consultant-status-count" data-value="0" data-login="false">${stat.counselWaitNoLoginCnt}</text>
            명
        </td>
    </tr>
    <tr>
        <td>후처리 등 기타</td>
        <td>
            <text class="-consultant-status-count"
                  data-value="2,3,4,5,6,7,8,9">${stat.hourToCurrentCnt.keySet().stream().filter(e -> e != 1).map(e -> stat.hourToCurrentCnt.get(e).maxWaitCnt).sum()}</text>
            명
        </td>
    </tr>
    <tr>
        <td>로그인 상담원</td>
        <td>
            <text class="-login-user-count">${stat.loginCnt}</text>
            명
        </td>
    </tr>
    </tbody>
</table>
<div class="-chart" style="height: 150px; position: absolute; bottom: 20px; left: 0; right: 0;"></div>

<script>
    const data = [<c:forEach var="e" items="${stat.hourToCurrentCnt}">{hour: '${e.key}시', maxWaitCnt: ${e.value.maxWaitCnt}}, </c:forEach>];
    drawLineChart(component.find('.-chart')[0], data, 'hour', ['maxWaitCnt'], {ticks: 4, yLabel: '', unitWidth: 30, colorClasses: ['bcolor-bar1']});
</script>
