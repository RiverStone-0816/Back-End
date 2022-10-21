const MEMBER_STATUS_CALLING = 1;

let currentUserStatusChangedAt;
let audioId;
let phoneNumber;

setInterval(function () {
    $('.-call-waiting-time').each(function () {
        $(this).text((parseInt($(this).text()) + 1) + '초');
    });

    if (currentUserStatusChangedAt) {
        const time = parseInt((new Date().getTime() - currentUserStatusChangedAt) / 1000);
        $('#status-keeping-time').text(pad(parseInt(time / 60), 2) + ":" + pad(time % 60, 2));
    } else {
        $('#status-keeping-time').text('00:00');
    }
}, 1000);

$(window).on('load', function () {
    restSelf.get('/api/auth/socket-info').done(function (response) {
        const fromUi = "EICN_CHATT";
        if (response.data.extension)
            ipccCommunicator.connect(response.data.callControlSocketUrl, response.data.pbxHost, response.data.companyId, response.data.userId, response.data.extension, response.data.password, response.data.idType, fromUi, response.data.isMulti);
    });
});

$('.-member-status').click(function (event) {
    event.stopPropagation();

    const status = parseInt($(this).attr('data-status'));

    if (status === MEMBER_STATUS_CALLING) return false;
    if (ipccCommunicator.status.cMemberStatus === MEMBER_STATUS_CALLING) return false;

    ipccCommunicator.setMemberStatus(status);

    if ($(this).hasClass('-pds-status')) {
        $('.pds-status-button-container').show();
    } else {
        $('.pds-status-button-container').hide();
        $(document).click();
    }

    return false;
});

$('.-member-status-pds').click(function (event) {
    event.stopPropagation();

    const pds = $(this).attr('data-status');

    if (parseInt(pds) === MEMBER_STATUS_CALLING)
        return;

    ipccCommunicator.setPdsStatus(pds);
});

$(document).on('click', '.-extension', function (event) {
    confirm('[' + $(event.target).attr('data-value') + '] 전화 연결 하시겠습니까?').done(function () {
        ipccCommunicator.clickDial('', $(event.target).attr('data-value'));
        $(document).click();
    });
});

