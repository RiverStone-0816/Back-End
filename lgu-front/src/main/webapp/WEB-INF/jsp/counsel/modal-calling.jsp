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


<div class="ui modal inverted tiny" id="modal-calling">
    <div class="content">
        <div class="customer-box-inner">
            <%--<div class="img-wrap">
                <div class="circle-wrap">
                    <div class="img-wrap">
                        <img src="../../../resources/images/profile.png">
                    </div>
                    <div class="circle"></div>
                </div>
            </div>--%>
            <div class="customer-info">
                <p class="number -calling-number"></p>
                <p class="route -calling-path"></p>
                <p class="second -call-waiting-time"></p>
            </div>
        </div>
    </div>
    <div class="actions">
        <%--<div class="pull-left">
            <div class="ui checkbox">
                <input type="checkbox" name="example">
                <label>거절 후 후처리</label>
            </div>
        </div>--%>
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
