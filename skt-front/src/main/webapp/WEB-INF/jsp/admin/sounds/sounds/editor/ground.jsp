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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/sounds/sounds/editor/"/>
        <form:form id="make-form" modelAttribute="form" cssClass="sub-content ui container fluid">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/sounds/sounds/editor/"))}</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="ui basic buttons">
                            <button class="ui button" type="button" onclick="saveOnSound()">음원으로저장</button>
                            <button class="ui button" type="button" onclick="saveOnColoring()">컬러링으로저장</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable border-top-default">
                        <tr>
                            <th>재생속도</th>
                            <td colspan="3">
                                <div class="ui form">
                                    <form:select path="playSpeed">
                                        <form:option value="100">정상</form:option>
                                        <form:option value="70">느리게</form:option>
                                        <form:option value="40">많이느리게</form:option>
                                        <form:option value="130">빠르게</form:option>
                                        <form:option value="180">많이빠르게</form:option>
                                    </form:select>
                                </div>
                            </td>
                            <th>음원명(한글)</th>
                            <td colspan="3">
                                <div class="ui form"><form:input path="soundName"/></div>
                            </td>
                            <th>파일명(한글)</th>
                            <td colspan="3">
                                <div class="ui form"><form:input path="fileName"/></div>
                            </td>
                        </tr>
                        <tr>
                            <th>음원입력</th>
                            <td colspan="10">
                                <div class="ui form">
                                    <div class="field">
                                        <form:textarea path="comment" placeholder="사용하실 음원을 입력해주세요."/>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>미리듣기</th>
                            <td colspan="10">
                                <div class="ui form align-left">
                                    <button type="button" class="ui button mini compact" style="margin-top: 2px" onclick="playPreListen()">미리듣기</button>
                                    <button type="button" style="display: none;" id="trigger-popup-player"></button>
                                </div>
                            </td>
                        </tr>
                    </table>
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
