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

<form:form modelAttribute="form" cssClass="ui modal small -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/preview-group/${entity == null ? null : entity.seq}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">프리뷰그룹[추가]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">기본항목설정</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">그룹명</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">프리뷰 유형</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="prvType">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${prvTypes}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">상담결과 유형</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="resultType">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${resultTypes}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini orange compact -select-group">
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
                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">추가정보</label>
                </div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="info" rows="3"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">프리뷰항목설정</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">다이얼시간</label></div>
                <div class="twelve wide column">
                    <form:input path="dialTimeout" size="2" cssClass="-input-numerical"/> 초 (30초이상권장)
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">RID(발신번호) 설정</label></div>
                <div class="six wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="ridKind" value="CAMPAIGN"/>
                        <label>그룹별RID지정</label>
                    </div>
                </div>
                <div class="six wide column">
                    <div class="ui form">
                        <form:input path="ridData" />
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="six wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="ridKind" value="PBX"/>
                        <label>내선별 PBX 설정에 따름</label>
                    </div>
                </div>
            </div>
            <div class="row blank">
                <div class="four wide column"><label class="control-label">과금번호설정</label></div>
                <div class="six wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="billingKind" value="NUMBER"/>
                        <label>그룹별번호</label>
                    </div>
                </div>
                <div class="six wide column">
                    <div class="ui form">
                        <form:select path="billingData">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${numbers}"/>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="six wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="billingKind" value="PBX"/>
                        <label>내선별 PBX 설정에 따름</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">상담원설정</label>
                </div>
                <div class="twelve wide column">
                    <c:forEach var="e" items="${memberKinds}">
                        <div class="field">
                            <div class="ui radio checkbox">
                                <form:radiobutton path="memberKind" value="${e.key}"/>
                                <label>${g.htmlQuote(e.value)}</label>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${persons}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.key)} (${g.htmlQuote(e.value)})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="memberDataList" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${members}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.key)} (${g.htmlQuote(e.value)})</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui orange button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.memberDataList = [];
        modal.find('[name="memberDataList"] option').each(function () {
            data.memberDataList.push($(this).val());
        });
    };
</script>
