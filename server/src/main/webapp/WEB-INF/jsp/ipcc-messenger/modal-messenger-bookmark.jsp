<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <form:form commandName="form" cssClass="ui modal window -json-submit" data-method="put"
               action="${pageContext.request.contextPath}/api/chatt/bookmark"
               data-before="prepareWriteMessengerBookmarkFormData" data-done="doneSubmitMessengerBookmarkFormData">

        <i class="close icon"></i>
        <div class="header">
            <span class="material-icons"> star </span> 즐겨찾기 편집
        </div>
        <div class="content">
            <div class="inner-box bb-unset pb-unset pull-height">
                <div class="ui icon fluid input small">
                    <input type="text" id="messenger-filter-text" placeholder="검색어 입력">
                    <i class="search link icon"></i>
                </div>
                <div class="ui grid -moving-container">
                    <div class="seven wide column pr-unset pb-unset">
                        <div class="select-multiple-title">사용자 리스트</div>
                        <select class="-left-selector" multiple="multiple">
                            <c:forEach var="e" items="${addOnPersons}">
                                <option value="${g.htmlQuote(e.key)}" class="-user-selector">${g.htmlQuote(e.value)} (${g.htmlQuote(e.key)})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="two wide column pb-unset">
                        <div class="select-multiple-control">
                            <div class="btn-wrap">
                                <button type="button" class="ui icon button -to-right">
                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/arrow-right.jsp">
                                        <jsp:param name="width" value="16"/>
                                        <jsp:param name="height" value="16"/>
                                    </jsp:include>
                                </button>
                                <button type="button" class="ui icon button -to-left">
                                    <jsp:include page="/WEB-INF/jsp/ipcc-messenger/svg/arrow-left.jsp">
                                        <jsp:param name="width" value="16"/>
                                        <jsp:param name="height" value="16"/>
                                    </jsp:include>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="seven wide column pl-unset pb-unset">
                        <div class="select-multiple-title">추가된 사용자</div>
                        <select name="memberList" class="-right-selector" multiple="multiple">
                            <c:forEach var="e" items="${memberList}">
                                <option value="${g.htmlQuote(e.id)}">${g.htmlQuote(e.idName)} (${g.htmlQuote(e.id)})</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui black button basic tiny close" onclick="closePopup()">취소</button>
            <button type="submit" class="ui darkblue button tiny">확인</button>
        </div>
    </form:form>

    <tags:scripts>
        <script>
            function closePopup() {
                (self.opener = self).close();
            }

            $('#messenger-filter-text').keyup(function () {
                filterItem($('#messenger-filter-text'));
            });

            function filterItem($this) {
                const text = ($this.val() || '').trim();

                $('.-user-selector').each(function () {
                    if ($(this).text().indexOf(text) >= 0) {
                        $(this).show();
                    } else {
                        $(this).hide();
                    }
                });
            }

            function prepareWriteMessengerBookmarkFormData(data) {
                data.memberList = [];
                $('[name="memberList"] option').each(function () {
                    data.memberList.push($(this).val());
                });
            }

            function doneSubmitMessengerBookmarkFormData() {
                if (window.isElectron) {
                    ipcRenderer.send('loadBookmarks');
                } else {
                    messenger.loadBookmarks();
                }

                closePopup();
            }
        </script>
    </tags:scripts>
</mobileTags:layout>
