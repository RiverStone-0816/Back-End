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
                <div class="sixteen wide column">
                    <h4 class="ui header title">기본항목설정</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">그룹명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="name"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]"
                         data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${searchOrganizationNames != null and searchOrganizationNames.size() > 0}">
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
                <div class="four wide column"><label class="control-label label-required">PDS유형</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="pdsType">
                            <form:option value="" label="선택"/>
                            <c:forEach var="e" items="${pdsTypes}">
                                <form:option value="${e.key}" label="${e.value}"/>
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
                            <c:forEach var="e" items="${hosts}">
                                <form:option value="${e.key}" label="${e.value}(${e.key})" data-host="${e.key}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="eight wide column">(콜을 발신할 교환기)</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">연결구분</label></div>
                <div class="twelve wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="connectKind" value="PDS_IVR" data-group="1"/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupConnectKind', 'PDS_IVR'))}</label>
                    </div>
                    <c:if test="${serviceKind eq 'SC'}">
                        <div class="ui radio checkbox">
                            <form:radiobutton path="connectKind" value="MEMBER" data-group="1"/>
                            <label>${g.htmlQuote(g.messageOf('PDSGroupConnectKind', 'MEMBER'))}</label>
                        </div>
                    </c:if>
                    <c:if test="${usingServices.contains('RSH')}">
                        <div class="ui radio checkbox">
                            <form:radiobutton path="connectKind" value="ARS_RSCH" data-group="2"/>
                            <label>${g.htmlQuote(g.messageOf('PDSGroupConnectKind', 'ARS_RSCH'))}</label>
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="row -connect-data-field">
                <div class="four wide column"><label class="control-label label-required">연결대상</label></div>
                <div class="four wide column -connect-data" data-value="PDS_IVR">
                    <div class="ui form">
                        <select name="connectData_PDS_IVR">
                            <option value="">IVR 선택</option>
                            <c:forEach var="e" items="${ivrs}">
                                <option value="${g.htmlQuote(e.key)}" ${form.connectKind == 'PDS_IVR' and e.key == form.connectData ? 'selected' : ''}>
                                        ${g.htmlQuote(e.value)}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <c:if test="${serviceKind eq 'SC'}">
                    <div class="four wide column -connect-data" data-value="MEMBER">
                        <div class="ui form">
                            <select name="connectData_MEMBER">
                                <option value="">그룹선택</option>
                                <c:forEach var="e" items="${queues}">
                                    <option value="${g.htmlQuote(e.name)}" data-host="${g.htmlQuote(e.host)}"
                                        ${form.connectKind == 'MEMBER' and e.name eq form.connectData ? 'selected' : ''}>
                                            ${g.htmlQuote(e.hanName)}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="eight wide column -connect-data" data-value="MEMBER">
                        (실행할 교환기 선택 후 설정 가능)
                    </div>
                </c:if>
                <c:if test="${usingServices.contains('RSH')}">
                    <div class="four wide column -connect-data" data-value="ARS_RSCH">
                        <div class="ui form">
                            <select name="connectData_ARS_RSCH">
                                <option value="">설문선택</option>
                                <c:forEach var="e" items="${researchList}">
                                    <option value="${e.key}" ${form.connectKind eq 'ARS_RSCH' and e.key == form.connectData ? 'selected' : ''}>
                                            ${g.htmlQuote(e.value)}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </c:if>
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
                <div class="sixteen wide column">
                    <h4 class="ui header title">PDS항목설정</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">다이얼시간</label></div>
                <div class="twelve wide column">
                    <form:input path="dialTimeout" size="2" cssClass="-input-numerical"/> 초 (30초이상권장)
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
                <div class="four wide column"><label class="control-label label-required">콜시도할전화번호필드</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select id="numberField" name="numberField">
                            <option value="ALL">모든번호필드</option>
                            <c:forEach var="e" items="${commonFields}">
                                <option value="${e.fieldId}" data-type="${e.type}"
                                    ${e.type == form.pdsType and e.fieldId eq form.numberField ? 'selected' : ''}>
                                        ${e.fieldInfo}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="eight wide column">(기본항목설정의 PDS유형을 선택 후 설정 가능)</div>
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
                            <form:option value="" label="RID(발신번호) 선택"/>
                            <form:options items="${rids}" itemValue="number" itemLabel="name"/>
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
                    <div class="ui radio checkbox checked">
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
                <div class="four wide column -pds-connect-kind-speed" data-group="2">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="speedKind" value="CHANNEL"/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupSpeedKind', 'CHANNEL'))}</label>
                    </div>
                </div>
                <div class="two wide column -pds-connect-kind-speed" data-group="2">
                    <div class="ui input form fluid">
                        <input type="number" name="speedData_CHANNEL"
                               min="1" max="500" placeholder="1~500" title="1~500"
                               value="${form.speedKind == 'CHANNEL' ? form.speedData : ''}">
                    </div>
                </div>
                <div class="four wide column remove-padding">채널 (500이하)</div>
            </div>
            <div class="row blank -connect-kind-update-data" data-value="Y">
                <div class="four wide column"><label class="control-label label-required">상담결과화면</label></div>
                <div class="twelve wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="resultKind" value=""/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupResultKind', ''))}</label>
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="four wide column">
                    <div class="ui radio checkbox">
                        <form:radiobutton path="resultKind" value="RS"/>
                        <label>${g.htmlQuote(g.messageOf('PDSGroupResultKind', 'RS'))}</label>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form ${form.resultKind eq 'RS' ? '' : 'disabled'}">
                        <form:select path="resultType">
                            <form:option value="" label="상담결과유형선택"/>
                            <form:options items="${rsTypes}"/>
                        </form:select>
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
    window.prepareWriteForm = function (data) {
        data.billingData = data.billingKind === 'DIRECT' ? data.billingData_DIRECT : data.billingData_NUMBER;
        delete data.billingData_DIRECT;
        delete data.billingData_NUMBER;

        data.speedData = data.speedKind === 'MEMBER' ? data.speedData_MEMBER : data.speedData_CHANNEL;
        delete data.speedData_MEMBER;
        delete data.speedData_CHANNEL;

        if (data.connectKind === 'MEMBER')
            data.connectData = data.connectData_MEMBER;
        else if (data.connectKind === 'PDS_IVR')
            data.connectData = data.connectData_PDS_IVR;
        else if (data.connectKind === 'ARS_RSCH')
            data.connectData = data.connectData_ARS_RSCH;

        delete data.connectData_MEMBER;
        delete data.connectData_PDS_IVR;
        delete data.connectData_ARS_RSCH;
    };

    modal.find('[name=runHost]').change(showHostBox);

    modal.find('[name=connectData_MEMBER]').find('option').filter(function () {
        return $(this).val() !== '';
    }).hide().each(function () {
        const selectedHostValue = modal.find('[name=runHost] option:selected').val();
        if (selectedHostValue === $(this).data('host'))
            $(this).show();
    });

    modal.find('[name=connectKind]').change(showConnectKindBox).change();

    modal.find('[name=pdsType]').change(changeNumberField);

    modal.find('[name=numberField]').find('option')
        .filter(function () {
            return $(this).val() !== 'ALL';
        }).hide().each(function () {
        const selectedValue = modal.find('[name=pdsType] option:selected').val();
        if (selectedValue === $(this).data('type').toString())
            $(this).show();
    });

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

    modal.find('[name=resultKind]').change(function () {
        const resultKind = modal.find('[name=resultKind]:checked').val();
        const resultType = modal.find('#resultType');
        resultType.val('');

        if (resultKind === 'RS') {
            resultType.parent().removeClass('disabled');
        } else {
            resultType.parent().addClass('disabled');
        }
    });

    function showHostBox() {
        const selectedValue = modal.find('[name=runHost] option:selected').val();

        modal.find('[name=connectData_MEMBER]').val('').find('option').filter(function () {
            return $(this).val() !== '';
        }).hide().each(function () {
            if (selectedValue === $(this).data('host'))
                $(this).show();
        });
    }

    function showConnectKindBox() {
        const ele = modal.find('[name=connectKind]:checked');
        const connectKind = ele.val();

        const connectDataField = modal.find('.-connect-data-field');
        const speedField = modal.find('.-pds-connect-kind-speed-field');

        if (!connectKind) {
            connectDataField.hide();
            speedField.hide();
            return;
        }

        connectDataField.show();
        connectDataField.find('.-connect-data').hide().filter(function () {
            return connectKind === $(this).data('value');
        }).show();

        speedField.show();
        speedField.find('.-pds-connect-kind-speed').hide().filter(function () {
            const v = ele.data('group');
            const kindValue = v === 1 ? 'MEMBER' : 'CHANNEL';
            modal.find('[name=speedKind][value=' + kindValue + ']').prop('checked', true);

            return v === $(this).data('group');
        }).show();
    }

    function changeNumberField() {
        const selectedValue = modal.find('[name=pdsType] option:selected').val();

        modal.find('[name=numberField]').val('ALL').find('option').filter(function () {
            return $(this).val() !== 'ALL';
        }).hide().each(function () {
            if (selectedValue === $(this).data('type').toString())
                $(this).show();
        });
    }
</script>
