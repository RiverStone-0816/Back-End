<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form class="ui modal -json-submit" data-method="post"
      action="${pageContext.request.contextPath}/api/research/${entity.researchId}/scenario"
      data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">설문시나리오[설정]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">설문제목</label></div>
                <div class="twelve wide column">
                    <div class="ui form flex">
                        <select name="researchItem">
                            <c:forEach var="e" items="${items}">
                                <option value="${e.itemId}">${g.htmlQuote(e.itemName)}</option>
                            </c:forEach>
                        </select>
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="attachType" value="TO_QUESTION" checked>
                                    <label>전체답변같은설문</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="attachType" value="TO_ANSWER">
                                    <label>답변별로다른설문</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">
                        <text>${g.htmlQuote(entity.researchName)}</text>
                        <button type="button" class="ui basic button pull-right -replace-root">시작설문변경</button>
                    </h4>
                </div>
            </div>
            <div class="row research-container">
                <ul class="research-list">
                    <c:if test="${scenario != null}">
                        <c:choose>
                            <c:when test="${scenario.path == ''}">
                                <li>
                                    <span class="ui right pointing basic mini label">1단계</span> ${g.htmlQuote(scenario.word)}
                                </li>
                                <c:forEach var="answer" items="${scenario.answerItems}" varStatus="answerStatus">
                                    <li>
                                        <span class="ui right pointing basic mini label">답변${answerStatus.index + 1}</span> ${g.htmlQuote(answer.word)}
                                        <button type="button" class="ui button basic mini -attach-to-answer" data-level="${answer.level}" data-path="${g.htmlQuote(answer.path)}">
                                            <i class="file icon"></i></button>
                                        <c:if test="${answer.child != null}">
                                            <ul class="scenario-list">
                                                <tags:scenario-item item="${answer.child}"/>
                                            </ul>
                                        </c:if>
                                    </li>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tags:scenario-item item="${scenario}"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form>

<script>
    const ATTACH_TYPE = {TO_QUESTION: 'TO_QUESTION', TO_ANSWER: 'TO_ANSWER'};

    const researchId = ${entity.researchId};
    const researchItems = {
        <c:forEach var="e" items="${items}">
        '${e.itemId}': {
            itemId: ${e.itemId},
            itemName: '${g.escapeQuote(e.itemName)}',
            word: '${g.escapeQuote(e.word)}',
            answers: [<c:forEach var="answer" items="${e.answers}">'${g.escapeQuote(answer)}', </c:forEach>],
        },
        </c:forEach>
    };
    const researchTreeContainer = $('.research-list');

    window.prepareWriteFormData = function (data) {
        delete data.researchItem;
        delete data.attachType;

        modal.find('[data-path]').sort().each(function () {
            const treeCodes = convertPathToTreeCodes($(this).attr('data-path'));

            let processingForm = data;
            treeCodes.map(function (treeCode, index) {
                processingForm.itemId = treeCode.itemId;
                processingForm.hasableAnswersChildTree = treeCode.answerNumber > 0;

                if (processingForm.hasableAnswersChildTree && !processingForm.childrenMappedByAnswerNumber)
                    processingForm.childrenMappedByAnswerNumber = {};

                if (index + 1 < treeCodes.length)
                    if (treeCode.answerNumber > 0) {
                        if (!processingForm.childrenMappedByAnswerNumber[treeCode.answerNumber]) {
                            const newProcessingForm = {};
                            processingForm.childrenMappedByAnswerNumber[treeCode.answerNumber] = newProcessingForm;
                            processingForm = newProcessingForm;
                        } else {
                            processingForm = processingForm.childrenMappedByAnswerNumber[treeCode.answerNumber];
                        }
                    } else {
                        if (!processingForm.childNotMappedByAnswerNumber) {
                            const newProcessingForm = {};
                            processingForm.childNotMappedByAnswerNumber = newProcessingForm;
                            processingForm = newProcessingForm;
                        } else {
                            processingForm = processingForm.childNotMappedByAnswerNumber;
                        }
                    }
            });
        });

        console.log(data);
    };

    modal.find('.-replace-root').click(function () {
        attachItem(researchTreeContainer, 1, "0");
    });

    modal.on('click', '.-attach-to-question', function (event) {
        const button = $(event.target).closest('button');
        const parentLevel = parseInt(button.attr('data-level'));
        const parentPath = button.attr('data-path');

        const ul = button.closest('ul');
        ul.children('.-research-item').remove();
        const container = $('<ul/>', {class: "scenario-list"});
        ul.append($('<li/>', {class: '-research-item'}).append(container));
        attachItem(container, parentLevel + 1, parentPath);
    });

    modal.on('click', '.-attach-to-answer', function (event) {
        const button = $(event.target).closest('button');
        const parentLevel = parseInt(button.attr('data-level'));
        const parentPath = button.attr('data-path');

        button.next('.scenario-list').remove();
        const container = $('<ul/>', {class: "scenario-list"}).insertAfter(button);
        attachItem(container, parentLevel + 1, parentPath);
    });

    function attachItem(container, level, parentPath) {
        const item = getSelectedResearchItem();
        container.empty();

        if (getAttachType() === ATTACH_TYPE.TO_QUESTION || !item.answers.length) {
            container.append($('<li/>')
                .append($('<span/>', {class: 'ui right pointing basic mini label', text: level + '단계'}))
                .append($('<text/>', {text: item.word}))
                .append($('<button/>', {
                    type: 'button',
                    class: "ui button basic mini -attach-to-question",
                    'data-level': level,
                    'data-path': parentPath + '-' + researchId + '_' + level + '_' + item.itemId + '_0',
                    html: '<i class="file icon"></i>'
                }))
            );
            item.answers.map(function (answer, i) {
                container.append($('<li/>')
                    .append($('<span/>', {class: 'ui right pointing basic mini label', text: '답변' + (i + 1)}))
                    .append($('<text/>', {text: answer}))
                );
            });
        } else {
            container.append($('<li/>')
                .append($('<span/>', {class: 'ui right pointing basic mini label', text: level + '단계'}))
                .append($('<text/>', {text: item.word}))
            );
            item.answers.map(function (answer, i) {
                container.append($('<li/>')
                    .append($('<span/>', {class: 'ui right pointing basic mini label', text: '답변' + (i + 1)}))
                    .append($('<text/>', {text: answer}))
                    .append($('<button/>', {
                        type: 'button',
                        class: "ui button basic mini -attach-to-answer",
                        'data-level': level,
                        'data-path': parentPath + '-' + researchId + '_' + level + '_' + item.itemId + '_' + (i + 1),
                        html: '<i class="file icon"></i>'
                    }))
                );
            });
        }
    }

    function TreeCode(code) {
        const splices = code.split('_');

        this.researchId = parseInt(splices[0]);
        this.level = parseInt(splices[1]);
        this.itemId = parseInt(splices[2]);
        this.answerNumber = parseInt(splices[3]);
        this.code = code;

        return this;
    }

    function convertPathToTreeCodes(path) {
        const result = [];
        const splices = path.split('-');
        for (let i = 1; i < splices.length; i++)
            result.push(new TreeCode(splices[i]));
        return result;
    }

    function getAttachType() {
        return modal.find('[name=attachType]:checked').val();
    }

    function getSelectedResearchItem() {
        const item = researchItems[modal.find('[name=researchItem]').val()];
        if (!item)
            throw 'None selected research item.';
        return item;
    }
</script>
