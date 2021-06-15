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


    <div class="billboard-wrap theme3">
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
                <div class="equal width row">
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
                <div class="sixteen wide column">
                    <div class="billboard-chart-wrap flex-flow-column full-height">
                        <div class="flex-100 billboard-chart-container">
                            dd
                        </div>
                        <div class="billboard-chart-label-wrap">
                            <ul>
                                <li><span class="symbol bcolor-bar1"></span><span class="text">응대율</span></li>
                                <li><span class="symbol bcolor-bar2"></span><span class="text">I/B전체</span></li>
                                <li><span class="symbol bcolor-bar3"></span><span class="text">연결요청</span></li>
                                <li><span class="symbol bcolor-bar4"></span><span class="text">응대호</span></li>
                                <li><span class="symbol bcolor-bar5"></span><span class="text">포기호</span></li>
                            </ul>
                        </div>
                    </div>
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
