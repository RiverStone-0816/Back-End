<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<%@ attribute name="node" required="true" type="kr.co.eicn.ippbx.server.model.MonitorIvrTree" %>
<%@ attribute name="statusCodes" required="true" type="java.util.Map" %>

<c:choose>
    <c:when test="${node.type == 0}">
        <c:if test="${node.type == 0 && node.nodes.size() > 0}">
            <c:set var="child" value="${node.nodes[0]}"/>
            <div class="item">
                <div class="content">
                    <div class="header"><span class="ui grey circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}[${child.waitingCustomerCount}]</div>
                    <div class="list">
                        <div class="item">
                            <i class="folder icon"></i>
                            <div class="content">
                                <div class="header">${child.name}</div>
                                <div class="list">
                                    <c:forEach var="grandchild" items="${child.nodes}">
                                        <tags:ivr-monitor-node node="${grandchild}" statusCodes="${statusCodes}"/>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </c:when>
    <c:when test="${node.type == 3}">
        <div class="item">
            <div class="column">
                <div class="list">
                    <div class="header"><span class="ui grey circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}[<text class="-custom-wait-count" data-hunt="${node.queueNameResponse.name}">${node.waitingCustomerCount}</text>]
                        [<text class="-consultant-status-count" data-value="0" data-hunt="${node.queueNameResponse.name}">${node.processingCustomerCount}</text>]</div>
                    <table class="ui celled table compact unstackable fixed">
                        <caption>${g.htmlQuote(node.queueNameResponse.hanName)}</caption>
                        <tbody>
                        <c:choose>
                            <c:when test="${node.queueMemberList != null && node.queueMemberList.size() > 0}">
                                <c:forEach var="person" items="${node.queueMemberList}">
                                    <tr>
                                        <td>${g.htmlQuote(person.idName)}(${g.htmlQuote(person.extension)})</td>
                                        <td><span data-peer="${g.htmlQuote(person.peer)}"
                                                  class="-status-label ui small label ${person.paused != null && person.paused == 0 ? 'blue' : null}">${g.htmlQuote(statusCodes.get(person.paused))} </span></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="2" class="null-data">소속된 상담원이 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="item">
            <div class="content">
                <div class="header"><span class="ui grey circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}[${node.waitingCustomerCount}]</div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<script>
    (function () {
        if (window.queues) {
            if (!queues['${g.escapeQuote(node.queueNameResponse.name)}']) queues['${node.queueNameResponse.name}'] = {name: '${g.escapeQuote(node.queueNameResponse.name)}', hanName: '${g.escapeQuote(node.queueNameResponse.hanName)}', peer: []};
            queues['${g.escapeQuote(node.queueNameResponse.name)}'].waitingCustomerCount = ${node.processingCustomerCount};
        }
    })()
</script>