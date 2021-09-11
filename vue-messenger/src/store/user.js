export default {
    namespaced: true,
    state() {
        return {
            credential: null,
        }
    },
    getters: {
        credential(state) {
            return state.credential
        }
    },
    mutations: {
        login(state, credential) {
            state.credential = credential
        },
        logout(state) {
            state.credential = null
        },
    }
}
