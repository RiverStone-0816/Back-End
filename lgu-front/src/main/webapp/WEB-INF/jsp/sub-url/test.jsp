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
                            <div class="panel-heading">
                                <h3 class="panel-title"><img src="<c:url value="/resources/images/chatbot-square.svg"/>"> 봇 에디터</h3>
                                <div class="pull-right">
                                    <button type="button" class="ui button">봇 복사</button>
                                    <button type="button" class="ui button">봇 테스트</button>
                                    <button type="button" class="ui button">봇 저장</button>
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
                                    <div class="chatbot-control-container active">
                                        <div class="chatbot-control-inner">
                                            <div class="chatbot-box-label">설정영역</div>
                                            <div class="chatbot-control-body">
                                                <div class="empty">선택된 내용이 없습니다.</div>
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


        </script>
    </tags:scripts>
</tags:layout>
