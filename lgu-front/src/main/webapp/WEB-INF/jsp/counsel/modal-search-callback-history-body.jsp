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

<div class="sub-content ui container fluid unstackable" id="modal-search-callback-history-body">
    <form:form id="search-callback-history-form" modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
               action="${pageContext.request.contextPath}/counsel/modal-search-callback-history-body"
               data-target="#modal-search-callback-history-body">
        <div class="panel-heading">
            <div class="pull-left">
                검색
            </div>
            <div class="pull-right">
                <div class="btn-wrap">
                    <button type="submit" class="ui brand basic button">검색</button>
                    <button type="button" class="ui grey basic button" onclick="modalReload()">초기화</button>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <div class="search-area">
                <div class="ui grid">
                    <div class="row">
                        <div class="three wide column"><label class="control-label">검색기간</label></div>
                        <div class="thirteen wide column">
                            <div class="date-picker from-to">
                                <div class="dp-wrap">
                                    <label class="control-label" for="startDate" style="display:none">From</label>
                                    <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                </div>
                                <span class="tilde">~</span>
                                <div class="dp-wrap">
                                    <label class="control-label" for="endDate" style="display:none">to</label>
                                    <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                </div>
                            </div>
                            <div class="ui basic buttons">
                                <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">인입경로</label></div>
                        <div class="three wide column">
                            <div class="ui form"><form:select path="svcNumber" items="${serviceOptions}"/></div>
                        </div>
                        <div class="three wide column"><label class="control-label">처리상태</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="status" items="${callbackStatusOptions}"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">고객번호</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:input path="callbackNumber"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
    <div class="panel">
        <div class="panel-heading">
            <div class="pull-left">
                <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
            </div>
            <div class="pull-right">
                <button type="button" class="ui basic green button" onclick="setCustomInfo()">고객정보 내보내기</button>
            </div>
        </div>
        <div class="panel-body" style="width: 100%; overflow-x: auto;">
            <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="CallbackHistory">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>수신번호</th>
                    <th>콜백번호</th>
                    <th>인입서비스</th>
                    <th>인입큐</th>
                    <th>상담원</th>
                    <th>처리상태</th>
                    <th>입력일시</th>
                    <th>처리일시</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${pagination.rows.size() > 0}">
                        <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                            <tr data-id="${e.seq}" data-phone-number="${g.htmlQuote(e.callbackNumber)}">
                                <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                <td>${g.htmlQuote(e.callerNumber)}</td>
                                <td>${g.htmlQuote(e.callbackNumber)}</td>
                                <td>${g.htmlQuote(e.svcName)}</td>
                                <td>${g.htmlQuote(e.queueName)}</td>
                                <td>${g.htmlQuote(e.idName)}</td>
                                <td>${e.status != null ? g.htmlQuote(message.getEnumText(e.status)) : null}</td>
                                <td><fmt:formatDate value="${e.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="null-data">조회된 데이터가 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    function modalReload() {
        $('#modal-menu').modalHide();
        popupDraggableModalFromReceivedHtml('/counsel/modal-search-callback-history', 'modal-search-callback-history');
    }

    (function () {
        const modal = $('#search-callback-history-form').closest('.modal');

        modal.find('.-button-set-range').click(function () {
            modal.find('.-button-set-range').removeClass('active');
            $(this).addClass('active');

            const interval = $(this).attr('data-interval');
            const number = $(this).attr('data-number');
            const endDate = modal.find('[name=endDate]');
            const startDate = modal.find('[name=startDate]');

            if (!endDate.val())
                endDate.val(moment().format('YYYY-MM-DD'));

            startDate.val(moment(endDate.val()).add(parseInt(number) * -1, interval).add(1, 'days').format('YYYY-MM-DD'));
        });
    })();

    function setCustomInfo() {
        const phoneNumber = getEntityId('CallbackHistory', 'phone-number');
        if (!phoneNumber)
            return;

        // if (ipccCommunicator.status.cMemberStatus !== 1 && ipccCommunicator.status.cMemberStatus !== 2)
        //     $('#counseling-target').text(phoneNumber);

        loadCustomInput(null, null, phoneNumber);
        $('#search-callback-history-form').closest('.modal').modalHide();
    }
</script>
