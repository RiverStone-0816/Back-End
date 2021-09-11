<template>
  <section class=" w-full">
    <h1 class="text-center w-full">Tensorflow Object Detection</h1>

    <div class="flex justify-between">
      <div class="py-5 w-1/2">
        <button v-if="!isStreaming" class="border rounded pr-2 shadow-md bg-gradient-to-r from-blue-500 to-indigo-800 w-full text-white" type="button" @click="openCamera">Open Camera</button>
        <button v-if="isStreaming" class="border rounded pr-2 shadow-md bg-gradient-to-r from-blue-500 to-indigo-800 w-full text-white" type="button" @click="stopCamera">Stop Camera</button>
        <button v-if="isStreaming" class="border rounded pr-2 shadow-md bg-gradient-to-r from-green-400 to-purple-900 w-full text-white" type="button" @click="snapShot">snapshot</button>
        <video v-show="isStreaming" ref="videoRef" autoplay width="500"/>
      </div>

      <div class="py-5 w-1/2">
        <div class="border rounded pr-2 shadow-md bg-gradient-to-r from-blue-500 to-indigo-800 w-full text-white invisible">&ensp;</div>
        <button class="border rounded pr-2 shadow-md bg-gradient-to-r from-red-500 to-pink-800 w-full text-white" type="button" @click="detect">Detect</button>
        <img ref="imgRef" alt="tensorflow source" crossOrigin='anonymous' src="https://images.unsplash.com/photo-1567581935884-3349723552ca" width="500"/>
        <p class="py-3">
          <span v-for="(e,i) in results" :key="i" class="p-2 border rounded-md m-2 border-red-500" v-text="e.class"/>
        </p>
      </div>
    </div>

  </section>
</template>

<script>
import {ref} from "vue";
import store from "@/store/index"

require('@tensorflow/tfjs-backend-cpu');
require('@tensorflow/tfjs-backend-webgl');
const cocoSsd = require('@tensorflow-models/coco-ssd');

export default {
  setup() {
    const imgRef = ref()
    const videoRef = ref()
    const isStreaming = ref(false)
    const results = ref([])

    async function detect() {
      store.commit('alert/show', 'analyzing...')

      const model = await cocoSsd.load()
      results.value = await model.detect(imgRef.value)

      store.commit('alert/close')
    }

    async function openCamera() {
      if (navigator.mediaDevices.getUserMedia) {
        const devices = await navigator.mediaDevices.enumerateDevices()
        const cams = devices.filter(e => e.kind === 'videoinput')

        if (!cams.length)
          return

        navigator.mediaDevices.getUserMedia({video: {deviceId: {exact: cams[0].deviceId}}}).then(stream => {
          isStreaming.value = true
          videoRef.value.srcObject = stream
        })
      }
    }

    function stopCamera() {
      const stream = videoRef.value.srcObject
      stream.getTracks().forEach(e => e.stop())
      isStreaming.value = false
    }

    function snapShot() {
      const canvas = document.createElement('canvas')
      canvas.width = videoRef.value.videoWidth
      canvas.height = videoRef.value.videoHeight
      const context = canvas.getContext('2d')
      context.drawImage(videoRef.value, 0, 0)
      const data = canvas.toDataURL('image/png')
      imgRef.value.setAttribute('src', data)
    }

    return {imgRef, videoRef, results, isStreaming, detect, openCamera, stopCamera, snapShot}
  },
}
</script>
