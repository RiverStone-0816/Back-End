<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/cs-route/${entity == null ? null : entity.seq}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">직전통화상담원연결라우팅[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">큐번호선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="queueNumber">
                            <form:option value="" label="선택안함"/>
                            <c:forEach var="e" items="${queues}">
                                <option value="${g.htmlQuote(e.key)}" ${e.key.equals(entity.queueNumber) ? 'selected' : ''}>${g.htmlQuote(e.value)}(${g.htmlQuote(e.key)})</option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">검색주기</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="cycle" value="DAY" class="hidden"/>
                                    <label>1일</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="cycle" value="WEEK" class="hidden"/>
                                    <label>1주</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="cycle" value="MONTH" class="hidden"/>
                                    <label>1개월</label>
                                </div>
                            </div>
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

</script>
