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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="sub-content ui container fluid unstackable" id="modal-search-call-history-body">
<form:form id="search-call-history-form" modelAttribute="search" method="get" class="panel panel-search -ajax-loader"
           action="${pageContext.request.contextPath}/counsel/modal-search-call-history-body"
           data-target="#modal-search-call-history-body">
        <div class="panel-heading">
            <div class="pull-left">
                검색
            </div>
            <div class="pull-right">
                <div class="btn-wrap">
                    <button type="submit" class="ui brand basic button">검색</button>
                    <button type="button" class="ui grey basic button" onclick="modalReload()">초기화</button>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <div class="search-area">
                <div class="ui grid">
                    <div class="row">
                        <div class="three wide column"><label class="control-label">검색기간</label></div>
                        <div class="thirteen wide column">
                            <div class="date-picker from-to">
                                <div class="dp-wrap">
                                    <label class="control-label" for="startDate" style="display:none">From</label>
                                    <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                    <span class="ui form inline">
                                                <form:select path="startHour">
                                                    <c:forEach var="e" begin="0" end="23">
                                                        <form:option value="${e}" label="${e}시"/>
                                                    </c:forEach>
                                                </form:select>
                                            </span>
                                </div>
                                <span class="tilde">~</span>
                                <div class="dp-wrap">
                                    <label class="control-label" for="endDate" style="display:none">to</label>
                                    <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                    <span class="ui form inline">
                                                <form:select path="endHour">
                                                    <c:forEach var="e" begin="0" end="23">
                                                        <form:option value="${e}" label="${e}시"/>
                                                    </c:forEach>
                                                </form:select>
                                            </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">부서조회</label></div>
                        <div class="six wide column">
                            <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                                <button type="button" class="ui icon button mini brand compact -select-group">
                                    <i class="search icon"></i>
                                </button>
                                <form:hidden path="groupCode"/>
                                <div class="ui breadcrumb -group-name">
                                    <c:choose>
                                        <c:when test="${searchOrganizationNames != null && searchOrganizationNames.size() > 0}">
                                            <c:forEach var="e" items="${searchOrganizationNames}" varStatus="status">
                                                <span class="section">${g.htmlQuote(e)}</span>
                                                <c:if test="${!status.last}">
                                                    <i class="right angle icon divider"></i>
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="section">부서를 선택해 주세요.</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <button type="button" class="ui icon button mini compact -clear-group">
                                    <i class="undo icon"></i>
                                </button>
                            </div>
                        </div>
                            <%--<div class="two wide column"><label class="control-label">고객등급</label></div>
                            <div class="two wide column">
                                <div class="ui form">
                                    <form:select path="customerRating">
                                        <form:option value="" label="선택안함"/>
                                        <form:options items="${customerGrades}"/>
                                    </form:select>
                                </div>
                            </div>--%>
                        <div class="three wide column"><label class="control-label">상담원</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="userId">
                                    <form:option value="" label="선택안함"/>
                                    <form:options items="${persons}" itemValue="id" itemLabel="idName"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">내선번호</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="extension">
                                    <form:option value="" label="선택안함"/>
                                    <form:options items="${extensions}" itemValue="extension" itemLabel="inUseIdName"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="three wide column"><label class="control-label">전화번호</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:input path="phone"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">헌트경로</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="iniNum">
                                    <form:option value="" label="선택안함"/>
                                    <form:options items="${queues}"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="sort" items="${sortTypes}"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">기타선택</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="callType">
                                    <form:option value="" label="수신/발신"/>
                                    <form:options items="${callTypes}"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="callStatus">
                                    <form:option value="" label="호상태 선택"/>
                                    <form:options items="${callStatuses}"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="etcStatus">
                                    <form:option value="" label="부가상태선택"/>
                                    <form:options items="${etcStatuses}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="ivrCode">
                                    <form:option value="" label="IVR선택"/>
                                    <form:options items="${ivrCodes}"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="ivrKey">
                                    <form:option value="${search.ivrKey}" label="${search.ivrKey}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="three wide column"><label class="control-label">통화시간별</label></div>
                        <div class="three wide column">
                            <div class="ui form">
                                <form:select path="byCallTime">
                                    <form:option value="" label="시간대선택"/>
                                    <form:options items="${callTimeTypes}"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="seven wide column">
                            <div class="ui input"><form:input path="callStartMinutes" size="2" class="-input-numerical"/></div>
                            <span>분</span>
                            <div class="ui input"><form:input path="callStartSeconds" size="2" class="-input-numerical"/></div>
                            <span>초</span>
                            <span class="tilde">~</span>
                            <div class="ui input"><form:input path="callEndMinutes" size="2" class="-input-numerical"/></div>
                            <span>분</span>
                            <div class="ui input"><form:input path="callEndSeconds" size="2" class="-input-numerical"/></div>
                            <span>초</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    <div class="panel">
        <div class="panel-heading">
            <div class="pull-left">
                <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
            </div>
            <div class="pull-right">
                <button type="button" class="ui basic green button" onclick="setCustomInfo()">고객정보 내보내기</button>
            </div>
        </div>
        <div class="panel-body" style="width: 100%; overflow-x: auto;">
            <table class="ui celled table structured compact unstackable ${pagination.rows.size() > 0 ? "selectable-only" : null}" data-entity="RecordHistory">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>대표서비스</th>
                    <%--<th>고객등급</th>--%>
                    <th>녹취</th>
                    <th>발신번호</th>
                    <th>수신번호</th>
                    <th>시간</th>
                    <th>수/발신</th>
                    <th>상담원</th>
                    <th>호상태(초)</th>
                    <th>부가상태</th>
                    <th>IVR</th>
                    <th>종료</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${pagination.rows.size() > 0}">
                        <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                            <c:set var="isFile" value="${g.htmlQuote(e.callStatusValue.contains('정상통화')) and fn:length(g.htmlQuote(e.recordFile)) > 0}"/>
                            <tr data-id="${e.seq}" data-phone-number="${e.inOut.contains('I') ? e.src : e.dst}">
                                <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                <td>${g.htmlQuote(e.service.svcName)}</td>
                                    <%--<td>${e.vipBlack == 'V' ? 'VIP' : e.vipBlack == 'B' ? '블랙리스트' : ''}</td>--%>
                                <td>
                                    <c:if test="${isFile}">
                                        <div class="popup-element-wrap">
                                            <c:if test="${user.listeningRecordingAuthority.equals('MY') && e.userid.equals(user.id)}">
                                                <button type="button" class="ui icon button mini compact -popup-records" data-id="${e.seq}">
                                                    <i class="volume up icon"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${user.listeningRecordingAuthority.equals('GROUP') && e.personList.groupCode.equals(user.groupCode)}">
                                                <button type="button" class="ui icon button mini compact -popup-records" data-id="${e.seq}">
                                                    <i class="volume up icon"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${user.listeningRecordingAuthority.equals('ALL')}">
                                                <button type="button" class="ui icon button mini compact -popup-records" data-id="${e.seq}">
                                                    <i class="volume up icon"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </c:if>
                                </td>
                                <td>${g.htmlQuote(e.src)}</td>
                                <td>${g.htmlQuote(e.dst)}</td>
                                <td><fmt:formatDate value="${e.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td >${g.htmlQuote(e.callKindValue)}</td>
                                <td>
                                    <c:set var="userName" value="${e.personList != null && e.personList.idName != null && e.personList.idName.length() > 0 ? e.personList.idName : null}"/>
                                    <c:choose>
                                        <c:when test="${userName != null}">${g.htmlQuote(userName)}</c:when>
                                        <c:otherwise>${g.htmlQuote(e.userid)}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${g.htmlQuote(e.callStatusValue)}</td>
                                <td>${g.htmlQuote(e.etcCallResultValue)}</td>
                                <td>${g.htmlQuote(e.ivrPathValue)}</td>
                                <td>${g.htmlQuote(e.callingHangupValue)}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="12" class="null-data">조회된 데이터가 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</form:form>
