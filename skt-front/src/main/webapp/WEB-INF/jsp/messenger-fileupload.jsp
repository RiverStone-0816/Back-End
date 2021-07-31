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
<%--@elvariable id="serviceUrl" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="talkSocketUrl" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--    <title>IPCC 프리미엄</title>--%>
    <script src="<c:url value="/webjars/jquery/3.4.1/jquery.min.js"/>"></script>
    <tags:css/>
</head>
<body>

<a href="javascript:$('#up_filename').click()" style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);">
    <i class="paperclip icon"></i> 파일업로드
</a>

<form id="uploadForm" name='uploadForm' enctype="multipart/form-data" action="${socketUrl}/fileupload" method="post">
    <input type="hidden" id="my_userid" name="my_userid" value="${g.escapeQuote(user.id)}"/>
    <input type="hidden" id="my_username" name="my_username" value="${g.escapeQuote(user.idName)}"/>
    <input type="hidden" id="company_id" name="company_id" value="${g.escapeQuote(user.companyId)}"/>
    <input type="hidden" id="basic_url" name="basic_url" value="${g.htmlQuote(messengerSocketUrl)}"/>
    <input type="hidden" id="web_url" name="web_url" value="${g.htmlQuote(apiServerUrl)}"/>
    <input type="hidden" id="room_id" name="room_id" value="${g.htmlQuote(roomId)}"/>

    <input type="file" name="up_filename" id='up_filename' multiple style='width:200px;display:none;'/>
    <input type="submit" id='up_btn' name="up_btn" value="업로드" style='width:60px;height:20px;display:none;'>
    <input type="button" id='cancel_btn' name="cancel_btn" value="취소" style='width:60px;height:20px;display:none;'>
</form>

<script type="text/javascript">
    $(window).on('load', function () {
        $('#up_filename').click();
    });

    $('input[type="file"]').change(function () {
        if ($(this).val())
            $('#uploadForm').submit();
    });

    $('#up_btn').click(function () {
        $('#uploadForm').submit();
    });
</script>
</body>
</html>
