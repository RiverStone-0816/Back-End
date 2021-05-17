<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<%@ page import="kr.co.eicn.ippbx.server.model.enums.NumberType" %>
<%--@elvariable id="type" type="kr.co.eicn.ippbx.server.model.enums.NumberType"--%>
<%--@elvariable id="numbers" type="java.util.List<kr.co.eicn.ippbx.server.model.dto.eicn.NumberSummaryResponse>"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/user/tel/number/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${numbers.size()}</span> 건</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui secondary menu">
                        <a class="item ${type == NumberType.SERVICE ? 'active' : null} -number-tab-label" data-type="SERVICE">대표번호</a>
                        <a class="item ${type == NumberType.HUNT ? 'active' : null} -number-tab-label" data-type="HUNT">큐번호</a>
                        <a class="item ${type == NumberType.PERSONAL ? 'active' : null} -number-tab-label" data-type="PERSONAL">개인번호</a>
                    </div>

                    <div class="ui tab active" data-tab="first">
                        <table class="ui celled table compact unstackable">
                            <thead>
                            <tr>
                                <th>No.</th>
                                <th>번호</th>
                                <th>서비스원번호</th>
                                <th>사용중인서비스</th>
                                <th>일정사용여부</th>
                                <th>소속교환기</th>
                                <th class="three wide">변경</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${numbers.size() > 0}">
                                    <c:forEach var="e" items="${numbers}" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td>
                                            <td>${g.htmlQuote(e.number)}</td>
                                            <td>${g.htmlQuote(e.svcCid)}</td>
                                            <td>${g.htmlQuote(e.useService)}</td>
                                            <td>${"Y".equals(g.htmlQuote(e.isSchedule)) ? "사용" : ""}</td>
                                            <td>${g.htmlQuote(e.hostName)}</td>
                                            <td>
                                                <div class="ui form">
                                                    <c:if test="${e.isTypeChange.name() eq 'Y'}">
                                                        <c:if test="${NumberType.of(e.type) != NumberType.SERVICE}">
                                                            <button onclick="updateNumberType('${g.htmlQuote(e.number)}', 'SERVICE')" class="ui button mini compact">대표번호</button>
                                                        </c:if>
                                                        <c:if test="${NumberType.of(e.type) != NumberType.HUNT}">
                                                            <button onclick="updateNumberType('${g.htmlQuote(e.number)}', 'HUNT')" class="ui button mini compact">큐번호</button>
                                                        </c:if>
                                                        <c:if test="${NumberType.of(e.type) != NumberType.PERSONAL}">
                                                            <button onclick="updateNumberType('${g.htmlQuote(e.number)}', 'PERSONAL')" class="ui button mini compact">개인번호</button>
                                                        </c:if>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="null-data">조회된 데이터가 없습니다.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            $('.-number-tab-label').click(function () {
                $('.-number-tab-label').removeClass('active');
                $(this).addClass('active');

                reload();
            });

            function updateNumberType(number, type) {
                restSelf.patch('/api/number/' + encodeURIComponent(number) + '/type', {type: type}).done(function () {
                    reload();
                });
            }

            function reload() {
                const type = $('.-number-tab-label.active:first').attr('data-type');
                location.href = contextPath + '/admin/user/tel/number?type=' + type;
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>