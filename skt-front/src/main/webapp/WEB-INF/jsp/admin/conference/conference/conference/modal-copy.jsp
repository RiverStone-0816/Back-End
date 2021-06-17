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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/conference/copy"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">회의실예약[추가]</div>

    <div class="scrolling content rows">
        <form:hidden path="targetSeq"/>
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">회의실</label></div>
                <div class="twelve wide column">
                    <form:select path="roomNumber" items="${rooms}"/>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의날짜</label></div>
                <div class="twelve wide column">
                    <form:input path="reserveDate" cssClass="-datepicker"/>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">예약시간</label></div>
                <div class="seven wide column">
                    <div class="ui form">
                        <select class="input-small-size time" name="fromHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.reserveFromTime / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="fromMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.reserveFromTime % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <span class="tilde">~</span>
                        <select class="input-small-size time" name="toHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.reservetoTime / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="toMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.reservetoTime % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <button class="ui button mini -check-duplicated-time" type="button">중복확인</button>
                    </div>
                </div>
                <div class="five wide column">
                    <span class="message text-green -express-usable-extension" style="display: none">사용가능</span>
                    <span class="message text-red -express-unusable-extension" style="display: none">사용불가</span>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column align-center">
                    복사한 회의를 붙여넣으시겠습니까?
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.reserveFromTime = parseInt(data.fromHour) * 60 + parseInt(data.fromMinute);
        data.reserveToTime = parseInt(data.toHour) * 60 + parseInt(data.toMinute);

        delete data.fromHour;
        delete data.fromMinute;
        delete data.toHour;
        delete data.toMinute;
    };

    modal.find('.-check-duplicated-time').click(function () {
        const fromHour = modal.find('[name=fromHour]').val();
        const fromMinute = modal.find('[name=fromMinute]').val();
        const toHour = modal.find('[name=toHour]').val();
        const toMinute = modal.find('[name=toMinute]').val();

        restSelf.post('/api/conference/duplicate', {
            reserveFromTime: parseInt(fromHour) * 60 + parseInt(fromMinute),
            reserveToTime: parseInt(toHour) * 60 + parseInt(toMinute),
            roomNumber: '${entity.roomNumber}',
            reserveDate: '${entity.reserveDate}',
        }).done(function (response) {
            if (response.data) {
                modal.find('.-express-usable-extension').hide();
                modal.find('.-express-unusable-extension').show();
            } else {
                modal.find('.-express-usable-extension').show();
                modal.find('.-express-unusable-extension').hide();
            }
        });
    });
</script>
