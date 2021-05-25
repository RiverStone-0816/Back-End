<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>

<%--@elvariable id="devel" type="java.lang.Boolean"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<!-- external library common -->
<style>
    @font-face {
        font-family: 'Material Icons';
        font-style: normal;
        font-weight: 400;
        src: url(<c:url value="/resources/vendors/material-design-icons/flUhRq6tzZclQEJ-Vdg-IuiaDsNc/MaterialIcons-Regular.eot"/>); /* For IE6-8 */
        src: local('Material Icons'), local('MaterialIcons-Regular'),
        url(<c:url value="/resources/vendors/material-design-icons/flUhRq6tzZclQEJ-Vdg-IuiaDsNc/MaterialIcons-Regular.woff2"/>) format('woff2'),
        url(<c:url value="/resources/vendors/material-design-icons/flUhRq6tzZclQEJ-Vdg-IuiaDsNc/MaterialIcons-Regular.woff"/>) format('woff'),
        url(<c:url value="/resources/vendors/material-design-icons/flUhRq6tzZclQEJ-Vdg-IuiaDsNc/MaterialIcons-Regular.ttf"/>) format('truetype');
    }
    .material-icons {
        font-family: 'Material Icons';
        font-weight: normal;
        font-style: normal;
        font-size: 24px;  /* Preferred icon size */
        display: inline-block;
        line-height: 1;
        text-transform: none;
        letter-spacing: normal;
        word-wrap: normal;
        white-space: nowrap;
        direction: ltr;

        /* Support for all WebKit browsers. */
        -webkit-font-smoothing: antialiased;
        /* Support for Safari and Chrome. */
        text-rendering: optimizeLegibility;

        /* Support for Firefox. */
        -moz-osx-font-smoothing: grayscale;

        /* Support for IE. */
        font-feature-settings: 'liga';
    }

    /*@import url('https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic&subset=latin');*/
</style>

<link href="<c:url value="/webjars/font-awesome/5.13.0/css/all.min.css"/>" rel="stylesheet"/>
<%--<link href="<c:url value="/webjars/Semantic-UI/2.4.1/semantic.css"/>" rel="stylesheet"/>--%>
<link href="<c:url value="/resources/css/semantic.css"/>" rel="stylesheet"/>

<!-- external library depend -->
<link href="<c:url value="/webjars/overlayscrollbars/1.9.1/css/OverlayScrollbars.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/webjars/jquery-ui/1.12.1/jquery-ui.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/webjars/toastr/2.1.2/build/toastr.css"/>" rel="stylesheet"/>
<link href="<c:url value="/resources/vendors/tagify/1.3.1/tagify.css?version=${version}"/>" rel="stylesheet"/>

<c:choose>
    <c:when test="${devel}">
        <link href="<c:url value="/resources/less/app.less?version=${version}"/>" rel="stylesheet/less"/>
        <script src="<c:url value="/webjars/less.js/2.7.3/dist/less.min.js"/>"></script>
    </c:when>
    <c:otherwise>
        <link href="<c:url value="/resources/compiled/${version}.min.css?version=${version}"/>" rel="stylesheet"/>
    </c:otherwise>
</c:choose>
