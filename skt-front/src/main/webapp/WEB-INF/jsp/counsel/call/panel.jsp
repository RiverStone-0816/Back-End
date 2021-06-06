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

<div class="ui bottom attached tab segment active" data-tab="call-panel">
    <div class="panel remove-margin">
        <div class="panel-heading">
            <div class="pull-left"><label class="panel-label">수발신정보</label></div>
            <div class="pull-right">
                <button class="ui button right floated sharp" onclick="clearCustomerAndCounselingInput()">초기화</button>
            </div>
        </div>
        <div class="panel-body">
            <table class="ui celled table compact unstackable">
                <tr>
                    <th>전화상태</th>
                    <td>
                        <text id="call-status"></text>
                        <button class="ui right floated button mini compact blue" id="partial-recoding" style="display: none;">
                            <i class="fa fa-play"></i>&ensp;<text>부분녹취</text>
                        </button>
                    </td>
                </tr>
                <tr>
                    <th>고객번호</th>
                    <td class="flex-td">
                        <div class="ui input fluid">
                            <input type="text" class="-calling-number" id="counseling-target"/>
                        </div>
                        <button class="ui button sharp light">전화걸기</button>
                    </td>
                </tr>
                <tr>
                    <th>수신경로</th>
                    <td class="-calling-path"></td>
                </tr>
                <tr>
                    <th>고객정보</th>
                    <td>
                        <div class="ui form">
                            <jsp:include page="/counsel/call/user-custom-info"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>통화이력</th>
                    <td>
                        <div class="ui form">
                            <jsp:include page="/counsel/call/user-call-history"/>
                        </div>
                    </td>
                </tr>
            </table>

            <%--TODO--%>
            <div class="ui top attached tabular menu light flex">
                <button class="item active" data-tab="monitoring">모니터링</button>
                <button class="item" data-tab="statistics">통계</button>
            </div>
            <div class="ui bottom attached tab segment active remove-padding remove-margin" data-tab="monitoring">
                <div class="pd10">
                    <label class="panel-label">상담원 현황</label>
                </div>
                <div class="ui internally celled grid compact">
                    <div class="row">
                        <div class="sixteen wide column">
                            <table class="ui celled table compact unstackable">
                                <thead>
                                <tr>
                                    <th>총원</th>
                                    <th>대기</th>
                                    <th>상담중</th>
                                    <th>후처리</th>
                                    <th>휴식</th>
                                    <th>식사</th>
                                    <th>로그아웃</th>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                    <div class="row">
                        <div class="six wide column">
                            <label class="panel-label">MY CALL 현황(금일)</label>
                        </div>
                        <div class="ten wide column">
                            <label class="panel-label">상담 그룹 현황</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="six wide column">
                            <table class="ui celled table compact unstackable">
                                <tr>
                                    <th>수신</th>
                                </tr>
                                <tr>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <th>콜백</th>
                                </tr>
                                <tr>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <th>발신</th>
                                </tr>
                                <tr>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <th>응대율</th>
                                </tr>
                                <tr>
                                    <td>-</td>
                                </tr>
                            </table>
                        </div>
                        <div class="ten wide column">
                            <table class="ui celled table compact unstackable">
                                <thead>
                                <tr>
                                    <th>상담그룹</th>
                                    <th>대기고객</th>
                                    <th>대기상담</th>
                                    <th>통화불가</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="ui bottom attached tab segment" data-tab="statistics">
                통계
            </div>
        </div>
    </div>


    <%--todo: cid 선택하는 ui가 없다
    <select id="cid">
        <option value="" label="">발신번호선택</option>
        <c:forEach var="e" items="${services}">
            <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}(${g.htmlQuote(e.key)})</option>
        </c:forEach>
    </select>
    --%>
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
                maindbResultSeq: maindbResultSeq
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
