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

<div class="ui modal inverted custom" id="modal-template-select">
    <i class="close icon"></i>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="sixteen wide column">
                    <table class="ui table celled compact fixed">
                        <thead>
                        <tr>
                            <th>내용</th>
                            <th class="four wide">선택</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${talkTemplates.size() > 0}">
                                <c:forEach var="e" items="${talkTemplates}">
                                    <tr>
                                        <td>
                                            <div class="template-item">
                                                <strong>${g.htmlQuote(e.mentName)}</strong>
                                                <p>${g.htmlQuote(e.ment)}</p>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="ui form">
                                                <button type="button" class="ui button mini compact"
                                                        onclick="$('#talk-message').val('${g.htmlQuote(g.escapeQuote(e.ment))}'); $('#modal-template-select').modalHide()">선택
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="2">
                                        <div class="null-data">등록된 채팅상담 템플릿이 없습니다.</div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<tags:scripts>
    <script  >
        function templateSelectPopup() {
            $('#modal-template-select').modal({
                context: $('#talk-room').parent()
            }).modal('show');
        }
    </script>
</tags:scripts>
