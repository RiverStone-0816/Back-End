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
        <tags:page-menu-tab url="/admin/stt/transcribe/group/"/>
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
                                <div class="two wide column"><label class="control-label">그룹선택</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="transcribeGroup">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${transGroups}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">그룹명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="groupName"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">담당상담원</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="userId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${persons}" itemValue="id" itemLabel="idName"/>
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
                        <div class="ui basic buttons">
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="TranscribeGroup" style="display: none;" onclick="popupModal(getEntityId('TranscribeGroup'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="TranscribeGroup" style="display: none;" onclick="deleteEntity(getEntityId('TranscribeGroup'))">삭제</button>
                        </div>
                        <button class="ui basic button -control-entity" data-entity="TranscribeGroup" style="display: none;" onclick="popupUploadModal(getEntityId('TranscribeGroup'))">전사파일업로드</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="TranscribeGroup">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>그룹명</th>
                            <th>담당자명</th>
                            <th>업로드파일수</th>
                            <th>진행상태</th>
                            <th>인식률</th>
                            <th>실행</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.groupName)}</td>
                                        <td>${g.htmlQuote(e.userId)}</td>
                                        <td>${g.htmlQuote(e.fileCnt)}</td>
                                        <td>${g.htmlQuote(g.messageOf('TranscribeGroupResultCode', e.status))}</td>
                                        <td>${String.format("%.2f", e.recRate)}</td>
                                        <td><button class="ui tiny button" type="button" onclick="executeStt(${e.seq})">STT요청</button></td>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/stt/transcribe/group/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupUploadModal(seq) {
                popupReceivedHtml('/admin/stt/transcribe/group/' + seq + '/modal-upload', 'modal-upload');
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/stt/transcribe/group/' + (seq || 'new') + '/modal', 'modal-group');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/transcribe-group/' + seq).done(function () {
                        reload();
                    });
                });
            }

            function executeStt(seq) {
                restSelf.post('/api/transcribe-group/' + seq + '/execute', null, function () {
                    alert('실행 실패')
                }).done((response) => {
                    if (response.data === 1){
                        restSelf.put('/api/transcribe-group/status/' + seq)
                        alert('STT 텍스트변환 요청 하였습니다.')
                    }else {
                        alert('STT 텍스트변환 요청에 실패하였습니다.')
                    }
                })
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>