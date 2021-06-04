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
            <div class="left">
                <div class="ui top attached tabular menu">
                    <button class="item" data-tab="call-view">전화</button>
                    <button class="item active" data-tab="talk-view">상담톡</button>
                </div>
                <div class="ui bottom attached tab segment" data-tab="call-view">
                    <div class="panel remove-margin">
                        <div class="panel-heading">
                            <label class="panel-label">수발신정보</label>
                            <button class="ui button right floated sharp">초기화</button>
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
                            <div class="ui top attached tabular menu flex">
                                <button class="item active" data-tab="monitoring">모니터링</button>
                                <button class="item" data-tab="statistics">통계</button>
                            </div>
                            <div class="ui bottom attached tab segment active remove-padding" data-tab="monitoring">
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
                <div class="ui bottom attached tab segment active" data-tab="talk-view">
                    <div class="ui top attached tabular menu flex">
                        <button class="item active" data-tab="talk-list-type-MY">상담중(1)</button>
                        <button class="item" data-tab="talk-list-type-TOT">비접수(1)</button>
                        <button class="item" data-tab="talk-list-type-OTH">타상담(1)</button>
                        <button class="item" data-tab="talk-list-type-END">종료(1)</button>
                    </div>
                    <div class="ui bottom attached tab segment active" data-tab="talk-list-type-MY">
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
                                        <div class="ui action input">
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
                    </div>
                    <div class="ui bottom attached tab segment" data-tab="talk-list-type-TOT">
                        통계
                    </div>
                    <div class="ui bottom attached tab segment" data-tab="talk-list-type-OTH">
                        통계
                    </div>
                    <div class="ui bottom attached tab segment" data-tab="talk-list-type-END">
                        통계
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="panel remove-mb panel-resizable top">
                    <div class="panel-heading">
                        <div class="pull-left">1</div>
                        <div class="pull-right">1</div>
                    </div>
                    <div class="panel-body">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="panel remove-mb panel-resizable middle">
                    <div class="panel-heading">
                        <div class="pull-left">1</div>
                        <div class="pull-right">1</div>
                    </div>
                    <div class="panel-body">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="panel bottom">
                    <div class="panel-heading">
                        <div class="pull-left">1</div>
                        <div class="pull-right">1</div>
                    </div>
                    <div class="panel-body">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                                <th>제목</th>
                                <td>제목</td>
                            </tr>
                        </table>
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
