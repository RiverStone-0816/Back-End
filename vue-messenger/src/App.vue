<template>

  <div v-if="path === PATH.LOGIN">
    <LOGIN/>
  </div>
  <div v-else>
    <AppHeader/>
    <router-view/>
  </div>

  <teleport to="#modals">
    <AlertModal/>
  </teleport>

</template>

<script>
import AlertModal from "@/components/singleton/AlertModal";
import LOGIN from "@/pages/Login";
import AppHeader from "@/components/singleton/AppHeader";
import {PATH} from "@/router";
import axios from "@/plugins/axios"

export default {
  components: {
    AlertModal,
    AppHeader,
    LOGIN
  },
  setup() {
    return {
      PATH: PATH
    }
  },
  data() {
    return {
      path: PATH.MAIN
    }
  },
  watch: {
    $route(to) {
      this.path = to.path
    }
  },
  created() {
    axios.get('/api/auth/sockets').then(response => {
      this.$store.state.socket = response.data.data
    }).catch(e => {
      console.log(e.response)
    })
  }
}
</script>
