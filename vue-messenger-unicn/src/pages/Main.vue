<template>
  <body class="font-sans-kr text-gray-800" @contextmenu.prevent>
  <div class="h-screen min-w-full">
    <div class="flex flex-col flex-auto flex-shrink-0 rounded-lg-2xl h-full" :style="'background-color: ' + backgroundColor">
      <div class="flex flex-col h-full overflow-x-auto mb-1" ref="chatBody" style="scroll-behavior: smooth">
        <div class="flex flex-col h-full">
          <div class="grid grid-cols-12 gap-y-2">
            <template v-for="(message, iMessage) in messages" :key="iMessage">
              <div v-if="message.sender === 'SERVER' && (message.messageType !== 'custom_text' && message.messageType !== 'ipcc_control')" class="col-start-1 col-end-10 pl-3 pt-0 rounded-lg">
                <div class="flex flex-row items-center">
                  <div class="h-8 w-8 rounded-lg-full overflow-hidden">
                    <img class="w-full" :src="botIcon === '' ? getBotIcon : botIcon" alt="bot-icon">
                  </div>
                  <div class="flex items-center p-3 font-bold">{{ displayName }}</div>
                </div>
              </div>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'ipcc_control'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xs">
                      <div v-if="message.data.image" class="relative rounded-lg pt-1 pb-3">
                        <img class="w-full" :src="getFileUrl(message.company, message.data.image)" alt="intro image">
                      </div>
                      <div>
                        <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.text_data }}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'intro'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 w-full shadow rounded-lg max-w-xl">
                      <div v-if="message.data.profile" class="relative rounded-lg pt-1 pb-3">
                        <img class="w-full" :src="getFileUrl(message.company, message.data.profile)" alt="intro image">
                      </div>
                      <div>
                        <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.msg }}</p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-if="message.data.channel_list && message.data.channel_list.length" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 w-full shadow rounded-lg max-w-xl">
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

                <div v-if="['A', 'F'].includes(message.data.schedule_info.schedule_kind)" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div>
                        <p style="white-space: pre">{{ message.data.schedule_info.schedule_ment }}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'fallback'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row items-center">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xl">
                      <div>
                        <p>{{ message.data.fallback_ment }}</p>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <button class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" @click.stop="actFallback(message)">
                      {{ getFallbackButtonName(message.data.fallback_action) }}
                    </button>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'api_result'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xs">
                      <div>
                        <p style="white-space: pre-wrap; line-break: anywhere;">{{ mmessage.data.result_content }}</p>
                      </div>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
                <div v-for="(e, i) in getButtonGroups(message)" :key="i" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm pb-0 rounded-lg w-full max-w-xs">
                      <span v-if="e instanceof Array">
                        <button v-for="(e2, j) in e" :key="j" class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" @click.stop="actButton(message, e2)">
                          {{ e2.btn_name }}
                        </button>
                      </span>
                    </div>
                    <div v-if="i === getButtonGroups(message).length" class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'member'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xs">
                      <div>
                        <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.ment }}</p>
                      </div>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'member_text'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white py-2 px-3 shadow rounded-lg max-w-xs">
                      <div class="divide-y">
                        <template v-if="message.data.replyingType">
                          <div class="flex pb-2 text-gray-400">
                            <div v-if="['image', 'image_temp'].includes(message.data.replyingType)" class="pr-4">
                              <img :src="message.data.replyingTarget" class="h-10">
                            </div>
                            <div>
                              <div class="pb-2">
                                <p v-if="message.data.replyingType === 'text'" style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.replyingTarget }}</p>
                                <a v-else :href="message.data.replyingTarget" target="_blank">{{ ['image', 'image_temp'].includes(message.data.replyingType) ? '사진' : '파일' }}</a>
                              </div>
                            </div>
                          </div>
                          <p class="pt-2" style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.text_data }}</p>
                        </template>
                        <p v-else style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.text_data }}</p>
                      </div>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && ['block', 'member_block_temp'].includes(message.messageType)">
                <div v-for="(e, i) in message.data?.display" :key="i" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div v-if="e.type === 'text'" class="relative text-sm bg-white shadow rounded-lg max-w-seohui py-2 px-3">
                      <p style="white-space: pre-wrap; line-break: anywhere;">{{ e.element[0]?.content }}</p>
                    </div>
                    <div v-else-if="e.type === 'image'" class="relative max-w-seohui">
                      <img alt="chat_image" class="w-full rounded-lg" :src="getFileUrl(message.company, e.element[0]?.image)">
                    </div>
                    <div v-else-if="e.type === 'card'" class="relative text-sm bg-white shadow rounded-lg max-w-seohui">
                      <div class="relative max-w-xs">
                        <img alt="chat_image" class="w-full rounded-t-lg" :src="getFileUrl(message.company, e.element[0]?.image)">
                      </div>
                      <div class="p-3 pb-0 text-base font-bold">
                        <p>{{ e.element[0]?.title }}</p>
                      </div>
                      <div class="p-3 pt-2">
                        <p style="white-space: pre-wrap; line-break: anywhere;">{{ e.element[0]?.content }}</p>
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
                            <img v-if="e2.image" class="rounded-lg w-12 h-12" :src="getFileUrl(message.company, e2.image)" alt="item image">
                          </div>
                          <div class="col-span-5">
                            <div class="pl-2 pt-0 pb-1">
                              <p v-if="e2.url"><a target="_blank" :href="e2.url">{{ e2.title }}</a></p>
                              <p v-else>{{ e2.title }}</p>
                            </div>
                            <div class="pl-2 pt-0 text-gray-600">
                              <p style="white-space: pre-wrap; line-break: anywhere;">{{ e2.content }}</p>
                            </div>
                          </div>
                        </div>
                      </template>
                    </div>
                    <div v-else-if="e.type === 'input'" class="relative text-sm bg-white shadow rounded-lg max-w-xs w-full divide-y divide-fuchsia-300 -api-parent" ref="apiparent">
                      <div class="p-2 pl-3 "><p>{{ e.element[0]?.title }}</p></div>
                      <div v-for="(e2, j) in getInputElements(e)" :key="j" class="p-2 pl-3 font-bold">
                        <p>{{ e2.input_display_name }}</p>
                        <div v-if="e2.input_type !== 'time'" class="relative w-full pt-2">
                          <input class="flex w-full border rounded-lg focus:outline-none focus:border-indigo-300 h-8 pl-2 pr-2"
                                 :type="(e2.input_type === 'calendar' && 'date') || (e2.input_type === 'number' && 'number') || (e2.input_type === 'secret' && 'password') || (e2.input_type === 'text' && 'text')"
                                 :name="e2.input_param_name" :data-value="e2.input_need_yn">
                        </div>
                        <div v-else class="ui multi form">
                          <select :name="e2.input_param_name + '.meridiem'" :data-value="e2.input_need_yn" class="slt rounded-lg h-10">
                            <option value="am">오전</option>
                            <option value="pm">오후</option>
                          </select>
                          <select :name="e2.input_param_name + '.hour'" :data-value="e2.input_need_yn" class="slt rounded-lg h-10">
                            <option v-for="hour in 12" :key="hour" :value="hour - 1">{{ hour - 1 }}</option>
                          </select>
                          <span class="unit">시</span>
                          <select :name="e2.input_param_name + '.minute'" :data-value="e2.input_need_yn" class="slt rounded-lg h-10">
                            <option v-for="minute in 60" :key="minute" :value="minute - 1">{{ minute - 1 }}</option>
                          </select>
                          <span class="unit">분</span>
                        </div>
                      </div>
                    </div>
                    <div v-if="!(message.data?.button?.length) && (i + 1 === message.data.display.length)" class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
                <div v-for="(e, i) in getButtonGroups(message)" :key="i" class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm pb-0 rounded-lg w-full max-w-xs">
                      <span v-if="e instanceof Array">
                        <button v-for="(e2, j) in e" :key="j" class="bg-gray-400 hover:bg-gray-500 text-white mb-2 ml-1 py-1 p-2 rounded-md" @click.stop="actButton(message, e2)">
                          {{ e2.btn_name }}
                        </button>
                      </span>
                      <span v-else>
                        <button class="bg-gray-400 hover:bg-gray-500 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop="actApi(message, e, $event)">
                          {{ e.btn_name }}
                        </button>
                      </span>
                    </div>
                    <div v-if="i + 1 === getButtonGroups(message).length" class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'member_image_temp'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative max-w-xs">
                      <img alt="chat_image" class="w-full rounded-lg" :src="message.data.fileUrl">
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'audio_start'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white shadow rounded-lg max-w-xs w-full divide-y divide-fuchsia-300 -api-parent" ref="apiparent">
                      <div class="p-2 pl-3 "><p>상담원이 음성통화를 요청합니다.</p></div>
                    </div>
                  </div>
                </div>
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm pb-0 rounded-lg w-full max-w-xs">
                      <button class="bg-gray-400 hover:bg-gray-500 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop="audioStart(true)">수락</button>
                      <button class="bg-gray-400 hover:bg-gray-500 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop="audioStart(false)">거절</button>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'video_start'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white shadow rounded-lg max-w-xs w-full divide-y divide-fuchsia-300 -api-parent" ref="apiparent">
                      <div class="p-2 pl-3 "><p>상담원이 영상통화를 요청합니다.</p></div>
                    </div>
                  </div>
                </div>
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div class="relative text-sm pb-0 rounded-lg w-full max-w-xs">
                      <button class="bg-gray-400 hover:bg-gray-500 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop="videoStart(true)">수락</button>
                      <button class="bg-gray-400 hover:bg-gray-500 text-white w-full mb-2 py-2 px-4 rounded-md" @click.stop="videoStart(false)">거절</button>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender === 'SERVER' && message.messageType === 'member_file'">
                <div class="col-start-1 col-end-13 p-3 pt-0 rounded-lg">
                  <div class="flex flex-row">
                    <div v-if="isImage(message.data.file_name)" class="relative max-w-xs">
                      <img alt="chat_image" class="w-full rounded-lg" :src="message.data.fileUrl">
                    </div>
                    <div v-else-if="isAudio(message.data.file_name)" class="relative max-w-xs maudio">
                      <audio :src="e.fileUrl" initaudio="false"></audio>
                      <div class="audio-control">
                        <a href="javascript:" class="fast-reverse"></a>
                        <a href="javascript:" class="play"></a>
                        <a href="javascript:" class="fast-forward"></a>
                        <div class="progress-bar">
                          <div class="progress-pass"></div>
                        </div>
                        <div class="time-keep">
                          <span class="current-time">00:00</span> / <span class="duration">00:00</span>
                        </div>
                      </div>
                    </div>
                    <div v-else-if="isVideo(message.data.file_name)" class="relative max-w-xs">
                      <video controls :src="e.fileUrl"></video>
                    </div>
                    <div v-else class="relative text-sm bg-white py-2 px-3 pb-0 shadow rounded-lg w-full max-w-xs">
                      <a class="w-full rounded-lg" :href="message.data.fileUrl">{{ message.data.file_name }}
                        <hr/>
                        <b>파일 저장하기</b></a>
                    </div>
                    <div class="flex text-xs pl-3 items-end">{{ getTimeFormat(message.time) }}</div>
                  </div>
                </div>
              </template>

              <template v-if="message.sender !== 'SERVER' && message.messageType === 'text'">
                <div class="col-start-1 col-end-13 pb-2 rounded-lg">
                  <div class="flex justify-start flex-row-reverse">
                    <div class="relative mr-3 text-sm bg-gray-700 py-2 px-3 shadow rounded-lg max-w-xs text-white">
                      <div style="white-space: pre-wrap; line-break: anywhere;">{{ message.data }}</div>
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
        <div v-if="homeEnable">
          <button class="flex items-center justify-center rounded-lg hover:bg-gray-200 h-10 w-10 text-white flex-shrink-0 mr-3" @click.stop="homeAction">
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
          <button class="flex items-center justify-center bg-gray-700 hover:bg-gray-800 rounded-lg h-10 w-10 text-white flex-shrink-0" @click.stop="sendText">
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
<iframe style="display: none" ref="runPhone"></iframe>
<script>
import moment from 'moment'
import $ from 'jquery'
import botIcon from '../assets/bot-icon.png'
import eicnIcon from '../assets/bot-icon.png'
import kakaoIcon from '../assets/kakao-icon.png'
import naverIcon from '../assets/naver-icon.png'
import lineIcon from '../assets/line-icon.png'
import debounce from '../utillities/mixins/debounce'
import SimpleTone from "@/assets/sounds/SimpleTone.mp3"
import BusySignal from "@/assets/sounds/BusySignal.mp3"
import store from '../store/index'
import Communicator from "../utillities/Communicator"
import maudio from "../utillities/maudio"
import sessionUtils from "@/utillities/sessionUtils"
import axios from "axios"
import modalOpener from "@/utillities/mixins/modalOpener"
import Janus from "../utillities/janus"


