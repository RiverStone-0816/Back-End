<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <form:form commandName="form" class="ui modal window tiny -json-submit" data-method="put"
               action="${pageContext.request.contextPath}/api/memo/profile" data-done="doneSubmit">
        <div class="inner-box bb-unset">
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
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui black button basic tiny close" onclick="closePopup()">닫기</button>
            <button type="submit" class="ui darkblue button tiny">적용</button>
        </div>
    </form:form>

    <tags:scripts>
        <script>
            $('#file').change(function () {
                uploadFile(this.files[0]).done(function (response) {
                    $('.file-name').text(response.data.originalName);
                    $('[name=originalName]').val(response.data.originalName);
                    $('[name=fileName]').val(response.data.fileName);
                    $('[name=filePath]').val(response.data.filePath);
                });
            });

            function closePopup() {
                (self.opener = self).close();
            }

            function doneSubmit() {
                alert('변경되었습니다.', function () {
                    const reader = new FileReader();
                    reader.onload = function () {
                        if (window.isElectron) {
                            window.ipcRenderer.send("profileChange", {"src": reader.result, "userId": '${user.id}'});
                        } else {
                            $(window.opener.document).find('.-picture[data-id="${user.id}"]').attr('src', reader.result);
                        }
                        closePopup();
                    };
                    reader.readAsDataURL($('#file')[0].files[0]);
                });
            }
        </script>
    </tags:scripts>
</mobileTags:layout>
