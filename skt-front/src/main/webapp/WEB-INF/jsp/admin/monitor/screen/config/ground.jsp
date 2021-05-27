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
        <tags:page-menu-tab url="/admin/monitor/screen/config/"/>
        <div class="sub-content ui container fluid">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="ScreenConfig" style="display: none;" onclick="popupModal(getEntityId('ScreenConfig'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="ScreenConfig" style="display: none;" onclick="deleteEntity(getEntityId('ScreenConfig'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="ScreenConfig">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>전광판명칭</th>
                            <th>사용디자인</th>
                            <th>표시데이터</th>
                            <th>슬라이딩문구 사용여부</th>
                            <th>슬라이딩문구</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td>${lookAndFeelToDescription.getOrDefault(e.lookAndFeel, '협외되지 않은 디자인: '.concat(e.lookAndFeel))}</td>
                                        <td>${g.htmlQuote(message.getEnumText(e.expressionType))}</td>
                                        <td>${e.showSlidingText ? '사용함' : '사용안함'}</td>
                                        <td>${g.htmlQuote(e.slidingText)}</td>
                                        <td>
                                            <button type="button" class="ui button mini compact" onclick="popupScreen(${e.seq})">전광판 열기</button>
                                        </td>
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
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupScreen(seq) {
                const ID = 'screen-popup';
                window.open(contextPath + '/admin/monitor/screen/' + seq, ID, "width=0 height=0 menubar=no status=no");
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/monitor/screen/config/' + (seq || 'new') + '/modal', 'modal-config');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/screen-config/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
