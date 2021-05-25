<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>

<%@ attribute name="e" required="true" type="kr.co.eicn.ippbx.front.model.OrganizationTree" %>
<%@ attribute name="maxLevel" required="true" type="java.lang.Integer" %>
<%@ attribute name="keyword" required="true" type="java.lang.String" %>

<div class="item">
    <i class="folder <%--open--%> icon"></i>
    <div class="content" style="font-size: 18px">
        <div class="header <%--select--%> ${keyword != null && keyword.length() > 0 && e.groupName.contains(keyword) ? 'highlight' : null}" onclick="showOrganizationSummary(this, ${e.seq})">
            ${g.htmlQuote(e.groupName)}
            <button onclick="event.stopPropagation(); showInput($(this).closest('.header'), ${e.seq}); return false" class="circular basic ui icon mini very compact button" title="수정">
                <i class="icon pencil alternate"></i>
            </button>
            <c:if test="${e.groupLevel < maxLevel}">
                <button onclick="event.stopPropagation(); addChild($(this).closest('.item').find('.list:first'), '${g.htmlQuote(e.groupCode)}', ${e.groupLevel + 1}); return false"
                        class="circular basic ui icon mini very compact button" title="추가">
                    <i class="icon plus"></i>
                </button>
            </c:if>
            <button onclick="event.stopPropagation(); deleteEntity(${e.seq});" class="circular basic ui icon mini very compact button" title="삭제">
                <i class="icon minus"></i>
            </button>
            <%--<button class="circular basic ui icon mini very compact button">
                <i class="icon angle up"></i>
            </button>
            <button class="circular basic ui icon mini very compact button">
                <i class="icon angle down"></i>
            </button>--%>
        </div>
        <div class="list">
            <c:forEach var="person" items="${e.persons}">
                <div class="item">
                    <div class="header ${keyword != null && keyword.length() > 0 && e.groupName.contains(keyword) ? 'highlight' : null}">
                        <i class="user outline icon"></i> ${g.htmlQuote(person.idName)} (${g.htmlQuote(person.id)})
                    </div>
                </div>
            </c:forEach>
            <c:forEach var="child" items="${e.children}">
                <tags:organization-editable-tree e="${child}" maxLevel="${maxLevel}" keyword="${keyword}"/>
            </c:forEach>
        </div>
    </div>
</div>
