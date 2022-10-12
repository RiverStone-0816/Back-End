<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>

<form:form modelAttribute="form" id="modal-sms-category-add-popup" cssClass="ui modal inverted tiny -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/sms-category">
    <i class="close icon"></i>
    <div class="header">
        SMS 카테고리 등록
    </div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <form:hidden path="categoryType" value="S"/>
                <div class="four wide column"><label class="control-label">카테고리 코드</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="categoryCode"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">카테고리 명</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="categoryName"/>
                    </div>
                </div>
                <div class="sixteen wide column">
                    <button type="submit" class="fluid ui button mini tb-margin">추가</button>
                </div>
            </div>
            <div class="row">
                <table class="ui table celled">
                    <thead>
                    <tr>
                        <th>코드</th>
                        <th>카테고리명</th>
                        <th class="one wide">관리</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${categories.size() > 0}">
                            <c:forEach var="e" items="${categories}">
                                <tr>
                                    <td>${g.htmlQuote(e.categoryCode)}</td>
                                    <td>${g.htmlQuote(e.categoryName)}</td>
                                    <td>
                                        <button type="button" class="ui button mini compact" onclick="removeCategory('${e.categoryCode}')">삭제</button>
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