<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<form:form commandName="search" class="-ajax-loader" method="get" data-method="get"
           action="${pageContext.request.contextPath}/ipcc-messenger/tab-call-history"
           data-target="#tab3">
    <div class="inner-box">
        <div class="ui middle aligned grid search">
            <div class="row">
                <div class="four wide column">검색기간</div>
                <div class="twelve wide column">
                    <div class="multi-input-wrap">
                        <div class="inner">
                            <div class="ui fluid small input">
                                <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                            </div>
                        </div>
                        <div class="tilde">~</div>
                        <div class="inner">
                            <div class="ui fluid small input">
                                <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">전화번호</div>
                <div class="twelve wide column">
                    <div class="ui fluid small input">
                        <form:input path="phone"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">기타선택</div>
                <div class="twelve wide column">
                    <div class="multi-input-wrap">
                        <div class="inner">
                            <div class="ui form">
                                <div class="field">
                                    <form:select path="callType">
                                        <form:option value="" label="수신/발신"/>
                                        <form:options items="${callTypes}"/>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                        <div class="tilde"></div>
                        <div class="inner">
                            <div class="ui form">
                                <div class="field">
                                    <form:select path="callStatus">
                                        <form:option value="" label="호상태 선택"/>
                                        <form:options items="${callStatuses}"/>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="pull-right">
                            <%--<button type="button" class="ui black button basic tiny">초기화</button>--%>
                        <button type="submit" class="ui darkblue button tiny">검색</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="inner-box bb-unset inner-scroll-wrap">
        <h1 class="sub-title">통화이력</h1>
        <table class="ui celled table fixed unstackable small compact">
            <thead>
            <tr>
                <th>발신번호</th>
                <th>수신번호</th>
                <th>날짜시간</th>
                <th>수/발신</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                <tr>
                    <td>${g.htmlQuote(e.src)}</td>
                    <td>${g.htmlQuote(e.dst)}</td>
                    <td><fmt:formatDate value="${e.ringDate}" pattern="MM-dd HH:mm"/></td>
                    <td>${g.htmlQuote(e.callKindValue)}(${g.htmlQuote(e.callStatusValue)})</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="panel-footer">
            <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/ipcc-messenger/tab-call-history/" pageForm="${search}" ajaxLoaderEnable="true" ajaxLoaderTarget="#tab3"/>
        </div>
    </div>
</form:form>
