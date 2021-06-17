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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="post" action="${pageContext.request.contextPath}/api/ring-back-tone/" data-done="reload">

    <i class="close icon"></i>
    <div class="header">컬러링 음원추가</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">음원명</label></div>
                <div class="thirteen wide column">
                    <div class="ui input fluid">
                        <form:input path="mohName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">파일선택</label></div>
                <div class="thirteen wide column">
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
