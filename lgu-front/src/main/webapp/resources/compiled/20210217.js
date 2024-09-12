if (!String.prototype.contains) {
    String.prototype.contains = function (arg) {
        return this.indexOf(arg) >= 0;
    };
}

String.prototype.toObject = function () {
    const options = {};
    if (this == null)
        return options;
    const tokens = this.trim().split(';');
    for (let i = 0; i < tokens.length; i++) {
        const pair = tokens[i].trim().split(':');
        if (pair.length < 2)
            continue;
        const key = pair[0].trim();
        const value = pair[1].trim();
        if (key.length <= 0)
            continue;
        options[key] = value;
    }
    return options;
};

String.prototype.toJquerySelector = function () {
    let result = '';
    const demlist = '.[]/!:';
    for (let i = 0; i < this.length; i++) {
        const c = this.charAt(i);
        if (demlist.indexOf(c) >= 0)
            result += '\\';
        result += c;
    }
    return result;
};(function () {
    const FormDataItem = (function () {
        return function (name, value, blobName) {
            this.name = name;
            this.value = value;
            this.blobName = blobName;
        };
    })();

    FormData.prototype._values = FormData.prototype.values;
    delete FormData.prototype.values;

    const _append = FormData.prototype.append;
    FormData.prototype.append = function (name, value, blobName) {
        if (!this.values) {
            this.values = [];
        }
        this.values.push(new FormDataItem(name, value, blobName));
        const argumentsArray = [];
        for (let i = 0; i < arguments.length; i++)
            argumentsArray.push(arguments[i]);
        _append.apply(this, argumentsArray);
    };

    FormData.prototype.stringify = function () {
        return $.param(this.values ? this.values : []);
    };
})();
(function ($) {
    $.fn.isAfter = function(sel){
        return this.prevAll().filter(sel).length !== 0;
    };

    $.fn.isBefore= function(sel){
        return this.nextAll().filter(sel).length !== 0;
    };

    $.blockUIFixed = function () {
        $.blockUI({
            css: {
                'border-color': 'transparent',
                'background-color': 'transparent',
                top: '50%',
                left: '50%',
                width: '',
                transform: 'translate(-50%,-50%)'
            },
            message: function () {
                if (loadingImageSource)
                    return $('<img/>', {src: loadingImageSource});

                return $('<div/>', {text: 'Communicating with the server..'});
            }
        });
    };

    $.onlyNumber = function (dom) {
        const value = '' + $(dom).val();
        let onlyNumber = '';
        for (let i = 0; i < value.length; i++) {
            const char = value.charAt(i);
            if (parseInt(char) >= 0 && parseInt(char) <= 9)
                onlyNumber += '' + char;
        }
        return onlyNumber;
    };

    $.addQueryString = function (url, data) {
        function concatDelimiter(url) {
            return url + (url.indexOf('?') >= 0 ? '&' : '?');
        }

        function addQueryString(url, key, value) {
            if (data === null || data === undefined)
                return url;

            if (value instanceof Array) {
                value.map(function (e) {
                    url = concatDelimiter(url) + encodeURIComponent(key) + '=' + encodeURIComponent(e);
                });
            } else {
                url = concatDelimiter(url) + encodeURIComponent(key) + '=' + encodeURIComponent(value);
            }

            return url;
        }

        if (!data || typeof data !== "object")
            return concatDelimiter(url) + encodeURIComponent((data === null || data === undefined) ? '' : data);

        for (let key in data) {
            if (data.hasOwnProperty(key)) {
                url = addQueryString(url, key, data[key]);
            }
        }

        return url;
    };

    $.fn.formDataObject = function () {
        const data = {};
        $(this).find('[name]').each(function () {
            const $this = $(this);
            const inputName = $this.attr('name');

            if (inputName.length > 0) {
                if ($this.attr('type') && $this.attr('type').toLowerCase() === "checkbox") {
                    if (!$this.is(':checked'))
                        return;

                    if (!data[inputName] || !(data[inputName] instanceof Array))
                        data[inputName] = [];

                    data[inputName].push($this.val());
                } else if ($this.attr('type') && $this.attr('type').toLowerCase() === "radio") {
                    if ($this.is(':checked')) {
                        data[inputName] = $this.val();
                    }
                } else {
                    data[inputName] = $this.val();
                }
            }
        });

        return data;
    };

    $.fn.asLeanModal = function () {
        return this.each(function () {
            const $this = $(this);
            $this.leanModal($this.data('options') ? $this.data('options').toObject() : {});
        });
    };

    $.fn.asInputFloat = function () {
        return this.each(function () {
            if (!$(this).is('input') && !$(this).is('textarea'))
                return;
            $(this).bind('focusout', (function () {
                const value = '' + $(this).val();
                let result = '';
                let firstComma = false;
                for (let i = 0; i < value.length; i++) {
                    const char = value.charAt(i);
                    if (parseInt(char) >= 0 && parseInt(char) <= 9)
                        result += '' + char;
                    if (char === '.' && !firstComma) {
                        firstComma = true;
                        result += '' + char;
                    }
                }

                if (result.substr(result.length - 1, 1) === '.')
                    result = result.substr(0, result.length - 1);

                return $(this).val(result);
            }));
        });
    };

    $.fn.asInputNumerical = function () {
        return this.each(function () {
            if (!$(this).is('input') && !$(this).is('textarea'))
                return;
            $(this).bind('keyup', (function () {
                return $(this).val($.onlyNumber(this));
            }));
        });
    };

    /**
     * ??-???-????
     * ??-????-????
     * ???-????-????
     * */
    $.fn.asInputPhone = function () {
        return this.each(function () {
            if (!$(this).is('input') && !$(this).is('textarea'))
                return;
            $(this).bind('keyup', (function () {
                const numbers = $.onlyNumber(this);

                let result = '';
                for (let i = 0; i < numbers.length && i < 11; i++) {
                    if ((numbers.length <= 9 && (i === 2 || i === 5))
                        || (numbers.length === 10 && (i === 2 || i === 6))
                        || (numbers.length > 10 && (i === 3 || i === 7)))
                        result += '-';

                    result += numbers.charAt(i);
                }

                return $(this).val(result);
            }));
        });
    };

    $.fn.asInputRestrictLength = function () {
        return this.each(function () {
            const $this = $(this);

            if (!$this.is('input') && !$this.is('textarea'))
                return;

            const options = $this.data('length').toObject();
            const maxLength = parseInt(options.length);

            let container = $this;
            const greatContainerCount = parseInt(options.container);
            if (!isNaN(greatContainerCount) && isFinite(greatContainerCount)) {
                for (let i = 0; i < greatContainerCount; i++)
                    container = container.parent();
            } else if (options.container != null && options.container.length > 0) {
                container = $this.closest(options.container);
            } else {
                container = $('body');
            }

            const target = container.find(options.target);
            if (!isNaN(maxLength) && isFinite(maxLength) && maxLength > 0) {
                $this.bind('keyup keydown keypress', checkLength);
                checkLength();
            }

            function checkLength() {
                const length = $this.val().length;
                if (length > maxLength)
                    $this.val($this.val().substring(0, maxLength));

                target.text(length > maxLength ? maxLength : length);
            }
        });
    };

    $.ajaxLoad = function (url, target, method, data, formData) {
        $.blockUIFixed();

        return $.ajax({
            url: $.addQueryString($.addQueryString(url, $.extend({ajaxLoaderReturnTo: location.href}, method === 'get' ? data : {})), {____t: new Date().getTime()}),
            cache: false,
            data: method === 'get' ? null : formData,
            type: method,
            processData: false,
            contentType: false,
            dataType: 'html'
        }).done(function (html) {
            target.empty();

            const mixedNodes = $.parseHTML(html, null, true);
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is('script')) {
                    const id = node.attr('id');
                    if (id) $(document.getElementById(id)).remove();
                    $('body').append(node);
                } else {
                    node.appendTo(target)
                        .bindHelpers();
                }
            }
        }).fail(function (e) {
            if (e.status === 401) alert("접근 권한이 없습니다. 로그인 정보를 확인하세요.");
            else alert("An error occurred. Please try again later");
        }).always(function () {
            $.unblockUI();
        });
    };

    $.ajaxLoadForm = function (_this, isForm) {
        const ajaxLoaderData = $.extend({target: _this}, $(_this).data());
        const target = $(ajaxLoaderData.target);
        const url = isForm ? _this.action ? _this.action : $(_this).data('action') : _this.href ? _this.href : $(_this).data('href');
        const method = isForm ? _this.method ? _this.method : $(_this).data('method') : 'get';

        const data = {};
        const formData = new FormData();

        $($(_this).find('[name]').filter('select, textarea, input').serializeArray()).each(function () {
            data[this.name] = this.value;
            formData.append(this.name, this.value);
        });

        $(_this).find('input[type="file"]').each(function () {
            const input = this;
            $.each(input.files, function (file) {
                formData.append(input.name, file, file.name);
            });
        });

        return $.ajaxLoad(url, target, method, data, formData);
    };

    $.fn.asAjaxLoader = function () {
        return this.each(function () {
            const _this = this;
            const isForm = $(this).is('form') || $(this).hasClass('-as-form');

            $(this).on(isForm ? 'submit' : 'click', function (event) {
                if (event.currentTarget !== _this) return;
                event.preventDefault();

                $.ajaxLoadForm(_this, isForm);
            });
        });
    };

    $.fn.asDatepicker = function () {
        return this.each(function () {
            const $this = $(this);
            const format = $this.data('format');
            try {
                $this.datepicker("destroy");
            } catch (ignored) {
            }
            $this.removeAttr('id');
            $this.removeClass('hasDatepicker');
            const options = $this.data('options') ? $this.data('options').toObject() : {};
            $this.datepicker($.extend(options, {
                dateFormat: format != null && format.length > 0 ? format : options.dateFormat != null ? options.dateFormat : 'yy-mm-dd'
            }));
            $this.prop("autocomplete", "off")
        });
    };

    $.fn.asLeadSelector = function () {
        if ($(this).is('input[type=checkbox]'))
            return this.change(function () {
                if ($(this).is(":checked"))
                    $($(this).data('target')).prop("checked", true);
                else
                    $($(this).data('target')).prop("checked", false);

                const post = $(this).data('post');
                if (post != null) {
                    const args = $(this).data('args');
                    eval(post)(this, args ? args.toObject() : null);
                }
            });
        else
            return this.click(function () {
                const status = $(this).data('status');
                if (status === 'on') {
                    $($(this).data('target')).prop("checked", false);
                    $(this).data('status', 'off');
                } else {
                    $($(this).data('target')).prop("checked", true);
                    $(this).data('status', 'on');
                }

                const post = $(this).data('post');
                if (post != null) {
                    const args = $(this).data('args');
                    eval(post)(this, args ? args.toObject() : null);
                }
            });
    };

    $.fn.animateCount = function () {
        return this.each(function () {
            $(this).prop('Count', 0).animate({
                Counter: $(this).text()
            }, {
                duration: 3000,
                easing: 'swing',
                step: function (now) {
                    $(this).text(Math.ceil(now));
                }
            })
        });
    };

    $.fn.asJsonData = function () {
        const deferred = jQuery.Deferred();
        const data = {};
        const files = {};

        const emptynull = $(this).attr('data-emptynull') === "true";

        function set(name, multiple, value) {
            const members = name.split('.');

            for (let target = data, i = 0; i < members.length; i++) {
                const last = i === members.length - 1;
                const member = members[i];
                const checkMap = /([0-9a-zA-Z_]+)\[([^\]]+)]/.exec(member);
                if (checkMap) {
                    const key = checkMap[1];
                    const attr = checkMap[2];
                    if (target[key] === undefined) target[key] = {};
                    target = target[key];
                    if (last) {
                        if (multiple) if (!target[attr]) target[attr] = [];
                        if (multiple) target[attr].push(value);
                        else target[attr] = value;
                    } else {
                        if (target[attr] === undefined) target[attr] = {};
                        target = target[attr];
                    }
                } else {
                    if (last) {
                        if (multiple) if (!target[member]) target[member] = [];
                        if (multiple) target[member].push(value);
                        else target[member] = value;
                    } else {
                        if (target[member] === undefined) target[member] = {};
                        target = target[member];
                    }
                }
            }
        }

        function convertFileToData() {
            for (let name in files) {
                if (files.hasOwnProperty(name)) {
                    let file = files[name];
                    if (file instanceof Array) {
                        if (!file.length) {
                            delete files[name];
                            continue;
                        }
                        file = file.splice(0, 1)[0];
                    } else {
                        delete files[name];
                    }

                    readFile(file).done(function (result) {
                        set(name, files[name] instanceof Array, {
                            data: result.data.split(',')[1],
                            fileName: extractFilename(result.fileName)
                        });
                        convertFileToData();
                    }).fail(function (error) {
                        deferred.reject('[' + name + '] ' + error);
                    });
                }
                return;
            }
            deferred.resolve(data);
        }

        $(this).find('[name]').each(function () {
            const $this = $(this);
            const multiple = $this.is('[multiple]') || $this.is('[data-multiple]');
            const name = $(this).attr('name');
            const value = $this.val();

            switch (($this.attr('type') || 'text').toLowerCase()) {
                case 'file': {
                    if (!this.files) break;
                    if (multiple) {
                        if (!files[name]) files[name] = [];
                        $.each(this.files, function (i, file) {
                            files[name].push(file);
                        });
                    } else if (this.files && this.files[0]) {
                        files[name] = this.files[0];
                    }
                    break;
                }
                case 'radio':
                case 'checkbox': {
                    if ($this.is(':checked'))
                        set(name, multiple, value);
                    break;
                }
                default: {
                    if (!emptynull || value)
                        set(name, multiple, value);
                }
            }
        });

        convertFileToData();
        return deferred;
    };

    $.fn.asJsonSubmit = function () {
        return this.submit(function (event) {
            event.preventDefault();
            return submitJsonData(this);
        });
    };

})(jQuery);// leanModal v1.1 by Ray Stone - http://finelysliced.com.au
// Dual licensed under the MIT and GPL

