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
<%--@elvariable id="usingservices" type="java.lang.String"--%>

<aside class="inverted-side-bar kms-side-bar side-bar-test test" id="assist-custom-sidebar">
    <%--아이콘--%>
    <button class="nav-bar">
        <i class="material-icons arrow"> keyboard_arrow_left </i>
    </button>

    <div id="assist-custom-sidebar-wrap">
        <div class="tab">
            <ul>
                <li>
                    <a>
                        지식관리 <i class="angle left icon"></i>
                        <div class="triangle"></div>
                    </a>
                </li>
            </ul>
        </div>

        <div class="content">
            <div class="top">
                <div class="top-inner">
                    <input type="hidden" id="search_category" name="id">
                    <input type="hidden" id="search_sorted" name="sort">
                    <input type="hidden" id="search_type_flag" name="search_type_flag">
                    <div class="search-box">
                        <div class="ui right icon input">
                            <input type="text" id="search_keyword" name="keyword" placeholder="지식을 검색하세요.">
                            <i class="search icon"></i>
                        </div>
                    </div>

                    <div class="filter-box">
                        <h2 class="title">KMS 지식정보</h2>
                        <ul class="filter-list">
                            <li class="active" data-param-value="HITS">정확도</li>
                            <li data-param-value="HITS">조회수</li>
                            <li data-param-value="LIKES">추천수</li>
                            <li data-param-value="CREATED_AT">등록일</li>
                            <li data-param-value="UPDATED_AT">수정일</li>
                        </ul>
                    </div>

                    <div class="top-menu-box">
                        <ul class="menus">
                            <li class="active" data-key="">통합검색</li>
                            <c:forEach var="item" items="${kmsCategoryMenu}" varStatus="status">
                                <li data-key="${item.id}">${item.name}</li>
                            </c:forEach>
                        </ul>
                    </div>

                    <div class="board-box">
                        <ul class="board-list">
                            <c:forEach var="item" items="${kmsList}" varStatus="status">
                                <li class="board-item" data-board-id="${item.id}">
                                    <div class="title-wrap" data-board-id="${item.id}">
                                        <div class="category">
                                            <c:forEach var="category" items="${kmsCategoryList}" varStatus="status">
                                                <c:if test="${category.value.id eq item.category}">
                                                    [${category.value.name}]
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                        <h3 class="title">${item.title}
                                            <a class="external-link" onClick="popupShowModal(${item.id})">
                                                <i class="icon external alternate"></i>
                                            </a>
                                        </h3>
                                        <ul class="sub-info-list">
                                            <li class="view">
                                                <c:if test="${item.visible eq 'true'}">
                                                    <i class="eye icon" style="color: #bf1455"></i>
                                                </c:if>
                                                <c:if test="${item.visible eq 'false'}">
                                                    <i class="icon eye"></i>
                                                </c:if>
                                                <span class="value">${item.hits}</span>
                                            </li>
                                            <li class="like">
                                                <c:if test="${item.liked eq 'true'}">
                                                    <i class="icon thumbs up active" data-id="${item.id}" data-liked="${item.liked}"></i>
                                                </c:if>
                                                <c:if test="${item.liked eq 'false'}">
                                                    <i class="icon thumbs up outline" data-id="${item.id}" data-liked="${item.liked}"></i>
                                                </c:if>
                                                <span class="value">${item.likes}</span>
                                            </li>
                                            <li class="bookmark">
                                                <c:if test="${item.bookmarked eq 'true'}">
                                                    <i class="icon star" style="color: #dfdf0f" data-id="${item.id}" data-bookmarked="${item.bookmarked}"></i>
                                                </c:if>
                                                <c:if test="${item.bookmarked eq 'false'}">
                                                    <i class="icon star outline" data-id="${item.id}" data-bookmarked="${item.bookmarked}"></i>
                                                </c:if>
                                                <span class="value">${item.bookmarkedCount}</span>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="content-wrap" data-board-id="${item.id}">
                                            ${item.content}
                                    </div>
                                    <div class="info-wrap" data-board-id="${item.id}">
                                        <div class="date">${item.updatedAt}</div>
                                        <ul class="tags">
                                            <c:forEach var="tag" items="${item.tags}" varStatus="status">
                                                <li>#${tag}</li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                    <div id="detail-view">
                        <div class="detail-view-inner-wrap">
                            <button class="close-btn"><i class="close icon"></i></button>
                            <input type="hidden" id="has-changes" name="has-changes" value="false">
                            <div class="header">lorem Ipsum</div>

                            <div class="sub-inner">
                                <div class="category-wrap">
                                    <div class="category">[category]</div>
                                </div>
                                <ul class="tags">
                                    <li>#tag1</li>
                                    <li>#tag2</li>
                                    <li>#tag3</li>
                                    <li>#tag4</li>
                                </ul>
                            </div>

                            <div class="detail-content">
                                Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
                            </div>
                            <div class="comment-wrap">
                                <input class="comment-input" type="text" placeholder="수정 요청 사항을 코멘트로 등록 할 수 있습니다.">
                                <button data-post-id="${item.id}" class="add-comment-btn">코멘트 등록</button>
                            </div>
                            <div class="info-wrap">
                                <div class="date">
                                    <div class="icon-wrap">
                                        <i class="calendar alternate outline icon"></i>
                                    </div>
                                    <div class="date-value">
                                        2023.06.22 18:02:45
                                    </div>
                                </div>
                                <ul class="info-icon-list">
                                    <li class="item views"><i class="eye icon"></i> <span class="value">855</span></li>
                                    <li class="item like"><i class="star icon"></i> <span class="value">12</span></li>
                                    <li class="item bookmark"><i class="thumbs up outline icon"></i> <span class="value">3</span></li>
                                </ul>
                            </div>
                            <div class="info-file">
                                첨부파일 : <a href="#">첨부파일1</a>, <a href="#">첨부파일2</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="bottom">
                <div class="dragger-box">
                    <c:forEach var="e" begin="1" end="2" varStatus="status">
                        <div class="-dashboard-widget widget-${status.index == 1 ? 'left' : 'right'}" data-ui-sequence="${e}" data-position="${status.index == 1 ? 'left' : 'right'}"
                                <c:if test="${dashboardSequenceToDashboard.get(e) != null}">
                                    data-id="${dashboardSequenceToDashboard.get(e).dashboardId}"
                                    data-name="${dashboardSequenceToDashboard.get(e).dashboardName}"
                                    data-type="${dashboardSequenceToDashboard.get(e).dashboardType}"
                                    data-value="${dashboardSequenceToDashboard.get(e).dashboardValue}"
                                </c:if>>
                            <h2 class="ui center aligned icon header">
                                <i class="circular plus icon"></i>
                            </h2>
                        </div>
                    </c:forEach>
                </div>

                <div class="bottom-menu-box" id="slider">
                    <ul class="bottom-menus">
                        <li class="-dashboard-component" data-id="0" data-name="공지" data-type="notice" data-value="">
                            공지
                        </li>
                        <li class="-dashboard-component" data-id="1" data-name="북마크" data-type="bookmark" data-value="">
                            북마크
                        </li>
                        <li class="-dashboard-component" data-id="2" data-name="메모장" data-type="memo" data-value="">
                            메모장
                        </li>
                        <li class="-dashboard-component" data-id="3" data-name="최근열람지식" data-type="recent" data-value="">
                            최근열람지식
                        </li>
                        <li class="-dashboard-component" data-id="4" data-name="조회랭킹" data-type="hits-rank" data-value="">
                            조회랭킹
                        </li>
                        <li class="-dashboard-component" data-id="5" data-name="추천랭킹" data-type="recommend-rank" data-value="">
                            추천랭킹
                        </li>
                        <li class="-dashboard-component" data-id="6" data-name="키워드랭킹" data-type="hits-tag-rank" data-value="">
                            키워드랭킹
                        </li>
                        <li class="-dashboard-component" data-id="7" data-name="코멘트" data-type="comment" data-value="">
                            코멘트
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</aside>

