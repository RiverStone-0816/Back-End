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

<div id="component-hits-tag-rank-wrap">
  <div class="title-wrap">
    <h3 class="title">키워드랭킹</h3>
    <div class="filter-wrap">
      <select name="" id="hits-tag-rank-select">
        <option value="month" selected>이번달</option>
        <option value="week">이번주</option>
        <option value="day">오늘</option>
      </select>
    </div>
  </div>

  <div class="content-list-wrap">
    <ul class="content-list">
      <c:forEach var="item" items="${hitsTagRankList}" varStatus="status">
        <li class="hits-tag-rank-item">
          <div class="hits-tag-rank-number">${status.count}</div>
          <div class="hits-tag-rank-title">${item.tag}</div>
          <div class="hits-tag-rank-icon"><i class="eye icon" /> ${item.hits}</div>
        </li>
      </c:forEach>
    </ul>
  </div>
</div>

<script>
  // kms 리스트 가져오기
  function getKmsList(){
    $('.content .top').asJsonData().done((data) => {
      const url = $.addQueryString('/counsel/kms/list', data)
      restSelf.get(url, null, null, true).done((res) => {
        const {
          kmsList, categoryList
        } = res.data.data

        $('#assist-custom-sidebar .board-list .board-item').remove()
        kmsList.forEach((v) => {
          let categoryName = categoryList.filter((category) => {return category.id === v.category})[0];
          let visibleTag = v.visible === true ? '<i class="eye icon" style="color: #bf1455"></i>' : '<i class="icon eye"></i>'
          let likeTag = v.liked === true ? '<i class="icon thumbs up active"></i>' : '<i class="icon thumbs up outline" data-id='+v.id+' data-liked='+v.liked+'></i>'
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
                  '<li>'+
                  visibleTag+
                  '<span class="value">'+v.hits+'</span>'+
                  '</li>'+
                  '<li>'+
                  likeTag+
                  '<span class="value">'+v.likes+'</span>'+
                  '</li>'+
                  '<li>'+
                  bookmarkTag+
                  '<span class="value">'+v.bookmarkedCount+'</span>'+
                  '</li>'+
                  '</ul>'+
                  '</div>'+
                  '<div class="content-wrap" data-board-id='+v.id+'>'+
                  v.content+
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

  // 새로고침
  $('#hits-tag-rank-select').on('change', (e) => {
    const selected = $(e.currentTarget).val()
    let startDate = ''
    let endDate = ''
    const url = '/counsel/kms/hits-tag-rank'

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
        hitsTagRankList
      } = res.data.data

      $('#component-hits-tag-rank-wrap .content-list-wrap .content-list li').remove()

      hitsTagRankList.forEach((value, index) => {
        $('#component-hits-tag-rank-wrap .content-list-wrap .content-list').append(
                '<li class="hits-tag-rank-item" data-board-id='+value.id+'>'+
                '<div class="hits-tag-rank-number">'+(index+1)+'</div>'+
                '<div class="hits-tag-rank-title">'+value.tag+'</div>'+
                '<div class="hits-tag-rank-icon"><i class="eye icon" /> '+value.hits+'</div>'+
                '</li>'
        )
      })
    })
  })

  $('#component-hits-tag-rank-wrap .content-list-wrap .content-list li .hits-tag-rank-title').on('click', (e) => {
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
</script>