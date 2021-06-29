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

<div id="center-stat">
    <table class="ui table celled fixed">
        <thead>
        <tr>
            <th>응대율</th>
            <th>고객대기자수</th>
            <th>근무상담사</th>
            <th>총 콜백</th>
            <th>미처리 콜백</th>
            <th>대기</th>
            <th>I/B통화중</th>
            <th>O/B통화중</th>
            <th>후처리</th>
            <th>기타</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="positive">${String.format("%.1f", stat.rateValue)}%</td>
            <td class="warning -custom-wait-count">${stat.waitCustomerCount}</td>
            <td class="negative -consultant-status-count" data-value="0,1,2,3,4,5,6,7,8">${stat.workingPerson}</td>
            <td>${stat.totalCallback}</td>
            <td>${stat.unprocessedCallback}</td>
            <td class="-consultant-status-count" data-value="0">${stat.waitPersonCount}</td>
            <td class="-consultant-status-count" data-value="1" data-call="IR,ID">${stat.inboundCall}</td>
            <td class="-consultant-status-count" data-value="1" data-call="OR,OD">${stat.outboundCall}</td>
            <td class="-consultant-status-count" data-value="2">${stat.postProcess}</td>
            <td class="-consultant-status-count" data-value="3,4,5,6,7,8,9">${stat.etc}</td>
        </tr>
        </tbody>
    </table>
</div>

