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
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<c:set var="activeMessenger" value="${serviceKind.equals('SC') && usingServices.contains('CHATT')}"/>

<div class="ui modal tiny" id="user-info-modal">
    <i class="close icon"></i>
    <div class="header">유저정보</div>
    <div class="content">
        <table class="ui table celled unstackable border-top-default">
            <tr>
                <th>이름</th>
                <td class="-field" data-name="idName"></td>
            </tr>
            <tr>
                <th>부서명</th>
                <td class="-field" data-name="groupName"></td>
            </tr>
            <tr>
                <th>메일주소</th>
                <td class="-field" data-name="emailInfo"></td>
            </tr>
            <tr>
                <th>전화번호</th>
                <td class="row-btn-wrap">
                    <text class="-field" data-name="hpNumber"></text>
                    <button type="button" class="-hp-number"></button>
                </td>
            </tr>
            <tr>
                <th>내선번호</th>
                <td class="row-btn-wrap">
                    <text class="-field" data-name="extension"></text>
                    <button type="button" class="-extension"></button>
                </td>
            </tr>
        </table>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">닫기</button>
    </div>
</div>

<div class="ui modal" id="room-creation-organization-modal">
    <i class="close icon"></i>
    <div class="header">
        <text class="-modal-title"></text>
    </div>
    <div class="content">
        <div class="organization-ul-wrap" id="room-creation-organization-panel">
            <template v-for="(team, i) in teams" :key="i">
                <ul v-if="team.person.length" class="organization-ul modal -messenger-folder" :data-id="team.groupCode">
                    <div class="title">
                        <span>{{ team.groupName }}</span>
                        <div class="dot-label-wrap">
                            <span class="dot-label active"></span> {{ getTeamLoginCount(team) }}
                            <span class="dot-label"></span> {{ getTeamLogoutCount(team) }}
                        </div>
                    </div>
                    <li class="belong">
                        <div class="user-wrap">
                            <div class="ui checkbox">
                                <input type="checkbox" @change="calcCountSelected"/>
                                <label>{{ hierarchicalOrganizationString(team) }}</label>
                            </div>
                        </div>
                    </li>
                    <li v-for="(person, j) in team.person" :key="j" class="-messenger-user" :data-id="person.id" :data-idName="person.idName" @click.stop.prevent="toggleActive($event)">
                        <div class="user-wrap">
                            <span class="user-icon" :class="person.isLoginChatt === 'L' && ' active '"></span>
                            <text>{{ person.idName }}</text>
                        </div>
                        <div class="btn-wrap">
                            <span class="ui mini label -consultant-status-with-color" :data-peer="person.peer" :style="'visibility: ' + (person.peer ? 'visible' : 'hidden')"></span>
                            <span v-if="person.extension" class="user-num">내선:{{ person.extension }}</span>
                        </div>
                    </li>
                </ul>
            </template>
        </div>
    </div>
    <div class="actions">
        <span class="left-txt">선택된 조직원 {{ selectedPersonCount }}명</span>
        <button type="button" class="ui button modal-close">취소</button>
        <button class="ui orange button modal-close" @click.stop.prevent="creation ? openRoom() : invite()">{{ creation ? '대화방 생성' : '대화방으로 초대' }}</button>
    </div>
