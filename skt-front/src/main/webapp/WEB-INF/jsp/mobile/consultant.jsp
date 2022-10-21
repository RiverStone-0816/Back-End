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

<tags:layout-mobile>
    <div id="wrap-mobile">
        <div class="ui container basic">
            <h3 class="ui dividing header">상담원 모니터링</h3>
            <div class="filter-container">
                <div class="ui form fluid">
                    <select onchange="changePersons($(this).val())">
                        <option value="">전체</option>
                        <c:forEach var="e" items="${list}">
                            <option value="${e.groupCode}">${g.htmlQuote(e.groupName)}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <table class="ui table celled unstackable mobile fixed">
                <thead>
                <tr>
                    <th>이름</th>
                    <th>상태</th>
                    <th>유지시간</th>
                    <th>수신/발신</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="e" items="${list}">
                    <c:forEach var="person" items="${e.person}">
                        <tr class="-person" data-group="${e.groupCode}">
                            <td>${g.htmlQuote(person.idName)}</td>
                            <td class="-consultant-status-with-color" data-peer="${g.htmlQuote(person.peer)}"></td>
                            <td class="-consultant-status" data-peer="${g.htmlQuote(person.peer)}"></td>
                            <td class="-consultant-status-time" data-peer="${g.htmlQuote(person.peer)}"></td>
                        </tr>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            function changePersons(value) {
                const persons = $('.-person').show();
                if (!value) return;

                persons.filter(function () {
                    return $(this).attr('data-group') !== value.trim();
                }).hide();
            }
        </script>
    </tags:scripts>
</tags:layout-mobile>
