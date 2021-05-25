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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="patch"
           action="${pageContext.request.contextPath}/api/queue/${g.htmlQuote(entity.name)}/blending"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">큐(그룹)[수정 : ${g.htmlQuote(entity.hanName)}, ${g.htmlQuote(entity.name)}]<span> ※ 큐는 유저 아이디가 아닌 내선기준입니다.</span></div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">큐그룹명</label></div>
                <div class="twelve wide column">${g.htmlQuote(entity.hanName)}(${g.htmlQuote(entity.number)})</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">콜블랜딩 정책 선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${blendingModes}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="blendingMode" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -blending-sub-input-container" data-value="W">
                <div class="four wide column"><label class="control-label">설정값</label></div>
                <div class="twelve wide column">
                    <div>
                        기준고객대기자명수: &ensp;<form:input path="waitingCount" cssClass="-input-numerical" cssStyle="width: 100px"/><span>명부터</span>
                    </div>
                    <div>
                        기준명수 초과후 유지시간: &ensp;<form:input path="waitingKeepTime" cssClass="-input-numerical" cssStyle="width: 100px"/><span>분 (5분이상권장)</span>
                    </div>
                </div>
            </div>
            <div class="row -blending-sub-input-container" data-value="T">
                <div class="four wide column"><label class="control-label">시간대설정</label></div>
                <div class="twelve wide column">
                    <form:select path="startHour" class="input-small-size time">
                        <c:forEach var="e" begin="0" end="23">
                            <form:option value="${e}"/>
                        </c:forEach>
                    </form:select>
                    <form:select path="startMinute" class="input-small-size minute">
                        <c:forEach var="e" begin="0" end="59">
                            <form:option value="${e}"/>
                        </c:forEach>
                    </form:select>
                    <span class="tilde">~</span>
                    <form:select path="endHour" class="input-small-size time">
                        <c:forEach var="e" begin="0" end="23">
                            <form:option value="${e}"/>
                        </c:forEach>
                    </form:select>
                    <form:select path="endMinute" class="input-small-size minute">
                        <c:forEach var="e" begin="0" end="59">
                            <form:option value="${e}"/>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="row -blending-sub-input-container" data-value="W,T">
                <div class="eight wide column"><label class="control-label">사용자리스트</label></div>
                <div class="eight wide column"><label class="control-label">추가된사용자</label></div>
            </div>
            <div class="row -blending-sub-input-container" data-value="W,T">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.peer)}[${g.htmlQuote(e.hostName)}][${g.htmlQuote(e.idName)}]</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="blendingUser" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.addPersons}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.peer)}[${g.htmlQuote(e.hostName)}][${g.htmlQuote(e.idName)}]</option>
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
        data.blendingUser = [];
        modal.find('[name="blendingUser"] option').each(function () {
            data.blendingUser.push($(this).val());
        });
    };

    const blendingMode = modal.find('[name=blendingMode]');

    function changeBlendingMode() {
        const value = blendingMode.filter(':checked').val();
        $('.-blending-sub-input-container').hide()
            .filter(function () {
                const values = $(this).attr('data-value').split(',') || '';
                return values.indexOf(value) >= 0;
            }).show();
    }

    blendingMode.on('click change', changeBlendingMode);
    changeBlendingMode();
</script>
