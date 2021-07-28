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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/service/log/web-log/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">시스템로그조회</div>
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
                                <th>검색기간</th>
                                <td colspan="7" class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="ui form inline">
                                                <form:select path="startHour">
                                                    <c:forEach var="e" begin="0" end="23">
                                                        <form:option value="${e}" label="${e}시"/>
                                                    </c:forEach>
                                                </form:select>
                                            </span>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="ui form inline">
                                                <form:select path="endHour">
                                                    <c:forEach var="e" begin="0" end="23">
                                                        <form:option value="${e}" label="${e}시"/>
                                                    </c:forEach>
                                                </form:select>
                                        </span>
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
                                <th>실행명</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="actionId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${actionTypes}"/>
                                            <form:option value="PDS" label="${serviceKind.equals('SC') ? 'PDS 관리' : 'AUTO IVR'}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>실행자명</th>
                                <td colspan="3">
                                    <div class="ui form"><form:input path="userName"/></div>
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
                        <h3 class="panel-total-count">
                            전체 <span class="text-primary -total-count">${pagination.totalCount != null ? pagination.totalCount : 0}</span> / ${limit}건
                        </h3>
                        <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        <button class="ui basic button -control-entity" data-entity="WebSecurity" style="display: none;" onclick="deleteWebSecurityLogs()">삭제</button>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/service/log/web-log/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable" : null}" data-entity="WebSecurity">
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>번호</th>
                            <th>실행자명(아이디)</th>
                            <th>시간</th>
                            <th>내선</th>
                            <th>아이디유형</th>
                            <th>실행아이피</th>
                            <th>실행명</th>
                            <th>상세실행명</th>
                            <th>실행내용</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>
                                            <div class="ui checkbox">
                                                <input type="checkbox" name="checkbox">
                                            </div>
                                        </td>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.userName)}(${g.htmlQuote(e.userId)})</td>
                                        <td>${g.htmlQuote(e.insertDate)}</td>
                                        <td>${g.htmlQuote(e.extension)}</td>
                                        <td>${e.idType != null ? g.htmlQuote(message.getEnumText(e.idType)) : null}</td>
                                        <td>${g.htmlQuote(e.secureIp)}</td>
                                        <td>${g.messageOf('WebSecureActionType', e.actionId) != null ? g.htmlQuote(g.messageOf('WebSecureActionType', e.actionId)) : g.htmlQuote(e.actionId)}</td>
                                        <td>${g.htmlQuote(g.messageOf('WebSecureActionSubType', e.actionSubId))}</td>
                                        <td>${g.htmlQuote(e.actionData)}</td>
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
    </div>

    <tags:scripts>
        <script id="toast-reservation-script">
            window.toastr.options = {
                "closeButton": true,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-bottom-right",
                "preventDuplicates": false,
                "onclick": function (event) {
                    $('.toast .toast-close-button').focus();
                },
                "showDuration": "300",
                "hideDuration": "1000",
                "timeOut": "0",
                "extendedTimeOut": "0",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut"
            };

            const div = $('<div/>').append(
                $('<div/>', {onclick: "$(this).closest('.toast').find('.toast-close-button').click();", style: 'cursor: pointer; margin-top: 5px;'})
            );
            function noticeWebLog() {
                div.append($('<b/>', {text: '감사 증적 크기 제한용량 초과'}));
                toastr.warning(div.html());
            }

            function overwriteWebLog() {
                div.append($('<b/>', {text: '제한용량 초과로 데이터 덮어쓰기'}));
                toastr.info(div.html());
            }
        </script>
        <script>
            function deleteWebSecurityLogs() {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    const list = [];
                    $('table[data-entity="WebSecurity"] tr.active').each(function () {
                        list.push($(this).attr('data-id'));
                    });
                    restSelf.delete('/api/web-log/', {checkIds: list}).done(function () {
                        reload();
                    });
                });
            }

            $('.-total-count').each(function () {
                const currentValue = $(this).text();
                const total = ${limit};

                if (currentValue > total) {
                    const limit = currentValue - total;
                    restSelf.delete('/api/web-log/overwrite/'+ encodeURIComponent(limit));
                    overwriteWebLog();
                }

                if ('${g.user.idType}' === 'J' || '${g.user.idType}' === 'A')
                    if ((currentValue / total * 100) >= (total * 90 / 100))
                        noticeWebLog();
            });

            function downloadExcel() {
                restSelf.put('/api/web-log/down/WEBLOG').done(function () {
                    window.open(contextPath + '/admin/service/log/web-log/_excel?${g.escapeQuote(search.query)}', '_blank');
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
