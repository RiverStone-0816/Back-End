<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<%--@elvariable id="metaTypes" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.CompanyTreeLevelNameResponse>"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/user/organization/"/>

        <div class="sub-content ui container fluid">
            <div class="ui grid complex">
                <div class="eight wide column">
                    <div class="panel">
                        <div class="panel-heading">
                            <div class="ui action input fluid">
                                <button onclick="addChild($('#organization-pan'))" class="ui compact button mr15">대분류추가</button>
                                <input type="text" placeholder="검색">
                                <button type="button" class="ui icon button" onclick="search($(this).prev().val())">
                                    <i class="search icon"></i>
                                </button>
                            </div>
                        </div>
                        <div class="panel-body">
                            <jsp:include page="/admin/user/organization/editable-pan"/>

                            <form id="organization-register-form" class="item -json-submit" style="display: none;"
                                  action="${pageContext.request.contextPath}/api/organization/" data-method="post"
                                  data-done="doneRegisterElement">
                                <input type="hidden" name="parentGroupCode">
                                <input type="hidden" name="groupLevel">

                                <i class="folder icon"></i>
                                <div class="content">
                                    <div class="header">
                                        <div class="ui mini action input">
                                            <input type="text" placeholder="명칭입력" name="groupName">
                                            <button type="submit" class="ui button">확인</button>
                                        </div>
                                        <button type="button" onclick="event.stopPropagation(); detachAddChild()();" class="circular basic ui icon mini very compact button">
                                            <i class="icon close"></i>
                                        </button>
                                    </div>
                                </div>
                            </form>

                            <form id="organization-update-form" class="item -json-submit" style="display: none;"
                                  action="${pageContext.request.contextPath}/api/organization/" data-method="put"
                                  data-done="doneUpdateElement">
                                <div class="content">
                                    <div class="header">
                                        <div class="ui mini action input">
                                            <input type="text" placeholder="명칭입력" name="groupName">
                                            <button type="submit" class="ui button">확인</button>
                                        </div>
                                        <button type="button" onclick="event.stopPropagation(); detachInput();" class="circular basic ui icon mini very compact button">
                                            <i class="icon close"></i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="eight wide column">
                    <div class="panel">
                        <div class="panel-heading">
                            <div class="pull-left">
                                <h3 class="panel-title">조직정보</h3>
                            </div>
                            <div class="pull-right">
                                <button class="ui basic button" onclick="popupMetaTypeSet()">META유형 수정</button>
                            </div>
                        </div>
                        <div class="panel-body" id="organization-element-summary"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const registerForm = $('#organization-register-form');
            const updateForm = $('#organization-update-form');

            function popupMetaTypeSet() {
                popupReceivedHtml('/admin/user/organization/meta-type/modal', 'modal-meta-type', 'tiny')
            }

            function showOrganizationSummary(element, seq) {
                replaceReceivedHtml('/admin/user/organization/' + seq + '/summary', '#organization-element-summary').done(function () {
                    $('#organization-pan').find('.select').removeClass('select');
                    $(element).addClass('select');
                });
            }

            function refreshPan() {
                $('#organization-pan').asJsonData().done(function (data) {
                    replaceReceivedHtml($.addQueryString('/admin/user/organization/editable-pan', data), '#organization-pan');
                });
            }

            function deleteEntity(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/organization/' + seq).done(function () {
                        refreshPan();
                    });
                });
            }

            function search(keyword) {
                $('#organization-pan').find('[name=keyword]').val(keyword);
                refreshPan();
            }

            function addChild(element, parentGroupCode, groupLevel) {
                registerForm.detach().appendTo($(element));
                registerForm.find('[name=parentGroupCode]').val(parentGroupCode || '');
                registerForm.find('[name=groupLevel]').val(groupLevel || 1);
                registerForm.find('[name=groupName]').val('');
                registerForm.show();
            }

            function doneRegisterElement() {
                registerForm.hide().detach().insertAfter($('#organization-pan'));
                refreshPan();
            }

            function showInput(headerElement, seq) {
                const header = $(headerElement);
                updateForm.show().detach().insertAfter(header);
                updateForm.find('[name=groupName]').val('');
                updateForm.attr('action', contextPath + '/api/organization/' + seq);
                header.hide();
            }

            function detachInput() {
                const header = updateForm.prev('.header');
                header.show();
                updateForm.hide().detach().insertAfter($('#organization-pan'));
            }

            function detachAddChild() {
                const header = registerForm.prev('.header');
                header.show();
                registerForm.hide().detach().insertAfter($('#organization-pan'));
            }

            function doneUpdateElement() {
                updateForm.hide().detach().insertAfter($('#organization-pan'));
                refreshPan();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
