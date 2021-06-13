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

    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/total/"/>
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
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
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
                    <c:forEach var="group" items="${groupToPersons}">

                        <div class="panel-section">
                            <div class="panel-heading transparent">
                                <div class="panel-sub-title">${g.escapeQuote(groups.get(group.key))}</div>
                                <ul class="state-list">
                                        <%--todo: 새로운 상태정보가 업데이트되면, 숫자 바꿔줘야 함.--%>
                                    <li>대기: ${group.value.steam().filter(e -> e.person.paused == 0).count()}명</li>
                                    <li>통화중: ${group.value.steam().filter(e -> e.person.paused == 1).count()}명</li>
                                    <li>후처리: ${group.value.steam().filter(e -> e.person.paused == 2).count()}명</li>
                                    <li>기타: ${group.value.steam().filter(e -> e.person.paused != 0 && e.person.paused != 1 && e.person.paused != 2).count()}명</li>
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
                                                <c:forEach var="i" begin="0" end="${group.value.size() / 2}">
                                                    <c:set var="e" value="${group.value.get(i)}"/>
                                                    <tr>
                                                        <td>${g.htmlQuote(e.person.idName)}</td>
                                                        <td>${g.htmlQuote(e.person.extension)}</td>
                                                        <td class="-consultant-status -consultant-status-with-color bcolor-bar${e.person.paused}" data-peer="${g.htmlQuote(e.person.peer)}">
                                                                ${memberStatuses.get(e.person.paused)}
                                                        </td>
                                                        <td class="-consultant-queue-name" data-peer="${g.htmlQuote(e.person.peer)}"></td>
                                                        <td><%--todo: 수신 응답/전체 --%></td>
                                                        <td><%--todo: 발신 전체 --%></td>
                                                        <td class="-consultant-calling-custom-number" data-peer="${g.htmlQuote(e.person.peer)}" >${g.htmlQuote(e.customNumber)}</td>
                                                        <td class="-consultant-status-time" data-peer="${g.htmlQuote(e.person.peer)}">00:00</td>
                                                        <td>
                                                            <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="play"></button>
                                                                <%--todo: 감청--%>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
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
                                                <c:forEach var="i" begin="0" end="${group.value.size() / 2}">
                                                    <c:set var="e" value="${group.value.get(i)}"/>
                                                    <tr>
                                                        <td>${g.htmlQuote(e.person.idName)}</td>
                                                        <td>${g.htmlQuote(e.person.extension)}</td>
                                                        <td class="-consultant-status -consultant-status-with-color bcolor-bar${e.person.paused}" data-peer="${g.htmlQuote(e.person.peer)}">
                                                                ${memberStatuses.get(e.person.paused)}
                                                        </td>
                                                        <td class="-consultant-queue-name" data-peer="${g.htmlQuote(e.person.peer)}"></td>
                                                        <td><%--todo: 수신 응답/전체 --%></td>
                                                        <td><%--todo: 발신 전체 --%></td>
                                                        <td class="-consultant-calling-custom-number" data-peer="${g.htmlQuote(e.person.peer)}" >${g.htmlQuote(e.customNumber)}</td>
                                                        <td class="-consultant-status-time" data-peer="${g.htmlQuote(e.person.peer)}">00:00</td>
                                                        <td>
                                                            <button class="play-btn"><img src="<c:url value="/resources/images/play.svg"/>" alt="play"></button>
                                                                <%--todo: 감청--%>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>

        </script>
    </tags:scripts>
</tags:tabContentLayout>
