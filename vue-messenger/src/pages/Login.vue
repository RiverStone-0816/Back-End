<!-- ref: https://tailwindcomponents.com/component/checkbox -->
<!-- ref: https://tailwindcomponents.com/component/cool-text-inputs-and-login -->
<template>
  <section class="flex w-full h-screen">
    <div class="m-auto w-72">

      <form v-if="!showingVerification" @submit.stop.prevent="prepareVerification">
        <h1 class="text-4xl font-black mb-4 text-center">Login</h1>
        <div class="mb-4 relative">
          <input v-model="loginForm.company" :class="loginForm.company && 'filled'" autofocus class="input" type="text">
          <label class="label">회사아이디</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="loginForm.id" :class="loginForm.id && 'filled'" class="input" type="text">
          <label class="label">아이디</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="loginForm.password" :class="loginForm.password && 'filled'" class="input" type="password">
          <label class="label">비밀번호</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="loginForm.extension" :class="loginForm.extension && 'filled'" class="input" type="text">
          <label class="label">내선번호</label>
        </div>
        <div class="text-right">
          <label class="inline-flex items-center mb-3">
            <input v-model="remember" class="form-checkbox h-5 w-5 text-orange-600" type="checkbox"><span class="ml-2 text-gray-700">로그인저장</span>
          </label>
        </div>

        <button class="bg-indigo-600 hover:bg-blue-dark text-white font-bold py-3 px-6 rounded w-full">로그인</button>
      </form>

      <form v-else @submit.stop.prevent="verify">
        <h1 class="text-4xl font-black mb-4 text-center">Login</h1>
        <div class="mb-4 relative">
          <input v-model="verifyForm.phone" :class="verifyForm.phone && 'filled'" autofocus class="input" type="text">
          <label class="label">전화번호</label>
        </div>
        <div class="mb-4 relative">
          <input v-model="verifyForm.code" :class="verifyForm.code && 'filled'" autofocus class="input" type="text">
          <label class="label">인증번호</label>
        </div>
        <div class="mb-4 relative" v-text="verificationStatus"></div>
        <button class="bg-indigo-600 hover:bg-blue-dark text-white font-bold py-3 px-6 rounded w-full">인증시도하기</button>
      </form>
    </div>
  </section>
</template>

<script>
import axios from "@/plugins/axois"
import router from "@/router"

export default {
  data() {
    return {
      communicator: this.$store.state.ipccAdmin.communicator,
      showingVerification: false,
      loginForm: {
        company: 'eicn',
        id: 'user0990',
        password: 'user12!@!',
        extension: '0990',
      },
      verifyForm: {
        phone: null,
        code: null,
        sessionId: null,
        pbxHost: null
      },
      remember: false,
      verificationStatus: null
    }
  },
  methods: {
    prepareVerification() {
      axios.post('/api/auth/check-login-condition', this.loginForm).then(response => {
        if (!response.data.number)
          return this.login()

        this.verifyForm.phone = response.data.number
        this.verifyForm.code = response.data.authNum
        this.verifyForm.sessionId = response.data.sessionId
        this.verifyForm.pbxHost = response.data.pbxHost
        this.showingVerification = true

        this.communicator.connect(this.$store.state.socket.monitor_connector, this.loginForm.company, this.loginForm.id, this.verifyForm.pbxHost)
      }).catch(e => {
        console.log(e.response)
      })
    },
    verify() {
      this.communicator.send({
        company_id: this.loginForm.company,
        userid: this.loginForm.id,
        target_pbx: this.verifyForm.pbxHost,
        command: 'CMD|ARS_AUTH|' + this.loginForm.company + ',' + this.loginForm.id + ',' + this.verifyForm.phone + ',' + this.verifyForm.code + ',' + this.verifyForm.sessionId
      })
    },
    login() {
      axios.get('/api/auth/confirm-login').then(() => {
        axios.get('/api/user/' + this.loginForm.id, this.loginForm).then(response => {
          alert('success to login')
          this.$store.commit('user/login', response.data.data)
          router.push('/')
        })
      })
    },
    setLabelClickHandler() {
      this.$el.querySelectorAll('.label').forEach(e => e.setAttribute('onclick', 'this.previousElementSibling.focus()'))
    },
  },
  mounted() {
    this.setLabelClickHandler()
  },
  updated() {
    this.setLabelClickHandler()
  },
  created() {
    const _this = this
    this.communicator.on('ARS_AUTH_RES', function (message, kind, peer, data1, data2) {
      switch (kind) {
        case 'CALLREQ_OK':
          return _this.verificationStatus = '인증시도'
        case 'CALLREQ_NOK':
          return _this.verificationStatus = '인증시도실패: ' + data2
        case 'HANGUP':
          return _this.verificationStatus = 'HANGUP'
        case 'DIALUP':
          return _this.verificationStatus = '전화연결'
        case 'FAIL':
          return _this.verificationStatus = '인증실패'
        case 'SUCC':
          axios.post('/api/auth/login', _this.loginForm)
              .then(() => this.login())
              .catch(e => console.log(e.response))
      }
    })
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
