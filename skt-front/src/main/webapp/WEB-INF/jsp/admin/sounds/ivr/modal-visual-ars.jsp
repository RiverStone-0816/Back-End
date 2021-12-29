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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="post"
           action="${pageContext.request.contextPath}/api/ivr/${g.htmlQuote(entity.code)}/apply"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">보이는 ARS 메뉴수정 [ivr명: ${g.htmlQuote(entity.name)}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">컨텍스트명</label>
                    <div class="ui slider checkbox">
                        <form:checkbox path="headerUse" value="Y"/>
                        <label>사용구분</label>
                    </div>
                </div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                        <form:input path="headerStr"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">텍스트영역</label>
                    <div class="ui slider checkbox">
                        <form:checkbox path="textareaUse" value="Y"/>
                        <label>사용구분</label>
                    </div>
                </div>
                <div class="twelve wide column">
                    <div class="ui fluid form">
                        <div class="field">
                            <form:textarea path="textStr"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">입력영역</label>
                    <div class="ui slider checkbox">
                        <form:checkbox path="inputareaUse" value="Y"/>
                        <label>사용구분</label>
                    </div>
                </div>
                <div class="twelve wide column ui form">
                    <c:forEach var="i" begin="0" end="4">
                        <div class="fields">
                            <div class="eight wide field">
                                <form:input path="titles[${i}].inputTitle" placeholder="타이틀"/>
                            </div>
                            <div class="three wide field">
                                <form:input path="titles[${i}].maxLen" placeholder="자리수" cssClass="-input-numerical"/>
                            </div>
                            <div class="five wide field">
                                <div class="ui form">
                                    <div class="field">
                                        <form:select path="titles[${i}].isView">
                                            <form:option value="Y" label="보임"/>
                                            <form:option value="N" label="숨김"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:forEach var="i" begin="0" end="9">
                        <div class="fields">
                            <div class="two wide field">
                                <form:input path="dtmfs[${i}].dtmfValue"/>
                            </div>
                            <div class="ten wide field">
                                <form:input path="dtmfs[${i}].dtmfTitle"/>
                            </div>
                            <div class="five wide field">
                                <div class="ui form">
                                    <div class="field">
                                        <form:select path="dtmfs[${i}].isView">
                                            <form:option value="Y" label="보임"/>
                                            <form:option value="N" label="숨김"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">컨트롤영역</label>
                    <div class="ui slider checkbox">
                        <form:checkbox path="controlUse" value="Y"/>
                        <label>사용구분</label>
                    </div>
                </div>
                <div class="twelve wide column ui form">
                    <div class="fields">
                        <div class="four wide field">
                            <div class="ui checkbox">
                                <form:checkbox path="prev" value="Y"/><label>이전</label>
                            </div>
                        </div>
                        <div class="four wide field">
                            <div class="ui checkbox">
                                <form:checkbox path="first" value="Y"/><label>처음으로</label>
                            </div>
                        </div>
                        <div class="four wide field">
                            <div class="ui checkbox">
                                <form:checkbox path="counseling" value="Y"/><label>상담원연결</label>
                            </div>
                        </div>
                        <div class="four wide field">
                            <div class="ui checkbox">
                                <form:checkbox path="end" value="Y"/><label>종료</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
            <%--적용상태 미리보기 - PC 웹상에서 mobile 미리보기 view(추후) --%>
            <%--<button type="button" class="ui left floated button" onclick="webVoiceView()">적용상태 미리보기</button>--%>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui brand button">확인</button>
    </div>
</form:form>

<script>
    window.prepareWriteFormData = function (data) {
        const titles = data.titles;
        data.titles = [];
        for (let i in titles) {
            if (titles.hasOwnProperty(i)) {
                if (!titles[i].inputTitle || !titles[i].maxLen) continue;
                data.titles.push(titles[i]);
            }
        }

        const dtmfs = data.dtmfs;
        data.dtmfs = [];
        for (let i in dtmfs) {
            if (dtmfs.hasOwnProperty(i)) {
                if (!dtmfs[i].dtmfTitle || !dtmfs[i].dtmfValue) continue;
                data.dtmfs.push(dtmfs[i]);
            }
        }
    };
</script>
