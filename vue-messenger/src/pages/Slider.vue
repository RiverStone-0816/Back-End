<template>
  <div class="flex flex-wrap w-full">
    <h1 class=" w-full text-center text-3xl my-4">Slider</h1>

    <div class="h-60 w-full relative">
      <div v-for="slider in sliders" :key="slider" class="absolute w-full">
        <transition name="fade">
          <div v-if="sliders[currentSlider] === slider" :class="slider" class="w-full h-60"/>
        </transition>
      </div>

      <div class="absolute bottom-3 flex w-full justify-center">
        <button v-for="(slider, index) in sliders" :key="slider" :class="slider" class="w-4 h-4 rounded-full mx-2"
                type="button" @click="changeSlider(index)"/>
      </div>
    </div>


    <div class="my-10 flex w-full justify-center">
      <button class="rounded border px-2 hover:bg-gray-100" type="button" @click="showingTitle = !showingTitle">
        toggle title
      </button>
    </div>
    <div class="flex w-full justify-center">
      <transition name="fade">
        <h1 v-show="showingTitle">Slider Carousel</h1>
      </transition>
    </div>
  </div>
</template>
<script>

export default {
  data() {
    return {
      currentSlider: 0,
      sliders: ['bg-blue-200', 'bg-yellow-200', 'bg-gray-200'],
      task: null,
      showingTitle: true
    }
  },
  computed: {},
  methods: {
    changeSlider(index) {
      this.currentSlider = index
      clearInterval(this.task)
    }
  },
  mounted() {
    this.task = setInterval(() => {
      this.currentSlider = (this.currentSlider + 1) % this.sliders.length
    }, 1000)
  },
  beforeUnmount() {
    clearInterval(this.task)
  }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease;
}
.fade-enter-from {
  opacity: 0;
  transform: translateX(-100%);
}
.fade-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>
