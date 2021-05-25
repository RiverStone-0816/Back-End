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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal inverted small">
    <i class="close icon"></i>
    <div class="header">${g.htmlQuote(field.fieldInfo)}</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <c:forEach var="e" items="${field.commonCodes}">
                <div class="row">
                    <div class="three wide column">
                        <label class="control-label">${g.htmlQuote(e.codeName)}</label>
                    </div>
                    <div class="thirteen wide column">
                        <div class="board-con-inner">${g.htmlQuote(e.script)}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
