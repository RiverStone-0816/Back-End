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

<c:set var="activeStt" value="${(g.usingServices.contains('AST') && g.user.isAstIn eq 'Y')}"/>

<div id="call-panel" class="active${activeStt ? '-stt':''}">
    <c:choose>
        <c:when test="${activeStt}">
            <div style="width: 50%; display: flex; flex-direction: column; margin-right: 10px;">
                <div class="panel call-info">
                    <div class="panel-heading">
                        <label class="control-label">수발신정보</label>
                        <button type="button" class="ui button mini right floated compact"
                                onclick="clearCustomerAndCounselingInput()">초기화
                        </button>
                    </div>
                    <div class="panel-body">
                        <table class="ui table celled definition">
                            <tbody>
                            <tr>
                                <td class="three wide">전화상태</td>
                                <td>
                                    <text id="call-status"></text>
                                    <button class="ui right floated button mini compact blue" id="partial-recoding-stt"
                                            style="display: none;">
                                        <i class="fa fa-play"></i>&ensp;<text>부분녹취</text>
                                    </button>
                                    <c:if test="${usingServices.contains('LGUCB')}">
                                        <button class="ui icon button mini compact translucent" id="call-bot" title="콜봇"
                                                onclick="popupCounselCallBot()" style="display: none;">
                                            <i class="tty alternate icon"></i>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td>고객번호</td>
                                <td class="-calling-number" id="counseling-target-stt"></td>
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
                                <td>전화걸기</td>
                                <td>
                                    <div class="ui form">
                                        <div class="two fields">
                                            <div class="field remove-padding">
                                                <select id="cid-stt">
                                                    <option value="" label="">발신번호선택</option>
                                                    <c:forEach var="e" items="${services}">
                                                        <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}(${g.htmlQuote(e.key)})</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="field remove-pr">
                                                <div class="ui action input fluid">
                                                    <input style="width: 115px !important;" type="text"
                                                           id="calling-number-stt" class="-calling-number"/>
                                                    <button type="button" class="ui icon button"
                                                            onclick="tryDial('MAINDB')">
                                                        <i class="phone icon"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>퀵메뉴</td>
                                <td>
                                    <button type="button" class="ui mini button compact"
                                            onclick="popupSearchMaindbCustomModal()"> 고객DB
                                    </button>
                                    <button type="button" class="ui mini button compact"
                                            onclick="popupSearchCounselingHistoryModal()"> 상담이력
                                    </button>
                                    <button type="button" class="ui mini button compact"
                                            onclick="popupSearchCallHistoryModal()"> 통화이력
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel call-setting" style="overflow: auto;">
                    <div class="panel-heading">
                        <label class="control-label">통화내역</label>
                    </div>
                    <div class="panel-body">
                        <jsp:include page="/counsel/call/user-call-stt-history"/>
                    </div>
                </div>
            </div>
            <div style="display: flex; flex: 1;">
                <jsp:include page="/counsel/call/stt-panel"/>
            </div>
        </c:when>
        <c:otherwise>
            <div class="panel call-info">
                <div class="panel-heading">
                    <label class="control-label">수발신정보</label>
                    <button type="button" class="ui button mini right floated compact"
                            onclick="clearCustomerAndCounselingInput()">초기화
                    </button>
                </div>
                <div class="panel-body">
                    <table class="ui table celled definition">
                        <tbody>
                        <tr>
                            <td class="three wide">전화상태</td>
                            <td>
                                <text id="call-status"></text>
                                <button class="ui right floated button mini compact blue" id="partial-recoding"
                                        style="display: none;">
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
            <div class="panel call-setting">
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
                                <button type="button" class="ui mini button compact"
                                        onclick="popupSearchMaindbCustomModal()"> 고객DB
                                </button>
                                <button type="button" class="ui mini button compact"
                                        onclick="popupSearchCounselingHistoryModal()"> 상담이력
                                </button>
                                <button type="button" class="ui mini button compact"
                                        onclick="popupSearchCallHistoryModal()"> 통화이력
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/counsel/call/modal-counseling-transfer"/>
<jsp:include page="/counsel/call/modal-counseling-reservation"/>

