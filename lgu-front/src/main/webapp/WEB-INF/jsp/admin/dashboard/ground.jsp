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

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <div class="menu-tab">
            <div class="inner">
                <ul>
                    <li><a href="<c:url value="/admin/dashboard/"/>" class="tab-on tab-indicator" title="대시보드">대시보드</a></li>
                </ul>
                <div class="ui breadcrumb">
                    <span class="section">대시보드 (1분 마다 정보 갱신)</span>
                </div>
            </div>
        </div>

        <div class="sub-content ui container fluid unstackable">
            <div class="ui grid dashboard">
                <div class="three wide column">
                    <jsp:include page="/admin/dashboard/component-service-monitor"/>
                </div>

                <div class="thirteen wide column">
                    <div class="ui four column grid">
                        <c:forEach var="e" begin="1" end="8">
                            <div class="column">
                                <div class="ui segment piece -dashboard-widget" data-ui-sequence="${e}"
                                        <c:if test="${dashboardSequenceToDashboard.get(e) != null}">
                                            data-id="${dashboardSequenceToDashboard.get(e).dashboardId}"
                                            data-name="${dashboardSequenceToDashboard.get(e).dashboardName}"
                                            data-type="${dashboardSequenceToDashboard.get(e).dashboardType}"
                                            data-value="${dashboardSequenceToDashboard.get(e).dashboardValue}"
                                        </c:if>>
                                    <h2 class="ui center aligned icon header">
                                        <i class="circular plus icon"></i>
                                    </h2>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="ui one column grid">
                        <div class="sixteen wide column waiting-piece-container">
                            <c:forEach var="e" items="${noneShowingDashboards}">
                                <div class="ui basic button -dashboard-component"
                                     data-id="${e.dashboardId}"
                                     data-name="${e.dashboardName}"
                                     data-type="${e.dashboardType}"
                                     data-value="${e.dashboardValue}">
                                    <i class="chart bar icon"></i>
                                        ${g.htmlQuote(e.dashboardName)}
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            const ONE_MINUTE = 60 * 1000;

            function removeDashboardComponent(id) {
                const widget = $('.-dashboard-widget[data-id=' + id + ']');
                if (!widget.length) return;

                restSelf.delete('/api/dashboard/' + widget.attr('data-ui-sequence')).done(function () {
                    widget.empty().append('<h2 class="ui center aligned icon header"><i class="circular plus icon"></i></h2>');

                    $('.waiting-piece-container').append(
                        $('<div/>', {
                            class: 'ui basic button -dashboard-component',
                            'data-id': widget.attr('data-id'),
                            'data-name': widget.attr('data-name'),
                            'data-type': widget.attr('data-type'),
                            'data-value': widget.attr('data-value')
                        }).draggable({
                            helper: "clone",
                            tolerance: 'pointer',
                            revert: true,
                            iframeFix: true
                        })
                            .append('<i class="chart bar icon"></i>')
                            .append($('<text/>', {text: widget.attr('data-name')}))
                    );

                    widget.removeAttr('data-id');
                    widget.removeAttr('data-name');
                    widget.removeAttr('data-type');
                    widget.removeAttr('data-value');
                });
            }

            function addDashboardComponent(uiSequence, id) {
                function set() {
                    const component = $('.-dashboard-component[data-id=' + id + ']');
                    const widget = $('.-dashboard-widget[data-ui-sequence=' + uiSequence + ']');
                    widget.attr('data-id', component.attr('data-id'));
                    widget.attr('data-name', component.attr('data-name'));
                    widget.attr('data-type', component.attr('data-type'));
                    widget.attr('data-value', component.attr('data-value'));

                    setWidgetContent(widget);

                    component.remove();
                }

                restSelf.post('/api/dashboard/', {seq: uiSequence, dashboardInfoId: id}).done(set).fail(function () {
                    restSelf.put('/api/dashboard/' + uiSequence, {seq: uiSequence, dashboardInfoId: id}).done(set);
                });
            }

            function setWidgetContent(widget) {
                if (widget.attr('data-id')) {
                    return receiveHtml(contextPath + '/admin/dashboard/component/', {
                        type: widget.attr('data-type'), value: widget.attr('data-value')
                    }, function () {
                    }, true).done(function (html) {
                        const mixedNodes = $.parseHTML(html, null, true);

                        const uiList = [];
                        const scripts = [];

                        (function () {
                            for (let i = 0; i < mixedNodes.length; i++) {
                                const node = $(mixedNodes[i]);
                                if (node.is('script')) scripts.push(node);
                                else uiList.push(node);
                            }
                            return scripts;
                        })();

                        widget.empty()
                            .append($('<i/>', {
                                class: 'close icon', click: function () {
                                    removeDashboardComponent(widget.attr('data-id'));
                                }
                            }))
                            .append($('<h5/>', {class: 'ui header', text: widget.attr('data-name')}))
                            .append(uiList);

                        scripts.map(function (script) {
                            eval('(function() { return function(component, uiList) {' + script.text() + '}; })()').apply(window, [widget, uiList]);
                        });
                    });
                } else {
                    widget.empty().append('<h2 class="ui center aligned icon header"><i class="circular plus icon"></i></h2>');
                }
            }

            $('.-dashboard-widget').each(function () {
                const $this = $(this);
                $(this).droppable({
                    accept: '.-dashboard-component',
                    greedy: true,
                    drop: function (event, ui) {
                        if ($this.attr('data-id')) return;
                        addDashboardComponent($this.attr('data-ui-sequence'), ui.draggable.attr('data-id'));
                    }
                });

                setWidgetContent($this);
                setInterval(function () {
                    if (!$(parent.document).find('#main').is('.change-mode') && $(parent.document).find('.tab-label-container').find('.active').data('href') === '/admin/dashboard/')
                        setWidgetContent($this);
                }, ONE_MINUTE);
            });

            $('.-dashboard-component').each(function () {
                $(this).draggable({
                    helper: "clone",
                    tolerance: 'pointer',
                    revert: true,
                    iframeFix: true
                });
            });

            setInterval(function () {
                if (!$(parent.document).find('#main').is('.change-mode') && $(parent.document).find('.tab-label-container').find('.active').data('href') === '/admin/dashboard/')
                    replaceReceivedHtmlInSilence('/admin/dashboard/component-service-monitor', '#component-service-monitor');
            }, ONE_MINUTE);

        </script>
    </tags:scripts>
</tags:tabContentLayout>
