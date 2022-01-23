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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stat/total/"/>
        <div class="sub-content ui container fluid">
            <form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">챗봇 리스트 조회</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body overflow-unset">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>기간설정</th>
                                <td>
                                    <div class="ui action input calendar-area">
                                        <input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>봇이름</th>
                                <td>
                                    <div class="ui form">
                                        <select>
                                            <option>봇이름1</option>
                                            <option>봇이름1</option>
                                            <option>봇이름1</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>4</span> 건</h3>
                        <div class="ui basic buttons">
                            <button type="button" class="ui button" onclick="chatbotAddPopup()">추가</button>
                            <button type="button" class="ui button" onclick="chatbotModifyPopup()">
                                수정
                            </button>
                            <button type="button" class="ui button" onclick="chatbotDeletePopup()">
                                삭제
                            </button>
                        </div>
                    </div>
                    <div class="pull-right">
                        <div class="ui pagination menu pull-right small">
                            <a href="#" class="item active">1</a>
                            <a href="#" class="item">2</a>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable selectable-only">
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>번호</th>
                            <th class="two wide">봇생성일</th>
                            <th class="two wide">담당자</th>
                            <th>봇 이름</th>
                            <th class="one wide">폴백</th>
                            <th class="one wide">복사</th>
                            <th class="one wide">테스트</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <div class="ui radio checkbox">
                                    <input type="radio" name="radio" tabindex="0" class="hidden"><label></label>
                                </div>
                            </td>
                            <td>1</td>
                            <td>2022-01-06 15:30:12</td>
                            <td>마스터</td>
                            <td>봇이름</td>
                            <td>
                                <button class="ui button mini compact" onclick="fallbackBlockPopup();">설정</button>
                            </td>
                            <td>
                                <button class="ui button mini compact" onclick="chatbotCopyPopup();">복사</button>
                            </td>
                            <td>
                                <button class="ui button mini compact" onclick="chatbotTestPopup();">테스트</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal tiny" id="chatbot-add-popup">
        <i class="close icon"></i>
        <div class="header">봇 추가</div>
        <div class="content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">봇 이름</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid"><input type="text"></div>
                        <p class="mt10">한번 설정한 봇 이름은 수정할 수 없습니다.</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui brand button">확인</button>
        </div>
    </div>

    <div class="ui modal tiny" id="fallback-block-popup">
        <i class="close icon"></i>
        <div class="header">폴백블록</div>
        <div class="content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">멘트입력</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="field">
                                <textarea rows="3" readonly></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">동작선택</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select id="actionSelect">
                                <option>유형선택</option>
                                <option>처음으로 가기</option>
                                <option value="connectBlock">다른 블록으로 연결</option>
                                <option value="consultGroup">상담그룹 연결</option>
                                <option value="connectUrl">URL 연결</option>
                                <option value="connectNumber">전화 연결</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectBlock">
                    <div class="four wide column"><label class="control-label">연결 블록 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>블록선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="consultGroup">
                    <div class="four wide column"><label class="control-label">상담그룹</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>그룹선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectUrl">
                    <div class="four wide column"><label class="control-label">연결 URL 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input type="text" placeholder="URL을 입력하세요.">
                        </div>
                    </div>
                </div>
                <div class="row fallback-action" id="connectNumber">
                    <div class="four wide column"><label class="control-label">연결 번호 설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <input type="text" placeholder="전화번호를 입력하세요.">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="submit" class="ui left floated button">미리보기</button>
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui brand button">확인</button>
        </div>
    </div>

    <%-- 미리보기 팝업--%>
    <div class="ui preview modal">
        <i class="close icon"></i>
        <div class="header">미리보기</div>
        <div class="content">
            <div class="preview-header">
                <img src="<c:url value="/resources/images/chatbot-icon-orange.svg"/>"><span>Chat Bot</span>
            </div>
            <div class="preview-content">
                <div class="sample-bubble">
                    텍스트 설정에서 입력한 텍스트가 노출. 텍스트 설정에서 입력한 텍스트가 노출.
                    텍스트 설정에서 입력한 텍스트가 노출. 텍스트 설정에서 입력한 텍스트가 노출.
                    <span class="time-text">22-12-12 12:12</span>
                </div>
                <div class="card">
                    <div class="card-img only">
                        <img src="https://recipe1.ezmember.co.kr/cache/recipe/2018/12/14/2e5a56658f3abe62fa741b2958e3354e1.jpg">
                    </div>
                </div>
                <div class="card">
                    <div class="card-img">
                        <img src="https://recipe1.ezmember.co.kr/cache/recipe/2018/12/14/2e5a56658f3abe62fa741b2958e3354e1.jpg">
                    </div>
                    <div class="card-content">
                        <div class="card-title">타이틀</div>
                        <div class="card-text">
                            타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀타이틀
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-list">
                        <div class="card-list-title">
                            <a href="#" target="_blank">ddddd</a>
                        </div>
                        <ul class="card-list-ul">
                            <li class="item">
                                <a href="ttt" target="_blank" class="link-wrap">
                                    <div class="item-content">
                                        <div class="subject">제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목</div>
                                        <div class="ment">내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용</div>
                                    </div>
                                </a>
                            </li>
                            <li class="item">
                                <a href="ttt" target="_blank" class="link-wrap">
                                    <div class="item-thumb">
                                        <div class="item-thumb-inner">
                                            <img src="https://recipe1.ezmember.co.kr/cache/recipe/2018/12/14/2e5a56658f3abe62fa741b2958e3354e1.jpg">
                                        </div>
                                    </div>
                                    <div class="item-content">
                                        <div class="subject">tt</div>
                                        <div class="ment">ttt</div>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="card">
                    <div class="card-list">
                        <ul class="card-list-ul">
                            <li class="item form">
                                <div class="label">test</div>
                                <div class="ui fluid input">
                                    <input type="text">
                                </div>
                            </li>
                            <li class="item form">
                                <div class="label">test</div>
                                <div class="ui multi form">
                                    <select class="slt">
                                        <option>오전</option>
                                        <option>오후</option>
                                    </select>
                                    <select class="slt">
                                        <option>12</option>
                                    </select>
                                    <span class="unit">시</span>
                                    <select class="slt">
                                        <option>55</option>
                                    </select>
                                    <span class="unit">분</span>
                                </div>
                            </li>
                            <li class="item"><button type="button" class="chatbot-button">제출</button></li>
                        </ul>
                    </div>
                </div>
                <div class="sample-bubble">
                    <button type="button" class="chatbot-button">이름없는 버튼</button>
                    <button type="button" class="chatbot-button">이름없는 버튼</button>
                    <span class="time-text">22-12-12 12:12</span>
                </div>
            </div>
        </div>
    </div>


    <tags:scripts>
        <script>
            function chatbotAddPopup() {
                $('#chatbot-add-popup').modalShow();
            }

            function chatbotDeletePopup() {
                confirm('정말 삭제하시겠습니까?');
            }

            function chatbotCopyPopup() {
                confirm('봇을 복사하시겠습니까?');
            }

            function fallbackBlockPopup() {
                $('#fallback-block-popup').modalShow();
            }

            function chatbotModifyPopup() {
                window.open('/sub-url/chatbot', '네이버팝업', 'width=1920px,height=1080px,scrollbars=yes');
            }
            function chatbotTestPopup() {
                $('.ui.preview.modal').dragModalShow();
            }

            $(document).ready(function() {
                $('#actionSelect').change(function() {
                    var result = $('#actionSelect option:selected').val();
                    $('.fallback-action').removeClass('active');
                    $("#" + result).addClass('active');

                });
            });
        </script>
    </tags:scripts>
</tags:tabContentLayout>
