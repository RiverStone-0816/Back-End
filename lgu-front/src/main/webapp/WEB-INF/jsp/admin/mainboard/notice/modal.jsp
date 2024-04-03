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
    <i class="close icon"></i>
    <div class="header">프리미엄공지</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">제목</label></div>
                <div class="thirteen wide column">
                    <div class="ui input fluid">
                        ${entity.title}
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column">
                    <label class="control-label">내용</label>
                </div>
                <div class="thirteen wide column">
                    <div class="ui form">
                        <div class="field">
                            ${g.htmlQuote(entity.content)}
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${entity.mainBoardFiles.size() > 0}">
            <div class="row">
                <div class="three wide column">
                    <label class="control-label">첨부</label>
                </div>
                <div class="thirteen wide column">
                    <div class="ui segment file-upload-body">
                        <div class="ui list" data-count="${entity != null ? entity.mainBoardFiles.size() : 0}">
                            <c:forEach var="file" items="${entity.mainBoardFiles}">
                                <div class="item">
                                    <button type="button" class="ui icon button mini basic white compact -deleting-file" data-id="${file.fileId}"><i class="close icon"></i></button>
                                    <a target="_blank" href="${pageContext.request.contextPath}/api/notice/id/${file.fileId}/resource">${g.htmlQuote(file.originalName)}</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            </c:if>
        </div>
    </div>
</div>

