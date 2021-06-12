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
                2021-05-16 22:34:23
            </div>
        </div>
        <div class="content">
            <div class="ui grid full-height remove-margin">
                <div class="twelve wide column">
                    <div class="ui equal width grid">
                        <div class="equal width row">
                            <div class="column">
                                <div class="board-box red">
                                    <div class="board-title">1</div>
                                    <div class="board-number">1</div>
                                </div>
                            </div>
                            <div class="column">1</div>
                        </div>
                        <div class="column">1</div>
                        <div class="column">1</div>
                        <div class="column">1</div>
                    </div>
                </div>
                <div class="four wide column remove-padding">
                    <div class="ui one column grid full-height remove-margin">
                        <div class="column">
                            <div class="board-label">
                                <div class="left">대기</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">통화중</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">후처리</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">로그아웃</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">휴식</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">식사</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">이석</div>
                                <div class="right">100</div>
                            </div>
                        </div>
                        <div class="column">
                            <div class="board-label">
                                <div class="left">기타</div>
                                <div class="right">100</div>
                            </div>
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

    </tags:scripts>
</tags:tabContentLayout>
