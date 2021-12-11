<!-- ref: https://tailwindcomponents.com/component/checkbox -->
<!-- ref: https://tailwindcomponents.com/component/cool-text-inputs-and-login -->
<template>
  <section class="flex w-full h-screen">
    <div class="m-auto" style="width: 36rem">

      <form v-show="!connected" @submit.stop.prevent="connect">
        <div class="mb-4 relative">
          <input v-model="form.url" :class="form.url && 'filled'" autofocus class="input" type="text">
          <label class="label">URL</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="form.senderKey" :class="form.senderKey && 'filled'" autofocus class="input" type="text">
          <label class="label">SENDER KEY</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="form.ip" :class="form.ip && 'filled'" class="input" type="text">
          <label class="label">IP</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="form.userKey" :class="form.userKey && 'filled'" class="input" type="text" disabled>
          <label class="label">USER KEY</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="form.mode" :class="form.mode && 'filled'" class="input" type="text" disabled>
          <label class="label">MODE</label>
        </div>
        <button class="bg-indigo-600 hover:bg-blue-dark text-white font-bold py-3 px-6 rounded w-full">Connect</button>
      </form>
    </div>
  </section>
</template>

<script>
import axios from 'axios'
import sessionUtils from '../utillities/sessionUtils'
import store from '../store/index'

export default {
  data() {
    return {
      communicator: window.communicator,
      connected: window.communicator.connected,
      form: {
        url: 'http://122.49.74.102:8200',
        senderKey: '049d87baa539f95a3ad40bf96e1f4bf8ac1031cd',
        userKey: sessionUtils.getSessionId(),
        ip: '',
        mode: 'local',
      },
    }
  },
  methods: {
    connect() {
      this.communicator.connect(this.form.url, this.form.senderKey, this.form.userKey, this.form.ip, this.form.mode,)
      this.communicator.on('webchatsvc_start', data => {
        if (data.result !== 'OK') return store.commit('alert/show', `로그인실패 : ${data.result}; ${data.result_data}`)
        this.openChat()
      })
    },
    openChat() {
      this.connected = true
      const modal = window.open(location.href, 'chat', `width=460,height=700,top=0,left=0,scrollbars=yes`)
      modal.communicator = this.communicator
    },
  },
  async created() {
    if (this.connected) this.openChat()
    this.form.ip = (await axios.get('https://api.ipify.org')).data
  }
}
</script>

<style scoped>
.input {
  @apply border border-gray-400 appearance-none rounded w-full px-3 py-3 pt-5 pb-2 focus:border-indigo-600 focus:outline-none
}

.label {
  @apply absolute mb-0 -mt-2 pt-4 pl-3 text-gray-400 text-base mt-2 cursor-text
}

.input {
  transition: border 0.2s ease-in-out;
}

.input:focus + .label,
.input:active + .label,
.input.filled + .label {
  font-size: .75rem;
  transition: all 0.2s ease-out;
  top: -0.5rem;
  color: #667eea;
}

.label {
  transition: all 0.2s ease-out;
  top: 0.4rem;
  left: 0;
}
</style>
