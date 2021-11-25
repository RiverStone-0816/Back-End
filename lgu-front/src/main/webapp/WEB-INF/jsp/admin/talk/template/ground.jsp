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
        <tags:page-menu-tab url="/admin/talk/template/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.rows.size()}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="ui basic button" onclick="popupModal()">추가</button>
                        <button type="button" class="ui basic button -control-entity" data-entity="TalkTemplate" style="display: none;" onclick="popupModal(getEntityId('TalkTemplate'))">수정</button>
                        <button type="button" class="ui basic button -control-entity" data-entity="TalkTemplate" style="display: none;" onclick="deleteEntity(getEntityId('TalkTemplate'))">삭제</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="TalkTemplate">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>유형</th>
                            <th>유형데이터</th>
                            <th>작성자</th>
                            <th>템플릿명</th>
                            <th>템플릿멘트</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size()> 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(templateTypes.get(e.type))}</td>
                                        <c:choose>
                                            <c:when test="${e.type.contains('G')}">
                                                <td>
                                                    <c:if test="${metaTypeList.get(e.companyTreeLevel) != null}">
                                                        ${g.htmlQuote(e.typeDataName)}(${g.htmlQuote(metaTypeList.get(e.companyTreeLevel))})
                                                    </c:if>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>${g.htmlQuote(e.typeDataName)}</td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>${g.htmlQuote(e.writeUserName)}</td>
                                        <td>${g.htmlQuote(e.mentName)}</td>
                                        <td style="white-space: pre-wrap">${g.htmlQuote(e.ment)}</td>
                                    </tr>
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
                    <div class="panel-footer">
                        <div class="pull-right">
                            <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/talk/template/" pageForm="${search}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupModal(seq) {
                popupReceivedHtml('/admin/talk/template/' + (seq || 'new') + '/modal', 'modal-talk-template');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/talk-template/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
