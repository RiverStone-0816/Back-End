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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/inbound-day-schedule/batch-delete" data-done="reload">

    <i class="close icon"></i>
    <div class="header">스케쥴 일괄 삭제</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">삭제 기준일</label></div>
                <div class="eight wide column">
                    <form:input path="deleteDate" cssClass="-datepicker"/>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">주의사항</label></div>
                <div class="sixteen wide column">
                    <label class="text-primary">
                        기준일을 포함하여 기준일 이전 날짜의 일별 스케쥴이 모두 삭제됩니다.<br>
                        검색 데이터가 아닌 전체 일별 스케쥴에서 날짜를 기준으로 삭제됩니다.<br>
                        예시로 삭제 기준일로 "2024-01-01" 선택 시,<br>
                        일정 종료일이 "2024-01-01"인 경우를 포함하여 이전 일별 스케쥴이 모두 삭제됩니다.
                    </label>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>
