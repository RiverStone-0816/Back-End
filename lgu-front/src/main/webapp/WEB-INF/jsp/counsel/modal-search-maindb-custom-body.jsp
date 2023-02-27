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

<form:form id="search-maindb-custom-form" modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
           action="${pageContext.request.contextPath}/counsel/modal-search-maindb-custom-body?type=${pageContext.request.getParameter('type')}&roomId=${pageContext.request.getParameter('roomId')}&senderKey=${pageContext.request.getParameter('senderKey')}&userKey=${pageContext.request.getParameter('userKey')}"
           data-target="#modal-search-maindb-custom-body">
    <input type="hidden" name="type" value="${pageContext.request.getParameter('type')}"/>
    <div class="panel-heading">
        <div class="pull-left">
            검색
        </div>
        <div class="pull-right">
            <div class="btn-wrap">
                <button type="submit" class="ui brand basic button">검색</button>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="search-area">
            <div class="ui grid">
                <div class="row">
                    <div class="three wide column"><label class="control-label">고객DB그룹</label></div>
                    <div class="three wide column">
                        <div class="ui form">
                            <form:select path="groupSeq" items="${customdbGroups}"/>
                        </div>
                    </div>
                    <div class="three wide column"><label class="control-label">채널검색</label></div>
                    <div class="three wide column">
                        <div class="ui form">
                            <form:select path="channelType">
                                <form:option value="PHONE" label="전화번호"/>
                                <form:option value="EMAIL" label="이메일"/>
                                <form:option value="TALK" label="상담톡"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui input fluid">
                            <form:input path="channelData"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="three wide column"><label class="control-label">검색항목</label></div>
                    <div class="three wide column">
                        <div class="ui form">
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
                    </div>
                    <div class="seven wide column -search-type-sub-input" data-type="DATE">
                        <div class="date-picker from-to">
                            <div class="dp-wrap">
                                <label for="startDate" style="display:none">From</label>
                                <form:input path="startDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="시작일"/>
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap">
                                <label for="endDate" style="display:none">to</label>
                                <form:input path="endDate" cssClass="-datepicker" cssStyle="width: 100%;" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                    <div class="three wide column -search-type-sub-input" data-type="TEXT">
                        <div class="ui input fluid">
                            <form:input path="keyword"/>
                        </div>
                    </div>
                    <div class="three wide column -search-type-sub-input" data-type="CODE">
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
            <button type="button" class="ui basic green button" onclick="setCustomInfo()">고객정보 내보내기</button>
        </div>
    </div>
    <div class="panel-body" style="width: 100%; overflow-x: auto;">
        <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="MaindbData">
            <thead>
            <tr>
                <th rowspan="2">번호</th>
                <th>기본정보</th>
                <th colspan="${customDbType.fields.size()}">고객정보필드</th>
                <th colspan="3">멀티채널정보(전화번호,상담톡아이디)</th>
            </tr>
            <tr>
                <th class="one wide" title="데이터생성일">데이터생성일</th>

                <c:forEach var="field" items="${customDbType.fields}">
                    <th title="${g.htmlQuote(field.fieldInfo)}">${g.htmlQuote(field.fieldInfo)}</th>
                </c:forEach>

                <th type="전화번호">전화번호</th>
                <th type="이메일">이메일</th>
                <th type="상담톡아이디">상담톡아이디</th>
            </tr>
            </thead>
            <tbody>

            <c:choose>
                <c:when test="${pagination.rows.size() > 0}">
                    <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                        <tr data-id="${g.htmlQuote(e.maindbSysCustomId)}" data-custom="${g.htmlQuote(e.maindbString_1)}" data-group="${search.groupSeq}">
                            <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                            <td><fmt:formatDate value="${e.maindbSysUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

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
                                                    <img class="profile-picture" src="<c:url value="/resources/ipcc-messenger/images/person.png"/>" style="border-radius: 50%; width: 21px; overflow: hidden;"/>
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
                                <c:forEach var="field" items="${e.multichannelList}">
                                    <c:if test="${field.channelType == 'PHONE'}">
                                        <text class="-phone-channel-data">${g.htmlQuote(field.channelData)}</text>
                                        &ensp;
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="field" items="${e.multichannelList}">
                                    <c:if test="${field.channelType == 'EMAIL'}">
                                        ${g.htmlQuote(field.channelData)}&ensp;
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="channel" items="${e.multichannelList}">
                                    <c:if test="${channel.channelType == 'TALK'}">
                                        ${g.htmlQuote(channel.channelData.split('_')[1])}&ensp;
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="${5 + customDbType.fields.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

