<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <div class="ui modal tiny window-room" id="room-popup">
        <i class="close icon -close-room"></i>
        <div class="header">
            <div style="display: none" id="members-tooltip" class="tool-shadow-box">
                <ul id="messenger-room-members"></ul>
            </div>
            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/chat.jsp">
                <jsp:param name="id" value="chat-icon"/>
                <jsp:param name="width" value="20"/>
                <jsp:param name="height" value="20"/>
            </jsp:include>
            <span class="room-title -chatroom-name" onclick="updateRoomName()"></span>
            <div class="btn-wrap">
                <button type="button" class="ui icon button" onclick="messenger.openInvitationModal()">
                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/person-plus.jsp">
                        <jsp:param name="width" value="16"/>
                        <jsp:param name="height" value="16"/>
                    </jsp:include>
                </button>
                <button type="button" class="ui icon button -leave-room">
                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/rect-in.jsp">
                        <jsp:param name="width" value="16"/>
                        <jsp:param name="height" value="16"/>
                    </jsp:include>
                </button>
            </div>
        </div>
        <div class="content dp-flex ff-column">
            <div class="inner-box flex room-top">
                <div class="ui icon fluid input small flex-auto">
                    <input type="text" class="-search-text" placeholder="검색어 입력">
                    <i class="search link icon -search-button"></i>
                </div>
                <div class="num -text-count">0/0</div>
                <div class="btn-wrap">
                    <button type="button" class="ui icon brand button -move-to-next-text">
                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/to-top.jsp">
                            <jsp:param name="width" value="16"/>
                            <jsp:param name="height" value="9.867"/>
                        </jsp:include>
                    </button>
                    <button type="button" class="ui icon brand button -move-to-prev-text">
                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/to-bottom.jsp">
                            <jsp:param name="width" value="16"/>
                            <jsp:param name="height" value="9.867"/>
                        </jsp:include>
                    </button>
                </div>
            </div>
            <div class="inner-box room-content flex-auto">
                <ul class="message-balloon"></ul>
            </div>
        </div>
        <div class="actions pd-unset">
            <div class="inner-box flex">
                <div class="file-att-wrap">
                    <button type="button" class="ui icon brand button -upload-file">
                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/clip.jsp">
                            <jsp:param name="width" value="20"/>
                            <jsp:param name="height" value="20"/>
                        </jsp:include>
                    </button>
                    <input type="file" class="dp-none">
                </div>
                <div class="flex-auto">
                    <div class="ui form">
                        <div class="field">
                            <textarea rows="2" id="messenger-message" style="resize: none;"></textarea>
                        </div>
                    </div>
                </div>
                <div class="pl10">
                    <button type="button" class="ui darkblue button fluid pull-height" onclick="messenger.sendMessage()">전송</button>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            $('#chat-icon').mouseenter(function () {
                $('#members-tooltip').show();
            }).mouseleave(function () {
                $('#members-tooltip').hide();
            });

            if (window.isElectron) {
                $('head').append('<script src="<c:url value="/resources/ipcc-messenger/js/electron/MessengerRoom.js?version=${version}"/>"><\/script>');
                messenger = new MessengerRoom('${id}', '${g.escapeQuote(user.id)}', '${g.escapeQuote(user.groupTreeName)}', '${g.escapeQuote(accessToken)}');

                function updateRoomName() {
                    prompt('채팅방 이름 변경').done(function (text) {
                        text = text.trim();
                        if (!text)
                            return;
                        restSelf.put('/api/chatt/${id}/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
                            messenger.changeRoomName('${g.user.companyId}', text);
                            messenger.roomInfo.roomName = text;
                            messenger.showRoomTitle();
                        });
                    });
                }

                window.addEventListener('focus', function () {
                    ipcRenderer.send('roomFocusOn', {"roomId" : messenger.roomId, "roomInfo" : messenger.roomInfo});
                });
            } else {

                function updateRoomName() {
                    prompt('채팅방 이름 변경').done(function (text) {
                        text = text.trim();
                        if (!text)
                            return;
                        restSelf.put('/api/chatt/${id}/room-name?newRoomName=' + encodeURIComponent(text)).done(function () {
                            messenger.communicator.changeRoomName('${id}', text);
                            messenger.currentRoom.roomName = text;
                            messenger.showRoomTitle();
                        });
                    });
                }
            }
        </script>
    </tags:scripts>
</mobileTags:layout>
