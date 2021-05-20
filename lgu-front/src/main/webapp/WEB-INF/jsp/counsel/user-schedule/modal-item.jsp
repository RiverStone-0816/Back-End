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

<form:form commandName="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/user-schedule/${entity == null ? null : entity.seq}"
           data-before="prepareUserScheduleForm" data-done="doneSubmitUserSchedule">

    <i class="close icon"></i>
    <div class="header">일정[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">제목</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><form:input path="title"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">종류</label></div>
                <div class="twelve wide column">
                    <div class="ui form"><form:select path="type" items="${types}"/></div>
                </div>
            </div>
            <div class="row">
                <fmt:formatDate var="startDate" value="${form.start}" pattern="yyyy-MM-dd"/>
                <fmt:formatDate var="startHour" value="${form.start}" pattern="HH"/>
                <fmt:formatDate var="startMinute" value="${form.start}" pattern="mm"/>
                <div class="four wide column"><label class="control-label">일시</label></div>
                <div class="six wide column">
                    <div class="ui input fluid">
                        <input type="text" name="_startDate" class="-datepicker" value="${startDate}"/>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui input fluid">
                        <input type="text" name="_startHour" class="-input-numerical" value="${startHour}" placeholder="시">
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui input fluid">
                        <input type="text" name="_startMinute" class="-input-numerical" value="${startMinute}" placeholder="분">
                    </div>
                </div>
                <fmt:formatDate var="endDate" value="${form.end}" pattern="yyyy-MM-dd"/>
                <fmt:formatDate var="endHour" value="${form.end}" pattern="HH"/>
                <fmt:formatDate var="endMinute" value="${form.end}" pattern="mm"/>
                <div class="four wide column"></div>
                <div class="six wide column">
                    <div class="ui input fluid">
                        <input type="text" name="_endDate" class="-datepicker" value="${endDate}"/>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui input fluid">
                        <input type="text" name="_endHour" class="-input-numerical" value="${endHour}" placeholder="시">
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui input fluid">
                        <input type="text" name="_endMinute" class="-input-numerical" value="${endMinute}" placeholder="분">
                    </div>
                </div>

                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <input type="checkbox" name="_isWholeDay" ${entity != null && entity.wholeDay ? 'checked' : ''}/>
                                <label>종일</label>
                            </div>
                            <div class="field">
                                <form:checkbox path="important" value="${true}"/>
                                <label>중요</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">추가정보</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="contents" rows="5"/>
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
