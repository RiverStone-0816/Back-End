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

<form:form id="cdr-evaluation-form" modelAttribute="form" cssClass="-json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/evaluation-result/${entity.id}/complete"
           data-before="prepareWriteFormData" data-done="doneWriteFormData">

    <div style="margin-bottom:10px">
        <table class="ui celled table compact unstackable two linebreak">
            <tbody>
            <tr>
                <th>평가지</th>
                <td>
                    <form:hidden path="resultTransfer"/>
                    <form:hidden path="cdrId"/>
                    <div class="ui form">
                        <form:select path="evaluationId">
                            <c:forEach var="e" items="${forms}">
                                <form:option value="${e.id}" label="${g.htmlQuote(e.name)}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </td>
                <th>평가자</th>
                <td>${g.htmlQuote(user.idName)}</td>
                <th>평가날짜</th>
                <td><fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
            <tr>
                <th>상담원</th>
                <td>
                    <div class="ui form">
                        <form:select path="targetUserid" disabled="true" items="${persons}" itemLabel="idName" itemValue="id"/>
                    </div>
                </td>
                <th>수/발</th>
                <td>${g.htmlQuote(cdr.callKindValue)}</td>
                <th>통화날짜</th>
                <td><fmt:formatDate value="${cdr.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
            <tr>
                <th>수신번호</th>
                <td>${g.htmlQuote(cdr.dst)}</td>
                <th>발신번호</th>
                <td>${g.htmlQuote(cdr.src)}</td>
                <td class="two wide" colspan="2">
                    <c:if test="${!user.listeningRecordingAuthority.equals('MY')}">
                    <c:choose>
                        <c:when test="${files.size() > 0}">
                            <c:forEach var="e" items="${files}" varStatus="status">
                                <audio data-src="${pageContext.request.contextPath}/api/record-file/resource-front-play/${e.cdr}/${g.urlEncode(e.uniqueid)}/?partial=${status.index}"
                                       controls class="audio" preload="none"></audio>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            녹취파일이 존재하지 않습니다.
                        </c:otherwise>
                    </c:choose>
                    </c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div style="margin-bottom:10px">
        <div id="evaluation-form"></div>
    </div>

    <div style="margin-bottom:10px">
        <table class="ui celled table compact unstackable two">
            <tbody>
            <tr>
                <th style="width: 200px;">메모</th>
                <td>
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="memo" rows="3"/>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
