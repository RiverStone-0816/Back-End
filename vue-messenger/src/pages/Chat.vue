<template>
  <section class="w-full">
    <h1 class="text-3xl text-center hidden md:block md:py-5">Chat App</h1>
    <div class="m-auto w-full md:w-2/3 flex flex-col justify-between p-3 border chatScreen">
      <div class="messages flex flex-col chat">
        <div v-if="channelMessages.length === 0" class="flex flex-col m-auto text-center justify-center mb-2 header">
          <p class="text-muted">This place is so quiet, start chatting...</p>
        </div>
        <div v-for="msg in channelMessages" :key="msg.id" :class="{ self: user.uid === msg.userId }" class="flex gap-2 w-75 mb-4 bubble">
          <img :src="msg.avatar || unknownAvatar" alt="avatar" height="50" width="50"/>
          <div class="text">
            <div class="username">{{ msg.sender }} {{ convertReadableTimeformat(msg.timeStamp) }}</div>
            <div class="chatfield p-2">
              <span v-html="convertToLink(msg.message)"></span>
            </div>
          </div>
        </div>
      </div>
      <div class="chatbox-wrapper">
        <input class="chatbox px-3 p-2" placeholder="Send a message..." type="text" @keyup.prevent.enter="userSendMsg"/>
      </div>
    </div>
  </section>
</template>

<script>
import moment from 'moment'
import {createMessageCollection, registerMessageHandler, sendMessage} from "@/utillities/firebase"
import avatar1 from '@/assets/avatar1.svg'
import router from "@/router"
import debounce from "@/utillities/mixins/debounce";

function compareTimeStamp(a, b) {
  const secDiff = a.seconds - b.seconds
  if (secDiff !== 0)
    return secDiff
  return a.nanoseconds - b.nanoseconds
}

export default {
  mixins: [
    debounce
  ],
  data() {
    return {
      channelMessages: [],
      unknownAvatar: avatar1,
      user: this.$store.state.user,
      messageBuffer: []
    }
  },
  methods: {
    convertReadableTimeformat(timestamp) {
      return moment(timestamp.seconds).format('MM-DD hh:mm')
    },
    scrollBottom() {
      const messages = document.getElementsByClassName('messages')[0];
      if (messages) {
        messages.scrollTop = messages.scrollHeight;
      }
    },
    userSendMsg(e) {
      if (e.target.value === '') return;
      sendMessage(e.target.value, this.user.credential?.uid, this.user.credential?.displayName || this.user.credential?.email, this.user.credential?.photoURL)
      e.target.value = '';
    },
    convertToLink(text) {
      return text.replace(
          /(?:(?:https?|ftp|file):\/\/|www\.|ftp\.)(?:\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\)|[-A-Z0-9+&@#\\/%=~_|$?!:,.])*(?:\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\)|[A-Z0-9+&@#\\/%=~_|$])/gim,
          (text) => `<a class="text-primary" rel="noreferrer" target="_blank" href="${text}">${text}</a>`,
      );
    },
    initChat() {
      createMessageCollection()
      registerMessageHandler((type, data) => {
        if (type === 'added') {
          this.update(data)
        }
      })
    },
    update(data) {
      const messageBuffer = this.messageBuffer
      const channelMessages = this.channelMessages

      messageBuffer.push(data)
      if (!data.timeStamp) {
        data.timeStamp = {}
        data.timeStamp.seconds = new Date().getTime()
        data.timeStamp.nanoseconds = 0
      }

      this.debounce(() => {
        console.log('debounce')
        let sorting = false
        messageBuffer.forEach(function (data) {
          channelMessages.push(data)
          if (channelMessages.length === 1 || compareTimeStamp(channelMessages[channelMessages.length - 2].timeStamp, data.timeStamp) <= 0)
            return

          sorting = true
        })
        this.messageBuffer = []
        sorting && channelMessages.sort(compareTimeStamp)
      })
    }
  },
  watch: {
    user() {
      if (this.user)
        this.initChat()
      else
        router.push('/')
    },
  },
  mounted() {
    this.initChat()
  },
  updated() {
    this.scrollBottom()
  }
}
</script>

<style scoped>
.chatbox {
  outline: none;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  background: wheat;
  transition: 0.2s;
  color: black;
  width: 100%;
}
.header {
  width: 100%;
  border-bottom: 2px gray solid;
}
h2 {
  color: purple
}
.messages {
  position: relative;
  height: 70vh;
  width: 100%;
  padding-right: 2em;
  overflow: auto;
}
.messages::-webkit-scrollbar {
  background: #d8d6d6;
  width: 8px;
  border-radius: 5px;
}
.messages::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: purple;
}
.chatfield {
  background: purple;
  color: wheat;
  border-radius: 0 10px 10px 10px;
}
.chatbox:focus,
.self .chatfield {
  background: purple;
  color: white;
}
.self .chatfield {
  border-top-right-radius: 0;
  border-top-left-radius: 10px;
}
.chatfield {
  font-size: 1rem;
  width: fit-content;
}
.bubble {
  flex-flow: row;
}
.bubble.self {
  flex-flow: row-reverse;
}
.self .chatfield {
  float: right;
}
.self {
  text-align: right;
  float: right;
  flex-flow: row-reverse;
}
img {
  -webkit-user-drag: none;
}
</style>
