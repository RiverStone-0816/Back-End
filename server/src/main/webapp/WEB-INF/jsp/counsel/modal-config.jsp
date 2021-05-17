<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.front.config.RequestMessage"--%>
<%--@elvariable id="cached" type="kr.co.eicn.ippbx.front.config.CachedEntity"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<div class="ui modal inverted">
    <i class="close icon"></i>
    <div class="header">상담화면설정</div>
    <div class="content rows scrolling">
        <div class="ui grid">
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">IPCC 관리 메뉴 즐겨찾기 등록</h4>
                </div>
            </div>
            <div class="row">
                <div class="three wide column"><label class="control-label">메뉴</label></div>
                <div class="four wide column">
                    <div class="ui checkbox">
                        <input type="checkbox" name="menu1">
                        <label>상담결과이력</label>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui checkbox">
                        <input type="checkbox" name="menu2">
                        <label>녹취/통화이력조회</label>
                    </div>
                </div>
                <div class="four wide column">
                    <div class="ui checkbox">
                        <input type="checkbox" name="menu3">
                        <label>콜백이력관리</label>
                    </div>
                </div>
                <c:if test="${usingServices.contains('NOT')}">
                    <div class="one wide column"></div>
                    <div class="three wide column"></div>
                    <div class="four wide column">
                        <div class="ui checkbox">
                            <input type="checkbox" name="menu4">
                            <label>공지사항</label>
                        </div>
                    </div>
                    <div class="four wide column">
                        <div class="ui checkbox">
                            <input type="checkbox" name="menu5">
                            <label>지식관리</label>
                        </div>
                    </div>
                    <div class="four wide column">
                        <div class="ui checkbox">
                            <input type="checkbox" name="menu6">
                            <label>메뉴얼</label>
                        </div>
                    </div>
                </c:if>
                <c:if test="${serviceKind.equals('SC') && usingServices.contains('QA')}">
                    <div class="one wide column"></div>
                    <div class="three wide column"></div>
                    <div class="four wide column">
                        <div class="ui checkbox">
                            <input type="checkbox" name="menu7">
                            <label>상담원평가결과이력</label>
                        </div>
                    </div>
                </c:if>
            </div>
            <c:if test="${!user.idType.equals('M')}">
                <div class="row">
                    <div class="sixteen wide column">
                        <h4 class="ui header title">채널별 처리율 설정</h4>
                    </div>
                </div>
                <div class="row">
                    <div class="three wide column"><label class="control-label">비율설정</label></div>
                    <div class="thirteen wide column">
                        <c:forEach var="e" items="${todoKinds}">
                            <div class="ui labeled input">
                                <div class="ui label">${g.htmlQuote(e.value)}</div>
                                <input type="text" placeholder="%" size="2" maxlength="2" class="-input-numerical" name="${g.htmlQuote(e.key)}"/>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            <div class="row">
                <div class="sixteen wide column">
                    <h4 class="ui header title">기타</h4>
                </div>
            </div>
            <div class="row">
                <div class="sixteen wide column">
                    <div class="ui checkbox">
                        <input type="checkbox" name="useCallNoticePopup"/>
                        <label>전화 알림창 사용 여부</label>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <c:if test="${serviceKind.equals('SC')}">
            <div class="ui button left floated" onclick="smsCategoryAdd()">SMS 카테고리 등록</div>
            <div class="ui button left floated" onclick="smsTemplateAdd()">SMS 상용문구 등록</div>
        </c:if>
        <button type="button" class="ui button modal-close">취소</button>
        <button type="button" class="ui blue button modal-close -save-config">확인</button>
    </div>
</div>

<script>
    (function () {
        const stringify = localStorage.getItem(ITEM_KEY_CONFIG);
        if (!stringify)
            return;

        const data = JSON.parse(stringify);

        modal.find('[name]').each(function () {
            const name = $(this).attr('name');
            const value = data[name];

            if ($(this).is('[type=checkbox]')) {
                $(this).prop('checked', !!value);
            } else {
                $(this).val(value || '');
            }
        });
    })();

    modal.find('.-save-config').click(function () {
        modal.asJsonData().done(function (data) {
            const config = {};

            keys(data).map(function (key) {
                config[key] = data[key];
            });

            const stringify = JSON.stringify(data);
            localStorage.setItem(ITEM_KEY_CONFIG, stringify);
            counselDisplayConfiguration = config;

            setAlertCurrentStatus();
            setConfiguredTab();
        });
    });
</script>