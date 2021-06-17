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
           action="${pageContext.request.contextPath}/api/talk-schedule-group/item/${entity == null ? null : entity.child}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">스케쥴[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <form:hidden path="parent"/>
            <div class="row">
                <div class="four wide column"><label class="control-label">시간</label></div>
                <div class="twelve wide column">
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
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">스케쥴유형선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="A" class="hidden"/>
                                    <label>자동멘트전송</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="kind" value="G" class="hidden"/>
                                    <label>서비스별그룹연결</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="A">
                <div class="four wide column"><label class="control-label">멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="kindData" items="${talkMentsOfStringSeq}"/>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="G">
                <div class="four wide column"><label class="control-label">첫인사멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <form:select path="firstMentId">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${talkMentsOfStringSeq}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="G">
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        비접수 <form:input path="limitNum" cssClass="-input-numerical" cssStyle="width: 50px;"/>개이상 초과시 자동멘트 송출(0개는 무한대)
                    </div>
                </div>
            </div>
            <div class="row -kind-data" data-kind="G">
                <div class="four wide column"><label class="control-label">비접수초과시멘트</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="limitMentId">
                            <form:option value="" label="선택안함"/>
                            <form:options items="${talkMentsOfStringSeq}"/>
                        </form:select>
                    </div>
                </div>
                <div class="eight wide column">(비접수초과 0이상시 필수)</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">통계적용여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="statYn" value="Y" class="hidden" />
                                    <label>적용</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="statYn" value="N" class="hidden"/>
                                    <label>비적용</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">업무시간사용여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="worktimeYn" value="Y" class="hidden"/>
                                    <label>적용</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="worktimeYn" value="N" class="hidden"/>
                                    <label>비적용</label>
                                </div>
                            </div>
                        </div>
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
    window.prepareWriteFormData = function (data) {
        data.fromhour = parseInt(data.fromHour) * 60 + parseInt(data.fromMinute);
        data.tohour = parseInt(data.toHour) * 60 + parseInt(data.toMinute);

        delete data.fromHour;
        delete data.fromMinute;
        delete data.toHour;
        delete data.toMinute;
    };

    modal.find('[name=kind]').change(function () {
        const kind = modal.find('[name=kind]:checked').val();
        modal.find('.-kind-data').hide().filter(function () {
            return $(this).attr('data-kind') === kind;
        }).show();
    }).change();
</script>
