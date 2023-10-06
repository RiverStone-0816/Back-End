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

<div class="ui modal">
    <i class="close icon"></i>
    <div class="header">스케쥴유형[미리보기]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <table class="ui structured celled table compact unstackable">
                    <thead>
                    <tr>
                        <th colspan="2">시간</th>
                        <th>수행유형</th>
                        <th>수행데이터</th>
                        <th>음원</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${entity.scheduleGroupLists.size() > 0}">
                            <c:forEach var="s" items="${entity.scheduleGroupLists}" varStatus="scheduleStatus">
                                <tr>
                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                    <td>
                                        <fmt:formatNumber value="${(s.fromhour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${s.fromhour % 60}" pattern="00"/>
                                        ~
                                        <fmt:formatNumber value="${(s.tohour / 60).intValue()}" pattern="00"/>:<fmt:formatNumber value="${s.tohour % 60}" pattern="00"/>
                                    </td>
                                    <td>
                                            ${s.kind == 'S' ? '음원만플레이'
                                                    : s.kind == 'D' ? '번호직접연결(내부번호연결)'
                                                    : s.kind == 'F' ? '착신전환(외부번호연결)'
                                                    : s.kind == 'I' ? 'IVR연결'
                                                    : s.kind == 'C' ? '예외컨텍스트'
                                                    : s.kind == 'V' ? '음성사서함'
                                                    : s.kind == 'CI' ? '예외컨택스트후IVR'
                                                    : s.kind == 'CD' ? '예외컨택스트후번호연결'
                                                    : s.kind == 'SMS' ? 'SMS'
                                                    : '알수없음: ' + s.kind}
                                    </td>
                                    <td>
                                        <c:if test="${s.kind == 'I' || s.kind == 'C'}">${g.htmlQuote(s.kindDataName)}</c:if>
                                        <c:if test="${s.kind == 'SMS'}">문자발송</c:if>
                                        <c:if test="${s.kind != 'I' && s.kind != 'C' && s.kind != 'SMS'}">${g.htmlQuote(s.kindData)}</c:if>
                                    </td>
                                    <td>${g.htmlQuote(s.kindSoundName)}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${entity.scheduleGroupLists.size() > 0}">
                                <tr>
                                    <td></td>
                                    <td colspan="5">
                                        <table class="time-table">
                                            <tbody>
                                            <tr>
                                                <c:forEach begin="0" end="23" var="h">
                                                    <td>${h}</td>
                                                </c:forEach>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>
                            <c:forEach var="s" items="${entity.scheduleGroupLists}" varStatus="scheduleStatus">
                                <tr>
                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                    <td colspan="5">
                                        <div class="-slider-time" data-start="${s.fromhour}" data-end="${s.tohour}"></div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="null-data">조회된 데이터가 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui blue button modal-close">확인</button>
    </div>
</div>
