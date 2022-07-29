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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/extension/${entity != null ? g.htmlQuote(entity.peer) : null}"
           data-before="prepareWriteFormData" data-done="reload">
    <input type="hidden" name="type" value="${serviceKind}">
    <c:if test="${entity != null}">
        <input type="hidden" name="billingNumber" value="${entity.billingNumber}">
    </c:if>
    <i class="close icon"></i>
    <div class="header">내선[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows scrolling">
        <c:if test="${entity != null}">
            <input type="hidden" name="oldPeer" value="${g.htmlQuote(form.peer)}"/>
        </c:if>
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">내선번호</label></div>
                <div class="four wide column">
                    <div class="ui action input fluid">
                        <form:input path="extension" readonly="${entity != null ? 'true' : 'false'}"/>
                        <button class="ui button" type="button" onclick="checkDuplicatedExtension()">중복확인</button>
                    </div>
                </div>
                <div class="four wide column">
                    <span class="message text-green -express-usable-extension" style="display: none">사용가능</span>
                    <span class="message text-red -express-unusable-extension" style="display: none">사용불가</span>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">개인070번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="number">
                            <form:option value="" label="선택안함"/>
                            <c:if test="${form.number != null && !numbers.contains(form.number)}">
                                <form:option value="${form.number}" label="${form.number}"/>
                            </c:if>
                            <c:forEach var="e" items="${numbers}">
                                <form:option value="${e.number}" label="${e.number}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <c:if test="${g.usingServices.contains('SPHONE')}">
            <div class="row">
                <div class="four wide column"><label class="control-label">소프트폰사용여부</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:select path="softphone">
                            <form:option value="N" label="사용안함"/>
                            <form:option value="Y" label="사용함"/>
                        </form:select>&nbsp; (라이센스:${softPhoneLicenseInfo.currentLicence}/${softPhoneLicenseInfo.licence})
                    </div>
                </div>
                <div class="four wide column -soft-phone"><label class="control-label">소프트폰연결여부</label></div>
                <div class="four wide column -soft-phone">
                    <div class="ui input fluid">
                        <form:select path="phoneKind">
                            <form:option value="N" label="사용안함"/>
                            <form:option value="S" label="사용함"/>
                        </form:select>
                    </div>
                </div>
            </div>
            </c:if>
            <div class="row">
                <div class="four wide column"><label class="control-label">STT사용여부</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:select path="stt">
                            <form:option value="N" label="사용안함"/>
                            <form:option value="Y" label="사용함"/>
                        </form:select>&nbsp; (라이센스:${sttLicenseInfo.currentLicence}/${sttLicenseInfo.licence})
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">전화기아이디</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="peer" readonly="true" placeholder="개인070번호에 의해 결정됨"/></div>
                </div>
                <%--<div class="eight wide column">
                    (전화설정시 인증아이디)
                </div>--%>
                <div class="four wide column"><label class="control-label">게이트웨이</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="outboundGw">
                            <form:option value="" label="선택안함"/>
                            <c:forEach var="e" items="${gateways}">
                                <form:option value="${e.host}" label="${e.name} (${e.host})"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">전화기암호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:password path="passwd" placeholder="패스워드 변경시 입력"/></div>
                </div>
                <div class="four wide column"><label class="control-label">CID</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="cid">
                            <form:option value="" label="선택안함"/>
                            <c:if test="${entity != null and g.htmlQuote(entity.voipTel) != ''}">
                                <form:option value="${entity.voipTel}" label="${entity.voipTel}"/>
                            </c:if>
                            <c:if test="${entity != null and g.htmlQuote(entity.cid) != '' and !entity.cid.equals(entity.voipTel)}">
                                <form:option value="${entity.cid}" label="${entity.cid}"/>
                            </c:if>
                            <c:forEach var="e" items="${cids}">
                                <form:option value="${e.cidNumber}" label="${e.cidNumber}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">지역번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="localPrefix"/>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">번호이동원번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="originalNumber"/>
                    </div>
                </div>
            </div>
            <c:if test="${user.telco.equals('SKBB')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">과금번호</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <form:select path="billingNumber">
                                <form:option value="" label="선택안함"/>
                                <c:forEach var="e" items="${billingNumbers}">
                                    <form:option value="${e.number}" label="${e.number}"/>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                    <div class="eight wide column"><label class="control-label" style="color: red">* 선택하지 않는경우 개인 번호로 적용</label></div>
                </div>
            </c:if>
            <div class="row">
                <div class="four wide column"><label class="control-label">녹취여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="recordType" value="M"/>
                                    <label>녹취함</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="recordType" value="S"/>
                                    <label>녹취안함 (라이센스:${licenseInfo.currentLicence}/${licenseInfo.licence})</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">착신전환여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${forwardWhen}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="forwardWhen" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -forward-number">
                <div class="four wide column"><label class="control-label">착신번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="forwardKind">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${forwardKinds}"/>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form">
                        <select class="-forwarding-value" data-kind="E" name="forwardingE">
                            <c:forEach var="e" items="${extensions}">
                                <option value="${g.htmlQuote(e)}" ${form.forwardKind == 'E' && e == form.forwarding ? 'selected' : null}>${g.htmlQuote(e)}</option>
                            </c:forEach>
                        </select>
                        <select class="-forwarding-value" data-kind="R" name="forwardingR">
                            <c:forEach var="e" items="${serviceNumbers}">
                                <option value="${g.htmlQuote(e.number)}" ${form.forwardKind == 'R' && e.number == form.forwarding ? 'selected' : null}>${g.htmlQuote(e.hostName)}[${g.htmlQuote(e.number)}]</option>
                            </c:forEach>
                        </select>
                        <input class="-forwarding-value" data-kind="O" type="text" name="forwardingO" value="${form.forwardKind == 'O' ? g.htmlQuote(form.forwarding) : null}"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">금지프리픽스</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="prevent" placeholder="(예)국제전화:00, 060서비스:060 (여러개는 , 로 구분)"/></div>
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
    modal.find('[name="softphone"]').change(function () {
        const softPhone = modal.find('[name="softphone"]').val();
        if (softPhone === 'Y')
            modal.find('.-soft-phone').show();
        else {
            modal.find('[name="phoneKind"]').val('N').prop("selected",true);
            modal.find('.-soft-phone').hide();
        }
    }).change();

    modal.find('[name=forwardWhen]').change(function () {
        const forwardData = modal.find('[name=forwardWhen]:checked').val();
        const forwardNumber = modal.find('.-forward-number');

        if (forwardData === 'N')
            forwardNumber.hide();
        else
            forwardNumber.show();
    }).change();

    modal.find('[name=forwardKind]').change(function () {
        const kind = $(this).val();
        modal.find('.-forwarding-value').hide().filter(function () {
            return $(this).attr('data-kind') === kind;
        }).show();
    }).change();

    modal.find('[name=number]').change(function () {
        const e = event.target;
        const value = e.value;
        const cid = modal.find('[name=cid]');
        $('#peer').val(value.substring(3));

        cid.empty();
        cid.append($('<option/>', {value: '', text: '선택안함'}));

        if (!!value)
            cid.append($(e).find(':selected').clone());

        if (!!'${entity.cid}' && '${entity.cid}' !== value)
            cid.append($('<option/>', {value: '${entity.cid}', text: '${entity.cid}'}).prop('selected', true));

        <c:forEach var="e" items="${cids}">
            cid.append($('<option/>', {value: '${e.cidNumber}', text: '${e.cidNumber}'}));
        </c:forEach>
    });


    window.prepareWriteFormData = function (data) {
        if (data.forwardKind) {
            data.forwardNum = data['forwarding' + data.forwardKind];
            data.forwarding = data.forwardKind + '|' + data.forwardNum;
        }

        delete data.forwardingE;
        delete data.forwardingR;
        delete data.forwardingO;
    };

    window.checkDuplicatedExtension = function () {
        const extension = modal.find('[name="extension"]').val();
        if (extension)
            restSelf.get('/api/extension/exists/?extension=' + encodeURIComponent(extension)).done(function (response) {
                if (response.data) {
                    modal.find('.-express-usable-extension').hide();
                    modal.find('.-express-unusable-extension').show();
                } else {
                    modal.find('.-express-usable-extension').show();
                    modal.find('.-express-unusable-extension').hide();
                }
            });


    };
</script>
