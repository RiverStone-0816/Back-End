<%@ tag pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<%@ attribute name="node" required="true" type="kr.co.eicn.ippbx.model.MonitorIvrTree" %>
<%@ attribute name="statusCodes" required="true" type="java.util.Map" %>

<c:choose>
    <c:when test="${node.type == 0}">
        <c:if test="${node.type == 0 && node.nodes.size() > 0}">
            <c:set var="child" value="${node.nodes[0]}"/>
            <li>
                <div class="header"><span class="ui circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}[${child.waitingCustomerCount}]</div>
                <ul class="">
                    <li>
                        <div class="header active"><i class="folder open icon"></i>${child.name}
                                <%--<button type="button" class="ui basic button mini" title="추가">음원듣기</button>--%>
                                <%--<button type="button" class="ui basic button mini" title="추가">버튼맵핑</button>--%>
                                <%--<button type="button" class="ui basic button mini" title="추가">삭제</button>--%>
                        </div>
                        <ul>
                            <c:forEach var="grandchild" items="${child.nodes}">
                                <tags:ivr-monitor-node node="${grandchild}" statusCodes="${statusCodes}"/>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>

                <script>
                    (function () {
                        if (window.queues) {
                            if (!queues['${g.escapeQuote(node.queueNameResponse.name)}']) queues['${node.queueNameResponse.name}'] = {
                                name: '${g.escapeQuote(node.queueNameResponse.name)}',
                                hanName: '${g.escapeQuote(node.queueNameResponse.hanName)}',
                                peer: []
                            };
                            queues['${g.escapeQuote(node.queueNameResponse.name)}'].waitingCustomerCount = ${node.processingCustomerCount};
                        }
                    })()
                </script>
            </li>
        </c:if>
    </c:when>
    <c:when test="${node.type == 3}">
        <li>
            <div class="header"><span class="ui circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}[
                <text class="-custom-wait-count" data-hunt="${node.queueNameResponse.name}">${node.waitingCustomerCount}</text>
                ]
                [
                <text class="-consultant-status-count" data-value="0" data-hunt="${node.queueNameResponse.name}">${node.processingCustomerCount}</text>
                ]
            </div>
            <div class="user-ivr-wrap">
                <h4 class="user-ivr-title">${g.htmlQuote(node.queueNameResponse.hanName)}</h4>
                <dl class="user-ivr-ul">
                    <c:choose>
                        <c:when test="${node.queueMemberList != null && node.queueMemberList.size() > 0}">
                            <c:forEach var="person" items="${node.queueMemberList}">
                                <dd class="box">
                                    <div class="name">${g.htmlQuote(person.idName)}(${g.htmlQuote(person.extension)})</div>
                                    <div class="state -status-label ${person.paused != null && person.paused == 0 ? 'wait' : null}" data-peer="${g.htmlQuote(person.peer)}">
                                            ${g.htmlQuote(statusCodes.get(person.paused))}
                                    </div>
                                </dd>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <%--<tr>
                                <td colspan="2" class="null-data">소속된 상담원이 없습니다.</td>
                            </tr>--%>
                        </c:otherwise>
                    </c:choose>
                </dl>
            </div>

            <script>
                (function () {
                    if (window.queues) {
                        if (!queues['${g.escapeQuote(node.queueNameResponse.name)}']) queues['${node.queueNameResponse.name}'] = {
                            name: '${g.escapeQuote(node.queueNameResponse.name)}',
                            hanName: '${g.escapeQuote(node.queueNameResponse.hanName)}',
                            peer: []
                        };
                        queues['${g.escapeQuote(node.queueNameResponse.name)}'].waitingCustomerCount = ${node.processingCustomerCount};
                    }
                })()
            </script>
        </li>
    </c:when>
    <c:otherwise>
        <li>
            <div class="header"><span class="ui circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}[${node.waitingCustomerCount}]</div>

            <script>
                (function () {
                    if (window.queues) {
                        if (!queues['${g.escapeQuote(node.queueNameResponse.name)}']) queues['${node.queueNameResponse.name}'] = {
                            name: '${g.escapeQuote(node.queueNameResponse.name)}',
                            hanName: '${g.escapeQuote(node.queueNameResponse.hanName)}',
                            peer: []
                        };
                        queues['${g.escapeQuote(node.queueNameResponse.name)}'].waitingCustomerCount = ${node.processingCustomerCount};
                    }
                })()
            </script>
        </li>
    </c:otherwise>
</c:choose>

