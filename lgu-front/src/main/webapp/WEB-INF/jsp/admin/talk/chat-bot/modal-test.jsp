<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:scripts/>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=440, initial-scale=0.8"/>
    <title></title>
    <tags:favicon/>
    <tags:css/>
</head>
<body class="overflow-auto">

<div id="chat-preview" class="chat-preview window">
    <div class="header">
        <img src="<c:url value="/resources/images/chatbot-icon.svg"/>" class="chatbot-icon"><span class="customer-title">Chat Bot</span>
    </div>
    <%--<div class="content">
        <div class="sample-bubble">
            <img src="<c:url value="/resources/images/eicn-sample.png"/>" class="customer-img">
            <p>이아이씨엔 채팅상담을 이용해 주셔서 감사합니다. 문의사항을 입력해주시면 상담원이 답변드리겠습니다. 감사합니다.</p>
        </div>
        <div class="sample-bubble">
            <p>다른 채널을 통한 상담을 원하시면 원하시는 서비스의 아이콘을 눌러주세요.</p>
            <div class="preview-channel-icon-container">
                <a href="#"><img src="<c:url value="/resources/images/kakao-icon.png"/>" class="preview-channel-icon"></a>
                <a href="#"><img src="<c:url value="/resources/images/ntalk-icon.png"/>" class="preview-channel-icon"></a>
                <a href="#"><img src="<c:url value="/resources/images/nband-icon.png"/>" class="preview-channel-icon"></a>
            </div>
        </div>
    </div>--%>

    <div v-for="(message, iMessage) in messages" :key="iMessage" :class="message.sender === 'SERVER' ? ' editor ' : ' send-message '" class=" content ">
        <template v-if="message.sender === 'SERVER'">
            <div v-if="message.data?.display?.length" v-for="(e, i) in message.data?.display" :key="i" :class="e.type === 'text' ? 'sample-bubble' : 'card'">
                <p v-if="e.type === 'text'">{{ e.element[0]?.content }}</p>
                <div v-if="e.type === 'image'" class="card-img">
                    <img :src="`/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e.element[0]?.image)" class="border-radius-1em">
                </div>
                <div v-if="e.type === 'card'" class="card-img">
                    <img :src="`/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e.element[0]?.image)" class="border-radius-top-1em">
                </div>
                <div v-if="e.type === 'card'" class="card-content">
                    <div class="card-title">{{ e.element[0]?.title }}</div>
                    <div class="card-text">{{ e.element[0]?.content }}</div>
                </div>
                <div v-if="e.type === 'list'" class="card-list">
                    <div class="card-list-title">
                        <a v-if="e.element[0]?.url" :href="e.element[0]?.url" target="_blank">{{ e.element[0]?.title }}</a>
                        <text v-else>{{ e.element[0]?.title }}</text>
                    </div>
                    <ul class="card-list-ul">
                        <li v-for="(e2, j) in getListElements(e)" :key="j" class="item">
                            <a :href="e2.url" target="_blank" class="link-wrap">
                                <div class="item-thumb" v-if="e2.image && e2.image">
                                    <div class="item-thumb-inner">
                                        <img :src="`/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e2.image)">
                                    </div>
                                </div>
                                <div class="item-content">
                                    <div class="subject">{{ e2.title }}</div>
                                    <div class="ment">{{ e2.content }}</div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>
                <span v-if="!(message.data?.button?.length) && (i + 1 === message.data.display.length)" class="time-text">{{ getTimeFormat(message.time) }}</span>
            </div>
            <div v-if="message.data?.button?.length" v-for="(e, i) in getButtonGroups(message)" :key="i" :class="e instanceof Array ? 'sample-bubble' : 'card'">
                <button v-if="e instanceof Array" v-for="(e2, j) in e" :key="j" type="button" class="chatbot-button" @click.stop.prevent="actButton(message, e2)">{{ e2.btn_name }}</button>
                <div v-else class="card-list">
                    <ul class="card-list-ul"><%--TODO: API 버튼 형태 작업해야함: 현재 socket에서 api parameter 정보 제대로 내려주지 않고 있다. --%>
                        <li v-if="e.api?.parameters" v-for="(e2, j) in e.api.parameters" :key="j" class="item form">
                            <div class="label">{{ e2.btn_name }}</div>
                            <div v-if="e2.type !== 'time'" class="ui fluid input">
                                <input type="text">
                            </div>
                            <div v-else class="ui multi form">
                                <select class="slt">
                                    <option>오전</option>
                                    <option>오후</option>
                                </select>
                                <select class="slt">
                                    <option>12</option>
                                </select>
                                <span class="unit">시</span>
                                <select class="slt">
                                    <option>55</option>
                                </select>
                                <span class="unit">분</span>
                            </div>
                        </li>
                        <li class="item">
                            <button type="button" class="chatbot-button" @click.stop.prevent="actApi(message, e2)">제출</button>
                        </li>
                    </ul>
                </div>
                <span v-if="i + 1 === getButtonGroups(message).length" class="time-text">{{ getTimeFormat(message.time) }}</span>
            </div>
        </template>
        <template v-else>
            <div class="bubble-wrap">
                <div class="bubble-inner">
                    <p class="bubble">{{ message.data }}</p>
                    <span class="time-text">{{ getTimeFormat(message.time) }}</span>
                </div>
            </div>
        </template>
    </div>

    <div v-if="socket" class="action">
        <div>
            <button type="button" class="home-btn"><img src="<c:url value="/resources/images/material-home.svg"/>"></button>
        </div>
        <div class="ui form">
            <input type="text" placeholder="문의사항을 입력하세요." v-model="input" @keyup.stop.prevent="$event.key === 'Enter' && sendText()">
        </div>
        <div>
            <button type="button" class="send-btn" @click.stop.prevent="sendText"><img src="<c:url value="/resources/images/material-send.svg"/>"></button>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        const preview = (() => {
            const SENDER = Object.freeze({SERVER: 'SERVER', USER: 'USER'})
            const o = Vue.createApp({
                setup() {
                    return {
                        botId: '${botId}',
                        request: {
                            company_id: '${g.escapeQuote(g.user.companyId)}',
                            sender_key: '${g.escapeQuote(g.user.companyId)}',
                            user_key: '${g.escapeQuote(sessionId)}',
                            mode: 'bottest',
                            my_ip: '${g.escapeQuote(ip)}',
                        }
                    }
                },
                data() {
                    return {
                        socket: null,
                        eventSequence: 0,
                        input: '',
                        messages: []
                    }
                },
                methods: {
                    createSocket() {
                        restSelf.get('/api/auth/socket-info').done(response => {
                            const url = response.data.chatbotSocketUrl
                            o.socket = io.connect(url, {'secure': url.startsWith('https')}, {'reconnect': true, 'resource': 'socket.io'})
                            o.socket.on('connect', () => o.socket.emit('webchatcli_start', o.request))
                                .on('disconnect', () => alert('연결이 종료되었습니다.'))
                                .on('webchatsvc_close', () => o.socket.disconnect())
                                .on('error', () => ({}))
                                .on('end', () => ({}))
                                .on('close', () => ({}))
                                .on('webchatsvc_message', data => o.messages.push({sender: SENDER.SERVER, time: new Date(), data: data.message_data}))
                                .on('webchatsvc_start', data => {
                                    if (data.result !== 'OK') return alert('로그인실패 :' + data.result + '; ' + data.result_data)
                                    $.isNumeric(o.botId) ? o.requestRootBlock() : o.requestIntro()
                                })
                        })
                    },
                    requestRootBlock() {
                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {message_type: 'bot_root_block', message_data: o.botId}))
                    },
                    requestIntro() {
                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {message_type: 'intro'}))
                    },
                    sendText() {
                        if (!o.input) return
                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {message_type: 'text', message_data: o.input}))
                        o.messages.push({sender: SENDER.USER, time: new Date(), data: JSON.parse(JSON.stringify(o.input))})
                        o.input = ''
                    },
                    actButton(message, button) {
                        console.log(message, button)
                        if (button.action === 'url') return window.open(button.next_action_data, null)

                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {
                            message_type: 'action', message_data: {
                                chatbot_id: message.data.chatbot_id,
                                parent_block_id: message.data.block_id,
                                btn_id: button.btn_id,
                                btn_name: button.btn_name,
                                action: button.action,
                                action_data: button.next_action_data,
                            },
                        }))
                    },
                    actApi(message, button) {
                        // TODO: API 버튼 형태 작업해야함: 현재 socket에서 api parameter 정보 제대로 내려주지 않고 있다
                        alert('현재 socket에서 api parameter 정보 제대로 내려주지 않고 있다.')
                    },
                    getMessageId() {
                        return o.request.user_key + '_' + (++o.eventSequence)
                    },
                    getButtonGroups(message) {
                        return message.data?.button?.reduce((list, e) => {
                            if (e.action === 'api') list.push(e)
                            else if (!list.length || !(list[list.length - 1] instanceof Array)) list.push([e])
                            else list[list.length - 1].push(e)
                            return list
                        }, [])
                    },
                    getListElements(display) {
                        return JSON.parse(JSON.stringify(display)).element?.sort((a, b) => (a.sequence - b.sequence)).splice(1)
                    },
                    getTimeFormat(value) {
                        return moment(value).format('YY-MM-DD HH:mm')
                    },
                },
                mounted() {
                    this.createSocket()
                }
            }).mount('#chat-preview')
            return o || o
        })()
    </script>
</tags:scripts>

<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>
</div>

</body>
</html>
