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
        <tags:page-menu-tab url="/admin/outbound/voc/stat/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">VOC통계</div>
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
                                <td class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                                <th>VOC그룹</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="vocGroupSeq">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${groups}"/>
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
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable border-top">
                        <thead>
                        <tr>
                            <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                <th>${status.index}단계</th>
                            </c:forEach>
                            <th>건수</th>
                        </tr>
                        </thead>
                        <tbody id="stat-sheet">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <tags:scripts>
        <script>
            const maxLevel = ${maxLevel};
            const nodes = [
                <c:forEach var="e" items="${root.children}">
                {<tags:research-tree-sheet-count item="${e}"/>},
                </c:forEach>
            ];

            (function () {
                const sheet = $('#stat-sheet');
                const codeToItem = {};
                const pathIsExpressed = {};

                function calculateParentPath(splicedPath, level) {
                    let path = "0";

                    for (let i = 1; i <= level; i++)
                        path += "-" + splicedPath[i];

                    return path;
                }

                function makeTr(item, i, indexArray) {
                    codeToItem[item.code] = item;
                    indexArray = indexArray || [i];

                    if (!item.leaf)
                        return item.children.map(function (item, i) {
                            const newIndexArray = indexArray.slice();
                            newIndexArray.push(i);
                            makeTr(item, i, newIndexArray);
                        });

                    const tr = $('<tr/>').appendTo(sheet);
                    const splicedPath = item.path.split('-');

                    indexArray.map(function (ignore, iIndexArray) {
                        const level = iIndexArray + 1;
                        const code = splicedPath[level];
                        const path = calculateParentPath(splicedPath, level);
                        if (pathIsExpressed[path]) return;

                        pathIsExpressed[path] = true;
                        const e = codeToItem[code];
                        tr.append($('<td/>', {rowspan: e.leafDescendantCount, text: e.itemName + (e.mappedNumber === 0 ? '' : ' - ' + e.mappedNumberDescription)}));
                    });

                    for (let level = item.level + 1; level <= maxLevel; level++)
                        tr.append($('<td/>'));

                    tr.append($('<td/>', {text: item.count}));
                }

                nodes.map(function (item, i) {
                    makeTr(item, 0);
                });
            })();
        </script>
    </tags:scripts>
</tags:tabContentLayout>
