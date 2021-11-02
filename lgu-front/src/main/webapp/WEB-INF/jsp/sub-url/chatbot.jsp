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

<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/jerosoler/Drawflow/dist/drawflow.min.css">
<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <div class="menu-tab">
            <div class="inner">
                <ul>
                    <li><a href="#" class="tab-on tab-indicator">채팅 봇</a></li>
                    <li>
                        <button type="button" onclick="createNode()">블락 생성</button>
                    </li>
                </ul>
            </div>
        </div>
        <div class="sub-content ui container fluid unstackable">
            <div class="tab-content active">
                <div class="panel">
                    <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                        <div>
                            <h3 class="panel-title"><img src="<c:url value="/resources/images/chatbot-square.svg"/>" class="vertical-align-middle mr5"> 봇 에디터</h3>
                        </div>
                        <div class="dp-flex">
                            <div class="bot-select-wrap">
                                <span>봇 리스트</span>
                                <div class="ui form">
                                    <select>
                                        <option></option>
                                    </select>
                                </div>
                                <button type="button" class="ui mini button">봇 추가</button>
                            </div>
                            <button type="button" class="ui mini button" onclick="botCopyPopup();">봇 복사</button>
                            <button type="button" class="ui mini button" onclick="botTestPopup();">봇 테스트</button>
                            <button type="button" class="ui mini button" onclick="botSavePopup();">봇 저장</button>
                        </div>
                    </div>
                    <div class="panel-body chatbot remove-padding flex-100">
                        <div class="chatbot-container">
                            <div class="chatbot-node-container">
                                <div class="chatbot-box-label">디스플레이</div>
                                <div class="pd13">
                                    <div class="display-item text" draggable="true" ondragstart="event.dataTransfer.setData('node', 'display-item-text')">
                                        <img src="<c:url value="/resources/images/item-text-icon.svg"/>"> 텍스트
                                    </div>
                                    <div class="display-item image" draggable="true" ondragstart="event.dataTransfer.setData('node', 'display-item-image')">
                                        <img src="<c:url value="/resources/images/item-image-icon.svg"/>"> 이미지
                                    </div>
                                    <div class="display-item card" draggable="true" ondragstart="event.dataTransfer.setData('node', 'display-item-card')">
                                        <img src="<c:url value="/resources/images/item-card-icon.svg"/>"> 카드
                                    </div>
                                    <div class="display-item list" draggable="true" ondragstart="event.dataTransfer.setData('node', 'display-item-list')">
                                        <img src="<c:url value="/resources/images/item-list-icon.svg"/>"> 리스트
                                    </div>
                                </div>
                                <div class="chatbot-box-label">블록리스트</div>
                                <div>
                                    <div class="chatbot-box-sub-label blue">특수 블록</div>
                                    <div class="block-list-container">
                                        <ul class="block-list-ul">
                                            <li class="block-list">
                                                <div class="block-name">폴백 블록</div>
                                                <div class="block-control">
                                                    <button type="button" class="ui mini button remove-margin" onclick="fallbackConfig.show()">수정</button>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="chatbot-box-sub-label skyblue">일반 블록</div>
                                    <div class="block-list-container">
                                        <ul id="block-list" class="block-list-ul">
                                            <li v-for="(e,i) in blocks" :key="i" class="block-list">
                                                <div class="block-name">{{e.name}}</div>
                                                <div v-if="e.autoReply" class="block-control">
                                                    <img src="<c:url value="/resources/images/chatbot-square-mini.svg"/>">
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="chatbot-main-container flex-100">
                                <div id="drawflow" ondragover="allowDrop(event)">
                                    <div class="ui modal bot-setting-popup" >
                                        <div class="header">
                                            봇 기본설정
                                        </div>
                                        <div class="content">
                                            <div class="mb10">이름</div>
                                            <div class="ui form fluid mb15">
                                                <input type="text">
                                            </div>
                                            <div class="mb10">폴백 멘트 입력</div>
                                            <div class="ui form fluid mb10">
                                                <textarea rows="3"></textarea>
                                            </div>
                                            <div class="mb10">동작</div>
                                            <div class="ui form fluid mb10">
                                                <select>
                                                    <option>처음으로가기</option>
                                                    <option>상담그룹연결</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="actions">
                                            <button type="button" class="ui small compact button modal-close">취소</button>
                                            <button type="button" class="ui small compact brand button">시작</button>
                                        </div>
                                    </div>
                                    <div class="bar-zoom">
                                        <button class="zoom-control-btn" type="button" onclick="editor.zoom_in()"><img src="<c:url value="/resources/images/zoom-plus.svg"/>"></button>
                                        <button class="zoom-control-btn" type="button" onclick="editor.zoom_reset()"><img src="<c:url value="/resources/images/zoom-refresh.svg"/>"></button>
                                        <button class="zoom-control-btn" type="button" onclick="editor.zoom_out()"><img src="<c:url value="/resources/images/zoom-minus.svg"/>"></button>
                                    </div>
                                </div>
                            </div>
                            <div class="chatbot-control-panel active">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">설정영역</div>
                                        <div class="chatbot-control-body">
                                            <div class="empty">선택된 내용이 없습니다.</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="fallback-config" class="chatbot-control-panel fallback-block-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">폴백 블록 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">이름</div>
                                            <div class="ui form fluid mb15">
                                                <input type="text" v-model="input.name">
                                            </div>
                                            <div class="mb15">대사 입력</div>
                                            <div class="ui form fluid mb15">
                                                <textarea rows="8" v-model="input.announcement"></textarea>
                                            </div>
                                            <div class="mb15">동작</div>
                                            <div class="ui form fluid mb15">
                                                <select v-model="input.action">
                                                    <option value="GOTO_ROOT">처음으로 가기</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="keyword-config" class="chatbot-control-panel keyword-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">키워드 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">키워드 생성</div>
                                            <div class="ui action fluid input mb15">
                                                <input type="text" placeholder="키워드 입력" v-model="input">
                                                <button @click.stop="addKeyword" class="ui button">추가</button>
                                            </div>
                                            <div class="mb15">키워드 목록</div>
                                            <div class="mb15">
                                                <span v-for="(e,i) in keywords" :key="i" class="ui basic large label">{{ e }}<i @click.stop="removeKeyword(e)" class="delete icon"></i></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="text-display-config" class="chatbot-control-panel text-display-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">텍스트 디스플레이 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">대사 입력</div>
                                            <div class="ui form fluid mb15">
                                                <textarea rows="8" v-model="data.text"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="image-display-config" class="chatbot-control-panel img-display-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">이미지 디스플레이 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">이미지 삽입</div>
                                            <input type="file" style="display: none" @change="uploadFile">
                                            <div class="ui action fluid input mb15" onclick="this.previousElementSibling.click()">
                                                <input type="text" readonly v-model="data.fileName">
                                                <label class="ui icon button">찾아보기</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="card-display-config" class="chatbot-control-panel card-display-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">카드 디스플레이 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">이미지 삽입</div>
                                            <input type="file" style="display: none" @change="uploadFile">
                                            <div class="ui action fluid input mb15" onclick="this.previousElementSibling.click()">
                                                <input type="text" readonly v-model="data.fileName">
                                                <label class="ui icon button">찾아보기</label>
                                            </div>
                                            <div class="mb15">타이틀 입력</div>
                                            <div class="ui form fluid mb15">
                                                <input type="text" v-model="data.title">
                                            </div>
                                            <div class="mb15">대사 입력</div>
                                            <div class="ui form fluid mb15">
                                                <textarea rows="8" v-model="data.announcement"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="list-display-config" class="chatbot-control-panel list-display-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">리스트 디스플레이 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">타이틀 입력</div>
                                            <div class="ui form fluid mb15">
                                                <input type="text" v-model="data.title">
                                            </div>
                                            <div class="mb15">타이틀 URL 입력</div>
                                            <div class="ui form fluid mb15">
                                                <input type="text" v-model="data.titleUrl">
                                            </div>
                                            <div class="mb15">리스트 설정</div>
                                            <div v-for="(e,i) in data.list" :key="i" class="list-control-container mb15">
                                                <div class="list-control-header">
                                                    <div>리스트{{ i + 1 }}</div>
                                                    <button v-if="i === 0" @click.stop="addListItem" class="ui icon small compact black button"><i class="plus icon"></i></button>
                                                    <button v-if="i !== 0" @click.stop="removeListItem(i)" class="ui icon small compact brand button"><i class="x icon"></i></button>
                                                </div>
                                                <table class="list-control-table">
                                                    <tr>
                                                        <th>제목</th>
                                                        <td>
                                                            <div class="ui form fluid">
                                                                <input type="text" v-model="e.title">
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>대사</th>
                                                        <td>
                                                            <div class="ui form fluid">
                                                                <textarea rows="3" v-model="e.announcement"></textarea>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>URL</th>
                                                        <td>
                                                            <div class="ui form fluid">
                                                                <input type="text" v-model="e.url">
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>이미지</th>
                                                        <td>
                                                            <input type="file" style="display: none" @change="uploadFile($event, i)">
                                                            <div class="ui action fluid input" onclick="this.previousElementSibling.click()">
                                                                <input type="text" readonly v-model="e.fileName">
                                                                <label class="ui icon button">찾아보기</label>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="button-config" class="chatbot-control-panel button-action-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner">
                                        <div class="chatbot-box-label">버튼 동작 관리
                                            <button class="ui mini button" @click.stop="save">저장</button>
                                        </div>
                                        <div class="chatbot-control-body">
                                            <div class="mb15">버튼 이름</div>
                                            <div class="ui form fluid mb15">
                                                <input type="text" v-model="data.name">
                                            </div>
                                            <div class="mb15">버튼 액션</div>
                                            <div class="ui form fluid mb15">
                                                <select v-model="data.action">
                                                    <option value="TO_NEXT_BLOCK">다음 블록으로 연결</option>
                                                    <option value="TO_OTHER_BLOCK">다른 블록으로 연결</option>
                                                    <option value="CALL_CONSULTANT">상담원 연결</option>
                                                    <option value="TO_URL">URL 연결</option>
                                                    <option value="MAKE_TEL_CALL">전화 연결</option>
                                                    <option value="CALL_API">외부연동</option>
                                                </select>
                                            </div>
                                            <div v-if="data.action === 'TO_OTHER_BLOCK'">
                                                <div class="mb15">연결 블록 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <select v-model="data.block">
                                                        <option v-for="(e,i) in blocks" :key="i" :value="e.id">{{ e.name }}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'CALL_CONSULTANT'">
                                                <div class="mb15">연결 그룹 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <select>
                                                        <option v-for="(e,i) in groups" :key="i" :value="e.name">{{ e.hanName }}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'TO_URL'">
                                                <div class="mb15">연결 URL 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text" v-model="data.url">
                                                    </div>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'MAKE_TEL_CALL'">
                                                <div class="mb15">연결 번호 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text" v-model="data.tel">
                                                    </div>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'CALL_API'">
                                                <div class="mb15">외부연동 URL 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text" v-model="data.api.url">
                                                    </div>
                                                </div>
                                                <div class="mb15">안내문구 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="5" v-model="data.api.description"></textarea>
                                                </div>
                                                <div class="mb15">항목설정</div>
                                                <div v-for="(e,i) in data.api.parameters" :key="i" class="list-control-container mb15">
                                                    <div class="list-control-header">
                                                        <div>항목{{ i + 1 }}</div>
                                                        <button v-if="i === 0" @click.stop="addApiParameterItem" class="ui icon small compact black button"><i class="plus icon"></i></button>
                                                        <button v-if="i !== 0" @click.stop="removeApiParameterItem(i)" class="ui icon small compact brand button"><i class="x icon"></i></button>
                                                    </div>
                                                    <table class="list-control-table">
                                                        <tr>
                                                            <th>타입</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <select v-model="e.type">
                                                                        <option value="TEXT">텍스트</option>
                                                                        <option value="NUMERICAL">숫자</option>
                                                                        <option value="DATE">날짜</option>
                                                                        <option value="TIME">시간</option>
                                                                    </select>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>항목명</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text" v-model="e.name">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>파라미터</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text" v-model="e.value">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="mb15 dp-flex align-items-center justify-content-space-between">
                                                    <div>답변 대사 사용</div>
                                                    <div class="ui fitted toggle checkbox">
                                                        <input type="checkbox" @change="data.api.usingResponse = $event.target.checked">
                                                        <label></label>
                                                    </div>
                                                </div>
                                                <div class="list-control-container mb15">
                                                    <div class="list-control-header">
                                                        <div>답변 설정</div>
                                                    </div>
                                                    <table class="list-control-table">
                                                        <tr>
                                                            <th>정상</th>
                                                            <td>
                                                                <div class="ui form fluid mb15">
                                                                    <textarea rows="4" v-model="data.api.outputFormat"></textarea>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>조회불가</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text" v-model="data.api.errorOutput">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div id="block-preview" class="chatbot-control-panel chat-preview-manage">
                                <div class="chatbot-control-container active">
                                    <button type="button" class="arrow-button"></button>
                                    <div class="chatbot-control-inner chatbot-ui">
                                        <div class="chatbot-box-label">미리보기<%--<button type="button" class="ui mini button">새로고침</button>--%></div>
                                        <div class="chatbot-control-body">
                                            <div class="chat-preview">
                                                <div class="header">
                                                    <img src="<c:url value="/resources/images/chatbot-icon.svg"/>" class="chatbot-icon">
                                                    <span class="customer-title">Chat Bot</span>
                                                </div>
                                                <div class="content editor">
                                                    <div v-for="(e,i) in displays" :class="e.type === 'TEXT' ? 'sample-bubble' : 'card'">
                                                        <p v-if="e.data && e.type === 'TEXT'">{{ e.data.text }}</p>
                                                        <div v-if="e.data && e.type === 'IMAGE'" class="card-img">
                                                            <img :src="e.data.fileUrl" class="border-radius-1em">
                                                        </div>
                                                        <div v-if="e.data && e.type === 'CARD'" class="card-img">
                                                            <img :src="e.data.fileUrl" class="border-radius-top-1em">
                                                        </div>
                                                        <div v-if="e.data && e.type === 'CARD'" class="card-content">
                                                            <div class="card-title">{{ e.data.title }}</div>
                                                            <div class="card-text">{{ e.data.announcement }}</div>
                                                        </div>
                                                        <div v-if="e.data && e.type === 'LIST'" class="card-list">
                                                            <div class="card-list-title">
                                                                <a v-if="titleUrl" :href="e.data.titleUrl" target="_blank">{{ e.data.title }}</a>
                                                                <text v-else>{{ e.data.title }}</text>
                                                            </div>
                                                            <ul class="card-list-ul">
                                                                <li v-for="(e2,j) in e.data.list" :key="j" class="item">
                                                                    <a :href="e2.url" target="_blank" class="link-wrap">
                                                                        <div class="item-thumb" v-if="e2.fileUrl && e2.fileUrl.trim()">
                                                                            <div class="item-thumb-inner">
                                                                                <img :src="e2.fileUrl">
                                                                            </div>
                                                                        </div>
                                                                        <div class="item-content">
                                                                            <div class="subject">{{ e2.title }}</div>
                                                                            <div class="ment">{{ e2.announcement }}</div>
                                                                        </div>
                                                                    </a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                        <span class="time-text">21-04-11 04:33</span>
                                                    </div>

                                                    <div v-if="buttons.length" v-for="(e, i) in getButtonGroups()" :key="i" :class="e instanceof Array ? 'sample-bubble' : 'card'">
                                                        <button v-if="e instanceof Array" v-for="(e2, j) in e" :key="j" type="button" class="chatbot-button">{{ e2.name }}</button>
                                                        <div v-else class="card-list">
                                                            <ul class="card-list-ul">
                                                                <li v-for="(e2, j) in e.api.parameters" :key="j" class="item form">
                                                                    <div class="label">{{ e2.name }}</div>
                                                                    <div v-if="e2.type !== 'TIME'" class="ui fluid input">
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
                                                        <span class="time-text">21-04-11 04:33</span>
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

    <div id="block-template" style="display: none">
        <div class="title-box">
            <div class="mb10">
                <input type="text" placeholder="블록명입력" v-model="name">
            </div>
            <div class="btn-wrap">
                <button @click.stop="configKeywords" class="ui tiny compact button">키워드 관리</button>
                <button @click.stop="showPreview" class="ui tiny compact button">미리보기</button>
                <button @click.stop="autoReply = !autoReply" class="ui tiny compact button preview-button" :class="autoReply && ' active'">{{ autoReply ? 'ON' : 'OFF' }}</button>
            </div>
        </div>
        <div class="box">
            <div class="inner">
                <ul v-if="displays && displays.length" class="button-item-ul">
                    <li v-for="(e,i) in displays" :key="i" class="button-item " :class="getDisplayClass(e.type)">
                        <div class="button-item-order-wrap">
                            <button @click.stop="moveUpDisplayItem(i)" class="up-button"></button>
                            <button @click.stop="moveDownDisplayItem(i)" class="down-button"></button>
                        </div>
                        <div class="button-item-inner">
                            <div class="start">{{ getDisplayText(e.type) }}</div>
                            <div class="end">
                                <button v-if="i === 0 && !showingEmptyDisplayItem" @click.stop="showingEmptyDisplayItem = true" class="ui icon small compact button"><i class="plus icon"></i></button>
                                <button @click.stop="configDisplayItem(i)" class="ui icon small compact button"><i class="cog icon"></i></button>
                                <button @click.stop="removeDisplayItem(i)" class="ui icon small compact brand button"><i class="x icon"></i></button>
                            </div>
                        </div>
                    </li>
                </ul>
                <div v-if="showingEmptyDisplayItem" @drop="dropDisplayItem" class="empty-item">디스플레이를 넣어주세요</div>
            </div>
            <div class="inner">
                <ul v-if="buttons && buttons.length" class="button-item-ul">
                    <li v-for="(e,i) in buttons" :key="i" class="button-item button">
                        <div class="button-item-order-wrap">
                            <button @click.stop="moveUpButtonItem(i)" class="up-button"></button>
                            <button @click.stop="moveDownButtonItem(i)" class="down-button"></button>
                        </div>
                        <div class="button-item-inner">
                            <div class="start">
                                <text>{{ e.name }}</text>
                            </div>
                            <div class="end">
                                <button v-if="i === 0 && !showingEmptyButtonItem" @click.stop="createButton" class="ui icon small compact button"><i class="plus icon"></i></button>
                                <button @click.stop="configButtonItem(i)" class="ui icon small compact button"><i class="cog icon"></i></button>
                                <button @click.stop="removeButtonItem(i)" class="ui icon small compact brand button"><i class="x icon"></i></button>
                            </div>
                        </div>
                    </li>
                </ul>
                <button v-if="showingEmptyButtonItem" @click.stop="createButton" class="empty-item">클릭시 버튼이 생성됩니다.</button>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script src="https://cdn.jsdelivr.net/gh/jerosoler/Drawflow/dist/drawflow.min.js"></script>
        <script>
            const blockList = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            blocks: []
                        }
                    },
                }).mount('#block-list')
                return o || o
            })()
            const fallbackConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            data: {name: '', announcement: '', action: 'GOTO_ROOT',},
                            input: {name: '', announcement: '', action: 'GOTO_ROOT',},
                        }
                    },
                    methods: {
                        save() {
                            for (let property in o.input) o.data[property] = o.input[property]
                        },
                        show() {
                            $('.chatbot-control-panel').removeClass('active')
                            $('.fallback-block-manage').addClass('active')
                            for (let property in o.data) o.input[property] = o.data[property]
                        }
                    }
                }).mount('#fallback-config')
                return o || o
            })()
            const keywordConfig = (() => {
                const o = Vue.createApp({
                    data() {
                        return {
                            nodeId: null,
                            input: '',
                            keywords: []
                        }
                    },
                    methods: {
                        load(nodeId, keywords) {
                            o.nodeId = nodeId
                            o.keywords = []
                            keywords.forEach(e => o.keywords.push(e))
                        },
                        save() {
                            if (!nodeBlockMap[o.nodeId]) return
                            nodeBlockMap[o.nodeId].keywords = []
                            o.keywords.forEach(e => nodeBlockMap[o.nodeId].keywords.push(e))
                        },
                        addKeyword() {
                            if (!o.input.trim()) return

                            if (o.keywords.includes(o.input.trim()))
                                return alert('해당 키워드는 이미 목록에 존재합니다.')

                            const includedBlocks = blockList.blocks.filter(block => block.keywords.includes(o.input.trim()))
                            if (includedBlocks.length)
                                return alert('해당 키워드는 [' + includedBlocks[0].name + '] 에서 사용되고 있습니다. 다른 키워드를 입력해주세요.')

                            o.keywords.push(o.input.trim())
                            o.input = ''
                        },
                        removeKeyword(keyword) {
                            if (!keyword || !keyword.trim()) return
                            o.keywords = o.keywords.filter(e => e !== keyword.trim())
                        }
                    },
                }).mount('#keyword-config')
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
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'TEXT') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {text: o.data.text}
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
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'IMAGE') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {fileName: o.data.fileName, fileUrl: o.data.fileUrl,}
                        },
                        uploadFile(event) {
                            const file = event.target.files[0]
                            event.target.value = null
                            if (!file || !file.name) return
                            uploadFile(file).done(response => {
                                o.data.fileName = response.data.originalName
                                o.data.fileUrl = `/files/download?file=` + encodeURIComponent(response.data.fileName)
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
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'CARD') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {fileName: o.data.fileName, fileUrl: o.data.fileUrl, title: o.data.title, announcement: o.data.announcement,}
                        },
                        uploadFile(event) {
                            const file = event.target.files[0]
                            event.target.value = null
                            if (!file || !file.name) return
                            uploadFile(file).done(response => {
                                o.data.fileName = response.data.originalName
                                o.data.fileUrl = `/files/download?file=` + encodeURIComponent(response.data.fileName)
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
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'LIST') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {title: o.data?.title, titleUrl: o.data?.titleUrl, list: []}
                            o.data.list.forEach(e => nodeBlockMap[o.nodeId].displays[o.displayIndex].data.list.push({
                                title: e?.title,
                                announcement: e?.announcement,
                                url: e?.url,
                                fileName: e?.fileName,
                                fileUrl: e?.fileUrl
                            }))
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
                                o.data.list[index].fileName = response.data.originalName
                                o.data.list[index].fileUrl = `/files/download?file=` + encodeURIComponent(response.data.fileName)
                            })
                        },
                    },
                }).mount('#list-display-config')
                return o || o
            })()
            const buttonConfig = (() => {
                const API_PARAMETER_TYPES = Object.freeze({TEXT: 'TEXT', NUMERICAL: 'NUMERICAL', DATE: 'DATE', TIME: 'TIME'})
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
                        },
                        save() {
                            if (!nodeBlockMap[o.nodeId] || !nodeBlockMap[o.nodeId].buttons[o.buttonIndex]) return
                            const prevAction = nodeBlockMap[o.nodeId].buttons[o.buttonIndex].action
                            const currentAction = o.data.action

                            const data = {}
                            for (let property in o.data) data[property] = o.data[property]
                            data.api = {}
                            for (let property in o.data.api) data.api[property] = o.data.api[property]
                            data.api.parameters = []
                            o.data.api.parameters.forEach(e => data.api.parameters.push({type: e.type, name: e.name, value: e.value}))
                            nodeBlockMap[o.nodeId].buttons[o.buttonIndex] = data

                            if (prevAction !== currentAction) {
                                if (prevAction === 'TO_NEXT_BLOCK') {
                                    // TODO: 뒤에 있던 블럭들 싹 지워야 한다. (트리 구조를 타고 쭉 전부)
                                } else if (currentAction === 'TO_OTHER_BLOCK') {
                                    // TODO: 지우자 커넥션
                                }
                                if (currentAction === 'TO_NEXT_BLOCK') {
                                    const node = editor.getNodeFromId(o.nodeId)
                                    const childNodeId = createNode(node.pos_x + 300, node.pos_y)
                                    editor.addConnection(o.nodeId, childNodeId, Object.keys(node.outputs)[o.buttonIndex], Object.keys(editor.getNodeFromId(childNodeId).inputs)[0])
                                    data.childNodeId = childNodeId
                                } else if (currentAction === 'TO_OTHER_BLOCK') {
                                    // TODO: 만들자 커넥션
                                }
                            }
                        },
                        loadGroups() {
                            restSelf.get('/api/queue/', {limit: 10000}).done(response => o.groups = response.data.rows)
                        },
                        checkDataStructure() {
                            if (!this.data) this.data = {}
                            if (this.data.name === undefined) this.data.name = null
                            if (this.data.action === undefined) this.data.action = null

                            if (this.data.block === undefined) this.data.block = null
                            if (this.data.group === undefined) this.data.group = null
                            if (this.data.url === undefined) this.data.url = null
                            if (this.data.tel === undefined) this.data.tel = null

                            if (!this.data.api) this.data.api = {}
                            if (this.data.api.url === undefined) this.data.api.url = null
                            if (this.data.api.description === undefined) this.data.api.description = null
                            if (!this.data.api.parameters || !this.data.api.parameters.length) this.data.api.parameters = [{type: API_PARAMETER_TYPES.TEXT, name: null, value: null}]
                            if (this.data.api.usingResponse === undefined) this.data.api.usingResponse = false
                            if (this.data.api.outputFormat === undefined) this.data.api.outputFormat = null
                            if (this.data.api.errorOutput === undefined) this.data.api.errorOutput = null
                        },
                        addApiParameterItem() {
                            o.data.api.parameters.push({type: API_PARAMETER_TYPES.TEXT, name: null, value: null})
                        },
                        removeApiParameterItem(index) {
                            if (index === 0) return
                            o.data.api.parameters.splice(index, 1)
                        },
                    },
                    created() {
                        this.checkDataStructure()
                        this.loadGroups()
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
                                if (e.action === 'CALL_API') list.push(e)
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

            function createNode(x, y) {
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
                    // ref: https://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid
                    const uuidv4 = () => 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8)
                        return v.toString(16)
                    })
                    const o = Vue.createApp({
                        data() {
                            return {
                                id: uuidv4(),
                                name: '',
                                displays: [],
                                buttons: [],
                                keywords: [],
                                autoReply: false,

                                nodeId: nodeId,
                                showingEmptyDisplayItem: false,
                                showingEmptyButtonItem: false,
                            }
                        },
                        methods: {
                            getDisplayClass(type) {
                                const DISPLAY_TYPE_TO_CLASS = {TEXT: 'text', IMAGE: 'image', CARD: 'card', LIST: 'list'}
                                return DISPLAY_TYPE_TO_CLASS[type]
                            },
                            getDisplayText(type) {
                                const DISPLAY_TYPE_TO_TEXT = {TEXT: '텍스트', IMAGE: '이미지', CARD: '카드', LIST: '리스트'}
                                return DISPLAY_TYPE_TO_TEXT[type]
                            },
                            moveUpDisplayItem(index) {
                                if (index <= 0) return
                                const item = o.displays.splice(index, 1)[0]
                                o.displays.splice(index - 1, 0, item)
                            },
                            moveDownDisplayItem(index) {
                                if (index >= o.displays.length - 1) return
                                const item = o.displays.splice(index, 1)[0]
                                o.displays.splice(index + 1, 0, item)
                            },
                            removeDisplayItem(index) {
                                o.displays.splice(index, 1)
                                this.showingEmptyDisplayItem = !this.displays || !this.displays.length
                            },
                            dropDisplayItem(event) {
                                o.showingEmptyDisplayItem = false
                                const type = event.dataTransfer.getData('node')
                                if (type === 'display-item-text') {
                                    o.displays.push({type: 'TEXT', name: ''})
                                } else if (type === 'display-item-image') {
                                    o.displays.push({type: 'IMAGE', name: ''})
                                } else if (type === 'display-item-card') {
                                    o.displays.push({type: 'CARD', name: ''})
                                } else if (type === 'display-item-list') {
                                    o.displays.push({type: 'LIST', name: ''})
                                } else {
                                    o.showingEmptyDisplayItem = true
                                }
                            },
                            moveUpButtonItem(index) {
                                if (index <= 0) return
                                const item = o.buttons.splice(index, 1)[0]
                                o.buttons.splice(index - 1, 0, item)
                                // TODO: 커넥션 순서 바꾸자
                            },
                            moveDownButtonItem(index) {
                                if (index >= o.buttons.length - 1) return
                                const item = o.buttons.splice(index, 1)[0]
                                o.buttons.splice(index + 1, 0, item)
                                // TODO: 커넥션 순서 바꾸자
                            },
                            removeButtonItem(index) {
                                const removedButton = o.buttons.splice(index, 1)[0]
                                o.showingEmptyButtonItem = !o.buttons || !o.buttons.length

                                if (removedButton.action === 'TO_NEXT_BLOCK')
                                    ; // TODO: 뒤에 있던 블럭들 싹 지워야 한다. (트리 구조를 타고 쭉 전부)
                            },
                            createButton() {
                                o.buttons.push({name: ''})
                                editor.addNodeOutput(o.nodeId)
                                o.configButtonItem(o.buttons.length - 1)
                                o.showingEmptyButtonItem = false
                            },
                            configKeywords() {
                                $('.chatbot-control-panel').removeClass('active')
                                $('.keyword-manage').addClass('active')
                                keywordConfig.load(o.nodeId, o.keywords)
                            },
                            configDisplayItem(index) {
                                $('.chatbot-control-panel').removeClass('active')
                                if (o.displays[index].type === 'TEXT') {
                                    $('.text-display-manage').addClass('active')
                                    textDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (o.displays[index].type === 'IMAGE') {
                                    $('.img-display-manage').addClass('active')
                                    imageDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (o.displays[index].type === 'CARD') {
                                    $('.card-display-manage').addClass('active')
                                    cardDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (o.displays[index].type === 'LIST') {
                                    $('.list-display-manage').addClass('active')
                                    listDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                            },
                            configButtonItem(index) {
                                $('.chatbot-control-panel').removeClass('active')
                                $('.button-action-manage').addClass('active')
                                buttonConfig.load(o.nodeId, index, o.buttons[index])
                            },
                            showPreview() {
                                $('.chatbot-control-panel').removeClass('active');
                                $('.chat-preview-manage').addClass('active');
                                blockPreview.load(o.displays, o.buttons)
                            }
                        },
                        updated() {
                            editor.updateConnectionNodes('node-' + o.nodeId)
                        },
                        mounted() {
                            this.showingEmptyDisplayItem = !this.displays || !this.displays.length
                            this.showingEmptyButtonItem = !this.buttons || !this.buttons.length
                        }
                    }).mount(block)
                    return o || o
                })()
                nodeBlockMap[nodeId] = app
                blockList.blocks.push(app)
                buttonConfig.blocks.push(app)
                return nodeId
            }

            editor.on('nodeRemoved', (nodeId) => {
                const app = nodeBlockMap[nodeId]
                blockList.blocks.splice(blockList.blocks.indexOf(app), 1)
                buttonConfig.blocks.splice(buttonConfig.blocks.indexOf(app), 1)
                delete nodeBlockMap[nodeId]
            })

            // 봇 추가 클릭 시 클립보드에 이미 복사한 봇이 있을 경우만 출력
            // confirmMulti('클립보드에 복사된 시나리오를 붙여넣겠습니다. 진행 하시겠습니까?');

            // 위 봇 붙여넣기 모달에서 신규추가 버튼 클릭 시 아래 모달 출력
            // confirm('기존 클립보드에 복사된 시나리오는 삭제 됩니다. 진행 하시겠습니까?');

            const botTestPopup = () => confirm('확인을 누르시면 자동 저장 후 테스트 기능이 활성화 됩니다.')
            const botCopyPopup = () => confirm('선택하신 시나리오를 클립보드에 복사합니다. 진행 하시겠습니까?')
            const allowDrop = event => event.preventDefault()

            $('.chatbot-control-container .arrow-button').click(function () {
                $(this).toggleClass('show')
                $(this).parent('.chatbot-control-container').toggleClass('active')
            })

            $('.bot-setting-popup').modal({
                dimmerSettings: { opacity: 0 },
                duration: 350,
                closable: false
            }).modal('show');
        </script>
    </tags:scripts>
</tags:tabContentLayout>
