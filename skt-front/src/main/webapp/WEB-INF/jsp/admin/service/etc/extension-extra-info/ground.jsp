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
        <tags:page-menu-tab url="/admin/service/etc/extension-extra-info/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">개인번호기타설정</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>내선번호</th>
                                <td>
                                    <div class="ui form"><form:input path="extension"/></div>
                                </td>
                                <th>지역번호</th>
                                <td>
                                    <div class="ui form"><form:input path="localPrefix"/></div>
                                </td>
                                <th>CID</th>
                                <td>
                                    <div class="ui form"><form:input path="cid"/></div>
                                </td>
                                <th>과금번호</th>
                                <td>
                                    <div class="ui form"><form:input path="billingNumber"/></div>
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
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span>건</h3>
                        <button class="ui basic button" onclick="popupUpdateExtensionExtraInfoModal()">수정</button>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/service/etc/extension-extra-info/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>내선번호</th>
                            <th>지역번호</th>
                            <th>CID</th>
                            <th>과금번호</th>
                            <th>CRM최초연결상태</th>
                            <th>CRM연결중전화끊김후자동상태</th>
                            <th>CRM비연결시 상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.cid}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.extension)}</td>
                                        <td>${g.htmlQuote(e.localPrefix)}</td>
                                        <td>${g.htmlQuote(e.cid)}</td>
                                        <td>${g.htmlQuote(e.billingNumber)}</td>
                                        <td>${e.firstStatus != null ? g.htmlQuote(message.getEnumText(e.firstStatus)) : null}</td>
                                        <td>${e.dialStatus != null ? g.htmlQuote(message.getEnumText(e.dialStatus)) : null}</td>
                                        <td>${e.logoutStatus != null ? g.htmlQuote(message.getEnumText(e.logoutStatus)) : null}</td>
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
            function popupUpdateExtensionExtraInfoModal() {
                popupReceivedHtml('/admin/service/etc/extension-extra-info/modal?${g.escapeQuote(search.query)}', 'modal-extension-extra-info');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
