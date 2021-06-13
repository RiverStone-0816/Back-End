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

    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/total/"/>
        <div class="sub-content ui container fluid unstackable">
            <form class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">모니터링[큐그룹별]</div>
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
                                <th>큐그룹</th>
                                <td colspan="11">
                                    <div class="ui form">
                                        <select onchange="filterQueue($(this).val())">
                                            <option value="">전체</option>
                                            <c:forEach var="e" items="${queues}">
                                                <option value="${g.escapeQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <c:forEach var="queue" items="${queueToPersons}">
                        <div class="panel-section -queue-section" data-name="${g.escapeQuote(queue.key)}">
                            <div class="panel-heading transparent">
                                <div class="panel-sub-title">${g.htmlQuote(queues.get(queue.key))}</div>
                                <ul class="state-list">
                                        <%--todo: 새로운 상태정보가 업데이트되면, 숫자 바꿔줘야 함.--%>
                                    <li>대기: ${queue.value.steam().filter(e -> e.person.paused == 0).count()}명</li>
                                    <li>통화중: ${queue.value.steam().filter(e -> e.person.paused == 1).count()}명</li>
                                    <li>후처리: ${queue.value.steam().filter(e -> e.person.paused == 2).count()}명</li>
                                    <li>기타: ${queue.value.steam().filter(e -> e.person.paused != 0 && e.person.paused != 1 && e.person.paused != 2).count()}명</li>
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
                                                <c:forEach var="i" begin="0" end="${queue.value.size() / 2}">
                                                    <c:set var="e" value="${queue.value.get(i)}"/>
                                                    <tr>
                                                        <td>${g.htmlQuote(e.person.idName)}</td>
                                                        <td>${g.htmlQuote(e.person.extension)}</td>
                                                        <td class="-consultant-status -consultant-status-with-color bcolor-bar${e.person.paused}" data-peer="${g.htmlQuote(e.person.peer)}">
                                                                ${memberStatuses.get(e.person.paused)}
                                                        </td>
                                                        <td class="-consultant-queue-name" data-peer="${g.htmlQuote(e.person.peer)}"></td>
                                                        <td><%--todo: 수신 응답/전체 --%></td>
                                                        <td><%--todo: 발신 전체 --%></td>
                                                        <td class="-consultant-calling-custom-number" data-peer="${g.htmlQuote(e.person.peer)}">${g.htmlQuote(e.customNumber)}</td>
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
                                                <c:forEach var="i" begin="${queue.value.size() / 2 + 0.1}" end="${queue.value().size()}">
                                                    <c:set var="e" value="${queue.value.get(i)}"/>
                                                    <tr>
                                                        <td>${g.htmlQuote(e.person.idName)}</td>
                                                        <td>${g.htmlQuote(e.person.extension)}</td>
                                                        <td class="-consultant-status -consultant-status-with-color bcolor-bar${e.person.paused}" data-peer="${g.htmlQuote(e.person.peer)}">
                                                                ${memberStatuses.get(e.person.paused)}
                                                        </td>
                                                        <td class="-consultant-queue-name" data-peer="${g.htmlQuote(e.person.peer)}"></td>
                                                        <td><%--todo: 수신 응답/전체 --%></td>
                                                        <td><%--todo: 발신 전체 --%></td>
                                                        <td class="-consultant-calling-custom-number" data-peer="${g.htmlQuote(e.person.peer)}">${g.htmlQuote(e.customNumber)}</td>
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
            function filterQueue(queueName) {
                if (queueName) {
                    $('.-queue-section').hide().filter('[data-name="' + queueName + '"]').show();
                } else {
                    $('.-queue-section').show();
                }
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
