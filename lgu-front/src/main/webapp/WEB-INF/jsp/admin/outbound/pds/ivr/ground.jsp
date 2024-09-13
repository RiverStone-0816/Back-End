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
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">
                                초기화
                            </button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">IVR</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="seq">
                                            <form:option value="" label="IVR선택"/>
                                            <c:forEach var="e" items="${rootNodes}">
                                                <form:option value="${e.seq}" label="${g.htmlQuote(e.name)}"/>
                                            </c:forEach>
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
                                                <i class="folder icon"></i><span class="ui header"><c:if
                                                    test="${e.button != null && e.button != ''}"><span
                                                    class="ui grey circular label tiny">${e.button}</span></c:if>${g.htmlQuote(e.name)}</span>
                                                <div class="ui buttons ivr-control">
                                                    <c:if test="${e.soundCode != null && e.soundCode != ''}">
                                                        <button type="button"
                                                                class="ui icon button mini compact -sound-play -play-trigger">
                                                            <i class="volume up icon" data-value="${e.soundCode}"></i>
                                                        </button>
                                                        <div class="ui popup top right"></div>
                                                    </c:if>
                                                    <button type="button" class="ui button mini compact"
                                                            onclick="popupKeyMapModal(${e.seq})">버튼맵핑
                                                    </button>
                                                    <button type="button" class="ui button mini compact"
                                                            onclick="deleteEntity(${e.code})">삭제
                                                    </button>
                                                </div>
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
    </div>

    <tags:scripts>
        <script>
            $('.-sound-play').click(function (event) {
                event.stopPropagation();

                const sound = $(this).find('i').first().data('value');
                const player = $(this).next().empty();

                if (player.hasClass('out'))
                    return;

                if (!sound)
                    return;

                const src = contextPath + "/api/ars/id/" + sound + "/resource?mode=PLAY";
                const audio = $('<audio controls/>').attr('data-src', src);
                player.append(audio);
                maudio({obj: audio[0], fastStep: 10});
            });

            function rootIvrModal() {
                popupReceivedHtml('/admin/outbound/pds/ivr/new/modal', 'root-ivr-modal');
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
