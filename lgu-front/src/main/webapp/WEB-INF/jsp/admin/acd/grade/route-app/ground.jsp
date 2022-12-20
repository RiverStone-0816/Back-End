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
        <tags:page-menu-tab url="/admin/acd/grade/route-app/"/>
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
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">신청일</label></div>
                                <div class="six wide column">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="endDate" style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">처리여부</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="result">
                                            <form:option value="" label="선택"/>
                                            <c:forEach var="e" items="${resultTypes}">
                                                <form:option value="${e.key}" label="${e.value}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">유형검색</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="type">
                                            <form:option value="" label="전체"/>
                                            <c:forEach var="e" items="${routeTypes}">
                                                <form:option value="${e.key}" label="${e.value}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">전화번호</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid"><form:input path="number"/></div>
                                </div>
                                <div class="two wide column"><label class="control-label">신청인아이디(이름)</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="appUserName"/>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">처리자이름</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <form:input path="rstUserName"/>
                                    </div>
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
                        <button class="ui basic button -control-entity" data-entity="RouteApplication" style="display: none;" onclick="popupAcceptModal(getEntityId('RouteApplication'))">승인</button>
                        <button class="ui basic button -control-entity" data-entity="RouteApplication" style="display: none;" onclick="rejectApplication(getEntityId('RouteApplication'))">반려</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="RouteApplication">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>신청일</th>
                            <th>유형</th>
                            <th>전화번호</th>
                            <th>신청인아이디(이름)</th>
                            <th>처리자</th>
                            <th>사유</th>
                            <th>처리여부</th>
                            <th>듣기/다운로드</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr <c:if test="${e.result.name() == 'NONE'}">data-id="${e.seq}"</c:if>>
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td><fmt:formatDate value="${e.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(message.getEnumText(e.type))}</td>
                                        <td>${e.number}</td>
                                        <td>${g.htmlQuote(e.appUserid)}(${g.htmlQuote(e.appUserName)})</td>
                                        <td>
                                            <c:if test="${e.rstUserid != null}">
                                                ${g.htmlQuote(e.rstUserid)}
                                                <c:if test="${e.rstUserName != null}">
                                                    (${g.htmlQuote(e.rstUserName)})
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>${g.htmlQuote(e.memo)}</td>
                                        <td>${g.htmlQuote(message.getEnumText(e.result))}</td>
                                        <td>
                                            <button type="button" class="ui icon button mini compact -popup-records" data-id="${e.cdrSeq}">
                                                <i class="volume up icon"></i>
                                            </button>
                                        </td>
                                    </tr>
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
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/acd/grade/route-app/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function popupAcceptModal(seq) {
                popupReceivedHtml('/admin/acd/grade/route-app/' + seq + '/modal-accept', 'modal-accept');
            }

            function rejectApplication(seq) {
                restSelf.put('/api/route-app/' + seq + '/reject').done(function () {
                    location.reload();
                });
            }

            $('.-popup-records').click(function (event) {
                event.stopPropagation();

                const $this = $(this);
                if ($this.attr('data-has-records'))
                    return;

                popupReceivedHtml('/admin/record/history/history/' + $this.attr('data-id') + '/modal-records', 'modal-records').done(function (html) {
                    const mixedNodes = $.parseHTML(html, null, true);

                    const modal = (function () {
                        for (let i = 0; i < mixedNodes.length; i++) {
                            const node = $(mixedNodes[i]);
                            if (node.is('#modal-records'))
                                return node;
                        }
                        throw 'cannot find modal element';
                    })();

                    $this.after(modal);
                    modal.find('audio').each(function () {
                        maudio({obj: this});
                    });
                });
            });
        </script>
    </tags:scripts>
</tags:tabContentLayout>
