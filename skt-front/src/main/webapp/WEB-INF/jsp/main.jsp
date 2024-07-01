<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>

<tags:layout>

    <c:choose>
    <c:when test="${g.user.idType eq 'M'}">
    <div class="content-wrapper -admin-panel">
        <iframe class="content-inner " id="main-content-m" src="<c:url value="/admin/application/maindb/result"/>"></iframe>
    </div>
    </c:when>

    <c:when test="${'A|J|B'.contains(g.user.idType)}">
        <div class="content-wrapper -admin-panel">
            <iframe class="content-inner " id="main-content-a" src="<c:url value="/admin/dashboard/total"/>"></iframe>
        </div>
    </c:when>
    </c:choose>

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

                /*$('.-main-content').attr('src', $this.attr('href'));*/
                $('#main-content-m').attr('src', $this.attr('href'));
                $('#main-content-a').attr('src', $this.attr('href'));

                if ($('.main').is('.change-mode')) {
                    changeMode();
                }

                return false;
            });
        </script>
    </tags:scripts>
</tags:layout>
