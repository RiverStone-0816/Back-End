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

<form:form modelAttribute="form" cssClass="ui modal -json-submit" data-method="${entity != null ? 'put' : 'post'}"
           action="${pageContext.request.contextPath}/api/queue/${entity != null ? g.htmlQuote(entity.name) : null}"
           data-before="prepareWriteFormData" data-done="reload">

    <i class="close icon"></i>
    <div class="header">큐(그룹)[${entity != null ? '수정' : '추가'}]<span> ※ 큐는 유저 아이디가 아닌 내선기준입니다.</span></div>

    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">큐그룹명</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="hanName"/></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">큐번호</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="number">
                            <form:option value="" label="지정안함"/>
                            <form:options items="${numbers}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">관련서비스</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="svcNumber">
                            <form:option value="" label="지정안함"/>
                            <form:options items="${services}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">부서선택</label></div>
                <div class="twelve wide column">
                    <div class="ui form organization-select -select-group-container" data-input="[name=groupCode]" data-name=".-group-name" data-select=".-select-group" data-clear=".-clear-group">
                        <button type="button" class="ui icon button mini blue compact -select-group">
                            <i class="search icon"></i>
                        </button>
                        <form:hidden path="groupCode"/>
                        <div class="ui breadcrumb -group-name">
                            <c:choose>
                                <c:when test="${entity != null}">
                                    <c:forEach var="e" items="${entity.companyTrees}" varStatus="status">
                                        <span class="section">${g.htmlQuote(e.groupName)}</span>
                                        <c:if test="${!status.last}">
                                            <i class="right angle icon divider"></i>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <span class="section">버튼을 눌러 소속을 선택하세요.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="ui icon button mini compact -clear-group">
                            <i class="undo icon"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">통화분배정책</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="strategy">
							<c:forEach var="e" items="${strategyOptions}">
                                <form:option value="${e.key}" label="${e.value}(${e.key})"/>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">최대대기자명수</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="maxlen" cssClass="-input-numerical"/><span>명</span></div>
                </div>
                <div class="eight wide column"></div>
                <div class="four wide column"></div>
                <div class="twelve wide column">
                    주의: 값을 0으로 셋팅하면 모든상담원이 대기가 아닐때 예비큐/콜백으로 바로 넘어감
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">큐 총대기시간</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="queueTimeout" cssClass="-input-numerical"/><span>초</span></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">사용자별 대기시간</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><form:input path="timeout" cssClass="-input-numerical"/><span>초 (15초이상)</span></div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">대기음원</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="musiconhold">
                            <form:option value="" label="기본링"/>
                            <form:option value="default" label="기본음악"/>
                            <c:if test="${ringBackTones.size() > 0}">
                                <c:forEach var="e" items="${ringBackTones}">
                                    <c:if test="${!e.key.equals('default')}">
                                    <form:option value="${e.key}" label="${e.value}"/>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </form:select>
                    </div>
                </div>
                <div class="four wide column">(컬러링 음원 관리에 등록)</div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">재시도 여부</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isRetry" value="false"/>
                                    <label>재시도 안함</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isRetry" value="true"/>
                                    <label>재시도 함</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -retry">
                <div class="four wide column"><label class="control-label">재시도 횟수</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="field">
                            <div class="ui input text">
                                <form:select path="retryMaxCount">
                                    <c:forEach var="e" begin="1" end="5">
                                        <form:option value="${e}">${e}번</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -retry">
                <div class="four wide column"><label class="control-label">재시도 음원</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="field">
                            <div class="ui input text">
                                <form:select path="retrySoundCode">
                                    <form:option value="" label="선택안함"/>
                                    <form:option value="TTS" label="TTS 음원"/>
                                    <c:forEach var="e" items="${sounds}">
                                        <form:option value="${e.key}" label="${e.value}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -retry" id="ttsText">
                <div class="four wide column"><label class="control-label">TTS 멘트</label></div>
                <div class="eight wide column">
                    <div class="ui form">
                        <div class="field">
                            <div class="ui input text">
                                <form:textarea path="ttsData"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -retry">
                <div class="four wide column"><label class="control-label">재시도 버튼</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <div class="field">
                            <div class="ui input fluid">
                                <form:select path="retryAction">
                                    <c:forEach var="e" begin="0" end="9">
                                        <form:option value="${e}" label="${e}"/>
                                    </c:forEach>
                                    <form:option value="#" label="#"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label">비연결시 행동</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="noConnectKind" value="NONE"/>
                                    <label>연결안함</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="noConnectKind" value="CONTEXT"/>
                                    <label>컨텍스트 연결</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="noConnectKind" value="HUNT"/>
                                    <label>예비큐 연결</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -connect-data">
                <div class="four wide column"><label class="control-label">연결 데이터</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <form:select path="noConnectData">
                            <form:option value="" label="지정안함"/>
                            <form:options items="${contexts}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="four wide column"><label class="control-label label-required">비상시 포워딩</label></div>
                <div class="twelve wide column">
                    <div class="ui form">
                        <div class="inline fields">
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isForwarding" value="N"/>
                                    <label>포워딩안함</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isForwarding" value="I"/>
                                    <label>내부포워딩</label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui radio checkbox">
                                    <form:radiobutton path="isForwarding" value="O"/>
                                    <label>외부포워딩</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -forwarding-data" data-value="I">
                <div class="four wide column"><label class="control-label label-required">내부 포워딩</label></div>
                <div class="four wide column">
                    <div class="ui form">
                        <select name="huntForwarding.I">
                            <option>지정안함</option>
                            <c:forEach var="e" items="${services}">
                                <option value="${g.htmlQuote(e.key)}" ${e.key == form.huntForwarding && form.isForwarding == 'I' ? 'selected' : ''}>[대표번호] ${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                            <c:forEach var="e" items="${queues}">
                                <option value="${g.htmlQuote(e.key)}" ${e.key == form.huntForwarding && form.isForwarding == 'I' ? 'selected' : ''}>[큐그룹] ${g.htmlQuote(e.value)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row -forwarding-data" data-value="O">
                <div class="four wide column"><label class="control-label label-required">외부 포워딩</label></div>
                <div class="four wide column">
                    <div class="ui input fluid"><input type="text" name="huntForwarding.O" value="${form.isForwarding == 'O' ? g.htmlQuote(form.huntForwarding) : ''}"/></div>
                </div>
            </div>
            <div class="row">
                <div class="eight wide column"><label class="control-label">사용자 리스트</label></div>
                <div class="eight wide column"><label class="control-label">추가된 사용자</label></div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="jp-multiselect -moving-container">
                        <div class="from-panel">
                            <select class="form-control -right-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${addOnPersons}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.extension)}[${g.htmlQuote(e.hostName)}][${g.htmlQuote(e.idName)} - ${g.htmlQuote(e.companyTrees.stream().map(f -> f.groupName).reduce((a, b) -> b).orElse(""))}]</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="move-panel">
                            <button type="button" class="btn-move-selected-right -to-left">›</button>
                            <button type="button" class="btn-move-selected-left -to-right">‹</button>
                        </div>
                        <div class="to-panel">
                            <select name="addPersons" class="form-control -left-selector" size="8" multiple="multiple">
                                <c:forEach var="e" items="${entity.addPersons}">
                                    <option value="${g.htmlQuote(e.peer)}">${g.htmlQuote(e.extension)}[${g.htmlQuote(e.hostName)}][${g.htmlQuote(e.idName)} - ${g.htmlQuote(e.companyTrees.stream().map(f -> f.groupName).reduce((a, b) -> b).orElse(""))}]</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row -strategy-sub-input-container" data-value="skill">
                <div class="sixteen wide column">스킬레벨설정</div>
                <div class="sixteen wide column">
                    <div class="items-wrap" id="skill-level-set"></div>
                </div>
            </div>
            <div class="row -strategy-sub-input-container" data-value="callrate">
                <div class="sixteen wide column">콜배율설정</div>
                <div class="sixteen wide column">
                    <div class="items-wrap" id="callrate-set"></div>
                </div>
            </div>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button -button-submit">확인</button>
    </div>
</form:form>

<script>
    modal.find('.-button-submit').click(() => {
        modal.submit();
        modal.find('.-button-submit').attr("disabled", "true");
    })

    const persons = {
        <c:forEach var="e" items="${addOnPersons}">
        "${g.escapeQuote(e.peer)}": {hostName: '${g.escapeQuote(e.hostName)}', extension: '${g.escapeQuote(e.extension)}', idName: '${g.escapeQuote(e.idName)}'},
        </c:forEach>
        <c:forEach var="e" items="${entity.addPersons}">
        '${g.escapeQuote(e.peer)}': {
            hostName: '${g.escapeQuote(e.hostName)}',
            extension: '${g.escapeQuote(e.extension)}',
            idName: '${g.escapeQuote(e.idName)}',
            penalty: '${e.penalty}',
            callRate: '${e.callRate}'
        },
        </c:forEach>
    };

    window.prepareWriteFormData = function (data) {
        data.addPersons = [];
        modal.find('[name="addPersons"] option').each(function () {
            const peer = $(this).val();
            const person = {peer: peer};
            if (data.strategy === "skill")
                person.penalty = data.penalty[peer];
            else if (data.strategy === "callrate")
                person.callRate = data.callRate[peer];

            data.addPersons.push(person);
        });

        data.huntForwarding = data.huntForwarding[data.isForwarding];

        delete data.penalty;
        delete data.callRate;

    };

    function showForwardingData() {
        const isForwarding = modal.find('[name=isForwarding]:checked').val();
        modal.find('.-forwarding-data').hide().filter(function () {
            return isForwarding === $(this).attr('data-value');
        }).show();
    }

    modal.find('[name=isForwarding]').change(showForwardingData);
    showForwardingData();

    const strategySelector = modal.find('[name=strategy]');
    strategySelector.change(function () {
        const value = $(this).val();
        const inputContainer = $('.-strategy-sub-input-container').hide().filter(function () {
            return $(this).attr('data-value') === value;
        }).show();

        if (inputContainer.length <= 0)
            return;

        /*const scrollingArea = $(this).closest('.scrolling');
        setTimeout(function () {
            scrollingArea.animate({scrollTop: inputContainer.offset().top});
        });*/

        if (inputContainer.attr('data-value') === 'skill')
            showSkillLevelInputs();
        if (inputContainer.attr('data-value') === 'callrate')
            showCallrateInputs();
    }).change();

    function showSkillLevelInputs() {
        const container = $('#skill-level-set');

        const addPersons = [];
        modal.find('[name="addPersons"] option').each(function () {
            addPersons.push($(this).val());
        });
        addPersons.map(function (peer) {
            if (container.children('.ui').filter(function () {
                return $(this).attr('data-peer') === peer;
            }).length > 0) return;

            const select = $('<select/>', {name: 'penalty[' + peer + ']'});
            for (let i = 1; i <= 9; i++)
                select.append($('<option/>', {value: i, text: i}));

            select.find('option').filter(function () {
                return persons[peer].penalty === $(this).val();
            }).prop('selected', true);

            container.append(
                $('<div/>', {class: "ui medium label", 'data-peer': peer})
                    .append($('<text/>', {text: peer + '[' + persons[peer].hostName + '][' + persons[peer].idName + ']'}))
                    .append(select)
            );
        });

        container.children('.ui').filter(function () {
            return addPersons.indexOf($(this).attr('data-peer')) < 0;
        }).each(function () {
            $(this).remove();
        });
    }

    function showCallrateInputs() {
        const container = $('#callrate-set');

        const addPersons = [];
        modal.find('[name="addPersons"] option').each(function () {
            addPersons.push($(this).val());
        });
        addPersons.map(function (peer) {
            if (container.children('.ui').filter(function () {
                return $(this).attr('data-peer') === peer;
            }).length > 0) return;

            container.append(
                $('<div/>', {class: "ui medium label", 'data-peer': peer})
                    .append($('<text/>', {text: peer + '[' + persons[peer].hostName + '][' + persons[peer].idName + ']'}))
                    .append($('<input/>', {type: 'text', placeholder: "%", maxLength: 3, name: 'callRate[' + peer + ']', value: persons[peer].callRate || 0}))
            );
        });

        container.children('.ui').filter(function () {
            return addPersons.indexOf($(this).attr('data-peer')) < 0;
        }).each(function () {
            $(this).remove();
        });
    }

    modal.find('.-to-left, .-to-right').click(function () {
        strategySelector.change();
    });

    let timer;
    modal.find('.-right-selector option, .-left-selector option').dblclick(function () {
        timer = setTimeout(function(){
            strategySelector.change();
        },150);
    });

    function showRetryRows() {
        const radio = modal.find('input[name=isRetry]:checked').val();
        if (radio === 'true') {
            $('.-retry').show();
            showTTSData();
        } else {
            $('.-retry').hide();
        }
    }

    function replaceConnectDataList() {
        const radio = modal.find('input[name=noConnectKind]:checked').val();
        const select = modal.find('[name=noConnectData]');
        modal.find('.-connect-data').hide();
        select.empty();

        if (radio === "CONTEXT") {
            modal.find('.-connect-data').show();
            select.append($('<option/>', {value: '', text: '컨텍스트 선택'}));
            <c:forEach var="e" items="${contexts}">
                select.append($('<option/>', {value: '${e.key}', text: '${e.value}'}).prop('selected', "${entity.noConnectData.equals(e.key) ? 'selected' : ''}"));
            </c:forEach>
        } else if (radio === "HUNT") {
            modal.find('.-connect-data').show();
            select.append($('<option/>', {value: '', text: '예비큐 선택'}));
            <c:forEach var="e" items="${subGroups}">
                select.append($('<option/>', {value: '${e.key}', text: '${e.value}'}).prop('selected', "${entity.noConnectData.equals(e.key) ? 'selected' : ''}"));
            </c:forEach>
        } else {
            modal.find('.-connect-data').hide();
        }
    }

    function showTTSData() {
        $('#ttsText').hide();
        if (modal.find('[name=retrySoundCode]').val() === 'TTS')
            $('#ttsText').show();
        else
            $('#ttsData').val('');
    }

    modal.find('[name=isRetry]').change(showRetryRows).change();

    modal.find('[name=retrySoundCode]').change(showTTSData);
    showTTSData();

    modal.find('input[name=noConnectKind]').change(replaceConnectDataList).change();
</script>
