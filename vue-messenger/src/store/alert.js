export default {
    namespaced: true,
    state() {
        return {
            showing: false,
            body: null
        }
    },
    getters: {
        showing(state) {
            return state.showing
        },
        body(state) {
            return state.body
        }
    },
    mutations: {
        show(state, body) {
            state.showing = true
            state.body = body
        },
        close(state) {
            state.showing = false
            self.close()
        }
    }
}
