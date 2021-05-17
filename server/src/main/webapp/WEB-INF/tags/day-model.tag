<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>

<%@ attribute name="day" required="true" type="kr.co.eicn.ippbx.front.model.ConferenceDay" %>

<td>
    <c:if test="${day != null}">
        <button type="button" class="btn-calendar btn-calendar-ap" data-date="${g.dateFormat(day.date)}">
            <i class="material-icons"> playlist_add </i>
        </button>
        <span class="day-number">${day.date.date}</span>

        <c:forEach var="e" items="${day.conferences}">
            <div class="calendar-item" data-seq="${e.seq}" data-date="${e.reserveDate}">
                    ${g.htmlQuote(e.confName)} [<fmt:formatNumber value="${(e.reserveFromTime / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${e.reserveFromTime % 60}"
                                                                                                                                                  pattern="00"/>~<fmt:formatNumber
                    value="${(e.reservetoTime / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${e.reservetoTime % 60}" pattern="00"/>]
            </div>
        </c:forEach>
    </c:if>
</td>