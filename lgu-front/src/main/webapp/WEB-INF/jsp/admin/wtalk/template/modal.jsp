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

<form:form modelAttribute="form" cssClass="ui modal small -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/wtalk-template/${entity == null ? null : entity.seq}"
           data-before="prepareTalkTemplateForm" data-done="reload">

    <form:hidden path="newFile"/>

    <i class="close icon"></i>
    <div class="header">상담톡템플릿[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">권한</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="type" items="${templateTypes}"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">유형</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="typeMent">
                            <form:option value="TEXT" label="텍스트"/>
                            <form:option value="PHOTO" label="이미지"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row -group-selector-container">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=typeData]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="typeData"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e)}</span>
                                        <c:if test="${!status.last}">
                                            <i class="right angle icon divider"></i>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">템플릿명</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="mentName"/>
                    </div>
                </div>
            </div>
            <div class="row -typement-inputs" data-typement="TEXT">
                <div class="four wide column"><label class="control-label">템플릿멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="align-right"><label>500자 이하</label></div>
                        <form:textarea path="ment" row="10" maxLength="500"/>
                    </div>
                </div>
            </div>
            <div class="row -typement-inputs" data-typement="PHOTO">
                <div class="four wide column"><label class="control-label">이미지</label></div>
                <div class="twelve wide column">
                    <form:hidden path="originalFileName"/>
                    <form:hidden path="filePath"/>
                    <div class="file-upload-header">
                        <label class="ui button blue mini compact" onclick="this.nextElementSibling.click()">파일찾기</label>
                        <input type="file">
                        <span class="file-name">${form.originalFileName != null && form.originalFileName != '' ? g.htmlQuote(form.originalFileName) : 'No file selected'}</span>
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
    modal.find('[name=type]').change(function () {
        if ($(this).val() === 'G') {
            modal.find('.-group-selector-container').show();
        } else {
            modal.find('.-group-selector-container').hide();
        }
    }).change();

    modal.find('[name="typeMent"]').change(function () {
        const typeMent = $(this).val()
        modal.find('.-typement-inputs').hide().filter(function () {
            return $(this).attr('data-typement') === typeMent
        }).show()
    }).change();

    modal.find('[type="file"]').change(function () {
        const _this = this
        uploadFile(this.files[0], modal.find('progress')).done(function (response) {
            modal.find('.file-name').text(response.data.originalName);
            modal.find('[name=originalFileName]').val(response.data.originalName);
            modal.find('[name=filePath]').val(response.data.filePath);
            modal.find('[name=newFile]').val(true);
            _this.value = null
        });
    })

    window.prepareTalkTemplateForm = function (data) {
        if (data.type === 'P')
            data.typeData = '${g.user.id}';
        if (data.type === 'C')
            data.typeData = '${g.user.companyId}';
    };
</script>
