<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/monitor/consultant/part/"/>
        <div class="sub-content ui container fluid">
            <div class="ui grid">
                <div class="sixteen wide column"></div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">센터현황모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div id="center-stat"></div>
                </div>
                <div class="eight wide column">
                    <div id="primary-stat"></div>
                </div>
                <div class="eight wide column">
                    <div id="waiting-number-chart"></div>
                </div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">큐그룹모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div id="hunt-monitor"></div>
                </div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">상담원모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div id="consultant-monitor"></div>
                </div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">통합 통계 모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div id="total-stat"></div>
                </div>
                <div class="sixteen wide column">
                    <div id="hunt-compare-chart"></div>
                </div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">큐그룹별 통계 모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div id="hunt-stat"></div>
                </div>
                <div class="sixteen wide column">
                    <h3 class="ui header center aligned">
                        <text class="content">시간대별 / 큐그룹별 통계 모니터링</text>
                    </h3>
                </div>
                <div class="sixteen wide column">
                    <div id="hunt-hour-chart"></div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            const ONE_MINUTE = 60 * 1000;

            $(window).on('load', function () {
                function load() {
                    const selected = $('#hunt-compare-chart').find('[name=queues]').find(':selected');

                    const fields = [];
                    if (selected.length === 2) {
                        selected.each(function () {
                            fields.push($(this).val());
                        });
                    } else {
                        fields.push('', '');
                    }

                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/center-stat', '#center-stat');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/primary-stat', '#primary-stat');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/waiting-number-by-time-chart', '#waiting-number-chart');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/hunt-monitor', '#hunt-monitor');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/consultant-monitor', '#consultant-monitor');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/total-stat', '#total-stat');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/hunt-compare-chart?queueAName='+fields[0]+'&queueBName'+fields[1], '#hunt-compare-chart');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/hunt-stat', '#hunt-stat');
                    replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/hunt-hour-chart', '#hunt-hour-chart');
                }

                load();
                setInterval(function () {
                    if (!$(parent.document).find('#main').is('.change-mode') && $(parent.document).find('.tab-label-container').find('.active').data('href') === '/admin/monitor/consultant/part/')
                        load();
                }, ONE_MINUTE);
            });

            function interceptionView() {
                $('#modal-interception-view').modalShow();
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>