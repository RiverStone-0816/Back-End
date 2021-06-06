<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:scripts/>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=440, initial-scale=0.8"/>
    <title>${serviceKind.equals('SC') ? 'IPCC 프리미엄' : '클컨 고객센터 PRO'}</title>
    <tags:favicon/>
    <tags:css/>
</head>
<body>

<div id="wrap">
    <tags:header/>

    <div id="main">
        <div class="consult-wrapper">
            <div class="left-panel">
                <div class="ui top attached tabular menu">
                    <button class="item" data-tab="call-view">전화</button>
                    <button class="item active" data-tab="talk-view">상담톡</button>
                </div>
                <div class="ui bottom attached tab segment" data-tab="call-view">
                    <div class="panel remove-margin">
                        <div class="panel-heading">
                            <div class="pull-left"><label class="panel-label">수발신정보</label></div>
                            <div class="pull-right"><button class="ui button right floated sharp">초기화</button></div>
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
            <div class="right-panel">
                <div class="panel remove-mb panel-resizable top">
                    <div class="panel-heading">
                        <div class="pull-left"><label class="panel-label">고객정보</label></div>
                        <div class="pull-right">
                            <div class="pull-right"><button class="ui button sharp light"><img src="<c:url value="/resources/images/save.svg"/>">신규고객정보 저장</button></div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="ui celled table compact unstackable">
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
                        <div class="pull-right"><button class="ui button sharp light"><img src="<c:url value="/resources/images/save.svg"/>">신규고객정보&상담결과 저장</button></div>
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
        </div>
        <%--<div class="content-wrapper">
            <jsp:doBody/>
        </div>
        <tags:nav/>--%>
    </div>
</div>





<div id="scripts">
    <tags:js/>
    <tags:alerts/>
    <tags:scripts method="pop"/>
</div>

</body>
</html>