<script>
    function setCustomInfo() {
        const customId = getEntityId('MaindbData');
        const customName = getEntityId('MaindbData', 'custom');
        const channel = $('#talk-custom-input').find('[name=channels]').attr('data-channel');
        if (!customId) return;

        // if (ipccCommunicator.status.cMemberStatus !== 1 && ipccCommunicator.status.cMemberStatus !== 2)
        //     $('#counseling-target').text($('.selectable-only[data-entity="MaindbData"] tr[data-id="' + customId + '"] .-phone-channel-data:first').text());

        <c:choose>
        <c:when test="${pageContext.request.getParameter('type') == 'CALL'}">
        loadCustomInput(${search.groupSeq}, customId);
        </c:when>
        <c:otherwise>
        if (['eicn','kakao'].includes(channel))
            talkCommunicator.sendCustomMatch('${g.escapeQuote(pageContext.request.getParameter('roomId'))}', '${g.escapeQuote(pageContext.request.getParameter('senderKey'))}',
                '${g.escapeQuote(pageContext.request.getParameter('userKey'))}', '${search.groupSeq}', customId, customName, channel);

        loadTalkCustomInput(${search.groupSeq}, customId,
            '${g.escapeQuote(pageContext.request.getParameter('roomId'))}',
            '${g.escapeQuote(pageContext.request.getParameter('senderKey'))}',
            '${g.escapeQuote(pageContext.request.getParameter('userKey'))}');
        </c:otherwise>
        </c:choose>

        $('#search-maindb-custom-form').closest('.modal').modalHide();
    }

    $('#search-maindb-custom-form [name=searchType]').change(function () {
        const type = $(this).find(':selected').attr('data-type');
        const fieldId = $(this).find(':selected').val();
        const subInput = $(this).closest('form').find('.-search-type-sub-input').hide();
        const codeSelect = $(this).closest('form').find('#code');

        $('input[name=startDate]').val('');
        $('input[name=endDate]').val('');
        codeSelect.empty();
        $('#keyword').val('');

        if (['DATE', 'DAY', 'DATETIME'].indexOf(type) >= 0) {
            subInput.filter('[data-type="DATE"]').show();
        } else if (['CODE', 'MULTICODE'].indexOf(type) >= 0) {
            const codeMap = JSON.parse('${codeMap}')[fieldId];

            codeSelect.append($('<option/>', {value: '', text: '선택안함'}));

            if (fieldId === 'MAINDB_CODE_3') {
                let sortCodeMap = [];

                for (const key in codeMap){
                    sortCodeMap.push({key : key, value: codeMap[key]});
                }

                sortCodeMap.sort(function (a, b){
                    return (a.value < b.value) ? -1 : (a.value > b.value) ? 1 : 0;
                })

                for (const code of sortCodeMap){
                    codeSelect.append($('<option/>', {value: code.key, text: code.value}));
                }
            } else {
                for (const key in codeMap) {
                    if (codeMap.hasOwnProperty(key)) {
                        codeSelect.append($('<option/>', {value: key, text: codeMap[key]}));
                    }
                }
            }

            subInput.filter('[data-type="CODE"]').show();
        } else {
            subInput.filter('[data-type="TEXT"]').show();
        }
    }).change();

    function setSearch() {
        $('#search-maindb-custom-form').find('#keyword').val('${search.keyword}').trigger("change");
        $('#search-maindb-custom-form').find('#code').val('${search.code}').trigger("change");

        $('#search-maindb-custom-form').find('input[name=startDate]').val('${search.startDate}');
        $('#search-maindb-custom-form').find('input[name=endDate]').val('${search.endDate}');
    }

    setSearch();
</script>
