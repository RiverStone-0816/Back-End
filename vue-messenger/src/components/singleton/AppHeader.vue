<template>
  <Disclosure v-slot="{ open }" as="nav" class="bg-gray-800">
    <div class="max-w-7xl mx-auto px-2 md:px-6 lg:px-8">
      <div class="relative flex items-center justify-between h-16">
        <div class="absolute inset-y-0 left-0 flex items-center md:hidden">
          <DisclosureButton
              class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
            <span class="sr-only">Open main menu</span>
            <MenuIcon v-if="!open" aria-hidden="true" class="block h-6 w-6"/>
            <XIcon v-else aria-hidden="true" class="block h-6 w-6"/>
          </DisclosureButton>
        </div>
        <div class="flex-1 flex items-center justify-center md:items-stretch md:justify-start">
          <div class="flex-shrink-0 flex items-center">
            <router-link to="/">
              <img :src="logo" alt="logo" class="block lg:hidden h-8 w-auto"/>
              <img :src="logo" alt="logo" class="hidden lg:block h-8 w-auto"/>
            </router-link>
          </div>
          <div class="hidden md:block md:ml-6">
            <div class="flex space-x-4">
              <button class="text-white px-3 py-2 rounded-md text-sm font-medium" @click="showingAbout = true">About</button>
              <router-link v-for="item in navigation" :key="item.name"
                           :aria-current="item.active && 'page'"
                           :class="[item.active ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white']"
                           :to="item.href"
                           class="px-3 py-2 rounded-md text-sm font-medium">{{ item.name }}
              </router-link>
              <button v-if="!user.credential" :class="'text-gray-300 hover:bg-gray-700 hover:text-white'" class="px-3 py-2 rounded-md text-sm font-medium" type="button"
                      @click="this.$store.commit('login/show')">
                Login
              </button>
            </div>
          </div>
        </div>

        <div v-if="user.credential" class="absolute inset-y-0 right-0 flex items-center pr-2 md:static md:inset-auto md:ml-6 md:pr-0">
          <button
              class="bg-gray-800 p-1 rounded-full text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
              type="button">
            <span class="sr-only">View notifications</span>
            <BellIcon aria-hidden="true" class="h-6 w-6"/>
          </button>

          <Menu as="div" class="ml-3 relative">
            <div>
              <MenuButton
                  class="bg-gray-800 flex text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white">
                <span class="sr-only">Open user menu</span>

                <img v-if="user.credential?.photoURL" :src="user.credential?.photoURL" alt="avatar" class="h-8 w-8 rounded-full"/>
                <UserIcon v-else class="h-8 w-8 rounded-full border text-white"/>
              </MenuButton>
            </div>
            <transition enter-active-class="transition ease-out duration-100"
                        enter-from-class="transform opacity-0 scale-95"
                        enter-to-class="transform opacity-100 scale-100"
                        leave-active-class="transition ease-in duration-75"
                        leave-from-class="transform opacity-100 scale-100"
                        leave-to-class="transform opacity-0 scale-95">
              <MenuItems class="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-10">
                <MenuItem v-slot="{ active }">
                  <a :class="[active ? 'bg-gray-100' : '', 'block px-4 py-2 text-sm text-gray-700']" href="#">{{ user.credential?.displayName || user.credential?.email }}</a>
                </MenuItem>
                <MenuItem v-slot="{ active }">
                  <a :class="[active ? 'bg-gray-100' : '', 'block px-4 py-2 text-sm text-gray-700']" href="#">Settings</a>
                </MenuItem>
                <MenuItem v-slot="{ active }">
                  <button :class="[active ? 'bg-gray-100' : '', 'block px-4 py-2 text-sm text-gray-700']" type="button" @click="logout">Logout</button>
                </MenuItem>
              </MenuItems>
            </transition>
          </Menu>
        </div>
      </div>
    </div>
    <DisclosurePanel class="md:hidden">
      <div class="px-2 pt-2 pb-3 space-y-1">
        <router-link v-for="item in navigation" :key="item.name"
                     :aria-current="item.active ? 'page' : undefined"
                     :class="[item.active ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white', 'block px-3 py-2 rounded-md text-base font-medium']"
                     :to="item.href">{{ item.name }}
        </router-link>
      </div>
    </DisclosurePanel>

    <teleport to="#modals">
      <div v-if="showingAbout" class="inset-0 fixed bg-gray-500 bg-opacity-50" @click="showingAbout = false">
        <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 h-1/2 w-1/2 bg-red-500 bg-opacity-20 text-center">
          I am studying Vue.js
        </div>
      </div>
    </teleport>
  </Disclosure>

</template>
<script>
import {Disclosure, DisclosureButton, DisclosurePanel, Menu, MenuButton, MenuItem, MenuItems} from '@headlessui/vue'
import {BellIcon, MenuIcon, UserIcon, XIcon} from "@zhuowenli/vue-feather-icons"
import logo from '@/assets/logo.png'

export default {
  components: {
    Disclosure,
    DisclosureButton,
    DisclosurePanel,
    Menu,
    MenuButton,
    MenuItem,
    MenuItems,
    BellIcon,
    MenuIcon,
    XIcon,
    UserIcon,
  },
  data() {
    const pages = {
      Calendar: '/calendar',
      Markdown: '/markdown',
      Slider: '/slider',
      Calculator: '/calculator',
      ReusableModal: '/reusable-modal',
      Chat: '/chat',
      Crud: '/crud',
      Tensorflow: '/tensorflow',
    }
    return {
      navigation: Object.keys(pages).map(e => ({name: e, href: pages[e], active: pages[e] === location.pathname})),
      user: this.$store.state.user,
      showingAbout: false,
      logo,
    }
  },
  methods: {
    logout() {
    },
  },
  watch: {
    $route(to) {
      this.navigation.map(item => item.active = item.href === to.path)
    }
  }
}
</script>
