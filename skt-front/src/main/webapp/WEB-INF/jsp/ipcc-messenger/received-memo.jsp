<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<form:form modelAttribute="search" class="-ajax-loader" method="get" data-method="get"
           action="${pageContext.request.contextPath}/ipcc-messenger/tab-received-memo"
           data-target="#tab4">
    <div class="inner-box" style="margin-top: 5px;">
        <div class="ui middle aligned grid search">
            <div class="row">
                <div class="four wide column">날짜</div>
                <div class="twelve wide column">
                    <div class="multi-input-wrap">
                        <div class="inner">
                            <div class="ui fluid small input">
                                <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                            </div>
                        </div>
                        <div class="tilde">~</div>
                        <div class="inner">
                            <div class="ui fluid small input">
                                <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">보낸사람</div>
                <div class="twelve wide column">
                    <div class="ui fluid small input">
                        <form:input path="sendUser"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">내용</div>
                <div class="twelve wide column">
                    <div class="ui fluid small input">
                        <form:input path="content"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="pull-left">
                        <form:checkbox path="displayUnreadMemo"/>
                        <label>안읽은 쪽지</label>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui black button compact tiny" onclick="deleteReceivedMessages()">삭제</button>
                            <%--<button type="button" class="ui black button basic compact tiny">초기화</button>--%>
                        <button type="submit" class="ui darkblue button compact tiny">검색</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="inner-box bb-unset inner-scroll-wrap inner-scroll-memo">
        <h1 class="sub-title">받은 쪽지함</h1>
        <table class="ui celled table fixed unstackable small compact">
            <thead>
            <tr>
                <th class="two wide" style="width: 3.5em;">선택</th>
                <th class="two wide" style="width: 3.5em;">쪽지</th>
                <th>보낸사람</th>
                <th>내용</th>
                <th style="width: 8em">날짜</th>
            </tr>
            </thead>
            <tbody >
            <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                <tr onclick="popupMemoModal(${e.seq}, ${e.sendUserid != g.user.id})" style="cursor: pointer;">
                    <td onclick="event.stopPropagation();" style="cursor: default;">
                        <input type="checkbox" class="-check-received-memo" data-id="${e.seq}">
                    </td>
                    <td><i class="envelope ${e.readYn == 'Y' ? 'open' : ''} icon state -read-received-memo" data-id="${e.seq}" data-sender="${g.escapeQuote(e.sendUserid)}"></i></td>
                    <td>${g.htmlQuote(e.sendUserName)}</td>
                    <td>${g.htmlQuote(e.content)}</td>
                    <td><fmt:formatDate value="${e.insertTime}" pattern="MM-dd HH:mm"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="panel-footer">
            <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/ipcc-messenger/tab-received-memo/" pageForm="${search}"
                             ajaxLoaderEnable="true" ajaxLoaderTarget="#tab4"/>
        </div>
    </div>
</form:form>

<tags:scripts>
    <script>
        function deleteReceivedMessages() {
            const memoSequences = [];
            $('.-check-received-memo:checked').each(function () {
                memoSequences.push($(this).attr('data-id'));
            });

            if (memoSequences.length === 0)
                return alert('데이터를 선택하여 주세요.');

            restSelf.delete('/api/memo', {memoSequences: memoSequences}).done(function () {
                alert('삭제되었습니다.', function () {
                    $('#tab4 button[type=submit]').click();
                });
            });
        }
    </script>
</tags:scripts>
