<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>

<%@ attribute name="day" required="true" type="kr.co.eicn.ippbx.front.model.UserScheduleDay" %>
<%@ attribute name="cellClass" %>

<td>
    <c:if test="${day != null}">
        <c:set var="lunarDate" value="${day.lunarDate}"/>
        <span data-value="${day.date.date}" class="day-number ${cellClass} ${g.dateFormat(day.date).equals(g.now()) ? 'today' : ''}"
              data-lunar-date-show="${day.date.date % 5 == 0}"
              data-lunar-date="음력 ${lunarDate.month}월 ${lunarDate.day}일">${day.date.date}</span>

        <c:forEach var="e" items="${day.schedules}">
            <fmt:formatDate var="startTime" value="${day.getDayStartTime(e)}" pattern="HH:mm"/>
            <div class="user-schedule-item ${e.wholeDay ? 'whole-day' : ''}"
                 data-seq="${e.seq}"
                 data-important="${e.important}"
                 data-type="${g.htmlQuote(message.getEnumText(e.type))}"
                 data-user-name="${g.htmlQuote(e.userName)}"
                 data-start="${e.start.time}"
                 data-end="${e.end.time}"
                 data-title="${g.htmlQuote(e.title)}"
                 data-id-check-key="${e.userid.equals(g.user.id) ? '0' : '1'}"
                 data-contents="${g.htmlQuote(e.contents)}"
                 title="${g.htmlQuote(e.title)} [${g.htmlQuote(e.userName)}]">
                <c:if test="${e.important}"><i class="exclamation icon color-bar1"></i></c:if> ${e.wholeDay ? '' : startTime} ${g.htmlQuote(e.title)} [${g.htmlQuote(e.userName)}]
            </div>
        </c:forEach>
    </c:if>
</td>
