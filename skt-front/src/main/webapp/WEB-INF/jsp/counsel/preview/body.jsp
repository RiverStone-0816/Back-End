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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="prv-wrapper">
    <div class="prv-left-panel">
        <div class="content-wrapper-frame">
            <div id="preview-list-body" class="full-height">
                <jsp:include page="/counsel/preview/list-body"/>
            </div>
        </div>
    </div>
    <div class="prv-right-panel">
        <div class="content-wrapper-frame">
            <div class="sub-content ui container fluid unstackable">
                <div class="panel panel-search" id="preview-custom-input"></div>
                <div class="panel" id="preview-counseling-input"></div>
            </div>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        function loadPreviewCustomInput(groupSeq, customId) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/preview/custom-input', {
                groupSeq: groupSeq || '',
                customId: customId || '',
            }), '#preview-custom-input');
        }

        function loadPreviewCounselingInput(groupSeq, customId) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/preview/counseling-input', {
                groupSeq: groupSeq || '',
                customId: customId || '',
            }), '#preview-counseling-input');
        }

        $(window).on('load', function () {
            const groupSeq = $('#search-preview-form [name=groupSeq]').val();
            if (groupSeq) {
                loadPreviewCustomInput(groupSeq);
                loadPreviewCounselingInput(groupSeq);
            }
        });
    </script>
</tags:scripts>
