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

<div class="calendar user-schedule-modal" data-year="${year}" data-month="${month}">
    <div class="toolbar">
        <h2>
            <button type="button" class="ui button -calendar-date">${year}년 ${month}월</button>
            <button type="button" class="ui blue button" onclick="userSchedule.moveToThisMonth()">오늘</button>
            <button type="button" class="ui button" onclick="userSchedule.popupItem()">신규</button>
        </h2>
        <div class="ui flowing popup transition hidden calendar-navigator" style="width: 340px; text-align: center;">
            <div>
                <button type="button" class="mini ui button calendar-navigator-to-left"><<</button>
                <text class="calendar-navigator-header">${year}</text>
                <button type="button" class="mini ui button calendar-navigator-to-right">>></button>
            </div>

            <div>
                <c:forEach begin="1" end="12" var="e">
                    <button type="button" class="mini ui basic button -calendar-navigator-set-month" style="width: 70px; margin-top: 0.5em;" data-value="${e}">${e}월</button>
                </c:forEach>
            </div>
        </div>
        <div class="ui buttons pull-left">
            <button type="button" class="mini ui blue button" onclick="userSchedule.moveMonth(1)"><i class="angle left icon"></i></button>
            <button type="button" class="mini ui blue button" onclick="userSchedule.moveMonth(-1)"><i class="angle right icon"></i></button>
        </div>
    </div>
    <div class="view-container">
        <table>
            <thead class="day-header">
            <tr>
                <th>일</th>
                <th>월</th>
                <th>화</th>
                <th>수</th>
                <th>목</th>
                <th>금</th>
                <th>토</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="week" items="${monthData}">
                <tr>
                    <tags:user-schedule-day-model day="${week.sunday}" cellClass="holiday"/>
                    <tags:user-schedule-day-model day="${week.monday}"/>
                    <tags:user-schedule-day-model day="${week.tuesday}"/>
                    <tags:user-schedule-day-model day="${week.wednesday}"/>
                    <tags:user-schedule-day-model day="${week.thursday}"/>
                    <tags:user-schedule-day-model day="${week.friday}"/>
                    <tags:user-schedule-day-model day="${week.saturday}"/>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    userSchedule.year = ${year};
    userSchedule.month = ${month};

    ui.find('.-calendar-date').popup({
        popup: ui.find('.calendar-navigator'),
        position: 'bottom left',
        on: 'click'
    });

    const calendarNavigatorHeader = ui.find('.calendar-navigator-header');

    ui.find('.calendar-navigator-to-left').click(function () {
        calendarNavigatorHeader.text(parseInt(calendarNavigatorHeader.text()) - 1);
    });

    ui.find('.calendar-navigator-to-right').click(function () {
        calendarNavigatorHeader.text(parseInt(calendarNavigatorHeader.text()) + 1);
    });

    ui.find('.-calendar-navigator-set-month').click(function () {
        const year = calendarNavigatorHeader.text();
        const month = $(this).attr('data-value');
        userSchedule.setMonth(year, month);
    });

    ui.find('.user-schedule-item').each(function () {
        const seq = $(this).attr('data-seq');
        const type = $(this).attr('data-type');
        const userName = $(this).attr('data-user-name');
        const start = parseInt($(this).attr('data-start'));
        const end = parseInt($(this).attr('data-end'));
        const title = $(this).attr('data-title');
        const contents = $(this).attr('data-contents');
        const important = $(this).attr('data-important') === 'true';
        const idCheckKey = $(this).attr('data-id-check-key');

        console.log(idCheckKey);
        $(this).popup({
            html: $('<div/>', {style: 'width: 280px;'})
                .append($('<h3/>')
                    .append($('<i/>', {class: important ? 'icon color-bar1 exclamation' : ''}))
                    .append($('<text/>', {text: title})))
                .append($('<div/>', {text: moment(start).format('YYYY-MM-DD HH:mm') + ' ~ ' + moment(end).format('YYYY-MM-DD HH:mm')}))
                .append($('<ul/>')
                    .append($('<li/>', {text: ' ● ' + type}))
                    .append($('<li/>', {text: ' ● ' + userName})))
                .append($('<div/>', {text: contents}))
                .append($('<div/>', {style: 'text-align: right;'})
                    .append($('<button/>', {
                        class: 'mini ui blue button',
                        text: '수정',
                        onclick: 'userSchedule.popupItem(' + seq + ')',
                        disabled: parseInt(idCheckKey) !== 0
                    }))
                    .append($('<button/>', {
                        class: 'mini ui button',
                        text: '삭제',
                        onclick: 'userSchedule.removeItem(' + seq + ')',
                        disabled: parseInt(idCheckKey) !== 0
                    })))
            ,
            position: 'right center',
            on: 'click'
        });
    });
</script>