// Modify by tinywind

(function ($) {
    window.leanModal = {
        _stackModal: [],
        _stackZIndex: [],
        _zIndexBackground: 910000,
        _zIndexBase: 920000,
        _zIndexInterval: 10,
        getModalZIndex: function (modal) {
            for (let i = 0; i < this._stackModal.length; i++)
                if (this._stackModal[i] === modal) {
                    console.error("already contains modal");
                    return null;
                }
            this._stackModal.push(modal);

            if (this._stackZIndex.length === 0) {
                const zIndex = this._zIndexBase;
                this._stackZIndex.push(zIndex);
                return zIndex;
            } else {
                zIndex = this._stackZIndex[this._stackZIndex.length - 1] + this._zIndexInterval;
                this._stackZIndex.push(zIndex);
                return zIndex;
            }
        },
        checkHideLeanOverlayContainer: function (modal) {
            for (let i = 0; i < this._stackModal.length; i++)
                if (this._stackModal[i] === modal) {
                    this._stackModal.splice(i, 1);
                    this._stackZIndex.splice(i, 1);
                }

            return this._stackModal.length === 0;
        }
    };

    $.fn.extend({
        leanModal: function (options) {
            if (options === 'destroy') {
                return this.each(function () {
                    const objects = this['lean-modal-objects'];
                    if (objects != null && objects instanceof Object) {
                        if (objects.modal != null && objects.modal.length > 0)
                            window.leanModal.checkHideLeanOverlayContainer(objects.modal[0]);

                        for (let key in objects)
                            if (objects.hasOwnProperty(key))
                                $(objects[key]).remove();
                    }
                });
            }

            const defaults = {closeButton: null};
            options = $.extend(defaults, options);
            let overlayContainer = $('#lean-overlay-container');
            if (overlayContainer.length === 0)
                overlayContainer = $('<div/>', {
                    id: 'lean-overlay-container'
                }).css({
                    position: 'fixed',
                    top: 0,
                    bottom: 0,
                    left: 0,
                    right: 0,
                    display: 'none',
                    'z-index': window.leanModal._zIndexBackground + ""
                }).appendTo($('body'));

            return this.each(function () {
                const overlay = $('<div/>', {
                    'class': 'lean-overlay'
                }).css({
                    display: "none",
                    opacity: 0.5,
                    position: "absolute",
                    top: 0,
                    bottom: 0,
                    left: 0,
                    right: 0,
                    'background-color': 'black'
                });
                overlayContainer.append(overlay);

                function showModal(modal, overlay) {
                    overlayContainer.show();
                    const zIndex = window.leanModal.getModalZIndex(modal[0]);
                    const top = (window.innerHeight - modal.outerHeight()) / 2;
                    modal.css({
                        'z-index': zIndex,
                        top: top < 0 ? "0px" : top + "px",
                        'max-height': (window.innerHeight - (top < 0 ? 0 : top)) + "px",
                        'margin-left': -(modal.outerWidth() / 2) + "px",
                        'transform': modal.outerWidth() ? 'none' : 'translateX(-50%)'
                    }).show()
                        .focus();
                    overlay.css({'z-index': zIndex - 1}).show();

                    $('body').addClass('no-scroll');
                }

                function closeModal(modal, overlay) {
                    modal.hide();
                    overlay.hide();

                    window.leanModal.checkHideLeanOverlayContainer(modal[0]);

                    const leanOverlayList = overlayContainer.find('.lean-overlay');
                    const numHiddenLeanOverlay = leanOverlayList.not(':hidden').length;
                    if (numHiddenLeanOverlay <= 0) {
                        overlayContainer.hide();
                        $('body').removeClass('no-scroll');
                    }
                }

                const href = $(this).attr("href");
                const modal = $(href != null ? href : $(this).attr('data-href'));
                modal.detach().appendTo(overlayContainer);

                if (modal.css('position') === 'fixed') {
                    modal.css({
                        display: "none",
                        'overflow-y': 'auto'
                    });
                } else {
                    modal.css({
                        display: "none",
                        'overflow-y': 'auto',
                        position: "absolute",
                        // bottom: 0,
                        left: 50 + "%",
                        'margin-left': -(modal.outerWidth() / 2) + "px",
                        'transform': modal.outerWidth() ? 'none' : 'translateX(-50%)'
                    });
                }

                $(this).click(function () {
                    showModal(modal, overlay);
                });

                $(options.closeButton).click(function () {
                    closeModal(modal, overlay);
                });

                this['lean-modal-objects'] = {modal: modal, overlay: overlay}
            });
        }
    })
})(jQuery);
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
        findAndMe(".-slider-time", this).each(function () {
            $(this).slider({range: true, min: 0, max: 24 * 60 - 1, disabled: $(this).attr('data-key') == null, step: 1, values: [$(this).attr('data-start'), $(this).attr('data-end')], slide: function (e, ui) {
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
                }});
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
            if (maxSelections){
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

                setTimeout(function (){
                    $this.change();
                }, 1);
            }
        });
        findAndMe('.-select-group-container', this).each(function () {
            const input = $(this).find($(this).attr('data-input'));
            const name = $(this).find($(this).attr('data-name'));
            $(this).find($(this).attr('data-select')).click(function () {
                popupSelectOrganizationModal(function (groupCode, groupNames, groupNameElement) {
                    input.val(groupCode);
                    name.empty().append(groupNameElement);
                });
            });
            $(this).find($(this).attr('data-clear')).click(function () {
                input.val('');
                name.html('<span class="section">버튼을 눌러 소속을 선택하세요.</span>');
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
        findAndMe('.nav-bar', this).each(function () { // side-bar toggle
            $(this).click(function () {
                $('.content-wrapper').toggleClass('remove-padding');
                $('.tab-label-container').toggleClass('hide-navbar');
                $(this).parent().toggleClass('nav-bar-hide');
            });
        });
        findAndMe('td .form, .table button, .table a', this).on("click", function () {
            event.stopPropagation();
        });
        findAndMe('.menu .item', this).tab();
        findAndMe(".sidebar-menu-container", this).overlayScrollbars({ // overflow scroll
            className: "os-theme-light"
        });
        findAndMe(".chat-body", this).overlayScrollbars({});


        /* jquery-ui.datepicker 호출. id 재생성되므로, 기존 id 속성을 삭제시킨다. */
        findAndMe('.-datepicker', this).asDatepicker();

        return this;
    };
})($ || jQuery);function TabController(tabLabelFlowContainerIndicator, tabLabelContainerIndicator, tabArrowIndicator, tabContentContainerIndicator) {
    this.tabLabelContainer = $(tabLabelContainerIndicator);
    this.tabContentContainer = $(tabContentContainerIndicator);
    this.tabArrow = $(tabArrowIndicator);
    this.tabLabelFlowContainer = $(tabLabelFlowContainerIndicator);
    this.tabLabels = [];
    this.tabContents = {};

    return this;
}

TabController.prototype.getTabLabelTotalWidth = function () {
    return this.tabLabelContainer.outerWidth();
};

TabController.prototype.decideTabArrowVisible = function () {
    const totalWidth = this.getTabLabelTotalWidth();
    if (totalWidth > this.tabLabelFlowContainer.outerWidth()) {
        this.tabArrow.show();
    } else {
        this.tabArrow.hide();
    }
};

TabController.prototype.moveToRightTabLabels = function (left) {
    const containerWidth = this.tabLabelFlowContainer.outerWidth();
    const labelsWidth = this.getTabLabelTotalWidth();

    if (!isNaN(left)) {
        this.tabLabelContainer.css('left', 0);
        return;
    }

    const currentLeft = parseFloat(this.tabLabelContainer.css('left'));
    this.tabLabelContainer.css('left', labelsWidth > currentLeft + containerWidth
        ? Math.max(containerWidth - labelsWidth, currentLeft - containerWidth)
        : currentLeft - containerWidth);
};

TabController.prototype.moveToLeftTabLabels = function () {
    const containerWidth = this.tabLabelFlowContainer.outerWidth();
    const currentLeft = parseFloat(this.tabLabelContainer.css('left'));
    this.tabLabelContainer.css('left', Math.min(currentLeft + containerWidth, 0));
};

TabController.prototype.attachTab = function (linker) {
    const _this = this;
    const title = $(linker).attr('title') || $(linker).text();
    const href = $(linker).attr('href');

    const validTabLabels = this.tabLabelContainer.find('.tab-label').filter(function () {
        return $(this).attr('data-href') === href;
    });

    const tabContentId = validTabLabels.length > 0 ? validTabLabels.attr('data-target-id') : ('tab-' + guid());
    let tabLabel;

    if (validTabLabels.length > 0) {
        tabLabel = validTabLabels
    } else {
        tabLabel = $('<li/>', {
            'class': 'tab-label', 'data-href': href, 'data-target-id': tabContentId, text: title,
            click: function () {
                _this.tabLabels.map(function (label) {
                    label.removeClass('active');
                });
                $(this).addClass('active');

                values(_this.tabContents).map(function (content) {
                    content.removeClass('active');
                });
                _this.tabContents[$(this).attr('data-target-id')].addClass('active');
                _this.decideTabArrowVisible();
            }
        });

        if ($(linker).attr('data-closable') !== 'false')
            tabLabel.append($('<button/>', {
                'class': 'tab-close',
                click: function () {
                    _this.removeTab($(this).closest('.tab-label'));
                    _this.decideTabArrowVisible();
                    try {
                        tab.close();
                    } catch (e) {
                        console.error(e);
                    }
                }
            }));

        _this.tabLabels.push(tabLabel);
    }

    if (validTabLabels.length === 0) {
        this.tabLabelContainer.prepend(tabLabel);
        const tabContent = $('<iframe/>', {id: tabContentId, name: tabContentId, 'class': 'tab-content active'});
        this.tabContentContainer.append(tabContent);
        _this.tabContents[tabContentId] = tabContent;
        this.decideTabArrowVisible();
    }

    // FIXME: extract
    const tab = window.open(href, tabContentId, "width=0 height=0 menubar=no status=no");

    tabLabel.click();
    this.moveToRightTabLabels(0);
};

TabController.prototype.removeTab = function (tabLabel) {
    const _this = this;
    const contentId = tabLabel.attr('data-target-id');
    const changingActiveLabel = tabLabel.hasClass('active');

    function getTabLabelIndex() {
        for (let i = 0; i < _this.tabLabels.length; i++)
            if (contentId === _this.tabLabels[i].attr('data-target-id'))
                return i;

        throw 'invalid tabLabel';
    }

    const labelIndex = getTabLabelIndex();
    _this.tabLabels.splice(labelIndex, 1);
    tabLabel.remove();

    _this.tabContents[contentId].remove();
    delete _this.tabContents[contentId];

    if (changingActiveLabel)
        try {
            const lastLabel = (_this.tabLabels[labelIndex] || _this.tabLabels[labelIndex - 1]);
            if (lastLabel)
                lastLabel.click();
        } catch (e) {
            console.error(e);
        }

    _this.decideTabArrowVisible();
};(function ($) {
    const WIDGET_GRID = 30;

    function createDom(klass) {
        return $('<div/>', {
            class: klass || ''
        });
    }

    /**
     * IVR Editor 객체. rendering, widget 정보를 가짐
     */
    function IvrEditor(container, option) {
        const $dom = createDom('ui-ivr-canvas');
        this.dom = $dom[0];
        this.widgets = [];
        this.option = option;
        this.container = container;

        (function init() {
            this.width = $(container).innerWidth();
            this.height = $(container).innerHeight();

            if (!this.width || !this.height) {
                return setTimeout(function () {
                    init.apply(this, []);
                }, 100);
            }

            $dom.css({
                position: 'absolute',
                top: 0,
                left: 0,
                width: this.width,
                height: this.height
            });

            $(container).append($dom)
                .css({
                    overflow: 'auto'
                });

            if (option.droppable) {
                const dropEvent = option.droppable.drop;
                this.dropEvent = dropEvent;
                delete option.droppable.drop;

                $dom.droppable($.extend({
                    drop: function (event, ui) {
                        if (!$(event.toElement).is('.ui-ivr-canvas'))
                            return;

                        if (dropEvent)
                            dropEvent.apply($dom, [event, ui]);
                    }
                }, option.droppable));
            }
        })();

        return this;
    }

    IvrEditor.prototype.addWidget = function (optionParam, connectionPoint) {
        const widget = new Widget(this, optionParam, connectionPoint);
        this.widgets.push(widget);
        $(this.dom).append(widget.dom);
        return widget;
    };

    IvrEditor.prototype.getWidget = function (option) {
        const widgets = this.widgets;
        if (typeof option === 'function') {
            const widget = widgets.filter(option)[0];
            if (!widget) throw 'cannot find widget';
            return widget;
        }

        const widgetId = option;
        const widget = this.widgets.filter(function (e) {
            return widgetId === e.option.id;
        })[0];
        if (!widget) throw 'invalid widgetId: ' + widgetId;
        return widget;
    };

    IvrEditor.prototype.updateWidget = function (widgetId, optionParam) {
        this.getWidget(widgetId).setContent(optionParam);
    };

    IvrEditor.prototype.offset = function (target) {
        const baseOffset = $(this.dom).offset();
        const targetOffset = $(target).offset();
        return {
            top: targetOffset.top - baseOffset.top,
            left: targetOffset.left - baseOffset.left
        };
    };

    IvrEditor.prototype.setMaxSizeIfOverflowByWidget = function (widget) {
        const widgetRight = widget.x + widget.width + 150/*padding*/;
        const widgetBottom = widget.y + widget.height + 150/*padding*/;

        this.width = this.width > widgetRight ? this.width : widgetRight;
        this.height = this.height > widgetBottom ? this.height : widgetBottom;

        this.width = Math.max(this.width, $(this.container).innerWidth());
        this.height = Math.max(this.height, $(this.container).innerHeight());

        $(this.dom).css({
            width: this.width,
            height: this.height
        });
        // 굳이 렌더링 종료를 안 기다려도 됨.
    };

    function findRootWidget(widget) {
        if (!widget)
            return null;

        if (widget.connectionPoint)
            return findRootWidget(widget.connectionPoint.widget);

        return widget;
    }

    IvrEditor.prototype.render = function () {
        if (!this.widgets)
            return;

        this.widgets.map(function (widget) {
            widget.rendered = false;
            if (widget.connectionPoint)
                widget.connectionPoint.rendered = false;
        });

        findRootWidget(this.widgets[0]).render();
    };

    /**
     * IVR 단계. 연결의 입출력(line drawing)을 위해서 ConnectionPoint가 생성되어야 한다. ConnectionPoint가 없을 때는 말단노드(leaf)로만 동작함.
     * Wiget은 shadow를 가짐. shadow는 drag and drop으로 하위 widget이 생성될 위치를 점유하고 drop시 widget 생성을 트리거
     *
     *  ----------------------------------------------------------------------------------------------------------
     *  |                                                       widget shadow                                    |
     *  |  ---------------------------------------------------------------------------------------------------   |
     *  |  |                              Widget                                                             |   |
     *  |  | *(line-end-point)      ------------------------------------------------------------------------ |   |
     *  |  |                        |                     ConnectionPoint1                                 | |   |
     *  |  |                        |                                                 (line-start-point) * | |   |
     *  |  |                        ------------------------------------------------------------------------ |   |
     *  |  |                        ------------------------------------------------------------------------ |   |
     *  |  |                        |                     ConnectionPoint2                                 | |   |
     *  |  |                        |                                                 (line-start-point) * | |   |
     *  |  |                        ------------------------------------------------------------------------ |   |
     *  |  ---------------------------------------------------------------------------------------------------   |
     *  ----------------------------------------------------------------------------------------------------------
     *
     *  맴버 connectionPoint는 부모 widget에서 자신에게 이어지는 점이다
     *  맴버 points는 자신이 가진 자식 widget들에게 이어지는 점이다
     */
    function Widget(editor, optionParam, connectionPoint) {
        if (!connectionPoint && ((optionParam.x == null) || (optionParam.y == null)))
            throw 'need connectionPoint or (optionParam.x, optionParam.y)';

        const widget = this;
        this.editor = editor;
        this.connectionPoint = connectionPoint;
        this.option = $.extend({
            title: 'none title',
            x: null,
            y: null,
            id: guid(),
            onlyShadow: true,
            points: []
        }, (typeof optionParam) === 'object' ? optionParam : {});
        this.width = null;
        this.height = null;
        this.rendered = false;
        this.points = [];
        this.line = null;

        function calculatePosition(value) {
            if (!value)
                return value;
            return Math.round(value / WIDGET_GRID) * WIDGET_GRID;
        }

        widget.option.x = calculatePosition(widget.option.x);
        widget.option.y = calculatePosition(widget.option.y);

        this.x = widget.option.x;
        this.y = widget.option.y;

        function moveWidget(event, ui) {
            const preX = widget.x;
            const preY = widget.y;

            ui.position.left = calculatePosition(ui.position.left);
            ui.position.top = calculatePosition(ui.position.top);

            widget.rendered = false;
            widget.x = ui.position.left;
            widget.y = ui.position.top;

            if (widget.x === preX && widget.y === preY)
                return;

            widget.points.map(function (point) {
                point.rendered = false;
            });
            widget.render();
        }

        const $dom = createDom('ui-ivr-widget-shadow')
            .append(createDom('ui-ivr-widget-line-end-point'))
            // .css({left: widget.x, top: widget.y})
            .droppable({
                accept: '.-init-ivr-node',
                greedy: true,
                drop: function (event, ui) {
                    if (!widget.option.onlyShadow)
                        return;

                    if (widget.editor.option.link)
                        widget.editor.option.link.apply(window, [event, ui, widget.option.id, widget.option, widget]);
                }
            })
            .draggable({
                containment: widget.editor.dom,
                scroll: false,
                // handle: '.ui-ivr-widget-handle',
                drag: moveWidget,
                iframeFix: true,
                stop: function (event, ui) {
                    moveWidget(event, ui);

                    if (widget.editor.option.dragStop)
                        widget.editor.option.dragStop.apply(window, [event, ui, widget.option.id, widget.option, widget]);
                }
            });

        this.dom = $dom[0];

        widget.setContent(widget.option);
    }

    Widget.prototype.setContent = function (optionParam) {
        const widget = this;
        widget.option = $.extend({
            title: 'none title',
            onlyShadow: true,
            points: []
        }, (typeof optionParam) === 'object' ? optionParam : {});
        if (!widget.option.x) widget.option.x = widget.x;
        if (!widget.option.y) widget.option.y = widget.y;
        widget.x = widget.option.x;
        widget.y = widget.option.y;
        widget.rendered = false;

        const $dom = $(this.dom);

        for (let key in this.option)
            if (this.option.hasOwnProperty(key) && typeof this.option[key] !== 'object')
                $dom.attr('data-' + key, this.option[key]);

        if (widget.option.onlyShadow) {
            widget.points.map(function (point) {
                point.remove();
            });

            $dom.find('.ui-ivr-widget').remove();
        } else {
            let widgetDom = $dom.find('.ui-ivr-widget');
            let isWebVoice = optionParam.data.isWebVoice;
            if (widgetDom.length > 0) {
                widgetDom.find('.ui-ivr-widget-title').text(widget.option.title);
            } else {
                if (isWebVoice.contains('Y'))
                    widgetDom = createDom('ui-ivr-widget')
                        .append(createDom('ui-ivr-widget-title').text(widget.option.title))
                        .append(createDom('ui-ivr-widget-handle'))
                        .append(createDom('ui-ivr-widget-eye').click(function () {
                            if (widget.editor.option.eye)
                                widget.editor.option.eye.apply(window, [widget.option.id, widget.option, widget]);
                        }))
                        .append(createDom('ui-ivr-widget-setup').click(function () {
                            if (widget.editor.option.setup)
                                widget.editor.option.setup.apply(window, [widget.option.id, widget.option, widget]);
                        }))
                        .append(createDom('ui-ivr-widget-remove').click(function () {
                            confirm('정말 삭제하시겠습니까?').done(function () {
                                widget.remove();
                                widget.editor.render();
                                if (widget.editor.option.remove)
                                    widget.editor.option.remove.apply(window, [widget.option.id, widget.option, widget]);
                            });
                        }));
                else
                    widgetDom = createDom('ui-ivr-widget')
                        .append(createDom('ui-ivr-widget-title').text(widget.option.title))
                        .append(createDom('ui-ivr-widget-handle'))
                        .append(createDom('ui-ivr-widget-setup').click(function () {
                            if (widget.editor.option.setup)
                                widget.editor.option.setup.apply(window, [widget.option.id, widget.option, widget]);
                        }))
                        .append(createDom('ui-ivr-widget-remove').click(function () {
                            confirm('정말 삭제하시겠습니까?').done(function () {
                                widget.remove();
                                widget.editor.render();
                                if (widget.editor.option.remove)
                                    widget.editor.option.remove.apply(window, [widget.option.id, widget.option, widget]);
                            });
                        }));

                $dom.append(widgetDom);
            }

            if (widget.option.points) {
                const pointKeys = widget.option.points.reduce(function (result, point) {
                    result.push(point.button);
                    return result;
                }, []);

                let pointsContainer = $dom.find('.ui-ivr-widget-point-container');
                if (pointsContainer.length <= 0) {
                    pointsContainer = createDom('ui-ivr-widget-point-container');
                    $dom.find('.ui-ivr-widget').append(pointsContainer);
                }

                pointKeys.map(function (number) {
                    const prevPoint = widget.points.filter(function (point) {
                        return point.option.number === number;
                    })[0];

                    const optionPoint = widget.option.points.filter(function (e) {
                        return e.button === number;
                    })[0];

                    if (prevPoint) {
                        prevPoint.setContent({
                            id: optionPoint.id,
                            number: number,
                            title: optionPoint.name
                        });
                    } else {
                        const point = new ConnectionPoint(widget, {
                            id: optionPoint.id,
                            number: number,
                            title: optionPoint.name
                        });
                        widget.points.push(point);
                        pointsContainer.append(point.dom);
                    }
                });

                const deletingPointIndices = [];
                widget.points.map(function (point, i) {
                    if (pointKeys.indexOf(point.option.number) < 0) {
                        deletingPointIndices.unshift(i);
                        point.remove();
                    }
                });

                deletingPointIndices.map(function (i) {
                    widget.points.splice(i, 1);
                });
            } else {
                widgetDom.find('.ui-ivr-widget-point-container').remove();
            }
        }
    };

    Widget.prototype.render = function () {
        const widget = this;
        if (widget.rendered)
            return;

        if (widget.connectionPoint && !widget.connectionPoint.rendered)
            return widget.connectionPoint.render();

        if ((widget.x == null) || (widget.y == null)) {
            const offset = widget.editor.offset($(widget.connectionPoint.dom).find('.ui-ivr-connection-point-line-start-point'));
            if (offset.left === 0 && offset.top === 0)
                console.log('connectionPoint 렌더링이 완료되지 못한 상태로 widget 렌더링 시도', {widgetId: widget.option.id});

            widget.x = offset.left + 50; // TODO: check
            widget.y = offset.top - 10; // TODO: check
        }

        widget.width = $(widget.dom).outerWidth();
        widget.height = $(widget.dom).outerHeight();
        widget.rendered = true;
        $(widget.dom).css({left: widget.x, top: widget.y});
        // 브라우저 렌더링이 되기 위한 시간이 필요하다.

        console.log('rendered ' + widget.option.id);

        setTimeout(function () {
            widget.drawLine();
            widget.editor.setMaxSizeIfOverflowByWidget(widget);
            widget.points.map(function (point) {
                point.render();
            });
        }, 100);
    };

    Widget.prototype.drawLine = function () {
        const widget = this;
        if (!widget.connectionPoint)
            return;

        if (!widget.rendered)
            return widget.render();

        if (!widget.line) {
            widget.line = new Line({
                connectionPoint: widget.connectionPoint,
                widget: widget
            });
            $(widget.editor.dom).append(widget.line.dom);
        }

        const endPointOffset = widget.editor.offset($(widget.dom).find('.ui-ivr-widget-line-end-point'));
        const startPointOffset = widget.editor.offset($(widget.connectionPoint.dom).find('.ui-ivr-connection-point-line-start-point'));
        widget.line.setPoint(
            new Point(startPointOffset.left, startPointOffset.top),
            new Point(endPointOffset.left, endPointOffset.top)
        );
        widget.line.render();
    };

    Widget.prototype.remove = function () {
        const widget = this;
        widget.points.map(function (point) {
            point.remove();
        });

        if (widget.connectionPoint) {
            $(widget.dom).find('.ui-ivr-widget').remove();
            widget.option.onlyShadow = true;
        } else {
            if (widget.line) widget.line.remove();
            $(widget.dom).remove();
            delete this;
        }
    };

    /**
     * Widget에서 다음 Widget 연결을 위한 속성. 다음 Widget으로 연결되는 Line의 시작지점을 가르킨다.
     *
     * 맴버 widget은 자신을 포함하는 widget을 가르킨다.
     * 맴버 nextWidget은 자식(다음) widget을 가르킨다.
     */
    function ConnectionPoint(widget, optionParam) {
        const connectionPoint = this;
        this.widget = widget;
        this.option = $.extend({
            number: null,
            title: 'none connection point title'
        }, (typeof optionParam) === 'object' ? optionParam : {});
        this.rendered = false;
        this.x = null;
        this.y = null;
        this.width = null;
        this.height = null;

        const $dom = createDom('ui-ivr-connection-point')
            .append(createDom('ui-ivr-connection-point-title').text(connectionPoint.option.title))
            .append(createDom('ui-ivr-connection-point-number').text(connectionPoint.option.number))
            .append(createDom('ui-ivr-connection-point-line-start-point'));

        this.dom = $dom[0];
        this.nextWidget = widget.editor.addWidget({id: this.option.id}, this);
    }

    ConnectionPoint.prototype.setContent = function (optionParam) {
        const connectionPoint = this;
        const $dom = $(this.dom);
        this.option = $.extend({
            number: null,
            title: 'none connection point title'
        }, (typeof optionParam) === 'object' ? optionParam : {});
        $dom.find('.ui-ivr-connection-point-title').text(connectionPoint.option.title);
        $dom.find('.ui-ivr-connection-point-number').text(connectionPoint.option.number);
    };

    ConnectionPoint.prototype.render = function () {
        const connectionPoint = this;
        if (connectionPoint.rendered)
            return;

        if (!connectionPoint.widget.rendered)
            return connectionPoint.widget.render();

        const offset = connectionPoint.widget.editor.offset(connectionPoint.dom);
        if (offset.left === 0 && offset.top === 0)
            console.log('widget 렌더링이 완료되지 못한 상태로 connectionPoint 렌더링 시도', {
                widgetId: connectionPoint.widget.option.id,
                button: connectionPoint.option.number
            });

        connectionPoint.x = offset.left;
        connectionPoint.y = offset.top;
        connectionPoint.width = $(connectionPoint.dom).outerWidth();
        connectionPoint.height = $(connectionPoint.dom).outerHeight();

        connectionPoint.rendered = true;
        connectionPoint.nextWidget.rendered = false;
        connectionPoint.nextWidget.render();
    };

    ConnectionPoint.prototype.remove = function () {
        this.nextWidget.connectionPoint = null;
        this.nextWidget.remove();
        $(this.dom).remove();
        delete this;
    };

    function Line(option) {
        this.dom = createDom('ui-ivr-line');
        this.connectionPoint = option.connectionPoint;
        this.widget = option.widget;
        this.startPoint = option.startPoint;
        this.endPoint = option.endPoint;
    }

    Line.prototype.setPoint = function (startPoint, endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    };

    Line.prototype.render = function () {
        const length = Math.sqrt(
            Math.pow(Math.abs(this.startPoint.x - this.endPoint.x), 2)
            + Math.pow(Math.abs(this.startPoint.y - this.endPoint.y), 2)
        );
        const degree = Math.asin((this.endPoint.y - this.startPoint.y) / length) * 180 / Math.PI;
        $(this.dom).css({
            left: this.startPoint.x + 2,
            top: this.startPoint.y,
            width: length,
            transform: (this.endPoint.x < this.startPoint.x ? 'scaleX(-1)' : '') + ' rotate(' + degree + 'deg)',
            transformOrigin: '0 0'
        });
        // 라인은 widget 렌더링 이후 처리되기 때문에 렌더링 종료를 안 기다려도 된다.
    };

    Line.prototype.remove = function () {
        $(this.dom).remove();
        delete this;
    };

    function Point(x, y) {
        this.x = x;
        this.y = y;
    }

    $.fn.ivrEditor = function (option, params1, params2) {
        const _this = this[0];
        if (!_this.ivrEditor) {
            _this.ivrEditor = new IvrEditor(_this, option);
            return this;
        }

        switch (option) {
            case 'init': {
                let prevOption = _this.ivrEditor.option;
                if (_this.ivrEditor.dropEvent)
                    prevOption.droppable = $.extend(prevOption.droppable, {drop: _this.ivrEditor.dropEvent});

                delete _this.ivrEditor;
                $(_this).empty();
                _this.ivrEditor = new IvrEditor(_this, prevOption);
                return this;
            }
            case 'destroy': {
                delete _this.ivrEditor;
                $(_this).empty();
                return this;
            }
            case 'addWidget': {
                _this.ivrEditor.addWidget(params1);
                return this;
            }
            case 'updateWidget': {
                _this.ivrEditor.updateWidget(params1, params2);
                return this;
            }
            case 'getIvrEditor': {
                return _this.ivrEditor;
            }
            case 'getWidgets': {
                return _this.ivrEditor.widgets;
            }
            case 'getWidget': {
                return _this.ivrEditor.getWidget(params1);
            }
            case 'render': {
                return _this.ivrEditor.render();
            }
            default: {
                console.log(arguments);
            }
        }

        return this;
    };
})(jQuery);
function IpccAdminCommunicator() {
    this.socket = null;
    this.init();
}

IpccAdminCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
IpccAdminCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
IpccAdminCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
IpccAdminCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
IpccAdminCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};
IpccAdminCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
IpccAdminCommunicator.prototype.connect = function (url, companyId, userId, pbxName) {
    this.url = url;
    this.request = {companyId: companyId, userId: userId, pbxName: pbxName};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'secure': url.contains('https')}, {'reconnect': true, 'resource': 'socket.io'});
        this.socket.emit('climsg_join', {
            company_id: _this.request.companyId,
            userid: _this.request.userId,
            pbxname: _this.request.pbxName
        }).on('connect', function () {
            _this.parse("NODEJS|KIND:CONNECT");
        }).on('svcmsg', function (data) {
            _this.parse(data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
        }).on('disconnect', function () {
            _this.processor.NODESVC_STATUS("DISCONNECT");
        }).on('error', function () {
            console.error(arguments);
            _this.processor.NODESVC_STATUS("ERROR");
        }).on('end', function () {
            _this.processor.NODESVC_STATUS("END");
        }).on('close', function () {
            _this.processor.NODESVC_STATUS("CLOSE");
        });
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }

    return this;
};
IpccAdminCommunicator.prototype.disconnect = function () {
    try {
        this.socket.emit('climsg_bye', "Bye.");
    } catch (ignored) {
    }
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
IpccAdminCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.serverIp, this.request.companyId, this.request.userId]);
};
IpccAdminCommunicator.prototype.send = function (message) {
    if (!this.socket)
        return this.processor.NODESVC_STATUS("RELOADED");

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};
IpccAdminCommunicator.prototype.processor = {
    LOGIN: null,
    LOGOUT: null,
    SERVER_STATUS: null,
    ADMPEER: null,
    ADMLOGIN: null,
    ADMCALLEVENT: null,
    ADMHANGUPEVENT: null,
    MEMBERSTATUS: null,
    PDSMEMBERSTATUS: null,
    SPY_RES: null,
    ADMQUEUE: null,
    LOGIN_CNT: null,
    STATUS_CNT: null,
    PEERSTATUS: null,
    NODESVC_STATUS: function (message, kind, peer, data1, data2, data3, data4, data5, data6, data7, data8) {
    },
    BYE: null
};
IpccAdminCommunicator.prototype.parse = function (message) {
    this.log(false, message);

    const variables = message.split("|");
    if (variables == null || variables.length < 2)
        return;

    const event = variables[0];
    const o = {};
    for (let i = 1; i < variables.length; i++) {
        const args = variables[i].split(":");
        o[args[0]] = '';

        for (let j = 1; j < args.length; j++)
            o[args[0]] += (j === 1 ? '' : ':') + args[j];
    }
    const args = [message, o.KIND, o.PEER, o.DATA1, o.DATA2, o.DATA3, o.DATA4, o.DATA5, o.DATA6, o.DATA7];

    const processor = this.processor[event];
    if (processor)
        processor.apply(this, args);

    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }
};function IpccPdsCommunicator() {
    this.socket = null;
    this.init();
}

IpccPdsCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
IpccPdsCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
IpccPdsCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
IpccPdsCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
IpccPdsCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};
IpccPdsCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
IpccPdsCommunicator.prototype.connect = function (url, companyId, userId, password) {
    this.url = url;
    this.request = {companyId: companyId, userId: userId, password: password};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnect': true, 'resource': 'socket.io'});
        this.socket.emit('climsg_login', {
            company_id: _this.request.companyId,
            userid: _this.request.userId,
            passwd: _this.request.password,
            serverip: 'PBX_VIP',
            option: 'pds'
        }).on('connect', function () {
            _this.parse("NODEJS|KIND:CONNECT_OK");
        }).on('svcmsg', function (data) {
            _this.parse(data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
        }).on('disconnect', function () {
            _this.processor.NODESVC_STATUS("DISCONNECT");
        }).on('error', function () {
            console.error(arguments);
            _this.processor.NODESVC_STATUS("ERROR");
        }).on('end', function () {
            _this.processor.NODESVC_STATUS("END");
        }).on('close', function () {
            _this.processor.NODESVC_STATUS("CLOSE");
        });
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }
};
IpccPdsCommunicator.prototype.disconnect = function () {
    try {
        this.send("Bye.");
    } catch (ignored) {
    }
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
IpccPdsCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.companyId, this.request.userId, this.request.password]);
};
IpccPdsCommunicator.prototype.send = function (message) {
    if (!this.socket)
        return this.processor.NODESVC_STATUS("RELOADED");

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};
IpccPdsCommunicator.prototype.processor = {
    LOGIN: null,
    LOGOUT: null,
    SERVER_STATUS: null,
    ADMPEER: null,
    ADMLOGIN: null,
    ADMCALLEVENT: null,
    ADMHANGUPEVENT: null,
    MEMBERSTATUS: null,
    PDSMEMBERSTATUS: null,
    SPY_RES: null,
    ADMQUEUE: null,
    LOGIN_CNT: null,
    STATUS_CNT: null,
    PEERSTATUS: null,
    NODESVC_STATUS: function (message, kind, peer, data0, data1, data2, data3, data4, data5, data6, data7, data8) {
    },
    BYE: null
};
IpccPdsCommunicator.prototype.parse = function (message) {
    this.log(false, message);

    const variables = message.split("|");
    if (variables == null || variables.length < 2)
        return;

    const event = variables[0];
    const o = {};
    for (let i = 1; i < variables.length; i++) {
        const args = variables[i].split(":");
        o[args[0]] = '';

        for (let j = 1; j < args.length; j++)
            o[args[0]] += (j === 1 ? '' : ':') + args[j];
    }

    if (event === "USERINPUT") {
        message = variables[1];
    }

    const args = [message, o.KIND, o.DATA1, o.DATA2, o.DATA3, o.DATA4, o.DATA5, o.DATA6, o.DATA7, o.DATA8, o.DATA9, o.DATA10, o.DATA11, o.DATA12, o.DATA13, o.DATA14, o.DATA15, o.DATA16];

    const processor = this.processor[event];
    if (processor)
        processor.apply(this, args);

    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }
};
IpccPdsCommunicator.prototype.start = function (pdsGroupId) {
    this.send("CMD|PDS_START|" + pdsGroupId);
};
IpccPdsCommunicator.prototype.stop = function (pdsGroupId) {
    this.send("CMD|PDS_STOP|" + pdsGroupId);
};
IpccPdsCommunicator.prototype.delete = function (pdsGroupId) {
    this.send("CMD|PDS_DELETE|" + pdsGroupId);
};
IpccPdsCommunicator.prototype.setRid = function (executeId, pdsGroupId, value) {
    this.send("CMD|PDS_SETRID|" + executeId + "," + pdsGroupId + "," + value);
};
IpccPdsCommunicator.prototype.setSpeed = function (pdsGroupId, value) {
    this.send("CMD|PDS_SETSPEED|" + pdsGroupId + "," + value);
};
IpccPdsCommunicator.prototype.setTimeout = function (pdsGroupId, value) {
    this.send("CMD|PDS_SETTIMEOUT|" + pdsGroupId + "," + value);
};
function IpccCommunicator() {
    this.socket = null;
    this.init();
}

IpccCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
IpccCommunicator.prototype.init = function () {
    this.url = null;
    this.peer = null;
    this.request = null;
    this.status = {
        eventNumber: 0,
        receivedClientCommand: false,
        bMemberStatus: 0,
        cMemberStatus: 0,
        recordId: null,
        redirect: null,
        clickKey: null
    };
    this.events = {};
    this.logClear();

    return this;
};
IpccCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
IpccCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
IpccCommunicator.prototype.log = function (isSend, text) {
    console.log((isSend ? "C->S" : "S->C") + ':: ' + this.status.eventNumber++ + '. ' + text);
};
IpccCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
IpccCommunicator.prototype.connect = function (url, serverIp, companyId, userId, extension, password, userType, fromUi, multi_yn) {
    this.url = url;
    this.request = {serverIp: serverIp, companyId: companyId, userId: userId, extension: extension, password: password, userType: userType, option: "0", fromUi: fromUi, multi_yn: multi_yn};
    this.log(true, "Login: " + JSON.stringify(this.request));

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnect': true, 'resource': 'socket.io'});
        this.socket.emit('climsg_login', {
            company_id: _this.request.companyId,
            userid: _this.request.userId,
            exten: _this.request.extension,
            passwd: _this.request.password,
            pbxhost: _this.request.serverIp,
            usertype: _this.request.userType,
            option: _this.request.option,
            fromUi: _this.request.fromUi,
            multi_yn: _this.request.multi_yn
        }).on('connect', function () {
            _this.parse("NODEJS|KIND:CONNECT_OK");
        }).on('svcmsg', function (data) {
            _this.parse(data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
        }).on('svcloginerror', function (data) {
            console.log(data);
            if (!_this.events.svcloginerror)
                return;
            for (let i = 0; i < _this.events.svcloginerror.length; i++) {
                _this.events.svcloginerror[i].apply(_this, [data]);
            }
        }).on('disconnect', function () {
            _this.processor.NODESVC_STATUS("DISCONNECT");
        }).on('error', function () {
            console.error(arguments);
            _this.processor.NODESVC_STATUS("ERROR");
        }).on('end', function () {
            _this.processor.NODESVC_STATUS("END");
        }).on('close', function () {
            _this.processor.NODESVC_STATUS("CLOSE");
        });
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }
};
IpccCommunicator.prototype.disconnect = function () {
    try {
        this.send("Bye.");
    } catch (ignored) {
    }
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
IpccCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [this.url, this.request.serverIp, this.request.companyId, this.request.userId, this.request.extension, this.request.password, this.request.userType]);
};
IpccCommunicator.prototype.send = function (message) {
    if (!this.peer || !('phoneNumber' in this.peer))
        throw 'connect() 수행 필요';

    this.status.receivedClientCommand = message.indexOf("RECEIVE") >= 0;

    if (!this.socket)
        return this.processor.NODESVC_STATUS("RELOADED");

    this.log(true, message);
    this.socket.emit('climsg_command', message);
};
IpccCommunicator.prototype.processor = {
    LOGIN: function (message, kind, data1, data2, data3, data4, data5, data6, data7, data8) {
        if (kind !== "LOGIN_OK" && kind !== "LOGIN_ALREADY")
            return;

        this.peer = {
            phoneNumber: data1,
            phonePeer: data5,
            userName: data2,
            forwardWhen: data6,
            forwardNumber: data7,
            recordType: data8
        };

        this.status.memberStatus = data3;
        this.status.phoneStatus = data4;

        this.send("CMD|LOGIN_ACK");
    },
    PEER: null,
    MEMBERSTATUS: function (message, kind) {
        if (this.status.cMemberStatus !== "1")
            this.status.bMemberStatus = this.status.cMemberStatus;
        this.status.cMemberStatus = parseInt(kind);
        this.status.memberStatus = parseInt(kind);
    },
    CALLEVENT: function (message, kind, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10){
        this.status.clickKey = data10 ? data10.toLowerCase() : '';
    },
    HANGUPEVENT: function (message, kind, data1, data2, data3, data4, data5, data6, data7, data8) {
        this.send("CMD|HANGUP_ACK|" + data5 + ","
            + (data8 !== ""
                ? data8
                : data1.length === 3 && data2.length === 3 //내선끊은후 이전 상태콘트롤
                    ? this.status.bMemberStatus
                    : 'NORMAL'));
    },
    RECORDSTATUS: null,
    CALLBACKEVENT: null,
    MULTICHANNEL: null,
    FORWARDING: null,
    CALLSTATUS: null,
    DTMFREADEVENT: null,
    PDSMEMBERSTATUS: null,
    PDS_READY: null,
    PDS_STOP: null,
    PDS_START: null,
    PDS_DELETE: null,
    PDS_STAT: null,
    PDS_STATUS: null,
    REC_START: null,
    REC_STOP: null,
    SERVER_STATUS: null,
    NODESVC_STATUS: function (message, kind) {
    },
    MSG: null,
    USERINPUT: null,
    BYE: null
};
IpccCommunicator.prototype.parse = function (message) {
    this.log(false, message);

    const variables = message.split("|");
    if (variables == null || variables.length < 2)
        return;

    const event = variables[0];
    const o = {};
    for (let i = 1; i < variables.length; i++) {
        const args = variables[i].split(":");
        o[args[0]] = '';

        for (let j = 1; j < args.length; j++)
            o[args[0]] += (j === 1 ? '' : ':') + args[j];
    }

    if (event === "USERINPUT") {
        message = variables[1];
    }

    const args = [message, o.KIND, o.DATA1, o.DATA2, o.DATA3, o.DATA4, o.DATA5, o.DATA6, o.DATA7, o.DATA8, o.DATA9, o.DATA10, o.DATA11, o.DATA12, o.DATA13, o.DATA14, o.DATA15, o.DATA16];

    const processor = this.processor[event];
    if (processor)
        processor.apply(this, args);

    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, args);
        }
    }
};
IpccCommunicator.prototype.setMemberStatus = function (status) {
    this.send("CMD|MEMBERSTATUS|" + status + "," + this.peer.phoneNumber + "," + this.status.memberStatus);
};
IpccCommunicator.prototype.setPdsStatus = function (status) {
    this.send("CMD|PDSMEMBERSTATUS|" + status + "," + this.peer.phoneNumber);
};
IpccCommunicator.prototype.clickDial = function (cid, number) {
    this.send("CMD|CLICKDIAL|" + cid + "," + number + ",oubbound");
};
IpccCommunicator.prototype.receiveCall = function () {
    this.send("CMD|RECEIVE|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.hangUp = function () {
    this.send("CMD|HANGUP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.pickUp = function () {
    this.send("CMD|PICKUP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.attended = function (extension) {
    this.send("CMD|ATTENDED|" + extension);
};
IpccCommunicator.prototype.attendedHangUp = function () {
    this.send("CMD|ATTENDEDHANGUP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.attendedToOut = function (phoneNumber) {
    this.send("CMD|ATTENDED_OUT|" + phoneNumber);
};
IpccCommunicator.prototype.redirect = function (extension) {
    this.send("CMD|REDIRECT|" + extension);
    this.status.redirect = this.status.recordId;
};
IpccCommunicator.prototype.redirectToOut = function (phoneNumber) {
    this.send("CMD|REDIRECT_OUT|" + phoneNumber.replace(/(\()|(\))|(-)/gi, ""));
    this.status.recordId = null;
};
IpccCommunicator.prototype.redirectHunt = function (phoneNumber) {
    this.send("CMD|REDIRECT_HUNT|" + phoneNumber);
    this.status.recordId = null;
};
IpccCommunicator.prototype.forward = function (forwarding) {
    this.send("CMD|FORWARDING|" + this.peer.phoneNumber + "," + forwarding + "," + this.peer.forwardWhen);
};
IpccCommunicator.prototype.getLastEvent = function () {
    this.send("CMD|GET_LASTEVENT|callevent");
};
IpccCommunicator.prototype.sendMessage = function (peer, message) {
    this.send("CMD|MSG|" + peer + "|" + encodeURI(message));
};
IpccCommunicator.prototype.eavesdrop = function (extension) {
    this.send("CMD|SPY|" + this.peer.phonePeer + "," + extension);
};
IpccCommunicator.prototype.startHolding = function () {
    this.send("CMD|HOLD_START|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.stopHolding = function () {
    this.send("CMD|HOLD_STOP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.startMultiCall = function (rid, extension, number) {
    this.send("CMD|MULTIDIAL_START|" + this.peer.phoneNumber + "," + rid + "," + extension + "," + number + ",M");
};
IpccCommunicator.prototype.stopMultiCall = function () {
    this.send("CMD|MULTIDIAL_END|" + this.peer.phoneNumber);
};
IpccCommunicator.prototype.startRecording = function () {
    this.send("CMD|REC_START|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.stopRecording = function () {
    this.send("CMD|REC_STOP|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.startListeningRecordedWithCaller = function (fileId, isMonitor /*TODO: 용도 확인*/) {
    this.send("CMD|RECORD_PLAY_START|" + fileId + "," + isMonitor);
};
IpccCommunicator.prototype.stopListeningRecordedWithCaller = function () {
    this.send("CMD|RECORD_PLAY_END|" + this.peer.phoneNumber);
};
// C -> S 전화거절
IpccCommunicator.prototype.reject = function () {
    this.send("CMD|REJECT|" + this.peer.phonePeer);
};
IpccCommunicator.prototype.connectArs = function (arsNumber) {
    this.send("CMD|ARS_CONNECT|" + arsNumber);
};
IpccCommunicator.prototype.startDTMF = function () {
    this.send("CMD|DTMFREAD|Y");
};
IpccCommunicator.prototype.stopDTMF = function () {
    this.send("CMD|DTMFREAD|N");
};
IpccCommunicator.prototype.arsConnect = function (dtmfType, memberNo) {
    const typeCode = (dtmfType === 'PASSWORD') ? '0000' : '1111';
    if (memberNo === undefined || memberNo === '') {
        this.send("CMD|ARS_CONNECT|" + typeCode + this.peer.phonePeer);
    } else {
        this.send("CMD|ARS_CONNECT|" + typeCode + this.peer.phonePeer + memberNo);
    }
};
IpccCommunicator.prototype.clickByCampaign = function (cid, number, type, typeId, customId) {
    this.send("CMD|CLICKBYCAMPAIGN|" + cid + "," + number + "," + (type || 'TAB') + "," + typeId + "," + (this.request.userId + '_' + moment().format('YYYYMMDDHHmmss') + "," + (customId || '')));
};
IpccCommunicator.prototype.protectArs = function (soundSeq) {
    this.send("CMD|PROTECT_ARS|" + soundSeq);
};
IpccCommunicator.prototype.applyCms = function (bank, account, holder, transferringDate, transferringAmount) {
    this.send("CMD|CMS|" + bank.replace(/[|]/gi, "") + "|"
        + account.replace(/[|]/gi, "") + "|"
        + holder.replace(/[|]/gi, "") + "|"
        + transferringDate.replace(/[|]/gi, "") + "|"
        + transferringAmount.replace(/[|]/gi, "") + "|"
    );
};
IpccCommunicator.prototype.voc = function (vocGroup, arsResearch) {
    this.send("CMD|VOC|" + vocGroup + arsResearch);
};function TalkCommunicator() {
    this.socket = null;
    this.init();
}

TalkCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
TalkCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
TalkCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
TalkCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
TalkCommunicator.prototype.log = function (isSend, command, body) {
    console.log((isSend ? "C->S" : "S->C") + ':: [' + (this.status.eventNumber++) + '.' + command + '] ' + (typeof body === 'object' ? JSON.stringify(body) : body));
};
TalkCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
TalkCommunicator.prototype.connect = function (url, companyId, groupCode, groupTreeName, groupLevel, userid, username, authtype, usertype) {
    this.url = url;
    this.request = {
        companyId: companyId,
        groupCode: groupCode,
        groupTreeName: groupTreeName,
        groupLevel: groupLevel,
        userid: userid,
        username: username,
        authtype: authtype,
        usertype: usertype
    };

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnect': true, 'resource': 'socket.io'});
        this.socket.emit('cli_join', {
            company_id: _this.request.companyId,
            group_code: _this.request.groupCode,
            group_tree_name: _this.request.groupTreeName,
            group_level: _this.request.groupLevel,
            userid: _this.request.userid,
            username: _this.request.username,
            authtype: _this.request.authtype,
            usertype: _this.request.usertype
        }).on('connect', function () {
            _this.log(false, 'connect', arguments);
        }).on('svc_login', function (data) {
            _this.log(false, 'svc_login', data);
            _this.process('svc_login', data);
        }).on('svc_logout', function (data) {
            _this.log(false, 'svc_logout', data);
            _this.process('svc_logout', data);
        }).on('svc_msg', function (data) {
            _this.log(false, 'svc_msg', data);
            _this.process('svc_msg', data);
        }).on('svc_control', function (data) {
            _this.log(false, 'svc_control', data);
            _this.process('svc_control', data);
        }).on('svc_end', function (data) {
            _this.log(false, 'svc_end', data);
            _this.process('svc_end', data);
        }).on('svcmsg_ping', function () {
            _this.socket.emit('climsg_pong');
        }).on('disconnect', function () {
            _this.log(false, 'disconnect', arguments);
        }).on('error', function () {
            _this.log(false, 'error', arguments);
        }).on('end', function () {
            _this.log(false, 'end', arguments);
        }).on('close', function () {
            _this.log(false, 'close', arguments);
        });
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }
};
TalkCommunicator.prototype.disconnect = function () {
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
TalkCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [
        this.url,
        this.request.companyId,
        this.request.groupCode,
        this.request.groupTreeName,
        this.request.groupLevel,
        this.request.userid,
        this.request.username,
        this.request.authtype,
        this.request.usertype
    ]);
};
TalkCommunicator.prototype.process = function (event, data) {
    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, [data, event]);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, [data, event]);
        }
    }

    return this;
};
TalkCommunicator.prototype.sendMessage = function (roomId, senderKey, userKey, contents) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        sender_key: senderKey,
        send_receive: "S",
        user_key: userKey,
        etc_data: "",
        contents: contents
    });
};
TalkCommunicator.prototype.assignUnassignedRoomToMe = function (roomId, senderKey, userKey) {
    this.socket.emit('cli_control', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        sender_key: senderKey,
        send_receive: "SZ",
        user_key: userKey,
        etc_data: "",
        contents: ""
    });
};
TalkCommunicator.prototype.assignAssignedRoomToMe = function (roomId, senderKey, userKey) {
    this.socket.emit('cli_control', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        sender_key: senderKey,
        send_receive: "SG",
        user_key: userKey,
        etc_data: "",
        contents: ""
    });
};
TalkCommunicator.prototype.deleteRoom = function (roomId, senderKey, userKey) {
    this.socket.emit('cli_end', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        sender_key: senderKey,
        send_receive: "SE",
        user_key: userKey,
        etc_data: "",
        contents: ""
    });
};function MessengerCommunicator() {
    this.socket = null;
    this.init();
}

