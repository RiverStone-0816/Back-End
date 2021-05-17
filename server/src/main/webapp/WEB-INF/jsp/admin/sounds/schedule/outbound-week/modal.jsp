<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form commandName="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/outbound-week-schedule/${entity == null ? null : entity.parent}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">주간스케쥴러[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">스케쥴명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">추가가능한번호</label></div>
                <div class="eight wide column"><label class="control-label">추가된번호</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${extensions}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.key)}[${g.htmlQuote(e.value)}]</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="extensions" class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.extensions}">
                                    <option value="${g.htmlQuote(e.extension)}">${g.htmlQuote(e.extension)} ${g.htmlQuote(e.inUseIdName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">요일</label></div>
                <div class="twelve wide column">
                    <c:forEach var="e" items="${dayOfWeeks}">
                        <div class="ui checkbox">
                            <form:checkbox path="weeks" multiple="multiple" value="${e.key}"/>
                            <label>${g.htmlQuote(e.value)}</label>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">시간선택</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <select class="input-small-size time" name="fromHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.fromhour / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="fromMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.fromhour % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <span class="tilde">~</span>
                        <select class="input-small-size time" name="toHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.tohour / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="toMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.tohour % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="three wide column"><label class="control-label">음원</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="soundCode" items="${sounds}"/>
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
        data.extensions = [];
        modal.find('[name="extensions"] option').each(function () {
            data.extensions.push($(this).val());
        });

        data.fromhour = parseInt(data.fromHour) * 60 + parseInt(data.fromMinute);
        data.tohour = parseInt(data.toHour) * 60 + parseInt(data.toMinute);

        delete data.fromHour;
        delete data.fromMinute;
        delete data.toHour;
        delete data.toMinute;
    };
</script>