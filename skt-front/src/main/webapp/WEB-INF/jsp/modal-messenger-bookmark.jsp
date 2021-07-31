<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/chatt/bookmark"
           data-before="prepareWriteMessengerBookmarkFormData" data-done="doneSubmitMessengerBookmarkFormData">

    <i class="close icon"></i>
    <div class="header">즐겨찾기 편집</div>
    <div class="content">
        <div class="ui grid">
            <div class="row remove-pb">
                <div class="eight wide column align-center"><label class="control-label">사용자 리스트</label></div>
                <div class="eight wide column align-center"><label class="control-label">추가된 사용자</label></div>
            </div>
            <div class="row remove-pt">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)} (${g.htmlQuote(e.key)})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="memberList" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${memberList}">
                                    <option value="${g.htmlQuote(e.id)}">${g.htmlQuote(e.idName)} (${g.htmlQuote(e.id)})</option>
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
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteMessengerBookmarkFormData = function (data) {
        data.memberList = [];
        modal.find('[name="memberList"] option').each(function () {
            data.memberList.push($(this).val());
        });
    };

    window.doneSubmitMessengerBookmarkFormData = function () {
        modal.find('.modal-close:first').click();
        messenger.loadBookmarks();
    };
</script>
