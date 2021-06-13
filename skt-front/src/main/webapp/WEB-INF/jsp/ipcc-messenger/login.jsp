<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <div id="wrap" class="login ui middle aligned center grid">
        <div class="login-form column">
            <div class="ui header">
                <h3><span>IPCC 메신저</span> 로그인</h3>
            </div>
            <div class="ui segment very padded column login-form-inner">
                <div class="logo-wrap">
                    <h1><a href="javascript:" title="메인"></a></h1>
                </div>
                <form:form id="login-form" modelAttribute="form" cssClass="login-box ui form">
                    <form:hidden path="isChatLogin" value="true"/>
                    <ol>
                        <li>
                            <div class="field">
                                <label>회사아이디</label>
                                <form:input autocomplete="false" path="company" placeholder="회사 아이디" cssClass="form-control"/>
                            </div>
                        </li>
                        <li>
                            <div class="field">
                                <label>아이디</label>
                                <form:input autocomplete="false" path="id" placeholder="아이디" cssClass="form-control"/>
                            </div>
                        </li>
                        <li>
                            <div class="field">
                                <label>비밀번호</label>
                                <form:password autocomplete="false" path="password" placeholder="비밀번호" cssClass="form-control"/>
                            </div>
                        </li>
                        <li>
                            <div class="field">
                                <label>내선번호</label>
                                <form:input autocomplete="false" path="extension" placeholder="내선번호" cssClass="form-control"/>
                            </div>
                        </li>
                        <li class="chk-box-wrap">
                            <div class="ui toggle checkbox">
                                <input type="checkbox" id="remember">
                                <label for="remember">로그인저장</label>
                            </div>
                        </li>
                        <li>
                            <button type="button" class="ui button pink fluid -login-submit">LOGIN</button>
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

    <div class="ui modal mini" id="modal-login">
        <div class="content">
            <div class="login-info">
                <label class="info-title">보안정보</label>
                <ul class="ui list">
                    <li>· 아이디 : <label id="login-id"></label></li>
                    <li>· 현재접근 IP : ${g.clientIp()}</li>
                    <li>· 최종로그인 시간 : <label id="last-login-time"></label></li>
                    <li>· 최종로그인 IP : <label id="last-login-ip"></label></li>
                    <li>· <label id="warring-text"></label></li>
                </ul>
            </div>
            <div class="change-password" id="password-update-form">
                <div class="row">
                    <div class="four wide column"><label class="control-label">새로운 비밀번호</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid"><input type="password" name="password" placeholder="문자/숫자/특수문자 9~20자"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">비밀번호 확인</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid"><input type="password" name="passwordConfirm"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <div class="ui positive right labeled icon button check-login" id="confirm-login">
                확인
                <i class="checkmark icon"></i>
            </div>
            <button type="button" id="password-change" class="ui blue button change-password">
                변경
            </button>
            <button type="button" id="cancel-login" class="ui button modal-close change-password">
                취소
            </button>
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

                return restSelf.get("/api/auth/confirm-login", null).done(function () {
                    location.href = contextPath + '/ipcc-messenger/main';
                });

                restSelf.get('/api/web-log/' + $('#id').val() + "/last-login").done(function (data) {

                    const loginInfo = data.data;

                    loginModal.find('#login-id').text(loginInfo.userId);
                    loginModal.find('#last-login-time').text(loginInfo.insertDate != null ? timestampToDateTime(loginInfo.insertDate) : '');
                    loginModal.find('#last-login-ip').text(loginInfo.secureIp);
                    loginModal.find('#warring-text').text(loginInfo.changePasswordDays);

                    if (loginInfo.passwordChangeDate == null) {
                        loginModal.find('#warring-text').text('비밀번호를 변경해 주세요.');
                        loginModal.find('.check-login').remove();
                        loginModal.find('#warring-text').parent().addClass('warning');
                    } else if (loginInfo.changePasswordDays >= 90) {
                        loginModal.find('#warring-text').text('패스워드 변경 후 ' + loginInfo.changePasswordDays + '일 초과');
                        loginModal.find('.check-login').remove();
                        loginModal.find('#warring-text').parent().addClass('warning');
                    } else {
                        loginModal.find('#warring-text').text('패스워드 변경 후 ' + loginInfo.changePasswordDays + '일 초과');
                        loginModal.find('.change-password').remove();
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
                                    location.href = contextPath + '/';
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
                    location.href = contextPath + '/';
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
        </script>
    </tags:scripts>
</mobileTags:layout>
