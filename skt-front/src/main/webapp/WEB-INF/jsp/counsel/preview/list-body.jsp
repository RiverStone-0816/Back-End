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

<div class="sub-content ui container fluid unstackable">
    <form:form id="search-preview-form" modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
               action="${pageContext.request.contextPath}/counsel/preview/list-body"
               data-target="#preview-list-body">

        <div class="panel-heading">
            <div class="pull-left">
                <div class="panel-label">프리뷰리스트</div>
            </div>
            <div class="pull-right">
                <div class="ui slider checkbox checked">
                    <input type="checkbox" name="newsletter" id="_newsletter" checked="" tabindex="0" class="hidden"><label for="_newsletter">검색옵션 전체보기</label>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <div class="search-area">
                <table class="ui celled table compact unstackable">
                    <tr>
                        <th>프리뷰 그룹</th>
                        <td>
                            <div class="ui form">
                                <form:select path="groupSeq">
                                    <form:option value="" label="선택안함"/>
                                    <form:options items="${previewGroups}"/>
                                </form:select>
                            </div>
                        </td>
                        <th>데이터생성일</th>
                        <td>
                            <div class="ui action input calendar-area">
                                <form:input path="createdStartDate" cssClass="-datepicker" placeholder="시작일"/>
                                <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                                <span class="tilde">~</span>
                                <form:input path="createdEndDate" cssClass="-datepicker" placeholder="종료일"/>
                                <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>담당자</th>
                        <td>
                            <div class="ui form">
                                <form:select path="personIdInCharge">
                                    <form:option value="" label="선택안함"/>
                                    <form:options items="${persons}"/>
                                </form:select>
                            </div>
                        </td>
                        <th>마지막상담일</th>
                        <td>
                            <div class="ui action input calendar-area">
                                <form:input path="lastResultStartDate" cssClass="-datepicker" placeholder="시작일"/>
                                <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                                <span class="tilde">~</span>
                                <form:input path="lastResultEndDate" cssClass="-datepicker" placeholder="종료일"/>
                                <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>검색항목</th>
                        <td>
                            <div class="ui form">
                                <form:select path="searchType">
                                    <form:option value="" label="선택안함"/>
                                    <form:option value="" label="--고객정보--"/>
                                    <c:forEach var="e" items="${previewType.fields}">
                                        <c:if test="${e.issearch == 'Y'}">
                                            <form:option value="${e.fieldId.substring(previewType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}"
                                                         data-type="${g.htmlQuote(e.fieldType)}"/>
                                        </c:if>
                                    </c:forEach>
                                    <form:option value="" label="--상담결과--"/>
                                    <c:forEach var="e" items="${resultType.fields}">
                                        <c:if test="${e.issearch == 'Y'}">
                                            <form:option value="${e.fieldId.substring(resultType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}"
                                                         data-type="${g.htmlQuote(e.fieldType)}"/>
                                        </c:if>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </td>
                        <th>검색어</th>
                        <td>
                            <div class="ui action input calendar-area -search-type-sub-input" data-type="DATE">
                                <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                                <span class="tilde">~</span>
                                <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                <button type="button" class="ui basic button -click-prev"><img src="/resources/images/calendar.svg" alt="calendar"></button>
                            </div>
                            <div class="ui form -search-type-sub-input">
                                <form:input path="keyword"/>
                            </div>
                        </td>
                    </tr>
                </table>
                <div class="button-area remove-mb">
                    <div class="align-right">
                        <button type="submit" class="ui button sharp brand large">검색</button>
                        <%--<button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>--%>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
    <div class="panel">
        <div class="panel-heading">
            <div class="pull-left">
                <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span>건</h3>
            </div>
            <div class="pull-right">
                <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/counsel/preview/list-body" pageForm="${search}" ajaxLoaderEnable="true"
                                 ajaxLoaderTarget="#preview-list-body"/>
            </div>
        </div>
        <div class="panel-body">
            <table class="ui celled table border structured compact unstackable" data-entity="PreviewData">
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
                                        <td>
                                            <c:choose>
                                                <c:when test="${field.fieldType == 'CODE'}">
                                                    ${field.codes.stream().filter(e -> e.codeId == value).map(e -> e.codeName).findFirst().orElse('')}
                                                </c:when>
                                                <c:when test="${field.fieldType == 'MULTICODE'}">
                                                    <c:forEach var="v" items="${value.split(',')}">
                                                        ${field.codes.stream().filter(e -> e.codeId == v).map(e -> e.codeName).findFirst().orElse('')}&ensp;
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    ${g.htmlQuote(value)}
                                                    <c:if test="${field.fieldType == 'NUMBER' && value != null && value != ''}">
                                                        <button type="button" class="ui icon button mini compact"
                                                                onclick="startPreview(${e.prvSysGroupId}, '${g.htmlQuote(g.escapeQuote(e.prvSysCustomId))}', '${g.htmlQuote(value)}')">
                                                            <i class="phone icon"></i>
                                                        </button>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${resultType != null}">
                                    <c:forEach var="field" items="${resultType.fields}">
                                        <td>
                                            <c:if test="${e.result != null}">
                                                <c:set var="value" value="${seqToFieldNameToValueMap.get(e.result.seq).get(field.fieldId)}"/>
                                                <c:choose>
                                                    <c:when test="${field.fieldType == 'CODE'}">
                                                        ${field.codes.stream().filter(e -> e.codeId == value).map(e -> e.codeName).findFirst().orElse('')}
                                                    </c:when>
                                                    <c:when test="${field.fieldType == 'MULTICODE'}">
                                                        <c:forEach var="v" items="${value.split(',')}">
                                                            ${field.codes.stream().filter(e -> e.codeId == v).map(e -> e.codeName).findFirst().orElse('')}&ensp;
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${g.htmlQuote(value)}
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </td>
                                    </c:forEach>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="${4 + previewType.fields.size() + resultType.fields.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        $('#preview-list').on('change', '#search-preview-form [name=searchType]', function () {
            const type = $(this).find(':selected').attr('data-type');
            const subInput = $(this).closest('form').find('.-search-type-sub-input').hide();

            if (['DATE', 'DAY', 'DATETIME'].indexOf(type) >= 0) {
                subInput.filter('[data-type="DATE"]').show();
            } else {
                subInput.filter(':not([data-type="DATE"])').show();
            }
        });

        $('#search-preview-form [name=searchType]').change();

        function startPreview(previewGroupId, previewCustomId, customNumber) {
            if (ipccCommunicator.status.cMemberStatus === 1)
                return alert("상담중 상태에서는 전화 걸기가 불가능합니다.");

            if (customNumber) {
                const cid = $('#cid').val()
                ipccCommunicator.clickByCampaign(cid, customNumber, 'PRV', previewGroupId, previewCustomId);
            }
        }
        <c:if test="${g.user.idType eq 'M'}">
        $('#personIdInCharge').attr('disabled', 'true');
        </c:if>
    </script>
</tags:scripts>

<script>
    if (window.$) {
        $('#search-preview-form [name=searchType]').change();
        <c:if test="${g.user.idType eq 'M'}">
        $('#personIdInCharge').attr('disabled', 'true');
        </c:if>
    }
</script>
