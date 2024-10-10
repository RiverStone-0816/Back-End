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

<div id="component-recommend-rank-wrap">
    <div class="title-wrap">
        <h3 class="title">추천랭킹</h3>
        <div class="filter-wrap">
            <button class="refresh-btn"><i class="sync alternate icon"></i></button>
        </div>
    </div>

    <div class="content-list-wrap">
        <ul class="content-list">
            <c:forEach var="item" items="${recommendList}" varStatus="status">
                <li class="recommend-rank-item" data-board-id="${item.id}">
                    <c:choose>
                        <c:when test="${status.count <= 3}">
                            <span class="item-ranking">${status.count}</span>
                        </c:when>
                    </c:choose>
                    <div class="category">
                        <c:forEach var="category" items="${categoryList}" varStatus="status">
                            <c:if test="${category.id eq item.category}">
                                [${category.name}]
                            </c:if>
                        </c:forEach>
                    </div>
                    <div class="recommend-rank-title">${item.title}</div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<script>

    function getList(e){
        const boardId = e.currentTarget.dataset.boardId
        const url = '/counsel/kms/' + boardId
        restSelf.get(url).done((res) => {
            const {categoryList, kmsList} = res.data.data
            const {
                bookmarked, bookmarkedCount, category, content, hits, id, liked, likes, tags, title, updatedAt, visible,
            } = kmsList
            const categoryName = categoryList.filter((v) => v.id === category)[0]
            let likeTag = liked === true ? '<i class="icon thumbs up active" data-id='+id+' data-liked='+liked+'></i> <span class="value">'+likes+'</span>' : '<i class="icon thumbs up outline" data-id='+id+' data-liked='+liked+'></i> <span class="value">'+likes+'</span>'
            let bookmarkTag = bookmarked === true ? '<i class="icon star" style="color: #dfdf0f" data-id='+id+' data-bookmarked='+bookmarked+'></i> <span class="value">'+bookmarkedCount+'</span>' : '<i class="icon star outline" data-id='+id+' data-bookmarked='+bookmarked+'></i> <span class="value">'+bookmarkedCount+'</span>'

            $('#detail-view .tags').empty()
            $('#detail-view').show();
            $('#detail-view .category').text('[' + categoryName.name + ']');
            tags.forEach((tag) => {
                $('#detail-view .tags').append("<li>#"+tag+"</li>")
            })
            $('#detail-view .header').text(title)
            $('#detail-view .detail-content').empty().append(content)
            $('#detail-view .date-value').text(unix_timestamp(updatedAt/1000))
            $('#detail-view .info-icon-list .views .value').text(hits)
            $('#detail-view .info-icon-list .like').html(likeTag)
            $('#detail-view .info-icon-list .bookmark').html(bookmarkTag)
            $('#detail-view .comment-wrap').show()
            $('#detail-view .info-file').hide()
        })
    }

    // 새로고침
    $('.refresh-btn').on('click', () => {
        const url = '/counsel/kms/recommend-rank'
        restSelf.get(url).done((res) => {
            const {
                recommendList, categoryList
            } = res.data.data

            $('#component-recommend-rank-wrap .content-list-wrap .content-list li').remove()
            recommendList.forEach((value, index) => {
                let categoryName = categoryList.filter((category) => {
                    if(category.id === value.category) {return category}
                })[0]
                $('#component-recommend-rank-wrap .content-list-wrap .content-list').append(
                    '<li class="recommend-rank-item" data-board-id='+value.id+'>'+
                    (index < 3 ? '<span class="item-ranking">'+(index+1)+'</span>' : '' )+
                    '<div class="category">'+
                    '[' + categoryName.name + ']' +
                    '</div>'+
                    '<div class="recommend-rank-title">'+value.title+'</div>'+
                    '</li>'
                )
            })

            $('#component-recommend-rank-wrap .recommend-rank-item').on('click', (e) => {
                getList(e)
            })
        })
    })

    // 상세보기
    $('#component-recommend-rank-wrap .recommend-rank-item').on('click', (e) => {
        getList(e)
    })
</script>