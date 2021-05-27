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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/record-file-enc/key/${entity == null ? null : entity.id}"
           data-before="prepareEncKeyForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">암호키[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">암호키 적용시간</label></div>
                <div class="twelve wide column">
                    <div class="date-picker">
                        <div class="dp-wrap">
                            <fmt:formatDate var="date" value="${entity != null ? entity.createTime : null}" pattern="yyyy-MM-dd"/>
                            <fmt:formatDate var="hour" value="${entity != null ? entity.createTime : null}" pattern="HH"/>
                            <fmt:formatDate var="minute" value="${entity != null ? entity.createTime : null}" pattern="mm"/>

                            <label for="recording-security-reserv" style="display:none">From</label>
                            <input type="text" name="date" id="recording-security-reserv" placeholder="날짜선택" class="-datepicker default-input-size" value="${date}"/>
                            <select class="input-small-size" name="hour">
                                <c:forEach var="e" begin="0" end="23">
                                    <fmt:formatNumber var="value" value="${e}" pattern="00"/>
                                    <option value="${value}" ${value == hour ? 'selected' : null}>${value}</option>
                                </c:forEach>
                            </select>
                            <select class="input-small-size" name="minute">
                                <c:forEach var="e" begin="0" end="59">
                                    <fmt:formatNumber var="value" value="${e}" pattern="00"/>
                                    <option value="${value}" ${value == minute ? 'selected' : null}>${value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">암호키</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid"><form:input path="encKey"/></div>
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
    window.prepareEncKeyForm = function (data) {
        data.applyDate = modal.find('[name=date]').val() + ' ' + modal.find('[name=hour]').val() + ':' + modal.find('[name=minute]').val() + ':00';
        delete data.date;
        delete data.hour;
        delete data.minute;
    };
</script>