MessengerCommunicator.prototype.CONSTANTS = {
    EVENT_WHOLE: '*'
};
MessengerCommunicator.prototype.init = function () {
    this.url = null;
    this.request = null;
    this.status = {
        eventNumber: 0
    };
    this.events = {};
    this.logClear();

    return this;
};
MessengerCommunicator.prototype.on = function (type, processor) {
    if (!processor)
        throw 'invalid argument';

    if (!this.events[type])
        this.events[type] = [];

    this.events[type].push(processor);

    return this;
};
MessengerCommunicator.prototype.off = function (type, processor) {
    if (!processor)
        return this.events[type] = [];

    if (!this.events[type])
        this.events[type] = [];

    const index = this.events[type].indexOf(processor);
    if (index >= 0)
        this.events[type].splice(index, 1);

    return this;
};
MessengerCommunicator.prototype.log = function (isSend, command, body) {
    console.log((isSend ? "C->S" : "S->C") + ':: [' + (this.status.eventNumber++) + '.' + command + '] ' + (typeof body === 'object' ? JSON.stringify(body) : body));
};
MessengerCommunicator.prototype.logClear = function () {
    this.status.eventNumber = 0;
};
MessengerCommunicator.prototype.connect = function (url, companyId, userid, username, passwd) {
    this.url = url;
    this.request = {
        companyId: companyId,
        userid: userid,
        username: username,
        passwd: passwd
    };

    const _this = this;
    try {
        this.socket = io.connect(url, {'reconnect': true, 'resource': 'socket.io'});
    } catch (error) {
        console.error(error);
        setTimeout(function () {
            _this.recovery();
        }, 2000);
    }

    const serverCommands = ['svc_login', 'svc_logout', 'svc_msg', 'svc_join_msg', 'svc_invite_room', 'svc_leave_room', 'svc_read_confirm', 'svc_memo_send'];
    const uncheckedServerCommands = ['connect', 'disconnect', 'error', 'end', 'close'];

    const o = this.socket.emit('chatt_login', {
        company_id: _this.request.companyId,
        userid: _this.request.userid,
        username: _this.request.username,
        passwd: _this.request.passwd
    }).on('svcmsg_ping', function () {
        _this.socket.emit('climsg_pong');
    });

    serverCommands.map(function (command) {
        o.on(command, function (data) {
            _this.process(command, data);
        });
    });
    uncheckedServerCommands.map(function (command) {
        o.on(command, function () {
            _this.log(false, command, arguments);
        });
    });
};
MessengerCommunicator.prototype.disconnect = function () {
    try {
        this.socket.disconnect();
    } catch (ignored) {
    }

    this.socket = null;
    this.init();
};
MessengerCommunicator.prototype.recovery = function () {
    if (!this.url || !this.request)
        throw 'connect() 수행 필요';
    this.connect.apply(this, [
        this.url,
        this.request.companyId,
        this.request.userid,
        this.request.username,
        this.request.passwd
    ]);
};
MessengerCommunicator.prototype.process = function (event, data) {
    this.log(false, event, data);

    if (this.events[event]) {
        const events = this.events[event];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, [data, event]);
        }
    }

    if (this.events[this.CONSTANTS.EVENT_WHOLE]) {
        const events = this.events[this.CONSTANTS.EVENT_WHOLE];
        for (let i = 0; i < events.length; i++) {
            events[i].apply(this, [data, event]);
        }
    }

    return this;
};
MessengerCommunicator.prototype.sendMessage = function (roomId, contents) {
    this.socket.emit('cli_msg', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        send_receive: "S",
        etc_data: "",
        contents: contents
    });
};
MessengerCommunicator.prototype.confirmMessage = function (roomId, messageId) {
    this.socket.emit('cli_read_confirm', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid,
        last_read_message_id: messageId
    });
};
MessengerCommunicator.prototype.join = function (roomId) {
    this.socket.emit('cli_join_room', {
        company_id: this.request.companyId,
        room_id: roomId,
        userid: this.request.userid
    });
};
MessengerCommunicator.prototype.leave = function (roomId) {
    this.socket.emit('cli_leave_room', {
        company_id: this.request.companyId,
        room_id: roomId,
        leave_userid: this.request.userid
    });
};
MessengerCommunicator.prototype.invite = function (roomId, userId, userName) {
    this.socket.emit('cli_invite_room', {
        company_id: this.request.companyId,
        room_id: roomId,
        invite_userid: this.request.userid,
        invited_username: ((userName instanceof Array) ? userName.join(',') : userName),
        invited_userid: ((userId instanceof Array) ? userId.join(',') : userId)
    });
};
MessengerCommunicator.prototype.changeRoomName = function (roomId, roomName) {
    this.socket.emit('cli_roomname_change', {
        company_id: this.request.companyId,
        room_id: roomId,
        change_room_name: roomName,
    });
};
MessengerCommunicator.prototype.sendMemo = function (userId, messageId) {
    this.socket.emit('cli_memo_send', {
        company_id: this.request.companyId,
        message_id: messageId,
        send_userid: this.request.userid,
        receive_userid: userId
    });
};
MessengerCommunicator.prototype.readMemo = function (messageId, sendUserid) {
    this.socket.emit('cli_memo_read', {
        company_id: this.request.companyId,
        read_userid: this.request.userid,
        message_id: messageId,
        send_userid: sendUserid,
    });
};
const restUtils = (function ($) {
    function call(url, data, type, urlParam, noneBlockUi) {
        if (!noneBlockUi) $.blockUIFixed();

        return $.ajax({
            type: type,
            url: $.addQueryString(urlParam ? $.addQueryString(url, data) : url, {____t: new Date().getTime()}),
            data: urlParam ? null : typeof data === "object" ? JSON.stringify(data) : data,
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).always(function () {
            if (!noneBlockUi) $.unblockUI();
        });
    }

    return {
        get: function (url, data, noneBlockUi) {
            return call(url, data, 'GET', true, noneBlockUi);
        },
        post: function (url, data, noneBlockUi) {
            return call(url, data, 'POST', false, noneBlockUi);
        },
        put: function (url, data, noneBlockUi) {
            return call(url, data, 'PUT', false, noneBlockUi);
        },
        patch: function (url, data, noneBlockUi) {
            return call(url, data, 'PATCH', false, noneBlockUi);
        },
        delete: function (url, data, noneBlockUi) {
            return call(url, data, 'DELETE', true, noneBlockUi);
        }
    }
})(jQuery);

function submitJsonData(form) {
    const _this = form;
    const $form = $(form);

    const deferred = $.Deferred();

    $form.asJsonData().done(function (data) {
        const before = $form.data('before');
        if (before) {
            let bReturn = window[before].apply(this, [data]);
            if (typeof bReturn !== 'undefined' && !!!bReturn) return false;
        }

        restUtils[($form.data('method') || _this.method || 'get').toLowerCase()].apply(_this, [_this.action, data, $form.data('noneBlockUi')]).done(function (data) {
            if (!disableLog) console.log(data);
            submitDone(_this, data, $form.data('done'));

            deferred.resolve(data, $form);
        }).fail(function (e) {
            try {
                const data = JSON.parse(e.responseText);
                if (!disableLog) console.log(data);
                submitDone(_this, data);
            } catch (exception) {
                if (e.status === 404)
                    return alert("Failed to request processing. Please retry it.");
                alert("error[" + e.status + "]: " + e.statusText);
            }

            deferred.reject(e);
        });
    }).fail(function (error) {
        alert(error);
    });

    return deferred.promise();
}

function readFile(file) {
    const deferred = jQuery.Deferred();
    if (!file) {
        deferred.reject("'file' is invalid.");
        return deferred;
    }

    const reader = new FileReader();
    reader.addEventListener("load", function () {
        deferred.resolve({
            data: reader.result,
            fileName: file.name
        });
    }, false);
    reader.addEventListener("error", function (error) {
        deferred.reject(error);
    }, false);
    reader.readAsDataURL(file);

    return deferred;
}

function extractFilename(path) {
    const lastSlash = path.lastIndexOf('/');
    const lastReverseSlash = path.lastIndexOf('\\');
    return path.substr(Math.max(lastSlash, lastReverseSlash) + 1);
}

// 출처: http://stackoverflow.com/questions/2901102/how-to-print-number-with-commas-as-thousands-separators-in-javascript
function numberWithCommas(n, floatCount) {
    const parts = n.toString().split(".");

    const float = parts[1]
        ? floatCount != null && floatCount >= 0 ? parts[1].toString().substring(0, floatCount) : parts[1]
        : null;

    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (float ? "." + float : "");
}

function nullSpace(o, postfix) {
    return o != null ? o + (postfix != null ? postfix : '') : ' ';
}

function removeDuplicatedElement(names) {
    const uniqueNames = [];
    $.each(names, function (i, el) {
        if ($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
    });
    return uniqueNames;
}

/**
 * 숫자 포맷팅. 실수 부분을 컷팅하기 위한 용도
 *
 * @param number 입력숫자
 * @param count 보여질 숫자 갯수
 * @param wholeInteger count를 초과했을 때, 나머지 정수를 보여줄지 여부. false이면 'count' 숫자 외엔 0으로 채움
 */
function numberOnlyCount(number, count, wholeInteger) {
    const parts = number.toString().split(".");

    const integerCount = parts[0].length >= count ? count : parts[0].length;
    const integer = wholeInteger || parts[0].length <= count
        ? parts[0]
        : parseInt(parts[0]) - (parseInt(parts[0]) % Math.pow(10, parts[0].length - count));

    const float = !parts[1] || integerCount >= count || count - integerCount <= 0
        ? ''
        : '.' + pad(Math.floor(parseFloat('0.' + parts[1]) * Math.pow(10, count - integerCount)), count - integerCount);

    return integer.toString() + float.toString();
}

function numberOnlyFloatCount(number, count, wholeInteger) {
    const parts = number.toString().split(".");
    return parts[0] + '.' + pad(Math.floor(parseFloat('0.' + (parts[1] || 0)) * Math.pow(10, count)), count);
}

function pad(num, size) {
    let s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}

//src: http://stackoverflow.com/questions/11381673/detecting-a-mobile-browser
const userAgent = (function () {
    const agent = navigator.userAgent || navigator.vendor || window.opera;

    function condition1() {
        return /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series([46])0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(agent);
    }

    function condition2() {
        return /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br([ev])w|bumb|bw-([nu])|c55\/|capi|ccwa|cdm-|cell|chtm|cldc|cmd-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc-s|devi|dica|dmob|do([cp])o|ds(12|-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly([-_])|g1 u|g560|gene|gf-5|g-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd-([mpt])|hei-|hi(pt|ta)|hp( i|ip)|hs-c|ht(c([- _agpst])|tp)|hu(aw|tc)|i-(20|go|ma)|i230|iac([ \-\/])|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja([tv])a|jbro|jemu|jigs|kddi|keji|kgt([ \/])|klon|kpt |kwc-|kyo([ck])|le(no|xi)|lg( g|\/([klu])|50|54|-[a-w])|libw|lynx|m1-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t([- ov])|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30([02])|n50([025])|n7(0([01])|10)|ne(([cm])-|on|tf|wf|wg|wt)|nok([6i])|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan([adt])|pdxg|pg(13|-([1-8]|c))|phil|pire|pl(ay|uc)|pn-2|po(ck|rt|se)|prox|psio|pt-g|qa-a|qc(07|12|21|32|60|-[2-7]|i-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h-|oo|p-)|sdk\/|se(c([-01])|47|mc|nd|ri)|sgh-|shar|sie([-m])|sk-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h-|v-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl-|tdg-|tel([im])|tim-|t-mo|to(pl|sh)|ts(70|m-|m3|m5)|tx-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c([- ])|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas-|your|zeto|zte-/i.test(agent.substr(0, 4));
    }

    function condition3() {
        return /android|ipad|playbook|silk/i.test(agent);
    }

    return {
        isMobile: function () {
            return condition1() || condition2();
        },
        isMobileOrTable: function () {
            return this.isMobile() || condition3()
        },
        getInternetExplorerVersion: function () {
            let rv = -1;
            if (navigator.appName === 'Microsoft Internet Explorer') {
                const ua = navigator.userAgent;
                const re = new RegExp("MSIE ([0-9]+[\.0-9]*)");
                if (re.exec(ua) != null)
                    rv = parseFloat(RegExp.$1);
            } else if (navigator.appName === 'Netscape') {
                const ua = navigator.userAgent;
                const re = new RegExp("Trident/.*rv:([0-9]+[\.0-9]*)");
                if (re.exec(ua) != null)
                    rv = parseFloat(RegExp.$1);
            }
            return rv;
        }
    };
})();

function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

/**
 * onkeydown="return stopEnterPropagation(event);"
 */
function stopEnterPropagation(event) {
    if (event.keyCode !== 13) return true;
    event.stopPropagation();
    return false;
}

function viewImage(imageUrl) {
    window.open('', '').document.write('<html lang="ko"><body onclick="self.close();"><img title="클릭하시면 창이 닫힙니다." src="' + imageUrl + '"</body></html>');
}

function reload() {
    location.reload();
}

function refreshPageWithoutParameters() {
    location.href = location.pathname;
}

function keys(o) {
    const result = [];

    for (let key in o)
        if (o.hasOwnProperty(key))
            result.push(key);

    return result;
}

function values(o) {
    const result = [];

    for (let key in o)
        if (o.hasOwnProperty(key))
            result.push(o[key]);

    return result;
}

function printErrorLog() {
    console.error(arguments);
}

function htmlQuote(text) {
    return text.replace(/"/gi, "&quot;")
        .replace(/'/gi, "&#39;")
        .replace(/</gi, "&lt;");
}

function timestampToDateTime(timestamp) {
    const date = new Date(timestamp);

    return date.getFullYear() + '-' + pad(date.getMonth() + 1, 2) + '-' + pad(date.getDate(), 2) + ' ' + pad(date.getHours(), 2) + ':' + pad(date.getMinutes(), 2) + ':' + pad(date.getSeconds(), 2);
}const restSelf = {
    get: function (url, data, failFunc, noneBlockUi) {
        return restUtils.get((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc : failFunction());
    },
    post: function (url, data, failFunc, noneBlockUi) {
        return restUtils.post((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc : failFunction());
    },
    put: function (url, data, failFunc, noneBlockUi) {
        return restUtils.put((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc : failFunction());
    },
    patch: function (url, data, failFunc, noneBlockUi) {
        return restUtils.patch((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc : failFunction());
    },
    delete: function (url, data, failFunc, noneBlockUi) {
        return restUtils.delete((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc : failFunction());
    }
};

function receiveHtml(url, data, failFunc, noneBlockUi) {
    function failFunction(defaultMessage) {
        return alert(defaultMessage ? defaultMessage : '처리 실패');
    }

    if (!noneBlockUi) $.blockUIFixed();
    return $.ajax({
        type: 'get',
        dataType: 'html',
        url: $.addQueryString($.addQueryString(url, data), {____t: new Date().getTime()})
    }).fail(function (e) {
        failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc.apply(null, [e]) : failFunction()
    }).always(function () {
        if (!noneBlockUi) $.unblockUI();
    });
}

function getDefaultReason(response) {
    if (response.fieldErrors && response.fieldErrors.length > 0)
        return response.fieldErrors[0].defaultMessage;

    if (response.reason != null)
        return response.reason;

    return "";
}

function failFunction(defaultMessage) {
    return function (response) {
        if (response.responseText) {
            try {
                return alert(getDefaultReason(JSON.parse(response.responseText)));
            } catch (e) {
                return console.error(e);
            }
        }

        alert(defaultMessage ? defaultMessage : '처리 실패');
    }
}

function submitDone(form, response, done) {
    if (response.result === "success") {
        if (done) eval(done).apply(form, [form, response]);
        return;
    }

    const reason = getDefaultReason(response);
    return alert(reason == null ? "처리에 실패하였습니다." : reason);
}

function popupWindow(url, popupId, width, height) {
    window.open((contextPath || '') + url, popupId || '_blank', "menubar=no,status=no,titlebar=no,scrollbars=yes,resizable=yes,width=" + (width || 1000) + ",height=" + (height || 700));
}

function popupReceivedHtml(url, id, classes) {
    if (!id) throw 'id is mandatory';

    return receiveHtml(contextPath + url).done(function (html) {
        $('#' + id).remove();

        const mixedNodes = $.parseHTML(html, null, true);

        const modal = (function () {
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is('.ui.modal'))
                    return node;
            }
            throw 'cannot find modal element';
        })();
        modal.attr('id', id)
            .attr('class', (modal.attr('class') || '') + ' ' + classes)
            .click(function (event) {
                event.stopPropagation();
            })
            .appendTo($('body'))
            .bindHelpers()
            .modalShow();

        (function () {
            const scripts = [];
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is('script'))
                    scripts.push(node);
            }
            return scripts;
        })().map(function (script) {
            eval('(function() { return function(modal) {' + script.text() + '} })()').apply(window, [modal]);
        });
    });
}

function linkCheck(link) {
    window.open(link.split("\/\/")[0].toLowerCase().indexOf('http') < 0 ? 'http://' + link : link);
}

function popupDraggableModalFromReceivedHtml(url, id, classes) {
    if (!id) throw 'id is mandatory';

    return receiveHtml(contextPath + url).done(function (html) {
        $('#' + id).remove();

        const mixedNodes = $.parseHTML(html, null, true);

        const modal = (function () {
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is('.ui.modal'))
                    return node;
            }
            throw 'cannot find modal element';
        })();
        modal.attr('id', id)
            .attr('class', (modal.attr('class') || '') + ' ' + classes)
            .click(function (event) {
                event.stopPropagation();
            })
            .appendTo($('body'))
            .bindHelpers()
            .dragModalShow();

        (function () {
            const scripts = [];
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is('script'))
                    scripts.push(node);
            }
            return scripts;
        })().map(function (script) {
            eval('(function() { return function(modal) {' + script.text() + '} })()').apply(window, [modal]);
        });
    });
}

/**
 * @param url 호출할 url
 * @param targetSelector 교체대상
 * @param uiSelector 받아온 html root element 중에 교체할 element. 만약 null이면 targetSelector 와 동일하게 간주된다.
 */
function replaceReceivedHtmlInSilence(url, targetSelector, uiSelector) {
    return replaceReceivedHtml(url, targetSelector, uiSelector, function () {
    }, true);
}

function replaceReceivedHtml(url, targetSelector, uiSelector, failFunc, noneBlockUi) {
    uiSelector = uiSelector || targetSelector;

    return receiveHtml(contextPath + url, null, failFunc, noneBlockUi).done(function (html) {
        const target = $(targetSelector);

        const mixedNodes = $.parseHTML(html, null, true);

        const ui = (function () {
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is(uiSelector))
                    return node;
            }
            throw 'all elements aren\'t matched with uiSelector: ' + uiSelector;
        })();
        ui.insertAfter(target).bindHelpers();
        target.remove();

        (function () {
            const scripts = [];
            for (let i = 0; i < mixedNodes.length; i++) {
                const node = $(mixedNodes[i]);
                if (node.is('script'))
                    scripts.push(node);
            }
            return scripts;
        })().map(function (script) {
            eval('(function() { return function(ui) {' + script.text() + '} })()').apply(window, [ui]);
        });
    });
}

