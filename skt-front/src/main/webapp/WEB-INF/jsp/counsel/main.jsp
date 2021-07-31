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
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<div class="consult-wrapper -counsel-panel">
    <jsp:include page="/messenger"/>

    <div class="-counsel-content-panel consult-center-panel" data-type="COUNSEL" style="display: none;">
        <div class="ui top attached tabular menu">
            <button class="item active" data-tab="call-panel" onclick="viewCallPanel()">전화</button>
            <c:if test="${user.isTalk.equals('Y')}">
                <button class="item" data-tab="talk-panel" onclick="viewTalkPanel(); $(this).removeClass('highlight'); $(this).removeClass('newImg');">
                    <text>상담톡</text>
                    <div></div>
                </button>
            </c:if>
        </div>

        <jsp:include page="/counsel/call/"/>

        <c:if test="${user.isTalk.equals('Y')}">
            <jsp:include page="/counsel/talk/"/>
        </c:if>
    </div>

    <div class="-counsel-content-panel consult-right-panel" data-type="COUNSEL" style="display: none;">
        <div id="custom-input-panel" class="top-area">
            <div class="panel top remove-mb panel-resizable -vertical-resizable">
                <div id="call-custom-input-container">
                    <div id="call-custom-input"></div>
                </div>
                <c:if test="${user.isTalk.equals('Y')}">
                    <div id="talk-custom-input-container" style="display: none;">
                        <div id="talk-custom-input"></div>
                    </div>
                </c:if>
            </div>
        </div>

        <div id="counseling-input-panel" class="middle-area">
            <div class="panel remove-mb panel-resizable -vertical-resizable">
                <div id="call-counseling-input-container">
                    <div id="call-counseling-input"></div>
                </div>
                <c:if test="${user.isTalk.equals('Y')}">
                    <div id="talk-counseling-input-container" style="display: none;">
                        <div id="talk-counseling-input"></div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="flex-100 bottom-area">
            <div class="ui top attached tabular menu line light flex remove-margin">
                <button class="item active" data-tab="todo">To-Do</button>
                <button class="item" data-tab="consult-history">상담이력</button>
                <%--FIXME:필요시 다음과 같이 추가--%>
                <%--<button class="item" data-tab="etc-lookup">기타조회</button>--%>
            </div>
            <div class="ui bottom attached tab segment remove-margin overflow-overlay active" data-tab="todo">
                <jsp:include page="/counsel/todo-list"/>
            </div>
            <div class="ui bottom attached tab segment remove-margin overflow-overlay" data-tab="consult-history">
                <table class="ui celled table unstackable">
                    <thead>
                    <tr>
                        <c:if test="${serviceKind.equals('SC')}">
                            <th>채널</th>
                        </c:if>
                        <th>수/발신</th>
                        <th>상담등록시간</th>
                        <th>전화번호</th>
                        <th>상담톡아이디</th>
                        <th>상담원</th>
                        <th>자세히</th>
                    </tr>
                    </thead>
                    <tbody id="counsel-list"></tbody>
                </table>
            </div>
            <div class="ui bottom attached tab segment remove-margin" data-tab="etc-lookup">기타조회</div>
        </div>
    </div>

    <iframe class="content-inner -counsel-content-panel" data-type="NOTICE" src="<c:url value="/admin/service/help/notice/"/>" style="display: none;"></iframe>
    <iframe class="content-inner -counsel-content-panel" data-type="KNOWLEDGE" src="<c:url value="/admin/service/help/task-script/"/>" style="display: none;"></iframe>
    <iframe class="content-inner -counsel-content-panel" data-type="CALENDAR" src="<c:url value="/user-schedule/"/>" style="display: none;"></iframe>
    <div class="content-inner -counsel-content-panel" data-type="PREVIEW">
        <jsp:include page="/counsel/preview/"/>
    </div>

    <jsp:include page="/counsel/modal-calling"/>
    <jsp:include page="/counsel/modal-route-application"/>
    <jsp:include page="/counsel/modal-ars"/>
    <jsp:include page="/counsel/modal-cms"/>
    <jsp:include page="/counsel/modal-send-message"/>
</div>

