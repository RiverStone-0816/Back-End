<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/application/maindb/data/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/application/maindb/data/"))}</div>
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
                                <th>고객DB그룹</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="groupSeq">
                                            <form:option value="" label="선택안함"/>
                                            <c:forEach var="group" items="${customdbGroups}">
                                                <form:option value="${group.key}" label="${group.value}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </td>
                                <th>채널검색</th>
                                <td>
                                    <div class="ui form flex">
                                        <form:select path="channelType">
                                            <form:option value="PHONE" label="전화번호"/>
                                            <form:option value="EMAIL" label="이메일"/>
                                            <form:option value="TALK" label="채팅상담"/>
                                        </form:select>
                                        <form:input path="channelData" cssClass="ml5"/>
                                    </div>
                                </td>
                                <th>검색항목</th>
                                <td colspan="3">
                                    <div class="ui form flex">
                                        <div class="ip-wrap">
                                            <form:select path="searchType">
                                                <form:option value="" label="선택안함"/>
                                                <form:option value="" label="--데이타기본정보--"/>
                                                <form:option value="${search.SEARCH_TYPE_CREATE_DATE}" label="데이터 생성일" data-type="DATE"/>
                                                <form:option value="" label="--고객정보필드--"/>
                                                <c:forEach var="e" items="${customDbType.fields}">
                                                    <c:if test="${e.issearch == 'Y'}">
                                                        <form:option value="MAINDB_${e.fieldId.substring(customDbType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}"
                                                                     data-type="${g.htmlQuote(e.fieldType)}"/>
                                                    </c:if>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                        <div class="ip-wrap -search-type-sub-input" data-type="DATE">
                                            <div class="ui action input calendar-area">
                                                <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                                <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                                <span class="tilde">~</span>
                                                <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                                <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                            </div>
                                        </div>
                                        <div class="ip-wrap -search-type-sub-input" data-type="TEXT">
                                            <form:input path="keyword"/>
                                        </div>
                                        <div class="ip-wrap -search-type-sub-input" data-type="CODE">
                                            <div class="ui form">
                                                <form:select path="code"/>
                                            </div>
                                        </div>
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
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                        <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal(null, ${search.groupSeq})">추가</button>
                            <button class="ui button -control-entity" data-entity="MaindbData" style="display: none;"
                                    onclick="popupModal(getEntityId('MaindbData'), getEntityId('MaindbData', 'group'))">수정
                            </button>
                            <c:if test="${'A|J'.contains(g.user.idType)}">
                            <button class="ui button -control-entity" data-entity="MaindbData" style="display: none;" onclick="deleteEntity(getEntityId('MaindbData'))">삭제</button>
                            </c:if>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/application/maindb/data/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="overflow-auto">
                        <table class="ui celled table structured border-top num-tbl unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="MaindbData">
                            <thead>
                            <tr>
                                <th rowspan="2">선택</th>
                                <th rowspan="2">번호</th>
                                <th colspan="2">기본정보</th>
                                <th colspan="${customDbType.fields.size()}">고객정보</th>
                                <th colspan="3">채널정보</th>
                            </tr>
                            <tr>
                                <th class="one wide">데이터생성일</th>
                                <th>담당자</th>

                                <c:forEach var="field" items="${customDbType.fields}">
                                    <th>${g.htmlQuote(field.fieldInfo)}</th>
                                </c:forEach>

                                <th>전화번호</th>
                                <th>이메일</th>
                                <th>채팅상담아이디</th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:choose>
                                <c:when test="${pagination.rows.size() > 0}">
                                    <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                        <tr data-id="${g.htmlQuote(e.maindbSysCustomId)}" data-group="${search.groupSeq}">
                                            <td>
                                                <div class="ui radio checkbox">
                                                    <input type="radio" name="radio">
                                                </div>
                                            </td>
                                            <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                            <td><fmt:formatDate value="${e.maindbSysUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>${g.htmlQuote(e.maindbSysDamdangName)}</td>

                                            <c:forEach var="field" items="${customDbType.fields}">
                                                <c:set var="value" value="${customIdToFieldNameToValueMap.get(e.maindbSysCustomId).get(field.fieldId)}"/>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${field.fieldType == 'CODE'}">
                                                            ${field.codes.stream().filter(code -> code.codeId == value).map(code -> code.codeName).findFirst().orElse('')}
                                                        </c:when>
                                                        <c:when test="${field.fieldType == 'MULTICODE'}">
                                                            <c:forEach var="v" items="${value.split(',')}">
                                                                ${field.codes.stream().filter(code -> code.codeId == v).map(code -> code.codeName).findFirst().orElse('')}&ensp;
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:when test="${field.fieldType == 'IMG'}">
                                                            <c:choose>
                                                                <c:when test="${value.length() > 0}">
                                                                    <img class="profile-picture" src="${apiServerUrl}/api/v1/admin/application/maindb/custominfo/resource?path=${g.urlEncode(value)}&token=${accessToken}"
                                                                         style="border-radius: 50%; width: 21px; height: 22px; overflow: hidden;"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img class="profile-picture" src="<c:url value="/resources/images/person.png"/>" style="border-radius: 50%; width: 21px; overflow: hidden;"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${g.htmlQuote(value)}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </c:forEach>

                                            <td>
                                                <c:forEach var="channel" items="${e.multichannelList}">
                                                    <c:if test="${channel.channelType == 'PHONE'}">
                                                        ${g.htmlQuote(channel.channelData)}&ensp;
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:forEach var="channel" items="${e.multichannelList}">
                                                    <c:if test="${channel.channelType == 'EMAIL'}">
                                                        ${g.htmlQuote(channel.channelData)}&ensp;
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:forEach var="channel" items="${e.multichannelList}">
                                                    <c:if test="${channel.channelType == 'TALK'}">
                                                        ${g.htmlQuote(channel.channelData)}&ensp;
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="${7 + customDbType.fields.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const searchForm = $('#search-form');

            const codeMapInfo = {
                <c:forEach var="field" items="${customDbType.fields}">
                <c:if test="${fn:contains(field.fieldId, 'CODE') and field.issearch == 'Y'}">
                '${field.fieldId}' : {
                    <c:forEach var="code" items="${field.codes}">
                    '${code.sequence}' : {
                        'codeId' : '${code.codeId}',
                        'codeName' : '${code.codeName}',
                    },
                    </c:forEach>
                },
                </c:if>
                </c:forEach>
            };

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
                    for(let i = 0 ; i < codeLength ; i++) {
                        codeSelect.append($('<option/>', {value: codeMap[i].codeId, text: codeMap[i].codeName}));
                    }

                    subInput.filter('[data-type="CODE"]').show();
                } else {
                    subInput.filter('[data-type="TEXT"]').show();
                }
            }).change();

            function popupModal(id, groupSeq) {
                popupReceivedHtml('/admin/application/maindb/data/' + encodeURIComponent(id || 'new') + '/modal?groupSeq=' + groupSeq, 'modal-data');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/maindb-data/' + encodeURIComponent(id)).done(function () {
                        reload();
                    });
                });
            }

            function downloadExcel() {
                window.open(contextPath + '/admin/application/maindb/data/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
            <c:if test="${customdbGroups == null}">
            alert("[고객DB그룹] 이 없습니다.");
            </c:if>

            $(window).on('load', function () {
                $('#keyword').val('${search.keyword}').trigger("change");
                $('#code').val('${search.code}').trigger("change");

                $('input[name=startDate]').val('${search.startDate}');
                $('input[name=endDate]').val('${search.endDate}');
            });
        </script>
    </tags:scripts>
</tags:tabContentLayout>
