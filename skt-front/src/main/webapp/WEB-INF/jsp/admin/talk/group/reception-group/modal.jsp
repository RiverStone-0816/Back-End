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
<%--@elvariable id="version" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/talk-reception-group/${entity == null ? null : entity.groupId}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">채팅상담그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">채팅상담그룹명(*)</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:input path="groupName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">비접수설정</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:input path="unassignCnt" cssClass="-input-numerical" placeholder="숫자를 입력하세요."/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">비접수멘트</label></div>
                <div class="eight wide column">
                    <div class="ui form">
                        <form:input path="unassignMent" placeholder="[주의]상담사 답변 후 답변하지 않을때 마지막 상담사 답변기준임."/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">상담사무응답처리</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="memberUnanswerMin">
                            <form:option value="0" label="사용하지않음"/>
                            <form:option value="5" label="5"/>
                            <form:option value="10" label="10"/>
                            <form:option value="15" label="15"/>
                            <form:option value="20" label="20"/>
                            <form:option value="25" label="25"/>
                            <form:option value="30" label="30"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">상담사무응답멘트</label></div>
                <div class="eight wide column">
                    <div class="ui form">
                        <form:input path="memberUnanswerMent" placeholder="[주의]고객 답변 후 상담사가 답변하지 않을때 마지막 고객 답변기준 1회."/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="four wide column"><label class="control-label">자동종료안내시간</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="autoWarnMin">
                            <form:option value="0" label="사용하지않음"/>
                            <form:option value="5" label="5"/>
                            <form:option value="10" label="10"/>
                            <form:option value="15" label="15"/>
                            <form:option value="20" label="20"/>
                            <form:option value="25" label="25"/>
                            <form:option value="30" label="30"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">자동종료안내멘트</label></div>
                <div class="eight wide column">
                    <div class="ui form">
                        <form:input path="autoWarnMent" placeholder="[주의]상담사 답변 후 답변하지 않을때 마지막 상담사 답변기준임."/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">자동종료시간</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="autoExpireMin">
                            <form:option value="0" label="사용하지않음"/>
                            <form:option value="5" label="5"/>
                            <form:option value="10" label="10"/>
                            <form:option value="15" label="15"/>
                            <form:option value="20" label="20"/>
                            <form:option value="25" label="25"/>
                            <form:option value="30" label="30"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">자동종료멘트</label></div>
                <div class="eight wide column">
                    <div class="ui form">
                        <form:input path="autoExpireMent" placeholder="[주의]자동종료안내를 마친 후 몇분 후 자동종료 할것인지 설정."/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">분배정책</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="talkStrategy">
                            <c:forEach var="e" items="${talkStrategy}">
                                <form:option value="${e.key}" label="${e.value}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">사용자 리스트</label></div>
                <div class="eight wide column"><label class="control-label">채팅상담그룹 사용자</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.key)} (${g.htmlQuote(e.value)})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="personIds" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.persons}">
                                    <option value="${g.htmlQuote(e.id)}">${g.htmlQuote(e.id)} (${g.htmlQuote(e.idName)})</option>
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
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.personIds = [];
        modal.find('[name="personIds"] option').each(function () {
            data.personIds.push($(this).val());
        });
    };
</script>