</div>

<script>
    function modalReload() {
        $('#modal-menu').modalHide();
        popupDraggableModalFromReceivedHtml('/counsel/modal-search-call-history', 'modal-search-call-history');
    }

    (function () {
        const ivrKeys = {<c:forEach var="e" items="${ivrKeys}">'${g.escapeQuote(e.key)}': {<c:forEach var="ivr" items="${e.value}">'${g.escapeQuote(ivr.type)}_${g.escapeQuote(ivr.button)}': '${g.escapeQuote(ivr.button)}번버튼', </c:forEach>}, </c:forEach>};

        const modal = $('#search-call-history-form').closest('.modal');
        modal.find('[name=ivrCode]').change(function () {
            const ivrKey = modal.find('[name=ivrKey]');
            const ivrKeyValue = ivrKey.val();
            const ivrCode = $(this).val();

            ivrKey.empty();

            if (!ivrCode) return;

            const options = ivrKeys[ivrCode];
            for (let value in options)
                if (options.hasOwnProperty(value))
                    ivrKey.append($('<option/>', {value: value, text: options[value]}));

            ivrKey.find('option').filter(function () {
                return ivrKeyValue === $(this).val();
            }).prop('selected', true);
        }).change();

        modal.find('[name=byCallTime]').change(function () {
            const value = $(this).val();

            if (value === 'ONE_LE') {
                modal.find('[name=callStartMinutes]').val('');
                modal.find('[name=callStartSeconds]').val('');
                modal.find('[name=callEndMinutes]').val(1);
                modal.find('[name=callEndSeconds]').val(0);
            } else if (value === 'ONE_TWO') {
                modal.find('[name=callStartMinutes]').val(1);
                modal.find('[name=callStartSeconds]').val(0);
                modal.find('[name=callEndMinutes]').val(2);
                modal.find('[name=callEndSeconds]').val(0);
            } else if (value === 'TWO_THREE') {
                modal.find('[name=callStartMinutes]').val(2);
                modal.find('[name=callStartSeconds]').val(0);
                modal.find('[name=callEndMinutes]').val(3);
                modal.find('[name=callEndSeconds]').val(0);
            } else if (value === 'THREE_FIVE') {
                modal.find('[name=callStartMinutes]').val(3);
                modal.find('[name=callStartSeconds]').val(0);
                modal.find('[name=callEndMinutes]').val(5);
                modal.find('[name=callEndSeconds]').val(0);
            } else if (value === 'FIVE_TEN') {
                modal.find('[name=callStartMinutes]').val(5);
                modal.find('[name=callStartSeconds]').val(0);
                modal.find('[name=callEndMinutes]').val(10);
                modal.find('[name=callEndSeconds]').val(0);
            } else if (value === 'TEN_THIRTY') {
                modal.find('[name=callStartMinutes]').val(10);
                modal.find('[name=callStartSeconds]').val(0);
                modal.find('[name=callEndMinutes]').val(30);
                modal.find('[name=callEndSeconds]').val(0);
            } else if (value === 'THIRTY_GE') {
                modal.find('[name=callStartMinutes]').val(30);
                modal.find('[name=callStartSeconds]').val(0);
                modal.find('[name=callEndMinutes]').val('');
                modal.find('[name=callEndSeconds]').val('');
            } else {
                modal.find('[name=callStartMinutes]').val('');
                modal.find('[name=callStartSeconds]').val('');
                modal.find('[name=callEndMinutes]').val('');
                modal.find('[name=callEndSeconds]').val('');
            }
        });

        modal.find('.-popup-records').click(function (event) {
            event.stopPropagation();

            const $this = $(this);
            if ($this.attr('data-has-records'))
                return;

            popupReceivedHtml(contextPath + '/admin/record/history/history/' + $this.attr('data-id') + '/modal-records', 'modal-records').done(function (html) {
                const mixedNodes = $.parseHTML(html, null, true);

                const modal = (function () {
                    for (let i = 0; i < mixedNodes.length; i++) {
                        const node = $(mixedNodes[i]);
                        if (node.is('.ui.modal'))
                            return node;
                    }
                    throw 'cannot find modal element';
                })();

                $this.after(modal);
                modal.find('audio').each(function () {
                    maudio({obj: this});
                });
            });
        });
    })();

    function setCustomInfo() {
        const phoneNumber = getEntityId('RecordHistory', 'phone-number');
        if (!phoneNumber)
            return;

        // $('#counseling-target').text(phoneNumber);

        loadCustomInput(null, null, phoneNumber);
        $('#search-call-history-form').closest('.modal').modalHide();
    }
</script>
