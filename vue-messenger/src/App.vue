<template>

  <div v-if="path === PATH.LOGIN">
    <LOGIN/>
  </div>
  <div v-else>
    <router-view/>
  </div>

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
import AlertModal from "@/components/singleton/AlertModal";
import LOGIN from "@/pages/Login";
import {PATH} from "@/router";
import axios from "@/plugins/axios"

// ref: https://stackoverflow.com/questions/50768678/axios-ajax-show-loading-when-making-ajax-request
export default {
  components: {
    AlertModal,
    LOGIN
  },
  setup() {
    return {
      PATH: PATH
    }
  },
  data() {
    return {
      path: PATH.MAIN,
      refCount: 0,
      isLoading: false
    }
  },
  watch: {
    $route(to) {
      this.path = to.path
    }
  },
  methods: {
    setLoading(isLoading) {
      if (isLoading) {
        this.refCount++;
        this.isLoading = true;
      } else if (this.refCount > 0) {
        this.refCount--;
        this.isLoading = (this.refCount > 0);
      }
    }
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
      return Promise.reject(error)
    })

    axios.get('/api/auth/sockets').then(response => {
      this.$store.state.socket = response.data.data
    }).catch(e => {
      console.log(e.response)
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
