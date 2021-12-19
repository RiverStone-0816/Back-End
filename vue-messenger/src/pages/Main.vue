<template>
  <body class="font-sans-kr text-gray-800">
  <div class="h-screen min-w-full">
    <div class="flex flex-col flex-auto flex-shrink-0 rounded-lg-2xl h-full" :style="'background-color: ' + backgroundColor">
      <div class="flex flex-col h-full overflow-x-auto mb-1" ref="chatBody" style="scroll-behavior: smooth">
        <div class="flex flex-col h-full">
          <div class="grid grid-cols-12 gap-y-2">
            <template v-for="(message, iMessage) in messages" :key="iMessage">
              <div v-if="message.sender === 'SERVER' && (message.messageType !== 'custom_text' && message.messageType !== 'ipcc_control')" class="col-start-1 col-end-10 pl-3 pt-0 rounded-lg">
                <div class="flex flex-row items-center">
                  <div class="h-8 w-8 rounded-lg-full overflow-hidden">
                    <img class="w-full" :src="getBotIcon()" alt="bot-icon">
                  </div>
                  <div class="flex items-center p-3 font-bold">{{ displayName }}</div>
                </div>
              </div>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'ipcc_control'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div v-if="message.data.image" class="relative rounded-lg pt-1 pb-3">
                        <img class="w-full" :src="message.data.image" alt="intro image">
                      </div>
                      <div>
                        <p style="white-space: pre-wrap">{{ message.data.text_data }}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'intro'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div v-if="message.data.image" class="relative rounded-lg pt-1 pb-3">
                        <img class="w-full" :src="message.data.image" alt="intro image">
                      </div>
                      <div>
                        <p style="white-space: pre-wrap">{{ message.data.msg }}</p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-if="message.data.channel_list && message.data.channel_list.length" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div>
                        <p>다른 채널을 통한 상담을 원하시면 원하시는 서비스의 아이콘을 눌러주세요. 해당 서비스에 연결됩니다.</p>
                      </div>
                      <div class="flex flex-row-reverse pt-2">
                        <div v-for="(channel, i) in message.data.channel_list" :key="i" class="pl-2">
                          <a :href="channel.channel_url" target="_blank">
                            <img alt="kakao" :src="channel.channel_type === 'eicn' ? getEicnIcon() : channel.channel_type === 'kakao' ? getKakaoIcon()
                                  : channel.channel_type === 'line' ? getLineIcon() : getNaverIcon()">
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'fallback'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 pb-0 shadow rounded-lg w-full max-w-xs">
                      <button class="bg-blue-600 hover:bg-blue-700 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop.prevent="actFallback(message)">
                        {{ message.data.fallback_ment }}
                      </button>
                    </div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'api_result'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div>
                        <p style="white-space: pre-wrap">{{ makeApiResultMessage(message.data.next_api_result_tpl, message.data.api_result_body) }}</p>
                      </div>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'member'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div>
                        <p style="white-space: pre-wrap">{{ message.data.ment }}</p>
                      </div>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'member_text'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div>
                        <p style="white-space: pre-wrap">{{ message.data.text_data }}</p>
                      </div>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'block'">
                <div v-for="(e, i) in message.data?.display" :key="i" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div v-if="e.type === 'text'" class="relative text-sm bg-white shadow rounded-lg max-w-xs py-2 px-3">
                      <p style="white-space: pre-wrap">{{ e.element[0]?.content }}</p>
                    </div>
                    <div v-else-if="e.type === 'image'" class="relative">
                      <img alt="chat_image" class="w-full rounded-lg" :src="e.element[0]?.image">
                    </div>
                    <div v-else-if="e.type === 'card'" class="relative text-sm bg-white shadow rounded-lg max-w-xs">
                      <div class="relative">
                        <img alt="chat_image" class="w-full rounded-t-lg" :src="e.element[0]?.image">
                      </div>
                      <div class="p-3 pb-0 text-base font-bold">
                        <p>{{ e.element[0]?.title }}</p>
                      </div>
                      <div class="p-3 pt-2">
                        <p style="white-space: pre-wrap">{{ e.element[0]?.content }}</p>
                      </div>
                    </div>
                    <div v-else-if="e.type === 'list'" class="relative text-sm bg-white shadow rounded-lg max-w-xs w-full divide-y divide-fuchsia-300">
                      <div class="p-2 pl-3 text-base font-bold">
                        <p v-if="e.element[0]?.url"><a target="_blank" :href="e.element[0]?.url">{{ e.element[0]?.title }}</a></p>
                        <p v-else>{{ e.element[0]?.title }}</p>
                      </div>
                      <template v-if="e.element[1]?.title || e.element[1]?.image || e.element[1]?.content">
                        <div v-for="(e2, j) in getListElements(e)" :key="j" class="grid grid-cols-6 p-3">
                          <div class="col-start-1 h-full">
                            <img v-if="e2.image" class="rounded-lg w-12 h-12" :src="e2.image" alt="item image">
                          </div>
                          <div class="col-span-5">
                            <div class="pl-2 pt-0 pb-1">
                              <p v-if="e2.url"><a target="_blank" :href="e2.url">{{ e2.title }}</a></p>
                              <p v-else>{{ e2.title }}</p>
                            </div>
                            <div class="pl-2 pt-0 text-gray-600">
                              <p style="white-space: pre-wrap">{{ e2.content }}</p>
                            </div>
                          </div>
                        </div>
                      </template>
                    </div>
                    <div v-if="!(message.data?.button?.length) && (i + 1 === message.data.display.length)" class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
                <div v-for="(e, i) in getButtonGroups(message)" :key="i" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div v-if="e instanceof Array" class="relative text-sm bg-white py-2 px-3 pb-0 shadow rounded-lg w-full max-w-xs">
                      <button v-for="(e2, j) in e" :key="j" class="bg-blue-600 hover:bg-blue-700 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop.prevent="actButton(message, e2)">
                        {{ e2.btn_name }}
                      </button>
                    </div>
                    <div v-else class="relative text-sm bg-white shadow rounded-lg max-w-xs w-full divide-y divide-fuchsia-300">
                      <div v-for="(e2, j) in e.api_param" :key="j" class="p-2 pl-3 text-base font-bold -api-parent">
                        <p>{{ e2.display_name }}</p>
                        <div v-if="e2.type !== 'time'" class="relative w-full pt-2">
                          <input class="flex w-full border rounded-lg focus:outline-none focus:border-indigo-300 h-10 pl-2 pr-2" type="text"
                                 :name="e2.param_name" :class="(e2.type === 'calendar' && ' -datepicker ') || (e2.type === 'number' && ' -input-numerical ')">
                        </div>
                        <div v-else class="ui multi form">
                          <select :name="e2.param_name + '.meridiem'" class="slt">
                            <option value="am">오전</option>
                            <option value="pm">오후</option>
                          </select>
                          <select :name="e2.param_name + '.hour'" class="slt">
                            <option v-for="hour in 12" :key="hour" :value="hour - 1">{{ hour - 1 }}</option>
                          </select>
                          <span class="unit">시</span>
                          <select :name="e2.param_name + '.minute'" class="slt">
                            <option v-for="minute in 60" :key="minute" :value="minute - 1">{{ minute - 1 }}</option>
                          </select>
                          <span class="unit">분</span>
                        </div>
                      </div>
                      <div class="relative text-sm bg-white py-2 px-3 pb-0 shadow rounded-b-lg w-full max-w-xs">
                        <button class="bg-blue-600 hover:bg-blue-700 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop.prevent="actApi(message, e, $event)">제출</button>
                      </div>
                    </div>
                    <div v-if="i + 1 === getButtonGroups(message).length" class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender !== 'SERVER' && message.messageType === 'text'">
                <div class="col-start-1 col-end-13 pb-2 rounded-lg">
                  <div class="flex justify-start flex-row-reverse">
                    <div class="relative mr-3 text-sm bg-gray-700 py-2 px-3 shadow rounded-lg max-w-xs text-white">
                      <div style="white-space: pre-wrap">{{ message.data }}</div>
                    </div>
                    <div class="flex text-xs pr-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>
            </template>
          </div>
        </div>
      </div>

      <!--하단 채팅 입력-->
      <div v-if="inputEnable" class="flex flex-row items-center h-16 rounded-lg-xl bg-white w-full px-3 border-t">
        <div>
          <button class="flex items-center justify-center rounded-lg hover:bg-gray-200 h-10 w-10 text-white flex-shrink-0 mr-3">
            <span class="ml-1">
              <svg height="25" viewBox="15 5 10 25" width="36" xmlns="http://www.w3.org/2000/svg">
                <path d="M15,30V21h6v9h7.5V18H33L18,4.5,3,18H7.5V30Z" fill="#514f65"/>
              </svg>
            </span>
          </button>
        </div>
        <div class="flex-grow">
          <div class="relative w-full">
            <input class="flex w-full border rounded-lg focus:outline-none focus:border-indigo-300 h-10 pl-2 pr-2" type="text" v-model="input" @keyup.stop.prevent="$event.key==='Enter'&&sendText()">
          </div>
        </div>
        <div class="ml-2">
          <button class="flex items-center justify-center bg-gray-700 hover:bg-gray-800 rounded-lg h-10 w-10 text-white flex-shrink-0" @click.stop.prevent="sendText">
            <span class="ml-1">
              <svg height="16" viewBox="0 0 18 16" width="18" xmlns="http://www.w3.org/2000/svg">
                <path d="M3.009,20.5,21,12.5,3.009,4.5,3,10.722,15.857,12.5,3,14.278Z" fill="#fff" transform="translate(-3 -4.5)"/>
              </svg>
            </span>
          </button>
        </div>
      </div>
    </div>
  </div>
  </body>
