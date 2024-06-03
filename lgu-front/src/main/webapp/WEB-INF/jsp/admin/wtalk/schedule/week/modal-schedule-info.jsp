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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/wtalk-schedule-week/"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">상담톡주간스케쥴러[추가]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">채널타입</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="channelType" value="kakao" checked>
                                    <label>카카오상담톡</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <input type="radio" name="channelType" value="eicn">
                                    <label>웹챗</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">추가가능상담톡서비스</label></div>
                <div class="eight wide column"><label class="control-label">추가된상담톡서비스</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${talkServices}">
                                    <option value="${g.htmlQuote(e.key)}" class="kakao-service">${g.htmlQuote(e.value)}</option>
                                </c:forEach>
                                <c:forEach var="e" items="${chatBotServices}">
                                    <option value="${g.htmlQuote(e.key)}" class="eicn-service" style="display: none">${g.htmlQuote(e.value)}</option>
                                </c:forEach>
                            </select>
                            <div class="ui action input pt5">
                                <input type="text" class="-left-selector-search"/>
                                <button type="button" class="ui button mini -left-selector-search-button">검색</button>
                            </div>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="senderKeys" class="form-control -right-selector" size="8" multiple="multiple"></select>
                            <div class="ui action input pt5">
                                <input type="text" class="-right-selector-search"/>
                                <button type="button" class="ui button mini -right-selector-search-button">검색</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">스케쥴유형선택</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select id="groupId" path="groupId">
                            <form:option value="" label="선택안함"/>
                            <c:forEach var="e" items="${scheduleInfos}">
                                <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="three wide column"><label class="control-label">요일 설정</label></div>
                <div class="five wide column">
                    <div class="ui toggle checkbox isEach">
                        <form:hidden path="isEach"/>
                        <input type="checkbox" id="isEach" name="isEach" value="true" tabindex="0" class="hidden" style="display: none;">
                        <label>월~일요일까지 일괄적용</label>
                    </div>
                </div>
            </div>
            <div class="row" id="each-week-schedule" style="display: none;">
                <div class="four wide column"><label class="control-label">개별 스케쥴 설정</label></div>
                <div class="twelve wide column">
                    <div class="ui grid remove-margin">
                        <div class="eight wide column remove-padding">
                            <div class=" ui form flex" style="margin-bottom: 5px;">
                                <label style="min-width: 50px">월요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="mon1" name="mon1">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class=" ui form flex" style="margin-bottom: 5px;">
                                <label style="min-width: 50px">화요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="tue2" name="tue2">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class=" ui form flex" style="margin-bottom: 5px;">
                                <label style="min-width: 50px">수요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="wed3" name="wed3">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class=" ui form flex" style="margin-bottom: 5px;">
                                <label style="min-width: 50px">목요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="thu4" name="thu4">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="eight wide column  remove-padding">
                            <div class=" ui form flex" style="margin-bottom: 5px;">
                                <label style="min-width: 50px">금요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="fri5" name="fri5">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class=" ui form flex" style="margin-bottom: 5px;">
                                <label style="min-width: 50px">토요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="sat6" name="sat6">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class=" ui form flex">
                                <label style="min-width: 50px">일요일 : </label>
                                <div style="margin: 0 10px;width: 100%;">
                                    <select id="sun7" name="sun7">
                                        <option value="">선택안함</option>
                                        <c:forEach var="e" items="${scheduleInfos}">
                                            <option value="${e.parent}" class="-channel-type" data-type="${e.channelType}"  style="${e.channelType == 'eicn' ? 'display: none' : ''}">${g.htmlQuote(e.name)}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="sixteen wide column remove-padding">
                            <label class="text-primary">
                                미선택 시 위에서 선택한 스케줄 유형으로 자동 적용됩니다.
                            </label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" id="schedule-info"></div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button -submit-form">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.senderKeys = [];
        modal.find('[name="senderKeys"] option').each(function () {
            data.senderKeys.push($(this).val());
        });

        if (data.isEach) {
            const groupId = data.groupId;
            data.weeks = {};

            data.weeks["1Mon"] = data.mon1 ? data.mon1 : groupId;
            data.weeks["2Tue"] = data.tue2 ? data.tue2 : groupId;
            data.weeks["3Wed"] = data.wed3 ? data.wed3 : groupId;
            data.weeks["4Thu"] = data.thu4 ? data.thu4 : groupId;
            data.weeks["5Fri"] = data.fri5 ? data.fri5 : groupId;
            data.weeks["6Sat"] = data.sat6 ? data.sat6 : groupId;
            data.weeks["7Sun"] = data.sun7 ? data.sun7 : groupId;
        }
    };

    modal.find('.-submit-form').click(function () {
        $('#modal-schedule-info').submit();
    });

    modal.find('[name=groupId]').change(function () {
        modal.find('#schedule-info').empty();
        if (this.value !== '') {
            receiveHtml(contextPath + '/admin/wtalk/schedule/week/modal-view-schedule-group?parent=' + this.value).done(function (html) {
                const mixedNodes = $.parseHTML(html, null, true);

                const ui = (function () {
                    for (let i = 0; i < mixedNodes.length; i++) {
                        const node = $(mixedNodes[i]);
                        if (node.is('.ui'))
                            return node;
                    }
                    throw 'cannot found .ui';
                })();

                const schedule = ui.find('.row').children('table');

                modal.find('#schedule-info').append(schedule);
                schedule.bindHelpers();
            });
        }
    });

    modal.find('[name=channelType]').change(() => {
        modal.find('.-left-selector').children().dblclick();

        modal.find('#groupId').val('');
        modal.find('#groupId').change();
        modal.find('.-channel-type').hide();
        modal.find('.-right-selector').children().hide();

        if (modal.find('[name=channelType]:checked').val() === 'kakao') {
            modal.find('.-right-selector').find('.kakao-service').show();
            modal.find('#groupId').find('[data-type=kakao]').show();
        }
        else {
            modal.find('.-right-selector').find('.eicn-service').show();
            modal.find('#groupId').find('[data-type=eicn]').show();
        }
    });

    modal.find('[name=isEach]').change(function () {
        const isEach = $(this).parent('.ui.checkbox').find('input').is(':checked');

        if (isEach)
            modal.find('#each-week-schedule').css('display', 'flex');
        else
            modal.find('#each-week-schedule').css('display', 'none');
    });

    modal.find('.ui.checkbox.isEach').checkbox({
        onChecked: function () {
            $(this).siblings('label').text('개별 요일 설정');
            $(this).closest('td').find('input').val(0);
        },
        onUnchecked: function () {
            $(this).siblings('label').text('월~일요일까지 일괄적용');
            $(this).closest('td').find('input').val(1);
        },
    });
</script>
