<template>
  <body class="h-screen overflow-hidden flex items-center justify-center" style="background: #edf2f7;">

  <div class="h-screen w-full flex antialiased text-gray-200 bg-gray-900 overflow-hidden">
    <div class="flex-1 flex flex-col">
      <div class="border-b-2 border-gray-800 p-2 flex flex-row z-20">
        <div class="bg-red-600 w-3 h-3 rounded-full mr-2"></div>
        <div class="bg-yellow-500 w-3 h-3 rounded-full mr-2"></div>
        <div class="bg-green-500 w-3 h-3 rounded-full mr-2"></div>
      </div>
      <main class="flex-grow flex flex-row min-h-0">
        <section class="flex flex-col flex-none overflow-auto w-24 hover:w-64 group lg:max-w-sm md:w-2/5 transition-all duration-300 ease-in-out">
          <div class="header p-4 flex flex-row justify-between items-center flex-none">
            <div class="w-16 h-16 relative flex flex-shrink-0" style="filter: invert(100%);">
              <img alt="ravisankarchinnam" class="rounded-full w-full h-full object-cover" src="https://avatars3.githubusercontent.com/u/22351907?s=60"/>
            </div>
            <p class="text-md font-bold hidden md:block group-hover:block">Messenger</p>
            <a class="block rounded-full hover:bg-gray-700 bg-gray-800 w-10 h-10 p-2 hidden md:block group-hover:block" href="#">
              <svg class="w-full h-full fill-current" viewBox="0 0 24 24">
                <path
                    d="M6.3 12.3l10-10a1 1 0 0 1 1.4 0l4 4a1 1 0 0 1 0 1.4l-10 10a1 1 0 0 1-.7.3H7a1 1 0 0 1-1-1v-4a1 1 0 0 1 .3-.7zM8 16h2.59l9-9L17 4.41l-9 9V16zm10-2a1 1 0 0 1 2 0v6a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V6c0-1.1.9-2 2-2h6a1 1 0 0 1 0 2H4v14h14v-6z"/>
              </svg>
            </a>
          </div>
          <div class="search-box p-4 flex-none">
            <div class="relative">
              <label>
                <input
                    class="rounded-full py-2 pr-6 pl-10 w-full border border-gray-800 focus:border-gray-700 bg-gray-800 focus:bg-gray-900 focus:outline-none text-gray-200 focus:shadow-md transition duration-300 ease-in"
                    placeholder="Search Messenger"
                    @change="filterPerson"/>
                <text class="absolute top-0 left-0 mt-2 ml-3 inline-block">
                  <svg class="w-6 h-6" viewBox="0 0 24 24">
                    <path d="M16.32 14.9l5.39 5.4a1 1 0 0 1-1.42 1.4l-5.38-5.38a8 8 0 1 1 1.41-1.41zM10 16a6 6 0 1 0 0-12 6 6 0 0 0 0 12z" fill="#bbb"/>
                  </svg>
                </text>
              </label>
            </div>
          </div>
          <div class="contacts p-2 flex-1 overflow-y-scroll">
            <PersonCard v-for="p in persons" :key="p.id" :activated="false" :chat="{lastMessage: 'Ok, see you at the subway in a bit.', lastMessageTime: null, readAll: true }"
                        :filter-keyword="personKeyword" :person="p" @call-chat="callChat(p.id)"/>
          </div>
        </section>
        <ChatRoom :person="currentRoomPerson" :roomId="currentRoomId"/>
      </main>
    </div>
  </div>
  </body>
</template>

<script>
import axios from '@/plugins/axios'
import sessionUtils from '@/utillities/sessionUtils'
import PersonCard from "@/components/PersonCard";
import ChatRoom from "@/components/ChatRoom";

export default {
  components: {
    ChatRoom,
    PersonCard
  },
  data() {
    return {
      me: null,
      personKeyword: '',
      activated: {},
      root: null,
      persons: [],
      currentRoomId: null,
      currentRoomPerson: null
    }
  },
  methods: {
    callAlert() {
      this.$store.commit('alert/show', '<div class="text-red-500">alert body!!!</div>')
    },
    filterPerson(event) {
      this.personKeyword = event.target.value
    },
    sortPersons() {
      this.persons.sort((a, b) => {
        console.log(a.chatRoom, b.chatRoom)
        if (!a.chatRoom || !a.chatRoom.chattRoom)
          return -1
        if (!b.chatRoom || !b.chatRoom.chattRoom)
          return 1
        return a.chatRoom.chattRoom.lastTime - b.chatRoom.chattRoom.lastTime
      })
    },
    callChat(personId) {
      const persons = this.persons.filter(p => p.id === personId)
      if (!persons || !persons.length)
        return

      const person = persons[0]
      if (person.chatRoom) {
        this.loadRoom(person, person.chatRoom.chattRoom.roomId)
      } else {
        this.createRoom(person)
      }
    },
    loadRoom(person) {
      this.currentRoomId = person.chatRoom.chattRoom.roomId
      this.currentRoomPerson = person
    },
    async createRoom(person) {
      this.currentRoomId = (await axios.post('/api/chatt/', {memberList: [person.id, this.me.id]})).data.data
      this.currentRoomPerson = person
    },
    loadRoomInfo(roomId) {
      axios.get(`/api/chatt/chatt-room/${roomId}`).then(res => res.data.data.chattingMembers.length === 1 && this.persons.forEach(person => (person.id === res.data.data.chattingMembers[0].userid) && (person.chatRoom = res)))
    }
  },
  async mounted() {
    this.me = (await sessionUtils.fetchMe())
    // this.$store.commit('monitor/login') // 굳이..
    this.$store.commit('messenger/login')
    this.$store.commit('messenger/on', {command: 'svc_login', func: data => this.persons.filter(p => p.id === data.userid).forEach(p => p.isLoginChatt = 'L')})
    this.$store.commit('messenger/on', {command: 'svc_logout', func: data => this.persons.filter(p => p.id === data.userid).forEach(p => p.isLoginChatt = 'N')})
    this.$store.commit('messenger/on', {
      command: 'svc_join_msg', func: data => {
        this.$store.state.messenger.communicator.join(data.room_id)
        this.loadRoomInfo(data.room_id)
      }
    })

    this.root = (await axios.get('/api/chatt')).data.data
    const extractPersonFromOrganization = (org) => {
      org.personList && org.personList.forEach(p => p.id !== this.me.id && p.isChatt === 'Y' && this.persons.push(p))
      org.organizationMetaChatt && org.organizationMetaChatt.forEach(o => extractPersonFromOrganization(o))
    }
    this.root.forEach(org => extractPersonFromOrganization(org))
    const rooms = (await axios.get('/api/chatt/chatt-room')).data.data;
    rooms.forEach(e => this.loadRoomInfo(e.chattRoom.roomId))
  },
}
</script>
