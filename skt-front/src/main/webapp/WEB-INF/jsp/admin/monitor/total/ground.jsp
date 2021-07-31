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
        <tags:page-menu-tab url="/admin/monitor/total/"/>
        <div class="sub-content ui container fluid">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">${g.htmlQuote(menu.getMenuName("/admin/monitor/total/"))}</div>
                    </div>
                </div>
            </div>
            <div class="panel panel-statstics">
                <div class="panel-body">
                    <div class="panel-section">
                        <div class="table-scroll-wrap">
                            <div id="center-stat"></div>
                        </div>
                    </div>
                    <div class="panel-section">
                        <div class="panel-sub-title">서비스 모니터링</div>
                        <div class="panel-sub-container">
                            <div class="ui grid">
                                <div class="sixteen wide column remove-pb">
                                    <div class="table-scroll-wrap">
                                        <div id="service-stat"></div>
                                    </div>
                                </div>
                            </div>
                            <div id="yesterday-today-comparing-chart"></div>
                        </div>
                    </div>
                    <div class="panel-section">
                        <div class="panel-sub-title">상담그룹 모니터링</div>
                        <div class="panel-sub-container">
                            <div class="table-scroll-wrap">
                                <div id="queue-stat"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            function load() {
                replaceReceivedHtmlInSilence('/admin/monitor/consultant/part/center-stat', '#center-stat');
                replaceReceivedHtmlInSilence('/admin/monitor/total/service-stat', '#service-stat');
                replaceReceivedHtmlInSilence('/admin/monitor/total/queue-stat', '#queue-stat');
                replaceReceivedHtmlInSilence('/admin/monitor/total/yesterday-today-comparing-chart', '#yesterday-today-comparing-chart');
            }

            $(window).on('load', function () {
                const ONE_MINUTE = 60 * 1000;
                setInterval(load, ONE_MINUTE);
                load();
            });
        </script>
    </tags:scripts>
</tags:tabContentLayout>
