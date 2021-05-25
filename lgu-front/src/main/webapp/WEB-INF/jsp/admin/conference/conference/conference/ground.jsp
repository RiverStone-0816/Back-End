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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/conference/conference/conference/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <form:hidden path="reserveYear"/>
                <form:hidden path="reserveMonth"/>

                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">회의실</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="roomNumber">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${rooms}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-body">
                    <div class="calendar">
                        <div class="toolbar">
                            <h2>${search.reserveYear}년 ${search.reserveMonth}월</h2>
                            <div class="ui buttons pull-left">
                                <button type="button" class="mini ui blue button" onclick="moveMonth(1)"><i class="angle left icon"></i></button>
                                <button type="button" class="mini ui blue button" onclick="moveMonth(-1)"><i class="angle right icon"></i></button>
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
                                        <tags:day-model day="${week.sunday}"/>
                                        <tags:day-model day="${week.monday}"/>
                                        <tags:day-model day="${week.tuesday}"/>
                                        <tags:day-model day="${week.wednesday}"/>
                                        <tags:day-model day="${week.thursday}"/>
                                        <tags:day-model day="${week.friday}"/>
                                        <tags:day-model day="${week.saturday}"/>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function moveMonth(addMonth) {
                const searchForm = $('#search-form');
                const year = parseInt(searchForm.find('[name=reserveYear]').val());
                const month = parseInt(searchForm.find('[name=reserveMonth]').val()) - addMonth;

                const calendar = moment().year(year).month(month - 1).date(1);
                searchForm.find('[name=reserveYear]').val(calendar.year());
                searchForm.find('[name=reserveMonth]').val(calendar.month() + 1);

                searchForm.submit();
            }

            function calendarPastePopup() {
                $('#modal-calendar-paste').modalShow();
                $('.ui.popup').popup('hide all');
            }

            $('.calendar-item').each(function () {
                const seq = $(this).attr('data-seq');
                const date = $(this).attr('data-date');
                $(this).popup({
                    html:
                        "<a href='javascript:' class='btn-calendar' onclick='popupModal(" + seq + ", \"" + date + "\");'>회의수정</a>" +
                        "<a href='javascript:' class='btn-calendar' onclick='popupCopyModal(" + seq + ");'>회의복사</a>" +
                        "<a href='javascript:' class='btn-calendar' onclick='deleteEntity(" + seq + ");'>회의삭제</a>" +
                        "<a href='javascript:' class='btn-calendar' onclick='popupInfoModal(" + seq + ");'>회의정보</a>",
                    position: 'right center',
                    on: 'click',
                });
            });

            $('.btn-calendar-ap').each(function () {
                const date = $(this).attr('data-date');
                $(this).popup({
                    html:
                        "<a href='javascript:' class='btn-calendar' onclick='popupModal(null, \"" + date + "\");'>회의추가</a>",
                    on: 'click'
                });
            });

            function popupCopyModal(seq) {
                $('.ui.popup').popup('hide all');
                popupReceivedHtml('/admin/conference/conference/conference/' + seq + '/modal-copy', 'modal-copy');
            }

            function popupInfoModal(seq) {
                $('.ui.popup').popup('hide all');
                popupReceivedHtml('/admin/conference/conference/conference/' + seq + '/modal-info', 'modal-info');
            }

            function popupModal(seq, date) {
                $('.ui.popup').popup('hide all');
                popupReceivedHtml('/admin/conference/conference/conference/' + (seq || 'new') + '/modal?date=' + date, 'modal-conference');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    $('.ui.popup').popup('hide all');
                    restSelf.delete('/api/conference/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
