(function ($) {
    "use strict";

    $(window).on('load', function () {
        $('body').bindHelpers();
    });

    $(document).on("click", 'a[href="#"]', function (e) {
        e.preventDefault();
    });

    $.datepicker.setDefaults({
        prevText: '이전 달',
        nextText: '다음 달',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        showMonthAfterYear: true,
        yearSuffix: '년'
    });

    moment.locale('kr', {
        months: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
        monthsShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
        weekdays: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
        weekdaysShort: ["일", "월", "화", "수", "목", "금", "토"],
        longDateFormat: {L: "YYYY.MM.DD", LL: "YYYY년 MMMM D일", LLL: "YYYY년 MMMM D일 A h시 mm분", LLLL: "YYYY년 MMMM D일 dddd A h시 mm분"},
        meridiem: {AM: '오전', am: '오전', PM: '오후', pm: '오후'},
        relativeTime: {future: "%s 후", past: "%s 전", s: "몇초", ss: "%d초", m: "일분", mm: "%d분", h: "한시간", hh: "%d시간", d: "하루", dd: "%d일", M: "한달", MM: "%d달", y: "일년", yy: "%d년"},
        ordinal: function (number) {
            return '일';
        }
    });
    moment.locale('kr');
})(jQuery);
