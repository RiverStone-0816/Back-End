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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="nine wide column remove-margin" style="height: calc(100% + 2rem);">
    <div class="panel full-height remove-margin">
        <div class="panel-heading" style="min-height:42px;max-height: 42px;">
            <label class="control-label">프리뷰 리스트</label>
        </div>
        <div class="panel-body" id="preview-list">
            <div class="sub-content ui container fluid unstackable remove-padding" id="preview-list-body">
                <jsp:include page="/counsel/preview/list-body"/>
            </div>
        </div>
    </div>
</div>
<div class="seven wide column" id="consulting-panel-prv">
    <div id="prv-custom-input-panel">
        <div class="panel" id="preview-custom-input"></div>
    </div>
    <div id="prv-counseling-input-panel">
        <div class="panel" id="preview-counseling-input"></div>
    </div>
</div>

<tags:scripts>
    <script>
        function startPreview(previewGroupId, previewCustomId, customNumber) {
            if (ipccCommunicator.status.cMemberStatus === 1)
                return alert("상담중 상태에서는 전화 걸기가 불가능합니다.");

            if (customNumber) {
                const cid = $('#cid${(g.usingServices.contains('AST') && g.user.isAstIn eq 'Y') || (g.usingServices.contains('BSTT') && g.user.isAstStt eq 'Y') ? "-stt" : ""}').val()
                ipccCommunicator.clickByCampaign(cid, customNumber, 'PRV', previewGroupId, previewCustomId);
            }
        }

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
            setCounselPrvSearch();

            const groupSeq = $('#search-preview-form [name=groupSeq]').val();
            if (groupSeq) {
                loadPreviewCustomInput(groupSeq);
                loadPreviewCounselingInput(groupSeq);
            }
        });
    </script>
</tags:scripts>
