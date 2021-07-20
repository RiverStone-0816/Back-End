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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<form:form modelAttribute="form" cssClass="ui modal small -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/task-script/category"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">분류관리</div>

    <div class="scrolling content rows">
        <div class="ui grid" id="category-list">
            <c:forEach var="e" items="${categories}">
                <div class="row -category-input-container">
                    <div class="sixteen wide column">
                        <div class="ui action fluid input">
                            <input type="text" name="category[${e.key}]" value="${g.htmlQuote(e.value)}" placeholder="분류명입력"/>
                            <button type="button" class="ui icon button -delete-category" data-id="${e.key}">
                                <i class="minus icon"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button left floated -add-category">분류추가</button>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    const originCategories = {
        <c:forEach var="e" items="${categories}">
        '${e.key}': '${g.escapeQuote(e.value)}',
        </c:forEach>
    };
    const categoryList = $('#category-list');

    modal.find('.-add-category').click(function () {
        prompt('분류명을 입력하십시오.').done(function (text) {
            if (text === '' || text === null)
                return alert('카테고리를 입력하세요')
            console.log(text);
            categoryList.append(
                $('<div class="row -category-input-container"/>').append(
                    $('<div class="sixteen wide column"/>').append(
                        $('<div class="ui action fluid input"/>').append(
                            $('<input/>', {type: "text", multiple: "multiple", name: "addingCategoryNames", value: text, path: "addingCategoryNames"})
                        ).append(
                            $('<button type="button" class="ui icon button"><i class="minus icon"></i></button>').click(function () {
                                $(this).closest('.-category-input-container').remove();
                            })
                        )
                    )
                )
            );
        });
        // const categoryName = jQuery.trim(name);
        // console.log(categoryName);
        // if (!name || !name.trim()) return;
        // name = name.trim();
    });

    modal.find('.-delete-category').click(function () {
        categoryList.append($('<input/>', {type: "hidden", multiple: "multiple", name: "deletingCategoryIds", value: $(this).attr('data-id')}));
        $(this).closest('.-category-input-container').remove();
    });

    window.prepareWriteFormData = function (data) {
        data.modifyingCategoryIdToNameMap = {};
        for (let id in data.category)
            if (data.category.hasOwnProperty(id))
                if (originCategories[id] !== data.category[id])
                    data.modifyingCategoryIdToNameMap[id] = data.category[id];

        delete data.category;
    };
</script>
