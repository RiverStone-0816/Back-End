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

<form:form modelAttribute="form" cssClass="ui modal small -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/enumeration-value/${type}/${g.htmlQuote(fieldId)}/codes/by-excel"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">코드 업로드</div>

    <div class="content rows scrolling">
        <div class="ui grid">
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
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    $('#file').change(function () {
        uploadFileExcel(this.files[0], modal.find('progress')).done(function (response) {
            modal.find('.file-name').text(response.data.originalName);
            modal.find('[name=originalName]').val(response.data.originalName);
            modal.find('[name=fileName]').val(response.data.fileName);
            modal.find('[name=filePath]').val(response.data.filePath);
        });
    });
</script>