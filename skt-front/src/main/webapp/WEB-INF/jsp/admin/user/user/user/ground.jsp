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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="pagination" type="kr.co.eicn.ippbx.util.page.Pagination<kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse>"--%>
<%--@elvariable id="searchOrganizationNames" type="java.util.List"--%>
<%--@elvariable id="search" type="kr.co.eicn.ippbx.model.search.PersonSearchRequest"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/user/user/user/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">계정설정</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label>검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>부서조회</th>
                                <td>
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
                                        <button type="button" class="ui icon button mini brand compact -select-group">
                                            <i class="search icon"></i>
                                        </button>
                                        <form:hidden path="groupCode"/>
                                        <div class="ui breadcrumb -group-name">
                                            <c:choose>
                                                <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                                    <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                                        <span class="section">${g.htmlQuote(e)}</span>
                                                        <c:if test="${!status.last}">
                                                            <i class="right angle icon divider"></i>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="section">부서를 선택해 주세요.</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <button type="button" class="ui icon button mini compact -clear-group">
                                            <i class="undo icon"></i>
                                        </button>
                                    </div>
                                </td>
                                <th>아이디</th>
                                <td colspan="3">
                                    <div class="ui form"><form:input path="id"/></div>
                                </td>
                                <th>성명</th>
                                <td colspan="3">
                                    <div class="ui form"><form:input path="idName"/></div>
                                </td>
                                <th>내선</th>
                                <td colspan="3">
                                    <div class="ui form"><form:input path="extension"/></div>
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
                <form:hidden path="sort"/>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="popupModal()">추가</button>
                            <button class="ui button -control-entity" data-entity="PersonList" style="display: none;" onclick="popupMenuModal(getEntityId('PersonList'))">권한/메뉴설정</button>
                            <button class="ui button -control-entity" data-entity="PersonList" style="display: none;" onclick="popupModal(getEntityId('PersonList'))">수정</button>
                            <button class="ui button -control-entity" data-entity="PersonList" style="display: none;" onclick="deleteEntity(getEntityId('PersonList'))">삭제</button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/user/user/user" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled structured table num-tbl border-top unstackable -sortable-table ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="PersonList"
                           data-search-form="#search-form" data-order-field="order">
                        <thead>
                        <tr>
                            <th rowspan="2">선택</th>
                            <th rowspan="2">번호</th>
                            <th rowspan="2"  data-sortable-value="NAME">성명-[아이디]
                                <c:if test="${search.sort.name() == 'NAME'}">
                                    <button class="sort-btn"><i class="material-icons"> arrow_drop_down </i></button>
                                </c:if>
                            </th>
                            <th rowspan="2">계정유형</th>
                            <th rowspan="2">내선</th>
                            <th rowspan="2" data-sortable-value="GROUP">소속
                                <c:if test="${search.sort.name() == 'GROUP'}">
                                    <button class="sort-btn"><i class="material-icons"> arrow_drop_down </i></button>
                                </c:if>
                            </th>
                            <th rowspan="2">녹취권한[듣기][다운][삭제]</th>
                            <th colspan="4">라이센스 할당 여부</th>
                        </tr>
                        <tr>
                            <c:if test="${services.contains('APP') || services.contains('API')}">
                            <th data-sortable-value="STAT">CTI<br>(라이센스:${license.statLicence.currentLicence}/${license.statLicence.licence})
                                <c:if test="${search.sort.name() == 'STAT'}">
                                    <button class="sort-btn"><i class="material-icons"> arrow_drop_down </i></button>
                                </c:if>
                            </th>
                            </c:if>
                            <c:if test="${services.contains('TALK')}">
                            <th data-sortable-value="TALK">채팅상담<br>(라이센스:${license.talkLicense.currentLicence}/${license.talkLicense.licence})
                                <c:if test="${search.sort.name() == 'TALK'}">
                                    <button class="sort-btn"><i class="material-icons"> arrow_drop_down </i></button>
                                </c:if>
                            </th>
                            </c:if>
                            <c:if test="${!serviceKind.equals('CC')}">
                                <c:if test="${services.contains('EMAIL')}">
                            <th>이메일상담여부<br>(라이센스:${license.emailLicense.currentLicence}/${license.emailLicense.licence})</th>
                                </c:if>
                            </c:if>
                            <c:if test="${services.contains('CHATT') || services.contains('CHATWIN') || services.contains('CHATMEMO')}">
                            <th data-sortable-value="CHATT">메신저사용여부<br>(라이센스:${license.chattLicense.currentLicence}/${license.chattLicense.licence})
                                <c:if test="${search.sort.name() == 'CHATT'}">
                                    <button class="sort-btn"><i class="material-icons"> arrow_drop_down </i></button>
                                </c:if>
                            </th>
                            </c:if>
                            <c:if test="${services.contains('PDS')}">
                                <th data-sortable-value="PDS">${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'} <br>(라이센스:${license.pdsLicense.currentLicence}/${license.pdsLicense.licence})
                                    <c:if test="${search.sort.name() == 'PDS'}">
                                        <button class="sort-btn"><i class="material-icons"> arrow_drop_down </i></button>
                                    </c:if>
                                </th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${g.htmlQuote(e.id)}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.idName)}-[${g.htmlQuote(e.id)}]</td>
                                        <td>${g.htmlQuote(g.messageOf('IdType', e.idType))}</td>
                                        <td>${e.extension}</td>
                                        <td class="five wide">
                                            <div class="ui breadcrumb">
                                                <c:forEach var="o" items="${e.companyTrees}" varStatus="oStatus">
                                                    <span class="section">${o.groupName}</span>
                                                    <c:if test="${!oStatus.last}">
                                                        <i class="right angle icon divider"></i>
                                                    </c:if>
                                                </c:forEach>
                                            </div>
                                        </td>
                                        <td>
                                            <span>${g.htmlQuote(g.messageOf('RecordingAuthorityType', e.listeningRecordingAuthority))}</span>
                                            <span>${g.htmlQuote(g.messageOf('RecordingAuthorityType', e.downloadRecordingAuthority))}</span>
                                            <span>${g.htmlQuote(g.messageOf('RecordingAuthorityType', e.removeRecordingAuthority))}</span>
                                        </td>
                                        <c:if test="${services.contains('APP') || services.contains('API')}">
                                        <td>${e.isStat == 'Y' ? '허용됨' : '허용되지 않음'}</td>
                                        </c:if>
                                        <c:if test="${services.contains('TALK')}">
                                        <td>${e.isTalk == 'Y' ? '허용됨' : '허용되지 않음'}</td>
                                        </c:if>
                                        <c:if test="${!serviceKind.equals('CC')}">
                                            <c:if test="${services.contains('EMAIL')}">
                                        <td>${e.isEmail == 'Y' ? '허용됨' : '허용되지 않음'}</td>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${services.contains('CHATT') || services.contains('CHATWIN') || services.contains('CHATMEMO')}">
                                        <td>${e.isChatt == 'Y' ? '허용됨' : '허용되지 않음'}</td>
                                        </c:if>
                                        <c:if test="${services.contains('PDS')}">
                                            <td>${e.isPds == 'Y' ? '허용됨' : '허용되지 않음'}</td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="10" class="null-data">조회된 데이터가 없습니다.</td>
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
            function resetMenu(userId) {
                restSelf.put('/api/menu/' + userId + '/reset').done(function () {
                    $('#modal-menu').modalHide();
                    popupMenuModal(userId);
                });
            }

            function popupMenuAttributesModal(userId, menuCode) {
                popupReceivedHtml('/admin/user/user/user/' + encodeURIComponent(userId) + '/modal-menu/' + encodeURIComponent(menuCode), 'modal-menu-attribute');
            }

            function popupUpdateSequenceModal(userId, menuCode) {
                popupReceivedHtml('/admin/user/user/user/' + encodeURIComponent(userId) + '/modal-menu-sequence/' + encodeURIComponent(menuCode || ''), 'modal-menu-sequence');
            }

            function popupMenuModal(userId) {
                popupReceivedHtml('/admin/user/user/user/' + encodeURIComponent(userId) + '/modal-menu', 'modal-menu');
            }

            function popupModal(userId) {
                popupReceivedHtml('/admin/user/user/user/' + encodeURIComponent(userId || 'new') + '/modal', 'modal-user');
            }

            function deleteEntity(userId) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/user/' + encodeURIComponent(userId)).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
