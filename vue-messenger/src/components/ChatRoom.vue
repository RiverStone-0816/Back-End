<template>
  <section class="flex flex-col flex-auto border-l border-gray-800">
    <div v-if="roomId" class="chat-header px-6 py-4 flex flex-row flex-none justify-between items-center shadow">
      <div class="flex">
        <div class="w-12 h-12 mr-4 relative flex flex-shrink-0">
          <img :src="getAvatar" alt="avatar" class="shadow-md rounded-full w-full h-full object-cover"/>
        </div>
        <div class="text-sm">
          <p class="font-bold" v-text="person && person.idName"/>
          <p v-text="getLastMessageTime"/>
        </div>
      </div>
      <div class="flex">
        <a class="block rounded-full hover:bg-gray-700 bg-gray-800 w-10 h-10 p-2 ml-4" href="#">
          <svg class="w-full h-full fill-current text-blue-500" viewBox="0 0 20 20">
            <path
                d="M2.92893219,17.0710678 C6.83417511,20.9763107 13.1658249,20.9763107 17.0710678,17.0710678 C20.9763107,13.1658249 20.9763107,6.83417511 17.0710678,2.92893219 C13.1658249,-0.976310729 6.83417511,-0.976310729 2.92893219,2.92893219 C-0.976310729,6.83417511 -0.976310729,13.1658249 2.92893219,17.0710678 Z M9,11 L9,10.5 L9,9 L11,9 L11,15 L9,15 L9,11 Z M9,5 L11,5 L11,7 L9,7 L9,5 Z"/>
          </svg>
        </a>
      </div>
    </div>
    <div v-if="roomId" ref="chatBody" class="chat-body p-4 flex-1 overflow-y-scroll" style="scroll-behavior: smooth">
      <div v-for="(group, i) in getDivedMessageGroup" :key="i" :class="group.type === 'TO' ? 'justify-end' : 'justify-start'" class="flex flex-row">
        <div v-if="group.type === MESSAGE_TYPES.FROM" class="flex flex-row justify-start">
          <div class="w-8 h-8 relative flex flex-shrink-0 mr-4">
            <img :src="getAvatar" alt="avatar" class="shadow-md rounded-full w-full h-full object-cover"/>
          </div>
          <div class="messages text-sm text-gray-700 grid grid-flow-row gap-2">
            <div v-for="(message, i) in group.messages" :key="i" class="flex items-center group">
              <p v-if="message.type === CONTENT_TYPES.TEXT" :class="i === 0 ? 'rounded-t-3xl' : i === group.messages.length -1 ? 'rounded-b-3xl' : ''"
                 class="px-6 py-3 rounded-r-3xl bg-gray-800 max-w-xs lg:max-w-md text-gray-200 whitespace-pre-line"
                 v-text="message.content"/>
              <a v-else-if="message.type === CONTENT_TYPES.IMAGE" :href="message.content" class="block w-64 h-64 relative flex flex-shrink-0 max-w-xs lg:max-w-md" target="_blank">
                <img :src="message.content" alt="hiking" class="absolute shadow-md w-full h-full rounded-r-3xl object-cover"/>
              </a>
            </div>
          </div>
        </div>
        <div v-else-if="group.type === MESSAGE_TYPES.TO" class="flex flex-row justify-end">
          <div class="messages text-sm text-white grid grid-flow-row gap-2">
            <div v-for="(message, i) in group.messages" :key="i" class="flex items-center flex-row-reverse group">
              <p v-if="message.type === CONTENT_TYPES.TEXT" :class="i === 0 ? 'rounded-t-3xl' : i === group.messages.length -1 ? 'rounded-b-3xl' : ''"
                 class="px-6 py-3 rounded-l-3xl bg-blue-700 max-w-xs lg:max-w-md  whitespace-pre-line"
                 v-text="message.content"/>
              <a v-else-if="message.type === CONTENT_TYPES.IMAGE" :href="message.content" class="block w-64 h-64 relative flex flex-shrink-0 max-w-xs lg:max-w-md" target="_blank">
                <img :src="message.content" alt="hiking" class="absolute shadow-md w-full h-full rounded-l-3xl object-cover"/>
              </a>
            </div>
          </div>
        </div>
        <p v-else class="p-4 text-center text-sm text-gray-500 w-full" v-text="group.expression"/>
      </div>
    </div>
    <div v-if="roomId" class="chat-footer flex-none">
      <div class="flex flex-row items-center p-4">
        <button class="flex flex-shrink-0 focus:outline-none mx-2 block text-blue-600 hover:text-blue-700 w-6 h-6" type="button">
          <svg class="w-full h-full fill-current" viewBox="0 0 20 20">
            <path d="M10,1.6c-4.639,0-8.4,3.761-8.4,8.4s3.761,8.4,8.4,8.4s8.4-3.761,8.4-8.4S14.639,1.6,10,1.6z M15,11h-4v4H9  v-4H5V9h4V5h2v4h4V11z"/>
          </svg>
        </button>
        <input style="display: none" type="file" @change="sendFile">
        <button class="flex flex-shrink-0 focus:outline-none mx-2 block text-blue-600 hover:text-blue-700 w-6 h-6" onclick="this.previousElementSibling.click()" type="button">
          <svg class="w-full h-full fill-current" viewBox="0 0 20 20">
            <path
                d="M11,13 L8,10 L2,16 L11,16 L18,16 L13,11 L11,13 Z M0,3.99406028 C0,2.8927712 0.898212381,2 1.99079514,2 L18.0092049,2 C19.1086907,2 20,2.89451376 20,3.99406028 L20,16.0059397 C20,17.1072288 19.1017876,18 18.0092049,18 L1.99079514,18 C0.891309342,18 0,17.1054862 0,16.0059397 L0,3.99406028 Z M15,9 C16.1045695,9 17,8.1045695 17,7 C17,5.8954305 16.1045695,5 15,5 C13.8954305,5 13,5.8954305 13,7 C13,8.1045695 13.8954305,9 15,9 Z"/>
          </svg>
        </button>
        <div class="relative flex-grow">
          <label>
            <input
                class="rounded-full py-2 pl-3 pr-10 w-full border border-gray-800 focus:border-gray-700 bg-gray-800 focus:bg-gray-900 focus:outline-none text-gray-200 focus:shadow-md transition duration-300 ease-in"
                placeholder="Aa"
                @change.stop.prevent="sendMessage"/>
          </label>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import avatar from "@/assets/avatar1.svg"
