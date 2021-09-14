import sessionUtils from "@/utillities/sessionUtils";

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
            sessionUtils.setMe(credential)
        },
        logout(state) {
            state.credential = null
        },
    }
}
