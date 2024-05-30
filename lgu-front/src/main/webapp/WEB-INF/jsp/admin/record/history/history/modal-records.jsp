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

<div class="ui modal xsmall">
    <i class="close icon"
       onclick="$(this).closest('.modal').find('audio').each(function() {$(this).stop(); this.pause(); this.currentTime=0})"></i>
    <div class="header">녹취파일확인</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <c:choose>
                <c:when test="${files != null && files.size() > 0}">
                    <c:set var="listeningAuthority"
                           value="${user.listeningRecordingAuthority.equals('MY') && list.personList.id.equals(user.id)
                                 or user.listeningRecordingAuthority.equals('GROUP') && list.personList.groupTreeName.contains(user.groupCode)
                                 or user.listeningRecordingAuthority.equals('ALL')}"/>
                    <c:set var="downloadAuthority"
                           value="${user.downloadRecordingAuthority.equals('MY') && list.personList.id.equals(user.id)
                                 or user.downloadRecordingAuthority.equals('GROUP') && list.personList.groupTreeName.contains(user.groupCode)
                                 or user.downloadRecordingAuthority.equals('ALL')}"/>
                    <c:forEach var="e" items="${files}" varStatus="status">
                        <c:if test="${listeningAuthority}">
                            <audio data-src="${pageContext.request.contextPath}/api/record-file/resource-front-play/${list.seq}/${g.urlEncode(list.uniqueid)}/?partial=${status.index}"
                                   controls class="audio" preload="none"></audio>
                        </c:if>
                        <c:if test="${downloadAuthority}">
                            <div class="center">
                                <a target="_blank"
                                   href="${pageContext.request.contextPath}/api/record-file/resource-front-down/${list.seq}/${g.urlEncode(list.uniqueid)}/${g.urlEncode(list.dstUniqueid)}/?partial=${status.index}">
                                    [ 파일다운로드 ]
                                </a>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    녹취파일이 존재하지 않습니다.
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>



