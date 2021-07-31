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
                        <form:select path="runHost" items="${hosts}"/>
                    </div>
                </div>
                <div class="eight wide column">(콜을 아웃바운드 시킬 교환기)</div>
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
                <div class="four wide column"><label class="control-label">실행할 전화번호필드</label></div>
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
                                            <option value="${g.htmlQuote(e.fieldId)}" selected>${g.htmlQuote(e.fieldInfo)}</option>
                                        </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                </div>
                <div class="eight wide column">(기본항목 설정의 업로드유형을 선택한 후 사용 가능)</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">발신표시번호 설정</label></div>

                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="ridKind" value="CAMPAIGN"/>
                        <label>그룹별RID지정</label>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui form">
                        <form:input path="ridData"/>
                    </div>
                </div>
                <div class="six wide column"></div>
                <div class="four wide column"></div>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="ridKind" value="FIELD"/>
                        <label>업로드RID지정</label>
                    </div>
                </div>
            </div>
            <div class="row blank">
                <div class="four wide column"><label class="control-label">과금번호설정</label></div>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="billingKind" value="NUMBER"/>
                        <label>그룹별번호</label>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui form">
                        <select name="billingData_NUMBER">
							<option value="">선택</option>
                            <c:forEach var="e" items="${numbers}">
                                <option value="${g.htmlQuote(e.key)}" ${form.billingKind == 'NUMBER' && e.key == form.billingData ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="six wide column"></div>
                <div class="four wide column"></div>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="billingKind" value="DIRECT"/>
                        <label>그룹별직접입력</label>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui input fluid">
                        <input type="text" name="billingData_DIRECT" value="${form.billingKind == 'DIRECT' ? g.htmlQuote(form.billingData) : ''}">
                    </div>
                </div>
            </div>
            <div class="row blank">
                <div class="four wide column"><label class="control-label">속도</label></div>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="speedKind" value="MEMBER"/>
                        <label>대기중상담원기준</label>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui form">
                        <select name="speedData_MEMBER">
                            <c:forEach var="e" begin="10" end="55" step="5">
                                <option value="${e}" ${form.speedKind == 'MEMBER' && form.speedData == e ? 'selected' : ''}>${e/10}배수</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="six wide column"></div>
                <div class="four wide column"></div>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="speedKind" value="CHANNEL"/>
                        <label>동시통화기준</label>
                    </div>
                </div>
                <div class="three wide column">
                    <input type="text" size="2" name="speedData_CHANNEL" value="${form.speedKind == 'CHANNEL' ? form.speedData : ''}"> 채널(500미만)
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
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
    };
</script>
