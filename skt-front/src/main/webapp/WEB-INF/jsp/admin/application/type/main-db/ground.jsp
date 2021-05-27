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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/application/type/main-db/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="popupModal()">추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable">
                        <thead>
                        <tr>
                            <th>유형명</th>
                            <th>필드명</th>
                            <th>기본필드명</th>
                            <th>필수</th>
                            <c:if test="${serviceKind.equals('SC')}">
                                <th>암호화여부</th>
                            </c:if>
                            <th>보이기</th>
                            <th>리스트</th>
                            <th>검색</th>
                            <th class="two wide">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <c:choose>
                                        <c:when test="${e.fields.size() > 0}">
                                            <c:forEach var="field" items="${e.fields}" varStatus="fieldStatus">
                                                <tr>
                                                    <c:if test="${fieldStatus.first}">
                                                        <td rowspan="${e.fields.size()}">${g.htmlQuote(e.name)}</td>
                                                    </c:if>
                                                    <td>${g.htmlQuote(field.fieldInfo)}</td>
                                                    <td>${g.htmlQuote(field.fieldName)}</td>
                                                    <td>${field.isneed == 'Y' ? 'O' : '-'}</td>
                                                    <c:if test="${serviceKind.equals('SC')}">
                                                        <td>${field.isenc == 'Y' ? 'O' : '-'}</td>
                                                    </c:if>
                                                    <td>${field.isdisplay == 'Y' ? 'O' : '-'}[${field.displaySeq}]</td>
                                                    <td>${field.isdisplayList == 'Y' ? 'O' : '-'}</td>
                                                    <td>${field.issearch == 'Y' ? 'O' : '-'}</td>
                                                    <c:if test="${fieldStatus.first}">
                                                        <td rowspan="${e.fields.size()}">
                                                            <div class="ui vertical buttons">
                                                                <button class="ui button mini compact" onclick="popupModal(${e.seq})">수정</button>
                                                                <button class="ui button mini compact" onclick="popupUpdateSequenceFieldsModal(${e.seq})">순서</button>
                                                                <button class="ui button mini compact" onclick="deleteEntity(${e.seq})">삭제</button>
                                                            </div>
                                                        </td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>${g.htmlQuote(e.name)}</td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                    <div class="ui vertical buttons">
                                                        <button class="ui button mini compact" onclick="popupModal(${e.seq})">수정</button>
                                                        <button class="ui button mini compact" onclick="deleteEntity(${e.seq})">삭제</button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" class="null-data">조회된 데이터가 없습니다.</td>
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
            function popupModal(seq) {
                popupReceivedHtml('/admin/application/type/main-db/' + (seq || 'new') + '/modal', 'modal-type');
            }

            function popupUpdateSequenceFieldsModal(seq) {
                popupReceivedHtml('/admin/application/type/main-db/' + seq + '/modal-update-sequence-fields', 'modal-update-sequence-fields');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.patch('/api/common-type/' + seq).done(function () {
                       reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