/**
 * @param func param: groupCode(string), groupNames(string array), groupNameElement(dom)
 */
function popupSelectOrganizationModal(func) {
    const id = 'modal-select-organization';
    popupReceivedHtml('/admin/user/organization/modal-select-organization', id, 'tiny').done(function () {
        const modal = $('#' + id);
        const headers = modal.find('.header');
        headers.click(function (e) {
            e.stopPropagation();
            headers.removeClass('select');
            $(this).addClass('select');
        });

        modal.find('.-button-submit').click(function () {
            const header = headers.filter('.select:first');
            if (!header.length)
                return;

            modal.modalHide();

            const groupCode = header.attr('data-group-code');
            const groupNames = header.attr('data-group-names').split('\u0001');

            func.apply(null, [groupCode, groupNames, groupNameElement(groupNames)]);
        });
        modal.find('.-button-search').click(function () {
            const keyword = modal.find('.-input-keyword').val();
            if (!keyword)
                return;

            headers.removeClass('highlight');
            headers.filter(function () {
                const name = $(this).attr('data-group-name');
                return name && name.indexOf(keyword) >= 0;
            }).addClass('highlight');
        });
    });
}

/**
 * @param names string array
 */
function groupNameElement(names) {
    const element = $('<div/>');

    for (let i = 0; i < names.length; i++) {
        element.append($('<span/>', {class: 'section', text: names[i]}));
        if (i < names.length - 1)
            element.append($('<i/>', {class: "right angle icon divider"}));
    }

    return element;
}

