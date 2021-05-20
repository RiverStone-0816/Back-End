<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <form:form commandName="form" cssClass="ui modal window tiny -json-submit" data-method="post"
               action="${pageContext.request.contextPath}/api/memo/${entity.seq}"
               data-done="doneSubmit">

        <input type="hidden" name="isWriteToMe" value="${entity.sendUserid == user.id}"/>
        <input type="hidden" name="receiveUserIds" multiple="multiple" value="${g.escapeQuote(entity.sendUserid)}"/>

        <i class="close icon"></i>
        <div class="header">
            <span class="material-icons"> create </span> ${g.user.id == entity.sendUserid ? '메세지 확인' : '답장 보내기'}
        </div>

        <div class="content">
            <div class="inner-box overflow-hidden">
                <c:choose>
                    <c:when test="${g.user.id == entity.sendUserid}">
                        <span class="pull-left">받는사람 : <c:forEach var="e" items="${entity.readReceiveUserInfos}"><span class="word important">${g.htmlQuote(e.userName)}</span></c:forEach><c:forEach var="e" items="${entity.unreadReceiveUserInfos}"><span class="word">${g.htmlQuote(e.userName)}</span></c:forEach></span>
                    </c:when>
                    <c:otherwise>
                        <span class="pull-left">보낸사람 : ${g.htmlQuote(entity.sendUserName)}</span>
                    </c:otherwise>
                </c:choose>
                <span class="pull-right"><fmt:formatDate value="${entity.insertTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
            </div>
            <div class="inner-box">
                <div class="ui form">
                    <div class="field">
                        <textarea rows="6" readonly>${g.htmlQuote(entity.content)}</textarea>
                    </div>
                </div>
            </div>
            <c:if test="${g.user.id != entity.sendUserid}">
                <div class="inner-box bb-unset">
                    <div class="ui form">
                        <div class="field">
                            <form:textarea path="content" rows="6" placeholder="내용 입력"/>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
        <div class="actions">
            <button type="button" class="ui black button basic tiny close" onclick="closePopup()">닫기</button>
            <c:if test="${g.user.id != entity.sendUserid}">
                <button type="submit" class="ui darkblue button tiny">보내기</button>
            </c:if>
        </div>
    </form:form>

    <tags:scripts>
        <script>
            function closePopup() {
                (self.opener = self).close();
            }

            if (window.isElectron) {
                function doneSubmit(form, response) {
                    $('form').asJsonData().done(function (data) {
                        data.receiveUserIds.map(function (receiveUserId) {
                            ipcRenderer.send("sendMemo", {"receiveUserId" : receiveUserId, "data" : response.data});
                        });

                        alert('전달되었습니다.', function () {
                            closePopup();
                        });
                    });
                }
            } else {
                function doneSubmit(form, response) {
                    $('form').asJsonData().done(function (data) {
                        data.receiveUserIds.map(function (receiveUserId) {
                            messenger.communicator.sendMemo(receiveUserId, response.data);
                        });

                        $(self.opener.document).find('#tab5 button[type=submit]').click();
                        alert('전달되었습니다.', function () {
                            closePopup();
                        });
                    });
                }
            }
        </script>
    </tags:scripts>
</mobileTags:layout>
