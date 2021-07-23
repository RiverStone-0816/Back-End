<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<tags:scripts/>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=440, initial-scale=0.8"/>
    <tags:favicon/>
    <tags:css/>
</head>
<body>
<div id="wrap" class="login <%--theme2--%>"> <%--변경된 테마 적용방법: #wrap에 theme2 클래스 추가--%>
    <div class="login-form">
        <div class="ui header">
            <p>최고의 고객상담을 실천하는</p>
            <h3>SK telecom</h3>
        </div>
        <div class="column login-form-inner">
            <form:form id="login-form" modelAttribute="form" cssClass="login-box">
                <form:hidden path="isChatLogin" value="false"/>
                <ol>
                    <li>
                        <div class="field">
                            <form:input autocomplete="false" path="company" placeholder="회사 아이디"  cssClass="form-control"/>
                        </div>
                    </li>
                    <li>
                        <div class="field">
                            <form:input autocomplete="false" path="id" placeholder="아이디" cssClass="form-control"/></div>
                    </li>
                    <li>
                        <div class="field">
                            <form:password autocomplete="false" path="password" placeholder="비밀번호"  cssClass="form-control"/></div>
                    </li>
                    <li>
                        <div class="field">
                            <form:input autocomplete="false" path="extension" placeholder="내선인증번호"  cssClass="form-control"/></div>
                    </li>
                    <li class="chk-box-wrap">
                        <div class="ccheck">
                            <input type="checkbox" id="remember">
                            <label for="remember">
                                <text class="chk_img">로그인 상태 유지</text>
                            </label>
                        </div>
                    </li>
                    <li>
                        <button type="button" class="ui fluid button -login-submit">LOGIN</button>
                    </li>
                </ol>
            </form:form>
            <div id="ars-auth-form" class="login-box ui form" style="display: none;">
                <ol>
                    <li>
                        <div class="field">
                            <label>전화번호</label>
                            <input type="hidden" readonly id="ars-session-id"/>
                            <input type="text" readonly id="ars-phone"/>
                        </div>
                    </li>
                    <li>
                        <div class="field">
                            <label>인증번호</label>
                            <input type="text" readonly id="ars-auth-num"/>
                        </div>
                    </li>
                    <li>
                        <div class="field">
                            <label>진행상태</label>
                            <input type="text" readonly id="ars-status"/>
                        </div>
                    </li>
                    <li>
                        <button type="button" class="ui basic button brand -try-ars-auth">인증시도하기</button>
                    </li>
                    <input type="hidden" id="pbxHost"/>
                </ol>
            </div>
        </div>
    </div>
</div>

<div class="ui modal" id="modal-login">
    <div class="content">
        <div class="login-info">
            <label class="info-title">보안정보</label>
            <table class="ui table celled compact unstackable">
                <tr>
                    <th>아이디</th>
                    <td><label id="login-id"></label></td>
                </tr>
                <tr>
                    <th>현재접근 IP</th>
                    <td>${g.clientIp()}</td>
                </tr>
                <tr>
                    <th>최종로그인 시간</th>
                    <td><label id="last-login-time"></td>
                </tr>
                <tr>
                    <th>최종로그인 IP</th>
                    <td><label id="last-login-ip"></label></td>
                </tr>
                <tr>
                    <td colspan="2"><label id="warring-text"></label></td>
                </tr>
            </table>

            <table class="ui table celled compact unstackable mt30" id="password-update-form">
                <tr>
                    <th>새로운 비밀번호</th>
                    <td>
                        <div class="ui form">
                            <input type="password" name="password" placeholder="문자/숫자/특수문자 9~20자">
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>비밀번호 확인</th>
                    <td>
                        <div class="ui form">
                            <input type="password" name="passwordConfirm">
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="actions">
        <button type="submit" class="ui orange button" id="confirm-login">확인</button>
        <button type="button" class="ui grey button" id="cancel-login">취소</button>
        <button type="button" class="ui orange button" id="password-change">변경</button>
    </div>
</div>

