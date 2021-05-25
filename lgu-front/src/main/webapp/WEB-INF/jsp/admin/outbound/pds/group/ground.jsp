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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/group/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
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
                                <div class="two wide column"><label class="control-label">그룹생성일</label></div>
                                <div class="nine wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
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
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">유형</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="pdsType">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${pdsTypes}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">그룹명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="name"/>
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
                        <div class="ui basic buttons">
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="PdsGroup" style="display: none;" onclick="popupModal(getEntityId('PdsGroup'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="PdsGroup" style="display: none;" onclick="deleteEntity(getEntityId('PdsGroup'))">삭제</button>
                        </div>
                        <div class="ui basic buttons">
                            <button class="ui button -control-entity" data-entity="PdsGroup" style="display: none;" onclick="popupUploadModal(getEntityId('PdsGroup'))">데이터업로드</button>
                                <%--                            <a href="<c:url value="/admin/outbound/pds/upload/"/>" class="ui button basic tab-indicator">업로드상세정보</a>--%>
                            <button class="ui basic button -control-entity" data-entity="PdsGroup" style="display: none;" onclick="url(getEntityId('PdsGroup'))">
                                <a href="${pageContext.request.contextPath}/admin/outbound/pds/custominfo/" class="url">데이터관리</a>
                            </button>
                        </div>
                        <button class="ui button -control-entity" data-entity="PdsGroup" style="display: none;" onclick="popupExecutionRequestModal(getEntityId('PdsGroup'))">실행요청</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="PdsGroup">
                        <thead>
                        <tr>
                            <th>그룹명</th>
                            <th>그룹생성일</th>
                            <th>마지막업로드날짜</th>
                            <th>업로드데이터수</th>
                            <th>업로드횟수</th>
                            <th>마지막업로드상태</th>
                            <th>마지막실행한날</th>
                            <th>실행횟수</th>
                            <th>실행상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}">
                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td><fmt:formatDate value="${e.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td><fmt:formatDate value="${e.lastUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${e.totalCnt}</td>
                                        <td>${e.uploadTryCnt}</td>
                                        <td>${g.htmlQuote(g.messageOf('PDSGroupUploadStatus', e.lastUploadStatus))}</td>
                                        <td><fmt:formatDate value="${e.lastExecuteDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${e.executeTryCnt}</td>
                                        <td>${g.htmlQuote(g.messageOf('PDSGroupExecuteStatus', e.lastExecuteStatus))}</td>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/pds/group/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(seq) {
                popupReceivedHtml('/admin/outbound/pds/group/' + (seq || 'new') + '/modal', 'modal-pds-group');
            }

            function popupUploadModal(seq) {
                popupReceivedHtml('/admin/outbound/pds/group/' + seq + '/modal-upload', 'modal-upload');
            }

            function popupExecutionRequestModal(seq) {
                popupReceivedHtml('/admin/outbound/pds/group/' + (seq || 'new') + '/modal-execution-request', 'modal-execution-request');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {[]
                    restSelf.delete('/api/pds-group/' + seq).done(function () {
                        reload();
                    });
                });
            }

            function url(seq) {
                $('.url').attr("href", "/admin/outbound/pds/custominfo?groupSeq="+ seq)
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
