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
           action="${pageContext.request.contextPath}/api/talk-reception-group/${entity == null ? null : entity.groupId}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">상담톡그룹[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">상담톡그룹명</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:input path="groupName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">사용자 리스트</label></div>
                <div class="eight wide column"><label class="control-label">추가된 사용자</label></div>
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
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">자동멘트</h4>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">초기 인사 멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <input type="text" placeholder="멘트내용입력">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">자동 종료 멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <input type="text" placeholder="멘트내용입력">
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="mt5">상담원이 메시지 송출  후
                        <select class="mr3 ml3">
                            <option>5</option>
                            <option>10</option>
                            <option>15</option>
                            <option>20</option>
                            <option>25</option>
                            <option>30</option>
                        </select>
                        분 동안 고객 답변이 없을 시 자동종료 안내
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">자동종료 안내 멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <input type="text" placeholder="멘트내용입력">
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="mt5">자동종료 안내 메시지 송출 후
                        <select class="mr3 ml3">
                            <option>5</option>
                            <option>10</option>
                            <option>15</option>
                            <option>20</option>
                            <option>25</option>
                            <option>30</option>
                        </select>
                        분 동안 고객 답변이 없을 시 자동종료 안내
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">비접수 초과시 멘트</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <input type="text" placeholder="멘트내용입력">
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="mt5">
                        상담원 비접수 건수가 <input type="text" size="2" class="ml3 mr3"> 건을 넘을 경우
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">추가 멘트1</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <input type="text" placeholder="멘트내용입력">
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="mt5">고객이 메시지 송출 후
                        <select class="mr3 ml3">
                            <option>5</option>
                            <option>10</option>
                            <option>15</option>
                            <option>20</option>
                            <option>25</option>
                            <option>30</option>
                        </select>
                        동안 상담원 답변이 없을 경우
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">추가 멘트2</label></div>
                <div class="twelve wide column">
                    <div class="ui form fluid">
                        <input type="text" placeholder="멘트내용입력">
                    </div>
                </div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="mt5">상담원이 메시지 송출 후
                        <select class="mr3 ml3">
                            <option>5</option>
                            <option>10</option>
                            <option>15</option>
                            <option>20</option>
                            <option>25</option>
                            <option>30</option>
                        </select>
                        동안 고객 답변이 없을 경우
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
        data.personIds = [];
        modal.find('[name="personIds"] option').each(function () {
            data.personIds.push($(this).val());
        });
    };
</script>