$.fn.modalShow = function () {
    $(this).modal({
        // context: '.tab-content.active',
        // dimmerSettings: {useFlex: true},
        allowMultiple: true,
        // observeChanges: true,
        // blurring: true,
        duration: 350,
        closable: false
    })
        .modal('show')
        .modal('refresh')
        .modal('cache sizes')
        .show();
    // $(document).popup('hide all');
};

$.fn.dragModalShow = function (container) {
    function moveModalToTop() {
        $this.appendTo(container || 'body:first');
    }

    function hideModal() {
        $this.hide();
    }

    const $this = $(this).show();
    moveModalToTop();

    setTimeout(function () {
        $this.css({
            'overflow-y': 'auto',
            position: "absolute",
            left: ($(window).width() / 2) + $(document).scrollLeft() - ($this.outerWidth() / 2),
            top: ($(window).height() / 2) + $(document).scrollTop() - ($this.outerHeight() / 2),
            maxHeight: "80%"
        }).draggable({
            iframeFix: true,
            containment: 'body',
            handle: '[class="header"],[class="header ui-draggable-handle"]'
        });
    }, 100);

    $this.find('[class="header"]')
        .off('click', moveModalToTop)
        .on('click', moveModalToTop);

    $this.find('.close.icon')
        .off('click', hideModal)
        .on('click', hideModal);

    $this.find('.modal-close')
        .off('click', hideModal)
        .on('click', hideModal);

    return $this;
};

$.fn.modalHide = function () {
    $(this).modal('hide')
        .hide();
    // $(document).popup('hide all');
};

$.fn.multiSearch = function () {
    $(this).multi({
        enable_search: false
    });
};

function getEntityId(entityName, fieldName) {
    return $('[data-entity]').filter(function () {
        return $(this).attr('data-entity') === entityName;
    }).find('.active').attr('data-' + (fieldName || 'id'));
}

function uploadFile(file, progressBar) {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return $.ajax({
        url: contextPath + '/api/file',
        data: formData,
        type: 'post',
        contentType: false,
        processData: false,
        xhr: function () {
            const xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
                $(progressBar).val(e.loaded * 100 / e.total);
            };
            return xhr;
        },
        success: function () {
            $(progressBar).val(100);
        }
    });
}

function uploadFileExcel(file, progressBar) {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return $.ajax({
        url: contextPath + '/api/file/excel',
        data: formData,
        type: 'post',
        contentType: false,
        processData: false,
        xhr: function () {
            const xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
                $(progressBar).val(e.loaded * 100 / e.total);
            };
            return xhr;
        },
        success: function () {
            $(progressBar).val(100);
        }
    });
}

function drawLineChart(container, data, xAxisField, yAxisFields, options) {
    if (!container)
        return;

    let containerWidth;
    let containerHeight;

    function draw(container, containerWidth, containerHeight) {
        $(container).empty();

        const ticks = (options && options.ticks) || 5;
        const colorClasses = (options && options.colorClasses) || [''];
        const unitWidth = (options && options.unitWidth) || 120;
        const margin = {
            top: (options && options.topMargin) || 20,
            right: (options && options.rightMargin) || 30,
            bottom: (options && options.bottomMargin) || 20,
            left: (options && options.leftMargin) || 30
        };

        const svgWidth = Math.max(data.length * unitWidth, containerWidth);

        const width = svgWidth - margin.left - margin.right;
        const height = containerHeight - margin.top - margin.bottom;

        const svg = d3.select(container)
            .append('svg')
            .attr('width', svgWidth)
            .attr('height', containerHeight);

        const x = d3.scaleBand().rangeRound([0, width]).padding(0.1);
        const y = d3.scaleLinear().rangeRound([height, 0]);

        const g = svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        x.domain(data.map(function (d) {
            return d[xAxisField];
        }));

        let maxY = d3.max(data, function (d) {
            return yAxisFields.reduce(function (accumulator, value) {
                return Math.max(accumulator, d[value]);
            }, 0);
        });

        y.domain([0, (options && options.maxY) || maxY * 1.2]);

        g.append("g")
            .attr("class", "d3-axis axis--x")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x)
                /*.tickSize(-height)*/)
            .append("text")
            // .attr("y", "1em")
            .attr('class', 'd3-legend-label')
            .text((options && options.xLabel) || '');

        g.append("g")
            .attr("class", "d3-axis axis--y")
            .call(
                d3.axisLeft(y)
                    .ticks(ticks)
                    .tickSize(-width)
            )
            .append("text")
            .attr("y", "1em")
            .attr('class', 'd3-legend-label')
            .text((options && options.yLabel) || '');

        function drawLine(field, colorClass) {
            g.append("path")
                .datum(data)
                .attr("class", "d3-line " + colorClass)
                .attr("d", d3.line()
                    .x(function (d) {
                        return x(d[xAxisField]) + x.bandwidth() / 2;
                    })
                    .y(function (d) {
                        return y(d[field] || 0);
                    })
                    .curve(d3.curveMonotoneX));

            g.selectAll(".dot")
                .data(data)
                .enter().append("circle")
                .attr("class", "d3-dot " + colorClass)
                .attr("cx", function (d, i) {
                    return x(d[xAxisField]) + x.bandwidth() / 2
                })
                .attr("cy", function (d) {
                    return y(d[field] || 0)
                })
                .attr("r", 3);

            g.selectAll()
                .data(data)
                .enter()
                .append("text")
                .attr("x", function (d) {
                    return x(d[xAxisField]) + x.bandwidth() / 2;
                })
                .attr("y", function (d) {
                    return y(d[field]) - 10;
                })
                .attr("class", 'd3-annotation')
                .attr("text-anchor", "middle")
                .text(function (d) {
                    const value = parseInt(d[field]);
                    if (options && options.valueText)
                        return options.valueText.apply(this, [value]);

                    return value;
                });
        }

        yAxisFields.map(function (field, i) {
            drawLine(field, colorClasses[i % colorClasses.length]);
        });
    }

    const jobId = setInterval(function () {
        let newContainerWidth = parseFloat(window.getComputedStyle(container, null).getPropertyValue("width"));
        let newContainerHeight = parseFloat(window.getComputedStyle(container, null).getPropertyValue("height"));

        if (containerWidth !== newContainerWidth || containerHeight !== newContainerHeight) {
            containerWidth = newContainerWidth;
            containerHeight = newContainerHeight;

            if (!containerWidth || !containerHeight) {
                clearInterval(jobId);
                return;
            }

            draw(container, containerWidth, containerHeight);
        }
    }, 1000);

}