const SENDER = Object.freeze({SERVER: 'SERVER', USER: 'USER'})

function zeroPad(nr, base) {
  let len = (String(base).length - String(nr).length) + 1;
  return len > 0 ? new Array(len).join('0') + nr : nr;
}

function getQueryStringValue(name) {
  name = name.replace(/[\\[]/, "\\[").replace(/[\]]/, "\\]")
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search)
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "))
}

// ref: http://daplus.net/javascript-%EC%A0%95%EA%B7%9C-%ED%91%9C%ED%98%84%EC%8B%9D%EC%9D%84-%ED%97%88%EC%9A%A9%ED%95%98%EB%8A%94-javascript%EC%9D%98-string-indexof-%EB%B2%84%EC%A0%84%EC%9D%B4-%EC%9E%88%EC%8A%B5%EB%8B%88/
String.prototype.regexIndexOf = function (regex, startpos) {
  var indexOf = this.substring(startpos || 0).search(regex);
  return (indexOf >= 0) ? (indexOf + (startpos || 0)) : indexOf;
}

export default {
  mixins: [debounce, modalOpener],
  components: {},
  setup() {
    return {
      REPLYING_INDICATOR: '\u001b',
      REPLYING_TEXT: '\u001a',
      REPLYING_IMAGE: '\u000f',
      REPLYING_FILE: '\u000e',
      REPLYING_IMAGE_TEMP: '\u000d',
      VCHAT_RECONNECT_INTERVAL: 5000,
    }
  },
  data() {
    return {
      communicator: new Communicator(),

      inputEnable: false,
      backgroundColor: '#f3f3f3',
      displayName: null,
      botId: null,
      lastReceiveMessageType: null,
      input: '',
      homeEnable: true,
      messages: [],
      botIcon: '',
      form: {
        url: 'https://cloudtalk.eicn.co.kr:442',
        senderKey: null,
        userKey: sessionUtils.getSessionId(),
        ip: '',
        mode: 'service',
      },
      webrtcData: null,

      //Webrtc
      WEBRTC_INFO: {
        server: {
          "webrtc_server_ip": null,
          "webrtc_server_port": null,
          "turn_server_ip": null,
          "turn_server_port": null,
          "turn_user": null,
          "turn_secret": null
        },
        env: { "ringtone": true, "busytone": true },
      },
      RINGTONE: null,
      BUSYTONE: null,
      opened: true,
      janusVChat: null,
      vchat: null,
      vchatOpaqueId: "vchat-"+Janus.randomString(12),
      vchatBitrateTimer: null,
      vchatSpinner: null,
      myUsername: window.form.remote_username,
      remoteUsername: window.form.my_username,
      doSimulcast: (getQueryStringValue("simulcast") === "yes" || getQueryStringValue("simulcast") === "true"),
      simulcastStarted: false,
      vchatRegistered: false,
      vchatRegisterTimerId: null,
      vchatReconnectTimerId: null,
      accept_vchat: null,
      callback_vchat_outgoing_call: () => {},
      callback_vchat_incoming_call: () => {},
      callback_vchat_accept: () => {},
      callback_vchat_hangup: () => {},
      callback_vchat_registered_status: () => {},
      callback_vchat_unregistered_status: () => {},
      callback_vchat_disconnected_status: () => {},

      DIAL_AUDIO_VCHAT_BTN: false,
      DIAL_VIDEO_VCHAT_BTN: false,
      HANGUP_VCHAT_BTN: false,
      ACCEPT_VCHAT_BTN: false,
      $ACCEPT_VCHAT_BTN: null,
      LOCAL_VCHAT_STREAM_OBJECT: $('#myvideo'),
      REMOTE_VCHAT_STREAM_OBJECT: $('#remotevideo'),
      start_recording: null,
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
    getInputElements(display) {
      return JSON.parse(JSON.stringify(display)).element?.sort((a, b) => (a.sequence - b.sequence)).splice(1)
    },
    makeApiResultMessage(data) {
      if (!data.api_result_error_ment) {
        const next_api_result_tpl = data.next_api_result_tpl
        const api_result_body = data.api_result_body
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
      } else {
        return data.api_result_error_ment
      }
    },
    sendText() {
      if (!this.input) return
      this.communicator.sendText(this.botId, this.input, this.lastReceiveMessageType)
      this.messages.push({sender: SENDER.USER, time: new Date(), data: JSON.parse(JSON.stringify(this.input)), messageType: 'text'})
      this.input = ''
    },
    homeAction() {
      this.communicator.requestRootBlock(this.botId)
    },
    getFallbackButtonName(action){
      if (action === 'first') {
        return "처음으로"
      } else if (action === 'url') {
        return "홈페이지 보기"
      } else if (action === 'member') {
        return "고객센터 연결"
      } else if (action === 'block') {
        return "이전으로"
      } else if (action === 'phone') {
        return "전화하기"
      } else {
        return "설정된 키워드가 없습니다."
      }
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
        return window.open('tel:', "_blank")
      } else {
        store.commit('alert/show', `미처리된 부분이 있음: ${message.data.fallback_action}:${message.data.fallback_action_data}`)
      }
    },
    actButton(message, button) {
      if (button.action === 'url') return window.open(button.next_action_data, null)

      if (button.action === 'phone') return window.open('tel:'+button.next_action_data, "_blank")

      this.communicator.sendAction(this.botId, {
        chatbot_id: message.data.chatbot_id,
        parent_block_id: message.data.block_id,
        btn_id: button.btn_id,
        btn_name: button.btn_name,
        action: button.action,
        action_data: button.next_action_data,
      }, this.lastReceiveMessageType)
    },
    actApi(message, button) {
      const data = {}
      const result = {}
      const elements = this.$refs.apiparent.querySelectorAll('[name]')
      for (let i = 0; i < elements.length; i++) {
        if (!elements[i].value && elements[i].dataset.value === 'Y') return store.commit('alert/show', {body: 'API 호출을 위해 필요 정보를 모두 입력해야 합니다.', isClose: false})

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
    getFileUrl(company, fileName) {
      return `https://cloudtalk.eicn.co.kr:442/webchat_bot_image_fetch?company_id=${encodeURIComponent(company)}&file_name=${encodeURIComponent(fileName)}&channel_type=${encodeURIComponent("eicn")}`
    },
    isImage(fileName) {
      if (!fileName) return false
      return fileName.toLowerCase().endsWith('.jpg')
          || fileName.toLowerCase().endsWith('.jpeg')
          || fileName.toLowerCase().endsWith('.png')
          || fileName.toLowerCase().endsWith('.bmp')
          || fileName.toLowerCase().endsWith('.gif')
    },
    isAudio(fileName) {
      if (!fileName) return false
      return fileName.toLowerCase().endsWith('.mp3')
          || fileName.toLowerCase().endsWith('.wav')
    },
    isVideo(fileName) {
      if (!fileName) return false
      return fileName.toLowerCase().endsWith('.mp4')
          || fileName.toLowerCase().endsWith('.avi')
          || fileName.toLowerCase().endsWith('.wmv')
          || fileName.toLowerCase().endsWith('.mov')
    },
    audioStart(chk) {
      if (chk) {
        this.set_record_file(this.webrtcData.record_file)
        this.start_vchat()
        this.communicator.sendWebrtcReady('audio_start_ready',{ready_result: 0, ready_result_msg: '준비완료'})
      } else{
        this.communicator.sendWebrtcReady('audio_start_ready',{ready_result: 1, ready_result_msg: '거절'})
      }
    },
    videoStart(chk) {
      if (chk) {
        this.set_record_file(this.webrtcData.record_file)
        this.start_vchat()
        this.communicator.sendWebrtcReady('video_start_ready',{ready_result: 0, ready_result_msg: '준비완료'})
      } else{
        this.communicator.sendWebrtcReady('video_start_ready',{ready_result: 1, ready_result_msg: '거절'})
      }
    },
    //webrtc
    start_vchat() {
      const _this = this
      if (_this.vchatReconnectTimerId) {
        clearInterval(_this.vchatReconnectTimerId);
        _this.vchatReconnectTimerId = null;
      }
      if (_this.janusVChat) {
        delete _this.janusVChat;
        _this.janusVChat = null;
      }

      Janus.init({debug: "all", callback: this.create_vchat_session});
    },
    create_vchat_session() {
      const webrtc_server = "wss://" + this.WEBRTC_INFO.server.webrtc_server_ip + ":" + this.WEBRTC_INFO.server.webrtc_server_port;
      const turn_server = "turn:" + this.WEBRTC_INFO.server.turn_server_ip + ":" + this.WEBRTC_INFO.server.turn_server_port;
      const turn_user = this.WEBRTC_INFO.server.turn_user;
      const turn_secret = this.WEBRTC_INFO.server.turn_secret;
      const _this = this

      // 세션 생성
      this.janusVChat = new Janus(
          {
            server: webrtc_server,
            iceServers: [ {urls: turn_server, username: turn_user, credential: turn_secret} ],
            success: function() {
              _this.vchatRegistered = false;

              // VideoCall 플러그인 Attach
              _this.janusVChat.attach(
                  {
                    plugin: "janus.plugin.videocall",
                    opaqueId: _this.vchatOpaqueId,
                    success: function(pluginHandle) {
                      _this.vchat = pluginHandle;
                      Janus.log("Plugin attached! (" + _this.vchat.getPlugin() + ", id=" + _this.vchat.getId() + ")");

                      _this.callback_vchat_unregistered_status();

                      // TODO: 버튼 클릭시 register???
                      _this.register_vchat(_this.myUsername);
                    },
                    error: function(error) {
                      Janus.error("  -- Error attaching plugin...", error);
                    },
                    consentDialog: function(on) {
                      // getUserMedia 호출 전에 trigger된다.
                      Janus.log("Consent dialog should be " + (on ? "on" : "off") + " now");
                      /*if (on) {
                      }
                      else {
                      }*/
                    },
                    iceState: function(state) {
                      Janus.log("ICE state changed to " + state);
                    },
                    mediaState: function(medium, on) {
                      Janus.log("Janus " + (on ? "started" : "stopped") + " receiving our " + medium);
                    },
                    webrtcState: function(on) {
                      Janus.log("Janus says our WebRTC PeerConnection is " + (on ? "up" : "down") + " now");
                    },
                    onmessage: function(msg, jsep) {
                      Janus.debug(" ::: Got a message :::", msg);

                      // TODO: error 체크 부분이 필요한 것인지 확인 필요
                      var error = msg["error"];
                      if (error) {
                        if (_this.vchatRegistered) {
                          // Reset status
                          _this.vchat.hangup();
                        }
                        Janus.error(error);
                        return;
                      }
                      /////////////////////////////////////////////

                      var result = msg["result"];
                      if (result) {
                        if (result["list"]) {
                          var list = result["list"];
                          Janus.debug("Got a list of registered peers:", list);
                          for (var mp in list) {
                            Janus.debug("  >> [" + list[mp] + "]");
                          }
                        }
                        else if (result["event"]) {
                          var event = result["event"];
                          $('#print_text').text("event: " + event);
                          if (event === 'registered') {
                            _this.myUsername = result["username"];
                            Janus.log("Successfully registered as " + _this.myUsername + "!");
                            if (!_this.vchatRegistered) {
                              _this.vchatRegistered = true;
                            }

                            _this.callback_vchat_registered_status();

                            // Get a list of available peers, just for fun
                            _this.vchat.send({ message: { request: "list" }});
                            // 등록 성공했으므로, Call 버튼 활성화
                            // TODO:
                            _this.DIAL_AUDIO_VCHAT_BTN = true
                            _this.DIAL_VIDEO_VCHAT_BTN = true
                          }
                          else if (event === 'calling') { // 발신중...
                            Janus.log("Waiting for the peer to answer...");

                            // 전화끊기(Hangup) 버튼 이벤트 등록
                            _this.HANGUP_VCHAT_BTN = true

                            // Ringtone 플레이
                            _this.playTone("ring");

                            _this.callback_vchat_outgoing_call();
                          }
                          else if (event === 'incomingcall') {
                            Janus.log("Incoming call from " + result["username"] + "!");
                            _this.remoteUsername = result["username"];

                            // 전화끊기(Hangup) 버튼 이벤트 등록
                            _this.HANGUP_VCHAT_BTN = true

                            // TODO: 전화수신을 알림
                            // 전화받기 함수 등록
                            _this.accept_vchat = function() {
                              _this.accept_vchat = null;
                              _this.vchat.createAnswer(
                                  {
                                    jsep: jsep,
                                    // No media provided: by default, it's sendrecv for audio and video
                                    media: { data: true },  // Let's negotiate data channels as well
                                    // If you want to test simulcasting (Chrome and Firefox only), then
                                    // pass a ?simulcast=true when opening this demo page: it will turn
                                    // the following 'simulcast' property to pass to janus.js to true
                                    simulcast: _this.doSimulcast,
                                    success: function(jsep) {
                                      Janus.debug("Got SDP!", jsep);
                                      var body = { request: "accept" };
                                      _this.vchat.send({ message: body, jsep: jsep });
                                    },
                                    error: function(error) {
                                      Janus.error("WebRTC error:", error);
                                    }
                                  }
                              );
                            };
                            _this.ACCEPT_VCHAT_BTN = true
                            _this.$ACCEPT_VCHAT_BTN = $('#btn-accept')
                            if (_this.$ACCEPT_VCHAT_BTN) {
                              _this.$ACCEPT_VCHAT_BTN.one('click', function () {
                                _this.accept_vchat();
                              });
                            }

                            // Ringtone 플레이
                            _this.playTone("ring");

                            _this.callback_vchat_incoming_call();
                          }
                          else if (event === 'accepted') {
                            var peer = result["username"];
                            if (!peer) {
                              Janus.log("Call started!");
                            }
                            else {
                              Janus.log(peer + " accepted the call!");
                              _this.remoteUsername = peer;
                            }
                            // Video call can start
                            if (jsep) {
                              _this.vchat.handleRemoteJsep({ jsep: jsep });
                            }

                            _this.start_recording(jsep);
                            _this.callback_vchat_accept();

                            // Ringtone 중지
                            _this.stopTones();

                            // TODO: 전화끊기(Hangup) 버튼 이벤트 등록
                            // doHangup() 호출
                          }
                          else if (event === 'update') {
                            // An 'update' event may be used to provide renegotiation attempts
                            if (jsep) {
                              if (jsep.type === "answer") {
                                _this.vchat.handleRemoteJsep({ jsep: jsep });
                              }
                              else {
                                _this.vchat.createAnswer(
                                    {
                                      jsep: jsep,
                                      media: { data: true },  // Let's negotiate data channels as well
                                      success: function(jsep) {
                                        Janus.debug("Got SDP!", jsep);
                                        var body = { request: "set" };
                                        _this.vchat.send({ message: body, jsep: jsep });
                                      },
                                      error: function(error) {
                                        Janus.error("WebRTC error:", error);
                                      }
                                    }
                                );
                              }
                            }
                          }
                          else if (event === 'hangup') {
                            Janus.log("Call hung up by " + result["username"] + " (" + result["reason"] + ")!");
                            // Reset status
                            _this.vchat.hangup();
                            if (_this.vchatSpinner) {
                              _this.vchatSpinner.stop();
                            }
                            if (_this.$LOCAL_VCHAT_STREAM_OBJECT) _this.$LOCAL_VCHAT_STREAM_OBJECT.hide();
                            if (_this.$REMOTE_VCHAT_STREAM_OBJECT) _this.$REMOTE_VCHAT_STREAM_OBJECT.hide();

                            // Busytone 플레이
                            _this.playTone("busy");

                            // TODO: 통화가 종료될 때 해야 할 일들 ... 버큰 재등록???

                            _this.callback_vchat_hangup();
                          }
                          else if (event === 'simulcast') {
                            // Is simulcast in place?
                            var substream = result["substream"];
                            var temporal = result["temporal"];
                            if ((substream !== null && substream !== undefined) || (temporal !== null && temporal !== undefined)) {
                              if (!_this.simulcastStarted) {
                                _this.simulcastStarted = true;
                                // TODO: simulcast를 지원할지 고려해보자...
                                //addSimulcastButtons(result["videocodec"] === "vp8" || result["videocodec"] === "h264");
                              }
                              // We just received notice that there's been a switch, update the buttons
                              // TODO: simulcast를 지원할지 고려해보자...
                              //updateSimulcastButtons(substream, temporal);
                            }
                          }
                        }
                      }   // End of "if (result)"
                      else {
                        // FIXME Error?
                        let error = msg["error"];
                        Janus.error("  -- Error attaching plugin...", error);

                        if (error.indexOf("already taken") > 0) {
                          // FIXME Use status codes...
                          // TODO: Register 버튼을 활성화... 이에 상응하는 동작 필요
                        }
                        // TODO Reset status
                        _this.vchat.hangup()
                        if (_this.vchatSpinner) {
                          _this.vchatSpinner.stop();
                        }
                        // TODO: 아래 동작에 상응하는 동작 추가 필요
                        /*
                        $('#waitingvideo').remove();
                        $('#videos').hide();
                        $('#peer').removeAttr('disabled').val('');
                        $('#call').removeAttr('disabled').html('Call')
                            .removeClass("btn-danger").addClass("btn-success")
                            .unbind('click').click(doCall);
                        $('#toggleaudio').attr('disabled', true);
                        $('#togglevideo').attr('disabled', true);
                        $('#bitrate').attr('disabled', true);
                        $('#curbitrate').hide();
                        $('#curres').hide();
                        */
                        if (_this.vchatBitrateTimer) {
                          clearInterval(_this.vchatBitrateTimer);
                        }
                        _this.vchatBitrateTimer = null;
                      }
                    },  // End of "onmessage"
                    onlocalstream: function(stream) {
                      Janus.debug(" ::: Got a local stream :::", stream);
                      // 내 음성/영상스트림을 VIDEO 객체에 연결
                      Janus.attachMediaStream($('#myvideo').get(0), stream);
                      $('#myvideo').get(0).muted = "muted";
                      // FIXME : 영상통화용 코드 작성
                    },
                    onremotestream: function(stream) {
                      Janus.debug(" ::: Got a Remote stream :::", stream);

                      // 상대방의 스트림에 영상이 없으면, local/remote video 객체를 숨김
                      if (stream.getVideoTracks().length == 0) {
                        Janus.debug(" ::: No Remote Video stream :::");
                        if (_this.$LOCAL_VCHAT_STREAM_OBJECT) _this.$LOCAL_VCHAT_STREAM_OBJECT.hide();
                        if (_this.$REMOTE_VCHAT_STREAM_OBJECT) _this.$REMOTE_VCHAT_STREAM_OBJECT.hide();
                      }
                      else {
                        Janus.debug(" ::: Found Remote Video stream :::");
                        if (_this.$LOCAL_VCHAT_STREAM_OBJECT) _this.$LOCAL_VCHAT_STREAM_OBJECT.show();
                        if (_this.$REMOTE_VCHAT_STREAM_OBJECT) _this.$REMOTE_VCHAT_STREAM_OBJECT.show();
                      }

                      // 상대방의 음성/영상스트림을 VIDEO 객체에 연결
                      Janus.attachMediaStream($('#remotevideo').get(0), stream);
                      // FIXME : 영상통화용 코드 작성
                    },
                    oncleanup: function() {
                      // FIXME : Element 초기화
                      if (_this.vchat) {
                        _this.vchat.callId = null;
                      }
                    }
                  }
              );
            },
            error: function(error) {
              _this.callback_vchat_disconnected_status();

              _this.vchatRegistered = false;
              //callback_disconnected_status();

              //Janus.error(error);
              console.log("error:" + error);

              if (_this.vchatReconnectTimerId) {
                clearInterval(_this.vchatReconnectTimerId);
                _this.vchatReconnectTimerId = null;
              }
              _this.vchatReconnectTimerId = setInterval(_this.start_vchat, _this.VCHAT_RECONNECT_INTERVAL);
            },
            destroyed: function() {
              _this.callback_vchat_disconnected_status();

              _this.vchatRegistered = false;
              //callback_disconnected_status();

              Janus.log("destroyed");
              //window.location.reload();

              if (_this.vchatReconnectTimerId) {
                clearInterval(_this.vchatReconnectTimerId);
                _this.vchatReconnectTimerId = null;
              }
              _this.vchatReconnectTimerId = setInterval(_this.start_vchat, _this.VCHAT_RECONNECT_INTERVAL);
            }
          }
      );
    },
    doMute() {
      var msg = {
        request: "set",
        audio: false,
      };
      this.vchat.send({message: msg});
      Janus.debug("Do Mute");
    },
    doUnmute() {
      var msg = {
        request: "set",
        audio: true,
      };
      this.vchat.send({message: msg});
      Janus.debug("Do Unmute");
    },
    register_vchat(username) {
      if (username === "") {
        Janus.error("  -- Error register_vchat : username required...");
        return;
      }

      if(/[^a-zA-Z0-9\\-]/.test(username)) {
        Janus.error('Input is not alphanumeric');
        //$('#username').removeAttr('disabled').val("");
        //$('#register').removeAttr('disabled').click(registerUsername);
        return;
      }

      var register = { request: "register", username: username };
      this.vchat.send({ message: register });
    },
    doCall(userName, doAudio, doVideo, doData) {
      const _this = this
      // Call someone
      if(userName === "") {
        Janus.error("Insert a userName to call (e.g., pluto)");
        return;
      }
      if(/[^a-zA-Z0-9\\-]/.test(userName)) {
        Janus.error('Input is not alphanumeric');
        //$('#peer').removeAttr('disabled').val("");
        //$('#call').removeAttr('disabled').click(doCall);
        return;
      }
      // Call this user
      this.vchat.createOffer(
          {
            // By default, it's sendrecv for audio and video...
            media: { data: doData, audio: doAudio, video: doVideo },
            // ... let's negotiate data channels as well
            // If you want to test simulcasting (Chrome and Firefox only), then
            // pass a ?simulcast=true when opening this demo page: it will turn
            // the following 'simulcast' property to pass to janus.js to true
            simulcast: _this.doSimulcast,
            success: function(jsep) {
              Janus.debug("Got SDP!", jsep);
              var body = { request: "call", username: userName };
              _this.vchat.send({ message: body, jsep: jsep });
            },
            error: function(error) {
              Janus.error("createOffer error...", error);
            },
            customizeSdp: function(jsep) {
              jsep.sdp = _this.customize_sdp(jsep.sdp);
            }
          });
    },
    customize_sdp(old_sdp) {
      var new_sdp = "";
      var exclude_codecs = [];
      const line = old_sdp.split(/\r\n/);

      console.log(" ======================== SDP =========================\n" + old_sdp);

      for (var i = 0; i < line.length; i++) {
        if (new_sdp != "") { new_sdp += "\r\n"; }

        if (line[i].indexOf("m=audio") == 0) {
          var audio_prefix = line[i].match(/^m=audio \d+ \S+ /g);

          if (audio_prefix.length > 0) {
            var codecs_str = line[i].substr(audio_prefix[0].length, line[i].length-audio_prefix[0].length);
            var codecs = codecs_str.match(/(\d+)/g);
            for (var j = 0; j < codecs.length; j++) {
              if (codecs[j] != 0 && codecs[j] != 8) {
                exclude_codecs.push(codecs[j]);
              }
            }

            new_sdp += audio_prefix + "0 8"
            for (let j = 0; j < exclude_codecs.length; j++) {
              new_sdp += " " + exclude_codecs[j];
            }
          }
        }
        else {
          new_sdp += line[i];
        }
      }

      console.log(" ======================== NEW SDP =========================\n" + new_sdp);

      return new_sdp;
    },
    doHangup() {
      var hangup = { request: "hangup" };
      this.vchat.send({ message: hangup });
      this.vchat.hangup();
    },
    set_record_file(record_file) {
      const _this = this
      this.start_recording = function() {
        if (record_file !== "") {
          var msg = {
            request: "set",
            audio: true,
            video: true,
            record: true,
            filename: record_file
          };
          _this.vchat.send({message: msg});
          Janus.debug("start recording: ", record_file);
        } else {
          Janus.error("  -- You must assign record_file...");
        }
      };
    },
    stopTones() {
      console.log("-------- RINGTONE.pause()");
      this.RINGTONE.pause();
      this.RINGTONE.currentTime = 0;

      // Busytone 중지
      console.log("-------- BUSYTONE.pause()");
      this.BUSYTONE.pause();
      this.BUSYTONE.currentTime = 0;
    },
    playTone(tone) {
      const _this = this
      if (!_this.WEBRTC_INFO.env.ringtone) { return; }

      this.stopTones();

      if (tone === "ring") {
        if (!_this.WEBRTC_INFO.env.ringtone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
          console.log("-------- RINGTONE.play()");
          _this.RINGTONE.currentTime = 0;
          _this.RINGTONE.play();

          // stop microphone stream acquired by getUserMedia
          stream.getTracks().forEach(function (track) { track.stop(); });
        });
      }
      else if (tone === "busy") {
        if (!_this.WEBRTC_INFO.env.busytone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
          console.log("-------- BUSYTONE.play()");
          _this.BUSYTONE.currentTime = 0;
          _this.BUSYTONE.play();

          // stop microphone stream acquired by getUserMedia
          stream.getTracks().forEach(function (track) { track.stop(); });
        });
      }
    },
  },
  created() {
    const UrlParams = new URLSearchParams(location.search)
    this.form.senderKey = UrlParams.get('senderKey')
  },
  updated() {
    this.debounce(() => this.$refs.chatBody.scroll({top: this.$refs.chatBody.scrollHeight}), 100)
    this.$nextTick(function () {
      const audio = $(this).find('audio');
      if (audio) {
        const audioControl = maudio(audio);
        audioControl.find('.mute, .volume-bar').remove()
      }
    })
  },
  async mounted() {
    this.form.ip = (await axios.get('https://api.ipify.org')).data
    this.communicator
        .on('webchatsvc_close', () => {
          this.communicator.disconnect()
          store.commit('alert/show', {body: '연결이 종료되었습니다.', isClose: true})
          this.inputEnable = false
        })
        .on('webchatsvc_message', data => {
          this.lastReceiveMessageType = data.message_type
          if (data?.message_data?.is_custinput_enable) this.inputEnable = data?.message_data?.is_custinput_enable === 'Y'
          if (data?.message_data?.input_enable_yn) this.inputEnable = data?.message_data?.input_enable_yn === 'Y'

          if (data.message_type === 'intro') {
            this.backgroundColor = data.message_data.bgcolor
            this.displayName = data.message_data.display_company_name
            if (data.message_data.image !== '') this.botIcon = `https://cloudtalk.eicn.co.kr:442/webchat_bot_image_fetch?company_id=${encodeURIComponent(data.company_id)}&file_name=${encodeURIComponent(data.message_data.image)}&channel_type=${encodeURIComponent("eicn")}`

            if (data.message_data.schedule_info.schedule_kind === 'B') {
              this.botId = data.message_data.schedule_info.schedule_data
              if (this.botId) this.communicator.requestRootBlock(this.botId)
            } else if (data.message_data.schedule_info.schedule_kind === 'G') {
              this.communicator.sendAction(this.botId, {
                chatbot_id: '',
                parent_block_id: '',
                btn_id: '',
                btn_name: '',
                action: 'member',
                action_data: data.message_data.schedule_info.schedule_data,
              }, this.lastReceiveMessageType)
            } else if (data.message_data.schedule_info.schedule_kind === 'A') {
              this.inputEnable = data.message_data.is_chatt_enable = false
            } else if (data.message_data.schedule_info.schedule_kind === 'F') {
              this.inputEnable = data.message_data.is_chatt_enable = false
            }
          } else if (data.message_type === 'member') {
            this.homeEnable = false
            this.messages.splice(0, this.messages.length)
          } else if (data.message_type === 'ipcc_control') {
            this.inputEnable = !(data.message_data.session_keep_yn === 'N')
          } else if (data.message_type === 'audio_start') {
            this.webrtcData = data.message_data
            this.WEBRTC_INFO.server.webrtc_server_ip = data.message_data.webrtc_server_ip
            this.WEBRTC_INFO.server.webrtc_server_port = data.message_data.webrtc_server_port
            this.WEBRTC_INFO.server.turn_server_ip = data.message_data.turn_server_ip
            this.WEBRTC_INFO.server.turn_server_port = data.message_data.turn_server_port
            this.WEBRTC_INFO.server.turn_user = data.message_data.turn_user
            this.WEBRTC_INFO.server.turn_secret = data.message_data.turn_secret
          } else if (data.message_type === 'video_start') {
            this.webrtcData = data.message_data
            this.WEBRTC_INFO.server.webrtc_server_ip = data.message_data.webrtc_server_ip
            this.WEBRTC_INFO.server.webrtc_server_port = data.message_data.webrtc_server_port
            this.WEBRTC_INFO.server.turn_server_ip = data.message_data.turn_server_ip
            this.WEBRTC_INFO.server.turn_server_port = data.message_data.turn_server_port
            this.WEBRTC_INFO.server.turn_user = data.message_data.turn_user
            this.WEBRTC_INFO.server.turn_secret = data.message_data.turn_secret
          }

          if (data.message_type === "member_text") {
            const contents = data.message_data.text_data
            if (this.REPLYING_INDICATOR === data.message_data.text_data.charAt(0)) {
              [contents.indexOf(this.REPLYING_TEXT), contents.indexOf(this.REPLYING_IMAGE), contents.indexOf(this.REPLYING_FILE), contents.indexOf(this.REPLYING_IMAGE_TEMP)].forEach((indicator, i) => {
                if (indicator < 0) return
                data.message_data.replyingType = i === 0 ? 'text' : i === 1 ? 'image' : i === 2 ? 'file' : 'image_temp'

                const replyingTarget = contents.substr(1, indicator - 1)

                if (data.message_data.replyingType === 'text') {
                  data.message_data.replyingTarget = replyingTarget
                } else if (data.message_data.replyingType === 'image_temp') {
                  data.message_data.replyingTarget = this.getFileUrl(data.company_id, replyingTarget)
                } else {
                  data.message_data.replyingTarget = this.getFileUrl(data.company_id, replyingTarget)
                }

                data.message_data.text_data = contents.substr(indicator + 1)
              })
            }
          } else if (['member_image_temp', 'member_file'].includes(data.message_type)) {
            data.message_data.fileUrl = this.getFileUrl(data.company_id, data.message_data.file_name)
          }

          console.log(data, {sender: SENDER.SERVER, time: new Date(), data: data.message_data, messageType: data.message_type, company: data.company_id})
          this.messages.push({sender: SENDER.SERVER, time: new Date(), data: data.message_data, messageType: data.message_type, company: data.company_id})
        })
        .on('webchatsvc_start', data => {
          if (data.result_data === 'NO_CHANNEL') return store.commit('alert/show', {body:'채널키가 없음.' ,isClose: true})
          if (data.result !== 'OK') return store.commit('alert/show', {body: `로그인실패 : ${data.result}; ${data.result_data}` ,isClose: true})
          this.communicator.requestIntro()
        })
        .connect(this.form.url, this.form.senderKey, this.form.userKey, this.form.ip, this.form.mode,)

    window.addEventListener("beforeunload", () => this.communicator.disconnect(), false)
  },
}
</script>
