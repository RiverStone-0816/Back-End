<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stt/transcribe/data/"/>
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
                                <div class="two wide column"><label class="control-label">처리상담원</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="userId">
                                            <form:option value="" label="선택안함"/>
                                            <form:options itemValue="id" itemLabel="idName" items="${persons}"/>
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
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui structured celled table compact unstackable">
                        <thead>
                        <tr>
                            <th>그룹명</th>
                            <th>학습</th>
                            <th>처리상담원</th>
                            <th>진행상황</th>
                            <th>파일명</th>
                            <th>STT상태</th>
                            <th>인식률측정</th>
                            <th>N</th>
                            <th>D</th>
                            <th>S</th>
                            <th>I</th>
                            <th>인식률</th>
                            <th>작업</th>
                            <th>실행</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <c:choose>
                                        <c:when test="${e.dataInfos != null && e.dataInfos.size() > 0}">
                                            <c:forEach var="info" items="${e.dataInfos}" varStatus="infoStatus">
                                                <tr>
                                                    <c:if test="${infoStatus.first}">
                                                        <td rowspan="${e.dataInfos.size()}">${g.htmlQuote(e.groupName)}</td>
                                                    </c:if>
                                                    <td>&nbsp;<input type="checkbox" class="learn" data-seq="${info.seq}" ${info.learn == 'Y' ? 'checked' : ''} /></td>
                                                    <td>${info.userId}</td>
                                                    <td>${g.htmlQuote(g.messageOf('TranscribeDataResultCode', e.status))}</td>
                                                    <td>${info.fileName}</td>
                                                    <td>${info.sttStatus}</td>
                                                    <td>${info.recStatus}</td>
                                                    <td>${String.format("%.2f", info.DRate)}</td>
                                                    <td>${String.format("%.2f", info.SRate)}</td>
                                                    <td>${String.format("%.2f", info.IRate)}</td>
                                                    <td>${String.format("%.2f", info.NRate)}</td>
                                                    <td>${String.format("%.2f", info.ARate)}</td>
                                                    <td>
                                                        <button class="ui button mini compact" onclick="location.href = '${pageContext.request.contextPath}/admin/stt/transcribe/write?groupCode=${info.groupCode}&fileSeq=${info.seq}'">전사작업</button>
                                                        <%--button class="ui button mini compact" onclick="deleteData(${info.seq})">삭제</button--%>
                                                    </td>
                                                    <c:if test="${infoStatus.first}">
                                                        <td rowspan="${e.dataInfos.size()}">
                                                            <button class="ui button mini compact" onclick="learnStt('${user.companyId}', ${info.groupCode})">측정요청</button>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td></td>
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
                                    <td colspan="14" class="null-data">조회된 데이터가 없습니다.</td>
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

            function learnStt(companyId, groupCode) {
                //restSelf.get('http://122.49.74.4:5210/stt_eval', {msg_type: "req_stt_eval", group_seq: groupCode, company_id: companyId, db_host: "${dbHost}"})
                restSelf.post('https://assist.eicn.co.kr:5210/stt_eval', {msg_type: "req_stt_eval", group_seq: groupCode, company_id: companyId, db_host: "${dbHost}"}).done(function (response) {
                    if (response.result === "OK"){
                        alert("인식률측정 요청을 실행하였습니다.");
                    }else{
                        alert("인식률측정 요청에 실패하였습니다.");
                    }
                })
            }

            function deleteData(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/talk-schedule-day/one/' + encodeURIComponent(seq)).done(function () {
                        reload();
                    });
                });
            }


            $('.learn').click(function () {
                const seq = $(this).attr('data-seq')

                restSelf.put('/api/transcribe-data/' + encodeURIComponent(seq) + '/' + $(this).is(':checked'));
            })
        </script>
    </tags:scripts>
</tags:tabContentLayout>
