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
        <tags:page-menu-tab url="/admin/sounds/schedule/holiday/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal()">추가</button>
                            <button class="ui button -control-entity" data-entity="Holiday" style="display: none;" onclick="popupModal(getEntityId('Holiday'))">수정</button>
                            <button class="ui button -control-entity" data-entity="Holiday" style="display: none;" onclick="deleteEntity(getEntityId('Holiday'))">삭제</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="Holiday">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>휴일</th>
                            <th>양력/음력</th>
                            <th>날짜</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.seq)}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.holyName)}</td>
                                        <td>${e.lunarYn == 'Y' ? '음력' : '양력'}</td>
                                        <td>${e.holyDate.split('-')[0]}월 ${e.holyDate.split('-')[1]}일</td>
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
            function popupModal(parent) {
                popupReceivedHtml('/admin/sounds/schedule/holiday/' + (parent || 'new') + '/modal', 'modal-holiday');
            }

            function deleteEntity(parent) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/holiday/' + parent).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
