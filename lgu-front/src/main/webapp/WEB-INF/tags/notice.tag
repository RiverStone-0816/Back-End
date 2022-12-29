<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>

<%@ attribute name="noticeList" required="true" type="java.util.List" %>
<%@ attribute name="noticeType" required="true" type="java.lang.String" %>

<div class="ui modal small inverted" id="main-notice">
    <i class="close icon"></i>
    <div class="header">공지사항</div>
    <div class="scrolling content rows">
        <c:forEach var="notice" items="${noticeList}" varStatus="status">
            <div class="ui grid main-notice" data-status="${status.index + 1}"
                 data-id="${notice.id}"
                 style="display: ${status.first ? 'block' : 'none'}; position: relative; margin-top: -1rem !important;">
                <div class="row">
                    <div class="three wide column">
                        <label class="control-label">제목</label>
                    </div>
                    <div class="thirteen wide column">
                        <div class="board-con-inner"
                             style="white-space: pre-wrap;">${g.htmlQuote(notice.title)}</div>
                    </div>
                </div>
                <div class="row">
                    <div class="three wide column">
                        <label class="control-label">내용</label>
                    </div>
                    <div class="thirteen wide column">
                        <c:choose>
                            <c:when test="${notice.contentType == 'T'}">
                                <div class="board-con-inner"
                                     style="white-space: pre-wrap;">${g.htmlQuote(notice.content)}</div>
                            </c:when>
                            <c:when test="${notice.contentType == 'H'}">
                                ${g.htmlQuote(notice.content)}
                            </c:when>
                            <c:when test="${notice.contentType == 'L'}">
                                <a href="${notice.content}" target="_blank">${g.htmlQuote(notice.content)}</a>
                            </c:when>
                            <c:otherwise>
                                <div class="board-con-inner"
                                     style="white-space: pre-wrap;">${g.htmlQuote(notice.content)}</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <c:if test="${notice.mainBoardFiles != null && notice.mainBoardFiles.size() > 0}">
                    <div class="row">
                        <div class="three wide column">
                            <label class="control-label">첨부파일</label>
                        </div>
                        <div class="thirteen wide column">
                            <div class="ui list filelist">
                                <c:forEach var="e" items="${notice.mainBoardFiles}">
                                    <div class="item">
                                        <i class="file alternate outline icon"></i>
                                        <div class="content">
                                            <a href="${apiServerUrl}/api/main-board-notice/${noticeType}/${e.fileId}/${e.mainBoardId}/specific-file-resource?token=${accessToken}" target="_blank">
                                                    ${g.htmlQuote(e.originalName)}
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <div class="actions">
        <div class="ui row pull-left">
            <div class="ui checkbox">
                <input type="checkbox" name="" tabindex="0" class="hidden" id="main-notice-checkbox">
                <label>오늘 하루 다시보지 않기</label>
            </div>
        </div>
        <div class="ui row buttons pull-right">
            <button type="button" class="ui blue button" onclick="noticeMove(-1, 'main-notice')"><i class="angle left icon"></i></button>
            <button type="button" class="ui blue button" onclick="noticeMove(1,'main-notice')"><i class="angle right icon"></i></button>
        </div>
        <div class="ui row center" style="text-align: center;">
            <label> <span class="current-page">1</span> / ${noticeList.size()}</label>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        const mainNoticeNext = $('#main-notice');
        let storageKey = "mainNotice_" + '${noticeType}'

        $('#main-notice-checkbox').change(function () {
            if ($(this).is(":checked"))
                handleStorage.setStorage(storageKey, 1);
            else
                handleStorage.removeStorage(storageKey);
        })

        <c:if test="${noticeList != null && noticeList.size() > 0}">
        if (handleStorage.getStorage(storageKey))
            mainNoticeNext.dragModalShow();
        </c:if>

        function noticeMove(add, target) {
            const max = ${noticeList.size()};

            const current = $('.main-notice').filter(function () {
                return $(this).css('display') === 'block';
            });

            const currentNumber = current.attr('data-status');

            if ( (currentNumber === '1' && add < 0 ) || (currentNumber === '' + max + '' && add > 0 ))
                return;

            current.css('display', 'none');

            if ( add > 0 ){
                current.next().css('display', 'block');
            }
            else {
                current.prev().css('display', 'block');
            }

            $('#main-notice').find('.current-page').text(parseInt(currentNumber) + add);
        }
    </script>
</tags:scripts>