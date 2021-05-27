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
        <tags:page-menu-tab url="/admin/sounds/sounds/editor/"/>
        <form:form id="make-form" modelAttribute="form" cssClass="sub-content ui container fluid">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">음원저장(저장후 음원관리 메뉴에서 관리 가능)</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" onclick="saveOnSound()" class="ui basic button">음원으로저장</button>
                        <button type="button" onclick="saveOnColoring()" class="ui basic button">컬러링으로저장</button>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="rows">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">재생속도</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="playSpeed">
                                            <form:option value="100">정상</form:option>
                                            <form:option value="70">느리게</form:option>
                                            <form:option value="40">많이느리게</form:option>
                                            <form:option value="130">빠르게</form:option>
                                            <form:option value="180">많이빠르게</form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column">
                                    <div class="ui popup top right" id="audio-player"></div>
                                </div>
                                <div class="two wide column"><label class="control-label">음원명(한글)</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="soundName"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">파일명(영문)</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="fileName"/></div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="sixteen wide column">
                                    <div class="ui fluid form">
                                        <div class="field">
                                            <form:textarea path="comment" placeholder="사용하실 음원을 입력해주세요."/>
                                        </div>
                                    </div>
                                    <div class="ui fluid form">
                                    <button type="button" class="ui button mini compact" style="margin-top: 2px" onclick="playPreListen()">미리듣기</button>
                                    <button type="button" style="display: none;" id="trigger-popup-player"></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>

    <tags:scripts>
        <script>
            const form = $('#make-form');

            function save(soundType, callback) {
                form.asJsonData().done(function (data) {
                    restSelf.post('/api/sounds-editor/make?soundType=' + encodeURIComponent(soundType), data).done(function (response) {
                        alert('저장되었습니다.');

                        return callback.apply(null, [response]);
                    });
                });
            }

            function saveOnColoring() {
                save('moh');
            }

            function saveOnSound() {
                save('sound', reload());
            }

            $('#trigger-popup-player').popup({
                popup: '#audio-player',
                on: 'click'
            });

            function playPreListen() {
                const player = $('#audio-player').empty();

                form.asJsonData().done(function (data) {
                    if (!data.playSpeed) return alert('재생속도를 선택하세요.');
                    if (!data.comment) return alert('음원을 입력하세요.');

                    restSelf.post('/api/sounds-editor/pre-listen', {playSpeed: data.playSpeed, comment: data.comment}).done(function (response) {
                        const src = $.addQueryString('${g.escapeQuote(apiServerUrl)}' + response.data, {token: '${g.escapeQuote(accessToken)}', ___t: new Date().getTime()});
                        const audio = $('<audio controls/>').attr('src', src);
                        player.append(audio);
                        maudio({obj: 'audio', fastStep: 10});

                        $('#trigger-popup-player').click();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
