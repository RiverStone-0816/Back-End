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

        .ui-chatbot-canvas {
            position: relative;
        }
        .ui-chatbot-canvas:before {
            content: ' ';
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            background-color: #f6f6f6;
            background-image: linear-gradient(90deg, #cdcccc 0px, #cdcccc 1px, transparent 1px, transparent 99px, transparent 100px),
            linear-gradient(#cdcccc 0px, #cdcccc 1px, transparent 1px, transparent 99px, transparent 100px),
            linear-gradient(#efefef 0px, #efefef 1px, transparent 1px, transparent 99px, transparent 100px),
            linear-gradient(90deg, #efefef 0px, #efefef 1px, transparent 1px, transparent 99px, transparent 100px),
            linear-gradient(transparent 0px, transparent 5px, #fff 5px, #fff 95px, transparent 95px, transparent 100px),
            linear-gradient(90deg, #efefef 0px, #efefef 1px, transparent 1px, transparent 99px, #efefef 99px, #efefef 100px),
            linear-gradient(90deg, transparent 0px, transparent 5px, #fff 5px, #fff 95px, transparent 95px, transparent 100px),
            linear-gradient(transparent 0px, transparent 1px, #fff 1px, #fff 99px, transparent 99px, transparent 100px),
            linear-gradient(#cdcccc, #cdcccc);
            background-size: 0 100%, 100% 0, 100% 15px, 15px 100%, 100% 0, 0 100%, 100px 100%, 100px 100px, 0 0;
            background-position: -1px -1px;
            opacity: 0.75;
        }

        .ui-chatbot-node:before {
            position: absolute !important;
            width: 230px;
            min-height: 30px;
            border: dashed 2px #d0d0d0;
            background: rgba(0, 0, 0, 0.05);
        }

        .ui-chatbot-node:hover {
            background: rgba(0, 0, 0, 0.1);
        }

        .ui-chatbot-node {
            position: relative;
        }

        .ui-chatbot-node-handle {
        }

        .ui-chatbot-node-title {
            cursor: pointer;
            overflow: hidden;
            white-space: nowrap;
            position: relative;
            display: block;
            font-weight: 700;
            line-height: 31px;
            padding: 0 0 0 11px;
            background: white;
            border-bottom: 1px solid grey;
        }

        .ui-chatbot-node-setup {
            position: absolute;
            right: 24px;
            top: 8px;
        }

        .ui-chatbot-node-eye {
            position: absolute;
            right: 45px;
            top: 8px;
        }

        .ui-chatbot-node-icon {
            font-family: "Font Awesome 5 Free", monospace;
            font-weight: 900;
            overflow: hidden;
            cursor: pointer;
            color: #7d7d7d;
        }

        .ui-chatbot-node-remove {
            position: absolute;
            right: 9px;
            top: 8px;
        }

        .ui-chatbot-node-remove:before {
        . ui-chatbot-node-icon();
            content: "\f00d";
        }

        .ui-chatbot-node-line-end-point {
            visibility: hidden;
            position: absolute;
            left: 0;
            top: 50%;
            transform: translate(-50%, -50%);
            overflow: hidden;
            border-radius: 50%;
            width: 5px;
            height: 5px;
            background-color: grey;
        }

        .ui-chatbot-node-point-container {
            position: relative;
            display: block;
            border-left: 0;
            border-right: 0;
        }

        .ui-chatbot-connection-point {
            position: relative;
            display: table;
            width: 100%;
            border-bottom: 1px solid grey;
        }

        .ui-chatbot-connection-point-title {
            overflow: hidden;
            white-space: normal;
            box-sizing: border-box;
            display: table-cell;
            border-right: 1px solid grey;
            height: 30px;
            vertical-align: middle;
            padding: 7px 7px;
            text-align: right;
            background: white;
            line-height: 1.3;
        }

        .ui-chatbot-connection-point-number {
            overflow: hidden;
            white-space: nowrap;
            box-sizing: border-box;
            width: 20%;
            display: table-cell;
            height: 30px;
            vertical-align: middle;
            padding: 0 7px;
            text-align: center;
            font-weight: bold;
            background: grey;
        }

        .ui-chatbot-connection-point-line-start-point {
            visibility: hidden;
            position: absolute;
            right: 0;
            top: 50%;
            transform: translate(50%, -50%);
            overflow: hidden;
            border-radius: 50%;
            width: 5px;
            height: 5px;
            background-color: grey;
        }

        .ui-chatbot-line {
            position: absolute !important;
            border-top: solid 1px grey;
        }
    </style>
</head>
<body>

<div id="app" style="position: relative; overflow: auto">
    <div ref="canvas" class="ui-chatbot-canvas"
         :style="'width: ' + canvasWidth + 'px; height: ' + canvasHeight + 'px;'"
         style="position: absolute; top: 0; left: 0;">

        <div v-for="node in nodes" :key="node.id" class="ui-chatbot-node"
             style="position: absolute;" :data-emtpy="node.empty"
             :style="'top: ' + node.y + 'px; left: ' + node.x + 'px;'">

            <div v-if="!node.empty" class="ui-chatbot-title" v-text="node.title" class="ui-chatbot-node-title"></div>
            <div v-if="!node.empty" v-for="point in points" :key="point.id" class="ui-chatbot-point">
                <div class="ui-chatbot-point-title" v-text="point.title"></div>
            </div>
        </div>

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
