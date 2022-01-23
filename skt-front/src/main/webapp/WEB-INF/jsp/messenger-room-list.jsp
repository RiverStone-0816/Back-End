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

<div id="room-list-area" class="panel full-height">
    <div class="panel-heading">
        <text>조직 대화방</text>
        <button type="button" class="panel-close"></button>
    </div>
    <div class="panel-body full-height remove-padding">
        <div class="pd10 panel-border-bottom">
            <div class="ui fluid icon input">
                <input type="text" placeholder="검색어 입력" v-model="filterString"/><%--TODO--%>
                <i class="search link icon"></i>
            </div>
        </div>
        <div class="pd10 full-height">
            <div class="organization-chat-list-wrap">
                <div class="chat-list-header">
                    <text>조직 대화방 목록</text>
                    <button type="button" class="ui basic right floated button" onclick="roomCreationOrganizationModalApp.popupCreationModal()">추가</button>
                </div>
                <div class="chat-list-body">
                    <div class="chat-list-container">
                        <ul id="messenger-chat-container">
                            <template v-for="(e, i) in roomList" :key="i">
                                <li v-if="!e.hidden" class="item" @click="loadRoom(e.roomId)">
                                    <div class="item-header">
                                        <button class="chat-item-title" @click.stop="popupRoomNameModal(e.roomId)">{{ e.roomName }}</button>
                                        <div class="chat-unread" v-if="e.unreadMessageTotalCount > 0">{{ e.unreadMessageTotalCount }}</div>
                                    </div>
                                    <div class="item-content">
                                        <div class="last-chat">{{ e.lastMsg }}</div>
                                        <div class="last-time">{{ getRoomLastMessageTimeFormat(e.lastTime) }}</div>
                                    </div>
                                </li>
                            </template>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        const roomList = Vue.createApp({
            data: function () {
                return {
                    roomMap: {},
                    roomList: [],
                    filterString: '',
                }
            },
            methods: {
                load: function () {
                    const _this = this
                    restSelf.get('/api/chatt/chatt-room', null, null, true).done(function (response) {
                        _this.roomList = []
                        response.data.forEach(function (e) {
                            _this.roomList.push(Object.assign(e.chattRoom, {unreadMessageTotalCount: e.unreadMessageTotalCount}))
                            const last = _this.roomList[_this.roomList.length - 1]
                            _this.roomMap[last.roomId] = last
                        })
                        _this.sortRooms()
                    })
                },
                getRoomLastMessageTimeFormat: function (time) {
                    return moment(time).format('MM-DD HH:mm')
                },
                hasUnreadMessage: function () {
                    for (let i = 0; i < this.roomList.length; i++)
                        if (this.roomList[i].unreadMessageTotalCount > 0)
                            return true
                    return false
                },
                loadRoom: function (roomId) {
                    messenger.loadRoom(roomId)
                },
                removeRoom: function (roomId) {
                    for (let i = 0; i < this.roomList.length; i++)
                        if (this.roomList[i].roomId === roomId) {
                            this.roomList.splice(i, 1)
                            break
                        }

                    delete this.roomMap[roomId]
                },
                popupRoomNameModal: function (roomId) {
                    prompt('새로운 채팅방이름을 입력하시오.').done(function (text) {
                        restSelf.put('/api/chatt/' + roomId + '/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
                            messengerCommunicator.changeRoomName(roomId, text);
                        });
                    })
                },
                receiveMessage: function (roomId, roomName, lastMsg, lastTime) {
                    if (this.roomMap[roomId]) {
                        const e = this.roomMap[roomId]
                        e.roomName = roomName
                        e.lastMsg = lastMsg
                        e.lastTime = lastTime
                        e.unreadMessageTotalCount++
                    } else {
                        this.roomList.push({
                            roomId: roomId,
                            roomName: roomName,
                            lastMsg: lastMsg,
                            lastTime: lastTime,
                            unreadMessageTotalCount: 1,
                        })
                        const last = this.roomList[this.roomList.length - 1]
                        this.roomMap[last.roomId] = last
                    }

                    this.sortRooms()
                },
                changeRoomName: function (roomId, roomName) {
                    this.roomMap[roomId] && (this.roomMap[roomId].roomName = roomName)
                },
                sortRooms: function () {
                    this.roomList.sort(function (a, b) {
                        return moment(b.lastTime).toDate().getTime() - moment(a.lastTime).toDate().getTime()
                    })
                },
                readMessages: function (roomId) {
                    this.roomMap[roomId] && (this.roomMap[roomId].unreadMessageTotalCount = 0)
                },
            },
            watch: {
                filterString() {
                    this.roomList.forEach(e => {
                        e.hidden = this.filterString && !e.roomName.includes(this.filterString) && !e.lastMsg?.includes(this.filterString)
                    })
                },
            },
            updated: function () {
                if (this.hasUnreadMessage())
                    $('#messenger-unread-indicator').addClass('unread')
                else
                    $('#messenger-unread-indicator').removeClass('unread')
            },
            mounted: function () {
                this.load()
            },
        }).mount('#room-list-area')
    </script>
</tags:scripts>
