import {createRouter, createWebHistory} from 'vue-router'
import Main from "@/pages/Main"
import sessionUtils from "@/utillities/sessionUtils"

export const PATH = {
    LOGIN: '/login',
    MAIN: '/'
}

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: PATH.MAIN, component: Main},
    ]
})

router.beforeEach(async (to, from, next) => {
    const me = await sessionUtils.fetchMe()
    if (!me && to.path !== PATH.LOGIN)
        return next(PATH.LOGIN)
    next()
})

export default router
