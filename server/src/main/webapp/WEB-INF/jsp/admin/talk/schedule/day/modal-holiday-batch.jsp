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

<form:form commandName="form" class="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/talk-schedule-day/holy"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">공휴일 일괄등록</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">추가 가능한 상담톡서비스</label></div>
                <div class="eight wide column"><label class="control-label">추가된 상담톡서비스</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${talkServices}">
                                    <option value="${g.htmlQuote(e.value)}">${g.htmlQuote(e.key)}[${g.htmlQuote(e.value)}]</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="senderKeys" class="form-control -left-selector" size="8" multiple="multiple"></select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">스케쥴 유형선택</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="groupId">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${scheduleGroups}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">공휴일 연도선택</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select name="year">
                            <c:forEach var="e" begin="${thisYear}" end="${thisYear + 20}">
                                <option value="${e}">${e}년</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title ui-draggable-handle">공휴일 지정</h4>
                </div>
            </div>
            <c:forEach var="e" items="${holidays}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">${g.htmlQuote(e.holyName)}</label></div>
                    <div class="four wide column">${e.holyDate.split('-')[0]}월 ${e.holyDate.split('-')[1]}일(${e.lunarYn == 'Y' ? '음력' : '양력'})</div>
                    <div class="eight wide column">
                        <div class="date-picker from-to">
                            <div class="dp-wrap">
                                <label class="control-label" for="from${e.seq}" style="display:none">From</label>
                                <input type="text" id="from${e.seq}" name="periodDates.[${e.seq}].fromDate" class="-datepicker" placeholder="시작일"
                                       data-month="${e.holyDate.split('-')[0]}" data-day="${e.holyDate.split('-')[1]}" data-lunar="${e.lunarYn}">
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap">
                                <label class="control-label" for="to${e.seq}" style="display:none">to</label>
                                <input type="text" id="to${e.seq}" name="periodDates.[${e.seq}].toDate" class="-datepicker" placeholder="종료일"
                                       data-month="${e.holyDate.split('-')[0]}" data-day="${e.holyDate.split('-')[1]}" data-lunar="${e.lunarYn}">
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.senderKeys = [];
        modal.find('[name="senderKeys"] option').each(function () {
            data.senderKeys.push($(this).val());
        });

        data.periodDates = values(data.periodDates);

        delete data.year;
    };

    modal.find('[name="year"]').change(function () {
        const year = $(this).val();

        modal.find('[name^=periodDates\\.]').each(function () {
            const month = parseInt($(this).attr('data-month'));
            const day = parseInt($(this).attr('data-day'));
            const isLunar = $(this).attr('data-lunar') === 'Y';

            const m = moment().year(year).month(month - 1).date(day);
            $(this).val(isLunar ? m.solar().format('YYYY-MM-DD') : m.format('YYYY-MM-DD'));
        });
    }).change();
</script>