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

<div class="ui bottom attached tab segment remove-margin remove-padding border-top-none" data-tab="talk-panel">
    <div class="display-flex flex-flow-row full-height">
        <div class="top-chat-list-wrap display-flex" id="talk-list-container">
            <div>
                <div class="ui top attached tabular menu light flex">
                    <template v-for="(e, i) in STATUSES" :key="i">
                        <button class="item" :class="(statuses[e.status].activated && ' active ') + (statuses[e.status].newMessages && ' newImg_c ')" @click="activeTab(e.status)"
                                <c:if test="${!g.user.admin()}">v-if="e.status !== 'REALLOCATION' && e.status !== 'END'" </c:if>
                                <c:if test="${g.user.admin()}">v-if="e.status !== 'END'"</c:if>>
                            <text>{{ e.text }} (<span>{{ statuses[e.status].rooms.length }}</span>)</text>
                        </button>
                    </template>
                </div>
            </div>
            <div class="flex-100">
                <div v-for="(e, i) in STATUSES" :key="i" class="ui bottom attached tab segment remove-margin remove-padding" :class="statuses[e.status].activated && 'active'">
                    <div class="sort-wrap">
                        <div class="ui form">
                            <div class="fields">
                                <div class="four wide field remove-pl">
                                    <select v-model="statuses[e.status].filter.type">
                                        <option v-for="(v, k) in SEARCH_TYPE" :key="k" :value="k">{{ v }}</option>
                                    </select>
                                </div>
                                <div class="nine wide field remove-padding">
                                    <div class="ui action input">
                                        <input type="text" v-model="statuses[e.status].filter.value"/>
                                        <button class="ui icon button" @click.stop="filter(e.status)">
                                            <i class="search icon"></i>
                                        </button>
                                    </div>
                                </div>
                                <div class="three wide field remove-pr">
                                    <select v-model="statuses[e.status].filter.ordering" @change="changeOrdering(e.status)">
                                        <option v-for="(v, k) in ORDERING_TYPE" :key="k" :value="k">{{ v }}</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="talk-list-container overflow-auto" ref="talk-list-wrap">
                        <ul v-if="statuses[e.status].rooms.length">
                            <li v-for="(room, j) in statuses[e.status].rooms" :key="j" class="item" @click="openRoom(room.roomId, room.userName)">
                                <div class="header">
                                    <div class="left">
                                        <div class="profile-image">
                                            <div v-if="e.status === 'REALLOCATION'" class="ui checkbox" @click.stop>
                                                <input type="checkbox" name="reallocating" multiple :value="room.roomId"/>
                                                <label></label>
                                            </div>
                                            <img v-else :src="getImage(room.roomId)">
                                        </div>
                                        <div class="profile-txt">
                                            <div class="label">{{ room.svcName }}</div>
                                            <div class="customer">{{ room.maindbCustomName ? room.maindbCustomName : '미등록고객' }}</div>
                                        </div>
                                    </div>
                                    <div class="right">
                                        <div class="user-name">상담원 : {{ room.userName }}</div>
                                        <div class="last-time">{{ timestampFormat(room.roomLastTime) }}</div>
                                    </div>
                                </div>
                                <div class="content">
                                    <div class="inner">
                                        <span v-if="!activatedRoomIds.includes(room.roomId) && room.hasNewMessage" class="ui mini brand label circular"> N </span>
                                        <i v-if="room.type !== 'text'">
                                            {{ room.type === 'photo' ? '사진' : room.type === 'audio' ? '음원' : room.type === 'file' ? '파일' : room.type }}
                                            {{ room.send_receive === 'R' ? '전송됨' : '전달 받음' }}
                                        </i>
                                        <text v-else>{{ room.content }}</text>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>

                <button v-if="statuses.REALLOCATION.rooms.length && statuses.REALLOCATION.activated"
                        class="ui button mini compact blue" @click.stop.prevent="showReallocationModal" style="position: absolute; bottom: 2em; right: 2em; z-index: 1;">
                    재분배
                </button>
            </div>

            <div class="ui modal inverted tiny" ref="reallocationModal" style="display: none">
                <i class="close icon"></i>
                <div class="header">재분배</div>
                <div class="content rows scrolling">
                    <div class="ui grid">
                        <div class="row">
                            <div class="four wide column"><label class="control-label">상담사선택</label></div>
                            <div class="twelve wide column">
                                <div class="jp-multiselect">
                                    <select class="form-control" name="persons" size="8" multiple="multiple">
                                        <option v-for="(e, i) in persons" :value="e.id">{{ e.idName }} [{{ e.extension }}]</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="actions">
                    <button type="button" class="ui button modal-close">취소</button>
                    <button type="button" class="ui blue button" @click.stop.prevent="reallocate">재분배</button>
                </div>
            </div>
        </div>

        <tags:scripts>
            <script>
                const talkListContainer = Vue.createApp({
                    setup: function () {
                        return {
                            STATUSES: Object.freeze([{status: 'MY', text: '상담중'}, {status: 'TOT', text: '비접수'}, {status: 'OTH', text: '타상담'}, {status: 'END', text: '종료'},
                                {status: 'REALLOCATION', text: '재분배'},]),
                            SEARCH_TYPE: Object.freeze({CUSTOM_NAME: '고객명', USER_NAME: '상담원명'}),
                            ORDERING_TYPE: Object.freeze({LAST_MESSAGE_TIME: '최근시간', CUSTOM_NAME: '고객명', USER_NAME: '상담원명'}),
                        }
                    },
                    data: function () {
                        return {
                            statuses: this.STATUSES.reduce((o, e) => {
                                o[e.status] = {...e, activated: false, newMessages: 0, rooms: [], filter: {type: 'CUSTOM_NAME', value: '', ordering: 'LAST_MESSAGE_TIME'}}
                                return o
                            }, {}),
                            roomMap: {},
                            activatedRoomIds: [],
                            persons: [],
                        }
                    },
                    methods: {
                        isReallocationStatus(status) {
                            return status !== this.statuses.END.status
                        },
                        load: function () {
                            const _this = this
                            return restSelf.get('/api/counsel/current-talk-list').done(function (response) {
                                _this.roomMap = {}
                                _this.STATUSES.forEach(e => _this.statuses[e.status].rooms = [])
                                response.data.forEach(function (e) {
                                    const status = e.roomStatus === 'E' ? _this.statuses.END.status
                                        : !e.userId ? _this.statuses.TOT.status
                                            : e.userId === userId ? _this.statuses.MY.status
                                                : _this.statuses.OTH.status

                                    talkRoomList.forEach(room => {
                                        room.roomStatus = e.roomStatus
                                        room.userId = e.userId
                                    })

                                    const appendNewRoom = () => {
                                        e.container = _this.statuses[status]
                                        e.container.rooms.push(e)
                                        if (_this.isReallocationStatus(status)) _this.statuses.REALLOCATION.rooms.push(e)
                                        _this.roomMap[e.roomId] = e
                                    }

                                    if (_this.roomMap[e.roomId]) {
                                        for (let i = 0; i < _this.roomMap[e.roomId].container.rooms.length; i++) {
                                            if (_this.roomMap[e.roomId].container.rooms[i].roomId === e.roomId) {
                                                if (status === _this.roomMap[e.roomId].container.status) {
                                                    Object.assign(_this.roomMap[e.roomId].container.rooms[i], e)
                                                } else {
                                                    _this.roomMap[e.roomId].container.rooms.splice(i, 1)
                                                    for (let j = 0; j < _this.statuses.REALLOCATION.rooms.length; j++) {
                                                        if (_this.statuses.REALLOCATION.rooms[j].roomId === e.roomId) {
                                                            _this.statuses.REALLOCATION.rooms.splice(j, 1)
                                                            break
                                                        }
                                                    }
                                                    delete _this.roomMap[e.roomId]
                                                    appendNewRoom()
                                                }
                                                break
                                            }
                                        }
                                    } else {
                                        appendNewRoom()
                                    }
                                })
                                _this.STATUSES.forEach(e => _this.filter(e.status))
                            })
                        },
                        removeRoom: function (roomId) {
                            if (!this.roomMap[roomId])
                                return

                            for (let i = 0; i < this.roomMap[roomId].container.rooms.length; i++) {
                                if (this.roomMap[roomId].container.rooms[i].roomId === roomId) {
                                    this.roomMap[roomId].container.rooms.splice(i, 1)
                                }
                            }

                            try {
                                delete this.roomMap[roomId]
                            } catch (ignored) {
                            }
                        },
                        filter: function (status) {
                            this.changeOrdering(status)

                            const condition = this.statuses[status].filter
                            const value = condition.value.trim()
                            const _this = this
                            this.statuses[status].rooms.forEach(function (e) {
                                e.showing = !value
                                    || (condition.type === _this.SEARCH_TYPE.CUSTOM_NAME && e.maindbCustomName && e.maindbCustomName.includes(value))
                                    || (condition.type === _this.SEARCH_TYPE.USER_NAME && e.userName && e.userName.includes(value))
                            })
                        },
                        changeOrdering: function (status) {
                            const _this = this
                            const condition = this.statuses[status].filter
                            this.statuses[status].rooms.sort(function (a, b) {
                                if (condition.ordering === _this.ORDERING_TYPE.CUSTOM_NAME) return a.maindbCustomName - b.maindbCustomName
                                if (condition.ordering === _this.ORDERING_TYPE.USER_NAME) return a.userName - b.userName
                                return a.roomLastTime - b.roomLastTime
                            })
                        },
                        updateRoom: function (roomId, messageType, content, messageTime) {
                            if (!this.roomMap[roomId])
                                return this.load()

                            this.roomMap[roomId].content = content
                            this.roomMap[roomId].roomLastTime = messageTime
                            this.roomMap[roomId].type = messageType

                            this.changeOrdering(this.roomMap[roomId].container.status)
                            if (this.isReallocationStatus(this.roomMap[roomId].container.status)) this.changeOrdering(this.statuses.REALLOCATION.status)
                        },
                        updateRoomUser: function (roomId, status, userKey, userId) {
                            if (!this.roomMap[roomId])
                                return this.load()

                            for (let i = 0; i < this.roomMap[roomId].container.rooms.length; i++) {
                                if (this.roomMap[roomId].container.rooms[i].roomId === roomId) {
                                    this.roomMap[roomId].container.rooms.splice(i, 1)
                                    break
                                }
                            }

                            this.roomMap[roomId].status = status
                            this.roomMap[roomId].userKey = userKey
                            this.roomMap[roomId].userId = userId
                            this.roomMap[roomId].userName = this.persons.filter(person => person.id === userId)[0]?.idName
                            this.roomMap[roomId].container = this.statuses[status]
                            this.statuses[status].rooms.push(this.roomMap[roomId])

                            this.changeOrdering(status)
                            if (this.isReallocationStatus(status)) this.changeOrdering(this.statuses.REALLOCATION.status)
                        },
                        updateRoomStatus: function (roomId, status, messageType, content, messageTime) {
                            if (!this.roomMap[roomId])
                                return this.load()

                            for (let i = 0; i < this.roomMap[roomId].container.rooms.length; i++) {
                                if (this.roomMap[roomId].container.rooms[i].roomId === roomId) {
                                    this.roomMap[roomId].container.rooms.splice(i, 1)
                                    break
                                }
                            }

                            if (messageTime) {
                                this.roomMap[roomId].content = content
                                this.roomMap[roomId].roomLastTime = messageTime
                                this.roomMap[roomId].type = messageType
                            }
                            this.roomMap[roomId].status = status
                            this.roomMap[roomId].container = this.statuses[status]
                            this.statuses[status].rooms.push(this.roomMap[roomId])

                            this.changeOrdering(status)
                            if (this.isReallocationStatus(status)) this.changeOrdering(this.statuses.REALLOCATION.status)
                        },
                        getImage: function (userName) {
                            return profileImageSources[Math.abs(userName.hashCode()) % profileImageSources.length]
                        },
                        activeTab: function (status) {
                            this.STATUSES.forEach(e => this.statuses[e.status].activated = e.status === status)
                        },
                        loadActivatedRoomIds: function () {
                            this.activatedRoomIds = talkRoomList.map(e => e.roomId)
                        },
                        timestampFormat: function (e) {
                            return moment(e).format('MM-DD HH:mm')
                        },
                        openRoom: function (roomId, userName) {
                            talkRoom.loadRoom(roomId, userName).done(() => this.loadActivatedRoomIds())
                        },
                        reallocate() {
                            $(this.$el.parentElement).asJsonData().done(data => {
                                if (!data?.reallocating?.length || !data?.persons?.[0]?.length)
                                    return $(this.$refs.reallocationModal).modalHide()

                                talkCommunicator.redistribution(
                                    data.reallocating.map(e => ({channel_type: this.roomMap[e].channelType, room_id: e, user_key: this.roomMap[e].userKey,})),
                                    data.persons[0],
                                )

                                $(this.$refs.reallocationModal).modalHide()
                            })
                        },
                        showReallocationModal() {
                            $(this.$refs.reallocationModal).dragModalShow(this.$el.parentElement)
                            const options = this.$refs.reallocationModal.querySelector('[name="persons"]').options
                            for (let i = 0; i < options.length; i++) options[i].selected = false
                        },
                        loadPersons() {
                            const _this = this
                            restSelf.get('/api/monit/').done(response => _this.persons = response.data.map(team => team.person).flatMap(persons => persons))
                        }
                    },
                    updated: function () {
                        $(this.$refs['talk-list-wrap']).overlayScrollbars({})
                    },
                    mounted: function () {
                        this.activeTab(this.STATUSES[0].status)
                        this.load()
                        this.loadPersons()
                    },
                }).mount('#talk-list-container')
            </script>
        </tags:scripts>

        <div class="btm-room-wrap">
            <div class="chat-container" id="talk-room">
                <div class="room" style="position: relative" @drop.stop="dropFiles" @dragover.prevent @click.stop="showingTemplateLevel = 0" @mouseup="mouseup">
                    <div class="chat-header">[{{ roomStatus }}]-{{ roomName }}</div>
                    <%--<div class="chat-search-btn-wrap">
                        <button @click.stop.prevent="showingSearchbar = true" class="chat-search-btn">
                            <img src="<c:url value="/resources/images/chat-search-icon.svg"/>">
                        </button>
                    </div>--%>
                    <%--<div v-if="showingSearchbar" class="chat-search-wrap active">
                        <div class="chat-search-control">
                            <div class="control-inner">
                                <input type="text" v-model="searchingText">
                                <button @click.stop.prevent="searchText" class="keyword-search-btn"><img src="<c:url value="/resources/images/chat-search-grey-icon.svg"/>"></button>
                                <div class="keyword-count">
                                    <text class="-text-count">{{ searchingTexts.length && (searchingTextIndex + 1) || 0 }} / {{ searchingTexts.length || 0 }}</text>
                                </div>
                                <button @click.stop.prevent="moveToPreviousText"><img src="<c:url value="/resources/images/chat-arrow-up-icon.svg"/>" alt="이전 검색단어"></button>
                                <button @click.stop.prevent="moveToNextText"><img src="<c:url value="/resources/images/chat-arrow-down-icon.svg"/>" alt="다음 검색단어"></button>
                            </div>
                        </div>
                        <button @click.stop.prevent="showingSearchbar = false" class="chat-search-close-btn">
                            <img src="<c:url value="/resources/images/chat-find-close-icon.svg"/>">
                        </button>
                    </div>--%>

                    <div class="chat-body" ref="chatBody" @scroll="loadAdditionalMessagesIfTop" style="overflow-y: scroll; scroll-behavior: smooth;">

                        <div v-if="showingDropzone" class="attach-overlay">
                            <img src="<c:url value="/resources/images/attach-plus-icon.svg"/>">
                            <p>채팅방에 파일을 바로 업로드하려면 여기에 드롭하세요.</p>
                        </div>

                        <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                            <div v-if="'SB' === e.sendReceive || e.messageType === 'block_temp'" class="chat-item" :class="e.messageType === 'block_temp' && ' chat-me '">
                                <div class="wrap-content">
                                    <div v-if="e.messageType !== 'block_temp'" class="profile-img">
                                        <img :src="getImage(roomId)">
                                    </div>
                                    <div class="txt-segment">
                                        <div class="txt-time">[{{ e.messageType === 'block_temp' ? (e.username || e.userId) : customName }}] {{ getTimeFormat(e.time) }}</div>
                                        <div v-if="!e.displays && !e.buttonGroups" class="chat bot">
                                            <div class="bubble" style="background-color: transparent; text-align: center; display: block; box-shadow: none;">
                                                <img src="<c:url value="/resources/images/loading.svg"/>" alt="loading"/>
                                            </div>
                                        </div>
                                        <div v-for="(display, j) in e.displays" class="chat bot">
                                            <div class="bubble">
                                                <div v-if="display.type === 'text'" class="txt-chat">
                                                    <p>{{ display.elementList[0]?.content }}</p>
                                                </div>
                                                <div v-else class="card">
                                                    <div v-if="display.type === 'image'" class="card-img">
                                                        <img :src="'${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=' + encodeURIComponent(display.elementList[0]?.image)">
                                                    </div>
                                                    <template v-if="display.type === 'card'">
                                                        <div class="card-img">
                                                            <img :src="'${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=' + encodeURIComponent(display.elementList[0]?.image)">
                                                        </div>
                                                        <div class="card-content">
                                                            <div class="card-title">{{ display.elementList[0]?.title }}</div>
                                                            <div class="card-text" style="white-space: pre-wrap;">{{ display.elementList[0]?.content }}</div>
                                                        </div>
                                                    </template>
                                                    <div v-if="display.type === 'list'" class="card-list">
                                                        <div class="card-list-title">
                                                            <a v-if="display.elementList[0]?.url" :href="display.elementList[0]?.url" target="_blank">{{ display.elementList[0]?.title }}</a>
                                                            <text v-else>{{ display.elementList[0]?.title }}</text>
                                                        </div>
                                                        <ul v-if="display.elementList?.length > 1" class="card-list-ul">
                                                            <li v-for="(e2, k) in getListElements(display)" class="item">
                                                                <a :href="e2.url" target="_blank" class="link-wrap">
                                                                    <div class="item-thumb">
                                                                        <div class="item-thumb-inner">
                                                                            <img v-if="e2.image"
                                                                                 :src="'${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=' + encodeURIComponent(e2.image)">
                                                                        </div>
                                                                    </div>
                                                                    <div class="item-content">
                                                                        <div class="subject">{{ e2.title }}</div>
                                                                        <div class="ment" style="white-space: pre-wrap;">{{ e2.content }}</div>
                                                                    </div>
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div v-for="(buttonGroup, j) in e.buttonGroups" class="chat bot">
                                            <div class="bubble">
                                                <div v-if="buttonGroup instanceof Array" class="button-inner">
                                                    <button v-for="(e2, j) in buttonGroup" type="button" class="chatbot-button">{{ e2.name }}</button>
                                                </div>
                                                <div v-else class="card">
                                                    <div class="card-list">
                                                        <ul class="card-list-ul">
                                                            <li v-for="(param, k) in buttonGroup.paramList" class="item form">
                                                                <div class="label">{{ param.displayName }}</div>
                                                                <div v-if="param.type !== 'api'" class="ui fluid input"><input type="text"></div>
                                                                <div v-else class="ui multi form">
                                                                    <select class="slt">
                                                                        <option>오전</option>
                                                                        <option>오후</option>
                                                                    </select>
                                                                    <select class="slt">
                                                                        <option v-for="hour in 12" :key="hour" :value="hour - 1">{{ hour - 1 }}</option>
                                                                    </select>
                                                                    <span class="unit">시</span>
                                                                    <select class="slt">
                                                                        <option v-for="minute in 60" :key="minute" :value="minute - 1">{{ minute - 1 }}</option>
                                                                    </select>
                                                                    <span class="unit">분</span>
                                                                </div>
                                                            </li>
                                                            <li class="item">
                                                                <div class="button-inner">
                                                                    <button type="button" class="chatbot-button">{{ buttonGroup.name }}</button>
                                                                </div>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-if="'RB' === e.sendReceive && e.messageType === 'text'" class="chat-item">
                                <div class="wrap-content">
                                    <div class="profile-img">
                                        <img :src="getImage(roomId)">
                                    </div>
                                    <div class="txt-segment">
                                        <div class="txt-time">[{{ customName }}] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="txt-chat">
                                                    <p>{{ e.contents }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-if="'SA' === e.sendReceive" class="chat-item chat-me">
                                <div class="wrap-content">
                                    <div class="txt-segment">
                                        <div class="txt-time">[오토멘트] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="txt-chat">
                                                    <p>{{ e.contents }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType !== 'info' && e.messageType !== 'block_temp'" class="chat-item"
                                 :class="['AF', 'S'].includes(e.sendReceive)<%-- && e.userId === ME--%> && 'chat-me'">
                                <div class="wrap-content">
                                    <div v-if="!['AF', 'S'].includes(e.sendReceive)" class="profile-img">
                                        <img :src="getImage(['AF', 'S'].includes(e.sendReceive) ? e.username : roomId)">
                                    </div>
                                    <div class="txt-segment">
                                        <div class="txt-time">
                                            <text v-if="!['AF', 'S'].includes(e.sendReceive) || e.username || e.userId">
                                                [{{ ['AF', 'S'].includes(e.sendReceive) ? (e.username || e.userId) : customName }}]
                                            </text>
                                            {{ getTimeFormat(e.time) }}
                                        </div>
                                        <div class="chat">
                                            <div class="bubble" @mouseup="mouseup">
                                                <div class="chat-more-nav">
                                                    <button @click="replying = e" class="nav-chat-reply-btn">
                                                        <img src="<c:url value="/resources/images/chat-reply-icon.svg"/>">답장
                                                    </button>
                                                    <button @click="popupTemplateModal(e)" class="nav-template-btn">
                                                        <img src="<c:url value="/resources/images/chat-more-template-icon.svg"/>">템플릿
                                                    </button>
                                                    <button @click="popupTaskScriptModal(e)" class="nav-knowhow-btn">
                                                        <img src="<c:url value="/resources/images/chat-more-knowledge-icon.svg"/>">지식관리
                                                    </button>
                                                    <button @click="popupSideViewRoomModal" class="nav-dual-btn">
                                                        <img src="<c:url value="/resources/images/chat-more-dual-icon.svg"/>">듀얼창
                                                    </button>
                                                </div>

                                                <div class="txt-chat">
                                                    <img v-if="e.messageType === 'photo'" :src="e.fileUrl" class="cursor-pointer" @click="popupImageView(e.fileUrl)">
                                                    <img v-else-if="e.messageType === 'image_temp'" :src="e.fileUrl" class="cursor-pointer" @click="popupImageView(e.fileUrl)">
                                                    <audio v-else-if="e.messageType === 'audio'" controls :src="e.fileUrl"></audio>
                                                    <a v-else-if="e.messageType === 'file'" target="_blank" :href="e.fileUrl">{{ e.contents }}</a>
                                                    <template v-else-if="e.messageType === 'text'">
                                                        <div v-if="e.replyingType" class="reply-content-container">
                                                            <div v-if="e.replyingType === 'image'" class="reply-content photo">
                                                                <img :src="e.replyingTarget">
                                                            </div>
                                                            <div v-if="e.replyingType === 'image_temp'" class="reply-content photo">
                                                                <img :src="e.replyingTarget">
                                                            </div>
                                                            <div class="reply-content">
                                                                <div class="target-msg">
                                                                    <template v-if="e.replyingType === 'text'">{{ e.replyingTarget }}</template>
                                                                    <a v-else :href="e.replyingTarget" target="_blank">{{ e.replyingType === 'image' ? '사진' : '파일' }}</a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <p>{{ e.contents }}</p>
                                                    </template>
                                                    <p v-else>{{ e.contents }}</p>
                                                </div>
                                                <button @click.stop.prevent="replying = e" class="chat-reply-btn"><img src="<c:url value="/resources/images/chat-reply-icon.svg"/>"></button>
                                            </div>
                                        </div>
                                        <a v-if="['file','photo','audio','image_temp'].includes(e.messageType)" target="_blank" :href="e.fileUrl"><i class="file icon"></i>저장하기</a>
                                    </div>
                                </div>
                            </div>
                            <p v-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType === 'info'" class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                            <p v-if="['RM', 'SZ', 'SG'].includes(e.sendReceive)" class="info-msg">
                                [{{ getTimeFormat(e.time) }}]
                                <text v-if="e.sendReceive === 'RM'">상담사연결을 요청하였습니다.</text>
                                <text v-if="e.sendReceive === 'SZ'">[{{ e.username }}] 상담사가 상담을 찜했습니다.</text>
                                <text v-if="e.sendReceive === 'SG'">[{{ e.username }}] 상담사가 상담을 가져왔습니다.</text>
                            </p>
                            <p v-if="['SE', 'RE', 'AE', 'E'].includes(e.sendReceive) && e.contents" class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                        </div>
                    </div>

                    <div class="write-chat">
                        <div v-if="replying !== null" class="view-to-reply" style="display: flex">
                            <div v-if="replying.messageType === 'photo'" class="target-image">
                                <img :src="replying.fileUrl" class="target-image-content">
                            </div>
                            <div v-if="replying.messageType === 'image_temp'" class="target-image">
                                <img :src="replying.fileUrl" class="target-image-content">
                            </div>
                            <div class="target-text">
                                <p class="target-user">{{ replying.username || customName }}에게 답장</p>
                                <p class="target-content">{{ replying.contents }}</p>
                            </div>
                            <div class="target-close" @click="replying=null">
                                <img src="<c:url value="/resources/images/chat-reply-close-icon.svg"/>">
                            </div>
                        </div>

                        <div v-if="showingTemplateBlocks" class="template-container template-block-container">
                            <div class="template-container-inner">
                                <ul class="template-ul">
                                    <li v-for="(e, i) in templateBlocks" :key="i" @click.stop="sendTemplateBlock(e.blockId)" class="template-list">
                                        <div class="template-title">/{{ e.name }}</div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div v-if="showingTemplateLevel" class="template-container">
                            <div class="template-container-inner">
                                <ul class="template-ul">
                                    <li v-for="(e, i) in getTemplates()" :key="i" :class="i === activatingTemplateIndex && 'active'" @click.stop="sendTemplate(e)" class="template-list">
                                        <div class="template-title">/{{ e.name }}</div>
                                        <img v-if="e.isImage" :src="e.url" class="template-image" :alt="e.fileName"/>
                                        <div v-if="e.isImage" class="template-content" style="text-decoration: underline">{{ e.fileName }}</div>
                                        <div v-else class="template-content">{{ e.text }}</div>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="wrap-inp">
                            <div class="inp-box">
                                <textarea placeholder="전송하실 메시지를 입력하세요." ref="message" @paste.prevent="pasteFromClipboard" @keyup.stop="keyup"></textarea>
                            </div>
                            <button type="button" class="send-btn" @click="sendMessage()">전송</button>
                        </div>

                        <div class="write-menu">
                            <div>
                                <button @click.stop.prevent="showingTemplateBlocks = true" data-inverted data-tooltip="템플릿" data-position="top center">
                                    <img src="<c:url value="/resources/images/chat-template-import-icon.svg"/>"></button>
                                <input style="display: none" type="file" @change="sendFile">
                                <button type="button" data-inverted data-tooltip="파일첨부" data-position="top center" onclick="this.previousElementSibling.click()">
                                    <img src="<c:url value="/resources/images/chat-file-icon.svg"/>"></button>

                                <button v-if="roomStatus === 'E'" data-inverted data-tooltip="대화방 내리기" data-position="top center" @click.stop.prevent="deleteRoom"
                                        :style="'visibility:'+(roomId?'visible':'hidden')"><img src="<c:url value="/resources/images/chat-down-icon.svg"/>"></button>
                                <button v-else-if="!userId" data-inverted data-tooltip="찜하기" data-position="top center" @click.stop.prevent="assignUnassignedRoomToMe"
                                        :style="'visibility:'+(roomId?'visible':'hidden')"><img src="<c:url value="/resources/images/chat-reservation-icon.svg"/>"></button>
                                <button v-else-if="userId !== ME" data-inverted data-tooltip="가져오기" data-position="top center" @click.stop.prevent="assignAssignedRoomToMe"
                                        :style="'visibility:'+(roomId?'visible':'hidden')"><img src="<c:url value="/resources/images/chat-import-icon.svg"/>"></button>
                                <button v-else data-inverted data-tooltip="대화종료" data-position="top right" @click.stop.prevent="finishCounsel"
                                        :style="'visibility:'+(roomId?'visible':'hidden')"><img src="<c:url value="/resources/images/chat-exit-icon.svg"/>"></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="talk-dual-popup" class="ui modal">
    <div class="header"></div>
    <div class="content">
        <div class="chat-container"></div>
    </div>
</div>

<jsp:include page="/counsel/talk/modal-template"/>

<tags:scripts>
    <script>
        (function makeSideViewRoomElement() {
            const talkRoomElement = document.getElementById('talk-room')
            const sideViewModalElement = document.getElementById('talk-dual-popup')
            const header = sideViewModalElement.querySelector('.header')
            const contentBody = sideViewModalElement.querySelector('.chat-container')
            for (let i = 0; i < talkRoomElement.children.length; i++)
                contentBody.append(talkRoomElement.children[i].cloneNode(true))

            header.append(contentBody.querySelector('.chat-header'))

            const closeButton = document.createElement('i')
            closeButton.className = 'close icon'
            closeButton.setAttribute('onclick', 'sideViewModal.closeModal()')
            sideViewModalElement.append(closeButton)
        })()

        const talkRoomProperties = {
            setup: function () {
                return {
                    ME: userId,
                    REPLYING_INDICATOR: '\u001b',
                    REPLYING_TEXT: '\u001a',
                    REPLYING_IMAGE: '\u000f',
                    REPLYING_FILE: '\u000e',
                    REPLYING_IMAGE_TEMP: '\u000d',
                }
            },
            data: function () {
                return {
                    roomId: null,
                    channelType: null,
                    userKey: null,
                    senderKey: null,
                    roomName: null,
                    roomStatus: null,
                    userId: null,
                    userName: null,
                    customName: null,

                    showingDropzone: false,

                    showingTemplateLevel: 0,
                    showingTemplateFilter: '',
                    activatingTemplateIndex: null,
                    templates: [],

                    showingTemplateBlocks: false,
                    templateBlocks: [],

                    replying: null,
                    bodyScrollingTimer: null,

                    messageList: [],
                }
            },
            methods: {
                loadRoom: function (roomId, userName) {
                    const _this = this
                    return restSelf.get('/api/counsel/current-talk-msg/' + roomId).done(function (response) {
                        _this.roomId = roomId
                        _this.userName = userName

                        _this.channelType = response.data.channelType
                        _this.userKey = response.data.userKey
                        _this.senderKey = response.data.senderKey
                        _this.roomName = response.data.roomName
                        _this.roomStatus = response.data.roomStatus
                        _this.userId = response.data.userId
                        _this.customName = response.data.customName

                        _this.messageList = []
                        response.data.talkMsgSummaryList.forEach(function (e) {
                            _this.appendMessage({
                                roomId: roomId,
                                time: e.insertTime,
                                messageType: e.type,
                                sendReceive: e.sendReceive,
                                contents: e.content,
                                userId: e.userId,
                                username: e.userName,
                            })
                        })
                        _this.scrollToBottom()
                    })
                },
                popupImageView: function (url) {
                    popupImageView(url)
                },
                getTimeFormat: function (time) {
                    return moment(time).format('MM-DD HH:mm')
                },
                appendMessage: function (message) {
                    if (this.roomId !== message.roomId)
                        return

                    const _this = this

                    console.log(message)

                    const setBlockInfo = response => {
                        message.displays = response.data.displayList?.sort((a, b) => (a.order - b.order))
                        message.buttonGroups = (() => {
                            return response.data.buttonList?.sort((a, b) => (a.order - b.order)).reduce((list, e) => {
                                if (e.action === 'api') list.push(e)
                                else if (!list.length || !(list[list.length - 1] instanceof Array)) list.push([e])
                                else list[list.length - 1].push(e)
                                return list
                            }, [])
                        })()
                        _this.$forceUpdate()
                    }

                    if (message.sendReceive === 'SB') {
                        try {
                            const result = message.contents.match(/^BOT:(\d+)-BLK:(\d+)$/)
                            restSelf.get('/api/chatbot/blocks/' + result[2], null, null, true).done(setBlockInfo)
                        } catch (e) {
                            console.error(e)
                        }
                    }

                    // TODO: 항상 모든 파일의 messageType이 file로 전달된다. => photo, audio로 분류해야 한다.
                    if (message.messageType === 'file') {
                        const isImage = (fileName) => {
                            if (!fileName) return false
                            return fileName.toLowerCase().endsWith('.jpg')
                                || fileName.toLowerCase().endsWith('.jpeg')
                                || fileName.toLowerCase().endsWith('.png')
                                || fileName.toLowerCase().endsWith('.bmp')
                        }
                        if (isImage(message.contents)) message.messageType = 'photo'
                    }

                    if (message.messageType === 'block_temp') {
                        const block = this.templateBlocks.filter(e => e.blockId === parseInt(message.contents))[0]
                        block && restSelf.get('/api/chatbot/blocks/' + block.blockId, null, null, true).done(setBlockInfo)
                    } else if (message.messageType === 'image_temp') {
                        message.originalFileUrl = message.contents
                        message.fileUrl = $.addQueryString(talkCommunicator.url + '/webchat_bot_image_fetch', {company_id: talkCommunicator.request.companyId, file_name: message.contents, channel_type: _this.channelType})
                    } else if (['file', 'photo', 'audio'].includes(message.messageType)) {
                        message.originalFileUrl = message.contents
                        message.fileUrl = $.addQueryString(talkCommunicator.url + '/webchat_bot_image_fetch', {company_id: talkCommunicator.request.companyId, file_name: message.contents, channel_type: _this.channelType})
                    } else if (this.REPLYING_INDICATOR === message.contents.charAt(0)) {
                        [message.contents.indexOf(this.REPLYING_TEXT), message.contents.indexOf(this.REPLYING_IMAGE), message.contents.indexOf(this.REPLYING_FILE), message.contents.indexOf(this.REPLYING_IMAGE_TEMP)].forEach((indicator, i) => {
                            if (indicator < 0) return
                            message.replyingType = i === 0 ? 'text' : i === 1 ? 'image' : i === 2 ? 'file' : 'image_temp'

                            const replyingTarget = message.contents.substr(1, indicator - 1)
                            if (message.replyingType === 'text') {
                                message.replyingTarget = replyingTarget
                            } else if (message.replyingType === 'image_temp') {
                                message.replyingTarget = $.addQueryString(talkCommunicator.url + '/webchat_bot_image_fetch', {
                                    company_id: talkCommunicator.request.companyId,
                                    file_name: replyingTarget,
                                    channel_type: _this.channelType
                                })
                            } else {
                                message.replyingTarget = $.addQueryString(talkCommunicator.url + '/webchat_bot_image_fetch', {
                                    company_id: talkCommunicator.request.companyId,
                                    file_name: replyingTarget,
                                    channel_type: _this.channelType
                                })
                            }

                            message.contents = message.contents.substr(indicator + 1)
                        })
                    }

                    if (this.messageList.length === 0)
                        return this.messageList.push(message)

                    if (this.messageList[0].time >= message.time)
                        return this.messageList.splice(0, 0, message)

                    if (this.messageList[this.messageList.length - 1].time <= message.time) {
                        this.scrollToBottom()
                        return this.messageList.push(message)
                    }

                    this.messageList.push(message)
                    this.messageList.sort(function (a, b) {
                        return a.time - b.time
                    })
                },
                getListElements(display) {
                    return JSON.parse(JSON.stringify(display)).elementList?.sort((a, b) => (a.order - b.order)).splice(1)
                },
                scrollTo: function (y) {
                    if (this.bodyScrollingTimer)
                        clearTimeout(this.bodyScrollingTimer)

                    const _this = this
                    this.$nextTick(function () {
                        _this.bodyScrollingTimer = setTimeout(function () {
                            _this.$refs.chatBody.scroll({top: y})
                        }, 100)
                    })
                },
                scrollToBottom: function () {
                    if (this.bodyScrollingTimer)
                        clearTimeout(this.bodyScrollingTimer)

                    const _this = this
                    this.$nextTick(function () {
                        _this.bodyScrollingTimer = setTimeout(function () {
                            _this.$refs.chatBody.scroll({top: _this.$refs.chatBody.scrollHeight})
                        }, 100)
                    })
                },
                getImage: function (userName) {
                    return profileImageSources[Math.abs(userName.hashCode()) % profileImageSources.length]
                },
                sendMessage: function (message) {
                    if (!message) message = this.$refs.message.value
                    if (!message || !this.roomStatus || this.roomStatus === 'E') return

                    if (this.replying) {
                        if (['file', 'audio'].includes(this.replying.messageType)) {
                            message = this.REPLYING_INDICATOR + this.replying.originalFileUrl + this.REPLYING_FILE + message
                        } else if (this.replying.messageType === 'photo') {
                            message = this.REPLYING_INDICATOR + this.replying.originalFileUrl + this.REPLYING_IMAGE + message
                        } else if (this.replying.messageType === 'image_temp') {
                            message = this.REPLYING_INDICATOR + (this.replying.originalFileUrl.substr(this.replying.originalFileUrl.lastIndexOf('/') + 1)) + this.REPLYING_IMAGE_TEMP + message
                        } else {
                            message = this.REPLYING_INDICATOR + this.replying.contents + this.REPLYING_TEXT + message
                        }
                    }

                    talkCommunicator.sendMessage(this.roomId, this.channelType, this.senderKey, this.userKey, message)
                    this.$refs.message.value = ''
                    this.showingTemplateLevel = 0
                    this.showingTemplateFilter = ''
                    this.replying = null
                    this.showingTemplateBlocks = false
                },
                uploadFile(file) {
                    return uploadFile(file)
                        .done(response => restSelf.post('/api/counsel/file', response.data)
                            .done(response => talkCommunicator.sendFile(this.roomId, this.channelType, this.senderKey, this.userKey, response.data)))
                },
                sendFile: function (event) {
                    const file = event.target.files[0]
                    event.target.value = null

                    if (!file || !file.name)
                        return

                    this.uploadFile(file)
                },
                dropFiles: function (event) {
                    this.showingDropzone = false

                    if (!event.dataTransfer)
                        return

                    for (let i = 0; i < event.dataTransfer.files.length; i++) {
                        this.uploadFile(event.dataTransfer.files[i])
                    }
                },
                pasteFromClipboard: function (event) {
                    let hasFile = false

                    for (let i = 0; i < event.clipboardData.items.length; i++) {
                        const item = event.clipboardData.items[i]
                        if (item.kind === 'file') {
                            hasFile = true
                            this.uploadFile(item.getAsFile())
                        }
                    }

                    if (!hasFile) this.$refs.message.value += event.clipboardData.getData('Text')
                },
                sendTemplate(template) {
                    if (template.isImage) {
                        talkCommunicator.sendImageTemplate(this.roomId, this.channelType, this.senderKey, this.userKey, template.filePath)
                        this.$refs.message.value = ''
                        this.showingTemplateLevel = 0
                        this.showingTemplateFilter = ''
                        this.replying = null
                        this.showingTemplateBlocks = false
                        return
                    }
                    this.sendMessage(template.text)
                },
                sendTemplateBlock(blockId) {
                    talkCommunicator.sendTemplateBlock(this.roomId, this.channelType, this.senderKey, this.userKey, blockId)
                    this.showingTemplateBlocks = false
                },
                getTemplates() {
                    const _this = this
                    return this.templates.filter(e => e.permissionLevel >= _this.showingTemplateLevel && e.name.includes(_this.showingTemplateFilter))
                },
                keyup: function (event) {
                    if (event.key === '/' && ['///', '//', '/'].includes(this.$refs.message.value)) {
                        if (this.getTemplates().length > 0) {
                            this.showingTemplateLevel = this.$refs.message.value === '///' ? 3 : this.$refs.message.value === '//' ? 2 : 1
                        } else {
                            this.showingTemplateLevel = 0
                        }
                        this.activatingTemplateIndex = null
                        return
                    }

                    if (event.key === 'Escape') {
                        this.showingTemplateLevel = 0
                        this.showingTemplateFilter = ''
                        this.replying = null
                        this.showingTemplateBlocks = false
                        return
                    }

                    if (this.showingTemplateLevel) {
                        const templates = this.getTemplates()
                        if (templates.length > 0 && event.key === 'ArrowDown') {
                            if (this.activatingTemplateIndex === null) return this.activatingTemplateIndex = 0
                            return this.activatingTemplateIndex = (this.activatingTemplateIndex + 1) % templates.length
                        }
                        if (templates.length > 0 && event.key === 'ArrowUp') {
                            if (this.activatingTemplateIndex === null) return this.activatingTemplateIndex = templates.length - 1
                            return this.activatingTemplateIndex = (this.activatingTemplateIndex - 1 + templates.length) % templates.length
                        }
                        if (templates[this.activatingTemplateIndex] && event.key === 'Enter') {
                            return this.sendTemplate(templates[this.activatingTemplateIndex])
                        }
                        this.showingTemplateFilter = this.$refs.message.value.substr(this.showingTemplateLevel).trim()
                        this.activatingTemplateIndex = 0
                    }

                    if (event.key === 'Enter') {
                        return this.sendMessage()
                    }
                },
                loadTemplates: function () {
                    const _this = this
                    restSelf.get('/api/talk-template/my/', null, false, null).done(function (response) {
                        _this.templates = []
                        response.data.forEach(function (e) {
                            _this.templates.push({
                                name: e.mentName,
                                permissionLevel: e.type === 'P' ? 3 : e.type === 'G' ? 2 : 1,
                                text: e.ment,
                                isImage: e.typeMent === 'P',
                                fileName: e.originalFileName,
                                filePath: e.filePath,
                                url: e.typeMent === 'P' ? $.addQueryString('${g.escapeQuote(apiServerUrl)}/api/v1/admin/talk/template/image', {
                                    filePath: e.filePath,
                                    token: '${g.escapeQuote(accessToken)}'
                                }) : null
                            })
                        })
                    })
                },
                loadTemplateBlocks: function () {
                    const _this = this
                    restSelf.get('/api/chatbot/blocks/template', null, false, null).done(function (response) {
                        _this.templateBlocks = []
                        response.data.forEach(function (e) {
                            _this.templateBlocks.push({botId: e.botId, blockId: e.blockId, name: e.botName + ' - ' + e.blockName})
                        })
                    })
                },
                popupTemplateModal: function (message, index) {
                    const _this = this
                    const modalId = 'modal-talk-template'
                    popupDraggableModalFromReceivedHtml('/admin/talk/template/new/modal', modalId).done(function () {
                        const modal = document.getElementById(modalId)
                        modal.querySelector('[name=typeMent]').value = 'TEXT'
                        modal.querySelector('[type=file]').value = null

                        const selectedTextContents = getSelectedTextContentOfSingleElement()
                        if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === _this.$refs['message-' + index].querySelector('.txt-chat p')) {
                            modal.querySelector('[name=ment]').value = selectedTextContents.text
                        } else if (message.fileType === 'image') {
                            modal.querySelector('[name=originalFileName]').value = message.fileName
                            modal.querySelector('[name=filePath]').value = message.originalFileUrl
                            modal.querySelector('.file-name').innerHTML = message.fileName
                            modal.querySelector('[name=typeMent]').value = 'PHOTO'
                        } else {
                            modal.querySelector('[name=ment]').value = message.contents
                        }
                        modal.querySelector('[name=typeMent]').dispatchEvent(new Event('change'))

                        const doneActionName = '_doneTemplatePost'
                        modal.setAttribute('data-done', doneActionName)
                        window[doneActionName] = function () {
                            modal.querySelector('.close.icon').click()
                            delete window[doneActionName]
                            _this.loadTemplates()
                        }
                    })
                },
                popupTaskScriptModal: function (message, index) {
                    let contents = message.contents
                    const selectedTextContents = getSelectedTextContentOfSingleElement()
                    if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === this.$refs['message-' + index].querySelector('.txt-chat p'))
                        contents = selectedTextContents.text

                    popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/modal-search?title=' + encodeURIComponent(contents), 'modal-search-task-script')
                },
                deleteRoom: function () {
                    const _this = this
                    restSelf.delete('/api/counsel/talk-remove-room/' + this.roomId, null, null).done(function () {
                        talkListContainer.removeRoom(_this.roomId)

                        _this.roomId = null
                        _this.showingTemplateLevel = 0
                        _this.replying = null
                        _this.messageList = []
                    })
                },
                assignUnassignedRoomToMe: function () {
                    talkCommunicator.assignUnassignedRoomToMe(this.roomId, this.channelType, this.senderKey, this.userKey)
                    // setTimeout(talkListContainer.load, 100)
                },
                assignAssignedRoomToMe: function () {
                    talkCommunicator.assignAssignedRoomToMe(this.roomId, this.channelType, this.senderKey, this.userKey)
                    // setTimeout(talkListContainer.load, 100)
                },
                finishCounsel: function () {
                    talkCommunicator.deleteRoom(this.roomId, this.channelType, this.senderKey, this.userKey)
                    // setTimeout(talkListContainer.load, 100)
                },
                popupSideViewRoomModal: function () {
                    if (!this.roomId)
                        return
                    sideViewModal.loadRoom(this.roomId, this.userName)
                },
                mouseup(event) {
                    if ($(event.target).closest('.chat-reply-btn').length) return

                    $('.chat-more-nav').hide()

                    if ($(event.target).closest('.chat-more-nav').length) return

                    event.stopImmediatePropagation()
                    $(event.target).closest('.chat-item').find('.chat-more-nav').css("display", "flex")
                }
            },
            mounted: function () {
                this.loadTemplates()
                this.loadTemplateBlocks()
            },
        }

        const talkRoom = Vue.createApp(Object.assign({}, talkRoomProperties)).mount('#talk-room')
        const sideViewModal = Vue.createApp(Object.assign({}, talkRoomProperties, {
            methods: {
                ...talkRoomProperties.methods,
                closeModal() {
                    this.roomId = null
                    this.messageList = []
                    talkListContainer.loadActivatedRoomIds()
                },
                loadRoom(roomId, userName) {
                    const _this = this
                    return talkRoomProperties.methods.loadRoom.apply(this, [roomId, userName]).done(() => {
                        talkListContainer.loadActivatedRoomIds()
                        $(_this.$el).closest('.modal')
                            .show()
                            .draggable({containment: '#main'})
                        _this.scrollToBottom()
                    })
                }
            },
        })).mount('#talk-dual-popup')
        const talkRoomList = [talkRoom, sideViewModal]
    </script>
