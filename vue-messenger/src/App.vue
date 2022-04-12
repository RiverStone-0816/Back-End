<template>

  <template v-if="isModal()">
    <VChart/>
  </template>
  <template v-else>
    <Main/>
  </template>

  <teleport to="#modals">
    <AlertModal/>

    <div v-if="isLoading">
      <div class="lds-ellipsis">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
      <div>Loading... ({{ refCount }})</div>
    </div>
  </teleport>

</template>

<script>
import axios from 'axios'
import AlertModal from './components/singleton/AlertModal'
/*import Login from './pages/Login'*/
import VChart from './pages/VChart'
import Main from './pages/Main'
import sessionUtils from './utillities/sessionUtils'
import modalOpener from "./utillities/mixins/modalOpener"

// ref: https://stackoverflow.com/questions/50768678/axios-ajax-show-loading-when-making-ajax-request
export default {
  mixins: [modalOpener],
  components: {AlertModal, VChart, Main,},
  data() {
    return {
      refCount: 0,
      isLoading: false
    }
  },
  methods: {
    setLoading(isLoading) {
      if (isLoading) {
        this.refCount++
        this.isLoading = true
      } else if (this.refCount > 0) {
        this.refCount--
        this.isLoading = (this.refCount > 0)
      }
    },
  },
  created() {
    axios.interceptors.request.use((config) => {
      this.setLoading(true)
      return config
    }, (error) => {
      this.setLoading(false)
      return Promise.reject(error)
    })

    axios.interceptors.response.use((response) => {
      this.setLoading(false)
      return response
    }, (error) => {
      this.setLoading(false)

      if (error.response && error.response.status === 401) {
        sessionUtils.clear()
        // FIXME: 401 처리
        return
      }

      return Promise.reject(error)
    })
  }
}
</script>
<style scoped>
/** https://loading.io/css/ **/
.lds-ellipsis {
  display: inline-block;
  position: relative;
  width: 64px;
  height: 64px;
}
.lds-ellipsis div {
  position: absolute;
  top: 27px;
  width: 11px;
  height: 11px;
  border-radius: 50%;
  background: #ddd;
  animation-timing-function: cubic-bezier(0, 1, 1, 0);
}
.lds-ellipsis div:nth-child(1) {
  left: 6px;
  animation: lds-ellipsis1 0.6s infinite;
}
.lds-ellipsis div:nth-child(2) {
  left: 6px;
  animation: lds-ellipsis2 0.6s infinite;
}
.lds-ellipsis div:nth-child(3) {
  left: 26px;
  animation: lds-ellipsis2 0.6s infinite;
}
.lds-ellipsis div:nth-child(4) {
  left: 45px;
  animation: lds-ellipsis3 0.6s infinite;
}
@keyframes lds-ellipsis1 {
  0% {
    transform: scale(0);
  }
  100% {
    transform: scale(1);
  }
}
@keyframes lds-ellipsis3 {
  0% {
    transform: scale(1);
  }
  100% {
    transform: scale(0);
  }
}
@keyframes lds-ellipsis2 {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(19px, 0);
  }
}
</style>
