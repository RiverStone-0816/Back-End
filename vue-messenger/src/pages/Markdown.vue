<template>
  <div class="flex flex-wrap w-full">
    <h1 class="w-full text-center text-3xl my-4">Markdown App</h1>
    <section class="flex m-auto w-10/12 h-screen">
      <article class="w-1/2 border">
        <textarea ref="input" class="w-full h-full" @input="update"></textarea>
      </article>
      <article class="w-1/2 border bg-gray-100" v-html="getMarked"/>
    </section>
  </div>
</template>
<script>
import marked from 'marked'
import useDebounce from "@/utillities/compositions/useDebounce";

export default {
  data() {
    return {
      text: '',
      debounce: useDebounce()
    }
  },
  computed: {
    getMarked() {
      return marked(this.text)
    }
  },
  methods: {
    update(e) {
      this.debounce(() => this.text = e.target.value)
    },
  },
  mounted() {
    this.$refs.input.focus()
  }
}
</script>
