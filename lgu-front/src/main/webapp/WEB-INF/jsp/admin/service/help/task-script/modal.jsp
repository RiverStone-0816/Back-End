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
           action="${pageContext.request.contextPath}/api/task-script/${entity == null ? null : entity.id}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">지식관리[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column">
                    <label class="control-label">분류</label>
                </div>
                <div class="thirteen wide column">
                    <div class="ui form">
                        <form:select path="categoryId">
                            <form:option value="" label="선택"/>
                            <form:options items="${categories}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">제목</label></div>
                <div class="thirteen wide column">
                    <div class="ui input fluid">
                        <form:input path="title"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">태그</label></div>
                <div class="thirteen wide column">
                    <div class="ui input fluid">
                        <div class="form-group">
                            <form:input path="tag" class="-tagify" placeholder='태그 입력 후 엔터'/>
                        </div>
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
                            <form:textarea path="content" rows="13"/>
                        </div>
                    </div>
                </div>
            </div>
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
                        <div class="ui list" data-count="${entity != null ? entity.fileInfo.size() : 0}">
                            <c:forEach var="file" items="${entity.fileInfo}">
                                <div class="item">
                                    <button type="button" class="ui icon button mini basic white compact -deleting-file" data-id="${file.id}"><i class="close icon"></i></button>
                                    <a target="_blank" href="${apiServerUrl}/api/v1/admin/help/notice/${file.id}/specific-file-resource?token=${accessToken}">${g.htmlQuote(file.originalName)}</a>
                                </div>
                            </c:forEach>
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
