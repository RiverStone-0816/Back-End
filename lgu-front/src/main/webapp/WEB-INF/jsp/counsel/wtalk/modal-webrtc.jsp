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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal inverted small">
    <i class="close icon"></i>
    <div class="header">음성/영상 통화</div>

    <div class="content rows scrolling">
        <div class="sub-content ui container fluid unstackable" id="modal-webrtc-body">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <span id="chatText">고객 수락 대기중...</span>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic green button" id="webrtcMute" onclick="doMute()">Mute</button>
                        <button type="button" class="ui basic green button" id="webrtcHangUp" onclick="doHangup()">끊기</button>
                    </div>
                </div>
                <div class="panel-body" style="width: 100%; overflow-x: auto;">
                    <div class="pull-left">
                        <img src="<c:url value='/resources/images/volume.gif'/>" id="remotevideoTmp" width="300" height="200" style="display: none">
                        <video id="remotevideo" autoplay playsinline width="300" height="200"/>
                    </div>
                    <div class="pull-right">
                        <img src="<c:url value='/resources/images/volume.gif'/>" id="myvideoTmp" width="300" height="200" style="display: none">
                        <video id="myvideo" autoplay playsinline muted="muted" width="300" height="200"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>