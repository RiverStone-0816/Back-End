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
            <h3 class="ui dividing header">콜백현황</h3>

            <table class="ui table celled unstackable mobile fixed">
                <thead>
                <tr>
                    <th>수신번호</th>
                    <th>콜백번호</th>
                    <th>상담원</th>
                    <th>요청시간</th>
                    <th>처리시간</th>
                    <th>처리상태</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${pagination.rows.size() > 0}">
                        <c:forEach var="e" items="${pagination.rows}">
                            <tr>
                                <td>${g.htmlQuote(e.callerNumber)}</td>
                                <td>${g.htmlQuote(e.callbackNumber)}</td>
                                <td>${g.htmlQuote(e.idName)}</td>
                                <td><fmt:formatDate value="${(e.inputDate)}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td><fmt:formatDate value="${(e.resultDate)}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td>${g.htmlQuote(message.getEnumText(e.status))}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <div style="margin-top: 1em;">
                <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/m/callback/" pageForm="${search}"/>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
        </script>
    </tags:scripts>
</tags:layout-mobile>
