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
        <tags:page-menu-tab url="/admin/record/file/enc/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">녹취암호화설정</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                        <div class="ui basic buttons">
                            <button type="button" class="ui button" onClick="encSetModal()">녹취파일 암호화 설정</button>
                            <button type="button" class="ui button" onclick="popupModal();">추가</button>
                                <%--<button type="button" class="ui basic button -control-entity" data-entity="RecordEnc" style="display: none;" onclick="popupModal(getEntityId('RecordEnc'))">수정</button>--%>
                            <button type="button" class="ui button -control-entity" data-entity="RecordEnc" style="display: none;" onclick="deleteEntity(getEntityId('RecordEnc'))">삭제</button>
                        </div>
                        <div class="ui label ml10">
                            녹취파일 암호화 설정값
                            <div class="detail">${encType.enable == 'Y' ? 'ZIP암호화' : encType.enable == 'B' ? 'AES-256 암호화' : '사용안함'}</div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/record/file/enc/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl fixed ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="RecordEnc">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>암호키 적용시간</th>
                            <th>암호키</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.id}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.encKey)}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="ui modal small" id="enc-set-modal">
        <i class="close icon"></i>
        <div class="header">
            녹취파일 암호화 설정
        </div>
        <div class="content rows">
            <div class="ui grid">
                <div class="row">
                    <div class="four wide column"><label class="control-label">설정</label></div>
                    <div class="twelve wide column">
                        <div class="ui form">
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <input name="enable" type="radio" value="Y" ${encType.enable == 'Y' ? 'checked' : null}>
                                        <label>ZIP암호화</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <input name="enable" type="radio" value="B" ${encType.enable == 'B' ? 'checked' : null}>
                                        <label>AES-256 암호화</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio checkbox">
                                        <input name="enable" type="radio" value="N" ${encType.enable == 'N' ? 'checked' : null}>
                                        <label>사용안함</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button" onclick="updateCurrentEncryptionType()">저장</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            function encSetModal() {
                $('#enc-set-modal').modalShow();
            }

            function popupModal(id) {
                popupReceivedHtml('/admin/record/file/enc/key/' + (id || 'new') + '/modal', 'modal-enc-key');
            }

            function updateCurrentEncryptionType() {
                restSelf.put('/api/record-file-enc/', {encType: $('[name=enable]:checked').val()}).done(function () {
                    alert('설정되었습니다.');
                    reload();
                });
            }

            function deleteEntity(id) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/record-file-enc/key/' + id).done(function () {
                        reload();
                    });
                });
            }
        </script>
    </tags:scripts>
</tags:tabContentLayout>
