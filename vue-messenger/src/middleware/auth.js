export default function (to, from, next, store) {
    if (!store.state.user.credential) {
        store.commit('login/show')
        return next('/')
    }

    next()
}
