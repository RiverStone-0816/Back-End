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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal inverted small">
    <i class="close icon" onclick="$(this).closest('.modal').find('audio').each(function() {$(this).stop(); this.pause(); this.currentTime=0})"></i>
    <div class="header">STT</div>

    <div class="sixteen wide column talk-room">
        <div id="stt-history-panel" class="chat-container overflow-hidden">
            <div class="room">
                <div class="chat-header dp-flex justify-content-space-between align-items-center">
                    <span id="text-line" :style="'visibility:'+(uniqueId ?'visible':'hidden')"
                          style="width:400px; padding:0 5px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                        [{{ inOut === 'I' ? '수신' : '발신' }}] - {{ phoneNumber }}
                    </span>
                    <button type="button" class="ui button mini left floated compact" :class="reMindCheck ? 'blue' : ''" @click="reMindCheck=!reMindCheck">리마인드</button>
                    <template v-if="true">
                        <c:forEach var="e" items="${files}">
                            <audio controls ref="audioPlayer" src="${apiServerUrl}/api/v1/admin/record/history/resource?path=${g.urlEncode(e.filePath)}&mode=PLAY&token=${accessToken}"></audio>
                            <%--                        <audio controls ref="audioPlayer" src="<c:url value="/resources/test9999_01058139289_20231102105303.mp3"/>"></audio>--%>
                        </c:forEach>
                    </template>
                </div>
                <div class="chat-body os-host os-theme-dark os-host-resize-disabled os-host-scrollbar-horizontal-hidden os-host-transition os-host-overflow os-host-overflow-y" style="height: 450px;">
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
                             style="overflow-y: scroll; scroll-behavior: smooth;">
                            <div v-show="!reMindCheck">
                                <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                    <div v-if="e.extension === phoneNumber && e.messageType === 'TEXT'" class="chat-item">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">[{{ e.extension }}] {{ getTimeFormat(e.time) }}</div>
                                            <div class="chat">
                                                <div class="bubble" @click="seek(e.startMs)" :style="e.startMs <= realCurrentTime && e.stopMs >= realCurrentTime ? 'border: 1px solid #c60452' : ''">
                                                    <div class="txt_chat">
                                                        <p>{{ e.contents }}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div v-if="e.extension !== phoneNumber && e.messageType === 'TEXT'" class="chat-item chat-me">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">
                                                <text>
                                                    [{{ e.extension }}]
                                                </text>
                                                {{ getTimeFormat(e.time) }}
                                            </div>
                                            <div class="chat">
                                                <div class="bubble" @click="seek(e.startMs)" :style="e.startMs <= realCurrentTime && e.stopMs >= realCurrentTime ? 'border: 1px solid #c60452' : ''">
                                                    <div class="txt_chat">
                                                        <p v-else>{{ e.contents }}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-show="reMindCheck">
                                <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                    <div v-if="e.extension === phoneNumber && e.messageType === 'TEXT' && e.reMind" class="chat-item">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">[{{ e.extension }}] {{ getTimeFormat(e.time) }}</div>
                                            <div class="chat">
                                                <div class="bubble" @click="seek(e.startMs)" :style="e.startMs <= realCurrentTime && e.stopMs >= realCurrentTime ? 'border: 1px solid #c60452' : ''">
                                                    <div class="txt_chat">
                                                        <p>{{ e.contents }}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div v-if="e.extension !== phoneNumber && e.messageType === 'TEXT' && e.reMind" class="chat-item chat-me">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">
                                                <text>
                                                    [{{ e.extension }}]
                                                </text>
                                                {{ getTimeFormat(e.time) }}
                                            </div>
                                            <div class="chat">
                                                <div class="bubble" @click="seek(e.startMs)" :style="e.startMs <= realCurrentTime && e.stopMs >= realCurrentTime ? 'border: 1px solid #c60452' : ''">
                                                    <div class="txt_chat">
                                                        <p v-else>{{ e.contents }}</p>
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
            </div>
        </div>
    </div>
</div>

