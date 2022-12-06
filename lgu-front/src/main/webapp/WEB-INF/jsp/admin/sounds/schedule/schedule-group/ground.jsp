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
        <tags:page-menu-tab url="/admin/sounds/schedule/schedule-group/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupScheduleGroupModal()">유형추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable">
                        <thead>
                        <tr>
                            <th class="two wide">유형명</th>
                            <th colspan="2">시간</th>
                            <th>수행유형</th>
                            <th>수행데이터</th>
                            <th>음원</th>
                            <th class="one wide">관리</th>
                            <th class="one wide">추가</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${e.scheduleGroupLists.size() > 0}">
                                            <c:forEach var="s" items="${e.scheduleGroupLists}" varStatus="scheduleStatus">
                                                <tr>
                                                    <c:if test="${scheduleStatus.first}">
                                                        <td rowspan="${e.scheduleGroupLists.size() * 2 + 1}">${g.htmlQuote(e.name)}</td>
                                                    </c:if>
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
                                                                    : s.kind == 'CD' ? '예외컨택스트후번호연결'
                                                                    : s.kind == 'CI' ? '예외컨택스트후IVR'
                                                                    : '알수없음: ' + s.kind}
                                                    </td>
                                                    <td>
                                                        <c:if test="${s.kind == 'I' || s.kind == 'C' || s.kind == 'CI' || s.kind == 'CD'}">${g.htmlQuote(s.kindDataName)}</c:if>
                                                        <c:if test="${s.kind != 'I' && s.kind != 'C' && s.kind != 'CI' && s.kind != 'CD'}">${g.htmlQuote(s.kindData)}</c:if>
                                                    </td>
                                                    <td>${g.htmlQuote(s.kindSoundName)}</td>
                                                    <td>
                                                        <button class="ui button mini compact" onclick="popupScheduleItemModal(${e.parent}, ${s.child})">수정</button>
                                                        <button class="ui button mini compact" onclick="deleteScheduleItem(${s.child})">삭제</button>
                                                    </td>
                                                    <c:if test="${scheduleStatus.first}">
                                                        <td rowspan="${e.scheduleGroupLists.size() * 2 + 1}">
                                                            <div class="ui vertical buttons">
                                                                <button class="ui button mini compact" onclick="popupScheduleItemModal(${e.parent})">항목추가</button>
                                                                <button class="ui button mini compact" onclick="deleteScheduleGroup(${e.parent})">유형삭제</button>
                                                                <button class="ui button mini compact -copy-group" onclick="copyScheduleGroup(${e.parent})">복사하기</button>
                                                            </div>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${e.scheduleGroupLists.size() > 0}">
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
                                            <c:forEach var="s" items="${e.scheduleGroupLists}" varStatus="scheduleStatus">
                                                <tr>
                                                    <td style="width: 50px;">${scheduleStatus.index + 1}</td>
                                                    <td colspan="5">
                                                        <div class="-slider-time" data-key="${s.child}" data-parent="${s.parent}" data-start="${s.fromhour}" data-end="${s.tohour}"></div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>${g.htmlQuote(e.name)}</td>
                                                <td style="width: 50px;"></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                    <div class="ui vertical buttons">
                                                        <button class="ui button mini compact" onclick="popupScheduleItemModal(${e.parent})">항목추가</button>
                                                        <button class="ui button mini compact" onclick="deleteScheduleGroup(${e.parent})">유형삭제</button>
                                                        <button class="ui button mini compact -paste-group" style="display: none;" onclick="pasteScheduleGroup(${e.parent})">붙여넣기</button>
                                                    </div>
                                                </td>
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

    <form id="modal-schedule-type-add" class="ui modal tiny -json-submit" data-method="post" action="<c:url value="/api/schedule-group/"/>" data-done="reload">
        <i class="close icon"></i>
        <div class="header">스케쥴유형[추가]</div>

        <div class="scrolling content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">유형명</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input name="name"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui blue button">확인</button>
        </div>
    </form>

    <tags:scripts>
        <script>
            function popupScheduleGroupModal() {
                const modal = $('#modal-schedule-type-add');
                modal.find('[name]').val('');
                modal.modalShow();
            }

            function deleteScheduleGroup(parentId) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/schedule-group/' + parentId).done(function () {
                        reload();
                    });
                });
            }

            let copyingGroupParent;

            function copyScheduleGroup(parent) {
                copyingGroupParent = parent;
                $('.-copy-group').hide();
                $('.-paste-group').show();
            }

            function pasteScheduleGroup(parent) {
                restSelf.post('/api/schedule-group/' + copyingGroupParent + '/' + parent + '/copy').done(function () {
                    reload();
                });
            }

            function deleteScheduleItem(childId) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/schedule-group/item/' + childId).done(function () {
                        reload();
                    });
                });
            }

            function popupScheduleItemModal(parentId, childId) {
                popupReceivedHtml('/admin/sounds/schedule/schedule-group/' + parentId + '/item/' + (childId || 'new') + '/modal', 'modal-schedule-item');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
