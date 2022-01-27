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

<div class="ui modal tiny" id="modal-messenger-room">
    <i class="close icon" onclick="messenger.closeRoom()"></i>

    <div class="header">
        <button class="room-title" @click.stop.prevent="popupRoomNameModal">{{ roomName }}</button>
        <div class="messenger-search-btn-wrap">
            <button @click.stop.prevent="showingSearchbar = true" class="messenger-chat-search-btn">
                <img src="<c:url value="/resources/images/chat-search-icon.svg"/>">
            </button>
        </div>
    </div>
    <div class="content" @drop.prevent="dropFiles" @dragover.prevent @dragenter.stop="showingDropzone=true" @click.stop="showingTemplateLevel=0">
        <div class="organi-chat-room-container">
            <div v-if="showingSearchbar" class="chat-search-wrap active">
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
            </div>
            <div class="organi-chat-room-content">
                <div class="chat-body" ref="chatBody" @scroll="loadAdditionalMessagesIfTop" style="overflow-y: scroll; scroll-behavior: smooth;">

                    <div v-if="showingDropzone" class="attach-overlay">
                        <img src="<c:url value="/resources/images/attach-plus-icon.svg"/>">
                        <p>채팅방에 파일을 바로 업로드하려면 여기에 드롭하세요.</p>
                    </div>

                    <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + e.messageId">
                        <p v-if="['SE', 'RE'].includes(e.sendReceive)" class="info-msg">[{{ getTimeFormat(e.time) }}]</p>
                        <p v-else-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType === 'info'" class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                        <div v-else-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType !== 'info'" class="chat-item"
                             :class="(['AF', 'S'].includes(e.sendReceive) && e.userId === userId && 'chat-me') + ' ' + (activatedSearchingTextMessageId === e.messageId && 'active')">
                            <div class="wrap-content">
                                <div class="txt-segment">
                                    <div class="txt-time">[{{ e.username }}] {{ getTimeFormat(e.time) }}</div>
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
                                            </div>

                                            <div class="count">{{ e.unreadCount || '' }}</div>
                                            <div class="txt-chat">
                                                <img v-if="e.messageType === 'file' && e.fileType === 'image'" :src="e.fileUrl" class="cursor-pointer" @click="popupImageView(e.fileUrl)">
                                                <audio v-else-if="e.messageType === 'file' && e.fileType === 'audio'" controls :src="e.fileUrl" style="height: 35px;"></audio>
                                                <a v-else-if="e.messageType === 'file'" target="_blank" :href="e.fileUrl">
                                                    <i class="paperclip icon"></i> {{ e.fileName }}
                                                    <p style="opacity: 50%; font-size: smaller; padding: 0 0.5em 1em;"> 용량: {{ e.fileSize }}</p>
                                                </a>
                                                <template v-else>
                                                    <div v-if="e.replyingType" class="reply-content-container">
                                                        <div v-if="e.replyingType === 'image'" class="reply-content photo">
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
                                            </div>
                                            <button @click="replying = e" class="chat-reply-btn"><img src="<c:url value="/resources/images/chat-reply-icon.svg"/>"></button>
                                        </div>
                                    </div>
                                    <a v-if="e.messageType === 'file'" target="_blank" :href="e.fileUrl"><i class="file icon"></i>저장하기</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="write-chat">
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
                        <div v-if="replying !== null" class="view-to-reply">
                            <div v-if="replying.messageType === 'file' && replying.fileType === 'image'" class="target-image">
                                <img :src="replying.fileUrl" class="target-image-content">
                            </div>
                            <div class="target-text">
                                <p class="target-user">{{ replying.username }}에게 답장</p>
                                <p class="target-content">{{ replying.messageType === 'file' ? replying.fileName : replying.contents }}</p>
                            </div>
                            <div class="target-close" @click="replying=null">
                                <img src="<c:url value="/resources/images/chat-reply-close-icon.svg"/>">
                            </div>
                        </div>

                        <div class="wrap-inp">
                            <textarea ref="message" @paste.prevent="pasteFromClipboard" @keyup.stop="keyup" placeholder="전송하실 메시지를 입력하세요."></textarea>
                            <button type="button" class="send-btn" @click="sendMessage()"></button>
                        </div>
                    </div>
                    <div class="write-menu">
                        <div>
                            <%--<button type="button" data-inverted data-tooltip="템플릿" data-position="top left">
                                <img src="<c:url value="/resources/images/chat-template-import-icon.svg"/>">
                            </button>--%>
                            <input style="display: none" type="file" @change="sendFile">
                            <button type="button" data-inverted data-tooltip="파일첨부" data-position="top center" onclick="this.previousElementSibling.click()">
                                <img src="<c:url value="/resources/images/chat-file-icon.svg"/>">
                            </button>
                            <button type="button" data-inverted data-tooltip="초대" data-position="top right" onclick="roomCreationOrganizationModalApp.popupInvitationModal()">
                                <img src="<c:url value="/resources/images/chat-user-add-icon.svg"/>">
                            </button>
                        </div>
                        <div>
                            <button type="button" data-inverted data-tooltip="나가기" data-position="top right" @click="leave">
                                <img src="<c:url value="/resources/images/chat-exit-icon.svg"/>" alt="나가기">
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        const messengerModal = document.getElementById('modal-messenger-room')
        const messenger = Vue.createApp({
            setup: function () {
                return {
                    READ_LIMIT: 100,
                    userId: userId,
                    REPLYING_INDICATOR: '\u001b',
                    REPLYING_TEXT: '\u001a',
                    REPLYING_IMAGE: '\u000f',
                    REPLYING_FILE: '\u000e',
                }
            },
            data: function () {
                return {
                    teams: [],

                    roomId: null,
                    roomName: '',

                    showingInvitationPanel: false,
                    showingDropzone: false,
                    showingSearchbar: false,

                    showingTemplateLevel: 0,
                    showingTemplateFilter: '',
                    activatingTemplateIndex: null,
                    templates: [],

                    replying: null,

                    searchingText: '',
                    searchingTexts: [],
                    searchingTextIndex: 0,
                    activatedSearchingTextMessageId: null,

                    startMessageTime: null,
                    startMessageId: null,
                    endMessageTime: null,
                    endMessageId: null,

                    members: {},
                    messageMap: {},
                    messageList: [],

                    bodyScrollingTimer: null,
                    messageLoading: false,
                }
            },
            methods: {
                loadRoom: function (roomId) {
                    const _this = this
                    restSelf.get('/api/chatt/' + roomId + '/chatting', {limit: this.READ_LIMIT}).done(function (response) {
                        _this.roomId = roomId
                        _this.roomName = response.data.roomName
                        _this.members = {}
                        response.data.chattingMembers.forEach(function (e) {
                            if (e.userid === userId)
                                return
                            _this.members[e.userid] = e
                        })
                        _this.messageMap = {}
                        _this.messageList = []
                        response.data.chattingMessages.forEach(function (e) {
                            _this.appendMessage({
                                roomId: e.roomId,
                                messageId: e.messageId,
                                time: e.insertTime,
                                messageType: e.type,
                                sendReceive: e.sendReceive,
                                contents: e.content,
                                userId: e.userid,
                                username: e.userName,
                                unreadCount: e.unreadMessageCount
                            })
                        })
                        _this.scrollToBottom()

                        _this.startMessageTime = _this.messageList[0] && _this.messageList[0].time
                        _this.startMessageId = _this.messageList[0] && _this.messageList[0].messageId
                        _this.endMessageTime = _this.messageList[0] && _this.messageList[_this.messageList.length - 1].time
                        _this.endMessageId = _this.messageList[0] && _this.messageList[_this.messageList.length - 1].messageId

                        if (_this.endMessageId)
                            messengerCommunicator.confirmMessage(roomId, _this.endMessageId)

                        _this.searchingText = ''
                        _this.searchingTexts = []
                        _this.searchingTextIndex = 0
                        _this.activatedSearchingTextMessageId = null

                        $(messengerModal).show()
                    })
                },
                loadAdditionalMessages: function (option) {
                    const _this = this

                    option = option || {}
                    if (this.startMessageId) option.startMessageId = this.startMessageId

                    return restSelf.get('/api/chatt/' + this.roomId + '/chatting', option).done(function (response) {
                        response.data.chattingMessages.forEach(function (e) {
                            _this.appendMessage({
                                roomId: e.roomId,
                                messageId: e.messageId,
                                time: e.insertTime,
                                messageType: e.type,
                                sendReceive: e.sendReceive,
                                contents: e.content,
                                userId: e.userid,
                                username: e.userName,
                                unreadCount: e.unreadMessageCount
                            })
                        })

                        _this.startMessageTime = _this.messageList[0] && _this.messageList[0].time
                        _this.startMessageId = _this.messageList[0] && _this.messageList[0].messageId
                        _this.endMessageTime = _this.messageList[0] && _this.messageList[_this.messageList.length - 1].time
                        _this.endMessageId = _this.messageList[0] && _this.messageList[_this.messageList.length - 1].messageId

                        if (_this.endMessageId)
                            messengerCommunicator.confirmMessage(roomId, _this.endMessageId)
                    })
                },
                loadAdditionalMessagesIfTop: function () {
                    if (this.$refs.chatBody.scrollTop) return
                    if (this.messageLoading) return

                    const _this = this
                    this.messageLoading = true
                    this.loadAdditionalMessages({limit: this.READ_LIMIT}).done(function () {
                        _this.messageLoading = false
                    })
                },
                openRoom: function (selectedId) {
                    const userIds = []

                    if (selectedId instanceof Array) {
                        selectedId.forEach(e => !userIds.includes(e) && userIds.push(e))
                    } else {
                        selectedId && !userIds.includes(selectedId) && userIds.push(selectedId)
                    }

                    !userIds.includes(userId) && userIds.push(userId)
                    if (userIds.length === 1) return

                    const _this = this
                    restSelf.post('/api/chatt/', {memberList: userIds}).done(function (response) {
                        $('#team-list').find('.-messenger-user').removeClass('active')
                        _this.loadRoom(response.data)
                    })
                },
                popupRoomNameModal: function () {
                    const roomId = this.roomId
                    prompt('새로운 채팅방이름을 입력하시오.').done(function (text) {
                        restSelf.put('/api/chatt/' + roomId + '/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
                            messengerCommunicator.changeRoomName(roomId, text)
                        })
                    })
                },
                hide: function () {
                    this.roomId = null
                    $(messengerModal).hide()
                },
                leave: function () {
                    const _this = this
                    confirm('채팅방을 나가시겠습니까?').done(function () {
                        restSelf.delete('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id), null, null, true).done(function () {
                            messengerCommunicator.leave(_this.roomId);
                            roomList.removeRoom(_this.roomId)
                            _this.hide()
                        })
                    })
                },
                leaveMember: function (roomId, userId) {
                    if (this.roomId !== roomId)
                        return

                    if (this.userId === userId)
                        return this.leave()

                    delete this.members[userId]

                    this.updateMessageReadCount()
                },
                searchText: function () {
                    if (!this.searchingText)
                        return
                    const _this = this
                    restSelf.get('/api/chatt/' + this.roomId + '/chatting', {message: this.searchingText, limit: this.READ_LIMIT}).done(function (response) {
                        _this.searchingText = ''
                        _this.searchingTexts = []
                        _this.searchingTextIndex = 0
                        _this.activatedSearchingTextMessageId = null

                        response.data.chattingMessages.sort(function (a, b) {
                            return b.insertTime - a.insertTime;
                        })
                        response.data.chattingMessages.forEach(function (e) {
                            _this.searchingTexts.push({messageId: e.messageId, time: e.insertTime})
                        })
                        _this.moveToText(0)
                    })
                },
                moveToText: function (index) {
                    if (!this.searchingTexts.length)
                        return

                    this.searchingTextIndex = (index + this.searchingTexts.length) % this.searchingTexts.length
                    if (!$.isNumeric(this.searchingTextIndex))
                        this.searchingTextIndex = 0

                    this.activatedSearchingTextMessageId = this.searchingTexts[this.searchingTextIndex].messageId

                    if (!this.messageMap[this.activatedSearchingTextMessageId]) {
                        const _this = this
                        this.loadAdditionalMessages({endMessageId: this.activatedSearchingTextMessageId}).done(function () {
                            _this.$nextTick(function () {
                                const e = _this.$refs['message-' + _this.activatedSearchingTextMessageId]
                                _this.scrollTo(e.getBoundingClientRect().y - e.parentElement.getBoundingClientRect().y)
                            })
                        })
                    } else {
                        const e = this.$refs['message-' + this.activatedSearchingTextMessageId]
                        this.scrollTo(e.getBoundingClientRect().y - e.parentElement.getBoundingClientRect().y)
                    }
                },
                moveToNextText: function () {
                    this.moveToText(this.searchingTextIndex - 1)
                },
                moveToPreviousText: function () {
                    this.moveToText(this.searchingTextIndex + 1)
                },
                popupImageView: function (url) {
                    popupImageView(url)
                },
                updateMessageReadCount: function () {
                    const _this = this
                    const memberIds = keys(this.members)
                    const lastReadMessageTimesOfEachMember = {}
                    memberIds.forEach(function (userId) {
                        const lastReadMessageId = _this.members[userId].lastReadMessageId
                        lastReadMessageTimesOfEachMember[userId] = lastReadMessageId && _this.messageMap[lastReadMessageId]
                            ? _this.messageMap[lastReadMessageId].time
                            : (_this.startMessageTime || 0)
                    })

                    const lastReadMessageTimes = values(lastReadMessageTimesOfEachMember)
                    lastReadMessageTimes.sort(function (a, b) {
                        return a - b
                    })

                    function getIndexInLastReadMessageTimes(time) {
                        for (let i = 0; i < lastReadMessageTimes.length; i++)
                            if (time <= lastReadMessageTimes[i])
                                return i
                        return lastReadMessageTimes.length
                    }

                    for (let i = this.messageList.length - 1; i >= 0; i--) {
                        this.messageList[i].unreadCount = getIndexInLastReadMessageTimes(this.messageList[i].time)
                        if (!this.messageList[i].unreadCount)
                            break
                    }
                },
                getTimeFormat: function (time) {
                    return moment(time).format('MM-DD HH:mm')
                },
                appendMessage: function (message, confirm) {
                    if (this.roomId !== message.roomId)
                        return

                    if (this.messageMap[message.messageId])
                        return

                    roomList.readMessages(message.roomId)
                    if (confirm)
                        messengerCommunicator.confirmMessage(message.roomId, message.messageId)

                    if (this.members[message.userId])
                        this.members[message.userId].lastReadMessageId = this.members[message.userId].lastReadMessageId > message.messageId ? this.members[message.userId].lastReadMessageId : message.messageId

                    if (message.messageType === 'file') {
                        const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(message.contents)
                        message.fileType = split[1].endsWith('g') ? 'image'
                            : split[1].contains('wav') || split[1].contains('mp') ? 'audio'
                                : null
                        message.originalFileUrl = split && split[4] || ''
                        message.fileUrl = $.addQueryString(split && split[4] || '', {token: '${g.escapeQuote(accessToken)}'})
                        message.fileName = split && split[2] || ''
                        message.fileSize = split && split[3] || ''
                    } else if (this.REPLYING_INDICATOR === message.contents.charAt(0)) {
                        [message.contents.indexOf(this.REPLYING_TEXT), message.contents.indexOf(this.REPLYING_IMAGE), message.contents.indexOf(this.REPLYING_FILE)].forEach((indicator, i) => {
                            if (indicator < 0) return
                            message.replyingType = i === 0 ? 'text' : i === 1 ? 'image' : 'file'

                            const replyingTarget = message.contents.substr(1, indicator - 1)
                            if (message.replyingType !== 'text') message.replyingTarget = $.addQueryString(replyingTarget, {token: '${g.escapeQuote(accessToken)}'})
                            else message.replyingTarget = replyingTarget

                            message.contents = message.contents.substr(indicator + 1)
                        })
                    }

                    this.messageMap[message.messageId] = message

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
                changeRoomName: function (roomId, roomName) {
                    this.roomId === roomId && (this.roomName = roomName)
                },
                sendMessage: function (message) {
                    if (!message) message = this.$refs.message.value
                    if (!message) return

                    if (this.replying) {
                        if (this.replying.messageType === 'file') {
                            message = this.REPLYING_INDICATOR + this.replying.originalFileUrl + (this.replying.fileType === 'image' ? this.REPLYING_IMAGE : this.REPLYING_FILE) + message
                        } else {
                            message = this.REPLYING_INDICATOR + this.replying.contents + this.REPLYING_TEXT + message
                        }
                    }

                    messengerCommunicator.sendMessage(this.roomId, message)
                    this.$refs.message.value = ''
                    this.showingTemplateLevel = 0
                    this.showingTemplateFilter = ''
                    this.replying = null
                },
                confirmRead: function (roomId, userId, messageId) {
                    if (this.roomId !== roomId)
                        return

                    if (this.members[userId]) {
                        this.members[userId].lastReadMessageId = messageId
                        this.updateMessageReadCount()
                    }
                },
                sendFile: function (event) {
                    const file = event.target.files[0]
                    event.target.value = null

                    if (!file || !file.name)
                        return

                    const _this = this
                    uploadFile(file).done(function (response) {
                        restSelf.post('/api/chatt/' + _this.roomId + '/upload-file', {filePath: response.data.filePath, originalName: response.data.originalName})
                    })
                },
                dropFiles: function (event) {
                    this.showingDropzone = false

                    if (!event.dataTransfer)
                        return

                    const _this = this
                    for (let i = 0; i < event.dataTransfer.files.length; i++) {
                        uploadFile(event.dataTransfer.files[i]).done(function (response) {
                            restSelf.post('/api/chatt/' + _this.roomId + '/upload-file', {filePath: response.data.filePath, originalName: response.data.originalName})
                        })
                    }
                },
                pasteFromClipboard: function (event) {
                    const _this = this
                    let hasFile = false

                    for (let i = 0; i < event.clipboardData.items.length; i++) {
                        const item = event.clipboardData.items[i]
                        if (item.kind === 'file') {
                            hasFile = true
                            uploadFile(event.clipboardData.items[i].getAsFile()).done(function (response) {
                                restSelf.post('/api/chatt/' + _this.roomId + '/upload-file', {filePath: response.data.filePath, originalName: response.data.originalName})
                            })
                        }
                    }

                    if (!hasFile) this.$refs.message.value += event.clipboardData.getData('Text')
                },
                sendTemplate(template) {
                    // TODO: 서버에 이미 존재하는 이미지 파일을 소켓에 전달하는 프로토콜 추가 필요 (이미지 템플릿으로 추가된 파일을 업로드할수 없다)
                    if (template.isImage) return alert('TODO: 서버에 이미 존재하는 이미지 파일을 소켓에 전달하는 프로토콜 추가 필요 (이미지 템플릿으로 추가된 파일을 업로드할수 없다)')
                    this.sendMessage(template.text)
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
                            // note: 이미지 템플릿은 적용안하기로 함.
                            if (e.typeMent === 'P') return

                            _this.templates.push({
                                name: e.mentName,
                                permissionLevel: e.type === 'P' ? 3 : e.type === 'G' ? 2 : 1,
                                text: e.ment,
                                isImage: e.typeMent === 'P',
                                fileName: e.originalFileName,
                                filePath: e.filePath,
                                url: e.typeMent === 'P' ? $.addQueryString('${g.escapeQuote(apiServerUrl)}/api/v1/admin/talk/template/image', {filePath: e.filePath, token: '${g.escapeQuote(accessToken)}'})
                                    : null
                            })
                        })
                    })
                },
                loadInvitationPersons: function () {
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
                invite: function (userIds, userNames) {
                    if (!userIds.length) return

                    const _this = this
                    messengerCommunicator.invite(this.roomId, userIds, userNames);
                    restSelf.put('/api/chatt/' + this.roomId + '/chatt-member', {memberList: userIds}).done(() => {
                        for (let i = 0; i < userIds.length; i++) _this.members[userIds[i]] = {userid: userIds[i], userName: userNames[i]}
                    })
                },
                popupTemplateModal: function (message) {
                    const _this = this
                    const modalId = 'modal-talk-template'
                    popupDraggableModalFromReceivedHtml('/admin/talk/template/new/modal', modalId).done(function () {
                        const modal = document.getElementById(modalId)
                        modal.querySelector('[name=typeMent]').value = 'TEXT'
                        modal.querySelector('[type=file]').value = null

                        const selectedTextContents = getSelectedTextContentOfSingleElement()
                        if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === _this.$refs['message-' + message.messageId].querySelector('.txt-chat p')) {
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
                popupTaskScriptModal: function (message) {
                    let contents = message.contents
                    const selectedTextContents = getSelectedTextContentOfSingleElement()
                    if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === this.$refs['message-' + message.messageId].querySelector('.txt-chat p'))
                        contents = selectedTextContents.text

                    popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/modal-search?title=' + encodeURIComponent(contents), 'modal-search-task-script')
                },
                mouseup(event) {
                    event.stopImmediatePropagation()
                    $('.chat-more-nav').hide()
                    $(event.target).find('.chat-more-nav').css("display","flex")
                }
            },
            updated: function () {
                updatePersonStatus()
            },
            mounted: function () {
                this.$nextTick(function () {
                    $(messengerModal)
                        .dragModalShow()
                        .hide()
                })
                this.loadTemplates()
                this.loadInvitationPersons()
            },
        }).mount(messengerModal)

        function receiveMessage(data) {
            roomList.receiveMessage(
                data.room_id,
                data.room_name,
                data.contents,
                parseFloat(data.cur_timestr) * 1000
            )
            messenger.appendMessage({
                roomId: data.room_id,
                messageId: data.message_id,
                time: parseFloat(data.cur_timestr) * 1000,
                messageType: data.type,
                sendReceive: data.send_receive,
                contents: data.contents,
                userId: data.userid,
                username: data.username,
                unreadCount: parseInt(data.member_cnt) - 1
            }, true)

            messenger.changeRoomName(data.room_id, data.room_name)
        }

        const messengerCommunicator = new MessengerCommunicator()
            .on('svc_login', data => {
                teamList && !teamList.teams.forEach(team => team.person.forEach(person => person.id === data.userid && (person.isLoginChatt = 'L'))) && teamList.$forceUpdate()
                roomCreationOrganizationModalApp && !roomCreationOrganizationModalApp.teams.forEach(team => team.person.forEach(person => person.id === data.userid && (person.isLoginChatt = 'L'))) && roomCreationOrganizationModalApp.$forceUpdate()
            })
            .on('svc_logout', data => {
                teamList && !teamList.teams.forEach(team => team.person.forEach(person => person.id === data.userid && (person.isLoginChatt = ''))) && teamList.$forceUpdate()
                roomCreationOrganizationModalApp && !roomCreationOrganizationModalApp.teams.forEach(team => team.person.forEach(person => person.id === data.userid && (person.isLoginChatt = ''))) && roomCreationOrganizationModalApp.$forceUpdate()
            })
            .on('svc_msg', receiveMessage)
            .on('svc_join_msg', function (data) {
                receiveMessage(data);
                messengerCommunicator.join(data.room_id);
            }).on('svc_invite_room', function (data) {
            }).on('svc_leave_room', function (data) {
                if (userId === data.leave_userid)
                    roomList.removeRoom(data.room_id)

                messenger(data.room_id, data.leave_userid)
            }).on('svc_read_confirm', function (data) {
                messenger.confirmRead(data.room_id, data.userid, data.last_read_message_id)
            }).on('svc_roomname_change', function (data) {
                roomList.changeRoomName(data.room_id, data.change_room_name)
                messenger.changeRoomName(data.room_id, data.change_room_name)
            })

        restSelf.get('/api/auth/socket-info').done(function (response) {
            messengerCommunicator.connect(response.data.messengerSocketUrl, response.data.companyId, response.data.userId, response.data.userName, response.data.password)
        })
    </script>
</tags:scripts>

<div class="ui modal small cover-modal-index" id="messenger-template-add-popup">
    <i class="close icon"></i>
    <div class="header">상담톡 템플릿 추가</div>
    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">사용권한</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select>
                            <option>개인</option>
                            <option>그룹</option>
                            <option>회사전체</option>
                        </select>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">유형</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select>
                            <option>텍스트</option>
                            <option>이미지</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">템플릿명</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <input type="text" value="">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">템플릿멘트</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="align-right"><label>500자 이하</label></div>
                        <textarea id="ment" name="ment" row="10" maxlength="500"></textarea>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">이미지</label>
                </div>
                <div class="twelve wide column">
                    <div class="file-upload-header">
                        <label for="file" class="ui button blue mini compact">파일찾기</label>
                        <input type="file" id="file">
                        <span class="file-name">No file selected</span>
                    </div>
                    <div>
                        <progress value="0" max="100" style="width:100%"></progress>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</div>
