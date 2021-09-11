<template>
  <div class="my-auto">
    <h1 class="text-center">Welcome to Vue Study</h1>

    <button class="border px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-300 hover:ring-2" type="button" @click="callAlert">call alert</button>
  </div>
</template>

<script>
export default {
  methods: {
    callAlert() {
      this.$store.commit('alert/show', '<div class="text-red-500">alert body!!!</div>')
    }
  }
}
</script>

<style scoped>

</style>
