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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/chatbot-service/${entity == null ? null : entity.seq}" data-done="reload">

    <i class="close icon"></i>
    <div class="header">고객 채팅창 설정[${entity != null ? '수정' : '추가'}]</div>

    <div class="content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">채널명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="channelName"/></div>
                </div>
                <div class="three wide column"><label class="control-label">senderKey</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="senderKey"/></div>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">회사명</label></div>
                <div class="five wide column">
                    <div class="ui input fluid"><form:input path="displayCompanyName"/></div>
                </div>
                <div class="three wide column"><label class="control-label">활성화</label></div>
                <div class="five wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox ${form.enableChat == true ? 'checked' : ''}">
                                    <form:radiobutton path="enableChat" class="hidden" value="${true}"/>
                                    <label>활성화(Y)</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox ${form.enableChat == false ? 'checked' : ''}">
                                    <form:radiobutton path="enableChat" class="hidden" value="${false}"/>
                                    <label>비활성화(N)</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="two wide column">
                    <label class="control-label">인사글</label>
                </div>
                <div class="fourteen wide column">
                    <div class="ui grid">
                        <div class="ten wide column">
                            <div class="ui form">
                                <div class="field">
                                    <form:textarea path="message" class="mb13"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="two wide column">
                    <label class="control-label">프로필이미지</label>
                </div>
                <div class="fourteen wide column">
                    <div class="file-upload-header">
                        <form:hidden path="profile"/>
                        <label class="ui button blue mini compact" onclick="this.nextElementSibling.click()">이미지 업로드</label>
                        <input type="file">
                        <span class="file-name">
                                ${entity.profileFileName != null && entity.profileFileName != '' ? g.htmlQuote(entity.profileFileName) : 'No file selected'}
                        </span>
                    </div>
                    <p class="text-gray mt5">정방형 이미지를 권장합니다. (이미지는 업로드시 반영됩니다.)</p>
                </div>
            </div>
            <div class="row">
                <div class="two wide column">
                    <label class="control-label">이미지</label>
                </div>
                <div class="fourteen wide column">
                    <div class="file-upload-header">
                        <form:hidden path="image"/>
                        <label class="ui button blue mini compact" onclick="this.nextElementSibling.click()">이미지 업로드</label>
                        <input type="file">
                        <span class="file-name">
                                ${entity.imageFileName != null && entity.imageFileName != '' ? g.htmlQuote(entity.imageFileName) : 'No file selected'}
                        </span>
                    </div>
                    <p class="text-gray mt5">가로축 100% 형태로 삽입됩니다. (이미지는 업로드시 반영됩니다.)</p>
                </div>
            </div>
            <div class="row">
                <div class="two wide column">
                    <label class="control-label">배경색</label>
                </div>
                <div class="fourteen wide column">
                    <div class="ui grid">
                        <div class="six wide column">
                            <form:input path="backgroundColor"/>
                            <p class="text-gray mt15">색상을 선택하거나 코드를 입력하시면 반영됩니다.</p>
                        </div>
                        <div class="six wide column">
                            <div class="ui input fluid flex-100 mb15">
                                HSL <input type="text" class="ml15 -hsl" readonly>
                            </div>
                            <div class="ui input fluid flex-100 mb15">
                                RGB <input type="text" class="ml15 -rgb" readonly>
                            </div>
                            <div class="ui input fluid flex-100 mb15">
                                HSV <input type="text" class="ml15 -hsv" readonly>
                            </div>
                            <div class="ui input fluid flex-100 mb15">
                                Hex <input type="text" class="ml15 -hex" readonly>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
                <%--<div class="row">
                    <div class="two wide column">
                        <label class="control-label">채널추가</label>
                    </div>
                    <div class="fourteen wide column">
                        <div class="ui grid">
                            <div class="ten wide column">
                                <div class="ui form flex mb10">
                                    <select class="mr0">
                                        <option>카카오톡</option>
                                    </select>
                                    <button type="button" class="ui small compact button mr0 ml10">추가하기</button>
                                </div>
                                <div class="channel-list">
                                    <div class="dp-flex channel-item">
                                        <img src="<c:url value="/resources/images/kakao-icon.png"/>" class="channel-icon mr10">
                                        <div class="ui input fluid flex-100">
                                            <input type="text" placeholder="채널ID 정보">
                                        </div>
                                        <button type="button" class="ui small compact button mr0 ml10">삭제하기</button>
                                    </div>
                                    <div class="dp-flex channel-item">
                                        <img src="<c:url value="/resources/images/nband-icon.png"/>" class="channel-icon mr10">
                                        <div class="ui input fluid flex-100">
                                            <input type="text" placeholder="채널ID 정보">
                                        </div>
                                        <button type="button" class="ui small compact button mr0 ml10">삭제하기</button>
                                    </div>
                                    <div class="dp-flex channel-item">
                                        <img src="<c:url value="/resources/images/ntalk-icon.png"/>" class="channel-icon mr10">
                                        <div class="ui input fluid flex-100">
                                            <input type="text" placeholder="채널ID 정보">
                                        </div>
                                        <button type="button" class="ui small compact button mr0 ml10">삭제하기</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>--%>
        </div>
            <%--<div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">채팅창 미리보기</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="chat-preview">
                        <div class="header">
                            <img src="<c:url value="/resources/images/chatbot-icon.svg"/>" class="chatbot-icon">
                            <span class="customer-title">고객사 이름</span>
                        </div>
                        <div class="content">
                            <div class="sample-bubble">
                                <img src="<c:url value="/resources/images/eicn-sample.png"/>" class="customer-img">
                                <p>이아이씨엔 채팅상담을 이용해 주셔서 감사합니다.
                                    문의사항을 입력해주시면 상담원이 답변드리겠습니다.
                                    감사합니다.</p>
                            </div>
                            <div class="sample-bubble">
                                <p>다른 채널을 통한 상담을 원하시면 원하시는 서비스의 아이콘을 눌러주세요.</p>
                                <div class="preview-channel-icon-container">
                                    <img src="<c:url value="/resources/images/kakao-icon.png"/>" class="preview-channel-icon">
                                    <img src="<c:url value="/resources/images/ntalk-icon.png"/>" class="preview-channel-icon">
                                    <img src="<c:url value="/resources/images/nband-icon.png"/>" class="preview-channel-icon">
                                </div>
                            </div>
                        </div>
                        <div class="footer">
                            <span class="sample-text">문의사항을 입력해주세요.</span>
                            <img src="<c:url value="/resources/images/send-btn.svg"/>">
                        </div>
                    </div>
                </div>
            </div>--%>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>
<script>
    const fillColor = color => {
        modal.find('[name=backgroundColor]').val(color.toHexString())
        modal.find('.-hex').val(color.toHexString())
        modal.find('.-hsl').val(color.toHslString())
        modal.find('.-rgb').val(color.toRgbString())
        modal.find('.-hsv').val(color.toHsvString())
    }

    modal.find('[name=backgroundColor]').spectrum({
        type: "flat",
        showPalette: false,
        showAlpha: false,
        showButtons: false,
        allowEmpty: false,
        change: fillColor,
        move: fillColor,
    })

    modal.find('[type="file"]').change(function () {
        const container = this.parentElement
        uploadFile(this.files[0]).done(function (response) {
            restSelf.post('/api/chatbot-service/image', response.data).done(imageResponse => {
                container.querySelector('.file-name').innerHTML = response.data.originalName;
                container.querySelector('[name]').value = imageResponse.data
            })
        })
    })
</script>
