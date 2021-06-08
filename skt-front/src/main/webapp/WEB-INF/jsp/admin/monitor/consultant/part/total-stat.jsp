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

<div id="total-stat" class="panel mb15">
    <div class="panel-heading">
        <label class="control-label">통계</label>
    </div>
    <div class="panel-body pd-1em">
        <table class="ui table celled fixed structured">
            <tbody>
            <tr>
                <th colspan="2">응대율</th>
                <td class="positive" colspan="2">${String.format("%.1f", stat.rateValue)}%</td>
                <th>인입호수(대표/직통)</th>
                <td class="negative">${stat.serviceTotal + stat.directTotal}(${stat.serviceTotal}/${stat.directTotal})</td>
                <th>단순조회</th>
                <td>${stat.viewCount}</td>
                <th>연결요청(대표/직통)</th>
                <td>${stat.serviceConnectionRequest + stat.directTotal}(${stat.serviceConnectionRequest}/${stat.directTotal})</td>
            </tr>
            <tr>
                <th>IB응대호(대표/직통)</th>
                <td>${stat.serviceSuccess + stat.directSuccess}(${stat.serviceSuccess}/${stat.directSuccess})</td>
                <th>IB포기호(대표/직통)</th>
                <td>${stat.serviceCancel + stat.directTotal - stat.directSuccess}(${stat.serviceCancel}/${stat.directTotal - stat.directSuccess})</td>
                <th>콜백</th>
                <td>${stat.callbackSuccess}</td>
                <th>OB응대호</th>
                <td>${stat.outboundSuccess}</td>
                <th>OB무응답호</th>
                <td>${stat.outboundCancel}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
