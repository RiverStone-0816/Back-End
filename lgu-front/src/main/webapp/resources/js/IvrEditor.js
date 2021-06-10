(function ($) {
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
        const deferred = $.Deferred();

        if (!this.widgets)
            return;

        const rendering = [];
        this.widgets.map(function (widget) {
            rendering.push(widget.option.id);
            widget.rendered = false;
            if (widget.connectionPoint)
                widget.connectionPoint.rendered = false;
        });

        findRootWidget(this.widgets[0]).render(deferred, rendering);

        return deferred;
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

    Widget.prototype.render = function (deferred, rendering) {
        const widget = this;
        if (widget.rendered)
            return;

        if (widget.connectionPoint && !widget.connectionPoint.rendered)
            return widget.connectionPoint.render(deferred, rendering);

        if ((widget.x == null) || (widget.y == null)) {
            const offset = widget.editor.offset($(widget.connectionPoint.dom).find('.ui-ivr-connection-point-line-start-point'));
            if (offset.left === 0 && offset.top === 0)
                console.log('connectionPoint 렌더링이 완료되지 못한 상태로 widget 렌더링 시도', {widgetId: widget.option.id});

            widget.x = offset.left + 50; // 패딩
            widget.y = offset.top - 10; // 패딩
        }

        widget.width = $(widget.dom).outerWidth();
        widget.height = $(widget.dom).outerHeight();
        widget.rendered = true;
        $(widget.dom).css({left: widget.x, top: widget.y});
        // 브라우저 렌더링이 되기 위한 시간이 필요하다.

        console.log('rendered ' + widget.option.id);
        if (rendering) {
            rendering.splice(rendering.indexOf(widget.option.id), 1);
            if (!rendering.length)
                deferred.resolve();
        }

        setTimeout(function () {
            widget.drawLine();
            widget.editor.setMaxSizeIfOverflowByWidget(widget);
            widget.points.map(function (point) {
                point.render(deferred, rendering);
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
        const iWidget = this.editor.widgets.indexOf(this);
        if (iWidget >= 0)
            this.editor.widgets.splice(iWidget, 1);

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

    ConnectionPoint.prototype.render = function (deferred, rendering) {
        const connectionPoint = this;
        if (connectionPoint.rendered)
            return;

        if (!connectionPoint.widget.rendered)
            return connectionPoint.widget.render(deferred, rendering);

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
        connectionPoint.nextWidget.render(deferred, rendering);
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
