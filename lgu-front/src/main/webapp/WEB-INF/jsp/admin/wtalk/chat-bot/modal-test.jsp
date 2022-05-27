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

<div id="chat-preview" class="chat-preview window" ref="chatBody">
    <div class="header">
        <img src="<c:url value="/resources/images/chatbot-icon.svg"/>" class="chatbot-icon"><span class="customer-title">Chat Bot</span>
    </div>

    <div v-for="(message, iMessage) in messages" :key="iMessage" :class="message.sender === 'SERVER' ? ' editor ' : ' send-message '" class=" content "
         @mouseenter="highlight(message.data?.block_id)" @mouseleave="dehighlight">
        <template v-if="message.sender === 'SERVER' && message.messageType === 'api_result'">
            <div class="card">
                <p style="white-space: pre-wrap">{{ makeApiResultMessage(message.data) }}</p>
                <span class="time-text">{{ getTimeFormat(message.time) }}</span>
            </div>
        </template>
        <template v-else-if="message.sender === 'SERVER' && message.messageType !== 'api_result'">
            <div v-if="message.data?.display?.length" v-for="(e, i) in message.data?.display" :key="i" :class="e.type === 'text' ? 'sample-bubble' : 'card'">
                <p v-if="e.type === 'text'" style="white-space: pre-wrap">{{ e.element[0]?.content }}</p>
                <div v-if="e.type === 'image'" class="card-img">
                    <img :src="`${pageContext.request.contextPath}/admin/wtalk/chat-bot/image?fileName=` + encodeURIComponent(e.element[0]?.image)" class="border-radius-1em">
                </div>
                <div v-if="e.type === 'card'" class="card-img">
                    <img :src="`${pageContext.request.contextPath}/admin/wtalk/chat-bot/image?fileName=` + encodeURIComponent(e.element[0]?.image)" class="border-radius-top-1em">
                </div>
                <div v-if="e.type === 'card'" class="card-content">
                    <div class="card-title">{{ e.element[0]?.title }}</div>
                    <div class="card-text" style="white-space: pre-wrap">{{ e.element[0]?.content }}</div>
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
                                        <img :src="`${pageContext.request.contextPath}/admin/wtalk/chat-bot/image?fileName=` + encodeURIComponent(e2.image)">
                                    </div>
                                </div>
                                <div class="item-content">
                                    <div class="subject">{{ e2.title }}</div>
                                    <div class="ment" style="white-space: pre-wrap">{{ e2.content }}</div>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>
                <div v-if="e.type === 'input'" class="card-list">
                    <ul class="card-list-ul">
                        <div align="left" class="label" style="padding: 0.7em 1em; border-bottom: 1px solid #dcdcdc;">{{ e.element[0]?.title }}</div>
                        <li v-for="(param, k) in getListElements(e)"
                            class="item form">
                            <div align="left" class="label">{{ param.input_display_name }}</div>
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
                <span v-if="!(message.data?.button?.length) && (i + 1 === message.data.display.length)" class="time-text">{{ getTimeFormat(message.time) }}</span>
            </div>
            <div v-if="message.data?.button?.length" v-for="(e, i) in getButtonGroups(message)" :key="i" class="sample-bubble">
                <span v-if="e instanceof Array">
                    <button v-for="(e2, j) in e" :key="j" type="button" class="chatbot-button" @click.stop.prevent="actButton(message, e2)">{{ e2.btn_name }}</button>
                </span>
                <span v-else>
                    <button type="button" class="chatbot-button" @click.stop.prevent="actApi(message, e, $event)">{{ e.btn_name }}</button>
                </span>
                <span v-if="i + 1 === getButtonGroups(message).length" class="time-text">{{ getTimeFormat(message.time) }}</span>
            </div>
        </template>
        <template v-else>
            <div class="bubble-wrap">
                <div class="bubble-inner">
                    <p class="bubble" style="white-space: pre-wrap">{{ message.data }}</p>
                    <span class="time-text">{{ getTimeFormat(message.time) }}</span>
                </div>
            </div>
        </template>
    </div>

    <div v-if="socket" class="action">
        <div>
            <button type="button" class="home-btn" @click.stop.prevent="homeAction"><img src="<c:url value="/resources/images/material-home.svg"/>"></button>
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
                        lastReceiveMessageType: null,
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
                        timeout: null,
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
                                .on('webchatsvc_message', data => {
                                    console.log(data)
                                    o.lastReceiveMessageType = data.message_type
                                    o.messages.push({sender: SENDER.SERVER, time: new Date(), data: data.message_data, messageType: data.message_type})
                                })
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
                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {
                            message_type: 'text',
                            message_data: {last_receive_message_type: o.lastReceiveMessageType, text_data: o.input,}
                        }))
                        o.messages.push({sender: SENDER.USER, time: new Date(), data: JSON.parse(JSON.stringify(o.input)), messageType: 'text'})
                        o.input = ''
                    },
                    homeAction() {
                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {
                            message_type: 'text',
                            message_data: {last_receive_message_type: o.lastReceiveMessageType, text_data: '처음으로',}
                        }))
                    },
                    actButton(message, button) {
                        console.log(message, button)
                        if (button.action === 'url') return window.open(button.next_action_data, null)

                        o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {
                            message_type: 'action', message_data: {
                                last_receive_message_type: o.lastReceiveMessageType,
                                chatbot_id: message.data.chatbot_id,
                                parent_block_id: message.data.block_id,
                                btn_id: button.btn_id,
                                btn_name: button.btn_name,
                                action: button.action,
                                action_data: button.next_action_data,
                            },
                        }))
                    },
                    actApi(message, button, event) {
                        console.log(message, button, event)

                        $(event.target).closest('.card-list').asJsonData().done(result => {
                            const data = {}
                            for (let name in result) {
                                if (typeof result[name] === 'object') {
                                    data[name] = zeroPad((result[name].meridiem === 'am' ? 0 : 12) + parseInt(result[name].hour), '00') + ':' + zeroPad(result[name].minute, '00')
                                } else {
                                    data[name] = result[name]
                                }
                                if (!data[name]) return alert('API 호출을 위해 필요 정보를 모두 입력해야 합니다.')
                            }

                            o.socket.emit('webchatcli_message', Object.assign(o.request, {message_id: o.getMessageId()}, {
                                message_type: 'action', message_data: {
                                    last_receive_message_type: o.lastReceiveMessageType,
                                    chatbot_id: message.data.chatbot_id,
                                    parent_block_id: message.data.block_id,
                                    btn_id: button.btn_id,
                                    btn_name: button.btn_name,
                                    action: button.action,
                                    action_data: button.next_action_data,
                                    api_param_value: data
                                },
                            }))
                        })
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
                    makeApiResultMessage(data) {
                        if (!data.api_result_error_ment) {
                            const next_api_result_tpl = data.next_api_result_tpl
                            const api_result_body = data.api_result_body
                            const KEYWORD_CHAR = '$'
                            let result = ''

                            for (let position = 0; ;) {
                                const indexKeywordChar = next_api_result_tpl.indexOf(KEYWORD_CHAR, position)
                                if (indexKeywordChar < 0) {
                                    result += next_api_result_tpl.substr(position)
                                    break
                                }
                                const indexSpace = next_api_result_tpl.regexIndexOf(/\s/, indexKeywordChar)
                                if (indexKeywordChar + 1 !== indexSpace) {
                                    result += next_api_result_tpl.substr(position, indexKeywordChar - position)
                                    const keyword = next_api_result_tpl.substr(indexKeywordChar + 1, indexSpace - indexKeywordChar - 1)
                                    if (api_result_body[keyword] !== undefined) {
                                        result += api_result_body[keyword]
                                    } else {
                                        result += KEYWORD_CHAR + keyword
                                    }
                                } else {
                                    result += KEYWORD_CHAR
                                }
                                position = indexSpace
                            }

                            return result
                        } else {
                            return data.api_result_error_ment
                        }
                    },
                    debounce(func, wait = 500) {
                        clearTimeout(this.timeout)
                        this.timeout = setTimeout(func, wait)
                    },
                    highlight(blockId) {
                        if (!$.isNumeric(blockId)) return
                        $(window.parentWindowBlocks[blockId]).addClass('highlight')
                    },
                    dehighlight() {
                        for (let blockId in window.parentWindowBlocks)
                            $(window.parentWindowBlocks[blockId]).removeClass('highlight')
                    },
                },
                updated() {
                    $(this.$el).parent().find('.-datepicker').each(function () {
                        $(this).asDatepicker()
                    })
                    $(this.$el).parent().find('.-input-numerical').each(function () {
                        $(this).off('keyup')
                        $(this).on('keyup', function () {
                            return $(this).val($.onlyNumber(this));
                        })
                    })

                    const container = this.$el.parentElement
                    this.debounce(() => container.scroll({top: container.scrollHeight}), 100)
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
