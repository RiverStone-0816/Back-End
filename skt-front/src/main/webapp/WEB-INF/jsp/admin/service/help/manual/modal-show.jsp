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

<div class="ui modal" id="modal-board-view">
    <i class="close icon" onclick="reload()"></i>
    <div class="header">글확인</div>
    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">제목</label></div>
                <div class="thirteen wide column">${g.htmlQuote(entity.title)}</div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">작성자</label></div>
                <div class="three wide column">${g.htmlQuote(entity.writer)}</div>
                <div class="three wide column"><label class="control-label">등록일</label></div>
                <div class="three wide column"><fmt:formatDate value="${entity.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                <div class="two wide column"><label class="control-label">조회수</label></div>
                <div class="two wide column">${entity.viewCnt}</div>
            </div>
            <c:if test="${entity.fileInfo != null && entity.fileInfo.size() > 0}">
                <div class="row">
                    <div class="three wide column">
                        <label class="control-label">첨부파일</label>
                    </div>
                    <div class="thirteen wide column">
                        <div class="ui list filelist">
                            <c:forEach var="e" items="${entity.fileInfo}">
                                <div class="item">
                                    <i class="file alternate outline icon"></i>
                                    <div class="content">
                                        <a href="${apiServerUrl}/api/v1/admin/help/notice/${e.id}/specific-file-resource?token=${accessToken}" target="_blank">
                                                ${g.htmlQuote(e.originalName)}
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>
