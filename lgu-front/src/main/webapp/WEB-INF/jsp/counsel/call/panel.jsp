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

<div id="call-panel" class="active">
    <div class="panel">
        <div class="panel-heading">
            <label class="control-label">수발신정보</label>
            <%--<div class="ui label">
                대기시간<div class="detail">00:34</div>
            </div>--%>
            <%--<div class="ui label">
                콜백<div class="detail">3건</div>
            </div>--%>
            <button type="button" class="ui button mini right floated compact" onclick="clearCustomerAndCounselingInput()">초기화</button>
        </div>
        <div class="panel-body">
            <table class="ui table celled definition">
                <tbody>
                <tr>
                    <td class="three wide">전화상태</td>
                    <td>
                        <text id="call-status"></text>
                        <button class="ui right floated button mini compact blue" id="partial-recoding" style="display: none;">
                            <i class="fa fa-play"></i>&ensp;<text>부분녹취</text>
                        </button>
                    </td>
                </tr>
                <tr>
                    <td>고객번호</td>
                    <td class="-calling-number" id="counseling-target"></td>
                </tr>
                <tr>
                    <td>수신경로</td>
                    <td class="-calling-path"></td>
                </tr>
                <tr>
                    <td>고객정보</td>
                    <td>
                        <div class="ui form">
                            <div class="ui form">
                                <jsp:include page="/counsel/call/user-custom-info"/>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>통화이력</td>
                    <td>
                        <div class="ui form">
                            <div class="ui form">
                                <jsp:include page="/counsel/call/user-call-history"/>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="panel">
        <div class="panel-heading">
            <label class="control-label">발신설정/전화걸기</label>
        </div>
        <div class="panel-body">
            <table class="ui table celled definition">
                <tbody>
                <tr>
                    <td class="three wide">발신표시</td>
                    <td>
                        <div class="ui form">
                            <select id="cid">
                                <option value="" label="">발신번호선택</option>
                                <c:forEach var="e" items="${services}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}(${g.htmlQuote(e.key)})</option>
                                </c:forEach>
                            </select>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>전화걸기</td>
                    <td>
                        <div class="ui action input fluid">
                            <input type="text" id="calling-number" class="-calling-number"/>
                            <button type="button" class="ui icon button" onclick="tryDial('MAINDB')">
                                <i class="phone icon"></i>
                            </button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>퀵메뉴</td>
                    <td>
                        <button type="button" class="ui mini button compact" onclick="popupSearchMaindbCustomModal()"> 고객DB</button>
                        <button type="button" class="ui mini button compact" onclick="popupSearchCounselingHistoryModal()"> 상담이력</button>
                        <button type="button" class="ui mini button compact" onclick="popupSearchCallHistoryModal()"> 통화이력</button>
                        <%--<button type="button" class="ui mini button compact" onclick="popupSearchCallbackModal()"> 콜백</button>--%>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/counsel/call/modal-counseling-transfer"/>
<jsp:include page="/counsel/call/modal-counseling-reservation"/>

<tags:scripts>
    <script>
        function tryDial(type) {
            const cid = $('#cid').val()
            const number = $('#calling-number').val();

            if (ipccCommunicator.status.cMemberStatus === 1) {
                alert("상담중 상태에서는 전화 걸기가 불가능합니다.");
                return;
            }

            if (!number) return;
            ipccCommunicator.clickByCampaign(cid, number, type, $('#call-custom-input [name=groupSeq]').val(), $('#call-custom-input .-custom-id').text());
        }

        function clearCustomerAndCounselingInput() {
            audioId = null;
            phoneNumber = null;
            clearTransferredUser();

            $('#call-status').empty().val('');
            $('#counseling-target').empty().val('');
            $('#calling-number').empty().val('');
            $('.-calling-path').empty().val('');
            $('#user-custom-info').empty().val('');
            $('#user-call-history').empty().val('');
            $('#counsel-list').empty().val('');


            loadCustomInput();
            loadUserCustomInfo();
            loadUserCallHistory();
        }

        function submitCallCustomInput() {
            return submitJsonData($('#call-custom-input')[0]);
        }

        function loadCustomInput(maindbGroupSeq, customId, phoneNumber) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/call/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                phoneNumber: phoneNumber || ''
            }), '#call-custom-input');
        }

        function loadCounselingInput(maindbGroupSeq, customId, phoneNumber, maindbResultSeq) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/call/counseling-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                phoneNumber: phoneNumber || '',
                maindbResultSeq : maindbResultSeq
            }), '#call-counseling-input');
        }

        function loadUserCustomInfo(channelData) {
            replaceReceivedHtmlInSilence('/counsel/call/user-custom-info?channelData=' + channelData, '#user-custom-info');
        }

        function loadUserCallHistory() {
            replaceReceivedHtmlInSilence('/counsel/call/user-call-history', '#user-call-history');
        }

        $(window).on('load', function () {
            loadCustomInput();
        });
    </script>
</tags:scripts>
