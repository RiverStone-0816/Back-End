<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mobileTags" tagdir="/WEB-INF/tags/mobile" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<form id="profile-form" class="inner-box bb-unset" style="margin-top: 10px;">
    <h1 class="sub-title no-drag">프로필 수정</h1>

    <div class="ui middle aligned grid search">
        <div class="row">
            <div class="four wide column">프로필 사진</div>
            <div class="twelve wide column">
                <div class="profile-picture-wrap">
                    <c:choose>
                        <c:when test="${user.profilePhoto != null && user.profilePhoto != ''}">
                            <img class="profile-picture -picture" data-id="${user.id}" src="${apiServerUrl}/api/memo/profile-resource?path=${g.urlEncode(user.profilePhoto)}&token=${accessToken}"
                                 style="border-radius: 50%; width: 100%; height: 100%; overflow: hidden;"/>
                        </c:when>
                        <c:otherwise>
                            <img class="profile-picture" src="<c:url value="/resources/ipcc-messenger/images/person.png"/>" style="border-radius: 50%; width: 100%; height: 100%; overflow: hidden;"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="profile-btn-wrap">
                    <button type="button" class="ui darkblue button mini" onclick="popupProfilePictureSubmitModal()">변경</button>
                    <button type="button" class="ui black button basic mini" onclick="showDefaultProfilePicture()">삭제</button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="four wide column">ID</div>
            <div class="twelve wide column">${g.htmlQuote(user.id)}</div>
        </div>
        <div class="row">
            <div class="four wide column">내선</div>
            <div class="twelve wide column">${g.htmlQuote(user.extension)}</div>
        </div>
    </div>
</form>

<tags:scripts>
    <script>
        function popupProfilePictureSubmitModal() {
            if (window.isElectron) {
                window.open(contextPath + "/ipcc-messenger/modal-profile-picture", "_profile");
            } else {
                window.open(contextPath + "/ipcc-messenger/modal-profile-picture", "_blank", "width=480, height=220");
            }
        }

        function showDefaultProfilePicture() {
            restSelf.delete('/api/memo/delete-specific-file').done(function () {
                $('.-picture[data-id="${user.id}"]').attr('src', "<c:url value="/resources/ipcc-messenger/images/person.png"/>");
            });
        }
    </script>
</tags:scripts>
