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
        <tags:page-menu-tab url="/admin/record/callback/history/"/>
        <div class="sub-content ui container fluid unstackable">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        검색
                    </div>
                    <div class="pull-right">
                        <div class="ui slider checkbox">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">검색기간</label></div>
                                <div class="ten wide column -buttons-set-range-container" data-startdate="[name=startDate]" data-enddate="[name=endDate]">
                                    <div class="date-picker from-to">
                                        <div class="dp-wrap">
                                            <label class="control-label" for="startDate" style="display:none">From</label>
                                            <form:input path="startDate" cssClass="-datepicker" placeholder="시작일"/>
                                        </div>
                                        <span class="tilde">~</span>
                                        <div class="dp-wrap">
                                            <label class="control-label" for="endDate" style="display:none">to</label>
                                            <form:input path="endDate" cssClass="-datepicker" placeholder="종료일"/>
                                        </div>
                                    </div>
                                    <div class="ui basic buttons">
                                        <button type="button" data-interval="day" data-number="1" class="ui button -button-set-range">당일</button>
                                        <button type="button" data-interval="day" data-number="3" class="ui button -button-set-range">3일</button>
                                        <button type="button" data-interval="day" data-number="7" class="ui button -button-set-range">1주일</button>
                                        <button type="button" data-interval="month" data-number="1" class="ui button -button-set-range">1개월</button>
                                        <button type="button" data-interval="month" data-number="3" class="ui button -button-set-range">3개월</button>
                                        <button type="button" data-interval="month" data-number="6" class="ui button -button-set-range">6개월</button>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">인입경로</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="svcNumber">
                                            <form:option value="" label="선택"/>
                                            <form:options items="${serviceOptions}"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="two wide column"><label class="control-label">처리상태</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:select path="status">
                                            <form:option value="" label="선택"/>
                                            <form:options items="${callbackStatusOptions}"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">고객번호</label></div>
                                <div class="two wide column">
                                    <div class="ui form">
                                        <form:input path="callbackNumber"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">전체 <span class="text-primary">${pagination.totalCount}</span> 건</h3>
                    </div>
                    <div class="pull-right">
                        <button class="ui basic green button excel-down-button" type="button" onclick="downloadExcel()">Excel 다운로드</button>
                        <button class="ui basic button -control-entity" data-entity="CallbackHistory" style="display: none;" onclick="popupRedistributionModal()">재분배</button>
                        <button class="ui basic button -control-entity" data-entity="CallbackHistory" style="display: none;" onclick="deleteCallbackHistories()">삭제</button>
                        <button class="ui basic button -control-entity" data-entity="CallbackHistory" style="display: none;" onclick="popupProcessModal()">처리</button>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table compact unstackable num-tbl ${pagination.rows.size() > 0 ? "selectable" : null}" data-entity="CallbackHistory">
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>수신번호</th>
                            <th>콜백번호</th>
                            <th>인입서비스</th>
                            <th>인입큐</th>
                            <th>상담원</th>
                            <th>처리상태</th>
                            <th>입력일시</th>
                            <th>처리일시</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${pagination.rows.size() > 0}">
                                <c:forEach var="e" items="${pagination.rows}" varStatus="status">
                                    <tr data-id="${e.seq}">
                                        <td>${(pagination.page - 1) * pagination.numberOfRowsPerPage + status.index + 1}</td>
                                        <td>${g.htmlQuote(e.callerNumber)}</td>
                                        <td>${g.htmlQuote(e.callbackNumber)}</td>
                                        <td class="${e.svcName != null && e.svcName != "" ? "" : "-ivr-chk"}">${e.svcName != null && e.svcName != "" ? g.htmlQuote(e.svcName) : e.ivrkey}</td>
                                        <td>${g.htmlQuote(e.queueName)}</td>
                                        <td>${g.htmlQuote(e.idName)}</td>
                                        <td>${e.status != null ? g.htmlQuote(message.getEnumText(e.status)) : null}</td>
                                        <td><fmt:formatDate value="${e.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td><fmt:formatDate value="${e.resultDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" class="null-data">조회된 데이터가 없습니다.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <tags:pagination navigation="${pagination.navigation}" url="${pageContext.request.contextPath}/admin/record/callback/history/" pageForm="${search}"/>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            function downloadExcel() {
                restSelf.put('/api/web-log/down/CALLBACK').done(function () {
                    window.open(contextPath + '/admin/record/callback/history/_excel?${g.escapeQuote(search.query)}', '_blank');
                });
            }

            function deleteCallbackHistories() {
                confirm('정말 삭제하시겠습니까?').done(function () {
                    const list = [];
                    $('table[data-entity="CallbackHistory"] tr.active').each(function () {
                        list.push($(this).attr('data-id'));
                    });
                    if (!list.length) return;

                    restSelf.delete('/api/callback-history/', {serviceSequence: list}).done(function () {
                        reload();
                    });
                });
            }

            function popupRedistributionModal() {
                const list = [];
                $('table[data-entity="CallbackHistory"] tr.active').each(function () {
                    list.push($(this).attr('data-id'));
                });
                if (!list.length) return;

                const url = $.addQueryString('/admin/record/callback/history/modal-redistribution', {serviceSequences: list});
                popupReceivedHtml(url, 'modal-redistribution');
            }

            function popupProcessModal() {
                const list = [];
                $('table[data-entity="CallbackHistory"] tr.active').each(function () {
                    list.push($(this).attr('data-id'));
                });
                if (!list.length) return;

                const url = $.addQueryString('/admin/record/callback/history/modal-process', {serviceSequences: list});
                popupReceivedHtml(url, 'modal-process');
            }

            $(window).on('load', function () {
                restSelf.get('/api/ivr/').done(function (response) {
                    $(".-ivr-chk").each(function(){
                        const textHTML = textCheckReturn(response.data, this.innerHTML, "", false);
                        this.innerHTML = textHTML;
                    });
                });
            })

            //ivr 체크하는 재귀함수.. 코드정리 나중에..
            function textCheckReturn(v, Strings, text, key){
                let obj = v;
                let obj2 = [];
                let temp2 = text;
                let temp3 = "";
                let texts = Strings.split("-");
                if(Strings.indexOf("-") > -1){
                    let item = texts[0].split("_");
                    let StringsTemp = "";
                    if(texts.length > 0) {
                        for (let i = 1; i < texts.length; i++) {
                            StringsTemp = StringsTemp + texts[i] + "-";
                        }
                        temp3 = StringsTemp.substring(0, StringsTemp.length - 1);
                    }else{
                        temp3 = ""
                    }
                    if(texts[0].indexOf("_") > -1) {
                        temp2 = temp2 + "["
                        for (let i = 0; i < obj.length; i++) {
                            if (obj[i].code == item[0]) {
                                temp2 = temp2 + obj[i].name + "_";
                                if (obj[i].type==item[1]) {
                                    temp2 = temp2 + obj[i].type + "_";
                                } else if (item[1]=="exconnect") {
                                    temp2 = temp2 + "예외처리_";
                                }
                                let chkbuttonInx = 0;
                                for(let j = 0;j < obj[i].nodes.length; j++){
                                    if(obj[i].nodes[j].button == item[2]){
                                        chkbuttonInx = j;
                                        temp2 = temp2 + obj[i].nodes[j].name;
                                    }
                                }
                                obj2 = obj[i].nodes[chkbuttonInx].nodes;
                                temp2 = temp2 + "]";
                                break;
                            }
                        }
                    }else{
                        obj2 = obj;
                    }
                }else{
                    let item = Strings.split("_");
                    if(Strings.indexOf("_") > -1) {
                        temp2 = temp2 + "["
                        for (let i = 0; i < obj.length; i++) {
                            if (obj[i].code == item[0]) {
                                temp2 = temp2 + obj[i].name + "_";
                                if (obj[i].type == item[1]) {
                                    temp2 = temp2 + obj[i].type + "_";
                                } else if (item[1] == "exconnect") {
                                    temp2 = temp2 + "예외처리_";
                                }
                                for(let j = 0;j < obj[i].nodes.length; j++){
                                    if(obj[i].nodes[j].button == item[2]){
                                        temp2 = temp2 + obj[i].nodes[j].name;
                                    }
                                }
                                temp2 = temp2 + "]";
                                break;
                            }
                        }
                        temp2 = "IVR : " + temp2;
                    }else{
                        temp2 = Strings;
                    }
                    key = true;
                }
                if(key) return temp2;
                return textCheckReturn(obj2, temp3, temp2, key);
            }

        </script>
    </tags:scripts>
</tags:tabContentLayout>
