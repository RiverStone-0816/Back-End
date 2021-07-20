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

<form:form modelAttribute="forms" cssClass="ui modal large -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/extension-extra-info/"
           data-before="prepareWriteForm" data-done="reload">

    <i class="close icon"></i>
    <div class="header">내선기타정보수정</div>

    <div class="scrolling content rows">
        <table class="ui celled table compact unstackable">
            <thead>
            <tr>
                <th><input type="checkbox" id="check-all"/></th>
                <th>내선번호</th>
                <th>지역번호</th>
                <th>CID</th>
                <th>과금번호</th>
                <th>CRM최초연결상태</th>
                <th>CRM연결중전화끊김후자동상태</th>
                <th>CRM비연결시 상태</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${forms.cidInfos != null && forms.cidInfos.size() > 0}">
                    <c:forEach var="form" items="${forms.cidInfos}" varStatus="status">
                        <tr>
                            <td>
                                <form:hidden path="cidInfos[${status.index}].peer"/>
                                <input type="checkbox" value="${peers.get(status.index)}"/>
                            </td>
                            <td>${g.htmlQuote(peerToExtensions.get(form.peer))}</td>
                            <td>
                                <div class="ui form">
                                    <form:input path="cidInfos[${status.index}].localPrefix"/>
                                </div>
                            </td>
                            <td>
                                <div class="ui form">
                                    <div class="fields">
                                        <div class="field">
                                            <form:select path="cidInfos[${status.index}].cid">
                                                <form:option value="${g.htmlQuote(form.billingNumber)}" label="${g.htmlQuote(form.billingNumber)}"/>
                                                <c:forEach var="e" items="${cidInfos}">
                                                    <form:option value="${e}" label="${e}"/>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td>${g.htmlQuote(form.billingNumber)}</td>
                            <td>
                                <div class="ui form">
                                    <div class="inline fields">
                                        <c:forEach var="type" items="${firstStatusTypes}">
                                            <div class="field">
                                                <div class="ui radio checkbox checked">
                                                    <form:radiobutton path="cidInfos[${status.index}].firstStatus" value="${type.key}" class="hidden"/>
                                                    <label>${type.value}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="ui form">
                                    <div class="inline fields">
                                        <c:forEach var="type" items="${dialStatusTypes}">
                                            <div class="field">
                                                <div class="ui radio checkbox checked">
                                                    <form:radiobutton path="cidInfos[${status.index}].dialStatus" value="${type.key}" class="hidden"/>
                                                    <label>${type.value}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="ui form">
                                    <div class="inline fields">
                                        <c:forEach var="type" items="${logoutStatusTypes}">
                                            <div class="field">
                                                <div class="ui radio checkbox checked">
                                                    <form:radiobutton path="cidInfos[${status.index}].logoutStatus" value="${type.key}" class="hidden"/>
                                                    <label>${type.value}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" class="null-data">조회된 데이터가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <div class="actions">
        <button type="button" class="ui button left floated" onclick="popupUpdateCidModal()">CID일괄적용</button>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteForm = function (data) {
        data.cidInfos = values(data.cidInfos);
    };

    window.popupUpdateCidModal = function () {
        const peers = [];
        modal.find('input[type=checkbox]:checked').map(function () {
            peers.push($(this).val());
        });
        if(peers.length > 0) {
            popupReceivedHtml($.addQueryString('/admin/service/etc/extension-extra-info/modal-update-cid', {peers: peers}), 'modal-update-cid');
        }else{
            alert("CID일괄적용 할 내선번호를 선택하세요.");
        }
    };

    $('#check-all').click(function () {
        if ($('#check-all').is(':checked')) {
            console.log('true');
            $('input[type=checkbox]').prop('checked', true);
        }
        else {
            console.log('false');
            $('input[type=checkbox]').prop('checked', false);
        }
    })
</script>
