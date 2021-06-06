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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<div class="consult-wrapper -counsel-panel">
    <div class="-counsel-content-panel left-panel" data-type="COUNSEL" style="display: none;">
        <div class="ui top attached tabular menu">
            <button class="item" data-tab="call-view">전화</button>
            <button class="item active" data-tab="talk-view">상담톡</button>
        </div>
        <div class="ui bottom attached tab segment" data-tab="call-view">
            <div class="panel remove-margin">
                <div class="panel-heading">
                    <div class="pull-left"><label class="panel-label">수발신정보</label></div>
                    <div class="pull-right">
                        <button class="ui button right floated sharp">초기화</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable">
                        <tr>
                            <th>전화상태</th>
                            <td></td>
                        </tr>
                        <tr>
                            <th>고객번호</th>
                            <td class="flex-td">
                                <div class="ui input fluid">
                                    <input type="text">
                                </div>
                                <button class="ui button sharp light">전화걸기</button>
                            </td>
                        </tr>
                        <tr>
                            <th>수신경로</th>
                            <td></td>
                        </tr>
                        <tr>
                            <th>고객정보</th>
                            <td>
                                <div class="ui form">
                                    <select>
                                        <option>고객정보 선택</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>통화이력</th>
                            <td>
                                <div class="ui form">
                                    <select>
                                        <option>통화이력 선택</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                    </table>
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
        </div>
        <div class="ui bottom attached tab segment remove-margin active" data-tab="talk-view">
            <div class="panel-resizable top-chat-list-wrap">
                <div class="ui top attached tabular menu light flex">
                    <button class="item active" data-tab="talk-list-type-MY">상담중(1)</button>
                    <button class="item" data-tab="talk-list-type-TOT">비접수(1)</button>
                    <button class="item" data-tab="talk-list-type-OTH">타상담(1)</button>
                    <button class="item" data-tab="talk-list-type-END">종료(1)</button>
                </div>
                <div class="ui bottom attached tab segment remove-margin active" data-tab="talk-list-type-MY">
                    <div class="sort-wrap">
                        <div class="ui form">
                            <div class="fields">
                                <div class="four wide field">
                                    <select>
                                        <option>고객명</option>
                                        <option>상담원명</option>
                                    </select>
                                </div>
                                <div class="nine wide field">
                                    <div class="ui input">
                                        <input type="text">
                                    </div>
                                </div>
                                <div class="three wide field">
                                    <select>
                                        <option>최근시간</option>
                                        <option>고객명</option>
                                        <option>상담원명</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="talk-list-container">
                        <ul>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                            <li class="item">
                                <div class="header">
                                    <div class="left"><span class="label">서비스 test</span><span class="customer">미등록 고객</span></div>
                                    <div class="right">상담원 : 홍길동</div>
                                </div>
                                <div class="content">
                                    <div class="left">마지막 메시지 출력</div>
                                    <div class="right">2021-05-21 09:00:00</div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="ui bottom attached tab segment remove-margin" data-tab="talk-list-type-TOT">
                    통계
                </div>
                <div class="ui bottom attached tab segment remove-margin" data-tab="talk-list-type-OTH">
                    통계
                </div>
                <div class="ui bottom attached tab segment remove-margin" data-tab="talk-list-type-END">
                    통계
                </div>
            </div>
            <div class="btm-room-wrap">
                <div class="chat-container">
                    <div class="room">
                        <div class="chat-header"></div>
                        <div class="chat-body">
                            <div class="chat-item chat-me">
                                <div class="wrap-content">
                                    <div class="txt-time">[김옥중] 2019-07-18 17:56:17</div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <p class="txt_chat">테스트</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="chat-item">
                                <div class="wrap-content">
                                    <div class="txt-time">[김옥중] 2019-07-18 17:56:17</div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <p class="txt_chat">TEST TEST TEST TEST TEST TEST TEST TEST TEST
                                                TEST TEST TEST TEST TEST TEST </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <p class="info-msg">[2019-07-16 17:50:10] [김옥중어드민]상담건을 찜하였습니다. TEXT</p>
                            <div class="chat-item">
                                <div class="wrap-content">
                                    <div class="txt-time">[김옥중] 2019-07-18 17:56:17</div>
                                    <div class="chat">
                                        <div class="bubble">
                                            <p class="txt_chat">
                                                <img src="https://ddragon.leagueoflegends.com/cdn/img/champion/tiles/Yuumi_0.jpg">
                                            </p>
                                        </div>
                                    </div>
                                    <a href="#">저장하기</a>
                                </div>
                            </div>
                        </div>

                        <div class="write-chat">
                            <div class="write-menu">
                                <button type="button" class="mini ui button compact">템플릿</button>
                                <button type="button" class="mini ui button compact">파일전송</button>
                            </div>
                            <div class="wrap-inp">
                                <div class="inp-box">
                                    <textarea id="talk-message" placeholder="전송하실 메시지를 입력하세요."></textarea>
                                </div>
                                <button type="button" class="send-btn">전송</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="-counsel-content-panel right-panel" data-type="COUNSEL" style="display: none;">
        <div class="panel remove-mb panel-resizable top">
            <div class="panel-heading">
                <div class="pull-left"><label class="panel-label">고객정보</label></div>
                <div class="pull-right">
                    <div class="pull-right">
                        <button class="ui button sharp light"><img src="<c:url value="/resources/images/save.svg"/>">신규고객정보 저장</button>
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <table class="ui celled table compact unstackable fixed">
                    <tr>
                        <th>고객DB</th>
                        <td colspan="3">
                            <div class="ui form flex">
                                <select>
                                    <option>고객정보 선택</option>
                                </select>
                                <select>
                                    <option>김민호 테스트</option>
                                </select>
                            </div>
                        </td>
                        <th>기념일1</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                        <th>기념일2</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>날짜</th>
                        <td>
                            <div class="ui form">
                                <div class="fields">
                                    <div class="eight wide field remove-pl">
                                        <input type="text" placeholder="날짜">
                                    </div>
                                    <div class="four wide field remove-padding">
                                        <input type="text" placeholder="시">
                                    </div>
                                    <div class="four wide field remove-pr">
                                        <input type="text" placeholder="분">
                                    </div>
                                </div>
                            </div>
                        </td>
                        <th>나이</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                        <th>고객명</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                        <th>소속</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>회사명</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                        <th>고객명</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                        <th>주소</th>
                        <td>
                            <div class="ui form">
                                <input type="text">
                            </div>
                        </td>
                        <th>고객등급</th>
                        <td>
                            <div class="ui form flex">
                                <select>
                                    <option>선택</option>
                                </select>
                                <button class="ui button sharp brand">상세</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>이미지</th>
                        <td colspan="7">
                            <div class="file-upload-header">
                                <label for="file" class="ui button sharp light mini compact">파일찾기</label>
                                <input type="file" id="file">
                                <span class="file-name">No file selected</span>
                            </div>
                            <div class="ui orange tiny progress" data-percent="49">
                                <div class="bar"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>멀티채널추가</th>
                        <td colspan="7">
                            <div class="ui grid remove-margin">
                                <div class="row remove-padding">
                                    <div class="twelve wide column remove-pl">
                                        <div class="ui form">
                                            <select name="channels" multiple="multiple" class="one-multiselect">
                                                <c:forEach var="c" items="${entity.multichannelList}">
                                                    <option value="${c.channelData}" data-type="${g.htmlQuote(c.channelType)}">
                                                        [${c.channelType == 'TALK' ? g.htmlQuote(talkServices.get(c.channelData.split('[_]')[0])) : g.htmlQuote(channelTypes.get(c.channelType))}]
                                                            ${g.htmlQuote(c.channelType == 'TALK' ? c.channelData.split('[_]')[1] : c.channelData)}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="four wide column remove-padding">
                                        <div class="ui form">
                                            <div class="field">
                                                <button type="button" class="ui button sharp light fluid -remove-channel">삭제</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row remove-pb">
                                    <div class="four wide column remove-pl">
                                        <div class="ui form">
                                            <select name="channelType">
                                                <c:forEach var="e" items="${channelTypes}">
                                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="eight wide column">
                                        <div class="ui form flex">
                                            <div class="field" style="flex: 1; display: none;">
                                                <select name="channelDataTalkService">
                                                    <c:forEach var="e" items="${talkServices}">
                                                        <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.value)}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="ui fluid action input">
                                                <input type="text" name="channelData">
                                                <button class="ui icon button -add-channel">추가</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="panel remove-mb panel-resizable middle">
            <div class="panel-heading">
                <div class="pull-left"><label class="panel-label">상담결과 입력</label></div>
                <div class="pull-right">
                    <button class="ui button sharp light"><img src="<c:url value="/resources/images/save.svg"/>">신규고객정보&상담결과 저장</button>
                </div>
            </div>
            <div class="panel-body">
                <table class="ui celled table compact unstackable">
                    <tr>
                        <th>관련서비스</th>
                        <td>
                            <div class="ui form">
                                <select>
                                    <option>선택</option>
                                </select>
                            </div>
                        </td>
                        <th>상담종류</th>
                        <td>
                            <div class="ui form">
                                <select>
                                    <option>선택</option>
                                </select>
                            </div>
                        </td>
                        <th>처리여부</th>
                        <td>
                            <div class="ui form">
                                <select>
                                    <option>선택</option>
                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>메모</th>
                        <td colspan="5">
                            <div class="ui form">
                                <div class="field">
                                    <textarea rows="2"></textarea>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div>
            <div class="ui top attached tabular menu light flex remove-margin">
                <button class="item active" data-tab="todo">To-Do</button>
                <button class="item" data-tab="consult-history">상담이력</button>
                <button class="item" data-tab="etc-lookup">기타조회</button>
            </div>
            <div class="ui bottom attached tab segment active" data-tab="todo">
                <table class="ui celled table unstackable">
                    <thead>
                    <tr>
                        <th>채널</th>
                        <th>요청시간</th>
                        <th>고객정보</th>
                        <th>처리상태</th>
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
            <div class="ui bottom attached tab segment" data-tab="consult-history">
                <table class="ui celled table unstackable">
                    <thead>
                    <tr>
                        <th>채널</th>
                        <th>수/발신</th>
                        <th>상담등록시간</th>
                        <th>전화번호</th>
                        <th>상담톡아이디</th>
                        <th>상담원</th>
                        <th>자세히</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
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
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
                    </tr>
                    <tr>
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
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
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
                    </tr>
                    <tr>
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
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
                        <td>-</td>
                        <td>-</td>
                        <td>-</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="ui bottom attached tab segment" data-tab="etc-lookup">
                기타조회
            </div>
        </div>

    </div>

    <iframe class="content-inner -counsel-content-panel" data-type="NOTICE" src="<c:url value="/admin/service/help/notice/"/>" style="display: none;"></iframe>
    <iframe class="content-inner -counsel-content-panel" data-type="KNOWLEDGE" src="<c:url value="/admin/service/help/task-script/"/>" style="display: none;"></iframe>
    <iframe class="content-inner -counsel-content-panel" data-type="CALENDAR" src="<c:url value="/user-schedule/"/>" style="display: none;"></iframe>

    <jsp:include page="/counsel/modal-calling"/>
    <jsp:include page="/counsel/modal-route-application"/>
    <jsp:include page="/counsel/modal-ars"/>
    <jsp:include page="/counsel/modal-cms"/>
    <jsp:include page="/counsel/modal-send-message"/>
