<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/menu/${userId}/${menu.menuCode}"
           data-done="doneUpdatingAttributes">

    <i class="close icon"></i>
    <div class="header">메뉴 속성 변경</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">메뉴명</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="menuName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">보임여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="view" value="${true}"/>
                                    <label>보임</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="view" value="${false}"/>
                                    <label>안보임</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
<%--            <c:if test="${menu.actionType == 'PAGE'}">--%>
<%--                <div class="row">--%>
<%--                    <div class="four wide column"><label class="control-label">사용자유형</label></div>--%>
<%--                    <div class="twelve wide column">--%>
<%--                        <div class="ui form">--%>
<%--                            <div class="inline fields">--%>
<%--                                <c:forEach var="e" items="${groupLevelAuths}">--%>
<%--                                    <div class="field">--%>
<%--                                        <div class="ui radio checkbox">--%>
<%--                                            <form:radiobutton path="groupLevelAuth" value="${e.key}"/>--%>
<%--                                            <label>${g.htmlQuote(e.value)}</label>--%>
<%--                                        </div>--%>
<%--                                    </div>--%>
<%--                                </c:forEach>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="row">--%>
<%--                    <div class="four wide column"><label class="control-label">부서선택</label></div>--%>
<%--                    <div class="twelve wide column">--%>
<%--                        <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"--%>
<%--                             data-clear=".-clear-group">--%>
<%--                            <button type="button" class="ui icon button mini blue compact -select-group">--%>
<%--                                <i class="search icon"></i>--%>
<%--                            </button>--%>
<%--                            <form:hidden path="groupCode"/>--%>
<%--                            <div class="ui breadcrumb -group-name">--%>
<%--                                <c:choose>--%>
<%--                                    <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">--%>
<%--                                        <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">--%>
<%--                                            <span class="section">${g.htmlQuote(e)}</span>--%>
<%--                                            <c:if test="${!status.last}">--%>
<%--                                                <i class="right angle icon divider"></i>--%>
<%--                                            </c:if>--%>
<%--                                        </c:forEach>--%>
<%--                                    </c:when>--%>
<%--                                    <c:otherwise>--%>
<%--                                        <span class="section">버튼을 눌러 소속을 선택하세요.</span>--%>
<%--                                    </c:otherwise>--%>
<%--                                </c:choose>--%>
<%--                            </div>--%>
<%--                            <button type="button" class="ui icon button mini compact -clear-group">--%>
<%--                                <i class="undo icon"></i>--%>
<%--                            </button>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </c:if>--%>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    window.doneUpdatingAttributes = function () {
        const groupLevelAuths = {
            <c:forEach var="e" items="${groupLevelAuths}">
            '${g.escapeQuote(e.key)}': '${g.escapeQuote(e.value)}',
            </c:forEach>
        };

        restSelf.get('/api/menu/${g.escapeQuote(userId)}/${g.escapeQuote(menu.menuCode)}').done(function (response) {
            const data = response.data;

            const menu = $('.-menu[data-id="${g.escapeQuote(menu.menuCode)}"]');
            if (data.viewYn === 'Y') menu.removeClass('hidden');
            else menu.addClass('hidden');

            menu.find('.-menu-name:first').text(data.menuName);

            if (data.actionType === 'PAGE') {
                const auth = menu.find('.-menu-group-level-auth:first');

                if (data.groupLevelAuthYn) {
                    auth.show().text(groupLevelAuths[data.groupLevelAuthYn] + (data.groupLevelAuthYn === 'G' ? ' (' + data.groupName + ')' : ''));
                } else {
                    auth.hide();
                }
            }
        });

        modal.modalHide();
    }
</script>