<tags:scripts>
    <script>
        const STORAGE_KEY = 'loginForm';
        const form = $('#login-form');
        const remember = $('#remember');
        const loginModal = $('#modal-login');

        const ipccAdminCommunicator = new IpccAdminCommunicator()
            .on('ARS_AUTH_RES', function (message, kind, peer, data1, data2) {
                if (kind === 'CALLREQ_OK') {
                    $('#ars-status').val('인증시도');
                } else if (kind === 'CALLREQ_NOK') {
                    $('#ars-status').val('인증시도실패: ' + data2);
                } else if (kind === 'HANGUP') {
                    $('#ars-status').val('HANGUP');
                } else if (kind === 'DIALUP') {
                    $('#ars-status').val('전화연결');
                } else if (kind === 'SUCC') {
                    $('#login-form').asJsonData().done(function (data) {
                        restSelf.post('/api/auth/login', data).done(function (response) {
                            after();
                        });
                    });
                } else if (kind === 'FAIL') {
                    $('#ars-status').val('인증실패');
                }
            });

        $('.-try-ars-auth').click(function () {
            $('#login-form').asJsonData().done(function (data) {
                ipccAdminCommunicator.send({
                    company_id: data.company,
                    userid: data.id,
                    target_pbx: $('#pbxHost').val(),
                    command: 'CMD|ARS_AUTH|' + data.company + ',' + data.id + ',' + $('#ars-phone').val() + ',' + $('#ars-auth-num').val() + ',' + $('#ars-session-id').val()
                });
            });
        });

        $('.-login-submit').click(function () {
            $('#login-form').asJsonData().done(function (data) {
                restSelf.post('/api/auth/check-login-condition', data, function (response) {
                    console.log(arguments);

                    if (response.status !== 401)
                        return failFunction().apply(window, [response]);

                    const responseData = JSON.parse(response.responseText).data;

                    $('#ars-phone').val(responseData.number);
                    $('#ars-auth-num').val(responseData.authNum);
                    $('#ars-session-id').val(responseData.sessionId);
                    $('#pbxHost').val(responseData.pbxHost)

                    form.hide();
                    $('#ars-auth-form').show();

                    ipccAdminCommunicator.connect("${g.escapeQuote(adminSocketUrl)}", data.company, data.id, responseData.pbxHost);
                }).done(function () {
                    after();
                });
            });
        });

        function after() {
            localStorage.setItem(STORAGE_KEY, remember.prop('checked') ? JSON.stringify(form.formDataObject()) : '');
            restSelf.get('/api/web-log/' + $('#id').val() + "/last-login").done(function (data) {

                const loginInfo = data.data;

                loginModal.find('#login-id').text(loginInfo.userId);
                loginModal.find('#last-login-time').text(loginInfo.insertDate != null ? timestampToDateTime(loginInfo.insertDate) : '');
                loginModal.find('#last-login-ip').text(loginInfo.secureIp);
                loginModal.find('#warring-text').text(loginInfo.changePasswordDays);

                if (loginInfo.passwordChangeDate == null) {
                    loginModal.find('#warring-text').text('비밀번호를 변경해 주세요.');
                    loginModal.find('#confirm-login').remove();
                    loginModal.find('#warring-text').parent().addClass('warning');
                } else if(loginInfo.changePasswordDays >= 90) {
                    loginModal.find('#warring-text').text('패스워드 변경 후 ' + loginInfo.changePasswordDays + '일 초과');
                    loginModal.find('#confirm-login').remove();
                    loginModal.find('#warring-text').parent().addClass('warning');
                } else {
                    loginModal.find('#warring-text').text('패스워드 변경 후 ' + loginInfo.changePasswordDays + '일 초과');
                    loginModal.find('#cancel-login,#password-change,#password-update-form').remove();
                    loginModal.find('#warring-text').parent().addClass('info');
                }

                loginModal.modalShow();
            });
        }

        loginModal.find('#confirm-login').click(function () {
            restSelf.get("/api/auth/confirm-login", null).done(function () {
                parent.location.href = contextPath + '/main';
            });
        });

        loginModal.find('#password-change').click(function () {
            loginModal.find('#password-update-form').asJsonData().done(function (data) {
                restSelf.get("/api/auth/confirm-login", null).done(function () {
                    restSelf.patch("/api/user/" + $('#id').val() + "/password", data).done(function () {
                        alert("변경되었습니다. 다시 로그인해 주세요", function () {
                            restSelf.get("/api/auth/logout").done(function () {
                                parent.location.href = contextPath + '/';
                            });
                        });
                    }).fail(function () {
                        restSelf.get("/api/auth/denied-login", null);
                    });
                });
            });
        });

        loginModal.find('#cancel-login').click(function () {
            restSelf.get("/api/auth/logout").done(function () {
                parent.location.href = contextPath + '/';
            });
        });

        $(document).ready(function () {
            const storedValues = localStorage.getItem(STORAGE_KEY);
            if (!storedValues) return;

            const values = JSON.parse(storedValues);
            if (!values) return;

            remember.prop('checked', true);

            const inputs = form.find('[name]');
            for (let key in values) {
                if (key !== 'isChatLogin') {
                    if (values.hasOwnProperty(key)) {
                        inputs.filter(function () {
                            return $(this).attr('name') === key;
                        }).val(values[key]);
                    }
                }
            }
        });

        form.find('input').keypress(function (e) {
            if(e.keyCode === 13)
                return $('.-login-submit').click();
        });
    </script>
</tags:scripts>

<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>
</div>

</body>
</html>
