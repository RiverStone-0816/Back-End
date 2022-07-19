<template>
  <body class="font-sans-kr text-gray-800 bg-blue-100" @contextmenu.prevent>
  <div class="h-screen m-auto">
    <div class="flex flex-col flex-auto h-full rounded-3xl shadow-body">
      <!--상단 홈 영역-->
      <!--bg-[#~~~~~~]이 버튼색상, 텍스트색상등과 같이 움직여야 하는 배경색, text-[#~~], border-[#~~]로 사용-->
      <div class="flex flex-row items-center h-14 bg-white w-full px-3" style="background-color: #0C4DA2">
        <!--홈버튼-->
        <button class="flex items-center justify-center rounded-lg hover:bg-slate-900/20 h-10 w-10 text-white" @click.stop="homeAction">
            <span>
              <svg xmlns="http://www.w3.org/2000/svg" width="23" height="18" viewBox="15 5 5 25">
                <path d="M15,30V21h6v9h7.5V18H33L18,4.5,3,18H7.5V30Z" fill="#fff" />
              </svg>
            </span>
        </button>
        <div class="flex-grow">
          <div class="relative w-full text-white text-sm text-center">
            {{ displayName }}
          </div>
        </div>
        <button class="flex items-center justify-center hover:bg-slate-900/20 rounded-lg h-10 w-10 text-white close-icon" @click.stop="closeAction" >
            <span>
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 23.828 23.828">
                <line x2="21" y2="21" transform="translate(1.414 1.414)" fill="none" stroke="#fff" stroke-linecap="round"
                      stroke-width="2" />
                <line y1="21" x2="21" transform="translate(1.414 1.414)" fill="none" stroke="#fff" stroke-linecap="round"
                      stroke-width="2" />
              </svg>
            </span>
        </button>
      </div>

      <!-- 음성통화 start -->
      <div class="bg-white border-b-2" v-show="audioChat">
        <div class="px-3 py-2 inline-block w-full">
          <div class="flex">
            <video id="aRemoteVideo" autoplay playsinline style="display: none"/>
            <video id="aMyVideo" autoplay playsinline muted="muted" style="display: none"/>
            <span class="flex m-auto text-base w-2/3 text-gray-500">
              {{ audioChatText }}
            </span>
            <button class="flex w-1/9 text-white py-2 px-3 rounded-lg h-10  hover:bg-gray-300/20" @click.stop="isMuteChange ? doUnmute : doMute">
              <svg xmlns="http://www.w3.org/2000/svg" width="17" height="29" viewBox="0 0 25 40">
                <path id="Icon_awesome-microphone" data-name="Icon awesome-microphone"
                      d="M12.375,24.75A6.75,6.75,0,0,0,19.125,18V6.75a6.75,6.75,0,0,0-13.5,0V18A6.75,6.75,0,0,0,12.375,24.75ZM23.625,13.5H22.5a1.125,1.125,0,0,0-1.125,1.125V18a9.01,9.01,0,0,1-9.9,8.956,9.273,9.273,0,0,1-8.1-9.357V14.625A1.125,1.125,0,0,0,2.25,13.5H1.125A1.125,1.125,0,0,0,0,14.625v2.824A12.762,12.762,0,0,0,10.688,30.224v2.4H6.75A1.125,1.125,0,0,0,5.625,33.75v1.125A1.125,1.125,0,0,0,6.75,36H18a1.125,1.125,0,0,0,1.125-1.125V33.75A1.125,1.125,0,0,0,18,32.625H14.063V30.251A12.387,12.387,0,0,0,24.75,18V14.625A1.125,1.125,0,0,0,23.625,13.5Z"
                      fill="#e1e1e1" />
              </svg>
            </button>
            <button class="flex bg-red-500 text-white ml-2 py-2 px-7 rounded-lg hover:shadow-lg h-10" @click.stop="doHangup">
              <svg class="m-auto" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 17 16">
                <g transform="translate(-1155.078 -10717.04)">
                  <g transform="translate(1155.078 10717.04)">
                    <path id="path"
                          d="M380.7,874.61l-2.44,2.44,2.44,2.44-.61.61-2.44-2.44-2.44,2.44-.61-.61,2.44-2.44-2.44-2.44.61-.61,2.44,2.44,2.44-2.44Z"
                          transform="translate(-367 -873.099)" fill="#fff" stroke="#fff" stroke-width="0.5" />
                    <path
                        d="M412.6,78.117a12.533,12.533,0,0,0,5.5,5.5l1.834-1.833a.663.663,0,0,1,.833-.167,8.716,8.716,0,0,0,3,.5.855.855,0,0,1,.833.833v2.917a.855.855,0,0,1-.833.833A14.226,14.226,0,0,1,409.6,72.533a.854.854,0,0,1,.833-.833h2.917a.854.854,0,0,1,.833.833,9.43,9.43,0,0,0,.5,3,.911.911,0,0,1-.167.833Z"
                        transform="translate(-409.6 -71.7)" fill="#fff" />
                  </g>
                </g>
              </svg>
            </button>
          </div>
        </div>
      </div>
      <!-- 음성통화 end -->

      <!-- 영상통화 start -->
      <div class="bg-white border-b-2" v-show="videoChat">
        <div class="px-3 py-2 inline-block w-full">
          <div class="flex h-60 mb-2">
            <video id="vRemoteVideo" autoplay playsinline width="180"/>
            <video id="vMyVideo" autoplay playsinline muted="muted" width="180"/>
          </div>
          <div class="flex">
            <span class="flex m-auto text-base w-2/3 text-gray-500">
              {{ videoChatText }}
            </span>
            <button class="flex w-1/9 text-white py-2 px-3 rounded-lg h-10" @click.stop="doMute">
              <svg xmlns="http://www.w3.org/2000/svg" width="17" height="29" viewBox="0 0 25 38">
                <path id="Icon_awesome-microphone" data-name="Icon awesome-microphone"
                      d="M12.375,24.75A6.75,6.75,0,0,0,19.125,18V6.75a6.75,6.75,0,0,0-13.5,0V18A6.75,6.75,0,0,0,12.375,24.75ZM23.625,13.5H22.5a1.125,1.125,0,0,0-1.125,1.125V18a9.01,9.01,0,0,1-9.9,8.956,9.273,9.273,0,0,1-8.1-9.357V14.625A1.125,1.125,0,0,0,2.25,13.5H1.125A1.125,1.125,0,0,0,0,14.625v2.824A12.762,12.762,0,0,0,10.688,30.224v2.4H6.75A1.125,1.125,0,0,0,5.625,33.75v1.125A1.125,1.125,0,0,0,6.75,36H18a1.125,1.125,0,0,0,1.125-1.125V33.75A1.125,1.125,0,0,0,18,32.625H14.063V30.251A12.387,12.387,0,0,0,24.75,18V14.625A1.125,1.125,0,0,0,23.625,13.5Z"
                      fill="#e1e1e1" />
              </svg>
            </button>
            <button class="flex bg-red-500 text-white ml-2 py-2 px-7 rounded-lg hover:shadow-lg h-10" @click.stop="doHangup">
              <svg class="m-auto" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 17 16">
                <g transform="translate(-1155.078 -10717.04)">
                  <g transform="translate(1155.078 10717.04)">
                    <path id="path"
                          d="M380.7,874.61l-2.44,2.44,2.44,2.44-.61.61-2.44-2.44-2.44,2.44-.61-.61,2.44-2.44-2.44-2.44.61-.61,2.44,2.44,2.44-2.44Z"
                          transform="translate(-367 -873.099)" fill="#fff" stroke="#fff" stroke-width="0.5" />
                    <path
                        d="M412.6,78.117a12.533,12.533,0,0,0,5.5,5.5l1.834-1.833a.663.663,0,0,1,.833-.167,8.716,8.716,0,0,0,3,.5.855.855,0,0,1,.833.833v2.917a.855.855,0,0,1-.833.833A14.226,14.226,0,0,1,409.6,72.533a.854.854,0,0,1,.833-.833h2.917a.854.854,0,0,1,.833.833,9.43,9.43,0,0,0,.5,3,.911.911,0,0,1-.167.833Z"
                        transform="translate(-409.6 -71.7)" fill="#fff" />
                  </g>
                </g>
              </svg>
            </button>
          </div>
        </div>
      </div>
      <!-- 영상통화 end -->

      <!--중단 말풍선 영역, 여기에 배경 컬러를 넣을 수 있음, 스크롤 스타일 적용-->
      <div class="h-full overflow-x-auto bg-gray-100" id="slim-scroll" ref="chatBody" :style="'scroll-behavior: smooth;background-color: '+backgroundColor">
        <template v-for="(message, iMessage) in messages" :key="iMessage">

          <!-- intro start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'intro'">
            <div class="pl-3 pt-3">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <!--회사로고 이미지 + 텍스트 출력-->
                  <div class="rounded-lg shadow">
                    <div class="bg-white py-2 px-3 rounded-lg">
                      <div v-if="message.data?.profile" class="pt-1">
                        <img :src="getFileUrl(message.company, message.data.profile)" class="w-full rounded-lg">
                      </div>
                      <div class="pt-4">
                        <span>
                          <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.msg }}</p>
                        </span>
                      </div>
                    </div>
                  </div>
                  <!--텍스트 출력 + 다른 채널 링크-->
                  <div v-if="message.data.channel_list && message.data.channel_list.length" class="rounded-lg shadow">
                    <div class="relative text-main bg-white py-2 px-3 shadow rounded-lg">
                      <div>
                        <span>
                          다른 채널을 통한 상담을 원하시면 원하시는 서비스의 아이콘을 눌러주세요.
                        </span>
                      </div>
                      <div class="flex flex-row-reverse pt-3">
                        <div v-for="(channel, i) in message.data.channel_list" :key="i" class="pl-2">
                          <a :href="channel.channel_url" target="_blank">
                            <img alt="kakao" :src="channel.channel_type === 'eicn' ? getEicnIcon() : channel.channel_type === 'kakao' ? getKakaoIcon()
                                  : channel.channel_type === 'line' ? getLineIcon() : getNaverIcon()">
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="text-xxs text-gray-600/100">
                    {{ getTimeFormat(message.time) }}
                  </div>
                </div>
              </div>
            </div>
            <div v-if="['A', 'F'].includes(message.data.schedule_info.schedule_kind)" class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--텍스트 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                  <span>
                    <p style="white-space: pre">{{ message.data.schedule_info.schedule_ment }}</p>
                  </span>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- intro end -->

          <!-- fallback start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'fallback'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--텍스트 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                    <span>
                      <p>{{ message.data.fallback_ment }}</p>
                    </span>
                  </div>
                  <div class="space-y-2" v-show="getLastOrder(iMessage)">
                    <button class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2" @click.stop="actFallback(message)">
                      {{ getFallbackButtonName(message.data.fallback_action) }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- fallback end -->

          <!-- api_result start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'api_result'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--이미지 출력 + 텍스트-->

                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div class="bg-white shadow rounded-lg">
                    <div class="p-3 pt-2 text-main">
                      <span>
                        <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.result_content }}</p>
                      </span>
                    </div>
                  </div>
                  <div v-for="(e, i) in getButtonGroups(message)" :key="i" class="space-y-2" v-show="getLastOrder(iMessage)">
                    <span v-if="e instanceof Array">
                      <button v-for="(e2, j) in e" :key="j"  class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2" @click.stop="actButton(message, e2)">
                        {{ e2.btn_name }}
                      </button>
                    </span>
                  </div>
                  <div class="text-xxs text-gray-600/100">
                    {{ getTimeFormat(message.time) }}
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- api_result end -->

          <!-- member start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'member'">
            <div class="pl-3 pt-3">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--텍스트 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                  <span>
                    <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.ment }}</p>
                  </span>
                  </div>
                  <div class="text-xxs text-gray-600/100">
                    {{ getTimeFormat(message.time) }}
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- member end -->

          <!-- member_text start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'member_text'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--텍스트 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                    <div v-if="message.data?.replyingTarget && message.data?.replyingType === 'text'" class="replying">
                      {{ message.data.replyingTarget }}
                    </div>
                    <div v-if="message.data?.replyingTarget && message.data?.replyingType === 'image'" class="replying">
                      <img :src="message.data.replyingTarget">
                    </div>
                    <span>
                      <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data.text_data }}</p>
                    </span>
                  </div>
                  <div class="text-xxs text-gray-600/100">
                    {{ getTimeFormat(message.time) }}
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- member_text end -->

          <!-- block,member_block_temp start -->
          <template v-if="message.sender === 'SERVER' && ['block', 'member_block_temp'].includes(message.messageType)">
            <div v-for="(e, i) in message.data?.display" :key="i" class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">

                <!-- text block start -->
                <div v-if="e.type === 'text'" class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                  <span>
                    <p style="white-space: pre-wrap; line-break: anywhere;">{{ e.element[0]?.content }}</p>
                  </span>
                  </div>
                </div>
                <!-- text block end -->

                <!-- image block start -->
                <div v-else-if="e.type === 'image'" class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div>
                    <img :src="getFileUrl(message.company, e.element[0]?.image)" alt="chat_image" class="w-full rounded-lg">
                  </div>
                </div>
                <!-- image block end -->

                <!-- card block start -->
                <div v-else-if="e.type === 'card'" class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div class="bg-white shadow rounded-lg">
                    <div>
                      <img :src="getFileUrl(message.company, e.element[0]?.image)" class="w-full rounded-t-lg">
                    </div>
                    <div class="p-3 pb-0 text-sm font-bold">
                    <span>
                      <p>{{ e.element[0]?.title }}</p>
                    </span>
                    </div>
                    <div class="p-3 pt-2 text-main">
                    <span>
                      <p style="white-space: pre-wrap; line-break: anywhere;">{{ e.element[0]?.content }}</p>
                    </span>
                    </div>
                  </div>
                </div>
                <!-- card block end -->

                <!-- list block start -->
                <div v-else-if="e.type === 'list'" class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div class="flex flex-row">
                    <div class="relative bg-white shadow rounded-lg max-w-xs w-full">
                      <div class="p-2 pl-3 border-b">
                      <span>
                        <p v-if="e.element[0]?.url"><a target="_blank" :href="e.element[0]?.url">{{ e.element[0]?.title }}</a></p>
                        <p v-else>{{ e.element[0]?.title }}</p>
                      </span>
                      </div>
                      <template v-if="e.element[1]?.title || e.element[1]?.image || e.element[1]?.content">
                        <div v-for="(e2, j) in getListElements(e)" :key="j" class="grid grid-cols-6 p-3">
                          <div class="col-span-1">
                            <img v-if="e2.image" :src="getFileUrl(message.company, e2.image)" class="rounded-lg w-10 h-10">
                          </div>
                          <div class="col-span-5">
                            <div class="pl-2 pt-0 pb-1 text-sm font-bold">
                          <span>
                            <p v-if="e2.url"><a target="_blank" :href="e2.url">{{ e2.title }}</a></p>
                            <p v-else>{{ e2.title }}</p>
                          </span>
                            </div>
                            <div class="pl-2 pt-0 text-gray-700">
                          <span>
                            <p style="white-space: pre-wrap; line-break: anywhere;">{{ e2.content }}</p>
                          </span>
                            </div>
                          </div>
                        </div>
                      </template>
                    </div>
                  </div>
                </div>
                <!-- list block end -->

                <!-- input block start -->
                <div v-else-if="e.type === 'input'" class="flex flex-col space-y-2 text-main m-2 mt-0 mr-4 w-button -api-parent" ref="apiparent">
                  <div class="flex flex-row">
                    <div class="relative text-sm bg-white shadow rounded-lg w-full">
                      <div class="p-2 pl-3 text-main border-b">
                      <span>
                        <p>{{ e.element[0]?.title }}</p>
                      </span>
                      </div>
                      <div class="p-2 pl-3 text-main">
                        <div class="space-y-2">
                          <div v-for="(e2, j) in getInputElements(e)" :key="j"  class="grid grid-cols-12 pt-2">
                            <div class="col-span-4">
                              <div class="py-2 relative font-bold flex justify-left">
                              <span class="inline-block align-middle">
                                <p>{{ e2.input_display_name }}</p>
                              </span>
                              </div>
                            </div>
                            <div class="col-span-8">
                              <div class="relative w-full">
                                <input :type="(e2.input_type === 'calendar' && 'date') || (e2.input_type === 'number' && 'number') || (e2.input_type === 'secret' && 'password') || (e2.input_type === 'text' && 'text') || (e2.input_type === 'time' && 'time')"
                                       :name="e2.input_param_name" :data-value="e2.input_need_yn"
                                       class="py-1.5 px-2.5 bg-gray-50 border border-gray-300 text-gray-900 text-main focus:outline-none rounded-lg w-full touch-y">
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- input block end -->

              </div>
            </div>
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <span class="rounded-full" style="width: 32px;height: 32px;"></span>
                <!-- button start -->
                <div v-for="(e, i) in getButtonGroups(message)" :key="i" class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div class="space-y-2" v-show="getLastOrder(iMessage)">
                    <span v-if="e instanceof Array">
                      <button v-for="(e2, j) in e" :key="j" class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2" @click.stop="actButton(message, e2)">
                        {{ e2.btn_name }}
                      </button>
                    </span>
                    <span v-else>
                      <button class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2" @click.stop="actApi(message, e, $event)">
                        {{ e.btn_name }}
                      </button>
                    </span>
                  </div>
                </div>
                <!-- button end -->

              </div>
            </div>
          </template>
          <!-- block,member_block_temp end -->

          <!-- member_image_temp block start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'member_image_temp'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--이미지 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div>
                    <img :src="message.data.fileUrl" alt="chat_image" class="w-full rounded-lg">
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- member_image_temp block end -->

          <!-- member_file block start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'member_file'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--이미지 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4">
                  <div v-if="isImage(message.data.file_name)">
                    <img :src="message.data.fileUrl" alt="chat_image" class="w-full rounded-lg">
                  </div>
                  <div v-else-if="isAudio(message.data.file_name)" class="maudio">
                    <audio :src="message.data" initaudio="false"></audio>
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
                  <div v-else-if="isVideo(message.data.file_name)">
                    <video controls :src="message.data"></video>
                  </div>
                  <div v-else>
                    <a class="w-full rounded-lg" :href="message.data.fileUrl">{{ message.data.file_name }}
                      <hr/>
                      <b>파일 저장하기</b></a>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- member_file block end -->

          <!-- text block start -->
          <template v-if="message.sender !== 'SERVER' && message.messageType === 'text'">
            <div class="pr-1 pt-1">
              <div class="flex items-end justify-end">
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 order-1 items-end">
                  <div class="px-3 py-2 rounded-lg inline-block bg-indigo-900 text-white shadow">
                  <span>
                    <p style="white-space: pre-wrap; line-break: anywhere;">{{ message.data }}</p>
                  </span>
                  </div>
                  <div class="text-xxs text-gray-600/100">
                    {{ getTimeFormat(message.time) }}
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- text block end -->

          <!-- audio start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'audio_start'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--텍스트 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                    <span>
                      <p>상담원이 음성통화를 요청합니다.</p>
                    </span>
                  </div>
                  <div class="space-y-2" v-show="getLastOrder(iMessage) && vChatButton">
                    <button class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2"  @click.stop="audioStart(true)">수락</button>
                    <button class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2"  @click.stop="audioStart(false)">거절</button>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- audio end -->

          <!-- video start -->
          <template v-if="message.sender === 'SERVER' && message.messageType === 'video_start'">
            <div class="pl-3 pt-1">
              <div class="flex items-start">
                <!--채널봇 아이콘-->
                <img :src="botIcon === '' ? getBotIcon : botIcon" class="rounded-full" style="width: 32px;height: 32px;">
                <!--텍스트 출력-->
                <div class="flex flex-col space-y-2 max-w-xxs text-main m-2 mt-0 mr-4 items-start">
                  <div class="px-3 py-2 rounded-lg inline-block bg-white text-gray-800 shadow">
                    <span>
                      <p>상담원이 영상통화를 요청합니다.</p>
                    </span>
                  </div>
                  <div class="space-y-2" v-show="getLastOrder(iMessage) && vChatButton">
                    <button class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2"  @click.stop="videoStart(true)">수락</button>
                    <button class="py-1 px-3 text-white rounded-lg text-xs hover:shadow-lg m-1" style="background-color: #0C4DA2"  @click.stop="videoStart(false)">거절</button>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- video end -->

        </template>
      </div>

      <!--하단 채팅 입력-->
      <div v-if="inputEnable" class="flex flex-row items-center h-16 bg-white w-full px-3 border-t">
        <div class="flex-grow">
          <div class="relative w-full">
            <input type="text" placeholder="질문을 입력해 주세요."
                   class="flex w-full rounded-lg focus:outline-none h-10 py-1 px-2 text-sm touch-y" v-model="input" @keyup.stop.prevent="$event.key==='Enter'&&sendText()">
          </div>
        </div>
        <div class="ml-2">
          <button
              class="flex items-center justify-center hover:bg-gray-300/20 rounded-lg h-10 w-10 text-white flex-shrink-0" @click.stop="sendText">
              <span class="ml-1">
                <svg xmlns="http://www.w3.org/2000/svg" width="23" height="20.444" viewBox="0 0 23 20.444">
                  <path id="Icon_material-send" data-name="Icon material-send"
                        d="M3.011,24.944,26,14.722,3.011,4.5,3,12.451l16.429,2.272L3,16.994Z" transform="translate(-3 -4.5)"
                        fill="#929292" />
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
import $ from 'jquery'
import botIcon from '../assets/bot-icon.png'
import eicnIcon from '../assets/bot-icon.png'
import kakaoIcon from '../assets/kakao-icon.png'
import naverIcon from '../assets/naver-icon.png'
import lineIcon from '../assets/line-icon.png'
import debounce from '../utillities/mixins/debounce'
import SimpleTone from "../assets/sounds/SimpleTone.mp3"
import BusySignal from "../assets/sounds/BusySignal.mp3"
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
      isIframe: false,
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
      RINGTONE: new Audio(SimpleTone),
      BUSYTONE: new Audio(BusySignal),
      opened: true,
      janusVChat: null,
      vchat: null,
      vchatOpaqueId: "vchat-"+Janus.randomString(12),
      vchatBitrateTimer: null,
      vchatSpinner: null,
      myUsername: null,
      remoteUsername: null,
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
      LOCAL_VCHAT_STREAM_OBJECT: null,
      REMOTE_VCHAT_STREAM_OBJECT: null,
      start_recording: null,

      isMuteChange: false,

      audioChat: false,
      audioChatText: '음성통화 연결중',

      videoChat: false,
      videoChatText: '영상통화 연결중',

      chkInit: true,

      vChatButton: false,


    }
  },
  methods: {
    getBotIcon: () => botIcon,
    getKakaoIcon: () => kakaoIcon,
    getNaverIcon: () => naverIcon,
    getLineIcon: () => lineIcon,
    getEicnIcon: () => eicnIcon,
    showAlert: content => store.commit('alert/show', content),
    getTimeFormat: value => moment(value).format('HH:mm'),
    getButtonGroups(message) {
      return message.data?.button?.reduce((list, e) => {
        if (['api','ipcc'].includes(e.action)) list.push(e)
        else if (!list.length || !(list[list.length - 1] instanceof Array)) list.push([e])
        else list[list.length - 1].push(e)
        return list
      }, [])
    },
    getLastOrder(chkNum) {
      return (this.messages.length - 1) === chkNum
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
    closeAction() {
      if(!this.isIframe) return
      window.parent.postMessage('closeIframe', '*')
      window.parent.focus()
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
      const elements = this.$refs.apiparent[this.$refs.apiparent.length-1].querySelectorAll('[name]')
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
        this.audioChat = true
        this.audioChatText = '음성통화 연결'
        this.communicator.sendWebrtcReady('audio_start_ready',{ready_result: 0, ready_result_msg: '준비완료', record_file_key: this.webrtcData.record_file_key})
      } else{
        this.communicator.sendWebrtcReady('audio_start_ready',{ready_result: 1, ready_result_msg: '거절'})
      }
      this.vChatButton = false
    },
    videoStart(chk) {
      if (chk) {
        this.set_record_file(this.webrtcData.record_file)
        this.start_vchat()
        this.videoChat = true
        this.videoChatText = '영상통화 연결'
        this.communicator.sendWebrtcReady('video_start_ready',{ready_result: 0, ready_result_msg: '준비완료', record_file_key: this.webrtcData.record_file_key})
      } else{
        this.communicator.sendWebrtcReady('video_start_ready',{ready_result: 1, ready_result_msg: '거절'})
      }
      this.vChatButton = false
    },
    //webrtc
    set_callback_vchat_registered_status(callback) {
      this.callback_vchat_registered_status = callback;
    },
    set_callback_vchat_unregistered_status(callback) {
      this.callback_vchat_unregistered_status = callback;
    },
    set_callback_vchat_disconnected_status(callback) {
      this.callback_vchat_disconnected_status = callback;
    },
    set_callback_vchat_outgoing_call(callback) {
      this.callback_vchat_outgoing_call = callback;
    },
    set_callback_vchat_incoming_call(callback) {
      this.callback_vchat_incoming_call = callback;
    },
    set_callback_vchat_accept(callback) {
      this.callback_vchat_accept = callback;
    },
    set_callback_vchat_hangup(callback) {
      this.callback_vchat_hangup = callback
    },
    start_vchat() {
      const _this = this
      this.set_callback_vchat_incoming_call(()=>{
        _this.audioChatText = '전화옴'
        _this.videoChatText = '전화옴'
      })
      this.set_callback_vchat_registered_status(()=>{
        _this.audioChatText = '음성통화 대기.'
        _this.videoChatText = '영상통화 대기.'
      })
      this.set_callback_vchat_unregistered_status(()=>{
        _this.audioChatText = '준비중..'
        _this.videoChatText = '준비중..'
      })
      this.set_callback_vchat_outgoing_call(()=>{
        _this.audioChatText = '전화거는중'
        _this.videoChatText = '전화거는중'
      })
      this.set_callback_vchat_disconnected_status(()=>{
        _this.audioChatText = '전화거는중'
        _this.videoChatText = '전화거는중'
      })
      this.set_callback_vchat_accept(()=>{
        _this.audioChatText = '통화중'
        _this.videoChatText = '통화중'
      })
      this.set_callback_vchat_hangup(()=>{
        _this.audioChatText = '통화종료'
        _this.videoChatText = '통화종료'
        _this.audioChat = false
        _this.videoChat = false
      })
      if (_this.vchatReconnectTimerId) {
        clearInterval(_this.vchatReconnectTimerId);
        _this.vchatReconnectTimerId = null;
      }
      if (_this.janusVChat) {
        delete _this.janusVChat;
        _this.janusVChat = null;
      }
      if (this.chkInit) {
        Janus.init({debug: "all", callback: this.create_vchat_session});
        _this.chkInit = false
      }
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
                            //_this.playTone("ring");

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
                            //_this.playTone("ring");

                            _this.callback_vchat_incoming_call();
                            setTimeout(() => {_this.accept_vchat()}, 500);
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

                            // Busytone 플레이
                            //_this.playTone("busy");

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
                        if (_this.vchatBitrateTimer) {
                          clearInterval(_this.vchatBitrateTimer);
                        }
                        _this.vchatBitrateTimer = null;
                      }
                    },  // End of "onmessage"
                    onlocalstream: function(stream) {
                      Janus.debug(" ::: Got a local stream :::", stream);
                      // 내 음성/영상스트림을 VIDEO 객체에 연결
                      Janus.attachMediaStream(_this.LOCAL_VCHAT_STREAM_OBJECT.get(0), stream);
                      _this.LOCAL_VCHAT_STREAM_OBJECT.get(0).muted = "muted";
                      // FIXME : 영상통화용 코드 작성
                    },
                    onremotestream: function(stream) {
                      Janus.debug(" ::: Got a Remote stream :::", stream);

                      // 상대방의 스트림에 영상이 없으면, local/remote video 객체를 숨김
                      if (stream.getVideoTracks().length == 0) {
                        Janus.debug(" ::: No Remote Video stream :::");
                      }
                      else {
                        Janus.debug(" ::: Found Remote Video stream :::");
                      }

                      // 상대방의 음성/영상스트림을 VIDEO 객체에 연결
                      Janus.attachMediaStream(_this.REMOTE_VCHAT_STREAM_OBJECT.get(0), stream);
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
      const _this = this
      if( this.isMuteChange) {
        let msg = {
          request: "set",
          audio: false,
        };
        _this.vchat.send({message: msg});
        Janus.debug("Do Mute");
        _this.isMuteChange = false
      } else {
        let msg = {
          request: "set",
          audio: true,
        };
        _this.vchat.send({message: msg});
        Janus.debug("Do Unmute");
        _this.isMuteChange = true
      }
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
          //_this.RINGTONE.play();

          // stop microphone stream acquired by getUserMedia
          stream.getTracks().forEach(function (track) { track.stop(); });
        });
      }
      else if (tone === "busy") {
        if (!_this.WEBRTC_INFO.env.busytone) { return; }

        navigator.mediaDevices.getUserMedia({ audio: true }).then(function (stream) {
          console.log("-------- BUSYTONE.play()");
          _this.BUSYTONE.currentTime = 0;
          //_this.BUSYTONE.play();

          // stop microphone stream acquired by getUserMedia
          stream.getTracks().forEach(function (track) { track.stop(); });
        });
      }
    },
  },
  created() {
    const UrlParams = new URLSearchParams(location.search)
    this.form.senderKey = UrlParams.get('senderKey')
    this.isIframe = UrlParams.get('isIframe') === 'true';
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
            this.botIcon = data.message_data.image === '' ? this.getBotIcon() : `https://cloudtalk.eicn.co.kr:442/webchat_bot_image_fetch?company_id=${encodeURIComponent(data.company_id)}&file_name=${encodeURIComponent(data.message_data.image)}&channel_type=${encodeURIComponent("eicn")}`

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
            this.myUsername = data.message_data.remote_username
            this.remoteUsername = data.message_data.my_username
            this.LOCAL_VCHAT_STREAM_OBJECT = $('#aMyVideo')
            this.REMOTE_VCHAT_STREAM_OBJECT = $('#aRemoteVideo')
            this.vChatButton = true
          } else if (data.message_type === 'video_start') {
            this.webrtcData = data.message_data
            this.WEBRTC_INFO.server.webrtc_server_ip = data.message_data.webrtc_server_ip
            this.WEBRTC_INFO.server.webrtc_server_port = data.message_data.webrtc_server_port
            this.WEBRTC_INFO.server.turn_server_ip = data.message_data.turn_server_ip
            this.WEBRTC_INFO.server.turn_server_port = data.message_data.turn_server_port
            this.WEBRTC_INFO.server.turn_user = data.message_data.turn_user
            this.WEBRTC_INFO.server.turn_secret = data.message_data.turn_secret
            this.myUsername = data.message_data.remote_username
            this.remoteUsername = data.message_data.my_username
            this.LOCAL_VCHAT_STREAM_OBJECT = $('#vMyVideo')
            this.REMOTE_VCHAT_STREAM_OBJECT = $('#vRemoteVideo')
            this.vChatButton = true
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

<!--tailwind 제공 css가 아닌 별도로 삽입-->
<style scoped>
.rounded-3xl {
  border-radius: 1.5rem;
}
.py-14 {
  padding-top: 3.5rem;
  padding-bottom: 3.5rem;
}
.max-w-sm {
  max-width: 24rem;
}
.max-w-xxs {
  max-width: 17rem;
}
.max-w-100vw{
  max-width: 100vw;
}

.text-xxs {
  font-size: 0.7rem;
}

.text-main {
  font-size: 0.8rem;
}

.w-button {
  width: 17rem;
}

.shadow-body {
  box-shadow: 0 15px 50px 5px rgb(0 0 0 / 0.25);
}

#slim-scroll::-webkit-scrollbar {
  width: 4px;
  cursor: pointer;
  /*background-color: rgba(229, 231, 235, var(--bg-opacity));*/
}

