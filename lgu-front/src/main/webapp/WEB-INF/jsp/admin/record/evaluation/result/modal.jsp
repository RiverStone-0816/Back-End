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

<div class="ui modal large">
    <i class="close icon"></i>
    <div class="header">상세보기</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="sixteen wide column">
                <div class="scrolling content pr15">
                    <div>
                        <div id="evaluation-form">
                            <table class="ui celled table compact unstackable structured relative">
                                <thead>
                                <tr>
                                    <th>분류</th>
                                    <th>항목명</th>
                                    <th>평가기준</th>
                                    <th style="width: 100px;">점수</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${entity.form.categories != null && entity.form.categories.size() > 0}">
                                        <c:forEach var="category" items="${entity.form.categories}">
                                            <c:forEach var="item" items="${category.items}" varStatus="itemStatus">
                                                <tr>
                                                    <c:if test="${itemStatus.first}">
                                                        <td rowspan="${category.items.size()}">${g.htmlQuote(category.name)}</td>
                                                    </c:if>
                                                    <td>${g.htmlQuote(item.name)}</td>
                                                    <td>
                                                        <c:if test="${item.remark != null && item.remark != ''}">
                                                            <div class="tooltip rating-description">
                                                                <i class="question circle icon" style="overflow: initial"></i>
                                                                <span class="tooltiptext" style="white-space: pre-wrap">${g.htmlQuote(item.remark)}</span>
                                                            </div>
                                                        </c:if>
                                                        <div style="white-space: pre-wrap;">${g.htmlQuote(item.valuationBasis)}</div>
                                                    </td>
                                                    <td>
                                                        <div class="ui form fluid">
                                                            <input type="text" name="scores.${item.id}" data-name="score" data-max="${item.maxScore}" value="${itemToScore.get(item.id)}"
                                                                   class="-input-numerical" readonly/>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4">평가항목이 등록되지 않았습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div style="margin-top:10px">
                        <table class="ui celled table compact unstackable two">
                            <tbody>
                            <tr>
                                <th>메모</th>
                                <td>
                                    <div class="ui form">
                                        <div class="field">
                                            <textarea rows="3" readonly>${g.htmlQuote(entity.memo)}</textarea>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${entity.challengeMemo != null && entity.challengeMemo != ''}">
                                <tr>
                                    <th>이의</th>
                                    <td>
                                        <div class="ui form">
                                            <div class="field">
                                                <textarea rows="3" readonly>${g.htmlQuote(entity.challengeMemo)}</textarea>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button modal-close">확인</button>
    </div>
</div>

<script>
    modal.find('[data-name=score][data-max]').on('focusout, blur', function () {
        const score = parseInt($(this).val());
        const max = parseInt($(this).attr('data-max'));
        $(this).val(isNaN(score) || !isFinite(score) ? 0 : score > max ? max : score);
    });
</script>
