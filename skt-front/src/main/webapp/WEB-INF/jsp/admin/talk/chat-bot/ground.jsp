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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/talk/chat-bot/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/talk/chat-bot/"))}</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>기간설정</th>
                                <td class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>봇이름</th>
                                <td>
                                    <div class="ui form">
                                        <form:input path="chatbotName"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                            <%--<h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>--%>
                        <div class="ui basic buttons">
                            <button type="button" class="ui button" onclick="chatbotAddPopup()">추가</button>
                            <button type="button" class="ui button -control-entity" data-entity="ChatBot" style="display: none" onclick="chatbotModifyPopup(getEntityId('ChatBot'))">수정</button>
                            <button type="button" class="ui button -control-entity" data-entity="ChatBot" style="display: none" onclick="chatbotDeletePopup(getEntityId('ChatBot'))">삭제</button>
                        </div>
                    </div>
                        <%--<div class="pull-right">
                            <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/talk/chat-bot/" pageForm="${search}"/>
                        </div>--%>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="ChatBot">
                            <%--<table class="ui celled table num-tbl unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="ChatBot">--%>
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>번호</th>
                            <th class="two wide">봇생성일</th>
                            <th class="two wide">봇생성자</th>
                            <th>봇 이름</th>
                            <th class="one wide">폴백</th>
                            <th class="one wide">복사</th>
                            <th class="one wide">테스트</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${status.index + 1}</td>
                                        <td>TODO: 2022-01-06 15:30:12</td>
                                        <td>TODO: 마스터</td>
                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td>
                                            <button class="ui button mini compact" onclick="fallbackBlockPopup(${e.id});">설정</button>
                                        </td>
                                        <td>
                                            <button class="ui button mini compact" onclick="chatbotCopyPopup(${e.id});">복사</button>
                                        </td>
                                        <td>
                                            <button class="ui button mini compact" onclick="chatbotTestPopup(${e.id});">테스트</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <%--<c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}" data-roomId="${g.htmlQuote(e.roomId)}" data-roomStatus="${g.htmlQuote(e.roomStatus)}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.roomName)}</td>
                                        <td>${g.htmlQuote(talkServices.get(e.senderKey))}</td>
                                        <td>${g.htmlQuote(g.messageOf('RoomStatus', e.roomStatus))}</td>
                                        <td>${g.htmlQuote(e.idName)}</td>
                                        <td>${g.htmlQuote(e.roomStartTime)}</td>
                                        <td>${g.htmlQuote(e.roomLastTime)}</td>
                                        <td>${g.htmlQuote(e.maindbCustomName == null || e.maindbCustomName == "" ? "미등록고객" : e.maindbCustomName)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>--%>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal tiny" id="chatbot-add-popup">
        <i class="close icon"></i>
        <div class="header">봇 추가</div>
        <div class="content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">멘트입력</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="field">
                                <textarea rows="3" readonly></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">동작선택</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select id="actionSelect">
                                <option>유형선택</option>
                                <option>처음으로 가기</option>
                                <option value="connectBlock">다른 블록으로 연결</option>
                                <option value="consultGroup">상담그룹 연결</option>
                                <option value="connectUrl">URL 연결</option>
                                <option value="connectNumber">전화 연결</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectBlock">
                    <div class="four wide column"><label class="control-label">연결 블록 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>블록선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="consultGroup">
                    <div class="four wide column"><label class="control-label">상담그룹</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>그룹선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectUrl">
                    <div class="four wide column"><label class="control-label">연결 URL 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input type="text" placeholder="URL을 입력하세요.">
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectNumber">
                    <div class="four wide column"><label class="control-label">연결 번호 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input type="text" placeholder="전화번호를 입력하세요.">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui brand button">확인</button>
        </div>
    </div>

    <div class="ui modal tiny" id="fallback-block-popup">
        <i class="close icon"></i>
        <div class="header">폴백블록</div>
        <div class="content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">멘트입력</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="field">
                                <textarea rows="3" readonly></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">동작선택</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select id="actionSelect">
                                <option>유형선택</option>
                                <option>처음으로 가기</option>
                                <option value="connectBlock">다른 블록으로 연결</option>
                                <option value="consultGroup">상담그룹 연결</option>
                                <option value="connectUrl">URL 연결</option>
                                <option value="connectNumber">전화 연결</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectBlock">
                    <div class="four wide column"><label class="control-label">연결 블록 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>블록선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="consultGroup">
                    <div class="four wide column"><label class="control-label">상담그룹</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>그룹선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectUrl">
                    <div class="four wide column"><label class="control-label">연결 URL 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input type="text" placeholder="URL을 입력하세요.">
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectNumber">
                    <div class="four wide column"><label class="control-label">연결 번호 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input type="text" placeholder="전화번호를 입력하세요.">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui brand button">확인</button>
        </div>
    </div>

    <%-- 미리보기 팝업--%>
    <div class="ui preview modal chatbot-modal" id="chat-preview">
        <i class="close icon"></i>
        <div class="header">미리보기</div>
        <div class="content">
            <div class="preview-header">
                <img src="<c:url value="/resources/images/chatbot-icon-orange.svg"/>" class="chatbot-icon"><span class="customer-title">Chat Bot</span>
            </div>

            <div class="preview-content" ref="chatBody">
                <div v-for="(message, iMessage) in messages" :key="iMessage" :class="message.sender === 'SERVER' ? ' editor ' : ' send-message '"
                     @mouseenter="highlight(message.data?.block_id)" @mouseleave="dehighlight">
                    <template v-if="message.sender === 'SERVER' && message.messageType === 'api_result'">
                        <div class="sample-bubble">
                            <p style="white-space: pre-wrap">{{ makeApiResultMessage(message.data.next_api_result_tpl, message.data.api_result_body) }}</p>
                            <span class="time-text">{{ getTimeFormat(message.time) }}</span>
                        </div>
                    </template>
                    <template v-else-if="message.sender === 'SERVER' && message.messageType !== 'api_result'">
                        <div v-if="message.data?.display?.length" v-for="(e, i) in message.data?.display" :key="i" :class="e.type === 'text' ? 'sample-bubble' : 'card'">
                            <p v-if="e.type === 'text'" style="white-space: pre-wrap">{{ e.element[0]?.content }}</p>
                            <div v-if="e.type === 'image'" class="card-img only">
                                <img :src="`${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e.element[0]?.image)" class="border-radius-1em">
                            </div>
                            <div v-if="e.type === 'card'" class="card-img">
                                <img :src="`${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e.element[0]?.image)" class="border-radius-top-1em">
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
                                                    <img :src="`${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e2.image)">
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
                            <span v-if="!(message.data?.button?.length) && (i + 1 === message.data.display.length)" class="time-text">{{ getTimeFormat(message.time) }}</span>
                        </div>
                        <div v-if="message.data?.button?.length" v-for="(e, i) in getButtonGroups(message)" :key="i" :class="e instanceof Array ? 'sample-bubble' : 'card'">
                            <button v-if="e instanceof Array" v-for="(e2, j) in e" :key="j" type="button" class="chatbot-button" @click.stop.prevent="actButton(message, e2)">{{ e2.btn_name }}</button>
                            <div v-else class="card-list">
                                <ul class="card-list-ul">
                                    <li v-if="e.api_param" v-for="(e2, j) in e.api_param" :key="j" class="item form">
                                        <div class="label">{{ e2.display_name }}</div>
                                        <div v-if="e2.type !== 'time'" class="ui fluid input">
                                            <input type="text" :name="e2.param_name" :class="(e2.type === 'calendar' && '-datepicker') || (e2.type === 'number' && '-input-numerical')">
                                        </div>
                                        <div v-else class="ui multi form">
                                            <select :name="e2.param_name + '.meridiem'" class="slt">
                                                <option value="am">오전</option>
                                                <option value="pm">오후</option>
                                            </select>
                                            <select :name="e2.param_name + '.hour'" class="slt">
                                                <option v-for="hour in 12" :key="hour" :value="hour - 1">{{ hour - 1 }}</option>
                                            </select>
                                            <span class="unit">시</span>
                                            <select :name="e2.param_name + '.minute'" class="slt">
                                                <option v-for="minute in 60" :key="minute" :value="minute - 1">{{ minute - 1 }}</option>
                                            </select>
                                            <span class="unit">분</span>
                                        </div>
                                    </li>
                                    <li class="item">
                                        <button type="button" class="chatbot-button" @click.stop.prevent="actApi(message, e, $event)">제출</button>
                                    </li>
                                </ul>
                            </div>
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
    </div>

    <tags:scripts>
        <script>
            function chatbotAddPopup() {
                alert('TODO: chatbotAddPopup()')
                $('#chatbot-add-popup').modalShow();
            }

            function chatbotDeletePopup(id) {
                confirm('정말 삭제하시겠습니까?').done(() => restSelf.delete('/api/chatbot/' + id).done(reload))
            }

            function chatbotCopyPopup(id) {
                confirm('봇을 복사하시겠습니까?').done(() => restSelf.post('/api/chatbot/' + id + '/copy').done(reload))
            }

            function fallbackBlockPopup(id) {
                alert('TODO: fallbackBlockPopup()')
                $('#fallback-block-popup').modalShow();
            }

            function chatbotModifyPopup(id) {
                alert('TODO: chatbotModifyPopup()')
                window.open('/sub-url/chatbot', '네이버팝업', 'width=1920px,height=1080px,scrollbars=yes');
            }

            function chatbotTestPopup(id) {
                $('#chat-preview').dragModalShow();
                preview.createSocket(id)
            }

            $(document).ready(function () {
                $('#actionSelect').change(function () {
                    var result = $('#actionSelect option:selected').val();
                    $('.fallback-action').removeClass('active');
                    $("#" + result).addClass('active');
                });
            });

            const preview = (() => {
                const SENDER = Object.freeze({SERVER: 'SERVER', USER: 'USER'})
                const o = Vue.createApp({
                    setup() {
                        return {
                            botId: '',
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
                        createSocket(botId) {
                            o.socket && o.socket.off('disconnect').disconnect()

                            o.botId = botId
                            o.timeout = null
                            o.eventSequence = 0
                            o.input = ''
                            o.messages = []

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
                        makeApiResultMessage(next_api_result_tpl, api_result_body) {
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
                }).mount('#chat-preview')
                return o || o
            })()
        </script>
    </tags:scripts>
</tags:tabContentLayout>
