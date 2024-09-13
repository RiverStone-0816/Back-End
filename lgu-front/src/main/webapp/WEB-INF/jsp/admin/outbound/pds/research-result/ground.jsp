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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/pds/research-result/"/>
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
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">
                                초기화
                            </button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색기간</label></div>
                                <div class="fourteen wide column -buttons-set-range-container"
                                     data-startdate="[name=createdStartDate]" data-enddate="[name=createdEndDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="createdStartDate" style="display:none">From</label>
                                            <form:input path="createdStartDate" cssClass="-datepicker"
                                                        placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="createdEndDate" style="display:none">to</label>
                                            <form:input path="createdEndDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1"
                                                class="ui button -button-set-range">당일
                                        </button>
                                        <button type="button" data-interval="day" data-number="3"
                                                class="ui button -button-set-range">3일
                                        </button>
                                        <button type="button" data-interval="day" data-number="7"
                                                class="ui button -button-set-range">1주일
                                        </button>
                                        <button type="button" data-interval="month" data-number="1"
                                                class="ui button -button-set-range">1개월
                                        </button>
                                        <button type="button" data-interval="month" data-number="3"
                                                class="ui button -button-set-range">3개월
                                        </button>
                                        <button type="button" data-interval="month" data-number="6"
                                                class="ui button -button-set-range">6개월
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column">
                                    <label class="control-label">
                                            ${serviceKind.equals('SC') ? "실행명" : "Auto IVR그룹"}
                                    </label>
                                </div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="executeId">
                                            <form:option value="" label="선택안함"/>
                                            <c:forEach var="e" items="${executingPdsList}">
                                                <form:option value="${e.key}" label="${e.value}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">수신번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="customNumber"/>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">검색항목</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="searchType">
                                            <form:option value="" label="선택안함"/>
                                            <c:forEach var="e" items="${pdsType.fields}">
                                                <c:if test="${e.issearch == 'Y'}">
                                                    <form:option value="${g.htmlQuote(e.fieldId)}"
                                                                 label="${g.htmlQuote(e.fieldInfo)}"
                                                                 data-type="${g.htmlQuote(e.fieldType)}"/>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="four wide column -search-type-sub-input" data-type="DATE">
                                    <div class="date-picker from-to" style="width: 100%">
                                        <div class="dp-wrap" style="width: 50%">
                                            <label for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"
                                                        cssStyle="width: 100%;"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap" style="width: 50%">
                                            <label for="endDate" style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"
                                                        cssStyle="width: 100%;"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="two wide column -search-type-sub-input" data-type="TEXT">
                                    <div class="ui input fluid">
                                        <form:input path="keyword"/>
                                    </div>
                                </div>
                                <div class="two wide column -search-type-sub-input" data-type="CODE">
                                    <div class="ui form">
                                        <form:select path="code"/>
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
                        <button class="ui basic green button" type="button" onclick="downloadExcel()">Excel 다운로드
                        </button>
                        <div class="ui basic buttons">
                            <button class="ui button -control-entity" data-entity="PdsResearchResult"
                                    style="display: none;" onclick="popupModal(getEntityId('PdsResearchResult'))">수정
                            </button>
                            <button class="ui button -control-entity" data-entity="PdsResearchResult"
                                    style="display: none;" onclick="deleteEntity(getEntityId('PdsResearchResult'))">삭제
                            </button>
                        </div>
                    </div>
                </div>
                <div class="panel-body" style="overflow-x: auto;">
                    <table class="ui celled table structured compact unstackable" data-entity="PdsResearchResult">
                        <thead>
                        <tr>
                            <th rowspan="2">번호</th>
                            <th colspan="2">기본정보</th>
                            <c:if test="${maxLevel > 0}">
                                <th colspan="${maxLevel}">설문정보</th>
                            </c:if>
                            <th colspan="${pdsType.fields.size()}">고객정보필드</th>
                        </tr>
                        <c:if test="${fn:length(pdsType.fields) > 0}">
                            <tr>
                                <th>설문등록시간</th>
                                <th>수신번호</th>
                                <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                    <th>${status.index}단계</th>
                                </c:forEach>
                                <c:forEach var="field" items="${pdsType.fields}">
                                    <th>${g.htmlQuote(field.fieldInfo)}</th>
                                </c:forEach>
                            </tr>
                        </c:if>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}">
                                        <td>${status.index + 1}</td>
                                        <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.customNumber)}</td>
                                        <c:forEach begin="1" end="${maxLevel}" varStatus="status">
                                            <c:set var="value"
                                                   value="${seqToPathIndexToValueMap.get(e.seq).get(status.index)}"/>
                                            <td>
                                                <c:if test="${value != null && value.split('_')[1] != null && idToNumberToDescription.get(value.split('_')[0]) != null}">
                                                    [${value.split('_')[1]}] ${idToNumberToDescription.get(value.split('_')[0]).get(value.split('_')[1])}
                                                </c:if>
                                            </td>
                                        </c:forEach>
                                        <c:forEach var="field" items="${pdsType.fields}">
                                            <c:set var="value"
                                                   value="${customIdToFieldNameToValueMap.get(e.customId).get(field.fieldId)}"/>
                                            <c:choose>
                                                <c:when test="${field.fieldType == 'CODE'}">
                                                    <td>
                                                            ${field.codes.stream().filter(e -> e.codeId == value).map(e -> e.codeName).findFirst().orElse('')}
                                                    </td>
                                                </c:when>
                                                <c:when test="${field.fieldType == 'MULTICODE'}">
                                                    <td>
                                                        <c:forEach var="v" items="${value.split(',')}">
                                                            ${field.codes.stream().filter(e -> e.codeId == v).map(e -> e.codeName).findFirst().orElse('')}&ensp;
                                                        </c:forEach>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="${not empty value and fn:length(g.htmlQuote(value)) > 30 ? 'break-td mw300' : ''}">
                                                            ${g.htmlQuote(value)}
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="${3 + (fn:length(pdsType.fields) > 0 ? pdsType.fields.size() : 1) + maxLevel}"
                                        class="null-data">
                                        조회된 데이터가 없습니다.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/pds/research-result/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const codeMapInfo = {
                <c:forEach var="field" items="${pdsType.fields}">
                <c:if test="${fn:contains(field.fieldId, 'CODE') and field.issearch == 'Y'}">
                '${field.fieldId}': {
                    <c:forEach var="code" items="${field.codes}">
                    '${code.sequence}': {
                        'codeId': '${code.codeId}',
                        'codeName': '${code.codeName}',
                    },
                    </c:forEach>
                },
                </c:if>
                </c:forEach>
            };

            const searchForm = $('#search-form');

            searchForm.find('[name=searchType]').change(function () {
                const type = $(this).find(':selected').attr('data-type');
                const fieldId = $(this).find(':selected').val();
                const subInput = $('.-search-type-sub-input').hide();
                const codeSelect = $('#code');

                $('input[name=startDate]').val('');
                $('input[name=endDate]').val('');
                codeSelect.empty();
                $('#keyword').val('');

                if (['DATE', 'DAY', 'DATETIME'].indexOf(type) >= 0) {
                    subInput.filter('[data-type="DATE"]').show();
                } else if (['CODE', 'MULTICODE'].indexOf(type) >= 0) {
                    const codeMap = codeMapInfo[fieldId];
                    codeSelect.append($('<option/>', {value: '', text: '선택안함'}));
                    const codeLength = codeMap ? Object.keys(codeMap).length : 0;
                    for (let i = 0; i < codeLength; i++) {
                        codeSelect.append($('<option/>', {value: codeMap[i].codeId, text: codeMap[i].codeName}));
                    }

                    subInput.filter('[data-type="CODE"]').show();
                } else {
                    subInput.filter('[data-type="TEXT"]').show();
                }
            }).change();

            function downloadExcel() {
                window.open(contextPath + '/admin/outbound/pds/research-result/_excel?${g.escapeQuote(search.query)}', '_blank');
            }

            $(window).on('load', function () {
                $('#keyword').val('${search.keyword}').trigger("change");
                $('#code').val('${search.code}').trigger("change");

                $('input[name=startDate]').val('${search.startDate}');
                $('input[name=endDate]').val('${search.endDate}');
            });
        </script>
    </tags:scripts>
</tags:tabContentLayout>
