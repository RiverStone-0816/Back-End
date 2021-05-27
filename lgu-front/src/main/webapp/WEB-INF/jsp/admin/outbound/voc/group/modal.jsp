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
           action="${pageContext.request.contextPath}/api/voc-group/${entity == null ? null : entity.seq}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">VOC그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">VOC/해피콜명</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:input path="vocGroupName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">진행여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${processKinds}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="processKind" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -term-input">
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="date-picker from-to">
                            <div class="dp-wrap">
                                <label class="control-label" for="startTerm" style="display:none">From</label>
                                <form:input path="startTerm" cssClass="-datepicker" placeholder="시작일"/>
                            </div>
                            <span class="tilde">~</span>
                            <div class="dp-wrap">
                                <label class="control-label" for="endTerm" style="display:none">to</label>
                                <form:input path="endTerm" cssClass="-datepicker" placeholder="종료일"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">진행종류</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${isArsSmsOptions}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isArsSms" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">진행자</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${vocGroupSenderOptions}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="sender" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                             </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -ars-input">
                <div class="four wide column"><label class="control-label">설문선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="arsResearchId">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${researches}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row -ars-auto-input">
                <div class="four wide column"><label class="control-label">발신콜조건</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${outboundTargetOptions}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="outboundTarget" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -outbound-target-input" data-value="ALL,MEMBER,CIDNUM">
                <div class="four wide column"><label class="control-label">발신콜상태</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${callKinds}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="outboundCallKind" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -outbound-target-input" data-value="CIDNUM">
                <div class="four wide column"><label class="control-label">발신콜발신번호선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="outboundTargetCidnum" items="${cids}"/>
                    </div>
                </div>
            </div>

            <div class="row -outbound-target-input" data-value="MEMBER">
                <div class="four wide column"><label class="control-label">발신콜 가능 상담원</label></div>
                <div class="six wide column" style="text-align: center;">추가가능 상담원</div>
                <div class="six wide column" style="text-align: center;">추가된 상담원</div>
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
                            <select name="outboundMemberList" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:if test="${entity.outboundMemberList != null}">
                                    <c:forEach var="e" items="${entity.outboundMemberList}">
                                        <option value="${g.htmlQuote(e)}">${g.htmlQuote(e)} (${g.htmlQuote(persons.get(e))})</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row -ars-auto-input">
                <div class="four wide column"><label class="control-label">수신콜조건</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${inboundTargetOptions}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="inboundTarget" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -inbound-target-input" data-value="ALL,MEMBER,SVCNUM">
                <div class="four wide column"><label class="control-label">수신콜상태</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${callKinds}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="inboundCallKind" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -inbound-target-input" data-value="SVCNUM">
                <div class="four wide column"><label class="control-label">수신콜대표번호선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="inboundTargetSvcnum" items="${cids}"/>
                    </div>
                </div>
            </div>

            <div class="row -inbound-target-input" data-value="MEMBER">
                <div class="four wide column"><label class="control-label">수신콜 가능 상담원</label></div>
                <div class="six wide column" style="text-align: center;">추가가능 상담원</div>
                <div class="six wide column" style="text-align: center;">추가된 상담원</div>
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
                            <select name="inboundMemberList" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:if test="${entity.inboundMemberList != null}">
                                    <c:forEach var="e" items="${entity.inboundMemberList}">
                                        <option value="${g.htmlQuote(e)}">${g.htmlQuote(e)} (${g.htmlQuote(persons.get(e))})</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="four wide column"><label class="control-label">추가정보</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:textarea path="information" rows="3"/>
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
        data.outboundMemberList = [];
        modal.find('[name="outboundMemberList"] option').each(function () {
            data.outboundMemberList.push($(this).val());
        });

        data.inboundMemberList = [];
        modal.find('[name="inboundMemberList"] option').each(function () {
            data.inboundMemberList.push($(this).val());
        });
    };

    modal.find('[name=processKind]').change(function () {
        const value = modal.find('[name=processKind]:checked').val();
        const input = modal.find('.-term-input');

        if (value === 'T')
            input.show();
        else
            input.hide();
    }).change();

    modal.find('[name=isArsSms]').change(function () {
        const isArsSms = modal.find('[name=isArsSms]:checked').val();
        const sender = modal.find('[name=sender]:checked').val();
        const arsInput = modal.find('.-ars-input');
        const input = modal.find('.-ars-input,.-ars-auto-input,.-outbound-target-input,.-inbound-target-input');

        if (isArsSms === 'ARS') {
            if (sender === 'AUTO')
                input.show();
            if (sender === 'MEMBER')
                arsInput.show();
        }
        else
            input.hide();
    }).change();

    modal.find('[name=sender]').change(function () {
        const isArsSms = modal.find('[name=isArsSms]:checked').val();
        const sender = modal.find('[name=sender]:checked').val();
        const input = modal.find('.-ars-auto-input,.-outbound-target-input,.-inbound-target-input');

        if (isArsSms === 'ARS' && sender === 'AUTO')
            input.show();
        else
            input.hide();
    }).change();

    modal.find('[name=outboundTarget]').change(function () {
        const value = modal.find('[name=outboundTarget]:checked').val();
        modal.find('.-outbound-target-input').each(function () {
            const contains = $(this).attr('data-value').split(',').indexOf(value) >= 0;
            if (contains)
                $(this).show();
            else
                $(this).hide();
        });
    }).change();

    modal.find('[name=inboundTarget]').change(function () {
        const value = modal.find('[name=inboundTarget]:checked').val();
        modal.find('.-inbound-target-input').each(function () {
            const contains = $(this).attr('data-value').split(',').indexOf(value) >= 0;
            if (contains)
                $(this).show();
            else
                $(this).hide();
        });
    }).change();

</script>
