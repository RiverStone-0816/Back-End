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
           action="${pageContext.request.contextPath}/api/conference/${entity != null ? entity.seq : null}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">회의실예약[${entity != null ? '수정' : '추가'}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <c:choose>
                <c:when test="${entity == null}">
                    <div class="row">
                        <div class="four wide column"><label class="control-label">회의실</label></div>
                        <div class="twelve wide column">
                            <form:select path="roomNumber" items="${rooms}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="four wide column"><label class="control-label">회의날짜</label></div>
                        <div class="twelve wide column">
                            <form:input path="reserveDate" cssClass="-datepicker"/>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <form:hidden path="reserveDate"/>
                    <div class="row">
                        <div class="four wide column"><label class="control-label">기본정보</label></div>
                        <div class="twelve wide column">
                            <form:hidden path="roomNumber"/>
                            [회의실명:${g.htmlQuote(room.roomName)}][회의실번호:${g.htmlQuote(room.roomNumber)}][회의날짜:${g.htmlQuote(entity.reserveDate)}]
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="row">
                <div class="four wide column"><label class="control-label">회의명</label></div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="confName"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">예약시간</label></div>
                <div class="seven wide column">
                    <div class="ui form">
                        <select class="input-small-size time" name="fromHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.reserveFromTime / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="fromMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.reserveFromTime % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <span class="tilde">~</span>
                        <select class="input-small-size time" name="toHour">
                            <c:forEach var="e" begin="0" end="23">
                                <option value="${e}" ${entity != null && (entity.reservetoTime / 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <select class="input-small-size minute" name="toMinute">
                            <c:forEach var="e" begin="0" end="59">
                                <option value="${e}" ${entity != null && (entity.reservetoTime % 60).intValue() == e ? 'selected' : null}>${e}</option>
                            </c:forEach>
                        </select>
                        <button class="ui button mini -check-duplicated-time" type="button">중복확인</button>
                    </div>
                </div>
                <div class="five wide column">
                    <span class="message text-green -express-usable-extension" style="display: none">사용가능</span>
                    <span class="message text-red -express-unusable-extension" style="display: none">사용불가</span>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">회의참석시음원</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="confSound" items="${soundList}"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">녹취</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${isRecordTypes}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isRecord" value="${e.key}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">초대시설정</label></div>
                <div class="two wide column">초대시 RID</div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:input path="confCid"/>
                    </div>
                </div>
                <div class="six wide column"></div>
                <div class="four wide column"></div>
                <div class="two wide column">머신디텍트</div>
                <div class="ten wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isMachineDetect" value="N"/>
                                    <label>전화버튼눌러서참여</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isMachineDetect" value="Y"/>
                                    <label>머신 디텍트하여 끊기</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">참여시설정</label></div>
                <div class="two wide column">
                    비밀번호사용
                </div>
                <div class="four wide column">
                    <div class="ui input fluid">
                        <form:password path="confPasswd" placeholder="4자리숫자"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">내부참여자</label></div>
                <div class="twelve wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${persons}">
                                    <option value="${g.htmlQuote(e.key)}">${g.htmlQuote(e.key)} (${g.htmlQuote(e.value)})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-right">›</button>
                            <button type="button" class="btn-move-selected-left -to-left">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="confPeerMembers" class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.inMemberList}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.peer)} (${g.htmlQuote(e.idName)})</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">외부참여자</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <select multiple="multiple" name="confOutMembers" style="height:150px;">
                            <c:forEach var="e" items="${entity.outMemberList}">
                                <option data-name="${g.htmlQuote(e.memberName)}" data-number="${g.htmlQuote(e.memberNumber)}">
                                        ${g.htmlQuote(e.memberName)} [${g.htmlQuote(e.memberNumber)}]
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="six wide field">
                                <input type="text" name="confOutMemberName" placeholder="참가자명">
                            </div>
                            <div class="six wide field">
                                <input type="text" name="confOutMemberNumber" placeholder="전화번호">
                            </div>
                            <div class="mini ui buttons">
                                <button type="button" class="ui button -add-out-member">추가</button>
                                <button type="button" class="ui button -remove-out-member">삭제</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        data.confPeerMembers = [];
        modal.find('[name="confPeerMembers"] option').each(function () {
            data.confPeerMembers.push($(this).val());
        });

        data.reserveFromTime = parseInt(data.fromHour) * 60 + parseInt(data.fromMinute);
        data.reserveToTime = parseInt(data.toHour) * 60 + parseInt(data.toMinute);

        delete data.fromHour;
        delete data.fromMinute;
        delete data.toHour;
        delete data.toMinute;

        data.confOutMembers = [];
        modal.find('[name="confOutMembers"] option').each(function () {
            data.confOutMembers.push({memberName: $(this).attr('data-name'), memberNumber: $(this).attr('data-number')});
        });

        delete data.confOutMemberName;
        delete data.confOutMemberNumber;
    };

    modal.find('.-add-out-member').click(function () {
        const name = modal.find('[name="confOutMemberName"]').val().trim();
        const number = modal.find('[name="confOutMemberNumber"]').val().trim();

        if (!name) return alert('참가자명을 입력하세요.');
        if (!number) return alert('전화번호를 입력하세요.');

        modal.find('[name="confOutMembers"]').append($('<option/>', {'data-name': name, 'data-number': number, text: name + ' [' + number + ']'}));
    });

    modal.find('.-remove-out-member').click(function () {
        modal.find('[name="confOutMembers"] option:selected').remove();
    });

    modal.find('.-check-duplicated-time').click(function () {
        const fromHour = modal.find('[name=fromHour]').val();
        const fromMinute = modal.find('[name=fromMinute]').val();
        const toHour = modal.find('[name=toHour]').val();
        const toMinute = modal.find('[name=toMinute]').val();

        restSelf.post('/api/conference/duplicate', {
            reserveFromTime: parseInt(fromHour) * 60 + parseInt(fromMinute),
            reserveToTime: parseInt(toHour) * 60 + parseInt(toMinute),
            roomNumber: modal.find('[name=roomNumber]').val(),
            reserveDate: modal.find('[name=reserveDate]').val(),
        }).done(function (response) {
            if (response.data) {
                modal.find('.-express-usable-extension').hide();
                modal.find('.-express-unusable-extension').show();
            } else {
                modal.find('.-express-usable-extension').show();
                modal.find('.-express-unusable-extension').hide();
            }
        });
    });
</script>
