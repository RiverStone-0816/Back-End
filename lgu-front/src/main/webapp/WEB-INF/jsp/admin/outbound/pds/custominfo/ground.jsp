<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/custominfo/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" commandName="search" method="get" class="panel panel-search">
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
                                <div class="two wide column"><label class="control-label">상담날짜</label></div>
                                <div class="ten wide column -buttons-set-range-container" data-startdate="[name=createdStartDate]" data-enddate="[name=createdEndDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="createdStartDate" style="display:none">From</label>
                                            <form:input path="createdStartDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="createdEndDate" style="display:none">to</label>
                                            <form:input path="createdEndDate" cssClass="-datepicker" placeholder="종료일"/>
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
                                <div class="two wide column"><label class="control-label">상담그룹</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="groupSeq">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${groups}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색항목</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="searchType">
                                            <form:option value="" label="선택안함"/>
                                            <c:forEach var="e" items="${pdsType.fields}">
                                                <c:if test="${e.issearch == 'Y'}">
                                                    <form:option value="${e.fieldId.substring(pdsType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}" data-type="${g.htmlQuote(e.fieldType)}"/>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="five wide column -search-type-sub-input" data-type="DATE">
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
                                </div>
                                <div class="two wide column -search-type-sub-input">
                                    <div class="ui input fluid">
                                        <form:input path="keyword"/>
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
                        <button class="ui basic green button" type="button" onclick="downloadExcel()">Excel 다운로드</button>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal(${search.groupSeq})">추가</button>
                            <button class="ui button -control-entity" data-entity="PdsCustominfo" style="display: none;"
                                    onclick="popupModal(getEntityId('PdsCustominfo', 'group'), getEntityId('PdsCustominfo'))">수정
                            </button>
                            <button class="ui button -control-entity" data-entity="PdsCustominfo" style="display: none;"
                                    onclick="deleteEntity(getEntityId('PdsCustominfo', 'group'), getEntityId('PdsCustominfo'))">삭제
                            </button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable fixed ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="PdsCustominfo">
                        <thead>
                        <tr>
                            <th rowspan="${pdsType.fields.size() == 0 ? "1" : "2"}" style="width: 5em;">번호</th>
                            <th>기본정보</th>
                            <th colspan="${pdsType.fields.size()}">고객정보필드</th>
                        </tr>
                        <c:if test="${fn:length(pdsType.fields) > 0}">
                            <tr>
                                <th class="one wide" title="데이터생성일">데이터생성일</th>
                                <c:forEach var="field" items="${pdsType.fields}">
                                    <th title="${g.htmlQuote(field.fieldInfo)}">${g.htmlQuote(field.fieldInfo)}</th>
                                </c:forEach>
                            </tr>
                        </c:if>
                        </thead>
                        <c:choose>
                            <c:when test="${pdsType.fields.size() != 0}">
                                <tbody>
                                <c:choose>
                                    <c:when test="${pagination.rows.size() > 0}">
                                        <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                            <tr data-id="${g.htmlQuote(e.pdsSysCustomId)}" data-group="${search.groupSeq}">
                                                <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                                <td><fmt:formatDate value="${e.pdsSysUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                                                <c:forEach var="field" items="${pdsType.fields}">
                                                    <td>${g.htmlQuote(customIdToFieldNameToValueMap.get(e.pdsSysCustomId).get(field.fieldId))}</td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="${2 + (fn:length(pdsType.fields) > 0 ? pdsType.fields.size() : 1)}" class="null-data">조회된 데이터가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/pds/custominfo/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal small" id="modal-pds-data-add-popup">
        <i class="close icon"></i>
        <div class="header">
            Auto IVR 추가[그룹명:김옥중테스트]
        </div>
        <div class="content rows scrolling">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">고객명</label></div>
                    <div class="six wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                    <div class="four wide column">
                        (max 100 Bytes.)
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">전화번호</label></div>
                    <div class="six wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                    <div class="four wide column">
                        (최대길이:숫자15개)
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(groupSeq, id) {
                popupReceivedHtml('/admin/outbound/pds/custominfo/' + encodeURIComponent(id || 'new') + '/modal?groupSeq=' + groupSeq, 'modal-data');
            }

            function deleteEntity(groupSeq, id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete($.addQueryString('/api/pds-custominfo/', {groupSeq: groupSeq, id: id})).done(function () {
                        reload();
                    });
                });
            }

            $('#search-form [name=searchType]').change(function () {
                const type = $(this).find(':selected').attr('data-type');
                const subInput = $('.-search-type-sub-input').hide();

                if (['DATE', 'DAY', 'DATETIME'].indexOf(type) >= 0) {
                    subInput.filter('[data-type="DATE"]').show();
                } else {
                    subInput.filter(':not([data-type="DATE"])').show();
                }
            }).change();

            function downloadExcel() {
                window.open(contextPath + '/admin/outbound/pds/custominfo/_excel?${g.escapeQuote(search.query)}', '_blank');
            }

            <c:if test="${groups == null}">
            alert("그룹이 없습니다.");
            </c:if>
        </script>
    </tags:scripts>
</tags:tabContentLayout>
