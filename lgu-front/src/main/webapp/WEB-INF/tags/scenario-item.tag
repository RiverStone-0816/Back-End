<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<%@ attribute name="item" required="true" type="kr.co.eicn.ippbx.model.ResearchQuestionItemComposite" %>
<c:set var="isNotMappedAnswer" value="${item.path.endsWith('_0')}"/>

<li>
    <span class="ui right pointing basic mini label">${item.level}단계</span> ${g.htmlQuote(item.word)}
    <c:if test="${isNotMappedAnswer}">
        <button type="button" class="ui button basic mini -attach-to-question" data-level="${item.level}" data-path="${g.htmlQuote(item.path)}">
            <i class="file icon"></i>
        </button>
    </c:if>
</li>

<c:forEach var="answer" items="${item.answerItems}" varStatus="answerStatus">
    <li>
        <span class="ui right pointing basic mini label">답변${answerStatus.index + 1}</span> ${g.htmlQuote(answer.word)}
        <c:if test="${!isNotMappedAnswer}">
            <button type="button" class="ui button basic mini -attach-to-answer" data-level="${answer.level}" data-path="${g.htmlQuote(answer.path)}">
                <i class="file icon"></i>
            </button>
        </c:if>
        <c:if test="${answer.child != null}">
            <ul class="scenario-list">
                <tags:scenario-item item="${answer.child}"/>
            </ul>
        </c:if>
    </li>
</c:forEach>

<c:if test="${item.childNode != null && item.childNode.size() > 0}">
    <li class="-research-item">
        <ul class="scenario-list">
            <tags:scenario-item item="${item.childNode.get(0)}"/>
        </ul>
    </li>
</c:if>
