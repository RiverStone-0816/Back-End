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

<form:form modelAttribute="form" cssClass="ui modal">

    <i class="close icon"></i>
    <div class="header">보이는 ARS 메뉴 [ivr명: ${g.htmlQuote(entity.name)}]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">컨텍스트명</label>
                    <div class="ui slider checkbox">
                            ${form.headerUse == 'Y' ? '사용' : '사용안함'}
                    </div>
                </div>
                <div class="twelve wide column">
                    <div class="ui input fluid">
                            ${g.htmlQuote(form.headerStr)}
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">텍스트영역</label>
                    <div class="ui slider checkbox">
                            ${form.textareaUse == 'Y' ? '사용' : '사용안함'}
                    </div>
                </div>
                <div class="twelve wide column">
                    <div class="ui fluid form">
                        <div class="field">
                                ${g.htmlQuote(form.textStr)}
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column">
                    <label class="control-label">입력영역</label>
                    <div class="ui slider checkbox">
                            ${form.inputareaUse}
                    </div>
                </div>
                <div class="twelve wide column ui form">
                    <c:forEach var="i" begin="0" end="4">
                        <div class="fields">
                            <div class="eight wide field">
                                    ${g.htmlQuote(form.titles.get(i).inputTitle)}
                            </div>
                            <div class="three wide field">
                                    ${g.htmlQuote(form.titles.get(i).maxLen)}
                            </div>
                            <div class="five wide field">
                                <div class="ui form">
                                    <div class="field">
                                            ${form.titles.get(i).isView == 'Y' ? '보임' : '숨김'}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:forEach var="i" begin="0" end="9">
                        <div class="fields">
                            <div class="two wide field">
                                    ${g.htmlQuote(form.dtmfs.get(i).dtmfValue)}
                            </div>
                            <div class="ten wide field">
                                    ${g.htmlQuote(form.dtmfs.get(i).dtmfTitle)}
                            </div>
                            <div class="five wide field">
                                <div class="ui form">
                                    <div class="field">
                                            ${form.dtmfs.get(i).isView == 'Y' ? '보임' : '숨김'}
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
                            ${form.controlUse == 'Y' ? '사용' : '사용안함'}
                    </div>
                </div>
                <div class="twelve wide column">
                    고정
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
            <%--적용상태 미리보기 - PC 웹상에서 mobile 미리보기 view(추후) --%>
            <%--<button type="button" class="ui left floated button" onclick="webVoiceView()">적용상태 미리보기</button>--%>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
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