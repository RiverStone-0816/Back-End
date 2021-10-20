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
                            <div class="panel-body remove-padding full-height">
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
                                                        <div>
                                                            <button class="ui mini button">수정</button>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="chatbot-box-sub-label skyblue">일반 블록</div>
                                            <div class="block-list-container">
                                                <ul class="block-list-ul">
                                                    <li class="block-list">
                                                        <div class="block-name">폴백 블록</div>
                                                        <div>
                                                            <button class="ui mini button">수정</button>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="chatbot-main-container flex-100">

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
                                                       <%-- <div class="drawflow">
                                                            <div class="parent-node">
                                                                <div class="drawflow-node">
                                                                    <div class="drawflow_content_node">
                                                                        <div>
                                                                            <div class="title-box">봇 기본설정</div>
                                                                            <div class="box">d</div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>--%>
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

    <tags:scripts>
        <script>
            var id = document.getElementById("drawflow");
            const editor = new Drawflow(id);
            editor.reroute = true;
            const dataToImport = {
                "drawflow": {
                    "Home": {
                        "data": {
                            "1": {
                                "id": 1,
                                "name": "welcome",
                                "data": {},
                                "class": "welcome",
                                "html": "    <div>      <div class=\"title-box\">Welcome!!</div>     <div class=\"box\">        <p>Simple flow library <b>demo</b>        <a href=\"https://github.com/jerosoler/Drawflow\" target=\"_blank\">Drawflow</a> by <b>Jero Soler</b></p><br>     <p>Multiple input / outputs<br>           Data sync nodes<br>           Import / export<br>           Modules support<br>           Simple use<br>           Type: Fixed or Edit<br>           Events: view console<br>           Pure Javascript<br>        </p>        <br>        <p><b><u>Shortkeys:</u></b></p>        <p> <b>Delete</b> for remove selected<br>         Mouse Left Click == Move<br>         Mouse Right == Delete Option<br>         Ctrl + Wheel == Zoom      Mobile support<br>        ...</p>      </div>    </div>",
                                "typenode": false,
                                "inputs": {},
                                "outputs": {},
                                "pos_x": 50,
                                "pos_y": 50
                            },
                            "2": {
                                "id": 2,
                                "name": "slack",
                                "data": {},
                                "class": "slack",
                                "html": "\n          <div>\n            <div class=\"title-box\"><i class=\"fab fa-slack\"></i> Slack chat message</div>\n          </div>\n          ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "7",
                                            "input": "output_1"
                                        }]
                                    }
                                },
                                "outputs": {},
                                "pos_x": 1028,
                                "pos_y": 87
                            },
                            "3": {
                                "id": 3,
                                "name": "telegram",
                                "data": {
                                    "channel": "channel_2"
                                },
                                "class": "telegram",
                                "html": "\n          <div>\n            <div class=\"title-box\"><i class=\"fab fa-telegram-plane\"></i> Telegram bot</div>\n            <div class=\"box\">\n              <p>Send to telegram</p>\n              <p>select channel</p>\n              <select df-channel>\n                <option value=\"channel_1\">Channel 1</option>\n                <option value=\"channel_2\">Channel 2</option>\n                <option value=\"channel_3\">Channel 3</option>\n                <option value=\"channel_4\">Channel 4</option>\n              </select>\n            </div>\n          </div>\n          ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "7",
                                            "input": "output_1"
                                        }]
                                    }
                                },
                                "outputs": {},
                                "pos_x": 1032,
                                "pos_y": 184
                            },
                            "4": {
                                "id": 4,
                                "name": "email",
                                "data": {},
                                "class": "email",
                                "html": "\n            <div>\n              <div class=\"title-box\"><i class=\"fas fa-at\"></i> Send Email </div>\n            </div>\n            ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "5",
                                            "input": "output_1"
                                        }]
                                    }
                                },
                                "outputs": {},
                                "pos_x": 1033,
                                "pos_y": 439
                            },
                            "5": {
                                "id": 5,
                                "name": "template",
                                "data": {
                                    "template": "Write your template"
                                },
                                "class": "template",
                                "html": "\n            <div>\n              <div class=\"title-box\"><i class=\"fas fa-code\"></i> Template</div>\n              <div class=\"box\">\n                Ger Vars\n                <textarea df-template></textarea>\n                Output template with vars\n              </div>\n            </div>\n            ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "6",
                                            "input": "output_1"
                                        }]
                                    }
                                },
                                "outputs": {
                                    "output_1": {
                                        "connections": [{
                                            "node": "4",
                                            "output": "input_1"
                                        }, {
                                            "node": "11",
                                            "output": "input_1"
                                        }]
                                    }
                                },
                                "pos_x": 607,
                                "pos_y": 304
                            },
                            "6": {
                                "id": 6,
                                "name": "github",
                                "data": {
                                    "name": "https://github.com/jerosoler/Drawflow"
                                },
                                "class": "github",
                                "html": "\n          <div>\n            <div class=\"title-box\"><i class=\"fab fa-github \"></i> Github Stars</div>\n            <div class=\"box\">\n              <p>Enter repository url</p>\n            <input type=\"text\" df-name>\n            </div>\n          </div>\n          ",
                                "typenode": false,
                                "inputs": {},
                                "outputs": {
                                    "output_1": {
                                        "connections": [{
                                            "node": "5",
                                            "output": "input_1"
                                        }]
                                    }
                                },
                                "pos_x": 341,
                                "pos_y": 191
                            },
                            "7": {
                                "id": 7,
                                "name": "facebook",
                                "data": {},
                                "class": "facebook",
                                "html": "\n        <div>\n          <div class=\"title-box\"><i class=\"fab fa-facebook\"></i> Facebook Message</div>\n        </div>\n        ",
                                "typenode": false,
                                "inputs": {},
                                "outputs": {
                                    "output_1": {
                                        "connections": [{
                                            "node": "2",
                                            "output": "input_1"
                                        }, {
                                            "node": "3",
                                            "output": "input_1"
                                        }, {
                                            "node": "11",
                                            "output": "input_1"
                                        }]
                                    }
                                },
                                "pos_x": 347,
                                "pos_y": 87
                            },
                            "11": {
                                "id": 11,
                                "name": "log",
                                "data": {},
                                "class": "log",
                                "html": "\n            <div>\n              <div class=\"title-box\"><i class=\"fas fa-file-signature\"></i> Save log file </div>\n            </div>\n            ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "5",
                                            "input": "output_1"
                                        }, {
                                            "node": "7",
                                            "input": "output_1"
                                        }]
                                    }
                                },
                                "outputs": {},
                                "pos_x": 1031,
                                "pos_y": 363
                            }
                        }
                    },
                    "Other": {
                        "data": {
                            "8": {
                                "id": 8,
                                "name": "personalized",
                                "data": {},
                                "class": "personalized",
                                "html": "\n            <div>\n              Personalized\n            </div>\n            ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "12",
                                            "input": "output_1"
                                        }, {
                                            "node": "12",
                                            "input": "output_2"
                                        }, {
                                            "node": "12",
                                            "input": "output_3"
                                        }, {
                                            "node": "12",
                                            "input": "output_4"
                                        }]
                                    }
                                },
                                "outputs": {
                                    "output_1": {
                                        "connections": [{
                                            "node": "9",
                                            "output": "input_1"
                                        }]
                                    }
                                },
                                "pos_x": 764,
                                "pos_y": 227
                            },
                            "9": {
                                "id": 9,
                                "name": "dbclick",
                                "data": {
                                    "name": "Hello World!!"
                                },
                                "class": "dbclick",
                                "html": "\n            <div>\n            <div class=\"title-box\"><i class=\"fas fa-mouse\"></i> Db Click</div>\n              <div class=\"box dbclickbox\" ondblclick=\"showpopup(event)\">\n                Db Click here\n                <div class=\"modal\" style=\"display:none\">\n                  <div class=\"modal-content\">\n                    <span class=\"close\" onclick=\"closemodal(event)\">&times;</span>\n                    Change your variable {name} !\n                    <input type=\"text\" df-name>\n                  </div>\n\n                </div>\n              </div>\n            </div>\n            ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": [{
                                            "node": "8",
                                            "input": "output_1"
                                        }]
                                    }
                                },
                                "outputs": {
                                    "output_1": {
                                        "connections": [{
                                            "node": "12",
                                            "output": "input_2"
                                        }]
                                    }
                                },
                                "pos_x": 209,
                                "pos_y": 38
                            },
                            "12": {
                                "id": 12,
                                "name": "multiple",
                                "data": {},
                                "class": "multiple",
                                "html": "\n            <div>\n              <div class=\"box\">\n                Multiple!\n              </div>\n            </div>\n            ",
                                "typenode": false,
                                "inputs": {
                                    "input_1": {
                                        "connections": []
                                    },
                                    "input_2": {
                                        "connections": [{
                                            "node": "9",
                                            "input": "output_1"
                                        }]
                                    },
                                    "input_3": {
                                        "connections": []
                                    }
                                },
                                "outputs": {
                                    "output_1": {
                                        "connections": [{
                                            "node": "8",
                                            "output": "input_1"
                                        }]
                                    },
                                    "output_2": {
                                        "connections": [{
                                            "node": "8",
                                            "output": "input_1"
                                        }]
                                    },
                                    "output_3": {
                                        "connections": [{
                                            "node": "8",
                                            "output": "input_1"
                                        }]
                                    },
                                    "output_4": {
                                        "connections": [{
                                            "node": "8",
                                            "output": "input_1"
                                        }]
                                    }
                                },
                                "pos_x": 179,
                                "pos_y": 272
                            }
                        }
                    }
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
