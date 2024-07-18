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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="entity" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="form" type="kr.co.eicn.ippbx.model.form.PersonFormRequest"--%>

<%@ page import="kr.co.eicn.ippbx.model.enums.RecordingAuthorityType" %>
<%@ page import="kr.co.eicn.ippbx.model.enums.DataSearchAuthorityType" %>

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity == null ? 'post' : 'put'}"
           action="${pageContext.request.contextPath}/api/user/${entity != null ? g.htmlQuote(entity.id) : null}" data-done="reload">

    <i class="close icon"></i>
    <div class="header">상담원사용자[${entity != null ? '수정' : '추가'}]</div>

    <div class="content scrolling rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label">아이디</label></div>
                <c:choose>
                    <c:when test="${entity == null}">
                        <div class="four wide column">
                            <div class="ui action input fluid">
                                <form:input path="id" onkeydown="checkDuplicate()"/>
                                <form:input path="isDuplicate" type="hidden"/>
                                <button type="button" class="ui button" onclick="checkDuplicatedId()">중복확인</button>
                            </div>
                        </div>
                        <div class="four wide column">
                            <span class="message text-green -usable-id-expression" style="display: none;">사용가능</span>
                            <span class="message text-red -unusable-id-expression" style="display: none;">사용불가</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="twelve wide column">
                                ${g.htmlQuote(entity.id)}
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">패스워드</label></div>
                <div class="four wide column">
                    <c:choose>
                        <c:when test="${entity == null}">
                            <div class="ui input fluid">
                                <form:password path="password" placeholder="문자/숫자/특수문자 8~20자"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="ui button compact mini" onclick="popupUpdatePasswordModal('${g.htmlQuote(entity.id)}')">패스워드변경</button>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="four wide column"><label class="control-label">성명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="idName"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${entity != null}">
                                    <c:forEach var="e" items="${entity.companyTrees}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e.groupName)}</span>
                                        <c:if test="${!status.last}">
                                            <i class="right angle icon divider"></i>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="four wide column"><label class="control-label">아이디유형구분</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="idType">
                            <form:option value="" label="선택"/>
                            <form:options items="${idTypes}"/>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column"><label class="control-label">근무상태</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="idStatus">
                            <form:option value="">정상근무</form:option>
                            <form:option value="S">휴직</form:option>
                            <form:option value="X">퇴사</form:option>
                        </form:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="four wide column"><label class="control-label">내선번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="extension">
                            <form:option value="" label="선택"/>
                            <c:forEach var="e" items="${extensions}">
                                <c:if test="${e != null && e != ''}">
                                    <form:option value="${e}" label="${e}"/>
                                </c:if>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <c:if test="${services.contains('PDS')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">${serviceKind.equals("SC") ? 'PDS' : 'Auto IVR'} 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isPds" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isPds" value="N"/>
                                        <label>사용안함 (라이센스:${license.pdsLicense.currentLicence}/${license.pdsLicense.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${!serviceKind.equals('CC')}">
                <c:if test="${services.contains('EMAIL')}">
                    <div class="row">
                        <div class="four wide column"><label class="control-label">이메일상담 여부</label></div>
                        <div class="twelve wide column">
                            <div class="ui form">
                                <div class="inline fields">
                                    <div class="field">
                                        <div class="ui radio checkbox">
                                            <form:radiobutton path="isEmail" value="Y"/>
                                            <label>사용</label>
                                        </div>
                                    </div>
                                    <div class="field">
                                        <div class="ui radio checkbox">
                                            <form:radiobutton path="isEmail" value="N"/>
                                            <label>사용안함 (라이센스:${license.emailLicense.currentLicence}/${license.emailLicense.licence})</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:if>
            <c:if test="${services.contains('APP') || services.contains('API')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">통계, 모니터링 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isStat" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isStat" value="N"/>
                                        <label>사용안함 (라이센스:${license.statLicence.currentLicence}/${license.statLicence.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="four wide column"><label class="control-label">상담원연결 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isCti" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isCti" value="N"/>
                                        <label>사용안함 (라이센스:${license.ctiLicence.currentLicence}/${license.ctiLicence.licence})</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <span style="color: slategrey;">CTI 전화상담 사용. 외부 연동일 경우 API 연결 개수</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${services.contains('KATLK') || services.contains('ECHBT')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">상담톡 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isTalk" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isTalk" value="N"/>
                                        <label>사용안함 (라이센스:${license.talkLicense.currentLicence}/${license.talkLicense.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${services.contains('CHATT') || services.contains('CHATWIN') || services.contains('CHATMEMO')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">메신저 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isChatt" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isChatt" value="N"/>
                                        <label>사용안함 (라이센스:${license.chattLicense.currentLicence}/${license.chattLicense.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${services.contains('ASTIN')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">통합상담지원 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isAstIn" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isAstIn" value="N"/>
                                        <label>사용안함 (라이센스:${license.astinLicense.currentLicence}/${license.astinLicense.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${services.contains('ASTOUT')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">독립상담지원 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isAstOut" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isAstOut" value="N"/>
                                        <label>사용안함 (라이센스:${license.astoutLicense.currentLicence}/${license.astoutLicense.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${services.contains('BSTT')}">
                <div class="row">
                    <div class="four wide column"><label class="control-label">STT 원문서비스 여부</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isAstStt" value="Y"/>
                                        <label>사용</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="isAstStt" value="N"/>
                                        <label>사용안함 (라이센스:${license.aststtLicense.currentLicence}/${license.aststtLicense.licence})</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="row">
                <div class="four wide column"><label class="control-label">녹취권한-듣기</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${recordingAuthorityTypes}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="listeningRecordingAuthority" value="${RecordingAuthorityType.of(e.key).code}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">녹취권한-다운</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${recordingAuthorityTypes}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="downloadRecordingAuthority" value="${RecordingAuthorityType.of(e.key).code}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">녹취권한-삭제</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${recordingAuthorityTypes}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="removeRecordingAuthority" value="${RecordingAuthorityType.of(e.key).code}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">데이터 검색 권한</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <c:forEach var="e" items="${dataSearchAuthorityTypes}">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <form:radiobutton path="dataSearchAuthority" value="${DataSearchAuthorityType.of(e.key).code}"/>
                                        <label>${g.htmlQuote(e.value)}</label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">휴대전화번호</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="hpNumber"/></div>
                </div>
                <div class="four wide column"><label class="control-label">이메일</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="emailInfo"/></div>
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
    window.checkDuplicatedId = function () {
        const id = modal.find('[name=id]').val();
        if (!id) return;

        restSelf.get('/api/user/is-id-available?userId=' + encodeURIComponent(id), null, function () {
            $('.-usable-id-expression').hide();
            $('.-unusable-id-expression').show();
            $('#isDuplicate').val('N');
        }, true).done(function () {
            $('.-unusable-id-expression').hide();
            $('.-usable-id-expression').show();
            $('#isDuplicate').val('Y');
        });
    };

    window.checkDuplicate = function () {
        $('#isDuplicate').val('N');
    }

    window.popupUpdatePasswordModal = function (id) {
        popupReceivedHtml('/admin/user/user/user/' + encodeURIComponent(id) + '/modal-update-password', 'modal-update-password', 'tiny');
    };
</script>
