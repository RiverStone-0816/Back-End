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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/talk/history/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">채팅상담이력조회</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>업로드날짜</th>
                                <td colspan="7" class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>상담자</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="id">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${users}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>대화방상태</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="roomStatus" items="${roomStatuses}"/>
                                    </div>
                                </td>
                                <th>상담톡서비스</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="senderKey">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${talkServices}"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>대화방명</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:input path="roomName"/>
                                    </div>
                                </td>
                                <th>정렬데이터</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="sort" items="${orderTypes}"/>
                                    </div>
                                </td>
                                <th>정렬순서</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="sequence">
                                            <form:option value="desc" label="내림차순"/>
                                            <form:option value="asc" label="오름차순"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                        <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        <button class="ui basic button -control-entity" data-entity="TalkHistory" style="display: none;"
                                onclick="popupModal(getEntityId('TalkHistory', 'id'), getEntityId('TalkHistory', 'roomId'), getEntityId('TalkHistory', 'roomStatus'))">메시지보기
                        </button>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/talk/history/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="TalkHistory">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>대화방명</th>
                            <th>상담톡서비스</th>
                            <th>대화방상태</th>
                            <th>상담원</th>
                            <th>시작시간</th>
                            <th>마지막메시지시간</th>
                            <th>고객명</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}" data-roomId="${g.htmlQuote(e.roomId)}" data-roomStatus="${g.htmlQuote(e.roomStatus)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.roomName)}</td>
                                        <td>${g.htmlQuote(talkServices.get(e.senderKey))}</td>
                                        <td>${g.htmlQuote(g.messageOf('RoomStatus', e.roomStatus))}</td>
                                        <td>${g.htmlQuote(e.idName)}</td>
                                        <td>${g.htmlQuote(e.roomStartTime)}</td>
                                        <td>${g.htmlQuote(e.roomLastTime)}</td>
                                        <td>${g.htmlQuote(e.maindbCustomName == null || e.maindbCustomName == "" ? "미등록고객" : e.maindbCustomName)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function downloadExcel() {
                window.open(contextPath + '/admin/talk/history/_excel?${g.escapeQuote(search.query)}', '_blank');
            }

            function popupModal(seq, roomId, roomStatus) {
                popupReceivedHtml('/admin/talk/history/' + seq + '/modal?roomId=' + encodeURIComponent(roomId) + '&roomStatus=' + encodeURIComponent(roomStatus), 'modal-message-history');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
