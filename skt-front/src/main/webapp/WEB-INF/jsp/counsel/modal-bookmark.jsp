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

<div class="ui modal">
    <i class="close icon"></i>
    <div class="header">즐겨찾기</div>
    <div class="content">
        <table class="ui celled table unstackable">
            <tbody id="link-form-body">
            <c:forEach var="i" items="${[0,1,2,3,4,5,6,7,8]}">
                <tr>
                    <td>
                        <i class="remove icon -remove-link" style="cursor: pointer;"></i>
                    </td>
                    <td>
                        <div class="ui form">
                            <input type="text" placeholder="타이틀" name="form.${i}.name" value="${list[i] != null ? g.htmlQuote(list[i].name) : ''}"/>
                        </div>
                    </td>
                    <td>
                        <div class="ui form">
                            <input type="text" placeholder="URL" name="form.${i}.reference" value="${list[i] != null ? g.htmlQuote(list[i].reference) : ''}"/>
                        </div>
                    </td>
                    <td class="one wide column">
                        <button type="button" class="ui button sharp navy" onclick="window.open($(this).closest('td').prev().find('input').val(), '_blank')">바로가기</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="actions">
        <button type="button" class="ui right floated button sharp brand">확인</button>
        <button type="submit" class="ui right floated button sharp modal-close">취소</button>
    </div>
</div>

<script>
    let iLink = 9;

    modal.on('click', '.-remove-link', function (event) {
        const index = iLink++;

        const _this = $(event.target);
        _this.closest('tr').remove();
        $('#link-form-body').append('<tr>' +
            '<td>' +
            '<i class="remove icon -remove-link" style="cursor: pointer;"></i>' +
            '</td>' +
            '<td>' +
            '<div class="ui form">' +
            '<input type="text" placeholder="타이틀" name="form.' + index + '.name"/>' +
            '</div>' +
            '</td>' +
            '<td>' +
            '<div class="ui form">' +
            '<input type="text" placeholder="URL" name="form.' + index + '.reference"/>' +
            '</div>' +
            '</td>' +
            '<td class="one wide column">' +
            '<button type="button" class="ui button sharp navy" onclick="window.open($(this).closest(\'td\').prev().find(\'input\').val(), \'_blank\')">바로가기</button>' +
            '</td>' +
            '</tr>');
    });

    // todo: 즐겨찾기 링크. 한번에 다 저장할수 있는 api 추가 필요
    modal.find('[type=submit]').click(function () {
        modal.asJsonData().done(function (data) {
            const indices = [0, 1, 2, 3, 4, 5, 6, 7, 8];

            function process(i) {
                indices.splice(0, 1);

                const form = data.form[i];
                if (form && form.name && form.reference) {
                    restSelf[!form.seq ? 'post' : 'put'].apply(null, ['/api/person-link/' + (form.seq || ''), {name: form.name, reference: form.reference}]).done(function () {
                        if (indices.length > 0) process(indices[0]);
                    });
                } else {
                    if (indices.length > 0) process(indices[0]);
                }
            }

            process(indices[0]);
            modal.modalHide();
        });
    });
</script>
