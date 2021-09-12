import {createStore} from 'vuex'
import alert from "@/store/alert"
import user from "@/store/user"
import ipcc from "@/store/ipcc";
import ipccAdmin from "@/store/ipccAdmin";
import socket from "@/store/socket";

export default createStore({
    modules: {alert, user, ipcc, ipccAdmin, socket}
})
