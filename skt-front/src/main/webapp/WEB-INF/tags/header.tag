<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>

<header id="header">
    <div class="header-main">
        <div class="start">
            <div class="logo">
                <h1>
                    <a href="<c:url value="/"/>"><img src="<c:url value="/resources/images/logo.png"/>" alt="logo"></a>
                </h1>
            </div>
            <div class="side-box">
                <div class="team">
                    <img src="<c:url value="/resources/images/layer-group.svg"/>">
                    <span>${g.htmlQuote(user.companyName.length() > 6 ? user.companyName.substring(0,6).concat("...") : user.companyName)}</span>
                </div>
                <div class="prof-info">
                    <img src="<c:url value="/resources/images/user.svg"/>">
                    <span class="name">${g.htmlQuote(user.idName.length() > 3 ? user.idName.substring(0,3).concat("..") : user.idName)}
                    <c:if test="${user.extension != null && user.extension != ''}">[${g.htmlQuote(user.extension)}]</c:if></span>
                    <i class="material-icons arrow"> keyboard_arrow_down </i>
                    <ul class="my-info-dropdown nav-ul">
                        <c:if test="${user.idType.equals('J')}">
                            <li><a target="_blank" href="/ipcc/mc_master/login.jsp">마스터페이지</a></li>
                        </c:if>
                        <li><a href="javascript:popupMyPasswordModal()">비밀번호변경</a></li>
                        <%--<li><a href="#">전화 알림창 ON</a></li>--%>
                        <li><a href="javascript:logout()">로그아웃</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <c:if test="${hasExtension && isStat}">
            <div class="end">
                <div class="ui buttons small top-ivr-groups">
                    <button class="ui button call-time" id="status-keeping-time">00:00</button>
                    <button class="ui button phone-call -call-receive" data-inverted="" data-tooltip="전화받기" data-position="bottom center"></button>
                    <button class="ui button phone-outgoing -call-pickup" data-inverted="" data-tooltip="당겨받기" data-position="bottom center"></button>
                    <button class="ui button phone-off -call-hold" data-inverted="" data-tooltip="통화보류" data-position="bottom center"></button>
                    <button class="ui button phone-cancel -call-hangup" data-inverted="" data-tooltip="전화끊기" data-position="bottom center"></button>
                    <button class="ui button phone-forwarded" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                </div>

                <div class="call-forwarded">
                    <div class="header">
                        호전환
                        <button class="call-forwarded-close"><img src="<c:url value="/resources/images/close.svg"/>" alt="close"></button>
                    </div>
                    <div class="content" id="counsel-nav"></div>
                </div>

                <div class="etc-groups">
                    <button class="ui button dial" data-inverted="" data-tooltip="다이얼" data-position="bottom center"></button>
                    <button id="button-bell-toggle" class="ui button bell" data-inverted="" data-tooltip="전화알림창" data-position="bottom right"></button>

                    <jsp:include page="/WEB-INF/jsp/dial-pad.jsp"/>
                </div>
            </div>
        </c:if>
    </div>

    <div class="header-sub">
        <div class="start">
            <button class="side-toggle-btn -admin-panel"><span class="material-icons">menu</span></button>
            <ul class="gnb-ul2 -admin-panel">
                <c:forEach var="e" items="${menu.menus}">
                    <c:if test="${e.viewYn == 'Y'}">
                        <c:choose>
                            <c:when test="${e.actionType == 'MENU'}">
                                <li><a href="#" class="-menu" data-menu-id="${g.escapeQuote(e.menuCode)}">${g.htmlQuote(e.menuName)}</a></li>
                            </c:when>
                            <c:when test="${e.actionType == 'PAGE'}">
                                <li><a href="<c:url value="${e.menuActionExeId}"/>" class="-menu-page">${g.htmlQuote(e.menuName)}</a></li>
                            </c:when>
                        </c:choose>
                    </c:if>
                </c:forEach>
            </ul>
            <c:if test="${hasExtension && isStat}">
                <ul class="gnb-ul2 -counsel-panel">
                    <li><a href="#" data-type="COUNSEL" class="-counsel-menu">상담화면</a></li>
                    <li><a href="#" data-type="NOTICE" class="-counsel-menu">공지사항</a></li>
                    <li><a href="#" data-type="KNOWLEDGE" class="-counsel-menu">지식관리</a></li>
                    <li><a href="#" data-type="CALENDAR" class="-counsel-menu">일정관리</a></li>
                    <li><a href="javascript:popupBookmark()" class="-counsel-panel bookmark-btn">즐겨찾기</a></li>
                </ul>
            </c:if>
        </div>
        <div class="center">
            <a class="ui button basic small -menu-page" href="<c:url value="/admin/dashboard/"/>">대쉬보드</a>
            <a class="ui button basic small -menu-page" href="<c:url value="/admin/monitor/screen/config"/>">전광판</a>
            <c:if test="${hasExtension && isStat}">
                <button class="ui button basic small" onclick="changeMode()" id="mode">관리모드</button>
            </c:if>
        </div>
        <c:if test="${hasExtension && isStat}">
            <div class="end" id="user-state"></div>
        </c:if>
    </div>
</header>