</div>
<tags:scripts>
    <script>
        const roomCreationOrganizationModal = $('#room-creation-organization-modal')
        const roomCreationOrganizationModalApp = Vue.createApp({
            data: function () {
                return {
                    teams: [],
                    selectedPersonCount: 0,
                    creation: true,
                }
            },
            methods: {
                load: function () {
                    const _this = this
                    restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                        _this.teams = []
                        const addTeam = (e, parentGroupNames) => {
                            const team = {
                                groupCode: e.groupCode,
                                groupName: e.groupName,
                                groupNames: [...parentGroupNames, e.groupName],
                                person: e.personList?.filter(person => person.id !== userId)?.map(person => ({
                                    id: person.id,
                                    idName: person.idName,
                                    extension: person.extension,
                                    hpNumber: person.hpNumber,
                                    emailInfo: person.emailInfo,
                                    isLoginChatt: person.isLoginChatt,
                                    peer: person.peer,
                                    groupCode: e.groupCode,
                                    groupName: e.groupName,
                                })) || []
                            }
                            _this.teams.push(team)
                            e.organizationMetaChatt?.map(childOrg => addTeam(childOrg, team.groupNames));
                        }
                        response.data.forEach(e => addTeam(e, []))
                    })
                },
                popupCreationModal() {
                    this.creation = true
                    roomCreationOrganizationModal.dragModalShow()
                },
                popupInvitationModal() {
                    this.creation = false
                    roomCreationOrganizationModal.dragModalShow()
                },
                getSelectedUsers() {
                    const ids = {}
                    $('#room-creation-organization-modal .-messenger-user.active').each(function () {
                        ids[$(this).attr('data-id')] = $(this).attr('data-idName')
                    })
                    $('#room-creation-organization-modal input:checked').each(function () {
                        $(this).closest('.-messenger-folder').find('.-messenger-user').each(function () {
                            ids[$(this).attr('data-id')] = $(this).attr('data-idName')
                        })
                    })
                    return ids
                },
                openRoom() {
                    messenger.openRoom(Object.keys(this.getSelectedUsers()))
                },
                invite() {
                    const ids = this.getSelectedUsers()
                    messenger.invite(Object.keys(ids), Object.values(ids))
                },
                calcCountSelected() {
                    this.selectedPersonCount = Object.keys(this.getSelectedUsers()).length
                },
                toggleActive(event) {
                    const element = event.currentTarget
                    if (event.shiftKey && this.lastActivated && this.lastActivated !== element) {
                        const lastActivated = this.lastActivated
                        let meetFirst = false, meetSecond = false
                        $('#room-creation-organization-modal .-messenger-user').each(function () {
                            if (meetSecond) return
                            if (meetFirst) {
                                $(this).addClass('active')

                                if (this === lastActivated || this === element)
                                    meetSecond = true

                                return
                            }
                            if (this === lastActivated || this === element) {
                                $(this).addClass('active')
                                meetFirst = true
                            }
                        })

                        this.lastActivated = null
                    } else {
                        $(element).toggleClass('active')

                        if ($(element).hasClass('active'))
                            this.lastActivated = element
                    }

                    this.calcCountSelected()
                },
                getTeamLoginCount: (team) => team.person.filter(e => e.isLoginChatt === 'L').length,
                getTeamLogoutCount: (team) => team.person.filter(e => e.isLoginChatt !== 'L').length,
                hierarchicalOrganizationString: (team) => team.groupNames.join('>'),
            },
            updated: function () {
                updatePersonStatus()
            },
            mounted: function () {
                this.load()
            },
        }).mount(roomCreationOrganizationModal[0])
    </script>
