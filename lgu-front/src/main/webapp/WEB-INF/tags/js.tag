<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>

<%--@elvariable id="devel" type="java.lang.Boolean"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<!-- external library common -->
<script src="<c:url value="/webjars/jquery/3.4.1/jquery.min.js"/>"></script>
<script src="<c:url value="/webjars/Semantic-UI/2.4.1/semantic.js"/>"></script>
<script src="<c:url value="/webjars/jquery-blockui/2.65/jquery.blockUI.js"/>"></script>
<script src="<c:url value="/webjars/momentjs/2.29.4/min/moment.min.js"/>"></script>
<script src="<c:url value="/webjars/d3js/5.9.1/d3.min.js"/>"></script>
<script src="<c:url value="/webjars/vue/3.2.10/dist/vue.global.prod.js"/>"></script>

<!-- external library depend -->
<script src="<c:url value="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"/>"></script>
<script src="<c:url value="/webjars/overlayscrollbars/1.9.1/js/jquery.overlayScrollbars.min.js"/>"></script>
<script src="<c:url value="/webjars/toastr/2.1.2/toastr.js"/>"></script>

<script src="<c:url value="/resources/vendors/highlight/1.0.0/jQuery.highlight.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/moment-lunar/moment-lunar.min.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/socket.io/2.3.1/socket.io.slim.js?version=${version}"/>"></script>
<script src="<c:url value="/resources/vendors/sha512.js?version=${version}"/>"></script>

<script src="<c:url value="/resources/vendors/flexslider/2.7.2/jquery.flexslider.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/tablesort/0.0.11/tablesort.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/multifile-master/jquery.MultiFile.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/spectrum/spectrum.min.js"/>" data-type="library"></script>

<script src="<c:url value="/resources/vendors/maudio.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/Amsify-Suggestags/jquery.amsify.suggestags.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/tagify/1.3.1/tagify.min.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/tagify/1.3.1/jQuery.tagify.min.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/chart.js/4.3.3/chart.min.js?version=${version}"/>" data-type="library"></script>
<script src="<c:url value="/resources/vendors/chart.js/chartjs-plugin-datalabels/2.2.0/datalabels.min.js?version=${version}"/>" data-type="library"></script>

<c:choose>
    <c:when test="${devel}">

        <%-- user library --%>
        <script src="<c:url value="/resources/js/string.ex.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/formData.ex.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/jquery.ex.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/jquery.leanModal.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/jquery.bind.helpers.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/TabController.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/IvrEditor.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/IpccAdminCommunicator.js?version=${version}"/>"></script>
        <script src="<c:url value="/resources/js/IpccPdsCommunicator.js?version=${version}"/>"></script>
        <script src="<c:url value="/resources/js/IpccCommunicator.js?version=${version}"/>"></script>
        <script src="<c:url value="/resources/js/TalkCommunicator.js?version=${version}"/>"></script>
        <script src="<c:url value="/resources/js/MessengerCommunicator.js?version=${version}"/>"></script>
        <c:if test="${usingServices.contains('SPHONE')}">
        <script src="<c:url value="/resources/js/adapter.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/janus.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/softphone-common.js?version=${version}"/>"></script>
        <script src="<c:url value="/resources/js/softphone-api.js?version=${version}"/>"></script>
        <script src="<c:url value="/resources/js/voicechat-api.js?version=${version}"/>"></script>
        </c:if>
        <%-- functions --%>
        <script src="<c:url value="/resources/js/common.func.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/depend.func.js?version=${version}"/>" data-type="library"></script>

        <%-- use strict --%>
        <script src="<c:url value="/resources/js/depend.use.strict.js?version=${version}"/>" data-type="library"></script>

        <%-- external --%>

    </c:when>
    <c:otherwise>
        <script src="<c:url value="/resources/compiled/${version}.js"/>" data-type="library"></script>
    </c:otherwise>
</c:choose>

<script>
    window.reloadWhenSelectSite = false;
    window.disableLog = ${devel};
    window.contextPath = '${pageContext.request.contextPath}';
    window.loadingImageSource = contextPath + '/resources/images/loading.svg';
    window.RINGTONE = new Audio(contextPath ? contextPath + '/resources/sounds/SimpleTone.mp3' : '/resources/sounds/SimpleTone.mp3');
    window.BUSYTONE = new Audio(contextPath ? contextPath + '/resources/sounds/BusySignal.mp3' : '/resources/sounds/BusySignal.mp3');
    RINGTONE.loop = true;

    <c:if test="${g.login}">
    window.userId = '${g.escapeQuote(user.id)}';
    </c:if>

    (function () {
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

        moment.updateLocale('kr', {
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
        moment.updateLocale('kr');
    }());

    window.profileImageSources = [...Array(24).keys()].map(i => contextPath + '/resources/images/profile/profile' + zeroPad(i + 1, '00') + '.png')
</script>
