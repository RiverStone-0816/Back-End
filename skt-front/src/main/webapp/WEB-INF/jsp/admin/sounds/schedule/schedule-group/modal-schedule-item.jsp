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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/schedule-group/item/${entity == null ? null : entity.child}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">스케쥴[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <form:hidden path="parent"/>
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">시간</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select class="input-small-size time" name="fromHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.fromhour / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="fromMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.fromhour % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <span class="tilde">~</span>
                        <select class="input-small-size time" name="toHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.tohour / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="toMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.tohour % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">스케쥴유형선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="S" class="hidden"/>
                                    <label>음원만플레이</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="D" class="hidden"/>
                                    <label>번호직접연결(내부번호연결)</label>
                                </div>
                            </div>
                        </div>
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="F" class="hidden"/>
                                    <label>착신전환(외부번호연결)</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="I" class="hidden"/>
                                    <label>IVR연결</label>
                                </div>
                            </div>
                        </div>
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="C" class="hidden"/>
                                    <label>예외컨텍스트</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="V" class="hidden"/>
                                    <label>음성사서함</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="S,D,F,C,V">
                <div class="four wide column"><label class="control-label">음원선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="kindSoundCode">
                            <form:option value="" label="선택안함"/>
                            <form:option value="TTS" label="TTS입력"/>
                            <c:forEach var="e" items="${soundList}">
                                <form:option value="${g.htmlQuote(e.seq)}" label="${g.htmlQuote(e.soundName)}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row -tts-data">
                <div class="four wide column"><label class="control-label">TTS 입력</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:textarea path="ttsData"/>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="D">
                <div class="four wide column"><label class="control-label">내부번호</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select name="kindDataD">
                            <option value="">직접연결번호선택</option>
                            <c:forEach var="e" items="${number070List}">
                                <option value="${g.htmlQuote(e.number)}" ${entity.kind == 'D' && entity.kindData == e.number ? 'selected' : null}>${g.htmlQuote(e.number)}(${g.messageOf("NumberType", e.type)})</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="F">
                <div class="four wide column"><label class="control-label">외부번호</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <input type="text" name="kindDataF" value="${g.htmlQuote(entity.kind == 'F' ? entity.kindData : '')}"/>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="I">
                <div class="four wide column"><label class="control-label">IVR</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select name="kindDataI">
                            <c:forEach var="e" items="${ivrTreeList}">
                                <option value="${g.htmlQuote(e.code)}" ${entity.kind == 'I' && entity.kindData == "".concat(e.code) ? 'selected' : null}>${g.htmlQuote(e.name)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="C">
                <div class="four wide column"><label class="control-label">컨텍스트</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select name="kindDataC">
                            <c:forEach var="e" items="${contextList}">
                                <option value="${g.htmlQuote(e.context)}" ${entity.kind == 'C' && entity.kindData == e.context ? 'selected' : null}>${g.htmlQuote(e.name)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">통계적용여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="statYn" value="Y" class="hidden"/>
                                    <label>적용</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="statYn" value="N" class="hidden"/>
                                    <label>비적용</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">업무시간사용여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="worktimeYn" value="Y" class="hidden"/>
                                    <label>적용</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="worktimeYn" value="N" class="hidden"/>
                                    <label>비적용</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.fromhour = parseInt(data.fromHour) * 60 + parseInt(data.fromMinute);
        data.tohour = parseInt(data.toHour) * 60 + parseInt(data.toMinute);

        delete data.fromHour;
        delete data.fromMinute;
        delete data.toHour;
        delete data.toMinute;

        if (data.kind === 'D') data.kindData = data.kindDataD;
        if (data.kind === 'F') data.kindData = data.kindDataF;
        if (data.kind === 'I') data.kindData = data.kindDataI;
        if (data.kind === 'C') data.kindData = data.kindDataC;

        delete data.kindDataD;
        delete data.kindDataF;
        delete data.kindDataI;
        delete data.kindDataC;

        if (data.kind === 'I') {
            delete data.kindSoundData;
            delete data.ttsData;
        }

        if (data.kindSoundData)
            delete data.ttsData;
        else
            delete data.kindSoundData;
    };

    modal.find('[name=kindSoundCode]').change(function () {
        if ($(this).val() !== 'TTS')
            modal.find('.-tts-data').hide();
        else
            modal.find('.-tts-data').show();
    }).change();

    modal.find('[name=kind]').change(function () {
        const kind = modal.find('[name=kind]:checked').val();
        modal.find('.-kind-data').hide().filter(function () {
            return $(this).attr('data-kind').split(',').indexOf(kind) >= 0;
        }).show();

        if (kind === 'I') {
            modal.find('.-tts-data').hide();
            modal.find('[name=kindSoundCode]').val('').prop("selected", true);
            modal.find('[name=ttsData]').val('');
        } else
            modal.find('[name=kindSoundCode]').change();
    }).change();
</script>
