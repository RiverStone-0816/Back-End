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
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui column grid" id="talk-panel">
    <div class="nine wide column" id="talk-list-container">
        <div class="talk-list-container">
            <div class="ui top attached tabular menu">
                <a v-for="(e, i) in STATUSES" :key="i" class="item" :class="(statuses[e.status].activated && ' active ') + (statuses[e.status].newMessages && ' newImg_c ')"
                   @click="activeTab(e.status)">
                    <text>{{ e.text }} (<span>{{ statuses[e.status].rooms.length }}</span>)</text>
                    <div></div>
                </a>
            </div>
            <div v-for="(e, i) in STATUSES" :key="i" class="ui bottom attached tab segment" :class="statuses[e.status].activated && 'active'">
                <div class="sort-wrap">
                    <div class="ui form">
                        <div class="fields">
                            <div class="four wide field">
                                <select v-model="statuses[e.status].filter.type">
                                    <option v-for="(v, k) in SEARCH_TYPE" :key="k" :value="k">{{ v }}</option>
                                </select>
                            </div>
                            <div class="nine wide field">
                                <div class="ui action input">
                                    <input type="text" v-model="statuses[e.status].filter.input"/>
                                    <button class="ui icon button" @click.stop="filter(e.status)">
                                        <i class="search icon"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="three wide field">
                                <select v-model="statuses[e.status].filter.ordering" @change="changeOrdering(e.status)">
                                    <option v-for="(v, k) in ORDERING_TYPE" :key="k" :value="k">{{ v }}</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="talk-list-wrap" ref="talk-list-wrap">
                    <ul v-if="statuses[e.status].rooms.length">
                        <li v-for="(room, j) in statuses[e.status].rooms" :key="j" class="talk-list" @click="openRoom(room.roomId, room.userName)">
                            <div v-if="room.showing" class="ui segment" :class="activatedRoomIds.includes(room.roomId) && 'active'">
                                <%--TODO: 카톡채널로 들어온 상담인지 어떻게 알지?--%>
                                <div class="ui top left attached label small blue">서비스 : {{ room.svcName }} <img src="<c:url value="/resources/images/kakao-icon.png"/>" class="channel-icon"></div>
                                <div class="ui top right attached label small">상담원 : {{ room.userName }}</div>
                                <div class="ui bottom right attached label small time">{{ timestampFormat(room.roomLastTime) }}</div>
                                <div class="ui divided items">
                                    <div class="item">
                                        <div class="thumb">
                                            <i class="user circle icon"></i>
                                        </div>
                                        <div class="middle aligned content">
                                            <div class="header">
                                                <text class="-custom-name">{{ room.maindbCustomName ? room.maindbCustomName : '미등록고객' }}</text>
                                                <span v-if="!activatedRoomIds.includes(room.roomId) && room.hasNewMessage" class="ui mini label circular"> N </span>
                                            </div>
                                            <div class="meta">
                                                <i v-if="room.type !== 'text'">
                                                    {{ room.type === 'photo' ? '사진' : room.type === 'audio' ? '음원' : room.type === 'file' ? '파일' : room.type }}
                                                    {{ room.send_receive === 'R' ? '전송됨' : '전달 받음' }}
                                                </i>
                                                <text v-else>{{ room.content }}</text>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <div v-else class="null-data">조회된 데이터가 없습니다.</div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const talkListContainer = Vue.createApp({
                setup: function () {
                    return {
                        STATUSES: Object.freeze([{status: 'MY', text: '상담중'}, {status: 'TOT', text: '비접수'}, {status: 'OTH', text: '타상담'}, {status: 'END', text: '종료'},]),
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
                    }
                },
                methods: {
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

                                function appendNewRoom() {
                                    e.container = _this.statuses[status]
                                    e.container.rooms.push(e)
                                    _this.roomMap[e.roomId] = e
                                }

                                if (_this.roomMap[e.roomId]) {
                                    for (let i = 0; i < _this.roomMap[e.roomId].container.rooms.length; i++) {
                                        if (_this.roomMap[e.roomId].container.rooms[i].roomId === e.roomId) {
                                            if (status === _this.roomMap[e.roomId].container.status) {
                                                Object.assign(_this.roomMap[e.roomId].container.rooms[i], e)
                                            } else {
                                                _this.roomMap[e.roomId].container.rooms.splice(i, 1)
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
                    updateRoom: function (roomId, messageType, content, messageTime) {
                        if (!this.roomMap[roomId])
                            return this.load()

                        this.roomMap[roomId].content = content
                        this.roomMap[roomId].roomLastTime = messageTime
                        this.roomMap[roomId].type = messageType

                        this.changeOrdering(this.roomMap[roomId].container.status)
                    },
                    activeTab: function (status) {
                        this.STATUSES.forEach(e => this.statuses[e.status].activated = e.status === status)
                    },
                    loadActivatedRoomIds: function () {
                        this.activatedRoomIds = talkRoomList.map(e => e.roomId)
                    },
                    removeRoom: function (roomId) {
                        if (!this.roomMap[roomId])
                            return

                        for (let i = 0; i < this.roomMap[roomId].container.rooms.length; i++) {
                            if (this.roomMap[roomId].container.rooms[i].roomId === roomId) {
                                this.roomMap[roomId].container.rooms.splice(i, 1)
                                delete this.roomMap[roomId]
                                return
                            }
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
                    timestampFormat: function (e) {
                        return moment(e).format('MM-DD HH:mm')
                    },
                    openRoom: function (roomId, userName) {
                        talkRoom.loadRoom(roomId, userName).done(() => this.loadActivatedRoomIds())
                    }
                },
                updated: function () {
                    $(this.$refs['talk-list-wrap']).overlayScrollbars({})
                },
                mounted: function () {
                    this.activeTab(this.STATUSES[0].status)
                    this.load()
                },
            }).mount('#talk-list-container')
        </script>
    </tags:scripts>

    <div class="seven wide column">
        <div id="talk-room" class="chat-container overflow-hidden" style="position: relative" @drop.stop="dropFiles" @dragover.prevent @click.stop="showingTemplates=false">
            <div v-if="showingDropzone" class="attach-overlay" @drop.prevent="dropFiles" @dragover.prevent @dragenter.stop="showingDropzone=true">
                <div class="inner">
                    <img src="<c:url value="/resources/images/circle-plus.svg"/>">
                    <p class="attach-text">파일을 채팅창에 바로 업로드하려면<br>여기에 드롭하세요.</p>
                </div>
            </div>
            <div class="room" @drop.prevent="dropFiles" @dragover.prevent @dragenter.stop="showingDropzone=true">
                <div class="chat-header dp-flex justify-content-space-between align-items-center">
                    <%--TODO: 카톡채널로 들어온 상담인지 어떻게 알지?--%>
                    <span :style="'visibility:'+(roomId?'visible':'hidden')"><img src="<c:url value="/resources/images/kakao-icon.png"/>" class="channel-icon"> [{{ roomStatus }}]-{{ roomName }}</span>
                    <button :style="'visibility:'+(roomId?'visible':'hidden')" class="ui button tiny compact button-sideview" @click.stop="popupSideViewRoomModal"></button>
                </div>
                <div class="chat-body os-host os-theme-dark os-host-resize-disabled os-host-scrollbar-horizontal-hidden os-host-transition os-host-overflow os-host-overflow-y">
                    <div class="os-resize-observer-host">
                        <div class="os-resize-observer observed" style="left: 0; right: auto;"></div>
                    </div>
                    <div class="os-size-auto-observer" style="height: calc(100% + 1px); float: left;">
                        <div class="os-resize-observer observed"></div>
                    </div>
                    <div class="os-content-glue" style="margin: -10px 0 0"></div>
                    <div class="os-padding">
                        <div ref="chatBody" @scroll="loadAdditionalMessagesIfTop" class="os-viewport os-viewport-native-scrollbars-invisible" style="overflow-y: scroll; scroll-behavior: smooth;">
                            <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                <p v-if="['SE', 'RE'].includes(e.sendReceive)" class="info-msg">[{{ getTimeFormat(e.time) }}]</p>
                                <p v-else-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType === 'info'" class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                                <div v-else-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType !== 'info'" class="chat-item"
                                     :class="['AF', 'S'].includes(e.sendReceive) && e.userId === ME && 'chat-me'">
                                    <div class="wrap-content">
                                        <div class="txt-time">[{{ e.username || customName }}] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div v-if="['AF', 'S'].includes(e.sendReceive) && e.userId === ME" class="chat-layer" style="visibility: hidden;">
                                                <div class="buttons">
                                                    <button @click="replying = e" class="button-reply" data-inverted data-tooltip="답장 달기" data-position="top center"></button>
                                                    <button @click="popupTemplateModal(e,i)" class="button-template" data-inverted data-tooltip="템플릿 만들기" data-position="top center"></button>
                                                    <button @click="popupTaskScriptModal(e,i)" class="button-knowledge" data-inverted data-tooltip="지식관리 호출" data-position="top center"></button>
                                                    <%--<button class="button-sideview" data-inverted data-tooltip="사이드 뷰" data-position="bottom center"></button>--%>
                                                </div>
                                            </div>
                                            <div class="bubble">
                                                <div class="txt_chat">
                                                    <img v-if="e.messageType === 'photo'" :src="e.fileUrl" class="cursor-pointer" @click="popupImageView(e.fileUrl)">
                                                    <audio v-if="e.messageType === 'audio'" controls :src="e.fileUrl"></audio>
                                                    <a v-if="e.messageType === 'file'" target="_blank" :href="e.fileUrl">{{ e.contents }}</a>
                                                    <pre v-if="e.messageType === 'text'">{{ e.contents }}</pre>
                                                </div>
                                                <a v-if="['file','photo','audio'].includes(e.messageType)" target="_blank" :href="e.fileUrl">저장하기</a>
                                            </div>
                                            <div v-if="!['AF', 'S'].includes(e.sendReceive) || e.userId !== ME" class="chat-layer" style="visibility: hidden;">
                                                <div class="buttons">
                                                    <button @click="replying = e" class="button-reply" data-inverted data-tooltip="답장 달기" data-position="top center"></button>
                                                    <button @click="popupTemplateModal(e,i)" class="button-template" data-inverted data-tooltip="템플릿 만들기" data-position="top center"></button>
                                                    <button @click="popupTaskScriptModal(e,i)" class="button-knowledge" data-inverted data-tooltip="지식관리 호출" data-position="top center"></button>
                                                    <%--<button class="button-sideview" data-inverted data-tooltip="사이드 뷰" data-position="bottom center"></button>--%>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="write-chat" @drop.stop="dropFiles" @dragover.prevent @dragenter.stop="showingDropzone=true">
                <div class="write-menu">
                    <div v-if="showingTemplates" class="template-container">
                        <div class="template-container-inner">
                            <ul class="template-ul">
                                <li v-for="(e, i) in templates" :key="i" :class="i === activatingTemplateIndex && 'active'" @click.stop="sendMessage(e.text)" class="template-list">
                                    <div class="template-title">/{{ e.name }}</div>
                                    <div class="template-content">{{ e.text }}</div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div v-if="replying !== null" class="view-to-reply">
                        <div v-if="replying.messageType === 'photo'" class="target-image">
                            <img :src="replying.fileUrl" class="target-image-content">
                        </div>
                        <div class="target-text">
                            <p class="target-user">{{ replying.username || customName }}에게 답장</p>
                            <p class="target-content">{{ replying.contents }}</p>
                        </div>
                        <div class="target-close" @click="replying=null">
                            <img src="<c:url value="/resources/images/icon-close.svg"/>">
                        </div>
                    </div>

                    <div :style="'visibility:'+(roomId?'visible':'hidden')">
                        <%--TODO--%>
                        <button type="button" class="mini ui button compact">봇템플릿</button>
                        <input style="display: none" type="file" @change="sendFile">
                        <button type="button" class="mini ui button icon compact mr10" onclick="this.previousElementSibling.click()" title="파일전송"><i class="paperclip icon"></i></button>
                        <%--TODO--%>
                        <button class="ui icon compact mini button" data-inverted="" data-tooltip="음성대화" data-variation="tiny" data-position="top center"><i class="microphone icon"></i></button>
                        <%--TODO--%>
                        <button class="ui icon compact mini button" data-inverted="" data-tooltip="화상대화" data-variation="tiny" data-position="top center"><i class="user icon"></i></button>
                        <%--TODO--%>
                        <div class="ui fitted toggle checkbox auto-ment vertical-align-middle ml5">
                            <input type="checkbox">
                            <label></label>
                        </div>
                        <button v-if="roomStatus === 'E'" class="mini ui button compact" @click.stop="deleteRoom">대화방내리기</button>
                        <button v-else-if="!userId" class="mini ui button compact" @click.stop="assignUnassignedRoomToMe">찜하기</button>
                        <button v-else-if="userId !== ME" class="mini ui button compact" @click.stop="assignAssignedRoomToMe">가져오기</button>
                        <button v-else class="mini ui button compact" @click.stop="finishCounsel">대화방종료</button>
                    </div>
                </div>

                <div class="wrap-inp">
                    <div class="inp-box">
                        <textarea placeholder="전송하실 메시지를 입력하세요." ref="message" @paste.prevent="pasteClipboardFiles" @keyup.stop="keyup"></textarea>
                    </div>
                    <button type="button" class="send-btn" @click="sendMessage()">전송</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="side-view-modal" class="ui modal side-view-room">
    <div class="chat-container"></div>
</div>

<tags:scripts>
    <script>
        (function makeSideViewRoomElement() {
            const talkRoomElement = document.getElementById('talk-room')
            const sideViewModalElement = document.getElementById('side-view-modal')
            const contentBody = sideViewModalElement.querySelector('.chat-container')
            for (let i = 0; i < talkRoomElement.children.length; i++)
                contentBody.append(talkRoomElement.children[i].cloneNode(true))

            sideViewModalElement.querySelector('.button-sideview').remove()
            const header = sideViewModalElement.querySelector('.chat-header')
            header.setAttribute('data-act', 'draggable')
            const closeButton = document.createElement('i')
            closeButton.className = 'close icon'
            closeButton.setAttribute('onclick', 'sideViewModal.closeModal()')
            header.append(closeButton)
        })()

        const talkRoomProperties = {
            setup: function () {
                return {ME: userId}
            },
            data: function () {
                return {
                    roomId: null,
                    userKey: null,
                    senderKey: null,
                    roomName: null,
                    roomStatus: null,
                    userId: null,
                    userName: null,
                    customName: null,

                    showingDropzone: false,

                    showingTemplates: false,
                    activatingTemplateIndex: null,
                    templates: [],

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

                    if (['file', 'photo', 'audio'].includes(message.messageType))
                        message.fileUrl = $.addQueryString(message.contents, {token: '${g.escapeQuote(accessToken)}'})

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
                sendMessage: function (message) {
                    if (!message) message = this.$refs.message.value
                    if (!message || !this.roomStatus || this.roomStatus === 'E') return
                    talkCommunicator.sendMessage(this.roomId, this.senderKey, this.userKey, message)
                    this.$refs.message.value = ''
                    this.showingTemplates = false
                    this.replying = null
                },
                sendFile: function (event) {
                    const file = event.target.files[0]
                    event.target.value = null

                    if (!file || !file.name)
                        return

                    const _this = this
                    uploadFile(file).done(function (response) {
                        restSelf.post('/api/counsel/' + _this.roomId + '/upload-file', {
                            filePath: response.data.filePath,
                            originalName: response.data.originalName,
                            sender_key: _this.senderKey,
                            user_key: _this.userKey
                        })
                    })
                },
                dropFiles: function (event) {
                    this.showingDropzone = false

                    if (!event.dataTransfer)
                        return

                    const _this = this
                    for (let i = 0; i < event.dataTransfer.files.length; i++) {
                        uploadFile(event.dataTransfer.files[i]).done(function (response) {
                            restSelf.post('/api/counsel/' + _this.roomId + '/upload-file', {
                                filePath: response.data.filePath,
                                originalName: response.data.originalName,
                                sender_key: _this.senderKey,
                                user_key: _this.userKey
                            })
                        })
                    }
                },
                pasteClipboardFiles: function (event) {
                    const _this = this
                    for (let i = 0; i < event.clipboardData.items.length; i++) {
                        uploadFile(event.clipboardData.items[i].getAsFile()).done(function (response) {
                            restSelf.post('/api/counsel/' + _this.roomId + '/upload-file', {
                                filePath: response.data.filePath,
                                originalName: response.data.originalName,
                                sender_key: _this.senderKey,
                                user_key: _this.userKey
                            })
                        })
                    }
                },
                keyup: function (event) {
                    if (event.key === '/' && this.$refs.message.value === '/' && this.templates.length > 0) {
                        this.showingTemplates = true
                        this.activatingTemplateIndex = null
                        return
                    }

                    if (event.key === 'Escape') {
                        this.showingTemplates = false
                        this.replying = null
                        return
                    }

                    if (this.showingTemplates && this.templates.length > 0 && event.key === 'ArrowDown') {
                        if (this.activatingTemplateIndex === null)
                            return this.activatingTemplateIndex = 0

                        return this.activatingTemplateIndex = (this.activatingTemplateIndex + 1) % this.templates.length
                    }

                    if (this.showingTemplates && this.templates.length > 0 && event.key === 'ArrowUp') {
                        if (this.activatingTemplateIndex === null)
                            return this.activatingTemplateIndex = this.templates.length - 1

                        return this.activatingTemplateIndex = (this.activatingTemplateIndex - 1 + this.templates.length) % this.templates.length
                    }

                    if (this.showingTemplates && this.templates[this.activatingTemplateIndex] && event.key === 'Enter') {
                        talkCommunicator.sendMessage(this.roomId, this.senderKey, this.userKey, this.templates[this.activatingTemplateIndex].text)
                        this.$refs.message.value = ''
                        this.showingTemplates = false
                        // TODO: send image
                    }
                },
                loadTemplates: function () {
                    const _this = this
                    restSelf.get('/api/talk-template/', null, false, null).done(function (response) {
                        _this.templates = []
                        response.data.forEach(function (e) {
                            _this.templates.push({name: e.mentName, text: e.ment})
                        })
                    })
                },
                popupTemplateModal: function (message, index) {
                    const _this = this
                    const modalId = 'modal-talk-template'
                    popupDraggableModalFromReceivedHtml('/admin/talk/template/new/modal', modalId).done(function () {
                        const modal = document.getElementById(modalId)

                        // TODO: 텍스트 유형 선택

                        const selectedTextContents = getSelectedTextContentOfSingleElement()
                        if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === _this.$refs['message-' + index].querySelector('.txt_chat pre'))
                            modal.querySelector('[name=ment]').value = selectedTextContents.text
                        else
                            modal.querySelector('[name=ment]').value = message.contents

                        // TODO: 이미지일 때는 이미지 유형 선택 후, 파일 ID 입력

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
                    if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === this.$refs['message-' + index].querySelector('.txt_chat pre'))
                        contents = selectedTextContents.text

                    popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/modal-search?title=' + encodeURIComponent(contents), 'modal-search-task-script')
                },
                deleteRoom: function () {
                    const _this = this
                    restSelf.delete('/api/counsel/talk-remove-room/' + this.roomId, null, null).done(function () {
                        talkListContainer.removeRoom(_this.roomId)

                        _this.roomId = null
                        _this.showingTemplates = false
                        _this.replying = null
                        _this.messageList = []
                    })
                },
                assignUnassignedRoomToMe: function () {
                    talkCommunicator.assignUnassignedRoomToMe(this.roomId, this.senderKey, this.userKey)
                    setTimeout(talkListContainer.load, 100)
                },
                assignAssignedRoomToMe: function () {
                    talkCommunicator.assignAssignedRoomToMe(this.roomId, this.senderKey, this.userKey)
                    setTimeout(talkListContainer.load, 100)
                },
                finishCounsel: function () {
                    talkCommunicator.deleteRoom(this.roomId, this.senderKey, this.userKey)
                    // 상태값 정도는 ...  socket으로 주면 안되나.. 통신비용 아깝
                    setTimeout(talkListContainer.load, 100)
                },
                popupSideViewRoomModal: function () {
                    if (!this.roomId)
                        return
                    sideViewModal.loadRoom(this.roomId, this.userName)
                },
            },
            mounted: function () {
                this.loadTemplates()
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
                        $(_this.$el).closest('.modal').show()
                        _this.scrollToBottom()
                    })
                }
            },
        })).mount('#side-view-modal')
        const talkRoomList = [talkRoom, sideViewModal]
    </script>
</tags:scripts>

<tags:scripts>
    <script>
        function loadTalkCustomInput(maindbGroupSeq, customId, roomId, senderKey, userKey) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/talk/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), '#talk-custom-input');
        }

        function loadTalkCounselingInput(maindbGroupSeq, customId, roomId, senderKey, userKey) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/talk/counseling-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), '#talk-counseling-input');
        }

        function processTalkMessage(data) {
            const messageTime = moment().format('YYYY-MM-DD') + ' ' + data.cur_timestr.substring(data.cur_timestr.length - 8, data.cur_timestr.length)
            talkListContainer.updateRoom(data.room_id, data.type, data.content, messageTime)

            talkRoomList.forEach(e => e.appendMessage({
                roomId: data.room_id,
                time: messageTime,
                messageType: data.type,
                sendReceive: data.send_receive,
                contents: data.content,
                userId: ['AF', 'S'].includes(data.send_receive) ? data.userid : null,
                username: ['AF', 'S'].includes(data.send_receive) ? data.username : data.customname,
            }))
        }

        window.talkCommunicator = new TalkCommunicator()
            .on('svc_msg', processTalkMessage)
            .on('svc_control', processTalkMessage)
            .on('svc_end', processTalkMessage)

        $(window).on('load', function () {
            loadTalkCustomInput()
            $('#side-view-modal').dragModalShow().hide()
        })
    </script>
</tags:scripts>
