import {createRouter, createWebHistory} from 'vue-router'
import Main from './pages/Main'
import store from './store/index'
import Login from "./pages/Login";

export const PATH = {
    LOGIN: '/login',
    MAIN: '/'
}

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: PATH.MAIN, component: Main},
        {path: PATH.LOGIN, component: Login},
    ]
})

router.beforeEach(async (to, from, next) => {
    if(to.path === PATH.LOGIN)
        return next()

    console.log(window.communicator, window.communicator?.connected, store.state.communicator, store.state.communicator.connected)

    if (window.communicator && window.communicator.connected) {
        store.state.communicator = window.communicator
        return next()
    }

    if (!store.state.communicator.connected)
        return next(PATH.LOGIN)
    next()
})

export default router