<tags:scripts>
    <script>

        function domToString(element) {
            var tmp = document.createElement("DIV");
            tmp.appendChild(element.cloneNode(true));
            return tmp.innerHTML;
        }

        // 컴토넌트 기본 세팅
        const leftComponent = localStorage.getItem('kms-left-component')
        const rightComponent = localStorage.getItem('kms-right-component')
        if(leftComponent){
            const leftCompnentValue = JSON.parse(leftComponent)
            addDashboardComponent(leftCompnentValue.uiSequence, leftCompnentValue.dataId, leftComponent.position)
        }
        if(rightComponent){
            const rightComponentValue = JSON.parse(rightComponent)
            addDashboardComponent(rightComponentValue.uiSequence, rightComponentValue.dataId, leftComponent.position)
        }

        function initialBindingEvent(){
            // 상세페이지 조회
            $('.board-list .title-wrap, .board-list .content-wrap').on('click', (e) => {
                if( $(e.target).is('a') || $(e.target).is('i') ) {
                    e.stopPropagation();
                    return
                }
                const boardId = e.currentTarget.dataset.boardId
                const url = '/counsel/kms/' + boardId

                restSelf.get(url, null, null, true).done((res) => {
                    const {kmsList, categoryList} = res.data.data
                    const {
                        bookmarked,
                        bookmarkedCount,
                        category,
                        content,
                        hits,
                        id,
                        liked,
                        likes,
                        tags,
                        title,
                        updatedAt,
                        visible,
                    } = kmsList

                    // 변경 체크 플래그 초기화
                    $('#detail-view #has-changes').val('false')


                    const categoryInfo = categoryList.filter((v) => v.id === category)[0]
                    let visibleTag = visible === true ? '<i class="eye icon" style="color: #bf1455"></i> <span class="value">'+hits+'</span>' : '<i class="icon eye"></i> <span class="value">'+hits+'</span>'
                    let likeTag = liked === true ? '<i class="icon thumbs up active" data-id='+id+' data-liked='+liked+'></i> <span class="value">'+likes+'</span>' : '<i class="icon thumbs up outline" data-id='+id+' data-liked='+liked+'></i> <span class="value">'+likes+'</span>'
                    let bookmarkTag = bookmarked === true ? '<i class="icon star" style="color: #dfdf0f" data-id='+id+' data-bookmarked='+bookmarked+'></i> <span class="value">'+bookmarkedCount+'</span>' : '<i class="icon star outline" data-id='+id+' data-bookmarked='+bookmarked+'></i> <span class="value">'+bookmarkedCount+'</span>'

                    $('#detail-view .tags').empty()
                    $('#detail-view').show();
                    $('#detail-view .category').text('[' + categoryInfo.name + ']');
                    tags.forEach((tag) => {
                        $('#detail-view .tags').append("<li>#"+tag+"</li>")
                    })

                    titleHTML = title
                    titleHTML = titleHTML +
                        '<a class="external-link" onClick="popupShowModal('+id+')">' +
                        '<i class="icon external alternate"></i>' +
                        '</a>'

                    console.log('titleHTML = ', titleHTML)

                    $('#detail-view .header').html(titleHTML)
                    $('#detail-view .detail-content').empty().append(content)
                    $('#detail-view .comment-wrap .add-comment-btn').attr('post-id', id)
                    $('#detail-view .date-value').text(unix_timestamp(updatedAt/1000))
                    $('#detail-view .info-icon-list .views').html(visibleTag)
                    $('#detail-view .info-icon-list .like').html(likeTag)
                    $('#detail-view .info-icon-list .bookmark').html(bookmarkTag)
                    $('#detail-view .comment-wrap').show()
                    $('#detail-view .info-file').hide()

                    // 상세보기 좋아요 기능
                    $('#assist-custom-sidebar #detail-view .info-icon-list .item .icon.thumbs').on('click', (e) => {
                        const targetId = e.target.dataset.id
                        const targetLiked = e.target.dataset.liked

                        const like = targetLiked === 'true' ? 'false' : 'true'
                        const url = '/counsel/kms/' + targetId + '/like' + '?like=' + like
                        restSelf.get(url, null, null, true).done((res) => {
                            const {
                                bookmarked,
                                bookmarkedCount,
                                category,
                                content,
                                hits,
                                id,
                                liked,
                                likes,
                                tags,
                                title,
                                updatedAt,
                                visible,
                            } = res.data.data

                            if(liked){
                                $(e.target).removeClass('outline').addClass('active')
                                $(e.currentTarget).siblings('span.value').text(likes)
                                e.target.dataset.liked = liked
                            }else{
                                $(e.target).removeClass('active').addClass('outline').data('liked', liked)
                                $(e.currentTarget).siblings('span.value').text(likes)
                                e.target.dataset.liked = liked
                            }
                            $('#detail-view #board-liked').val(liked)
                            $('#detail-view #board-likes').val(likes)
                        })

                        // 상세보기 종료시 게시물 업데이트 유무 기록 (like,bookmark)
                        $('#detail-view #has-changes').val('true')
                    })
                    // 상세보기 북마크 기능
                    $('#assist-custom-sidebar #detail-view .info-icon-list .item .icon.star').on('click', (e) => {
                        const targetId = e.target.dataset.id
                        const targetBookmarked = e.target.dataset.bookmarked

                        const bookmark = targetBookmarked === 'true' ? 'false' : 'true'
                        const url = '/counsel/kms/' + targetId + '/bookmark' + '?bookmark=' + bookmark
                        restSelf.get(url, null, null, true).done((res) => {
                            const {
                                bookmarked, liked, bookmarkedCount
                            } = res.data.data

                            if(bookmarked){
                                $(e.target).removeClass('outline').addClass('active')
                                e.target.dataset.bookmarked = bookmarked
                                $(e.currentTarget).siblings('span.value').text(bookmarkedCount)
                                e.target.dataset.bookmarked = bookmarked
                            }else{
                                $(e.target).removeClass('active').addClass('outline').data('liked', liked)
                                e.target.dataset.bookmarked = bookmarked
                                $(e.currentTarget).siblings('span.value').text(bookmarkedCount)
                                e.target.dataset.bookmarked = bookmarked
                            }
                        })

                        // 상세보기 종료시 게시물 업데이트 유무 기록 (like,bookmark)
                        $('#detail-view #has-changes').val('true')
                    })
                })
            })
            // 좋아요 기능
            $('#assist-custom-sidebar .board-item .sub-info-list .icon.thumbs.up, #assist-custom-sidebar #detail-view .info-icon-list .item .icon.thumbs').on('click', (e) => {
                const targetId = e.target.dataset.id
                const targetLiked = e.target.dataset.liked

                const like = targetLiked === 'true' ? 'false' : 'true'
                const url = '/counsel/kms/' + targetId + '/like' + '?like=' + like
                restSelf.get(url, null, null, true).done((res) => {
                    const {
                        bookmarked, bookmarkedCount, category, content, hits, id, liked, likes, tags, title, updatedAt, visible,
                    } = res.data.data

                    if(liked){
                        $(e.target).removeClass('outline').addClass('active')
                        $(e.currentTarget).siblings('span.value').text(likes)
                        e.target.dataset.liked = liked
                    }else{
                        $(e.target).removeClass('active').addClass('outline').data('liked', liked)
                        $(e.currentTarget).siblings('span.value').text(likes)
                        e.target.dataset.liked = liked
                    }
                })
            })
            // 북마크 기능
            $('#assist-custom-sidebar .board-item .sub-info-list .icon.star').on('click', (e) => {
                const targetId = e.target.dataset.id
                const targetBookmarked = e.target.dataset.bookmarked

                const bookmark = targetBookmarked === 'true' ? 'false' : 'true'
                const url = '/counsel/kms/' + targetId + '/bookmark' + '?bookmark=' + bookmark
                restSelf.get(url, null, null, true).done((res) => {
                    const {
                        bookmarked, liked, bookmarkedCount
                    } = res.data.data

                    if(bookmarked){
                        $(e.target).removeClass('outline').addClass('active')
                        e.target.dataset.bookmarked = bookmarked
                        $(e.currentTarget).siblings('span.value').text(bookmarkedCount)
                        e.target.dataset.bookmarked = bookmarked
                    }else{
                        $(e.target).removeClass('active').addClass('outline').data('liked', liked)
                        e.target.dataset.bookmarked = bookmarked
                        $(e.currentTarget).siblings('span.value').text(bookmarkedCount)
                        e.target.dataset.bookmarked = bookmarked
                    }
                })
            })
            // 태그로 검색
            $('#assist-custom-sidebar .board-item .tags li').on('click', (e) => {
                const shiftKeyEvent = e.shiftKey
                const currentInputValue = $('#search_keyword').val() || ''
                const clickTagName = e.target.outerText.replace('#', '')

                if(shiftKeyEvent) {
                    $('#search_keyword').val(currentInputValue + ' ' + clickTagName)
                    return getKmsList()
                }

                $('#search_keyword').val(clickTagName)
                getKmsList()
            })
        }

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

        // 상세페이지 조회 (모달)
        function popupShowModal(id) {
            console.log('popupShowModal 실행@')
            popupReceivedHtml('/counsel/kms/' + (id) + '/modal', 'modal-kms-wrap');
        }
        // kms 리스트 가져오기
        function getKmsList(){
            $('.content .top').asJsonData().done((data) => { // allSearchingFlag 가 true면 태그/제목/내용 검색, 아니면 태그 검색
                console.log('getKmsList > data = ', data)
                const url = $.addQueryString('/counsel/kms/list', data)
                restSelf.get(url, null, null, true).done((res) => {
                    const {
                        kmsList, categoryList
                    } = res.data.data

                    $('#assist-custom-sidebar .board-list .board-item').remove()
                    kmsList.forEach((v) => {
                        let categoryName = categoryList.filter((category) => {return category.id === v.category})[0];
                        let visibleTag = v.visible === true ? '<i class="eye icon" style="color: #bf1455"></i>' : '<i class="icon eye"></i>'
                        let likeTag = v.liked === true ? '<i class="icon thumbs up active" data-id='+v.id+' data-liked='+v.liked+'></i>' : '<i class="icon thumbs up outline" data-id='+v.id+' data-liked='+v.liked+'></i>'
                        let bookmarkTag = v.bookmarked === true ? '<i class="icon star" style="color: #dfdf0f" data-id='+v.id+' data-bookmarked='+v.bookmarked+'></i>' : '<i class="icon star outline" data-id='+v.id+' data-bookmarked='+v.bookmarked+'></i>'
                        $('#assist-custom-sidebar .board-list').append(
                            '<li class="board-item" data-board-id='+v.id+'>' +
                            '<div class="title-wrap" data-board-id='+v.id+'>'+
                            '<div class="category">'+
                            categoryName.name+
                            '</div>'+
                            '<h3 class="title">' + v.title +
                            '<a '+
                            'class="external-link"'+
                            ' onClick=popupShowModal('+v.id+')'+
                            '>'+
                            '<i class="icon external alternate"></i>'+
                            '</a>'+
                            '</h3>'+
                            '<ul class="sub-info-list">'+
                            '<li class="visible">'+
                            visibleTag+
                            '<span class="value">'+v.hits+'</span>'+
                            '</li>'+
                            '<li class="like">'+
                            likeTag+
                            '<span class="value">'+v.likes+'</span>'+
                            '</li>'+
                            '<li class="bookmark">'+
                            bookmarkTag+
                            '<span class="value">'+v.bookmarkedCount+'</span>'+
                            '</li>'+
                            '</ul>'+
                            '</div>'+
                            '<div class="content-wrap" data-board-id='+v.id+'>'+
                            // (v.content.replace(/<(?!br\s*\/?)[^>]+>/g, '')) +
                            v.content +
                            '</div>'+
                            '<div class="info-wrap" data-board-id='+v.id+'>'+
                            '<div class="date">'+ unix_timestamp(v.updatedAt/1000)+'</div>'+
                            '<ul class="tags">'+
                            (v.tags.map((tag) => {return '<li>#'+tag+'</li>'}).join(''))+
                            '</ul>'+
                            '</div>'+
                            '</li>'
                        )
                    })

                    // 이벤트 다시 바인드
                    initialBindingEvent()
                })
            })
        }
        // 위젯 영역 드래그 바인드
        const ONE_MINUTE = 60 * 1000;
        function addDashboardComponent(uiSequence, id, position) {
            localStorage.setItem('kms-'+position+'-component', JSON.stringify({uiSequence, dataId: id, position}))
            function set() {
                const component = $('.-dashboard-component[data-id=' + id + ']');
                const widget = $('.-dashboard-widget[data-ui-sequence=' + uiSequence + ']');
                widget.attr('data-id', component.attr('data-id'));
                widget.attr('data-name', component.attr('data-name'));
                widget.attr('data-type', component.attr('data-type'));
                widget.attr('data-value', component.attr('data-value'));

                setWidgetContent(widget);

                component.remove();
            }

            set()
        }
        function setWidgetContent(widget) {
            if (widget.attr('data-id')) {
                return receiveHtml(contextPath + '/counsel/kms/component/', {
                    type: widget.attr('data-type'), value: widget.attr('data-value')
                }, function () {
                }, true).done(function (html) {
                    const mixedNodes = $.parseHTML(html, null, true);

                    const uiList = [];
                    const scripts = [];

                    (function () {
                        for (let i = 0; i < mixedNodes.length; i++) {
                            const node = $(mixedNodes[i]);
                            if (node.is('script')) scripts.push(node);
                            else uiList.push(node);
                        }
                        return scripts;
                    })();

                    widget.empty()
                        .append($('<i/>', {
                            class: 'close icon', click: function () {
                                removeDashboardComponent(widget.attr('data-id'));
                            }
                        }))
                        .append(uiList);

                    scripts.map(function (script) {
                        eval('(function() { return function(component, uiList) {' + script.text() + '} })()').apply(window, [widget, uiList]);
                    });
                });
            } else {
                widget.empty().append('<h2 class="ui center aligned icon header"><i class="circular plus icon"></i></h2>');
            }
        }
        function removeDashboardComponent(id) {
            const widget = $('.-dashboard-widget[data-id=' + id + ']');
            if (!widget.length) return;

            widget.empty().append('<h2 class="ui center aligned icon header"><i class="circular plus icon"></i></h2>');
            $('.bottom-menus').append(
                $('<li/>', {
                    class: '-dashboard-component',
                    'data-id': widget.attr('data-id'),
                    'data-name': widget.attr('data-name'),
                    'data-type': widget.attr('data-type'),
                    'data-value': widget.attr('data-value')
                }).draggable({
                    helper: "clone",
                    tolerance: 'pointer',
                    revert: true,
                    iframeFix: true
                })
                    .append($('<text/>', {text: widget.attr('data-name')}))
            );
            widget.removeAttr('data-id');
            widget.removeAttr('data-name');
            widget.removeAttr('data-type');
            widget.removeAttr('data-value');
        }

        // filter click
        $('.filter-list li').on('click', (el) => {
            $(el.currentTarget).siblings().removeClass('active')
            $(el.currentTarget).addClass('active')
            $('#search_sorted').val(el.currentTarget.dataset.paramValue)
            getKmsList()
        })
        // category click
        $('.menus li').on('click', (el) => {
            $(el.currentTarget).siblings().removeClass('active')
            $(el.currentTarget).addClass('active')
            $('#search_category').val(el.currentTarget.dataset.key)
            if(el.currentTarget.dataset.key === 'all') $('#search_category').val('all')
            getKmsList()
        })
        // 하단 메뉴 드래그 바인드
        $('.-dashboard-component').each(function () {
            $(this).draggable({
                helper: "clone",
                tolerance: 'pointer',
                revert: true,
                iframeFix: true
            });
        });
        $('.-dashboard-widget').each(function () {
            const $this = $(this);
            $(this).droppable({
                accept: '.-dashboard-component',
                greedy: true,
                drop: function (event, ui) {
                    // if ($this.attr('data-id')) return;
                    removeDashboardComponent($this.attr('data-id'))
                    addDashboardComponent($this.attr('data-ui-sequence'), ui.draggable.attr('data-id'), $this.attr('data-position'));
                }
            });

            setWidgetContent($this);
        });


        // droppable 사용을 위해 slick에 drag 이벤트 제거
        $(function() {
            $('*[draggable!=true]','.slick-track').unbind('dragstart');
            $( ".draggable-element" ).draggable();
        });

        // kms 리스트 검색
        $('.search-box .input input').keydown((e) => {
            if(e.which !== 13) return
            getKmsList()
        })

        $(".search-box .search.icon").on('click', () => {
            const keyword = $('search_keyword').val()
            getKmsList(keyword)
        })

        $(document).ready(() => {
            initialBindingEvent()
            getKmsList()
        })

        // 상세페이지 닫기
        $('#detail-view .close-btn').on('click', () => {
            $('#assist-custom-sidebar #detail-view').hide()

            // 상세페이지에서 좋아요/복마크 등 정보가 변경되었을 수 있기 때문에 list를 업데이트 함.
            const hasChangesValue = $('#detail-view #has-changes').val()
            const hasChanges = (hasChangesValue==='true')
            if(hasChanges) getKmsList()
        })

        // 코멘트 등록
        $(document).on('click', '.comment-wrap .add-comment-btn', function(e) {
            console.log('e.target = ', e.target)
            const id = $(e.target).attr('post-id')
            const content = $('.comment-wrap .comment-input').val()
            const url = $.addQueryString('/counsel/kms/comment?id=' + id + '&content=' +content)
            console.log('url = ', url)
            restSelf.get(url, null, null, true).done((res) => {
                console.log('res = ', res)
                const {
                    result, createCommentId
                } = res.data.data

                console.log('result = ', result)
                console.log('createCommentId = ', createCommentId)

                console.log('boolean = ', result === 'success' && createCommentId)

                if(result === 'success' && createCommentId){
                    $('.comment-wrap .comment-input').val('')
                    alert("등록이 완료 되었습니다.")
                }
            })
        });
    </script>
</tags:scripts>