<template>
  <div v-if="isHiding" :class="activated ? 'bg-gray-800' : 'hover:bg-gray-800 cursor-pointer'" class="flex justify-between items-center p-3 rounded-lg relative" @click="$emit('callChat')">
    <div class="w-16 h-16 relative flex flex-shrink-0">
      <img :src="getAvatar" alt="avatar" class="shadow-md rounded-full w-full h-full object-cover"/>
      <div v-if="person.isLoginChatt === 'L'" class="absolute bg-gray-900 p-1 rounded-full bottom-0 right-0">
        <div class="bg-green-500 rounded-full w-3 h-3"></div>
      </div>
    </div>
    <div class="flex-auto min-w-0 ml-4 mr-6 hidden md:block group-hover:block">
      <p v-text="person.idName"/>
      <div :class="activated ? 'font-bold' : 'text-gray-600'" class="flex items-center text-sm">
        <div class="min-w-0">
          <p class="truncate" v-text="chat.lastMessage"/>
        </div>
        <p class="ml-2 whitespace-no-wrap" v-text="getLastMessageTime"/>
      </div>
    </div>

    <div v-if="chat.readAll" class="w-4 h-4 flex flex-shrink-0 hidden md:block group-hover:block">
      <img :src="getAvatar" alt="avatar" class="rounded-full w-full h-full object-cover"/>
    </div>
  </div>
</template>

<script>
import moment from "moment"
import avatar from "@/assets/avatar1.svg"

export default {
  props: {
    person: {
      idName: String,
      profilePhoto: String,
    },
    chat: {
      lastMessage: String,
      lastMessageTime: Date,
      readAll: Boolean,
    },
    activated: Boolean,
    filterKeyword: String
  },
  computed: {
    getAvatar() {
      return this.person.profilePhoto || avatar
    },
    getLastMessageTime() {
      if (!this.chat.lastMessageTime)
        return ''

      const messageTime = moment(this.chat.lastMessageTime)
      const duration = moment.duration(moment(new Date()).diff(messageTime))

      return duration.asMinutes() < 2 ? 'Just now'
          : duration.asHours() < 1 ? `Before ${duration.asMinutes()} minutes`
              : duration.asHours() < 2 ? 'Before 1 hour'
                  : duration.asDays() < 1 ? `Before ${duration.asHours()} hours`
                      : messageTime.format('D MMM')
    },
    isHiding() {
      return this.person.idName.includes(this.filterKeyword) || (this.chat && this.chat.lastMessage.includes(this.filterKeyword))
    }
  }
}
</script>
