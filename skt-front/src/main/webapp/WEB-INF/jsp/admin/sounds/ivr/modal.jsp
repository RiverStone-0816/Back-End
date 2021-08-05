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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%@ page import="kr.co.eicn.ippbx.model.enums.IvrMenuType" %>

<c:set var="CONNECT_MENU" value="${IvrMenuType.CONNECT_MENU.code}"/>
<c:set var="CONNECT_REPRESENTABLE_NUMBER" value="${IvrMenuType.CONNECT_REPRESENTABLE_NUMBER.code}"/>
<c:set var="CONNECT_HUNT_NUMBER" value="${IvrMenuType.CONNECT_HUNT_NUMBER.code}"/>
<c:set var="CONNECT_INNER_NUMBER" value="${IvrMenuType.CONNECT_INNER_NUMBER.code}"/>
<c:set var="CONNECT_OUTER_NUMBER" value="${IvrMenuType.CONNECT_OUTER_NUMBER.code}"/>
<c:set var="FINISH_AFTER_DONE_EXCEPTION" value="${IvrMenuType.FINISH_AFTER_DONE_EXCEPTION.code}"/>
<c:set var="CONNECT_MENU_AFTER_DONE_EXCEPTION" value="${IvrMenuType.CONNECT_MENU_AFTER_DONE_EXCEPTION.code}"/>
<c:set var="CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION" value="${IvrMenuType.CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION.code}"/>
<c:set var="CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION" value="${IvrMenuType.CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION.code}"/>
<c:set var="CONNECT_INNER_NUMBER_DIRECTLY" value="${IvrMenuType.CONNECT_INNER_NUMBER_DIRECTLY.code}"/>
<c:set var="RETRY_MENU" value="${IvrMenuType.RETRY_MENU.code}"/>
<c:set var="FINISH_AFTER_CONNECT_SOUND" value="${IvrMenuType.FINISH_AFTER_CONNECT_SOUND.code}"/>
<c:set var="TO_PREVIOUS_MENU" value="${IvrMenuType.TO_PREVIOUS_MENU.code}"/>
<c:set var="TO_FIRST_MENU" value="${IvrMenuType.TO_FIRST_MENU.code}"/>

