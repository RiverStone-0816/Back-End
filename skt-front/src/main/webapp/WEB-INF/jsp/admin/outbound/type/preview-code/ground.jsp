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
        <tags:page-menu-tab url="/admin/outbound/type/preview-code/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/outbound/type/preview-code/"))}</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable border-top">
                        <thead>
                        <tr>
                            <th>유형명</th>
                            <th>용도</th>
                            <th>필드명</th>
                            <th>연동된하위코드</th>
                            <th>코드</th>
                            <th>코드수정</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <c:choose>
                                        <c:when test="${e.commonFields != null && e.commonFields.size() > 0}">
                                            <c:forEach var="field" items="${e.commonFields}" varStatus="fieldStatus">
                                                <tr>
                                                    <c:if test="${fieldStatus.first}">
                                                        <td rowspan="${e.commonFields.size()}">${g.htmlQuote(e.name)}</td>
                                                        <td rowspan="${e.commonFields.size()}">${g.htmlQuote(g.messageOf('CommonTypeKind', e.kind))}</td>
                                                    </c:if>
                                                    <td>${g.htmlQuote(field.fieldInfo)}</td>
                                                    <td>${g.htmlQuote(field.relatedFieldInfo)}</td>
                                                    <td class="five wide">
                                                        <c:if test="${field.commonCodes != null && field.commonCodes.size() > 0}">
                                                            <table class="include-table" style="table-layout: fixed">
                                                                <tbody>
                                                                <c:forEach var="code" items="${field.commonCodes}">
                                                                    <tr>
                                                                        <td class="two wide">${g.htmlQuote(code.codeId)}</td>
                                                                        <td class="three wide">${g.htmlQuote(code.codeName)}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </c:if>
                                                    </td>
                                                    <td class="two wide">
                                                        <div class="ui form">
                                                            <button class="ui button mini compact" onclick="popupModal(${e.seq}, '${g.htmlQuote(field.fieldId)}')">수정</button>
<%--                                                            <button class="ui button mini compact" onclick="popupUploadModal(${e.seq}, '${g.htmlQuote(field.fieldId)}')">엑셀업로드</button>--%>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td rowspan="${e.commonFields.size()}">${g.htmlQuote(e.name)}</td>
                                                <td rowspan="${e.commonFields.size()}">${g.htmlQuote(g.messageOf('CommonTypeKind', e.kind))}</td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
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
            function popupUploadModal(seq, fieldId) {
                popupReceivedHtml('/admin/outbound/type/code/' + seq + '/' + encodeURIComponent(fieldId) + '/modal-upload', 'modal-upload');
            }

            function popupModal(type, fieldId) {
                popupReceivedHtml('/admin/outbound/type/code/modal-field?type=' + type + '&fieldId=' + encodeURIComponent(fieldId), 'modal-field');
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
