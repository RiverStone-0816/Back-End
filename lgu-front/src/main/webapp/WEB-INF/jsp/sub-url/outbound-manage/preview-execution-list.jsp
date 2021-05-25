<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <div class="menu-tab">
            <div class="inner">
                <ul>
                    <li><a href="<c:url value="/admin/outbound/preview/group/"/>" class="tab-indicator">프리뷰그룹관리</a></li>
<%--                    <li><a href="<c:url value="/sub-url/outbound-manage/preview-execution-list"/>" class="tab-on tab-indicator">프리뷰실행리스트</a></li>--%>
                    <li><a href="<c:url value="/admin/outbound/preview/data/"/>" class="tab-indicator">데이터관리</a></li>
                    <li><a href="<c:url value="/admin/outbound/preview/result/"/>" class="tab-indicator">상담결과이력</a></li>
                </ul>
                <div class="ui breadcrumb">
                    <span class="section">아웃바운드관리</span>
                    <i class="right angle icon divider"></i>
                    <span class="section">프리뷰</span>
                    <i class="right angle icon divider"></i>
                    <span class="active section">프리뷰실행리스트</span>
                </div>
            </div>
        </div>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">프리뷰실행명</label></div>
                                <div class="two wide column">
                                    <div class="ui input fluid">
                                        <input type="text">
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">업로드일</label></div>
                                <div class="ten wide column">
                                    <div class="fourteen wide column">
                                        <div class="date-picker from-to">
                                            <div class="dp-wrap">
                                                <label class="control-label" for="from" style="display:none">From</label>
                                                <input type="text" id="from" name="from" placeholder="시작일">
                                                <select class="time input-small-size"></select>
                                            </div>
                                            <span class="tilde">~</span>
                                            <div class="dp-wrap">
                                                <label class="control-label" for="to" style="display:none">to</label>
                                                <input type="text" id="to" name="to" placeholder="종료일">
                                                <select class="time input-small-size"></select>
                                            </div>
                                        </div>
                                        <div class="ui basic buttons">
                                            <button class="ui button">당일</button>
                                            <button class="ui button active">3일</button>
                                            <button class="ui button">1주일</button>
                                            <button class="ui button">1개월</button>
                                            <button class="ui button">3개월</button>
                                            <button class="ui button">6개월</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">진행상태</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <select>
                                            <option>미진행</option>
                                            <option>진행중</option>
                                            <option>완료</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">11</span> 건</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui table structured celled compact unstackable">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>업로드일시</th>
                            <th>프리뷰실행명</th>
                            <th>실행그룹</th>
                            <th>진행상태</th>
                            <th>총건수</th>
                            <th>시도</th>
                            <th>통화</th>
                            <th>비수신</th>
                            <th>잔여건수</th>
                            <th class="three wide column">관리</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%--<tr>
                            <td colspan="11" class="null-data">조회된 데이터가 없습니다.</td>
                        </tr>--%>
                        <tr>
                            <td>번호</td>
                            <td>업로드일시</td>
                            <td>프리뷰실행명</td>
                            <td>실행그룹</td>
                            <td>진행상태</td>
                            <td>총건수</td>
                            <td>시도</td>
                            <td>통화</td>
                            <td>비수신</td>
                            <td>잔여건수</td>
                            <td>
                                <div class="ui form">
                                    <button class="ui button mini compact" onclick="previewRedistributionPopup()">재분배
                                    </button>
                                    <button class="ui button mini compact">다운로드</button>
                                    <button class="ui button mini compact">삭제</button>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <div class="ui pagination menu pull-right small">
                        <a class="item">처음</a>
                        <a class="item">1</a>
                        <a class="item">2</a>
                        <a class="item active">3</a>
                        <a class="item">끝</a>

                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="ui modal" id="modal-preview-redistribution">
        <i class="close icon"></i>
        <div class="header">
            프리뷰 재분배
        </div>
        <div class="content rows scrolling">
            <table class="ui table celled compact unstackable">
                <thead>
                <tr>
                    <th class="one wide">선택</th>
                    <th>상담원</th>
                    <th>분배건수</th>
                    <th class="one wide">선택</th>
                    <th>상담원</th>
                    <th>분배건수</th>
                </tr>
                </thead>
                <tbody>
                    <%-- <tr>
                         <td colspan="6" class="null-data">조회된 데이터가 없습니다.</td>
                     </tr>--%>
                <tr>
                    <td>
                        <div class="ui checkbox"><input type="checkbox" name="example"></div>
                    </td>
                    <td>김옥중</td>
                    <td>34</td>
                    <td>
                        <div class="ui checkbox"><input type="checkbox" name="example"></div>
                    </td>
                    <td>김옥중</td>
                    <td>34</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="actions">
            <div class="ui button left floated">일괄회수</div>
            <div class="ui button left floated">선택회수</div>
            <div class="ui button left floated">일괄분배</div>
            <div class="ui button left floated">지정분배</div>
            회수건수 : 3
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            function previewRedistributionPopup() {
                $('#modal-preview-redistribution').modalShow();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
