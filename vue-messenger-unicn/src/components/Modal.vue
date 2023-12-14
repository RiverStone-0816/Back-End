<template>
  <TransitionRoot :show="showing" appear as="template">
    <Dialog as="div">
      <div class="fixed inset-0 z-10 overflow-y-auto">
        <div class="min-h-screen px-4 text-center">
          <TransitionChild as="template"
                           enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100"
                           leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
            <DialogOverlay :class="overlayClass" class="fixed inset-0"/>
          </TransitionChild>

          <span aria-hidden="true" class="inline-block h-screen align-middle">&#8203;</span>

          <TransitionChild as="template"
                           enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100"
                           leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
            <div class="inline-block w-full max-w-md p-6 my-8 overflow-hidden text-left align-middle transition-all transform bg-white shadow-xl rounded-2xl">
              <DialogTitle as="h3" class="text-lg font-medium leading-6 text-gray-900">
                <slot name="header"/>
              </DialogTitle>
              <div class="mt-2">
                <p class="text-sm text-gray-500 py-5">
                  <slot name="body"/>
                </p>
              </div>

              <div class="mt-4">
                <slot name="footer">
                  <button
                      class="inline-flex justify-center px-4 py-2 text-sm font-medium text-blue-900 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-blue-500"
                      type="button" @click="this.$emit('close')">Close of self</button>
                </slot>
              </div>
            </div>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script>
import {Dialog, DialogOverlay, DialogTitle, TransitionChild, TransitionRoot,} from '@headlessui/vue'

export default {
  props: {
    showing: Boolean, overlayClass: String
  },
  components: {
    TransitionRoot,
    TransitionChild,
    Dialog,
    DialogOverlay,
    DialogTitle,
  },
}
</script>
