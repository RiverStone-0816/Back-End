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

<tags:tabContentLayout>

    <div class="dashboard-wrapper -admin-panel">
        <div class="content-inner" id="main-dashboard">
            <div class="top">
                <div class="panel panel-dashboard">
                    <div class="panel-heading">
                        <div class="panel-label">호 통계 및 관련</div>
                    </div>
                    <div class="panel-body">
                        <div class="ui grid remove-mb">
                            <div class="five wide column remove-pr">
                                <div class="ui grid">
                                    <div class="sixteen wide column remove-pb">
                                        <div class="visual-panel">
                                            <div class="panel-heading">실시간 응답율</div>
                                            <div class="panel-body">
                                                <span class="num">58%</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="eight wide column remove-pr">
                                        <div class="visual-panel">
                                            <div class="panel-heading">수신건</div>
                                            <div class="panel-body">
                                                <span class="num">185</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="eight wide column">
                                        <div class="visual-panel">
                                            <div class="panel-heading">비수신건</div>
                                            <div class="panel-body">
                                                <span class="num">185</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="eleven wide column">
                                <div class="visual-panel">
                                    <div class="panel-heading">인바운드 현황</div>
                                    <div class="panel-body">
                                        <div class="chart-wrap">
                                            차트삽입
                                        </div>
                                        <div class="chart-label-wrap">
                                            <ul>
                                                <li><span class="symbol color-1"></span><span class="text">I/B전체</span></li>
                                                <li><span class="symbol color-2"></span><span class="text">단순조회</span></li>
                                                <li><span class="symbol color-3"></span><span class="text">연결요청</span></li>
                                                <li><span class="symbol color-4"></span><span class="text">응대호</span></li>
                                                <li><span class="symbol color-5"></span><span class="text">포기호</span></li>
                                                <li><span class="symbol color-6"></span><span class="text">콜백</span></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <table class="ui celled table compact unstackable structured">
                            <thead>
                            <tr>
                                <th rowspan="2">날짜/시간</th>
                                <th colspan="6">I/B 콜 현황</th>
                                <th colspan="6">성과지표</th>
                                <th colspan="5">응대호 대기시간 분석</th>
                                <th colspan="5">포기호 대기시간 분석</th>
                            </tr>
                            <tr>
                                <th>I/B<br>전체콜</th>
                                <th>단순조회</th>
                                <th>연결요청</th>
                                <th>응대호</th>
                                <th>포기호</th>
                                <th>콜백</th>

                                <th>I/B<br>총통화시간</th>
                                <th>평균통화시간</th>
                                <th>평균대기시간</th>
                                <th>호응답률</th>
                                <th>서비스레벨<br>호응답률</th>
                                <th>단순조회율</th>

                                <th>~10(초)</th>
                                <th>~20(초)</th>
                                <th>~30(초)</th>
                                <th>~40(초)</th>
                                <th>40~(초)</th>
                                <th>~10(초)</th>
                                <th>~20(초)</th>
                                <th>~30(초)</th>
                                <th>~40(초)</th>
                                <th>40~(초)</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th>합계</th>

                                <td><span>0</span></td>
                                <td><span>0</span></td>
                                <td><span>0</span></td>
                                <td><span>0</span></td>
                                <td><span>0</span></td>
                                <td><span>0</span></td>

                                <td>00:00:00</td>
                                <td>00:00:00</td>
                                <td>00:00:00</td>
                                <td>0.0%</td>
                                <td>0.0%</td>
                                <td>0.0%</td>

                                <td>0</td>
                                <td>0</td>
                                <td>0</td>
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
                </div>
            </div>
            <div class="bottom">
                <div class="state-area">
                    <div class="panel panel-dashboard">
                        <div class="panel-heading">
                            <div class="panel-label">상담원 상태</div>
                        </div>
                        <div class="panel-body">
                            <div class="ui grid">
                                <div class="six wide column">
                                    <div class="chart-wrap"></div>
                                    <div class="chart-label-wrap">
                                        <ul>
                                            <li>
                                                <span class="symbol color-1"></span>
                                                <span class="text">대기</span>
                                            </li>
                                            <li>
                                                <span class="symbol color-2"></span>
                                                <span class="text">휴식</span>
                                            </li>
                                            <li>
                                                <span class="symbol color-3"></span>
                                                <span class="text">통화중</span>
                                            </li>
                                            <li>
                                                <span class="symbol color-4"></span>
                                                <span class="text">식사</span>
                                            </li>
                                            <li>
                                                <span class="symbol color-5"></span>
                                                <span class="text">후처리</span>
                                            </li>
                                            <li>
                                                <span class="symbol color-6"></span>
                                                <span class="text">이석</span>
                                            </li>
                                            <li>
                                                <span class="symbol color-7"></span>
                                                <span class="text">로그아웃</span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="ten wide column">
                                    <table class="ui celled table compact">
                                        <thead>
                                        <tr>
                                            <th>콜그룹명</th>
                                            <th>고객대기</th>
                                            <th>대기</th>
                                            <th>통화중</th>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
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
                                            <td>콜그룹명</td>
                                            <td>0</td>
                                            <td>0</td>
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
                            </div>
                        </div>
                    </div>
                </div>
                <div class="rank-area">
                    <div class="panel panel-dashboard">
                        <div class="panel-heading">
                            <div class="pull-left">
                                <div class="panel-label">최다수신 TOP 10</div>
                            </div>
                            <div class="pull-right">
                                <button type="button" class="arrow-btn">◀</button>
                                <button type="button" class="arrow-btn">▶</button>
                            </div>
                        </div>
                        <div class="panel-body">
                            <table class="ui celled table compact">
                                <thead>
                                <tr>
                                    <th>상담사명</th>
                                    <th>상담사ID</th>
                                    <th>내선번호</th>
                                    <th>내선번호</th>
                                    <th>상태</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td class="call-state-bg">통화중</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td class="wait-state-bg">대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td class="bell-state-bg">벨울림</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td class="after-state-bg">후처리</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td class="logout-state-bg">로그아웃</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td class="etc-state-bg">기타</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="rank-area">
                    <div class="panel panel-dashboard">
                        <div class="panel-heading">
                            <div class="pull-left">
                                <div class="panel-label">최다수신 TOP 10</div>
                            </div>
                            <div class="pull-right">
                                <button type="button" class="arrow-btn">◀</button>
                                <button type="button" class="arrow-btn">▶</button>
                            </div>
                        </div>
                        <div class="panel-body">
                            <table class="ui celled table compact">
                                <thead>
                                <tr>
                                    <th>상담사명</th>
                                    <th>상담사ID</th>
                                    <th>내선번호</th>
                                    <th>내선번호</th>
                                    <th>상태</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                <tr>
                                    <td>상담사명</td>
                                    <td>상담사ID</td>
                                    <td>내선번호</td>
                                    <td>내선번호</td>
                                    <td>대기</td>
                                </tr>
                                </tbody>
                            </table>
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
</tags:tabContentLayout>
