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
                    <button class="item active" data-tab="call-view">전화</button>
                    <button class="item" data-tab="talk-view">상담톡</button>
                </div>
                <div class="ui bottom attached tab segment active" data-tab="call-view">
                    <div class="panel">
                        <div class="panel-heading">d</div>
                        <div class="panel-body">d</div>
                    </div>
                </div>
                <div class="ui bottom attached tab segment" data-tab="talk-view">
                  2222
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
