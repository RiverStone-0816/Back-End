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
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>

<mobileTags:layout>
    <c:choose>
        <c:when test="${!(usingServices.contains('CHATWIN') && user.isChatt == 'Y') && !hasExtension}">
            <tags:scripts>
                <script>
                    alert('사용 권한이 없습니다. 내선번호 또는 메신저 권한이 필요합니다.', function () {
                        restSelf.get("/api/auth/logout").done(function () {
                            location.href = contextPath + '/ipcc-messenger';
                        });
                    });
                </script>
            </tags:scripts>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${usingServices.contains('CHATWIN') && user.isChatt == 'Y'}">
                    <main class="container">
                        <aside class="left-panel no-drag">
                                <%--<div class="logo">
                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/uplus.jsp">
                                        <jsp:param name="width" value="35"/>
                                        <jsp:param name="height" value="30"/>
                                    </jsp:include>
                                </div>--%>
                            <section>
                                <ul class="nav">
                                    <c:if test="${hasExtension}">
                                        <li style="margin: 10px 0; height: 70px !important;">
                                            <button class="ui icon button brand" id="user-state-change" title="내상태">
                                                <c:forEach var="e" items="${statusCodes}">
                                                    <c:if test="${e.statusNumber == 0}">
                                                        <span class="material-icons">${e.icon == null || e.icon.trim() == '' ? 'person' : g.htmlQuote(e.icon)}</span>
                                                        <span class="state-name">${g.escapeQuote(e.statusName)}</span>
                                                    </c:if>
                                                </c:forEach>
                                            </button>
                                            <div class="call-state" style="margin: 2px 0 0 -5px; width: 50px; align-content: center;">
                                                <div class="detail overflow-hidden" id="status-keeping-time" style="text-align: center">
                                                        <%--<span class="pull-right" id="status-keeping-time"></span>--%>
                                                </div>
                                            </div>
                                        </li>
                                        <li class="delimiter"></li>
                                    </c:if>
                                    <li>
                                        <button type="button" class="ui icon brand button -tab-indicator" data-tab="tab-profile" title="프로필관리" id="profile-setting-indicator"
                                                style="padding: calc(0.78571429em - 3px); margin-top: 15px; position: relative;">
                                            <c:choose>
                                                <c:when test="${user.profilePhoto != null && user.profilePhoto != ''}">
                                                    <img class="profile-picture -picture" data-id="${user.id}"
                                                         src="${apiServerUrl}/api/memo/profile-resource?path=${g.urlEncode(user.profilePhoto)}&token=${accessToken}"
                                                         style="border-radius: 50%; width: 31px; height: 31px; overflow: hidden;"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="profile-picture -picture" data-id="${user.id}" src="<c:url value="/resources/ipcc-messenger/images/person.png"/>"
                                                         style="border-radius: 50%; width: 31px; height: 31px; overflow: hidden;"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <i class="cog icon" style="position: absolute; bottom: 9px; right: 3px;"></i>
                                        </button>
                                    </li>
                                    <li>
                                        <button type="button" class="ui icon brand button active -tab-indicator" title="조직도" data-tab="tab1">
                                            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/list.jsp">
                                                <jsp:param name="width" value="25"/>
                                                <jsp:param name="height" value="25"/>
                                            </jsp:include>
                                        </button>
                                    </li>
                                    <li>
                                        <span class="number -message-indicator"></span>
                                        <button type="button" class="ui icon brand button -tab-indicator" title="채팅" data-tab="tab2">
                                            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/chat.jsp">
                                                <jsp:param name="width" value="25"/>
                                                <jsp:param name="height" value="25"/>
                                            </jsp:include>
                                        </button>
                                    </li>
                                    <li>
                                        <span class="number" id="unread-memo-indicator" style="display: ${unreadMemoCount > 0 ? '' : 'none'};">${unreadMemoCount}</span>
                                        <button type="button" class="ui icon brand button -tab-indicator" title="받은쪽지" data-tab="tab4">
                                            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/message-in.jsp">
                                                <jsp:param name="width" value="25"/>
                                                <jsp:param name="height" value="25"/>
                                            </jsp:include>
                                        </button>
                                    </li>
                                    <li>
                                        <button type="button" class="ui icon brand button -tab-indicator" title="보낸쪽지" data-tab="tab5">
                                            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/message-out.jsp">
                                                <jsp:param name="width" value="25"/>
                                                <jsp:param name="height" value="25"/>
                                            </jsp:include>
                                        </button>
                                    </li>
                                </ul>
                            </section>

                            <button class="ui icon brand button state" id="setting-tab-indicator" style="bottom: 60px; overflow-x: hidden; text-align: center; width: 46px;">
                                <i class="ellipsis horizontal icon"></i><span class="state-name">더보기</span>
                            </button>

                            <button class="ui icon brand button state" onclick="logout()" style="overflow-x: hidden; text-align: center; width: 46px;">
                                <i class="window close outline icon"></i><span class="state-name" style="font-size: 12px;">로그아웃</span>
                            </button>
                        </aside>
                        <div class="right-panel ${hasExtension ? '' : 'none-callcontrol'}">
                            <div class="header">
                                <c:if test="${hasExtension}">
                                    <div class="call-state" style="height: 25px; margin-top: 7px;">
                                        <div class="num" style="text-align: center;">
                                            <span style="font-size: 12px; width: 100%;">${g.user.idName.length() > 3 ? g.user.idName.substring(0, 3).concat('..') : g.user.idName}[내선:${g.user.extension}]</span>
                                                <%--<input id="" type="text" value="" style="width: 100%; text-align: center;">--%>
                                        </div>
                                    </div>
                                    <div class="inner-box" id="call-remote-container">
                                        <div class="call-state">
                                            <div class="num">
                                                <input id="calling-number" class="-calling-number" type="text" value="" placeholder="전화번호 입력">
                                            </div>
                                            <div class="name">
                                                <input id="calling-custom" class="-calling-custom" type="text" value="" placeholder="고객명">
                                                    <%--<span class="pull-right" id="status-keeping-time"></span>--%>
                                            </div>
                                        </div>
                                        <div class="state-control">
                                            <ul>
                                                <li>
                                                    <button type="button" class="ui icon button brand state" onclick="tryDial('MAINDB')">
                                                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-out.jsp">
                                                            <jsp:param name="width" value="20"/>
                                                            <jsp:param name="height" value="20"/>
                                                        </jsp:include>
                                                        <div class="explain">전화걸기</div>
                                                    </button>
                                                </li>
                                                <li>
                                                    <button type="button" class="ui icon button brand state -call-receive">
                                                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call.jsp">
                                                            <jsp:param name="width" value="20"/>
                                                            <jsp:param name="height" value="20"/>
                                                        </jsp:include>
                                                        <div class="explain">전화받기</div>
                                                    </button>
                                                </li>
                                                <li>
                                                    <button type="button" class="ui icon button brand state -call-hangup">
                                                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-stop.jsp">
                                                            <jsp:param name="width" value="20"/>
                                                            <jsp:param name="height" value="20"/>
                                                        </jsp:include>
                                                        <div class="explain">전화끊기</div>
                                                    </button>
                                                </li>
                                                <li>
                                                    <button type="button" class="ui icon button brand state -call-pickup">
                                                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-in.jsp">
                                                            <jsp:param name="width" value="20"/>
                                                            <jsp:param name="height" value="20"/>
                                                        </jsp:include>
                                                        <div class="explain">당겨받기</div>
                                                    </button>
                                                </li>
                                                <li>
                                                    <button type="button" class="ui icon button brand state -call-hold">
                                                        <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-pause.jsp">
                                                            <jsp:param name="width" value="20"/>
                                                            <jsp:param name="height" value="20"/>
                                                        </jsp:include>
                                                        <div class="explain">통화보류</div>
                                                    </button>
                                                </li>
                                            </ul>
                                            <ul>
                                                <c:forEach var="e" items="${ProtectArs}">
                                                    <li>
                                                        <button type="button" class="ui icon button ment">
                                                            <div class="explain" data-value="${g.htmlQuote(e.key)}"
                                                                 title="${e.key.startsWith('s') ? (e.key.endsWith('1') ? '성희롱 경고 멘트 후 전화 재연결' : '성희롱 경고 멘트 후 전화 종료') :
                                                             (e.key.endsWith('1') ? '폭언 경고 멘트 후 전화 재연결' : '폭언 경고 멘트 후 전화 종료')}">${g.htmlQuote(e.value)}</div>
                                                        </button>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </c:if>
                                <div class="inner-box flex" id="search-container" style="border-color: transparent;">
                                    <div class="ui icon fluid input small flex-auto">
                                        <input type="text" id="messenger-filter-text">
                                        <i class="search link icon"></i>
                                    </div>
                                    <div class="pl10">
                                        <button type="button" class="ui icon brand button" title="즐겨찾기" onclick="messenger.popupBookmarkModal()">
                                            <span class="material-icons">grade</span>
                                        </button>
                                        <button type="button" class="ui icon brand button" title="채팅" onclick="messenger.openRoom()">
                                            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/chat.jsp">
                                                <jsp:param name="width" value="20"/>
                                                <jsp:param name="height" value="20"/>
                                            </jsp:include>
                                        </button>
                                        <button type="button" class="ui icon brand button" title="쪽지" onclick="messenger.popupMessageSendModal()">
                                            <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/message-out.jsp">
                                                <jsp:param name="width" value="20"/>
                                                <jsp:param name="height" value="20"/>
                                            </jsp:include>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-container">
                                <div id="tab1" class="tab-cont active">
                                    <div>
                                        <div class="inner-box" style="border-color: transparent; margin-top: 20px;">
                                            <h1 class="sub-title no-drag"><i class="star icon"></i>즐겨찾기</h1>
                                            <ul class="favorites-list" id="messenger-bookmark-panel"></ul>
                                        </div>
                                        <div class="inner-box" style="margin-top: -15px;">
                                                <%--<h1 class="sub-title no-drag">조직도</h1>--%>
                                            <div class="ui list organization-container" id="messenger-organization-panel"></div>
                                        </div>
                                    </div>
                                </div>
                                <div id="tab2" class="tab-cont">
                                    <div class="inner-box bb-unset">
                                        <h1 class="sub-title no-drag">채팅목록</h1>
                                        <div class="room-list-wrap">
                                            <ul class="room-list" id="messenger-chat-container"></ul>
                                        </div>
                                    </div>
                                </div>
                                <div id="tab3" class="tab-cont pt80">
                                    <jsp:include page="/ipcc-messenger/tab-call-history"/>
                                </div>
                                <div id="tab4" class="tab-cont pt80">
                                    <jsp:include page="/ipcc-messenger/tab-received-memo"/>
                                </div>
                                <div id="tab5" class="tab-cont pt80">
                                    <jsp:include page="/ipcc-messenger/tab-sent-memo"/>
                                </div>
                                <div id="tab-profile" class="tab-cont pt80">
                                    <jsp:include page="/ipcc-messenger/tab-profile"/>
                                </div>
                                <div id="tab-setting" class="tab-cont pt80">
                                    <jsp:include page="/ipcc-messenger/tab-navigation"/>
                                </div>
                            </div>
                            <div class="remote-panel shadow-box">
                                <ul>
                                    <c:forEach var="e" items="${statusCodes}">
                                        <li>
                                            <button type="button" class="ui icon button brand state -member-status ${e.statusName == 'PDS' ? '-pds-status' : ''}" data-status="${e.statusNumber}">
                                                <span class="material-icons">${e.icon == null || e.icon.trim() == '' ? 'person' : g.htmlQuote(e.icon)}</span>
                                                <span class="state-name">${g.escapeQuote(e.statusName)}</span>
                                            </button>
                                        </li>
                                        <c:if test="${e.statusName == 'PDS'}">
                                            <li class="pds-status-button-container" style="display: none">
                                                <button type="button" class="ui icon button brand state -member-status-pds" data-status="0">
                                                    <span class="state-name">대기</span>
                                                </button>
                                            </li>
                                            <li class="pds-status-button-container" style="display: none">
                                                <button type="button" class="ui icon button brand state -member-status-pds" data-status="1">
                                                    <span class="state-name">상담중</span>
                                                </button>
                                            </li>
                                            <li class="pds-status-button-container" style="display: none">
                                                <button type="button" class="ui icon button brand state -member-status-pds" data-status="2">
                                                    <span class="state-name">후처리</span>
                                                </button>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                            <div class="-call-count">
                                <span class="absensce">
                                    <button type="button" class="ui icon button absensce -history-tab-indicator" data-value="/admin/record/history/history/ibk">
                                        <span class="explain">부재중전화</span>
                                    </button>:
                                    <span class="absensce-count">0</span>건
                                </span>
                                &ensp;
                                <span class="callback">
                                    <button type="button" class="ui icon button callback -history-tab-indicator" data-value="/admin/record/callback/history/ibk">
                                        <span class="explain">콜백</span>
                                    </button>:
                                    <span class="callback-count">0</span>건
                                </span>
                            </div>
                        </div>
                    </main>
                </c:when>
                <c:otherwise>
                    <main class="container no-messenger">
                        <aside class="left-panel no-drag">
                            <section style="margin: 10px 0; height: 81px; border-bottom: solid 1px #dbdbdb; !important;">
                                <ul class="nav">
                                    <li>
                                        <button class="ui icon button brand" id="user-state-change" title="내상태">
                                            <c:forEach var="e" items="${statusCodes}">
                                                <c:if test="${e.statusNumber == 0}">
                                                    <span class="material-icons">${e.icon == null || e.icon.trim() == '' ? 'person' : g.htmlQuote(e.icon)}</span>
                                                    <span class="state-name">${g.escapeQuote(e.statusName)}</span>
                                                </c:if>
                                            </c:forEach>
                                        </button>
                                        <div class="call-state" style="margin: 2px 0 0 -5px; width: 50px; align-content: center;">
                                            <div class="detail overflow-hidden" id="status-keeping-time" style="text-align: center">
                                                    <%--<span class="pull-right" id="status-keeping-time"></span>--%>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </section>
                        </aside>
                        <div class="right-panel">
                            <div class="header">
                                <div class="call-state" style="height: 25px; margin-top: 7px;">
                                    <div class="num" style="text-align: center;">
                                        <span style="font-size: 12px; width: 100%;">${g.user.idName.length() > 3 ? g.user.idName.substring(0, 3).concat('..') : g.user.idName}[내선:${g.user.extension}]</span>
                                            <%--<input id="" type="text" value="" style="width: 100%; text-align: center;">--%>
                                    </div>
                                </div>
                                <div class="inner-box" id="call-remote-container">
                                    <div class="call-state">
                                        <div class="num">
                                            <input id="calling-number" class="-calling-number" type="text" value="" placeholder="전화번호 입력">
                                        </div>
                                        <div class="name">
                                            <input id="calling-custom" class="-calling-custom" type="text" value="" placeholder="고객명">
                                                <%--<span class="pull-right" id="status-keeping-time"></span>--%>
                                        </div>
                                    </div>
                                    <div class="state-control">
                                        <ul>
                                            <li>
                                                <button type="button" class="ui icon button brand state" onclick="tryDial('MAINDB')">
                                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-out.jsp">
                                                        <jsp:param name="width" value="20"/>
                                                        <jsp:param name="height" value="20"/>
                                                    </jsp:include>
                                                    <div class="explain">전화걸기</div>
                                                </button>
                                            </li>
                                            <li>
                                                <button type="button" class="ui icon button brand state -call-receive">
                                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call.jsp">
                                                        <jsp:param name="width" value="20"/>
                                                        <jsp:param name="height" value="20"/>
                                                    </jsp:include>
                                                    <div class="explain">전화받기</div>
                                                </button>
                                            </li>
                                            <li>
                                                <button type="button" class="ui icon button brand state -call-hangup">
                                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-stop.jsp">
                                                        <jsp:param name="width" value="20"/>
                                                        <jsp:param name="height" value="20"/>
                                                    </jsp:include>
                                                    <div class="explain">전화끊기</div>
                                                </button>
                                            </li>
                                            <li>
                                                <button type="button" class="ui icon button brand state -call-pickup">
                                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-in.jsp">
                                                        <jsp:param name="width" value="20"/>
                                                        <jsp:param name="height" value="20"/>
                                                    </jsp:include>
                                                    <div class="explain">당겨받기</div>
                                                </button>
                                            </li>
                                            <li>
                                                <button type="button" class="ui icon button brand state -call-hold">
                                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/call-pause.jsp">
                                                        <jsp:param name="width" value="20"/>
                                                        <jsp:param name="height" value="20"/>
                                                    </jsp:include>
                                                    <div class="explain">통화보류</div>
                                                </button>
                                            </li>
                                            <li>
                                                <button class="ui icon button brand state" onclick="logout()">
                                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/logout.jsp">
                                                        <jsp:param name="width" value="20"/>
                                                        <jsp:param name="height" value="20"/>
                                                    </jsp:include>
                                                    <div class="explain">로그아웃</div>
                                                </button>
                                            </li>
                                        </ul>
                                        <ul>
                                            <c:forEach var="e" items="${ProtectArs}">
                                                <li>
                                                    <button type="button" class="ui icon button ment">
                                                        <div class="explain" data-value="${g.htmlQuote(e.key)}"
                                                             title="${e.key.startsWith('S') ? (e.key.endsWith('1') ? '성희롱 경고 멘트 후 전화 재연결' : '성희롱 경고 멘트 후 전화 종료') :
                                                             (e.key.endsWith('1') ? '폭언 경고 멘트 후 전화 재연결' : '폭언 경고 멘트 후 전화 종료')}">${g.htmlQuote(e.value)}</div>
                                                    </button>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <div class="remote-panel shadow-box">
                                <ul>
                                    <c:forEach var="e" items="${statusCodes}">
                                        <li>
                                            <button type="button" class="ui icon button brand state -member-status ${e.statusName == 'PDS' ? '-pds-status' : ''}" data-status="${e.statusNumber}">
                                                <span class="material-icons">${e.icon == null || e.icon.trim() == '' ? 'person' : g.htmlQuote(e.icon)}</span>
                                                <span class="state-name">${g.escapeQuote(e.statusName)}</span>
                                            </button>
                                        </li>
                                        <c:if test="${e.statusName == 'PDS'}">
                                            <li class="pds-status-button-container" style="display: none">
                                                <button type="button" class="ui icon button brand state -member-status-pds" data-status="0">
                                                    <span class="state-name">대기</span>
                                                </button>
                                            </li>
                                            <li class="pds-status-button-container" style="display: none">
                                                <button type="button" class="ui icon button brand state -member-status-pds" data-status="1">
                                                    <span class="state-name">상담중</span>
                                                </button>
                                            </li>
                                            <li class="pds-status-button-container" style="display: none">
                                                <button type="button" class="ui icon button brand state -member-status-pds" data-status="2">
                                                    <span class="state-name">후처리</span>
                                                </button>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </main>
                </c:otherwise>
            </c:choose>

            <jsp:include page="/counsel/modal-calling"/>
            <tags:scripts>
                <script id="status-script">
                    const statusCodes = {
                        <c:forEach var="e" items="${statusCodes}">
                        '${e.statusNumber}': {
                            name: '${g.escapeQuote(e.statusName)}',
                            icon: '${g.escapeQuote(e.icon)}',
                        },
                        </c:forEach>
                    };

                    const userStatuses = {
                        <c:forEach var="e" items="${peerStatuses}">
                        <c:if test="${peerToUserId.get(e.peer) != null && peerToUserId.get(e.peer) != ''}">
                        '${peerToUserId.get(e.peer).toLowerCase()}': {
                            peer: '${g.escapeQuote(e.peer)}',
                            status: ${e.status},
                            login: ${e.login},
                            userId: '${peerToUserId.getOrDefault(e.peer, '').toLowerCase()}',
                        },
                        </c:if>
                        </c:forEach>
                    };

                    const peerToUserIds = {
                        <c:forEach var="e" items="${peerStatuses}">
                        '${g.escapeQuote(e.peer)}': '${peerToUserId.getOrDefault(e.peer, '').toLowerCase()}',
                        </c:forEach>
                    };

                    const queues = {
                        <c:forEach var="e" items="${queues}">
                        '${g.htmlQuote(e.name)}': {
                            name: '${g.htmlQuote(e.name)}',
                            hanName: '${g.htmlQuote(e.hanName)}',
                            number: '${g.htmlQuote(e.number)}',
                            waitingCustomerCount: ${e.waitingCustomerCount},
                            peers: [<c:forEach var="peer" items="${e.peers}">'${g.escapeQuote(peer)}', </c:forEach>],
                        },
                        </c:forEach>
                    };
                </script>
                <script src="<c:url value="/resources/ipcc-messenger/js/callcontrol.js?version=${version}"/>"></script>
                <c:if test="${user.isChatt.equals('Y')}">
                <script>
                    if (window.isElectron)
                        $('head').append('<script src="<c:url value="/resources/ipcc-messenger/js/electron/Messenger.electron.js?version=${version}"/>"><\/script>')
                    else
                        $('head').append('<script src="<c:url value="/resources/ipcc-messenger/js/Messenger.js?version=${version}"/>"><\/script>')
                </script>
                </c:if>
                <script>
                    function updateUserStatus(userId, status) {
                        const dom = $('.-status-icon').filter(function () {
                            return ($(this).attr('data-id') || '').toLowerCase() === userId.toLowerCase();
                        }).text(userStatuses[userId] && statusCodes[userStatuses[userId].status] ? statusCodes[userStatuses[userId].status].icon || 'person' : 'person');

                        if (status) {
                            if (userStatuses[userId].login)
                                dom.prev().addClass('active');
                            else
                                dom.prev().removeClass('active');
                        }

                        $('#user-state-change').css({'padding': (dom.text().trim() === ('event_note') ? '2px' : '2px 10px 2px 10px')});
                    }

                    function tryDial(type) {
                        const cid = ''; // 비워두면 기본 cid로 시도함
                        const number = $('#calling-number').val();

                        if (!number) {
                            alert("존재하지 않는 번호입니다.");
                            return;
                        }

                        restSelf.get('/api/maindb-data/custom-info/' + number).done(function (response) {
                            const customName = response.data;
                            if (customName !== phoneNumber) {
                                const idName = customName.split('[')[0];
                                $('.-calling-custom').val(idName.length > 5 ? idName.substring(0,4).concat('..').concat(customName.replace(idName, '')) : customName);
                            }
                        });

                        if (ipccCommunicator.status.cMemberStatus === 1) {
                            alert("상담중 상태에서는 전화 걸기가 불가능합니다.");
                            return;
                        }

                        ipccCommunicator.clickByCampaign(cid, number, type, '', '');
                    }

                    createIpccAdminCommunicator()
                        /*.on("ADMLOGIN", function (message, kind, nothing, peer, extension, userIdParam) {
                            const userId = peerToUserIds[peer] || userIdParam;
                            if (!userId)
                                return;

                            if (!userStatuses[userId]) userStatuses[userId] = {peer: peer, status: 0, login: false, userId: userId};
                            userStatuses[userId].peer = peer;
                            if (kind === 'LOGIN') userStatuses[userId].login = true;
                            if (kind === 'LOGOUT') userStatuses[userId].login = false;

                            updateUserStatus(userId);
                        })*/
                        .on("MEMBERSTATUS", function (message, kind, nothing, peer, status) {
                            const userId = peerToUserIds[peer];

                            if (!userId)
                                return;

                            userStatuses[userId].status = parseInt(status);

                            updateUserStatus(userId, false);
                        });

                        const messenger = new Messenger('${g.escapeQuote(user.id)}', '${g.escapeQuote(user.isEmail)}', '${g.escapeQuote(accessToken)}');

                    $(window).on('load', function () {
                        <c:choose>
                        <c:when test="${usingServices.contains('CHATWIN') && user.isChatt == 'Y'}">
                        messenger.init();
                        </c:when>
                        <c:otherwise>
                        messenger.loginSuccess()
                        </c:otherwise>
                        </c:choose>
                    });

                    $('.-tab-indicator').click(function () {
                        const activeTab = $(this).data('tab');
                        $('.nav li button').removeClass('active');
                        $(this).addClass('active');
                        $('.tab-container .tab-cont').removeClass('active');
                        $("#" + activeTab).addClass('active');
                        if (activeTab === "tab-profile" || activeTab === "tab3" || activeTab === "tab4" || activeTab === "tab5") {
                            $('#search-container').hide();
                        } else {
                            $('#search-container').show();
                        }
                    });

                    $('.-history-tab-indicator').click(function () {
                        const href = $(this).attr('data-value');

                        if (window.isElectron)
                            window.open(href, '_ipcc');
                        else {
                            window.open(href);
                        }
                    });

                    $('#setting-tab-indicator').click(function (event) {
                        event.stopPropagation();

                        <c:choose>
                        <c:when test="${usingServices.contains('CHATWIN') && user.isChatt == 'Y'}">
                        $('.nav').find('[data-tab]').each(function () {
                            $(this).removeClass('active');
                            $('#' + $(this).attr('data-tab')).removeClass('active');
                        });

                        $('#search-container').hide();
                        $('#tab-setting').addClass('active');
                        </c:when>
                        <c:otherwise>
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('active');
                            $('#tab-setting').removeClass('active');
                        } else {
                            $(this).addClass('active');
                            $('#tab-setting').addClass('active');
                        }
                        </c:otherwise>
                        </c:choose>

                        return false;
                    });

                    $('#user-state-change').click(function (event) {
                        event.stopPropagation();

                        $('.remote-panel').show();
                        return false;
                    });

                    $('.sidebar-menu > li').click(function (event) {
                        $(this).children('.treeview-menu').toggleClass('on');
                        $(this).find('.arrow').toggleClass('rotate');
                    });

                    $(document).click(function () {
                        $('.remote-panel').hide();
                    });

                    $('#user-view-btn').each(function () {
                        $(this).click(function () {
                            $(this).find('.icon').toggleClass('rotate');
                        });
                    });

                    $('.item.user').mouseenter(function () {
                        $(this).append("<div class='tool-shadow-box'>통화중</div>");
                    }).mouseleave(function () {
                        $(this).children('.tool-shadow-box').remove();
                    }).each(function () {
                        $(this).click(function () {
                            $(this).toggleClass('toggle');
                        });
                    });

                    $('.ment .explain').on('click', function () {
                        ipccCommunicator.protectArs($(this).attr('data-value').split('_')[0], $(this).attr('data-value').split('_')[1])
                    });

                    $('.absensce .-history-tab-indicator').on('click', function () {
                        $('.absensce-count').text(0);
                    });

                    $('.callback .-history-tab-indicator').on('click', function () {
                        $('.callback-count').text(0);
                    });

                    function popupMemoModal(seq, withReply) {
                        const memoIcon = $('.-read-received-memo').filter(function () {
                            return parseInt($(this).attr('data-id')) === seq;
                        });

                        if (memoIcon.length && !memoIcon.hasClass('open')) {
                            const indicator = $('#unread-memo-indicator');
                            const currentCount = parseInt(indicator.text());
                            indicator.text((currentCount || 1) - 1);

                            if (currentCount - 1 > 0)
                                indicator.show();
                            else
                                indicator.hide();

                            memoIcon.addClass('open');

                            messenger.communicator.readMemo(seq, memoIcon.attr('data-sender'));
                        }

                        messenger.popupMemoModal(seq, withReply);
                    }

                    $('.star.icon').click(function () {
                        messenger.ui.bookmarkPanel.find('.-messenger-bookmark').slideToggle(0);
                    });

                    function logout() {
                        restSelf.get("/api/auth/logout").done(function () {
                            location.href = contextPath + '/ipcc-messenger';
                        });
                    }
                </script>
            </tags:scripts>
        </c:otherwise>
    </c:choose>
</mobileTags:layout>
