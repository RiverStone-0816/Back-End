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
        <tags:page-menu-tab url="/admin/talk/template/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get"  class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/talk/template/"))}</div>
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
                                <th>유형</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="type" id="selecttype">
                                            <form:option value="" label="전체"/>
                                            <form:options items="${types}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>유형데이터</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="metaType">
                                            <form:option value="" label="전체"/>
                                            <form:options items="${metaTypeLists}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>작성자</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="userName">
                                            <form:option value="" label="전체"/>
                                            <form:options items="${writeUserId}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>템플릿명</th>
                                <td colspan="3">
                                    <div class="ui form">
                                        <form:select path="mentName">
                                            <form:option value="" label="전체"/>
                                            <form:options items="${mentName}"/>
                                        </form:select>
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
            </form:form>

            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span class="text-primary">${list.size()}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button type="button" class="ui button" onclick="popupModal()">추가</button>
                            <button type="button" class="ui button -control-entity" data-entity="TalkTemplate" style="display: none;" onclick="popupModal(getEntityId('TalkTemplate'))">수정</button>
                            <button type="button" class="ui button -control-entity" data-entity="TalkTemplate" style="display: none;" onclick="deleteEntity(getEntityId('TalkTemplate'))">삭제</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="TalkTemplate">
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>번호</th>
                            <th>유형</th>
                            <th>유형데이터</th>
                            <th>작성자</th>
                            <th>템플릿명</th>
                            <th>템플릿멘트</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>
                                            <div class="ui radio checkbox">
                                                <input type="radio" name="radio">
                                            </div>
                                        </td>
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(templateTypes.get(e.type))}</td>
                                        <c:choose>
                                            <c:when test="${e.type.contains('G')}">
                                                <td>
                                                    <c:if test="${metaTypeList.get(e.companyTreeLevel) != null}">
                                                        ${g.htmlQuote(e.typeData)}(${g.htmlQuote(metaTypeList.get(e.companyTreeLevel))})
                                                    </c:if>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>${g.htmlQuote(e.typeData)}</td>
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

            $.find('[name=type]').change(function () {
                if ($(this).val() === 'G') {
                    $.find('[name=metaType]').show();
                } else {
                    $.find('[name=metaType]').hide();
                }
            }).change();

            window.prepareWriteForm = function (data) {
                if (data.type === 'P')
                    data.typeData = '${g.user.id}';
                if (data.type === 'C')
                    data.typeData = '${g.user.companyId}';
            };

        </script>
    </tags:scripts>
</tags:tabContentLayout>
