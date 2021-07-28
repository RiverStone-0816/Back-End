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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/record/history/history/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <form:hidden path="limit"/>
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">통화이력조회</div>
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox checked">
                            <label for="_newsletter">검색옵션 전체보기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter" checked>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <table class="ui celled table compact unstackable">
                            <tr class="-detail-search-input" style="display: none;">
                                <th>검색기간</th>
                                <td colspan="7" class="-buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="ui action input calendar-area">
                                        <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                        <span class="tilde">~</span>
                                        <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        <button type="button" class="ui basic button -click-prev"><img src="<c:url value="/resources/images/calendar.svg"/>" alt="calendar"></button>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </td>
                            </tr>
                            <tr class="-detail-search-input" style="display: none;">
                                <th>부서조회</th>
                                <td colspan="3">
                                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group"
                                         data-clear=".-clear-group">
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
                                </td>
                                <th>상담원</th>
                                <td>
                                    <div class="ui form">
                                        <select name="userId">
                                            <option value="" label="선택안함"></option>
                                            <c:forEach var="e" items="${persons}">
                                                <option value="${g.htmlQuote(e.id)}">${g.htmlQuote(e.idName)}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                                <th>내선번호</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="extension">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${extensions}" itemValue="extension" itemLabel="extension"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th>전화번호</th>
                                <td>
                                    <div class="ui form">
                                        <form:input path="phone"/>
                                    </div>
                                </td>
                                <th>정렬순서</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="sort" items="${sortTypes}"/>
                                    </div>
                                </td>
                                <th>기타정보</th>
                                <td colspan="${serviceKind.contains('SC') ? '3' : '7'}">
                                    <div class="ui form flex">
                                        <form:select path="callType">
                                            <form:option value="" label="수/발신 선택"/>
                                            <form:options items="${callTypes}"/>
                                        </form:select>
                                        <form:select path="callStatus" cssClass="ml5">
                                            <form:option value="" label="호상태 선택"/>
                                            <form:options items="${callStatuses}"/>
                                        </form:select>
                                        <form:select path="etcStatus" cssClass="ml5">
                                            <form:option value="" label="부가상태 선택"/>
                                            <form:options items="${etcStatuses}"/>
                                        </form:select>
                                        <form:select path="ivrCode" cssClass="ml5">
                                            <form:option value="" label="IVR 선택"/>
                                            <form:options items="${ivrCodes}"/>
                                        </form:select>
                                        <form:select path="ivrKey" cssClass="ml5">
                                            <form:option value="${search.ivrKey}" label="${search.ivrKey}"/>
                                        </form:select>
                                    </div>
                                </td>
                            </tr>
                            <tr class="-detail-search-input" style="display: none;">
                                <th>대표번호</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="iniNum">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${services}" itemValue="svcNumber" itemLabel="svcName"/>
                                        </form:select>
                                    </div>
                                </td>
                                <th>수신그룹</th>
                                <td>
                                    <div class="ui form">
                                        <form:select path="secondNum">
                                            <form:option value="" label="선택안함"/>
                                            <form:options items="${queues}"/>
                                        </form:select>
                                    </div>
                                </td>
                                <c:if test="${serviceKind.contains('SC')}">
                                    <th>통화시간별</th>
                                    <td colspan="3">
                                        <div class="ui form flex">
                                            <div class="ip-wrap">
                                                <form:select path="byCallTime">
                                                    <form:option value="" label="선택안함"/>
                                                    <form:options items="${callTimeTypes}"/>
                                                </form:select>
                                            </div>
                                            <div class="ip-wrap">
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
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                        <div class="button-area remove-mb">
                            <div class="align-left">
                                <button class="ui button sharp light large action-button" type="button" id="select-all" onclick="selectAll()">전체 선택</button>
                                <button class="ui button sharp light large action-button" type="button" id="down-all" onclick="postBatchDownload()">녹취 일괄다운로드 등록</button>
                                <c:if test="${company.isServiceAvailable('QA')}">
                                    <button class="ui button sharp light large action-button" type="button" id="batch-evaluation" onclick="popupBatchEvaluationModal()">일괄평가</button>
                                </c:if>
                                <c:if test="${g.user.downloadRecordingAuthority.equals('ALL')}">
                                    <form:checkbox path="batchDownloadMode" cssStyle="display: none;"/>
                                    <button type="button" class="ui button sharp light large check ${search.batchDownloadMode == true ? 'active' : ''}"
                                            onclick="$(this).toggleClass('active'); $('[name=batchDownloadMode]').prop('checked', $(this).hasClass('active')); $('.-batch-download-alert').show()">
                                        일자별 녹취 다운
                                    </button>
                                    <span class="inline-txt -batch-download-alert" style="display: none;">* 일자별 녹취 다운은 최대 1일씩 다운이 가능합니다.</span>
                                </c:if>
                                <c:if test="${company.isServiceAvailable('QA') && !g.user.idType.equals('M')}">
                                    <form:checkbox path="batchEvaluationMode" cssStyle="display: none;"/>
                                    <button type="button" class="ui button sharp light large check ${search.batchEvaluationMode == true ? 'active' : ''}"
                                            onclick="$(this).toggleClass('active'); $('[name=batchEvaluationMode]').prop('checked', $(this).hasClass('active'))">상담원 일괄 평가모드
                                    </button>
                                </c:if>
                            </div>
                            <div class="align-right">
                                <button type="submit" class="ui button sharp brand large">검색</button>
                                <button type="button" class="ui button sharp light large" onclick="refreshPageWithoutParameters()">초기화</button>
                                <button type="button" class="ui button sharp light large" style="padding-left: 0.5em; padding-right: 0.5em;"
                                        onclick="$('.-detail-search-input').show(); $(this).remove()">﹀
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-total-count">전체 <span>${pagination.totalCount}</span> 건</h3>
                        <button class="ui button sharp light large excel action-button excel-down-button" type="button" id="excel-down" onclick="downloadExcel()">엑셀 다운로드</button>
                    </div>
                    <div class="pull-right">
                        <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/record/history/history/" pageForm="${search}"/>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table border-top num-tbl unstackable ${pagination.rows.size() > 0 ? "selectable" : null}" data-entity="RecordHistory">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>서비스명</th>
                            <th>발신번호</th>
                            <th>수신번호</th>
                            <th>시간</th>
                            <th>수/발신</th>
                            <c:if test="${serviceKind.equals('SC')}">
                                <th>고객등급</th>
                            </c:if>
                            <th>상담원</th>
                            <th>호상태(초)</th>
                            <th>부가상태</th>
                            <th>IVR</th>
                            <th>종료</th>
                            <th>녹취</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <c:set var="isFile" value="${g.htmlQuote(e.callStatusValue.contains('정상통화')) and fn:length(g.htmlQuote(e.recordFile)) > 0}"/>
                                    <tr data-id="${e.seq}" data-file="${isFile}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.service.svcName)}</td>
                                        <td>${g.htmlQuote(e.src)}</td>
                                        <td>${g.htmlQuote(e.dst)}</td>
                                        <td><fmt:formatDate value="${e.ringDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>${g.htmlQuote(e.callKindValue)}</td>
                                        <c:if test="${serviceKind.equals('SC')}">
                                            <td>
                                                <c:if test="${e.grade != null}">
                                                    <c:set var="colorInfo" value="${e.grade.contains('V') ? 'blue' : 'red'}"/>
                                                    <span class="ui ${colorInfo} basic label mini compact sparkle-${colorInfo}" style="line-height: 15px; width: 47px;">
                                                            ${e.grade.contains('V') ? 'VIP' : 'BLACK'}
                                                    </span>
                                                </c:if>
                                            </td>
                                        </c:if>
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
                                        <td>
                                            <div class="popup-element-wrap">
                                                <c:if test="${isFile}">
                                                    <c:if test="${!user.listeningRecordingAuthority.equals('NO')}">
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
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${!user.removeRecordingAuthority.equals('NO') and isFile}">
                                                    <c:if test="${user.removeRecordingAuthority.equals('ALL') or user.removeRecordingAuthority.equals('GROUP') && e.personList.groupCode.equals(user.groupCode) or user.removeRecordingAuthority.equals('MY') && e.personList.id.equals(user.id)}">
                                                        <button class="ui icon button mini compact translucent" title="녹취 삭제" onclick="deleteRecord(${e.seq})">
                                                            <i class="cancel alternate icon"></i>
                                                        </button>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${company.isServiceAvailable('QA') and isFile}">
                                                    <button class="ui icon button mini compact translucent" title="상담원평가" onclick="popupEvaluationModal(${e.seq})">
                                                        <i class="pencil alternate icon"></i>
                                                    </button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="13" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

    <div class="ui modal xlarge" id="modal-consultant-rating">
        <i class="close icon"></i>
        <div class="header">
            상담원 평가
        </div>
        <div class="actions">
            <button class="ui button left floated">전송</button>
            <button class="ui button modal-close">취소</button>
            <button class="ui blue button">저장</button>
        </div>
    </div>

    <tags:scripts>
        <script>
            const ivrKeys = {<c:forEach var="e" items="${ivrKeys}">'${g.escapeQuote(e.key)}': {<c:forEach var="ivr" items="${e.value}">'${g.escapeQuote(ivr.type)}_${g.escapeQuote(ivr.button)}': '${g.escapeQuote(ivr.button)}번버튼', </c:forEach>}, </c:forEach>};

            const searchForm = $('#search-form');
            searchForm.find('[name=ivrCode]').change(function () {
                const ivrKey = searchForm.find('[name=ivrKey]');
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

            searchForm.find('[name=byCallTime]').change(function () {
                const value = $(this).val();

                if (value === 'ONE_LE') {
                    searchForm.find('[name=callStartMinutes]').val('');
                    searchForm.find('[name=callStartSeconds]').val('');
                    searchForm.find('[name=callEndMinutes]').val(1);
                    searchForm.find('[name=callEndSeconds]').val(0);
                } else if (value === 'ONE_TWO') {
                    searchForm.find('[name=callStartMinutes]').val(1);
                    searchForm.find('[name=callStartSeconds]').val(0);
                    searchForm.find('[name=callEndMinutes]').val(2);
                    searchForm.find('[name=callEndSeconds]').val(0);
                } else if (value === 'TWO_THREE') {
                    searchForm.find('[name=callStartMinutes]').val(2);
                    searchForm.find('[name=callStartSeconds]').val(0);
                    searchForm.find('[name=callEndMinutes]').val(3);
                    searchForm.find('[name=callEndSeconds]').val(0);
                } else if (value === 'THREE_FIVE') {
                    searchForm.find('[name=callStartMinutes]').val(3);
                    searchForm.find('[name=callStartSeconds]').val(0);
                    searchForm.find('[name=callEndMinutes]').val(5);
                    searchForm.find('[name=callEndSeconds]').val(0);
                } else if (value === 'FIVE_TEN') {
                    searchForm.find('[name=callStartMinutes]').val(5);
                    searchForm.find('[name=callStartSeconds]').val(0);
                    searchForm.find('[name=callEndMinutes]').val(10);
                    searchForm.find('[name=callEndSeconds]').val(0);
                } else if (value === 'TEN_THIRTY') {
                    searchForm.find('[name=callStartMinutes]').val(10);
                    searchForm.find('[name=callStartSeconds]').val(0);
                    searchForm.find('[name=callEndMinutes]').val(30);
                    searchForm.find('[name=callEndSeconds]').val(0);
                } else if (value === 'THIRTY_GE') {
                    searchForm.find('[name=callStartMinutes]').val(30);
                    searchForm.find('[name=callStartSeconds]').val(0);
                    searchForm.find('[name=callEndMinutes]').val('');
                    searchForm.find('[name=callEndSeconds]').val('');
                } else {
                    searchForm.find('[name=callStartMinutes]').val('');
                    searchForm.find('[name=callStartSeconds]').val('');
                    searchForm.find('[name=callEndMinutes]').val('');
                    searchForm.find('[name=callEndSeconds]').val('');
                }
            });

            $('.-popup-records').click(function (event) {
                event.stopPropagation();

                const $this = $(this);
                if ($this.attr('data-has-records'))
                    return;

                popupReceivedHtml(contextPath + '/admin/record/history/history/' + $this.attr('data-id') + '/modal-records', 'modal-records').done(function (html) {
                    const mixedNodes = $.parseHTML(html, null, true);

                    const modal = (function () {
                        for (let i = 0; i < mixedNodes.length; i++) {
                            const node = $(mixedNodes[i]);
                            if (node.is('#modal-records'))
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

            function deleteRecord(seq) {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    restSelf.delete('/api/record-history/' + seq).done(function () {
                        reload();
                    });
                });
            }

            function popupEvaluationModal(seq) {
                popupReceivedHtml('/admin/record/history/history/' + seq + '/modal-evaluation', 'modal-evaluation');
            }

            function popupSttModal(seq) {
                popupReceivedHtml('/admin/record/history/history/' + seq + '/modal-stt', 'modal-stt');
            }

            function downloadExcel() {
                restSelf.put('/api/web-log/down/SEARCH').done(function () {
                    window.open(contextPath + '/admin/record/history/history/_excel?${g.escapeQuote(search.query)}', '_blank');
                });
            }

            function selectAll() {
                $('table[data-entity="RecordHistory"] tr[data-id]').addClass('active');
            }

            function postBatchDownload() {
                const sequences = [];
                $('table[data-entity="RecordHistory"] tr[data-id].active').each(function () {
                    if ($(this).data('file'))
                        sequences.push($(this).attr('data-id'));
                });
                if (sequences.length === 0)
                    return alert('데이터를 선택해주세요');
                popupReceivedHtml($.addQueryString('/admin/record/history/history/modal-batch-download', {sequences: sequences}), 'modal-batch-download');
            }

            function popupBatchEvaluationModal() {
                const seq = [];
                $('table[data-entity="RecordHistory"] tr[data-id].active').each(function () {
                    if ($(this).data('file'))
                        seq.push($(this).attr('data-id'));
                });
                if (seq.length === 0)
                    return alert('데이터를 선택해주세요');
                popupReceivedHtml($.addQueryString('/admin/record/history/history/modal-batch-evaluation', {seq: seq}), 'modal-batch-evaluation');
            }

            function showActionButton() {
                if (searchForm.find('[name=batchDownloadMode]').prop('checked')) {
                    $("#excel-down").hide();
                    if (!searchForm.find('[name=batchEvaluationMode]').prop('checked'))
                        $("#batch-evaluation").hide();
                } else {
                    $("#down-all").hide();
                    if (searchForm.find('[name=batchEvaluationMode]').prop('checked'))
                        $("#excel-down").hide();
                    else {
                        $("#select-all").hide();
                        $("#batch-evaluation").hide();
                    }
                }
            }

            function changePaginationLimit() {
                if (searchForm.find('[name=batchDownloadMode]').prop('checked'))
                    searchForm.find('[name=limit]').val(1000);
                else
                    searchForm.find('[name=limit]').val(30);
            }

            showActionButton();
            searchForm.find('[name=batchDownloadMode]').on("change", function () {
                changePaginationLimit();
                searchForm.submit();
            });

            searchForm.find('[name=batchEvaluationMode]').on("change", function () {
                searchForm.submit();
            });

        </script>
    </tags:scripts>
</tags:tabContentLayout>
