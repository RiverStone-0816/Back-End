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

<form:form modelAttribute="form" cssClass="ui modal small">

    <i class="close icon"></i>
    <div class="header">${typeName}</div>

    <div class="content rows scrolling">
        <div class="ui centered grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">메뉴명(*)</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">${g.htmlQuote(form.name)}</div>
                </div>
            </div>
            <c:if test="${(entity == null && form.parentSeq == null) || (entity != null && entity.treeName.split('[_]')[1] == null)}">
                <c:if test="${form.introSoundCode != null && form.introSoundCode != 'TTS'}">
                    <div class="row">
                        <div class="four wide column">
                            <label class="control-label">인트로음원</label>
                            <button type="button" class="ui icon button mini compact -sound-download -play-trigger" data-sound-input=".-input[data-name=introSoundCode]"
                                    data-tts-input=".-input[data-name=introTtsData]">
                                <i class="arrow down icon"></i>
                            </button>
                            <button type="button" class="ui icon button mini compact -sound-play -play-trigger" data-sound-input=".-input[data-name=introSoundCode]"
                                    data-tts-input=".-input[data-name=introTtsData]">
                                <i class="volume up icon"></i>
                            </button>
                            <div class="ui popup top left"></div>
                        </div>
                        <div class="twelve wide column">
                            <div class="ui form">
                                <c:forEach var="e" items="${sounds}">
                                    <c:if test="${e.key == form.introSoundCode}">
                                        <text class="-input" data-name="introSoundCode" data-value="${g.escapeQuote(e.key)}">${g.htmlQuote(e.value)}</text>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${form.introSoundCode == 'TTS'}">
                    <div class="row -intro-tts-data">
                        <div class="four wide column">
                            <label class="control-label">인트로음원(TTS)</label>
                            <button type="button" class="ui icon button mini compact -sound-download -play-trigger" data-sound-input=".-input[data-name=introSoundCode]"
                                    data-tts-input=".-input[data-name=introTtsData]">
                                <i class="arrow down icon"></i>
                            </button>
                            <button type="button" class="ui icon button mini compact -sound-play -play-trigger" data-sound-input=".-input[data-name=introSoundCode]"
                                    data-tts-input=".-input[data-name=introTtsData]">
                                <i class="volume up icon"></i>
                            </button>
                            <div class="ui popup top left"></div>
                        </div>
                        <div class="twelve wide column">
                            <div class="ui form">
                                <text class="-input" data-name="introSoundCode" style="display: none" data-value="TTS"></text>
                                <text class="-input" data-name="introTtsData">${entity != null && entity.ttsData != null ? g.htmlQuote(entity.ttsData.split('[|]')[0]) : ''}</text>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:if>
            <c:if test="${[FINISH_AFTER_DONE_EXCEPTION,
                            CONNECT_MENU_AFTER_DONE_EXCEPTION,
                            CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION,
                            CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">예외컨텍스트(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <c:forEach var="e" items="${contexts}">
                                <c:if test="${entity != null && entity.typeData != null && entity.typeData.split('[|]')[0] == e.key}">
                                    ${g.htmlQuote(e.value)}
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${form.soundCode != null && form.soundCode != 'TTS'}">
                <div class="row">
                    <c:set var="requireSoundMenus" value="${[CONNECT_MENU,
                                                            CONNECT_REPRESENTABLE_NUMBER,
                                                            CONNECT_MENU_AFTER_DONE_EXCEPTION,
                                                            CONNECT_INNER_NUMBER_DIRECTLY]}"/>
                    <div class="four wide column">
                        <label class="control-label">음원${requireSoundMenus.contains(form.type) ? '(*)' : ''}</label>
                        <button type="button" class="ui icon button mini compact -sound-download -play-trigger" data-sound-input=".-input[data-name=soundCode]"
                                data-tts-input=".-input[data-name=ttsData]">
                            <i class="arrow down icon"></i>
                        </button>
                        <button type="button" class="ui icon button mini compact -sound-play -play-trigger" data-sound-input=".-input[data-name=soundCode]" data-tts-input=".-input[data-name=ttsData]">
                            <i class="volume up icon"></i>
                        </button>
                        <div class="ui popup top left"></div>
                    </div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <c:forEach var="e" items="${sounds}">
                                <c:if test="${e.key == form.soundCode}">
                                    <text class="-input" data-name="soundCode" data-value="${g.escapeQuote(e.key)}">${g.htmlQuote(e.value)}</text>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${form.soundCode == 'TTS'}">
                <div class="row -tts-data">
                    <div class="four wide column">
                        <label class="control-label">음원(TTS)</label>
                        <button type="button" class="ui icon button mini compact -sound-download -play-trigger" data-sound-input=".-input[data-name=soundCode]"
                                data-tts-input=".-input[data-name=ttsData]">
                            <i class="arrow down icon"></i>
                        </button>
                        <button type="button" class="ui icon button mini compact -sound-play -play-trigger" data-sound-input=".-input[data-name=soundCode]" data-tts-input=".-input[data-name=ttsData]">
                            <i class="volume up icon"></i>
                        </button>
                        <div class="ui popup top left"></div>
                    </div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <text class="-input" data-name="soundCode" style="display: none" data-value="TTS"></text>
                            <text class="-input" data-name="ttsData">
                                    ${entity != null && entity.ttsData != null ? g.htmlQuote(entity.ttsData.split('[|]')[entity.introSoundCode == 'TTS' ? 1 : 0]) : ''}
                            </text>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_REPRESENTABLE_NUMBER,
                            CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">대표번호(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <c:forEach var="e" items="${serviceNumbers}">
                                <c:if test="${entity != null && entity.typeData != null && entity.typeData.split('[|]')[form.type == CONNECT_REPRESENTABLE_NUMBER ? 0 : 1] == e.key}">
                                    ${g.htmlQuote(e.value)}
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_HUNT_NUMBER,
                            CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">큐번호(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <c:forEach var="e" items="${queues}">
                                <c:if test="${entity != null && entity.typeData != null && entity.typeData.split('[|]')[form.type == CONNECT_HUNT_NUMBER ? 0 : 1] == e.key}">
                                    ${g.htmlQuote(e.value)}
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_INNER_NUMBER].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">내선번호(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <c:forEach var="e" items="${extensions}">
                                <c:if test="${entity != null && entity.typeData != null && entity.typeData.split('[|]')[0] == e.key}">
                                    ${g.htmlQuote(e.value)}[${e.key}]
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${[CONNECT_OUTER_NUMBER].contains(form.type)}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">연결될외부번호(*)</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid">
                                ${entity != null && entity.typeData != null ? g.htmlQuote(entity.typeData.split('[|]')[0]) : ''}
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
                                <c:forEach var="e" items="${webVoiceYn}">
                                    <c:if test="${e.key == entity.isWebVoice}">
                                        ${g.htmlQuote(e.value)}
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="row dtmf-container">
                    <c:if test="${entity != null && entity.nodes != null && entity.nodes.size() > 0}">
                        <c:forEach var="e" items="${entity.nodes}" varStatus="status">
                            <div class="seven wide column">
                                <div class="ui action right labeled input mini fluid">
                                    <div style="flex: 0.3 0 auto;">[${g.htmlQuote(e.button)}] : ${g.htmlQuote(e.name)}</div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
            </c:if>
        </div>
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

        const sound = modal.find($(this).attr('data-sound-input')).val() || modal.find($(this).attr('data-sound-input')).attr('data-value');
        const tts = modal.find($(this).attr('data-tts-input')).val() || modal.find($(this).attr('data-tts-input')).attr('data-value');

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
            '        <input type="text" class="-button-menu-name" placeholder="메뉴명"/>' +
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
</script>
