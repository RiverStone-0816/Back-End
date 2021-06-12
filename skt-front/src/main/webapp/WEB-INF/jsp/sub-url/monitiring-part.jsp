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

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>

<tags:layout>


    <div class="content-inner">
        <div class="content-wrapper">
            <div class="sub-content ui container fluid unstackable">
                <form class="panel panel-search">
                    <div class="panel-heading">
                        <div class="pull-left">
                            <div class="panel-label">모니터링[부서별]</div>
                        </div>
                        <div class="pull-right">
                            <div class="ui slider checkbox">
                                <input type="checkbox" name="newsletter" id="_newsletter" tabindex="0" class="hidden"><label for="_newsletter">검색 옵션 전체보기</label>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="search-area">
                            <table class="ui celled table compact unstackable">
                                <tr>
                                    <th>부서별</th>
                                    <td colspan="11">
                                        <%-- todo : 태그만 넣어뒀는데 기능 연동 요망--%>
                                        <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                                            <button type="button" class="ui icon button mini blue compact -select-group">
                                                <i class="search icon"></i>
                                            </button>
                                            <input id="groupCode" name="groupCode" type="hidden" value="">
                                            <div class="ui breadcrumb -group-name">
                                                <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                            </div>
                                            <button type="button" class="ui icon button mini compact -clear-group">
                                                <i class="undo icon"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </form>
                <div class="panel panel-statstics">
                    <div class="panel-body">
                        <div class="panel-section">
                            <div class="panel-heading transparent">
                                <div class="panel-sub-title">
                                    서비스문의
                                </div>
                                <ul class="state-list">
                                    <li>대기: 00명</li>
                                    <li>통화중: 00명</li>
                                    <li>후처리: 00명</li>
                                    <li>기타: 00명</li>
                                </ul>
                            </div>
                            <div class="panel-body">
                                <div class="ui grid stackable">
                                    <div class="eight wide column">
                                        <div class="table-scroll-wrap">
                                            <table class="ui celled table compact unstackable">
                                                <thead>
                                                <tr>
                                                    <th>이름</th>
                                                    <th>내선</th>
                                                    <th>상태</th>
                                                    <th>인입경로</th>
                                                    <th colspan="2">통화량</th>
                                                    <th>고객번호</th>
                                                    <th>시간</th>
                                                    <th>감청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="call-state-bg">통화중</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="wait-state-bg">대기</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="bell-state-bg">벨울림</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="after-state-bg">후처리</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="logout-state-bg">로그아웃</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="etc-state-bg">기타</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="eight wide column">
                                        <div class="table-scroll-wrap">
                                            <table class="ui celled table compact unstackable">
                                                <thead>
                                                <tr>
                                                    <th>이름</th>
                                                    <th>내선</th>
                                                    <th>상태</th>
                                                    <th>인입경로</th>
                                                    <th colspan="2">통화량</th>
                                                    <th>고객번호</th>
                                                    <th>시간</th>
                                                    <th>감청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="call-state-bg">통화중</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="wait-state-bg">대기</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="bell-state-bg">벨울림</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="after-state-bg">후처리</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="logout-state-bg">로그아웃</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="etc-state-bg">기타</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-section">
                            <div class="panel-heading transparent">
                                <div class="panel-sub-title">
                                    서비스문의
                                </div>
                                <ul class="state-list">
                                    <li>대기: 00명</li>
                                    <li>통화중: 00명</li>
                                    <li>후처리: 00명</li>
                                    <li>기타: 00명</li>
                                </ul>
                            </div>
                            <div class="panel-body">
                                <div class="ui grid stackable">
                                    <div class="eight wide column">
                                        <div class="table-scroll-wrap">
                                            <table class="ui celled table compact unstackable">
                                                <thead>
                                                <tr>
                                                    <th>이름</th>
                                                    <th>내선</th>
                                                    <th>상태</th>
                                                    <th>인입경로</th>
                                                    <th colspan="2">통화량</th>
                                                    <th>고객번호</th>
                                                    <th>시간</th>
                                                    <th>감청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="call-state-bg">통화중</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="wait-state-bg">대기</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="bell-state-bg">벨울림</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="after-state-bg">후처리</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="logout-state-bg">로그아웃</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="etc-state-bg">기타</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="eight wide column">
                                        <div class="table-scroll-wrap">
                                            <table class="ui celled table compact unstackable">
                                                <thead>
                                                <tr>
                                                    <th>이름</th>
                                                    <th>내선</th>
                                                    <th>상태</th>
                                                    <th>인입경로</th>
                                                    <th colspan="2">통화량</th>
                                                    <th>고객번호</th>
                                                    <th>시간</th>
                                                    <th>감청</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="call-state-bg">통화중</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="wait-state-bg">대기</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="bell-state-bg">벨울림</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="after-state-bg">후처리</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="logout-state-bg">로그아웃</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>홍길동</td>
                                                    <td>123</td>
                                                    <td class="etc-state-bg">기타</td>
                                                    <td>1서비스 / 1헌트</td>
                                                    <td>수신:4/5</td>
                                                    <td>발신:4/5</td>
                                                    <td>01000000000</td>
                                                    <td>01:30</td>
                                                    <td>
                                                        <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="logo"></button>
                                                    </td>
                                                </tr>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            $(document).on('click', '.-menu-page', function (event) {
                const $this = $(event.target);
                event.stopPropagation();
                event.preventDefault();

                $('.-menu-page').parent().removeClass('active');
                $this.parent().addClass('active');

                $('#main-content').attr('src', $this.attr('href'));

                if ($('#main').is('.change-mode')) {
                    changeMode();
                }

                return false;
            });
        </script>
    </tags:scripts>
</tags:layout>
