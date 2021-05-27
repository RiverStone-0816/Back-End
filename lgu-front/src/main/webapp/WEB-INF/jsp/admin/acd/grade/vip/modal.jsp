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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/gradelist/${entity == null ? null : entity.seq}"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">VIP[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">번호</label></div>
                <div class="twelve wide column">
                    <form:hidden path="grade"/>
                    <div class="ui input fluid"><form:input path="gradeNumber"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">우선순위부여방법</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="type" class="hidden" value="A"/>
                                    <label>우선순위 부여</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="type" class="hidden" value="B"/>
                                    <label>특정큐 선택</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="ui form">
                        <form:select path="queueNumber">
                            <form:option value="" label="선택안함"/>
                            <c:forEach var="e" items="${queues}">
                                <form:option value="${g.htmlQuote(e.key)}" label="${g.htmlQuote(e.value)}(${g.htmlQuote(e.key)})"/>
                            </c:forEach>
                        </form:select>
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
    modal.find('[name=type]').change(function () {
        const type = modal.find('[name=type]:checked').val();
        const input = modal.find('[name=queueNumber]');

        if (type === 'B')
            input.show();
        else
            input.hide();
    }).change();
</script>