</tags:scripts>

<tags:scripts>
    <script>
        function loadTalkCustomInput(maindbGroupSeq, customId, roomId, channelType, senderKey, userKey) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/talk/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                channelType: channelType || '',
                senderKey: senderKey || '',
                userKey: userKey || '',
            }), '#talk-custom-input');
        }

        function loadTalkCounselingInput(maindbGroupSeq, customId, roomId, channelType, senderKey, userKey) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/talk/counseling-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                channelType: channelType || '',
                senderKey: senderKey || '',
                userKey: userKey || '',
            }), '#talk-counseling-input');
        }

        function processTalkMessage(data) {
            if (!data.send_receive)
                return talkListContainer.updateRoomStatus(data.room_id, data.userid === userId ? 'MY' : 'OTH')

            const messageTime = moment().format('YYYY-MM-DD') + ' ' + data.cur_timestr.substring(data.cur_timestr.length - 8, data.cur_timestr.length)

            if (['SZ', 'SG'].includes(data.send_receive)) {
                talkListContainer.updateRoomStatus(data.room_id, data.userid === userId ? 'MY' : 'OTH', data.type, data.content, messageTime)
            } else if (['SE', 'RE'].includes(data.send_receive)) {
                talkListContainer.updateRoomStatus(data.room_id, 'END', data.type, data.content, messageTime)
            } else {
                talkListContainer.updateRoom(data.room_id, data.type, data.content, messageTime)
            }

            talkRoomList.forEach(e => e.appendMessage({
                roomId: data.room_id,
                time: messageTime,
                messageType: data.type,
                sendReceive: data.send_receive,
                contents: data.content,
                userId: data.send_receive === 'AF' || data.send_receive.startsWith('S') ? data.userid : null,
                username: data.send_receive === 'AF' || data.send_receive.startsWith('S') ? data.username : data.customname,
            }))
        }

        window.talkCommunicator = new TalkCommunicator()
            .on('svc_msg', processTalkMessage)
            .on('svc_control', processTalkMessage)
            .on('svc_redist', data => talkListContainer.updateRoomUser(data.room_id, data.userid === userId ? 'MY' : 'OTH', data.user_key, data.userid))
            .on('svc_end', processTalkMessage)
            .on('svc_login', data => $('#distributee').prop("checked", data.dist_yn === 'Y'))
            .on('svc_dist_yn', data => $('#distributee').prop("checked", data.dist_yn === 'Y'))

        $(window).on('load', function () {
            loadTalkCustomInput()
            $('#talk-dual-popup').dragModalShow().hide()
        })
    </script>
</tags:scripts>
