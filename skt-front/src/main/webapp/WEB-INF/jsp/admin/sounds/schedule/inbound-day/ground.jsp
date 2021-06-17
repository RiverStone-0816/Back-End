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
        <tags:page-menu-tab url="/admin/sounds/schedule/inbound-day/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">휴일일정관리</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>번호선택</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="number">
                                            <form:option value="" label="선택안함"/>
                                            <c:forEach var="e" items="${numbers}">
                                                <form:option value="${e.number}" label="${e.number}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </td>
                                <th>스케쥴유형선택</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="groupId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${groups}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>요일</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="searchDate">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${dayOfWeeks}"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>부서선택</th>
                                <td colspan="11">
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini orange compact -select-group">
                                            <i class="search icon"></i>
                                        </button>
                                        <form:hidden path="groupCode"/>
                                        <div class="ui breadcrumb -group-name">
                                            <c:choose>
                                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                                        <span class="section">${g.htmlQuote(e)}</span>
                                                        <c:if test="${!status.last}">
                                                            <i class="right angle icon divider"></i>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <button type="button" class="ui icon button mini compact -clear-group">
                                            <i class="undo icon"></i>
                                        </button>
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
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupHolidayBatchModal()">공휴일 일괄등록</button>
                            <button class="ui button" onclick="popupScheduleInfoModal()">추가</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>대표서비스</th>
                            <th>원번호</th>
                            <th>소속</th>
                            <th>날짜</th>
                            <th>유형</th>
                            <th class="two wide">유형보기</th>
                            <th class="one wide">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <c:choose>
                                        <c:when test="${e.scheduleInfos != null && e.scheduleInfos.size() > 0}">
                                            <c:forEach var="info" items="${e.scheduleInfos}" varStatus="infoStatus">
                                                <tr>
                                                    <c:if test="${infoStatus.first}">
                                                        <td rowspan="${e.scheduleInfos.size()}">${g.htmlQuote(e.number)}</td>
                                                        <td rowspan="${e.scheduleInfos.size()}">${g.htmlQuote(e.svcName)}</td>
                                                        <td rowspan="${e.scheduleInfos.size()}">${g.htmlQuote(e.svcCid)}</td>
                                                    </c:if>

                                                    <td class="five wide">
                                                        <div class="ui breadcrumb">
                                                            <c:forEach var="o" items="${info.companyTrees}" varStatus="oStatus">
                                                                <span class="section">${o.groupName}</span>
                                                                <c:if test="${!oStatus.last}">
                                                                    <i class="right angle icon divider"></i>
                                                                </c:if>
                                                            </c:forEach>
                                                        </div>
                                                    </td>
                                                    <td>${info.fromdate} ~ ${info.todate}</td>
                                                    <td>${info.scheduleGroup.name}</td>
                                                    <td>
                                                        <button class="ui button mini compact" onclick="popupViewScheduleGroupModal(${info.scheduleGroup.parent})">유형보기</button>
                                                        <button class="ui button mini compact" onclick="popupScheduleInfoModal(${info.seq})">수정</button>
                                                        <button class="ui button mini compact" onclick="deleteType('${info.seq}')">삭제</button>
                                                    </td>
                                                    <c:if test="${infoStatus.first}">
                                                        <td rowspan="${e.scheduleInfos.size()}">
                                                            <button class="ui button mini compact" onclick="deleteEntity('${g.htmlQuote(e.number)}')">삭제</button>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>${g.htmlQuote(e.number)}</td>
                                                <td>${g.htmlQuote(e.svcName)}</td>
                                                <td>${g.htmlQuote(e.svcCid)}</td>

                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                    <button class="ui button mini compact" onclick="deleteEntity('${g.htmlQuote(e.number)}')">삭제</button>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
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
        <script>
            function popupHolidayBatchModal() {
                popupReceivedHtml('/admin/sounds/schedule/inbound-day/modal-holiday-batch', 'modal-holiday-batch');
            }

            function popupScheduleInfoModal(seq) {
                popupReceivedHtml('/admin/sounds/schedule/inbound-day/' + (seq || 'new') + '/modal-schedule-info', 'modal-schedule-info');
            }

            function popupViewScheduleGroupModal(parent) {
                popupReceivedHtml('/admin/sounds/schedule/inbound-day/modal-view-schedule-group?parent=' + parent, 'modal-view-schedule-group');
            }

            function deleteType(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/inbound-day-schedule/service/type/' + seq).done(function () {
                        reload();
                    });
                });
            }

            function deleteEntity(number) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/inbound-day-schedule/' + encodeURIComponent(number)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
