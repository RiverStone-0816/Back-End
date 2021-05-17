<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <div class="ui modal window" id="msg-send-popup">
        <i class="close icon"></i>
        <div class="header" style="fill: white;">
            <span class="material-icons"><jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/chat.jsp">
                <jsp:param name="id" value="chat-icon"/>
                <jsp:param name="width" value="20"/>
                <jsp:param name="height" value="20"/>
            </jsp:include></span> &ensp;초대하기
        </div>
        <div class="content dp-flex ff-column">
            <div class="ui icon fluid input small -filter-text">
                <input type="text" id="messenger-filter-text" placeholder="검색어 입력">
                <i class="search link icon"></i>
            </div>
            <div class="inner-box scroll modal-organization-wrap" style="display: block;">
                <h1 class="sub-title no-drag">조직도</h1>
                <div class="ui list organization-container" id="organization"></div>
            </div>
            </div>
        <div class="actions">
            <button type="button" class="ui darkblue button tiny" onclick="invite()">초대하기</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            if (window.isElectron) {
                $('head').append('<script src="<c:url value="/resources/ipcc-messenger/js/electron/MessengerInvitation.js?version=${version}"/>"><\/script>');
                const messengerInvitation = new MessengerInvitation();
                function invite() {
                    messengerInvitation.invite();
                }
            } else {
                function invite() {
                    const users = [];
                    const userNames = [];
                    const userMap = {};

                    $('.-messenger-user').filter('.active').removeClass('active').each(function () {
                        const id = $(this).attr('data-id');
                        const name = $(this).attr('data-name');

                        if (users.indexOf(id) >= 0)
                            return;

                        if (keys(messenger.currentRoom.members).filter(function (userid) {
                            return userid === id;
                        }).length > 0)
                            return;

                        users.push(id);
                        userNames.push(name);
                        userMap[id] = name;
                    });

                    if (!users.length)
                        return;

                    messenger.communicator.invite(messenger.currentRoom.id, users, userNames);
                    restSelf.put('/api/chatt/' + encodeURIComponent(messenger.currentRoom.id) + '/chatt-member', {memberList: users}).done(function () {
                        users.map(function (e) {
                            if (!messenger.currentRoom.members[e])
                                messenger.currentRoom.members[e] = {}

                            messenger.currentRoom.members[e].userid = e;
                            messenger.currentRoom.members[e].userName = userMap[e];
                        });
                        messenger.showRoomTitle();

                        messenger._loadRoomName(messenger.currentRoom.id);

                        self.close();
                    });
                }
            }

            $('#messenger-filter-text').keyup(function () {
                filterItem($('#messenger-filter-text'));
            });

            function filterItem($this) {
                const text = ($this.val() || '').trim();

                $('.-messenger-folder').show().filter(function () {
                    return $(this).parent()[0] ===  $('.organization-container')[0];
                }).each(function () {
                    function renderAndReturnContainsText(element) {
                        let containsText = false;

                        if (element.hasClass('-messenger-folder')) {
                            containsText = element.children('.content').children('.header').text().indexOf(text) >= 0;

                            element.children('.content').children('.list').children().map(function () {
                                containsText |= renderAndReturnContainsText($(this));
                            });
                        } else {
                            containsText = element.text().indexOf(text) >= 0;
                        }

                        if (containsText) {
                            element.show();
                            return true;
                        } else {
                            element.hide();
                            return false;
                        }
                    }

                    renderAndReturnContainsText($(this));
                });
            }
        </script>
    </tags:scripts>
</mobileTags:layout>
