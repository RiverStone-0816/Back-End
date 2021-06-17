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

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>

<tags:tabContentLayout>


    <div class="billboard-wrap">
        <div class="header">
            <div class="pull-left">
                전광판 타이틀
            </div>
            <div class="pull-right">
                <div class="time-wrap">2021-05-16 22:34:23</div>
            </div>
        </div>
        <div class="content">
            <div class="ui grid full-height remove-margin">
                <div class="equal width row" style="height:45%">
                    <div class="column">
                        <div class="board-box incoming-call full-height">
                            <div class="board-title flex-100">인입콜</div>
                            <div class="board-number flex-160">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box connection-request full-height">
                            <div class="board-title flex-100">연결요청</div>
                            <div class="board-number flex-160">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box reception full-height">
                            <div class="board-title flex-100">수신</div>
                            <div class="board-number flex-160">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box non-reception full-height">
                            <div class="board-title flex-100">비수신</div>
                            <div class="board-number flex-160">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-box response-rate full-height">
                            <div class="board-title flex-100">응대율</div>
                            <div class="board-number flex-160">185</div>
                        </div>
                    </div>
                </div>
                <div class="equal width row remove-padding" style="height:55%">
                    <table class="billboard-table">
                        <thead>
                            <tr>
                                <th>헌트명</th>
                                <th>고객대기</th>
                                <th>상담대기</th>
                                <th>통화중</th>
                                <th>후처리</th>
                                <th>기타</th>
                                <th>응대율</th>
                            </tr>
                        </thead>
                        <tbody class="active">
                            <tr>
                                <td>가나다라마바사</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                            </tr>
                            <tr>
                                <td>가나다라마바사</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                            </tr>
                            <tr>
                                <td>가나다라마바사</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                            </tr>
                            <tr>
                                <td>가나다라마바사</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                            </tr>
                            <tr>
                                <td>가나다라마바사</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                            </tr>
                        </tbody>
                        <tbody>
                        <tr>
                            <td>가나다라마바사</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                        </tr>
                        <tr>
                            <td>가나다라마바사</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                            <td>1</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="footer">
            공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구
        </div>
    </div>


    <tags:scripts>
        <script>
        </script>
    </tags:scripts>
</tags:tabContentLayout>
