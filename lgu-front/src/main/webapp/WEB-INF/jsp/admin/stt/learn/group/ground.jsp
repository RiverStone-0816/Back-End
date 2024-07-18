<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stt/learn/group/"/>
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
                                        <form:select path="learnGroup">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${learnGroups}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">그룹명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="groupName"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">학습상태</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="learnStatus">
                                            <form:option value="" label="선택안함"/>
                                            <form:option value="" label="미요청"/>
                                            <form:option value="" label="요청완료"/>
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
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="TranscribeGroup">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>그룹명</th>
                            <th>학습그룹정보</th>
                            <th>진행상태</th>
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
                                        <td>
                                            <c:forEach var="groupName" items="${e.groupList}">
                                                ${g.htmlQuote(groupName)}
                                                <br/>
                                            </c:forEach>
                                        </td>
                                        <td>${g.htmlQuote(e.learnStatus)}</td>
                                        <td><button class="ui tiny button" type="button" onclick="executeStt('${user.companyId}', ${e.seq}, '${e.learnStatus}')">학습요청</button></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/stt/learn/group/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupUploadModal(seq) {
                popupReceivedHtml('/admin/stt/learn/group/' + seq + '/modal-upload', 'modal-upload');
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/stt/learn/group/' + (seq || 'new') + '/modal', 'modal-group');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/learn-group/' + seq).done(function () {
                        reload();
                    });
                });
            }

            function executeStt(companyId, seq, learnStatus) {
                if (learnStatus === 'OK') {
                    confirm('이미 학습실행한 그룹입니다. 다시 요청하시겠습니까?').done(function () {
                        restSelf.post('https://assist.eicn.co.kr:5220/make_model',{msg_type: "req_make_model", group_seq: seq, company_id: companyId, db_host: "${dbHost}"}).done(function (response) {
                            if (response.result === "OK"){
                                restSelf.put('/api/learn-group/status/' + seq)
                                alert("학습 요청을 실행하였습니다.");
                            }else{
                                alert("학습 요청에 실패하였습니다.");
                            }
                        })
                    });
                    //return;
                } else if(learnStatus === 'A') {
                    restSelf.post('https://assist.eicn.co.kr:5220/make_model',{msg_type: "req_make_model", group_seq: seq, company_id: companyId, db_host: "${dbHost}"}).done(function (response) {
                        if (response.result === "OK"){
                            restSelf.put('/api/learn-group/status/' + seq)
                            alert("학습 요청을 실행하였습니다.");
                        }else{
                            alert("학습 요청에 실패하였습니다.");
                        }
                    })
                } else {
                    alert("학습요청 실행 중입니다.");
                }
            }

        </script>
    </tags:scripts>
</tags:tabContentLayout>
