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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stat/result/link/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/stat/result/link/"))}</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body overflow-unset">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>기간설정</th>
                                <td>
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                </td>
                                <th>수발신선택</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="type">
                                            <form:option value="" label="선택안함" data-type=""/>
                                            <c:forEach var="e" items="${sendReceiveTypes}">
                                                <form:option value="${g.htmlQuote(e.key)}" label="${g.htmlQuote(e.value)}"/>
                                            </c:forEach>
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
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <div class="panel-section">
                        <div class="panel-section-title">
                            <div class="title-txt">
                                상담코드통계[단계형] <span class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</span>
                            </div>
                            <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        </div>
                        <div class="table-scroll-wrap">
                            <table class="ui celled table compact unstackable structured fixed border-top">
                                <thead>
                                <tr>
                                    <th colspan="2">고객유형</th>
                                    <th colspan="2">업무구분</th>
                                    <th colspan="2">세부구분</th>
                                    <c:forEach var="date" items="${dates}">
                                        <th>${g.htmlQuote(date)}</th>
                                    </c:forEach>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <c:forEach var="e" items="${list}">
                                            <c:forEach var="e2" items="${e.fieldResponses}" varStatus="status">
                                                <c:if test="${status.first}">
                                                    <td rowspan="${e.fieldResponses.stream().map(e2 -> e2.codeResponses.size()).sum() + 1}">${g.htmlQuote(e.name)}</td>
                                                    <td rowspan="${e.fieldResponses.stream().map(e2 -> e2.codeResponses.size()).sum() + 1}" class="-e1-sum" data-e1="${g.escapeQuote(e.name)}"></td>
                                                </c:if>
                                                <c:forEach var="e3" items="${e2.codeResponses}" varStatus="status2">
                                                    <tr>
                                                        <c:if test="${status2.first}">
                                                            <td rowspan="${e2.codeResponses.size()}">${g.htmlQuote(e2.fieldInfo)}</td>
                                                            <td rowspan="${e2.codeResponses.size()}" class="-e2-sum" data-e1="${g.escapeQuote(e.name)}" data-e2="${e2.fieldId}"></td>
                                                        </c:if>
                                                        <td>${g.htmlQuote(e3.codeName)}</td>
                                                        <td class="-e3-sum" data-e1="${g.escapeQuote(e.name)}" data-e2="${e2.fieldId}" data-e3="${g.escapeQuote(e3.codeId)}"></td>
                                                        <c:forEach var="date" items="${dates}">
                                                            <td>${codeToDateToCountMap.get(e3.codeId).getOrDefault(date, 0)}</td>
                                                        </c:forEach>
                                                    </tr>
                                                </c:forEach>
                                            </c:forEach>

                                            <tr class="highlight">
                                                <td colspan="5">합계</td>
                                                <td class="-e1-sum" data-e1="${g.escapeQuote(e.name)}"></td>
                                                <c:forEach var="date" items="${dates}">
                                                    <td class="-date-sum" data-e1="${g.escapeQuote(e.name)}" data-date="${g.escapeQuote(date)}"></td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="${6 + dates.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            (function () {
                const stat = {
                    <c:forEach var="e" items="${list}">
                    '${g.escapeQuote(e.name)}': {
                        sum: 0,
                        children: {
                            <c:forEach var="e2" items="${e.fieldResponses}" varStatus="status">
                            '${g.escapeQuote(e2.fieldId)}': {
                                sum: 0,
                                children: {
                                    <c:forEach var="e3" items="${e2.codeResponses}" varStatus="status2">
                                    '${g.escapeQuote(e3.codeId)}': {
                                        sum: 0,
                                        children: {
                                            <c:forEach var="date" items="${dates}">
                                            '${g.escapeQuote(date)}': ${codeToDateToCountMap.get(e3.codeId).getOrDefault(date, 0)},
                                            </c:forEach>
                                        },
                                    },
                                    </c:forEach>
                                },
                            },
                            </c:forEach>
                        },
                        dateSums: {
                            <c:forEach var="date" items="${dates}">
                            '${g.escapeQuote(date)}': 0,
                            </c:forEach>
                        },
                    },
                    </c:forEach>
                };

                keys(stat).map(function (e1Id) {
                    keys(stat[e1Id].children).map(function (e2Id) {
                        keys(stat[e1Id].children[e2Id].children).map(function (e3Id) {
                            keys(stat[e1Id].children[e2Id].children[e3Id].children).map(function (date) {
                                const value = stat[e1Id].children[e2Id].children[e3Id].children[date];
                                stat[e1Id].children[e2Id].children[e3Id].sum += value;
                                stat[e1Id].children[e2Id].sum += value;
                                stat[e1Id].sum += value;
                                stat[e1Id].dateSums[date] += value;
                            });
                        });
                    });
                });

                $('.-e1-sum').each(function () {
                    $(this).text(stat[$(this).attr('data-e1')].sum);
                });
                $('.-e2-sum').each(function () {
                    $(this).text(stat[$(this).attr('data-e1')].children[$(this).attr('data-e2')].sum);
                });

                $('.-e3-sum').each(function () {
                        $(this).text(stat[$(this).attr('data-e1')].children[$(this).attr('data-e2')].children[$(this).attr('data-e3')].sum);
                });

                $('.-date-sum').each(function () {
                    $(this).text(stat[$(this).attr('data-e1')].dateSums[$(this).attr('data-date')]);
                });
            })();

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/result/link/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
