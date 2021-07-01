const restSelf = {
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
                if (response.status === 401)
                    location.href = 'main';

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
