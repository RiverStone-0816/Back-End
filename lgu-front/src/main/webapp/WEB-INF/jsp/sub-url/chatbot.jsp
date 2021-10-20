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

<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/jerosoler/Drawflow/dist/drawflow.min.css">
<script src="https://cdn.jsdelivr.net/gh/jerosoler/Drawflow/dist/drawflow.min.js"></script>

<tags:layout>
    <div class="tab-label-flow-container">
        <ul class="tab-label-container" style="left: 0;"></ul>
    </div>
    <button class="tab-arrow tab-arrow-left" style="display: none;" onclick="tabController.moveToLeftTabLabels()"><i class="material-icons"> keyboard_arrow_left </i></button>
    <button class="tab-arrow tab-arrow-right" style="display: none;" onclick="tabController.moveToRightTabLabels()"><i class="material-icons"> keyboard_arrow_right </i></button>

    <div class="tab-content-container">
        <div class="tab-content active">
            <div class="content-wrapper-frame flex-100">
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
                                <div>
                                    <button type="button" class="ui mini button">봇 복사</button>
                                    <button type="button" class="ui mini button">봇 테스트</button>
                                    <button type="button" class="ui mini button">봇 저장</button>
                                </div>
                            </div>
                            <div class="panel-body chatbot remove-padding">
                                <div class="chatbot-container">
                                    <div class="chatbot-node-container">
                                        <div class="chatbot-box-label">디스플레이</div>
                                        <div class="pd13">
                                            <div class="display-item text" draggable="true" ondragstart="drag(event)" data-node="display-text">
                                                <img src="<c:url value="/resources/images/item-text-icon.svg"/>"> 텍스트
                                            </div>
                                            <div class="display-item image" draggable="true" ondragstart="drag(event)" data-node="display-image">
                                                <img src="<c:url value="/resources/images/item-image-icon.svg"/>"> 이미지
                                            </div>
                                            <div class="display-item card" draggable="true" ondragstart="drag(event)" data-node="display-card">
                                                <img src="<c:url value="/resources/images/item-card-icon.svg"/>"> 카드
                                            </div>
                                            <div class="display-item list" draggable="true" ondragstart="drag(event)" data-node="display-list">
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
                                                            <button class="ui mini button">수정</button>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="chatbot-box-sub-label skyblue">일반 블록</div>
                                            <div class="block-list-container">
                                                <ul class="block-list-ul">
                                                    <li class="block-list">
                                                        <div class="block-name">블록1</div>
                                                        <div class="block-contorl">
                                                            <button class="ui mini button">수정</button>
                                                        </div>
                                                    </li>
                                                    <li class="block-list">
                                                        <div class="block-name">블록1블록1블록1블록1블록1블록1블록1블록1블록1블록1블록1블록1블록1블록1블록1</div>
                                                        <div class="block-control">
                                                            <img src="<c:url value="/resources/images/chatbot-square-mini.svg"/>">
                                                            <button class="ui mini button">수정</button>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="chatbot-main-container flex-100">
                                        <div id="drawflow" ondrop="drop(event)" ondragover="allowDrop(event)">
                                            <div class="bar-zoom">
                                                <button class="zoom-control-btn" type="button" onclick="editor.zoom_in()"><img src="<c:url value="/resources/images/zoom-plus.svg"/>"></button>
                                                <button class="zoom-control-btn" type="button" onclick="editor.zoom_reset()"><img src="<c:url value="/resources/images/zoom-refresh.svg"/>"></button>
                                                <button class="zoom-control-btn" type="button" onclick="editor.zoom_out()"><img src="<c:url value="/resources/images/zoom-minus.svg"/>"></button>
                                            </div>
                                        </div>
                                    </div>
                                    <%--기본--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">설정영역</div>
                                            <div class="chatbot-control-body">
                                                <div class="empty">선택된 내용이 없습니다.</div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--폴백 블록 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">폴백 블록 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">이름</div>
                                                <div class="ui form fluid mb15">
                                                    <input type="text">
                                                </div>
                                                <div class="mb15">멘트 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="8"></textarea>
                                                </div>
                                                <div class="mb15">동작</div>
                                                <div class="ui form fluid mb15">
                                                    <select>
                                                        <option>처음으로 가기</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--키워드 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">키워드 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">키워드 생성</div>
                                                <div class="ui action fluid input mb15">
                                                    <input type="text" placeholder="키워드 입력">
                                                    <button class="ui button">추가</button>
                                                </div>
                                                <div class="mb15">키워드 목록</div>
                                                <div class="mb15">
                                                    <span class="ui basic large label">
                                                      키위
                                                      <i class="delete icon"></i>
                                                    </span>
                                                    <span class="ui basic large label">
                                                      키워드
                                                      <i class="delete icon"></i>
                                                    </span>
                                                    <span class="ui basic large label">
                                                      챗봇기능
                                                      <i class="delete icon"></i>
                                                    </span>
                                                    <span class="ui basic large label">
                                                      가나다라마
                                                      <i class="delete icon"></i>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--텍스트 디스플레이 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">텍스트 디스플레이 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">멘트 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="8"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--이미지 디스플레이 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">이미지 디스플레이 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">이미지 삽입</div>
                                                <div class="ui action fluid input mb15">
                                                    <input type="text" readonly="">
                                                    <input type="file" id="file">
                                                    <label class="ui icon button" for="file">
                                                        찾아보기
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--카드 디스플레이 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">이미지 디스플레이 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">이미지 삽입</div>
                                                <div class="ui action fluid input mb15">
                                                    <input type="text" readonly="">
                                                    <input type="file" id="file">
                                                    <label class="ui icon button" for="file">
                                                        찾아보기
                                                    </label>
                                                </div>
                                                <div class="mb15">타이틀 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <input type="text">
                                                </div>
                                                <div class="mb15">멘트 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="8"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--리스트 디스플레이 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">이미지 디스플레이 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">타이틀 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <input type="text">
                                                </div>
                                                <div class="mb15">타이틀 URL 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <input type="text">
                                                </div>
                                                <div class="mb15">리스트 설정</div>
                                                <div class="list-control-container mb15">
                                                    <div class="list-control-header">
                                                        <div>리스트1</div>
                                                        <button class="ui icon small compact black button">
                                                            <i class="plus icon"></i>
                                                        </button>
                                                    </div>
                                                    <table class="list-control-table">
                                                        <tr>
                                                            <th>제목</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>멘트</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <textarea rows="3"></textarea>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>URL</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>이미지</th>
                                                            <td>
                                                                <div class="ui action fluid input">
                                                                    <input type="text" readonly="">
                                                                    <input type="file" id="file">
                                                                    <label class="ui icon button" for="file">
                                                                        찾아보기
                                                                    </label>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="list-control-container mb15">
                                                    <div class="list-control-header">
                                                        <div>리스트1</div>
                                                        <button class="ui icon small compact brand button">
                                                            <i class="x icon"></i>
                                                        </button>
                                                    </div>
                                                    <table class="list-control-table">
                                                        <tr>
                                                            <th>제목</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>멘트</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <textarea rows="3"></textarea>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>URL</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>이미지</th>
                                                            <td>
                                                                <div class="ui action fluid input">
                                                                    <input type="text" readonly="">
                                                                    <input type="file" id="file">
                                                                    <label class="ui icon button" for="file">
                                                                        찾아보기
                                                                    </label>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--버튼 동작 관리--%>
                                    <%--<div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">버튼 동작 관리<button class="ui mini button">저장</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="mb15">버튼 이름</div>
                                                <div class="ui form fluid mb15">
                                                    <input type="text">
                                                </div>
                                                <div class="mb15">버튼 액션</div>
                                                <div class="ui form fluid mb15">
                                                    <select>
                                                        <option>다음 블록으로 연결</option>
                                                        <option>상담원 연결</option>
                                                        <option>URL 연결</option>
                                                        <option>외부연동</option>
                                                    </select>
                                                </div>
                                                <div class="mb15">연결 블록 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <select>
                                                        <option>블록1</option>
                                                    </select>
                                                </div>
                                                <div class="mb15">연결 그룹 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <select>
                                                        <option>상담원 그룹 이름</option>
                                                    </select>
                                                </div>
                                                <div class="mb15">연결 URL 설정</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text">
                                                    </div>
                                                </div>
                                                <div class="mb15">외부연동 URL 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <div class="ui form fluid mb15">
                                                        <input type="text">
                                                    </div>
                                                </div>
                                                <div class="mb15">안내문구 입력</div>
                                                <div class="ui form fluid mb15">
                                                    <textarea rows="5"></textarea>
                                                </div>
                                                <div class="mb15">항목설정</div>
                                                <div class="list-control-container mb15">
                                                    <div class="list-control-header">
                                                        <div>항목1</div>
                                                        <button class="ui icon small compact black button">
                                                            <i class="plus icon"></i>
                                                        </button>
                                                    </div>
                                                    <table class="list-control-table">
                                                        <tr>
                                                            <th>타입</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <select>
                                                                        <option>텍스트</option>
                                                                    </select>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>항목명</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>파라미터</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="list-control-container mb15">
                                                    <div class="list-control-header">
                                                        <div>항목1</div>
                                                        <button class="ui icon small compact brand button">
                                                            <i class="x icon"></i>
                                                        </button>
                                                    </div>
                                                    <table class="list-control-table">
                                                        <tr>
                                                            <th>타입</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <select>
                                                                        <option>텍스트</option>
                                                                    </select>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>항목명</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>파라미터</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="mb15 dp-flex align-items-center justify-content-space-between">
                                                    <div>답변 멘트 사용</div>
                                                    <div class="ui fitted toggle checkbox">
                                                        <input type="checkbox">
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
                                                                    <textarea rows="4">[[$result1]] 고객님. 안녕하세요? [[$result2]] 에 [[$result3]]을(를) [[$result4]] 개 구매 하셨습니다.</textarea>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>항목없음</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text" value="조회되는 항목이 없습니다.">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>조회불가</th>
                                                            <td>
                                                                <div class="ui form fluid">
                                                                    <input type="text" value="조회가 불가능합니다. 잠시 후 다시 이용해주세요.">
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--%>

                                    <%--채팅미리보기--%>
                                    <div class="chatbot-control-container active">
                                        <button type="button" class="arrow-button"></button>
                                        <div class="chatbot-control-inner chatbot-ui">
                                            <div class="chatbot-box-label">미리보기<button class="ui mini button">새로고침</button></div>
                                            <div class="chatbot-control-body">
                                                <div class="chat-preview">
                                                    <div class="header">
                                                        <img src="<c:url value="../resources/images/chatbot-icon.svg"/>" class="chatbot-icon">
                                                        <span class="customer-title">Chat Bot</span>
                                                    </div>
                                                    <div class="content editor">
                                                        <div class="sample-bubble">
                                                            <p>
                                                                [ 이아이씨엔 ] 고객님 안녕하세요.
                                                                [ 2021-07-29 ] 에 총 [IP460S] 을(를)
                                                                [ 30 ] 개 구매하셨습니다.
                                                            </p>
                                                        </div>
                                                        <div class="sample-bubble">
                                                            <button type="button" class="chatbot-button">이름없는 버튼</button>
                                                            <button type="button" class="chatbot-button">이름없는 버튼</button>
                                                        </div>
                                                        <div class="card">
                                                            <div class="card-list">
                                                                <div class="card-list-title">카드 리스트</div>
                                                                <ul class="card-list-ul">
                                                                    <li class="item">
                                                                        <div class="item-thumb">
                                                                            <div class="item-thumb-inner">
                                                                                <img src="https://img.insight.co.kr/static/2016/03/04/700/292z8mhg980269202q5s.jpg">
                                                                            </div>
                                                                        </div>
                                                                        <div class="item-content">
                                                                            <div class="subject">리스트 제목이 나오는 공간</div>
                                                                            <div class="ment">리스트 멘트가 나오는 공간</div>
                                                                        </div>
                                                                    </li>
                                                                    <li class="item">
                                                                        <div class="item-thumb">
                                                                            <div class="item-thumb-inner">
                                                                                <img src="https://img.insight.co.kr/static/2016/03/04/700/292z8mhg980269202q5s.jpg">
                                                                            </div>
                                                                        </div>
                                                                        <div class="item-content">
                                                                            <div class="subject">리스트 제목이 나오는 공간</div>
                                                                            <div class="ment">리스트 멘트가 나오는 공간리스트 멘트가 나오는 공간리스트 멘트가 나오는 공간리스트 멘트가 나오는 공간</div>
                                                                        </div>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                        <div class="card">
                                                            <div class="card-list">
                                                                <ul class="card-list-ul">
                                                                    <li class="item form">
                                                                        <div class="label">이름</div>
                                                                        <div class="ui fluid input">
                                                                            <input type="text">
                                                                        </div>
                                                                    </li>
                                                                    <li class="item form">
                                                                        <div class="label">사업자 번호</div>
                                                                        <div class="ui fluid input">
                                                                            <input type="text">
                                                                        </div>
                                                                    </li>
                                                                    <li class="item form">
                                                                        <div class="label">가입일자</div>
                                                                        <div class="ui fluid input">
                                                                            <input type="text" class="-datepicker hasDatepicker calendar-ipt">
                                                                        </div>
                                                                    </li>
                                                                    <li class="item form">
                                                                        <div class="label">가입일시</div>
                                                                        <div class="ui multi form">
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
                                                        </div>
                                                        <div class="card">
                                                            <div class="card-img">
                                                                <img src="<c:url value="../resources/images/eicn-sample.png"/>">
                                                            </div>
                                                        </div>
                                                        <div class="card">
                                                            <div class="card-img">
                                                                <img src="<c:url value="../resources/images/eicn-sample.png"/>">
                                                            </div>
                                                            <div class="card-content">
                                                                <div class="card-title">
                                                                    카드 디스플레이 타이틀
                                                                </div>
                                                                <div class="card-text">
                                                                    IPCC 화면에 로그인 하려면 지정된 계정 정보를 입력해야 합니다.
                                                                    IPCC 화면에 로그인 하려면 지정된 계정 정보를 입력해야 합니다.
                                                                    IPCC 화면에 로그인 하려면 지정된 계정 정보를 입력해야 합니다.
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
        </div>
    </div>

    <%--키워드 중복 modal--%>
    <%--<div class="ui mini modal keyword-confirm">
        <i class="close icon"></i>
        <div class="header">키워드 중복</div>
        <div class="scrolling content rows">
            해당 키워드는 [ 블록1 ] 에서 사용되고 있습니다.
            다른 키워드를 입력해주세요.
        </div>
        <div class="actions">
            <button type="button" class="ui blue button modal-close">확인</button>
        </div>
    </div>--%>



    <tags:scripts>
        <script>
            function keywordConfirmPopup() {
                $('.keyword-confirm').dragModalShow();
            }

            var id = document.getElementById("drawflow");
            const editor = new Drawflow(id);
            editor.reroute = true;
            const dataToImport = {
                "drawflow": {
                    "Home": {
                        "data": {
                            "1": {
                                "id": 1,
                                "name": "example1",
                                "data": {},
                                "class": "example1",
                                "html": "<div>\n" +
                                "                                                                        <div class=\"title-box\">\n" +
                                "                                                                            <div class=\"mb10\">\n" +
                                "                                                                                <input type=\"text\" placeholder=\"텍스트 입력\">\n" +
                                "                                                                            </div>\n" +
                                "                                                                            <div class=\"btn-wrap\">\n" +
                                "                                                                                <button type=\"button\" class=\"ui tiny compact button\">키워드 관리</button>\n" +
                                "                                                                                <button type=\"button\" class=\"ui tiny compact button\">미리보기</button>\n" +
                                "                                                                                <button type=\"button\" class=\"ui tiny compact button preview-button\"><img src=\"../resources/images/chatbot-icon-white.svg\">OFF</button>\n" +
                                // "                                                                                <button type=\"button\" class=\"ui tiny compact button preview-button active\"><img src=\"../resources/images/chatbot-icon-white-active.svg\">ON</button>\n" +
                                "                                                                            </div>\n" +
                                "                                                                        </div>\n" +
                                "                                                                        <div class=\"box\">\n" +
                                "                                                                            <div class=\"inner\">\n" +
                                "                                                                                <div class=\"empty-item\">디스플레이를 넣어주세요</div>\n" +
                                "                                                                            </div>\n" +
                                "                                                                            <div class=\"inner\">\n" +
                                "                                                                                <button type=\"button\" class=\"empty-item\">클릭시 버튼이 생성됩니다.</button>\n" +
                                "                                                                            </div>\n" +
                                "                                                                        </div>\n" +
                                "                                                                    </div>",
                                "typenode": false,
                                "inputs": {},
                                "outputs": {},
                                "pos_x": 50,
                                "pos_y": 50
                            },
                            "2": {
                                "id": 2,
                                "name": "example2",
                                "data": {},
                                "class": "example2",
                                "html": "<div>\n" +
                                    "                                                                        <div class=\"title-box\">\n" +
                                    "                                                                            <div class=\"mb10\">\n" +
                                    "                                                                                <input type=\"text\" placeholder=\"텍스트 입력\">\n" +
                                    "                                                                            </div>\n" +
                                    "                                                                            <div class=\"btn-wrap\">\n" +
                                    "                                                                                <button type=\"button\" class=\"ui tiny compact button\">키워드 관리</button>\n" +
                                    "                                                                                <button type=\"button\" class=\"ui tiny compact button\">미리보기</button>\n" +
                                    "                                                                                <%--<button type=\"button\" class=\"ui tiny compact button poreview-button\"><img src=\"<c:url value=\"/resources/images/chatbot-icon-white.svg\"/>\">OFF</button>--%>\n" +
                                    "                                                                                <button type=\"button\" class=\"ui tiny compact button preview-button active\"><img src=\"../resources/images/chatbot-icon-white.svg\">ON</button>\n" +
                                    "                                                                            </div>\n" +
                                    "                                                                        </div>\n" +
                                    "                                                                        <div class=\"box\">\n" +
                                    "                                                                            <div class=\"inner\">\n" +
                                    "                                                                                <ul class=\"button-item-ul\">\n" +
                                    "                                                                                    <li class=\"button-item text\">\n" +
                                    "                                                                                        <div class=\"button-item-inner\">\n" +
                                    "                                                                                            <div class=\"start\">\n" +
                                    "                                                                                                <img src=\"../../resources/images/item-text-icon.svg\">텍스트\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                            <div class=\"end\">\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"plus icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"cog icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact brand button\"><i class=\"x icon\"></i></button>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                        </div>\n" +
                                    "                                                                                    </li>\n" +
                                    "                                                                                    <li class=\"button-item image\">\n" +
                                    "                                                                                        <div class=\"button-item-inner\">\n" +
                                    "                                                                                            <div class=\"start\">\n" +
                                    "                                                                                                <img src=\"../../resources/images/item-image-icon.svg\">이미지\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                            <div class=\"end\">\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"plus icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"cog icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact brand button\"><i class=\"x icon\"></i></button>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                        </div>\n" +
                                    "                                                                                    </li>\n" +
                                    "                                                                                    <li class=\"button-item card\">\n" +
                                    "                                                                                        <div class=\"button-item-inner\">\n" +
                                    "                                                                                            <div class=\"start\">\n" +
                                    "                                                                                                <img src=\"../../resources/images/item-card-icon.svg\">카드\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                            <div class=\"end\">\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"plus icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"cog icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact brand button\"><i class=\"x icon\"></i></button>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                        </div>\n" +
                                    "                                                                                    </li>\n" +
                                    "                                                                                    <li class=\"button-item list\">\n" +
                                    "                                                                                        <div class=\"button-item-inner\">\n" +
                                    "                                                                                            <div class=\"start\">\n" +
                                    "                                                                                                <img src=\"../../resources/images/item-list-icon.svg\">리스트\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                            <div class=\"end\">\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"plus icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"cog icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact brand button\"><i class=\"x icon\"></i></button>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                        </div>\n" +
                                    "                                                                                    </li>\n" +
                                    "                                                                                </ul>\n" +
                                    "                                                                            </div>\n" +
                                    "                                                                            <div class=\"inner\">\n" +
                                    "                                                                                <ul class=\"button-item-ul\">\n" +
                                    "                                                                                    <li class=\"button-item button\">\n" +
                                    "                                                                                        <div class=\"button-item-inner\">\n" +
                                    "                                                                                            <div class=\"start\">\n" +
                                    "                                                                                                <text>이름없는버튼이름없는버튼</text>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                            <div class=\"end\">\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"plus icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"cog icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact brand button\"><i class=\"x icon\"></i></button>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                        </div>\n" +
                                    "                                                                                    </li>\n" +
                                    "                                                                                    <li class=\"button-item button\">\n" +
                                    "                                                                                        <div class=\"button-item-inner\">\n" +
                                    "                                                                                            <div class=\"start\">\n" +
                                    "                                                                                                <text>이름없는버튼이름없는버튼</text>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                            <div class=\"end\">\n" +
                                    "                                                                                                <button class=\"ui icon small compact button\"><i class=\"cog icon\"></i></button>\n" +
                                    "                                                                                                <button class=\"ui icon small compact brand button\"><i class=\"x icon\"></i></button>\n" +
                                    "                                                                                            </div>\n" +
                                    "                                                                                        </div>\n" +
                                    "                                                                                    </li>\n" +
                                    "                                                                                </ul>\n" +
                                    "                                                                            </div>\n" +
                                    "                                                                        </div>\n" +
                                    "                                                                    </div>",
                                "typenode": false,
                                "inputs": {},
                                "outputs": {},
                                "pos_x": 50,
                                "pos_y": 300
                            },
                            "3": {
                                "id": 3,
                                "name": "example3",
                                "data": {},
                                "class": "example3",
                                "html": "<div>\n" +
                                    "                                                                        <div class=\"title-box\">봇 기본설정</div>\n" +
                                    "                                                                        <div class=\"box\">\n" +
                                    "                                                                            <div class=\"inner\">\n" +
                                    "                                                                                <div class=\"mb10\">이름</div>\n" +
                                    "                                                                                <div class=\"input-wrap mb15\">\n" +
                                    "                                                                                    <input type=\"text\">\n" +
                                    "                                                                                </div>\n" +
                                    "                                                                                <div class=\"mb10\">폴백 멘트 입력</div>\n" +
                                    "                                                                                <div class=\"ui form fluid mb10\">\n" +
                                    "                                                                                    <textarea rows=\"3\"></textarea>\n" +
                                    "                                                                                </div>\n" +
                                    "                                                                                <div class=\"mb10\">동작</div>\n" +
                                    "                                                                                <div class=\"ui form fluid mb10\">\n" +
                                    "                                                                                    <select>\n" +
                                    "                                                                                        <option>동작</option>\n" +
                                    "                                                                                    </select>\n" +
                                    "                                                                                </div>\n" +
                                    "                                                                                <div class=\"action-wrap\">\n" +
                                    "                                                                                    <button type=\"button\" class=\"ui tiny compact button\">취소</button>\n" +
                                    "                                                                                    <button type=\"button\" class=\"ui tiny compact brand button\">확인</button>\n" +
                                    "                                                                                </div>\n" +
                                    "                                                                            </div>\n" +
                                    "                                                                        </div>\n" +
                                    "                                                                    </div>",
                                "typenode": false,
                                "inputs": {},
                                "outputs": {},
                                "pos_x": 350,
                                "pos_y": 50
                            },
                        }
                    },
                }
            }
            editor.start();
            editor.import(dataToImport);




            // Events!
            editor.on('nodeCreated', function(id) {
                console.log("Node created " + id);
            })

            editor.on('nodeRemoved', function(id) {
                console.log("Node removed " + id);
            })

            editor.on('nodeSelected', function(id) {
                console.log("Node selected " + id);
            })

            editor.on('moduleCreated', function(name) {
                console.log("Module Created " + name);
            })

            editor.on('moduleChanged', function(name) {
                console.log("Module Changed " + name);
            })

            editor.on('connectionCreated', function(connection) {
                console.log('Connection created');
                console.log(connection);
            })

            editor.on('connectionRemoved', function(connection) {
                console.log('Connection removed');
                console.log(connection);
            })

            editor.on('mouseMove', function(position) {
                console.log('Position mouse x:' + position.x + ' y:'+ position.y);
            })

            editor.on('nodeMoved', function(id) {
                console.log("Node moved " + id);
            })

            editor.on('zoom', function(zoom) {
                console.log('Zoom level ' + zoom);
            })

            editor.on('translate', function(position) {
                console.log('Translate x:' + position.x + ' y:'+ position.y);
            })

            editor.on('addReroute', function(id) {
                console.log("Reroute added " + id);
            })

            editor.on('removeReroute', function(id) {
                console.log("Reroute removed " + id);
            })

            /* DRAG EVENT */

            /* Mouse and Touch Actions */

            var elements = document.getElementsByClassName('drag-drawflow');
            for (var i = 0; i < elements.length; i++) {
                elements[i].addEventListener('touchend', drop, false);
                elements[i].addEventListener('touchmove', positionMobile, false);
                elements[i].addEventListener('touchstart', drag, false );
            }

            var mobile_item_selec = '';
            var mobile_last_move = null;
            function positionMobile(ev) {
                mobile_last_move = ev;
            }

            function allowDrop(ev) {
                ev.preventDefault();
            }

            function drag(ev) {
                if (ev.type === "touchstart") {
                    mobile_item_selec = ev.target.closest(".drag-drawflow").getAttribute('data-node');
                } else {
                    ev.dataTransfer.setData("node", ev.target.getAttribute('data-node'));
                }
            }

            function drop(ev) {
                if (ev.type === "touchend") {
                    var parentdrawflow = document.elementFromPoint( mobile_last_move.touches[0].clientX, mobile_last_move.touches[0].clientY).closest("#drawflow");
                    if(parentdrawflow != null) {
                        addNodeToDrawFlow(mobile_item_selec, mobile_last_move.touches[0].clientX, mobile_last_move.touches[0].clientY);
                    }
                    mobile_item_selec = '';
                } else {
                    ev.preventDefault();
                    var data = ev.dataTransfer.getData("node");
                    addNodeToDrawFlow(data, ev.clientX, ev.clientY);
                }

            }

            function addNodeToDrawFlow(name, pos_x, pos_y) {
                if(editor.editor_mode === 'fixed') {
                    return false;
                }
                pos_x = pos_x * ( editor.precanvas.clientWidth / (editor.precanvas.clientWidth * editor.zoom)) - (editor.precanvas.getBoundingClientRect().x * ( editor.precanvas.clientWidth / (editor.precanvas.clientWidth * editor.zoom)));
                pos_y = pos_y * ( editor.precanvas.clientHeight / (editor.precanvas.clientHeight * editor.zoom)) - (editor.precanvas.getBoundingClientRect().y * ( editor.precanvas.clientHeight / (editor.precanvas.clientHeight * editor.zoom)));


                switch (name) {
                    case 'facebook':
                        var facebook = `
        <div>
          <div class="title-box"><i class="fab fa-facebook"></i> Facebook Message</div>
        </div>
        `;
                        editor.addNode('facebook', 0,  1, pos_x, pos_y, 'facebook', {}, facebook );
                        break;
                    case 'slack':
                        var slackchat = `
          <div>
            <div class="title-box"><i class="fab fa-slack"></i> Slack chat message</div>
          </div>
          `
                        editor.addNode('slack', 1, 0, pos_x, pos_y, 'slack', {}, slackchat );
                        break;
                    case 'github':
                        var githubtemplate = `
          <div>
            <div class="title-box"><i class="fab fa-github "></i> Github Stars</div>
            <div class="box">
              <p>Enter repository url</p>
            <input type="text" df-name>
            </div>
          </div>
          `;
                        editor.addNode('github', 0, 1, pos_x, pos_y, 'github', { "name": ''}, githubtemplate );
                        break;
                    case 'telegram':
                        var telegrambot = `
          <div>
            <div class="title-box"><i class="fab fa-telegram-plane"></i> Telegram bot</div>
            <div class="box">
              <p>Send to telegram</p>
              <p>select channel</p>
              <select df-channel>
                <option value="channel_1">Channel 1</option>
                <option value="channel_2">Channel 2</option>
                <option value="channel_3">Channel 3</option>
                <option value="channel_4">Channel 4</option>
              </select>
            </div>
          </div>
          `;
                        editor.addNode('telegram', 1, 0, pos_x, pos_y, 'telegram', { "channel": 'channel_3'}, telegrambot );
                        break;
                    case 'aws':
                        var aws = `
          <div>
            <div class="title-box"><i class="fab fa-aws"></i> Aws Save </div>
            <div class="box">
              <p>Save in aws</p>
              <input type="text" df-db-dbname placeholder="DB name"><br><br>
              <input type="text" df-db-key placeholder="DB key">
              <p>Output Log</p>
            </div>
          </div>
          `;
                        editor.addNode('aws', 1, 1, pos_x, pos_y, 'aws', { "db": { "dbname": '', "key": '' }}, aws );
                        break;
                    case 'log':
                        var log = `
            <div>
              <div class="title-box"><i class="fas fa-file-signature"></i> Save log file </div>
            </div>
            `;
                        editor.addNode('log', 1, 0, pos_x, pos_y, 'log', {}, log );
                        break;
                    case 'google':
                        var google = `
            <div>
              <div class="title-box"><i class="fab fa-google-drive"></i> Google Drive save </div>
            </div>
            `;
                        editor.addNode('google', 1, 0, pos_x, pos_y, 'google', {}, google );
                        break;
                    case 'email':
                        var email = `
            <div>
              <div class="title-box"><i class="fas fa-at"></i> Send Email </div>
            </div>
            `;
                        editor.addNode('email', 1, 0, pos_x, pos_y, 'email', {}, email );
                        break;

                    case 'template':
                        var template = `
            <div>
              <div class="title-box"><i class="fas fa-code"></i> Template</div>
              <div class="box">
                Ger Vars
                <textarea df-template></textarea>
                Output template with vars
              </div>
            </div>
            `;
                        editor.addNode('template', 1, 1, pos_x, pos_y, 'template', { "template": 'Write your template'}, template );
                        break;
                    case 'multiple':
                        var multiple = `
            <div>
              <div class="box">
                Multiple!
              </div>
            </div>
            `;
                        editor.addNode('multiple', 3, 4, pos_x, pos_y, 'multiple', {}, multiple );
                        break;
                    case 'personalized':
                        var personalized = `
            <div>
              Personalized
            </div>
            `;
                        editor.addNode('personalized', 1, 1, pos_x, pos_y, 'personalized', {}, personalized );
                        break;
                    case 'dbclick':
                        var dbclick = `
            <div>
            <div class="title-box"><i class="fas fa-mouse"></i> Db Click</div>
              <div class="box dbclickbox" ondblclick="showpopup(event)">
                Db Click here
                <div class="modal" style="display:none">
                  <div class="modal-content">
                    <span class="close" onclick="closemodal(event)">&times;</span>
                    Change your variable {name} !
                    <input type="text" df-name>
                  </div>

                </div>
              </div>
            </div>
            `;
                        editor.addNode('dbclick', 1, 1, pos_x, pos_y, 'dbclick', { name: ''}, dbclick );
                        break;

                    default:
                }
            }

            var transform = '';
            function showpopup(e) {
                e.target.closest(".drawflow-node").style.zIndex = "9999";
                e.target.children[0].style.display = "block";
                //document.getElementById("modalfix").style.display = "block";

                //e.target.children[0].style.transform = 'translate('+translate.x+'px, '+translate.y+'px)';
                transform = editor.precanvas.style.transform;
                editor.precanvas.style.transform = '';
                editor.precanvas.style.left = editor.canvas_x +'px';
                editor.precanvas.style.top = editor.canvas_y +'px';
                console.log(transform);

                //e.target.children[0].style.top  =  -editor.canvas_y - editor.container.offsetTop +'px';
                //e.target.children[0].style.left  =  -editor.canvas_x  - editor.container.offsetLeft +'px';
                editor.editor_mode = "fixed";

            }

            function closemodal(e) {
                e.target.closest(".drawflow-node").style.zIndex = "2";
                e.target.parentElement.parentElement.style.display  ="none";
                //document.getElementById("modalfix").style.display = "none";
                editor.precanvas.style.transform = transform;
                editor.precanvas.style.left = '0px';
                editor.precanvas.style.top = '0px';
                editor.editor_mode = "edit";
            }

            function changeModule(event) {
                var all = document.querySelectorAll(".menu ul li");
                for (var i = 0; i < all.length; i++) {
                    all[i].classList.remove('selected');
                }
                event.target.classList.add('selected');
            }

            function changeMode(option) {

                //console.log(lock.id);
                if(option == 'lock') {
                    lock.style.display = 'none';
                    unlock.style.display = 'block';
                } else {
                    lock.style.display = 'block';
                    unlock.style.display = 'none';
                }

            }

        </script>
        <script>


            $('.chatbot-control-container .arrow-button').click(function(){
                $(this).toggleClass('show');
                $(this).parent('.chatbot-control-container').toggleClass('active');
            });

            $("input:text").click(function() {
                $(this).parent().find("input:file").click();
            });

            $('input:file', '.ui.action.input')
                .on('change', function(e) {
                    var name = e.target.files[0].name;
                    $('input:text', $(e.target).parent()).val(name);
                });

        </script>
    </tags:scripts>
</tags:layout>
