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
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<div class="ui bottom attached tab segment remove-margin active" data-tab="call-panel">
    <div class="panel remove-margin full-height">
        <div class="panel-heading">
            <div class="pull-left"><label class="panel-label">수발신정보</label></div>
            <div class="pull-right">
                <button class="ui button right floated sharp" onclick="clearCustomerAndCounselingInput()">초기화</button>

            </div>
        </div>
        <div class="panel-body overflow-hidden">
            <table class="ui celled table compact unstackable border-top-default call-top-area">
                <tr>
                    <th>전화상태</th>
                    <td>
                        <text id="call-status"></text>
                        <c:if test="${!(g.serviceKind.equals('CC') && usingServices.contains('TYPE2'))}">
                            <button class="ui right floated button mini compact blue" id="partial-recoding"
                                    style="display: none;">
                                <i class="fa fa-play"></i>&ensp;<text>부분녹취</text>
                            </button>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <th>발신표시</th>
                    <td>
                        <div class="ui form flex">
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
                    <th>고객번호</th>
                    <td class="">
                        <div class="ui form flex">
                            <text style="display: none;" id="counseling-target" class="-calling-number"></text>
                            <input type="text" class="-calling-number" id="calling-number"/>
                            <button type="button" class="ui button sharp light ml5" onclick="tryDial('MAINDB')">전화걸기</button>
                        </div>
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
                        <div class="ui form flex">
                            <jsp:include page="/counsel/call/user-call-history"/>
                            <button type="button" class="ui button sharp light ml5" onclick="popupSearchCallHistoryModal()">통화이력</button>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="call-bottom-area">
                <div class="flex-100 bottom-area">
                <div class="ui top attached tabular menu line light flex remove-margin">
                    <button class="item active" data-tab="consult-history">상담이력</button>
                    <button class="item" data-tab="todo">To-Do</button>
                    <%--FIXME:필요시 다음과 같이 추가--%>
                    <%--<button class="item" data-tab="etc-lookup">기타조회</button>--%>
                </div>
                <div class="ui bottom attached tab segment remove-margin overflow-overlay active" data-tab="consult-history">
                    <table class="ui celled table unstackable">
                        <thead>
                        <tr>
                            <c:if test="${serviceKind.equals('SC')}">
                                <th>채널</th>
                            </c:if>
                            <th>수/발신</th>
                            <th>상담등록시간</th>
                            <th>전화번호</th>
                            <c:if test="${usingServices.contains('ECHBT') || usingServices.contains('KATLK')}">
                                <th>상담톡아이디</th>
                            </c:if>
                            <th>상담원</th>
                            <th>자세히</th>
                        </tr>
                        </thead>
                        <tbody id="counsel-list"></tbody>
                    </table>
                </div>
                <div class="ui bottom attached tab segment remove-margin overflow-overlay" data-tab="todo">
                    <jsp:include page="/counsel/todo-list"/>
                </div>
                <%--<div class="ui bottom attached tab segment remove-margin" data-tab="etc-lookup">기타조회</div>--%>
            </div>
            </div>
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
            console.log("calling-number:"+number);


            if (ipccCommunicator.status.cMemberStatus === 1) {
                alert("상담중 상태에서는 전화 걸기가 불가능합니다.");
                return;
            }

            if (!number) return;
            ipccCommunicator.clickByCampaign(cid, number, type, $('#call-custom-input [name=groupSeq]').val(), $('#call-custom-input .-custom-id').text());

        }

        //초기화
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
        //고객정보 제출
        function submitCallCustomInput() {
            return submitJsonData($('#call-custom-input')[0]);
        }

        function loadCustomInput(maindbGroupSeq, customId, phoneNumber, resultSeq, uniqueId, inOut) {
            return replaceReceivedHtmlInSilence($.addQueryString('/counsel/call/custom-input', {
                maindbGroupSeq: maindbGroupSeq || '',
                customId: customId || '',
                phoneNumber: phoneNumber || '',
                resultSeq: resultSeq || '',
            }), '#call-custom-input').done(() => {
                if (uniqueId)
                    audioId = uniqueId;
                if (inOut)
                    callType = inOut;
            });
        }

        function loadCallingInfo(userPhone = '', userName = '', regDate = '', birthYear = '', gender = '', userSi = '', userGu = '', address = '',
                                 email = '', userIdx = '', blackYn = '' , targetForm = 'calling-info') {
            if (targetForm === '')
                targetForm = 'calling-info'
            const target = $('#' + targetForm + '');

            if( target.find('.-username').val()!==''){
                target.find('.-userphone').attr('readonly',true);
                target.find('.-username').attr('readonly',true);
                target.find('.-useridx').attr('readonly',true);
            }


            target.find('.-userphone').text(userPhone).val(userPhone);
            target.find('.-username').text(userName).val(userName);
            target.find('.-regdate').text(regDate.split(' ')[0]).val(regDate.split(' ')[0]);
            target.find('.-birthyear').text(birthYear).val(birthYear);
            target.find('.-customer-name').text(userName).val(userName);
            target.find('.-gender').text(gender).val(gender);
            target.find('.-usersi').text(userSi).val(userSi);
            target.find('.-usergu').text(userGu).val(userGu);
            target.find('.-address').text(address).val(address);
            target.find('.-email').text(email).val(email);
            target.find('.-useridx').text(userIdx).val(userIdx);
            target.find('.-blackYn').text(blackYn).val(blackYn);

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

        function loadMyCallStat() {
            restSelf.get('/api/stat/user/my-call-stat', null, null, true).done(function (response) {
                const elements = $('.-stat-my-call');
                keys(response.data).map(function (fieldName) {
                    const e = elements.filter('[data-field="' + fieldName + '"]');
                    e.text((response.data[fieldName] || 0).toFixed(parseInt(e.attr('data-fixed')) || 0));
                });
            });
        }

        function loadMyCallTime() {
            replaceReceivedHtmlInSilence('/counsel/call/stat', '#my-call-time');
        }


        $(window).on('load', function () {
            loadCustomInput();
            loadMyCallStat();
            loadMyCallTime();
            updatePersonStatus();

            setInterval(loadMyCallStat, 5 * 60 * 1000);
            setInterval(loadMyCallTime, 5 * 60 * 1000);
        });
    </script>
</tags:scripts>
