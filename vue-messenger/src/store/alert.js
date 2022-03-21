export default {
    namespaced: true,
    state() {
        return {
            showing: false,
            body: null,
            isClose: false
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
            state.body = body.body
            state.isClose = body.isClose
        },
        close(state) {
            state.showing = false
            if(state.isClose) self.close()
        }
    }
}
