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
                        <%--<li><a href="<c:url value="/admin/outbound/pds/result-group/"/>" class="tab-indicator">상담그룹설정</a></li>--%>
                        <%--<li><a href="<c:url value="/admin/outbound/pds/ivr/"/>" class="tab-indicator">IVR설정</a></li>--%>
                    <li><a href="<c:url value="/admin/outbound/pds/group/"/>" class="tab-indicator">그룹관리/실행요청</a></li>
                        <%--<li><a href="<c:url value="/admin/outbound/pds/upload/"/>" class="tab-indicator">업로드이력관리</a></li>--%>
                    <li><a href="<c:url value="/admin/outbound/pds/custominfo/"/>" class="tab-on tab-indicator">데이터관리</a></li>
                    <li><a href="<c:url value="/admin/outbound/pds/monit/"/>" class="tab-indicator">실행/모니터링</a></li>
                        <%--<li><a href="<c:url value="/admin/outbound/pds/history/"/>" class="tab-indicator">실행이력</a></li>--%>
                    <li><a href="<c:url value="/admin/outbound/pds/research-result/"/>" class="tab-indicator">결과이력</a></li>
                        <%--<li><a href="<c:url value="/sub-url/outbound-manage/pds-consulting-history"/>" class="tab-indicator">상담결과이력</a></li>--%>
                </ul>
                <div class="ui breadcrumb">
                    <span class="section">아웃바운드관리</span>
                    <i class="right angle icon divider"></i>
                    <span class="section">Auto IVR 관리</span>
                    <i class="right angle icon divider"></i>
                    <span class="active section">IVR설정</span>
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
                                <div class="two wide column"><label class="control-label">상담날짜</label></div>
                                <div class="ten wide column">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="from" style="display:none">From</label>
                                            <input type="text" id="from" name="from" placeholder="시작일">
                                            <select class="time"></select>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="to" style="display:none">to</label>
                                            <input type="text" id="to" name="to" placeholder="종료일">
                                            <select class="time"></select>
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
                                <div class="two wide column"><label class="control-label">상담그룹</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <select>
                                            <option>선택</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색항목</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <select id="search_fieldid" name="search_fieldid">
                                            <option value="">검색필드선택</option>
                                            <option value="">--데이터기본정보--</option>
                                            <option value="PRV_SYS_UPLOAD_DATE">데이터생성일</option>
                                            <option value="PRV_SYS_DAMDANG_ID">담당자</option>
                                            <option value="">--고객DB업로드필드--</option>
                                            <option value="PRV_STRING_1">고객명</option>
                                            <option value="PRV_NUMBER_1">전화번호1</option>
                                            <option value="PRV_STRING_2">고객정보</option>
                                            <option value="">--상담결과필드--</option>
                                            <option value="RS_CODE_1">상담종류</option>
                                            <option value="RS_CODE_2">처리여부</option>
                                            <option value="RS_STRING_1">메모</option>
                                        </select>
                                    </div>
                                </div>
                                    <%--검색항목이 데이터생성일인경우 출력--%>
                                <div class="five wide column">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label for="from" style="display:none">From</label>
                                            <input type="text" id="from" name="from" placeholder="시작일">
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label for="to" style="display:none">to</label>
                                            <input type="text" id="to" name="to" placeholder="종료일">
                                        </div>
                                    </div>
                                </div>
                                    <%--검색항목이 담당자일경우 출력--%>
                                    <%--<div class="two wide column">
                                        <div class="ui form">
                                            <select>
                                                <option>담당자선택</option>
                                            </select>
                                        </div>
                                    </div>--%>
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
                    <div class="pull-right">
                        <div class="ui button basic green">Excel 다운로드</div>
                        <div class="ui basic buttons">
                            <button class="ui button" onclick="pdsDataAddPopup()">데이터추가</button>
                            <button class="ui button">데이터수정</button>
                            <button class="ui button">데이터삭제</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled selectable-only table structured compact unstackable fixed">
                        <thead>
                        <tr>
                            <th rowspan="2" class="one wide">번호</th>
                            <th colspan="3">업로드필드정보</th>
                            <th colspan="3">상담결과필드</th>
                        </tr>
                        <tr>
                            <th>업로드일</th>
                            <th>고객명</th>
                            <th>전화번호</th>
                            <th>제안상태</th>
                            <th>관심도</th>
                            <th>메모</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%--<tr>
                            <td colspan="7" class="null-data">조회된 데이터가 없습니다.</td>
                        </tr>--%>
                        <tr>
                            <td>1</td>
                            <td>2019-10-24 14:23:59</td>
                            <td>김옥중</td>
                            <td>01011111111</td>
                            <td>제안상태</td>
                            <td>관심도</td>
                            <td>메모</td>
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

    <div class="ui modal small" id="modal-pds-data-add-popup">
        <i class="close icon"></i>
        <div class="header">
            Auto IVR 추가[그룹명:김옥중테스트]
        </div>
        <div class="content rows scrolling">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">고객명</label></div>
                    <div class="six wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                    <div class="four wide column">
                        (max 100 Bytes.)
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">전화번호</label></div>
                    <div class="six wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                    <div class="four wide column">
                        (최대길이:숫자15개)
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            function pdsDataAddPopup() {
                $('#modal-pds-data-add-popup').modalShow();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
