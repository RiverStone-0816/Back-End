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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/talk-reception-group/${entity == null ? null : entity.groupId}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">상담톡그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">상담톡그룹명</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:input path="groupName"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">관련상담톡서비스</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="senderKey">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${talkServices}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">사용자 리스트</label></div>
                <div class="eight wide column"><label class="control-label">추가된 사용자</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.key)} (${g.htmlQuote(e.value)})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="personIds" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.persons}">
                                    <option value="${g.htmlQuote(e.id)}">${g.htmlQuote(e.id)} (${g.htmlQuote(e.idName)})</option>
                                </c:forEach>
                            </select>
                        </div>
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
        data.personIds = [];
        modal.find('[name="personIds"] option').each(function () {
            data.personIds.push($(this).val());
        });
    };
</script>
