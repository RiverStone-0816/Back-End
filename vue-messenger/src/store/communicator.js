import Communicator from '../utillities/Communicator'

export default {
    namespaced: false,
    state() {
        return {
            communicator: new Communicator(),
        }
    },
}
