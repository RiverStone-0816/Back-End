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

<table class="ui celled table unstackable" id="todo-list">
    <thead>
    <tr>
        <th style="min-width: 60px;">채널</th>
        <th style="min-width: 130px;">요청시간</th>
        <th style="min-width: 145px;">고객정보</th>
        <th style="min-width: 70px;">처리상태</th>
        <th style="min-width: 70px;">완료</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="e" items="${list}">
        <tr>
            <td>${g.htmlQuote(message.getEnumText(e.todoKind))}</td>
            <td><fmt:formatDate value="${e.regdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>
                <div class="ui action input fluid" style="text-align: center;">
                    <c:if test="${e.todoInfo != null && e.todoInfo != ''}">
                        <input type="text" readonly value="${g.htmlQuote(e.todoInfo)}"/>
                       <button type="button" class="ui icon button" onclick="$('#calling-number').val('${g.htmlQuote(e.todoInfo)}')"><i class="phone icon"></i></button>
                    </c:if>
                </div>
            </td>
            <td>${g.htmlQuote(message.getEnumText(e.todoStatus))}</td>
            <td>
                <button type="button" class="ui button mini compact" data-method="put" style="width:36px;" onclick="changeToDoDone(${e.seq})"><i class="check icon" ></i></button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script>
    function changeToDoDone(seq) {
        restSelf.put("api/counsel/to-do/done/"+ seq).done(function(){
            loadTodoList();
        });
    }

</script>