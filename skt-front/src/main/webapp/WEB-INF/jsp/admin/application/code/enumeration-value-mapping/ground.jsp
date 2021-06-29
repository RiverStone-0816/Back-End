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
        <tags:page-menu-tab url="/admin/application/code/enumeration-value-mapping/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">연동코드등록관리</div>
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
                            <th>연동DB</th>
                            <th style="width: 40px;"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}">
                                    <c:forEach var="field" items="${e.conFields}">
                                        <tr>
                                            <td>${g.htmlQuote(e.name)}</td>
                                            <td>${g.htmlQuote(g.messageOf('CommonTypeKind', e.kind))}</td>
                                            <td>${g.htmlQuote(field.fieldInfo)}</td>
                                            <td>
                                                <div class="ui form">
                                                    <div class="inline field">
                                                        <select name="conGroupId">
                                                            <option value="">선택안함</option>
                                                            <c:forEach var="conGroup" items="${conGroupList}">
                                                                <option value="${g.htmlQuote(conGroup.key)}" ${field.conGroupId == conGroup.key ? 'selected' : null}>${g.htmlQuote(conGroup.value)}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <button class="ui button mini compact"
                                                        onclick="updateFieldMapping(${e.seq}, '${g.htmlQuote(field.fieldId)}', $(this).closest('tr').find('[name=conGroupId]').val())">수정
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" class="null-data">조회된 데이터가 없습니다.</td>
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
            function updateFieldMapping(seq, fieldId, conGroupId) {
                restSelf.put('/api/enumeration-value-mapping/type/' + seq + '/field/' + encodeURIComponent(fieldId) + '/code', {
                    type: seq,
                    fieldId: fieldId,
                    conGroupId: conGroupId
                }).done(function () {
                    alert('반영되었습니다.');
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