import moment from "moment"
import axios from "@/plugins/axios";
import sessionUtils from "@/utillities/sessionUtils";
import debounce from "@/utillities/mixins/debounce";
import stringUtils from "@/utillities/stringUtils";

export default {
  mixins: [debounce],
  props: {
    person: {
      id: String,
      idName: String,
      profilePhoto: String,
    },
    roomId: String
  },
  setup() {
    return {
      MESSAGE_TYPES: {TIME: 'TIME', FROM: 'FROM', TO: 'TO'},
      CONTENT_TYPES: {TEXT: 'TEXT', IMAGE: 'IMAGE', SOUND: 'SOUND', FILE: 'FILE'},
      READ_LIMIT: 10,
    }
  },
  data() {
    return {
      me: null,
      accessToken: '',
      currentRoomId: null,
      lastMessageTime: null,

      roomName: null,
      members: {},
      messages: {},
      searchingMessage: null,
      searchingMessages: [],
      startMessageTime: null,
      startMessageId: null,
      endMessageTime: null,
      endMessageId: null,
      chattingMessages: [],
    }
  },
  methods: {
    async init() {
      this.$store.state.messenger.communicator.join(this.currentRoomId)

      this.searchingMessage = null
      this.searchingMessages = []
      this.members = {}
      this.messages = {}

      const entity = (await axios.get(`/api/chatt/${this.currentRoomId}/chatting`, {limit: this.READ_LIMIT})).data.data
      entity.chattingMembers.forEach(e => this.members[e.userid] = e)
      entity.chattingMessages.sort((a, b) => a.insertTime - b.insertTime)

      this.roomName = entity.roomName
      this.startMessageTime = entity.chattingMessages.length ? entity.chattingMessages[0].insertTime : null
      this.startMessageId = entity.chattingMessages.length ? entity.chattingMessages[0].messageId : null
      this.endMessageTime = entity.chattingMessages.length ? entity.chattingMessages[entity.chattingMessages.length - 1].insertTime : null
      this.endMessageId = entity.chattingMessages.length ? entity.chattingMessages[entity.chattingMessages.length - 1].messageId : null
      this.chattingMessages = entity.chattingMessages

      if (entity.chattingMessages.length)
        this.$store.state.messenger.communicator.confirmMessage(this.currentRoomId, entity.chattingMessages[entity.chattingMessages.length - 1].messageId)

      this.debounce(() => this.$refs.chatBody.scroll({top: this.$refs.chatBody.scrollHeight}), 100)
    },
    appendMessage(data) {
      if (data.room_id !== this.currentRoomId)
        return

      this.$store.state.messenger.communicator.confirmMessage(data.room_id, data.message_id)

      const o = {
        roomId: data.room_id,
        type: data.type,
        sendReceive: data.send_receive,
        content: data.contents,
        userid: data.userid,
        insertTime: parseFloat(data.cur_timestr) * 1000,
        unreadMessageCount: 1,
      }
      this.chattingMessages.push(o)

      this.$forceUpdate()
      this.debounce(() => this.$refs.chatBody.scroll({top: this.$refs.chatBody.scrollHeight}), 100)
      this.$emit('appendMessage', this.person.id, o)
    },
    sendMessage(event) {
      const message = event.target.value
      if (!message || !this.currentRoomId)
        return

      this.$store.state.messenger.communicator.sendMessage(this.currentRoomId, message)
      event.target.value = ''
    },
    async sendFile(event) {
      const file = event.target.files[0]
      event.target.value = null

      // form.append('room_id', this.currentRoomId)
      const form = new FormData()
      form.append('file', file, file.name)

      const fileMeta = (await axios.post(`/api/file`, form, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })).data.data
      console.log(fileMeta)

      await axios.post(`/api/chatt/${this.currentRoomId}/upload-file`, {filePath: fileMeta.filePath, originalName: fileMeta.originalName})
    }
  },
  computed: {
    getAvatar() {
      return this.person && this.person.profilePhoto || avatar
    },
    getLastMessageTime() {
      if (!this.lastMessageTime)
        return ''

      const messageTime = moment(this.lastMessageTime)
      const duration = moment.duration(moment(new Date()).diff(messageTime))

      return duration.asMinutes() < 2 ? 'Just now'
          : duration.asHours() < 1 ? `Before ${parseInt(duration.asMinutes())} minutes`
              : duration.asHours() < 2 ? 'Before 1 hour'
                  : duration.asDays() < 1 ? `Before ${parseInt(duration.asHours())} hours`
                      : messageTime.format('lll')
    },
    getDivedMessageGroup() {
      const groups = []

      this.chattingMessages.forEach(e => {
        if (e.type === 'info' || ['SZ', 'SG', 'SE', 'RE'].includes(e.sendReceive))
          return

        if (!groups.length)
          groups.push({type: this.MESSAGE_TYPES.TIME, value: e.insertTime, expression: moment(e.insertTime).format('lll')})

        const messageType = e.userid === this.me.id ? this.MESSAGE_TYPES.TO : this.MESSAGE_TYPES.FROM
        let lastGroup = groups[groups.length - 1]

        if (e.insertTime - lastGroup.lastMessageTime > 5 * 1000 * 60)
          groups.push((lastGroup = {type: this.MESSAGE_TYPES.TIME, value: e.insertTime, expression: moment(e.insertTime).format('lll')}))

        const split = /^([^|]+)\|([^|]+)\|([^|]+)\|([^|]+)$/.exec(e.content);
        const message = {
          type: e.type !== 'file' ? this.CONTENT_TYPES.TEXT
              : split[1].endsWith('g') ? this.CONTENT_TYPES.IMAGE
                  : split[1].contains('wav') || split[1].contains('mp') ? this.CONTENT_TYPES.SOUND
                      : this.CONTENT_TYPES.FILE,
          content: e.type !== 'file' ? e.content : stringUtils.addQueryString(split && split[4] || '', {token: this.accessToken}),
          fileName: split && split[2] || '',
          fileSize: split && split[3] || '',
          insertTime: e.insertTime,
        }
        this.lastMessageTime = e.insertTime

        if (lastGroup.type !== messageType)
          return groups.push({type: messageType, messages: [message], lastMessageTime: e.insertTime})

        lastGroup.messages.push(message)
        lastGroup.lastMessageTime = e.insertTime
      })

      return groups
    }
  },
  async updated() {
    if (!this.roomId || this.currentRoomId === this.roomId)
      return

    this.currentRoomId = this.roomId
    await this.init()
  },
  async mounted() {
    this.me = (await sessionUtils.fetchMe())
    this.accessToken = (await sessionUtils.fetchAccessToken())
    this.$store.commit('messenger/on', {command: 'svc_msg', func: this.appendMessage})
  }
}
</script>
