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

<c:if test="${services != null && services.size() > 0}">
    <div class="accordion">
        <button class="team-title">
            <div class="team">
                <img src="<c:url value="/resources/images/link_square_icon.svg"/>">
                <span>대표번호로 돌려주기</span>
            </div>
        </button>
        <ul class="team-list">
            <c:forEach var="e" items="${services}">
                <li class="item">
                    <div class="user">
                        <text>${g.htmlQuote(e.svcName)}</text>
                        <button type="button" class="forwarded-btn" title="전화돌려주기" onclick="ipccCommunicator.redirectHunt('${g.escapeQuote(e.svcNumber)}')"></button>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<c:if test="${queues != null && queues.size() > 0}">
    <div class="accordion">
        <button class="team-title">
            <div class="team">
                <img src="<c:url value="/resources/images/link_square_icon.svg"/>">
                <span>헌트번호로 돌려주기</span>
            </div>
        </button>
        <ul class="team-list">
            <c:forEach var="e" items="${queues}">
                <li class="item">
                    <div class="user">
                        <text>${g.htmlQuote(e.hanName)}</text>
                        <button type="button" class="forwarded-btn" title="전화돌려주기" onclick="ipccCommunicator.redirectHunt('${g.escapeQuote(e.number)}')"></button>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>
