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

<%--@elvariable id="search" type="kr.co.eicn.ippbx.model.search.PhoneSearchRequest"--%>
<%--@elvariable id="pagination" type="kr.co.eicn.ippbx.util.page.Pagination<kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoSummaryResponse>"--%>
<%--@elvariable id="licenseInfo" type="kr.co.eicn.ippbx.model.LicenseInfo"--%>

<%@ page import="kr.co.eicn.ippbx.model.enums.ForwardWhen" %>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/user/tel/extension/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" class="panel panel-search" method="get">
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
                                <div class="two wide column"><label class="control-label">내선</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="extension"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">070번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="voipTel"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">지역번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="localPrefix"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">CID번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="cid"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">

                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <c:if test="${!g.user.idType.equals('M')}">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                        <button class="ui basic button -control-entity" data-entity="Extension" style="display: none;" onclick="popupModal(getEntityId('Extension'))">수정</button>
                        <button class="ui basic button -control-entity" data-entity="Extension" style="display: none;" onclick="deleteEntity(getEntityId('Extension'))">삭제</button>
                        </c:if>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="Extension">
                        <thead>
                        <tr>
                            <th>No.</th>
                            <th>내선</th>
                            <th>개인070(교환기)</th>
                            <th>번호이동원번호</th>
                            <th>지역번호</th>
                            <th>CID</th>
                            <th>녹취여부(${licenseInfo.currentLicence} / ${licenseInfo.licence})</th>
                            <c:if test="${g.usingServices.contains('DUSTT')}">
                            <th>STT(${sttLicenseInfo.currentLicence} / ${sttLicenseInfo.licence})</th>
                            </c:if>
                            <c:if test="${g.usingServices.contains('SPHONE')}">
                            <th>소프트폰(${softPhoneLicenseInfo.currentLicence} / ${softPhoneLicenseInfo.licence})</th>
                            </c:if>
                            <th>착신전환</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.peer)}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.extension)}</td>
                                        <td>${g.htmlQuote(e.voipTel)}</td>
                                        <td>${g.htmlQuote(e.originalNumber)}</td>
                                        <td>${g.htmlQuote(e.localPrefix)}</td>
                                        <td>${g.htmlQuote(e.cid)}</td>
                                        <td>${g.htmlQuote(g.messageOf('RecordType', e.recordType))}</td>
                                        <td>${g.htmlQuote(g.messageOf('SttType', e.stt))}</td>
                                        <td>${g.htmlQuote(g.messageOf('SoftPhoneType', e.softphone))}</td>
                                        <td>
                                            <c:set var="when" value="${ForwardWhen.of(e.forwardWhen)}"/>
                                            <c:choose>
                                                <c:when test="${when == ForwardWhen.NONE}">
                                                    ${g.htmlQuote(g.messageOf('ForwardWhen', e.forwardWhen))}
                                                </c:when>
                                                <c:otherwise>
                                                    [${g.htmlQuote(g.messageOf('ForwardWhen', e.forwardWhen))}][${g.htmlQuote(e.fwKind)}][${g.htmlQuote(e.fwNum)}]
                                                </c:otherwise>
                                            </c:choose>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/user/tel/extension/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(peer) {
                popupReceivedHtml('/admin/user/tel/extension/' + encodeURIComponent(peer || 'new') + '/modal', 'modal-extension');
            }

            function deleteEntity(peer) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/extension/' + encodeURIComponent(peer)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
