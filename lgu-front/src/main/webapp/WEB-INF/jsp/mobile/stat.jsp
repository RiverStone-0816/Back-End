<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:layout-mobile>
    <div id="wrap-mobile">
        <div class="ui container basic">
            <h3 class="ui dividing header">통계</h3>
            <div class="filter-container">
                <div class="ui form fluid">
                    <select onchange="changeView($(this).val())">
                        <option value="INBOUND" selected>인바운드 통계</option>
                        <option value="CONSULTANT">상담원 통계</option>
                    </select>
                </div>
            </div>

            <table class="ui table celled unstackable mobile fixed -view-type" data-value="INBOUND">
                <thead>
                <tr>
                    <th style="width: 120px;">서비스</th>
                    <th>연결요청</th>
                    <th>응답호</th>
                    <th>포기호</th>
                    <th>응답률</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="datum" items="${data.services}">
                    <tr>
                        <th>${g.htmlQuote(datum.serviceNumber)}</th>
                        <th>${datum.connectionRequest}</th>
                        <th>${datum.successCall}</th>
                        <th>${datum.cancelCall}</th>
                        <th><fmt:formatNumber value="${datum.responseRate}" pattern="#.#"/></th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <table class="ui table celled unstackable mobile fixed -view-type" data-value="CONSULTANT" style="display: none">
                <thead>
                <tr>
                    <th>이름</th>
                    <th>로그인</th>
                    <th>수신</th>
                    <th>발신</th>
                    <th>합계</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="e" items="${list}">
                    <tr>
                        <td>${g.htmlQuote(e.person.idName)}</td>
                        <td>
                            <i class="phone icon ${e.isPhone != 'Y' ? 'translucent' : ''} -consultant-phone-status" data-peer="${g.htmlQuote(e.person.peer)}"></i>
                        </td>
                        <td>${e.inboundSuccess}</td>
                        <td>${e.outboundSuccess}</td>
                        <td>${e.statTotal}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </div>

    <tags:scripts>
        <script>
            function changeView(type) {
                $('.-view-type').hide().filter(function () {
                    return $(this).attr('data-value') === type;
                }).show();
            }
        </script>
    </tags:scripts>
</tags:layout-mobile>
