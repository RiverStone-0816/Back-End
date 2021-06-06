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

<tags:tabContentLayout>
    <style>
        .calendar.user-schedule-modal .view-container table td {height: 100px;}
    </style>

    <div class="content-wrapper-frame">
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-body" style="max-height: calc(100% - 150px);">
                    <div class="calendar user-schedule-modal"></div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const modal = $(document);

            function loadContent(year, month) {
                replaceReceivedHtml($.addQueryString('/user-schedule/content', {year: year || ${year}, month: month || ${month}}), modal.find('.calendar:first'), '.calendar');
            }

            loadContent();

            window.userSchedule = {
                reload: function () {
                    loadContent(moment().year(), moment().month() + 1);
                },
                moveToThisMonth: function () {
                    loadContent();
                },
                moveMonth: function (addMonth) {
                    const content = modal.find('.calendar:first');
                    const year = parseInt(content.attr('data-year'));
                    const month = parseInt(content.attr('data-month')) - addMonth;
                    const calendar = moment().year(year).month(month - 1).date(1);

                    loadContent(calendar.year(), calendar.month() + 1);
                },
                setMonth: function (year, month) {
                    loadContent(year, month);
                },
                popupItem: function (itemSeq) {
                    popupDraggableModalFromReceivedHtml('/user-schedule/modal-item/' + (itemSeq || ''), 'modal-user-schedule-item');
                    $('.user-schedule-item').popup('hide');
                },
                removeItem: function (itemSeq) {
                    const _this = this;
                    restSelf.delete('/api/user-schedule/' + itemSeq).done(function () {
                        _this.reload();
                        $('.user-schedule-item').popup('hide');
                    });
                }
            };
        </script>
    </tags:scripts>
</tags:tabContentLayout>
