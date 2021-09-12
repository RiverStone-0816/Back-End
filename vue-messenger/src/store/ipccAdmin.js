import IpccAdminCommunicator from "@/utillities/IpccAdminCommunicator";

export default {
    namespaced: true,
    state() {
        return {
            communicator: new IpccAdminCommunicator(),
        }
    },
    getters: {
        communicator(state) {
            return state.communicator
        }
    },
}
