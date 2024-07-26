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

<form:form id="search-preview-form" modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
           action="${pageContext.request.contextPath}/counsel/preview/list-body"
           data-target="#preview-list-body">
    <div class="panel-heading dp-flex align-items-center justify-content-space-between" style="background-color: transparent !important;">
        <div class="pull-left">
            검색
        </div>
        <div class="pull-right">
            <div class="btn-wrap">
                <button type="submit" class="ui brand basic button">검색</button>
            </div>
        </div>
    </div>
    <div class="panel-body" style="overflow: hidden;">
        <div class="search-area">
            <div class="ui grid">
                <div class="row">
                    <div class="two wide column"><label class="control-label">프리뷰 그룹</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <form:select path="groupSeq">
                                <form:option value="" label="선택안함"/>
                                <form:options items="${previewGroups}"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="three wide column"><label class="control-label">데이터생성일</label></div>
                    <div class="seven wide column">
                        <div class="date-picker from-to" style="width: 100%">
                            <div class="dp-wrap" style="width: 50%">
                                <label for="createdStartDate" style="display:none">From</label>
                                <form:input path="createdStartDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="시작일"/>
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap" style="width: 50%">
                                <label for="createdEndDate" style="display:none">to</label>
                                <form:input path="createdEndDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="two wide column"><label class="control-label">담당자</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <form:select path="personIdInCharge">
                                <form:option value="" label="선택안함"/>
                                <form:options items="${persons}"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="three wide column"><label class="control-label">마지막상담일</label></div>
                    <div class="seven wide column">
                        <div class="date-picker from-to" style="width: 100%">
                            <div class="dp-wrap" style="width: 50%">
                                <label for="createdStartDate" style="display:none">From</label>
                                <form:input path="lastResultStartDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="시작일"/>
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap" style="width: 50%">
                                <label for="createdEndDate" style="display:none">to</label>
                                <form:input path="lastResultEndDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="two wide column"><label class="control-label">검색항목</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <form:select path="searchType">
                                <form:option value="" label="선택안함"/>
                                <form:option value="" label="--고객정보--"/>
                                <c:forEach var="e" items="${previewType.fields}">
                                    <c:if test="${e.issearch == 'Y'}">
                                        <form:option value="${e.fieldId.substring(previewType.kind.length() + 1)}"
                                                     label="${g.htmlQuote(e.fieldInfo)}"
                                                     data-type="${g.htmlQuote(e.fieldType)}"/>
                                    </c:if>
                                </c:forEach>
                                <form:option value="" label="--상담결과--"/>
                                <c:forEach var="e" items="${resultType.fields}">
                                    <c:if test="${e.issearch == 'Y'}">
                                        <form:option value="${e.fieldId.substring(resultType.kind.length() + 1)}"
                                                     label="${g.htmlQuote(e.fieldInfo)}"
                                                     data-type="${g.htmlQuote(e.fieldType)}"/>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                    <div class="seven wide column -search-type-sub-input" data-type="DATE">
                        <div class="date-picker from-to" style="width: 100%">
                            <div class="dp-wrap" style="width: 50%">
                                <label for="startDate" style="display:none">From</label>
                                <form:input path="startDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="시작일"/>
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap" style="width: 50%">
                                <label for="endDate" style="display:none">to</label>
                                <form:input path="endDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                    <div class="seven wide column -search-type-sub-input" data-type="CODE">
                        <div class="ui form">
                            <form:select path="code"/>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui checkbox">
                            <form:checkbox path="isConsultComplete" value="Y"/>
                            <label>상담미완료</label>
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
    </div>
    <div class="panel-body" style="overflow-x: auto;">
        <table class="ui celled table structured compact unstackable line-break-table ${pagination.totalCount > 0 ? "selectable-only" : null}" data-entity="PreviewData">
            <thead>
            <tr>
                <th rowspan="2">번호</th>
                <th colspan="3">기본정보</th>
                <c:if test="${previewType != null}">
                    <th colspan="${previewType.fields.size()}">고객정보필드</th>
                </c:if>
                <c:if test="${resultType != null}">
                    <th colspan="${resultType.fields.size()}">상담결과필드</th>
                </c:if>
            </tr>
            <tr>
                <th>데이터생성일</th>
                <th>상담자</th>
                <th>마지막상담일</th>

                <c:if test="${previewType != null}">
                    <c:forEach var="field" items="${previewType.fields}">
                        <th>${g.htmlQuote(field.fieldInfo)}</th>
                    </c:forEach>
                </c:if>
                <c:if test="${resultType != null}">
                    <c:forEach var="field" items="${resultType.fields}">
                        <th title="${g.htmlQuote(field.fieldInfo)}">${g.htmlQuote(field.fieldInfo)}</th>
                    </c:forEach>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${pagination.rows.size() > 0}">
                    <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                        <tr data-id="${g.htmlQuote(e.prvSysCustomId)}" data-phone="${e.result != null ? g.htmlQuote(e.result.customNumber) : null}">
                            <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>

                            <td><fmt:formatDate value="${e.prvSysUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${g.htmlQuote(e.prvSysDamdangName)}</td>
                            <td><fmt:formatDate value="${e.prvSysLastResultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                            <c:if test="${previewType != null}">
                                <c:forEach var="field" items="${previewType.fields}">
                                    <c:set var="value" value="${customIdToFieldNameToValueMap.get(e.prvSysCustomId).get(field.fieldId)}"/>
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
                                                <c:if test="${field.fieldType == 'NUMBER' && value != null && value != ''}">
                                                    <button type="button" class="ui icon button mini compact"
                                                            onclick="startPreview(${e.prvSysGroupId}, '${g.htmlQuote(g.escapeQuote(e.prvSysCustomId))}', '${g.htmlQuote(value)}')">
                                                        <i class="phone icon"></i>
                                                    </button>
                                                </c:if>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:if>
                            <c:if test="${resultType != null}">
                                <c:forEach var="field" items="${resultType.fields}">
                                    <c:set var="value" value="${e.result != null ? seqToFieldNameToValueMap.get(e.result.seq).get(field.fieldId).trim() : ''}"/>
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
                            </c:if>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="${4 + previewType.fields.size() + resultType.fields.size()}" class="null-data">조회된
                            데이터가 없습니다.
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <div class="panel-footer">
        <tags:pagination navigation="${pagination.navigation}"
                         url="${pageContext.request.contextPath}/counsel/preview/list-body" pageForm="${search}"
                         ajaxLoaderEnable="true"
                         ajaxLoaderTarget="#preview-list-body"/>
    </div>
</div>

<script>
    function setCounselPrvSearch() {
        const prvCodeMapInfo = {
            <c:forEach var="field" items="${previewType.fields}">
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
            <c:forEach var="field" items="${resultType.fields}">
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

        const searchForm = $('#search-preview-form');

        const groupSeq = searchForm.find('[name=searchType]').val();
        if (groupSeq) {
            loadPreviewCustomInput(groupSeq);
            loadPreviewCounselingInput(groupSeq);
        }

        <c:if test="${g.user.idType eq 'M'}">
        $('#personIdInCharge').attr('disabled', 'true');
        </c:if>

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
                const codeMap = prvCodeMapInfo[fieldId];
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
    }

    if (window.$) {
        setCounselPrvSearch()

        $('#search-preview-form').find('input[name=startDate]').val('${search.startDate}');
        $('#search-preview-form').find('input[name=endDate]').val('${search.endDate}');
        $('#search-preview-form').find('#keyword').val('${search.keyword}');
        $('#search-preview-form').find('#code').val('${search.code}');
    }
</script>
