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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/extension-extra-info/cid"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">CID 일괄적용</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <c:forEach var="peer" items="${form.peers}">
                <input type="hidden" name="peers" value="${g.htmlQuote(peer)}" multiple/>
            </c:forEach>
            <div class="row">
                <div class="three wide column"><label class="control-label">CID</label></div>
                <div class="six wide column">
                    <div class="ui input fluid">
                        <form:select path="cid" items="${cidInfos}"/>
                    </div>
                </div>
                <div class="seven wide column">
                    <span class="message text-red">[주의] 체크되어있는 내선번호에 모두 적용됩니다.</span>
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
