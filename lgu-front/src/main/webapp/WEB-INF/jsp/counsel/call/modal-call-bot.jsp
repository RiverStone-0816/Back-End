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
    <i class="close icon"></i>
    <div class="header">콜봇</div>

    <div class="sixteen wide column talk-room">
        <div id="call-bot-record-history-panel" class="chat-container overflow-hidden">
            <div class="room">
                <div class="chat-header dp-flex justify-content-space-between align-items-center">
                <span id="text-line" :style="'visibility:'+(uniqueId ?'visible':'hidden')"
                      style="width:400px; padding:0 5px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                  콜봇 [{{ inOut === 'I' ? '수신' : '발신' }}] - {{ ani }}
                </span>
                </div>
                <div class="chat-body os-host os-theme-dark os-host-resize-disabled os-host-scrollbar-horizontal-hidden os-host-transition os-host-overflow os-host-overflow-y"
                     style="height: 450px;">
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
                            <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                <div v-if="!e.ai" class="chat-item">
                                    <div class="profile-img">
                                        <img :src="getImage(ani)">
                                    </div>
                                    <div class="wrap-content">
                                        <div class="txt-time">[{{ ani }}] {{ getTimeFormat(e.endTs) }}</div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="txt_chat">
                                                    <p>{{ e.message }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="e.ai" class="chat-item chat-me">
                                    <div class="profile-img">
                                        <img :src="getImage(dnis)">
                                    </div>
                                    <div class="wrap-content">
                                        <div class="txt-time">
                                            <text>
                                                [{{ dnis }}]
                                            </text>
                                            {{ getTimeFormat(e.endTs) }}
                                        </div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="txt_chat">
                                                    <p>{{ e.message }}</p>
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

    const callBotRecordHistoryProperties = {
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
                uniqueId: '${cdr.uniqueid}',
                callStartTime: null,
                callEndTime: null,
                ani: '${cdr.inOut eq 'I' ? cdr.src : cdr.dst}',
                dnis: '${cdr.inOut eq 'I' ? cdr.dst : cdr.src}',

                bodyScrollingTimer: null,

                messageList: [
                    {
                        <fmt:formatDate var="startTs" value="${cdr.ringDate}" pattern="yyyyMMddHHmmssSSS"/>
                        <fmt:formatDate var="endTs" value="${cdr.hangupDate}" pattern="yyyyMMddHHmmssSSS"/>
                        "startTs": "${not empty startTs ? startTs : '20230101000000000'}",
                        "endTs": "${not empty endTs ? endTs : '20230101000000000'}",
                        "message": "불러올 데이터가 없습니다.",
                        "ai": true
                    }
                ],

                inOut: '${cdr.inOut}',
            }
        },
        async mounted() {
            this.loadRoom()
        },
        methods: {
            jsonDataParse: function (data) {
                return typeof data === 'string' ? JSON.parse(data) : JSON.parse(JSON.stringify(data))
            },
            loadRoom: function () {
                const _this = this
                return restSelf.get('/api/record-history/call-bot/${cdr.inOut eq 'I' ? cdr.uniqueid : cdr.cmpClickKey}/' + ('${cdr.ringDate}'.split(' ')[0].replaceAll('-', '')) + '/${cdr.etc5}/${cdr.etc4}/${cdr.src}').done(function (response) {
                    console.log("response.data = ", response.data);
                    if (response.data.resultMsg !== 'Success')
                        return

                    console.log("response.data.resultMsg = ", response.data.resultMsg);
                    _this.callStartTime = response.data.result.callStartTime;
                    _this.callEndTime = response.data.result.callEndTime;
                    _this.messageList = response.data.result.dialogs;
                })
            },
            getTimeFormat: function (time) {
                return moment(this.getDateFormat(time)).format('MM-DD HH:mm:ss')
            },
            getDateFormat: function (time) {
                return time.substring(0, 4) + '-' + time.substring(4, 6) + '-' + time.substring(6, 8) + ' ' + time.substring(8, 10)
                    + ':' + time.substring(10, 12) + ':' + time.substring(12, 14) + '.' + time.substring(14);
            },
            appendMessage: function (message) {
                if (this.uniqueId !== message.uniqueId)
                    return

                if (this.messageList.length === 0)
                    return this.messageList.push(message)

                if (this.messageList[0].endTs >= message.endTs)
                    return this.messageList.splice(0, 0, message)

                if (this.messageList[this.messageList.length - 1].time <= message.time) {
                    this.scrollToBottom()
                    return this.messageList.push(message)
                }

                this.messageList.push(message)
                this.scrollToBottom()
                this.messageList.sort(function (a, b) {
                    return a.endTs - b.endTs
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
        },
    }
    const callBotRecordHistoryPanel = Vue.createApp(Object.assign({}, callBotRecordHistoryProperties)).mount('#call-bot-record-history-panel')

</script>

<style>
    .border-line {
        border: 1px solid #e5e5e5;
    }
</style>