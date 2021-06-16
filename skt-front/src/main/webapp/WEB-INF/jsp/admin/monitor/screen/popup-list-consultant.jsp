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

<%--@elvariable id="config" type="kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScreenConfig"--%>
<%--@elvariable id="data" type="kr.co.eicn.ippbx.front.model.ScreenDataForIntegration"--%>
<%--@elvariable id="statusCodes" type="java.util.Map"--%>
<%--@elvariable id="statusCodeKeys" type="java.util.List"--%>

<%--@elvariable id="personStatuses" type="java.util.List<kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse>"--%>

<tags:layout-screen>
    <div class="billboard-wrap ${config.lookAndFeel == 2 ? 'theme2' : config.lookAndFeel == 3 ? 'theme3' : ''}">
        <div class="header">
            <div class="pull-left">${g.htmlQuote(config.name)}</div>
            <div class="pull-right">
                <div class="menu-btn-wrap">
                    <button class="menu-open-btn" id="billboard-popup-btn" onclick="billboardPopup()"></button>
                </div>
                <div class="time-wrap -time"></div>
            </div>
        </div>
        <div class="content">
            <div class="ui grid full-height remove-margin flex-flow-column">
                <div class="equal width row height-13rem">
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">가용율</div>
                            <div class="bottom" id="login-user-rate"></div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">대기</div>
                            <div class="bottom -consultant-status-count" data-value="0"></div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">통화중</div>
                            <div class="bottom -consultant-status-count" data-value="1"></div>
                        </div>
                    </div>
                    <div class="column">
                        <div class="board-label-vertical">
                            <div class="top flex-160">후처리</div>
                            <div class="bottom -consultant-status-count" data-value="2"></div>
                        </div>
                    </div>

                    <c:set var="START_STATUS_INDEX" value="${3}"/><%--3~ --%>
                    <c:set var="FIXED_STATUS_COUNT" value="${4}"/><%--가용률, 대기, 통화중, 후처리--%>
                    <c:set var="EXPRESSED_WIDGET_COUNT" value="${6}"/><%--상단에 표현할 위젯 갯수--%>
                    <c:set var="restExressingStatusCount" value="${EXPRESSED_WIDGET_COUNT - FIXED_STATUS_COUNT}"/>
                    <c:set var="EXPRESSING_STATUS_COUNT" value="${statusCodes.size() - 2}"/><%--FIXME: 만약 PDS가 8번이고, PDS를 제외해야 한다면 조건식으로 다음으로 변경: statusCodes.size() - 3 --%>
                    <c:set var="completed" value="${false}"/>
                    <c:forEach var="i" begin="${START_STATUS_INDEX}" end="${EXPRESSING_STATUS_COUNT}" varStatus="status"><%--마지막 9는 취급안함--%>
                        <c:set var="key" value="${statusCodeKeys[i]}"/>
                        <c:if test="${restExressingStatusCount > 0 && !completed}">
                            <c:set var="restExressingStatusCount" value="${restExressingStatusCount - 1}"/>
                            <c:choose>
                                <c:when test="${restExressingStatusCount > 0 || status.last}">
                                    <div class="column">
                                        <div class="board-label-vertical">
                                            <div class="top flex-160">${g.htmlQuote(statusCodes.get(key))}</div>
                                            <div class="bottom -consultant-status-count" data-value="${key}"></div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="completed" value="${true}"/>
                                    <div class="column">
                                        <div class="board-label-vertical">
                                            <div class="top flex-160">기타</div>
                                            <div class="bottom -consultant-status-count"
                                                 data-value="<c:forEach var="e2" begin="${i}" end="${EXPRESSING_STATUS_COUNT}" varStatus="status2">${statusCodeKeys[e2]}${status2.last ? '' : ','}</c:forEach>"></div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                </div>
                <div class="row flex-100">
                    <div id="billboard-tab1" class="sixteen wide column remove-pb billboard-tab-content current">
                        <div class="ui five column grid">
                            <c:set var="statusClasses" value="${['stay', 'call', 'after', 'rest', 'rest', 'rest', 'rest', 'rest', 'rest', 'rest']}"/>
                            <c:forEach var="e" items="${personStatuses}">
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left -consultant-screen-status-class ${statusClasses[e.person.paused]}" data-peer="${g.escapeQuote(e.person.peer)}"></div>
                                        <div class="right">
                                            <div class="time -consultant-status-time">00:00</div>
                                            <div class="name">${g.htmlQuote(e.person.idName)}</div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div id="billboard-tab2" class="sixteen wide column remove-pb billboard-tab-content">
                        <div class="ui five column grid">
                            <div class="column">
                                <div class="user-label">
                                    <div class="left call"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left after"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left etc"></div>
                                    <div class="right">
                                        <div class="time">00:00</div>
                                        <div class="name">김이상담</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="user-label">
                                    <div class="left"></div>
                                    <div class="right">
                                        <div class="time"></div>
                                        <div class="name"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <ul class="billboard-tabs">
                    <li class="tab-link current" data-tab="billboard-tab1"></li>
                    <li class="tab-link" data-tab="billboard-tab2"></li>
                </ul>
            </div>
        </div>

        <c:if test="${config.showSlidingText}">
            <div class="footer">
                <h2 style="width: 100%;">
                    <text id="sliding-text" class="marquee-text">${g.htmlQuote(config.slidingText)}</text>
                </h2>
            </div>
        </c:if>
    </div>

    <div class="ui mini modal ${config.lookAndFeel == 2 ? 'theme2' : config.lookAndFeel == 3 ? 'theme3' : ''}" id="modal-billboard">
        <i class="close icon"></i>
        <div class="content">
            <div class="billboard-inner">
                <label class="title">상태별 강조 시간</label>
                <div class="inner">
                    <ul>
                        <c:forEach var="e" items="${statusCodes}">
                            <c:if test="${e.key != 9}">
                                <li>
                                    <div class="left"><span class="symbol state-wait"></span>${g.htmlQuote(e.value)}</div>
                                    <div class="right"><input type="text" name="status-threshold-${e.key}" placeholder="분">분</div>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui fluid button" onclick="saveThresholds()">저장</button>
        </div>
    </div>

    <jsp:include page="/admin/dashboard/script-for-queue-and-person-status"/>
    <tags:scripts>
        <script>
            setInterval(function () {
                $('.-time').text(moment().format('YYYY-MM-DD HH:mm:ss'));
            }, 500);

            $(document).ready(function () {
                window.resizeTo(620, 690);
            });

            $(window).on('load', function () {
                window.resizeTo(1800, 1000);
            });

            const textElement = $('#sliding-text');
            const container = textElement.parent();
            setInterval(function () {
                const textWidth = textElement.width();
                const containerWidth = container.width();

                if (textWidth >= containerWidth)
                    container.addClass('marquee');
                else
                    container.removeClass('marquee');
            }, 3 * 1000);

            $('.billboard-tabs li').click(function () {
                var tab_id = $(this).attr('data-tab');

                $('.billboard-tabs li').removeClass('current');
                $('.billboard-tab-content').removeClass('current');

                $(this).addClass('current');
                $("#" + tab_id).addClass('current');
            })

            function billboardPopup() {
                $('#modal-billboard').modalShow();
            }

            const _updatePersonStatus = updatePersonStatus;

            window.updatePersonStatus = function () {
                _updatePersonStatus();

                const _peerStatuses = values(peerStatuses);
                $('#login-user-rate').text((_peerStatuses.filter(function (peer) {
                    return peer.login;
                }).length / _peerStatuses.length).toFixed(1) + '%');

                const statusClasses = ['stay', 'call', 'after', 'rest', 'rest', 'rest', 'rest', 'rest', 'rest', 'rest'];
                $('.-consultant-screen-status-class').each(function () {
                    const p = peerStatuses[$(this).attr('data-peer')];
                    if (!p)
                        return;

                    statusClasses.map(function (c) {
                        $(this).removeClass(c);
                    });
                    $(this).addClass(statusClasses[p.status]);
                });
            };
            updatePersonStatus();
            updateQueues();

            const STORAGE_KEY = 'screenConsultantStatusThresholdForm';

            function saveThresholds() {
                $('#modal-billboard').asJsonData().done(function (data) {
                    localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
                });
            }

            $(window).on('load', function () {
                const storedValues = localStorage.getItem(STORAGE_KEY);
                if (!storedValues)
                    return;

                const values = JSON.parse(storedValues);

                const inputs = $('#modal-billboard').find('[name]');
                for (let key in values) {
                    if (values.hasOwnProperty(key)) {
                        inputs.filter(function () {
                            return $(this).attr('name') === key;
                        }).val(values[key]);
                    }
                }
            });
        </script>
    </tags:scripts>
</tags:layout-screen>
