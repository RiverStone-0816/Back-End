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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/user-schedule/${entity == null ? null : entity.seq}"
           data-before="prepareUserScheduleForm" data-done="doneSubmitUserSchedule">

    <i class="close icon"></i>
    <div class="header">일정 ${entity != null ? '수정' : '추가'}</div>

    <div class="content rows scrolling">
        <table class="ui celled table compact unstackable border-top-default">
            <tr>
                <th>제목</th>
                <td colspan="2">
                    <div class="ui form"><form:input path="title"/></div>
                </td>
            </tr>
            <tr>
                <th>종류</th>
                <td colspan="2">
                    <div class="ui form"><form:select path="type" items="${types}"/></div>
                </td>
            </tr>
            <tr>
                <th>일시</th>
                <td class="align-left">
                    <div class="ui action input calendar-area">
                        <fmt:formatDate var="startDate" value="${form.start}" pattern="yyyy-MM-dd"/>
                        <fmt:formatDate var="startHour" value="${form.start}" pattern="HH"/>
                        <fmt:formatDate var="startMinute" value="${form.start}" pattern="mm"/>
                        <input name="_startDate" class="-datepicker hasDatepicker" placeholder="시작일" type="text" value="${startDate}"  autocomplete="off" size="15">
                        <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                        <span class="piece"><input type="text" name="_startHour" class="-input-numerical ml5" value="${startHour}" placeholder="시" size="1"> <text>시</text></span>
                        <span class="piece"><input type="text" name="_startMinute" class="-input-numerical ml5" value="${startMinute}" placeholder="분" size="1"> <text>분</text></span>
                        <span class="tilde">~</span>
                        <fmt:formatDate var="endDate" value="${form.end}" pattern="yyyy-MM-dd"/>
                        <fmt:formatDate var="endHour" value="${form.end}" pattern="HH"/>
                        <fmt:formatDate var="endMinute" value="${form.end}" pattern="mm"/>
                        <input name="_endDate" class="-datepicker hasDatepicker" placeholder="종료일" type="text" value="${endDate}"  autocomplete="off" size="15">
                        <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                        <span class="piece"><input type="text" name="_endHour" class="-input-numerical ml5" value="${endHour}" placeholder="시" size="1"> <text>시</text></span>
                        <span class="piece"><input type="text" name="_endMinute" class="-input-numerical ml5" value="${endMinute}" placeholder="분" size="1"> <text>분</text></span>
                    </div>
                </td>
                <td class="one wide">
                    <div class="ui checkbox">
                        <input type="checkbox" name="_isWholeDay" ${entity != null && entity.wholeDay ? 'checked' : ''}/>
                        <label>종일</label>
                    </div>
                    <div class="ui checkbox">
                        <form:checkbox path="important" value="${true}"/>
                        <label>중요</label>
                    </div>
                </td>
            </tr>
            <tr>
                <th>추가정보</th>
                <td colspan="2">
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="contents" rows="5"/>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    window.prepareUserScheduleForm = function (data) {
        if (!data._startDate
            || !$.isNumeric(data._startHour)
            || !$.isNumeric(data._startMinute)
            || !data._endDate
            || !$.isNumeric(data._endHour)
            || !$.isNumeric(data._endMinute)) {
            alert('날짜 정보가 비어 있습니다.');
            throw '날짜 정보가 비어 있습니다.';
        }

        data.start = moment(data._startDate).hour(data._startHour).minute(data._startMinute).toDate().getTime();
        data.end = moment(data._endDate).hour(data._endHour).minute(data._endMinute).toDate().getTime();

        delete data._startDate;
        delete data._startHour;
        delete data._startMinute;
        delete data._endDate;
        delete data._endHour;
        delete data._endMinute;
        delete data._isWholeDay;
    };

    window.doneSubmitUserSchedule = function () {
        modal.modalHide();
        userSchedule.reload();
    };

    modal.find('[name=_isWholeDay]').change(function () {
        if ($(this).is(":checked")) {
            modal.find('[name=_startHour]').val(0);
            modal.find('[name=_startMinute]').val(0);
            modal.find('[name=_endHour]').val(23);
            modal.find('[name=_endMinute]').val(59);
        }
    });

    modal.find('[name=_startHour],[name=_startMinute],[name=_endHour],[name=_endMinute]').change(function () {
        if (modal.find('[name=_startHour]').val() !== 0) modal.find('[name=_isWholeDay]').prop('checked', false);
        if (modal.find('[name=_startMinute]').val() !== 0) modal.find('[name=_isWholeDay]').prop('checked', false);
        if (modal.find('[name=_endHour]').val() !== 23) modal.find('[name=_isWholeDay]').prop('checked', false);
        if (modal.find('[name=_endMinute]').val() !== 59) modal.find('[name=_isWholeDay]').prop('checked', false);
    });
</script>
