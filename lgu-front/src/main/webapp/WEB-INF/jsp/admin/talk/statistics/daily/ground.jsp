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
        <tags:page-menu-tab url="/admin/talk/statistics/daily/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div>
                        검색
                    </div>
                    <div class="dp-flex align-items-center">
                        <div class="ui slider checkbox mr15">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색기간</label></div>
                                <div class="fourteen wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="endDate" style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">서비스선택</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="senderKey" >
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${talkServices}"/>
                                        </form:select>
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
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic green button" onclick="downloadExcel()">Excel 다운로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable fixed">
                        <thead>
                        <tr>
                            <th>날짜</th>
                            <th>개설대화방수</th>
                            <th>종료대화방수</th>
                            <th>수신메시지수</th>
                            <th>발신메시지수</th>
                            <th>자동멘트수</th>
                            <th>초과자동멘트수</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <tr>
                                        <td>${e.statDate}</td>
                                        <td>${e.startRoomCnt}</td>
                                        <td>${e.endRoomCnt}</td>
                                        <td>${e.inMsgCnt}</td>
                                        <td>${e.outMsgCnt}</td>
                                        <td>${e.autoMentCnt}</td>
                                        <td>${e.autoMentExceedCnt}</td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td>합계</td>
                                    <td>${list.stream().map(e2 -> e2.startRoomCnt).sum()}</td>
                                    <td>${list.stream().map(e2 -> e2.endRoomCnt).sum()}</td>
                                    <td>${list.stream().map(e2 -> e2.inMsgCnt).sum()}</td>
                                    <td>${list.stream().map(e2 -> e2.outMsgCnt).sum()}</td>
                                    <td>${list.stream().map(e2 -> e2.autoMentCnt).sum()}</td>
                                    <td>${list.stream().map(e2 -> e2.autoMentExceedCnt).sum()}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td>합계</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
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
                window.open(contextPath + '/admin/talk/statistics/daily/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
