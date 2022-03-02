<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="usingservices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<c:set var="activeMessenger" value="${serviceKind.equals('SC') && usingServices.contains('CHATT')}"/>

<aside class="side-bar consulting-side">
    <div class="side-bar-tab-container">
        <ul class="side-bar-tab">
            <li class="active" data-tab="organization-area"><i class="list ul icon"></i>조직도</li>
            <c:if test="${activeMessenger}">
                <li data-tab="room-list-area"><i class="comments icon" id="messenger-unread-indicator"></i>채팅방</li>
            </c:if>
        </ul>
    </div>
    <button class="nav-bar"><i class="material-icons arrow">keyboard_arrow_left</i></button>
    <div class="side-bar-content active" id="organization-area">
        <div class="organization-area-inner">
            <div class="sidebar-menu-container">
                <c:if test="${activeMessenger}">
                    <div id="bookmark-list" class="consulting-accordion favorite active" onclick="toggleFold(event, this)">
                        <div class="consulting-accordion-label">
                            <div>
                                즐겨찾기
                            </div>
                            <div>
                                <button type="button" class="ui basic white very mini compact button ml10" @click.stop="popupBookmarkModal">편집</button>
                            </div>
                        </div>
                        <div class="consulting-accordion-content">
                            <ul class="treeview-menu treeview-on consulting-accordion-content favorite overflow-overlay">
                                <li v-if="!list.length" class="empty">등록된 즐겨찾기가 없습니다.</li>
                                <li v-else v-for="(e, i) in list" :key="i" style="cursor: pointer; padding-right: 0px;" class="-messenger-user" :data-id="e.id" onclick="toggleActive(event, this)"
                                    @dblclick.stop.prevent="openRoom(e.id)" >
                                    <div>
                                        <i class="user outline icon -consultant-login" :data-peer="e.peer" data-logon-class="online"></i>
                                        <span class="user">{{ e.idName }}<text v-if="e.extension">[{{ e.extension }}]</text></span>
                                    </div>
                                    <div v-if="e.peer">
                                        <button v-if="e.extension" class="ui icon button mini compact" @click.stop.prevent="redirectTo(e.extension)" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                        <span class="ui mini label -consultant-status-with-color" :data-peer="e.peer"></span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:if>
                <div id="team-list" class="consulting-accordion organization overflow-hidden dp-flex flex-flow-column active">
                    <div class="consulting-accordion-label" onclick="toggleFold(event, this)">
                        <div>
                            조직도
                        </div>
                        <div>
                            <c:if test="${activeMessenger}">
                                <button class="ui basic white very mini compact button ml5" @click.stop.prevent="openRoom()">선택대화</button>
                                <button type="button" class="ui basic white very mini compact button ml5" style="padding: 5px 5px;"
                                        onclick="$('#team-list,#bookmark-list').find('.-messenger-user').removeClass('active')">선택해제</button>
                            </c:if>
                        </div>
                    </div>
                    <div class="consulting-accordion-content overflow-overlay flex-100">
                        <ul class="side-organization-ul">
                            <li v-for="(team, i) in teams" :key="i" class="consulting-accordion active" style="padding-right: 0px;">
                                <div class="consulting-accordion-label team" onclick="toggleFold(event, this)">
                                    <div class="left">
                                        <i class="folder open icon"></i>
                                        <span class="team-name">{{ team.groupName }}</span>
                                    </div>
                                    <div class="right">
                                        <button type="button" class="ui basic white very mini compact button ml5"
                                                onclick="$(this).closest('.consulting-accordion').find('.-messenger-user').addClass('active')">선택</button>
                                        <button type="button" class="ui basic white very mini compact button ml5"
                                                onclick="$(this).closest('.consulting-accordion').find('.-messenger-user').removeClass('active')">해제</button>
                                    </div>
                                </div>
                                <ul class="treeview-menu consulting-accordion-content">
                                    <li v-for="(person, j) in team.person" :key="j" class="team-item -messenger-user" :data-id="person.id" @click.stop.prevent="toggleActive($event)"
                                        @dblclick.stop.prevent="openRoom(person.id)" style="padding-right: 0px;">
                                        <div style="padding-left: 5px">
                                            <i class="user outline icon -consultant-login" :data-peer="person.peer" data-logon-class="online"></i>
                                            <span class="user">{{ person.idName }}<text v-if="person.extension">[{{ person.extension }}]</text></span>
                                        </div>
                                        <div v-if="person.peer">
                                            <button v-if="person.extension" class="ui icon button mini compact" @click.stop.prevent="redirectTo(person.extension)" title="전화돌려주기">
                                                <i class="share icon"></i>
                                            </button>
                                            <span class="ui mini label -consultant-status-with-color" :data-peer="person.peer"></span>
                                        </div>
                                        <c:if test="${usingservices.contains('IM')}">
                                            <button type="button" class="ui icon button mini compact -note-send" title="쪽지발송" @click="popupNoteModal(person)">
                                                <i class="comment alternate icon"></i>
                                            </button>
                                        </c:if>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="box-container">
                <div class="box-container-inner">
                    <div class="box">
                        <div class="sidebar-label">
                            <div>
                                외부링크
                                <button class="ui basic white very mini compact button ml10" onclick="popupBookmarkConfiguration()">편집</button>
                            </div>
                            <div>
                                <i class="material-icons arrow">keyboard_arrow_right</i>
                            </div>
                        </div>
                        <div class="box-content">
                            <div class="ui list" id="outer-link-list"></div>
                        </div>
                    </div>
                    <div class="box call-transform">
                        <div class="sidebar-label">
                            <div>
                                대표/헌트번호돌려주기
                            </div>
                            <div>
                                <i class="material-icons arrow">keyboard_arrow_right</i>
                            </div>
                        </div>
                        <div class="box-content">
                            <jsp:include page="/counsel/call-transfer"/>
                        </div>
                    </div>
                    <div class="box">
                        <div class="sidebar-label">
                            <div>
                                처리현황(30초마다 갱신)
                            </div>
                            <div>
                                <i class="material-icons arrow">keyboard_arrow_right</i>
                            </div>
                        </div>
                        <div class="box-content">
                            <jsp:include page="/counsel/current-status"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <c:if test="${activeMessenger}">
        <jsp:include page="/messenger"/>
    </c:if>

</aside>

<tags:scripts>
    <script>
        function toggleFold(event, target) {
            event.stopPropagation()
            const accordion = $(target).closest('.consulting-accordion')
            accordion.toggleClass('active')
            $(target).find('.arrow:first').text(accordion.hasClass('active') ? 'keyboard_arrow_down' : 'keyboard_arrow_right')
        }

        function toggleActive(event, target) {
            event.stopPropagation()
            $(target).toggleClass('active')
        }

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
                popupNoteModal: function (person) {
                    noteSendPopup(person.extension, person.idName)
                },
                openRoom: function (userId) {
                    messenger.openRoom(userId)
                },
                toggleActive(event) {
                    const element = event.currentTarget
                    if(event.shiftKey && this.lastActivated && this.lastActivated !== element) {
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
            },
            updated: function () {
                updatePersonStatus()
            },
            mounted: function () {
                this.load()
            },
        }).mount('#team-list')

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
                        window.doneSubmitBookmarkFormData = function () {
                            $('#modal-messenger-bookmark').find('.modal-close:first').click()
                            _this.load()
                        }
                    })
                },
                openRoom: function (userId) {
                    messenger.openRoom(userId)
                }
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
