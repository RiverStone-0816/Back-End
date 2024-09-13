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
           action="${pageContext.request.contextPath}/api/preview-group/${entity.seq}/fields/by-excel"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">프리뷰그룹 업로드</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">그룹명</label></div>
                <div class="twelve wide column">${g.htmlQuote(entity.name)}</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">프리뷰 유형</label></div>
                <div class="twelve wide column">${g.htmlQuote(entity.prvTypeName)}</div>
            </div>

            <div class="row">
                <div class="four wide column"><label class="control-label">유형업로드필드</label></div>
                <div class="twelve wide column">
                    <c:forEach var="e" items="${entity.fieldInfoType}">
                        <text style="display: inline-block; margin-right: 1em;">${g.htmlQuote(e.key)}[${g.htmlQuote(e.value)}]</text>
                    </c:forEach>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label label-required">파일선택</label>
                    <i class="circular red info icon small comment-icon" title="※ 파일 업로드 주의사항 ※
                            1. 첫 행 필수 기입
                            2. 해당 프리뷰 그룹의 상담원 설정이 '담당자아이디 사용'으로 되어 있을 경우, '담당자ID' 필드 필수 기입(조건부 필수)
                            3. 필수 기입 필드(빨간 배경) 누락 시, 데이터 업로드가 실패할 수 있습니다.
                            4. 필드명 수정 불가. 필드명 수정 시, 데이터 업로드가 실패할 수 있습니다.
                            5. 모든 셀은 표시 형식이 '텍스트' 형식이어야 합니다."></i>
                </div>
                <div class="twelve wide column">
                    <form:hidden path="fileName"/>
                    <form:hidden path="filePath"/>
                    <form:hidden path="originalName"/>
                    <div class="file-upload-header">
                        <label class="ui button blue mini compact" onclick="downloadExampleExcel('${entity.seq}')">
                            예시 Excel 다운로드
                        </label>
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
