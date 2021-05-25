<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:scripts/>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=440, initial-scale=0.8"/>
    <title></title>
    <tags:favicon/>
    <tags:css/>
</head>
<body>

<jsp:doBody/>

<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>

    <script>
        /*TODO: 임시처리*/
        $(document).ready(function () {
            if (window.name && window.name.indexOf('menu') === 0) {
                const menuTab = $(window.document).find('.menu-tab');
                const subContent = $(window.document).find('.sub-content:first');
                subContent.children().each(function () {
                    $(this).detach().appendTo($(window.document).find('.content-wrapper-frame:first'));
                });

                menuTab.remove();
                subContent.remove();
            }
        });
    </script>
</div>

</body>
</html>
