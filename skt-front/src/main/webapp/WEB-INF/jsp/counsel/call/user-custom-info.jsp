<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<select id="user-custom-info" onchange="loadCallingInfo(this.selectedOptions[0].getAttribute('data-group-id'), this.selectedOptions[0].getAttribute('data-custom-id'), this.value);">
    <option value="" label="">고객정보 선택</option>
    <c:forEach var="e" items="${list}">
        <option value="${e.channel_data}" data-group-id="${e.maindb_group_id}" data-custom-id="${e.maindb_custom_id}">[${e.groupName}]${e.maindb_custom_name}</option>
    </c:forEach>
</select>
