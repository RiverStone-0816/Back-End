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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/transcribe-group/upload/${entity == null ? null : entity.seq}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">전사그룹[파일업로드]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">파일선택</label></div>
                <div class="thirteen wide column">
                    <div class="file-upload-header">
                        <label for="file" class="ui button blue mini compact">파일찾기</label>
                        <input type="file" id="file" onchange="uploadFile(this.files[0])">
                        <span class="file-name">No file selected</span>
                    </div>
                    <div>
                        <progress value="0" max="100" style="width:100%"></progress>
                    </div>
                    <div class="ui segment file-upload-body">
                        <div class="ui list" data-count="0">

                        </div>
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
    const fileList = modal.find('.file-upload-body .list');

    modal.find('[type="file"]').change(function () {
        uploadFile(this.files[0], modal.find('progress')).done(function (response) {
            modal.find('.file-name').text(response.data.originalName);

            const fileIndex = parseInt(fileList.attr('data-count')) + 1;
            fileList.attr('data-count', fileIndex);

            const fileItem = $('<div class="item"/>').appendTo(fileList);
            fileItem
                .append($('<button type="button" class="ui icon button mini basic white compact"><i class="close icon"></i></button>').click(function () {
                    fileItem.remove();
                }))
                .append($('<text/>', {text: response.data.originalName}))
                .append($('<input/>', {type: 'hidden', name: 'addingFiles[' + fileIndex + '].originalName', value: response.data.originalName}))
                .append($('<input/>', {type: 'hidden', name: 'addingFiles[' + fileIndex + '].fileName', value: response.data.fileName}))
                .append($('<input/>', {type: 'hidden', name: 'addingFiles[' + fileIndex + '].filePath', value: response.data.filePath}));
        });
    });

    modal.find('.-deleting-file').click(function () {
        const fileId = $(this).attr('data-id');
        $(this).closest('.item').remove();
        fileList.append($('<input/>', {type: 'hidden', multiple: 'multiple', name: 'deletingFiles', value: fileId}));
    });

    window.prepareWriteFormData = function (data) {
        if (data.addingFiles)
            data.addingFiles = values(data.addingFiles);
    };
</script>