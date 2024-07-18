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
<%--@elvariable id="accessToken" type="java.lang.String"--%>
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="usingServices" type="java.lang.String"--%>

<div class="ui column grid" id="stt-panel" style="flex: 1;">
    <div class="sixteen wide column talk-room" style="height: 100%;">
        <div id="stt-call-panel" class="chat-container overflow-hidden" style="height: 100%;">
            <div class="room"  style="height: 100%; display: flex; flex-direction: column; position: relative;">
                <div class="chat-header dp-flex justify-content-space-between align-items-center">
                    <span id="text-line" :style="'visibility:'+(uniqueId ?'visible':'hidden')"
                          style="padding:0 5px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                        [{{ inOut === 'I' ? '수신' : '발신' }}] - {{ phoneNumber }}
                    </span>
                    <button v-if="chkAst && reMindCheck" id="remind-copy-btn" @Click="remindCopyBtn">복사</button>
                    <div class="ui toggle checkbox checkRemind" v-show="chkAst">
                        <input type="checkbox" id="checkRemind" v-model="reMindCheck"/><label></label>
                    </div>
                </div>
                <div class="chat-body os-host os-theme-dark os-host-resize-disabled os-host-scrollbar-horizontal-hidden os-host-transition os-host-overflow os-host-overflow-y" style="height: 450px; flex: 1;">
                    <div class="os-resize-observer-host">
                        <div class="os-resize-observer observed" style="left: 0; right: auto;"></div>
                    </div>
                    <div class="os-size-auto-observer" style="height: calc(100% + 1px); float: left;">
                        <div class="os-resize-observer observed"></div>
                    </div>
                    <div class="os-content-glue" style="margin: -10px 0 0"></div>
                    <div class="os-padding">
                        <div ref="chatBody"
                             class="os-viewport os-viewport-native-scrollbars-invisible"
                             style="overflow-y: scroll; scroll-behavior: smooth;">
                            <div v-show="!reMindCheck">
                                <%--                                <button @Click="runChartTestActive()">시작</button>--%>
                                <%--                                <button @Click="runChartTestStop()">종료</button>--%>
                                <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                    <div v-if="e.extension === phoneNumber && e.messageType === 'TEXT'" class="chat-item">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">[{{ e.extension }}] {{ getTimeFormat(e.time) }}</div>
                                            <div class="chat">
                                                <div class="bubble" :style="e.reMind ? '' : ''">
                                                    <div class="txt_chat" @Click="allTypeSearchForSttText">
                                                        <p>{{ e.contents }}</p>
                                                    </div>

                                                    <span class="remind-checkbox"><input type="checkbox" v-model="e.reMind" @click="e.reMind=!e.reMind;remindUpdate(e.messageId,e.reMind, e.messageSeq)"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div v-if="e.extension !== phoneNumber && e.messageType === 'TEXT'" class="chat-item chat-me">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">
                                                <text>
                                                    [{{ e.username != '' ? e.username : e.extension }}]
                                                </text>
                                                {{ getTimeFormat(e.time) }}
                                            </div>
                                            <div class="chat">
                                                <div class="bubble" :style="e.reMind ? '' : ''">
                                                    <div class="txt_chat" @Click="allTypeSearchForSttText">
                                                        <p>{{ e.contents }}</p>
                                                    </div>

                                                    <span class="remind-checkbox"><input type="checkbox" v-model="e.reMind" @click="e.reMind=!e.reMind;remindUpdate(e.messageId,e.reMind, e.messageSeq)"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div v-show="reMindCheck">
                                <div v-for="(e, i) in messageList" :key="i" :ref="'message-' + i">
                                    <div v-if="e.extension === phoneNumber && e.messageType === 'TEXT' && e.reMind" class="chat-item">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">[{{ e.extension }}] {{ getTimeFormat(e.time) }}</div>
                                            <div class="chat">
                                                <div class="bubble" :style="e.reMind ? '' : ''">
                                                    <div class="txt_chat">
                                                        <p>{{ e.contents }}</p>
                                                    </div>

                                                    <span class="remind-checkbox"><input type="checkbox" v-model="e.reMind" @click="e.reMind=!e.reMind;remindUpdate(e.messageId,e.reMind, e.messageSeq)"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div v-if="e.extension !== phoneNumber && e.messageType === 'TEXT' && e.reMind" class="chat-item chat-me">
                                        <div class="profile-img">
                                            <img :src="getImage(e.extension)">
                                        </div>
                                        <div class="wrap-content">
                                            <div class="txt-time">
                                                <text>
                                                    [{{ e.username != '' ? e.username : e.extension }}]
                                                </text>
                                                {{ getTimeFormat(e.time) }}
                                            </div>
                                            <div class="chat">
                                                <div class="bubble" :style="e.reMind ? '' : ''">
                                                    <div class="txt_chat">
                                                        <p>{{ e.contents }}</p>
                                                    </div>

                                                    <span class="remind-checkbox"><input type="checkbox" v-model="e.reMind" @click="e.reMind=!e.reMind;remindUpdate(e.messageId,e.reMind, e.messageSeq)"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="write-chat" style="display: flex; flex-direction: column; background-color: #fff; height: 35%; max-height: 201px;" v-show="chkAst">
                    <div style="display: flex; flex-direction: row; margin-top: 1%; height: 100%;">
                        <div style="flex: 1; display: flex; flex-direction: column; flex-basis: 70%; position: relative;">
                            <div style="padding-left: 20px; margin-bottom: 7px;">키워드 추천</div>
                            <div style="float: left; padding: 0 3% 0 0; overflow: auto; height: 100%;">
                                <div id="chart-wrap" ref="chartWrap" style="width: 100%; min-width: 100%;"></div>
                                <ul id="chart-excluded-keyword" style="max-height: 65px; overflow-y: auto; margin-top: 5px; user-select: none;">
                                    <li v-for="(keyword, i) in excludedKeywords" :key="i" :ref="'e-' + i"
                                        style="display: inline-block; margin: 0 2px; border: 1px solid #ccc; padding: 4px 7px; font-size: 12px; border-radius: 4px; cursor: pointer; margin-bottom: 5px;"
                                    <%--                                        @click="searchKmsList(keyword, $event)"--%>
                                        @click="chartKeywordClickEvent(keyword, $event)"
                                    >
                                        {{ keyword }}
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <hr style=" height:auto; width:.01vw;border-width:0;color:rgba(34, 36, 38, 0.15);background-color:rgba(34, 36, 38, 0.15);margin-top: -4px; margin-bottom: 0;">
                        <div style="flex: 1; display: flex; flex-direction: column; flex-basis: 30%;"><div style="margin-bottom: 5px; text-align: center;">금지어</div>
                            <div id="chart-prohibit-keyword" style="float: left; overflow: auto; padding: 3% 3% 3% 3%; margin-bottom: 5px; max-height: 143px; text-align: center;">
                                <%--                                <span v-for="(e, i) in kmsKeyword.filter((f) => f.prohibit_yn === 'Y')" :key="i" :ref="'kmsKeyword-' + i" style="display: inline-block;">--%>
                                <%--                                    <button class="ui button mini right floated compact" style="margin-top: 0.4rem; padding: 4px 8px; min-width: 65px; border-radius: 4px; font-size: 12px; margin-right: 0.4rem; border: 1px solid #ccc; background-color: #fff; color: #ccc; font-family: none;" @click="searchKmsList(e.keyword, $event)">{{ e.keyword }}</button>--%>
                                <%--                                </span>--%>
                                <li v-for="(keyword, i) in prohibitionKeywords" :key="i" :ref="'e-' + i"
                                    style="display: inline-block; margin: 0 4px; border: 1px solid #ccc; padding: 4px 7px; font-size: 12px; border-radius: 4px; margin-bottom: 5px;"
                                <%--                                        @click="searchKmsList(keyword, $event)"--%>
                                >
                                    {{ keyword }}
                                </li>
                            </div>
                        </div>
                    </div>
                    <div class="write-menu" :style="adminContent2 ? 'height: 100%;' : 'height: 32px;'" ref="adminChatting" style="position: absolute; left: 0; bottom: 0; width: 100%; display: flex; flex-direction: column; flex: 1; border-top: 2px solid rgba(0, 0, 0, 0.06); background-color: #f7f5f5;">
                        <div style="width: 100%;">관리자 채팅<img v-show="isAdminNewMessage" src="<c:url value="/resources/images/n_icon.svg"/>" style="margin-bottom: -6px;">
                            <button @click="reduceChatPanel()" id="etc-mini-panel-resizer" type="button" style=" border-radius: 0.28571429rem; border: solid 1px #D4D4D5; float: right; padding: 1%; height: 15px; cursor: pointer;">
                                <i id='keyboardtext' class="material-icons arrow" style="font-size: 20px; margin-top: -15px;">keyboard_arrow_up</i>
                            </button>
                        </div>
                        <div id="adminContent" v-show="adminContent2" style="width: 100%; display: flex; height: 100%; flex-direction: column;">
                            <div class="chat-body" style="flex: 1; white-space: pre-line;">
                                <div ref="adminChat" style="overflow-y: scroll; scroll-behavior: smooth; height: 100%;">
                                    <div v-for="(e, i) in adminMessage" :key="i" :ref="'adminMessage-' + i">
                                        <div class="chat-item" v-if="e.userid !== loginId">
                                            <div class="wrap-content">
                                                <div class="txt-time">
                                                    <text>
                                                        [{{e.userid || '마스터' }}]
                                                    </text>
                                                </div>
                                                <div class="chat">
                                                    <div class="bubble" style="border: 1px solid grey;">
                                                        <div class="txt_chat">
                                                            <p>{{ e.message }}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="chat-item chat-me" v-if="e.userid === loginId">
                                            <div class="wrap-content">
                                                <div class="chat">
                                                    <div class="bubble" style="border: 1px solid grey;">
                                                        <div class="txt_chat">
                                                            <p>{{ e.message }}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div v-show="adminContent2" style="display: flex; width: 100%; flex: 0; padding-bottom: 15px; ">
                            <input class="panel-message" ref="adminMessage" style="width: 75%" placeholder="전송하실 메시지를 입력하세요." @keydown.stop="keyDown"/>
                            <button type="button" class="ui mini button compact" style="margin: 4px;" @click="messageSend()">전송</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<tags:scripts>
    <script>
        const sttRequestUrl = "${sttRequestUrl}"

        function truncateText(text, maxLength) {
            if (text.length > maxLength) {
                return text.slice(0, maxLength) + '...';
            }
            return text;
        }

        const callPanelProperties = {
            mounted(){
            },
            setup: function () {
                return {
                    ME: userId,
                    REPLYING_INDICATOR: '\u001b',
                    REPLYING_TEXT: '\u001a',
                    REPLYING_IMAGE: '\u000f',
                    REPLYING_FILE: '\u000e',
                    REPLYING_IMAGE_TEMP: '\u000d',
                }
            },
            data: function () {
                return {
                    companyId: '${g.user.companyId}',
                    uniqueId: null,
                    channelType: null,
                    userId: '',
                    userName: null,
                    phoneNumber: null,

                    loginId: '${g.user.id}',

                    bodyScrollingTimer: null,
                    adminBodyScrollingTimer: null,
                    messageList: [],

                    inOut: null,
                    adminContent2: false,
                    reMindCheck: false,

                    kmsKeyword: [],
                    chartKeywordArray: [], // 키워드를 배열로 저장 중복값 존재 (원본 data)
                    chartKeywordData: {}, // chartKeywordArray를 {k: v} 형태로 저장 (echart가 사용하는 data)
                    excludedKeywords: [], // 차트에 표시되지 않는 키워드를 저장 (상위 3개를 제외한 값)
                    prohibitionKeywords: new Set(), // 금지 키워드
                    keywordChart: null, // chart element
                    chartUpdateCount: 0,
                    runChartTest: null, // test setInterval
                    chartColors: ['#B20048', '#F5BA46', '#35BA9B', '#E6553D', '#35BA9B'],


                    adminMessage: [],
                    adminMessageCheckDuplicate: [],
                    isAdminNewMessage: false,
                    personList: {
                        <c:forEach var="e" items="${personListMap}">
                        '${e.key}': '${e.value}',
                        </c:forEach>
                    },

                    chkAst: <c:if test="${usingServices.contains('AST')}">true</c:if><c:if test="${!usingServices.contains('AST')}">false</c:if>,

                }
            },
            methods: {
                jsonDataParse: function (data) {
                    return typeof data === 'string' ? JSON.parse(data) : JSON.parse(JSON.stringify(data))
                },
                loadRoom: function (uniqueId, number, inOut) {
                    const _this = this

                    console.log('stt.jsp > loadRoom 실행@')
                    console.log('stt.jsp > loadRoom > sttRequestUrl = ', sttRequestUrl)
                    restUtils.post(sttRequestUrl + '/get_stt_data', {
                        mode: "stt_text",
                        extension: _this.extension,
                        userid: _this.loginId,
                        uniqueid: uniqueId,
                        company_id: _this.companyId,
                        // dialup_date: 1,
                        // hangup_date: 1,
                    }, false).done(function (response) {
                        _this.uniqueId = uniqueId
                        _this.inOut = inOut
                        _this.phoneNumber = number

                        if(response && response.stt_text && response.stt_text.length){
                            response.stt_text.forEach(function (e) {
                                _this.appendMessage({
                                    uniqueId: _this.uniqueId,
                                    phoneNumber: _this.phoneNumber,
                                    time: e.insert_time,
                                    messageType: e.kind,
                                    contents: e.text,
                                    userId: e.ipcc_userid,
                                    extension: e.my_extension,
                                    username: _this.userName,
                                    kmsKeyword: e.kms_keyword,
                                    startMs: e.start_ms,
                                    stopMs: e.stop_ms,
                                    reMind: e.remind_yn === 'Y',
                                    messageId: e.message_id
                                })
                            })
                        }
                        _this.scrollToBottom()
                    })
                },
                clearRoom: function () {
                    const _this = this
                    _this.uniqueId = null
                    _this.messageList = []
                    _this.kmsKeyword = []
                    _this.adminMessage = []
                },
                popupImageView: function (url) {
                    popupImageView(url)
                },
                getTimeFormat: function (time) {
                    return moment(time).format('MM-DD HH:mm')
                },
                appendMessage: function (message) {
                    console.log('appendMessage > message = ', message)
                    if (this.uniqueId !== message.uniqueId)
                        return

                    if (this.messageList.length === 0)
                        return this.messageList.push(message)

                    if (this.messageList[0].time >= message.time)
                        return this.messageList.splice(0, 0, message)

                    if (this.messageList[this.messageList.length - 1].time <= message.time) {
                        this.scrollToBottom()
                        return this.messageList.push(message)
                    }

                    this.messageList.push(message)
                    this.scrollToBottom()
                    this.messageList.sort(function (a, b) {
                        return a.time - b.time
                    })
                },
                appendAdminMessage: function (message) {
                    console.log('VUE.JS > message = ', message)
                    // 데몬이 메세지를 중복으로 보냈는지 체크
                    let checkDuplicate = this.adminMessageCheckDuplicate.filter((v) => {
                        if(v == message.message_seq) return v
                    });
                    // 중복인 메세지가 있으면 return
                    if(checkDuplicate.length) return
                    else this.adminMessageCheckDuplicate.push(message.message_seq)


                    if (!this.adminContent2)
                        this.isAdminNewMessage = message.userid !== this.loginId

                    if (this.adminMessage.length === 0)
                        return this.adminMessage.push(message)

                    if (this.adminMessage[0].time >= message.time)
                        return this.adminMessage.splice(0, 0, message)

                    if (this.adminMessage[this.adminMessage.length - 1].time <= message.time) {
                        this.adminScrollToBottom()
                        return this.adminMessage.push(message)
                    }

                    this.adminMessage.push(message)
                    this.adminScrollToBottom()
                    this.adminMessage.sort(function (a, b) {
                        return a.time - b.time
                    })
                },
                initRenderChart: function(){
                    const chartDom = document.getElementById('chart-wrap');
                    const keywordChart = echarts.init(chartDom);
                    let option;
                    let keywordNames = []
                    let chartValue = []
                    option = {
                        grid: {
                            left: 15,
                            right: 20,
                            top: 0,
                            bottom: 0,
                            containLabel: true
                        },
                        xAxis: {
                            max: 'dataMax',
                            nameGap: 5,
                            axisLabel: {
                                show: false,
                            },
                            splitLine: {
                                show: false
                            },
                            animationDuration: 300,
                            animationDurationUpdate: 300,
                        },
                        yAxis: {
                            triggerEvent: true,
                            type: 'category',
                            data: keywordNames,
                            nameGap: 5,
                            nameLocation: 'start',
                            inverse: true,
                            animationDuration: 300,
                            animationDurationUpdate: 300,
                            max: 0, // only the largest 3 bars will be displayed
                            position: 'left',
                        },
                        series: [
                            {
                                realtimeSort: true,
                                type: 'bar',
                                data: chartValue,
                                label: {
                                    show: true,
                                    position: 'right',
                                    valueAnimation: true
                                }
                            }
                        ],
                        animationDuration: 0,
                        animationDurationUpdate: 300,
                        animationEasing: 'linear',
                        animationEasingUpdate: 'linear'
                    };
                    keywordChart.setOption(option);
                    this.keywordChart = keywordChart

                    // y축 레이블, 이벤트 바인딩
                    keywordChart.on('click', (params) => {
                        if(params.targetType !== 'axisLabel') return

                        this.chartKeywordClickEvent(params.value, params.event.event)
                    })

                    window.keywordChart = {
                        chart: keywordChart,
                        initialize: () => {
                            // ui상의 element를 삭제하고 vue 객체의 state를 초기화
                            this.runChartTestStop()
                            window.keywordChart.chart.clear()
                            $('#chart-excluded-keyword li').remove()
                            $('#chart-prohibit-keyword > span').remove()
                            $('#chart-prohibit-keyword > li').remove()
                            this.chartKeywordArray = []
                            this.chartKeywordData = {}
                            this.excludedKeywords = []
                            this.keywordChart = null
                            this.chartUpdateCount = 0
                            this.prohibitionKeywords = new Set()
                            window.keywordChart = null
                            keywordChart.dispose();
                        }
                    }
                },
                appendKeyword: function(keyword) {
                    for (const keywordObj of keyword) {
                        if(keywordObj.keyword_yn === 'Y') {
                            this.chartKeywordArray.push(keywordObj.keyword)
                        }
                        else if(keywordObj.prohibit_yn === 'Y') {
                            this.prohibitionKeywords.add(keywordObj.keyword)
                            return
                        }
                        else console.log('해당 사항이 없는 keyword')
                    }

                    if(this.chartUpdateCount === 0) {
                        this.initRenderChart()
                    };

                    const data = {};
                    this.chartKeywordArray.forEach((keyword) => {
                        data[keyword] = (data[keyword] || 0)+1;
                    });
                    const keywordNames = Object.keys(data)
                    const keywordValues = Object.values(data)
                    // data: [1,2,3] 이런 형태였던 데이터를 data: [{value: 1, itemStyle: {color: 'red'}, {...}] 와 같이 바꿔야 함. (차트 색상을 각각 다르게 하기 위해)
                    const keywordValuesStructure = keywordValues.map((v, i) => {
                        return {value: v, itemStyle: {color: this.chartColors[i]}}
                    })

                    const maxCount = keywordNames.length-1 <= 2 ? keywordNames.length-1 : 2
                    const height = (maxCount+1) * 20
                    this.chartKeywordData = data
                    if(height < 100) this.keywordChart.resize({height})
                    this.keywordChart.setOption({
                        yAxis: {
                            type: 'category',
                            axisLine: {
                                show: false, // y축 라벨, 축선, 축표시 없애기
                            },
                            axisTick: {
                                show: false, // y축 눈금 없애기
                            },
                            axisLabel: {
                                formatter: function (value) {
                                    let label = value.toString(); // y축 텍스트 값 가져오기
                                    return '{customLabel|' + truncateText(label, 4) + '}';
                                },
                                rich: {
                                    customLabel: {
                                        color: '#333',
                                        // backgroundColor: '#c60452',
                                        // width: 65,
                                        // align: 'center',
                                        // height: 28,
                                        // borderRadius: 3,
                                    }
                                }
                            },
                            data: keywordNames,
                            max: maxCount, // only the largest 3 bars will be displayed
                        },
                        series: [
                            {
                                type: 'bar',
                                // data: keywordValues,
                                data: keywordValuesStructure,
                                barWidth: 10,
                            }
                        ]
                    })

                    // 가장 많이 언급된 키워드 3개는 차트로 표시, 나머지는 li로 표시
                    let sortable = [];
                    for (var v in data) {
                        sortable.push([v, data[v]]);
                    }
                    sortable.sort(function(a, b) {
                        return b[1] - a[1];
                    });
                    const targetArr = sortable.slice(3, sortable.length)
                    const excludedKeywords = targetArr.map((v) => v[0])
                    this.excludedKeywords = excludedKeywords
                    this.chartUpdateCount = this.chartUpdateCount + 1
                },
                // 키워드 1개만 클릭한 경우 (태그 검색)
                chartKeywordClickEvent: function(keyword, event){
                    const shiftKeyEvent = event.shiftKey
                    if(shiftKeyEvent){
                        this.allTypeSearch(keyword, event, shiftKeyEvent)
                    }else{
                        this.onlyKeywordTypeSearch(keyword, event, shiftKeyEvent)
                    }

                },
                // KMS 글, 태그 검색
                onlyKeywordTypeSearch: function (keyword, event) {
                    let search_type_flag = 'tag'
                    if(!event) {
                        console.error("onlyKeywordTypeSearch 이벤트에 event 객체가 없습니다.")
                        return
                    }

                    console.log('onlyKeywordTypeSearch > keyword = ', keyword)
                    $('#assist-custom-sidebar #search_type_flag').val(search_type_flag)
                    $('#assist-custom-sidebar #search_keyword').val(keyword)
                    $('#assist-custom-sidebar').addClass('active')
                    // $('#assist-custom-sidebar .search-box .search.icon').click()
                    return getKmsList()
                },
                // KMS 글, 태그/제목/내용 검색
                allTypeSearch: function (keyword, event, shiftKeyEvent) {
                    let search_type_flag = 'all'
                    if(!event) {
                        console.error("allTypeSearch 이벤트에 event 객체가 없습니다.")
                        return
                    }

                    const currentInputValue = $('#search_keyword').val() || ''
                    let clickTagName = event.target.outerText.replace('#', '')
                    console.log('currentInputValue = ', currentInputValue)
                    console.log('clickTagName = ', clickTagName)
                    if(!clickTagName) clickTagName = keyword
                    let resultKeywordValue = ''
                    if(shiftKeyEvent){
                        resultKeywordValue = currentInputValue + ' ' + clickTagName
                    }else{
                        resultKeywordValue = clickTagName
                    }
                    console.log('allTypeSearch, 최종 인풋 값 = ', resultKeywordValue)
                    $('#assist-custom-sidebar').addClass('active')
                    $('#assist-custom-sidebar #search_keyword').val(resultKeywordValue)
                    $('#assist-custom-sidebar #search_type_flag').val(search_type_flag)
                    // $('#assist-custom-sidebar .search-box .search.icon').click()
                    return getKmsList()
                },
                allTypeSearchForSttText: function (event) {
                    console.log('event = ', event)
                    let search_type_flag = 'all'
                    const shiftKeyEvent = event.shiftKey
                    const keyword = event.target.innerText
                    console.log('keyword = ', keyword)

                    const currentInputValue = $('#search_keyword').val() || ''
                    console.log('currentInputValue = ', currentInputValue)
                    let resultKeywordValue = ''
                    if(shiftKeyEvent){
                        resultKeywordValue = currentInputValue + ' ' + keyword
                    }else{
                        resultKeywordValue = keyword
                    }
                    console.log('allTypeSearch, 최종 인풋 값 = ', resultKeywordValue)
                    $('#assist-custom-sidebar').addClass('active')
                    $('#assist-custom-sidebar #search_keyword').val(resultKeywordValue)
                    $('#assist-custom-sidebar #search_type_flag').val(search_type_flag)
                    return getKmsList()
                },
                runChartTestActive: function(){
                    const runChartTest = setInterval(() => {
                        const testArray = ['긴글테스트', '두글', '세글자', '네글자다', '추가', '모니터', '서버'];
                        const testWaringArray = ['금지어', '싸가지', '비하발언']
                        const randomValue = testArray[Math.floor(Math.random() * testArray.length)];
                        const randomWaringValue = testWaringArray[Math.floor(Math.random() * testWaringArray.length)];
                        this.appendKeyword([{keyword: randomValue, keyword_yn: 'Y'}])
                        this.appendKeyword([{keyword: randomWaringValue, prohibit_yn: 'Y'}])
                    }, 1200)
                    this.runChartTest = runChartTest
                },
                runChartTestStop: function(){
                    clearInterval(this.runChartTest)
                    this.runChartTest = null
                },
                getListElements(display) {
                    return JSON.parse(JSON.stringify(display)).elementList?.sort((a, b) => (a.order - b.order)).splice(1)
                },
                scrollTo: function (y) {
                    if (this.bodyScrollingTimer)
                        clearTimeout(this.bodyScrollingTimer)

                    const _this = this
                    this.$nextTick(function () {
                        _this.bodyScrollingTimer = setTimeout(function () {
                            _this.$refs.chatBody.scroll({top: y})
                        }, 100)
                    })
                },
                scrollToBottom: function () {
                    if (this.bodyScrollingTimer)
                        clearTimeout(this.bodyScrollingTimer)

                    const _this = this
                    this.$nextTick(function () {
                        _this.bodyScrollingTimer = setTimeout(function () {
                            _this.$refs.chatBody.scroll({top: _this.$refs.chatBody.scrollHeight})
                        }, 100)
                    })
                },
                adminScrollToBottom: function () {
                    if (this.adminBodyScrollingTimer)
                        clearTimeout(this.adminBodyScrollingTimer)

                    const _this = this
                    // console.log(_this.$refs.adminChat.scrollHeight);
                    this.$nextTick(function () {
                        _this.adminBodyScrollingTimer = setTimeout(function () {
                            _this.$refs.adminChat.scroll({top: _this.$refs.adminChat.scrollHeight})
                        }, 100)
                    })
                },
                getImage: function (userName) {
                    return profileImageSources[Math.abs(userName.hashCode()) % profileImageSources.length]
                },
                deleteRoom: function () {
                    talkCommunicator.deleteRoom(this.roomId, this.channelType, this.senderKey, this.userKey)
                },
                reduceChatPanel: function (){
                    this.adminContent2= !this.adminContent2;
                    if(this.adminContent2)
                        this.isAdminNewMessage = false

                    let keyboardtext = document.getElementById('keyboardtext');
                    if(this.adminContent2){
                        keyboardtext.innerText='keyboard_arrow_down';
                    }else{
                        keyboardtext.innerText='keyboard_arrow_up';
                    }
                    this.adminScrollToBottom()
                },
                keywordSet: function (keyword) {
                    kmsKeyword = keyword
                    loadKmsList('')
                },
                remindUpdate: function (messageId, remindYn, messageSeq) {
                    const _this = this
                    console.log('remindUpdate > messageSeq = ', messageSeq)

                    restUtils.post(sttRequestUrl + '/set_stt_data', {
                        mode: 'remind',
                        company_id: _this.companyId,
                        uniqueid: _this.uniqueId,
                        userid: _this.loginId,
                        message_id: messageId,
                        remind_yn: remindYn ? 'Y' : 'N',
                        message_seq: messageSeq
                    }, false).done((data) => {
                        console.log('remindUpdate > done > data = ', data)
                    })
                    /*restSelf.put('/api/stt-text/remind/' + messageId, {
                        remindYn: remindYn ? 'Y' : 'N'
                    })*/
                },
                remindCopyBtn: function(){
                    console.log('this.messageList = ', this.messageList)
                    const remindArray = this.messageList.filter((v) => v.reMind === true);
                    console.log('remindArray = ', remindArray)
                    const remindContents = remindArray.map((v) => {
                        return v.contents
                    }).join('\n');
                    console.log('remindContents = ', remindContents)
                    window.navigator.clipboard.writeText(remindContents)
                    alert("복사 되었습니다.")
                },
                messageSend: function (message) {
                    if (!message) message = this.$refs.adminMessage.value.trim();
                    ipccAssistCommunicator.mentorCall(this.uniqueId, message, 'MENTOR');
                    this.$refs.adminMessage.value='';
                },
                keyDown: function (event) {
                    if (event.key === 'Enter') {
                        event.preventDefault();
                        return this.messageSend()
                    }
                }
            },
        }
        const sttCallPanel = Vue.createApp(Object.assign({}, callPanelProperties)).mount('#stt-call-panel')
    </script>
