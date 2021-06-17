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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/mail-file/${entity == null ? null : entity.id}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">발송물[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">카테고리</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <c:choose>
                            <c:when test="${entity == null}">
                                <form:select path="categoryCode">
                                    <form:option value="" label="선택안함"/>
                                    <c:forEach var="category" items="${categories}">
                                        <c:if test="${category.key.startsWith('E')}">
                                            <form:option value="${category.key}">[EMAIL] ${g.htmlQuote(category.value)}</form:option>
                                        </c:if>
                                    </c:forEach>
                                    <c:forEach var="category" items="${categories}">
                                        <c:if test="${category.key.startsWith('F')}">
                                            <form:option value="${category.key}">[FAX] ${g.htmlQuote(category.value)}</form:option>
                                        </c:if>
                                    </c:forEach>
                                </form:select>
                            </c:when>
                            <c:otherwise>
                                [${g.htmlQuote(message.getEnumText(entity.sendMedia))}] ${g.htmlQuote(entity.categoryCode)}
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">발송물 이름</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <div class="ui input fluid">
                            <form:input path="name"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">발송물 설명</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <div class="field">
                            <form:textarea path="desc" rows="2"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">파일선택</label></div>
                <div class="twelve wide column">
                    <form:hidden path="fileName"/>
                    <form:hidden path="filePath"/>
                    <form:hidden path="originalName"/>
                    <div class="file-upload-header">
                        <label for="file" class="ui button blue mini compact">파일찾기</label>
                        <input type="file" id="file">
                        <span class="file-name">No file selected</span>
                    </div>
                    <div>
                        <progress value="0" max="100" style="width:100%"></progress>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    modal.find('[type="file"]').change(function () {
        uploadFile(this.files[0], modal.find('progress')).done(function (response) {
            modal.find('.file-name').text(response.data.originalName);
            modal.find('[name=originalName]').val(response.data.originalName);
            modal.find('[name=fileName]').val(response.data.fileName);
            modal.find('[name=filePath]').val(response.data.filePath);
        });
    });
</script>
