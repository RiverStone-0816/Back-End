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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/preview-data/${groupSeq}/data/redistribution"
           data-before="prepareRedistributionForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">프리뷰 재분배 설정</div>

    <div class="scrolling content rows">
        <c:forEach var="e" items="${form.customIdList}">
            <input type="hidden" name="customIdList" value="${e}" multiple="multiple"/>
        </c:forEach>

        <div class="ui grid">
            <div class="row">
                <div class="eight wide column"><label class="control-label">사용자 리스트</label></div>
                <div class="eight wide column"><label class="control-label">분배 받을 사용자</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.id)}">${g.htmlQuote(e.id)} ${g.htmlQuote(e.idName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="userIdList" class="form-control -left-selector" size="8" multiple="multiple"></select>
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
    window.prepareRedistributionForm = function (data) {
        data.userIdList = [];
        modal.find('[name="userIdList"] option').each(function () {
            data.userIdList.push($(this).val());
        });
    };
</script>
