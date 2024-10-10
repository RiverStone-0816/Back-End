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

<div id="component-comment-wrap">
    <div class="title-wrap">
        <h3 class="title">코멘트</h3>

        <c:if test="${!g.getUser().getIdType().equals('A')}">
            <div class="filter-wrap">
                <select name="comment-filter" id="comment-filter">
                    <option value="">전체</option>
                    <option value="replied">답변완료</option>
                    <option value="not-replied">미답변</option>
                </select>
                <button class="refresh-btn"><i class="sync alternate icon"></i></button>
            </div>
        </c:if>
    </div>

    <div class="content-list-wrap">
        <ul class="content-list">
            <c:forEach var="comment" items="${commentList}" varStatus="status">
                <li
                        class="comment-item"
                        data-create="${comment.createdAt}"
                        data-kms-category-id="${comment.knowledge.category}"
                        data-kms-category-name="${comment.categoryName}"
                        data-kms-id="${comment.knowledge.id}"
                        data-kms-title="${comment.knowledge.title}"
                        data-kms-content="<c:out value='${comment.knowledge.content}' />"
                        data-requester-id="${comment.requester}"
                        data-requester-name="${comment.requesterName}"
                        data-requester-content="${comment.requestContent}"
                        data-reply-content="${comment.replyContent}"
                >
                    <div class="info-wrap">
                        <div class="date">
                                ${comment.formatCreatedAt}
                        </div>
                        <div class="title" data-comment-id="${comment.knowledge.id}">${comment.knowledge.title}</div>
                    </div>
                    <div class="content-wrap">
                        ${comment.requesterName} - ${comment.requestContent}
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div id="comment-modal-wrap">
        <div id="comment-modal">
            <div class="header">코멘트</div>

            <div class="content-wrap">
                <div class="kms-title"><span class="category"></span> <span class="value"></span></div>
                <div class="kms-content"></div>
                <div class="request-wrap">
                    <div class="request-info">
                        <div class="request-create"></div>
                        <div class="request-name"></div>
                    </div>

                    <table class="request-table">
                        <tbody>
                        <tr>
                            <td class="label">상담원<br /> 코멘트</td>
                            <td class="value"></td>
                        </tr>
                        </tbody>
                    </table>

                    <table class="reply-table">
                        <tbody>
                        <tr>
                            <td class="label">답변</td>
                            <td class="value"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="actions">
<%--                <button type="button" class="ui button modal-close">취소</button>--%>
                <button type="submit" class="ui blue button post-btn">확인</button>
            </div>
        </div>
    </div>
</div>

<script>

    // 질문/답변 모달
    $(document).on('click', '.content-list .comment-item', (e) => {
        const target = $(e.currentTarget)
        const categoryId = $(target).attr('data-kms-category-id')
        const create = $(target).attr('data-create')
        const kmsId = $(target).attr('data-kms-id')
        const kmsTitle = $(target).attr('data-kms-title')
        const kmsCategoryName = $(target).attr('data-kms-category-name')
        const kmsContent = $(target).attr('data-kms-content')
        const requesterId = $(target).attr('data-requester-id')
        const requesterName = $(target).attr('data-requester-name')
        const requesterContent = $(target).attr('data-requester-content')
        const replyContent = $(target).attr('data-reply-content')

        $('#comment-modal-wrap').show()
        $('#comment-modal-wrap .content-wrap .kms-title .category').text('[' + kmsCategoryName + ']')
        $('#comment-modal-wrap .content-wrap .kms-title .value').text(kmsTitle)
        $('#comment-modal-wrap .content-wrap .kms-content').html(kmsContent)
        $('#comment-modal-wrap .content-wrap .request-create').text(moment.unix(create/1000).format('YYYY-MM-DD HH:mm:ss'))
        $('#comment-modal-wrap .content-wrap .request-name').text(requesterName)
        $('#comment-modal-wrap .content-wrap .request-table td.value').text(requesterContent)
        $('#comment-modal-wrap .content-wrap .reply-table td.value').text(replyContent)
        $('#detail-view .comment-wrap').show()
        $('#detail-view .info-file').hide()

    })
    // 질문/답변 모달 닫기
    $(document).on('click', '#comment-modal-wrap .actions button', (e) => {
        $('#comment-modal-wrap').hide()
    })

    function getList(e){
        const commentFilterType = $('#comment-filter option:selected').val()
        console.log('commentFilterType = ', commentFilterType)
        const url = '/counsel/kms/comment/json?commentFilterType=' + commentFilterType
        console.log('url = ', url)
        restSelf.get(url).done((res) => {
            const {categoryList, commentList} = res.data.data

            $('#component-comment-wrap .content-list-wrap .content-list li').remove()

            for (let comment of commentList) {
                const {categoryName, createdAt, formatCreatedAt, knowledge, replyContent, requestContent, requester, requesterName} = comment
                const {bookmarked, bookmarkedCount, category, content, hits, id, liked, likes, tags, title, updatedAt, visible} = knowledge

                $('#component-comment-wrap .content-list-wrap .content-list').append(
                    '<li' +
                    '        class="comment-item"' +
                    '        data-create="'+ createdAt +'"' +
                    '        data-kms-category-id="'+ category +'"' +
                    '        data-kms-category-name="'+ categoryName +'"' +
                    '        data-kms-id="'+ id +'"' +
                    '        data-kms-title="'+ title +'"' +
                    '        data-kms-content="'+ content +'"' +
                    '        data-requester-id="'+ requester +'"' +
                    '        data-requester-name="'+ requesterName +'"' +
                    '        data-requester-content="'+ requestContent +'"' +
                    '        data-reply-content="'+ replyContent +'"' +
                    '>' +
                    '    <div class="info-wrap">' +
                    '        <div class="date">' +
                    '            '+ formatCreatedAt +'' +
                    '        </div>' +
                    '        <div class="title" data-comment-id="'+ id +'">'+ title +'</div>' +
                    '    </div>' +
                    '    <div class="content-wrap">' +
                    '        '+ requesterName +' - '+ requestContent +'' +
                    '    </div>' +
                    '</li>'
                )
            }
        })
    }

    // 새로고침
    $('#component-comment-wrap .refresh-btn').on('click', () => {
        getList()
    })



</script>