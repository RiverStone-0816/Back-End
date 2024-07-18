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
        <tags:page-menu-tab url="/admin/outbound/pds/ivr/"/>
        <div class="sub-content ui container fluid unstackable">
            <form class="panel panel-search" method="get">
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
                                <div class="two wide column"><label class="control-label">루트IVR 선택</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <select name="seq">
                                            <option value="">IVR선택</option>
                                            <c:forEach var="e" items="${rootNodes}">
                                                <option value="${e.seq}" ${e.seq == seq ? 'selected' : null}>${g.htmlQuote(e.name)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-right">
                        <button class="ui basic button" onclick="rootIvrModal()">루트IVR 추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="ui grid">
                        <div class="row">
                            <div class="sixteen wide column">
                                <div id="pds-ivr-container">
                                    <c:forEach var="e" items="${list}">
                                        <div class="ui segments">
                                            <div class="ui segment">
                                                <i class="folder icon"></i><span class="ui header"><c:if test="${e.button != null && e.button != ''}"><span
                                                    class="ui grey circular label tiny">${e.button}</span></c:if>${g.htmlQuote(e.name)}</span>
                                                <div class="ui buttons ivr-control">
                                                    <c:if test="${e.soundCode != null && e.soundCode != ''}">
                                                        <button type="button" class="ui button mini compact -play-trigger" data-target="#ivr-sound-${e.seq}">음원듣기</button>
                                                    </c:if>
                                                    <button type="button" class="ui button mini compact" onclick="popupKeyMapModal(${e.seq})">버튼맵핑</button>
                                                    <button type="button" class="ui button mini compact" onclick="deleteEntity(${e.code})">삭제</button>
                                                </div>
                                                <c:if test="${e.soundCode != null && e.soundCode != ''}">
                                                    <div class="ui popup top right" id="ivr-sound-${e.seq}">
                                                        <div class="maudio">
                                                            <audio data-src="${pageContext.request.contextPath}/api/ars/id/${g.htmlQuote(e.seq)}/resource?mode=PLAY"></audio>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                            <div class="ui secondary segment">
                                                <div class="ui list">
                                                    <c:forEach var="node" items="${e.nodes}">
                                                        <tags:ivr-node node="${node}"/>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <form:form modelAttribute="form" id="root-ivr-modal" class="ui modal mini -json-submit" data-method="post"
               action="${pageContext.request.contextPath}/api/pds-ivr/"
               data-done="reload">

        <i class="close icon"></i>
        <div class="header">IVR[추가]</div>

        <div class="content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="five wide column"><label class="control-label">IVR명</label></div>
                    <div class="eleven wide column">
                        <div class="ui input fluid">
                            <form:input path="name"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="five wide column"><label class="control-label">음원선택</label></div>
                    <div class="eleven wide column">
                        <div class="ui form">
                            <form:select path="soundCode" items="${sounds}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui blue button">저장</button>
        </div>
    </form:form>

    <tags:scripts>
        <script>
            function rootIvrModal() {
                $('#root-ivr-modal').modalShow();
            }

            function popupKeyMapModal(seq) {
                popupReceivedHtml('/admin/outbound/pds/ivr/' + seq + '/modal-key-map', 'modal-key-map');
            }

            function deleteEntity(code) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/pds-ivr/' + code).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
