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
        findAndMe('.-tagify', this).each(function () {
            $(this).tagify();
        });
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
        findAndMe('.-click-prev', this).click(function () {
            $(this).prev().click();
        });
        findAndMe('.selectable-only tr', this).not('.ui').click(function () {
            if (!$(this).attr('data-id')) return;

            $(this).siblings().removeClass('active');
            $(this).siblings().find('input[type=radio]:first').prop('checked', false);
            $(this).find('input[type=radio]:first').prop('checked', true);

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
            $(this).find('input[type=checkbox]:first').prop('checked', $(this).hasClass('active'));

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

            container.on('dblclick', '.-right-selector option, .-left-selector option', function () {
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
        findAndMe(".-overlay-scroll", this).overlayScrollbars({});
        findAndMe(".-vertical-resizable", this).each(function () {
            $(this).resizable({
                minHeight: parseInt($(this).attr('data-resizable-min-height') || 53),
                maxHeight: parseInt($(this).attr('data-resizable-max-height') || 610)
            });
        });
        findAndMe('audio', this).each(function () {
            maudio({obj: this});
        });
        findAndMe(".float-field-wrap", this).each(function() {
            $('.float-field.inline').last().addClass('last');
        });
        findAndMe(".-slider-time", this).each(function () {
            $(this).slider({
                range: true, min: 0, max: 24 * 60 - 1, disabled: $(this).attr('data-key') == null, step: 1, values: [$(this).attr('data-start'), $(this).attr('data-end')], slide: function (e, ui) {
                    ui.handle.innerHTML = pad(parseInt(ui.values[ui.handleIndex] / 60), 2) + ':' + pad(ui.values[ui.handleIndex] % 60, 2);
                }, change: function (e, ui) {
                    if ($(this).attr('data-key') != null && $(this).attr('data-key') !== '') {
                        restSelf.put("/api/schedule-group/item/time/" + $(this).attr('data-key'), {
                            parent: $(this).attr('data-parent'),
                            fromhour: ui.values[0],
                            tohour: ui.values[1]
                        }).done(function () {
                            alert('변경되었습니다.', function () {
                                reload();
                            });
                        });
                    }
                }, stop: function (e, ui) {
                    ui.handle.innerHTML = '';
                }
            });
        });
        findAndMe('.-play-trigger', this).each(function () {
            $(this).popup({
                popup: $($(this).attr('data-target') || $(this).next()),
                on: 'click'
            });
        });
        findAndMe('select.dropdown', this).each(function () {
            const options = {};
            const maxSelections = $(this).attr('data-dropdown-max-selections');
            if (maxSelections) {
                options.maxSelections = parseInt(maxSelections);
                $(this).dropdown(options);
            } else {
                const values = $(this).val();
                $(this).find('option').prop('selected', false).removeAttr('selected');
                $(this).dropdown(options);

                const $this = $(this);

                values.map(function (v) {
                    $this.find('option').filter(function () {
                        return $(this).val() === v;
                    }).prop('selected', true);
                });

                setTimeout(function () {
                    $this.change();
                }, 1);
            }
        });
        findAndMe('.-select-group-container', this).each(function () {
            const input = $(this).find($(this).attr('data-input'));
            const name = $(this).find($(this).attr('data-name'));
            $(this).find($(this).attr('data-select')).click(function () {
                popupSelectOrganizationModal(function (groupCode, groupNames, groupNameElement) {
                    input.val(groupCode).change();
                    name.empty().append(groupNameElement);
                });
            });
            $(this).find($(this).attr('data-clear')).click(function () {
                input.val('').change();
                name.html('<span class="section">부서를 선택해 주세요.</span>');
            });
        });
        findAndMe(".-buttons-set-range-container", this).each(function () {
            const $this = $(this);

            const startDate = $this.find($this.attr('data-startdate'));
            const endDate = $this.find($this.attr('data-enddate'));

            const buttons = $this.find('.-button-set-range');
            buttons.click(function () {
                buttons.removeClass('active');
                $(this).addClass('active');

                const interval = $(this).attr('data-interval');
                const number = $(this).attr('data-number');

                if (!endDate.val())
                    endDate.val(moment().format('YYYY-MM-DD'));

                startDate.val(moment(endDate.val()).add(parseInt(number) * -1, interval).add(1, 'days').format('YYYY-MM-DD'));
            });
        });
        /* 태헌씨 스크립트 */

        findAndMe("select.level", this).each(function () {
            for (let i = 1; i < 11; i++) {
                $(this).append("<option>" + i + "</option>");
            }
        });
        findAndMe('.ui.checkbox', this).checkbox();
        findAndMe('.panel-heading .slider', this).on("click", function () {
            $(this).parents('.panel').toggleClass('hide');
        });
        findAndMe('.treeview-menu', this).each(function () { // 좌측네비게이션
            $(this)
                .siblings()
                .click(function () {
                    $(this)
                        .siblings(".treeview-menu")
                        .toggleClass("treeview-on");
                    $(this).toggleClass("on");
                    $(this)
                        .find(".arrow")
                        .toggleClass("rotate");
                });
        });
        findAndMe('.side-toggle-btn', this).each(function () { // side-bar toggle
            $(this).click(function () {
                $('.content-wrapper').toggleClass('remove-padding');
                $('.side-bar').toggleClass('hide');
            });
        });
        findAndMe('.menu .item', this).tab();

        findAndMe('td .form, .table button, .table a', this).on("click", function () {
            event.stopPropagation();
        });

        /* jquery-ui.datepicker 호출. id 재생성되므로, 기존 id 속성을 삭제시킨다. */
        findAndMe('.-datepicker', this).asDatepicker();

        return this;
    };
})($ || jQuery);
