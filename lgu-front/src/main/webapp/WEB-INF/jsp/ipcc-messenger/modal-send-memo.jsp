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

<%--@elvariable id="form" type="kr.co.eicn.ippbx.front.model.form.LoginForm"--%>

<mobileTags:layout>
    <form:form commandName="form" cssClass="ui modal window tiny -json-submit" data-method="post"
               action="${pageContext.request.contextPath}/api/memo/"
               data-before="prepareSubmit" data-done="doneSubmit">

        <i class="close icon"></i>
        <div class="header">
            <span class="material-icons"> create </span> 쪽지 보내기
        </div>
        <div class="content dp-flex ff-column">
                <%--<div class="inner-box flex">
                    <div class="ui fluid input small flex-auto">
                        <input type="text" name="receiveUserIds" readonly>
                    </div>
                    <div class="pl10">
                        <button class="ui icon brand button" id="user-view-btn">
                            <i class="angle down icon"></i>
                        </button>
                    </div>
                </div>--%>
            <div class="ui icon fluid input small -filter-text">
                <input type="text" id="messenger-filter-text" placeholder="검색어 입력">
                <i class="search link icon"></i>
            </div>
            <div class="inner-box scroll modal-organization-wrap" style="display: block; overflow-x: hidden;">
                <h1 class="sub-title no-drag">조직도</h1>
                <div class="ui list organization-container" id="organization"></div>
            </div>
            <div class="inner-box bb-unset flex-auto">
                <div class="ui form pull-height">
                    <div class="field pull-height">
                        <form:textarea path="content" class="pull-height"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <div class="pull-left -write-me">
                <form:checkbox path="isWriteToMe"/>
                <label>내게 쓰기</label>
            </div>
            <button type="button" class="ui black button basic tiny close" onclick="closePopup()">닫기</button>
            <button type="submit" class="ui darkblue button tiny">보내기</button>
        </div>
    </form:form>

    <tags:scripts>
        <script>
            let lastActiveElement = null;

            function closePopup() {
                (self.opener = self).close();
            }

            if (window.isElectron) {
                function doneSubmit(form, response) {
                    $('form').asJsonData().done(function (data) {
                        prepareSubmit(data);

                        data.receiveUserIds.map(function (receiveUserId) {
                            ipcRenderer.send("sendMemo", {"receiveUserId" : receiveUserId, "data" : response.data});
                        });

                        alert('전달되었습니다.', function () {
                            closePopup();
                        });
                    });
                }
            } else {
                function doneSubmit(form, response) {
                    $('form').asJsonData().done(function (data) {
                        prepareSubmit(data);

                        console.log(response);

                        data.receiveUserIds.map(function (receiveUserId) {
                            messenger.communicator.sendMemo(receiveUserId, response.data);
                        });

                        $(self.opener.document).find('#tab5 button[type=submit]').click();
                        alert('전달되었습니다.', function () {
                            closePopup();
                        });
                    });
                }
            }

            $('[name=isWriteToMe]').change(function () {
                if ($(this).is(':checked')) {
                    $('.modal-organization-wrap').hide();
                } else {
                    $('.modal-organization-wrap').show();
                }
            }).change();

            function prepareSubmit(data) {
                data.receiveUserIds = [];

                <c:forEach items="${members}" var="item">
                data.receiveUserIds.push("${item}");
                </c:forEach>

                data.receiveUserIds.push();
                if (data.isWriteToMe) {
                    data.receiveUserIds.push('${g.escapeQuote(user.id)}');
                } else {
                    $('.-messenger-folder.active').each(function () {
                        $(this).find('.-messenger-user').each(function () {
                            const id = $(this).attr('data-id');
                            if (data.receiveUserIds.indexOf(id) >= 0)
                                return;
                            data.receiveUserIds.push(id);
                        });
                    });

                    $('.-messenger-user.active').each(function () {
                        if (data.receiveUserIds.indexOf($(this).attr('data-id')) >= 0)
                            return;

                        data.receiveUserIds.push($(this).attr('data-id'));
                    });

                    if (!data.receiveUserIds || (!data.receiveUserIds.length && '${members}'.length === 0)) {
                        alert('대상자가 없습니다.');
                        throw '대상자가 없습니다.';
                    }
                    if (!data.content || !data.content.length) {
                        alert('전송할 메시지가 없습니다.');
                        throw '전송할 메시지가 없습니다.';
                    }
                }
            }

            $(document).ready(function () {
                if ('${members}'.length > 0) {
                    $('.modal-organization-wrap').hide();
                    $('.-write-me').hide();
                }

                function attachFolder(container, e, level) {
                    level = level || 1;

                    const item = $('<div/>', {class: 'item -messenger-folder'})
                        .append($('<span/>', {
                            class: 'material-icons', text: 'filter_' + (level > 9 ? '9_plus' : level)
                        }))
                        .appendTo(container);

                    const content = $('<div/>', {class: 'content'})
                        .append(
                            $('<div/>', {class: 'header level -messenger-folder-header', text: e.groupName})
                                .click(function (event) {
                                    const item = $(this).closest('.item');
                                    const users = $('.-messenger-user');
                                    const folders = $('.-messenger-folder');

                                    if (event.ctrlKey) {
                                        item.toggleClass('active');
                                    } else {
                                        users.removeClass('active');
                                        folders.removeClass('active');
                                        item.addClass('active');
                                    }
                                })
                        )
                        .appendTo(item);

                    const childrenContainer = $('<div/>', {class: 'list'}).css({'display': groupTree.contains(e.groupTreeName) ? '' : 'none'})
                        .appendTo(content);

                    if (e.personList)
                        e.personList.map(function (e) {
                            attachPerson(childrenContainer, e);
                        });

                    if (e.organizationMetaChatt)
                        e.organizationMetaChatt.map(function (e) {
                            attachFolder(childrenContainer, e, level + 1);
                        });
                }

                function attachPerson(container, e) {
                    if(e.id === '${g.escapeQuote(user.id)}')
                        return;

                    let text = '';
                    if (e.extension) text += ' / 내선:' + e.extension;
                    if (e.hpNumber && '${g.user.isEmail}' === 'Y') text += ' / 휴대폰:' + e.hpNumber;
                    if (e.emailInfo) text += ' / 이메일:' + e.emailInfo;
                    if (text.indexOf(' / ') === 0) text = text.substr(3);

                    $('<div/>', {class: 'item user -messenger-user', 'data-id': e.id, 'data-name': e.idName})
                        .append(
                            $('<div/>', {class: 'picture-container'})
                                .append($('<img/>', {
                                    class: 'picture -picture -status-icon ' + (e.isLoginChatt === 'L' ? 'active' : ''),
                                    'data-id': e.id,
                                    src: e.profilePhoto
                                        ? apiServerUrl + '/api/memo/profile-resource?path=' + encodeURIComponent(e.profilePhoto) + '&token=' + encodeURIComponent(accessToken)
                                        : defaultProfilePicture
                                }))
                        )
                        .append(
                            $('<div/>', {class: 'content'})
                                .append(
                                    $('<div/>', {class: 'header'})
                                        .append($('<span/>', {class: 'name', text: e.idName}))
                                        .append($('<span/>', {class: 'detail', text: text}))
                                )
                        )
                        .appendTo(container)
                        .click(function (event) {
                            if (event.ctrlKey) {
                                $(this).toggleClass('active');

                                if ($(this).hasClass('active')) {
                                    lastActiveElement = this;
                                }
                                return;
                            }

                            const users = $('.-messenger-user');
                            const folders = $('.-messenger-folder');

                            if (event.shiftKey && lastActiveElement) {
                                users.removeClass('active');
                                folders.removeClass('active');
                                $(lastActiveElement).addClass('active');

                                const _this = this;

                                if (_this === lastActiveElement)
                                    return;

                                let meetActiveElement = false;
                                let meetMe = false;
                                users.each(function () {
                                    if (this === lastActiveElement) {
                                        $(this).addClass('active');
                                        meetActiveElement = true;
                                    } else if (this === _this) {
                                        $(this).addClass('active');
                                        meetMe = true;
                                    } else {
                                        if (meetActiveElement ^ meetMe)
                                            $(this).addClass('active');
                                    }
                                });
                            } else {
                                users.removeClass('active');
                                folders.removeClass('active');
                                $(this).addClass('active');
                                lastActiveElement = this;
                            }
                        });
                }

                restSelf.get('/api/chatt/', null, null, true).done(function (response) {
                    const organization = $('#organization').empty();
                    response.data.map(function (e) {
                        attachFolder(organization, e);
                    });

                    $('.material-icons').click(function(){
                        const list = $(this).next(".content").find('.list');
                        const folderHeader = $(this).text().contains('1') ? $(this).find('.list') : $(this).next(".content").find('.-messenger-folder-header');
                        list.slideToggle(0, '', function () {
                            if (list.css('display') === 'none')
                                folderHeader.css({'color': '#dbdbdb'})
                            else
                                folderHeader.css({'color': ''})
                        });
                    });
                });

                $('#messenger-filter-text').keyup(function () {
                    filterItem($('#messenger-filter-text'));
                });

                function filterItem($this) {
                    const text = ($this.val() || '').trim();

                    $('.-messenger-folder').show().filter(function () {
                        return $(this).parent()[0] ===  $('.organization-container')[0];
                    }).each(function () {
                        function renderAndReturnContainsText(element) {
                            let containsText = false;

                            if (element.hasClass('-messenger-folder')) {
                                containsText = element.children('.content').children('.header').text().indexOf(text) >= 0;

                                element.children('.content').children('.list').children().map(function () {
                                    containsText |= renderAndReturnContainsText($(this));
                                });
                            } else {
                                containsText = element.text().indexOf(text) >= 0;
                            }

                            if (containsText) {
                                element.show();
                                return true;
                            } else {
                                element.hide();
                                return false;
                            }
                        }

                        renderAndReturnContainsText($(this));
                    });
                }
            });
        </script>
    </tags:scripts>
</mobileTags:layout>
