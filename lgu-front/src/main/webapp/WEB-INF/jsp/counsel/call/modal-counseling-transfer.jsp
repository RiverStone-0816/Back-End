<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<div class="ui modal inverted tiny" id="modal-consulting-move-popup">
    <i class="close icon"></i>
    <div class="header">상담이관</div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <%--<div class="row">
                <div class="four wide column"><label class="control-label">이관대상</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select>
                            <option>개인</option>
                            <option>그룹</option>
                        </select>
                    </div>
                </div>
            </div>--%>
            <div class="row">
                <div class="four wide column"><label class="control-label">담당지정</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select id="transferred-user">
                            <option value="">담당자선택</option>
                            <c:forEach var="e" items="${users}">
                                <c:if test="${user.id != e.key}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close" onclick="clearTransferredUser()">취소</button>
        <button type="button" class="ui blue button modal-close">확인</button>
    </div>
</div>

<tags:scripts>
    <script>
        function popupTransferModal() {
            $('#modal-consulting-move-popup').dragModalShow()
        }

        function clearTransferredUser() {
            $('#transferred-user').val('');
        }

        function getTransferredUser() {
            return $('#transferred-user').val();
        }
    </script>
</tags:scripts>
