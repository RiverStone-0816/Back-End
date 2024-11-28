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
        <tags:page-menu-tab url="/admin/stat/result/individual/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/stat/result/individual/"))}</div>
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
                                <th>수/발신선택</th>
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
                            <tr>
                                <th>고객DB그룹</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="groupSeq">
                                            <c:forEach var="group" items="${maindbGroups}">
                                                <form:option value="${group.seq}" label="${group.name}" data-result-type="${group.resultType}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </td>
                                <th>필드선택</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="fieldId">
                                            <form:option value="" label="선택안함" data-type=""/>
                                            <c:forEach var="e" items="${fieldList}">
                                                <form:option value="${g.htmlQuote(e.fieldId)}" label="${g.htmlQuote(e.fieldInfo)}" data-type="${g.htmlQuote(e.type)}"/>
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
                                상담코드통계[개별형] <span class="sub header">${g.dateFormat(search.startDate)} ~ ${g.dateFormat(search.endDate)}</span>
                            </div>
                            <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        </div>
                        <div class="overflow-auto">
                            <table class="ui celled table compact unstackable">
                                <thead>
                                <tr>
                                    <th>상담필드</th>
                                    <th>코드</th>
                                    <c:forEach var="date" items="${dates}">
                                        <th>${g.htmlQuote(date)}</th>
                                    </c:forEach>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <c:forEach var="e" items="${list}">
                                            <tr>
                                                <td>${g.htmlQuote(e.fieldInfo)}</td>
                                                <td>${g.htmlQuote(e.codeName)}</td>
                                                <c:forEach var="date" items="${dates}">
                                                    <td>${codeToDateToCountMap.get(e).getOrDefault(date, 0)}</td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="${2 + dates.size()}" class="null-data">조회된 데이터가 없습니다.</td>
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
            const searchForm = $('#search-form');
            const groupSelect = searchForm.find('#groupSeq');
            const fieldSelect = searchForm.find('#fieldId');

            groupSelect.change(function () {
                const resultType = $(this).find(':selected').attr('data-result-type');
                fieldSelect.val('').prop("selected", true);
                fieldSelect.find('option').show();

                fieldSelect.find('option[value!=""]').each(function () {
                    const type = $(this).attr('data-type');

                    if (type !== resultType)
                        $(this).hide();
                });

            });

            (function () {
                const stat = {
                    <c:forEach var="e" items="${list}">
                    '${g.escapeQuote(e.codeId)}': [
                        <c:forEach var="date" items="${dates}">
                        ${codeToDateToCountMap.get(e).getOrDefault(date, 0)},
                        </c:forEach>
                    ],
                    </c:forEach>
                };

                $('.-code-sum').each(function () {
                    let sum = 0;
                    stat[$(this).attr('data-code')].map(function (value) {
                        sum += value;
                    });
                    $(this).text(sum);
                });

                const resultType = groupSelect.find(':selected').attr('data-result-type');

                if (!resultType) {
                    return;
                }

                fieldSelect.find('option[value!=""]').each(function () {
                    const type = $(this).attr('data-type');

                    if (type !== resultType)
                        $(this).hide();
                });

                fieldSelect.find('option[data-type="${search.resultType}"][value="${search.fieldId}"]').prop("selected", true);
            })();

            function downloadExcel() {
                window.open(contextPath + '/admin/stat/result/individual/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
