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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/research-item/${entity == null ? null : entity.seq}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">설문문항[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid -answer-container">
            <div class="row">
                <div class="four wide column"><label class="control-label">문항제목</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="itemName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서조회</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini orange compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${entity != null}">
                                    <c:forEach var="e" items="${entity.companyTrees}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e.groupName)}</span>
                                        <c:if test="${!status.last}">
                                            <i class="right angle icon divider"></i>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="section">부서를 선택해 주세요.</span>
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
                <div class="four wide column"><label class="control-label">질문</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:textarea path="word" rows="3"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">음원(ARS설문시 사용)</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <c:forEach var="e" items="${soundKinds}">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="soundKind" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                                <c:if test="${e.key == 'S'}">
                                    <div class="ui form">
                                        <form:select path="soundCode" items="${sounds}"/>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">답변개수</label></div>
                <div class="twelve wide column">
                    <div class="ui action input">
                        <input type="text" placeholder="개수입력" value="${entity != null && entity.answers != null ? entity.answers.size() : ''}">
                        <button type="button" class="ui button -button-create-answer-inputs">생성</button>
                    </div>
                </div>
            </div>

            <c:forEach var="answer" items="${entity.answers}" varStatus="answerStatus">
                <div class="row -answer-row">
                    <div class="four wide column"><label class="control-label -answer-number">답변${answerStatus.index + 1}</label></div>
                    <div class="twelve wide column">
                        <div class="ui action fluid input">
                            <input type="text" name="answerRequests" placeholder="답변내용입력" multiple="multiple" value="${g.htmlQuote(answer)}"/>
                            <button type="button" class="ui icon button -button-remove-answer"><i class="minus icon"></i></button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    const answerContainer = modal.find('.-answer-container');

    modal.on('click', '.-button-remove-answer', function (event) {
        $(event.target).closest('.-answer-row').remove();
        const answerNumbers = modal.find('.-answer-row .-answer-number');
        for (let i = 0; i < answerNumbers.length; i++)
            $(answerNumbers[i]).text('답변' + (i + 1));
    });

    modal.find('.-button-create-answer-inputs').click(function () {
        const answerCount = parseInt($(this).prev().val());
        if (isNaN(answerCount) || !isFinite(answerCount)) return;
        <c:if test="${serviceKind.equals('CC')}">
        if (Number(answerCount) > 5) {
            alert("답변은 [5]개를 넘을 수 없습니다.");
            return;
        }
        </c:if>
        const answerRows = modal.find('.-answer-row');
        for (let i = answerRows.length - 1; i >= 0; i--)
            if (i >= answerCount)
                $(answerRows[i]).remove();

        for (let i = answerRows.length; i < answerCount; i++)
            answerContainer.append('<div class="row -answer-row">' +
                '<div class="four wide column"><label class="control-label -answer-number">답변' + (i + 1) + '</label></div>' +
                '<div class="twelve wide column">' +
                '<div class="ui action fluid input">' +
                '<input type="text" name="answerRequests" placeholder="답변내용입력" multiple="multiple"/>' +
                '<button type="button" class="ui icon button -button-remove-answer"><i class="minus icon"></i></button>' +
                '</div>' +
                '</div>' +
                '</div>');
    });
</script>
