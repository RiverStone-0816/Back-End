<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form commandName="form" cssClass="ui modal tiny -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/route-app/${entity.seq}/accept"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">신청 처리</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">번호</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        ${g.htmlQuote(entity.number)}
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">우선순위부여방법</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="routeType" class="hidden" value="A"/>
                                    <label>${entity.type.equals('VIP') ? '우선순위 부여' : '차단'}</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="routeType" class="hidden" value="B"/>
                                    <label>특정큐 선택</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="ui form">
                        <form:select path="routeQueueNumber" items="${queues}"/>
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
    modal.find('[name=routeType]').change(function () {
        const type = modal.find('[name=routeType]:checked').val();
        const input = modal.find('[name=routeQueueNumber]');

        if (type === 'B')
            input.show();
        else
            input.hide();
    }).change();
</script>