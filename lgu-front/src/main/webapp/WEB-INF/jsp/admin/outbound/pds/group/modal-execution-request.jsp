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
<%--@elvariable id="commonFields" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.SummaryCommonFieldResponse>"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/pds-group/${entity.seq}/execute"
           data-before="prepareWriteForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">실행요청</div>

    <div class="scrolling content rows">
        <div class="ui grid">

            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">실행설정</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">실행명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="executeName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">실행할교환기</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="runHost">
                            <c:forEach var="e" items="${hosts}">
                                <form:option value="${e.key}" label="${e.value}(${e.key})" data-host="${e.key}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="eight wide column">(콜을 발신할 교환기)</div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">PDS항목설정</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">다이얼시간</label></div>
                <div class="twelve wide column">
                    <form:input path="dialTimeout" size="2" cssClass="-input-numerical"/> 초 (30초이상권장)
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">녹취</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="isRecord">
                            <form:option value="Y" label="녹취함"/>
                            <form:option value="N" label="녹취안함"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">콜시도할전화번호필드</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select name="numberField">
                            <c:choose>
                                <c:when test="${form.numberField eq ''}">
                                    <option value="ALL" selected>모든전화필드</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="ALL">모든전화필드</option>
                                    <c:forEach var="e" items="${commonFields}">
                                        <c:if test="${e.fieldId eq form.numberField}">
                                            <option value="${g.htmlQuote(e.fieldId)}"
                                                    selected>${g.htmlQuote(e.fieldInfo)}</option>
                                        </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">RID(발신번호) 설정</label></div>
                <div class="four wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="ridKind" value="CAMPAIGN"/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupRidKind', 'CAMPAIGN'))}</label>
                    </div>
                </div>
                <div class="eight wide column">
                    <div class="ui form ${form.ridKind eq 'CAMPAIGN' ? '' : 'disabled'}">
                        <form:select path="ridData">
                            <form:option value="" label="번호선택"/>
                            <form:options items="${rids}" itemValue="number" itemLabel="name" cssStyle="white-space: pre;"/>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="four wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="ridKind" value="FIELD"/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupRidKind', 'FIELD'))}</label>
                    </div>
                </div>
            </div>
            <div class="row blank">
                <div class="four wide column"><label class="control-label label-required">과금번호설정</label></div>
                <div class="four wide column" style="display: none;">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="billingKind" value="NUMBER"/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupBillingKind', 'NUMBER'))}</label>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form ${form.billingKind eq 'NUMBER' ? '' : 'disabled'}">
                        <select name="billingData_NUMBER">
                            <option value="">과금번호 선택</option>
                            <c:forEach var="e" items="${numbers}">
                                <option value="${g.htmlQuote(e.key)}" ${form.billingKind == 'NUMBER' && e.key == form.billingData ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
<%--                <div class="four wide column"></div>--%>
<%--                <div class="four wide column"></div>--%>
<%--                <div class="four wide column">--%>
<%--                    <div class="ui radio checkbox">--%>
<%--                        <form:radiobutton path="billingKind" value="DIRECT"/>--%>
<%--                        <label>${g.htmlQuote(g.messageOf('PDSGroupBillingKind', 'DIRECT'))}</label>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="four wide column">--%>
<%--                    <div class="ui input fluid ${form.billingKind eq 'DIRECT' ? '' : 'disabled'}">--%>
<%--                        <input type="text" name="billingData_DIRECT" class="-input-numerical" placeholder="과금번호 입력"--%>
<%--                               value="${form.billingKind == 'DIRECT' ? g.htmlQuote(form.billingData) : ''}">--%>
<%--                    </div>--%>
<%--                </div>--%>
            </div>
            <div class="row blank -pds-connect-kind-speed-field">
                <div class="four wide column"><label class="control-label label-required">속도</label></div>
                <c:choose>
                    <c:when test="${form.speedKind eq 'MEMBER'}">
                        <div class="four wide column -pds-connect-kind-speed" data-group="1">
                            <div class="ui radio checkbox">
                                <form:radiobutton path="speedKind" value="MEMBER"/>
                                <label>${g.htmlQuote(g.messageOf('PDSGroupSpeedKind', 'MEMBER'))}</label>
                            </div>
                        </div>
                        <div class="four wide column -pds-connect-kind-speed" data-group="1">
                            <div class="ui form">
                                <select name="speedData_MEMBER">
                                    <c:forEach var="e" items="${pdsGroupSpeedOptions}">
                                        <option value="${e.key}" ${form.speedKind == 'MEMBER' and form.speedData == e.key ? 'selected' : ''}>
                                                ${e.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="four wide column -pds-connect-kind-speed" data-group="2">
                            <div class="ui radio checkbox">
                                <form:radiobutton path="speedKind" value="CHANNEL"/>
                                <label>${g.htmlQuote(g.messageOf('PDSGroupSpeedKind', 'CHANNEL'))}</label>
                            </div>
                        </div>
                        <div class="four wide column -pds-connect-kind-speed" data-group="2">
                            <input type="text" size="2" name="speedData_CHANNEL" class="-input-numerical"
                                   value="${form.speedKind == 'CHANNEL' ? form.speedData : ''}"> 채널 (500미만)
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteForm = function (data) {
        data.billingData = data.billingKind === 'DIRECT' ? data.billingData_DIRECT : data.billingData_NUMBER;
        delete data.billingData_DIRECT;
        delete data.billingData_NUMBER;

        data.speedData = data.speedKind === 'MEMBER' ? data.speedData_MEMBER : data.speedData_CHANNEL;
        delete data.speedData_MEMBER;
        delete data.speedData_CHANNEL;

        delete data.connectData_MEMBER;
        delete data.connectData_PDS_IVR;
        delete data.connectData_ARS_RSCH;
    };

    modal.find('[name=ridKind]').change(function () {
        const ridKind = modal.find('[name=ridKind]:checked').val();
        const ridDate = modal.find('#ridData');
        ridDate.val('');

        if (ridKind === 'CAMPAIGN') {
            ridDate.parent().removeClass('disabled');
        } else {
            ridDate.parent().addClass('disabled');
        }
    });

    modal.find('[name=billingKind]').change(function () {
        const billingKind = modal.find('[name=billingKind]:checked').val();
        const billingDataNUMBER = modal.find('[name=billingData_NUMBER]');
        const billingDataDIRECT = modal.find('[name=billingData_DIRECT]');
        billingDataNUMBER.val('');
        billingDataDIRECT.val('');

        if (billingKind === 'NUMBER') {
            billingDataNUMBER.parent().removeClass('disabled');
            billingDataDIRECT.parent().addClass('disabled');
        } else {
            billingDataNUMBER.parent().addClass('disabled');
            billingDataDIRECT.parent().removeClass('disabled');
        }
    });
</script>