<tags:scripts>
    <script id="toast-reservation-script">
        window.toastr.options = {
            "closeButton": true,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-bottom-right",
            "preventDuplicates": false,
            "onclick": function (event) {
                $('.toast .toast-close-button').focus();
            },
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "0",
            "extendedTimeOut": "0",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };

        const toastedCalling = {};
        const toastingCalling = [];

        function setReservation() {
            return restSelf.get('/api/counsel/recent-consultation-reservation', null, function () {
            }, true).done(function (response) {
                for (let i = 0; i < response.data.length; i++) {
                    toastingCalling.push(response.data[i]);
                }
            });
        }

        function toastReservation() {
            if (toastingCalling.length <= 0)
                return;

            toastingCalling.sort(function (a, b) {
                return a.time - b.time;
            });

            const now = new Date().getTime();

            for (let i = 0; i < toastingCalling.length; i++) {
                const e = toastingCalling.shift();
                if (e.time > now) {
                    toastingCalling.unshift(e);
                    return;
                }

                noticeReservation(e.todoInfo, parseInt(e.detailConnectInfo));
            }
        }

        function noticeReservation(phone, time) {
            const preTime = toastedCalling[phone];
            if (preTime) {
                if (preTime + 1000 * 60 * 5 > time)
                    return;
            }

            toastedCalling[phone] = time;

            const div = $('<div/>').append(
                $('<div/>', {onclick: "$(this).closest('.toast').find('.toast-close-button').click(); $('#calling-number').val('" + phone + "');", style: 'cursor: pointer;'})
                    .append($('<text/>', {text: '[' + moment(time).format('HH:mm') + '] '}))
                    .append($('<b/>', {text: phone}))
            );
            toastr.warning(div.html(), '통화약속');
        }

        $(document).ready(function () {
            setReservation();
            if ($(parent.document).find('#main').is('.change-mode')) {
                setInterval(setReservation, 60000);
            }
            setInterval(toastReservation, 3000);
        });
    </script>
    <script>
        const ITEM_KEY_CONFIG = "counselDisplayConfiguration";
        let counselDisplayConfiguration = {useCallNoticePopup: true};

        function viewCallPanel() {
            $('#talk-panel').removeClass('active');
            $('#call-panel').addClass('active');
            $('#call-custom-input-container,#call-counseling-input-container').show();
            $('#talk-custom-input-container,#talk-counseling-input-container').hide();

            $('#etc-panel-resizer').hide();
            callExpandEtcPanel();
        }

        function viewTalkPanel() {
            $('#call-panel').removeClass('active');
            $('#talk-panel').addClass('active');
            $('#call-custom-input-container,#call-counseling-input-container').hide();
            $('#talk-custom-input-container,#talk-counseling-input-container').show();

            $('#etc-panel-resizer').show();
            reduceEtcPanel();
        }

        function expandEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_down');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $('#talk-panel').addClass('reduce-panel');
            $('#etc-panel').addClass('expand-panel');
        }

        function callExpandEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_up');
            $('#talk-panel').removeClass('expand-panel');
            $('#etc-panel').removeClass('reduce-panel');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
        }

        function reduceEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_up');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $('#talk-panel').addClass('expand-panel');
            $('#etc-panel').addClass('reduce-panel');
        }

        $('#etc-panel-resizer').click(function () {
            if ($(this).find('i').text() === 'keyboard_arrow_down')
                reduceEtcPanel();
            else
                expandEtcPanel();
        });

        $('.-counsel-panel-indicator').click(function () {
            $('.-counsel-panel-indicator').removeClass('active');
            $(this).addClass('active');
        });

        function popupFieldInfo(type, fieldId, selectValue) {
            var isEmpty = function (value) {
                if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
                    return true
                } else {
                    return false
                }
            };
            if (isEmpty(selectValue)) {
                alert('선택하세요.');
                return;
            }
            popupDraggableModalFromReceivedHtml($.addQueryString('/counsel/modal-field-info', {type: type, fieldId: fieldId, selectItem: selectValue}), 'modal-field-info');
        }


        function popupSearchMaindbCustomModal(type, roomId, senderKey, userKey) {
            popupDraggableModalFromReceivedHtml($.addQueryString('/counsel/modal-search-maindb-custom', {
                type: type || 'CALL',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), 'modal-search-maindb-custom');
        }

        function popupSearchCounselingHistoryModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-counseling-history', 'modal-search-counseling-history');
        }

        function popupSearchCallHistoryModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-call-history', 'modal-search-call-history');
        }

        function popupSearchCallbackModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-callback-history', 'modal-search-callback-history');
        }

        function popupCounselDisplayConfiguration() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-config', 'modal-config');
        }

        function popupCounselingInfo(seq) {
            popupDraggableModalFromReceivedHtml('/counsel/modal-counseling-info?seq=' + seq, 'modal-counseling-info');
        }

        function talkHistoryView(roomId) {
            popupDraggableModalFromReceivedHtml('/admin/talk/history/modal?roomId=' + encodeURIComponent(roomId), 'modal-consulting-history-talk-view');
        }

        function setAlertCurrentStatus() {
            const ui = $('#current-status-sheet');

            const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
            if (!stringify)
                return;

            const data = JSON.parse(stringify);

            keys(data).map(function (key) {
                const tr = ui.find('[data-name="' + key + '"]');
                const value = parseInt(tr.attr('data-value'));
                if (value !== 0 && (!value || isNaN(value))) return;

                const threshold = parseInt(data[key]);
                if (!threshold || isNaN(threshold)) return;

                if (threshold >= value)
                    tr.addClass('negative');
                else
                    tr.removeClass('negative');
            });
        }

        function setConfiguredTab() {
            $('.-configured-indicator,.-configured-tab').hide();

            const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
            if (!stringify)
                return;

            const data = JSON.parse(stringify);

            keys(data).map(function (key) {
                $('.-configured-indicator').filter(function () {
                    return $(this).attr('data-tab') === key;
                }).show();

                $('.-configured-tab').filter(function () {
                    return $(this).attr('data-tab') === key;
                }).css('display', '');
            });

            if ($('.-configured-indicator.active').css('display') === 'none') {
                $('.-counsel-panel-indicator[data-tab="call-panel"]').click();
            } else {
                $('.-configured-indicator.active').click();
            }
        }

        function loadCoworkerNavigation() {
            replaceReceivedHtmlInSilence('/counsel/coworker-navigation', '#counsel-nav');
        }

        function loadOuterLink() {
            replaceReceivedHtmlInSilence('/counsel/outer-link', '#outer-link-list');
        }

        function loadCurrentStatus() {
            replaceReceivedHtmlInSilence('/counsel/current-status', '#current-status-sheet');
        }

        function loadTodoList() {
            replaceReceivedHtmlInSilence('/counsel/todo-list', '#todo-list');
        }

        function loadCounselingList(customId) {
            if (customId && customId !== '') {
                replaceReceivedHtmlInSilence($.addQueryString('/counsel/counsel-list', {customId: customId}), '#counsel-list');
            } else {
                $('#counsel-list').empty();
            }
        }

        $(window).on('load', function () {
            loadCoworkerNavigation();
            loadOuterLink();
            loadCurrentStatus();
            setInterval(function () {
                if ($(parent.document).find('#main').is('.change-mode')) {
                    loadCoworkerNavigation();
                    loadOuterLink();
                    loadCurrentStatus();
                    loadTodoList();
                }
            }, 30 * 1000);

            $('#call-control-panel').show();

            (function () {
                const configString = localStorage.getItem(ITEM_KEY_CONFIG);
                if (!configString) {
                    localStorage.setItem(ITEM_KEY_CONFIG, JSON.stringify(counselDisplayConfiguration));
                } else {
                    counselDisplayConfiguration = JSON.parse(configString);
                }
            })();

            setAlertCurrentStatus();
            setConfiguredTab();
        });
    </script>
    <script>
        const services = {<c:forEach var="e" items="${services}">'${g.escapeQuote(e.key)}': '${g.escapeQuote(e.value)}', </c:forEach>};

        let currentUserStatus = null;
        let currentUserStatusChangedAt = null;
        setInterval(function () {
            $('.-call-waiting-time').each(function () {
                $(this).text((parseInt($(this).text()) + 1) + '초');
            });

            if (currentUserStatusChangedAt !== null) {
                const time = parseInt((new Date().getTime() - currentUserStatusChangedAt) / 1000);
                $('#status-keeping-time').text(pad(parseInt(time / 60), 2) + ":" + pad(time % 60, 2));
            }
        }, 1000);

        $(window).on('load', function () {
            updateQueues();
            updatePersonStatus();
        });
    </script>
    <script>
        window.ipccCommunicator = new IpccCommunicator();

        let audioId, phoneNumber;
        ipccCommunicator
            .on('LOGIN', function (message, kind /*[ LOGIN_OK | LOGIN_ALREADY | LOGOUT | ... ]*/) {
                if (kind === "LOGOUT")
                    return alert("로그아웃");
                if (kind === 'LOGIN_OK' || kind === 'LOGIN_ALREADY')
                    return;
                alert("IPCC 웹소켓 로긴 실패");
            })
            .on('PEER', function (message, kind, data1, data2 /*[ OK | REGISTERED | REACHABLE | NOK | UNREACHABLE | UNREGISTERED | ... ]*/) {
            })
            .on('MEMBERSTATUS', function (message, kind) {
                const status = parseInt(kind);
                $('.-member-status')
                    .removeClass("active")
                    .filter(function () {
                        return parseInt($(this).attr('data-status')) === status;
                    }).addClass("active");

                currentUserStatus = status;
                currentUserStatusChangedAt = new Date().getTime();
            })
            .on('PDSMEMBERSTATUS', function (message, kind) {
                if (!$.isNumeric(pdsStatus))
                    return;

                const status = parseInt(kind);

                $('.-member-status-pds')
                    .removeClass("active")
                    .filter(function () {
                        return parseInt($(this).attr('data-status')) === status;
                    }).addClass("active");

                currentUserStatusChangedAt = new Date().getTime();
            })
            .on('CALLEVENT', function (message, kind /*[ IR | ID | OR | OD | ... ]*/, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12, data13) {
                if (kind === "PICKUP" || kind === "ID" || kind === "OD")
                    $('#partial-recoding').show().find('text').text('부분녹취');

                if (kind === 'IR') { // 인바운드 링울림
                    audioId = data8;
                    phoneNumber = data1;
                    const callingPath = data3;
                    const extension = data2;
                    const secondNum = data4;

                    const candidateQueues = values(queues).filter(function (queue) {
                        return queue.number === secondNum;
                    });

                    const queueName = candidateQueues && candidateQueues.length > 0 ? candidateQueues[0].hanName : null;

                    $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('.-call-waiting-time').text('0초');
                    $('#call-status').text('[수신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
                    loadUserCustomInfo(phoneNumber);
                    $('#user-call-history').empty().append('<option>통화진행중</option>');

                    if (counselDisplayConfiguration.useCallNoticePopup)
                        viewCallReception();

                    loadCustomInput(null, null, phoneNumber).done(function () {
                        $('.item[data-tab="counsel-list"]').click();
                    });
                } else if (kind === 'PICKUP') { //픽업
                    audioId = data8;
                    phoneNumber = data1;
                    const callingPath = data3;
                    const extension = data2;
                    const secondNum = data4;

                    const candidateQueues = values(queues).filter(function (queue) {
                        return queue.number === secondNum;
                    });

                    const queueName = candidateQueues && candidateQueues.length > 0 ? candidateQueues[0].hanName : null;

                    $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('.-call-waiting-time').text('0초');
                    $('#call-status').text('[수신] 당겨받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');

                    /*if (counselDisplayConfiguration.useCallNoticePopup)
                        viewCallReception();
*/
                    loadCustomInput(null, null, phoneNumber).done(function () {
                        $('.item[data-tab="counsel-list"]').click();
                    });
                } else if (kind === 'ID') { // 인바운드 통화시작
                    audioId = data8;
                    phoneNumber = data1;

                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('#modal-calling').modalHide();
                    $('#call-status').text('[수신] 전화받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');
                } else if (kind === 'OR') { // 아웃바운드 링울림
                    audioId = data8;
                    phoneNumber = data2;

                    if (data9 === 'PRV') {
                        const previewGroupId = data11;
                        const previewCustomId = data12;

                        loadPreviewCustomInput(previewGroupId, previewCustomId);
                        loadPreviewCounselingInput(previewGroupId, previewCustomId);
                    } else {
                        $('.-calling-number').val(phoneNumber).text(phoneNumber);
                        $('#call-status').text('[발신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
                        $('#user-call-history').empty().append('<option>통화진행중</option>');

                        loadCustomInput(null, null, phoneNumber).done(function () {
                            $('.item[data-tab="counsel-list"]').click();
                        });
                    }
                } else if (kind === 'OD') { // 아웃바운드 통화시작
                    audioId = data8;
                    phoneNumber = data2;

                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('#call-status').text('[발신] 전화받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');
                }
            })
            .on('HANGUPEVENT', function () {
                $('#partial-recoding').hide();
                $('#call-status').text('통화종료');

                // IR 이후, 곧장 HANGUP이 일어나면 modal의 transition때문에 옳바르게 dimmer가 상태변화되지 못한다.
                setTimeout(function () {
                    $('#modal-calling').modalHide();
                }, 1000);

                //HANGUP이 일어나고 DB에 데이터가 쌓이는 시간 감안
                setTimeout(function () {
                    loadUserCallHistory();
                }, 5000);
            })
            .on('FORWARDING', function (message, kind/*[ OK | ... ]*/, data1, data2/*[ (공백) | A | B | C | T | ... ]*/) {
            })
            .on('CALLSTATUS', function (message, kind/*[ REDIRECT | ... ]*/, data1, data2/*[ NOCHAN | BUSY ]*/) {
            })
            .on('DTMFREADEVENT', function (message, kind) {
            })
            .on('PDSMEMBERSTATUS', function (message, kind) {
            })
            .on('MSG', function (message, kind, data1, data2) {
                if (!data1 || !data2)
                    return;

                const decodeMessage = decodeURI(data2).replace(/[ψ\n]/gi, "<br/>");
                toastr.info(data1, decodeMessage);
            })
            .on('CMS', function (message, kind, data1) {
                if (kind === 'OK') {
                    $('#modal-cms-popup').modalHide();
                    return alert('적용되었습니다.');
                } else {
                    return alert('실패하였습니다: ' + data1);
                }
            })
            .on('REC_START'/*부분녹취 시작*/, function (message, kind/*[ NOK | STARTOK ]*/, data1, partialRecordCnt) {
                const partialRecord = $('#partial-recoding');
                if (kind === 'NOK') {
                    console.error("부분녹취실패 : {}", message);
                    return;
                }
                if (kind === 'STARTOK')
                    $(partialRecord).find('text').text('부분녹취(' + partialRecordCnt + ")");
            })
            .on('REC_STOP'/*부분녹취 중지*/, function (message, kind/*[ NOK | STOPOK ]*/, data1, partialRecordCnt) {
                const partialRecord = $('#partial-recoding');
                if (kind === 'NOK') {
                    console.error("부분녹취실패 : {}", message);
                    return;
                }
                if (kind === 'STOPOK')
                    $(partialRecord).find('text').text('부분녹취(' + partialRecordCnt + ")");
            })
            .on('BYE', function (message, kind/*[ SAME_UID | SAME_PID ]*/, data1, data2, data3) {
                switch (kind) {
                    case"SAME_UID":
                        return alert("다른 컴퓨터에서 같은 아이디로 로긴되어서 서버와 끊김");
                    case "SAME_PID":
                        return alert("다른 컴퓨터에서 같은 내선으로로 로긴되어서 서버와 끊김");
                    default:
                        return alert("[" + kind + "]" + data3 + "(" + data1 + ")");
                }
            });


        let pdsStatus;

        $(window).on('load', function () {
            restSelf.get('/api/auth/socket-info').done(function (response) {
                const fromUi = "EICN_IPCC";
                if (response.data.extension != null)
                    ipccCommunicator.connect(response.data.callControlSocketUrl, response.data.pbxHost, response.data.companyId, response.data.userId, response.data.extension, hex_sha512(response.data.password), response.data.idType, fromUi, response.data.isMulti);

                <c:if test="${user.isTalk.equals('Y')}">
                talkCommunicator.connect(response.data.talkSocketUrl, response.data.companyId, response.data.groupCode, response.data.groupTreeName, response.data.groupLevel, response.data.userId, response.data.userName, "USER", response.data.idType);
                </c:if>
            });

            $(".-call-reject").click(function () {
                $('#modal-calling').modalHide();
                ipccCommunicator.reject();
            });

            $(".-call-receive").click(function () {
                $('#modal-calling').modalHide();
                ipccCommunicator.receiveCall();
            });

            $(".-call-hangup").click(function () {
                ipccCommunicator.hangUp();
                ipccCommunicator.stopMultiCall();
            });

            $('.-call-pickup').click(function () {
                ipccCommunicator.pickUp();
            });

            $(".-call-hold").click(function () {
                if (ipccCommunicator.status.cMemberStatus !== MEMBER_STATUS_CALLING)
                    return;

                if ($(this).hasClass("active")) {
                    ipccCommunicator.stopHolding();
                    $(this).removeClass("active");
                } else {
                    ipccCommunicator.startHolding();
                    $(this).addClass("active");
                }
            });

            $('#partial-recoding').click(function () {
                if ($(this).find('i').hasClass('fa-play')) {
                    ipccCommunicator.startRecording();
                    const recordIcon = $(this).find('i');
                    recordIcon.removeClass('fa-play').addClass('fa-stop').addClass("partial-record-stop").show();
                } else {
                    ipccCommunicator.stopRecording();
                    $(this).find('i').removeClass('fa-stop').removeClass("partial-record-stop").addClass('fa-play').show();
                }
            });
        });
    </script>
</tags:scripts>
