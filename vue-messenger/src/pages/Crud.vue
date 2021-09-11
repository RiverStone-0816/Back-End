<template>
  <div class="flex flex-wrap justify-center w-full">
    <div class="w-2/3">
      <div class="flex justify-between my-5">
        <h1 class="text-3xl">CRUD</h1>
        <CrudRegisteringModule @add-new-user="getPagination"/>
      </div>
      <table class="w-full">
        <thead>
        <tr>
          <th class="border bg-gray-100"></th>
          <th class="border bg-gray-100">email</th>
          <th class="border bg-gray-100">name</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="e in list" :key="e.id">
          <td class="border text-center flex justify-center">
            <img :alt="e.first_name + ' ' + e.last_name" :src="e.avatar"/>
          </td>
          <td class="border text-center" v-text="e.email"/>
          <td class="border text-center" v-text="e.first_name + ' ' + e.last_name"/>
        </tr>
        </tbody>
      </table>

      <VueTailwindPagination :current="page" :per-page="limit" :total="total" @page-changed="onPageClick"/>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import VueTailwindPagination from "@ocrv/vue-tailwind-pagination";
import CrudRegisteringModule from "@/components/CrudRegisteringModule";

const API_BASE_URL = 'https://reqres.in/api/users'

export default {
  components: {VueTailwindPagination, CrudRegisteringModule},
  data() {
    return {
      list: [],
      page: 1,
      limit: 3,
      total: undefined,
    }
  },
  methods: {
    async getPagination() {
      try {
        const {data} = await axios.get(API_BASE_URL, {
          params: {page: this.page, per_page: this.limit}
        })
        this.page = data.page
        this.total = data.total
        this.list = data.data
      } catch (e) {
        console.error(e)
      }
    },
    onPageClick(page) {
      this.page = page
    }
  },
  watch: {
    page() {
      this.getPagination()
    }
  },
  mounted() {
    this.getPagination()
  }
}
</script>
