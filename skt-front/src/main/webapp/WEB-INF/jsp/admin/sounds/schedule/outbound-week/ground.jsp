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
        <tags:page-menu-tab url="/admin/sounds/schedule/outbound-week/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">발신제한일정관리(주간)</div>
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
                                <th>일정명</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:input path="name"/>
                                    </div>
                                </td>
                                <th>요일</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="week">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${dayOfWeeks}"/>
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
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable border-top">
                        <thead>
                        <tr>
                            <th>일정명</th>
                            <th>번호</th>
                            <th>요일</th>
                            <th>시간</th>
                            <th>음원</th>
                            <th class="one wide">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <c:choose>
                                        <c:when test="${e.outScheduleLists != null && e.outScheduleLists.size() > 0}">
                                            <c:forEach var="scheduleListItem" items="${e.outScheduleLists}" varStatus="infoStatus">
                                                <tr>
                                                    <c:if test="${infoStatus.first}">
                                                        <td rowspan="${e.outScheduleLists.size()}">${g.htmlQuote(e.name)}</td>
                                                    </c:if>

                                                    <td>${g.htmlQuote(scheduleListItem.extension)}</td>
                                                    <td>${g.htmlQuote(g.messageOf('DayOfWeek', scheduleListItem.week))}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${(scheduleListItem.fromhour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber
                                                            value="${scheduleListItem.fromhour % 60}" pattern="00"/>
                                                        ~
                                                        <fmt:formatNumber value="${(scheduleListItem.tohour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${scheduleListItem.tohour % 60}"
                                                                                                                                                                pattern="00"/>
                                                    </td>
                                                    <td>${g.htmlQuote(scheduleListItem.soundName)}</td>

                                                    <c:if test="${infoStatus.first}">
                                                        <td rowspan="${e.outScheduleLists.size()}">
                                                            <button class="ui button mini compact" onclick="popupModal(${e.parent})">수정</button>
                                                            <button class="ui button mini compact" onclick="deleteEntity(${e.parent})">삭제</button>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>${g.htmlQuote(e.name)}</td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                    <button class="ui button mini compact" onclick="popupModal(${e.parent})">수정</button>
                                                    <button class="ui button mini compact" onclick="deleteEntity('${g.htmlQuote(e.parent)}')">삭제</button>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
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
            function popupModal(parent) {
                popupReceivedHtml('/admin/sounds/schedule/outbound-week/' + (parent || 'new') + '/modal', 'modal-schedule');
            }

            function deleteEntity(parent) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/outbound-week-schedule/' + parent).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
