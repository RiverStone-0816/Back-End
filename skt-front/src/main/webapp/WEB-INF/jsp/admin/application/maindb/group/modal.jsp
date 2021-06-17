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
           action="${pageContext.request.contextPath}/api/maindb-group/${entity == null ? null : entity.seq}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">고객정보그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">그룹명</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini orange compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
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
                <div class="four wide column"><label class="control-label">고객정보유형</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="maindbType">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${maindbTypes}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">상담결과유형</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="resultType">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${resultTypes}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <c:if test="${entity == null}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">데이터중복체크여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isDupUse" value="Y"/>
                                        <label>중복체크</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isDupUse" value="N" />
                                        <label>중복체크하지않음</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row -dup-is-update-data" data-value="Y">
                    <div class="four wide column"><label class="control-label">중복체크항목</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <c:forEach var="e" items="${dupKeyKTypes}">
                                    <div class="field">
                                        <div class="ui radio checkbox">
                                            <form:radiobutton path="dupKeyKind" value="${e.key}"/>
                                            <label>${g.htmlQuote(e.value)}</label>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <form:select path="dupNeedField">
                                <form:option value="" label="필수항목선택"/>
                                <c:forEach var="e" items="${maindbFields}">
									<form:option value="${e.fieldId}" label="${e.fieldInfo}" data-type="${e.type}"/>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="row -dup-is-update-data" data-value="Y">
                    <div class="four wide column"><label class="control-label">업로드중복처리방법</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="dupIsUpdate" value="Y"/>
                                        <label>해당데이터업데이트(UPDATE)</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="dupIsUpdate" value="N"/>
                                        <label>처리안함(SKIP)</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="row">
                <div class="four wide column"><label class="control-label">추가정보</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="info"/>
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
    window.prepareWriteFormData = function (data) {
        if (data.isDupUse === 'N') {
            delete data.dupIsUpdate;
            delete data.dupKeyKind;
            delete data.dupNeedField;
        }
    };

    modal.find('[name=maindbType]').change(changeDupNeedFiled);
    changeDupNeedFiled();
    modal.find('[name=isDupUse]').change(showIsDupBox);
    showIsDupBox();
    modal.find('[name=dupKeyKind]').change(showDupKeyKindBox);
    showDupKeyKindBox();
    //TODO: ie issue 익스에서 제대로 변경이안됨...
    function changeDupNeedFiled() {
        const selectedValue = modal.find('[name=maindbType] option:selected').val();
        modal.find('[name=dupNeedField]').val('').find('option').filter(function() {return $(this).val() !== '';}).hide().each(function () {
            if (selectedValue == $(this).data('type')) // string == integer
                $(this).show();
        });
    }

    function showIsDupBox() {
        const isDupUse = modal.find('[name=isDupUse]:checked').val();
        modal.find('.-dup-is-update-data').hide().filter(function () {
            return isDupUse === $(this).attr('data-value');
        }).show();
    }

    function showDupKeyKindBox() {
        const dupKeyKind = modal.find('[name=dupKeyKind]:checked').val();
        modal.find('[name=dupNeedField]').val('').hide().filter(function () {
            return dupKeyKind !== 'NUM';
        }).show();
    }
</script>
