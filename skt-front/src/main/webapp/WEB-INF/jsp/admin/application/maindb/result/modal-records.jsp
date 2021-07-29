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

<div class="ui modal xsmall">
    <div class="header">녹취 듣기</div>
    <i class="close icon"></i>

    <div class="content">
        <div class="ui grid">
            <div class="sixteen wide column">
                <c:choose>
                    <c:when test="${files != null && files.size() > 0}">
                        <c:forEach var="e" items="${files}">
                            <c:if test="${user.listeningRecordingAuthority.equals('MY') && entity.personList.id.equals(user.id)}">
                                <audio controls src="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=PLAY&token=${accessToken}" class="audio"
                                       preload="none"></audio>
                                <c:if test="${!user.downloadRecordingAuthority.equals('NO')}">
                                    <div class="mt15 align-center"><a target="_blank" href="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=DOWN&token=${accessToken}">[
                                        파일다운로드 ]</a></div>
                                </c:if>
                            </c:if>
                            <c:if test="${user.listeningRecordingAuthority.equals('GROUP') && entity.personList.groupTreeName.contains(user.groupCode)}">
                                <audio controls src="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=PLAY&token=${accessToken}" class="audio"
                                       preload="none"></audio>
                                <c:if test="${user.downloadRecordingAuthority.equals('MY') && entity.personList.id.equals(user.id)}">
                                    <div class="mt15 align-center"><a target="_blank" href="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=DOWN&token=${accessToken}">[
                                        파일다운로드 ]</a></div>
                                </c:if>
                                <c:if test="${user.downloadRecordingAuthority.equals('GROUP') && entity.personList.groupTreeName.contains(user.groupCode)}">
                                    <div class="mt15 align-center"><a target="_blank" href="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=DOWN&token=${accessToken}">[
                                        파일다운로드 ]</a></div>
                                </c:if>
                            </c:if>
                            <c:if test="${user.listeningRecordingAuthority.equals('ALL')}">
                                <audio controls src="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=PLAY&token=${accessToken}" class="audio"
                                       preload="none"></audio>
                                <c:if test="${user.downloadRecordingAuthority.equals('MY') && entity.personList.id.equals(user.id)}">
                                    <div class="mt15 align-center"><a target="_blank" href="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=DOWN&token=${accessToken}">[
                                        파일다운로드 ]</a></div>
                                </c:if>
                                <c:if test="${user.downloadRecordingAuthority.equals('GROUP') && entity.personList.groupTreeName.contains(user.groupCode)}">
                                    <div class="mt15 align-center"><a target="_blank" href="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=DOWN&token=${accessToken}">[
                                        파일다운로드 ]</a></div>
                                </c:if>
                                <c:if test="${user.downloadRecordingAuthority.equals('ALL')}">
                                    <div class="mt15 align-center"><a target="_blank" href="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=DOWN&token=${accessToken}">[
                                        파일다운로드 ]</a></div>
                                </c:if>
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
    <div class="footer"></div>
</div>