</div>

<jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
<tags:scripts>
    <script id="toast-reservation-script">
        window.toastr.options = {
            "closeButton": true,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-bottom-right",
            "preventDuplicates": false,
            "onclick": function (event) {
                $('.toast .toast-close-button').focus();
            },
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "0",
            "extendedTimeOut": "0",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };

        const toastedCalling = {};
        const toastingCalling = [];

        function setReservation() {
            return restSelf.get('/api/counsel/recent-consultation-reservation', null, function () {
            }, true).done(function (response) {
                for (let i = 0; i < response.data.length; i++) {
                    toastingCalling.push(response.data[i]);
                }
            });
        }

        function toastReservation() {
            if (toastingCalling.length <= 0)
                return;

            toastingCalling.sort(function (a, b) {
                return a.time - b.time;
            });

            const now = new Date().getTime();

            for (let i = 0; i < toastingCalling.length; i++) {
                const e = toastingCalling.shift();
                if (e.time > now) {
                    toastingCalling.unshift(e);
                    return;
                }

                noticeReservation(e.todoInfo, parseInt(e.detailConnectInfo));
            }
        }

        function noticeReservation(phone, time) {
            const preTime = toastedCalling[phone];
            if (preTime) {
                if (preTime + 1000 * 60 * 5 > time)
                    return;
            }

            toastedCalling[phone] = time;

            const div = $('<div/>').append(
                $('<div/>', {onclick: "$(this).closest('.toast').find('.toast-close-button').click(); $('#calling-number').val('" + phone + "');", style: 'cursor: pointer;'})
                    .append($('<text/>', {text: '[' + moment(time).format('HH:mm') + '] '}))
                    .append($('<b/>', {text: phone}))
            );
            toastr.warning(div.html(), '통화약속');
        }

        $(document).ready(function () {
            setReservation();
            if ($(parent.document).find('#main').is('.change-mode')) {
                setInterval(setReservation, 60000);
            }
            setInterval(toastReservation, 3000);
        });
    </script>
    <script>
        const ITEM_KEY_CONFIG = "counselDisplayConfiguration";
        let counselDisplayConfiguration = {useCallNoticePopup: true};

        function viewCallPanel() {
            $('#talk-panel').removeClass('active');
            $('#call-panel').addClass('active');
            $('#call-custom-input-panel,#call-counseling-input-panel').show();
            $('#talk-custom-input-panel,#talk-counseling-input-panel').hide();

            $('#etc-panel-resizer').hide();
            callExpandEtcPanel();
        }

        function viewTalkPanel() {
            $('#call-panel').removeClass('active');
            $('#talk-panel').addClass('active');
            $('#call-custom-input-panel,#call-counseling-input-panel').hide();
            $('#talk-custom-input-panel,#talk-counseling-input-panel').show();

            $('#etc-panel-resizer').show();
            reduceEtcPanel();
        }

        function expandEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_down');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $('#talk-panel').addClass('reduce-panel');
            $('#etc-panel').addClass('expand-panel');
            $("#etc-panel .os-content").show();
        }

        function callExpandEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_up');
            $('#talk-panel').removeClass('expand-panel');
            $('#etc-panel').removeClass('reduce-panel');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $("#etc-panel .os-content").show();
        }

        function reduceEtcPanel() {
            $('#etc-panel-resizer').find('i').text('keyboard_arrow_up');
            $('#talk-panel').removeClass('reduce-panel');
            $('#etc-panel').removeClass('expand-panel');
            $('#talk-panel').addClass('expand-panel');
            $('#etc-panel').addClass('reduce-panel');
            $("#etc-panel .os-content").hide();
        }

        $('#etc-panel-resizer').click(function () {
            if ($(this).find('i').text() === 'keyboard_arrow_down')
                reduceEtcPanel();
            else
                expandEtcPanel();
        });

        $('.-counsel-panel-indicator').click(function () {
            $('.-counsel-panel-indicator').removeClass('active');
            $(this).addClass('active');
        });

        function popupFieldInfo(type, fieldId, selectValue) {
            var isEmpty = function (value) {
                if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
                    return true
                } else {
                    return false
                }
            };
            if (isEmpty(selectValue)) {
                alert('선택하세요.');
                return;
            }
            popupDraggableModalFromReceivedHtml($.addQueryString('/counsel/modal-field-info', {type: type, fieldId: fieldId, selectItem: selectValue}), 'modal-field-info');
        }


        function popupSearchMaindbCustomModal(type, roomId, senderKey, userKey) {
            popupDraggableModalFromReceivedHtml($.addQueryString('/counsel/modal-search-maindb-custom', {
                type: type || 'CALL',
                roomId: roomId || '',
                senderKey: senderKey || '',
                userKey: userKey || ''
            }), 'modal-search-maindb-custom');
        }

        function popupSearchCounselingHistoryModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-counseling-history', 'modal-search-counseling-history');
        }

        function popupSearchCallHistoryModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-call-history', 'modal-search-call-history');
        }

        function popupSearchCallbackModal() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-search-callback-history', 'modal-search-callback-history');
        }

        function popupCounselDisplayConfiguration() {
            popupDraggableModalFromReceivedHtml('/counsel/modal-config', 'modal-config');
        }

        function popupCounselingInfo(seq) {
            popupDraggableModalFromReceivedHtml('/counsel/modal-counseling-info?seq=' + seq, 'modal-counseling-info');
        }

        function setAlertCurrentStatus() {
            const ui = $('#current-status-sheet');

            const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
            if (!stringify)
                return;

            const data = JSON.parse(stringify);

            keys(data).map(function (key) {
                const tr = ui.find('[data-name="' + key + '"]');
                const value = parseInt(tr.attr('data-value'));
                if (value !== 0 && (!value || isNaN(value))) return;

                const threshold = parseInt(data[key]);
                if (!threshold || isNaN(threshold)) return;

                if (threshold >= value)
                    tr.addClass('negative');
                else
                    tr.removeClass('negative');
            });
        }

        function setConfiguredTab() {
            $('.-configured-indicator,.-configured-tab').hide();

            const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
            if (!stringify)
                return;

            const data = JSON.parse(stringify);

            keys(data).map(function (key) {
                $('.-configured-indicator').filter(function () {
                    return $(this).attr('data-tab') === key;
                }).show();

                $('.-configured-tab').filter(function () {
                    return $(this).attr('data-tab') === key;
                }).css('display', '');
            });

            if ($('.-configured-indicator.active').css('display') === 'none') {
                $('.-counsel-panel-indicator[data-tab="call-panel"]').click();
            } else {
                $('.-configured-indicator.active').click();
            }
        }

        $("#etc-panel .segment").overlayScrollbars({});

        function loadCoworkerNavigation() {
            replaceReceivedHtmlInSilence('/counsel/coworker-navigation', '#counsel-nav');
        }

        function loadOuterLink() {
            replaceReceivedHtmlInSilence('/counsel/outer-link', '#outer-link-list');
        }

        function loadCurrentStatus() {
            replaceReceivedHtmlInSilence('/counsel/current-status', '#current-status-sheet');
        }

        function loadTodoList() {
            replaceReceivedHtmlInSilence('/counsel/todo-list', '#todo-list');
        }

        function loadCounselingList(customId) {
            if (customId && customId !== '') {
                replaceReceivedHtmlInSilence($.addQueryString('/counsel/counsel-list', {customId: customId}), '#counsel-list');
            } else {
                $('#counsel-list').empty();
            }
        }

        $(window).on('load', function () {
            loadCoworkerNavigation();
            loadOuterLink();
            loadCurrentStatus();
            setInterval(function () {
                if ($(parent.document).find('#main').is('.change-mode')) {
                    loadCoworkerNavigation();
                    loadOuterLink();
                    loadCurrentStatus();
                    loadTodoList();
                }
            }, 30 * 1000);

            $('#call-control-panel').show();

            (function () {
                const configString = localStorage.getItem(ITEM_KEY_CONFIG);
                if (!configString) {
                    localStorage.setItem(ITEM_KEY_CONFIG, JSON.stringify(counselDisplayConfiguration));
                } else {
                    counselDisplayConfiguration = JSON.parse(configString);
                }
            })();

            setAlertCurrentStatus();
            setConfiguredTab();
        });
    </script>
    <script>
        const services = {<c:forEach var="e" items="${services}">'${g.escapeQuote(e.key)}': '${g.escapeQuote(e.value)}', </c:forEach>};

        let currentUserStatus = null;
        let currentUserStatusChangedAt = null;
        setInterval(function () {
            $('.-call-waiting-time').each(function () {
                $(this).text((parseInt($(this).text()) + 1) + '초');
            });

            if (currentUserStatusChangedAt !== null) {
                const time = parseInt((new Date().getTime() - currentUserStatusChangedAt) / 1000);
                $('#status-keeping-time').text(pad(parseInt(time / 60), 2) + ":" + pad(time % 60, 2));
            }
        }, 1000);

        $(window).on('load', function () {
            updateQueues();
            updatePersonStatus();

            /*TODO: 임시처리*/
            $('.-configured-tab[data-tab=menu1]').empty().append($('<iframe/>', {id: 'menu1', name: 'menu1', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            $('.-configured-tab[data-tab=menu2]').empty().append($('<iframe/>', {id: 'menu2', name: 'menu2', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            $('.-configured-tab[data-tab=menu3]').empty().append($('<iframe/>', {id: 'menu3', name: 'menu3', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            <c:if test="${usingServices.contains('NOT')}">
            $('.-configured-tab[data-tab=menu4]').empty().append($('<iframe/>', {id: 'menu4', name: 'menu4', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            $('.-configured-tab[data-tab=menu5]').empty().append($('<iframe/>', {id: 'menu5', name: 'menu5', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            $('.-configured-tab[data-tab=menu6]').empty().append($('<iframe/>', {id: 'menu6', name: 'menu6', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            </c:if>
            <c:if test="${serviceKind.equals('SC')}">
            $('.-configured-tab[data-tab=menu7]').empty().append($('<iframe/>', {id: 'menu7', name: 'menu7', class: 'tab-menu', style: 'width: 100%; height: 100%;'}));
            </c:if>
            // TODO
            <%--window.menu1 = window.open(contextPath + '/admin/application/maindb/result/', 'menu1', "width=0 height=0 menubar=no status=no");--%>
            <%--window.menu2 = window.open(contextPath + '/admin/record/history/history/', 'menu2', "width=0 height=0 menubar=no status=no");--%>
            <%--window.menu3 = window.open(contextPath + '/admin/record/callback/history/', 'menu3', "width=0 height=0 menubar=no status=no");--%>
            <%--<c:if test="${usingServices.contains('NOT')}">--%>
            <%--window.menu4 = window.open(contextPath + '/admin/service/help/notice/', 'menu4', "width=0 height=0 menubar=no status=no");--%>
            <%--window.menu5 = window.open(contextPath + '/admin/service/help/task-script/', 'menu5', "width=0 height=0 menubar=no status=no");--%>
            <%--window.menu6 = window.open(contextPath + '/admin/service/help/manual/', 'menu6', "width=0 height=0 menubar=no status=no");--%>
            <%--</c:if>--%>
            <%--<c:if test="${serviceKind.equals('SC') && usingServices.contains('QA')}">--%>
            <%--window.menu7 = window.open(contextPath + '/admin/record/evaluation/result/', 'menu7', "width=0 height=0 menubar=no status=no");--%>
            <%--</c:if>--%>
        });
    </script>
    <script>
        window.ipccCommunicator = new IpccCommunicator();

        let audioId, phoneNumber;
        ipccCommunicator
            .on('LOGIN', function (message, kind /*[ LOGIN_OK | LOGIN_ALREADY | LOGOUT | ... ]*/) {
                if (kind === "LOGOUT")
                    return alert("로그아웃");
                if (kind === 'LOGIN_OK' || kind === 'LOGIN_ALREADY')
                    return;
                alert("IPCC 웹소켓 로긴 실패");
            })
            .on('PEER', function (message, kind, data1, data2 /*[ OK | REGISTERED | REACHABLE | NOK | UNREACHABLE | UNREGISTERED | ... ]*/) {
            })
            .on('MEMBERSTATUS', function (message, kind) {
                const status = parseInt(kind);
                $('.-member-status')
                    .removeClass("active")
                    .filter(function () {
                        return parseInt($(this).attr('data-status')) === status;
                    }).addClass("active");

                currentUserStatus = status;
                currentUserStatusChangedAt = new Date().getTime();
            })
            .on('PDSMEMBERSTATUS', function (message, kind) {
                if (!$.isNumeric(pdsStatus))
                    return;

                const status = parseInt(kind);

                $('.-member-status-pds')
                    .removeClass("active")
                    .filter(function () {
                        return parseInt($(this).attr('data-status')) === status;
                    }).addClass("active");

                currentUserStatusChangedAt = new Date().getTime();
            })
            .on('CALLEVENT', function (message, kind /*[ IR | ID | OR | OD | ... ]*/, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12, data13) {
                if (kind === "PICKUP" || kind === "ID" || kind === "OD")
                    $('#partial-recoding').show().find('text').text('부분녹취');

                if (kind === 'IR') { // 인바운드 링울림
                    audioId = data8;
                    phoneNumber = data1;
                    const callingPath = data3;
                    const extension = data2;
                    const secondNum = data4;

                    const candidateQueues = values(queues).filter(function (queue) {
                        return queue.number === secondNum;
                    });

                    const queueName = candidateQueues && candidateQueues.length > 0 ? candidateQueues[0].hanName : null;

                    $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('.-call-waiting-time').text('0초');
                    $('#call-status').text('[수신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
                    loadUserCustomInfo(phoneNumber);
                    $('#user-call-history').empty().append('<option>통화진행중</option>');

                    if (counselDisplayConfiguration.useCallNoticePopup)
                        viewCallReception();

                    loadCustomInput(null, null, phoneNumber).done(function () {
                        $('.item[data-tab="counsel-list"]').click();
                    });
                } else if (kind === 'PICKUP') { //픽업
                    audioId = data8;
                    phoneNumber = data1;
                    const callingPath = data3;
                    const extension = data2;
                    const secondNum = data4;

                    const candidateQueues = values(queues).filter(function (queue) {
                        return queue.number === secondNum;
                    });

                    const queueName = candidateQueues && candidateQueues.length > 0 ? candidateQueues[0].hanName : null;

                    $(".-calling-path").text((services[callingPath] != null ? services[callingPath] + '(' + callingPath + ')' : callingPath) + (queueName ? ' ' + queueName + '(' + secondNum + ')' : ''));
                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('.-call-waiting-time').text('0초');
                    $('#call-status').text('[수신] 당겨받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');

                    /*if (counselDisplayConfiguration.useCallNoticePopup)
                        viewCallReception();
*/
                    loadCustomInput(null, null, phoneNumber).done(function () {
                        $('.item[data-tab="counsel-list"]').click();
                    });
                } else if (kind === 'ID') { // 인바운드 통화시작
                    audioId = data8;
                    phoneNumber = data1;

                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('#modal-calling').modalHide();
                    $('#call-status').text('[수신] 전화받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');
                } else if (kind === 'OR') { // 아웃바운드 링울림
                    audioId = data8;
                    phoneNumber = data2;

                    if (data9 === 'PRV') {
                        const previewGroupId = data11;
                        const previewCustomId = data12;

                        loadPreviewCustomInput(previewGroupId, previewCustomId);
                        loadPreviewCounselingInput(previewGroupId, previewCustomId);
                    } else {
                        $('.-calling-number').val(phoneNumber).text(phoneNumber);
                        $('#call-status').text('[발신] 전화벨 울림 [' + moment().format('HH시 mm분') + ']');
                        $('#user-call-history').empty().append('<option>통화진행중</option>');

                        loadCustomInput(null, null, phoneNumber).done(function () {
                            $('.item[data-tab="counsel-list"]').click();
                        });
                    }
                } else if (kind === 'OD') { // 아웃바운드 통화시작
                    audioId = data8;
                    phoneNumber = data2;

                    $('.-calling-number').val(phoneNumber).text(phoneNumber);
                    $('#call-status').text('[발신] 전화받음 [' + moment().format('HH시 mm분') + ']');
                    $('#user-call-history').empty().append('<option>통화진행중</option>');
                }
            })
            .on('HANGUPEVENT', function () {
                $('#partial-recoding').hide();
                $('#call-status').text('통화종료');

                // IR 이후, 곧장 HANGUP이 일어나면 modal의 transition때문에 옳바르게 dimmer가 상태변화되지 못한다.
                setTimeout(function () {
                    $('#modal-calling').modalHide();
                }, 1000);

                //HANGUP이 일어나고 DB에 데이터가 쌓이는 시간 감안
                setTimeout(function () {
                    loadUserCallHistory();
                }, 5000);
            })
            .on('FORWARDING', function (message, kind/*[ OK | ... ]*/, data1, data2/*[ (공백) | A | B | C | T | ... ]*/) {
            })
            .on('CALLSTATUS', function (message, kind/*[ REDIRECT | ... ]*/, data1, data2/*[ NOCHAN | BUSY ]*/) {
            })
            .on('DTMFREADEVENT', function (message, kind) {
            })
            .on('PDSMEMBERSTATUS', function (message, kind) {
            })
            .on('MSG', function (message, kind, data1, data2) {
                if (!data1 || !data2)
                    return;

                const decodeMessage = decodeURI(data2).replace(/[ψ\n]/gi, "<br/>");
                toastr.info(data1, decodeMessage);
            })
            .on('CMS', function (message, kind, data1) {
                if (kind === 'OK') {
                    $('#modal-cms-popup').modalHide();
                    return alert('적용되었습니다.');
                } else {
                    return alert('실패하였습니다: ' + data1);
                }
            })
            .on('REC_START'/*부분녹취 시작*/, function (message, kind/*[ NOK | STARTOK ]*/, data1, partialRecordCnt) {
                const partialRecord = $('#partial-recoding');
                if (kind === 'NOK') {
                    console.error("부분녹취실패 : {}", message);
                    return;
                }
                if (kind === 'STARTOK')
                    $(partialRecord).find('text').text('부분녹취(' + partialRecordCnt + ")");
            })
            .on('REC_STOP'/*부분녹취 중지*/, function (message, kind/*[ NOK | STOPOK ]*/, data1, partialRecordCnt) {
                const partialRecord = $('#partial-recoding');
                if (kind === 'NOK') {
                    console.error("부분녹취실패 : {}", message);
                    return;
                }
                if (kind === 'STOPOK')
                    $(partialRecord).find('text').text('부분녹취(' + partialRecordCnt + ")");
            })
            .on('BYE', function (message, kind/*[ SAME_UID | SAME_PID ]*/, data1, data2, data3) {
                switch (kind) {
                    case"SAME_UID":
                        return alert("다른 컴퓨터에서 같은 아이디로 로긴되어서 서버와 끊김");
                    case "SAME_PID":
                        return alert("다른 컴퓨터에서 같은 내선으로로 로긴되어서 서버와 끊김");
                    default:
                        return alert("[" + kind + "]" + data3 + "(" + data1 + ")");
                }
            });


        let pdsStatus;

        $(window).on('load', function () {
            restSelf.get('/api/auth/socket-info').done(function (response) {
                const fromUi = "EICN_IPCC";
                if (response.data.extension != null)
                    ipccCommunicator.connect(response.data.callControlSocketUrl, response.data.pbxHost, response.data.companyId, response.data.userId, response.data.extension, hex_sha512(response.data.password), response.data.idType, fromUi, response.data.isMulti);

                <c:if test="${user.isTalk.equals('Y')}">
                talkCommunicator.connect(response.data.talkSocketUrl, response.data.companyId, response.data.groupCode, response.data.groupTreeName, response.data.groupLevel, response.data.userId, response.data.userName, "USER", response.data.idType);
                </c:if>
            });

            $(".-call-reject").click(function () {
                $('#modal-calling').modalHide();
                ipccCommunicator.reject();
            });

            $(".-call-receive").click(function () {
                $('#modal-calling').modalHide();
                ipccCommunicator.receiveCall();
            });

            $(".-call-hangup").click(function () {
                ipccCommunicator.hangUp();
                ipccCommunicator.stopMultiCall();
            });

            $('.-call-pickup').click(function () {
                ipccCommunicator.pickUp();
            });

            $(".-call-hold").click(function () {
                if (ipccCommunicator.status.cMemberStatus !== MEMBER_STATUS_CALLING)
                    return;

                if ($(this).hasClass("active")) {
                    ipccCommunicator.stopHolding();
                    $(this).removeClass("active");
                } else {
                    ipccCommunicator.startHolding();
                    $(this).addClass("active");
                }
            });

            $('#partial-recoding').click(function () {
                if ($(this).find('i').hasClass('fa-play')) {
                    ipccCommunicator.startRecording();
                    const recordIcon = $(this).find('i');
                    recordIcon.removeClass('fa-play').addClass('fa-stop').addClass("partial-record-stop").show();
                } else {
                    ipccCommunicator.stopRecording();
                    $(this).find('i').removeClass('fa-stop').removeClass("partial-record-stop").addClass('fa-play').show();
                }
            });
        });
    </script>
</tags:scripts>
