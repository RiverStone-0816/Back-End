<template>
  <section class="flex flex-wrap w-full">
    <div class="m-auto">
      <h1 class="text-3xl text-center">Re-usable Modal</h1>

      <div class="pt-32 text-center">
        <button
            class="px-4 py-2 text-sm font-medium text-white bg-black rounded-md bg-opacity-20 hover:bg-opacity-30 focus:outline-none focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75"
            type="button" @click="showing = true">
          Open dialog
        </button>
      </div>
    </div>

    <Modal :showing="showing" overlayClass="bg-gray-100 bg-opacity-20" @close="showing = false">
      <template #header>
        <strong>HEADER1</strong>
      </template>
      <template #body>
        <small>BODY1123123</small>
      </template>
      <!--
      <template #footer>
        <div class="text-center">
          <button class="inline-flex justify-center px-4 py-2 text-sm font-medium text-blue-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500" type="button"
                  @click="showing = false">Close of parent</button>
        </div>
      </template>
      -->
    </Modal>
  </section>
</template>

<script>
import Modal from "@/components/Modal";

export default {
  components: {
    Modal
  },
  data() {
    return {
      showing: false
    }
  }
}
</script>

<style scoped>

</style>
