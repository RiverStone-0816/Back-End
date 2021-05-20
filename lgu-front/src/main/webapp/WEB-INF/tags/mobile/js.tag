<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>

<%--@elvariable id="devel" type="java.lang.Boolean"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="cipherKey" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>

<!-- external library common -->
<script src="<c:url value="/webjars/jquery/3.4.1/jquery.min.js"/>"></script>
<script src="<c:url value="/webjars/Semantic-UI/2.4.1/semantic.js"/>"></script>
<script src="<c:url value="/webjars/jquery-blockui/2.65/jquery.blockUI.js"/>"></script>
<script src="<c:url value="/webjars/momentjs/2.21.0/min/moment.min.js"/>"></script>
<%--<script src="<c:url value="/webjars/d3js/5.9.1/d3.min.js"/>"></script>--%>

<!-- external library depend -->
<script src="<c:url value="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"/>"></script>
<script src="<c:url value="/webjars/cryptojs/3.1.2/rollups/aes.js"/>"></script>
<%--<script src="<c:url value="/webjars/overlayscrollbars/1.9.1/js/jquery.overlayScrollbars.min.js"/>"></script>--%>
<%--<script src="<c:url value="/webjars/toastr/2.1.2/toastr.js"/>"></script>--%>

<%--<script src="<c:url value="/resources/vendors/highlight/1.0.0/jQuery.highlight.js?version=${version}"/>" data-type="library"></script>--%>
<%--<script src="<c:url value="/resources/vendors/moment-lunar/moment-lunar.min.js?version=${version}"/>" data-type="library"></script>--%>
<script src="<c:url value="/resources/vendors/socket.io/2.1.1/socket.io.js?version=${version}"/>"></script>
<script src="<c:url value="/resources/vendors/sha512.js?version=${version}"/>"></script>

<%--<script src="<c:url value="/resources/vendors/flexslider/2.7.2/jquery.flexslider.js?version=${version}"/>" data-type="library"></script>--%>
<%--<script src="<c:url value="/resources/vendors/tablesort/0.0.11/tablesort.js?version=${version}"/>" data-type="library"></script>--%>
<%--<script src="<c:url value="/resources/vendors/multifile-master/jquery.MultiFile.js?version=${version}"/>" data-type="library"></script>--%>
<script src="<c:url value="/resources/vendors/maudio.js?version=${version}"/>" data-type="library"></script>
<%--<script src="<c:url value="/resources/vendors/Amsify-Suggestags/jquery.amsify.suggestags.js?version=${version}"/>" data-type="library"></script>--%>
<%--<script src="<c:url value="/resources/vendors/tagify/1.3.1/tagify.min.js?version=${version}"/>" data-type="library"></script>--%>
<%--<script src="<c:url value="/resources/vendors/tagify/1.3.1/jQuery.tagify.min.js?version=${version}"/>" data-type="library"></script>--%>

<c:choose>
    <c:when test="${devel}">
        <%-- user library --%>
        <script src="<c:url value="/resources/js/string.ex.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/formData.ex.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/jquery.ex.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/jquery.leanModal.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/ipcc-messenger/js/jquery.bind.helpers.mobile.js?version=${version}"/>" data-type="library"></script>
        <%--<script src="<c:url value="/resources/js/TabController.js?version=${version}"/>" data-type="library"></script>--%>
        <%--<script src="<c:url value="/resources/js/IvrEditor.js?version=${version}"/>" data-type="library"></script>--%>
        <script src="<c:url value="/resources/js/IpccAdminCommunicator.js?version=${version}"/>"></script>
        <%--<script src="<c:url value="/resources/js/IpccPdsCommunicator.js?version=${version}"/>"></script>--%>
        <script src="<c:url value="/resources/js/IpccCommunicator.js?version=${version}"/>"></script>
        <%--<script src="<c:url value="/resources/js/TalkCommunicator.js?version=${version}"/>"></script>--%>
        <script src="<c:url value="/resources/js/MessengerCommunicator.js?version=${version}"/>"></script>

        <%-- functions --%>
        <script src="<c:url value="/resources/js/common.func.js?version=${version}"/>" data-type="library"></script>
        <script src="<c:url value="/resources/js/depend.func.js?version=${version}"/>" data-type="library"></script>

        <%-- use strict --%>
        <script src="<c:url value="/resources/ipcc-messenger/js/depend.use.strict.mobile.js?version=${version}"/>" data-type="library"></script>

        <%-- external --%>

    </c:when>
    <c:otherwise>
        <script src="<c:url value="/resources/compiled/${version}.js"/>" data-type="library"></script>
    </c:otherwise>
</c:choose>

<script>
    window.reloadWhenSelectSite = false;
    window.disableLog = ${devel};
    window.contextPath = '${pageContext.request.contextPath}';
    window.loadingImageSource = contextPath + '/resources/images/loading.svg';
    window.cipherKey = '${cipherKey}';
    <c:if test="${g.login}">
    window.userId = '${g.escapeQuote(user.id)}';
    window.groupTree = '${g.escapeQuote(user.groupTreeName)}';
    </c:if>

    window.defaultProfilePicture = "<c:url value="/resources/ipcc-messenger/images/person.png"/>";
    window.accessToken = '${g.escapeQuote(accessToken)}';
    window.apiServerUrl = '${g.escapeQuote(apiServerUrl)}';
</script>
