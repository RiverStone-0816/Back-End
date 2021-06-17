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

<form:form modelAttribute="form" cssClass="ui modal tiny -json-submit" data-method="put"
           action="${pageContext.request.contextPath}/api/menu/${userId}/user-menu-sequence/${menu.menuCode != null ? menu.menuCode : ''}"
           data-before="prepareWriteFormData" data-done="doneUpdatingSequences">

    <i class="close icon"></i>
    <div class="header">순서변경</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="twelve wide column">
                    <div class="ui form">
                        <select multiple="multiple" name="menuCodes" class="one-multiselect">
                            <c:forEach var="e" items="${menu.children}">
                                <option value="${g.htmlQuote(e.menuCode)}">${g.htmlQuote(e.menuName)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-first">맨위로</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-prev">위로</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-next">아래로</button>
                        </div>
                        <div class="field">
                            <button type="button" class="fluid ui button basic grey -to-last">맨아래로</button>
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
    const select = modal.find('[name=menuCodes]');
    window.prepareWriteFormData = function (data) {
        data.menuCodes = [];
        const options = select.find('option');

        for (let i = 0; i < options.length; i++) {
            data.menuCodes.push($(options[i]).val());
        }
    };

    window.doneUpdatingSequences = function (form, response) {
        <c:if test="${menu.menuCode != null}">
        const container = $('.-menu[data-id="${g.escapeQuote(menu.menuCode)}"] > .-menu-container');
        </c:if>
        <c:if test="${menu.menuCode == null}">
        const container = $('.auth-tree > ul');
        </c:if>

        const children = container.children().detach();

        const options = select.find('option');
        for (let i = 0; i < options.length; i++)
            children.filter(function () {
                return $(this).attr('data-id') === $(options[i]).val();
            }).appendTo(container);

        modal.modalHide();
    }

    modal.find('.-to-first').click(function () {
        const options = select.find('option:selected').detach();
        select.prepend(options);
    });
    modal.find('.-to-prev').click(function () {
        const prev = select.find('option:selected:first').prev();
        if (!prev.length) return;

        const options = select.find('option:selected').detach();
        options.insertBefore(prev);
    });
    modal.find('.-to-next').click(function () {
        const next = select.find('option:selected:last').next();
        if (!next.length) return;

        const options = select.find('option:selected').detach();
        options.insertAfter(next);
    });
    modal.find('.-to-last').click(function () {
        const options = select.find('option:selected').detach();
        select.append(options);
    });
</script>
