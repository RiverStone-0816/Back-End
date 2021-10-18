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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/chatbot/event/profile/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div class="pull-left">검색</div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">인증날짜</label></div>
                                <div class="ten wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker"/>
                                            <form:select path="startHour">
                                                <c:forEach var="e" begin="0" end="23" step="1">
                                                    <form:option value="${e}" label="${e}시"/>
                                                </c:forEach>
                                            </form:select>
                                            <label>:00:00</label>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker"/>
                                            <form:select path="endHour">
                                                <c:forEach var="e" begin="0" end="23" step="1">
                                                    <form:option value="${e}" label="${e}시"/>
                                                </c:forEach>
                                            </form:select>
                                            <label>:59:59</label>
                                        </div>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">챗봇선택</label></div>
                                <div class="three wide column">
                                    <div class="ui form">
                                        <form:select path="chatbotId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${talkServiceList}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">카카오프로필닉네임</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="profileName"/>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">카카오프로필전화번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="phoneNumber"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic button -control-entity" data-entity="ChatbotProfile" style="display: none;" onclick="popupModal(getEntityId('ChatbotProfile'))">이벤트내보내기</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="ChatbotProfile">
                        <tr>
                            <th>번호</th>
                            <th>카카오챗봇</th>
                            <th>인증날짜</th>
                            <th>카카오프로필닉네임</th>
                            <th>카카오프로필전화번호</th>
                            <th>상담톡아이디</th>
                            <th>고객DB명</th>
                        </tr>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.chatbotName)}</td>
                                        <td><fmt:formatDate value="${e.authenticationDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.profileName)}</td>
                                        <td>${g.htmlQuote(e.phoneNumber)}</td>
                                        <td>${g.htmlQuote(e.profileName)}</td>
                                        <td>${g.htmlQuote(e.maindbCustomName)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7">결과가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/chatbot/event/profile" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>
    <tags:scripts>
        <script>
            $('#search-form').on('submit', function () {
                const form = $(this);
                form.find('#startDate').val(form.find() )
            });

            function popupModal(seq) {
                popupReceivedHtml('/admin/chatbot/event/profile/send/event/' + seq, 'modal-send-event');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>