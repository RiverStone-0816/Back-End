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
        <td>고객대기</td>
        <td class="-custom-wait-count" data-hunt-number="${g.htmlQuote(huntNumber)}">${stat.customWaitCnt}</td>
    </tr>
    <tr>
        <td>상담대기</td>
        <td class="-consultant-status-count" data-hunt-number="${g.htmlQuote(huntNumber)}" data-value="0" data-login="true">${stat.counselWaitCnt}</td>
    </tr>
    <tr>
        <td>비로그인 대기</td>
        <td class="-consultant-status-count" data-hunt-number="${g.htmlQuote(huntNumber)}" data-value="0" data-login="false">${stat.counselWaitNoLoginCnt}</td>
    </tr>
    <tr>
        <td>통화중</td>
        <td class="-consultant-status-count" data-hunt-number="${g.htmlQuote(huntNumber)}" data-value="1">${stat.callingCnt}</td>
    </tr>
    <tr>
        <td>후처리 등 기타</td>
        <td class="-consultant-status-count" data-hunt-number="${g.htmlQuote(huntNumber)}" data-value="2,3,4,5,6,7,8,9">${stat.etcCnt}</td>
    </tr>
    <tr>
        <td>응답률</td>
        <td>${stat.rateValue}%</td>
    </tr>
    </tbody>
</table>

<script>
    component.find('[data-hunt-number]').each(function () {
        const queue = getQueueFromNumber($(this).attr('data-hunt-number'));
        if (queue) $(this).attr('data-hunt', queue.name);
        else console.error('unknown queue number: ' + $(this).attr('data-hunt-number'));
    });

    (function () {
        if (window.queues) {
            const queue = getQueueFromNumber('${g.escapeQuote(huntNumber)}');
            if (queue) queue.waitingCustomerCount = '${stat.customWaitCnt}';

            if (updateQueues)
                updateQueues();
        }
    })();
</script>
