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
                    <%--<a href="<c:url value="/"/>"><img src="http://www.eicn.co.kr/img/main_logo.png"></a>--%>
                </h1>
            </div>
            <div class="side-box">
                <div class="team">
                    <svg width="15" height="14" viewBox="0 0 15 14">
                        <path d="M.363,4.047,7.188,6.936a.8.8,0,0,0,.624,0l6.824-2.889a.594.594,0,0,0,0-1.095L7.812.063a.8.8,0,0,0-.624,0L.363,2.952a.594.594,0,0,0,0,1.095ZM14.636,6.461l-1.7-.72-4.736,2a1.8,1.8,0,0,1-1.4,0l-4.735-2-1.7.72a.593.593,0,0,0,0,1.094l6.824,2.887a.8.8,0,0,0,.624,0l6.825-2.887a.593.593,0,0,0,0-1.094Zm0,3.494-1.7-.717L8.2,11.244a1.8,1.8,0,0,1-1.4,0L2.059,9.238l-1.7.717a.593.593,0,0,0,0,1.094l6.824,2.887a.8.8,0,0,0,.624,0l6.825-2.887a.593.593,0,0,0,0-1.094Z" transform="translate(0 0.001)" fill="#39374d"/>
                    </svg>
                    <span>${g.htmlQuote(user.companyName.length() > 6 ? user.companyName.substring(0,6).concat("...") : user.companyName)}</span>
                </div>
                <div class="prof-info">
                    <svg width="16" height="14.5" viewBox="0 0 16 14.5">
                        <g id="Icon_feather-user" data-name="Icon feather-user" transform="translate(0.66 0.5)">
                            <path d="M21,26.987v-1.5c0-1.652-1.679-2.991-3.75-2.991H9.75C7.679,22.5,6,23.839,6,25.491v1.5" transform="translate(-6.16 -12.987)" fill="#39374d" stroke="#39374d" stroke-width="1"/>
                            <path d="M17.983,7.491A2.991,2.991,0,1,1,14.991,4.5,2.991,2.991,0,0,1,17.983,7.491Z" transform="translate(-7.651 -4.5)" fill="#39374d" stroke="#39374d" stroke-linecap="round" stroke-linejoin="round" stroke-width="1"/>
                        </g>
                    </svg>
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
            <div class="ui buttons small top-ivr-groups">
                <button class="ui button">00:00</button>
                <button class="ui button">
                    <svg width="21.542" height="21.58" viewBox="0 0 21.542 21.58">
                        <path id="Icon_feather-phone-call" data-name="Icon feather-phone-call" d="M15.532,5.323A4.778,4.778,0,0,1,19.307,9.1M15.532,1.5a8.6,8.6,0,0,1,7.6,7.588m-.956,7.626v2.867a1.911,1.911,0,0,1-2.083,1.911,18.912,18.912,0,0,1-8.247-2.934,18.635,18.635,0,0,1-5.734-5.734A18.912,18.912,0,0,1,3.176,4.539a1.911,1.911,0,0,1,1.9-2.083H7.944A1.911,1.911,0,0,1,9.856,4.1a12.27,12.27,0,0,0,.669,2.685,1.911,1.911,0,0,1-.43,2.016L8.881,10.015a15.29,15.29,0,0,0,5.734,5.734l1.214-1.214a1.911,1.911,0,0,1,2.016-.43,12.27,12.27,0,0,0,2.685.669A1.911,1.911,0,0,1,22.174,16.714Z" transform="translate(-2.417 -0.672)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                    </svg>
                </button>
                <button class="ui button">
                    <svg width="21.773" height="21.812" viewBox="0 0 21.773 21.812">
                        <g id="Icon_feather-phone-outgoing" data-name="Icon feather-phone-outgoing" transform="translate(-2.417 -0.439)">
                            <path d="M31.234,7.234V1.5H25.5" transform="translate(47.674 9.689) rotate(180)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M24,8.19,30.69,1.5" transform="translate(47.129 9.689) rotate(180)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M22.174,17.258v2.867a1.911,1.911,0,0,1-2.083,1.911A18.912,18.912,0,0,1,11.843,19.1a18.635,18.635,0,0,1-5.734-5.734A18.912,18.912,0,0,1,3.176,5.083,1.911,1.911,0,0,1,5.077,3H7.944A1.911,1.911,0,0,1,9.856,4.644a12.27,12.27,0,0,0,.669,2.685,1.911,1.911,0,0,1-.43,2.016L8.881,10.559a15.29,15.29,0,0,0,5.734,5.734l1.214-1.214a1.911,1.911,0,0,1,2.016-.43,12.271,12.271,0,0,0,2.685.669,1.911,1.911,0,0,1,1.644,1.94Z" transform="translate(0 -0.544)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                        </g>
                    </svg>
                </button>
                <button class="ui button">
                    <svg width="22.121" height="22.121" viewBox="0 0 22.121 22.121">
                        <g id="Icon_feather-phone-off" data-name="Icon feather-phone-off" transform="translate(-0.439 -0.439)">
                            <path d="M10.957,13.282a14.545,14.545,0,0,0,3.1,2.364l1.155-1.155a1.818,1.818,0,0,1,1.918-.409,11.673,11.673,0,0,0,2.555.636,1.818,1.818,0,0,1,1.564,1.818v2.727a1.818,1.818,0,0,1-1.982,1.818,17.991,17.991,0,0,1-7.845-2.791,17.654,17.654,0,0,1-3.027-2.427M5.966,12.827A17.991,17.991,0,0,1,3.175,4.982,1.818,1.818,0,0,1,4.984,3H7.712A1.818,1.818,0,0,1,9.53,4.564a11.673,11.673,0,0,0,.636,2.555,1.818,1.818,0,0,1-.409,1.918L8.6,10.191" transform="translate(-0.657 -0.591)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M21.5,1.5l-20,20" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                        </g>
                    </svg>
                </button>
                <button class="ui button">
                    <svg width="21.773" height="21.812" viewBox="0 0 21.773 21.812">
                        <g id="Icon_feather-phone-missed" data-name="Icon feather-phone-missed" transform="translate(-2.417 -0.439)">
                            <path d="M31.234,1.5,25.5,7.234" transform="translate(-8.104)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M25.5,1.5l5.734,5.734" transform="translate(-8.104)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M22.174,17.258v2.867a1.911,1.911,0,0,1-2.083,1.911A18.912,18.912,0,0,1,11.843,19.1a18.635,18.635,0,0,1-5.734-5.734A18.912,18.912,0,0,1,3.176,5.083,1.911,1.911,0,0,1,5.077,3H7.944A1.911,1.911,0,0,1,9.856,4.644a12.27,12.27,0,0,0,.669,2.685,1.911,1.911,0,0,1-.43,2.016L8.881,10.559a15.29,15.29,0,0,0,5.734,5.734l1.214-1.214a1.911,1.911,0,0,1,2.016-.43,12.271,12.271,0,0,0,2.685.669,1.911,1.911,0,0,1,1.644,1.94Z" transform="translate(0 -0.544)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                        </g>
                    </svg>
                </button>
                <button class="ui button">
                    <svg width="21.462" height="21.812" viewBox="0 0 21.462 21.812">
                        <g id="Icon_feather-phone-forwarded" data-name="Icon feather-phone-forwarded" transform="translate(-2.417 -0.439)">
                            <path d="M28.5,1.5l3.823,3.823L28.5,9.145" transform="translate(-9.193)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M22.5,7.5h7.645" transform="translate(-7.016 -2.177)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M22.174,17.258v2.867a1.911,1.911,0,0,1-2.083,1.911A18.912,18.912,0,0,1,11.843,19.1a18.635,18.635,0,0,1-5.734-5.734A18.912,18.912,0,0,1,3.176,5.083,1.911,1.911,0,0,1,5.077,3H7.944A1.911,1.911,0,0,1,9.856,4.644a12.27,12.27,0,0,0,.669,2.685,1.911,1.911,0,0,1-.43,2.016L8.881,10.559a15.29,15.29,0,0,0,5.734,5.734l1.214-1.214a1.911,1.911,0,0,1,2.016-.43,12.271,12.271,0,0,0,2.685.669,1.911,1.911,0,0,1,1.644,1.94Z" transform="translate(0 -0.544)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                        </g>
                    </svg>
                </button>
            </div>
            <div class="etc-groups">
                <button class="ui small button">
                    <svg width="22" height="22" viewBox="0 0 22 22">
                        <g transform="translate(-318 -125)">
                            <circle cx="2" cy="2" r="2" transform="translate(318 125)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(318 134)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(318 143)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(327 125)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(327 134)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(327 143)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(336 125)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(336 134)" fill="#333"/>
                            <circle cx="2" cy="2" r="2" transform="translate(336 143)" fill="#333"/>
                        </g>
                    </svg>
                </button>
                <button class="ui small button">
                    <svg width="22" height="22" viewBox="0 0 21.763 24.01">
                        <g id="Icon_feather-bell" data-name="Icon feather-bell" transform="translate(-3.75 -2.25)">
                            <path d="M21.386,9.754a6.754,6.754,0,1,0-13.508,0c0,7.88-3.377,10.131-3.377,10.131H24.763s-3.377-2.251-3.377-10.131" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                            <path d="M19.3,31.5a2.251,2.251,0,0,1-3.895,0" transform="translate(-2.721 -7.112)" fill="none" stroke="#333" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/>
                        </g>
                    </svg>
                </button>
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
            <button class="ui button basic small">전광판</button>
            <button class="ui button basic small">관리모드</button>
        </div>
        <div class="end">
            <button class="ui button small">대기</button>
            <button class="ui button small">상담중</button>
            <button class="ui button small">후처리</button>
            <button class="ui button small">식사</button>
            <button class="ui button small">PDS</button>
        </div>
    </div>
</header>


<tags:scripts>
    <script>
        function logout() {
            restSelf.get("/api/auth/logout").done(function () {
                location.href = contextPath + '/';
            });
        }

        function master() {
            location.href = "/ipcc/mc_master/login.jsp";
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
            $('.-menu:first').click();
        });
    </script>
</tags:scripts>
