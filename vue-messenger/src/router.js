import {createRouter, createWebHistory} from 'vue-router'
import Calendar from "@/pages/Calendar"
import Home from "@/pages/Home"
import Markdown from "@/pages/Markdown"
import Slider from "@/pages/Slider";
import ReusableModal from "@/pages/ReusableModal";
import Crud from "@/pages/Crud";
// import Login from "@/pages/Login";
import sessionUtils from "@/utillities/sessionUtils"

export const PATH = {
    LOGIN: '/login',
    MAIN: '/'
}

const router = createRouter({
    history: createWebHistory(),
    routes: [
        // {path: PATH.LOGIN, component: Login},
        {path: PATH.MAIN, component: Home},
        {path: '/calendar', component: Calendar},
        {path: '/markdown', component: Markdown},
        {path: '/slider', component: Slider},
        {path: '/reusable-modal', component: ReusableModal},
        {path: '/crud', component: Crud},
    ]
})
export default router

router.beforeEach(async (to, from, next) => {
    const me = await sessionUtils.fetchMe()
    if (!me && to.path !== PATH.LOGIN)
        return next(PATH.LOGIN)
    next()
})
