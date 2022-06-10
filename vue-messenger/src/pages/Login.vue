<!-- ref: https://tailwindcomponents.com/component/checkbox -->
<!-- ref: https://tailwindcomponents.com/component/cool-text-inputs-and-login -->
<template>
  <section class="flex w-full h-screen">
    <div class="m-auto" style="width: 36rem">
      <div class="mb-4 relative text-center rounded w-full">
        <text style="font-size: 80px;">테스트 페이지</text>
      </div>
      <div class="w-full text-center">
        <button class="bg-indigo-600 hover:bg-blue-dark text-white font-bold py-3 px-6 rounded" @click.stop.prevent="connect">챗봇열기</button>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios'
import Communicator from '../utillities/Communicator'
import sessionUtils from '../utillities/sessionUtils'
import store from '../store/index'
import modalOpener from "../utillities/mixins/modalOpener"


export default {
  mixins: [modalOpener],
  data() {
    return {
      communicator: new Communicator(),
      opened: true,
      form: {
        url: 'https://cloudtalk.eicn.co.kr:442',
        senderKey: null,
        userKey: sessionUtils.getSessionId(),
        ip: '',
        mode: 'service',
      },
    }
  },
  methods: {
    connect() {
      this.communicator.connect(this.form.url, this.form.senderKey, this.form.userKey, this.form.ip, this.form.mode,)
      this.communicator.on('webchatsvc_start', data => {
        if (data.result !== 'OK') return store.commit('alert/show', {body: `로그인실패 : ${data.result}; ${data.result_data}` ,isClose: true})
        this.communicator.disconnect()
        this.openChat(this.form.url, this.form.senderKey, this.form.userKey, this.form.ip, this.form.mode,)
        this.opened = true
      })
    },
  },
  async created() {
    const UrlParams = new URLSearchParams(location.search)
    this.form.senderKey = UrlParams.get('senderKey')
    this.form.ip = (await axios.get('https://api.ipify.org')).data
    if (this.opened) this.openChat(this.form.url, this.form.senderKey, this.form.userKey, this.form.ip, this.form.mode,)
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
