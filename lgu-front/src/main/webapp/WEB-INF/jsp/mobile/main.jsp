<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.test.config.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:layout-mobile>
    <div id="wrap-mobile">
        <div class="ui container basic">
            <div class="ui grid">
                <div class="sixteen wide column">
                    <div class="ui two column grid main-link">
                        <div class="column">
                            <div class="ui segment black">
                                <a href="<c:url value="/m/operation"/>">
                                    <h3 class="ui header"><i class="globe icon"></i></h3>
                                    <h4 class="ui header">운영현황</h4>
                                </a>
                            </div>
                        </div>
                        <div class="column">
                            <div class="ui segment teal">
                                <a href="<c:url value="/m/consultant"/>">
                                    <h3 class="ui header"><i class="tasks icon"></i></h3>
                                    <h4 class="ui header">상담원<br>모니터링</h4>
                                </a>
                            </div>
                        </div>
                        <div class="column">
                            <div class="ui segment blue">
                                <a href="<c:url value="/m/stat"/>">
                                    <h3 class="ui header"><i class="chart bar outline icon"></i></h3>
                                    <h4 class="ui header">통계</h4>
                                </a>
                            </div>
                        </div>
                        <div class="column">
                            <div class="ui segment grey">
                                <a href="<c:url value="/m/callback"/>">
                                    <h3 class="ui header"><i class="phone icon"></i></h3>
                                    <h4 class="ui header">콜백</h4>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
        </script>
    </tags:scripts>
</tags:layout-mobile>