<script>

    const sttRequestUrl = "${sttRequestUrl}"

    const callhistoryProperties = {
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
                companyId : '${g.user.companyId}',
                uniqueId: '${uniqueId}',
                phoneNumber: '${phoneNumber}',
                extension: '${g.user.extension}',
                userId: null,
                userName: null,
                timer: null,
                loginId: '${g.user.id}',
                dialupDate: '${dialupDate}',
                hangupDate: '${hangupDate}',

                bodyScrollingTimer: null,

                messageList: [],

                myUserName: null,
                remoteUserName: null,
                inOut: 'O',

                audioControl: null,
                progressTimer: null,
                realCurrentTime : 0,

                reMindCheck: false,
            }
        },
        async mounted() {
            this.loadRoom(this.uniqueId, this.phoneNumber, this.inOut)
            this.audioControl = this.$refs.audioPlayer
            this.audioControl.addEventListener('play', this.startProgressTimer)
            this.audioControl.addEventListener('pause', this.clearProgressTimer)

            this.audioControl.addEventListener('timeupdate', this.sttScrollHandler)
        },
        methods: {
            jsonDataParse: function (data) {
                return typeof data === 'string' ? JSON.parse(data) : JSON.parse(JSON.stringify(data))
            },
            playTime: function () {
                this.realCurrentTime = this.audioControl.currentTime * 1000
            },
            startProgressTimer: function () {
                const _this = this
                this.progressTimer = setInterval(_this.playTime, 100)
            },
            clearProgressTimer: function () {
                const _this = this
                clearInterval(_this.progressTimer)
            },
            seek: function (ms) {
                this.audioControl.currentTime = ms / 1000
            },
            loadRoom: function () {
                const _this = this

                restUtils.post(sttRequestUrl + '/get_stt_data', {
                    mode: "stt_text",
                    extension: _this.extension,
                    userid: _this.loginId,
                    uniqueid: _this.uniqueId,
                    company_id: _this.companyId,
                    dialup_date: _this.dialupDate,
                    hangup_date: _this.hangupDate,
                }, false).done(function (response) {
                    console.log('loadRoom > done > response = ', response)
                    response.stt_text.forEach(function (e) {
                        _this.appendMessage({
                            uniqueId: _this.uniqueId,
                            phoneNumber: _this.phoneNumber,
                            time: e.insert_time,
                            messageType: e.kind,
                            contents: e.text,
                            userId: e.ipcc_userid,
                            extension: e.my_extension,
                            username: _this.userName,
                            kmsKeyword: e.kms_keyword,
                            startMs: e.start_ms,
                            stopMs: e.stop_ms,
                            reMind: e.remind_yn === 'Y',
                            messageId: e.message_id
                        })
                    })
                    _this.scrollToBottom()
                })
            },
            getTimeFormat: function (time) {
                return moment(time).format('MM-DD HH:mm:ss')
            },
            appendMessage: function (message) {
                if (this.uniqueId !== message.uniqueId)
                    return

                if (this.messageList.length === 0)
                    return this.messageList.push(message)

                if (this.messageList[0].time >= message.time)
                    return this.messageList.splice(0, 0, message)

                if (this.messageList[this.messageList.length - 1].time <= message.time) {
                    this.scrollToBottom()
                    return this.messageList.push(message)
                }

                this.messageList.push(message)
                this.scrollToBottom()
                this.messageList.sort(function (a, b) {
                    return new Date(a.time) - new Date(b.time)
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
            updateTimer() {
                this.timer = (this.audio.currentTime * 1000).toFixed(0);
            },
            sttScrollHandler: function(){
                const currentTime = this.audioControl.currentTime; // 현재 재생 시점
                const duration = this.audioControl.duration; // 전체 재생 길이
                const currentPlayPercent = (currentTime / duration) * 100; // 현재 재생이 몇 퍼센트 지점인지
                const scrollPosition = this.$refs.chatBody.scrollHeight * (currentPlayPercent / 100); // chatBody(div)의 scrollHeight를 이동
                this.$refs.chatBody.scrollTop  = scrollPosition // 스크롤TOP 변경

            }
        },
    }
    const sttHistoryPanel = Vue.createApp(Object.assign({}, callhistoryProperties)).mount('#stt-history-panel')

</script>

<style>
    .border-line {
        border: 1px solid #e5e5e5;
    }
</style>