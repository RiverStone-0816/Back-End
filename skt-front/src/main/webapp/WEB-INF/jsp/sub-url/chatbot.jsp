<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<tags:scripts/>


<link rel="stylesheet" href="<c:url value="/resources/vendors/drawflow/0.0.53/drawflow.min.css?version=${version}"/>"/>

<tags:layout-chatbot>

    <div class="chatbot-main-container">
        <div class="chatbot-header">
            <div class="header-left">
                <img src="<c:url value="/resources/images/chatbot-icon-white.svg"/>">
                <h2 class="caption">시나리오 봇 설정</h2>
            </div>
            <div class="header-right">
                <div class="checkbox-wrap">
                    <span class="grid">격자</span>
                    <div class="ui toggle checkbox">
                        <input type="checkbox" name="public">
                    </div>
                </div>
                <div class="bar-zoom">
                    <span class="ratio">비율</span>
                    <button class="zoom-control-btn plus" type="button" onclick="editor.zoom_in()"><img src="<c:url value="/resources/images/zoom-plus.svg"/>"></button>
                    <button class="zoom-control-btn refresh" type="button" onclick="editor.zoom_reset()"><img src="<c:url value="/resources/images/zoom-refresh.svg"/>"></button>
                    <button class="zoom-control-btn minus" type="button" onclick="editor.zoom_out()"><img src="<c:url value="/resources/images/zoom-minus.svg"/>"></button>
                </div>
                <button class="ui brand button">저장</button>
            </div>

        </div>
        <div id="drawflow" ondrop="drop(event)" ondragover="allowDrop(event)">
        </div>
    </div>


    <%--텍스트 컨텐츠 수정 팝업--%>
    <div class="ui chatbot text-contents modal">
        <i class="close icon"></i>
        <div class="header">텍스트 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">컨텐츠 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요.">
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">멘트</span></div>
                    <div class="twelve wide column">
                        <textarea placeholder="멘트를 입력하세요."></textarea>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button">저장</button>
        </div>
    </div>

    <%--이미지 컨텐츠 수정 팝업--%>
    <div class="ui chatbot image-contents modal">
        <i class="close icon"></i>
        <div class="header">이미지 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">컨텐츠 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요.">
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">이미지</span></div>
                    <div class="twelve wide column">
                        <div class="display-flex">
                            <div class="file-text-wrap">이미지를 추가해주세요.</div>
                            <div class="filebox">
                                <label for="ex_file">추가</label>
                                <input type="file" id="ex_file">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--카드 컨텐츠 수정 팝업--%>
    <div class="ui chatbot card-contents modal">
        <i class="close icon"></i>
        <div class="header">카드 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">컨텐츠 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요.">
                    </div>
                </div>
                <div class="top aligned row">
                    <div class="four wide column"><span class="subject">멘트</span></div>
                    <div class="twelve wide column">
                        <textarea placeholder="멘트를 입력하세요."></textarea>
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">이미지</span></div>
                    <div class="twelve wide column">
                        <div class="display-flex">
                            <div class="file-text-wrap">이미지를 추가해주세요.</div>
                            <div class="filebox">
                                <label for="ex_file">추가</label>
                                <input type="file" id="ex_file">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--리스트 컨텐츠 수정 팝업--%>
    <div class="ui chatbot list-contents modal">
        <i class="close icon"></i>
        <div class="header">리스트 컨텐츠 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">컨텐츠 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="컨텐츠 이름을 입력하세요.">
                    </div>
                </div>
                <div class="row">
                    <div class="sixteen wide column">
                        <div class="list-box">
                            <div class="list-header">
                                <div class="list-name">리스트 1</div>
                                <button type="button" class="list-plus"></button>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">리스트 이름</div>
                                <div class="list-right">
                                    <input type="text" placeholder="리스트 이름을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-start">
                                <div class="list-left">멘트</div>
                                <div class="list-right">
                                    <textarea placeholder="멘트를 입력하세요."></textarea>
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">URL</div>
                                <div class="list-right">
                                    <input type="text" placeholder="URL을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">이미지</div>
                                <div class="list-right">
                                    <div class="display-flex">
                                        <div class="file-text-wrap">이미지를 추가해주세요.</div>
                                        <div class="filebox">
                                            <label for="ex_file">추가</label>
                                            <input type="file" id="ex_file">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="list-box">
                            <div class="list-header">
                                <div class="list-name">리스트 1</div>
                                <button type="button" class="list-delete"></button>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">리스트 이름</div>
                                <div class="list-right">
                                    <input type="text" placeholder="리스트 이름을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-start">
                                <div class="list-left">멘트</div>
                                <div class="list-right">
                                    <textarea placeholder="멘트를 입력하세요."></textarea>
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">URL</div>
                                <div class="list-right">
                                    <input type="text" placeholder="URL을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">이미지</div>
                                <div class="list-right">
                                    <div class="display-flex">
                                        <div class="file-text-wrap">이미지를 추가해주세요.</div>
                                        <div class="filebox">
                                            <label for="ex_file">추가</label>
                                            <input type="file" id="ex_file">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>


    <%--다음블록 수정 팝업--%>
    <div class="ui chatbot next-block modal">
        <i class="close icon"></i>
        <div class="header">블록 연결버튼 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">버튼 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="버튼 이름을 입력하세요.">
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">유형</span></div>
                    <div class="twelve wide column">
                        <input type="text" value="다음 블록" readonly>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--블록 연결 수정 팝업--%>
    <div class="ui chatbot connect-block modal">
        <i class="close icon"></i>
        <div class="header">블록 연결버튼 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">버튼 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="버튼 이름을 입력하세요.">
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">유형</span></div>
                    <div class="twelve wide column">
                        <input type="text" value="블록 연결" readonly>
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 블록</span></div>
                    <div class="twelve wide column">
                        <select>
                            <option>블록 선택</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--상담원 연결 수정 팝업--%>
    <div class="ui chatbot consultant-connect modal">
        <i class="close icon"></i>
        <div class="header">블록 연결버튼 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">버튼 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="버튼 이름을 입력하세요.">
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">유형</span></div>
                    <div class="twelve wide column">
                        <input type="text" value="상담원 연결" readonly>
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 그룹</span></div>
                    <div class="twelve wide column">
                        <select>
                            <option>상담원 그룹 선택</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--전화 연결 수정 팝업--%>
    <div class="ui chatbot call-connect modal">
        <i class="close icon"></i>
        <div class="header">블록 연결버튼 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">버튼 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="버튼 이름을 입력하세요.">
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">유형</span></div>
                    <div class="twelve wide column">
                        <input type="text" value="전화 연결" readonly>
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 그룹</span></div>
                    <div class="twelve wide column">
                        <select>
                            <option>전화 그룹 선택</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--연결URL 수정 팝업--%>
    <div class="ui chatbot url-connect modal">
        <i class="close icon"></i>
        <div class="header">블록 연결버튼 수정</div>
        <div class="content">
            <div class="ui vertically divided grid">
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">버튼 이름</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="버튼 이름을 입력하세요.">
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">유형</span></div>
                    <div class="twelve wide column">
                        <input type="text" value="URL 연결" readonly>
                    </div>
                </div>
                <div class="middle aligned row">
                    <div class="four wide column"><span class="subject">연결 URL</span></div>
                    <div class="twelve wide column">
                        <input type="text" placeholder="URL을 입력하세요.">
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
        </div>
    </div>

    <%--외부DB  수정 팝업--%>
    <div class="ui chatbot externaldb modal">
        <i class="close icon"></i>
        <div class="header">블록 연결버튼 수정</div>
        <div class="content">
            <div class="ui divided grid">
                <div class="eight wide column">
                    <div class="ui vertically divided grid">
                        <div class="middle aligned row">
                            <div class="four wide column"><span class="subject">버튼 이름</span></div>
                            <div class="twelve wide column">
                                <input type="text" placeholder="버튼 이름을 입력하세요.">
                            </div>
                        </div>
                        <div class="middle aligned row">
                            <div class="four wide column"><span class="subject">유형</span></div>
                            <div class="twelve wide column">
                                <input type="text" value="외부  DB 조회" readonly>
                            </div>
                        </div>
                        <div class="middle aligned row">
                            <div class="four wide column"><span class="subject">연동 URL</span></div>
                            <div class="twelve wide column">
                                <input type="text" placeholder="URL을 입력하세요.">
                            </div>
                        </div>
                        <div class="top aligned row">
                            <div class="four wide column"><span class="subject">안내 문구</span></div>
                            <div class="twelve wide column">
                                <textarea placeholder="안내 문구를 입력하세요."></textarea>
                            </div>
                        </div>
                        <div class="row">
                            <div class="sixteen wide column">
                                <div class="answer-ment-check">
                                    <div>답변 멘트 사용</div>
                                    <div class="ui toggle checkbox">
                                        <input type="checkbox" name="public" tabindex="0" class="hidden"><label></label>
                                    </div>
                                </div>
                                <div class="list-box">
                                    <div class="list-header">
                                        <div class="list-name">답변 설정</div>
                                    </div>
                                    <div class="list-row align-items-start">
                                        <div class="list-left">정상</div>
                                        <div class="list-right">
                                            <textarea readonly></textarea>
                                        </div>
                                    </div>
                                    <div class="list-row align-items-center">
                                        <div class="list-left">항목없음</div>
                                        <div class="list-right">
                                            <input type="text" readonly>
                                        </div>
                                    </div>
                                    <div class="list-row align-items-start">
                                        <div class="list-left">조회 불가</div>
                                        <div class="list-right">
                                            <textarea readonly></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="eight wide column">
                    <div class="list-box-wrap">
                        <div class="list-box">
                            <div class="list-header">
                                <div class="list-name">항목 1</div>
                                <button type="button" class="list-plus"></button>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">타입</div>
                                <div class="list-right">
                                    <select>
                                        <option>타입 선택</option>
                                        <option>숫자 입력</option>
                                        <option>텍스트 입력</option>
                                        <option>달력 입력</option>
                                        <option>시간 입력</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">항목 이름</div>
                                <div class="list-right">
                                    <input type="text" placeholder="항목 이름을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">파라미터</div>
                                <div class="list-right">
                                    <input type="text" placeholder="파라미터를 입력하세요.">
                                </div>
                            </div>
                        </div>
                        <div class="list-box">
                            <div class="list-header">
                                <div class="list-name">항목 2</div>
                                <button type="button" class="list-delete"></button>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">타입</div>
                                <div class="list-right">
                                    <select>
                                        <option>타입 선택</option>
                                        <option>숫자 입력</option>
                                        <option>텍스트 입력</option>
                                        <option>달력 입력</option>
                                        <option>시간 입력</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">항목 이름</div>
                                <div class="list-right">
                                    <input type="text" placeholder="항목 이름을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">파라미터</div>
                                <div class="list-right">
                                    <input type="text" placeholder="파라미터를 입력하세요.">
                                </div>
                            </div>
                        </div>
                        <div class="list-box">
                            <div class="list-header">
                                <div class="list-name">항목 3</div>
                                <button type="button" class="list-delete"></button>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">타입</div>
                                <div class="list-right">
                                    <select>
                                        <option>타입 선택</option>
                                        <option>숫자 입력</option>
                                        <option>텍스트 입력</option>
                                        <option>달력 입력</option>
                                        <option>시간 입력</option>
                                    </select>
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">항목 이름</div>
                                <div class="list-right">
                                    <input type="text" placeholder="항목 이름을 입력하세요.">
                                </div>
                            </div>
                            <div class="list-row align-items-center">
                                <div class="list-left">파라미터</div>
                                <div class="list-right">
                                    <input type="text" placeholder="파라미터를 입력하세요.">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui small compact left floated grey button remove-ml">삭제</button>
            <button type="button" class="ui small compact button">취소</button>
            <button type="button" class="ui small compact brand button remove-mr">저장</button>
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
    <script src="<c:url value="/resources/vendors/drawflow/0.0.53/drawflow.min.js?version=${version}"/>" data-type="library"></script>
    <script>
        var id = document.getElementById("drawflow");
        const editor = new Drawflow(id);
        editor.reroute = true;
        const dataToImport = {
            "drawflow": {
                "Home": {
                    "data": {
                        1: {
                            id: 1,
                            name: "example1",
                            data: {},
                            class: "example1",
                            html: `
<div>
    <div class="header">
        <span>봇 템플릿</span>
        <div class="ui toggle checkbox">
            <input type="checkbox" name="public" tabindex="0" class="hidden"><label></label>
        </div>
        <button type="button" class="preview-btn" onclick="chatbotPreview()">미리보기</button>
    </div>
    <div class="content">
        <div class="ui vertically divided grid">
            <div class="middle aligned row">
                <div class="four wide column"><span class="subject">블록이름</span></div>
                <div class="twelve wide column">
                    <input type="text" placeholder="블록 이름을 입력하세요.">
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">키워드</span></div>
                <div class="twelve wide column">
                    <div class="display-flex mb10">
                        <input type="text" placeholder="키워드를 입력하세요.">
                        <button type="button" class="action-btn ml5">추가</button>
                    </div>
                    <div class="keyword-box">
                        <span class="ui label">안녕하세요<i class="delete icon"></i></span>
                    </div>
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">컨텐츠</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>텍스트 컨텐츠</option>
                                <option>이미지 컨텐츠</option>
                                <option>카드 컨텐츠 컨텐츠</option>
                                <option>리스트 컨텐츠</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                        <li class="item text">
                            <div class="contents-item">
                                <div class="contents-title">텍스트 컨텐츠</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="textContentsModify()">수정</button>
                        </li>
                        <li class="item image">
                            <div class="contents-item">
                                <div class="contents-title">이미지 컨텐츠</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="imageContentsModify()">수정</button>
                        </li>
                        <li class="item card">
                            <div class="contents-item">
                                <div class="contents-title">카드 컨텐츠</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="cardContentsModify()">수정</button>
                        </li>
                        <li class="item list">
                            <div class="contents-item">
                                <div class="contents-title">리스트 컨텐츠</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="listContentsModify()">수정</button>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">버튼</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>다음 블록</option>
                                <option>블록 연결</option>
                                <option>상담원 연결</option>
                                <option>전화 연결</option>
                                <option>URL 블록</option>
                                <option>외부 DB 조회</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>`,
                            typenode: false,
                            inputs: {},
                            outputs: {
                                "output_1": {
                                    "connections": [{
                                        "node": "2",
                                        "output": "input_1"
                                    }]
                                }
                            },
                            pos_x: 250,
                            pos_y: 50
                        },
                        2: {
                            id: 2,
                            name: "example2",
                            data: {},
                            class: "example2",
                            html: `
<div>
    <div class="header">
        <span>봇 템플릿</span>
        <div class="ui toggle checkbox">
            <input type="checkbox" name="public" tabindex="0" class="hidden"><label></label>
        </div>
        <button type="button" class="preview-btn" onclick="chatbotPreview()">미리보기</button>
    </div>
    <div class="content">
        <div class="ui vertically divided grid">
            <div class="middle aligned row">
                <div class="four wide column"><span class="subject">블록이름</span></div>
                <div class="twelve wide column">
                    <input type="text" placeholder="블록 이름을 입력하세요.">
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">키워드</span></div>
                <div class="twelve wide column">
                    <div class="display-flex mb10">
                        <input type="text" placeholder="키워드를 입력하세요.">
                        <button type="button" class="action-btn ml5">추가</button>
                    </div>
                    <div class="keyword-box">
                        <span class="ui label">안녕하세요<i class="delete icon"></i></span>
                    </div>
                </div>
            </div>
            <div class="middle aligned row">
                <div class="four wide column"><span class="subject">컨텐츠</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>텍스트 컨텐츠</option>
                                <option>이미지 컨텐츠</option>
                                <option>카드 컨텐츠 컨텐츠</option>
                                <option>리스트 컨텐츠</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">버튼</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>다음 블록</option>
                                <option>블록 연결</option>
                                <option>상담원 연결</option>
                                <option>전화 연결</option>
                                <option>URL 블록</option>
                                <option>외부 DB 조회</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                        <li class="item button">
                            <div class="contents-item">
                                <div class="contents-title">다음 블록</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="nextBlockModify()">수정</button>
                        </li>
                        <li class="item button">
                            <div class="contents-item">
                                <div class="contents-title">블록 연결</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="connectBlockModify()">수정</button>
                        </li>
                        <li class="item button">
                            <div class="contents-item">
                                <div class="contents-title">상담원 연결</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="consultantConnectModify()">수정</button>
                        </li>
                        <li class="item button">
                            <div class="contents-item">
                                <div class="contents-title">전화 연결</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="callConnectModify()">수정</button>
                        </li>
                        <li class="item button">
                            <div class="contents-item">
                                <div class="contents-title">URL 연결</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="urlConnectModify()">수정</button>
                        </li>
                        <li class="item button">
                            <div class="contents-item">
                                <div class="contents-title">외부 DB 조회</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="externalDbConnectModify()">수정</button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>`,
                            typenode: false,
                            inputs: {
                                "input_1": {
                                    "connections": [{
                                        "node": "1",
                                        "input": "output_1"
                                    }]
                                }
                            },
                            outputs: {
                                "output_1": {
                                    "connections": [
                                        {
                                            "node": "4",
                                            "output": "input_1"
                                        }
                                    ]
                                },
                                "output_2": {
                                    "connections": [
                                        {
                                            "node": "5",
                                            "output": "input_1"
                                        }
                                    ]
                                },
                            },
                            pos_x: 650,
                            pos_y: 300
                        },

                        4: {
                            id: 4,
                            name: "example4",
                            data: {},
                            class: "example4",
                            html: `
<div>
    <div class="header">
        <span>봇 템플릿</span>
        <div class="ui toggle checkbox">
            <input type="checkbox" name="public" tabindex="0" class="hidden"><label></label>
        </div>
        <button type="button" class="preview-btn" onclick="chatbotPreview()">미리보기</button>
    </div>
    <div class="content">
        <div class="ui vertically divided grid">
            <div class="middle aligned row">
                <div class="four wide column"><span class="subject">블록이름</span></div>
                <div class="twelve wide column">
                    <input type="text" placeholder="블록 이름을 입력하세요.">
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">키워드</span></div>
                <div class="twelve wide column">
                    <div class="display-flex mb10">
                        <input type="text" placeholder="키워드를 입력하세요.">
                        <button type="button" class="action-btn ml5">추가</button>
                    </div>
                    <div class="keyword-box">
                        <span class="ui label">안녕하세요<i class="delete icon"></i></span>
                    </div>
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">컨텐츠</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>텍스트 컨텐츠</option>
                                <option>이미지 컨텐츠</option>
                                <option>카드 컨텐츠 컨텐츠</option>
                                <option>리스트 컨텐츠</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                        <li class="item text">
                            <div class="contents-item">
                                <div class="contents-title">텍스트 컨텐츠</div>
                                <div class="contents-arrow-wrap">
                                    <button type="button" class="down-btn">▼</button>
                                    <button type="button" class="up-btn">▲</button>
                                </div>
                            </div>
                            <button type="button" class="action-btn" onclick="textContentsModify()">수정</button>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">버튼</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>다음 블록</option>
                                <option>블록 연결</option>
                                <option>상담원 연결</option>
                                <option>전화 연결</option>
                                <option>URL 블록</option>
                                <option>외부 DB 조회</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>`,
                            typenode: false,
                            inputs: {
                                "input_1": {
                                    "connections": [{
                                        "node": "2",
                                        "input": "output_1"
                                    }]
                                }
                            },
                            outputs: {},
                            pos_x: 1100,
                            pos_y: 50
                        },
                        5: {
                            id: 5,
                            name: "example5",
                            data: {},
                            class: "example5",
                            html: `
<div>
   <div class="header">
        <span>봇 템플릿</span>
        <div class="ui toggle checkbox">
            <input type="checkbox" name="public" tabindex="0" class="hidden"><label></label>
        </div>
        <button type="button" class="preview-btn" onclick="chatbotPreview()">미리보기</button>
    </div>
    <div class="content">
        <div class="ui vertically divided grid">
            <div class="middle aligned row">
                <div class="four wide column"><span class="subject">블록이름</span></div>
                <div class="twelve wide column">
                    <input type="text" placeholder="블록 이름을 입력하세요.">
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">컨텐츠</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>텍스트 컨텐츠</option>
                                <option>이미지 컨텐츠</option>
                                <option>카드 컨텐츠 컨텐츠</option>
                                <option>리스트 컨텐츠</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="top aligned row">
                <div class="four wide column"><span class="subject">버튼</span></div>
                <div class="twelve wide column">
                    <ul class="content-list">
                        <li class="item">
                            <select>
                                <option>유형선택</option>
                                <option>다음 블록</option>
                                <option>블록 연결</option>
                                <option>상담원 연결</option>
                                <option>전화 연결</option>
                                <option>URL 블록</option>
                                <option>외부 DB 조회</option>
                            </select>
                            <button type="button" class="action-btn">추가</button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
</div>`,
                            typenode: false,
                            inputs: {
                                "input_1": {
                                    "connections": [{
                                        "node": "2",
                                        "input": "output_2"
                                    }]
                                },
                            },
                            outputs: {},
                            pos_x: 1100,
                            pos_y: 450
                        },
                    }
                },
            }
        }
        editor.start();
        editor.import(dataToImport);


        // Events!
        editor.on('nodeCreated', function (id) {
            console.log("Node created " + id);
        })

        editor.on('nodeRemoved', function (id) {
            console.log("Node removed " + id);
        })

        editor.on('nodeSelected', function (id) {
            console.log("Node selected " + id);
        })

        editor.on('moduleCreated', function (name) {
            console.log("Module Created " + name);
        })

        editor.on('moduleChanged', function (name) {
            console.log("Module Changed " + name);
        })

        editor.on('connectionCreated', function (connection) {
            console.log('Connection created');
            console.log(connection);
        })

        editor.on('connectionRemoved', function (connection) {
            console.log('Connection removed');
            console.log(connection);
        })

        editor.on('mouseMove', function (position) {
            console.log('Position mouse x:' + position.x + ' y:' + position.y);
        })

        editor.on('nodeMoved', function (id) {
            console.log("Node moved " + id);
        })

        editor.on('zoom', function (zoom) {
            console.log('Zoom level ' + zoom);
        })

        editor.on('translate', function (position) {
            console.log('Translate x:' + position.x + ' y:' + position.y);
        })

        editor.on('addReroute', function (id) {
            console.log("Reroute added " + id);
        })

        editor.on('removeReroute', function (id) {
            console.log("Reroute removed " + id);
        })

        /* DRAG EVENT */

        /* Mouse and Touch Actions */

        var elements = document.getElementsByClassName('drag-drawflow');
        for (var i = 0; i < elements.length; i++) {
            elements[i].addEventListener('touchend', drop, false);
            elements[i].addEventListener('touchmove', positionMobile, false);
            elements[i].addEventListener('touchstart', drag, false);
        }

        var mobile_item_selec = '';
        var mobile_last_move = null;

        function positionMobile(ev) {
            mobile_last_move = ev;
        }

        function allowDrop(ev) {
            ev.preventDefault();
        }

        function drag(ev) {
            if (ev.type === "touchstart") {
                mobile_item_selec = ev.target.closest(".drag-drawflow").getAttribute('data-node');
            } else {
                ev.dataTransfer.setData("node", ev.target.getAttribute('data-node'));
            }
        }

        function drop(ev) {
            if (ev.type === "touchend") {
                var parentdrawflow = document.elementFromPoint(mobile_last_move.touches[0].clientX, mobile_last_move.touches[0].clientY).closest("#drawflow");
                if (parentdrawflow != null) {
                    addNodeToDrawFlow(mobile_item_selec, mobile_last_move.touches[0].clientX, mobile_last_move.touches[0].clientY);
                }
                mobile_item_selec = '';
            } else {
                ev.preventDefault();
                var data = ev.dataTransfer.getData("node");
                addNodeToDrawFlow(data, ev.clientX, ev.clientY);
            }

        }

        function addNodeToDrawFlow(name, pos_x, pos_y) {
            if (editor.editor_mode === 'fixed') {
                return false;
            }
            pos_x = pos_x * (editor.precanvas.clientWidth / (editor.precanvas.clientWidth * editor.zoom)) - (editor.precanvas.getBoundingClientRect().x * (editor.precanvas.clientWidth / (editor.precanvas.clientWidth * editor.zoom)));
            pos_y = pos_y * (editor.precanvas.clientHeight / (editor.precanvas.clientHeight * editor.zoom)) - (editor.precanvas.getBoundingClientRect().y * (editor.precanvas.clientHeight / (editor.precanvas.clientHeight * editor.zoom)));


            switch (name) {
                case 'facebook':
                    var facebook = `
        <div>
          <div class="title-box"><i class="fab fa-facebook"></i> Facebook Message</div>
        </div>
        `;
                    editor.addNode('facebook', 0, 1, pos_x, pos_y, 'facebook', {}, facebook);
                    break;
                case 'slack':
                    var slackchat = `
          <div>
            <div class="title-box"><i class="fab fa-slack"></i> Slack chat message</div>
          </div>
          `
                    editor.addNode('slack', 1, 0, pos_x, pos_y, 'slack', {}, slackchat);
                    break;
                case 'github':
                    var githubtemplate = `
          <div>
            <div class="title-box"><i class="fab fa-github "></i> Github Stars</div>
            <div class="box">
              <p>Enter repository url</p>
            <input type="text" df-name>
            </div>
          </div>
          `;
                    editor.addNode('github', 0, 1, pos_x, pos_y, 'github', {"name": ''}, githubtemplate);
                    break;
                case 'telegram':
                    var telegrambot = `
          <div>
            <div class="title-box"><i class="fab fa-telegram-plane"></i> Telegram bot</div>
            <div class="box">
              <p>Send to telegram</p>
              <p>select channel</p>
              <select df-channel>
                <option value="channel_1">Channel 1</option>
                <option value="channel_2">Channel 2</option>
                <option value="channel_3">Channel 3</option>
                <option value="channel_4">Channel 4</option>
              </select>
            </div>
          </div>
          `;
                    editor.addNode('telegram', 1, 0, pos_x, pos_y, 'telegram', {"channel": 'channel_3'}, telegrambot);
                    break;
                case 'aws':
                    var aws = `
          <div>
            <div class="title-box"><i class="fab fa-aws"></i> Aws Save </div>
            <div class="box">
              <p>Save in aws</p>
              <input type="text" df-db-dbname placeholder="DB name"><br><br>
              <input type="text" df-db-key placeholder="DB key">
              <p>Output Log</p>
            </div>
          </div>
          `;
                    editor.addNode('aws', 1, 1, pos_x, pos_y, 'aws', {"db": {"dbname": '', "key": ''}}, aws);
                    break;
                case 'log':
                    var log = `
            <div>
              <div class="title-box"><i class="fas fa-file-signature"></i> Save log file </div>
            </div>
            `;
                    editor.addNode('log', 1, 0, pos_x, pos_y, 'log', {}, log);
                    break;
                case 'google':
                    var google = `
            <div>
              <div class="title-box"><i class="fab fa-google-drive"></i> Google Drive save </div>
            </div>
            `;
                    editor.addNode('google', 1, 0, pos_x, pos_y, 'google', {}, google);
                    break;
                case 'email':
                    var email = `
            <div>
              <div class="title-box"><i class="fas fa-at"></i> Send Email </div>
            </div>
            `;
                    editor.addNode('email', 1, 0, pos_x, pos_y, 'email', {}, email);
                    break;

                case 'template':
                    var template = `
            <div>
              <div class="title-box"><i class="fas fa-code"></i> Template</div>
              <div class="box">
                Ger Vars
                <textarea df-template></textarea>
                Output template with vars
              </div>
            </div>
            `;
                    editor.addNode('template', 1, 1, pos_x, pos_y, 'template', {"template": 'Write your template'}, template);
                    break;
                case 'multiple':
                    var multiple = `
            <div>
              <div class="box">
                Multiple!
              </div>
            </div>
            `;
                    editor.addNode('multiple', 3, 4, pos_x, pos_y, 'multiple', {}, multiple);
                    break;
                case 'personalized':
                    var personalized = `
            <div>
              Personalized
            </div>
            `;
                    editor.addNode('personalized', 1, 1, pos_x, pos_y, 'personalized', {}, personalized);
                    break;
                case 'dbclick':
                    var dbclick = `
            <div>
            <div class="title-box"><i class="fas fa-mouse"></i> Db Click</div>
              <div class="box dbclickbox" ondblclick="showpopup(event)">
                Db Click here
                <div class="modal" style="display:none">
                  <div class="modal-content">
                    <span class="close" onclick="closemodal(event)">&times;</span>
                    Change your variable {name} !
                    <input type="text" df-name>
                  </div>

                </div>
              </div>
            </div>
            `;
                    editor.addNode('dbclick', 1, 1, pos_x, pos_y, 'dbclick', {name: ''}, dbclick);
                    break;

                default:
            }
        }

        var transform = '';

        function showpopup(e) {
            e.target.closest(".drawflow-node").style.zIndex = "9999";
            e.target.children[0].style.display = "block";
            //document.getElementById("modalfix").style.display = "block";

            //e.target.children[0].style.transform = 'translate('+translate.x+'px, '+translate.y+'px)';
            transform = editor.precanvas.style.transform;
            editor.precanvas.style.transform = '';
            editor.precanvas.style.left = editor.canvas_x + 'px';
            editor.precanvas.style.top = editor.canvas_y + 'px';
            console.log(transform);

            //e.target.children[0].style.top  =  -editor.canvas_y - editor.container.offsetTop +'px';
            //e.target.children[0].style.left  =  -editor.canvas_x  - editor.container.offsetLeft +'px';
            editor.editor_mode = "fixed";

        }

        function closemodal(e) {
            e.target.closest(".drawflow-node").style.zIndex = "2";
            e.target.parentElement.parentElement.style.display = "none";
            //document.getElementById("modalfix").style.display = "none";
            editor.precanvas.style.transform = transform;
            editor.precanvas.style.left = '0px';
            editor.precanvas.style.top = '0px';
            editor.editor_mode = "edit";
        }

        function changeModule(event) {
            var all = document.querySelectorAll(".menu ul li");
            for (var i = 0; i < all.length; i++) {
                all[i].classList.remove('selected');
            }
            event.target.classList.add('selected');
        }

        function changeMode(option) {

            //console.log(lock.id);
            if (option == 'lock') {
                lock.style.display = 'none';
                unlock.style.display = 'block';
            } else {
                lock.style.display = 'block';
                unlock.style.display = 'none';
            }

        }


        function textContentsModify() {
            $('.ui.chatbot.text-contents.modal').dragModalShow();
        }

        function imageContentsModify() {
            $('.ui.chatbot.image-contents.modal').dragModalShow();
        }

        function cardContentsModify() {
            $('.ui.chatbot.card-contents.modal').dragModalShow();
        }

        function listContentsModify() {
            $('.ui.chatbot.list-contents.modal').dragModalShow();
        }

        function nextBlockModify() {
            $('.ui.chatbot.next-block.modal').dragModalShow();
        }

        function connectBlockModify() {
            $('.ui.chatbot.connect-block.modal').dragModalShow();
        }

        function consultantConnectModify() {
            $('.ui.chatbot.consultant-connect.modal').dragModalShow();
        }

        function callConnectModify() {
            $('.ui.chatbot.call-connect.modal').dragModalShow();
        }

        function urlConnectModify() {
            $('.ui.chatbot.url-connect.modal').dragModalShow();
        }

        function externalDbConnectModify() {
            $('.ui.chatbot.externaldb.modal').dragModalShow();
        }

        function chatbotPreview() {
            $('.ui.preview.modal').dragModalShow();
        }


        // 키워드 사용중일 시
        //  alert('해당 키워드는 [블록1]에 사용되고 있습니다. 다른 키워드를 입력해주세요.');


    </script>

</tags:scripts>

</tags:layout-chatbot>