function drawBarChart(container, data, xAxisField, yAxisFields, options) {
    setTimeout(function () {
        $(container).empty();

        const ticks = (options && options.ticks) || 5;
        const colorClasses = (options && options.colorClasses) || [''];
        const unitWidth = (options && options.unitWidth) || 120;
        const margin = {
            top: (options && options.topMargin) || 20,
            right: (options && options.rightMargin) || 30,
            bottom: (options && options.bottomMargin) || 20,
            left: (options && options.leftMargin) || 30
        };
        const containerWidth = parseFloat(window.getComputedStyle(container, null).getPropertyValue("width"));
        const containerHeight = parseFloat(window.getComputedStyle(container, null).getPropertyValue("height")) - 30;

        if (!containerWidth || !containerHeight)
            return drawBarChart(container, data, xAxisField, yAxisFields, options);

        setTimeout(function () {
            const newContainerWidth = parseFloat(window.getComputedStyle(container, null).getPropertyValue("width"));
            const newContainerHeight = parseFloat(window.getComputedStyle(container, null).getPropertyValue("height")) - 30;

            if (newContainerWidth !== containerWidth || newContainerHeight !== containerHeight)
                return drawBarChart(container, data, xAxisField, yAxisFields, options);
        }, 1000);

        const svgWidth = Math.max(data.length * unitWidth, containerWidth);

        const width = svgWidth - margin.left - margin.right;
        const height = containerHeight - margin.top - margin.bottom;

        const svg = d3.select(container)
            .append('svg')
            .attr('width', svgWidth)
            .attr('height', containerHeight);

        const x = d3.scaleBand().rangeRound([0, width]).padding(0.1);
        const y = d3.scaleLinear().rangeRound([height, 0]);

        const g = svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        x.domain(data.map(function (d) {
            return d[xAxisField];
        }));

        let maxY = d3.max(data, function (d) {
            return yAxisFields.reduce(function (accumulator, value) {
                return Math.max(accumulator, d[value]);
            }, 0);
        });

        y.domain([0, (options && options.maxY) || maxY * 1.2]);

        g.append("g")
            .attr("class", "d3-axis axis--x")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x)
                /*.tickSize(-height)*/)
            .append("text")
            // .attr("y", "1em")
            .attr('class', 'd3-legend-label')
            .text((options && options.xLabel) || '');

        g.append("g")
            .attr("class", "d3-axis axis--y")
            .call(
                d3.axisLeft(y)
                    .ticks(ticks)
                    .tickSize(-width)
            )
            .append("text")
            .attr("y", "1em")
            .attr('class', 'd3-legend-label')
            .text((options && options.yLabel) || '');

        function drawBar(member, barClass, xTransform) {
            g.selectAll()
                .data(data)
                .enter()
                .append("rect")
                .attr("class", barClass)
                .attr("x", function (d) {
                    return x(d[xAxisField]) + xTransform;
                })
                .attr("width", x.bandwidth() / yAxisFields.length)
                .attr("y", function (d) {
                    return height;
                })
                .attr("height", 0)
                .transition()
                .duration(1000)
                .delay(function (d, i) {
                    return i * 50;
                })
                .attr("y", function (d) {
                    return y(d[member]);
                })
                .attr("height", function (d) {
                    return height - y(d[member]);
                });
        }

        yAxisFields.map(function (field, i) {
            drawBar(field, colorClasses[i % colorClasses.length], i * x.bandwidth() / yAxisFields.length);
        });

    }, 50);
}

function drawStackedBarChart(container, data, xAxisField, yAxisFields, options) {
    setTimeout(function () {
        $(container).empty();

        const ticks = (options && options.ticks) || 5;
        const colorClasses = (options && options.colorClasses) || [''];
        const unitWidth = (options && options.unitWidth) || 120;
        const margin = {
            top: (options && options.topMargin) || 20,
            right: (options && options.rightMargin) || 30,
            bottom: (options && options.bottomMargin) || 20,
            left: (options && options.leftMargin) || 30
        };
        const containerWidth = parseFloat(window.getComputedStyle(container, null).getPropertyValue("width"));
        const containerHeight = parseFloat(window.getComputedStyle(container, null).getPropertyValue("height")) - 30;

        if (!containerWidth || !containerHeight)
            return drawStackedBarChart(container, data, xAxisField, yAxisFields, options);

        setTimeout(function () {
            const newContainerWidth = parseFloat(window.getComputedStyle(container, null).getPropertyValue("width"));
            const newContainerHeight = parseFloat(window.getComputedStyle(container, null).getPropertyValue("height")) - 30;

            if (newContainerWidth !== containerWidth || newContainerHeight !== containerHeight)
                return drawStackedBarChart(container, data, xAxisField, yAxisFields, options);
        }, 1000);

        const svgWidth = Math.max(data.length * unitWidth, containerWidth);

        const width = svgWidth - margin.left - margin.right;
        const height = containerHeight - margin.top - margin.bottom;

        const svg = d3.select(container)
            .append('svg')
            .attr('width', svgWidth)
            .attr('height', containerHeight);

        const x = d3.scaleBand().rangeRound([0, width]).padding(0.6);
        const y = d3.scaleLinear().rangeRound([height, 0]);
        const colorOrdinal = d3.scaleOrdinal().domain(yAxisFields).range(colorClasses);

        const g = svg.append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        x.domain(data.map(function (d) {
            return d[xAxisField];
        }));

        let maxY = d3.max(data, function (d) {
            return yAxisFields.reduce(function (accumulator, value) {
                return accumulator + d[value];
            }, 0);
        });

        y.domain([0, (options && options.maxY) || maxY * 1.2]).nice();

        g.append("g")
            .attr("class", "d3-axis axis--x")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x)
                /*.tickSize(-height)*/)
            .append("text")
            // .attr("y", "1em")
            .attr('class', 'd3-legend-label')
            .text((options && options.xLabel) || '');

        g.append("g")
            .attr("class", "d3-axis axis--y")
            .call(
                d3.axisLeft(y)
                    .ticks(ticks)
                    .tickSize(-width)
            )
            .append("text")
            .attr("y", "1em")
            .attr('class', 'd3-legend-label')
            .text((options && options.yLabel) || '');

        const stackedData = d3.stack()
            .keys(yAxisFields)
            (data);

        g.append("g")
            .selectAll("g")
            .data(stackedData)
            .enter()
            .append("g")
            .attr("class", function (d) {
                return colorOrdinal(d.key);
            })
            .selectAll("rect")
            .data(function (d) {
                return d;
            })
            .enter().append("rect")
            .attr("x", function (d) {
                return x(d.data[xAxisField]);
            })
            .attr("y", function (d) {
                return y(d[1]);
            })
            .attr("height", function (d) {
                return y(d[0]) - y(d[1]);
            })
            .attr("width", x.bandwidth());

        g.append("g")
            .selectAll("g")
            .data(stackedData)
            .enter()
            .append("g")
            .attr("class", function (d) {
                return 'text-' + colorOrdinal(d.key);
            })
            .selectAll("text")
            .data(function (d) {
                return d;
            })
            .enter()
            .append("text")
            .attr("text-anchor", "middle")
            .attr("x", function (d) {
                return x(d.data[xAxisField]) + x.bandwidth() / 2;
            })
            .attr("y", function (d) {
                return y(d[0]) + ((y(d[1]) - y(d[0])) / 2) + 5 /*font size / 2*/;
            })
            .text(function (d) {
                const value = d[1] - d[0];
                return value ? value : '';
            });

    }, 50);
}

function drawPieChart(svgSelector, rate, options) {
    setTimeout(function () {
        rate = rate || 0;

        const svg = document.querySelector(svgSelector);
        $(svg).empty();
        const dataSet = [rate, 1 - rate];

        const width = $(svg).width();
        const height = $(svg).height();
        const colorClasses = (options && options.colorClasses) || [''];

        if (!width || !height)
            return drawPieChart(svgSelector, rate, options);

        const arc = d3.arc().innerRadius((options && options.innerRadius || 40)).outerRadius((options && options.outerRadius || 70));
        const pie = d3.select(svg)
            .selectAll("g")
            .data(
                d3.pie()
                    .startAngle((options && options.startAngle || 0) * (Math.PI / 180))
                    .endAngle((options && options.endAngle || 360) * (Math.PI / 180))
                    // .padAngle(0.02)
                    .sort(null)
                    (dataSet)
            )
            .enter()
            .append("g")
            .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

        pie.append("path")
            .attr("class", function (d, i) {
                return colorClasses[i % colorClasses.length];
            })
            .transition()
            .duration(200)
            .delay(function (d, i) {
                return i * 200
            })
            .attrTween("d", function (d, i) {
                const interpolate = d3.interpolate(
                    {startAngle: d.startAngle, endAngle: d.startAngle},
                    {startAngle: d.startAngle, endAngle: d.endAngle}
                );

                return (function (t) {
                    return arc(interpolate(t));
                });
            });


        /*const sum = dataSet.reduce(function (accumulator, value) {
            return accumulator + value;
        }, 0);*/

        /*d3.select(svgSelector)
            .append("text")
            .attr("class", "d3-total")
            .attr("transform", "translate(" + (width / 2 - 15) + ", " + (height / 2) + ")")
            .text((rate * 100) + "%");*/

        d3.select(svgSelector)
            .append("text")
            .attr("class", "d3-total-label")
            .attr("transform", "translate(" + (width / 2) + ", " + (height / 2) + ")")
            .text(options && options.innerLabel || (rate * 100).toFixed(1) + "%");

        /*pie.append("text")
            .attr("class", "d3-num")
            .attr("transform", function (d, i) {
                const position = arc.centroid(d);
                position[1] += 5;
                return "translate(" + position + ")";
            })
            .text(function (d, i) {
                return ((d.value || 0) / sum * 100).toFixed(1) + '%';
            });*/
    }, 50);
}

function createIpccAdminCommunicator() {
    const ipccAdminCommunicator = new IpccAdminCommunicator();
    ipccAdminCommunicator.log = function () {
    };
    restSelf.get('/api/auth/socket-info').done(function (response) {
        ipccAdminCommunicator.connect(response.data.adminSocketUrl, response.data.companyId, response.data.userId, response.data.pbxName);
    });
    return ipccAdminCommunicator;
}

function alert(text, func) {
    $('#lean-overlay-container').click();

    // const modal = $('<div id="alert-modal" class="alert-modal" style="display: none;"><h3>alert</h3><i class="close icon"></i><text></text></div>').appendTo($('body'));
    const modal = $('<div id="alert-modal" class="alert-modal" style="display: none;"><i class="close icon"></i><i class="exclamation icon"></i><div class="content"></div><div class="action"><button class="fluid ui button">확인</button></div><div/>', {
        id: 'alert-modal',
        class: 'alert-modal',
        style: 'display: none;'
    }).appendTo($('body'));
    modal.find('.content').text(text);
    const linker = $('<a/>', {href: '#alert-modal'}).leanModal({closeButton: '#lean-overlay-container'}).click();
    $('.alert-modal').transition('bounce');

    function removeGarbage() {
        modal.remove();
        linker.remove();

        $('#lean-overlay-container').off('click', removeGarbage);

        if (func != null) {
            func();
        }
    }

    $('#lean-overlay-container').on('click', removeGarbage);
}

function confirm(text) {
    const deferred = $.Deferred();

    $('#lean-overlay-container').click();

    const modal = $('<div id="alert-modal" class="alert-modal" style="display: none;">' +
        '<i class="close icon"></i>' +
        '<i class="exclamation icon"></i>' +
        '<div class="content"></div>' +
        '<div class="actions" style="text-align: center;"><button class="ui button blue -confirm">확인</button><button class="ui button -cancel">취소</button></div>' +
        '<div/>', {
        id: 'alert-modal',
        class: 'alert-modal',
        style: 'display: none;'
    }).appendTo($('body'));
    modal.find('.content').text(text);
    modal.click(function (event) {
        event.stopPropagation();
    });

    modal.find('.-confirm').click(function () {
        setTimeout(function () {
            deferred.resolve(text);
        }, 10);
    });

    modal.find('.-cancel').click(function () {
        setTimeout(function () {
            deferred.reject(text);
        }, 10);
    });

    const linker = $('<a/>', {href: '#alert-modal'}).leanModal({closeButton: '#alert-modal button'}).click();
    $('.alert-modal').transition('bounce');

    function removeGarbage() {
        modal.remove();
        linker.remove();
    }

    $('#alert-modal button').on('click', removeGarbage);

    return deferred.promise();
}

function prompt(text, isPassword) {
    const deferred = $.Deferred();

    $('#lean-overlay-container').click();

    const modal = $('<div id="alert-modal" class="alert-modal" style="display: none;">' +
        '<i class="close icon"></i>' +
        '<div class="content"></div>' +
        '<div class="ui input fluid" style="margin-bottom: 1em;"><input type="' + (isPassword ? 'password' : 'text') + '" name="text" style="width: 100%;"/></div>' +
        '<div class="actions" style="text-align: center;"><button class="ui button blue -confirm">확인</button><button class="ui button -cancel">취소</button></div>' +
        '<div/>', {
        id: 'alert-modal',
        class: 'alert-modal',
        style: 'display: none;'
    }).appendTo($('body'));
    modal.find('.content').text(text);
    modal.click(function (event) {
        event.stopPropagation();
    });

    modal.find('.-confirm').click(function () {
        setTimeout(function () {
            const text = modal.find('[name=text]').val();
            deferred.resolve(text);
        }, 10);
    });

    modal.find('.-cancel').click(function () {
        setTimeout(function () {
            deferred.reject();
        }, 10);
    });

    const linker = $('<a/>', {href: '#alert-modal'}).leanModal({closeButton: '#alert-modal button'}).click();
    $('.alert-modal').transition('bounce');

    function removeGarbage() {
        modal.remove();
        linker.remove();
    }

    $('#alert-modal button').on('click', removeGarbage);

    return deferred.promise();
}
(function ($) {
    "use strict";

    $(window).on('load', function () {
        $('body').bindHelpers();
    });

    $(document).on("click", 'a[href="#"]', function (e) {
        e.preventDefault();
    });

    $(document).on('click', '.tab-indicator', function (event) {
        event.preventDefault();
        event.stopPropagation();
        parent.tabController.attachTab(this);
    });
})(jQuery);
