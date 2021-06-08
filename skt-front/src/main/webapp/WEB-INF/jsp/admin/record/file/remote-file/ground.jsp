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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/record/file/remote-file/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">녹취용량관리(백업서버)</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading align-center">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                        <button type="button" class="ui basic button -control-entity" data-entity="Record" style="display: none;" onclick="deleteEntity(getEntityId('Record'))">삭제</button>
                    </div>
                    <div class="pull-right">
                        <div class="progress-wrap">
                            <div class="ui blue progress small -disk-occ" data-percent="${disk.use}">
                                <div class="bar">
                                    <div class="progress">${disk.use}</div>
                                </div>
                                <div class="label">
                                    사용중디스크 : ${disk.used}
                                    <span class="slash">/</span>
                                    사용가능디스크 : ${disk.avail}
                                    <span class="slash">/</span>
                                    사용비율 : ${disk.use}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl" data-entity="Record">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>파일명</th>
                            <th>파일크기</th>
                            <th>다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.fileName)}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.fileName)}</td>
                                        <td><fmt:formatNumber value="${e.size}" pattern="#,###"/></td>
                                        <td>
                                            <a class="ui icon button mini compact"
                                               href="${apiServerUrl}/api/v1/admin/record/remote-file/resource?fileName=${g.htmlQuote(e.fileName)}&token=${accessToken}">
                                                <i class="arrow down icon"></i>
                                            </a>
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
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            $(document).ready(function () {
                $('.-disk-occ').progress();
            });

            function deleteEntity(fileName) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/record-remote-file/' + encodeURIComponent(fileName)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
