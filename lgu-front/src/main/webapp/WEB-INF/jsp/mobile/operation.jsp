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
            <h3 class="ui dividing header">운영현황</h3>
            <table class="ui table celled definition structured unstackable mobile fixed">
                <tbody>
                <tr>
                    <th>고객대기</th>
                    <td class="-custom-wait-count">${data.customerWaiting}</td>
                    <th>응대율</th>
                    <td>${data.responseRate}%</td>
                </tr>
                <tr>
                    <th>상담대기</th>
                    <td class="-consultant-status-count" data-value="0" data-login="true"></td>
                    <th>상담중</th>
                    <td class="-consultant-status-count" data-value="1"></td>
                </tr>
                <tr>
                    <th>후처리</th>
                    <td class="-consultant-status-count" data-value="2"></td>
                    <th>휴식</th>
                    <td class="-consultant-status-count" data-value="3"></td>
                </tr>

                <c:forEach var="e" items="${statusCodes}" varStatus="status">
                    <c:if test="${e.statusNumber > 3}">
                        <c:if test="${e.statusNumber % 2 == 0}"><tr></c:if>
                        <th>${g.htmlQuote(e.statusName)}</th>
                        <td class="-consultant-status-count" data-value="${e.statusNumber}"></td>
                        <c:if test="${status.last || e.statusNumber % 2 != 0}"></tr></c:if>
                    </c:if>
                </c:forEach>
                    <%--<tr>
                        <th>식사</th>
                        <td>5</td>
                        <th>기타</th>
                        <td>80%</td>
                    </tr>--%>
                <tr>
                    <th>로그인</th>
                    <td class="-login-user-count" data-login="true"></td>
                    <th>로그아웃</th>
                    <td class="-logout-user-count" data-login="false"></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
        </script>
    </tags:scripts>
</tags:layout-mobile>
