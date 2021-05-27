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
        <tags:page-menu-tab url="/admin/record/callback/distribution/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건 </h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModalHuntDistribution()">큐설정하기</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact structured unstackable num-tbl">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>서비스명</th>
                            <th>서비스원번호</th>
                            <th>서비스070번호</th>
                            <th>큐(그룹)</th>
                            <th>분배받는사람</th>
                            <th class="one wide">상담원설정</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr>
                                    <td rowspan="${e.hunts.size() > 0 ? e.hunts.size() : 1}">${status.index + 1}</td>
                                    <td rowspan="${e.hunts.size() > 0 ? e.hunts.size() : 1}">${g.htmlQuote(e.svcName)}</td>
                                    <td rowspan="${e.hunts.size() > 0 ? e.hunts.size() : 1}">${g.htmlQuote(e.svcNumber)}</td>
                                    <td rowspan="${e.hunts.size() > 0 ? e.hunts.size() : 1}">${g.htmlQuote(e.svcCid)}</td>

                                    <c:choose>
                                        <c:when test="${e.hunts.size() > 0}">
                                            <c:forEach var="hunt" items="${e.hunts}" varStatus="huntStatus">
                                                <c:if test="${!huntStatus.first}">
                                                    <tr>
                                                </c:if>
                                                <td>${g.htmlQuote(hunt.hanName)}</td>
                                                <td>
                                                    <c:forEach var="u" items="${hunt.idNames}" varStatus="userStatus">
                                                        ${g.htmlQuote(u.idName)}<c:if test="${!userStatus.last}">, </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td>
                                                    <div class="ui form">
                                                        <button class="ui button mini" onclick="popupModalUserDistribution('${g.htmlQuote(e.svcNumber)}', '${g.htmlQuote(hunt.queueNumber)}')">상담원설정</button>
                                                    </div>
                                                </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModalHuntDistribution() {
                popupReceivedHtml('/admin/record/callback/distribution/modal-hunt-distribution', 'modal-hunt-distribution');
            }

            function popupModalUserDistribution(svcNumber, huntNumber) {
                popupReceivedHtml($.addQueryString('/admin/record/callback/distribution/modal-user-distribution', {svcNumber:svcNumber, huntNumber:huntNumber}), 'modal-user-distribution');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
