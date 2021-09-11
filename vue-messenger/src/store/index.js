import {createStore} from 'vuex'
import login from "@/store/login"
import alert from "@/store/alert"
import user from "@/store/user"

export default createStore({
    modules: {login, alert, user}
})
