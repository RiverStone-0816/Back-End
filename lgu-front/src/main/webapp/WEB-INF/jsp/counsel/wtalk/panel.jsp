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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>

<div class="ui column grid" id="talk-panel">
    <div class="eight wide column" id="talk-list-container">
        <div class="talk-list-container">
            <div class="ui top attached tabular menu">
                <template v-for="(e, i) in STATUSES" :key="i">
                    <a class="item" style="border-top-width: 1px; border-color: #D4D4D5; border-radius: 0.28571429rem 0.28571429rem 0px 0px;"
                       :class="(statuses[e.status].activated && ' active ') + (statuses[e.status].newMessages && ' newImg_c ')"
                       @click="activeTab(e.status)"
                       <c:if test="${!g.user.admin()}">v-if="e.status !== 'REALLOCATION'"</c:if>>
                        <text>{{ e.text }} (<span>{{ statuses[e.status].rooms.length }}</span>)</text>
                        <div></div>
                    </a>
                </template>
            </div>
            <div v-for="(e, i) in STATUSES" :key="i" class="ui bottom attached tab segment"
                 :class="statuses[e.status].activated && 'active'" style="width: 100%;">
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
                                    <input :style="'display:'+(statuses[e.status].filter.type =='USER_NAME' ?'none':'visible')" type="text" v-model="statuses[e.status].filter.value"/>
                                    <select :style="'display:'+(statuses[e.status].filter.type =='USER_NAME' ?'visible':'none')" v-model="statuses[e.status].filter.value">
                                            <option :value=""></option>
                                            <option v-for="(e, i) in persons" :value="e.idName">{{ e.idName }}</option>
                                    </select>
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
                        <li v-for="(room, j) in statuses[e.status].rooms" :key="j" class="talk-list"
                            @click="openRoom(room.roomId, room.userName)">
                            <div v-if="room.showing" class="ui segment"
                                 :class="activatedRoomIds.includes(room.roomId) && 'active'">
                                <div class="ui top left attached label small" style="width:65%; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; background-color:#616b77; color: white;">
                                    <table>
                                        <thead>
                                        <th>
                                    <img v-if="room.channelType === 'kakao'"
                                         src="<c:url value="/resources/images/kakao-icon.png"/>" style="padding-right: 3%; height: 15px; position: absolute;">
                                    <img v-if="room.channelType === 'eicn'"
                                         src="<c:url value="/resources/images/chatbot-icon.svg"/>" style="padding-right: 3%; height: 15px; position: absolute;">
                                    <img v-if="room.channelType === 'naverline'"
                                         src="<c:url value="/resources/images/line-icon.png"/>" style="padding-right: 3%; height: 15px; position: absolute;">
                                    <img v-if="room.channelType === 'navertt'"
                                         src="<c:url value="/resources/images/ntalk-icon.png"/>" style="padding-right: 3%; height: 15px; position: absolute;">
                                        </th>
                                    <th style="padding-left: 20px; width:150px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; display: inline-block; font-weight: normal;">서비스 : {{ room.svcName }} </th>
                                    <th style="padding-right: 10px; display: inline-block;">/</th>
                                    <th style="width:130px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; display: inline-block; font-weight: normal;"> 상담원 : {{ room.userName }}</th>
                                        </thead>
                                    </table>
                                </div>
                                <div class="ui top right attached label small time">
                                    {{ timestampFormat(room.roomLastTime) }}
                                </div>
                                <div class="ui divided items">
                                    <div class="item">
                                        <div class="thumb">
                                            <div class="profile-img">
                                                <div v-if="e.status === 'REALLOCATION'" class="ui checkbox" @click.stop>
                                                    <input type="checkbox" name="reallocating" multiple
                                                           :value="room.roomId"/>
                                                    <label></label>
                                                </div>
                                                <img v-else :src="getImage(room.roomId)">
                                            </div>
                                        </div>
                                        <div class="middle aligned content">
                                            <div class="headerpanerl" style="padding-top: 8px; width:80px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                                <text class="-custom-name"
                                                      style="padding-right: 15px; font-weight: bold; font-size: 0.885714rem;">
                                                    {{ room.maindbCustomName ? room.maindbCustomName : '미등록고객' }}
                                                </text>
                                                <span v-if="!activatedRoomIds.includes(room.roomId) && room.hasNewMessage"
                                                      class="ui mini label circular"> N </span>
                                            </div>
                                            <div style="position: relative; top: -13px; right: -70px;  font-size: 0.88571429rem; width:350px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                                <text style="font-size: 0.88571429rem; ">{{ room.lastMessage }}</text>
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

            <button v-if="statuses.REALLOCATION.rooms.length && statuses.REALLOCATION.activated"
                    class="ui button mini compact blue" @click.stop.prevent="showReallocationModal"
                    style="position: absolute; bottom: 2em; right: 2em; z-index: 1;">
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
                                    <option v-for="(e, i) in persons" :value="e.id">{{ e.idName }} [{{ e.extension }}]
                                    </option>
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
                        STATUSES: Object.freeze([
                            {status: 'MY', text: '상담중'},
                            {status: 'TOT', text: '비접수'},
                            {status: 'OTH', text: '타상담'},
                            {status: 'END', text: '종료'},
                            {status: 'REALLOCATION', text: '재분배'},
                        ]),
                        SEARCH_TYPE: Object.freeze({CUSTOM_NAME: '고객명', USER_NAME: '상담원명'}),
                        SEARCH_TYPE_CODE: Object.freeze({CUSTOM_NAME: 'CUSTOM_NAME', USER_NAME: 'USER_NAME'}),
                        ORDERING_TYPE: Object.freeze({LAST_MESSAGE_TIME: '최근시간', CUSTOM_NAME: '고객명', USER_NAME: '상담원명'}),
                        ORDERING_TYPE_CODE: Object.freeze({LAST_MESSAGE_TIME: 'LAST_MESSAGE_TIME', CUSTOM_NAME: 'CUSTOM_NAME', USER_NAME: 'USER_NAME'}),
                    }
                },
                data: function () {
                    return {
                        statuses: this.STATUSES.reduce((o, e) => {
                            o[e.status] = {
                                ...e,
                                activated: false,
                                newMessages: 0,
                                rooms: [],
                                filter: {type: 'CUSTOM_NAME', value: '', ordering: 'LAST_MESSAGE_TIME'}
                            }
                            return o
                        }, {}),
                        roomMap: {},
                        activatedRoomIds: [],
                        persons: [],
                        isSearch: false,
                    }
                },
                methods: {
                    isReallocationStatus(status) {
                        return status !== this.statuses.END.status
                    },
                    load: function () {
                        const _this = this
                        return restSelf.get('/api/counsel/current-talk-list', null, null, true).done(function (response) {
                            _this.roomMap = {}
                            _this.STATUSES.forEach(e => _this.statuses[e.status].rooms = [])
                            response.data.forEach(function (e) {
                                if (!e.talkRoomMode)
                                    return

                                const status = e.talkRoomMode

                                e.lastMessage = _this.getLastMessage(e.send_receive, e.type, e.content, e.userName)

                                talkRoomList.forEach(room => {
                                    if (room.roomId === e.roomId){
                                        room.roomStatus = e.roomStatus
                                        room.userId = e.userId
                                    }
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
                        const condition = this.statuses[status].filter
                        const value = condition.value.trim()

                        const _this = this
                        this.statuses[status].rooms.forEach(function (e) {
                            e.showing = !value
                                || (condition.type === _this.SEARCH_TYPE_CODE.CUSTOM_NAME && e.maindbCustomName && e.maindbCustomName.includes(value))
                                || (condition.type === _this.SEARCH_TYPE_CODE.USER_NAME && e.userName && e.userName.includes(value))
                        })
                    },
                    changeOrdering: function (status) {
                        const _this = this
                        const condition = this.statuses[status].filter
                        this.statuses[status].rooms.sort(function (a, b) {
                            if (condition.ordering === _this.ORDERING_TYPE_CODE.CUSTOM_NAME) return a["maindbCustomName"].localeCompare(b["maindbCustomName"])
                            if (condition.ordering === _this.ORDERING_TYPE_CODE.USER_NAME) return a["userName"].localeCompare(b["userName"])
                            return b["roomLastTime"].localeCompare(a["roomLastTime"])
                        })
                    },
                    updateRoom: function (roomId, messageType, content, messageTime, sendReceive, customName) {
                        if (!this.roomMap[roomId])
                            return this.load()

                        this.roomMap[roomId].content = content
                        this.roomMap[roomId].roomLastTime = messageTime
                        this.roomMap[roomId].type = messageType
                        this.roomMap[roomId].lastMessage = this.getLastMessage(sendReceive, messageType, content, this.roomMap[roomId].userName)

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
                    updateRoomStatus: function (roomId, status, sendReceive, messageType, content, messageTime) {
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
                            this.roomMap[roomId].lastMessage = this.getLastMessage(sendReceive, messageType, content, this.roomMap[roomId].userName)
                        }

                        this.roomMap[roomId].status = status
                        this.roomMap[roomId].container = this.statuses[status]
                        this.statuses[status].rooms.push(this.roomMap[roomId])

                        this.changeOrdering(status)
                        if (this.isReallocationStatus(status)) this.changeOrdering(this.statuses.REALLOCATION.status)
                        this.load()
                    },
                    getImage: function (userName) {
                        return profileImageSources[Math.abs(userName.hashCode()) % profileImageSources.length]
                    },
                    activeTab: function (status) {
                        this.statuses[status].newMessages = 0
                        const condition = this.statuses[status].filter
                        var value = this.statuses[status].filter.value
                        const _this = this

                        this.STATUSES.forEach(e => {this.statuses[e.status].activated = e.status === status
                        if(status==='END')
                            {
                                if(!this.isSearch) {
                                    value='${g.user.idName}'
                                    this.statuses[status].filter.type='USER_NAME'
                                    this.statuses[status].filter.value='${g.user.idName}'
                                    this.isSearch = true
                                }
                                this.statuses[status].rooms.forEach(function (e) {
                                    e.showing = !value
                                        || (condition.type === _this.SEARCH_TYPE_CODE.CUSTOM_NAME && e.maindbCustomName && e.maindbCustomName.includes(value))
                                        || (condition.type === _this.SEARCH_TYPE_CODE.USER_NAME && e.userName && e.userName.includes(value))
                                })
                            }
                        })
                    },
                    loadActivatedRoomIds: function () {
                        this.activatedRoomIds = talkRoomList.map(e => e.roomId)
                    },
                    timestampFormat: function (e) {
                        return moment(e).format('MM-DD HH:mm')
                    },
                    openRoom: function (roomId, userName) {
                        talkRoom.loadRoom(roomId, userName).done(() => {
                            loadTalkCustomInput(this.roomMap[roomId].maindbGroupId, this.roomMap[roomId].maindbCustomId, roomId, this.roomMap[roomId].senderKey, this.roomMap[roomId].userKey, this.roomMap[roomId].channelType);
                            this.loadActivatedRoomIds();
                        })
                    },
                    reallocate() {
                        $(this.$el.parentElement).asJsonData().done(data => {
                            if (!data?.reallocating?.length || !data?.persons?.[0]?.length)
                                return $(this.$refs.reallocationModal).modalHide()

                            talkCommunicator.redistribution(
                                data.reallocating.map(e => ({
                                    channel_type: this.roomMap[e].channelType,
                                    room_id: e,
                                    user_key: this.roomMap[e].userKey,
                                })),
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
                    },
                    getLastMessage(sendReceive, type, content, userName) {
                        let lastMessage = ""

                        const fileType = checkFileType(type, content);

                        if (type === 'block') {
                            if (sendReceive === 'SB')
                                lastMessage = '블록을 전송하였습니다.'
                            else if (sendReceive === 'S')
                                lastMessage = '템플릿을 전송하였습니다.'
                            else if (['RE', 'E'].includes(sendReceive))
                                lastMessage = '챗봇이 존재하지 않습니다.'
                        } else if (['RM', 'SZ', 'SG', 'SD', 'SE', 'SAS', 'SVS', 'RAR', 'RVR', 'RARC', 'RVRC'].includes(sendReceive)) {
                            if (sendReceive === 'RM') {
                                lastMessage = '상담사연결을 요청하였습니다.'
                            } else if (sendReceive === 'SZ') {
                                lastMessage = userName + '상담사가 상담을 찜했습니다.'
                            } else if (sendReceive === 'SG') {
                                lastMessage = userName + '상담사가 상담을 가져왔습니다.'
                            } else if (sendReceive === 'SD') {
                                lastMessage = userName + '상담사가 상담을 내렸습니다.'
                            } else if (sendReceive === 'SE') {
                                lastMessage = userName + '상담사가 상담을 종료했습니다.'
                            } else if (['RAR','SAS'].includes(sendReceive)) {
                                lastMessage = '음성통화를 요청합니다.'
                            } else if (['RVR','SVS'].includes(sendReceive)) {
                                lastMessage = '영상통화를 요청합니다.'
                            } else if (sendReceive === 'RARC') {
                                lastMessage = '음성통화 완료'
                            } else if (sendReceive === 'RVRC') {
                                lastMessage = '영상통화 완료'
                            }
                        }
                        else if (fileType === 'photo')
                            lastMessage = '사진을 전송했습니다.'
                        else if (fileType === 'video')
                            lastMessage = '비디오를 전송했습니다.'
                        else if (fileType === 'audio')
                            lastMessage = '음원을 전송했습니다.'
                        else
                            lastMessage = content

                        return lastMessage
                    },
                    changeCustomName(data) {
                        this.roomMap[data.room_id].maindbCustomName = data.maindb_custom_name;
                        this.roomMap[data.room_id].maindbCustomId = data.maindb_custom_id;
                        this.roomMap[data.room_id].maindbGroupId = data.maindb_group_id;
                        talkRoom.customName = data.maindb_custom_name;
                    }
                },
                updated: function () {
                    $(this.$refs['talk-list-wrap']).overlayScrollbars({})
                },
                mounted: function () {
                    this.load()
                    this.loadPersons()
                },
            }).mount('#talk-list-container')
        </script>
    </tags:scripts>

    <div class="eight wide column talk-room" style="height: 100%;">
        <div id="talk-room" class="chat-container overflow-hidden" @drop.stop="dropFiles"
             @dragover.prevent @click.stop="showingTemplateLevel = 0">
            <div v-if="showingDropzone" class="attach-overlay" @drop.prevent="dropFiles" @dragover.prevent
                 @dragenter.stop="showingDropzone=true">
                <div class="inner">
                    <img src="<c:url value="/resources/images/circle-plus.svg"/>">
                    <p class="attach-text">파일을 채팅창에 바로 업로드하려면<br>여기에 드롭하세요.</p>
                </div>
            </div>
            <div class="room" @drop.prevent="dropFiles" @dragover.prevent @dragenter.stop="showingDropzone=true">
                <div class="chat-header dp-flex justify-content-space-between align-items-center">
                    <span id="text-line" :style="'visibility:'+(roomId?'visible':'hidden')"
                          style="width:400px; padding:0 5px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;"> <%--v-if="roomName"--%>
                        <img v-if="channelType === 'kakao'" src="<c:url value="/resources/images/kakao-icon.png"/>"
                             class="channel-icon">
                        <img v-if="channelType === 'eicn'" src="<c:url value="/resources/images/chatbot-icon.svg"/>"
                             class="channel-icon">
                        <img v-if="channelType === 'naverline'" src="<c:url value="/resources/images/line-icon.png"/>"
                             class="channel-icon">
                        <img v-if="channelType === 'navertt'" src="<c:url value="/resources/images/ntalk-icon.png"/>"
                             class="channel-icon">
                        [{{ talkStatus }}] {{ roomName }}
                    </span>
                    <button :style="'visibility:'+(roomId?'visible':'hidden')"
                            class="ui button tiny compact button-sideview"
                            @click.stop="popupSideViewRoomModal"></button>
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
                        <div ref="chatBody" @scroll="loadAdditionalMessagesIfTop"
                             class="os-viewport os-viewport-native-scrollbars-invisible"
                             style="overflow-y: scroll; scroll-behavior: smooth;" @click.stop="showingTemplateBlocks=false">
                            <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">

                                <div v-if="'SB' === e.sendReceive || e.messageType === 'block'" class="chat-item"
                                     :class="e.messageType === 'block' && ' chat-me '">
                                    <div v-if="blocks!=null" class="profile-img">
                                        <img :src="getImage(roomId)">
                                    </div>
                                    <div class="wrap-content">
                                        <div v-if="blocks!=null" class="txt-time">[{{ e.messageType === 'block' ?
                                            (e.username || e.userId) : customName }}] {{ getTimeFormat(e.time) }}
                                        </div>
                                        <div v-if="!e.displays && !e.buttonGroups" class="chat bot">
                                        </div>
                                        <div v-for="(display, j) in e.displays" class="chat bot">
                                            <div class="bubble" style="border-radius: .5rem;">
                                                <div v-if="display.type === 'text'" class="txt_chat">
                                                    <p>{{ display.elementList[0]?.content }}</p>
                                                </div>
                                                <div v-else class="card" >
                                                    <div v-if="display.type === 'image'" class="card-img">
                                                        <img :src="'${pageContext.request.contextPath}/admin/wtalk/chat-bot/image?fileName=' + encodeURIComponent(display.elementList[0]?.image)">
                                                    </div>
                                                    <template v-if="display.type === 'card'">
                                                        <div class="card-img">
                                                            <img :src="'${pageContext.request.contextPath}/admin/wtalk/chat-bot/image?fileName=' + encodeURIComponent(display.elementList[0]?.image)">
                                                        </div>
                                                        <div class="card-content">
                                                            <div class="card-title">{{ display.elementList[0]?.title
                                                                }}
                                                            </div>
                                                            <div class="card-text" style="white-space: pre-wrap;">{{
                                                                display.elementList[0]?.content }}
                                                            </div>
                                                        </div>
                                                    </template>
                                                    <div v-if="display.type === 'list'" class="card-list">
                                                        <div class="card-list-title">
                                                            <a v-if="display.elementList[0]?.url"
                                                               :href="display.elementList[0]?.url" target="_blank">{{
                                                                display.elementList[0]?.title }}</a>
                                                            <text v-else>{{ display.elementList[0]?.title }}</text>
                                                        </div>
                                                        <ul v-if="display.elementList?.length > 1" class="card-list-ul">
                                                            <li v-for="(e2, k) in getListElements(display)"
                                                                class="item">
                                                                <a :href="e2.url" target="_blank" class="link-wrap">
                                                                    <div class="item-thumb">
                                                                        <div class="item-thumb-inner">
                                                                            <img v-if="e2.image"
                                                                                 :src="'${pageContext.request.contextPath}/admin/wtalk/chat-bot/image?fileName=' + encodeURIComponent(e2.image)">
                                                                        </div>
                                                                    </div>
                                                                    <div class="item-content">
                                                                        <div class="subject">{{ e2.title }}</div>
                                                                        <div class="ment"
                                                                             style="white-space: pre-wrap;">{{
                                                                            e2.content }}
                                                                        </div>
                                                                    </div>
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                    <div v-if="display.type === 'input'" class="card-list" style="border-radius: .5rem; border-color: black">
                                                        <div class="card-list">
                                                            <ul class="card-list-ul">
                                                                <div align="left" class="label" style="padding: 0.7em 1em; border-bottom: 1px solid #dcdcdc;">{{ display.elementList[0]?.title }}</div>
                                                                <li v-for="(param, k) in getListElements(display)"
                                                                    class="item form">
                                                                    <div align="left" class="label">{{ param.displayName }}</div>
                                                                    <div v-if="param.inputType !== 'time'" class="ui fluid input">
                                                                        <input :type="(param.input_type === 'calendar' && 'date') || (param.input_type === 'number' && 'number') || (param.input_type === 'secret' && 'password') || (param.input_type === 'text' && 'text')"
                                                                        style="border-color: #0c0c0c; border-radius: .5rem;"></div>
                                                                    <div v-else class="ui multi form">
                                                                        <select class="slt" style="border-color: #0c0c0c">
                                                                            <option>오전</option>
                                                                            <option>오후</option>
                                                                        </select>
                                                                        <select class="slt" style="border-color: #0c0c0c">
                                                                            <option v-for="hour in 12" :key="hour"
                                                                                    :value="hour - 1">{{ hour - 1 }}
                                                                            </option>
                                                                        </select>
                                                                        <span class="unit" style="font-weight: 900; color: black">시</span>
                                                                        <select class="slt" style="border-color: #0c0c0c">
                                                                            <option v-for="minute in 60" :key="minute"
                                                                                    :value="minute - 1">{{ minute - 1 }}
                                                                            </option>
                                                                        </select>
                                                                        <span class="unit" style="font-weight: 900; color: black">분</span>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <%--<div v-for="(buttonGroup, j) in e.buttonGroups" class="chat bot">
                                            <div class="bubble" style="background-color: #dce6f2; width: 350px;">
                                                <div v-if="buttonGroup instanceof Array" class="button-inner"
                                                     style="width: 350px;">
                                                    <span v-for="(e2, j) in buttonGroup"><button
                                                            v-if="['url', 'phone'].includes(e2.action)"
                                                            style="background-color:#a2a7b0; margin: 5px; padding-left:10px; padding-right:10px; height: 30px;"
                                                            type="button" class="chatbot-button">{{ e2.name }}</button></span>
                                                </div>
                                            </div>
                                        </div>--%>
                                    </div>
                                </div>
                                <div v-if="'RB' === e.sendReceive && e.messageType === 'text'" class="chat-item">
                                    <div class="profile-img">
                                        <img :src="getImage(roomId)">
                                    </div>
                                    <div class="wrap-content">
                                        <div class="txt-time">[{{ customName }}] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="txt_chat">
                                                    <p>{{ e.contents }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="['SA', 'AM', 'AW', 'SAA', 'AE'].includes(e.sendReceive) || ('SB' === e.sendReceive && 'text' === e.type)" class="chat-item chat-me">
                                    <div class="wrap-content">
                                        <div class="txt-time">[오토멘트] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble" style="background-color: rgba(224,57,151,0.28);">
                                                <div class="txt_chat">
                                                    <p>{{ e.contents }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="['SAS', 'SVS'].includes(e.sendReceive)" class="chat-item chat-me" >
                                    <div class="wrap-content">
                                        <div class="txt-time">[오토멘트] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble" style="background-color: rgba(224,57,151,0.28);">
                                                <div class="txt_chat" style="width: 100px;height: 50px">
                                                    <div class="loader10"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="['RARC'].includes(e.sendReceive)" class="chat-item chat-me" >
                                    <div class="wrap-content">
                                        <div class="txt-time">[오토멘트] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble" style="background-color: rgba(224,57,151,0.28);">
                                                <div class="txt_chat">
                                                    <div style="width: 320px">
                                                        <audio controls :src="'${apiServerUrl}/api/v1/admin/record/file/resource?fileName='+jsonDataParse(e.contents).record_file+'&token=${accessToken}'" initaudio="false"></audio>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="['RVRC'].includes(e.sendReceive)" class="chat-item chat-me" >
                                    <div class="wrap-content">
                                        <div class="txt-time">[오토멘트] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble" style="background-color: rgba(224,57,151,0.28);">
                                                <div class="txt_chat">
                                                    <video controls :src="'${apiServerUrl}/api/v1/admin/record/file/resource?fileName='+jsonDataParse(e.contents).record_file+'&token=${accessToken}'"></video>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="['AF', 'S', 'R'].includes(e.sendReceive) && e.messageType !== 'info' && e.messageType !== 'block'"
                                     class="chat-item"
                                     :class="['AF', 'S'].includes(e.sendReceive)<%-- && e.userId === ME--%> && 'chat-me'">
                                    <div class="profile-img">
                                        <img :src="getImage(['AF', 'S'].includes(e.sendReceive) ? e.username : roomId)">
                                    </div>
                                    <div class="wrap-content">
                                        <div class="txt-time">
                                            <text v-if="!['AF', 'S'].includes(e.sendReceive) || e.username || e.userId">
                                                [{{ ['AF', 'S'].includes(e.sendReceive) ? (e.username || e.userId) :
                                                customName }}]
                                            </text>
                                            {{ getTimeFormat(e.time) }}
                                        </div>
                                        <div class="chat">
                                            <div v-if="['AF', 'S'].includes(e.sendReceive)<%-- && e.userId === ME--%>"
                                                 class="chat-layer" style="visibility: hidden;">
                                                <div class="buttons">
                                                    <button @click="replying = e" class="button-reply" data-inverted
                                                            data-tooltip="답장 달기" data-position="top center"></button>
                                                    <button @click="popupTemplateModal(e,i)" class="button-template"
                                                            data-inverted data-tooltip="템플릿 만들기"
                                                            data-position="top center"></button>
                                                    <button @click="popupTaskScriptModal(e,i)" class="button-knowledge"
                                                            data-inverted data-tooltip="지식관리 호출"
                                                            data-position="top center"></button>
                                                </div>
                                            </div>
                                            <div class="bubble">
                                                <div class="txt_chat">
                                                    <img v-if="e.messageType === 'photo'" :src="e.fileUrl"
                                                         class="cursor-pointer" @click="popupImageView(e.fileUrl)">
                                                    <img v-else-if="e.messageType === 'image_temp'" :src="e.fileUrl"
                                                         class="cursor-pointer" @click="popupImageView(e.fileUrl)">
                                                    <div v-else-if="e.messageType === 'audio'" class="maudio" style="width: 330px">
                                                        <audio controls :src="e.fileUrl" initaudio="false"></audio>
                                                    </div>
                                                    <video v-else-if="e.messageType === 'video'" controls :src="e.fileUrl"></video>
                                                    <a v-else-if="e.messageType === 'file'" target="_blank"
                                                       :href="e.fileUrl">{{ e.contents }}</a>
                                                    <template v-else-if="e.messageType === 'text'">
                                                        <div v-if="e.replyingType" class="reply-content-container">
                                                            <div v-if="e.replyingType === 'image'"
                                                                 class="reply-content photo">
                                                                <img :src="e.replyingTarget">
                                                            </div>
                                                            <div v-if="e.replyingType === 'image_temp'"
                                                                 class="reply-content photo">
                                                                <img :src="e.replyingTarget">
                                                            </div>
                                                            <div class="reply-content">
                                                                <div class="target-msg">
                                                                    <template v-if="e.replyingType === 'text'">{{
                                                                        e.replyingTarget }}
                                                                    </template>
                                                                    <a v-else :href="e.replyingTarget" target="_blank">{{
                                                                        e.replyingType === 'image' ? '사진' : '파일' }}</a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <p>{{ e.contents }}</p>
                                                    </template>
                                                    <p v-else>{{ e.contents }}</p>
                                                </div>
                                                <a v-if="['file','photo','audio','image_temp'].includes(e.messageType)"
                                                   target="_blank" :href="e.fileUrl" class="save-txt">저장하기</a>
                                            </div>
                                            <div v-if="!['AF', 'S'].includes(e.sendReceive)<%-- || e.userId !== ME--%>"
                                                 class="chat-layer" style="visibility: hidden;">
                                                <div class="buttons">
                                                    <button @click="replying = e" class="button-reply" data-inverted
                                                            data-tooltip="답장 달기" data-position="top center"></button>
                                                    <button @click="popupTemplateModal(e,i)" class="button-template"
                                                            data-inverted data-tooltip="템플릿 만들기"
                                                            data-position="top center"></button>
                                                    <button @click="popupTaskScriptModal(e,i)" class="button-knowledge"
                                                            data-inverted data-tooltip="지식관리 호출"
                                                            data-position="top center"></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <p v-if="['S', 'R'].includes(e.sendReceive) && e.messageType === 'info'"
                                   class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                                <p v-if="['RM', 'SZ', 'SG', 'SD', 'SE'].includes(e.sendReceive)" class="info-msg">
                                    [{{ getTimeFormat(e.time) }}]
                                    <text v-if="e.sendReceive === 'RM'">상담사연결을 요청하였습니다.</text>
                                    <text v-if="e.sendReceive === 'SZ'">[{{ e.username }}] 상담사가 상담을 찜했습니다.</text>
                                    <text v-if="e.sendReceive === 'SG'">[{{ e.username }}] 상담사가 상담을 가져왔습니다.</text>
                                    <text v-if="e.sendReceive === 'SD'">[{{ e.username }}] 상담사가 상담을 내렸습니다.</text>
                                    <text v-if="e.sendReceive === 'SE'">[{{ e.username }}] 상담사가 상담을 종료했습니다.</text>
                                </p>
                                <p v-if="['RE', 'E', 'RR', 'RT'].includes(e.sendReceive) && e.contents"
                                   class="info-msg">[{{ getTimeFormat(e.time) }}] {{ e.contents }}</p>
                                <p v-if="e.messageType === 'block' && !e.displays && !e.buttonGroups" class="info-msg">
                                    <text>[{{ getTimeFormat(e.time) }}]챗봇이 존재하지않습니다.</text>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="write-chat" @drop.stop="dropFiles" @dragover.prevent @dragenter.stop="showingDropzone=true" @click.stop="showingTemplateBlocks=false">
                <div class="write-menu">
                    <div v-if="showingTemplateBlocks" class="template-container template-block-container" style="min-width: 90%;">
                        <div class="template-container-inner">
                            <ul class="template-ul">
                                <li v-if="channelType==='eicn'" v-for="(e, i) in templateBlocks" :key="i" @click.stop="sendTemplateBlock(e.blockId)"
                                    class="template-list" >
                                    <div class="template-title">/[봇]</div>
                                    <div class="template-content">[ {{ e.name }} ]</div>
                                </li>
                                <li v-for="(e, i) in getTemplates()" :key="i"
                                    :class="i === activatingTemplateIndex && 'active'" @click.stop="sendTemplate(e)"
                                    class="template-list">
                                    <div class="template-title">/[{{ e.isImage ? '이미지' : '텍스트'}}]</div>
                                    <div v-if="e.isImage">
                                        <div class="template-content">[<img :src="e.url" class="template-image" :alt="e.fileName"/></div>
                                    </div>
                                    <div v-if="e.isImage" class="template-content" style="text-decoration: underline">-{{ e.fileName }}]</div>
                                    <div v-else>
                                        <div class="template-content">[{{ e.name }} - {{ e.text }}]</div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div v-if="showingTemplateLevel" class="template-container">
                        <div class="template-container-inner">
                            <ul class="template-ul">
                                <li v-for="(e, i) in getTemplates()" :key="i"
                                    :class="i === activatingTemplateIndex && 'active'" @click.stop="sendTemplate(e)"
                                    class="template-list">
                                    <div class="template-title">/[{{ e.isImage ? '이미지' : '텍스트'}}]</div>
                                    <div v-if="e.isImage">
                                        <div class="template-content">[<img :src="e.url" class="template-image" :alt="e.fileName"/></div>
                                    </div>
                                    <div v-if="e.isImage" class="template-content" style="text-decoration: underline">-{{ e.fileName }}]</div>
                                    <div v-else>
                                        <div class="template-content">[{{ e.name }} - {{ e.text }}]</div>
                                    </div>
                                </li>
                                <li v-if="channelType==='eicn'" v-for="(e, i) in templateBlocks" :key="i" @click.stop="sendTemplateBlock(e.blockId)"
                                    class="template-list" >
                                    <div class="template-title">/[봇]</div>
                                    <div class="template-content">[ {{ e.name }} ]</div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div v-if="replying !== null" class="view-to-reply">
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
                            <img src="<c:url value="/resources/images/icon-close.svg"/>">
                        </div>
                    </div>

                    <div :style="'visibility:'+(roomId?'visible':'hidden')">
                        <span v-show="!isMessage">
                            <button v-if="channelType==='eicn'" class="mini ui button compact mr5"
                                    @click.stop="getTemplate">템플릿
                            </button>
                            <button v-if="channelType==='kakao'" class="mini ui button compact mr5"
                                    @click.stop="getTemplateAll">템플릿
                            </button>
                            <input style="display: none" type="file" @change="sendFile">
                            <button type="button" class="mini ui button icon compact mr5" data-inverted
                                    data-variation="tiny" data-position="top center"
                                    onclick="this.previousSibling.click()"><i class="paperclip icon"></i></button>
                            <%--TODO: 음성대화--%>
                            <button v-if="channelType==='eicn' && isVChat" class="ui icon compact mini button mr5" data-inverted
                                    data-tooltip="음성대화" data-variation="tiny" data-position="top center" @click="startWebrtc('SAS')"><i
                                    class="microphone icon"></i></button>
                            <%--TODO: 화상대화--%>
                            <button v-if="channelType==='eicn' && isVChat" class="ui icon compact mini button mr5" data-inverted
                                    data-tooltip="화상대화" data-variation="tiny" data-position="top center" @click="startWebrtc('SVS')"><i
                                    class="user icon"></i></button>
                            <%--TODO: 자동멘트--%>
                            <div class="ui fitted toggle checkbox auto-ment vertical-align-middle">
                                <input type="checkbox" :value="isAutoEnable" v-model="isAutoEnable"
                                       @change="setAutoEnable(roomId)">
                                <label></label>
                            </div>
                        </span>
                    </div>
                    <div :style="'visibility:'+(roomId?'visible':'hidden')">
                        <button v-if="roomStatus === 'E'" class="mini ui button compact" @click.stop="deleteRoom">
                            대화방내리기
                        </button>
                        <button v-else-if="!userId" class="mini ui button compact"
                                @click.stop="assignUnassignedRoomToMe">찜하기
                        </button>
                        <div v-else-if="userId !== ME">
                            <button class="mini ui button compact" @click.stop="interruptingRoom" :style="'margin-right: 5px'">끼어들기
                            </button>
                            <button class="mini ui button compact" @click.stop="assignAssignedRoomToMe">가져오기
                            </button>
                        </div>
                        <button v-else class="mini ui button compact" @click.stop="finishCounsel">대화방종료</button>
                    </div>
                </div>

                <div class="wrap-inp" v-show="!isMessage">
                    <div class="inp-box">
                        <textarea placeholder="전송하실 메시지를 입력하세요." ref="message" @paste.prevent="pasteFromClipboard" @keyup.stop="keyup" :disabled="isMessage"></textarea>
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
                    isAutoEnable: false,
                    talkStatus: null,

                    loginId: '${g.user.id}',
                    isMessage: false,
                    isVChat: true,

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

                    myUserName: null,
                    remoteUserName: null,
                    recordFile: null,
                }
            },
            updated: function () {
                this.$nextTick(function () {
                    const audio = $(this).find('audio');
                    if (audio) {
                        const audioControl = maudio(audio);
                        audioControl.find('.mute, .volume-bar').remove()
                    }
                })
            },
            methods: {
                jsonDataParse: function (data) {
                    return typeof data === 'string' ? JSON.parse(data) : JSON.parse(JSON.stringify(data))
                },
                loadRoom: function (roomId, userName) {
                    const _this = this
                    const statues = talkListContainer.statuses
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
                        _this.isAutoEnable = response.data.isAutoEnable === 'Y'
                        _this.isMessage = !(response.data.userId === _this.loginId && response.data.roomStatus === 'G')
                        _this.isVChat = response.data.channelType === 'eicn'

                        const status = _this.roomStatus === 'E' ? statues.END.status
                            : !_this.userId ? statues.TOT.status
                                : _this.userId === '${g.user.id}' ? statues.MY.status
                                    : statues.OTH.status

                        _this.talkStatus = statues[status].text

                        _this.messageList = []
                        response.data.talkMsgSummaryList.forEach(function (e) {
                            _this.appendMessage({
                                roomId: roomId,
                                time: e.insertTime,
                                messageType: e.type,
                                sendReceive: e.sendReceive,
                                contents: e.content,
                                userId: e.userId,
                                username: e.userName
                            })
                        })
                        _this.scrollToBottom()
                    })
                },
                clearRoom: function () {
                    const _this = this
                    _this.roomId = null
                    _this.messageList = []
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

                    if (['RI', 'SI', 'RB'].includes(message.sendReceive) && message.type !== 'text')
                        return

                    const _this = this

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

                    message.messageType = checkFileType(message.messageType, message.contents)

                    if (message.messageType === 'block') {
                        const block = this.templateBlocks.filter(e => e.blockId === parseInt(message.contents))[0]
                        block && restSelf.get('/api/chatbot/blocks/' + block.blockId, null, null, true).done(setBlockInfo)
                    } else if (message.messageType === 'image_temp') {
                        message.originalFileUrl = message.contents
                        message.fileUrl = message.sendReceive === 'R'? message.contents : $.addQueryString(talkCommunicator.url + '/webchat_bot_image_fetch', {
                            company_id: talkCommunicator.request.companyId,
                            file_name: message.contents,
                            channel_type: _this.channelType
                        })
                    } else if (['file', 'photo', 'audio', 'video'].includes(message.messageType)) {
                        message.originalFileUrl = message.contents
                        message.fileUrl = message.sendReceive === 'R'? message.contents : $.addQueryString(talkCommunicator.url + '/webchat_bot_image_fetch', {
                            company_id: talkCommunicator.request.companyId,
                            file_name: message.contents,
                            channel_type: _this.channelType
                        })
                    } else if (!['SAS','RAR','SVS','RVR','RARC','RVRC'].includes(message.sendReceive) && this.REPLYING_INDICATOR === message.contents.charAt(0)) {
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

                    if (['RARC','RVRC'].includes(message.sendReceive)) {

                        let i = 0
                        this.messageList.forEach((data) =>{
                            if(['SAS','SVS'].includes(data.sendReceive)){
                                console.log(typeof data.contents);
                                let contents = typeof data.contents === 'string' ? JSON.parse(data.contents) : data.contents
                                let mContents = typeof message.contents === 'string' ? JSON.parse(message.contents) : message.contents
                                if(contents.record_file_key === mContents.record_file_key)
                                    _this.messageList.splice(i,1)
                            }
                            i++
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

                    talkListContainer.roomMap[message.roomId].lastMessage = talkListContainer.getLastMessage(message.sendReceive, message.messageType, message.content, message.userName)
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
                    if (!message) message = this.$refs.message.value.trim()
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
                        .done(response => restSelf.post('/api/counsel/file/' + this.channelType, response.data)
                            .done(response => talkCommunicator.sendFile(this.roomId, this.channelType, this.senderKey, this.userKey, response.data)))
                },
                sendFile: function (event) {
                    const _this = this
                    const file = event.target.files[0]
                    event.target.value = null

                    if (!file || !file.name)
                        return

                    _this.uploadFile(file)
                },
                dropFiles: function (event) {
                    this.showingDropzone = false

                    if (!event.dataTransfer)
                        return

                    for (let i = 0; i < event.dataTransfer.files.length; i++) {
                        this.uploadFile(event.dataTransfer.files[i])
                    }
                },
                setAutoEnable: function (roomId) {
                    restSelf.put('/api/counsel/talk-auto-enable/' + roomId, {isAutoEnable: (this.isAutoEnable ? "Y" : "N")})
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
                getTemplateAll:function (){
                    this.getTemplates()
                    this.showingTemplateLevel=1
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
                    } else
                        this.showingTemplateLevel = 0

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
                    restSelf.get('/api/wtalk-template/my/', null, false, null).done(function (response) {
                        _this.templates = []
                        response.data.forEach(function (e) {
                            _this.templates.push({
                                name: e.mentName,
                                permissionLevel: e.type === 'P' ? 3 : e.type === 'G' ? 2 : 1,
                                text: e.ment,
                                isImage: e.typeMent === 'P',
                                fileName: e.originalFileName,
                                filePath: e.filePath,
                                url: e.typeMent === 'P' ? $.addQueryString('${g.escapeQuote(apiServerUrl)}/api/v1/admin/wtalk/template/image', {
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
                            _this.templateBlocks.push({
                                botId: e.botId,
                                blockId: e.blockId,
                                name: e.botName + ' - ' + e.blockName
                            })
                        })
                    })
                },
                popupTemplateModal: function (message, index) {
                    const _this = this
                    const modalId = 'modal-talk-template'
                    popupDraggableModalFromReceivedHtml('/admin/wtalk/template/new/modal', modalId).done(function () {
                        const modal = document.getElementById(modalId)
                        modal.querySelector('[name=typeMent]').value = 'TEXT'
                        modal.querySelector('[type=file]').value = null

                        const selectedTextContents = getSelectedTextContentOfSingleElement()
                        if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === _this.$refs['message-' + index].querySelector('.txt_chat p')) {
                            modal.querySelector('[name=ment]').value = selectedTextContents.text
                        } else if (message.messageType === 'photo') {
                            modal.querySelector('[name=originalFileName]').value = message.originalFileUrl
                            modal.querySelector('[name=filePath]').value = message.fileUrl
                            modal.querySelector('.file-name').innerHTML = message.originalFileUrl
                            modal.querySelector('[name=typeMent]').value = 'PHOTO'
                            modal.querySelector('progress').value = 100
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
                    if (selectedTextContents && selectedTextContents.text && selectedTextContents.parent === this.$refs['message-' + index].querySelector('.txt_chat p'))
                        contents = selectedTextContents.text

                    popupDraggableModalFromReceivedHtml('/admin/service/help/task-script/modal-search?title=' + encodeURIComponent(contents), 'modal-search-task-script')
                },
                deleteRoom: function () {
                    talkCommunicator.deleteRoom(this.roomId, this.channelType, this.senderKey, this.userKey)
                    loadTalkCustomInput()
                    loadTalkCounselingInput()
                },
                assignUnassignedRoomToMe: function () {
                    talkCommunicator.assignUnassignedRoomToMe(this.roomId, this.channelType, this.senderKey, this.userKey)
                    this.userId = '${g.user.id}'
                    this.isMessage = false
                },
                assignAssignedRoomToMe: function () {
                    talkCommunicator.assignAssignedRoomToMe(this.roomId, this.channelType, this.senderKey, this.userKey)
                    this.userId = '${g.user.id}'
                    this.isMessage = false
                },
                interruptingRoom: function () {
                    this.isMessage = false
                },
                finishCounsel: function () {
                    talkCommunicator.endRoom(this.roomId, this.channelType, this.senderKey, this.userKey)
                },
                popupSideViewRoomModal: function () {
                    if (!this.roomId)
                        return
                    sideViewModal.loadRoom(this.roomId, this.userName)
                },
                getTemplate: function () {
                    this.showingTemplateBlocks = true
                    this.loadTemplates()
                    this.loadTemplateBlocks()
                },
                startWebrtc: function (receive) {
                    talkCommunicator.sendWebrtc(this.roomId, this.channelType, this.senderKey, this.userKey, receive)
                },

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
        })).mount('#side-view-modal')

        const talkRoomList = [talkRoom, sideViewModal]
        talkListContainer.activeTab(talkListContainer.STATUSES[0].status)
    </script>
</tags:scripts>

<tags:scripts>
    <script>
        function popupWebrtcModal() {
            popupDraggableModalFromReceivedHtml('/counsel/wtalk/modal-webrtc', 'modal-webrtc')
        }

        function loadTalkCustomInput(maindbGroupSeq, customId, roomId, senderKey, userKey, channel) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/wtalk/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || '',
                channel: channel || ''
            }), '#talk-custom-input');
        }

        function loadTalkCounselingInput(maindbGroupSeq, customId, roomId, senderKey, userKey) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/wtalk/counseling-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || '',
            }), '#talk-counseling-input');
        }

        function processTalkMessage(data) {
            if (!data.send_receive)
                return talkListContainer.updateRoomStatus(data.room_id, data.userid === userId ? 'MY' : 'OTH')

            const messageTime = moment().format('YYYY-MM-DD') + ' ' + data.cur_timestr.substring(data.cur_timestr.length - 8, data.cur_timestr.length)

            if (['SZ', 'SG'].includes(data.send_receive)) {
                if (data.userid === userId)
                    talkListContainer.activeTab('MY')
                talkRoom.talkStatus = talkListContainer.statuses[data.userid === userId ? 'MY' : 'OTH'].text
                talkListContainer.updateRoomStatus(data.room_id, data.userid === userId ? 'MY' : 'OTH', data.send_receive, data.type, data.content, messageTime)
            } else if (['SE', 'RE', 'AE'].includes(data.send_receive)) {
                if (data.userid === userId) {
                    talkRoom.talkStatus = talkListContainer.statuses['END'].text
                    talkListContainer.activeTab('END')
                    talkRoom.isMessage = true
                }
                talkListContainer.updateRoomStatus(data.room_id, 'END', data.send_receive, data.type, data.content, messageTime)
            } else if (['SD'].includes(data.send_receive)) {
                talkListContainer.load()
                talkRoom.clearRoom()

            } else if (['SAS','SVS'].includes(data.send_receive) && data.userid === userId) {
                talkRoom.myUserName = data.content.my_username
                talkRoom.remoteUserName = data.content.remote_username
                talkRoom.recordFile = data.content.recoed_file
                WEBRTC_INFO.server=data.content

                set_myusername_vchat(data.content.my_username)
                set_remoteusername_vchat(data.content.remote_username)
                set_record_file(data.content.record_file)
                set_ringtone_volume(0.2)
                set_busytone_volume(0.2)

                popupWebrtcModal()

            } else if (['RAR','RVR'].includes(data.send_receive) && data.userid === userId) {
                if (data.content.ready_result === 0) {
                    $('#chatText').text('고객이 통화를 수락했습니다.');
                    set_local_vchat_stream_object($('#myvideo'))
                    set_remote_vchat_stream_object($('#remotevideo'))
                    set_local_vchat_stream_object_no($('#myvideoTmp'))
                    set_remote_vchat_stream_object_no($('#remotevideoTmp'))

                    set_callback_vchat_hangup(() => {
                        $('#chatText').text('통화가 종료 되었습니다.')
                        talkCommunicator.sendWebrtcEnd(data.room_id, data.channel_type, data.sender_key, data.user_key, data.send_receive === 'RAR' ? 'SAE' : 'SVE', WEBRTC_INFO.server.webrtc_server_ip, WEBRTC_INFO.server.my_username, WEBRTC_INFO.server.remote_username, WEBRTC_INFO.server.record_file)
                        setTimeout(() => {
                            $('#modal-webrtc').hide()
                        }, 1500)
                    })

                    set_callback_vchat_outgoing_call(() => {
                        $('#chatText').text(data.send_receive === 'RAR' ? '고객과 음성통화를 시도합니다.' : '고객과 영상통화를 시도합니다.')
                    })

                    set_callback_vchat_accept(() => {
                        $('#chatText').text(data.send_receive === 'RAR' ? '고객과 음성통화중.' : '고객과 영상통화중.')
                    })

                    start_vchat();

                    setTimeout(function () {
                        data.send_receive === 'RAR' ? doVoiceChat() : doVideoChat()
                    }, 3000)
                } else {
                    $('#chatText').text('고객이 통화를 거절합니다. ');
                    setTimeout(function () {
                        $('#modal-webrtc').hide()
                    }, 1500)
                }
            } else if (['RARC','RVRC'].includes(data.send_receive) && data.userid === userId) {
                console.log(data.content);
            } else {
                talkListContainer.updateRoom(data.room_id, data.type, data.content, messageTime, data.send_receive, data.customname)
                if (data.send_receive === 'R' && data.userid === userId && talkRoom.roomId !== data.room_id) {
                    talkListContainer.statuses['MY'].newMessages++
                }
                if (data.send_receive === 'R' && data.userid === userId && $('#mode').text() === '관리모드') {
                    $('#newChangeMode').addClass('admin-new-img');
                }
                if (data.send_receive === 'SA' && data.userid === '') {
                    talkListContainer.statuses['TOT'].newMessages++
                    $('.item[data-tab="talk-panel"]').addClass('newImg');
                    if ($('#mode').text() === '관리모드') $('#newChangeMode').addClass('admin-new-img');
                }
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
            .on('svc_login', data => {
                if (data.userid === userId) $('#distributee').prop("checked", data.dist_yn === 'Y');
                if (data.dist_yn === 'D' && data.userid === userId) $('#isDistributee').hide();
            })
            .on('svc_dist_yn', data => {
                if (data.userid === userId) $('#distributee').prop("checked", data.dist_yn === 'Y');
                if (data.dist_yn === 'D' && data.userid === userId) $('#isDistributee').hide();
            })
            .on('svc_delete', processTalkMessage)
            .on('svc_custom_match', data => {
                talkListContainer.changeCustomName(data)
            })
            .on('svc_webrtc', processTalkMessage)
            .on('svc_webrtc_ready', processTalkMessage)
            .on('svc_webrtc_record', processTalkMessage)


        function checkFileType(type, content) {
            if (['file', 'image', 'audio', 'video'].includes(type)) {
                const isImage = (fileName) => {
                    if (!fileName) return false
                    return fileName.toLowerCase().endsWith('.jpg')
                        || fileName.toLowerCase().endsWith('.jpeg')
                        || fileName.toLowerCase().endsWith('.png')
                        || fileName.toLowerCase().endsWith('.bmp')
                        || fileName.toLowerCase().endsWith('.gif')
                }
                const isAudio = (fileName) => {
                    if (!fileName) return false
                    return fileName.toLowerCase().endsWith('.mp3')
                        || fileName.toLowerCase().endsWith('.wav')
                }
                const isVideo = (fileName) => {
                    if (!fileName) return false
                    return fileName.toLowerCase().endsWith('.mp4')
                        || fileName.toLowerCase().endsWith('.avi')
                        || fileName.toLowerCase().endsWith('.wmv')
                        || fileName.toLowerCase().endsWith('.mov')
                }

                if (isImage(content)) return 'photo'
                else if(isAudio(content)) return 'audio'
                else if(isVideo(content)) return 'video'
            }

            return type;
        }

        $(window).on('load', function () {
            loadTalkCustomInput()
            $('#side-view-modal').dragModalShow().hide()
        })
    </script>
</tags:scripts>