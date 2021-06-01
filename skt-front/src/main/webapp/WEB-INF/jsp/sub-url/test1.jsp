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

<tags:tabContentLayout>


    <div id="wrap">
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
                            개발테스트
                        </a>
                        <a href="#">
                            <div class="prof-info">
                                <span class="name">홍길동[620]</span>
                            </div>
                            <%--<i class="material-icons arrow"> keyboard_arrow_down </i>--%>
                        </a>
                        <ul class="my-info-dropdown nav-ul">
                            <li><a href="#">비밀번호변경</a></li>
                            <li><a href="#">전화 알림창 ON</a></li>
                            <li><a href="#">로그아웃</a></li>
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
                        <li class="active"><a href="#">설정메뉴</a></li>
                        <li><a href="#">관리메뉴</a></li>
                        <li><a href="#">조회메뉴</a></li>
                        <li><a href="#">통계메뉴</a></li>
                        <li><a href="#">기타메뉴</a></li>
                    </ul>
                </div>
                <div class="center">
                    <button class="ui button basic small">대쉬보드</button>
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
        <div id="main">
            <div class="content-wrapper">
                <div class="content-inner">
                    test
                </div>
            </div>
            <aside class="side-bar">
                <div class="sidebar-menu-container">
                    <div class="page-title-wrap">
                        <h2 class="page-title">설정메뉴</h2>
                    </div>
                    <div class="sidebar-menu-wrap">
                        <ul class="sidebar-menu">
                            <li class="active"><a href="#" class="link-txt">조직관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">번호관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">대표서비스설정</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">내선관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">랜덤RID관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">사용자설정</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">당겨받기그룹설정</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">라우팅신청관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">VIP라우팅</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">Blacklist라우팅</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">직전통화 상담원연결 라우팅</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">고객DB유형</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">상담결과유형</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">그룹관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">SMS카테고리관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">SMS상용문구관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">FAX/E-mail카테고리관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">FAX/E-mail발송물관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">이메일설정관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">이메일수신그룹관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">음원에디터</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">ARS음원관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">컬러링음원관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">IVR에디터</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">[수신]스케쥴유형관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">녹취암호화관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">상담톡정보관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">상담톡수신그룹관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">상담톡자동멘트관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">스케쥴유형</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            <li><a href="#" class="link-txt">상담템플릿관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                        </ul>
                    </div>
                        <%--<div class="page-title-wrap">
                            <h2 class="page-title">관리메뉴</h2>
                        </div>
                        <div class="sidebar-menu-wrap">
                            <ul class="sidebar-menu">
                                <li class="active"><a href="#" class="link-txt">큐(그룹)관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">코드/멀티코드</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">연동코드관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">[수신]주간스케쥴러</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">[수신]일별스케쥴러</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">[발신]주간스케쥴러</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">[발신]일별스케쥴러</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">공휴일관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">녹취파일관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">녹취파일관리(분리형)</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">모니터링[부서별]</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">모니터링[IVR]</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">모니터링[큐그룹별]</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">전광판</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">주간스케쥴러</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">일별스케쥴러</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            </ul>
                        </div>--%>
                        <%--<div class="page-title-wrap">
                            <h2 class="page-title">조회메뉴</h2>
                        </div>
                        <div class="sidebar-menu-wrap">
                            <ul class="sidebar-menu">
                                <li class="active"><a href="#" class="link-txt">데이터관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">업로드이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담결과이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">SMS발송이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">FAX/E-mail발송이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">이메일상담이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">녹취/통화이력조회</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">일괄녹취다운관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담톡이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            </ul>
                        </div>--%>
                        <%--<div class="page-title-wrap">
                            <h2 class="page-title">통계메뉴</h2>
                        </div>
                        <div class="sidebar-menu-wrap">
                            <ul class="sidebar-menu">
                                <li class="active"><a href="#" class="link-txt">총통화통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">인바운드통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">인입경로별통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">아웃바운드콜실적</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담원(개인별)실적통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담원별콜실적</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">큐(그룹)별통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담결과통계[연계]</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담결과통계[개별]</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담톡일별통게</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담톡시간별통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담톡상담원별통계</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            </ul>
                        </div>--%>
                       <%-- <div class="page-title-wrap">
                            <h2 class="page-title">기타메뉴</h2>
                        </div>
                        <div class="sidebar-menu-wrap">
                            <ul class="sidebar-menu">
                                <li class="active"><a href="#" class="link-txt">로그인이력</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">웹로그관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">컨텍스트관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">내선기타정보설정</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">상담원상태변경</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">IPCC 라이센스 현황</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">공지사항</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">지식관리</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                                <li><a href="#" class="link-txt">메뉴얼</a><a href="#" target="_blank" class="link-new material-icons"> open_in_new </a></li>
                            </ul>
                        </div>--%>
                </div>
            </aside>
        </div>
    </div>

    <tags:scripts>
        <script>
            const sideBox = $('.side-box');
            $(sideBox).mouseenter(function () {
                $(this).find('.nav-ul').show();
            });
            $(sideBox).mouseleave(function () {
                $(this).find('.nav-ul').hide();
            });
        </script>
    </tags:scripts>
</tags:tabContentLayout>
