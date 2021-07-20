<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/conference/minutes-save"
           data-done="reload">

    <i class="close icon"></i>
    <div class="header">회의정보</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <form:hidden path="confId"/>
            <div class="row">
                <div class="four wide column"><label class="control-label">기본정보</label></div>
                <div class="twelve wide column">
                    [회의실명:${g.htmlQuote(room.roomName)}][회의실번호:${g.htmlQuote(entity.roomNumber)}][회의날짜:${g.htmlQuote(entity.reserveDate)}]
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의정보</label></div>
                <div class="twelve wide column">
                    [예약자:${g.htmlQuote(entity.reserveAdminName)}][녹취:${g.htmlQuote(g.messageOf('ConfInfoIsRecord', entity.isRecord))}]
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의제어</label></div>
                <div class="twelve wide column">
                    <button class="ui button mini">회의시작</button>
                    <button class="ui button mini">전체초대</button>
                    <button class="ui button mini">회의종료</button>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의진행메세지</label></div>
                <div class="twelve wide column">
                    ${g.htmlQuote('ConfInfoStatusType', entity.status)}
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">참가자</label></div>
                <div class="twelve wide column">
                    <div class="items-wrap">
                        <c:forEach var="e" items="${entity.inMemberList}">
                            <div class="ui medium label">${g.htmlQuote(e.idName)}(${g.htmlQuote(e.peer)})</div>
                        </c:forEach>
                        <c:forEach var="e" items="${entity.outMemberList}">
                            <div class="ui medium label">${g.htmlQuote(e.memberName)}(${g.htmlQuote(e.memberNumber)})</div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의록저장/녹취듣기</label></div>
                <div class="twelve wide column">
                    <button class="ui button mini">회의록저장</button>
                    <button class="ui button mini">녹취듣기</button>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의록작성</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="confMinute" rows="5"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

