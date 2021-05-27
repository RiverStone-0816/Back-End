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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/outbound/preview/data/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                        <span style="color: red;padding-left:20px;">※ [프리뷰 그룹] 선택 후 검색을 눌러주세요.</span>
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
                                <div class="two wide column"><label class="control-label">프리뷰 그룹</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="groupSeq">
                                            <form:option value="" label="선택안함" />
                                            <form:options  items="${previewGroups}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">데이터생성일</label></div>
                                <div class="five wide column">
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
                                </div>
                                <div class="two wide column"><label class="control-label">담당자</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="personIdInCharge">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${persons}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">마지막상담일</label></div>
                                <div class="five wide column">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="createdStartDate" style="display:none">From</label>
                                            <form:input path="lastResultStartDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="createdEndDate" style="display:none">to</label>
                                            <form:input path="lastResultEndDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">검색항목</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="searchType">
                                            <form:option value="" label="선택안함"/>
                                            <form:option value="" label="--고객정보--"/>
                                            <c:forEach var="e" items="${previewType.fields}">
                                                <c:if test="${e.issearch == 'Y'}">
                                                    <form:option value="PRV_${e.fieldId.substring(previewType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}"
                                                                 data-type="${g.htmlQuote(e.fieldType)}"/>
                                                </c:if>
                                            </c:forEach>
                                            <form:option value="" label="--상담결과--"/>
                                            <c:forEach var="e" items="${resultType.fields}">
                                                <c:if test="${e.issearch == 'Y'}">
                                                    <form:option value="RS_${e.fieldId.substring(resultType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}"
                                                                 data-type="${g.htmlQuote(e.fieldType)}"/>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <%--<div class="five wide column -search-type-sub-input" data-type="DATE">
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
                                </div>--%>
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
                        <button class="ui basic button -control-entity" data-entity="PreviewData" style="display: none;" onclick="popupRedistributionModal()">재분배</button>
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                    </div>
                </div>
                <div class="panel-body" style="overflow-x: auto;">
                    <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable" : null}" data-entity="PreviewData">
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

                            <th rowspan="2">관리</th>
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
                                    <tr data-id="${g.htmlQuote(e.prvSysCustomId)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>

                                        <td><fmt:formatDate value="${e.prvSysUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.prvSysDamdangName)}</td>
                                        <td><fmt:formatDate value="${e.prvSysLastResultDate.equals(e.prvSysUploadDate) ? '' : e.prvSysLastResultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

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
                                        <td>
                                            <button class="ui mini compact button" onclick="popupModal('${g.htmlQuote(e.prvSysCustomId)}')">수정</button>
                                            <button class="ui mini compact button" onclick="deleteEntity('${g.htmlQuote(e.prvSysCustomId)}')">삭제</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="${5 + previewType.fields.size() + resultType.fields.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/outbound/preview/data/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            $('#search-form [name=searchType]').change(function () {
                const type = $(this).find(':selected').attr('data-type');
                const subInput = $('.-search-type-sub-input').hide();

                if (['DATE', 'DAY', 'DATETIME'].indexOf(type) >= 0) {
                    subInput.filter('[data-type="DATE"]').show();
                } else {
                    subInput.filter(':not([data-type="DATE"])').show();
                }
            }).change();

            function popupModal(id) {
                if('${search.groupSeq}' == ''){
                    alert('프리뷰 그룹을 [ 검색 ] 해 주세요.');
                }else{
                    popupReceivedHtml('/admin/outbound/preview/data/${search.groupSeq}/data/' + encodeURIComponent(id || 'new') + '/modal', 'modal-data');
                }
            }

            function popupRedistributionModal() {
                const list = [];
                $('table[data-entity="PreviewData"] tr.active').each(function () {
                    list.push($(this).attr('data-id'));
                });
                if (!list.length) return;

                const url = $.addQueryString('/admin/outbound/preview/data/modal-redistribution', {groupSeq: '${search.groupSeq}', customIdList: list});
                popupReceivedHtml(url, 'modal-redistribution');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/preview-data/${search.groupSeq}/data/' + id).done(function () {
                        reload();
                    });
                });
            }

            function downloadExcel() {
                window.open(contextPath + '/admin/outbound/preview/data/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
            <c:if test="${previewGroups == null}">
            alert("[프리뷰 그룹] 이 없습니다.");
            </c:if>
            <c:if test="${g.user.idType eq 'M'}">
            $('#personIdInCharge').attr('disabled', 'true');
            </c:if>
        </script>
    </tags:scripts>
</tags:tabContentLayout>
