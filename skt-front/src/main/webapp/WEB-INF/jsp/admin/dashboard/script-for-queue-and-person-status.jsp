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

<tags:scripts>
    <script>
        window.MEMBER_STATUS_CALLING = 1;

        window.queues = {
            <c:forEach var="e" items="${queues}">
            '${g.htmlQuote(e.name)}': {
                name: '${g.htmlQuote(e.name)}',
                hanName: '${g.htmlQuote(e.hanName)}',
                number: '${g.htmlQuote(e.number)}',
                waitingCustomerCount: ${e.waitingCustomerCount},
                peers: [<c:forEach var="peer" items="${e.peers}">'${g.escapeQuote(peer)}', </c:forEach>],
            },
            </c:forEach>
        };
        window.peerStatuses = {
            <c:forEach var="e" items="${peerStatuses}">
            '${g.escapeQuote(e.peer)}': {
                peer: '${g.escapeQuote(e.peer)}',
                status: ${e.status},
                login: ${e.login},
                userId: '${peerToUserId.getOrDefault(e.peer, '')}',
            },
            </c:forEach>
        };

        window.statusCodes = {<c:forEach var="e" items="${statusCodes}">'${e.key}': '${g.escapeQuote(e.value)}', </c:forEach>};
        window.statusToColorClass = {
            '0': 'bcolor-bar1',
            '1': 'bcolor-bar2',
            '2': 'bcolor-bar3',
            '3': 'bcolor-bar4',
            '4': 'bcolor-bar5',
            '5': 'bcolor-bar6',
            '6': 'bcolor-bar7',
            '7': 'bcolor-bar8',
            '8': 'bcolor-bar9',
            '9': 'bcolor-bar10'
        };
        window.serviceNumberToQueueName = {
            <c:forEach var="e" items="${serviceNumberToQueueName}">
            '${g.escapeQuote(e.key)}': [<c:forEach var="e2" items="${e.value}">'${g.escapeQuote(e2)}', </c:forEach>],
            </c:forEach>
        };

        function getQueueFromNumber(number) {
            for (let name in queues)
                if (queues.hasOwnProperty(name))
                    if (queues[name].number === number)
                        return queues[name];
            return null;
        }

        function updateQueues() {
            $('.-custom-wait-count').each(function () {
                const queueName = $(this).attr('data-hunt');
                if (queueName) {
                    $(this).text(queues[queueName].waitingCustomerCount);
                } else {
                    $(this).text(values(queues).reduce(function (sum, queue) {
                        return Number(sum + queue.waitingCustomerCount);
                    }, 0));
                }
            });
        }

        function updatePersonStatus() {
            $('.-login-user-count').each(function () {
                const queueName = $(this).attr('data-hunt');
                const serviceNumber = $(this).attr('data-service');

                if (queueName) {
                    $(this).text(values(peerStatuses).filter(function (peer) {
                        return queues[queueName].peers.indexOf(peer.peer) >= 0
                            && peer.login;
                    }).length);
                } else if (serviceNumber) {
                    $(this).text(values(peerStatuses).filter(function (person) {
                        const queueNames = serviceNumberToQueueName[serviceNumber];
                        if (!queueNames)
                            return false;

                        for (let i = 0; i < queueNames.length; i++)
                            return queues[queueNames[i]] && queues[queueNames[i]].peers.indexOf(person.peer) >= 0 && person.login;
                    }).length);
                } else {
                    $(this).text(values(peerStatuses).filter(function (peer) {
                        return peer.login;
                    }).length);
                }
            });

            $('.-logout-user-count').each(function () {
                const queueName = $(this).attr('data-hunt');
                const serviceNumber = $(this).attr('data-service');

                if (queueName) {
                    $(this).text(values(peerStatuses).filter(function (peer) {
                        return queues[queueName].peers.indexOf(peer.peer) >= 0
                            && !peer.login;
                    }).length);
                } else if (serviceNumber) {
                    $(this).text(values(peerStatuses).filter(function (person) {
                        const queueNames = serviceNumberToQueueName[serviceNumber];
                        if (!queueNames)
                            return false;

                        for (let i = 0; i < queueNames.length; i++)
                            return queues[queueNames[i]] && queues[queueNames[i]].peers.indexOf(person.peer) >= 0 && !person.login;
                    }).length);
                } else {
                    $(this).text(values(peerStatuses).filter(function (peer) {
                        return !peer.login;
                    }).length);
                }
            });

            $('.-consultant-status-count').each(function () {
                const statuses = $(this).attr('data-value').split(',');
                const queueName = $(this).attr('data-hunt');
                const loginFilter = $(this).attr('data-login');
                const serviceNumber = $(this).attr('data-service');
                const callStatuses = $(this).attr('data-call') ? $(this).attr('data-call').split(',') : null;

                let peerStatusesValues = values(peerStatuses).filter(function (person) {
                    return statuses.indexOf(person.status + '') >= 0;
                });

                if (queueName)
                    peerStatusesValues = peerStatusesValues.filter(function (person) {
                        return queues[queueName].peers.indexOf(person.peer) >= 0;
                    });

                if (callStatuses)
                    peerStatusesValues = peerStatusesValues.filter(function (person) {
                        return callStatuses.indexOf(person.callStatus) >= 0;
                    });

                if (loginFilter)
                    peerStatusesValues = peerStatusesValues.filter(function (person) {
                        return person.login + '' === loginFilter;
                    });

                if (serviceNumber)
                    peerStatusesValues = peerStatusesValues.filter(function (person) {
                        const queueNames = serviceNumberToQueueName[serviceNumber];
                        if (!queueNames)
                            return false;

                        for (let i = 0; i < queueNames.length; i++)
                            if (queues[queueNames[i]] && queues[queueNames[i]].peers.indexOf(person.peer) >= 0)
                                return true;

                        return false;
                    });

                $(this).text(peerStatusesValues.length);
            });

            $('.-consultant-status').each(function () {
                const peerStatus = peerStatuses[$(this).attr('data-peer')];
                if (!peerStatus) return;
                $(this).text(statusCodes[peerStatus.status]);

                if ($(this).data('sortValue') != null && $(this).data('sortValue') !== '') {
                    $(this).data('sortValue', peerStatus.status);
                }

                $(this).css({
                    backgroundColor: peerStatus.status === 0 ? 'skyblue' : peerStatus.status === 1 ? 'yellow' : peerStatus.status === 2 ? 'orange' :
                        peerStatus.status === 3 ? '#FF7171' : peerStatus.status === 4 ? '#86E57F' : peerStatus.status === 8 ? '#C98AFF' : '#ABABAB'
                });
            });

            $('.-consultant-status-with-color').each(function () {
                const _this = $(this);
                const peer = _this.attr('data-peer');
                const status = peerStatuses[peer].status;
                _this.text(statusCodes[status]);

                values(statusToColorClass).map(function (colorClass) {
                    _this.removeClass(colorClass);
                });

                if (statusToColorClass[status])
                    _this.addClass(statusToColorClass[status]);
            });

            $('.-consultant-send-receive-status').each(function () {
                const peerStatus = peerStatuses[$(this).attr('data-peer')];
                if (!peerStatus) return;

                if (peerStatus.status !== MEMBER_STATUS_CALLING)
                    return $(this).text('');

                $(this).text(peerStatus.callStatus === 'OR' || peerStatus.callStatus === 'OD' ? '발신'
                    : peerStatus.callStatus === 'IR' || peerStatus.callStatus === 'ID' ? '수신' : '');
            });

            $('.-consultant-calling-custom-number').each(function () {
                const peerStatus = peerStatuses[$(this).attr('data-peer')];
                if (!peerStatus) return;

                if (peerStatus.status !== MEMBER_STATUS_CALLING)
                    return $(this).text('');

                $(this).text(peerStatus.callStatus === 'OR' || peerStatus.callStatus === 'OD' ? peerStatus.calledNumber
                    : peerStatus.callStatus === 'IR' || peerStatus.callStatus === 'ID' ? peerStatus.callingNumber : '');
            });

            $('.-consultant-queue-name').each(function () {
                const peerStatus = peerStatuses[$(this).attr('data-peer')];
                if (!peerStatus) return;

                if (peerStatus.status !== MEMBER_STATUS_CALLING)
                    return $(this).text('');

                const queue = getQueueFromNumber(peerStatus.queueNumber);
                if (!queue) return;

                $(this).text(peerStatus.callStatus === 'IR' || peerStatus.callStatus === 'ID' ? queue.hanName
                    : ''
                );
            });

            $('.-consultant-login').each(function () {
                const loginStatus = $(this).attr('data-value');
                if (loginStatus === "Y") {
                    $(this).removeAttr('data-value');
                    $(this).removeClass('translucent');
                    $(this).addClass('blue');
                    return;
                }

                const peerStatus = peerStatuses[$(this).attr('data-peer')];
                if (!peerStatus) return;

                $(this).removeClass('translucent');
                $(this).addClass('blue');

                if (!peerStatus.login) {
                    $(this).removeClass('blue');
                    $(this).addClass('translucent');
                }
            });
        }

        function updatePhoneStatus(peer, status) {
            $('.-consultant-phone-status').each(function () {
                const peerStatus = peerStatuses[peer];
                if (!peerStatus) return;

                if (status.contains('UNREGISTERED') || status.contains('UNREACHABLE'))
                    $('.-consultant-phone-status[data-peer="' + peerStatus.peer + '"]').addClass('translucent').removeClass('blue');
                else
                    $('.-consultant-phone-status[data-peer="' + peerStatus.peer + '"]').removeClass('translucent').addClass('blue');
            });
        }

        setInterval(function () {
            $('.-consultant-status-time').each(function () {
                const peerStatus = peerStatuses[$(this).attr('data-peer')];
                if (!peerStatus) return;

                if (!peerStatus.callStatusUpdatedTime)
                    peerStatus.callStatusUpdatedTime = new Date().getTime();

                const callingTime = parseInt(((peerStatus.callStatusUpdatedTime) - new Date().getTime()) / 1000) * -1;
                $(this).text(pad(parseInt(callingTime / 60), 2) + ':' + pad(callingTime % 60, 2));
            });
        }, 1000);

        createIpccAdminCommunicator()
            .on("ADMLOGIN", function (message, kind, nothing, peer, extension, userId, userName) {
                if (!peerStatuses[peer]) peerStatuses[peer] = {peer: peer, status: 0, login: false, userId: userId};
                peerStatuses[peer].userId = userId;
                if (kind === 'LOGIN') peerStatuses[peer].login = true;
                if (kind === 'LOGOUT') peerStatuses[peer].login = false;

                updatePersonStatus();
            })
            .on("ADMPEER", function (message, kind, noting, peer, status) {
                if (!peerStatuses[peer]) peerStatuses[peer] = {peer: peer, status: 0, login: true};

                updatePhoneStatus(peer, status);
            })
            .on("MEMBERSTATUS", function (message, kind, nothing, peer, currentStatus, previousStatus) {
                if (!peerStatuses[peer]) peerStatuses[peer] = {peer: peer, status: 0, login: true};
                peerStatuses[peer].status = parseInt(currentStatus);
                peerStatuses[peer].callStatusUpdatedTime = new Date().getTime();

                updatePersonStatus();
            })
            .on("ADMQUEUE", function (message, kind /*[JOIN|LEAVE]*/, nothing, huntName, waitingCount, uniqueId) {
                if (!queues[huntName]) queues[huntName] = {waitingCustomerCount: 0, peers: []};
                queues[huntName].waitingCustomerCount = parseInt(waitingCount);

                updateQueues();
            })
            .on("ADMCALLEVENT", function (message, kind /*[OR|OD|IR|ID|PICKUP]*/, peer, callingNumber, calledNumber, value1 /*발신시 CID, 수신시 인입번호*/, value2 /*발신시 과금번호, 수신시 헌트번호*/, ivrKey, recordFile, startTime, uniqueId, isPds) {
                peerStatuses[peer].callStatus = kind;
                peerStatuses[peer].callingNumber = callingNumber;
                peerStatuses[peer].calledNumber = calledNumber;

                if (kind === 'IR' || kind === 'ID')
                    peerStatuses[peer].queueNumber = value2;

                updatePersonStatus();
            });
    </script>
</tags:scripts>
