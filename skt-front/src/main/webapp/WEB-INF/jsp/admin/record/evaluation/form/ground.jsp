<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/record/evaluation/form/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button -control-entity" data-entity="EvaluationForm" style="display: none;" onclick="popupPreviewModal(getEntityId('EvaluationForm'))">미리보기</button>
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="EvaluationForm" style="display: none;" onclick="popupModal(getEntityId('EvaluationForm'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="EvaluationForm" style="display: none;" onclick="deleteEntity(getEntityId('EvaluationForm'))">삭제</button>
                        <button class="ui basic button -control-entity" data-entity="EvaluationForm" style="display: none;" onclick="popupModal(null, getEntityId('EvaluationForm'))">복사</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="EvaluationForm">
                        <thead>
                        <tr>
                            <th>평가지명</th>
                            <th>진행여부</th>
                            <th>진행기간</th>
                            <th class="two wide">
                                숨김
                                <button class="ui button mini compact" onclick="updateFormHiddenAttributes()">확인</button>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}">
                                    <tr data-id="${e.id}">
                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td>${g.htmlQuote(message.getEnumText(e.useType))}</td>
                                        <td><fmt:formatDate value="${e.startDate}" pattern="yyyy-MM-dd"/> ~ <fmt:formatDate value="${e.endDate}" pattern="yyyy-MM-dd"/></td>
                                        <td>
                                            <div class="ui form checkbox fitted">
                                                <input type="checkbox" ${e.hidden == 'Y' ? 'checked' : ''}>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/record/evaluation/form/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const formIdToHidden = {
                <c:forEach var="e" items="${pagination.rows}">'${e.id}': ${e.hidden == 'Y'}, </c:forEach>
            };

            function popupPreviewModal(id) {
                popupReceivedHtml('/admin/record/evaluation/form/' + id + '/modal-preview', 'modal-preview');
            }

            function popupModal(id, copyingTargetId) {
                popupReceivedHtml('/admin/record/evaluation/form/' + (id || 'new') + '/modal?copyingTargetId=' + (copyingTargetId || ''), 'modal-form');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까? 관련 평가가 있을 경우 함께 삭제됩니다.').done(function () {
                    restSelf.delete('/api/evaluation-form/' + id).done(function () {
                        reload();
                    });
                });
            }

            function updateFormHiddenAttributes() {
                const updatingFormAndHiddenAttribute = {};

                $('table[data-entity=EvaluationForm] tr[data-id]').each(function () {
                    const id = $(this).attr('data-id');
                    const hidden = $(this).find('input').is(':checked');

                    if (formIdToHidden[id] !== hidden)
                        updatingFormAndHiddenAttribute[id] = hidden;
                });

                function process() {
                    const ids = keys(updatingFormAndHiddenAttribute);
                    if (ids.length <= 0)
                        return alert('반영되었습니다.');

                    restSelf.put('/api/evaluation-form/' + ids[0] + '/' + (updatingFormAndHiddenAttribute[ids[0]] ? 'hide' : 'show')).done(function () {
                        formIdToHidden[ids[0]] = updatingFormAndHiddenAttribute[ids[0]];
                        delete updatingFormAndHiddenAttribute[ids[0]];
                        process();
                    });
                }

                process();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
