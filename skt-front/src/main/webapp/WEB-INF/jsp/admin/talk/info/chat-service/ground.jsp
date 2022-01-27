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

<tags:tabContentLayout>
    <link rel="stylesheet" href="<c:url value="/resources/vendors/spectrum/spectrum.min.css"/>">
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/talk/info/chat-service/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">웹채팅상담정보관리</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${list.size()}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button type="button" class="ui button" onclick="customerChatSetting()">추가</button>
                            <button type="button" class="ui button -control-entity" data-entity="ChatbotService" style="display: none;" onclick="popupModal(getEntityId('ChatbotService'))">
                                수정
                            </button>
                            <button type="button" class="ui button -control-entity" data-entity="ChatbotService" style="display: none;" onclick="deleteEntity(getEntityId('ChatbotService'))">
                                삭제
                            </button>
                        </div>
                    </div>
                        <%--<div class="pull-right">
                            <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/talk/chat-bot/" pageForm="${search}"/>
                        </div>--%>
                </div>
                <div class="panel-body">
                    <table class="ui celled table num-tbl unstackable ${list.size() > 0 ? "selectable-only" : null}" data-entity="ChatbotService">
                        <thead>
                        <tr>
                            <th class="one wide">번호</th>
                            <th>채널이름</th>
                            <th>senderKey</th>
                            <th>활성화</th>
                            <th>회사명</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${list.size() > 0}">
                                <c:forEach var="e" items="${list}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${status.index + 1}</td>
                                        <td>${g.htmlQuote(e.channelName)}</td>
                                        <td>${g.htmlQuote(e.senderKey)}</td>
                                        <td>${e.enableChat ? '활성화' : '비활성화'}</td>
                                        <td>${g.htmlQuote(e.displayCompanyName)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal" id="customer-chat-setting">
        <i class="close icon"></i>
        <div class="header">고객 채팅창 설정 [수정]</div>
        <div class="content rows">
            <div class="ui divided grid">
                <div class="nine wide column">
                    <div class="ui grid">
                        <div class="row">
                            <div class="sixteen wide column"><h3 class="modal-title">기본정보 설정</h3></div>
                            <div class="four wide column"><label class="control-label">표시 이름</label></div>
                            <div class="twelve wide column">
                                <div class="ui input fluid"><input type="text"></div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">채널 이름</label></div>
                            <div class="twelve wide column">
                                <div class="ui input fluid"><input type="text"></div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">채널 키</label></div>
                            <div class="twelve wide column">
                                <div class="ui input fluid"><input type="text"></div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">채널 사용</label></div>
                            <div class="twelve wide column">
                                <div class="ui form">
                                    <div class="inline fields">
                                        <div class="field">
                                            <div class="ui radio checkbox">
                                                <input type="radio" checked="checked">
                                                <label>활성화(Y)</label>
                                            </div>
                                        </div>
                                        <div class="field">
                                            <div class="ui radio checkbox">
                                                <input type="radio">
                                                <label>비활성화(N)</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">인사 멘트</label></div>
                            <div class="twelve wide column">
                                <div class="ui form fluid">
                                    <div class="field">
                                        <textarea rows="4"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">채널 추가</label></div>
                            <div class="twelve wide column">
                                <div class="display-flex align-items-center mb10">
                                    <div class="ui form flex-100">
                                        <select>
                                            <option>카카오톡</option>
                                            <option>라인</option>
                                            <option>네이버밴드</option>
                                            <option>네이버톡톡</option>
                                        </select>
                                    </div>
                                    <button type="button" class="ui basic ml10 button">추가하기</button>
                                </div>
                                <div class="display-flex align-items-center mb10">
                                    <img src="<c:url value="/resources/images/kakao-icon.png"/>" class="chat-sns-icon">
                                    <div class="ui input flex-100 ml10">
                                        <input type="text" placeholder="채널ID 정보">
                                    </div>
                                    <button type="button" class="ui basic ml10 button">삭제하기</button>
                                </div>
                                <div class="display-flex align-items-center mb10">
                                    <img src="<c:url value="/resources/images/ntalk-icon.png"/>" class="chat-sns-icon">
                                    <div class="ui input flex-100 ml10">
                                        <input type="text" placeholder="채널ID 정보">
                                    </div>
                                    <button type="button" class="ui basic ml10 button">삭제하기</button>
                                </div>
                                <div class="display-flex align-items-center mb10">
                                    <img src="<c:url value="/resources/images/nband-icon.png"/>" class="chat-sns-icon">
                                    <div class="ui input flex-100 ml10">
                                        <input type="text" placeholder="채널ID 정보">
                                    </div>
                                    <button type="button" class="ui basic ml10 button">삭제하기</button>
                                </div>
                                <div class="display-flex align-items-center">
                                    <img src="<c:url value="/resources/images/line-icon.png"/>" class="chat-sns-icon">
                                    <div class="ui input flex-100 ml10">
                                        <input type="text" placeholder="채널ID 정보">
                                    </div>
                                    <button type="button" class="ui basic ml10 button">삭제하기</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="seven wide column">
                    <div class="ui grid">
                        <div class="row">
                            <div class="sixteen wide column"><h3 class="modal-title">디자인 설정</h3></div>
                            <div class="four wide column"><label class="control-label">프로필 이미지</label></div>
                            <div class="twelve wide column">
                                <div class="file-upload-header">
                                    <label for="file" class="ui button brand mini compact">파일찾기</label>
                                    <input type="file" id="file">
                                    <span class="file-name">No file selected</span>
                                </div>
                                <p class="modal-grey-txt">정방형 이미지를 권장합니다.</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">이미지</label></div>
                            <div class="twelve wide column">
                                <div class="file-upload-header">
                                    <label for="file" class="ui button brand mini compact">파일찾기</label>
                                    <input type="file" id="file">
                                    <span class="file-name">No file selected</span>
                                </div>
                                <p class="modal-grey-txt">가로축 100% 형태로 삽입됩니다.</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="four wide column"><label class="control-label">배경색</label></div>
                            <div class="twelve wide column align-center">
                                <input id="color-picker" value='#276cb8' />
                                <div>
                                    <div class="display-flex align-items-center mb10">
                                        <div>RGB</div>
                                        <div class="ui input fluid ml15">
                                            <input type="text">
                                        </div>
                                    </div>
                                    <div class="display-flex align-items-center mb10">
                                        <div>HSL</div>
                                        <div class="ui input fluid ml15">
                                            <input type="text">
                                        </div>
                                    </div>
                                    <div class="display-flex align-items-center mb10">
                                        <div>HSV</div>
                                        <div class="ui input fluid ml15">
                                            <input type="text">
                                        </div>
                                    </div>
                                    <div class="display-flex align-items-center">
                                        <div>HEX</div>
                                        <div class="ui input fluid ml15">
                                            <input type="text">
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
            <button type="button" class="ui button modal-close">취소</button>
            <button type="submit" class="ui brand button">확인</button>
        </div>
    </div>

    <tags:scripts>
        <script src="<c:url value="/resources/vendors/spectrum/spectrum.min.js"/> "></script>
        <script>

            $('#color-picker').spectrum({
                type: "flat",
                showPalette: false,
                showAlpha: false,
                showButtons: false,
                allowEmpty: false
            });

            function customerChatSetting() {
                $('#customer-chat-setting').modalShow();
            }

            function popupModal(seq) {
                popupReceivedHtml('/admin/talk/info/chat-service/' + (seq || 'new') + '/modal', 'modal-chatbot-service');
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/chat-service/' + seq).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
