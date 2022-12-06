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


<div class="ui modal inverted tiny" id="modal-calling">
    <div class="content">
        <div class="customer-box-inner">
            <div class="customer-info">
                <p class="number -calling-number" style="margin: 0;"></p>
                <p class="route -calling-path" style="margin: 0;"></p>
                <p class="second -call-waiting-time" style="margin: 0;"></p>
            </div>
        </div>
    </div>
    <div class="actions" style="padding-top: 5px !important;">
        <button type="button" class="ui black deny button modal-close -call-reject">전화거절</button>
        <button type="button" class="ui positive right labeled icon button modal-close -call-receive">
            전화받기<i class="checkmark icon"></i>
        </button>
    </div>
</div>

<tags:scripts>
    <script>
        function viewCallReception() {
            $('#modal-calling').modal({
                // context: '.tab-content-container',
                blurring: true,
                centered: true,
                closable: false
            }).modal('show');
        }
    </script>
</tags:scripts>
