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
        <tags:page-menu-tab url="/admin/application/maindb/group/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">그룹관리</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>

                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>유형</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="maindbType">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${maindbTypes}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>그룹별</th>
                                <td colspan="3">
                                    <div class="ui input fluid">
                                        <form:input path="name"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                        <div class="ui buttons">
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="MaindbGroup" style="display: none;" onclick="popupModal(getEntityId('MaindbGroup'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="MaindbGroup" style="display: none;" onclick="deleteEntity(getEntityId('MaindbGroup'))">삭제</button>
                        </div>
                        <button class="ui basic button -control-entity" data-entity="MaindbGroup" style="display: none;" onclick="popupUploadModal(getEntityId('MaindbGroup'))">고객정보업로드</button>
                    </div>
                </div>
                <div class="panel-body" style="overflow-x: auto;">
                    <table class="ui celled table structured compact unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="MaindbGroup">
                        <thead>
                        <tr>
                            <th rowspan="2">번호</th>
                            <th colspan="6">그룹기본정보</th>
                            <th colspan="3">데이터중복체크정보</th>
                            <th colspan="3">업로드정보</th>
                            <th rowspan="2">소속</th>
                        </tr>
                        <tr>
                            <th>그룹명</th>
                            <th>그룹생성일</th>
                            <th>고객정보유형</th>
                            <th>상담결과유형</th>
                            <th>마지막업로드날짜</th>
                            <th>추가정보</th>

                            <th>체크여부</th>
                            <th>체크항목</th>
                            <th>업로드시처리방법</th>

                            <th>데이터수</th>
                            <th>업로드횟수</th>
                            <th>마지막업로드상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>

                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td><fmt:formatDate value="${e.makeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.maindbName)}</td>
                                        <td>${g.htmlQuote(e.resultName)}</td>
                                        <td><fmt:formatDate value="${e.lastUploadDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.info)}</td>

                                        <td>${e.isDupUse == 'Y' ? 'O' : 'X'}</td>
                                        <td>${g.htmlQuote(g.messageOf('DupKeyKind', e.dupKeyKind))}</td>
                                        <td>${e.dupIsUpdate == 'Y' ? '업데이트' : '처리안함'}</td>

                                        <td>${e.totalCnt}</td>
                                        <td>${e.uploadTryCnt}</td>
                                        <td>${e.lastUploadStatus == 'Y' ? '업로드완료' : '업로드안함'}</td>

                                        <td>
                                            <c:forEach var="e" items="${e.groupTreeNames}" varStatus="status">
                                                <span class="section">${g.htmlQuote(e)}</span>
                                                <c:if test="${!status.last}">
                                                    <i class="right angle icon divider"></i>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                    </tr>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/application/maindb/group/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupUploadModal(seq) {
                popupReceivedHtml('/admin/application/maindb/group/' + seq + '/modal-upload', 'modal-upload');
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/application/maindb/group/' + (seq || 'new') + '/modal', 'modal-group');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/maindb-group/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
