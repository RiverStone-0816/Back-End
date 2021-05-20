<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<%--@elvariable id="metaTypes" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.ServiceListSummaryResponse>"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/user/tel/service/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="ServiceList" style="display: none;" onclick="popupModal(getEntityId('ServiceList'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="ServiceList" style="display: none;" onclick="deleteEntity(getEntityId('ServiceList'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="ServiceList">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>서비스명</th>
                            <th>서비스번호</th>
                            <th>소속교환기</th>
                            <th>서비스원번호</th>
                            <th>소속</th>
                            <th>서비스레벨</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="e" items="${list}" varStatus="status">
                            <tr data-id="${g.htmlQuote(e.seq)}">
                                <td>${status.index + 1}</td>
                                <td>${g.htmlQuote(e.svcName)}</td>
                                <td>${g.htmlQuote(e.svcNumber)}</td>
                                <td>${g.htmlQuote(e.hostName)}</td>
                                <td>${g.htmlQuote(e.svcCid)}</td>
                                <td class="five wide">
                                    <div class="ui breadcrumb">
                                        <c:forEach var="o" items="${e.organizationSummary}" varStatus="oStatus">
                                            <span class="section">${o.groupName}</span>
                                            <c:if test="${!oStatus.last}">
                                                <i class="right angle icon divider"></i>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </td>
                                <td>${g.htmlQuote(e.serviceLevel)}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(seq) {
                popupReceivedHtml('/admin/user/tel/service/' + encodeURIComponent(seq || 'new') + '/modal', 'modal-service');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/service/' + seq).done(function () {
                        location.reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
