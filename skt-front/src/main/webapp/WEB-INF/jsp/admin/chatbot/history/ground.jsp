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
        <tags:page-menu-tab url="/admin/chatbot/history/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button"
                                    onclick="refreshPageWithoutParameters()">초기화
                            </button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">이름</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:input path="requestBlockName"/>
                                    </div>
                                </div>
                                <div class="two wide column" data-type="DATE"><label class="control-label">검색기간</label></div>
                                <div class="date-picker from-to">
                                    <div class="dp-wrap">
                                        <label for="createdStartDate" style="display:none">From</label>
                                        <form:input path="createdStartDate" cssClass="-datepicker" placeholder="시작일"/>
                                    </div>
                                    <span class="tilde">~</span>
                                    <div class="dp-wrap">
                                        <label for="createdEndDate" style="display:none">to</label>
                                        <form:input path="createdEndDate" cssClass="-datepicker" placeholder="종료일"/>
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
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건
                        </h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui table celled">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>챗봇명</th>
                            <th>전송시간</th>
                            <th>카카오프로필명</th>
                            <th>상담톡아이디</th>
                            <th>블럭명</th>
                            <th>고객발화</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr>
                                        <td>${e.seq}</td>
                                        <td>${e.botName}</td>
                                        <td><fmt:formatDate value="${e.insertDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${e.nickName}</td>
                                        <td>${e.requestUserPlusFriendUserKey}</td>
                                        <td>${e.requestBlockName}</td>
                                        <td>${e.requestUtterance}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/chatbot/history" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function consultingHistoryEmailView() {
                $('#modal-consulting-history-email-view').modalShow();
            }
        </script>
        <script>
            function consultingHistoryTalkView(roomId) {
                popupReceivedHtml('/admin/wtalk/history/modal?roomId=' + encodeURIComponent(roomId), 'modal-consulting-history-talk-view');
            }

            const searchForm = $('#search-form');

            $('.-popup-records').click(function (event) {
                event.stopPropagation();

                const $this = $(this);
                if ($this.attr('data-has-records'))
                    return;

                popupReceivedHtml('/admin/application/maindb/salespos/' + $this.attr('data-id') + '/modal-records', 'modal-records').done(function (html) {
                    const mixedNodes = $.parseHTML(html, null, true);

                    const modal = (function () {
                        for (let i = 0; i < mixedNodes.length; i++) {
                            const node = $(mixedNodes[i]);
                            if (node.is('.ui.modal'))
                                return node;
                        }
                        throw 'cannot find modal element';
                    })();

                    $this.after(modal);
                    modal.find('audio').each(function () {
                        maudio({obj: this});
                    });
                });
            });

            function popupModal(id) {
                popupReceivedHtml('/admin/application/maindb/salespos/' + encodeURIComponent(id || 'new') + '/modal', 'modal-result');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/maindb-salespos/' + encodeURIComponent(id)).done(function () {
                        reload();
                    });
                });
            }

            function replaceCodeSelect() {
                searchForm.find()
            }

            function formSubmit() {

                let objText = $('#modal-result').find('[data-value="Y"]');
                for (let i = 0; i < objText.length; i++) {
                    if (objText[i].getAttribute('data-type') === 'text') {
                        if (objText[i].value.trim() === "") {
                            alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                            return;
                        }
                    } else if (objText[i].getAttribute('data-type') === 'select') {
                        if (objText[i].options[objText[i].selectedIndex].value === "") {
                            alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 선택 해 주세요.");
                            return;
                        }
                    } else {
                        if (objText[i].value.trim() === "") {
                            alert("[" + objText[i].getAttribute('data-text') + "] 을(를) 입력 해 주세요.");
                            return;
                        }
                    }
                }
                $('#modal-result').submit();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>