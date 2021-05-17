<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div id="talk-list-container-${search.mode}">
    <div class="sort-wrap">
        <div class="ui form">
            <div class="fields">
                <div class="four wide field">
                    <select class="-search-type">
                        <option value="customName" selected>고객명</option>
                        <option value="userName">상담원명</option>
                    </select>
                </div>
                <div class="nine wide field">
                    <div class="ui action input">
                        <input type="text" class="-search-value">
                        <button type="button" class="ui icon button -search-button">
                            <i class="search icon"></i>
                        </button>
                    </div>
                </div>
                <div class="three wide field">
                    <select class="-orderby">
                        <option value="lastMessageTime" selected>최근시간</option>
                        <option value="customName">고객명</option>
                        <option value="userName">상담원명</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div class="talk-list-wrap">
        <c:choose>
            <c:when test="${talkList.size() > 0}">
                <ul>
                    <c:forEach var="e" items="${talkList}">
                        <li class="talk-list" data-id="${g.htmlQuote(e.roomId)}" data-sender-key="${g.htmlQuote(e.senderKey)}" data-user-key="${g.htmlQuote(e.userKey)}">
                            <div class="ui segment">
                                <div class="ui top left attached label small blue">서비스 : ${g.htmlQuote(e.svcName)}</div>
                                <div class="ui top right attached label small">상담원 :
                                    <text class="-user-name">${g.htmlQuote(e.userName)}</text>
                                </div>
                                <div class="ui bottom right attached label small time -time"><fmt:formatDate value="${e.roomLastTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                <div class="ui divided items">
                                    <div class="item">
                                        <div class="thumb">
                                            <i class="user circle icon"></i>
                                        </div>
                                        <div class="middle aligned content">
                                            <div class="header">
                                                <text class="-custom-name">${e.maindbCustomName != null ? g.htmlQuote(e.maindbCustomName) : '미등록고객'}</text>
                                                <span class="ui mini label circular -indicator-new-message" style="display: none"> N </span>
                                            </div>
                                            <div class="meta">
                                                <c:choose>
                                                    <c:when test="${e.type == 'photo' && e.send_receive == 'S'}">
                                                        <i>사진 전송됨</i>
                                                    </c:when>
                                                    <c:when test="${e.type == 'photo' && e.send_receive == 'R'}">
                                                        <i>사진 전달 받음</i>
                                                    </c:when>
                                                    <c:when test="${e.type == 'audio' && e.send_receive == 'S'}">
                                                        <i>음원 전송됨</i>
                                                    </c:when>
                                                    <c:when test="${e.type == 'audio' && e.send_receive == 'R'}">
                                                        <i>음원 전달 받음</i>
                                                    </c:when>
                                                    <c:when test="${e.type == 'file' && e.send_receive == 'S'}">
                                                        <i>파일 전송됨</i>
                                                    </c:when>
                                                    <c:when test="${e.type == 'file' && e.send_receive == 'R'}">
                                                        <i>파일 전달 받음</i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${g.htmlQuote(e.content)}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <div class="null-data">
                    조회된 데이터가 없습니다.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    $('.item[data-tab="talk-list-type-${search.mode}"] span').text(${talkList.size()});
    ui.find(".talk-list-wrap").overlayScrollbars({});
    ui.find('.talk-list').click(function () {
        ui.find('.talk-list').each(function () {
            $(this).find('.ui.segment').removeClass('active');
        });
        $(this).find('.ui.segment').removeClass('active');
        $(this).find('.-indicator-new-message').hide();

        loadTalkRoom($(this).attr('data-id'), $(this).attr('data-sender-key'), $(this).attr('data-user-key'));
    });

    let searchType = 'customName';
    let searchValue = '';
    ui.find('.-search-button').click(function () {
        searchType = ui.find('.-search-type').val();
        searchValue = ui.find('.-search-value').val();

        ui.find('.talk-list').hide().filter(function () {
            return $(this).find(searchType === 'userName' ? '.-user-name' : '.-custom-name').text().indexOf(searchValue) >= 0;
        }).show();
    });

    if (!window.talkList)
        window.talkList = {};

    function checkNewMessage(talk, roomId, newLastMessageSeq) {
        const originLastMessageSeq = talk.lastMessageSeq;
        if (!newLastMessageSeq) return;
        talk.lastMessageSeq = newLastMessageSeq;

        <c:if test="${showNoti}">
        if (!originLastMessageSeq || newLastMessageSeq > originLastMessageSeq) {
            const item = $('.talk-list[data-id="' + roomId + '"]');
            item.find('.-indicator-new-message').show();
            $('.item[data-tab="talk-list-type-${search.mode}"]').addClass('newImg_c');
            $('.item[data-tab="talk-panel"]').addClass('newImg');
        }
        </c:if>
    }

    <c:forEach var="e" items="${talkList}">
    if (!talkList['${g.escapeQuote(e.roomId)}']) talkList['${g.escapeQuote(e.roomId)}'] = {};
    talkList['${g.escapeQuote(e.roomId)}'].user = '${g.escapeQuote(e.userId)}'
    checkNewMessage(talkList['${g.escapeQuote(e.roomId)}'], '${g.escapeQuote(e.roomId)}', ${e.lastMessageSeq});
    </c:forEach>

    ui.find('.-orderby').change(function () {
        const orderby = $(this).val();
        const sorted = ui.find('.talk-list').sort(function (a, b) {
            const result = $(b).find(orderby === 'lastMessageTime' ? '.-time' : orderby === 'customName' ? '.-custom-name' : '.-user-name').text()
                > $(a).find(orderby === 'lastMessageTime' ? '.-time' : orderby === 'customName' ? '.-custom-name' : '.-user-name').text();

            return (result ? 1 : -1);
        });
        ui.find('.talk-list-wrap  ul').append(sorted.detach());
    }).change();
</script>