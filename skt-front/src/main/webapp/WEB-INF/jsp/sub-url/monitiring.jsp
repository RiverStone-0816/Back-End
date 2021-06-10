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

    <div class="content-wrapper">
        <div class="panel panel-search">
            <div class="panel-heading">
                <div class="pull-left">
                    <div class="panel-label">모니터링</div>
                </div>
            </div>
        </div>
        <div class="panel panel-statstics">
            <div class="panel-body">
                <div class="panel-section">
                    <table class="ui celled table compact unstackable">
                        <thead>
                        <tr>
                            <th>응대율</th>
                            <th>고객대기자수</th>
                            <th>근무상담사</th>
                            <th>총 콜백</th>
                            <th>미처리 콜백</th>
                            <th>대기</th>
                            <th>I/B통화중</th>
                            <th>O/B통화중</th>
                            <th>후처리</th>
                            <th>기타</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>응대율</td>
                            <td>고객대기자수</td>
                            <td>근무상담사</td>
                            <td>총 콜백</td>
                            <td>미처리 콜백</td>
                            <td>대기</td>
                            <td>I/B통화중</td>
                            <td>O/B통화중</td>
                            <td>후처리</td>
                            <td>기타</td>
                        </tr>
                        <tr>
                            <td>운영팀</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="panel-section">
                    <div class="panel-sub-title">서비스 모니터링</div>
                    <div class="panel-sub-container">
                        <div class="ui grid">
                            <div class="sixteen wide column remove-pb">
                                <table class="ui celled table compact unstackable">
                                    <thead>
                                    <tr>
                                        <th>서비스명</th>
                                        <th>인입호수</th>
                                        <th>단순조회</th>
                                        <th>연결요청</th>
                                        <th>응답호</th>
                                        <th>포기호</th>
                                        <th>콜백</th>
                                        <th>응답율</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                    </tr>
                                    <tr>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                    </tr>
                                    <tr class="total-tr">
                                        <td>합계</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                        <td>0</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="eight wide column compact-pr">
                                <div class="inner-box">
                                    <h2 class="chart-title">수신 통계</h2>
                                    <div class="pd10 align-center">
                                        차트영역
                                    </div>
                                    <div class="chart-label-wrap-bar">
                                        <ul>
                                            <li><span class="symbol color-2"></span><span class="text">어제</span></li>
                                            <li><span class="symbol color-1"></span><span class="text">오늘</span></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="eight wide column compact-pl">
                                <div class="inner-box">
                                    <h2 class="chart-title">응답율 통계</h2>
                                    <div class="pd10 align-center">
                                        차트영역
                                    </div>
                                    <div class="chart-label-wrap-bar">
                                        <ul>
                                            <li><span class="symbol color-2"></span><span class="text">어제</span></li>
                                            <li><span class="symbol color-1"></span><span class="text">오늘</span></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-section">
                    <div class="panel-sub-title">상담그룹 모니터링</div>
                    <div class="panel-sub-container">
                        <table class="ui celled table compact unstackable">
                            <thead>
                            <tr>
                                <th>큐그룹명</th>
                                <th>고객대기</th>
                                <th>대기</th>
                                <th>상담중</th>
                                <th>후처리</th>
                                <th>휴식</th>
                                <th>식사</th>
                                <th>로그인</th>
                                <th>로그아웃</th>
                                <th>전체</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>큐그룹명</td>
                                <td>고객대기</td>
                                <td>대기</td>
                                <td>상담중</td>
                                <td>후처리</td>
                                <td>휴식</td>
                                <td>식사</td>
                                <td>로그인</td>
                                <td>로그아웃</td>
                                <td>전체</td>
                            </tr>
                            </tbody>
                        </table>
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
