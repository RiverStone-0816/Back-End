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

<div id="component-notice-wrap">
    <div class="title-wrap">
        <h3 class="title">공지사항</h3>
    </div>

    <div class="content-list-wrap">
        <ul class="content-list">
            <c:forEach var="item" items="${noticeList}" varStatus="status">
                <li class="notice-item" data-board-id="${item.id}">
                    <div class="notice-title">${item.title} [<fmt:formatDate value="${item.createdAt}" pattern="MM.dd" />]</div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<script>
    function unix_timestamp(t){
        var date = new Date(t*1000);
        var year = date.getFullYear();
        var month = "0" + (date.getMonth()+1);
        var day = "0" + date.getDate();
        var hour = "0" + date.getHours();
        var minute = "0" + date.getMinutes();
        var second = "0" + date.getSeconds();
        return year + "-" + month.substr(-2) + "-" + day.substr(-2) + " " + hour.substr(-2) + ":" + minute.substr(-2) + ":" + second.substr(-2);
    }

    function getList(e){
        const boardId = e.currentTarget.dataset.boardId
        const url = '/counsel/kms/notice/' + boardId
        restSelf.get(url).done((res) => {
            const {
                id, important, title, content, hits, creator, createdAt, creatorName
            } = res.data.data
            const files = res.data.data.files.map((file) => {
                return "<a href='https://kmsweb.uplus.co.kr/api/kms/file/"+ file.company +"/" + file.id + "'>" + file.originalName +"</a>"
            }).join(', ')

            console.log(id, important, title, content, hits, creator, unix_timestamp(createdAt), creatorName);

            $('#detail-view .tags').empty()
            $('#detail-view').show();
            $('#detail-view .category').empty()
            $('#detail-view .header').text(title)
            $('#detail-view .detail-content').empty().append(content)
            $('#detail-view .date-value').text(unix_timestamp(createdAt/1000))
            $('#detail-view .info-icon-list .views .value').text(hits)
            $('#detail-view .info-icon-list .like').empty()
            $('#detail-view .info-icon-list .bookmark').empty()
            $('#detail-view .comment-wrap').hide()
            $('#detail-view .info-file').show().empty().append("" +
                "첨부파일 : " + files);
        })
    }

    $('#component-notice-wrap .notice-item').on('click', (e) => {
        getList(e)
    })
</script>