</template>

<script>
import moment from 'moment'
import botIcon from '../assets/bot-icon.png'
import kakaoIcon from '../assets/kakao-icon.png'
import naverIcon from '../assets/naver-icon.png'
import lineIcon from '../assets/line-icon.png'
import eicnIcon from '../assets/eicn-icon.png'
import debounce from '../utillities/mixins/debounce'
import store from '../store/index'

const SENDER = Object.freeze({SERVER: 'SERVER', USER: 'USER'})

function zeroPad(nr, base) {
  let len = (String(base).length - String(nr).length) + 1;
  return len > 0 ? new Array(len).join('0') + nr : nr;
}

export default {
  mixins: [debounce],
  components: {},
  data() {
    return {
      communicator: window.communicator,

      inputEnable: false,
      backgroundColor: '#f3f3f3',
      displayName: null,
      botId: null,
      lastReceiveMessageType: null,
      input: '',

      messages: []
    }
  },
  methods: {
    getBotIcon: () => botIcon,
    getKakaoIcon: () => kakaoIcon,
    getNaverIcon: () => naverIcon,
    getLineIcon: () => lineIcon,
    getEicnIcon: () => eicnIcon,
    showAlert: content => store.commit('alert/show', content),
    getTimeFormat: value => moment(value).format('YY-MM-DD HH:mm'),
    getButtonGroups(message) {
      return message.data?.button?.reduce((list, e) => {
        if (e.action === 'api') list.push(e)
        else if (!list.length || !(list[list.length - 1] instanceof Array)) list.push([e])
        else list[list.length - 1].push(e)
        return list
      }, [])
    },
    getListElements(display) {
      return JSON.parse(JSON.stringify(display)).element?.sort((a, b) => (a.sequence - b.sequence)).splice(1)
    },
    makeApiResultMessage(next_api_result_tpl, api_result_body) {
      const KEYWORD_CHAR = '$'
      let result = ''

      for (let position = 0; ;) {
        const indexKeywordChar = next_api_result_tpl.indexOf(KEYWORD_CHAR, position)
        if (indexKeywordChar < 0) {
          result += next_api_result_tpl.substr(position)
          break
        }
        const indexSpace = next_api_result_tpl.regexIndexOf(/\s/, indexKeywordChar)
        if (indexKeywordChar + 1 !== indexSpace) {
          result += next_api_result_tpl.substr(position, indexKeywordChar - position)
          const keyword = next_api_result_tpl.substr(indexKeywordChar + 1, indexSpace - indexKeywordChar - 1)
          if (api_result_body[keyword] !== undefined) {
            result += api_result_body[keyword]
          } else {
            result += KEYWORD_CHAR + keyword
          }
        } else {
          result += KEYWORD_CHAR
        }
        position = indexSpace
      }

      return result
    },
    sendText() {
      if (!this.input) return
      this.communicator.sendText(this.botId, this.input, this.lastReceiveMessageType)
      this.messages.push({sender: SENDER.USER, time: new Date(), data: JSON.parse(JSON.stringify(this.input)), messageType: 'text'})
      this.input = ''
    },
    actFallback(message) {
      if (message.data.fallback_action === 'first') {
        this.communicator.requestRootBlock(this.botId)
      } else if (message.data.fallback_action === 'url') {
        window.open(message.data.fallback_action_data, '_blank')
      } else if (message.data.fallback_action === 'member') {
        this.communicator.sendAction(this.botId, {
          chatbot_id: this.botId,
          action: 'member',
          action_data: message.data.fallback_action_data,
        }, this.lastReceiveMessageType)
      } else if (message.data.fallback_action === 'block') {
        this.communicator.sendAction(this.botId, {
          chatbot_id: this.botId,
          action: 'block',
          action_data: message.data.fallback_action_data,
        }, this.lastReceiveMessageType)
      } else if (message.data.fallback_action === 'phone') {
        store.commit('alert/show', 'phone 처리 방법을 전달받아야 함')
      } else {
        store.commit('alert/show', `미처리된 부분이 있음: ${message.data.fallback_action}:${message.data.fallback_action_data}`)
      }
    },
    actButton(message, button) {
      if (button.action === 'url') return window.open(button.next_action_data, null)

      this.communicator.sendAction(this.botId, {
        chatbot_id: message.data.chatbot_id,
        parent_block_id: message.data.block_id,
        btn_id: button.btn_id,
        btn_name: button.btn_name,
        action: button.action,
        action_data: button.next_action_data,
      }, this.lastReceiveMessageType)
    },
    actApi(message, button, event) {
      const data = {}
      const result = {}
      const elements = event.target.closest('.-api-parent').querySelectorAll('[name]')
      for (let i = 0; i < elements.length; i++) {
        if (!elements[i].value) return store.commit('alert/show', 'API 호출을 위해 필요 정보를 모두 입력해야 합니다.')

        const split = elements[i].name.split('.')
        if (split.length === 1) {
          result[elements[i].name] = elements[i].value
        } else {
          if (!result[split[0]]) result[split[0]] = {}
          result[split[0]][split[1]] = elements[i].value
        }
      }
      for (let name in result) {
        if (typeof result[name] === 'object') {
          data[name] = zeroPad((result[name].meridiem === 'am' ? 0 : 12) + parseInt(result[name].hour), '00') + ':' + zeroPad(result[name].minute, '00')
        } else {
          data[name] = result[name]
        }
      }

      this.communicator.sendAction(this.botId, {
        chatbot_id: message.data.chatbot_id,
        parent_block_id: message.data.block_id,
        btn_id: button.btn_id,
        btn_name: button.btn_name,
        action: button.action,
        action_data: button.next_action_data,
        api_param_value: data
      }, this.lastReceiveMessageType)
    },
  },
  updated() {
    this.debounce(() => this.$refs.chatBody.scroll({top: this.$refs.chatBody.scrollHeight}), 100)
  },
  mounted() {
    this.communicator
        .on('disconnect', () => store.commit('alert/show', '연결이 종료되었습니다.'))
        .on('webchatsvc_message', data => {
          this.lastReceiveMessageType = data.message_type

          if (data.message_type === 'intro') {
            this.inputEnable = data.message_data.is_chatt_enable === 'Y'
            this.backgroundColor = data.message_data.bgcolor
            this.displayName = data.message_data.display_company_name
            this.botId = data.message_data.schedule_info?.schedule_data

            if (this.botId) this.communicator.requestRootBlock(this.botId)
          } else if (data.message_type === 'member') {
            this.messages.splice(0, this.messages.length)
          } else if (data.message_type === 'ipcc_control') {
            this.inputEnable = !(data.message_data.session_keep_yn === 'N')
          }

          this.messages.push({sender: SENDER.SERVER, time: new Date(), data: data.message_data, messageType: data.message_type})
        })
        .requestIntro()
  },
}
</script>
