<template>
  <div class="mx-auto w-9/12 ">
    <section class="my-3 flex justify-between">
      <button class="p-2 border rounded w-[calc(100%/7)]" style="width: calc(100% / 7)" type="button" @click="toPrevMonth">Prev</button>
      <div class="text-2xl my-2">
        <div class="px-3 font-bold">{{ currentYear }}년 {{ getMonthLocalString(currentMonth) }}</div>
      </div>
      <button class="p-2 border rounded w-[calc(100%/7)]" style="width: calc(100% / 7)" type="button" @click="toNextMonth">Next</button>
    </section>
    <section class="flex">
      <p v-for="day in days" :key="day" class="h-5 text-center mb-5 w-[calc(100%/7)]" style="width: calc(100% / 7)">{{ day }}</p>
    </section>
    <section class="flex flex-wrap">
      <p v-for="i in getStartDay(currentYear, currentMonth)" :key="i"
         class="h-5 text-center w-[calc(100%/7)]" style="width: calc(100% / 7)"></p>
      <p v-for="i in daysInMonth(currentYear, currentMonth)" :key="i"
         :class="isToday(currentYear, currentMonth, i) && 'font-bold text-yellow-600'"
         class="h-5 text-center w-[calc(100%/7)]" style="width: calc(100% / 7)">{{ i }}</p>
    </section>
  </div>
</template>
<script>
const DECEMBER = 11, JANUARY = 0

export default {
  data() {
    return {
      currentMonth: new Date().getMonth(),
      currentYear: new Date().getFullYear(),
      days: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    }
  },
  methods: {
    getStartDay(year, month) {
      return new Date(year, month, 1).getDay()
    },
    getMonthLocalString(month) {
      const date = new Date()
      date.setMonth(month)
      return date.toLocaleString('ko', {month: 'long'})
    },
    daysInMonth(year, month) {
      return new Date(year, month + 1, 0).getDate()
    },
    toPrevMonth() {
      this.currentMonth--
      if (this.currentMonth < JANUARY) {
        this.currentMonth = DECEMBER
        this.currentYear--
      }
    },
    toNextMonth() {
      this.currentMonth++
      if (this.currentMonth > DECEMBER) {
        this.currentMonth = JANUARY
        this.currentYear++
      }
    },
    isToday(year, month, date) {
      const today = new Date()
      return today.getFullYear() === year
          && today.getMonth() === month
          && today.getDate() === date
    }
  }
}
</script>
