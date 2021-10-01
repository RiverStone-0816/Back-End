<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>

<aside class="side-bar manage-side">
    <button class="nav-bar"><i class="material-icons arrow"> keyboard_arrow_left </i></button>
    <div class="sidebar-menu-container">
        <ul class="sidebar-menu">
            <li><a href="<c:url value="/admin/dashboard/"/>" class="tab-indicator" title="대시보드"><i class="material-icons menu-icon"> computer </i><span>대시보드</span></a></li>
            <c:forEach var="e" items="${menu.menus}">
                <c:if test="${e.viewYn == 'Y'}">
                    <c:choose>
                        <c:when test="${e.actionType == 'MENU'}">
                            <li>
                                <a href="#" title="${g.htmlQuote(e.menuName)}">
                                    <i class="material-icons"> ${g.htmlQuote(e.icon)} </i><span>${g.htmlQuote(e.menuName)}</span>
                                    <i class="material-icons arrow"> keyboard_arrow_down </i>
                                </a>
                                <ul class="treeview-menu">
                                    <c:forEach var="e2" items="${e.children}">
                                        <c:if test="${e2.viewYn == 'Y'}">
                                            <c:choose>
                                                <c:when test="${e2.actionType == 'MENU'}">
                                                    <c:set var="presentLink" value="${false}"/>
                                                    <c:forEach var="e3" items="${e2.children}">
                                                        <c:if test="${e3.viewYn == 'Y' && !presentLink}">
                                                            <c:set var="presentLink" value="${true}"/>
                                                            <li>
                                                                <a href="<c:url value="${e3.menuActionExeId}"/>" class="tab-indicator" title="${g.htmlQuote(e3.menuName)}">
                                                                        ${g.htmlQuote(e2.menuName)}
                                                                </a>
                                                            </li>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:when test="${e2.actionType == 'PAGE'}">
                                                    <li>
                                                        <a href="<c:url value="${e2.menuActionExeId}"/>" class="tab-indicator" title="${g.htmlQuote(e2.menuName)}">${g.htmlQuote(e2.menuName)}</a>
                                                    </li>
                                                </c:when>
                                            </c:choose>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:when>
                        <c:when test="${e.actionType == 'PAGE'}">
                            <li>
                                <a href="<c:url value="${e.menuActionExeId}"/>" title="${g.htmlQuote(e.menuName)}" class="tab-indicator">
                                    <i class="material-icons"> ${g.htmlQuote(e.icon)} </i><span>${g.htmlQuote(e.menuName)}</span>
                                </a>
                            </li>
                        </c:when>
                    </c:choose>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</aside>

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:if test="${hasExtension}">
    <aside class="side-bar consulting-side">
        <div class="side-bar-tab-container">
            <ul class="side-bar-tab">
                <li class="active" data-tab="organization-area"><i class="list ul icon"></i>조직도</li>
                <li data-tab="room-list-area"><i class="comments icon unread"></i>채팅방</li>
            </ul>
        </div>
        <button class="nav-bar"><i class="material-icons arrow">keyboard_arrow_left</i></button>
        <div class="side-bar-content active" id="organization-area">
            <div class="organization-area-inner">
                <div class="sidebar-menu-container">
                    <div class="consulting-accordion favorite active">
                        <div class="consulting-accordion-label">
                            <div>
                                즐겨찾기 <button type="button" class="ui basic white very mini compact button ml10" onclick="messenger.popupBookmarkModal();">편집</button>
                            </div>
                            <div>
                                <i class="material-icons arrow">keyboard_arrow_down</i>
                            </div>
                        </div>
                        <div class="consulting-accordion-content">
                            <ul class="treeview-menu treeview-on consulting-accordion-content favorite overflow-overlay">
                                <%--<li class="empty">등록된 즐겨찾기가 없습니다.</li>--%>
                                <li>
                                    <div>
                                        <i class="user outline icon online"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <i class="user outline icon"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <i class="user outline icon"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <i class="user outline icon"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <i class="user outline icon"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <i class="user outline icon online"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        <i class="user outline icon"></i>
                                        <span class="user">상담사1[0990]</span>
                                        <button type="button" class="ui icon button mini compact -redirect-to" data-extension="0990" title="전화돌려주기">
                                            <i class="share icon"></i>
                                        </button>
                                    </div>
                                    <div>
                                        <span class="ui mini label -consultant-status-with-color teal" data-peer="78390990">대기</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="consulting-accordion organization overflow-hidden dp-flex flex-flow-column active">
                        <div class="consulting-accordion-label">
                            <div>
                                조직도 <button type="button" class="ui basic white very mini compact button ml10">선택대화</button>
                            </div>
                            <div>
                                <i class="material-icons arrow">keyboard_arrow_down</i>
                            </div>
                        </div>
                        <div class="consulting-accordion-content overflow-overlay flex-100">
                            <ul class="" id="counsel-nav"></ul>
                        </div>
                    </div>
                </div>
                <div class="box-container">
                    <div class="box-container-inner">
                        <div class="box">
                            <div class="sidebar-label">
                                <div>
                                    외부링크<button class="ui basic white very mini compact button ml10" onclick="popupBookmarkConfiguration()">편집</button>
                                </div>
                                <div>
                                    <i class="material-icons arrow">keyboard_arrow_right</i>
                                </div>
                            </div>
                            <div class="box-content">
                                <div class="ui list" id="outer-link-list"></div>
                            </div>
                        </div>
                        <div class="box call-transform">
                            <div class="sidebar-label">
                                <div>
                                    대표/헌트번호돌려주기
                                </div>
                                <div>
                                    <i class="material-icons arrow">keyboard_arrow_right</i>
                                </div>
                            </div>
                            <div class="box-content">
                                <jsp:include page="/counsel/call-transfer"/>
                            </div>
                        </div>
                        <div class="box">
                            <div class="sidebar-label">
                                <div>
                                    처리현황(30초마다 갱신)
                                </div>
                                <div>
                                    <i class="material-icons arrow">keyboard_arrow_right</i>
                                </div>
                            </div>
                            <div class="box-content">
                                <jsp:include page="/counsel/current-status"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="side-bar-content overflow-overlay" id="room-list-area">
            <div class="room-list-area-inner">
                <ul class="side-room-list-ul">
                    <li class="list">
                        <div class="header">
                            <div class="room-name"><text>제목입니다.</text></div>
                            <div class="last-message-time">09-27 12:39</div>
                        </div>
                        <div class="content">
                            <div class="preview">내용입니다.</div>
                            <div class="unread">
                                <span class="number">9</span>
                            </div>
                        </div>
                    </li>
                    <li class="list">
                        <div class="header">
                            <div class="room-name"><text>제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목</text></div>
                            <div class="last-message-time">09-27 12:39</div>
                        </div>
                        <div class="content">
                            <div class="preview">내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용</div>
                            <div class="unread">
                                <span class="number">9</span>
                            </div>
                        </div>
                    </li>
                    <li class="list active">
                        <div class="header">
                            <div class="room-name"><text>제목입니다.</text></div>
                            <div class="last-message-time">09-27 12:39</div>
                        </div>
                        <div class="content">
                            <div class="preview">내용입니다.</div>
                            <div class="unread">
                                <span class="number">9</span>
                            </div>
                        </div>
                    </li>
                    <li class="list">
                        <div class="header">
                            <div class="room-name"><text>제목입니다.</text></div>
                            <div class="last-message-time">09-27 12:39</div>
                        </div>
                        <div class="content">
                            <div class="preview">내용입니다.</div>
                            <div class="unread">
                                <span class="number">9</span>
                            </div>
                        </div>
                    </li>
                    <li class="list">
                        <div class="header">
                            <div class="room-name"><text>제목입니다.</text></div>
                            <div class="last-message-time">09-27 12:39</div>
                        </div>
                        <div class="content">
                            <div class="preview">내용입니다.</div>
                            <div class="unread">
                                <span class="number">9</span>
                            </div>
                        </div>
                    </li>
                    <li class="list">
                        <div class="header">
                            <div class="room-name"><text>제목입니다.</text></div>
                            <div class="last-message-time">09-27 12:39</div>
                        </div>
                        <div class="content">
                            <div class="preview">내용입니다.</div>
                            <div class="unread">
                                <span class="number">9</span>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </aside>
</c:if>