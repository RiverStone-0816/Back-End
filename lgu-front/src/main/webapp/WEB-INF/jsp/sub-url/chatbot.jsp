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
                                    <div class="chatbot-main-container">
                                        <div id="drawflow"></div>
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
                                                            <img src="<c:url value="../resources/images/eicn-sample.png"/>" class="customer-img">
                                                            <p>이아이씨엔 채팅상담을 이용해 주셔서 감사합니다.
                                                                문의사항을 입력해주시면 상담원이 답변드리겠습니다.
                                                                감사합니다.</p>
                                                        </div>
                                                        <div class="sample-bubble">
                                                            <p>
                                                                [ 이아이씨엔 ] 고객님 안녕하세요.
                                                                [ 2021-07-29 ] 에 총 [IP460S] 을(를)
                                                                [ 30 ] 개 구매하셨습니다.
                                                            </p>
                                                        </div>
                                                        <div class="sample-bubble">
                                                            <button type="button" class="chatbot-button">이름없는 버튼</button>
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
            const id = document.getElementById("drawflow");
            const editor = new Drawflow(id);
            editor.start();

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
