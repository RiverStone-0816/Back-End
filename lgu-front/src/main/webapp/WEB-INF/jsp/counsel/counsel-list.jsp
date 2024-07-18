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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>


<tbody id="counsel-list">
<c:forEach var="e" items="${list}">
    <tr>
        <c:if test="${serviceKind.equals('SC')}">
            <td>
                <c:choose>
                    <c:when test="${e.groupKind == 'PHONE'}">통화</c:when>
                    <c:when test="${e.groupKind == 'EMAIL'}">이메일</c:when>
                    <c:when test="${e.groupKind == 'TALK'}">채팅상담</c:when>
                </c:choose>
            </td>
        </c:if>

        <td>${g.htmlQuote(e.inOutValue)}</td>

        <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

        <c:set var="value" value="${''}"/>
        <c:forEach var="field" items="${e.multichannelList}">
            <c:if test="${field.channelType == 'PHONE'}">
                <c:set var="value" value="${value.concat(field.channelData).concat(' ')}"/>
            </c:if>
        </c:forEach>
        <td title="${g.htmlQuote(value)}">${g.htmlQuote(value)}</td>
        <td title="${g.htmlQuote(e.customInfo.maindbString_1)}">${g.htmlQuote(e.customInfo.maindbString_1)}</td>
        <td title="${g.htmlQuote(e.userName)}">${g.htmlQuote(e.userName)}</td>
        <td>
            <button type="button" class="ui button mini compact" onclick="popupCounselingInfo(${e.seq})">열람</button>
            <c:if test="${e.callType != '' && e.uniqueid != ''}">
                <button type="button" class="ui button mini compact blue" onclick="popupSttInfo(${e.seq})">STT</button>
            </c:if>
        </td>
    </tr>
</c:forEach>
</tbody>
