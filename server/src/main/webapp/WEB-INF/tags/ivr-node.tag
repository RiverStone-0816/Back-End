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

<%@ attribute name="node" required="true" type="kr.co.eicn.ippbx.server.model.dto.eicn.PDSIvrResponse" %>

<div class="item">
    <div class="content">
        <div class="header"><span class="ui grey circular label tiny">${node.button}</span>${g.htmlQuote(node.name)}</div>
        <c:if test="${node.type == 5 && node.nodes.size() > 0}">
            <c:set var="child" value="${node.nodes[0]}"/>
            <div class="list">
                <div class="item">
                    <i class="folder icon"></i>
                    <div class="content">
                        <div class="header">${child.name}
                            <div class="ui buttons ivr-control">
                                <c:if test="${node.soundCode != null && node.soundCode != ''}">
                                    <button type="button" class="ui button mini compact -play-trigger" data-target="#ivr-sound-${node.seq}">음원듣기</button>
                                </c:if>
                                <button type="button" class="ui button mini compact" onclick="popupKeyMapModal(${child.seq})">버튼맵핑</button>
                                <button type="button" class="ui button mini compact" onclick="deleteEntity(${child.code})">삭제</button>
                            </div>
                            <c:if test="${node.soundCode != null && node.soundCode != ''}">
                                <div class="ui popup top right" id="ivr-sound-${node.seq}">
                                    <div class="maudio">
                                        <audio controls src="${apiServerUrl}/api/v1/admin/sounds/ars/${node.soundCode}/resource?token=${accessToken}"></audio>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <div class="list">
                            <c:forEach var="grandchild" items="${child.nodes}">
                                <tags:ivr-node node="${grandchild}"/>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>