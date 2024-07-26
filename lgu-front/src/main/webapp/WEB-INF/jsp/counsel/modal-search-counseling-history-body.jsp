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

<form:form id="search-counseling-history-form" modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
           action="${pageContext.request.contextPath}/counsel/modal-search-counseling-history-body?mode=${mode}"
           data-target="#modal-search-counseling-history-body">
    <div class="panel-heading">
        <div class="pull-left">
            검색
        </div>
        <div class="pull-right">
            <div class="btn-wrap">
                <button type="submit" class="ui brand basic button">검색</button>
                <button type="button" class="ui grey basic button" onclick="modalReload()">초기화</button>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="search-area">
            <div class="ui grid">
                <div class="row">
                    <div class="three wide column"><label class="control-label">검색기간</label></div>
                    <div class="thirteen wide column">
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
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="three wide column"><label class="control-label">상담자</label></div>
                    <div class="three wide column">
                        <div class="ui form">
                            <form:select path="userId">
                                <form:option value="" label="선택안함"/>
                                <form:options items="${users}" itemValue="id" itemLabel="idName"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="three wide column"><label class="control-label">고객DB그룹</label></div>
                    <div class="three wide column">
                        <div class="ui form">
                            <form:select path="groupSeq" items="${customdbGroups}"/>
                        </div>
                    </div>
                </div>
                <div class="row">
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
                                <form:option value="" label="--상담결과필드--"/>
                                <c:forEach var="e" items="${resultType.fields}">
                                    <c:if test="${e.issearch == 'Y'}">
                                        <form:option value="RS_${e.fieldId.substring(resultType.kind.length() + 1)}" label="${g.htmlQuote(e.fieldInfo)}"
                                                     data-type="${g.htmlQuote(e.fieldType)}"/>
                                    </c:if>
                                </c:forEach>
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
                                <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap">
                                <label for="endDate" style="display:none">to</label>
                                <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                    <div class="three wide column -search-type-sub-input" data-type="TEXT">
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
            <button type="button" class="ui basic green button" onclick="setCustomInfo()">고객/상담결과 내보내기</button>
        </div>
    </div>
    <div class="panel-body" style="width: 100%; overflow-x: auto;">
        <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="MaindbResult">
            <thead>
            <tr>
                <th rowspan="2">번호</th>
                <th colspan="7">상담기본정보</th>
                <th colspan="${resultType.fields.size()}">상담결과필드</th>
                <th colspan="${customDbType.fields.size()}">고객정보필드</th>
                <th colspan="3">채널정보</th>
            </tr>
            <tr>
                <th>채널</th>
                <th>수/발신</th>
                <th>상담등록시간</th>
                <th>상담원</th>
                <th>이관상담원</th>
                <th>고객채널정보</th>
                <th>플레이</th>

                <c:forEach var="field" items="${resultType.fields}">
                    <th title="${g.htmlQuote(field.fieldInfo)}">${g.htmlQuote(field.fieldInfo)}</th>
                </c:forEach>

                <c:forEach var="field" items="${customDbType.fields}">
                    <th>${g.htmlQuote(field.fieldInfo)}</th>
                </c:forEach>

                <th>전화번호</th>
                <th>이메일</th>
                <th>상담톡아이디</th>
            </tr>
            </thead>
            <tbody>

            <c:choose>
                <c:when test="${pagination.rows.size() > 0}">
                    <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                        <tr data-id="${g.htmlQuote(e.seq)}" data-group="${e.groupId}" data-custom-id="${e.customId}" data-room-id="${e.hangupMsg}" data-group-kind="${e.groupKind}">
                            <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>

                            <td>${g.htmlQuote(e.groupKindValue)}</td>
                            <td>${g.htmlQuote(e.inOutValue)}</td>
                            <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${g.htmlQuote(e.userName)}</td>
                            <td>${g.htmlQuote(e.userTrName)}</td>
                            <td>${g.htmlQuote(e.customNumber)}</td>
                            <td class="popup-td">
                                <div class="popup-element-wrap">
                                    <c:choose>
                                        <c:when test="${e.groupKind == 'PHONE' && not empty e.uniqueid}">
                                            <c:if test="${user.listeningRecordingAuthority.equals('MY') && e.userid.equals(user.id)
                                                       or user.listeningRecordingAuthority.equals('GROUP') && e.personList.groupCode.equals(user.groupCode)
                                                       or user.listeningRecordingAuthority.equals('ALL')}">
                                                <button type="button"
                                                        class="ui icon button mini compact -popup-records"
                                                        data-id="${e.seq}">
                                                    <i class="volume up icon"></i>
                                                </button>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${e.groupKind == 'TALK' && not empty e.hangupMsg}">
                                            <button type="button" class="ui icon button mini compact"
                                                    onclick="talkHistoryView('${e.hangupMsg}')">
                                                <i class="comment alternate icon"></i>
                                            </button>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </td>

                            <c:forEach var="field" items="${resultType.fields}">
                                <c:set var="value" value="${seqToFieldNameToValueMap.get(e.seq).get(field.fieldId)}"/>
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
                                                    <img class="profile-picture" src="${pageContext.request.contextPath}/api/maindb-data/resource?path=${g.urlEncode(value)}"
                                                         style="border-radius: 50%; width: 21px; height: 22px; overflow: hidden;"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="profile-picture" src="<c:url value="/resources/ipcc-messenger/images/person.png"/>" style="border-radius: 50%; width: 21px; overflow: hidden;"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            ${fn:length(g.htmlQuote(value)) < 15 ? g.htmlQuote(value) : fn:substring(g.htmlQuote(value), 0, 15).concat("···")}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>

                            <c:forEach var="field" items="${customDbType.fields}">
                                <c:set var="value" value="${customIdToFieldNameToValueMap.get(e.customId).get(field.fieldId)}"/>
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
                                                    <img class="profile-picture" src="${pageContext.request.contextPath}/api/maindb-data/resource?path=${g.urlEncode(value)}"
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
                                <c:forEach var="channel" items="${e.multichannelList}">
                                    <c:if test="${channel.channelType == 'PHONE'}">
                                        <text class="-phone-channel-data">${g.htmlQuote(channel.channelData)}</text>
                                        &ensp;
                                    </c:if>
                                </c:forEach>
                            </td>

                            <c:set var="value" value="${''}"/>
                            <c:forEach var="channel" items="${e.multichannelList}">
                                <c:if test="${channel.channelType == 'EMAIL'}">
                                    <c:set var="value" value="${value.concat(channel.channelData).concat(' ')}"/>
                                </c:if>
                            </c:forEach>
                            <td>${g.htmlQuote(value)}</td>

                            <c:set var="value" value="${''}"/>
                            <c:forEach var="channel" items="${e.multichannelList}">
                                <c:if test="${channel.channelType == 'TALK'}">
                                    <c:set var="value" value="${value.concat(channel.channelData).concat(' ').split('_')[1]}"/>
                                </c:if>
                            </c:forEach>
                            <td>${g.htmlQuote(value)}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="${11 + customDbType.fields.size() + resultType.fields.size()}" class="null-data">조회된 데이터가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

