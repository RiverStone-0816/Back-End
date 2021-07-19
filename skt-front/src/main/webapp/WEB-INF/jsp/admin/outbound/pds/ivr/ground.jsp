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
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">IVR연결(PDS)</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <input type="checkbox" name="newsletter" id="_newsletter" checked="" tabindex="0" class="hidden"><label for="_newsletter">검색옵션 전체보기</label>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>루트IVR 선택</th>
                                <td colspan="7">
                                    <div class="ui form">
                                        <select name="seq">
                                            <option value="">IVR선택</option>
                                            <c:forEach var="e" items="${rootNodes}">
                                                <option value="${e.seq}" ${e.seq == seq ? 'selected' : null}>${g.htmlQuote(e.name)}</option>
                                            </c:forEach>
                                        </select>
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
            </form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
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
                                                            <audio controls src="${apiServerUrl}/api/v1/admin/sounds/ars/${e.soundCode}/resource?token=${accessToken}"></audio>
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
            <button type="submit" class="ui orange button">저장</button>
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
