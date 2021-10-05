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

<aside class="side-bar consulting-side">
    <div class="side-bar-tab-container">
        <ul class="side-bar-tab">
            <li class="active" data-tab="organization-area"><i class="list ul icon"></i>조직도</li>
            <li data-tab="room-list-area"><i class="comments icon" id="messenger-unread-indicator"></i>채팅방</li>
        </ul>
    </div>
    <button class="nav-bar"><i class="material-icons arrow">keyboard_arrow_left</i></button>
    <div class="side-bar-content active" id="organization-area">
        <div class="organization-area-inner">
            <div class="sidebar-menu-container">
                <div class="consulting-accordion favorite active" onclick="toggleFold(event, this)">
                    <div class="consulting-accordion-label">
                        <div>
                            즐겨찾기
                            <button type="button" class="ui basic white very mini compact button ml10" onclick="event.stopPropagation(); popupBookmarkModal();">편집</button>
                        </div>
                        <div>
                            <i class="material-icons arrow">keyboard_arrow_down</i>
                        </div>
                    </div>
                    <div class="consulting-accordion-content">
                        <ul class="treeview-menu treeview-on consulting-accordion-content favorite overflow-overlay" id="bookmark-list">
                            <li v-if="!list.length" class="empty">등록된 즐겨찾기가 없습니다.</li>
                            <li v-else v-for="(e, i) in list" :key="i" style="cursor: pointer" onclick="toggleActive(event, this)">
                                <div>
                                    <i class="user outline icon -consultant-login" :data-peer="e.peer" data-logon-class="online"></i>
                                    <span class="user">{{ e.idName }}[{{ e.extension }}]</span>
                                    <button class="ui icon button mini compact" @click.stop="redirectTo(e.extension)" title="전화돌려주기">
                                        <i class="share icon"></i>
                                    </button>
                                </div>
                                <div>
                                    <span class="ui mini label -consultant-status-with-color" :data-peer="e.peer"></span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="consulting-accordion organization overflow-hidden dp-flex flex-flow-column active" onclick="toggleFold(event, this)">
                    <div class="consulting-accordion-label">
                        <div>
                            조직도
                            <button type="button" class="ui basic white very mini compact button ml10">선택대화</button>
                        </div>
                        <div>
                            <i class="material-icons arrow">keyboard_arrow_down</i>
                        </div>
                    </div>
                    <div class="consulting-accordion-content overflow-overlay flex-100">
                        <ul class="side-organization-ul" id="team-list">
                            <li v-for="(team, i) in teams" :key="i" class="consulting-accordion active" onclick="toggleFold(event, this)">
                                <div class="consulting-accordion-label team">
                                    <div class="left">
                                        <i class="folder open icon"></i>
                                        <span class="team-name">{{ team.groupName }}</span>
                                    </div>
                                    <div class="right">
                                        <i class="material-icons arrow">keyboard_arrow_down</i>
                                    </div>
                                </div>
                                <ul class="treeview-menu consulting-accordion-content">
                                    <li v-for="(person, j) in team.person" :key="j" class="team-item" onclick="toggleActive(event, this)">
                                        <div>
                                            <i class="user outline icon -consultant-login" :data-peer="person.peer" data-logon-class="online"></i>
                                            <span class="user">{{ person.idName }}[{{ person.extension }}]</span>
                                            <button type="button" class="ui icon button mini compact" @click.stop="redirectTo(person.extension)" title="전화돌려주기">
                                                <i class="share icon"></i>
                                            </button>
                                        </div>
                                        <div>
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

    <%--TODO: 채팅방 리스트 + 채팅방을 Vue 컴포넌트로 분리 --%>
<jsp:include page="/WEB-INF/jsp/messenger.jsp"/>

</aside>

<tags:scripts>
    <script>
        function toggleFold(event, target) {
            event.stopPropagation()
            $(target).toggleClass('active')
            $(target).find('.arrow:first').text($(target).hasClass('active') ? 'keyboard_arrow_down' : 'keyboard_arrow_right')
        }

        function toggleActive(event, target) {
            event.stopPropagation()
            $(target).toggleClass('active')
        }

        const teamList = Vue.createApp({
            data: function () {
                return {
                    teams: []
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
            },
            updated: function () {
                updatePersonStatus()
            },
            mounted: function () {
                this.load()
            },
        }).mount('#bookmark-list')

        function popupBookmarkModal() {
            popupReceivedHtml('/modal-messenger-bookmark', 'modal-messenger-bookmark').done(function () {
                window.doneSubmitBookmarkFormData = function () {
                    $('#modal-messenger-bookmark').find('.modal-close:first').click()
                    bookmarkList.load()
                }
            })
        }
    </script>
</tags:scripts>
