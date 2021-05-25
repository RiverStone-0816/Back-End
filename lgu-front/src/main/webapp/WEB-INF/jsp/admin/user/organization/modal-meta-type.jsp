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

<%--@elvariable id="metaTypes" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.CompanyTreeLevelNameResponse>"--%>

<form class="ui modal -json-submit" data-method="put" action="${pageContext.request.contextPath}/api/organization/meta-type" data-done="reload">

    <i class="close icon"></i>
    <div class="header">META유형 설정</div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <c:forEach var="e" items="${metaTypes}" varStatus="status">
                <div class="row">
                    <div class="four wide column"><label class="control-label">${e.groupLevel}단계 명칭</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid">
                            <input type="text" name="treeNameMap[${e.groupLevel}]" value="${g.htmlQuote(e.groupTreeName)}"/>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form>
