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

<div class="ui modal tiny inverted" id="modal-template-select">
    <i class="close icon"></i>
    <div class="header">템플릿</div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="sixteen wide column">
                    <button type="button" class="ui button mini compact" id="imA">전체</button>
                    <button type="button" class="ui button mini compact" id="imC">회사</button>
                    <button type="button" class="ui button mini compact" id="imG">그룹</button>
                    <button type="button" class="ui button mini compact" id="imP">개인</button>

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
                                    <c:if test="${e.type=='C'}">
                                    <tr>
                                        <td class="imC">
                                            <div class="template-item">
                                                <strong>${g.htmlQuote(e.mentName)}</strong>
                                                <p>${g.htmlQuote(e.ment)}</p>
                                            </div>
                                        </td>
                                        <td class="imC">
                                            <div class="ui form">
                                                <button type="button" class="ui button mini compact"
                                                        onclick="$('#talk-message').val('${g.htmlQuote(g.escapeQuote(e.ment))}'); $('#modal-template-select').modalHide()">선택
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                    </c:if>
                                    <c:if test="${e.type=='G'}">
                                        <tr>
                                            <td class="imG">
                                                <div class="template-item">
                                                    <strong>${g.htmlQuote(e.mentName)}</strong>
                                                    <p>${g.htmlQuote(e.ment)}</p>
                                                </div>
                                            </td>
                                            <td class="imG">
                                                <div class="ui form">
                                                    <button type="button" class="ui button mini compact"
                                                            onclick="$('#talk-message').val('${g.htmlQuote(g.escapeQuote(e.ment))}'); $('#modal-template-select').modalHide()">선택
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${e.type=='P'}">
                                        <tr>
                                            <td class="imP">
                                                <div class="template-item">
                                                    <strong>${g.htmlQuote(e.mentName)}</strong>
                                                    <p>${g.htmlQuote(e.ment)}</p>
                                                </div>
                                            </td>
                                            <td class="imP">
                                                <div class="ui form">
                                                    <button type="button" class="ui button mini compact"
                                                            onclick="$('#talk-message').val('${g.htmlQuote(g.escapeQuote(e.ment))}'); $('#modal-template-select').modalHide()">선택
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
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
    <script>
        function templateSelectPopup() {
            $('#modal-template-select').dragModalShow();
        }

        $("#imA").on("click",function(){
            $(".imC").show();
            $(".imG").show();
            $(".imP").show();
        });

        $("#imC").on("click",function(){
            $(".imC").show();
            $(".imG").hide();
            $(".imP").hide();
        });

        $("#imP").on("click",function(){
            $(".imP").show();
            $(".imG").hide();
            $(".imC").hide();
        });

        $("#imG").on("click",function(){
            $(".imG").show();
            $(".imC").hide();
            $(".imP").hide();
        });
    </script>
</tags:scripts>
<%-- </tags:tabContentLayout>--%>
