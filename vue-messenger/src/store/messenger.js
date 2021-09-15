import Messenger from "@/utillities/MessengerCommunicator"
import axios from "@/plugins/axios";

export default {
    namespaced: true,
    state() {
        return {
            communicator: new Messenger(),
        }
    },
    getters: {
        communicator(state) {
            return state.communicator
        }
    },
    mutations: {
        async login(state) {
            state.communicator.disconnect()
            const data = (await axios.get('/api/auth/socket-info')).data.data
            if (!data)
                return
            state.communicator.connect(data.messengerSocketUrl, data.companyId, data.userId, data.userName, data.password)
        },
    }
}