<script>

    function modalReload() {
        $('#modal-menu').modalHide();
        popupDraggableModalFromReceivedHtml('/counsel/modal-search-counseling-history', 'modal-search-counseling-history');
    }

    function setCustomInfo() {
        const seq = getEntityId('MaindbResult');
        const customId = getEntityId('MaindbResult', 'custom-id');
        const roomId = getEntityId('MaindbResult', 'room-id');
        const channel = getEntityId('MaindbResult', 'group-kind');
        const panelMode = '${mode}';

        if (!seq) return;

        const phoneNumber = $('.selectable-only[data-entity="MaindbResult"] tr[data-id="' + seq + '"] .-phone-channel-data:first').text();

        if (channel === 'PHONE') {
            loadCustomInput(${search.groupSeq}, customId, phoneNumber, '', '', '', seq);
        } else if (panelMode === 'call') {
            loadCustomInput(${search.groupSeq}, customId, phoneNumber, '', '', '', seq);
        } else if (panelMode === 'talk') {
            loadTalkCustomInput(${search.groupSeq}, customId, roomId, '', '', '',  seq);
            if (channel === 'TALK') {
                viewTalkRoom(roomId, false);
            }
        }

        $('#search-counseling-history-form').closest('.modal').modalHide();
    }

    (function () {
        const modal = $('#search-counseling-history-form').closest('.modal');

        modal.find('.-button-set-range').click(function () {
            modal.find('.-button-set-range').removeClass('active');
            $(this).addClass('active');

            const interval = $(this).attr('data-interval');
            const number = $(this).attr('data-number');
            const endDate = modal.find('[name=createdEndDate]');
            const startDate = modal.find('[name=createdStartDate]');

            if (!endDate.val())
                endDate.val(moment().format('YYYY-MM-DD'));

            startDate.val(moment(endDate.val()).add(parseInt(number) * -1, interval).add(1, 'days').format('YYYY-MM-DD'));
        });

        modal.find('.-popup-records').click(function (event) {
            event.stopPropagation();

            const $this = $(this);
            if ($this.attr('data-has-records'))
                return;

            popupReceivedHtml('/admin/application/maindb/result/' + $this.attr('data-id') + '/modal-records', 'modal-records').done(function (html) {
                const mixedNodes = $.parseHTML(html, null, true);

                const modal = (function () {
                    for (let i = 0; i < mixedNodes.length; i++) {
                        const node = $(mixedNodes[i]);
                        if (node.is('.ui.modal'))
                            return node;
                    }
                    throw 'cannot find modal element';
                })();

                $this.after(modal);
                modal.find('audio').each(function () {
                    maudio({obj: this});
                });
            });
        });

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
            <c:forEach var="field" items="${resultType.fields}">
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

        modal.find('[name=searchType]').change(function () {
            const type = $(this).find(':selected').attr('data-type');
            const fieldId = $(this).find(':selected').val();
            const subInput = modal.find('.-search-type-sub-input').hide();
            const codeSelect = modal.find('#code');

            modal.find('input[name=startDate]').val('');
            modal.find('input[name=endDate]').val('');
            codeSelect.empty();
            modal.find('#keyword').val('');

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

        modal.find('[name=channelType]').change(function () {
            modal.find('[name=channelData]').val('');
        });
    })();

    function setSearch() {
        $('#search-counseling-history-form').find('#keyword').val('${search.keyword}').trigger("change");
        $('#search-counseling-history-form').find('#code').val('${search.code}').trigger("change");

        $('#search-counseling-history-form').find('input[name=startDate]').val('${search.startDate}');
        $('#search-counseling-history-form').find('input[name=endDate]').val('${search.endDate}');
    }

    setSearch();
</script>
