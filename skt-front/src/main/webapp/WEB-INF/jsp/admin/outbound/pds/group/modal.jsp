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
<%--@elvariable id="queues" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.SummaryPDSQueueNameResponse>"--%>
<%--@elvariable id="commonFields" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.SummaryCommonFieldResponse>"--%>
<%--@elvariable id="researchList" type="java.util.Map<kr.co.eicn.ippbx.model.dto.eicn.SummaryResearchListResponse>"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/pds-group/${entity == null ? null : entity.seq}"
           data-before="prepareWriteForm" data-done="reload">
    <form:hidden path="billingKind"/>
    <i class="close icon"></i>
    <div class="header">${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'}그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">그룹명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">업로드유형</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="pdsType">
                            <form:option value="" label="선택" />
                            <c:forEach var="e" items="${pdsTypes}">
                                <form:option value="${e.key}" label="${e.value}" />
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">실행할교환기</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="runHost">
                            <form:option value="" label="선택"/>
                            <c:if test="${hosts.size() > 0}">
                                <c:forEach var="e" items="${hosts}">
                                    <form:option value="${e.key}" label="${e.value}(${e.key})" data-host="${e.key}"/>
                                </c:forEach>
                            </c:if>
                        </form:select>
                    </div>
                </div>
                <div class="eight wide column">(콜을 아웃바운드 시킬 교환기)</div>
            </div>
            <div class="row blank">
                <div class="four wide column"><label class="control-label">연결대상</label></div>
                <c:if test="${serviceKind.equals('SC')}">
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <form:radiobutton path="connectKind" value="MEMBER" data-group="1"/>
                            <label>상담원그룹</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui form">
                            <select name="connectData_MEMBER">
                                <option value="">그룹선택</option>
                                <c:forEach var="e" items="${queues}">
                                    <option value="${g.htmlQuote(e.name)}" ${form.connectKind == 'MEMBER' && e.name eq form.connectData ? 'selected' : ''} data-host="${g.htmlQuote(e.host)}">${g.htmlQuote(e.hanName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="six wide column">(실행할 교환기 선택 후 사용가능)</div>
                    <div class="four wide column"></div>
                </c:if>
<%--                <div class="four wide column"></div>--%>
<%--                <div class="three wide column">--%>
<%--                    <div class="ui radio checkbox">--%>
<%--                        <form:radiobutton path="connectKind" value="PDS_IVR" data-group="2"/>--%>
<%--                        <label>Auto IVR</label>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="three wide column">--%>
<%--                    <div class="ui form">--%>
<%--                        <select name="connectData_PDS_IVR">--%>
<%--                            <option value="">IVR 선택</option>--%>
<%--                            <c:forEach var="e" items="${ivrs}">--%>
<%--                                <option value="${g.htmlQuote(e.key)}" ${form.connectKind == 'PDS_IVR' && e.key == form.connectData ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>--%>
<%--                            </c:forEach>--%>
<%--                        </select>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="four wide column"></div>--%>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="connectKind" value="ARS_RSCH" data-group="2"/>
                        <label>ACS(ARS설문)</label>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui form">
                        <select name="connectData_RESEARCH">
                            <option value="">설문선택</option>
                            <c:forEach var="e" items="${researchList}">
                                <option value="${e.key}" ${form.connectKind eq 'ARS_RSCH' && e.key == form.connectData ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서조회</label></div>
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
                                    <span class="section">부서를 선택해 주세요.</span>
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
                <div class="four wide column"><label class="control-label">추가정보</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="info"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">다이얼시간</label></div>
                <div class="twelve wide column">
                    <form:input path="dialTimeout" size="2" value="30" cssClass="-input-numerical"/> 초 (30초이상권장)
                </div>
            </div>
            <c:if test="${serviceKind.equals('SC')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label label-required">녹취</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <form:select path="isRecord">
                                <form:option value="Y" label="녹취함"/>
                                <form:option value="N" label="녹취안함"/>
                            </form:select>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">실행할 전화번호필드</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="numberField">
							<form:option value="ALL" label="모든번호필드" />
                            <c:if test="${commonFields.size() > 0}">
                                <c:forEach var="e" items="${commonFields}">
                                    <form:option value="${e.fieldId}" label="${e.fieldInfo}" data-type="${e.type}"/>
                                </c:forEach>
                            </c:if>
                        </form:select>
                    </div>
                </div>
                <div class="eight wide column">(기본항목 설정의 업로드유형을 선택한 후 사용 가능)</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">발신표시번호 설정</label></div>
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
                <div class="four wide column"><label class="control-label">속도</label></div>
                <div class="three wide column -pds-connect-kind-speed" data-group="1">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="speedKind" value="MEMBER"/>
                        <label>대기중상담원기준</label>
                    </div>
                </div>
                <div class="three wide column -pds-connect-kind-speed" data-group="1">
                    <div class="ui form">
                        <select name="speedData_MEMBER">
                            <c:forEach var="e" begin="10" end="55" step="5">
                                <option value="${e}" ${form.speedKind == 'MEMBER' && form.speedData == e ? 'selected' : ''}>${e/10}배수</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="three wide column -pds-connect-kind-speed" data-group="2">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="speedKind" value="CHANNEL"/>
                        <label>동시통화기준</label>
                    </div>
                </div>
                <div class="three wide column -pds-connect-kind-speed" data-group="2">
                    <input type="text" size="2" name="speedData_CHANNEL" value="${form.speedKind == 'CHANNEL' ? form.speedData : ''}"> 채널(500미만)
                </div>
            </div>
            <div class="row blank -connect-kind-update-data" data-value="Y">
                <div class="four wide column"><label class="control-label">상담결과(상담원화면)</label></div>
                <div class="six wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="resultKind" value="RSCH"/>
                        <label>없음 또는 고객사화면</label>
                    </div>
                </div>
                <div class="six wide column">
                </div>
                <div class="four wide column"></div>
                <div class="three wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="resultKind" value="RS"/>
                        <label>상담결과유형화면</label>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui form">
                        <form:select path="resultType">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${rsTypes}"/>
                        </form:select>
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
    window.prepareWriteForm = function (data) {
        data.speedData = data.speedKind === 'MEMBER' ? data.speedData_MEMBER : data.speedData_CHANNEL;

        delete data.speedData_MEMBER;
        delete data.speedData_CHANNEL;

        if (data.connectKind === 'MEMBER') {
            data.connectData = data.connectData_MEMBER;
        } else if (data.connectKind === 'PDS_IVR') {
            data.connectData = data.connectData_PDS_IVR;
        } else if (data.connectKind === 'ARS_RSCH') {
            data.connectData = data.connectData_RESEARCH;
        }

        delete data.connectData_MEMBER;
        delete data.connectData_PDS_IVR;
        delete data.connectData_RESEARCH;
    };

    modal.find('[name=runHost]').change(showHostBox).change();
    const selectedHostValue = modal.find('[name=runHost] option:selected').val();
    modal.find('[name=connectData_MEMBER]').find('option').filter(function() {return $(this).val() !== '';}).hide().each(function () {
        if (selectedHostValue === $(this).data('host'))
            $(this).show();
    });
    modal.find('[name=connectKind]').change(showConnectKindBox);
    showConnectKindBox();
    modal.find('[name=pdsType]').change(changeNumberField);
    const selectedValue = modal.find('[name=pdsType] option:selected').val();
    modal.find('[name=numberField]').find('option').filter(function() {return $(this).val() !== 'ALL';}).hide().each(function () {
        if (selectedValue == $(this).data('type')) // string == integer
            $(this).show();
    });

    function showHostBox() {
        const selectedValue = modal.find('[name=runHost] option:selected').val();
        modal.find('[name=connectData_MEMBER]').val('').find('option').filter(function() {return $(this).val() !== '';}).hide().each(function () {
            if (selectedValue === $(this).data('host'))
                $(this).show();
        });
    }

    function showConnectKindBox() {
        const ele = modal.find('[name=connectKind]:checked');
        const connectKind = ele.val();
        modal.find('.-connect-kind-update-data').hide().filter(function () {
            return connectKind === 'MEMBER';
        }).show();
        modal.find('.-pds-connect-kind-speed').hide().filter(function () {
            const v = ele.data('group');
            const kindValue = v === 1 ? 'MEMBER' : 'CHANNEL';
            modal.find('[name=speedKind][value=' + kindValue + ']').prop('checked', true);
            return v === $(this).data('group');
        }).show();
    }

    function changeNumberField() {
        const selectedValue = modal.find('[name=pdsType] option:selected').val();
        modal.find('[name=numberField]').val('ALL').find('option').filter(function() {return $(this).val() !== 'ALL';}).hide().each(function () {
            if (selectedValue == $(this).data('type')) // string == integer
                $(this).show();
        });
    }
</script>