</tags:scripts>

<tags:scripts>
    <script>
        function processSttMessage(data) {
            console.log('processSttMessage data = ', data)
            sttCallPanel.appendMessage({
                uniqueId: data.call_uniqueid,
                phoneNumber: phoneNumber,
                time: data.time,
                messageType: data.kind,
                contents: data.data?.text,
                userId: data.ipcc_userid,
                extension: data.my_extension,
                username: data.ipcc_username,
                kmsKeyword: "",
                startMs: data.data?.start_ms,
                stopMs: data.data?.stop_ms,
                reMind: false,
                messageId: data.message_id,
                messageSeq: data.message_seq
            })
        }

        function processSttKeyword(data) {
            sttCallPanel.appendKeyword(data.data.keyword);
        }

        function processAdminMessage(data) {
            sttCallPanel.appendAdminMessage({
                company_id: data.company_id,
                message_id: data.message_id,
                userid: data.userid,
                target_userid: data.target_userid,
                call_uniqueid: data.call_uniqueid,
                message: data.message,
                time: Date.now()
            })
        }

        function sttLoad(uniqueId, number, userName) {
            sttClear();
            sttCallPanel.loadRoom(uniqueId, number, userName);
        }

        function sttClear() {
            sttCallPanel.clearRoom();
        }
    </script>
</tags:scripts>
