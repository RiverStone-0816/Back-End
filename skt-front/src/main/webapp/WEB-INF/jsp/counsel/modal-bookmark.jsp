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

<div class="ui modal inverted">
    <i class="close icon"></i>
    <div class="header">즐겨찾기설정</div>
    <div class="content rows scrolling">
        <div class="ui grid form">
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">외부사이트 즐겨찾기 등록</h4>
                </div>
            </div>
            <c:forEach var="i" items="${[0,1,2]}">
                <div class="row">
                    <div class="three wide column">
                        <c:if test="${list[i] != null}">
                            <i class="remove icon -remove-link" style="cursor: pointer;" data-value="${list[i].seq}"></i>
                        </c:if>
                        <label class="control-label">URL${i+1}</label>
                    </div>
                    <div class="thirteen wide column">
                        <div class="fields remove-mb">
                            <c:if test="${list[i] != null}">
                                <input type="hidden" name="form.${i}.seq" value="${list[i].seq}"/>
                            </c:if>
                            <div class="three wide field">
                                <input type="text" placeholder="타이틀" name="form.${i}.name" value="${list[i] != null ? g.htmlQuote(list[i].name) : ''}"/>
                            </div>
                            <div class="thirteen wide field">
                                <input type="text" placeholder="URL" name="form.${i}.reference" value="${list[i] != null ? g.htmlQuote(list[i].reference) : ''}"/>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="submit" class="ui blue button">확인</button>
    </div>
</div>

<script>
    modal.find('.-remove-link').click(function () {
        const _this = $(this);
        const seq = $(this).attr('data-value');
        restSelf.delete('/api/person-link/' + seq).done(function () {
            _this.remove();

            const seqInput = modal.find('[name$=".seq"][value="' + seq + '"]');
            const index = seqInput.attr('name').split('.')[1];
            modal.find('[name^="form.' + index + '."]').val('');
            loadOuterLink();
        });
    });

    modal.find('[type=submit]').click(function () {
        modal.asJsonData().done(function (data) {
            const indices = [0, 1, 2];

            function process(i) {
                indices.splice(0, 1);

                const form = data.form[i];
                if (form && form.name && form.reference) {
                    restSelf[!form.seq ? 'post' : 'put'].apply(null, ['/api/person-link/' + (form.seq || ''), {name: form.name, reference: form.reference}]).done(function () {
                        if (indices.length > 0) process(indices[0]);
                        else loadOuterLink();
                    });
                } else {
                    if (indices.length > 0) process(indices[0]);
                    else loadOuterLink();
                }
            }

            process(indices[0]);
            modal.modalHide();
        });
    });
</script>
