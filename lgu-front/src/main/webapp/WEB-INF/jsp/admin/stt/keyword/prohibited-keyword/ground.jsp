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
        <tags:page-menu-tab url="/admin/stt/keyword/prohibited-keyword/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <text style="color: red">* 클릭하면 삭제 할수 있습니다.</text>
                    </div>
                    <div class="pull-right">
                        추천 <input type="text" class="-keyword">
                        <button class="ui basic button" onclick="keywordInsertEntity()">추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable fixed">
                        <tbody>
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${list.size() > 0}">
                                            <c:forEach var="e" items="${list}" varStatus="status">
                                                <c:choose>
                                                    <c:when test="${e.keywordYn == 'Y'}">
                                                        <button type="button" class="ui button left floated"
                                                                style="border-radius: 0.99rem;background-color: rgb(1,26,253); color: white; font-family: none; margin-bottom: 0.25rem"
                                                                onclick="deleteEntity('${e.keyword}')">${e.keyword}</button>
                                                    </c:when>
                                                </c:choose>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            등록된 추천키워드가 없습니다.
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <text style="color: red">* 클릭하면 삭제 할수 있습니다.</text>
                    </div>
                    <div class="pull-right">
                        금지 <input type="text" class="-prohibit">
                        <button class="ui basic button" onclick="prohibitInsertEntity()">추가</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable fixed">
                        <tbody>
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${list.size() > 0}">
                                        <c:forEach var="e" items="${list}" varStatus="status">
                                            <c:choose>
                                                <c:when test="${e.prohibitYn == 'Y'}">
                                                    <button type="button" class="ui button left floated"
                                                            style="border-radius: 0.99rem;background-color: rgb(255, 0, 0); color: white; font-family: none; margin-bottom: 0.25rem"
                                                            onclick="deleteEntity('${e.keyword}')">${e.keyword}</button>
                                                </c:when>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        등록된 금지키워드가 없습니다.
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <tags:scripts>
        <script>
            function keywordInsertEntity() {
                const keyword = $('.-keyword').val();
                confirm(keyword + '를(을) 추가 합니다.').done(function() {
                    restSelf.post('/api/stt/keyword/prohibited-keyword/keyword/'+keyword, null).done(function() {
                        reload();
                    })
                });
            }

            function prohibitInsertEntity() {
                const keyword = $('.-prohibit').val();
                confirm(keyword + '를(을) 추가 합니다.').done(function() {
                    restSelf.post('/api/stt/keyword/prohibited-keyword/prohibit/'+keyword, null).done(function() {
                        reload();
                    })
                });
            }

            function deleteEntity(keyword) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/stt/keyword/prohibited-keyword/' + keyword).done(function () {
                        reload();
                    });
                });
            }

            function reload() {
                location.href = contextPath + '/admin/stt/keyword/prohibited-keyword/';
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>