#slim-scroll::-webkit-scrollbar-track {
  background-color: rgba(229, 231, 235, var(--bg-opacity));
  cursor: pointer;
  /*background: red;*/
}

#slim-scroll::-webkit-scrollbar-thumb {
  cursor: pointer;
  background-color: #80808038;
  /*outline: 1px solid slategrey;*/
}
.h-14 {
  height: 3.5rem;
}
.rounded-t-3xl {
  border-top-left-radius: 1.5rem;
  border-top-right-radius: 1.5rem;
}
.bg-gray-100 {
  --tw-bg-opacity: 1;
  background-color: rgb(243 244 246 / var(--tw-bg-opacity));
}
.pt-3 {
  padding-top: 0.75rem;
}
.items-start {
  align-items: flex-start;
}
.rounded-full {
  border-radius: 9999px;
}
.m-1 {
  margin: 0.1rem;
}
.m-2 {
  margin: 0.5rem;
}
.mt-0 {
  margin-top: 0px;
}
.mr-4 {
  margin-right: 1rem;
}
.text-gray-600\/100 {
  color: rgb(75 85 99);
}
.max-w-xs {
  max-width: 20rem;
}
.border-b {
  border-bottom-width: 1px;
}
.py-1\.5 {
  padding-top: 0.375rem;
  padding-bottom: 0.375rem;
}
.px-2\.5 {
  padding-left: 0.625rem;
  padding-right: 0.625rem;
}
.col-span-1 {
  grid-column: span 1 / span 1;
}
.col-span-4 {
  grid-column: span 4 / span 4;
}
.col-span-8 {
  grid-column: span 8 / span 8;
}
.bg-gray-50 {
  --tw-bg-opacity: 1;
  background-color: rgb(249 250 251 / var(--tw-bg-opacity));
}
.border-gray-300 {
  --tw-border-opacity: 1;
  border-color: rgb(209 213 219 / var(--tw-border-opacity));
}
.order-1 {
  order: 1;
}
.bg-indigo-900 {
  --tw-bg-opacity: 1;
  background-color: rgb(49 46 129 / var(--tw-bg-opacity));
}
.rounded-b-3xl {
  border-bottom-right-radius: 1.5rem;
  border-bottom-left-radius: 1.5rem;
}
.justify-end {
  justify-content:end
}
.justify-left {
  justify-content:left
}
.space-y-2 > :not([hidden]) ~ :not([hidden]) {
  --tw-space-y-reverse: 0;
  margin-top: calc(0.5rem * calc(1 - var(--tw-space-y-reverse)));
  margin-bottom: calc(0.5rem * var(--tw-space-y-reverse));
}
.hover\:shadow-lg:hover{
  --tw-shadow:0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
  --tw-shadow-colored:0 10px 15px -3px var(--tw-shadow-color), 0 4px 6px -4px var(--tw-shadow-color);
  box-shadow:var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow)
}
.focus\:outline-none:focus{
  outline:2px solid transparent;outline-offset:2px
}
.border-b-2 {
  border-bottom-width: 2px;
}
.w-2\/3 {
  width: 66.666667%;
}
.bg-red-500 {
  --tw-bg-opacity: 1;
  background-color: rgb(239 68 68 / var(--tw-bg-opacity));
}
.px-7 {
  padding-left: 1.75rem;
  padding-right: 1.75rem;
}
input[type="text"],select:focus,textarea .touch-y{
  touch-action: pan-y;
  font-size:  max(0.8rem, 16px) !important;
}
.replying {
  color: #998D1F;
  border-bottom: 1px solid #998D1F;
  padding: 0 0 4px 0;
  margin: 0 0 4px 0;
  img {
    width: auto;
    height: 50px;
  }
}

</style>
