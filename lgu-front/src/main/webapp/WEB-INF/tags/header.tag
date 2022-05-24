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

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>
<header id="header">
    <div class="logo">
        <h1>
            <a href="<c:url value="/"/>"><img src="<c:url value="/resources/images/logo.png"/>"></a>
            <%--<a href="<c:url value="/"/>"><img src="http://www.eicn.co.kr/img/main_logo.png"></a>--%>
        </h1>
    </div>
    <div class="gnb-wrapper">
        <div class="pull-left" id="call-control-panel" style="display: none;">
            <div class="gnb-btn-wrap" id="user-ivr">
                <button class="ui button basic compact white -call-receive" onmousedown="$(this).addClass('active')" onmouseup="$(this).removeClass('active')"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="black"><path d="M0 0h24v24H0z" fill="none"/><path d="M20 15.5c-1.25 0-2.45-.2-3.57-.57-.35-.11-.74-.03-1.02.24l-2.2 2.2c-2.83-1.44-5.15-3.75-6.59-6.59l2.2-2.21c.28-.26.36-.65.25-1C8.7 6.45 8.5 5.25 8.5 4c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1 0 9.39 7.61 17 17 17 .55 0 1-.45 1-1v-3.5c0-.55-.45-1-1-1zM19 12h2c0-4.97-4.03-9-9-9v2c3.87 0 7 3.13 7 7zm-4 0h2c0-2.76-2.24-5-5-5v2c1.66 0 3 1.34 3 3z"/></svg>전화받기</button>
                <button class="ui button basic compact white -call-hangup" onmousedown="$(this).addClass('active')" onmouseup="$(this).removeClass('active')"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="black"><path d="M0 0h24v24H0z" fill="none"/><path d="M17.34 14.54l-1.43-1.43c.56-.73 1.05-1.5 1.47-2.32l-2.2-2.2c-.28-.28-.36-.67-.25-1.02.37-1.12.57-2.32.57-3.57 0-.55.45-1 1-1H20c.55 0 1 .45 1 1 0 3.98-1.37 7.64-3.66 10.54zm-2.82 2.81C11.63 19.64 7.97 21 4 21c-.55 0-1-.45-1-1v-3.49c0-.55.45-1 1-1 1.24 0 2.45-.2 3.57-.57.35-.12.75-.03 1.02.24l2.2 2.2c.81-.42 1.58-.9 2.3-1.46L1.39 4.22l1.42-1.41L21.19 21.2l-1.41 1.41-5.26-5.26z"/></svg>전화끊기</button>
                <button class="ui button basic compact white -call-pickup" onmousedown="$(this).addClass('active')" onmouseup="$(this).removeClass('active')"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="black"><path d="M0 0h24v24H0z" fill="none"/><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2zm13.54-7.1l-.71-.7L13 9.29V5h-1v6h6v-1h-4.15z"/></svg>당겨받기</button>
                <button class="ui button basic compact white -call-hold"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="black"><path d="M0 0h24v24H0z" fill="none"/><path d="M17 3h-2v7h2V3zm3 12.5c-1.25 0-2.45-.2-3.57-.57-.35-.11-.74-.03-1.02.24l-2.2 2.2c-2.83-1.44-5.15-3.75-6.59-6.59l2.2-2.21c.28-.26.36-.65.25-1C8.7 6.45 8.5 5.25 8.5 4c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1 0 9.39 7.61 17 17 17 .55 0 1-.45 1-1v-3.5c0-.55-.45-1-1-1zM19 3v7h2V3h-2z"/></svg>통화보류</button>
                <c:if test="${g.usingServices.contains('SPHONE') && g.user.phoneKind.equals('S')}">
                <button class="ui button basic compact white dial">다이얼패드</button>
                </c:if>
<%--                <button class="ui button basic compact white" onclick="faxEmailSend()"><i class="material-icons"> email </i>FAX/메일발송</button>--%>
                <jsp:include page="/WEB-INF/jsp/dial-pad.jsp"/>
            </div>
            <div class="gnb-btn-wrap" id="user-state"></div>
            <div class="gnb-call-time">
                <i class="material-icons"> phone_in_talk </i>
                <span id="status-keeping-time">00:00</span>
            </div>
        </div>
        <div class="pull-right">
            <div class="mode-set">
                <div class="selectbox header-nav-container">
                    <div class="select"><div id="newChangeMode"></div><text id="mode">관리모드</text> <span>ON</span><i class="material-icons arrow"> keyboard_arrow_down </i></div>
                    <div class="option nav-ul">
                        <ul>
                            <c:if test="${hasExtension && isStat}">
                                <li><a href="javascript:" onclick="changeMode();" id="mode-switcher">모드변경</a></li>
                            </c:if>
                            <li><a href="javascript:" onclick="sitemapOpen();">사이트맵</a></li>
                            <c:if test="${('A|B'.contains(g.user.idType)) && (usingServices.contains('DUTA') || usingServices.contains('DUSTT'))}">
                            <li><a href="${g.doubUrl.concat(g.urlEncode(g.baseUrl.concat(g.urlEncode(';jsessionid='.concat(g.sessionId)))))}" target="_blank">AI 분석</a></li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="my-info header-nav-container">
                <a href="#">
                    <div class="prof-info">
                        <span class="name">${g.htmlQuote(user.companyName.length() > 6 ? user.companyName.substring(0,6).concat("...") : user.companyName)}</span>
                        <span class="role">${g.htmlQuote(user.idName.length() > 3 ? user.idName.substring(0,3).concat("..") : user.idName)}
                            <c:if test="${user.extension != null && user.extension != ''}">[${g.htmlQuote(user.extension)}]</c:if></span>
                    </div>
                    <i class="material-icons arrow"> keyboard_arrow_down </i>
                </a>
                <ul class="my-info-dropdown nav-ul">
                    <c:if test="${user.idType.equals('J')}">
                        <li><a href="#" onclick="master()">마스터페이지</a></li>
                    </c:if>
                    <li><a href="#" onclick="popupMyPasswordModal()">비밀번호변경</a></li>
                    <li><a href="#" onclick="logout()">로그아웃</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>

<jsp:include page="/modal-update-password"/>
<jsp:include page="/modal-site-map"/>

<tags:scripts>
    <script>
        function logout() {
            restSelf.get("/api/auth/logout").done(function () {
                location.href = contextPath + '/';
            });
        }

        function master() {
            location.href="/ipcc/mc_master/login.jsp";
        }

        const headerNavContainer = $('.header-nav-container');
        $(headerNavContainer).mouseenter(function () {
            $(this).find('.nav-ul').show();
        });
        $(headerNavContainer).mouseleave(function () {
            $(this).find('.nav-ul').hide();
        });

        function changeMode() {
            $('#main').toggleClass('change-mode');
            if ($('#main').is('.change-mode')) {
                $('#mode').text('상담모드');
                $('.tab-menu').contents().find('.excel-down-button').hide();
                $('#newChangeMode').removeClass('admin-new-img');
            } else {
                $('#mode').text('관리모드');
                $('.tab-menu').contents().find('.excel-down-button').show();
            }
        }
        $('.gnb-wrapper .dial').click(function () {
            $('.dial-pad').toggle();
        });
        $('.dial-close').click(function () {
            $('.dial-pad').hide();
        });
        (function () {
            if (${hasExtension} && ${isStat} && ${user.idType eq 'M'})
                changeMode();
        })();
    </script>
</tags:scripts>
