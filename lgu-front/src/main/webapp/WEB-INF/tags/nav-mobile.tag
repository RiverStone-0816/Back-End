<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>

<div class="ui bottom fixed five item menu inverted">
    <a class="item" href="<c:url value="/m/"/>">홈</a>
    <a class="item" href="<c:url value="/m/operation"/>">운영현황</a>
    <a class="item" href="<c:url value="/m/consultant"/>">상담원<br>모니터링</a>
    <a class="item" href="<c:url value="/m/stat"/>">통계</a>
    <a class="item" href="<c:url value="/m/callback"/>">콜백</a>
</div>n
