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

<div class="ui modal inverted small" style="max-height: unset !important;" data-name="stt-monit">
    <i class="close icon" onclick="$(this).closest('.modal').find('audio').each(function() {$(this).stop(); this.pause(); this.currentTime=0});window.top.ipccAssistCommunicator.monit('user0989','MONIT_STOP')"></i>
    <div class="header">STT</div>

    <div class="sixteen wide column talk-room">
        <div id="stt-history-panel" class="chat-container overflow-hidden">
            <div class="room">
                <div class="chat-header dp-flex justify-content-space-between align-items-center">
                    <span id="text-line" :style="'visibility:'+(uniqueId ?'visible':'hidden')"
                          style="width:400px; padding:0 5px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                        [{{ inOut === 'I' ? '수신' : '발신' }}] - {{ phoneNumber }}
                    </span>
                </div>
                <div class="chat-body os-host os-theme-dark os-host-resize-disabled os-host-scrollbar-horizontal-hidden os-host-transition os-host-overflow os-host-overflow-y" style="height: 600px;">
                    <div class="os-resize-observer-host">
                        <div class="os-resize-observer observed" style="left: 0; right: auto;"></div>
                    </div>
                    <div class="os-size-auto-observer" style="height: calc(100% + 1px); float: left;">
                        <div class="os-resize-observer observed"></div>
                    </div>
                    <div class="os-content-glue" style="margin: -10px 0 0"></div>
                    <div class="os-padding" style="display: flex; flex-direction: column;">
                        <div ref="chatBody"
                             class="os-viewport os-viewport-native-scrollbars-invisible"
                             style="overflow-y: scroll; scroll-behavior: smooth; height: 50%;">
                            <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                <div v-if="e.extension === phoneNumber && e.messageType === 'TEXT'" class="chat-item">
                                    <div class="profile-img">
                                        <img :src="getImage(e.extension)">
                                    </div>
                                    <div class="wrap-content">
                                        <div class="txt-time">[{{ e.extension }}] {{ getTimeFormat(e.time) }}</div>
                                        <div class="chat">
                                            <div class="bubble">
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
                                                [{{ userName }}]
                                            </text>
                                            {{ getTimeFormat(e.time) }}
                                        </div>
                                        <div class="chat">
                                            <div class="bubble">
                                                <div class="txt_chat">
                                                    <p>{{ e.contents }}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="write-chat" style="display: flex; flex-direction: column; margin-top: auto; height: 50%;">
                            <div style="display: flex; flex-direction: row; margin-top: 1%; height: 80px;">
                                <div style="flex: 1; display: flex; flex-direction: column;"><div style="padding-left: 20px; margin-bottom: 5px;">키워드 추천</div>
                                    <div style="float: left;/*padding-top: 4%; padding-left: 5%;*/padding: 0 3% 3% 3%; overflow: auto; margin-bottom: 5px;">
                                        <span v-for="(e, i) in kmsKeyword.filter((f) => f.keyword_yn === 'Y')" :key="i" :ref="'kmsKeyword-' + i">
                                            <button class="ui button mini right floated compact" style="border-radius: 0.99rem; font-size: 0.55rem; margin-right: 0.4rem; color: white;font-family: none; background-color: #CACBCD;" @click="keywordSet(e.keyword)">{{ e.keyword }}</button>
                                        </span>
                                    </div>
                                </div>
                                <hr style=" height:auto; width:.01vw;border-width:0;color:rgba(34, 36, 38, 0.15);background-color:rgba(34, 36, 38, 0.15);margin-top: -7px; margin-bottom: 0;">
                                <div style="flex: 1; display: flex; flex-direction: column;"><div style="padding-left: 20px; margin-bottom: 5px;">금지어</div>
                                    <div style="float: left; overflow: auto; padding: 0 3% 3% 3%; margin-bottom: 5px;">
                                        <span v-for="(e, i) in kmsKeyword.filter((f) => f.prohibit_yn === 'Y')" :key="i" :ref="'kmsKeyword-' + i">
                                            <button class="ui button mini right floated compact" style="border-radius: 0.99rem; font-size: 0.55rem; margin-right: 0.4rem; background-color: #FF0000; color: white; font-family: none;" @click="keywordSet(e.keyword)">{{ e.keyword }}</button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="write-menu" ref="adminChatting" style="display: flex; flex-direction: column; flex: 1; border-top: 2px solid rgba(0, 0, 0, 0.06); height: inherit;">
                                <div id="adminContent" style="width: 100%; display: flex; height: 100%; flex-direction: column; height: 80%;">
                                    <div class="chat-body" style="flex: 1; white-space: pre-line;">
                                        <div ref="adminChat" style=" overflow-y: scroll; scroll-behavior: smooth; height: 100%">
                                            <div v-for="(e, i) in adminMessage" :key="i" :ref="'adminMessage-' + i">
                                                <div class="chat-item" v-if="e.userid !== loginId">
                                                    <div class="wrap-content">
                                                        <div class="txt-time">
                                                            <text>
                                                                [{{ personList[e.userid] }}]
                                                            </text>
                                                        </div>
                                                        <div class="chat">
                                                            <div class="bubble" style="border: 1px solid #c60452;">
                                                                <div class="txt_chat">
                                                                    <p>{{ e.message }}</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="chat-item chat-me" v-if="e.userid === loginId">
                                                    <div class="wrap-content">
                                                        <div class="txt-time">
                                                            <text>
                                                                [{{ personList[e.userid] }}]
                                                            </text>
                                                        </div>
                                                        <div class="chat">
                                                            <div class="bubble" style="border: 1px solid #c60452;">
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
                                <div style="display: flex; width: 100%; flex: 0; padding-bottom: 15px; ">
                                    <input class="panel-message" ref="adminMessage" style="width: 80%" placeholder="전송하실 메시지를 입력하세요." @keydown.stop="keyDown"/>
                                    <button type="button" class="ui mini button compact" style="margin: 4px;" @click="messageSend()">전송</button>
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
    const ss= 'modal-stt-monit-'+userId;
    $(document).ready(function (){
        $("#ss").attr("style","max-height:unset;");
    });



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
                uniqueId: '${callUniqueId}',
                phoneNumber: '${sttCdr.customNumber}',
                userId: '${sttCdr.ipccUserid}',
                userName: '${sttCdr.ipccUsername}',
                timer: null,
                loginId: '${g.user.id}',

                bodyScrollingTimer: null,

                messageList: [],

                myUserName: null,
                remoteUserName: null,
                inOut: '${sttCdr.inOut}',

                kmsKeyword: [],
                adminMessage: [],
                personList: {
                    <c:forEach var="e" items="${personListMap}">
                    '${e.key}': '${e.value}',
                    </c:forEach>
                },
            }
        },
        methods: {
            jsonDataParse: function (data) {
                return typeof data === 'string' ? JSON.parse(data) : JSON.parse(JSON.stringify(data))
            },
            loadRoom: function () {
                const _this = this
                return restSelf.get('/api/counsel/stt-text/${callUniqueId}').done(function (response) {
                    response.data.forEach(function (e) {
                        _this.appendMessage({
                            uniqueId: _this.uniqueId,
                            phoneNumber: _this.phoneNumber,
                            time: e.insertTime,
                            messageType: e.kind,
                            contents: e.text,
                            userId: e.ipccUserid,
                            extension: e.myExtension,
                            username: _this.userName,
                            kmsKeyword: e.kmsKeyword,
                            startMs: e.startMs,
                            stopMs: e.stopMs,
                            reMind: e.remindYn === 'Y',
                            messageId: e.messageId
                        })
                    })
                    _this.scrollToBottom()
                })
            },
            loadChat: function() {
                const _this = this
                return restSelf.get('/api/counsel/stt-chat/${callUniqueId}').done(function (response) {
                    response.data.forEach(function (e) {
                        _this.appendAdminMessage({
                            company_id: "",
                            message_id: e.messageId,
                            userid: e.userid,
                            target_userid: e.targetUserid,
                            call_uniqueid: e.callUniqueid,
                            message: e.message,
                            time: _this.getDateToTime(e.insertTime)
                        })
                    })
                    _this.scrollToBottom()
                })
            },
            getDateToTime: function (str) {
                return new Date(str).getTime()
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
                    return a.time - b.time
                })
            },
            appendAdminMessage: function (message) {
                if (this.adminMessage.length === 0)
                    return this.adminMessage.push(message)

                if (this.adminMessage[0].time >= message.time)
                    return this.adminMessage.splice(0, 0, message)

                if (this.adminMessage[this.adminMessage.length - 1].time <= message.time) {
                    this.adminScrollToBottom()
                    return this.adminMessage.push(message)
                }

                this.adminMessage.push(message)
                this.adminScrollToBottom()
                this.adminMessage.sort(function (a, b) {
                    return a.time - b.time
                })
            },
            appendKeyword: function(keyword) {
                const _this = this
                keyword.forEach(e => {
                    return !(_this.kmsKeyword.some(k => {
                        return k.keyword === e.keyword;
                    })) ? _this.kmsKeyword.push(e) : null
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
            adminScrollToBottom: function () {
                if (this.adminBodyScrollingTimer)
                    clearTimeout(this.adminBodyScrollingTimer)

                const _this = this
                console.log(_this.$refs.adminChat.scrollHeight);
                this.$nextTick(function () {
                    _this.adminBodyScrollingTimer = setTimeout(function () {
                        _this.$refs.adminChat.scroll({top: _this.$refs.adminChat.scrollHeight})
                    }, 100)
                })
            },
            getImage: function (userName) {
                return profileImageSources[Math.abs(userName.hashCode()) % profileImageSources.length]
            },
            updateTimer() {
                this.timer = (this.audio.currentTime * 1000).toFixed(0);
            },
            messageSend: function (message) {
                if (!message) message = this.$refs.adminMessage.value.trim();
                window.top.ipccAssistCommunicator.mentorCall(this.uniqueId, message, '${sttCdr.ipccUserid}');
                this.$refs.adminMessage.value='';
            },
            keyDown: function (event) {
                if (event.key === 'Enter') {
                    event.preventDefault();
                    return this.messageSend()
                }
            }
        },
        mounted() {
            this.loadRoom();
            this.loadChat();
            window.top.ipccAssistCommunicator.monit('${userId}', 'MONIT_START');
        },
    }
    const sttHistoryPanel = Vue.createApp(Object.assign({}, callhistoryProperties)).mount('#stt-history-panel')

    window.top.ipccAssistCommunicator
        .on('ADMINMONITSTT',function (message, kind, data1) {
            console.log('ADMINMONITSTT',data1);
            const data = JSON.parse(data1);
            processSttMessageMonit(data);
        })
        .on('ADMINMONITKEYWORD',function (message, kind, data1) {
            console.log('ADMINMONITKEYWORD',data1);
            const data = JSON.parse(data1);
            processAdminSttKeyword(data);
        })
        .on('ADMINMESSAGE', function (message, kind, data1) {
            console.log('ADMINMESSAGE',data1);
            const data = JSON.parse(data1);
            processAdminMessageMonit(data);
        });

    function processSttMessageMonit(data) {
        sttHistoryPanel.appendMessage({
            uniqueId: data.call_uniqueid,
            phoneNumber: data.custom_number,
            messageType: data.kind,
            contents: data.data?.text,
            userId: data.ipcc_userid,
            extension: data.my_extension,
            username: data.ipcc_username,
            startMs: data.data?.start_ms,
            stopMs: data.data?.stop_ms,
            messageId: data.message_id
        })
    }

    function processAdminMessageMonit(data) {
        sttHistoryPanel.appendAdminMessage({
            company_id: data.company_id,
            message_id: data.message_id,
            userid: data.userid,
            target_userid: data.target_userid,
            call_uniqueid: data.call_uniqueid,
            message: data.message,
            time: Date.now()
        })
    }

    function processAdminSttKeyword(data) {
        sttHistoryPanel.appendKeyword(data.data.keyword);
    }

</script>
