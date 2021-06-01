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
                <a href="#">
                    ${g.htmlQuote(user.companyName.length() > 6 ? user.companyName.substring(0,6).concat("...") : user.companyName)}
                </a>
                <a href="#">
                    <div class="prof-info">
                        <span class="name">${g.htmlQuote(user.idName.length() > 3 ? user.idName.substring(0,3).concat("..") : user.idName)}
                        <c:if test="${user.extension != null && user.extension != ''}">[${g.htmlQuote(user.extension)}]</c:if></span>
                    </div>
                    <%--<i class="material-icons arrow"> keyboard_arrow_down </i>--%>
                </a>
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
        <div class="end">
            <div class="ui buttons small top-ivr-groups">
                <button class="ui button call-btn">00:00</button>
                <button class="ui button active">전화받기</button>
                <button class="ui button">보류</button>
                <button class="ui button">전화끊기</button>
                <button class="ui button">돌려주기</button>
            </div>
            <div class="etc-groups">
                <button class="ui small button">아이콘</button>
                <button class="ui small button">아이콘</button>
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
            <div>
                <button class="ui button small">대기</button>
                <button class="ui button small">상담중</button>
                <button class="ui button small">후처리</button>
                <button class="ui button small">식사</button>
                <button class="ui button small">PDS</button>
            </div>
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

        const headerNavContainer = $('.header-nav-container');
        $(headerNavContainer).mouseenter(function () {
            $(this).find('.nav-ul').show();
        });
        $(headerNavContainer).mouseleave(function () {
            $(this).find('.nav-ul').hide();
        });

        const sideBox = $('.side-box');
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