</tags:scripts>
<div class="consult-left-panel">
    <div class="panel consult-organization-panel full-height">
        <div class="panel-heading">
            <text>조직도</text>
            <div class="btn-wrap">
                <button type="button" class="ui basic button" id="organi-state">현황</button>
                <c:if test="${user.isChatt.equals('Y')}">
                    <button type="button" class="ui basic button" id="organi-room">
                        <text class="message-indicator">0</text>
                        <text>대화방</text>
                    </button>
                </c:if>
            </div>
            <div class="state-header">현황</div>
            <button class="state-header-close"></button>
        </div>
        <div class="panel-body remove-pl remove-pr">
            <div id="bookmark-list" class="panel-segment favor">
                <div class="panel-segment-header">
                    <text>즐겨찾기</text>
                    <button class="ui basic mini button" @click.stop.prevent="popupBookmarkModal">편집</button>
                </div>
                <div class="panel-segment-body overflow-overlay">
                    <div class="area">
                        <ul class="organization-ul border-bottom-none remove-padding">
                            <div v-if="!list.length" class="empty">즐겨찾기가 없습니다.</div>
                            <li v-else v-for="(e, i) in list" :key="i" class="-messenger-user" :data-id="e.id" @click.stop="toggleActive($event)" @dblclick.stop.prevent="openRoom(e.id)">
                                <div class="user-wrap">
                                    <span class="user-icon" :class="e.isLoginChatt === 'L' && ' active '"></span>
                                    <text>{{ e.idName }}</text>
                                </div>
                                <div class="btn-wrap">
                                    <span class="ui mini label -consultant-status-with-color" :data-peer="e.peer" :style="'visibility: ' + (e.peer ? 'visible' : 'hidden')"></span>
                                    <div class="buttons">
                                        <button class="arrow button" data-inverted data-tooltip="호전환" data-position="bottom center" @click.stop.prevent="attendedTo(e.extension)"></button>
                                        <button class="talk ${user.isTalk.equals('Y') ? 'on' : 'off'} button" @click.stop.prevent="toTalkSearch(e.id)"></button>
                                        <button class="info button" data-inverted data-tooltip="정보" data-position="bottom center" @click.stop.prevent="popupUserModal(e)"></button>
                                    </div>
                                </div>
                                <div class="state-wrap">
                                    <text>전화</text>
                                    <span class="num -user-total-call-count" :data-id="e.id">0</span>
                                    <text>채팅</text>
                                    <span class="num -user-total-chat-count" :data-id="e.id">0</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <tags:scripts>
                <script>
                    const bookmarkList = Vue.createApp({
                        data: function () {
                            return {
                                list: []
                            }
                        },
                        methods: {
                            load: function () {
                                const _this = this
                                restSelf.get('/api/chatt/bookmark-list', null, null, true).done(function (response) {
                                    _this.list = []
                                    response.data.forEach(function (e) {
                                        if (e.id === userId) return
                                        _this.list.push(e)
                                    })
                                })
                            },
                            redirectTo: function (extension) {
                                ipccCommunicator.redirect(extension)
                            },
                            attendedTo: function (extension) {
                                ipccCommunicator.attended(extension)
                            },
                            popupBookmarkModal: function () {
                                const _this = this
                                popupReceivedHtml('/modal-messenger-bookmark', 'modal-messenger-bookmark').done(function () {
                                    window.doneSubmitMessengerBookmarkFormData = function () {
                                        $('#modal-messenger-bookmark').find('.modal-close:first').click()
                                        _this.load()
                                    }
                                })
                            },
                            toTalkSearch(userId) {
                                <c:if test="${user.isTalk.equals('Y')}">
                                // TODO: 상담톡 검색화면으로 이동

                                if ($('#organi-state').hasClass('active'))
                                    $('#organi-state').click();
                                if ($('#organi-room').hasClass('active'))
                                    $('#organi-room').click();

                                $('.-counsel-content-panel .item[data-tab="talk-panel"]').click();
                                $('.item[data-tab="talk-list-type-OTH"]').click();

                                const ui = $('#talk-list-panel');
                                ui.find('.-search-type').val('userId');
                                ui.find('.-search-value').val(userId);
                                ui.find('.-search-button').click();
                                </c:if>
                                <c:if test="${!user.isTalk.equals('Y')}">
                                alert('상담톡 권한이 없습니다.');
                                </c:if>
                            },
                            popupUserModal(user) {
                                teamList.popupUserModal(user)
                            },
                            openRoom(userId) {
                                messenger.openRoom(userId)
                            },
                            toggleActive(event) {
                                $(event.target).closest('li').toggleClass('active')
                            },
                        },
                        updated: function () {
                            updatePersonStatus()
                        },
                        mounted: function () {
                            this.load()
                        },
                    }).mount('#bookmark-list')
                </script>
            </tags:scripts>

            <div id="team-list" class="panel-segment list">
                <div class="panel-segment-header">
                    <text>조직도</text>
                    <c:if test="${activeMessenger}">
                        <button class="ui basic mini button" @click.stop.prevent="openRoom()">선택대화</button>
                    </c:if>
                </div>
                <div class="panel-segment-body overflow-overlay">
                    <div class="area">
                        <template v-for="(team, i) in teams" :key="i">
                            <ul v-if="team.person.length" class="organization-ul -messenger-folder" :data-id="team.groupCode">
                                <div class="title">
                                    <span class="team-name">{{ team.groupName }}</span>
                                    <div class="dot-label-wrap">
                                        <span class="dot-label active"></span> {{ getTeamLoginCount(team) }}
                                        <span class="dot-label"></span> {{ getTeamLogoutCount(team) }}
                                    </div>
                                </div>
                                <li class="belong">
                                    <div class="user-wrap">
                                        <div class="ui checkbox">
                                            <input type="checkbox"/>
                                            <label>{{ hierarchicalOrganizationString(team) }}</label>
                                        </div>
                                    </div>
                                </li>
                                <li v-for="(person, j) in team.person" :key="j" class="-messenger-user" :data-id="person.id" :data-group="team.groupCode" @click.stop.prevent="toggleActive($event)"
                                    @dblclick.stop.prevent="openRoom(person.id)">
                                    <div class="user-wrap">
                                        <span class="user-icon" :class="person.isLoginChatt === 'L' && ' active '"></span>
                                        <text>{{ person.idName }}</text>
                                    </div>
                                    <div class="btn-wrap">
                                        <span class="ui mini label -consultant-status-with-color" :data-peer="person.peer" :style="'visibility: ' + (person.peer ? 'visible' : 'hidden')"></span>
                                        <div class="buttons">
                                            <button class="arrow button" data-inverted data-tooltip="호전환" data-position="bottom center" @click.stop.prevent="attendedTo(person.extension)"></button>
                                            <button class="talk ${user.isTalk.equals('Y') ? 'on' : 'off'} button" @click.stop.prevent="toTalkSearch(person.id)"></button>
                                            <button class="info button" data-inverted data-tooltip="정보" data-position="bottom center" @click.stop.prevent="popupUserModal(person)"></button>
                                        </div>
                                    </div>
                                    <div class="state-wrap">
                                        <text>전화</text>
                                        <span class="num -user-total-call-count" :data-id="person.id">0</span>
                                        <text>채팅</text>
                                        <span class="num -user-total-chat-count" :data-id="person.id">0</span>
                                    </div>
                                </li>
                            </ul>
                        </template>
                    </div>
                </div>
            </div>
            <tags:scripts>
                <script>
                    const teamList = Vue.createApp({
                        data: function () {
                            return {
                                teams: [],
                                lastActivated: null,
                            }
                        },
                        methods: {
                            load: function () {
                                const _this = this
                                restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                                    _this.teams = []
                                    const addTeam = (e, parentGroupNames) => {
                                        const team = {
                                            groupCode: e.groupCode,
                                            groupName: e.groupName,
                                            groupNames: [...parentGroupNames, e.groupName],
                                            person: e.personList?./*filter(person => person.id !== userId)?.*/map(person => ({
                                                id: person.id,
                                                idName: person.idName,
                                                extension: person.extension,
                                                hpNumber: person.hpNumber,
                                                emailInfo: person.emailInfo,
                                                isLoginChatt: person.isLoginChatt,
                                                peer: person.peer,
                                                groupCode: e.groupCode,
                                                groupName: e.groupName,
                                            })) || []
                                        }
                                        _this.teams.push(team)
                                        e.organizationMetaChatt?.map(childOrg => addTeam(childOrg, team.groupNames));
                                    }
                                    response.data.forEach(e => addTeam(e, []))
                                })
                            },
                            redirectTo: function (extension) {
                                ipccCommunicator.redirect(extension)
                            },
                            attendedTo: function (extension) {
                                ipccCommunicator.attended(extension)
                            },
                            toTalkSearch(userId) {
                                <c:if test="${user.isTalk.equals('Y')}">
                                // TODO: 상담톡 검색화면으로 이동

                                if ($('#organi-state').hasClass('active'))
                                    $('#organi-state').click();
                                if ($('#organi-room').hasClass('active'))
                                    $('#organi-room').click();

                                $('.-counsel-content-panel .item[data-tab="talk-panel"]').click();
                                $('.item[data-tab="talk-list-type-OTH"]').click();

                                const ui = $('#talk-list-panel');
                                ui.find('.-search-type').val('userId');
                                ui.find('.-search-value').val(userId);
                                ui.find('.-search-button').click();
                                </c:if>
                                <c:if test="${!user.isTalk.equals('Y')}">
                                alert('상담톡 권한이 없습니다.');
                                </c:if>
                            },
                            popupUserModal(user) {
                                const modal = $('#user-info-modal');

                                user.groupName = this.teams.filter(team => team.person.filter(person => person.id === user.id)[0])[0]?.groupName;
                                modal.find('.-field').each(function () {
                                    $(this).text(user[$(this).attr('data-name')]);
                                });
                                modal.find('.-extension').attr('onclick', user.extension ? 'ipccCommunicator.clickDial("", "' + user.extension + '")' : '');
                                modal.find('.-hp-number').attr('onclick', user.hpNumber ? 'ipccCommunicator.clickDial("", "' + user.hpNumber + '")' : '');
                                modal.dragModalShow();
                            },
                            openRoom(userId) {
                                if (userId) return messenger.openRoom(userId)

                                const ids = []
                                $('#bookmark-list .-messenger-user.active, #team-list .-messenger-user.active').each(function () {
                                    const id = $(this).attr('data-id')
                                    !ids.includes(id) && ids.push(id)
                                })
                                $('#team-list input:checked').each(function () {
                                    $(this).closest('.-messenger-folder').find('.-messenger-user').each(function () {
                                        const id = $(this).attr('data-id')
                                        !ids.includes(id) && ids.push(id)
                                    })
                                })
                                messenger.openRoom(ids)
                            },
                            toggleActive(event) {
                                const element = event.currentTarget
                                if (event.shiftKey && this.lastActivated && this.lastActivated !== element) {
                                    const lastActivated = this.lastActivated
                                    let meetFirst = false, meetSecond = false
                                    $('#team-list .-messenger-user').each(function () {
                                        if (meetSecond) return
                                        if (meetFirst) {
                                            $(this).addClass('active')

                                            if (this === lastActivated || this === element)
                                                meetSecond = true

                                            return
                                        }
                                        if (this === lastActivated || this === element) {
                                            $(this).addClass('active')
                                            meetFirst = true
                                        }
                                    })

                                    this.lastActivated = null
                                } else {
                                    $(element).toggleClass('active')

                                    if ($(element).hasClass('active'))
                                        this.lastActivated = element
                                }
                            },
                            getTeamLoginCount: (team) => team.person.filter(e => e.isLoginChatt === 'L').length,
                            getTeamLogoutCount: (team) => team.person.filter(e => e.isLoginChatt !== 'L').length,
                            hierarchicalOrganizationString: (team) => team.groupNames.join('>'),
                        },
                        updated: function () {
                            updatePersonStatus()
                        },
                        mounted: function () {
                            this.load()
                        },
                    }).mount('#team-list')
                </script>
            </tags:scripts>
        </div>
    </div>
    <div class="organi-pop-wrap">
        <div class="organi-pop-second">
            <jsp:include page="/WEB-INF/jsp/messenger-room-list.jsp"/>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/modal-messenger-room.jsp"/>
<jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
<tags:scripts>
    <script>
        const leftPanel = $('.consult-left-panel');
        $('#organi-state').click(function () {
            leftPanel.toggleClass('wide');
            $('.consult-wrapper .consult-center-panel').toggleClass('control');
            if (leftPanel.hasClass('wide') === true) {
                $('.content-inner.-counsel-content-panel').addClass('control');
            } else {
                $('.content-inner.-counsel-content-panel').removeClass('control');
            }
        });

        $('.panel-segment.favor').resizable({minHeight: 88});

        $('.consult-organization-panel .panel-heading .ui.basic.button').click(function () {
            $(this).toggleClass('active');
        });

        $('.consult-organization-panel .state-header-close').click(function () {
            leftPanel.removeClass('wide');
            $('#organi-state').removeClass('active');
            $('.consult-wrapper .consult-center-panel').removeClass('control');
            if (leftPanel.hasClass('wide') === true) {
                $('.content-inner.-counsel-content-panel').addClass('control');
            } else {
                $('.content-inner.-counsel-content-panel').removeClass('control');
            }
        });

        $('.organi-pop-second .panel-close').click(function () {
            $('.consult-organization-panel #organi-room').removeClass('active');
            $(this).parents('.organi-pop-second').hide();
        });

        $('#organi-room').click(function () {
            <c:if test="${activeMessenger}">
            $('.organi-pop-second').toggle();
            </c:if>

            <c:if test="${!activeMessenger}">
            alert('메신저 라이센스가 없습니다.');
            </c:if>
        });

        $(window).on('load', function () {
            setInterval(function () {
                restSelf.get('/api/chatt/user-score-moniter', null, null, true).done(function (response) {
                    response.data.map(function (e) {
                        $('.-user-total-call-count[data-id="' + e.userId + '"]').text(e.totalCnt);
                        $('.-user-total-chat-count[data-id="' + e.userId + '"]').text(e.talkCnt);
                    });
                });
            }, 1000 * 60);
        });
    </script>
</tags:scripts>
