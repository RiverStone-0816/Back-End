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

<div class="ui modal inverted mini" id="modal-consulting-reservation-popup">
    <i class="close icon"></i>
    <div class="header">상담예약</div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">날짜선택</label></div>
                <div class="twelve wide column">
                    <div class="-datepicker"></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">시간선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="field">
                            <div class="two fields remove-margin">
                                <div class="field">
                                    <select name="hour">
                                        <c:forEach begin="0" end="23" var="e">
                                            <option value="${e}">${e}시</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="field">
                                    <select name="minute">
                                        <c:forEach begin="0" end="59" var="e">
                                            <option value="${e}">${e}분</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="time-setter" class="-set-time-from-now" value="10">
                                    <label>10분후</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="time-setter" class="-set-time-from-now" value="30">
                                    <label>30분후</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="time-setter" class="-set-time-from-now" value="60">
                                    <label>60분후</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button" onclick="reserveCounseling()">확인</button>
    </div>
</div>

<tags:scripts>
    <script>
        const ReservationModal = $('#modal-consulting-reservation-popup');

        function popupReservationModal() {
            ReservationModal.dragModalShow();

            const m = moment();

            ReservationModal.find('input').prop('checked', false);
            ReservationModal.find('.-datepicker').datepicker("setDate", m.format('YYYY-MM-DD'));
            ReservationModal.find('[name=hour]').val(m.format('H'));
            ReservationModal.find('[name=minute]').val(m.format('m'));
        }

        ReservationModal.find('.-set-time-from-now').change(function () {
            const m = moment().add(parseInt($(this).val()), 'minutes');

            ReservationModal.find('.-datepicker').datepicker("setDate", m.format('YYYY-MM-DD'));
            ReservationModal.find('[name=hour]').val(m.format('H'));
            ReservationModal.find('[name=minute]').val(m.format('m'));
        });

        function reserveCounseling() {
            const date = moment(ReservationModal.find('.-datepicker').datepicker("getDate")).format('YYYY-MM-DD');
            const hour = ReservationModal.find('[name=hour]').val();
            const minute = ReservationModal.find('[name=minute]').val();

            if (!date || !hour || !minute)
                return alert('시간정보를 모두 입력하세요.');

            if (!phoneNumber)
                return alert('상담 중인 고객을 대상으로 예약이 가능합니다.');

            restSelf.post('/api/counsel/consultation-reservation', {customNumber: phoneNumber, reservationTime: moment(date).hour(hour).minute(minute).toDate()}).done(function () {
                alert('상담 예약되었습니다.');
                ReservationModal.modalHide();

                loadTodoList();
                $('.item[data-tab="todo-list"]').click();
            });
        }
    </script>
</tags:scripts>
