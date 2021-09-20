import {createStore} from 'vuex'
import alert from "@/store/alert"
import user from "@/store/user"
import ipcc from "@/store/ipcc"
import monitor from "@/store/monitor"
import messenger from "@/store/messenger"
import socket from "@/store/socket"

export default createStore({
    modules: {alert, user, ipcc, monitor, messenger, socket}
})
