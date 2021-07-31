<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="menu" type="kr.co.eicn.ippbx.front.model.CurrentUserMenu"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/record/evaluation/item/"/>
        <div class="sub-content ui container fluid unstackable">
            <form class="panel panel-search" method="get">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label>검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">평가지</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <select name="formId">
                                            <option value="">선택안함</option>
                                            <c:forEach var="e" items="${forms}">
                                                <option value="${g.htmlQuote(e.key)}" ${formId == e.key ? 'selected' : ''} >${g.htmlQuote(e.value)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">현재배점 <span class="text-primary" id="total-score">0</span> / 100</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic button" onclick="saveEvaluationItem()">저장하기</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact structured unstackable">
                        <thead>
                        <tr>
                            <th>분류</th>
                            <th class="four wide">항목명</th>
                            <th class="four wide">평가기준</th>
                            <th class="one wide">배점</th>
                            <th class="four wide">비고</th>
                        </tr>
                        </thead>
                        <tbody id="item-sheet">
                        <c:choose>
                            <c:when test="${categories != null && categories.size() > 0}">
                                <c:forEach var="category" items="${categories}" varStatus="categoryStatus">
                                    <c:choose>
                                        <c:when test="${category.items.size() > 0}">
                                            <c:forEach var="item" items="${category.items}" varStatus="itemStatus">
                                                <tr class="${itemStatus.first ? '-header-category-tr' : ''}">
                                                    <c:if test="${itemStatus.first}">
                                                        <td rowspan="${category.items.size()}">
                                                            <div class="ui action input ">
                                                                <input type="text" data-target="category" data-name="name" placeholder="분류명입력" value="${g.htmlQuote(category.name)}">
                                                                <button type="button" class="ui icon button" onclick="addCategory(this)"><i class="plus icon"></i></button>
                                                                <button type="button" class="ui icon button" onclick="removeCategory(this)"><i class="minus icon"></i></button>
                                                            </div>
                                                        </td>
                                                    </c:if>
                                                    <td>
                                                        <div class="ui action input">
                                                            <input type="text" data-target="item" data-name="name" placeholder="항목명입력" value="${g.htmlQuote(item.name)}">
                                                            <button type="button" class="ui icon button" onclick="addItem(this)"><i class="plus icon"></i></button>
                                                            <button type="button" class="ui icon button" onclick="removeItem(this)"><i class="minus icon"></i></button>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div class="ui form">
                                                            <div class="field">
                                                                <textarea data-target="item" data-name="valuationBasis" class="fluid" style="padding-top: 5px;">${g.htmlQuote(item.valuationBasis)}</textarea>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div class="ui input fluid">
                                                            <input type="text" data-target="item" data-name="maxScore" class="-input-numerical" value="${item.maxScore}">
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div class="ui form">
                                                            <div class="field">
                                                                <textarea data-target="item" data-name="remark" class="fluid">${g.htmlQuote(item.remark)}</textarea>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr class="-header-category-tr">
                                                <td rowspan="1">
                                                    <div class="ui action input ">
                                                        <input type="text" data-target="category" data-name="name" placeholder="분류명입력" value="${g.htmlQuote(category.name)}">
                                                        <button type="button" class="ui icon button" onclick="addCategory(this)"><i class="plus icon"></i></button>
                                                        <button type="button" class="ui icon button" onclick="removeCategory(this)"><i class="minus icon"></i></button>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui action input ">
                                                        <input type="text" data-target="item" data-name="name" placeholder="항목명입력">
                                                        <button type="button" class="ui icon button" onclick="addItem(this)"><i class="plus icon"></i></button>
                                                        <button type="button" class="ui icon button" onclick="removeItem(this)"><i class="minus icon"></i></button>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui form">
                                                        <div class="field">
                                                            <textarea data-target="item" data-name="valuationBasis" class="fluid"></textarea>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui input fluid">
                                                        <input type="text" data-target="item" data-name="maxScore" class="-input-numerical" value="1">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="ui form">
                                                        <div class="field">
                                                            <textarea data-target="item" data-name="remark" class="fluid"></textarea>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr class="-header-category-tr">
                                    <td rowspan="1">
                                        <div class="ui action input ">
                                            <input type="text" data-target="category" data-name="name" placeholder="분류명입력">
                                            <button type="button" class="ui icon button" onclick="addCategory(this)"><i class="plus icon"></i></button>
                                            <button type="button" class="ui icon button" onclick="removeCategory(this)"><i class="minus icon"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="ui action input ">
                                            <input type="text" data-target="item" data-name="name" placeholder="항목명입력">
                                            <button type="button" class="ui icon button" onclick="addItem(this)"><i class="plus icon"></i></button>
                                            <button type="button" class="ui icon button" onclick="removeItem(this)"><i class="minus icon"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="ui form">
                                            <div class="field">
                                                <textarea data-target="item" data-name="valuationBasis" class="fluid"></textarea>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="ui input fluid">
                                            <input type="text" data-target="item" data-name="maxScore" class="-input-numerical" value="1">
                                        </div>
                                    </td>
                                    <td>
                                        <div class="ui form">
                                            <div class="field">
                                                <textarea data-target="item" data-name="remark" class="fluid"></textarea>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function ratingViewPopup() {
                $('#modal-rating-view').modalShow();
            }

            function createEmptyCategoryTr() {
                return $('<tr class="-header-category-tr">\n' +
                    '    <td rowspan="1">\n' +
                    '        <div class="ui action input ">\n' +
                    '            <input type="text" data-target="category" data-name="name" placeholder="분류명입력">\n' +
                    '            <button type="button" class="ui icon button" onclick="addCategory(this)"><i class="plus icon"></i></button>\n' +
                    '            <button type="button" class="ui icon button" onclick="removeCategory(this)"><i class="minus icon"></i></button>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui action input ">\n' +
                    '            <input type="text" data-target="item" data-name="name" placeholder="항목명입력">\n' +
                    '            <button type="button" class="ui icon button" onclick="addItem(this)"><i class="plus icon"></i></button>\n' +
                    '            <button type="button" class="ui icon button" onclick="removeItem(this)"><i class="minus icon"></i></button>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui form">\n' +
                    '            <div class="field">\n' +
                    '                <textarea data-target="item" data-name="valuationBasis" class="fluid"></textarea>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui input fluid">\n' +
                    '            <input type="text" data-target="item" data-name="maxScore" class="-input-numerical" value="1">\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui form">\n' +
                    '            <div class="field">\n' +
                    '                <textarea data-target="item" data-name="remark" class="fluid"></textarea>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '</tr>');
            }

            function createEmptyItemTr() {
                return $('<tr>\n' +
                    '    <td>\n' +
                    '        <div class="ui action input ">\n' +
                    '            <input type="text" data-target="item" data-name="name" placeholder="항목명입력">\n' +
                    '            <button type="button" class="ui icon button" onclick="addItem(this)"><i class="plus icon"></i></button>\n' +
                    '            <button type="button" class="ui icon button" onclick="removeItem(this)"><i class="minus icon"></i></button>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui form">\n' +
                    '            <div class="field">\n' +
                    '                <textarea data-target="item" data-name="valuationBasis" class="fluid"></textarea>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui input fluid">\n' +
                    '            <input type="text" data-target="item" data-name="maxScore" class="-input-numerical" value="1">\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '    <td>\n' +
                    '        <div class="ui form">\n' +
                    '            <div class="field">\n' +
                    '                <textarea data-target="item" data-name="remark" class="fluid"></textarea>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </td>\n' +
                    '</tr>');
            }

            function addCategory(trigger) {
                const tr = $(trigger).closest('tr');
                const tbody = tr.parent();

                const nextHeaderTr = tr.nextAll('.-header-category-tr:first');
                if (nextHeaderTr.length > 0) {
                    createEmptyCategoryTr().insertBefore(nextHeaderTr);
                } else {
                    tbody.append(createEmptyCategoryTr());
                }

                calculateTotalScore();
            }

            function removeCategory(trigger) {
                const tr = $(trigger).closest('tr');
                const tbody = tr.parent();

                const removingTrCount = tr.find('td:first').attr('rowspan') || 1;

                const nextAll = tr.nextAll();
                for (let i = removingTrCount - 2; i >= 0; i--)
                    $(nextAll[i]).remove();
                tr.remove();

                if (tbody.children('tr').length === 0)
                    tbody.append(createEmptyCategoryTr());

                calculateTotalScore();
            }

            function addItem(trigger) {
                const tr = $(trigger).closest('tr');

                if (!tr.is('.-header-category-tr')) {
                    const headerCategoryTr = tr.prevAll('.-header-category-tr:first');
                    const firstTd = headerCategoryTr.find('td:first');
                    const rowspan = parseInt(firstTd.attr('rowspan') || 1);
                    firstTd.attr('rowspan', rowspan + 1);
                } else {
                    const firstTd = tr.find('td:first');
                    const rowspan = parseInt(firstTd.attr('rowspan') || 1);
                    firstTd.attr('rowspan', rowspan + 1);
                }

                createEmptyItemTr().insertAfter(tr);

                calculateTotalScore();
            }

            function removeItem(trigger) {
                const tr = $(trigger).closest('tr');

                if (!tr.is('.-header-category-tr')) {
                    const headerCategoryTr = tr.prevAll('.-header-category-tr:first');
                    const firstTd = headerCategoryTr.find('td:first');
                    const rowspan = parseInt(firstTd.attr('rowspan') || 1);
                    firstTd.attr('rowspan', Math.max(1, rowspan - 1));
                    tr.remove();
                } else {
                    const firstTd = tr.find('td:first');
                    const rowspan = parseInt(firstTd.attr('rowspan') || 1);
                    if (rowspan > 1) {
                        firstTd.attr('rowspan', Math.max(1, rowspan - 1));
                        firstTd.nextAll().remove();

                        const nextTr = tr.next();
                        nextTr.children().detach().appendTo(tr);
                        nextTr.remove();
                    } else {
                        const newTr = createEmptyCategoryTr();
                        newTr.insertBefore(tr);
                        newTr.find('input:first').val(tr.find('input:first').val());
                        tr.remove();
                    }
                }

                calculateTotalScore();
            }

            function calculateTotalScore() {
                let totalScore = 0;
                $('#item-sheet [data-name=maxScore]').each(function () {
                    let value = parseInt($(this).val());
                    if (isNaN(value) || !isFinite(value) || value < 1 || value > 100)
                        value = 1;

                    $(this).val(value);
                    totalScore += value;
                });

                $('#total-score').text(totalScore);
            }

            $('#item-sheet').on('focusout, blur', '[data-name=maxScore]', function () {
                calculateTotalScore();
            });

            calculateTotalScore();

            function saveEvaluationItem() {
                calculateTotalScore();

                const totalScore = parseInt($('#total-score').text());
                if (totalScore !== 100)
                    return alert('최종배점이 100점이어야 합니다.');

                const categories = [];
                let category;
                $('#item-sheet tr').each(function () {
                    const categoryName = $(this).find('[data-target="category"][data-name="name"]').val();
                    const itemName = $(this).find('[data-target="item"][data-name="name"]').val();
                    const valuationBasis = $(this).find('[data-target="item"][data-name="valuationBasis"]').val();
                    const maxScore = $(this).find('[data-target="item"][data-name="maxScore"]').val();
                    const remark = $(this).find('[data-target="item"][data-name="remark"]').val();

                    if ($(this).is('.-header-category-tr')) {
                        if (category)
                            categories.push(category);

                        category = {name: categoryName, items: []};
                    }

                    category.items.push({name: itemName, valuationBasis: valuationBasis, maxScore: maxScore, remark: remark});
                });

                if (category)
                    categories.push(category);

                restSelf.post('/api/evaluation-item/target/${formId}', {categories: categories}).done(function () {
                    alert('저장되었습니다.');
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
