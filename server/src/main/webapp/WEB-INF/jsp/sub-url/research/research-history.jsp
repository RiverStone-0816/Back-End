<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <div class="menu-tab">
            <div class="inner">
                <ul>
                    <li><a href="<c:url value="/sub-url/research/research-history"/>" class="tab-on tab-indicator">설문결과이력</a></li>
                </ul>
                <div class="ui breadcrumb">
                    <span class="section">설문관리</span>
                    <i class="right angle icon divider"></i>
                    <span class="section">설문관리</span>
                    <i class="right angle icon divider"></i>
                    <span class="active section">설문결과이력</span>
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
                                <div class="two wide column"><label class="control-label">검색기간</label></div>
                                <div class="nine wide column">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="from" style="display:none">From</label>
                                            <input type="text" id="from" name="from" placeholder="시작일">
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="to" style="display:none">to</label>
                                            <input type="text" id="to" name="to" placeholder="종료일">
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
                                <div class="two wide column"><label class="control-label">설문그룹</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <select>
                                            <option>선택</option>
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
                    <div class="pull-right">
                        <button class="ui basic button">샘플</button>
                    </div>
                </div>
                <div class="panel-body">
                    페이지 기획 대기중(시트추가)
                        <%--<table class="ui selectable-only structured celled table compact unstackable num-tbl">
                            <thead>
                            <tr>
                                <th rowspan="2">번호</th>
                                <th colspan="2">그룹기본정보</th>
                                <th colspan="4">업로드정보</th>
                                <th colspan="4">실행정보</th>
                            </tr>
                            <tr>
                                <th>그룹명</th>
                                <th>그룹생성일</th>
                                <th>마지막업로드날짜</th>
                                <th>업로드데이터수</th>
                                <th>업로드횟수</th>
                                <th>마지막업로드상태</th>
                                <th>마지막실행한날</th>
                                <th>실행횟수</th>
                                <th>실행상태</th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr>
                                <td rowspan="2">번호</td>
                                <td colspan="2">그룹기본정보</td>
                                <td colspan="4">업로드정보</td>
                                <td colspan="4">실행정보</td>
                            </tr>
                            <tr>
                                <td>그룹명</td>
                                <td>그룹생성일</td>
                                <td>마지막업로드날짜</td>
                                <td>업로드데이터수</td>
                                <td>업로드횟수</td>
                                <td>마지막업로드상태</td>
                                <td>마지막실행한날</td>
                                <td>실행횟수</td>
                                <td>실행상태</td>
                            </tr>
                            </tbody>
                        </table>--%>
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
    </div>

    <div class="ui modal tiny" id="modal-research-group-add">
        <i class="close icon"></i>
        <div class="header">
            설문그룹[추가]
        </div>
        <div class="scrolling content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">설문명</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">설문문항</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <select>
                                <option>선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">추가정보</label></div>
                    <div class="twelve wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <div class="ui modal tiny" id="modal-client-data-upload">
        <i class="close icon"></i>
        <div class="header">
            고객정보업로드
        </div>
        <div class="scrolling content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">그룹명</label></div>
                    <div class="twelve wide column">
                        test
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">유형</label></div>
                    <div class="twelve wide column">
                        고객정보
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">유형업로드필드</label></div>
                    <div class="twelve wide column">
                        전화번호1 [NUMBER]_고객명[STRING]
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">업로드화일</label></div>
                    <div class="twelve wide column">
                        <div class="file-upload-header">
                            <label for="file" class="ui button blue mini compact">파일찾기</label>
                            <input type="file" id="file">
                            <span class="file-name">login-bg.jpg</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">확인</button>
        </div>
    </div>

    <div class="ui modal" id="modal-research-start">
        <i class="close icon"></i>
        <div class="header">
            설문실행
        </div>
        <div class="scrolling content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">실행할교환기</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <select>
                                <option>교환기선택</option>
                            </select>
                        </div>
                    </div>
                    <div class="eight wide column">
                        (콜을 아웃바운드 시킬 교환기)
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">다이얼시간</label></div>
                    <div class="twelve wide column">
                        <input type="text" size="2"> 초 (30초이상권장)
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">녹취</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <select>
                                <option>녹취안함</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">콜시도할전화번호필드</label></div>
                    <div class="four wide column">
                        <div class="ui form">
                            <select>
                                <option>녹취안함</option>
                            </select>
                        </div>
                    </div>
                    <div class="eight wide column">
                        (기본항목 설정의 업로드유형을 선택한 후 사용 가능)
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">RID(발신번호) 설정</label></div>
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>그룹별RID지정</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui input fluid">
                            <input type="text">
                        </div>
                    </div>
                    <div class="six wide column"></div>
                    <div class="four wide column"></div>
                    <div class="four wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>업로드RID필드</label>
                        </div>
                    </div>
                </div>
                <div class="row blank">
                    <div class="four wide column"><label class="control-label">Auto IVR 속도</label></div>
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>대기중상담원기준</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui form">
                            <select>
                                <option>1배수</option>
                            </select>
                        </div>
                    </div>
                    <div class="six wide column"></div>
                    <div class="four wide column"></div>
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>동시통화기준</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <input type="text" size="2"> 채널(500미만)
                    </div>
                </div>
                <div class="row blank">
                    <div class="four wide column"><label class="control-label">연결대상</label></div>
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>상담원그룹</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui form">
                            <select>
                                <option>상담원그룹선택</option>
                            </select>
                        </div>
                    </div>
                    <div class="six wide column"></div>
                    <div class="four wide column"></div>
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>Auto IVR</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui form">
                            <select>
                                <option>IVR선택</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">상담결과(상담원화면)</label></div>
                    <div class="four wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>없음 또는 고객사화면</label>
                        </div>
                    </div>
                    <div class="eight wide column"></div>
                    <div class="four wide column"></div>
                    <div class="three wide column">
                        <div class="ui radio checkbox">
                            <input type="radio" name="radio" checked="checked">
                            <label>상담결과유형화면</label>
                        </div>
                    </div>
                    <div class="three wide column">
                        <div class="ui form">
                            <select>
                                <option>선택</option>
                            </select>
                        </div>
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
            function researchGroupAddPopup() {
                $('#modal-research-group-add').modalShow();
            }

            function clientDataUploadPopup() {
                $('#modal-client-data-upload').modalShow();
            }

            function researchStartPopup() {
                $('#modal-research-start').modalShow();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>