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
                <div class="menu-btn-wrap"><button class="menu-open-btn" id="billboard-popup-btn" onclick="billboardPopup()"></button></div>
                <div class="time-wrap">2021-05-16 22:34:23</div>
            </div>
        </div>
        <div class="content">
            <div class="ui grid full-height remove-margin flex-flow-column">
                <div class="equal width row height-13rem">
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">가용율</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">대기</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">통화중</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">후처리</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">휴식</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">식사</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">이석</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">기타</div>
                            <div class="bottom">185</div>
                        </div>
                    </div>
                </div>
                <div class="row flex-100">
                    <div id="billboard-tab1" class="sixteen wide column remove-pb billboard-tab-content current">
                        <div class="ui five column grid full-height">
                            <div class="column">
                                <div class="user-label">
                                    <div class="left call"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left after"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left etc"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left meal"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left move"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left rest"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left stay"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="billboard-tab2" class="sixteen wide column remove-pb billboard-tab-content">
                        <div class="ui five column grid full-height">
                            <div class="column">
                                <div class="user-label">
                                    <div class="left call"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left after"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left etc"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <ul class="billboard-tabs">
                    <li class="tab-link current" data-tab="billboard-tab1"></li>
                    <li class="tab-link" data-tab="billboard-tab2"></li>
                </ul>
            </div>
        </div>
        <div class="footer">
            공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구
        </div>
    </div>

    <div class="ui mini modal theme3" id="modal-billboard">
        <i class="close icon"></i>
        <div class="content">
            <div class="billboard-inner">
                <label class="title">상태별 강조 시간</label>
                <div class="inner">
                    <ul>
                        <li>
                            <div class="left"><span class="symbol state-wait"></span>대기</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol state-call"></span>통화</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol state-after"></span>후처리</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol state-etc"></span>휴식</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol state-etc"></span>식사</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol state-etc"></span>이석</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol state-etc"></span>기타</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui fluid button">저장</button>
        </div>
    </div>


    <tags:scripts>

        <script>
            $('.billboard-tabs li').click(function(){
                var tab_id = $(this).attr('data-tab');

                $('.billboard-tabs li').removeClass('current');
                $('.billboard-tab-content').removeClass('current');

                $(this).addClass('current');
                $("#"+tab_id).addClass('current');
            })

            function billboardPopup() {
                $('#modal-billboard').modalShow();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
