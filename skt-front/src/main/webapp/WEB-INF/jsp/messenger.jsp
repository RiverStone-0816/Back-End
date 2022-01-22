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
            <ul class="organization-ul modal"></ul>
        </div>
    </div>
    <div class="actions">
        <span class="left-txt">선택된 조직원 <text class="-selected-user-count">0</text>명</span>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui orange button modal-close -submit">확인</button>
    </div>
</div>

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
                            <li v-else v-for="(e, i) in list" :key="i" class="-messenger-user" :data-id="e.id" onclick="toggleActive(event, this)" @dblclick.stop.prevent="openRoom(e.id)">
                                <div class="user-wrap">
                                    <span class="user-icon" :class="e.isLoginChatt === 'L' && ' active '"></span>
                                    <text>{{ e.idName }}</text>
                                </div>
                                <div class="btn-wrap">
                                    <span class="ui mini label -consultant-status-with-color" :data-peer="e.peer" :style="'visibility: ' + (e.peer ? 'visible' : 'hidden')"></span>
                                    <div class="buttons">
                                        <button class="arrow button" data-inverted data-tooltip="호전환" data-position="bottom center" @click.stop.prevent="redirectTo(e.extension)"></button>
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
                            openRoom: function (userId) {
                                messenger.openRoom(userId)
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
                        <%--TODO--%>
                        <button type="button" class="ui basic mini button" onclick="messenger.openRoomFromOrganization()">선택대화</button>
                    </c:if>
                </div>
                <div class="panel-segment-body overflow-overlay">
                    <div class="area">
                        <ul v-for="(team, i) in teams" :key="i" class="organization-ul" :data-id="team.groupCode">
                            <div class="title">
                                <span class="team-name">{{ team.groupName }}</span>
                                <div class="dot-label-wrap">
                                    <span class="dot-label active"></span>
                                    <text class="-group-login-user-count" :data-group="team.groupCode">{{ getTeamLoginCount(team) }}</text>
                                    <span class="dot-label"></span>
                                    <text class="-group-logout-user-count" :data-group="team.groupCode">{{ getTeamLogoutCount(team) }}</text>
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
                                        <button class="arrow button" data-inverted data-tooltip="호전환" data-position="bottom center" @click.stop.prevent="redirectTo(person.extension)"></button>
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
                                restSelf.get('/api/monit/', null, null, true).done(function (response) {
                                    _this.teams = []
                                    response.data.forEach(function (team) {
                                        if (!team.person || !team.person.length || (team.person.length === 1 && team.person[0].id === userId)) return
                                        _this.teams.push(team)
                                        for (let i in team.person)
                                            if (team.person[i].id === userId)
                                                return team.person.splice(i, 1)
                                    })
                                })
                            },
                            redirectTo: function (extension) {
                                ipccCommunicator.redirect(extension)
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
                            openRoom: function (userId) {
                                messenger.openRoom(userId)
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
                            getTeamLoginCount(team) {
                                console.log(team)
                                return 0
                            },
                            getTeamLogoutCount(team) {
                                console.log(team)
                                return 0
                            },
                            hierarchicalOrganizationString(team) {
                                console.log(team)
                                return team.groupName
                            },
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
