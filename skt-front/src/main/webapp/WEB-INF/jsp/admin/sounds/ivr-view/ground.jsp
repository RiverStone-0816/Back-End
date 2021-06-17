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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/sounds/ivr/"/>
        <div class="sub-content ui container fluid">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">IVR뷰어</div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr>
                                <th>선택</th>
                                <td colspan="7">
                                    <div class="ui form">
                                        <select onchange="loadRootIvrTree($(this).val())" style="vertical-align: -2px;">
                                            <c:forEach var="e" items="${roots}">
                                                <option value="${e.key}" ${e.key == seq ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="panel ivr-panel">
                <div class="panel-body">
                    <div id="helper-container"></div>
                    <div style="position: absolute; right: 0; left: 0; top: 0; bottom: 0;">
                        <div id="editor"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            const rootSeq = ${seq == null ? 'null' : seq};
            const editor = $('#editor');
            const updateNode = {name: "", type: 1, soundCode: "TTS", ttsDataStrings: [""], copyToSourceId: ""}

            function popupModal(seq, type, rootSeq) {
                return popupReceivedHtml($.addQueryString('/admin/sounds/ivr/' + seq + '/modal/view', {type: type || '', rootSeq: ${seq == null ? 'null' : seq}}), 'modal-menu');
            }

            function popupWebVoiceModal(code) {
                return popupReceivedHtml($.addQueryString('/admin/sounds/ivr/' + encodeURIComponent(code) + '/modal-visual-ars/view'), 'modal-visual-ars');
            }

            function addOrUpdateWidget(entity, ifNoExistWidgetException) {
                console.log(entity);
                var entityName = entity.name;
                if (parseInt(entity.type) === 3) {
                    var value = entity.typeData.split(/[|_]/);
                    entityName = entityName + "[" + value[0] + "]";
                }

                if (parseInt(entity.type) === 5) {
                    entityName = entityName + "[" + entity.typeData + "]";
                }

                const option = {
                    title: entityName,
                    x: parseInt(entity.posX),
                    y: parseInt(entity.posY),
                    onlyShadow: false,
                    id: entity.seq,
                    type: entity.type,
                    data: entity,
                    points: entity.nodes.reduce(function (result, child) {
                        // if (child.button)
                        result.push({
                            id: child.seq,
                            button: child.button,
                            name: child.name
                        });
                        return result;
                    }, [])
                };

                try {
                    editor.ivrEditor('getWidget', entity.seq).setContent(option);
                } catch (e) {
                    if (ifNoExistWidgetException) throw e;
                    editor.ivrEditor('addWidget', option);
                }
            }

            $(window).on('load', function () {
                editor.ivrEditor({
                    eye: function (id, data) {
                        popupWebVoiceModal(data.data.code);    //보이는 ARS사용
                    },
                    setup: function (id, widgetData, widget) {
                        popupModal(id); // 메뉴 정보 불러오기
                    },
                    link: function (event, ui, id, widgetData, widget) {
                        const isMenu = ui.draggable.attr('data-menu') === 'true';
                        const level = ui.draggable.attr('data-level');

                        if (isMenu) {
                            popupNewModal(ui.draggable.attr('data-type'), parseInt(ui.draggable.css('left')), parseInt(ui.draggable.css('top')), id);
                        } else
                            popupModal(id, ui.draggable.attr('data-type'));
                    },
                });

                if (rootSeq) {
                    restSelf.get('/api/ivr/').done(function (response) {
                        const rootNodes = response.data;

                        function addWidget(node, isRoot) {
                            addOrUpdateWidget(node, !isRoot);

                            if (node.nodes)
                                node.nodes.map(function (child) {
                                    if (child.type !== 0 || child.nodes.length > 0)
                                        addWidget(child, false)
                                });
                        }

                        rootNodes.map(function (node) {
                            if (node.seq === rootSeq) {
                                console.log(node.ttsData);
                                updateNode.ttsDataStrings[0] = node.ttsData;
                                addWidget(node, true);
                            }
                        });

                        editor.ivrEditor('render').done(function () {
                            const widgets = editor.ivrEditor('getWidgets');

                            for (let i = 0; i < widgets.length; i++) {
                                const widget = widgets[i];

                                if (!widget.option.onlyShadow)
                                    continue;

                                i--;
                                widget.remove();

                                if (widget.connectionPoint) {
                                    const parent = widget.connectionPoint.widget;
                                    for (let i = 0; i < parent.option.points.length; i++) {
                                        if (parent.option.points[i].id === widget.option.id) {
                                            parent.option.points.splice(i, 1);
                                            break;
                                        }
                                    }
                                    parent.setContent(parent.option);
                                }
                            }

                            $('.ui-draggable').draggable('destroy');
                            $('.ui-ivr-widget-remove').remove();
                        });
                    });
                } else {
                    addNewRootIvrTree();
                }
            });

            function loadRootIvrTree(seq) {
                location.href = contextPath + '/admin/sounds/ivr/view?seq=' + seq;
            }

            function init() {
                editor.ivrEditor('init');
            }

            $('.ivr-header-icon')
                .transition('set looping')
                .transition('pulse', '2000ms');

        </script>
    </tags:scripts>
</tags:tabContentLayout>
