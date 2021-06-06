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

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>

<tags:layout>

    <div class="content-wrapper -admin-panel">
    <iframe class="content-inner" id="main-content" src="<c:url value="/admin/dashboard/"/>"></iframe>
    </div>

    <c:if test="${hasExtension && isStat}">
        <jsp:include page="/counsel/"/>
    </c:if>

    <tags:scripts>
        <script>
            $(document).on('click', '.-menu-page', function (event) {
                const $this = $(event.target);
                event.stopPropagation();
                event.preventDefault();

                $('.-menu-page').parent().removeClass('active');
                $this.parent().addClass('active');

                $('#main-content').attr('src', $this.attr('href'));

                if ($('#main').is('.change-mode')) {
                    changeMode();
                }

                return false;
            });
        </script>
    </tags:scripts>
</tags:layout>
