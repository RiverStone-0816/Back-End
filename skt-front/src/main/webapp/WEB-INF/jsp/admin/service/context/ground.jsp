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
        <tags:page-menu-tab url="/admin/service/context/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/service/context/"))}</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                        <div class="ui buttons">
                            <button class="ui basic button" onclick="popupModal()">추가</button>
                            <button class="ui basic button -control-entity" data-entity="Context" style="display: none;" onclick="popupModal(getEntityId('Context'))">수정</button>
                            <button class="ui basic button -control-entity" data-entity="Context" style="display: none;" onclick="deleteEntity(getEntityId('Context'))">삭제</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable fixed ${list.size() > 0 ? "selectable-only" : null}" data-entity="Context">
                        <thead>
                        <tr>
                            <th style="width: 48px;">선택</th>
                            <th>번호</th>
                            <th>컨텍스트명</th>
                            <th>컨텍스트</th>
                            <th class="two wide">보이는 ARS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.context)}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.name)}</td>
                                        <td>${g.htmlQuote(e.context)}</td>
                                        <td>
                                            <c:if test="${e.isWebVoice == 'Y'}">
                                                <div class="ui form">
                                                    <button class="ui button mini compact" onclick="popupVisualArsModal('${g.htmlQuote(e.context)}')">보이는 ARS</button>
                                                </div>
                                            </c:if>
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

    <div class="ui modal tiny" id="modal-webvoice-view">
        <i class="close icon"></i>
        <div class="header">
            적용상태 미리보기
        </div>
        <div class="scrolling content rows">
            <div class="row">
                <img src="http://www.eicn.co.kr/img/voice_ex1.png">
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(context) {
                popupReceivedHtml('/admin/service/context/' + encodeURIComponent(context || 'new') + '/modal', 'modal-context');
            }

            function popupVisualArsModal(context) {
                popupReceivedHtml('/admin/service/context/' + encodeURIComponent(context) + '/modal-visual-ars', 'modal-visual-ars');
            }

            function deleteEntity(context) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/context/' + encodeURIComponent(context)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
