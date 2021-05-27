(function ($) {
    function findAndMe(selector, context) {
        return context.find(selector).add(context.filter(selector));
    }

    $.fn.bindHelpers = function () {
        findAndMe('.-ajax-loader', this).asAjaxLoader();
        findAndMe('.-json-submit', this).asJsonSubmit();
        findAndMe('.-lead-selector', this).asLeadSelector();
        findAndMe('.-lean-modal', this).asLeanModal();
        findAndMe('.-input-float', this).asInputFloat();
        findAndMe('.-input-numerical', this).asInputNumerical();
        findAndMe('.-input-phone', this).asInputPhone();
        findAndMe('.-input-restrict-length', this).asInputRestrictLength();
        findAndMe('.-count', this).animateCount();
        findAndMe('input[type=hidden][name^=_][value]', this).remove();
        findAndMe('.modal-close', this).each(function () {
            $(this).click(function () {
                $(this).parents('.modal').modal('hide');
            });
        });
        findAndMe('form .button-reset', this).click(function () {
            const form = $(this).closest('form');
            form.find('[name]').each(function () {
                const value = $(this).attr('data-init-value') || '';
                $(this).val(value);
                if ($(this).is('select')) {
                    $(this).find('option').filter(function () {
                        return value ? $(this).val() === value : !$(this).val();
                    }).prop('selected', true);
                }
            });
        });
        findAndMe('.selectable-only tr', this).not('.ui').click(function () {
            if (!$(this).attr('data-id')) return;

            $(this).siblings().removeClass('active');

            if ($(this).hasClass('active')) {
                $(this).removeClass('active');
                $('.-control-entity[data-entity="' + $(this).closest('table').attr('data-entity') + '"]').hide();
            } else {
                $(this).addClass('active');
                $('.-control-entity[data-entity="' + $(this).closest('table').attr('data-entity') + '"]').show();
            }
        });
        findAndMe('.selectable tr', this).not('.ui').click(function () {
            if (!$(this).attr('data-id')) return;

            const table = $(this).closest('table');
            const entityName = table.attr('data-entity');
            $(this).toggleClass('active');
            const activeTrList = table.find('tr.active');
            if (activeTrList.length > 0) {
                $('.-control-entity[data-entity="' + entityName + '"]').show();
            } else {
                $('.-control-entity[data-entity="' + entityName + '"]').hide();
            }
        });
        findAndMe('.-moving-container', this).each(function () {
            const container = $(this);
            const rightSelector = container.find('.-right-selector');
            const leftSelector = container.find('.-left-selector');

            function toLeft() {
                const rightValues = rightSelector.val();
                const leftOptions = leftSelector.find('option');
                rightSelector.find('option').filter(function () {
                    return rightValues.indexOf($(this).val()) >= 0;
                }).detach().filter(function () {
                    const rightOption = $(this);
                    return !leftOptions.filter(function () {
                        return $(this).val() === rightOption.val();
                    }).length;
                }).appendTo(leftSelector);
            }

            function toRight() {
                const leftValues = leftSelector.val();
                const rightOptions = rightSelector.find('option');
                leftSelector.find('option').filter(function () {
                    return leftValues.indexOf($(this).val()) >= 0;
                }).detach().filter(function () {
                    const leftOption = $(this);
                    return !rightOptions.filter(function () {
                        return $(this).val() === leftOption.val();
                    }).length;
                }).appendTo(rightSelector);
            }

            container.find('.-to-left').click(toLeft);
            container.find('.-to-right').click(toRight);

            container.on('dblclick','.-right-selector option, .-left-selector option', function () {
                const toLeft = $(this).parent().hasClass('-right-selector');
                $(this).detach().appendTo(toLeft ? leftSelector : rightSelector);
            });

        });
        findAndMe('.-sortable-table', this).each(function () {
            const table = $(this);
            const searchForm = $(table.attr('data-search-form'));
            const input = searchForm.find('[name="' + table.attr('data-order-field') + '"]');

            table.find('[data-sortable-value]').each(function () {
                $(this).css('cursor', 'pointer')
                    .click(function () {
                        input.val($(this).attr('data-sortable-value'));
                        searchForm.submit();
                    });
            });
        });
        findAndMe('audio', this).each(function () {
            maudio({obj: this});
        });

        /* jquery-ui.datepicker 호출. id 재생성되므로, 기존 id 속성을 삭제시킨다. */
        findAndMe('.-datepicker', this).asDatepicker();

        return this;
    };
})($ || jQuery);
