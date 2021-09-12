import IpccCommunicator from "@/utillities/IpccCommunicator";

export default {
    namespaced: true,
    state() {
        return {
            communicator: new IpccCommunicator(),
        }
    },
    getters: {
        communicator(state) {
            return state.communicator
        }
    },
}
