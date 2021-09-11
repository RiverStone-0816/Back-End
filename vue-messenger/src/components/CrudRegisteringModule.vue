<template>
  <div>
    <button class="border px-3 py-2 rounded-md text-sm font-medium hover:bg-blue-300 hover:ring-2 float-right" type="button" @click="showingModal = true">
      add user
    </button>

    <teleport to="body">
      <Modal :showing="showingModal" overlay-class="bg-blue-500 bg-opacity-50" @close="showingModal = false">
        <template #header>
          <strong>Add User</strong>
        </template>
        <template #body>
          <form @submit.prevent>
            <div class="mt-2">
              <label>first name</label>
              <input v-model="form.first_name" class="rounded shadow p-2 w-full" placeholder="enter your username or email" type="text"/>
            </div>
            <div class="mt-2">
              <label>last name</label>
              <input v-model="form.last_name" class="rounded shadow p-2 w-full" placeholder="enter your username or email" type="text"/>
            </div>
            <div class="mt-2">
              <label>email</label>
              <input v-model="form.email" class="rounded shadow p-2 w-full" placeholder="enter your username or email" type="text"/>
            </div>
            <div class="mt-2">
              <label>avatar</label>
              <input v-model="form.avatar" class="rounded shadow p-2 w-full" placeholder="enter your username or email" type="text"/>
            </div>
          </form>
        </template>
        <template #footer>
          <div class="text-center">
            <button class="p-2 border rounded hover:bg-blue-200 focus:bg-blue-200 " type="button" @click="submitUser">submit</button>
          </div>
        </template>
      </Modal>
    </teleport>
  </div>
</template>
<script>
import Modal from "@/components/Modal"
import axios from "axios";

const API_BASE_URL = 'https://reqres.in/api/users'

export default {
  components: {
    Modal
  },
  data() {
    return {
      showingModal: false,
      form: {
        first_name: null,
        last_name: null,
        email: null,
        avatar: null
      }
    }
  },
  methods: {
    async submitUser() {
      try {
        const response = await axios.post(API_BASE_URL, {
          params: this.form
        })
        console.log(response)

        if (200 <= response.status < 300) {
          this.showingModal = false
          this.form = {
            first_name: null,
            last_name: null,
            email: null,
            avatar: null
          }

          // When headlessui Modals are over 1, cannot find focusable element.
          setTimeout(() => this.$store.commit('alert/show', 'success'), 500)

          this.$emit('add-new-user')
        } else {
          this.showingModal = false
          setTimeout(() => this.$store.commit('alert/show', response.statusText), 500)
        }
      } catch (e) {
        console.error(e)
      }
    },
  }
}
</script>
