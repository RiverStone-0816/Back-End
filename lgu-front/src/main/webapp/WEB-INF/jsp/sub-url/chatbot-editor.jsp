<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<tags:scripts/>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=440, initial-scale=0.8"/>
    <title></title>
    <tags:favicon/>
    <style>
        .ui-chatbot-canvas {position: absolute; top: 0; left: 0;}
        .ui-chatbot-block {}
        .ui-chatbot-block-point {}
        .ui-chatbot-block-title {}
        .ui-chatbot-display-container {}
        .ui-chatbot-display-element {}
        .ui-chatbot-display-element[data-type='TEXT'] {}
        .ui-chatbot-display-element[data-type='IMAGE'] {}
        .ui-chatbot-display-element[data-type='CARD'] {}
        .ui-chatbot-display-element[data-type='LIST'] {}
        .ui-chatbot-display-text {}
        .ui-chatbot-display-image {}
        .ui-chatbot-display-card {}
        .ui-chatbot-display-card-head {}
        .ui-chatbot-display-card-body {}
        .ui-chatbot-display-card-image {}
        .ui-chatbot-display-list {}
        .ui-chatbot-display-list-item {}
        .ui-chatbot-display-list-item-head {}
        .ui-chatbot-display-list-item-body {}
        .ui-chatbot-display-list-item-image {}
        .ui-chatbot-button {}
        .ui-chatbot-button-point {}
        .ui-chatbot-button[data-type='TO_NEXT_BLOCK'] {}
        .ui-chatbot-button[data-type='TO_OTHER_BLOCK'] {}
        .ui-chatbot-button[data-type='TO_URL'] {}
        .ui-chatbot-button[data-type='CALL_CONSULTANT'] {}
        .ui-chatbot-button[data-type='MAKE_TEL_CALL'] {}
        .ui-chatbot-button[data-type='CALL_API'] {}
        .ui-chatbot-button-title {}
        .ui-chatbot-line path {stroke: black; fill: transparent}
    </style>
</head>
<body>

<div id="app" style="position: relative; overflow: auto">
    <div ref="canvas" class="ui-chatbot-canvas" :style="'width: ' + canvasWidth + 'px; height: ' + canvasHeight + 'px;'" style="position: absolute; top: 0; left: 0;">
        <div v-for="block in blocks" :key="block.id" class="ui-chatbot-block" style="position: absolute;" :style="'top: ' + block.y + 'px; left: ' + block.x + 'px;'">
            <div :ref="'blockPoint.' + block.id" class="ui-chatbot-block-point"></div>
            <div class="ui-chatbot-block-title">{{block.title}}</div>

            <div class="ui-chatbot-display-container">
                <div v-for="(element, i) in displayElements" :key="i" class="ui-chatbot-display-element" :data-type="element.type">
                    <text v-if="element.type === DISPLAY_TYPE.TEXT" class="ui-chatbot-display-text">{{element.content.TEXT}}</text>
                    <img v-else-if="element.type === DISPLAY_TYPE.IMAGE" class="ui-chatbot-display-image" alt="display content" :src="element.content.IMAGE"/>
                    <div v-else-if="element.type === DISPLAY_TYPE.CARD" class="ui-chatbot-display-card">
                        <div class="ui-chatbot-display-card-head">{{element.content.CARD.head}}</div>
                        <div class="ui-chatbot-display-card-body">{{element.content.CARD.body}}</div>
                        <img class="ui-chatbot-display-card-image" alt="display content" :src="element.content.CARD.image"/>
                    </div>
                    <div v-else-if="element.type === DISPLAY_TYPE.LIST" class="ui-chatbot-display-list">
                        <a v-if="e.url" v-for="(e, i) in element.content.LIST" :key="i" class="ui-chatbot-display-list-item" target="_blank" :href="e.url">
                            <div class="ui-chatbot-display-list-item-head">{{e.head}}</div>
                            <div class="ui-chatbot-display-list-item-body">{{e.body}}</div>
                            <img class="ui-chatbot-display-list-item-image" alt="display content" :src="e.image"/>
                        </a>
                        <div v-if="!e.url" v-for="(e, i) in element.content.LIST" :key="i" class="ui-chatbot-display-list-item">
                            <div class="ui-chatbot-display-list-item-head">{{e.head}}</div>
                            <div class="ui-chatbot-display-list-item-body">{{e.body}}</div>
                            <img class="ui-chatbot-display-list-item-image" alt="display content" :src="e.image"/>
                        </div>
                    </div>
                </div>
            </div>

            <div v-for="button in buttons" :key="button.id" class="ui-chatbot-button" :data-type="button.type">
                <div :ref="'buttonPoint.' + button.id" class="ui-chatbot-button-point"></div>
                <div class="ui-chatbot-button-title">{{button.title}}</div>
            </div>
        </div>

        <svg class="ui-chatbot-line" v-for="(e,i) in getLineElements()" :key="i" xmlns="http://www.w3.org/2000/svg">
            <path :d="renderLine(e)"/>
        </svg>
    </div>
</div>

<tags:scripts>
    <script src="<c:url value="/webjars/vue/3.2.10/dist/vue.global.js"/>"></script>
    <script src="<c:url value="/resources/js/chatbot-editor.js"/>"></script>
</tags:scripts>

<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>
</div>

</body>
</html>
