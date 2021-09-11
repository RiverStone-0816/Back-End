export default {
    namespaced: true,
    state() {
        return {
            showing: false,
        }
    },
    mutations: {
        show(state) {
            state.showing = true
        },
        hide(state) {
            state.showing = false
        },
    }
}
