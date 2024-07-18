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

<div id="component-memo-wrap">
    <div class="title-wrap">
        <h3 class="title">메모장</h3>
        <div class="filter-wrap">
            <input id="memo-search" type="text" placeholder="메모장 검색">
            <button class="add-memo"><i class="plus icon"></i></button>
        </div>
    </div>

    <ul class="memo-wrap">
        <c:choose>
            <c:when test="${memoList != null && memoList.size() > 0}">
                <c:forEach var="memo" items="${memoList}" varStatus="status">
                    <li class="memo-item" data-id="${memo.id}">
                        <div class="info-wrap">
                            <div class="date"><fmt:formatDate value="${memo.createdAt}" pattern="yyyy.MM.dd" /></div>
                                <%--                            <div class="date">${memo.createdAt}</div>--%>
                            <input class="title" value="${memo.title}" data-id="${memo.id}" data-memo-value="${memo.title}" />
                            <div class="icon-wrap">
                                <div class="star" data-memo-id="${memo.id}" data-bookmarked="${memo.bookmarked}">
                                    <c:if test="${memo.bookmarked eq 'true'}">
                                        <i class="star icon active" data-memo-id="${memo.id}" data-bookmarked="${memo.bookmarked}"></i>
                                    </c:if>
                                    <c:if test="${memo.bookmarked eq 'false'}">
                                        <i class="star outline icon" data-memo-id="${memo.id}" data-bookmarked="${memo.bookmarked}"></i>
                                    </c:if>
                                </div>
                                <div class="trash" data-memo-id="${memo.id}">
                                    <i class="trash alternate outline icon" data-memo-id="${memo.id}"></i>
                                </div>
                            </div>
                        </div>
                        <textarea name="" cols="30" rows="10" class="memo-body" data-id="${memo.id}" data-memo-value="${memo.content}">${memo.content}</textarea>
                    </li>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <li class="memo-item not-found" data-id="${memo.id}">
                    메모 목록이 없습니다.
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>

<script>
    // 날짜 변환
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

    function initialBindingEvent(){
        // 추가 모달
        $('.add-memo').on('click', () => {
            popupModal()
        })
        // 메모장 active 이벤트
        $('#component-memo-wrap .memo-item .memo-body').on('click', (e) => {
            $('#component-memo-wrap .memo-item .memo-body').removeClass('active')
            $(e.currentTarget).addClass('active')
        })
        // 북마크
        $('#component-memo-wrap .info-wrap .icon-wrap div.star').on('click', (e) => {
            e.stopPropagation()
            const targetId = e.currentTarget.dataset.memoId
            const bookmarked = e.currentTarget.dataset.bookmarked === 'true' ? false : true
            const url = '/counsel/kms/memo/' + targetId + '/bookmarked/' + bookmarked

            restSelf.put(url).done((res) => {
                if(bookmarked === true || bookmarked === "true") {
                    $(e.currentTarget).attr('data-bookmarked', bookmarked, "data-id", targetId).empty().append(
                        '<i class="star icon active" data-memo-id='+targetId+' data-bookmarked='+bookmarked+'></i>'
                    )
                }
                else {
                    $(e.currentTarget).attr('data-bookmarked', bookmarked, "data-id", targetId).empty().append(
                        '<i class="star icon outline" data-memo-id='+targetId+' data-bookmarked='+bookmarked+'></i>'
                    )
                }
            })
        })
        // 삭제
        $('#component-memo-wrap .info-wrap .icon-wrap .trash').on('click', (e) => {
            e.stopPropagation()
            confirm('정말 삭제하시겠습니까?').done((el) => {
                const url = '/counsel/kms/memo/'
                const targetId = e.currentTarget.dataset.memoId

                restSelf.delete(url+ targetId).done((res) => {
                    alert("삭제 완료.")
                    $(e.target).parents('li.memo-item').remove()
                })
            })
        })
        // 메모장 제목 update
        // $('#component-memo-wrap .memo-wrap .memo-item .info-wrap .title').on('focusout', (e) => {
        $(document).on('focusout', '#component-memo-wrap .memo-wrap .memo-item .info-wrap .title', (e) => {
            updateMemo(e)
        })
        // $('#component-memo-wrap .memo-item .memo-body').on('focusout', (e) => {
        $(document).on('focusout', '#component-memo-wrap .memo-item .memo-body', (e) => {
            updateMemo(e)
        })
    }

    function updateMemo(e){
        const $titleEl = $(e.currentTarget).parents('li.memo-item').find('input.title')
        const title = $titleEl.val()

        const $contentEl = $(e.currentTarget).parents('li.memo-item').find('textarea.memo-body')
        const content = $contentEl.val()

        const targetId = e.currentTarget.dataset.id
        const url = '/counsel/kms/memo/' + targetId
        const formData = new FormData();

        // 변했는지 체크
        const targetPrevValue = $(e.currentTarget).attr('data-memo-value')
        const targetNextValue = $(e.currentTarget).val()

        if(targetPrevValue == targetNextValue) return

        formData.append('title', title)
        formData.append('content', content)
        restSelf.put(url, {title, content}).done((res) => {
            const {result} = res.data.data
            if(result === 'success') alert("수정 되었습니다.")
            $titleEl.attr('data-memo-value', title).val(title)
            $contentEl.attr('data-memo-value', content).val(content)
        })
    }

    function popupModal(id) {
        popupReceivedHtml('/counsel/kms/memo/' + (id || 'new') + '/modal', 'modal-kms');
    }

    // 검색
    $('#memo-search').keydown((e) => {
        if(e.which !== 13) return
        const keyword = $('#memo-search').val() || ''

        // 리스트 조회
        const url = '/counsel/kms/memo?keyword=' + keyword
        restSelf.get(url).done((res) => {
            const {kmsMemoList} = res.data.data
            $('#component-memo-wrap .memo-wrap li').remove()
            kmsMemoList.forEach((newMemo) => {
                const bookmarkTag = newMemo.bookmarked === true ? '<i class="star icon active" data-memo-id='+newMemo.id+' data-bookmarked='+newMemo.bookmarked+'></i>' : '<i class="star outline icon" data-memo-id='+newMemo.id+' data-bookmarked='+newMemo.bookmarked+'></i>'

                $('#component-memo-wrap .memo-wrap').append(
                    '<li class="memo-item">'+
                    '<div class="info-wrap">'+
                    '<div class="date">'+unix_timestamp(newMemo.createdAt/1000)+'</div>'+
                    '<input class="title" value="'+newMemo.title+'" data-id='+newMemo.id+' />'+
                    '<div class="icon-wrap">'+
                    '    <div class="star" data-memo-id='+newMemo.id+' data-bookmarked='+newMemo.bookmarked+'>'+
                    bookmarkTag+
                    '    </div>'+
                    '    <div class="trash" data-memo-id='+newMemo.id+'>'+
                    '        <i class="trash alternate outline icon" data-memo-id='+newMemo.id+'></i>'+
                    '    </div>'+
                    '</div>'+
                    '</div>'+
                    '<textarea name="" cols="30" rows="10" class="memo-body" data-id='+newMemo.id+'>'+newMemo.content+'</textarea>'+
                    '</li>'
                )
            })

            // 이벤트 다시 바인드
            initialBindingEvent()
        })
    })

    initialBindingEvent()
</script>