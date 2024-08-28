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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/pds-result-group/${entity == null ? null : entity.name}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">상담그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">큐(그룹)명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="hanName"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">실행할교환기</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="runHost">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${hosts}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">통화분배정책</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="strategy">
                            <c:forEach var="e" items="${strategyOptions}">
                                <form:option value="${e.key}" label="${e.value}(${e.key})"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">비연결시컨텍스트</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="busyContext">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${contexts}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">사용자리스트</label></div>
                <div class="eight wide column"><label class="control-label">추가된사용자</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.userId)}">${g.htmlQuote(e.extension)}[${g.htmlQuote(e.idName)} - ${g.htmlQuote(e.companyTrees.stream().map(f -> f.groupName).reduce((a, b) -> b).orElse(""))}]</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="addPersons" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.addPersons}">
                                    <option value="${g.htmlQuote(e.userId)}">${g.htmlQuote(e.extension)}[${g.htmlQuote(e.idName)} - ${g.htmlQuote(e.companyTrees.stream().map(f -> f.groupName).reduce((a, b) -> b).orElse(""))}]</option>
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
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.addPersons = [];
        modal.find('[name="addPersons"] option').each(function () {
            data.addPersons.push({userId: $(this).val()});
        });
    };
</script>
