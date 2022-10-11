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
<%--@elvariable id="usingServices" type="java.lang.String"--%>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>
<tags:layout>
    <div class="tab-label-flow-container">
        <ul class="tab-label-container" style="left: 0;"></ul>
    </div>
    <button class="tab-arrow tab-arrow-left" style="display: none;" onclick="tabController.moveToLeftTabLabels()"><i class="material-icons"> keyboard_arrow_left </i></button>
    <button class="tab-arrow tab-arrow-right" style="display: none;" onclick="tabController.moveToRightTabLabels()"><i class="material-icons"> keyboard_arrow_right </i></button>

    <div class="tab-content-container manage-main"></div>

    <c:if test="${hasExtension && isStat}">
        <jsp:include page="/counsel/"/>
    </c:if>

    <div class="ui modal inverted tiny" id="modal-sms-send-popup">
        <i class="close icon"></i>
        <div class="header">
            SMS 발송
        </div>
        <div class="content rows scrolling">
            <div class="ui grid">
                <div class="row">
                    <div class="sixteen wide column">
                        <div class="ui form fluid">
                            <div class="field">
                                <textarea rows="10"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">카테고리</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>카테고리선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">상용문구</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select multiple="multiple">
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">전화번호</label></div>
                    <div class="twelve wide column ui form">
                        <div class="fields">
                            <div class="sixteen wide field">
                                <input type="text">
                            </div>
                        </div>
                        <div class="fields">
                            <div class="sixteen wide field">
                                <input type="text">
                            </div>
                        </div>
                        <div class="fields">
                            <div class="sixteen wide field">
                                <input type="text">
                            </div>
                        </div>
                        <div class="fields">
                            <div class="sixteen wide field">
                                <input type="text">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <div class="ui modal inverted tiny" id="modal-fax-mail-send-popup">
        <i class="close icon"></i>
        <div class="header">
            FAX/메일 발송
        </div>
        <div class="content rows scrolling">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">발송매체</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>FAX</option>
                                <option>E-mail</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">카테고리</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>카테고리선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">발송물</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select multiple="multiple">
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                                <option>[EICN 전화번호 안내] EICN 전화번호는 000-000-000 입니다.</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">수신주소</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <div class="ui modal inverted large" id="modal-rating-confirm">
        <i class="close icon"></i>
        <div class="header">
            상담원 평가 [결과]
        </div>
        <div class="scrolling content rows">
            <div style="margin-bottom:10px">
                <table class="ui celled table compact unstackable two fixed linebreak">
                    <tbody>
                    <tr>
                        <th>평가지</th>
                        <td>
                            <div class="ui form">
                                <select>
                                    <option>1</option>
                                </select>
                            </div>
                        </td>
                        <th>평가자</th>
                        <td>마스터</td>
                        <th>평가날짜</th>
                        <td>2015-02-27 17:21:40</td>
                        <th>상담원</th>
                        <td>
                            <div class="ui form">
                                <select>
                                    <option>1</option>
                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>수발</th>
                        <td>수신</td>
                        <th>수신번호</th>
                        <td>153</td>
                        <th>발신번호</th>
                        <td>133</td>
                        <th>통화날짜</th>
                        <td>2015-02-27 17:21:40</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div style="margin-bottom:10px">
                <table class="ui celled table compact unstackable structured">
                    <thead>
                    <tr>
                        <th colspan="2">분류</th>
                        <th rowspan="2">평가항목</th>
                        <th rowspan="2" class="small-td">점수</th>
                        <th rowspan="2" class="small-td">선택</th>
                    </tr>
                    <tr>
                        <th>분류1</th>
                        <th>분류2</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td rowspan="5">111111</td>
                        <td rowspan="5">1111111</td>
                        <td>222222222</td>
                        <td>1</td>
                        <td>
                            <div class="ui form radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td rowspan="5">111111</td>
                        <td rowspan="5">1111111</td>
                        <td>222222222</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td rowspan="5">111111</td>
                        <td rowspan="5">1111111</td>
                        <td>222222222</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>1111111111</td>
                        <td>1</td>
                        <td>
                            <div class="ui radio fitted checkbox">
                                <input type="radio">
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div style="margin-bottom:10px">
                <table class="ui celled table compact unstackable two">
                    <tbody>
                    <tr>
                        <th>메모</th>
                        <td>
                            <div class="ui form">
                                <div class="field">
                                    <textarea rows="3"></textarea>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>이의제기</th>
                        <td>
                            <div class="ui form">
                                <div class="field">
                                    <textarea rows="3"></textarea>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <div class="ui modal tiny" id="modal-consulting-history-talk-view" style="height: 750px;">

    </div>

    <tags:scripts>
        <script>
            /*if (disableLog) {
                ipccCommunicator.log = function () {
                };
            }*/

            window.tabController = new TabController(
                '.tab-label-flow-container',
                '.tab-label-container',
                '.tab-arrow',
                '.tab-content-container.manage-main'
            );

            $(window).on('load', function () {
                $('.tab-indicator[title="대시보드"]').click();
            });

            function smsCategoryAdd() {
                popupDraggableModalFromReceivedHtml('/counsel/modal-sms-category', 'modal-sms-category-add-popup')
            }

            function ratingConfirmPopup() {
                $('#modal-rating-confirm').dragModalShow();
            }

            function smsTemplateAdd() {
                popupDraggableModalFromReceivedHtml('/counsel/modal-sms-template', 'modal-sms-template-add-popup')
            }

            function smsSend() {
                $('#modal-sms-send-popup').dragModalShow()
            }

            function faxEmailSend() {
                $('#modal-fax-mail-send-popup').dragModalShow()
            }

            function talkHistoryView(roomId) {
                popupReceivedHtml('/admin/wtalk/history/modal?roomId=' + encodeURIComponent(roomId), 'modal-consulting-history-talk-view');
            }

            <c:if test="${usingServices.contains('SPHONE') && g.user.softphone.equals('Y')}">
            $(document).ready(() => {
                // WebRTC 소프트폰 사용 가능 여부 확인
                if (is_support_softphone('${g.user.phoneKind}') === false) {
                    return false;
                }

                set_ringtone_volume(1.0);
                set_busytone_volume(1.0);

                set_local_sipcall_stream_object($('#myVideo'));
                set_remote_sipcall_stream_object($('#remoteVideo'));

                set_accept_sipcall_btn_object($(".-call-receive"));

                set_callback_sipcall_registered_status(() => {
                    $('div.dial-pad .header').css('background-color','lime');
                });
                set_callback_sipcall_unregistered_status(() => {
                    $('div.dial-pad .header').css('background-color','red');
                });
                set_callback_sipcall_disconnected_status(() => {
                    $('div.dial-pad .header').css('background-color','grey');
                });


                restSelf.get('/api/auth/softPhone-info').done(function (response) {
                    if(set_webrtc_server_info(response.data.serverInformation)){
                        start_sipcall();
                    }
                });
            })
            </c:if>
        </script>
    </tags:scripts>
</tags:layout>