<form:form modelAttribute="form" cssClass="ui modal small -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/ivr/${entity == null ? null : entity.seq}"
           data-before="prepareWriteFormData" data-done="doneWriteFormData">

    <i class="close icon"></i>
    <div class="header">${typeName}[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <form:hidden path="type"/>
        <form:hidden path="posX"/>
        <form:hidden path="posY"/>
        <form:hidden path="parentSeq"/>
        <div class="ui centered grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">메뉴명(*)</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <c:if test="${(entity == null && form.parentSeq == null) || (entity != null && entity.treeName.split('[_]')[1] == null)}">
                <div class="row">
                    <div class="four wide column">
                        <label class="control-label">인트로음원선택</label>
                        <button type="button" class="ui icon button mini compact -sound-download -play-trigger" data-sound-input="[name=introSoundCode]" data-tts-input="[name=introTtsData]">
                            <i class="arrow down icon"></i>
                        </button>
                        <button type="button" class="ui icon button mini compact -sound-play -play-trigger" data-sound-input="[name=introSoundCode]" data-tts-input="[name=introTtsData]">
                            <i class="volume up icon"></i>
                        </button>
                        <div class="ui popup top left"></div>
                    </div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <form:select path="introSoundCode" >
                                <form:option value="" label="선택안함"/>
                                <form:option value="TTS" label="TTS입력"/>
                                <c:forEach var="e" items="${sounds}">
                                    <form:option value="${e.seq}" label="${e.soundName}"/>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="row -intro-tts-data">
                    <div class="four wide column"><label class="control-label">인트로TTS입력</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <textarea name="introTtsData">${entity != null && entity.ttsData != null ? g.htmlQuote(entity.ttsData.split('[|]')[0]) : ''}</textarea>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[FINISH_AFTER_DONE_EXCEPTION,
                            CONNECT_MENU_AFTER_DONE_EXCEPTION,
                            CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION,
                            CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">예외컨텍스트선택(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select name="typeDataStrings" data-multiple="multiple" class="wantsort2">
                                <option value="">선택안함</option>
                                <c:forEach var="e" items="${contexts}">
                                    <option value="${e.key}" ${entity != null && entity.typeData != null && entity.typeData.split('[|]')[0] == e.key ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="row">
                <c:set var="requireSoundMenus" value="${[CONNECT_MENU,
                                                            CONNECT_REPRESENTABLE_NUMBER,
                                                            CONNECT_MENU_AFTER_DONE_EXCEPTION,
                                                            CONNECT_INNER_NUMBER_DIRECTLY]}"/>
                <div class="four wide column">
                    <label class="control-label">음원선택${requireSoundMenus.contains(form.type) ? '(*)' : ''}</label>
                    <button type="button" class="ui icon button mini compact -sound-download -play-trigger" data-sound-input="[name=soundCode]" data-tts-input="[name=ttsData]">
                        <i class="arrow down icon"></i>
                    </button>
                    <button type="button" class="ui icon button mini compact -sound-play -play-trigger" data-sound-input="[name=soundCode]" data-tts-input="[name=ttsData]">
                        <i class="volume up icon"></i>
                    </button>
                    <div class="ui popup top left"></div>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="soundCode">
                            <c:if test="${!requireSoundMenus.contains(form.type)}">
                                <form:option value="" label="선택안함"/>
                            </c:if>
                            <form:option value="TTS" label="TTS입력"/>
                            <c:forEach var="e" items="${sounds}">
                                <form:option value="${e.seq}" label="${e.soundName}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row -tts-data">
                <div class="four wide column"><label class="control-label">TTS입력</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <textarea name="ttsData">${entity != null && entity.ttsData != null ? g.htmlQuote(entity.ttsData.split('[|]')[entity.introSoundCode == 'TTS' ? 1 : 0]) : ''}</textarea>
                    </div>
                </div>
            </div>
            <c:if test="${[CONNECT_REPRESENTABLE_NUMBER,
                            CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">대표번호선택(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select name="typeDataStrings" data-multiple="multiple" class="wantsort">
                                <option value="">선택안함</option>
                                <c:forEach var="e" items="${serviceNumbers}">
                                    <option value="${e.key}" ${entity != null && entity.typeData != null && entity.typeData.split('[|]')[form.type == CONNECT_REPRESENTABLE_NUMBER ? 0 : 1] == e.key ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_HUNT_NUMBER,
                            CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">수신그룹번호선택(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select name="typeDataStrings" data-multiple="multiple">
                                <option value="" label="선택안함"></option>
                                <c:forEach var="e" items="${queues}">
                                    <option value="${e.number}" ${entity != null && entity.typeData != null && entity.typeData.split('[|]')[form.type == CONNECT_HUNT_NUMBER ? 0 : 1] == e.number ? 'selected' : ''}>${g.htmlQuote(e.hanName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_INNER_NUMBER].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">내선번호선택(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select name="typeDataStrings" data-multiple="multiple" class="wantsort">
                                <option value="" label="선택안함"></option>
                                <c:forEach var="e" items="${extensions}">
                                    <option value="${e.key}" ${entity != null && entity.typeData != null && entity.typeData.split('[|]')[0] == e.key ? 'selected' : ''}>${g.htmlQuote(e.value)}[${e.key}]</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_OUTER_NUMBER].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">연결될외부번호(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid">
                            <input type="text" name="typeDataStrings" data-multiple="multiple"
                                   value="${entity != null && entity.typeData != null ? g.htmlQuote(entity.typeData.split('[|]')[0]) : ''}"/>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_MENU,
                            CONNECT_MENU_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <c:if test="${serviceKind.equals('SC') && usingServices.contains('WV')}">
                    <div class="row">
                        <div class="four wide column"><label class="control-label">보이는ARS사용</label></div>
                        <div class="twelve wide column">
                            <div class="ui form">
                                <form:select path="isWebVoice">
                                   <form:options items="${webVoiceYn}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="row dtmf-container">
                    <c:choose>
                        <c:when test="${entity != null && entity.nodes != null && entity.nodes.size() > 0}">
                            <c:forEach var="e" items="${entity.nodes}" varStatus="status">
                                <div class="seven wide column">
                                    <div class="ui action right labeled input mini fluid">
                                        <input type="hidden" class="-button-sequence" value="${e.seq}"/>
                                        <input type="text" class="-button-number" placeholder="번호" style="flex: 0.3 0 auto;" value="${g.htmlQuote(e.button)}"/>
                                        <input type="text" class="-button-menu-name" placeholder="메뉴명입력" value="${g.htmlQuote(e.name)}"/>
                                        <button type="button" class="ui icon button ${status.first ? '-add-button' : '-remove-button'}"><i class="${status.first ? 'plus' : 'close'} icon"></i></button>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="seven wide column">
                                <div class="ui action right labeled input mini fluid">
                                    <input type="hidden" class="-button-sequence" value=""/>
                                    <input type="text" class="-button-number" placeholder="번호" style="flex: 0.3 0 auto;"/>
                                    <input type="text" class="-button-menu-name" placeholder="메뉴명입력"/>
                                    <button type="button" class="ui icon button -add-button"><i class="plus icon"></i></button>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        adjustToForm(data)
    };

    function adjustToForm(data) {
        data.ttsDataStrings = [];
        if (data.introSoundCode === 'TTS') data.ttsDataStrings.push(data.introTtsData);
        if (data.soundCode === 'TTS') data.ttsDataStrings.push(data.ttsData);
        if (data.typeDataStrings && data.typeDataStrings.length > 2 && data.typeDataStrings[data.typeDataStrings.length - 2] === 'TTS') data.ttsDataStrings.push(data.retryTtsData);

        data.buttons = [];
        const buttonSequence = $('.-button-sequence');
        const buttonNumbers = $('.-button-number');
        const buttonMenuNames = $('.-button-menu-name');
        for (let i = 0; i < buttonNumbers.length; i++) {
            const seq = $(buttonSequence[i]).val();
            const number = $(buttonNumbers[i]).val();
            const name = $(buttonMenuNames[i]).val();

            if (!number || !name) continue;
            data.buttons.push({seq: seq, button: number, name: name});
        }

        delete data.introTtsData;
        delete data.ttsData;
        delete data.retryTtsData;

        return data;
    }

    modal.find('[name=parentSeq]').change(function () {
        const parentSeq = modal.find('[name=parentSeq]').val();
        console.log(parentSeq);

        if (parentSeq === 1)
            alert('최대 2레벨까지 생성 가능합니다.');
    }).change();

    modal.find('.-play-trigger').each(function () {
        $(this).popup({
            popup: $($(this).attr('data-target') || $(this).next()),
            on: 'click'
        });
    });

    modal.find('.-sound-download').click(function (event) {
        event.stopPropagation();

        const sound = modal.find($(this).attr('data-sound-input')).val();
        const tts = modal.find($(this).attr('data-tts-input')).val();

        if (!sound)
            return;

        if (sound !== 'TTS') {
            const src = "${g.escapeQuote(apiServerUrl)}/api/v1/admin/sounds/ars/" + sound + "/resource?token=${g.escapeQuote(accessToken)}";
            $('<a/>', {href: src, target: '_blank', style: 'display: none;'}).appendTo('body').click();
        } else if (tts) {
            restSelf.post('/api/sounds-editor/pre-listen', {playSpeed: 100, comment: tts}).done(function (response) {
                const src = $.addQueryString('${g.escapeQuote(apiServerUrl)}' + response.data, {token: '${g.escapeQuote(accessToken)}', ___t: new Date().getTime()});
                $('<a/>', {href: src, target: '_blank', style: 'display: none;'}).appendTo('body').click();
            });
        }
    });

    modal.find('.-sound-play').click(function (event) {
        event.stopPropagation();

        const sound = modal.find($(this).attr('data-sound-input')).val();
        const tts = modal.find($(this).attr('data-tts-input')).val();
        const player = $(this).next().empty();

        if (player.hasClass('out'))
            return;

        if (!sound)
            return;

        if (sound !== 'TTS') {
            const src = "${g.escapeQuote(apiServerUrl)}/api/v1/admin/sounds/ars/" + sound + "/resource?token=${g.escapeQuote(accessToken)}";
            const audio = $('<audio controls/>').attr('src', src);
            player.append(audio);
            maudio({obj: audio[0], fastStep: 10});
        } else if (tts) {
            restSelf.post('/api/sounds-editor/pre-listen', {playSpeed: 100, comment: tts}).done(function (response) {
                const src = $.addQueryString('${g.escapeQuote(apiServerUrl)}' + response.data, {token: '${g.escapeQuote(accessToken)}', ___t: new Date().getTime()});
                const audio = $('<audio controls/>').attr('src', src);
                player.append(audio);
                maudio({obj: 'audio', fastStep: 10});
            });
        }
    });

    const buttonContainer = $('.dtmf-container');

    modal.find('.-add-button').click(function () {
        buttonContainer.append(
            '<div class="seven wide column">' +
            '    <div class="ui action right labeled input mini fluid">' +
            '        <input type="text" class="-button-number" placeholder="번호" style="flex: 0.3 0 auto;"/>' +
            '        <input type="text" class="-button-menu-name" placeholder="메뉴명입력"/>' +
            '        <button type="button" class="ui icon button -remove-button"><i class="close icon"></i></button>' +
            '    </div>' +
            '</div>'
        );
    });

    modal.on('click', '.-remove-button', function (event) {
        $(event.target).closest('.ui.action').parent().remove();
    });

    modal.find('[name=introSoundCode]').change(function () {
        if ($(this).val() !== 'TTS') modal.find('.-intro-tts-data').hide();
        else modal.find('.-intro-tts-data').show();
    }).change();

    modal.find('[name=soundCode]').change(function () {
        if ($(this).val() !== 'TTS') modal.find('.-tts-data').hide();
        else modal.find('.-tts-data').show();
    }).change();

    modal.find('.-retry-sound').change(function () {
        if ($(this).val() !== 'TTS') modal.find('.-retry-tts-data').hide();
        else modal.find('.-retry-tts-data').show();
    }).change();

    window.doneWriteFormData = function (form, response) {
        function loadWidget() {
            restSelf.get('/api/ivr/' + ${entity != null ? entity.seq : 'response.data'}).done(function (response) {
                addOrUpdateWidget(response.data);
                modal.modalHide();
                editor.ivrEditor('render');
            });
        }

        $(modal).asJsonData().done(function (data) {
            adjustToForm(data);

            if (data.parentSeq) {
                restSelf.get('/api/ivr/' + data.parentSeq).done(function (response) {
                    addOrUpdateWidget(response.data, true);
                    loadWidget();
                });
            } else {
                loadWidget();
            }
        });

        loadRootIvrTree(${form.rootSeq != null ? form.rootSeq : 'response.data'});
    };

/*    function sortSelect(wantsort) {
        var sel = $('#wantsort'); var optionList = sel.find('option');
        optionList.sort(function(a, b){
            if (a.text > b.text) return 1;
            else if (a.text < b.text) return -1; else { if (a.value > b.value) return 1;
                else if (a.value < b.value) return -1; else return 0; } });
        sel.html(optionList); }

    $(document).ready(function(){ sortSelect('wantsort'); });*/
    var options = $(".wantsort option");                    // Collect options
    options.detach().sort(function(a,b) {               // Detach from select, then Sort
        var at = $(a).text();
        var bt = $(b).text();
        return (at > bt)?1:((at < bt)?-1:0);            // Tell the sort function how to order
    });
    options.appendTo(".wantsort");

    var options = $(".wantsort2 option");                    // Collect options
    options.detach().sort(function(a,b) {               // Detach from select, then Sort
        var at = $(a).text();
        var bt = $(b).text();
        return (at > bt)?1:((at < bt)?-1:0);            // Tell the sort function how to order
    });
    options.appendTo(".wantsort2");
</script>
