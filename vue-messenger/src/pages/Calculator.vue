<template>
  <section class="flex w-full">
    <div class="m-auto w-48">
      <h1 class="text-2xl text-center">Calculator</h1>
      <p ref="expression" class="text-3xl text-right mt-10 my-4 overflow-y-hidden break-all" style="min-height: 3rem">{{ data.text }}</p>
      <small>{{ data.operand1 }} {{ data.operator }} {{ data.operand2 }}</small>
      <div class="grid grid-cols-4 gap-1">
        <button v-for="letter in data.letters" :key="letter" class="p-2 w-10 h-10 border rounded shadow mb-2" type="button" @click="pressed(letter)" v-text="letter"></button>
      </div>
    </div>
  </section>
</template>

<script>
import {ref} from 'vue'
import useWindowEvent from "@/utillities/compositions/useWindowEvent"

export default {
  setup() {
    const data = ref({
      letters: [1, 2, 3, '+', 4, 5, 6, '-', 7, 8, 9, '*', 0, 'C', '=', '/',],
      text: '',
      operand1: '',
      operand2: '',
      operator: '',
    })

    const calculate = () => data.value.operator === '+' ? parseFloat(data.value.operand1) + parseFloat(data.value.operand2)
        : data.value.operator === '-' ? parseFloat(data.value.operand1) - parseFloat(data.value.operand2)
            : data.value.operator === '*' ? parseFloat(data.value.operand1) * parseFloat(data.value.operand2)
                : parseFloat(data.value.operand1) / parseFloat(data.value.operand2)

    const pressed = letter => {
      if (letter === 'C') {
        data.value.operand1 = ''
        data.value.operand2 = ''
        data.value.operator = ''
        data.value.text = ''
        return
      }

      if (isFinite(letter) && !isNaN(letter)) {
        if (!data.value.operand1 || !data.value.operator) {
          data.value.operand1 += letter
          data.value.text = data.value.operand1
        } else {
          data.value.operand2 += letter
          data.value.text = data.value.operand2
        }
        return
      }

      if (['+', '-', '*', '/'].includes(letter)) {
        if (!data.value.operand1)
          return

        if (data.value.operand2) {
          const result = calculate()
          data.value.operand1 = isFinite(result) && !isNaN(result) ? result : '0'
        }

        data.value.operator = letter
        data.value.operand2 = ''
        data.value.text = ''
        return
      }

      if (letter === '=') {
        const result = calculate()
        data.value.text = isFinite(result) && !isNaN(result) ? result : '0'
        data.value.operand1 = data.value.text
        data.value.operand2 = ''
        data.value.operator = ''
      }
    }

    useWindowEvent('keyup', (event) => pressed(event.key.toLowerCase() === 'enter' ? '=' : event.key.toLowerCase() === 'c' ? 'C' : event.key))

    return {data, pressed}
  }
}
</script>
