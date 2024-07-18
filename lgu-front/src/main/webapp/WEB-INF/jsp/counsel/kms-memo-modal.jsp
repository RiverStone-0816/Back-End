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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal" id="memo-modal">
    <i class="close icon"></i>
    <div class="header">메모[추가]</div>

    <div class="scrolling content rows">
        <div class="ui grid">
            <div class="row">
                <div class="three wide column"><label class="control-label">제목</label></div>
                <div class="thirteen wide column">
                    <div class="ui input fluid">
                        <input type="text" id="memo-title" value="">
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="three wide column">
                    <label class="control-label">내용</label>
                </div>
                <div class="thirteen wide column">
                    <div class="ui form">
                        <div class="field">
                            <textarea id="memo-content" rows="10"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button post-btn">확인</button>
    </div>
</div>
<script>
    function popupModal(id) {
        popupReceivedHtml('/counsel/kms/memo/' + (id || 'new') + '/modal', 'modal-kms');
    }

    $('button.post-btn').on('click', () => {
        const title = $('#memo-title').val()
        const content = $('#memo-content').val()
        const formData = new FormData();
        formData.append('title', title)
        formData.append('content', content)

        const url = contextPath + '/counsel/kms/memo/new'
        $.blockUIFixed();
        $.ajax({
            method: 'post',
            dataType: 'json',
            processData: false,
            contentType: false,
            url: url,
            data: formData,
            success: (res) => {
                if(res.result !== 'success') return alert('정보를 가져오지 못했습니다.')
                alert("메모 등록 완료.")

                // 새 메모로 리스트 업데이트
                const url = '/counsel/kms/memo'
                restSelf.get(url).done((res) => {
                    const {kmsMemoList} = res.data.data
                    $('#component-memo-wrap .memo-wrap li').remove()
                    kmsMemoList.forEach((newMemo) => {
                        const bookmarkTag = newMemo.bookmarked === true ? '<i class="star icon active" data-memo-id='+newMemo.id+' data-bookmarked='+newMemo.bookmarked+'></i>' : '<i class="star outline icon" data-memo-id='+newMemo.id+' data-bookmarked='+newMemo.bookmarked+'></i>'

                        $('#component-memo-wrap .memo-wrap').append(
                            '<li class="memo-item" data-id='+newMemo.id+'>'+
                            '<div class="info-wrap">'+
                            '<div class="date">'+unix_timestamp(newMemo.createdAt/1000)+'</div>'+
                            // '<input class="title" value='+newMemo.title+' data-id='+newMemo.id+' />'+
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
                    // 메모 추가 modal
                    $('.add-memo').on('click', () => {
                        popupModal()
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
                        confirm('정말 삭제하시겠습니까?').done((el) => {
                            const url = '/counsel/kms/memo/'
                            const targetId = e.currentTarget.dataset.memoId

                            restSelf.delete(url+ targetId).done((res) => {
                                alert("삭제 완료.")
                                $(e.target).parents('li.memo-item').remove()
                            })
                        })
                    })
                    // 메모장 height 이벤트
                    $('#component-memo-wrap .memo-item .memo-body').on('click', (e) => {
                        $('#component-memo-wrap .memo-item .memo-body').removeClass('active')
                        $(e.currentTarget).addClass('active')
                    })
                })
            },
            error: (xhr, status, error) => {
                console.log('xhr = ', xhr, 'status = ', status, 'error = ', error)
                if(status === 'error') alert("메모 등록 처리중, 에러가 발생 했습니다.")
            }
        })
            .always((alwaysResult) => {
                $.unblockUI()
                modal.modalHide();
            })
    })
</script>