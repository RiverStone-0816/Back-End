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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal inverted small">
    <i class="close icon"></i>
    <div class="header">상담정보</div>

    <div class="content rows scrolling">
        <table class="ui table celled definition">
            <tbody>
            <tr>
                <td class="three wide">상담원</td>
                <td class="left aligned">${entity.userName}</td>
            </tr>
            <tr>
                <td class="three wide">채널</td>
                <td class="left aligned">
                    ${entity.groupKind == 'PHONE' ? '전화': entity.groupKind == 'TALK' ? '상담톡' : ''}
                    <c:choose>
                        <c:when test="${entity.groupKind == 'PHONE' && not empty entity.uniqueid}">
                            <div class="center aligned">
                                <c:set var="listeningAuthority"
                                       value="${user.listeningRecordingAuthority.equals('MY') && entity.personList.id.equals(user.id)
                                             or user.listeningRecordingAuthority.equals('GROUP') && entity.personList.groupTreeName.contains(user.groupCode)
                                             or user.listeningRecordingAuthority.equals('ALL')}"/>
                                <c:set var="downloadAuthority"
                                       value="${user.downloadRecordingAuthority.equals('MY') && entity.personList.id.equals(user.id)
                                             or user.downloadRecordingAuthority.equals('GROUP') && entity.personList.groupTreeName.contains(user.groupCode)
                                             or user.downloadRecordingAuthority.equals('ALL')}"/>
                                <c:forEach var="e" items="${files}" varStatus="status">
                                    <c:if test="${listeningAuthority}">
                                        <audio data-src="${pageContext.request.contextPath}/api/record-file/resource-front-play/${e.cdr}/${g.urlEncode(e.uniqueid)}/?partial=${status.index}"
                                               controls class="audio" preload="none"></audio>
                                    </c:if>
                                    <c:if test="${downloadAuthority}">
                                        <div class="center">
                                            <a target="_blank"
                                               href="${pageContext.request.contextPath}/api/record-file/resource-front-down/${e.cdr}/${g.urlEncode(e.uniqueid)}/${g.urlEncode(e.dstUniqueid)}/?partial=${status.index}">
                                                [ 파일다운로드 ]
                                            </a>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:when test="${entity.groupKind == 'TALK' && not empty entity.hangupMsg}">
                            <button class="ui button mini compact right floated"
                                    onclick="talkHistoryView('${entity.hangupMsg}')">상담이력확인
                            </button>
                        </c:when>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="three wide">상담등록시간</td>
                <td class="left aligned"><fmt:formatDate value="${entity.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
            <c:forEach var="field" items="${resultType.fields}">
                <c:set var="name" value="${field.fieldId.substring(resultType.kind.length() + '_'.length()).toLowerCase()}"/>
                <c:set var="value" value="${resultFieldNameToValueMap.get(field.fieldId)}"/>
                <tr>
                    <td class="three wide">${g.htmlQuote(field.fieldInfo)}</td>
                    <td class="left aligned">
                        <c:choose>
                            <c:when test="${field.fieldType == 'MULTICODE'}">
                                <c:forEach var="e" items="${field.codes}">
                                    <c:if test="${value.indexOf(e.codeId) >= 0}">
                                        ${g.htmlQuote(e.codeName)}&ensp;
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:when test="${field.fieldType == 'CODE'}">
                                <c:forEach var="e" items="${field.codes}">
                                    <c:if test="${value == e.codeId}">
                                        ${g.htmlQuote(e.codeName)}&ensp;
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <pre style="white-space: pre-wrap">${g.htmlQuote(value)}</pre>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
