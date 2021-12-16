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
    <div class="content-wrapper-frame">
        <div class="menu-tab">
            <div class="inner">
                <ul>
                    <li><a href="#" class="tab-on tab-indicator">채팅 봇</a></li>
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
                        <div id="bot-list" class="dp-flex">
                            <div class="bot-select-wrap">
                                <span>봇 리스트</span>
                                <div class="ui form">
                                    <select @change.stop.prevent="changeBot" v-model="select">
                                        <option value=""></option>
                                        <option v-for="(e,i) in bots" :key="i" :value="e.id">{{ e.name }}</option>
                                    </select>
                                </div>
                                <button type="button" class="ui mini button" onclick="chatbotSettingModal.show()">봇 추가</button>
                            </div>
                            <button class="ui mini button" @click.stop.prevent="copy">봇 복사</button>
                            <button class="ui mini button" @click.stop.prevent="test">봇 테스트</button>
                            <button class="ui mini button" @click.stop.prevent="save">봇 저장</button>
                            <button class="ui mini button" @click.stop.prevent="remove">봇 삭제</button>
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
                                    <div class="bar-zoom">
                                        <button class="zoom-control-btn" type="button" onclick="editor.zoom_in()"><img src="<c:url value="/resources/images/zoom-plus.svg"/>"></button>
                                        <button class="zoom-control-btn" type="button" onclick="editor.zoom_reset()"><img src="<c:url value="/resources/images/zoom-refresh.svg"/>"></button>
                                        <button class="zoom-control-btn" type="button" onclick="editor.zoom_out()"><img src="<c:url value="/resources/images/zoom-minus.svg"/>"></button>
                                    </div>
                                </div>
                            </div>
                            <div class="chatbot-control-panel empty-panel active">
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

                                            <div class="mb15">봇 수행중 고객입력 가능 여부</div>
                                            <div class="ui fitted toggle checkbox">
                                                <input type="checkbox" @change="input.enableCustomerInput = $event.target.checked" :checked="input.enableCustomerInput">
                                                <label></label>
                                            </div>

                                            <div v-if="input.enableCustomerInput" class="mt15">
                                                <div class="mb15">폴백 대사 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="8" v-model="input.fallbackMent"></textarea>
                                                </div>
                                                <div class="mb15">동작</div>
                                                <div class="ui form fluid mb15">
                                                    <select v-model="input.fallbackAction">
                                                        <option value="first">처음으로 가기</option>
                                                        <option value="member">상담그룹연결</option>
                                                        <option value="url">URL 연결</option>
                                                        <option value="block">다른 블록으로 연결</option>
                                                        <option value="phone">전화 연결</option>
                                                    </select>
                                                </div>
                                                <div v-if="input.fallbackAction === 'member'" class="mb10">상담그룹</div>
                                                <div v-if="input.fallbackAction === 'member'" class="ui form fluid mb10">
                                                    <select v-model="input.nextGroupId">
                                                        <option v-for="(e,i) in groups" :key="i" :value="e.groupId">{{ e.groupName }}</option>
                                                    </select>
                                                </div>
                                                <div v-if="input.fallbackAction === 'url'" class="mb10">연결 URL 설정</div>
                                                <div v-if="input.fallbackAction === 'url'" class="ui form fluid mb10">
                                                    <input type="text" v-model="input.nextUrl">
                                                </div>
                                                <div v-if="input.fallbackAction === 'block'">
                                                    <div class="mb15">연결 블록 설정</div>
                                                    <div class="ui form fluid mb15">
                                                        <select v-model="input.nextBlockId">
                                                            <option v-for="(e,i) in blocks" :key="i" :value="e.id">{{ e.name }}</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div v-if="input.fallbackAction === 'phone'" class="mb10">연결 번호 설정</div>
                                                <div v-if="input.fallbackAction === 'phone'" class="ui form fluid mb10">
                                                    <input type="text" v-model="input.nextPhone">
                                                </div>
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
                                                    <option value="">다음 블록으로 연결</option>
                                                    <option value="block">다른 블록으로 연결</option>
                                                    <option value="member">상담원 연결</option>
                                                    <option value="url">URL 연결</option>
                                                    <option value="phone">전화 연결</option>
                                                    <option value="api">외부연동</option>
                                                </select>
                                            </div>
                                            <div v-if="data.action === 'block'">
                                                <div class="mb15">연결 블록 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <select v-model="data.nextBlockId">
                                                        <option v-for="(e,i) in blocks" :key="i" :value="e.id">{{ e.name }}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'member'">
                                                <div class="mb15">연결 그룹 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <select v-model="data.nextGroupId">
                                                        <option v-for="(e,i) in groups" :key="i" :value="e.groupId">{{ e.groupName }}</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'url'">
                                                <div class="mb15">연결 URL 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text" v-model="data.nextUrl">
                                                    </div>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'phone'">
                                                <div class="mb15">연결 번호 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text" v-model="data.nextPhone">
                                                    </div>
                                                </div>
                                            </div>
                                            <div v-if="data.action === 'api'">
                                                <div class="mb15">외부연동 URL 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text" v-model="data.api.nextApiUrl">
                                                    </div>
                                                </div>
                                                <div class="mb15">안내문구 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="5" v-model="data.api.nextApiMent"></textarea>
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
                                                                        <option value="text">텍스트</option>
                                                                        <option value="number">숫자</option>
                                                                        <option value="calendar">날짜</option>
                                                                        <option value="time">시간</option>
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
                                                        <input type="checkbox" @change="data.api.usingResponse = $event.target.checked" :checked="data.api.usingResponse">
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
                                                                    <textarea rows="4" v-model="data.api.nextApiResultTemplate"></textarea>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>조회불가</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text" v-model="data.api.nextApiErrorMent">
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
                                                    <div v-for="(e,i) in displays" :class="e.type === 'text' ? 'sample-bubble' : 'card'">
                                                        <p v-if="e.data && e.type === 'text'" style="white-space: pre-wrap">{{ e.data.text }}</p>
                                                        <div v-if="e.data && e.type === 'image'" class="card-img">
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

    <div id="chatbot-setting-modal" class="ui modal chatbot-setting-modal">
        <div class="header">봇 기본설정</div>
        <div class="content">
            <div class="mb10">이름</div>
            <div class="ui form fluid mb15">
                <input type="text" v-model="name">
            </div>

            <div class="mb10">봇 수행중 고객입력 가능 여부</div>
            <div class="ui fitted toggle checkbox">
                <input type="checkbox" @change="enableCustomerInput = $event.target.checked" :checked="enableCustomerInput">
                <label></label>
            </div>

            <div v-if="enableCustomerInput" class="mt10">
                <div class="mb10">폴백 대사 입력</div>
                <div class="ui form fluid mb10">
                    <textarea rows="3" v-model="fallbackMent"></textarea>
                </div>
                <div class="mb10">동작</div>
                <div class="ui form fluid mb10">
                    <select v-model="fallbackAction">
                        <option value="first">처음으로 가기</option>
                        <option value="member">상담그룹연결</option>
                        <option value="url">URL 연결</option>
                        <option value="phone">전화 연결</option>
                    </select>
                </div>
                <div v-if="fallbackAction === 'member'" class="mb10">상담그룹</div>
                <div v-if="fallbackAction === 'member'" class="ui form fluid mb10">
                    <select v-model="nextGroupId">
                        <option v-for="(e,i) in groups" :key="i" :value="e.groupId">{{ e.groupName }}</option>
                    </select>
                </div>
                <div v-if="fallbackAction === 'url'" class="mb10">연결 URL 설정</div>
                <div v-if="fallbackAction === 'url'" class="ui form fluid mb10">
                    <input type="text" v-model="nextUrl">
                </div>
                <div v-if="fallbackAction === 'phone'" class="mb10">연결 번호 설정</div>
                <div v-if="fallbackAction === 'phone'" class="ui form fluid mb10">
                    <input type="text" v-model="nextPhone">
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact button" @click.stop="hide">취소</button>
            <button type="button" class="ui small compact brand button" @click.stop="start">시작</button>
        </div>
    </div>

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
                            alert('저장된 블록을 대상으로만 테스트됩니다.', () => window.open(contextPath + '/admin/talk/chat-bot/' + o.current + '/modal-test', '_blank', 'width=420px,height=800px,top=100,left=100,scrollbars=yes,resizable=no'))
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
                                isTemplateEnable: block?.autoReply,
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
                                restSelf.put('/api/chatbot/' + o.current, form).done(() => alert('저장되었습니다.', o.load))
                            } else {
                                restSelf.post('/api/chatbot/', form).done(response => {
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
                        changeBot() {
                            const change = () => restSelf.get('/api/chatbot/' + o.select).done(response => {
                                o.current = o.select

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
                                    app.autoReply = block.isTemplateEnable
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
                                    app.showingEmptyDisplayItem = !app.displays.length
                                    app.showingEmptyButtonItem = !app.buttons.length

                                    app.buttons.forEach(() => editor.addNodeOutput(nodeId))
                                }
                                createBlock(data.blockInfo)

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
                            })

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
                        this.load()
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
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'text') return
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
                            if (!nodeBlockMap[o.nodeId].displays[o.displayIndex] || nodeBlockMap[o.nodeId].displays[o.displayIndex].type !== 'image') return
                            nodeBlockMap[o.nodeId].displays[o.displayIndex].data = {fileName: o.data.fileName, fileUrl: o.data.fileUrl,}
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
                                    data.childNodeId = createNode(null, node.pos_x + 300, node.pos_y)
                                    app.createConnection(o.buttonIndex, nodeBlockMap[data.childNodeId].id)
                                } else if (currentAction === 'block') {
                                    app.createConnection(o.buttonIndex, data.nextBlockId)
                                }
                            } else if (currentAction === 'block' && preBlock !== data.nextBlockId) {
                                app.removeConnection(o.buttonIndex)
                                app.createConnection(o.buttonIndex, data.nextBlockId)
                            }
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
                                autoReply: false,

                                nodeId: nodeId,
                                showingEmptyDisplayItem: false,
                                showingEmptyButtonItem: false,
                            }
                        },
                        methods: {
                            getDisplayClass(type) {
                                const DISPLAY_TYPE_TO_CLASS = {text: 'text', image: 'image', card: 'card', list: 'list'}
                                return DISPLAY_TYPE_TO_CLASS[type]
                            },
                            getDisplayText(type) {
                                const DISPLAY_TYPE_TO_TEXT = {text: '텍스트', image: '이미지', card: '카드', list: '리스트'}
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
                                    o.displays.push({type: 'text', name: ''})
                                } else if (type === 'display-item-image') {
                                    o.displays.push({type: 'image', name: ''})
                                } else if (type === 'display-item-card') {
                                    o.displays.push({type: 'card', name: ''})
                                } else if (type === 'display-item-list') {
                                    o.displays.push({type: 'list', name: ''})
                                } else {
                                    o.showingEmptyDisplayItem = true
                                }
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
                                o.showingEmptyButtonItem = !o.buttons || !o.buttons.length

                                editor.removeNodeOutput(o.nodeId, o.getOutputClass(index))

                                if (removedButton.action === '')
                                    nodeBlockMap[removedButton.childNodeId].delete()
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
                                if (o.displays[index].type === 'text') {
                                    $('.text-display-manage').addClass('active')
                                    textDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (o.displays[index].type === 'image') {
                                    $('.img-display-manage').addClass('active')
                                    imageDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (o.displays[index].type === 'card') {
                                    $('.card-display-manage').addClass('active')
                                    cardDisplayConfig.load(o.nodeId, index, o.displays[index].data)
                                }
                                if (o.displays[index].type === 'list') {
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
