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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<link rel="stylesheet" href="<c:url value="/resources/vendors/drawflow/0.0.53/drawflow.min.css?version=${version}"/>"/>
<tags:tabContentLayout>

    <div class="chatbot-main-container">
        <div class="chatbot-header" id="bot-list">
            <div class="header-left">
                <img src="<c:url value="/resources/images/chatbot-icon-white.svg"/>">
                <h2 class="caption">시나리오 봇 설정</h2>
            </div>
            <div class="header-right">
                <div class="checkbox-wrap">
                    <span class="grid">격자</span>
                    <div class="ui toggle checkbox">
                        <input type="checkbox" name="public" onchange="$('#drawflow').css({background: $(this).prop('checked') ? '' : 'transparent'})">
                    </div>
                </div>
                <div class="bar-zoom">
                    <span class="ratio">비율</span>
                    <button class="zoom-control-btn plus" type="button" onclick="editor.zoom_in()"><img src="<c:url value="/resources/images/zoom-plus.svg"/>" alt="zoom-plus"></button>
                    <button class="zoom-control-btn refresh" type="button" onclick="editor.zoom_reset()"><img src="<c:url value="/resources/images/zoom-refresh.svg"/>" alt="zoom-refresh"></button>
                    <button class="zoom-control-btn minus" type="button" onclick="editor.zoom_out()"><img src="<c:url value="/resources/images/zoom-minus.svg"/>" alt="zoom-minus"></button>
                </div>
                <button class="ui brand button" @click.stop.prevent="save">저장</button>
            </div>
        </div>
        <div id="drawflow" style="background: transparent" ondrop="drop(event)" ondragover="allowDrop(event)">
        </div>
    </div>

    <div id="block-template" style="display: none">
        <div class="header">
            <span>블록</span>
            <div class="ui toggle checkbox">
                <input type="checkbox" name="public" tabindex="0" class="hidden" v-model="isTemplateEnable">
                <label @click.stop.prevent="toggleTemplateEnable"></label>
            </div>
            <button class="preview-btn" @click.stop.prevent="showPreview">미리보기</button>
        </div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">블록이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="블록 이름을 입력하세요." v-model="name">
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">키워드</span></div>
                    <div class="twelve wide column">
                        <div class="display-flex mb10">
                            <input type="text" placeholder="키워드를 입력하세요." v-model="keywordInput">
                            <button class="action-btn ml5" @click.stop.prevent="addKeyword">추가</button>
                        </div>
                        <div class="keyword-box">
                            <span v-for="(e,i) in keywords" :key="i" class="ui label">{{ e }}<i @click.stop="removeKeyword(e)" class="delete icon"></i></span>
                        </div>
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">컨텐츠</span></div>
                    <div class="twelve wide column">
                        <ul class="content-list">
                            <li class="item">
                                <select v-model="displayTypeOption">
                                    <option value="text">텍스트 컨텐츠</option>
                                    <option value="image">이미지 컨텐츠</option>
                                    <option value="card">카드 컨텐츠 컨텐츠</option>
                                    <option value="list">리스트 컨텐츠</option>
                                </select>
                                <button @click.stop.prevent="addDisplayItem" class="action-btn">추가</button>
                            </li>

                            <template v-if="displays && displays.length">
                                <li v-for="(e,i) in displays" :key="i" class="item" :class="getDisplayClass(e.type)">
                                    <div class="contents-item">
                                        <div class="contents-title">{{ getDisplayText(e.type) }} 컨텐츠</div>
                                        <div class="contents-arrow-wrap">
                                            <button @click.stop.prevent="moveDownDisplayItem(i)" class="down-btn">▼</button>
                                            <button @click.stop.prevent="moveUpDisplayItem(i)" class="up-btn">▲</button>
                                        </div>
                                    </div>
                                    <button @click.stop.prevent="configDisplayItem(i)" class="action-btn">수정</button>
                                </li>
                            </template>
                        </ul>
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">버튼</span></div>
                    <div class="twelve wide column">
                        <ul class="content-list">
                            <li class="item">
                                <select v-model="buttonTypeOption">
                                    <option value="">다음 블록으로 연결</option>
                                    <option value="block">다른 블록으로 연결</option>
                                    <option value="member">상담원 연결</option>
                                    <option value="url">URL 연결</option>
                                    <option value="phone">전화 연결</option>
                                    <option value="api">외부연동</option>
                                </select>
                                <button @click.stop.prevent="addButtonItem" class="action-btn">추가</button>
                            </li>

                            <template v-if="buttons && buttons.length">
                                <li v-for="(e,i) in buttons" :key="i" class="item button">
                                    <div class="contents-item">
                                        <div class="contents-title">{{ e.name }}</div>
                                        <div class="contents-arrow-wrap">
                                            <button @click.stop.prevent="moveDownButtonItem(i)" class="down-btn">▼</button>
                                            <button @click.stop.prevent="moveUpButtonItem(i)" class="up-btn">▲</button>
                                        </div>
                                    </div>
                                    <button @click.stop.prevent="configButtonItem(i)" class="action-btn">수정</button>
                                </li>
                            </template>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="block-preview" class="ui preview modal">
        <i class="close icon"></i>
        <div class="header">미리보기</div>
        <div class="content">
            <div class="preview-header">
                <img src="<c:url value="/resources/images/chatbot-icon-orange.svg"/>" class="chatbot-icon"><span class="customer-title">Chat Bot</span>
            </div>
            <div class="preview-content">
                <div v-for="(e,i) in displays" :class="e.type === 'text' ? 'sample-bubble' : 'card'">
                    <p v-if="e.data && e.type === 'text'" style="white-space: pre-wrap">{{ e.data.text }}</p>
                    <div v-if="e.data && e.type === 'image'" class="card-img only">
                        <img :src="`${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e.data.fileUrl)" class="border-radius-1em">
                    </div>
                    <div v-if="e.data && e.type === 'card'" class="card-img">
                        <img :src="`${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e.data.fileUrl)" class="border-radius-top-1em">
                    </div>
                    <div v-if="e.data && e.type === 'card'" class="card-content">
                        <div class="card-title">{{ e.data.title }}</div>
                        <div class="card-text" style="white-space: pre-wrap">{{ e.data.announcement }}</div>
                    </div>
                    <div v-if="e.data && e.type === 'list'" class="card-list">
                        <div class="card-list-title">
                            <a v-if="e.data.titleUrl" :href="e.data.titleUrl" target="_blank">{{ e.data.title }}</a>
                            <text v-else>{{ e.data.title }}</text>
                        </div>
                        <ul class="card-list-ul">
                            <li v-for="(e2,j) in e.data.list" :key="j" class="item">
                                <a :href="e2.url" target="_blank" class="link-wrap">
                                    <div class="item-thumb" v-if="e2.fileUrl && e2.fileUrl.trim()">
                                        <div class="item-thumb-inner">
                                            <img :src="`${pageContext.request.contextPath}/admin/talk/chat-bot/image?fileName=` + encodeURIComponent(e2.fileUrl)">
                                        </div>
                                    </div>
                                    <div class="item-content">
                                        <div class="subject">{{ e2.title }}</div>
                                        <div class="ment" style="white-space: pre-wrap">{{ e2.announcement }}</div>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <span v-if="!(buttons?.length) && (i + 1 === displays.length)" class="time-text">21-04-11 04:33</span>
                </div>

                <div v-if="buttons.length" v-for="(e, i) in getButtonGroups()" :key="i" :class="e instanceof Array ? 'sample-bubble' : 'card'">
                    <button v-if="e instanceof Array" v-for="(e2, j) in e" :key="j" type="button" class="chatbot-button">{{ e2.name }}</button>
                    <div v-else class="card-list">
                        <ul class="card-list-ul">
                            <li v-for="(e2, j) in e.api.parameters" :key="j" class="item form">
                                <div class="label">{{ e2.name }}</div>
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
                                <button type="button" class="chatbot-button">제출</button>
                            </li>
                        </ul>
                    </div>
                    <span v-if="i + 1 === getButtonGroups().length" class="time-text">21-04-11 04:33</span>
                </div>
            </div>
        </div>
    </div>

    <div id="text-display-config" class="ui chatbot text-contents modal">
        <i class="close icon"></i>
        <div class="header">텍스트 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">멘트</span></div>
                    <div class="twelve wide column">
                        <textarea placeholder="멘트를 입력하세요." v-model="data.text"></textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button @click.stop.prevent="remove" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button modal-close">취소</button>
            <button @click.stop.prevent="save" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>
    <div id="image-display-config" class="ui chatbot image-contents modal">
        <i class="close icon"></i>
        <div class="header">이미지 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">이미지</span></div>
                    <div class="twelve wide column">
                        <div class="display-flex">
                            <div class="file-text-wrap">{{ data.fileName || '이미지를 추가해주세요.' }}</div>
                            <div class="filebox">
                                <input type="file" style="display: none" @change="uploadFile">
                                <label onclick="this.previousElementSibling.click()">추가</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button @click.stop.prevent="remove" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button modal-close">취소</button>
            <button @click.stop.prevent="save" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>
    <div id="card-display-config" class="ui chatbot card-contents modal">
        <i class="close icon"></i>
        <div class="header">카드 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">타이틀</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요." v-model="data.title">
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">멘트</span></div>
                    <div class="twelve wide column">
                        <textarea placeholder="멘트를 입력하세요." v-model="data.announcement"></textarea>
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">이미지</span></div>
                    <div class="twelve wide column">
                        <div class="display-flex">
                            <div class="file-text-wrap">{{ data.fileName || '이미지를 추가해주세요.' }}</div>
                            <div class="filebox">
                                <input type="file" style="display: none" @change="uploadFile">
                                <label onclick="this.previousElementSibling.click()">추가</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button @click.stop.prevent="remove" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button modal-close">취소</button>
            <button @click.stop.prevent="save" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>
    <div id="list-display-config" class="ui chatbot list-contents modal">
        <i class="close icon"></i>
        <div class="header">리스트 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">타이틀</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요." v-model="data.title">
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">타이틀 URL</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요." v-model="data.titleUrl">
                    </div>
                </div>
                <div class="row">
                    <div class="sixteen wide column">
                        <div v-for="(e,i) in data.list" :key="i" class="list-box">
                            <div class="list-header">
                                <div class="list-name">리스트 {{ i + 1 }}</div>
                                <button v-if="i === 0" @click.stop="addListItem" class="list-plus"></button>
                                <button v-if="i !== 0" @click.stop="removeListItem(i)" class="list-delete"></button>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">리스트 이름</div>
                                <div class="list-right">
                                    <input type="text" placeholder="리스트 이름을 입력하세요." v-model="e.title">
                                </div>
                            </div>
                            <div class="list-row align-items-start">
                                <div class="list-left">멘트</div>
                                <div class="list-right">
                                    <textarea placeholder="멘트를 입력하세요." v-model="e.announcement"></textarea>
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">URL</div>
                                <div class="list-right">
                                    <input type="text" placeholder="URL을 입력하세요." v-model="e.url">
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">이미지</div>
                                <div class="list-right">
                                    <div class="display-flex">
                                        <div class="file-text-wrap">{{ e.fileName || '이미지를 추가해주세요.' }}</div>
                                        <div class="filebox">
                                            <input type="file" style="display: none" @change="uploadFile($event, i)">
                                            <label onclick="this.previousElementSibling.click()">추가</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button @click.stop.prevent="remove" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button modal-close">취소</button>
            <button @click.stop.prevent="save" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <div id="button-config" class="ui chatbot modal">
        <i class="close icon"></i>
        <div v-if="data.action === ''" class="header">다음 블록으로 연결</div>
        <div v-if="data.action === 'block'" class="header">다른 블록으로 연결</div>
        <div v-if="data.action === 'member'" class="header">상담원 연결</div>
        <div v-if="data.action === 'url'" class="header">URL 연결</div>
        <div v-if="data.action === 'phone'" class="header">전화 연결</div>
        <div v-if="data.action === 'api'" class="header">외부연동</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div v-if="data.action !== 'api'" class="middle aligned row">
                    <div class="four wide column"><span class="subject">버튼 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="버튼 이름을 입력하세요." v-model="data.name">
                    </div>
                </div>
                <div v-if="data.action === 'block'" class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 블록</span></div>
                    <div class="twelve wide column">
                        <select v-model="data.nextBlockId">
                            <option v-for="(e,i) in blocks" :key="i" :value="e.id">{{ e.name }}</option>
                        </select>
                    </div>
                </div>
                <div v-if="data.action === 'member'" class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 그룹</span></div>
                    <div class="twelve wide column">
                        <select v-model="data.nextGroupId">
                            <option v-for="(e,i) in groups" :key="i" :value="e.groupId">{{ e.groupName }}</option>
                        </select>
                    </div>
                </div>
                <div v-if="data.action === 'url'" class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 URL</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="URL을 입력하세요." v-model="data.nextUrl">
                    </div>
                </div>
                <div v-if="data.action === 'phone'" class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 번호</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="URL을 입력하세요." v-model="data.nextPhone">
                    </div>
                </div>
                <div v-if="data.action === 'api'" class="ui divided grid">
                    <div class="eight wide column">
                        <div class="ui vertically divided grid">
                            <div class="middle aligned row">
                                <div class="four wide column"><span class="subject">버튼 이름</span></div>
                                <div class="twelve wide column">
                                    <input type="text" placeholder="버튼 이름을 입력하세요." v-model="data.name">
                                </div>
                            </div>
                            <div class="middle aligned row">
                                <div class="four wide column"><span class="subject">연동 URL</span></div>
                                <div class="twelve wide column">
                                    <input type="text" placeholder="URL을 입력하세요." v-model="data.api.nextApiUrl">
                                </div>
                            </div>
                            <div class="top aligned row">
                                <div class="four wide column"><span class="subject">안내 문구</span></div>
                                <div class="twelve wide column">
                                    <textarea placeholder="안내 문구를 입력하세요." rows="6" v-model="data.api.nextApiMent"></textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="sixteen wide column">
                                    <div class="answer-ment-check">
                                        <div>답변 멘트 사용</div>
                                        <div class="ui toggle checkbox">
                                            <input type="checkbox" name="public" class="hidden" @change="data.api.usingResponse = $event.target.checked" :checked="data.api.usingResponse">
                                            <label></label>
                                        </div>
                                    </div>
                                    <div class="list-box">
                                        <div class="list-header">
                                            <div class="list-name">답변 설정</div>
                                        </div>
                                        <div class="list-row align-items-start">
                                            <div class="list-left">정상</div>
                                            <div class="list-right">
                                                <textarea v-model="data.api.nextApiResultTemplate" rows="4"></textarea>
                                            </div>
                                        </div>
                                        <div class="list-row align-items-start">
                                            <div class="list-left">조회 불가</div>
                                            <div class="list-right">
                                                <textarea v-model="data.api.nextApiErrorMent"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="eight wide column">
                        <div class="list-box-wrap">
                            <div v-for="(e,i) in data.api.parameters" :key="i" class="list-box">
                                <div class="list-header">
                                    <div class="list-name">항목 {{ i + 1 }}</div>
                                    <button v-if="i === 0" @click.stop="addApiParameterItem" class="list-plus"></button>
                                    <button v-if="i !== 0" @click.stop="removeApiParameterItem(i)" class="list-delete"></button>
                                </div>
                                <div class="list-row align-items-center">
                                    <div class="list-left">타입</div>
                                    <div class="list-right">
                                        <select v-model="e.type">
                                            <option value="text">텍스트</option>
                                            <option value="number">숫자</option>
                                            <option value="calendar">날짜</option>
                                            <option value="time">시간</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="list-row align-items-center">
                                    <div class="list-left">항목 이름</div>
                                    <div class="list-right">
                                        <input type="text" placeholder="항목 이름을 입력하세요." v-model="e.name">
                                    </div>
                                </div>
                                <div class="list-row align-items-center">
                                    <div class="list-left">파라미터</div>
                                    <div class="list-right">
                                        <input type="text" placeholder="파라미터를 입력하세요." v-model="e.value">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button @click.stop.prevent="remove" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button modal-close">취소</button>
            <button @click.stop.prevent="save" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <div id="block-list"></div>
    <div id="fallback-config"></div>
    <div id="chatbot-setting-modal"></div>

    <tags:scripts>
        <script src="<c:url value="/resources/vendors/drawflow/0.0.53/drawflow.min.js?version=${version}"/>" data-type="library"></script>
        <script>
            const botList = (() => {
                const o = Vue.createApp({
                    data() {
                        return {current: '', select: '', bots: []}
                    },
                    methods: {
                        load() {
                            return restSelf.get('/api/chatbot/').done(response => {
                                o.bots = response.data
                            })
                        },
                        test() {
                            if (!$.isNumeric(o.current)) return alert('봇 시나리오가 선택되지 않았습니다.')
                            confirm('봇 테스트 선택 시 수정하신 내용은 자동으로 적용 됩니다.').done(() => o.save().done(() => o.loadBot(o.current).done(() => {
                                const popup = window.open(contextPath + '/admin/talk/chat-bot/' + o.current + '/modal-test', '_blank', 'width=420px,height=800px,top=100,left=100,scrollbars=yes,resizable=no')
                                popup.parentWindowBlocks = {}
                                for (let nodeId in nodeBlockMap)
                                    popup.parentWindowBlocks[nodeBlockMap[nodeId].id] = document.querySelector('#node-' + nodeId)
                            })))
                        },
                        copy() {
                            if (!$.isNumeric(o.current)) return alert('봇 시나리오가 선택되지 않았습니다.')
                            restSelf.post('/api/chatbot/' + o.current + '/copy').done(() => alert('봇 시나리오가 복사되었습니다.', o.load))
                        },
                        save() {
                            if (!fallbackConfig.data || !fallbackConfig.data.fallbackAction)
                                return alert('봇 시나리오가 생성(선택)되지 않았습니다.')

                            const convertBlock = block => ({
                                id: block?.id,
                                posX: block ? editor.getNodeFromId(block.nodeId).pos_x : 0,
                                posY: block ? editor.getNodeFromId(block.nodeId).pos_y : 0,
                                name: block?.name,
                                keyword: block?.keywords.length === 0 ? '' : block?.keywords.reduce((a, b) => (a + '|' + b)),
                                isTemplateEnable: block?.isTemplateEnable,
                                displayList: block?.displays.map((e, i) => ({
                                    order: i,
                                    type: e.type,
                                    elementList: e.type === 'text' ? [{order: 0, content: e.data?.text}]
                                        : e.type === 'image' ? [{order: 0, image: e.data?.fileUrl}]
                                            : e.type === 'card' ? [{order: 0, image: e.data?.fileUrl, title: e.data?.title, content: e.data?.announcement}]
                                                : [{order: 0, title: e.data?.title, url: e.data?.titleUrl}].concat(e.data?.list?.map((e2, j) =>
                                                    ({order: j + 1, title: e2.title, content: e2.announcement, url: e2.url, image: e2.fileUrl})))
                                })),
                                buttonList: block?.buttons.map((e, i) => ({
                                    order: i,
                                    buttonName: e.name,
                                    action: e.action,
                                    nextBlockId: e.action === '' ? nodeBlockMap[e.childNodeId].id : e.nextBlockId,
                                    nextGroupId: e.nextGroupId,
                                    nextUrl: e.nextUrl,
                                    nextPhone: e.nextPhone,
                                    nextApiUrl: e.api?.nextApiUrl,
                                    nextApiMent: e.api?.nextApiMent,
                                    isResultTemplateEnable: e.api?.usingResponse,
                                    nextApiResultTemplate: e.api?.nextApiResultTemplate,
                                    nextApiErrorMent: e.api?.nextApiErrorMent,
                                    paramList: e.api?.parameters?.map(e2 => ({type: e2.type, paramName: e2.value, displayName: e2.name})),
                                    // connectedBlockInfo: e.action === '' ? convertBlock(nodeBlockMap[e.childNodeId]) : null
                                })),
                                children: block?.buttons.filter(e => e.action === '').map(e => convertBlock(nodeBlockMap[e.childNodeId])),
                            })

                            const form = Object.assign({}, fallbackConfig.data, {blockInfo: convertBlock(blockList.blocks[0])})
                            if ($.isNumeric(o.current)) {
                                return restSelf.put('/api/chatbot/' + o.current, form).done(() => alert('저장되었습니다.', o.load))
                            } else {
                                return restSelf.post('/api/chatbot/', form).done(response => {
                                    o.current = response.data
                                    o.select = response.data
                                    alert('저장되었습니다.', o.load)
                                })
                            }
                        },
                        remove() {
                            if (!$.isNumeric(o.current)) return alert('봇 시나리오가 선택되지 않았습니다.')
                            confirm('삭제하시겠습니까?').done(() => restSelf.delete('/api/chatbot/' + o.current).done(() => alert('봇 시나리오가 삭제되었습니다.', () => {
                                o.init()
                                o.load()
                            })))
                        },
                        init() {
                            o.current = ''
                            o.select = ''
                            delete fallbackConfig.data
                            blockList.blocks.splice(0, blockList.blocks.length)
                            buttonConfig.blocks.splice(0, buttonConfig.blocks.length)
                            fallbackConfig.blocks.splice(0, fallbackConfig.blocks.length)
                            for (let property in nodeBlockMap) delete nodeBlockMap[property]
                            editor.clear()
                        },
                        loadBot(botId) {
                            return restSelf.get('/api/chatbot/' + botId).done(response => {
                                o.current = botId
                                o.select = botId

                                const data = response.data

                                blockList.blocks.splice(0, blockList.blocks.length)
                                buttonConfig.blocks.splice(0, buttonConfig.blocks.length)
                                fallbackConfig.blocks.splice(0, fallbackConfig.blocks.length)
                                for (let property in nodeBlockMap) delete nodeBlockMap[property]
                                editor.clear()

                                fallbackConfig.data = {
                                    name: data.name,
                                    enableCustomerInput: false,
                                    fallbackMent: data.fallbackMent,
                                    fallbackAction: data.fallbackAction,
                                    nextBlockId: data.nextBlockId,
                                    nextGroupId: data.nextGroupId,
                                    nextUrl: data.nextUrl,
                                    nextPhone: data.nextPhone
                                }
                                chatbotSettingModal.hide()

                                $('.chatbot-control-panel').removeClass('active')
                                $('.empty-panel').addClass('active')

                                const nodeIdToConnections = {}
                                const createBlock = block => {
                                    block.children?.forEach(e => createBlock(e))

                                    const nodeId = createNode(block.id, block.posX, block.posY)
                                    const app = nodeBlockMap[nodeId]

                                    app.name = block.name
                                    app.keywords = block.keyword?.split('|').filter(keyword => keyword) || []
                                    app.isTemplateEnable = block.isTemplateEnable
                                    app.displays = block.displayList.sort((a, b) => (a.order - b.order)).map(e => {
                                        if (e.type === 'text') return {type: 'text', data: {text: e.elementList?.[0]?.content}}
                                        if (e.type === 'image') return {type: 'image', data: {fileUrl: e.elementList?.[0]?.image, fileName: e.elementList?.[0]?.image}}
                                        if (e.type === 'card') return {
                                            type: 'card',
                                            data: {
                                                fileUrl: e.elementList?.[0]?.image,
                                                fileName: e.elementList?.[0]?.image,
                                                title: e.elementList?.[0]?.title,
                                                announcement: e.elementList?.[0]?.content,
                                            }
                                        }

                                        if (!e.elementList) return {type: 'list'}
                                        e.elementList.sort((a, b) => (a.order - b.order))
                                        return {
                                            type: 'list',
                                            data: {
                                                title: e.elementList[0]?.title,
                                                titleUrl: e.elementList[0]?.url,
                                                list: e.elementList.splice(1).map(e2 => ({title: e2.title, announcement: e2.content, url: e2.url, fileUrl: e2.image, fileName: e2.image,}))
                                            }
                                        }
                                    })

                                    app.buttons = block.buttonList.sort((a, b) => (a.order - b.order)).map((e, i) => {
                                        const childNodeId = (() => {
                                            if (e.action !== 'block' && e.action !== '') return
                                            const childBlockId = block.children?.filter(childBlock => (childBlock.parentButtonId === e.id))[0]?.id
                                            return blockList.blocks.filter(createdBlock => createdBlock.id === childBlockId)[0]?.nodeId
                                        })()
                                        const action = $.isNumeric(childNodeId) ? '' : e.action === 'block' || e.action === '' ? 'block' : e.action
                                        if (e.action === 'block' || e.action === '') {
                                            if (!nodeIdToConnections[nodeId]) nodeIdToConnections[nodeId] = {}
                                            nodeIdToConnections[nodeId][i] = e.nextBlockId
                                        }
                                        return {
                                            name: e.name,
                                            action: action,
                                            nextBlockId: action ? e.nextBlockId : null,
                                            childNodeId: action ? null : childNodeId,
                                            nextGroupId: e.nextGroupId,
                                            nextUrl: e.nextUrl,
                                            nextPhone: e.nextPhone,
                                            api: {
                                                nextApiUrl: e.nextApiUrl,
                                                nextApiMent: e.nextApiMent,
                                                usingResponse: e.isResultTemplateEnable,
                                                nextApiResultTemplate: e.nextApiResultTemplate,
                                                nextApiErrorMent: e.nextApiErrorMent,
                                                parameters: e.paramList?.sort((a, b) => (a.order - b.order)).map(e2 => ({type: e2.type, value: e2.paramName, name: e2.displayName}))
                                            }
                                        }
                                    })

                                    app.buttons.forEach(() => editor.addNodeOutput(nodeId))
                                }

                                if (data.blockInfo) createBlock(data.blockInfo)
                                else createBlock({id: 0, posX: 100, posY: 100, name: '', isTemplateEnable: false, displayList: [], buttonList: []})

                                for (let nodeId in nodeIdToConnections) {
                                    const app = nodeBlockMap[nodeId]
                                    const connections = nodeIdToConnections[nodeId]
                                    for (let buttonIndex in connections) {
                                        try {
                                            app.createConnection(parseInt(buttonIndex), connections[buttonIndex])
                                        } catch (e) {
                                            console.error(e)
                                        }
                                    }
                                }

                                lastBlockId = Math.max.apply(null, [0].concat(blockList.blocks.map(e => e.id))) + 1

                                setTimeout(function () {
                                    Object.keys(nodeBlockMap).forEach(nodeId => editor.updateConnectionNodes('node-' + nodeId))
                                }, 1000)
                            })
                        },
                        changeBot() {
                            const change = () => this.loadBot(o.select)

                            if (o.current && o.current !== o.select) {
                                confirm('저장되지 않은 내용은 모두 버려집니다. 변경하시겠습니까?')
                                    .done(() => {
                                        if (!o.select) o.init()
                                        else change()
                                    })
                                    .fail(() => (o.select = o.current))
                            } else if (o.current !== o.select) {
                                change()
                            }
                        },
                    },
                    mounted() {
                        this.load().done(() => {
                            <c:if test="${id != null}">
                            this.loadBot(${id})
                            </c:if>
                        })
                    }
                }).mount('#bot-list')
                return o || o
            })()
            const blockList = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            blocks: []
                        }
                    },
                    methods: {
                        highlight(block) {
                            for (let nodeId in nodeBlockMap)
                                if (nodeBlockMap[nodeId].id === block.id)
                                    $('#node-' + nodeId).addClass('highlight')
                        },
                        dehighlight() {
                            for (let nodeId in nodeBlockMap)
                                $('#node-' + nodeId).removeClass('highlight')
                        },
                    },
                }).mount('#block-list')
                return o || o
            })()
            const fallbackConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            groups: [],
                            blocks: [],
                            input: {name: '', enableCustomerInput: false, fallbackMent: '', fallbackAction: 'first', nextBlockId: null, nextGroupId: null, nextUrl: null, nextPhone: null,},
                        }
                    },
                    methods: {
                        save() {
                            for (let property in o.input) o.data[property] = o.input[property]
                        },
                        show() {
                            if (!o.data)
                                return alert('봇 시나리오가 생성되지 않았습니다.')

                            $('.chatbot-control-panel').removeClass('active')
                            $('.fallback-block-manage').addClass('active')
                            for (let property in o.data) o.input[property] = o.data[property]
                        }
                    }
                }).mount('#fallback-config')
                return o || o
            })()
            const chatbotSettingModal = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            groups: [],
                            name: '', enableCustomerInput: false, fallbackMent: '', fallbackAction: 'first', nextGroupId: null, nextUrl: null, nextPhone: null,
                        }
                    },
                    methods: {
                        show() {
                            o.name = ''
                            o.enableCustomerInput = false
                            o.fallbackMent = ''
                            o.fallbackAction = 'first'
                            o.nextGroupId = null
                            o.nextUrl = null
                            o.nextPhone = null

                            $('#chatbot-setting-modal').modal({
                                dimmerSettings: {opacity: 0},
                                duration: 350,
                                closable: false
                            }).modal('show')
                        },
                        start() {
                            botList.current = ''
                            botList.select = ''

                            blockList.blocks.splice(0, blockList.blocks.length)
                            buttonConfig.blocks.splice(0, buttonConfig.blocks.length)
                            fallbackConfig.blocks.splice(0, fallbackConfig.blocks.length)
                            for (let property in nodeBlockMap) delete nodeBlockMap[property]
                            editor.clear()

                            fallbackConfig.data = {
                                name: o.name,
                                enableCustomerInput: o.enableCustomerInput,
                                fallbackMent: o.fallbackMent,
                                fallbackAction: o.fallbackAction,
                                nextGroupId: o.nextGroupId,
                                nextUrl: o.nextUrl,
                                nextPhone: o.nextPhone
                            }
                            o.hide()
                            createNode()

                            $('.chatbot-control-panel').removeClass('active')
                            $('.empty-panel').addClass('active')
                        },
                        hide() {
                            $('#chatbot-setting-modal').modalHide()
                        }
                    }
                }).mount('#chatbot-setting-modal')
                return o || o
            })()
            const textDisplayConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            nodeId: null,
                            displayIndex: null,
                            data: {text: null},
                        }
                    },
                    methods: {
                        load(nodeId, displayIndex, data) {
                            o.nodeId = nodeId
                            o.displayIndex = displayIndex
                            o.data = {text: data?.text}
                        },
                        save() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'text') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {text: o.data.text}
                            $(o.$el.parentElement).modalHide()
                        },
                        remove() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'text') return
                            nodeBlockMap[o.nodeId].removeDisplayItem(o.displayIndex)
                            $(o.$el.parentElement).modalHide()
                        },
                    },
                }).mount('#text-display-config')
                return o || o
            })()
            const imageDisplayConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            nodeId: null,
                            displayIndex: null,
                            data: {fileName: null, fileUrl: null,},
                        }
                    },
                    methods: {
                        load(nodeId, displayIndex, data) {
                            o.nodeId = nodeId
                            o.displayIndex = displayIndex
                            o.data = {fileName: data?.fileName, fileUrl: data?.fileUrl}
                        },
                        save() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'image') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {fileName: o.data.fileName, fileUrl: o.data.fileUrl,}
                            $(o.$el.parentElement).modalHide()
                        },
                        remove() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'image') return
                            nodeBlockMap[o.nodeId].removeDisplayItem(o.displayIndex)
                            $(o.$el.parentElement).modalHide()
                        },
                        uploadFile(event) {
                            const file = event.target.files[0]
                            event.target.value = null
                            if (!file || !file.name) return
                            uploadFile(file).done(response => {
                                const originalName = response.data.originalName
                                restSelf.post('/api/chatbot/image', response.data).done(response => {
                                    o.data.fileName = originalName
                                    o.data.fileUrl = response.data
                                })
                            })
                        },
                    },
                }).mount('#image-display-config')
                return o || o
            })()
            const cardDisplayConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            nodeId: null,
                            displayIndex: null,
                            data: {fileName: null, fileUrl: null, title: null, announcement: null,},
                        }
                    },
                    methods: {
                        load(nodeId, displayIndex, data) {
                            o.nodeId = nodeId
                            o.displayIndex = displayIndex
                            o.data = {fileName: data?.fileName, fileUrl: data?.fileUrl, title: data?.title, announcement: data?.announcement,}
                        },
                        save() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'card') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {fileName: o.data.fileName, fileUrl: o.data.fileUrl, title: o.data.title, announcement: o.data.announcement,}
                            $(o.$el.parentElement).modalHide()
                        },
                        remove() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'card') return
                            nodeBlockMap[o.nodeId].removeDisplayItem(o.displayIndex)
                            $(o.$el.parentElement).modalHide()
                        },
                        uploadFile(event) {
                            const file = event.target.files[0]
                            event.target.value = null
                            if (!file || !file.name) return
                            uploadFile(file).done(response => {
                                const originalName = response.data.originalName
                                restSelf.post('/api/chatbot/image', response.data).done(response => {
                                    o.data.fileName = originalName
                                    o.data.fileUrl = response.data
                                })
                            })
                        },
                    },
                }).mount('#card-display-config')
                return o || o
            })()
            const listDisplayConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            nodeId: null,
                            displayIndex: null,
                            data: {title: null, titleUrl: null, list: [{title: null, announcement: null, url: null, fileName: null, fileUrl: null}],},
                        }
                    },
                    methods: {
                        load(nodeId, displayIndex, data) {
                            o.nodeId = nodeId
                            o.displayIndex = displayIndex
                            o.data = {title: data?.title, titleUrl: data?.titleUrl, list: []}
                            data && data.list && data.list.forEach(e => o.data.list.push({title: e?.title, announcement: e?.announcement, url: e?.url, fileName: e?.fileName, fileUrl: e?.fileUrl}))
                            if (!o.data.list || !o.data.list.length) o.data.list = [{title: null, announcement: null, url: null, fileName: null, fileUrl: null}]
                        },
                        save() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'list') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {title: o.data?.title, titleUrl: o.data?.titleUrl, list: []}
                            o.data.list.forEach(e => nodeBlockMap[o.nodeId].displays[o.displayIndex].data.list.push({
                                title: e?.title,
                                announcement: e?.announcement,
                                url: e?.url,
                                fileName: e?.fileName,
                                fileUrl: e?.fileUrl
                            }))
                            $(o.$el.parentElement).modalHide()
                        },
                        remove() {
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'list') return
                            nodeBlockMap[o.nodeId].removeDisplayItem(o.displayIndex)
                            $(o.$el.parentElement).modalHide()
                        },
                        addListItem() {
                            o.data.list.push({title: null, announcement: null, url: null, fileName: null, fileUrl: null})
                        },
                        removeListItem(index) {
                            if (index === 0) return
                            o.data.list.splice(index, 1)
                        },
                        uploadFile(event, index) {
                            const file = event.target.files[0]
                            event.target.value = null
                            if (!file || !file.name) return
                            uploadFile(file).done(response => {
                                const originalName = response.data.originalName
                                restSelf.post('/api/chatbot/image', response.data).done(response => {
                                    o.data.list[index].fileName = originalName
                                    o.data.list[index].fileUrl = response.data
                                })
                            })
                        },
                    },
                }).mount('#list-display-config')
                return o || o
            })()
            const buttonConfig = (() => {
                const API_PARAMETER_TYPES = Object.freeze({text: 'text', number: 'number', calendar: 'calendar', time: 'time'})
                const o = Vue.createApp({
                    data() {
                        return {
                            nodeId: null,
                            buttonIndex: null,
                            data: null,
                            blocks: [],
                            groups: [],
                        }
                    },
                    methods: {
                        load(nodeId, buttonIndex, data) {
                            o.nodeId = nodeId
                            o.buttonIndex = buttonIndex
                            o.data = {}
                            if (data) for (let property in data) o.data[property] = data[property]
                            o.data.api = {}
                            if (data.api) for (let property in data.api) o.data.api[property] = data.api[property]
                            o.data.api.parameters = []
                            data.api && data.api.parameters && data.api.parameters.forEach(e => o.data.api.parameters.push({type: e.type, name: e.name, value: e.value}))
                            o.checkDataStructure()

                            if (data.action === 'api') $(o.$el.parentElement).addClass('externaldb')
                            else $(o.$el.parentElement).removeClass('externaldb')
                        },
                        save() {
                            const app = nodeBlockMap[o.nodeId]
                            if (!app || !app.buttons[o.buttonIndex]) return

                            const prevAction = app.buttons[o.buttonIndex].action
                            const preChildNodeId = app.buttons[o.buttonIndex].childNodeId
                            const preBlock = app.buttons[o.buttonIndex].block
                            const currentAction = o.data.action

                            const data = {}
                            for (let property in o.data) data[property] = o.data[property]
                            data.api = {}
                            for (let property in o.data.api) data.api[property] = o.data.api[property]
                            data.api.parameters = []
                            o.data.api.parameters.forEach(e => data.api.parameters.push({type: e.type, name: e.name, value: e.value}))
                            app.buttons[o.buttonIndex] = data

                            if (prevAction !== currentAction) {
                                if (prevAction === '') {
                                    nodeBlockMap[preChildNodeId].delete()
                                } else if (prevAction === 'block') {
                                    app.removeConnection(o.buttonIndex)
                                }

                                if (currentAction === '') {
                                    const node = editor.getNodeFromId(o.nodeId)
                                    data.childNodeId = createNode(null, node.pos_x + 500, node.pos_y)
                                    app.createConnection(o.buttonIndex, nodeBlockMap[data.childNodeId].id)
                                } else if (currentAction === 'block') {
                                    app.createConnection(o.buttonIndex, data.nextBlockId)
                                }
                            } else if (currentAction === 'block' && preBlock !== data.nextBlockId) {
                                app.removeConnection(o.buttonIndex)
                                app.createConnection(o.buttonIndex, data.nextBlockId)
                            }

                            $(o.$el.parentElement).modalHide()
                        },
                        remove() {
                            const app = nodeBlockMap[o.nodeId]
                            app.removeButtonItem(o.buttonIndex)
                            $(o.$el.parentElement).modalHide()
                        },
                        checkDataStructure() {
                            if (!this.data) this.data = {}
                            if (this.data.name === undefined) this.data.name = null
                            if (this.data.action === undefined) this.data.action = null

                            if (this.data.nextBlockId === undefined) this.data.nextBlockId = null
                            if (this.data.nextGroupId === undefined) this.data.nextGroupId = null
                            if (this.data.nextUrl === undefined) this.data.nextUrl = null
                            if (this.data.nextPhone === undefined) this.data.nextPhone = null

                            if (!this.data.api) this.data.api = {}
                            if (this.data.api.nextApiUrl === undefined) this.data.api.nextApiUrl = null
                            if (this.data.api.nextApiMent === undefined) this.data.api.nextApiMent = null
                            if (!this.data.api.parameters || !this.data.api.parameters.length) this.data.api.parameters = [{type: API_PARAMETER_TYPES.text, name: null, value: null}]
                            if (this.data.api.usingResponse === undefined) this.data.api.usingResponse = false
                            if (this.data.api.nextApiResultTemplate === undefined) this.data.api.nextApiResultTemplate = null
                            if (this.data.api.nextApiErrorMent === undefined) this.data.api.nextApiErrorMent = null
                        },
                        addApiParameterItem() {
                            o.data.api.parameters.push({type: API_PARAMETER_TYPES.text, name: null, value: null})
                        },
                        removeApiParameterItem(index) {
                            if (index === 0) return
                            o.data.api.parameters.splice(index, 1)
                        },
                    },
                    created() {
                        this.checkDataStructure()
                    }
                }).mount('#button-config')
                return o || o
            })()
            const blockPreview = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            displays: [],
                            buttons: [],
                        }
                    },
                    methods: {
                        load(displays, buttons) {
                            o.displays = displays
                            o.buttons = buttons
                        },
                        getButtonGroups() {
                            return o.buttons.reduce((list, e) => {
                                if (e.action === 'api') list.push(e)
                                else if (!list.length || !(list[list.length - 1] instanceof Array)) list.push([e])
                                else list[list.length - 1].push(e)
                                return list
                            }, [])
                        },
                    },
                }).mount('#block-preview')
                return o || o
            })()

            const editor = new Drawflow(document.getElementById("drawflow"))
            editor.start();
            editor.container.addEventListener('keydown', event => event.stopImmediatePropagation(), true)
            editor.container.addEventListener('mousedown', event => event.target.classList.contains('output') && event.stopImmediatePropagation(), true)

            const nodeBlockMap = {}

            let lastBlockId = 0
            const createBlockId = () => (++lastBlockId)

            function createNode(blockId, x, y) {
                if (typeof x !== 'number' || typeof y !== 'number') {
                    editor.zoom_reset()
                    editor.canvas_x = 0
                    editor.canvas_y = 0
                    editor.pos_x = 0
                    editor.pos_y = 0
                    editor.editor_selected = true
                    editor.position({type: null, clientX: 0, clientY: 0})
                    editor.editor_selected = false

                    const rect = editor.container.getBoundingClientRect()
                    x = rect.width / 2 - 100 // note: 대충 초기 사이즈가 (200,200)쯤 될것이다.. 스타일에 따라 달라지니 중요하지 않다.
                    y = rect.height / 2 - 100
                }
                const nodeId = editor.addNode('BLOCK', 1, 0, x, y, '', {}, '')
                const template = document.getElementById('block-template')
                const block = document.getElementById('node-' + nodeId).querySelector('.drawflow_content_node')
                for (let i = 0; i < template.children.length; i++)
                    block.append(template.children[i].cloneNode(true))

                const app = (() => {
                    const o = Vue.createApp({
                        data() {
                            return {
                                id: blockId ?? createBlockId(),
                                name: '',
                                displays: [],
                                buttons: [],
                                keywords: [],
                                isTemplateEnable: false,

                                nodeId: nodeId,

                                keywordInput: '',
                                displayTypeOption: 'text',
                                buttonTypeOption: '',
                            }
                        },
                        methods: {
                            addKeyword() {
                                if (!o.keywordInput.trim()) return

                                if (o.keywords.includes(o.keywordInput.trim()))
                                    return alert('해당 키워드는 이미 목록에 존재합니다.')

                                const includedBlocks = blockList.blocks.filter(block => block.keywords.includes(o.keywordInput.trim()))
                                if (includedBlocks.length)
                                    return alert('해당 키워드는 [' + includedBlocks[0].name + '] 에서 사용되고 있습니다. 다른 키워드를 입력해주세요.')

                                o.keywords.push(o.keywordInput.trim())
                                o.keywordInput = ''
                            },
                            removeKeyword(keyword) {
                                if (!keyword || !keyword.trim()) return
                                o.keywords = o.keywords.filter(e => e !== keyword.trim())
                            },
                            getDisplayClass(type) {
                                const DISPLAY_TYPE_TO_CLASS = {text: 'text', image: 'image', card: 'card', list: 'list'}
                                return DISPLAY_TYPE_TO_CLASS[type]
                            },
                            getDisplayText(type) {
                                const DISPLAY_TYPE_TO_TEXT = {text: '텍스트', image: '이미지', card: '카드', list: '리스트'}
                                return DISPLAY_TYPE_TO_TEXT[type]
                            },
                            moveUpDisplayItem(index) {
                                $('#text-display-config,#image-display-config,#card-display-config,#list-display-config').each(function () {
                                    if ($(this).css('display') !== 'block') return
                                    alert('컨텐츠 수정 모달을 닫은 후 진행해 주십시오.')
                                    throw '컨텐츠 수정 모달을 닫은 후 진행해 주십시오.'
                                })

                                if (index <= 0) return
                                const item = o.displays.splice(index, 1)[0]
                                o.displays.splice(index - 1, 0, item)
                            },
                            moveDownDisplayItem(index) {
                                $('#text-display-config,#image-display-config,#card-display-config,#list-display-config').each(function () {
                                    if ($(this).css('display') !== 'block') return
                                    alert('컨텐츠 수정 모달을 닫은 후 진행해 주십시오.')
                                    throw '컨텐츠 수정 모달을 닫은 후 진행해 주십시오.'
                                })

                                if (index >= o.displays.length - 1) return
                                const item = o.displays.splice(index, 1)[0]
                                o.displays.splice(index + 1, 0, item)
                            },
                            removeDisplayItem(index) {
                                o.displays.splice(index, 1)
                            },
                            addDisplayItem() {
                                if (!o.displayTypeOption) return
                                o.displays.push({type: o.displayTypeOption})
                            },
                            moveUpButtonItem(index) {
                                if (index <= 0) return
                                const item = o.buttons.splice(index, 1)[0]
                                o.buttons.splice(index - 1, 0, item)

                                const outputs = editor.getNodeFromId(o.nodeId).outputs
                                const targetOutputClass = o.getOutputClass(index)
                                const destinationOutputClass = o.getOutputClass(index - 1)

                                outputs[targetOutputClass].connections.forEach(e => {
                                    editor.removeSingleConnection(o.nodeId, e.node, targetOutputClass, e.output)
                                    editor.addConnection(o.nodeId, e.node, destinationOutputClass, e.output)
                                })
                                outputs[destinationOutputClass].connections.forEach(e => {
                                    editor.removeSingleConnection(o.nodeId, e.node, destinationOutputClass, e.output)
                                    editor.addConnection(o.nodeId, e.node, targetOutputClass, e.output)
                                })

                                if (buttonConfig.nodeId === o.nodeId) {
                                    if (buttonConfig.buttonIndex === index) {
                                        buttonConfig.buttonIndex = index - 1
                                    } else if (buttonConfig.buttonIndex === index - 1) {
                                        buttonConfig.buttonIndex = index
                                    }
                                }
                            },
                            moveDownButtonItem(index) {
                                if (index >= o.buttons.length - 1) return
                                const item = o.buttons.splice(index, 1)[0]
                                o.buttons.splice(index + 1, 0, item)

                                const outputs = editor.getNodeFromId(o.nodeId).outputs
                                const targetOutputClass = o.getOutputClass(index)
                                const destinationOutputClass = o.getOutputClass(index + 1)

                                outputs[targetOutputClass].connections.forEach(e => {
                                    editor.removeSingleConnection(o.nodeId, e.node, targetOutputClass, e.output)
                                    editor.addConnection(o.nodeId, e.node, destinationOutputClass, e.output)
                                })
                                outputs[destinationOutputClass].connections.forEach(e => {
                                    editor.removeSingleConnection(o.nodeId, e.node, destinationOutputClass, e.output)
                                    editor.addConnection(o.nodeId, e.node, targetOutputClass, e.output)
                                })

                                if (buttonConfig.nodeId === o.nodeId) {
                                    if (buttonConfig.buttonIndex === index) {
                                        buttonConfig.buttonIndex = index + 1
                                    } else if (buttonConfig.buttonIndex === index + 1) {
                                        buttonConfig.buttonIndex = index
                                    }
                                }
                            },
                            removeButtonItem(index) {
                                const removedButton = o.buttons.splice(index, 1)[0]

                                editor.removeNodeOutput(o.nodeId, o.getOutputClass(index))

                                if (removedButton.action === '')
                                    nodeBlockMap[removedButton.childNodeId].delete()
                            },
                            addButtonItem() {
                                const button = {action: o.buttonTypeOption, name: ''}
                                o.buttons.push(button)
                                editor.addNodeOutput(o.nodeId)

                                if (o.buttonTypeOption === '') {
                                    const app = nodeBlockMap[o.nodeId]
                                    const node = editor.getNodeFromId(o.nodeId)
                                    button.childNodeId = createNode(null, node.pos_x + 500, node.pos_y)
                                    app.createConnection(o.buttons.length - 1, nodeBlockMap[button.childNodeId].id)
                                }
                            },
                            showPreview() {
                                $('#block-preview').dragModalShow()

                                blockPreview.load(o.displays, o.buttons)
                            },
                            createConnection(buttonIndex, blockId) {
                                const next = ((blockId) => {
                                    for (let nodeId in nodeBlockMap)
                                        if (nodeBlockMap[nodeId].id === blockId)
                                            return {nodeId: nodeId, blockId: nodeBlockMap[nodeId].id, inputClass: nodeBlockMap[nodeId].getInputClass()}
                                })(blockId)
                                editor.addConnection(o.nodeId, next.nodeId, o.getOutputClass(buttonIndex), next.inputClass)
                            },
                            removeConnection(buttonIndex) {
                                const outputs = editor.getNodeFromId(o.nodeId).outputs
                                const outputClass = o.getOutputClass(buttonIndex)
                                outputs[outputClass].connections.forEach(e => editor.removeSingleConnection(o.nodeId, e.node, outputClass, e.output))
                            },
                            getOutputClass(buttonIndex) {
                                return Object.keys(editor.getNodeFromId(o.nodeId).outputs)[buttonIndex]
                            },
                            getInputClass() {
                                return Object.keys(editor.getNodeFromId(o.nodeId).inputs)[0]
                            },
                            delete() {
                                o.buttons.filter(e => e.action === '').forEach(e => nodeBlockMap[e.childNodeId].delete())
                                editor.removeNodeId('node-' + o.nodeId)
                                o.$.appContext.app.unmount()
                            },
                            configDisplayItem(index) {
                                const item = o.displays[index]
                                if (!item?.type) return

                                $('#text-display-config,#image-display-config,#card-display-config,#list-display-config').modalHide()
                                if (item.type === 'text') {
                                    $('#text-display-config').dragModalShow()
                                    textDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (item.type === 'image') {
                                    $('#image-display-config').dragModalShow()
                                    imageDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (item.type === 'card') {
                                    $('#card-display-config').dragModalShow()
                                    cardDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (item.type === 'list') {
                                    $('#list-display-config').dragModalShow()
                                    listDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                            },
                            configButtonItem(index) {
                                $('#button-config').dragModalShow()
                                buttonConfig.load(o.nodeId, index, o.buttons[index])
                            },
                            toggleTemplateEnable() {
                                o.isTemplateEnable = !o.isTemplateEnable
                            },
                        },
                        updated() {
                            editor.updateConnectionNodes('node-' + o.nodeId)
                        },
                        mounted() {
                        }
                    }).mount(block)
                    return o || o
                })()
                nodeBlockMap[nodeId] = app
                blockList.blocks.push(app)
                buttonConfig.blocks.push(app)
                fallbackConfig.blocks.push(app)

                blockList.blocks.sort((a, b) => (a.id - b.id))
                buttonConfig.blocks.sort((a, b) => (a.id - b.id))
                fallbackConfig.blocks.sort((a, b) => (a.id - b.id))

                return nodeId
            }

            editor.on('nodeRemoved', (nodeId) => {
                const app = nodeBlockMap[nodeId]
                blockList.blocks.splice(blockList.blocks.indexOf(app), 1)
                buttonConfig.blocks.splice(buttonConfig.blocks.indexOf(app), 1)
                fallbackConfig.blocks.splice(fallbackConfig.blocks.indexOf(app), 1)
                delete nodeBlockMap[nodeId]
            })

            restSelf.get('/api/talk-reception-group/').done(response => {
                chatbotSettingModal.groups = response.data
                fallbackConfig.groups = response.data
                buttonConfig.groups = response.data
            })

            const allowDrop = event => event.preventDefault()

            $('.chatbot-control-container .arrow-button').click(function () {
                $(this).toggleClass('show')
                $(this).parent('.chatbot-control-container').toggleClass('active')
            })
        </script>
    </tags:scripts>
</tags:tabContentLayout>
