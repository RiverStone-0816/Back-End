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

<jsp:include page="/WEB-INF/jsp/messenger-room-list.jsp"/>

<div id="messenger-modal" class="ui modal large ui-resizable ui-draggable show-rooms show-room" style="width: 500px; display: block; position: absolute; left: 335px; top: 265px;">
    <div class="chat-container" @drop.prevent="dropFiles" @dragover.prevent @click.stop="showingTemplates = false" style="position: absolute; top: 0; right: 0; left: 0; bottom: 0;">
        <div class="room" style="position: absolute; top: 0; right: 0; left: 0; bottom: 0;">
            <div class="chat-header" data-act="draggable" :title="roomName">
                <button class="ui mini compact icon button" @click="popupInvitationModal">
                    <i class="user plus icon"></i>
                </button>
                <text @click="popupRoomNameModal" class="room-name">{{ roomName }}</text>
                <i class="x icon" @click="hide" style="position: absolute; right: 10px; top: 13px;"></i>
            </div>
            <div class="chat-body os-host os-theme-dark os-host-resize-disabled os-host-scrollbar-horizontal-hidden os-host-transition os-host-overflow os-host-overflow-y"
                 style="height: calc(100% - 170px); position: absolute; top: 41px; right: 0; left: 0; bottom: 105px;">
                <div class="os-resize-observer-host">
                    <div class="os-resize-observer observed" style="left: 0; right: auto;"></div>
                </div>
                <div class="os-size-auto-observer" style="height: calc(100% + 1px); float: left;">
                    <div class="os-resize-observer observed"></div>
                </div>
                <div class="os-content-glue" style="margin: -10px 0 0; width: 497px; height: 397px;"></div>
                <div class="os-padding">
                    <div ref="chatBody" @scroll="loadAdditionalMessagesIfTop" class="os-viewport os-viewport-native-scrollbars-invisible" style="overflow-y: scroll; scroll-behavior: smooth;">
                        <div class="os-content" style="padding: 10px 0 0; height: 100%; width: 100%;">
                            <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + e.messageId">
                                <p v-if="['SE', 'RE'].includes(e.sendReceive)" class="info-msg">[{{ getTimeFormat(e.time) }}]</p>
                                <p v-else-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType === 'info'" class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                                <div v-else-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType !== 'info'" class="chat-item"
                                     :class="(['AF', 'S'].includes(e.sendReceive) && e.userId === userId && 'chat-me') + ' ' + (activatedSearchingTextMessageId === e.messageId && 'active')">
                                    <div class="wrap-content">
                                        <div class="txt-time">[{{ e.username }}] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="outer-unread-count">{{ e.unreadCount || '' }}</div>
                                                <div class="txt_chat">
                                                    <img v-if="e.messageType === 'file' && e.fileType === 'image'" :src="e.fileUrl">
                                                    <audio v-else-if="e.messageType === 'file' && e.fileType === 'audio'" controls :src="e.fileUrl" style="height: 35px;"></audio>
                                                    <a v-else-if="e.messageType === 'file'" target="_blank" :href="e.fileUrl">
                                                        <i class="paperclip icon"></i> {{ e.fileName }}
                                                        <p style="opacity: 50%; font-size: smaller; padding: 0 0.5em 1em;"> 용량: {{ e.fileSize }}</p>
                                                    </a>
                                                    <p v-else>{{ e.contents }}</p>
                                                </div>
                                                <a v-if="e.messageType === 'file'" target="_blank" :href="e.fileUrl">저장하기</a>
                                            </div>
                                            <div v-if="e.userId !== userId" class="chat-layer" style="visibility: hidden;">
                                                <div class="buttons">
                                                    <button @click="replying = e" class="button-reply" data-inverted data-tooltip="답장 달기" data-position="bottom center"></button>
                                                    <button @click="popupTemplateModal(e)" class="button-template" data-inverted data-tooltip="템플릿 만들기" data-position="bottom center"></button>
                                                    <button @click="popupTaskScriptModal(e)" class="button-knowledge" data-inverted data-tooltip="지식관리 호출" data-position="bottom center"></button>
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
                <div class="os-scrollbar os-scrollbar-horizontal os-scrollbar-unusable">
                    <div class="os-scrollbar-track os-scrollbar-track-off">
                        <div class="os-scrollbar-handle" style="transform: translate(0px, 0); width: 100%;"></div>
                    </div>
                </div>
                <div class="os-scrollbar os-scrollbar-vertical">
                    <div class="os-scrollbar-track os-scrollbar-track-off">
                        <div class="os-scrollbar-handle" style="transform: translate(0px, 0); height: 79.4411%;"></div>
                    </div>
                </div>
                <div class="os-scrollbar-corner"></div>
            </div>
        </div>
        <div class="write-chat" style="position: absolute; bottom: 0;">
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
                <div class="target-text">
                    <p class="target-user">{{ replying.username }}에게 답장</p>
                    <p class="target-content">{{ replying.contents }}</p>
                </div>
                <div class="target-close" @click="replying=null">
                    <img src="<c:url value="/resources/images/icon-close.svg"/>">
                </div>
            </div>
            <div class="write-menu">
                <div class="dp-flex align-items-center">
                    <input style="display: none" type="file" @change="sendFile">
                    <button type="button" class="mini ui button icon compact mr10" onclick="this.previousElementSibling.click()" title="파일전송"><i class="paperclip icon"></i></button>
                    <div class="ui small action input mr10">
                        <input type="text" placeholder="찾기" v-model="searchingText">
                        <button class="ui icon button" @click.stop="searchText">
                            <i class="search icon"></i>
                        </button>
                    </div>
                    <text data-total="0" data-index="0" class="mr10">{{ searchingTexts.length && (searchingTextIndex + 1) || 0 }}/{{ searchingTexts.length || 0 }}</text>
                    <button type="button" class="mini ui button icon compact mr10" @click.stop="moveToPreviousText"><i class="angle up icon"></i></button>
                    <button type="button" class="mini ui button icon compact" @click.stop="moveToNextText"><i class="angle down icon"></i></button>
                </div>
                <div>
                    <button type="button" class="mini ui button compact" @click="leave">나가기</button>
                </div>
            </div>
            <div class="wrap-inp">
                <div class="inp-box">
                    <textarea placeholder="전송하실 메시지를 입력하세요." ref="message" @paste.prevent="pasteClipboardImage" @keyup.stop="keyup"></textarea>
                </div>
                <button type="button" class="send-btn" @click="sendMessage()">전송</button>
            </div>
        </div>
    </div>
