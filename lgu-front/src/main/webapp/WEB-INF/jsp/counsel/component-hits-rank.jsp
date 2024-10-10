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

<div id="component-hits-rank-wrap">
    <div class="title-wrap">
        <h3 class="title">조회랭킹</h3>
        <div class="filter-wrap">
            <select name="" id="hits-rank-select">
                <option value="month" selected>이번달</option>
                <option value="week">이번주</option>
                <option value="day">오늘</option>
            </select>
        </div>
    </div>

    <div class="content-list-wrap">
        <ul class="content-list">
            <c:forEach var="item" items="${hitsRankList}" varStatus="status">
                <li class="hits-rank-item" data-board-id="${item.knowledge.id}">
                    <c:choose>
                        <c:when test="${status.count <= 3}">
                            <span class="item-ranking">${status.count}</span>
                        </c:when>
                    </c:choose>
                    <div class="category">
                        <c:forEach var="category" items="${categoryList}" varStatus="status">
                            <c:if test="${category.id eq item.knowledge.category}">
                                [${category.name}]
                            </c:if>
                        </c:forEach>
                    </div>
                    <div class="hits-rank-title">${item.knowledge.title}</div>
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
    $('#hits-rank-select').on('change', (e) => {
        const selected = $(e.currentTarget).val()
        let startDate = ''
        let endDate = ''
        const url = '/counsel/kms/hits-rank'

        switch(selected) {
            case 'month':
                startDate = dayjs().startOf('month').format('YYYY-MM-DD')
                endDate = dayjs().endOf('month').format('YYYY-MM-DD')
                break;
            case 'week':
                startDate = dayjs().startOf('week').format('YYYY-MM-DD')
                endDate = dayjs().endOf('week').format('YYYY-MM-DD')
                break;
            case 'day':
                startDate = dayjs().format('YYYY-MM-DD')
                endDate = dayjs().format('YYYY-MM-DD')
                break;
            default:
                startDate = new Date(now.getYear(), now.getMonth(), 1);
                endDate = new Date(now.getYear(), now.getMonth()+1, 0);
                break;
        }
        restSelf.get(url, {startDate, endDate}).done((res) => {
            const {
                hitsRankList, categoryList
            } = res.data.data
            const {
                bookmarked, bookmarkedCount, category, content, hits, id, liked, likes, tags, title, updatedAt, visible,
            } = hitsRankList

            $('#component-hits-rank-wrap .content-list-wrap .content-list li').remove()

            hitsRankList.forEach((value, index) => {
                let categoryName = categoryList.filter((category) => {
                    if(category.id === value.knowledge.category) {return category}
                })[0]
                $('#component-hits-rank-wrap .content-list-wrap .content-list').append(
                    '<li class="hits-rank-item" data-board-id='+value.knowledge.id+'>'+
                    (index < 3 ? '<span class="item-ranking">'+(index+1)+'</span>' : '' )+
                    '<div class="category">'+
                    '[' + categoryName.name + ']' +
                    '</div>'+
                    '<div class="hits-rank-title">'+value.knowledge.title+'</div>'+
                    '</li>'
                )
            })

            $('#component-hits-rank-wrap .hits-rank-item').on('click', (e) => {
                getList(e)
            })
        })
    })

    // 상세보기
    $('#component-hits-rank-wrap .hits-rank-item').on('click', (e) => {
        getList(e)
    })
</script>