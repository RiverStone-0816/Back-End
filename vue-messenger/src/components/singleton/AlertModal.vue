<template>
  <TransitionRoot :show="store.showing" appear as="template">
    <Dialog as="div" @close="this.$store.commit('alert/close')">
      <div class="fixed inset-0 z-10 overflow-y-auto">
        <div class="min-h-screen px-4 text-center">
          <TransitionChild as="template"
                           enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100"
                           leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
            <DialogOverlay class="fixed inset-0"/>
          </TransitionChild>

          <span aria-hidden="true" class="inline-block h-screen align-middle">&#8203;</span>

          <TransitionChild as="template"
                           enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100"
                           leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
            <div class="inline-block w-full max-w-md p-6 my-8 overflow-hidden text-left align-middle transition-all transform bg-white shadow-xl rounded-2xl">
              <div class="mt-2">
                <p class="text-sm text-gray-500 py-5" v-html="store.body"></p>
              </div>

              <div class="mt-4 text-center">
                <button
                    ref="closeButton"
                    class="inline-flex justify-center px-4 py-2 text-sm font-medium text-blue-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                    type="button" @click="this.$store.commit('alert/close')">
                  Close
                </button>
              </div>
            </div>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script>
import {Dialog, DialogOverlay, TransitionChild, TransitionRoot,} from '@headlessui/vue'
import {mapGetters} from 'vuex'

export default {
  components: {
    TransitionRoot,
    TransitionChild,
    Dialog,
    DialogOverlay,
  },
  data() {
    return {
      store: this.$store.state.alert
    }
  },
  computed: {
    ...mapGetters([
      'alert/showing',
      'alert/body',
    ])
  },
  watch: {
    'alert/showing'() {
      console.log(this.store)
    }
  },
  updated() {
    this.$refs.closeButton.focus()
  }
}
</script>