</div>
<tags:scripts>
    <script>
        const messengerModal = document.getElementById('messenger-modal')
        const messenger = Vue.createApp({
            setup: function () {
                return {
                    READ_LIMIT: 100,
                    userId: userId,
                }
            },
            data: function () {
                return {
                    roomId: null,
                    roomName: '',

                    showingTemplates: false,
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
                openRoom: function () {
                    const userIds = [userId]
                    $('.-messenger-user.active').each(function () {
                        const id = $(this).attr('data-id')
                        if (!userIds.includes(id))
                            userIds.push(id)
                    })

                    if (userIds.length === 1)
                        return

                    const _this = this
                    restSelf.post('/api/chatt/', {memberList: userIds}).done(function (response) {
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
                popupInvitationModal: function () {
                    // todo
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
                        message.fileUrl = $.addQueryString(split && split[4] || '', {token: '${g.escapeQuote(accessToken)}'})
                        message.fileName = split && split[2] || ''
                        message.fileSize = split && split[3] || ''
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
                    messengerCommunicator.sendMessage(this.roomId, message)
                    this.$refs.message.value = ''
                    this.showingTemplates = false
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
                    if (!event.dataTransfer)
                        return
                    const _this = this
                    for (let i = 0; i < event.dataTransfer.files.length; i++) {
                        uploadFile(event.dataTransfer.files[i]).done(function (response) {
                            restSelf.post('/api/chatt/' + _this.roomId + '/upload-file', {filePath: response.data.filePath, originalName: response.data.originalName})
                        })
                    }
                },
                pasteClipboardFiles: function (event) {
                    const _this = this
                    for (let i = 0; i < event.clipboardData.items.length; i++) {
                        uploadFile(event.clipboardData.items[i].getAsFile()).done(function (response) {
                            restSelf.post('/api/chatt/' + _this.roomId + '/upload-file', {filePath: response.data.filePath, originalName: response.data.originalName})
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
                        messengerCommunicator.sendMessage(this.roomId, this.templates[this.activatingTemplateIndex].text)
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
                popupTemplateModal: function (message) {
                    const _this = this
                    const modalId = 'modal-talk-template'
                    popupDraggableModalFromReceivedHtml('/admin/talk/template/new/modal', modalId).done(function () {
                        const modal = document.getElementById(modalId)

                        // TODO: 텍스트 유형 선택

                        const selectedTextContents = getSelectedTextContentOfSingleElement()
                        if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === _this.$refs['message-' + message.messageId].querySelector('.txt_chat p'))
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
                popupTaskScriptModal: function (message) {
                    let contents = message.contents
                    const selectedTextContents = getSelectedTextContentOfSingleElement()
                    if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === this.$refs['message-' + message.messageId].querySelector('.txt_chat p'))
                        contents = selectedTextContents.text

                    popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/modal-search?title=' + encodeURIComponent(contents), 'modal-search-task-script')
                }
            },
            updated: function () {
            },
            mounted: function () {
                this.$nextTick(function () {
                    $(messengerModal)
                        .resizable({
                            helper: 'ui-resizable-helper',
                            minWidth: 500,
                            minHeight: 500,
                            start: function () {
                                $('iframe').css('pointer-events', 'none')
                            },
                            stop: function () {
                                $('iframe').css('pointer-events', 'auto')
                            }
                        })
                        .dragModalShow()
                        .hide()
                })
                this.loadTemplates()
            },
        }).mount(messengerModal)

        function getSelectedTextContentOfSingleElement() {
            const range = getSelection().getRangeAt(0)
            if (range.startContainer !== range.endContainer)
                return null

            return {
                parent: range.startContainer.parentElement,
                text: range.cloneContents().textContent
            }
        }

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