$(document).on('click', '.-hp-number', function (event) {
    confirm('전화 연결 하시겠습니까?').done(function () {
        $('#calling-number').val($(event.target).attr('data-value'));
        tryDial('MAINDB');
        $(document).click();
    });
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

const ipccCommunicator = new IpccCommunicator()
    .on('svcloginerror', function (data) {
        alert("[실패] " + data.error_text || JSON.stringify(data), function () {
            logout();
        });
    })
    .on('LOGIN', function (message, kind /*[ LOGIN_OK | LOGIN_ALREADY | LOGOUT | ... ]*/) {
        if (kind === "LOGOUT")
            return alert("로그아웃");
        if (kind === 'LOGIN_OK' || kind === 'LOGIN_ALREADY') {
            return restSelf.get('/api/monit/').done(function (response) {
                response.data.map(function (group) {
                    if (!group.person)
                        return;
                    group.person.map(function (person) {
                        const userId = person.id.toLowerCase();
                        peerToUserIds[person.peer] = userId;
                        if (userStatuses[userId])
                            userStatuses[userId].peer = person.peer;

                        if (person.extension) {
                            $('.-messenger-user,.-messenger-bookmark').filter('[data-id="' + userId + '"]').find('.-extension').text('내선:' + person.extension).attr('data-value', person.extension);
                        } else {
                            $('.-messenger-user,.-messenger-bookmark').filter('[data-id="' + userId + '"]').find('.-extension').text('').attr('data-value', '');
                        }
                    });
                });
            });
        }
        alert("[실패] " + kind);
    })
    .on('MEMBERSTATUS', function (message, kind) {
        const status = parseInt(kind);
        $('.-member-status')
            .removeClass("active")
            .filter(function () {
                return parseInt($(this).attr('data-status')) === status;
            }).addClass("active");

        currentUserStatusChangedAt = new Date().getTime();

        const statusIndicator = $('#user-state-change');
        statusIndicator.find('.material-icons').text(statusCodes[status] && statusCodes[status].icon || 'person');
        statusIndicator.find('.state-name').text(statusCodes[status] && statusCodes[status].name || '');

        if (statusCodes[status] && statusCodes[status].name === 'PDS') {
            $('.pds-status-button-container').show();
        } else {
            $('.pds-status-button-container').hide();
        }
    })
    .on('PDSMEMBERSTATUS', function (message, kind) {
        const status = parseInt(kind);

        $('.-member-status-pds')
            .removeClass("active")
            .filter(function () {
                return parseInt($(this).attr('data-status')) === status;
            }).addClass("active");

        currentUserStatusChangedAt = new Date().getTime();
    })
    .on('CALLEVENT', function (message, kind /*[ IR | ID | OR | OD | ... ]*/, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12) {
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

            $(".-calling-service").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
            $('.-calling-number').val(phoneNumber).text(phoneNumber);
            $('.-call-waiting-time').text('0초');
            restSelf.get('/api/maindb-data/' + phoneNumber + '/name').done(function (response) {
                const customName = response.data;
                if (customName !== phoneNumber) {
                    const idName = customName.split('[')[0];
                    $('.-calling-custom').val(idName.length > 5 ? idName.substring(0,4).concat('..').concat(customName.replace(idName, '')) : customName);
                }
            });
            console.log('[수신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
            $('#modal-calling').modalShow();

            if (window.isElectron)
                window.ipcRenderer.send('calling');
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

            // $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
            $('.-calling-number').val(phoneNumber).text(phoneNumber);
            $('.-call-waiting-time').text('0초');
            console.log('[수신] 당겨받음 [' + moment().format('HH시 mm분') + ']');
        } else if (kind === 'ID') { // 인바운드 통화시작
            audioId = data8;
            phoneNumber = data1;

            $('.-calling-number').val(phoneNumber).text(phoneNumber);
            restSelf.get('/api/maindb-data/' + phoneNumber + '/name').done(function (response) {
                const customName = response.data;
                if (customName !== phoneNumber) {
                    const idName = customName.split('[')[0];
                    $('.-calling-custom').val(idName.length > 5 ? idName.substring(0,4).concat('..').concat(customName.replace(idName, '')) : customName);
                }
            });
            $('#modal-calling').modalHide();
            console.log('[수신] 전화받음 [' + moment().format('HH시 mm분') + ']');
        } else if (kind === 'OR') { // 아웃바운드 링울림
            audioId = data8;
            phoneNumber = data2;

            if (data9 === 'PRV') {
                const previewGroupId = data11;
                const previewCustomId = data12;
            } else {
                $('.-calling-number').val(phoneNumber).text(phoneNumber);
                console.log('[발신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
            }
        } else if (kind === 'OD') { // 아웃바운드 통화시작
            audioId = data8;
            phoneNumber = data2;

            $('.-calling-number').val(phoneNumber).text(phoneNumber);
            console.log('[발신] 전화받음 [' + moment().format('HH시 mm분') + ']');
        }
        restSelf.get('/api/maindb-data/' + phoneNumber + '/name').done(function (response) {
            const customName = response.data;
            if (customName !== phoneNumber) {
                const idName = customName.split('[')[0];
                $('.-calling-custom').val(idName.length > 5 ? idName.substring(0,4).concat('..').concat(customName.replace(idName, '')) : customName);
            }
        });
    })
    .on('HANGUPEVENT', function (message, kind, comp, peer, data1, data2, data3, data4, data5) {
        $('#partial-recoding').hide();
        console.log('통화종료' + peer);

        if (kind === 'I' && data3 === '0') {
            const count = Number($('.absensce-count').text()) + 1;
            $('.absensce-count').text(count);
        }

        // IR 이후, 곧장 HANGUP이 일어나면 modal의 transition때문에 옳바르게 dimmer가 상태변화되지 못한다.
        setTimeout(function () {
            $('#modal-calling').modalHide();
        }, 1000);
    })
    .on('CALLBACKEVENT', function (message, kind, data1) {
        const count = Number($('.callback-count').text()) + Number(data1);
        $('.callback-count').text(count);
    })
    .on('BYE', function (message, kind/*[ SAME_UID | SAME_PID ]*/, data1, data2, data3) {
        switch (kind) {
            case"SAME_UID":
                return alert("다른 컴퓨터에서 같은 아이디로 로긴되어서 서버와 끊김", logout);
            case "SAME_PID":
                return alert("다른 컴퓨터에서 같은 내선으로로 로긴되어서 서버와 끊김", logout);
            default:
                return alert("[" + kind + "]" + data3 + "(" + data1 + ")");
        }
    });

function tryDial(type) {
    const cid = ''; // 비워두면 기본 cid로 시도함
    const number = $('#calling-number').val();

    if (ipccCommunicator.status.cMemberStatus === 1) {
        alert("상담중 상태에서는 전화 걸기가 불가능합니다.");
        return;
    }

    if (!number) {
        alert("존재하지 않는 번호입니다.");
        return;
    }

    ipccCommunicator.clickByCampaign(cid, number, type, '', '');
}