<div class="ui modal" id="bookmark-popup">
    <i class="close icon"></i>
    <div class="header">
        즐겨찾기 설정
    </div>
    <div class="content">
        <table class="ui celled table unstackable">
            <tbody>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="ui radio checkbox">
                        <input type="radio" name="radio" checked="checked">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td>
                    <div class="ui form">
                        <input type="text">
                    </div>
                </td>
                <td class="one wide column">
                    <a href="#" class="ui button sharp navy" target="_blank">바로가기</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="actions">
        <button class="ui left floated button sharp light remove-ml">선택 지우기</button>
        <button class="ui right floated button sharp brand">확인</button>
        <button class="ui right floated button sharp">취소</button>
    </div>
</div>

<jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
<jsp:include page="/modal-update-password"/>

<tags:scripts>
    <script>
        <c:if test="${hasExtension && isStat}">
        $('.-counsel-menu').click(function () {
            $('.-counsel-menu').parent().removeClass('active');
            $(this).parent().addClass('active');

            const type = $(this).attr('data-type');

            $('.-counsel-content-panel').hide().filter(function () {
                return type === $(this).attr('data-type');
            }).show();
        });

        function changeMode() {
            $('#main').toggleClass('change-mode');
            if ($('#main').is('.change-mode')) { // 상담모드
                $('#mode').text('상담모드');
                $('.-admin-panel').hide();
                $('.-counsel-panel').show();

                if ($('.-counsel-menu').filter(function () {
                    return $(this).hasClass('active');
                }).length === 0) {
                    $('.-counsel-menu[data-type=COUNSEL]').click();
                }
            } else {
                $('#mode').text('관리모드');
                $('.-admin-panel').show();
                $('.-counsel-panel').hide();
            }
        }

        $('#button-bell-toggle').click(function () {
            if ($(this).hasClass('bell')) {
                $(this).removeClass('bell').addClass('bell-off');
            } else {
                $(this).addClass('bell').removeClass('bell-off');
            }
        });

        $(window).on('load', function () {
            changeMode();
        });
        </c:if>

        function popupBookmark() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-bookmark', 'modal-bookmark');
        }

        $('.accordion .team-title').click(function () {
            $(this).siblings('.team-list').toggle();
        })

        function logout() {
            restSelf.get("/api/auth/logout").done(function () {
                location.href = contextPath + '/';
            });
        }

        const sideBox = $('.prof-info');
        $(sideBox).mouseenter(function () {
            $(this).find('.nav-ul').show();
        });
        $(sideBox).mouseleave(function () {
            $(this).find('.nav-ul').hide();
        });

        const menuTree = {
            <c:forEach var="e" items="${menu.menus}">
            <c:if test="${e.viewYn == 'Y' && e.actionType == 'MENU'}">
            '${g.escapeQuote(e.menuCode)}': {
                menuName: '${g.escapeQuote(e.menuName)}',
                children: [
                    <c:forEach var="e2" items="${e.children}">
                    <c:if test="${e2.viewYn == 'Y' && e2.actionType == 'PAGE'}">
                    {
                        menuCode: '${g.escapeQuote(e2.menuCode)}',
                        menuName: '${g.escapeQuote(e2.menuName)}',
                        menuActionExeId: '${g.escapeQuote(e2.menuActionExeId)}',
                    },
                    </c:if>
                    </c:forEach>
                ]
            },
            </c:if>
            </c:forEach>
        };

        $('.-menu').click(function () {
            $('.-menu').parent().removeClass('active');
            $(this).parent().addClass('active');

            $('#selected-menu').text($(this).text());
            const list = $('#menu-list').empty();

            const selectedMenu = menuTree[$(this).attr('data-menu-id')];
            if (!selectedMenu)
                throw 'invalid selectedMenu: ' + selectedMenu;

            selectedMenu.children.map(function (e) {
                list.append(
                    $('<li/>')
                        .append($('<a/>', {href: e.menuActionExeId, class: 'link-txt -menu-page', text: e.menuName}))
                        .append($('<a/>', {target: '_blank', href: e.menuActionExeId, class: 'link-new material-icons', text: 'open_in_new'}))
                );
            });
        });

        $(window).on('load', function () {
            for (let status in statusCodes) {

                if (status === '9')
                    continue;

                if (statusCodes.hasOwnProperty(status)) {
                    const isPds = statusCodes[status] === 'PDS';

                    $('<button/>', {
                        type: 'button',
                        class: "ui button small -member-status " + (isPds ? 'status-is-pds' : ''),
                        'data-status': status,
                        text: statusCodes[status],
                        click: function () {
                            const status = parseInt($(this).attr('data-status'));
                            if (status === MEMBER_STATUS_CALLING)
                                return;

                            if (ipccCommunicator.status.cMemberStatus === MEMBER_STATUS_CALLING)
                                return;

                            ipccCommunicator.setMemberStatus(status);
                        }
                    }).appendTo('#user-state');

                    if (isPds) {
                        pdsStatus = parseInt(status);

                        const pdsStatusButtonContainer = $('<div/>', {
                            class: 'pds-status-button-container',
                        }).appendTo('#user-state');

                        const pdsStatuses = {0: '대기', 1: '상담중', 2: '후처리'}
                        keys(pdsStatuses).map(function (pds) {
                            pdsStatusButtonContainer.append($('<button/>', {
                                type: 'button',
                                class: "ui button small mini -member-status-pds",
                                'data-status': pds,
                                text: pdsStatuses[pds],
                                click: function () {
                                    if (parseInt(pds) === MEMBER_STATUS_CALLING)
                                        return;

                                    ipccCommunicator.setPdsStatus(pds);
                                }
                            }));
                        });
                    }
                }
            }

            $('.-menu:first').click();
        });
    </script>
</tags:scripts>
