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
                    <a href="<c:url value="/"/>"><img src="<c:url value="/resources/images/logo.png"/>"></a>
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
                            <li><a href="#" onclick="master()">마스터페이지</a></li>
                        </c:if>
                        <li><a href="javascript:popupMyPasswordModal()">비밀번호변경</a></li>
                        <li><a href="#">전화 알림창 ON</a></li>
                        <li><a href="javascript:logout()">로그아웃</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="end">
            <c:if test="${hasExtension && isStat}">
                <div class="ui buttons small top-ivr-groups">
                    <button class="ui button call-time">00:00</button>
                    <button class="ui button phone-call" data-inverted="" data-tooltip="전화받기" data-position="bottom center"></button>
                    <button class="ui button phone-outgoing" data-inverted="" data-tooltip="당겨받기" data-position="bottom center"></button>
                    <button class="ui button phone-off" data-inverted="" data-tooltip="통화보류" data-position="bottom center"></button>
                    <button class="ui button phone-cancel" data-inverted="" data-tooltip="전화끊기" data-position="bottom center"></button>
                    <button class="ui button phone-forwarded" data-inverted="" data-tooltip="호전환" data-position="bottom center"></button>
                </div>
            </c:if>
            <div class="etc-groups">
                <button class="ui button dial"  data-inverted="" data-tooltip="다이얼" data-position="bottom center"></button>
                <c:if test="${hasExtension && isStat}">
                    <button class="ui button bell"  data-inverted="" data-tooltip="전화알림창" data-position="bottom right"></button>
                    <%--전화알림 off일 경우--%>
                    <%--<button class="ui button bell-off"  data-inverted="" data-tooltip="전화알림창" data-position="bottom right"></button>--%>
                </c:if>
            </div>
        </div>
    </div>

    <div class="header-sub">
        <div class="start">
            <button class="side-toggle-btn"><span class="material-icons">menu</span></button>
            <ul class="gnb-ul2">
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
        </div>
        <div class="center">
            <a class="ui button basic small -menu-page" href="<c:url value="/admin/dashboard/"/>">대쉬보드</a>
            <a class="ui button basic small -menu-page" href="<c:url value="/admin/monitor/screen/config"/>">전광판</a>
            <c:if test="${hasExtension && isStat}">
                <button class="ui button basic small" onclick="modeChange()" id="mode">관리모드</button>
            </c:if>
        </div>
        <c:if test="${hasExtension && isStat}">
            <div class="end" id="user-state"></div>
        </c:if>
    </div>
</header>

<jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
<jsp:include page="/modal-update-password"/>

<tags:scripts>
    <script>
        function logout() {
            restSelf.get("/api/auth/logout").done(function () {
                location.href = contextPath + '/';
            });
        }

        function modeChange() {
            $('#main').toggleClass('change-mode'); // TODO: check
            if ($('#main').is('.change-mode')) {
                $('#mode').text('상담모드');
            } else {
                $('#mode').text('관리모드');
            }
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