<tags:scripts>
    <script>
        function tryDial(type) {
            const cid = $('#cid${activeStt ? "-stt" : ""}').val();
            const number = $('#calling-number${activeStt ? "-stt" : ""}').val();

            if (ipccCommunicator.status.cMemberStatus === 1) {
                alert("상담중 상태에서는 전화 걸기가 불가능합니다.");
                return;
            }

            if (!number) return;
            ipccCommunicator.clickByCampaign(cid, number, type, $('#call-custom-input [name=groupSeq]').val(), $('#call-custom-input .-custom-id').text());

            // 키워드 차트 초기화
            if (window.keywordChart != null) window.keywordChart.initialize()
        }

        function clearCustomerAndCounselingInput(isResultSave = false) {
            if (!isResultSave && ipccCommunicator.status.cMemberStatus === 1)
                return alert('상담 중에는 초기화할 수 없습니다.');

            audioId = null;
            callType = null;
            phoneNumber = null;
            ipccCommunicator.status.clickKey = null;
            clearTransferredUser();

            $('#call-status').empty().val('');
            $('#counseling-target${activeStt ? "-stt" : ""}').empty().val('');
            $('#calling-number${activeStt ? "-stt" : ""}').empty().val('');
            $('.-calling-path').empty().val('');
            $('#user-custom-info').empty().val('');
            $('#user-call-history').empty().val('');
            $('#counsel-list').empty().val('');

            <c:if test="${usingServices.contains('LGUCB')}">
            $('#call-bot').hide();
            </c:if>

            loadCustomInput();
            loadUserCustomInfo();
            loadUserCallHistory();
            <c:if test="${g.usingServices.contains('ASTIN') && g.user.isAstIn eq 'Y'}">
            sttClear();
            </c:if>

            // 키워드 차트 초기화
            if (window.keywordChart != null) window.keywordChart.initialize()
        }

        function submitCallCustomInput() {
            return submitJsonData($('#call-custom-input')[0]);
        }

        function loadCustomInput(maindbGroupSeq, customId, phoneNumber, uniqueId, inOut, callSeq, maindbResultSeq) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/call/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                phoneNumber: phoneNumber || '',
                maindbResultSeq: maindbResultSeq || ''
            }), '#call-custom-input').done(() => {
                if (uniqueId)
                    audioId = uniqueId;
                if (inOut)
                    callType = inOut;
                if (callSeq)
                    callUnique = callSeq;
            });
        }

        function loadCounselingInput(maindbGroupSeq, customId, phoneNumber, maindbResultSeq) {
            replaceReceivedHtmlInSilence($.addQueryString('/counsel/call/counseling-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                phoneNumber: phoneNumber || '',
                maindbResultSeq: maindbResultSeq
            }), '#call-counseling-input');
        }

        function loadUserCustomInfo(channelData) {
            if (channelData)
                replaceReceivedHtmlInSilence('/counsel/call/user-custom-info?channelData=' + channelData, '#user-custom-info');
        }

        /**
         *
         * @param phoneNumber
         * loadUserCallHistory 함수는 [OD, ID, 통화종료, 초기화 클릭] 일 때 호출 된다. 핸드폰 번호가 없다면 오늘 통화한 모든 고객의 정보 노출,
         * 핸드폰 번호가 있다면 해당 고객의 과거 30일간의 통화를 노출한다.
         */
        function loadUserCallHistory(phoneNumber) {
            console.log('loadUserCallHistory 실행 @@@')

            <c:if test="${activeStt}">
            if (phoneNumber) replaceReceivedHtmlInSilence('/counsel/call/user-call-stt-history?phoneNumber=' + phoneNumber, '#user-call-stt-history');
            else replaceReceivedHtmlInSilence('/counsel/call/user-call-stt-history', '#user-call-stt-history');
            </c:if>

            <c:if test="${!activeStt}">
            replaceReceivedHtmlInSilence('/counsel/call/user-call-history', '#user-call-history');
            </c:if>
        }

        $(window).on('load', function () {
            loadCustomInput();
        });
    </script>
</tags:scripts>
