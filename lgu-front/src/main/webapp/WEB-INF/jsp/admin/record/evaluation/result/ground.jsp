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
        <tags:page-menu-tab url="/admin/record/evaluation/result/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div>
                        검색
                    </div>
                    <div class="dp-flex align-items-center">
                        <div class="ui slider checkbox mr15">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
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
                                <div class="two wide column"><label class="control-label">평가일</label></div>
                                <div class="six wide column -buttons-set-range-container" data-startdate="[name=startEvaluationDate]" data-enddate="[name=endEvaluationDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="startEvaluationDate" style="display:none">From</label>
                                            <form:input path="startEvaluationDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="endEvaluationDate" style="display:none">to</label>
                                            <form:input path="endEvaluationDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">상담일</label></div>
                                <div class="six wide column -buttons-set-range-container" data-startdate="[name=startRingDate]" data-enddate="[name=endRingDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="startRingDate" style="display:none">From</label>
                                            <form:input path="startRingDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="endRingDate" style="display:none">to</label>
                                            <form:input path="endRingDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">평가지</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="evaluationId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${forms}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">상담원</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="userId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${persons}" itemValue="id" itemLabel="idName"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">진행상태</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="status">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${statuses}"/>
                                        </form:select>
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
                        <button class="ui basic green button" type="button" onclick="downloadExcel()">Excel 다운로드</button>
                        <button class="ui button -control-entity" data-entity="EvaluationResult" style="display: none;" onclick="popupModal(getEntityId('EvaluationResult'))">상세보기</button>
                        <button class="ui button -control-entity" data-entity="EvaluationResult" style="display: none;" onclick="deleteEntity(getEntityId('EvaluationResult'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl ${pagination.rows.size() > 0 ? 'selectable-only' : null}" data-entity="EvaluationResult">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>상담원</th>
                            <th>수신번호</th>
                            <th>발신번호</th>
                            <th>통화시간</th>
                            <th>평가지</th>
                            <th>평가시간</th>
                            <th>점수</th>
                            <th>진행상태</th>
                            <th>이의제기</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.targetUserName)}</td>
                                        <td>${e.cdr.dst}</td>
                                        <td>${e.cdr.src}</td>
                                        <td><fmt:formatDate value="${e.cdr.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.form.name)}</td>
                                        <td><fmt:formatDate value="${e.evaluationDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${e.totalScore}</td>
                                        <td>${g.htmlQuote(message.getEnumText(e.processStatus))}</td>
                                        <td>
                                            <c:if test="${e.processStatus.literal == 'EVALUATION_ING' && user.id == e.targetUserid}">
                                                <button type="button" class="ui button compact mini" onclick="disputeEvaluation(${e.id})">이의제기</button>
                                                <button type="button" class="ui button compact mini" onclick="confirmEvaluation(${e.id})">확인</button>
                                            </c:if>
                                            <c:if test="${e.processStatus.literal == 'OBJECTION' && ['J', 'A', 'B'].contains(user.idType)}">
                                                <button type="button" class="ui button compact mini" onclick="processObjection(${e.id})">처리</button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="10" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/record/evaluation/result/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal tiny" id="modal-rating-memo">
        <i class="close icon"></i>
        <div class="header">이의제기</div>

        <div class="content scrolling rows">
            <div class="ui grid">
                <div class="row">
                    <div class="sixteen wide column"><label class="control-label">상담원 의견</label></div>
                    <div class="sixteen wide column">
                        <div class="ui form">
                            <div class="field">
                                <input type="hidden" name="id"/>
                                <textarea rows="3" name="challengeMemo"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button" onclick="submitDisputing()">저장</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            function processObjection(id) {
                popupReceivedHtml('/admin/record/evaluation/result/' + id + '/modal-evaluation', 'modal-evaluation');
            }

            function disputeEvaluation(id) {
                $('#modal-rating-memo').modalShow();
                $('#modal-rating-memo [name=id]').val(id);
            }

            function submitDisputing() {
                $('#modal-rating-memo').asJsonData().done(function (data) {
                    restSelf.put('/api/evaluation-result/' + data.id + '/dispute', {challengeMemo: data.challengeMemo}).done(function () {
                        alert('반영되었습니다.');
                        reload();
                    });
                });
            }

            function confirmEvaluation(id) {
                restSelf.put('/api/evaluation-result/' + id + '/confirm').done(function () {
                    alert('반영되었습니다.');
                    reload();
                });
            }

            function popupModal(id) {
                popupReceivedHtml('/admin/record/evaluation/result/' + id + '/modal', 'modal-form');
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/evaluation-result/' + id).done(function () {
                        reload();
                    });
                });
            }

            function downloadExcel() {
                window.open(contextPath + '/admin/record/evaluation/result/_excel?${g.escapeQuote(search.query)}', '_blank');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
