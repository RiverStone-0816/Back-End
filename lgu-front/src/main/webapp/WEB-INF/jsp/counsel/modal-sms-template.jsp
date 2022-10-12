<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>

<form:form modelAttribute="form" id="modal-sms-template-add-popup" cssClass="ui modal inverted tiny -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/sms-message-template">
    <i class="close icon"></i>
    <div class="header">
        SMS 상용문구 등록
    </div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">카테고리</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="categoryCode">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${categories}" itemValue="categoryCode" itemLabel="categoryName"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">내용</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <div class="field">
                            <textarea name="content" rows="5"></textarea>
                        </div>
                    </div>
                </div>
                <div class="sixteen wide column">
                    <button type="submit" class="fluid ui button mini tb-margin">추가</button>
                </div>
            </div>
            <div class="row">
                <table class="ui table celled break">
                    <thead>
                    <tr>
                        <th>카테고리</th>
                        <th>내용</th>
                        <th>등록일</th>
                        <th class="one wide">관리</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${templates.size() > 0}">
                            <c:forEach var="e" items="${templates}">
                                <tr>
                                    <td>${g.htmlQuote(e.categoryName)}</td>
                                    <td>${g.htmlQuote(e.content)}</td>
                                    <td><fmt:formatDate value="${e.createdAt}" pattern="yyyy-MM-dd"/></td>
                                    <td>
                                        <button type="button" class="ui button mini compact" onclick="removeTemplate('${e.id}')">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3">등록된 카테고리가 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">닫기</button>
    </div>
</form:form>