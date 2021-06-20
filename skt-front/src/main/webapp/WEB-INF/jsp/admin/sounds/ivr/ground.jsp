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
            <div class="panel ivr-panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">IVR에디터</div>
                    </div>
                    <div class="pull-right">
                        <select onchange="loadRootIvrTree($(this).val())" class="mr5">
                            <c:forEach var="e" items="${roots}">
                                <option value="${e.key}" ${e.key == seq ? 'selected' : ''}>${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                        <button class="ui basic button" onclick="addNewRootIvrTree()">추가</button>
                        <c:if test="${seq != null}">
                            <button class="ui basic button" onclick="deleteRootIvrTree(rootSeq)">삭제</button>
                            <button class="ui basic button" onclick="copyRootIvrTree(rootSeq)">복사</button>
                        </c:if>
                    </div>
                </div>
                <div class="panel-body">
                    <div id="helper-container"></div>
                    <div class="node-container" style="position: absolute; left: 0; width: 300px; top: 0; bottom: 0; overflow-x: hidden; overflow-y: auto;">
                        <h5 class="ui header">IVR리스트
                            <text class="sub header">하단 항목을 우측에 드래그하세요.</text>
                        </h5>
                        <div style="text-align: center;">
                            <c:forEach var="e" items="${menuTypes}">
                                <div class="-init-ivr-node node-type center aligned ui segment <%--${e.rootable ? '-rootable' : ''}--%>" data-menu="${e.menu}" data-type="${e.code}"
                                     data-level="${serviceKind.equals('SC') ? null : (level.get(seq) != null ? level.get(seq) : null)}">
                                    <i class="${e.imageType.name() == 'SOUND' ? 'music' : e.imageType.name() == 'FINISH' ? 'share' : 'bars'} icon"></i>${g.htmlQuote(message.getEnumText(e))}
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="ivr-editor-inner">
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

            function popupNewModal(type, posX, posY, parentSeq, rootSeq) {
                return popupReceivedHtml($.addQueryString('/admin/sounds/ivr/new/modal', {type: type, posX: posX, posY: posY, parentSeq: (parentSeq || ''), rootSeq: ${seq == null ? 'null' : seq}}), 'modal-menu');
            }

            function popupModal(seq, type, rootSeq) {
                return popupReceivedHtml($.addQueryString('/admin/sounds/ivr/' + seq + '/modal', {type: type || '', rootSeq: ${seq == null ? 'null' : seq}}), 'modal-menu');
            }

            function popupWebVoiceModal(code) {
                return popupReceivedHtml($.addQueryString('/admin/sounds/ivr/' + encodeURIComponent(code) + '/modal-visual-ars'), 'modal-visual-ars');
            }

            function deleteEntity(seq) {
                return restSelf.delete('/api/ivr/' + seq);
            }

            function deleteRootIvrTree(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    deleteEntity(seq).done(function () {
                        loadRootIvrTree('');
                    });
                });
            }

            function copyRootIvrTree(seq) {
                prompt('새로운 ROOT IVR의 이름을 입력하십시오.').done(function (text) {
                    if (!text)
                        return;
                    updateNode.name = text;
                    updateNode.copyToSourceId = seq;
                    restSelf.post('/api/ivr/', updateNode).done(function (response) {
                        loadRootIvrTree(response.data);
                    });
                });
            }

            function updatePosition(seq, posX, posY) {
                restSelf.put('/api/ivr/' + seq + '/position', {posX: posX, posY: posY}, function () {
                }, true);
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
                    droppable: {
                        accept: '.-init-ivr-node.-rootable',
                        drop: function (event, ui) {
                            popupNewModal(ui.draggable.attr('data-type'), event.offsetX, event.offsetY);
                        }
                    },
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
                    dragStop: function (event, ui, id, widgetData, widget) {
                        updatePosition(id, parseInt($(ui.helper).css('left')), parseInt($(ui.helper).css('top')));
                    },
                    remove: function (id, widgetData, widget) {
                        if (id === rootSeq) {
                            deleteEntity(id).done(function () {
                                loadRootIvrTree('');
                            });
                        } else {
                            deleteEntity(id).done(function () {
                                loadRootIvrTree(rootSeq);
                            });
                        }
                    }
                });

                $('.-init-ivr-node').draggable({
                    helper: "clone",
                    tolerance: 'pointer',
                    iframeFix: true,
                    appendTo: '#helper-container',
                    opacity: 1
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

                        editor.ivrEditor('render');
                    });
                } else {
                    addNewRootIvrTree();
                }
            });

            function addNewRootIvrTree() {
                prompt('새로운 ROOT IVR의 이름을 입력하십시오.').done(function (text) {
                    if (!text)
                        return;

                    restSelf.post('/api/ivr/', {name: text, type: 1, soundCode: "TTS", ttsDataStrings: [""]}).done(function (response) {
                        loadRootIvrTree(response.data);
                    });
                });
            }

            function loadRootIvrTree(seq) {
                location.href = contextPath + '/admin/sounds/ivr?seq=' + seq;
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
